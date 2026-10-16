package zone.moddev.mc.ironagefurniture.fixture.migration;

import java.lang.reflect.Field;
import java.util.BitSet;
import java.util.function.BiConsumer;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.SimpleBitStorage;
import zone.moddev.mc.ironagefurniture.api.enumerations.SconceMetal;
import zone.moddev.mc.ironagefurniture.migration.LegacyWorldDataHook;

/** Numeric chunks cross the real vanilla flattening fix, not a hand-made palette. */
final class NumericSconceMigrationProbe {
    static void run(ServerLevel level, BiConsumer<Boolean, String> check) {
        String[] families = {"floor_empty_iron", "wall_empty_iron", "floor_torch_iron", "wall_torch_iron_unlit",
                "floor_redtorch_iron", "wall_redtorch_iron_unlit", "floor_glow_iron", "wall_glow_iron",
                "floor_lava_iron", "wall_lava_iron", "floor_red_iron_thirteen", "wall_red_iron_fifteen",
                "floor_candle_iron_two", "wall_candle_iron_four_unlit"};
        Class<?> hook = LegacyWorldDataHook.class;
        try {
            var install = hook.getDeclaredMethod("installLegacyBlockStates", CompoundTag.class);
            var restore = hook.getDeclaredMethod("restoreInstalledStates");
            Field active = hook.getDeclaredField("legacyWorldActive");
            install.setAccessible(true); restore.setAccessible(true); active.setAccessible(true);
            boolean wasActive = active.getBoolean(null);
            java.util.Map<Field, BitSet> bitmaps = new java.util.LinkedHashMap<>();
            for (String name : new String[]{"LEGACY_IAF_BLOCK_IDS", "LEGACY_PADDED_BLOCK_IDS", "LEGACY_CFM_BLOCK_IDS"}) {
                Field field = hook.getDeclaredField(name); field.setAccessible(true); bitmaps.put(field, (BitSet)((BitSet)field.get(null)).clone());
            }
            try {
                for (int sourceVersion : new int[]{512, 1343}) {
                    CompoundTag snapshot = new CompoundTag(); ListTag ids = new ListTag();
                    for (int family = 0; family < families.length; family++) {
                        CompoundTag id = new CompoundTag(); id.putString("K", "ironagefurniture:light_metal_ironage_sconce_" + families[family]);
                        id.putInt("V", 3900 + family); ids.add(id);
                    }
                    snapshot.put("ids", ids); install.invoke(null, snapshot); active.setBoolean(null, true);
                    CompoundTag root = new CompoundTag(), chunk = new CompoundTag(), section = new CompoundTag();
                    root.putInt("DataVersion", sourceVersion); root.put("Level", chunk);
                    chunk.putInt("xPos", 0); chunk.putInt("zPos", 0); chunk.putBoolean("TerrainPopulated", true); chunk.putBoolean("LightPopulated", true);
                    chunk.putLong("LastUpdate", 0); chunk.putLong("InhabitedTime", 0); chunk.putIntArray("HeightMap", new int[256]); chunk.putByteArray("Biomes", new byte[256]);
                    chunk.put("Entities", new ListTag()); chunk.put("TileTicks", new ListTag());
                    byte[] blocks = new byte[4096], add = new byte[2048], metadata = new byte[2048];
                    ListTag tiles = new ListTag(); int cell = 0;
                    for (SconceMetal metal : SconceMetal.values()) for (int family = 0; family < families.length; family++) for (int facing = 0; facing < 4; facing++) {
                        int numeric = 3900 + family;
                        blocks[cell] = (byte)numeric; add[cell >> 1] |= (byte)((numeric >> 8) << ((cell & 1) * 4));
                        metadata[cell >> 1] |= (byte)(facing << ((cell & 1) * 4));
                        CompoundTag tile = new CompoundTag(); tile.putString("id", sourceVersion == 512 ? "ironagefurniture:metal_variant" : "ironagefurniture:sconce_metal");
                        tile.putInt("x", cell & 15); tile.putInt("y", 96 + (cell >> 8)); tile.putInt("z", (cell >> 4) & 15);
                        tile.putString("Metal", metal.getName()); tiles.add(tile); cell++;
                    }
                    section.putByte("Y", (byte)6); section.putByteArray("Blocks", blocks); section.putByteArray("Add", add); section.putByteArray("Data", metadata);
                    section.putByteArray("BlockLight", new byte[2048]); section.putByteArray("SkyLight", new byte[2048]);
                    ListTag sections = new ListTag(); sections.add(section); chunk.put("Sections", sections); chunk.put("TileEntities", tiles);
                    try (var storage = new net.minecraft.world.level.chunk.storage.SimpleRegionStorage(
                            new net.minecraft.world.level.chunk.storage.RegionStorageInfo("numeric-sconces", net.minecraft.world.level.Level.OVERWORLD, "chunk"),
                            java.nio.file.Path.of("numeric-sconce-storage-" + sourceVersion), net.minecraft.util.datafix.DataFixers.getDataFixer(), false,
                            net.minecraft.util.datafix.DataFixTypes.CHUNK)) {
                        root = storage.upgradeChunkTag(root, -1, null, net.minecraft.SharedConstants.getCurrentVersion().dataVersion().version());
                        net.minecraft.world.level.chunk.storage.SerializableChunkData.parse(level, level.palettedContainerFactory(), root);
                    }
                    CompoundTag converted = null;
                    for (var value : root.getListOrEmpty("sections")) if (((CompoundTag)value).getIntOr("Y", -100) == 6) converted = ((CompoundTag)value).getCompoundOrEmpty("block_states");
                    check.accept(converted != null, "The actual legacy chunk datafixer retains the sconce section");
                    ListTag palette = converted.getListOrEmpty("palette"); int width = Math.max(4, 32 - Integer.numberOfLeadingZeros(palette.size() - 1));
                    var data = new SimpleBitStorage(width, 4096, converted.getLongArray("data").orElseThrow());
                    cell = 0;
                    for (SconceMetal metal : SconceMetal.values()) for (String family : families) for (int facing = 0; facing < 4; facing++) {
                        String canonical = family.replace("_candle_iron_two", "_candle_iron").replace("_candle_iron_four_unlit", "_candle_iron");
                        String token = metal.isBaseMetal() ? "basemetals_" + metal.getName() : metal.getName();
                        String expected = "ironagefurniture:light_metal_ironage_sconce_" + canonical.replaceFirst("_iron(?=_|$)", "_" + token);
                        CompoundTag actual = palette.getCompoundOrEmpty(data.get(cell++));
                        check.accept(actual.getStringOr("id", "").equals(expected), "Numeric " + sourceVersion + " sconce retains its exact material: " + family + " " + metal);
                        check.accept(actual.getCompoundOrEmpty("properties").getStringOr("facing", "").equals(Direction.from2DDataValue(facing).getName()), "Numeric sconces retain their old facing");
                    }
                    System.out.println("IAF_NUMERIC_SCONCES_PASS version=" + sourceVersion + " blocks=" + cell + " metals=23");
                }
            } finally {
                restore.invoke(null); active.setBoolean(null, wasActive);
                for (var entry : bitmaps.entrySet()) { BitSet bitmap = (BitSet)entry.getKey().get(null); bitmap.clear(); bitmap.or(entry.getValue()); }
            }
        } catch (ReflectiveOperationException | java.io.IOException failure) { throw new AssertionError("Numeric sconce upgrade failed", failure); }
    }
    private NumericSconceMigrationProbe() { }
}
