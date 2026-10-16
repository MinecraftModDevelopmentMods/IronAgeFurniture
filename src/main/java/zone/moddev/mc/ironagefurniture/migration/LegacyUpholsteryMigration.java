package zone.moddev.mc.ironagefurniture.migration;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.BitStorage;
import zone.moddev.mc.ironagefurniture.api.enumerations.UpholsteryColour;

/** Moves pre-flattening upholstery tiles into the coloured block-state palette. */
public final class LegacyUpholsteryMigration {
    private static final String TILE_MARKER = "IronAgeFurnitureLegacyUpholstery";
    private static final String SHIELD_MARKER = "IronAgeFurnitureLegacyShield";
    private static final String METAL_MARKER = "IronAgeFurnitureLegacySconceMetal";
    private static final String METAL_TILE_ID = "IronAgeFurnitureLegacyMetalTileId";
    private LegacyUpholsteryMigration() { }

    public static boolean isUpholsteredPath(String path) {
        return path.startsWith("bed_wood_") || path.startsWith("bed_canopy_")
                || path.startsWith("chair_wood_ironage_wingback_") || path.startsWith("chair_wood_ironage_throne_");
    }
    static void prepareTiles(CompoundTag level, boolean oldIds) {
        for (int index = 0; index < level.getListOrEmpty("TileEntities").size(); index++) {
            CompoundTag tile = level.getListOrEmpty("TileEntities").getCompoundOrEmpty(index);
            String id = tile.getStringOr("id", "");
            boolean upholstery = id.equals("ironagefurniture:upholstery_colour") || id.equals("iafbygaddon:upholstery_colour") || id.equals("UpholsteryColour");
            boolean shield = id.equals("ironagefurniture:shield_chair") || id.equals("iafbygaddon:shield_chair") || id.equals("ShieldChair");
            boolean metal = isLegacyMetalTile(tile);
            if (!upholstery && !shield && !metal) continue;
            if (upholstery) NativeLegacyBridge.preserveTileColour(tile);
            if (metal) tile.putString(METAL_TILE_ID, id);
            // Vanilla's schemas retain a sign's unknown fields. Keep the real
            // identity private until the chunk has crossed those schemas.
            tile.putString("id", oldIds ? "Sign" : "minecraft:sign");
            tile.putBoolean(upholstery ? TILE_MARKER : metal ? METAL_MARKER : SHIELD_MARKER, true);
        }
    }
    private static boolean isLegacyMetalTile(CompoundTag tile) {
        String id = tile.getStringOr("id", "");
        return id.equals("ironagefurniture:metal_variant") || id.equals("MetalVariant")
                || id.equals("ironagefurniture:sconce_metal") || id.equals("SconceMetal")
                || id.equals("ironagefurniture:metal_sconce") && tile.contains("Metal");
    }
    static void prepareMetalTiles(CompoundTag level) {
        // The released 1.10 tile was metal_variant; 1.12 renamed it sconce_metal.
        // Also cover same-version chunks that reach the final decoder directly.
        for (var value : level.getListOrEmpty("TileEntities")) {
            CompoundTag tile = (CompoundTag)value;
            if (!isLegacyMetalTile(tile)) continue;
            tile.putString(METAL_TILE_ID, tile.getStringOr("id", ""));
            tile.putString("id", "minecraft:sign"); tile.putBoolean(METAL_MARKER, true);
        }
    }
    static int finishTiles(CompoundTag level) {
        int converted = 0;
        ListTag tiles = level.getListOrEmpty("TileEntities");
        for (int index = tiles.size() - 1; index >= 0; index--) {
            CompoundTag tile = tiles.getCompoundOrEmpty(index);
            if (tile.getBooleanOr(SHIELD_MARKER, false)) {
                tile.putString("id", "ironagefurniture:shield_chair");
                tile.remove(SHIELD_MARKER);
            } else if (tile.getBooleanOr(TILE_MARKER, false) || tile.getBooleanOr(METAL_MARKER, false)) {
                if (applyColour(level, tile)) {
                    if (tile.getBooleanOr("IronAgeFurnitureCandleStorage", false)) {
                        tile.remove(TILE_MARKER); tile.remove(METAL_MARKER); tile.remove(METAL_TILE_ID); tile.remove("Color"); tile.remove("Metal");
                    } else tiles.remove(index);
                    converted++;
                } else {
                    // Do not silently discard an unconverted tile's colour.
                    tile.putString("id", tile.getBooleanOr(METAL_MARKER, false) ? tile.getStringOr(METAL_TILE_ID, "ironagefurniture:sconce_metal") : "ironagefurniture:upholstery_colour");
                    tile.remove(METAL_TILE_ID);
                    tile.remove(TILE_MARKER);
                    tile.remove(METAL_MARKER);
                }
            }
        }
        return converted;
    }
    private static boolean applyColour(CompoundTag level, CompoundTag tile) {
        int x = tile.getIntOr("x", 0), y = tile.getIntOr("y", 0), z = tile.getIntOr("z", 0);
        if (x >> 4 != level.getIntOr("xPos", 0) || z >> 4 != level.getIntOr("zPos", 0)) return false;
        ListTag sections = level.getListOrEmpty("Sections");
        for (int index = 0; index < sections.size(); index++) {
            CompoundTag section = sections.getCompoundOrEmpty(index);
            if (section.getByteOr("Y", (byte)0) != y >> 4) continue;
            ListTag palette = section.getListOrEmpty("Palette");
            if (palette.isEmpty()) return false;
            int bits = Math.max(4, 32 - Integer.numberOfLeadingZeros(palette.size() - 1));
            long[] oldData = section.getLongArray("BlockStates").orElseGet(() -> new long[0]);
            BitStorage states = palette.size() == 1 && oldData.length == 0 ? new net.minecraft.util.SimpleBitStorage(bits, 4096) : LegacyPaletteData.read(bits, oldData);
            if (states == null) return false;
            int cell = (y & 15) << 8 | (z & 15) << 4 | x & 15;
            int oldIndex = states.get(cell);
            if (oldIndex >= palette.size()) return false;
            CompoundTag state = palette.getCompoundOrEmpty(oldIndex).copy();
            String name = state.getStringOr("Name", "");
            boolean metal = tile.getBooleanOr(METAL_MARKER, false);
            if (!name.startsWith("ironagefurniture:") || (metal
                    ? !name.startsWith("ironagefurniture:light_metal_ironage_sconce_")
                    : !isUpholsteredPath(name.substring("ironagefurniture:".length())))) return false;
            CompoundTag properties = state.getCompoundOrEmpty("Properties");
            if (metal) properties.putString("metal", zone.moddev.mc.ironagefurniture.api.enumerations.SconceMetal.byName(tile.getStringOr("Metal", "")).getName());
            else properties.putString("colour", UpholsteryColour.byName(tile.getStringOr("Color", "")).getName());
            state.put("Properties", properties);
            int replacement = palette.indexOf(state);
            if (replacement < 0) { replacement = palette.size(); palette.add(state); }
            int newBits = Math.max(4, 32 - Integer.numberOfLeadingZeros(palette.size() - 1));
            if (newBits != bits) {
                BitStorage expanded = new net.minecraft.util.SimpleBitStorage(newBits, 4096);
                for (int position = 0; position < 4096; position++) expanded.set(position, states.get(position));
                states = expanded;
            }
            states.set(cell, replacement);
            section.putLongArray("BlockStates", states.getRaw());
            section.put("Palette", palette);
            return true;
        }
        return false;
    }
    /** Damage was an item colour before flattening, not durability on these items. */
    static boolean migrateItem(CompoundTag stack) {
        String id = stack.getStringOr("id", "");
        if (!id.startsWith("ironagefurniture:") && !id.startsWith("iafbygaddon:")) return false;
        if (!isUpholsteredPath(id.substring(id.indexOf(':') + 1))) return false;
        CompoundTag tag = stack.getCompoundOrEmpty("tag");
        UpholsteryColour fixed = UpholsteryColour.RED;
        for (UpholsteryColour candidate : UpholsteryColour.values()) if (candidate != UpholsteryColour.RED
                && id.endsWith("_" + candidate.getName())
                && (fixed == UpholsteryColour.RED || candidate.getName().length() > fixed.getName().length())) fixed = candidate;
        UpholsteryColour colour = fixed != UpholsteryColour.RED ? fixed : tag.contains("Color") ? UpholsteryColour.byName(tag.getStringOr("Color", ""))
                : UpholsteryColour.byItemMetadata(stack.contains("Damage") ? stack.getIntOr("Damage", 0) : tag.getIntOr("Damage", 0));
        String base = fixed == UpholsteryColour.RED ? id : id.substring(0, id.length() - fixed.getName().length() - 1);
        String path = LegacyPaddedBenchIds.currentId(net.minecraft.resources.Identifier.parse(base)).toString();
        // The right canopy half was never an inventory item.
        path = path.replace("bed_canopy_foot_right_lower_", "bed_canopy_foot_left_lower_");
        path = zone.moddev.mc.ironagefurniture.api.UpholsteryItemData.itemId(net.minecraft.resources.Identifier.parse(path), colour).toString();
        boolean changed = !path.equals(id) || tag.contains("Color") || stack.contains("Damage") || tag.contains("Damage");
        if (!changed) return false;
        stack.putString("id", path);
        tag.remove("Color");
        tag.remove("Damage");
        if (tag.isEmpty()) stack.remove("tag"); else stack.put("tag", tag);
        stack.remove("Damage");
        return true;
    }
}
