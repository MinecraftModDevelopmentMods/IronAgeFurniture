package zone.moddev.mc.ironagefurniture.fixture.migration;

import java.util.function.BiConsumer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import zone.moddev.mc.ironagefurniture.api.SconceMetalData;
import zone.moddev.mc.ironagefurniture.api.blocks.base.LightHolderSconce;
import zone.moddev.mc.ironagefurniture.api.enumerations.SconceMetal;
import zone.moddev.mc.ironagefurniture.migration.LegacySconceMigration;
import zone.moddev.mc.ironagefurniture.migration.NativeLegacyBridge;

/** Exercise old shared identities against every registered fixed-material state. */
final class SconceMigrationProbe {
    static void run(ServerLevel level, BiConsumer<Boolean, String> check) {
        int states = 0;
        CompoundTag unknown = new CompoundTag(); unknown.putString("Name", "ironagefurniture:light_metal_ironage_sconce_unknown");
        CompoundTag untouched = unknown.copy();
        check.accept(!LegacySconceMigration.flattenState(unknown) && unknown.equals(untouched), "Unrecognised furniture is never guessed into an iron frame");
        for (Block block : BuiltInRegistries.BLOCK.stream().filter(value -> value instanceof LightHolderSconce).toList()) {
            String target = BuiltInRegistries.BLOCK.getKey(block).toString();
            check.accept(block.getStateDefinition().getProperty("metal") == null, "Fixed-material sconces have no mutable metal property");
            SconceMetal metal = SconceMetalData.get(block.defaultBlockState());
            check.accept(metal == SconceMetalData.material(BuiltInRegistries.BLOCK.getKey(block).getPath()), "Registered material agrees with its persistent identity");
            String token = metal.isBaseMetal() ? "basemetals_" + metal.getName() : metal.getName();
            String legacyId = target.replaceFirst("_" + token + "(?=_|$)", "_iron");
            for (BlockState state : block.getStateDefinition().getPossibleStates()) {
                CompoundTag raw = net.minecraft.nbt.NbtUtils.writeBlockState(state);
                // The native codec uses id/properties; old palettes used Name/Properties.
                if (raw.contains("id")) { raw.putString("Name", raw.getStringOr("id", "")); raw.remove("id"); }
                if (raw.contains("properties")) { raw.put("Properties", raw.getCompoundOrEmpty("properties")); raw.remove("properties"); }
                raw.putString("Name", legacyId);
                CompoundTag properties = raw.getCompoundOrEmpty("Properties"); properties.putString("metal", metal.getName()); raw.put("Properties", properties);
                CompoundTag original = properties.copy(); original.remove("metal");
                check.accept(LegacySconceMigration.flattenState(raw), "Shared-metal state is recognised before decoding");
                check.accept(raw.getStringOr("Name", "").equals(target) && raw.getCompoundOrEmpty("Properties").equals(original), "Every material and light state survives flattening");
                CompoundTag once = raw.copy();
                check.accept(!LegacySconceMigration.flattenState(raw) && raw.equals(once), "Fixed-material palettes are idempotent");
                states++;
            }
        }
        int current = net.minecraft.SharedConstants.getCurrentVersion().dataVersion().version();
        CompoundTag sameVersion = MigrationProbe.chunk("ironagefurniture:light_metal_ironage_sconce_floor_empty_iron", true);
        sameVersion.putInt("DataVersion", current); modernCell(sameVersion).getCompoundOrEmpty("properties").putString("metal", "gold");
        try (var storage = new net.minecraft.world.level.chunk.storage.SimpleRegionStorage(
                new net.minecraft.world.level.chunk.storage.RegionStorageInfo("sconce-contract", net.minecraft.world.level.Level.OVERWORLD, "chunk"),
                java.nio.file.Path.of("sconce-storage-contract"), net.minecraft.util.datafix.DataFixers.getDataFixer(), false, net.minecraft.util.datafix.DataFixTypes.CHUNK)) {
            sameVersion = storage.upgradeChunkTag(sameVersion, -1, null, current);
            net.minecraft.world.level.chunk.storage.SerializableChunkData.parse(level, level.palettedContainerFactory(), sameVersion);
            check.accept(modernCell(sameVersion).getStringOr("id", "").endsWith("_empty_gold"), "The actual storage/decoder hooks migrate same-Minecraft-version worlds before decoding");
        } catch (java.io.IOException failure) { throw new AssertionError(failure); }
        for (SconceMetal metal : SconceMetal.values()) for (boolean modern : new boolean[]{false, true})
                for (boolean wall : new boolean[]{false, true}) for (int format = 0; format < 3; format++) {
            for (String family : new String[]{"empty_iron", "torch_iron_unlit", "redtorch_iron", "glow_iron", "lava_iron", "red_iron_thirteen", "torch_iron_twin", "candle_iron", "rocksalt_iron"}) {
                String iron = "light_metal_ironage_sconce_" + (wall ? "wall" : "floor") + "_" + family;
                String token = metal.isBaseMetal() ? "basemetals_" + metal.getName() : metal.getName();
                String target = "ironagefurniture:" + iron.replaceFirst("_iron(?=_|$)", "_" + token);
                CompoundTag root = MigrationProbe.chunk(format == 2 ? target : "ironagefurniture:" + iron, modern);
                CompoundTag state = modern ? modernCell(root) : MigrationProbe.cell(root);
                CompoundTag properties = state.getCompoundOrEmpty(modern ? "properties" : "Properties");
                properties.putString("facing", "west"); properties.putString("waterlogged", "true");
                if (format != 1) properties.putString("metal", format == 2 && metal != SconceMetal.IRON ? "iron" : metal.getName());
                ListTag tiles = modern ? root.getListOrEmpty("block_entities") : root.getCompoundOrEmpty("Level").getListOrEmpty("TileEntities");
                if (format == 1) {
                    CompoundTag tile = new CompoundTag(); tile.putString("id", new String[]{"ironagefurniture:metal_variant", "ironagefurniture:sconce_metal", "MetalVariant", "SconceMetal"}[metal.ordinal() % 4]); tile.putString("Metal", metal.getName());
                    tile.putInt("x", 0); tile.putInt("y", 96); tile.putInt("z", 0); tiles.add(tile);
                }
                NativeLegacyBridge.prepareChunk(root, modern ? 5006 : 2730); NativeLegacyBridge.finishChunk(root);
                state = modern ? modernCell(root) : MigrationProbe.cell(root);
                check.accept(state.getStringOr(modern ? "id" : "Name", "").equals(target), "Placed sconce metal survives both palette layouts and old material tiles: " + family);
                properties = state.getCompoundOrEmpty(modern ? "properties" : "Properties");
                check.accept(!properties.contains("metal") && properties.getStringOr("facing", "").equals("west") && properties.getStringOr("waterlogged", "").equals("true"),
                        "Placed sconce retains facing/water while removing only the redundant metal property");
                CompoundTag once = root.copy(); NativeLegacyBridge.prepareChunk(root, current); NativeLegacyBridge.finishChunk(root);
                check.accept(root.equals(once), "Chunk migration is byte-structure stable on a second load");
            }
        }
        for (SconceMetal metal : SconceMetal.values()) for (int version : new int[]{512, 1343, 1976, 2230, 2586, 2730, 5006, current}) {
            boolean modern = version >= 5006;
            CompoundTag player = new CompoundTag();
            for (String location : new String[]{"Inventory", "EnderItems"}) {
                ListTag items = new ListTag();
                for (int format = 0; format < 3; format++) {
                    CompoundTag item = new CompoundTag(); item.putString("id", "ironagefurniture:light_metal_ironage_sconce_floor_empty_iron");
                    item.putInt(modern ? "count" : "Count", 7); item.putByte("Slot", (byte)format);
                    CompoundTag custom = new CompoundTag(); custom.putLong("Keep", 123456789012345L);
                    if (format == 0) item.putShort("Damage", (short)metal.ordinal()); else custom.putString("Metal", metal.getName());
                    if (modern) { CompoundTag components = new CompoundTag(); components.put("minecraft:custom_data", custom); item.put("components", components); }
                    else item.put("tag", custom);
                    items.add(item);
                }
                player.put(location, items);
            }
            CompoundTag fixed = (CompoundTag)net.minecraft.util.datafix.DataFixTypes.PLAYER.update(net.minecraft.util.datafix.DataFixers.getDataFixer(),
                    new com.mojang.serialization.Dynamic<>(NbtOps.INSTANCE, player), version, current).getValue();
            for (String location : new String[]{"Inventory", "EnderItems"}) for (Tag entry : fixed.getListOrEmpty(location)) {
                ItemStack decoded = ItemStack.CODEC.parse(level.registryAccess().createSerializationContext(NbtOps.INSTANCE), entry).getOrThrow();
                check.accept(BuiltInRegistries.ITEM.getKey(decoded.getItem()).equals(SconceMetalData.itemId(metal)) && decoded.getCount() == 7,
                        "Released-version player/Ender storage retains metal and stack count, version=" + version);
                check.accept(decoded.get(net.minecraft.core.component.DataComponents.CUSTOM_DATA).copyTag().getLongOr("Keep", 0) == 123456789012345L,
                        "Unrelated typed components survive the actual player datafixer");
            }
            CompoundTag once = fixed.copy(); NativeLegacyBridge.prepareItems(fixed, current); NativeLegacyBridge.finishItems(fixed);
            check.accept(fixed.equals(once), "Released-version item migration is idempotent");
        }
        System.out.println("IAF_SCONCE_FLATTENING_PASS states=" + states + " metals=" + SconceMetal.values().length);
    }
    private static CompoundTag modernCell(CompoundTag root) {
        CompoundTag states = root.getListOrEmpty("sections").getCompoundOrEmpty(0).getCompoundOrEmpty("block_states");
        ListTag palette = states.getListOrEmpty("palette");
        int width = Math.max(4, 32 - Integer.numberOfLeadingZeros(palette.size() - 1));
        return palette.getCompoundOrEmpty(new net.minecraft.util.SimpleBitStorage(width, 4096, states.getLongArray("data").orElseThrow()).get(0));
    }
    private SconceMigrationProbe() { }
}
