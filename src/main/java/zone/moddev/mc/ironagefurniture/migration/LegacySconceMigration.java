package zone.moddev.mc.ironagefurniture.migration;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.Identifier;
import zone.moddev.mc.ironagefurniture.api.SconceMetalData;
import zone.moddev.mc.ironagefurniture.api.blocks.base.LightHolderSconce;
import zone.moddev.mc.ironagefurniture.api.enumerations.SconceMetal;

/** Runs on raw palettes, before the block codec can discard an old metal property. */
public final class LegacySconceMigration {
    public static boolean flattenState(CompoundTag state) {
        Identifier id = Identifier.tryParse(state.getStringOr("Name", ""));
        if (id == null || !id.getNamespace().equals("ironagefurniture") || !id.getPath().startsWith("light_metal_ironage_sconce_")) return false;
        CompoundTag properties = state.getCompoundOrEmpty("Properties");
        String family = SconceMetalData.ironPath(id.getPath());
        if (!zone.moddev.mc.ironagefurniture.init.SconceVariants.hasFamily(family)) return false;
        // A material-specific identity wins over stray legacy custom data.
        SconceMetal metal = SconceMetalData.material(id.getPath());
        if (metal == SconceMetal.IRON && properties.contains("metal")) metal = SconceMetal.byName(properties.getStringOr("metal", "iron"));
        Identifier target = Identifier.fromNamespaceAndPath(id.getNamespace(), SconceMetalData.blockPath(family, metal));
        if (!(BuiltInRegistries.BLOCK.getValue(target) instanceof LightHolderSconce)) return false;
        boolean changed = !id.equals(target) || properties.contains("metal");
        state.putString("Name", target.toString());
        properties.remove("metal");
        if (properties.isEmpty()) state.remove("Properties"); else state.put("Properties", properties);
        return changed;
    }

    static void migrate(CompoundTag level) {
        ListTag tiles = level.getListOrEmpty("TileEntities");
        java.util.Set<String> occupied = new java.util.HashSet<>();
        for (Tag value : tiles) {
            CompoundTag tile = (CompoundTag)value;
            occupied.add(tile.getIntOr("x", 0) + ":" + tile.getIntOr("y", 0) + ":" + tile.getIntOr("z", 0));
        }
        for (Tag value : level.getListOrEmpty("Sections")) {
            CompoundTag section = (CompoundTag)value;
            ListTag palette = section.getListOrEmpty("Palette");
            boolean sconces = false;
            for (Tag entry : palette) {
                CompoundTag state = (CompoundTag)entry;
                flattenState(state);
                sconces |= state.getStringOr("Name", "").startsWith("ironagefurniture:light_metal_ironage_sconce_");
            }
            if (!sconces) continue;
            int bits = Math.max(4, 32 - Integer.numberOfLeadingZeros(palette.size() - 1));
            long[] data = section.getLongArray("BlockStates").orElseGet(() -> new long[0]);
            net.minecraft.util.BitStorage storage = palette.size() == 1 && data.length == 0
                    ? new net.minecraft.util.SimpleBitStorage(bits, 4096) : LegacyPaletteData.read(bits, data);
            if (storage == null) throw new IllegalStateException("Invalid legacy sconce palette encoding");
            for (int cell = 0; cell < 4096; cell++) {
                Identifier id = Identifier.tryParse(palette.getCompoundOrEmpty(storage.get(cell)).getStringOr("Name", ""));
                if (id == null || !(BuiltInRegistries.BLOCK.getValue(id) instanceof LightHolderSconce block)) continue;
                int x = (level.getIntOr("xPos", 0) << 4) + (cell & 15), y = (section.getIntOr("Y", 0) << 4) + (cell >> 8),
                        z = (level.getIntOr("zPos", 0) << 4) + ((cell >> 4) & 15);
                if (!occupied.add(x + ":" + y + ":" + z)) continue;
                CompoundTag tile = new CompoundTag(); tile.putInt("x", x); tile.putInt("y", y); tile.putInt("z", z);
                tile.putString("id", block instanceof zone.moddev.mc.ironagefurniture.api.blocks.lightsource.phasefour.CandleSconceBlock
                        ? "ironagefurniture:candle_sconce" : "ironagefurniture:metal_sconce");
                tiles.add(tile);
            }
        }
        level.put("TileEntities", tiles);
    }
    private LegacySconceMigration() { }
}
