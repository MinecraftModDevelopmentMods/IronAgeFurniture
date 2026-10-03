package zone.moddev.mc.ironagefurniture.migration;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.BitArray;
import zone.moddev.mc.ironagefurniture.api.enumerations.UpholsteryColour;

/** Moves pre-flattening upholstery tiles into the coloured block-state palette. */
public final class LegacyUpholsteryMigration {
    private static final String TILE_MARKER = "IronAgeFurnitureLegacyUpholstery";
    private static final String SHIELD_MARKER = "IronAgeFurnitureLegacyShield";
    private LegacyUpholsteryMigration() { }

    public static boolean isUpholsteredPath(String path) {
        return path.startsWith("bed_wood_") || path.startsWith("bed_canopy_")
                || path.startsWith("chair_wood_ironage_wingback_") || path.startsWith("chair_wood_ironage_throne_");
    }
    static void prepareTiles(CompoundNBT level, boolean oldIds) {
        for (int index = 0; index < level.getList("TileEntities", 10).size(); index++) {
            CompoundNBT tile = level.getList("TileEntities", 10).getCompound(index);
            String id = tile.getString("id");
            boolean upholstery = id.equals("ironagefurniture:upholstery_colour") || id.equals("UpholsteryColour");
            boolean shield = id.equals("ironagefurniture:shield_chair") || id.equals("ShieldChair");
            if (!upholstery && !shield) continue;
            // Vanilla's schemas retain a sign's unknown fields. Keep the real
            // identity private until the chunk has crossed those schemas.
            tile.putString("id", oldIds ? "Sign" : "minecraft:sign");
            tile.putBoolean(upholstery ? TILE_MARKER : SHIELD_MARKER, true);
        }
    }
    static int finishTiles(CompoundNBT level) {
        int converted = 0;
        ListNBT tiles = level.getList("TileEntities", 10);
        for (int index = tiles.size() - 1; index >= 0; index--) {
            CompoundNBT tile = tiles.getCompound(index);
            if (tile.getBoolean(SHIELD_MARKER)) {
                tile.putString("id", "ironagefurniture:shield_chair");
                tile.remove(SHIELD_MARKER);
            } else if (tile.getBoolean(TILE_MARKER)) {
                if (applyColour(level, tile)) {
                    tiles.remove(index);
                    converted++;
                } else {
                    // Do not silently discard an unconverted tile's colour.
                    tile.putString("id", "ironagefurniture:upholstery_colour");
                    tile.remove(TILE_MARKER);
                }
            }
        }
        return converted;
    }
    private static boolean applyColour(CompoundNBT level, CompoundNBT tile) {
        int x = tile.getInt("x"), y = tile.getInt("y"), z = tile.getInt("z");
        if (y < 0 || y > 255 || x >> 4 != level.getInt("xPos") || z >> 4 != level.getInt("zPos")) return false;
        ListNBT sections = level.getList("Sections", 10);
        for (int index = 0; index < sections.size(); index++) {
            CompoundNBT section = sections.getCompound(index);
            if ((section.getByte("Y") & 255) != y >> 4) continue;
            ListNBT palette = section.getList("Palette", 10);
            if (palette.isEmpty()) return false;
            int bits = Math.max(4, 32 - Integer.numberOfLeadingZeros(palette.size() - 1));
            long[] oldData = section.getLongArray("BlockStates");
            if (oldData.length != (4096 * bits + 63) / 64) return false;
            BitArray states = new BitArray(bits, 4096, oldData);
            int cell = (y & 15) << 8 | (z & 15) << 4 | x & 15;
            int oldIndex = states.getAt(cell);
            if (oldIndex >= palette.size()) return false;
            CompoundNBT state = palette.getCompound(oldIndex).copy();
            String name = state.getString("Name");
            if (!name.startsWith("ironagefurniture:") || !isUpholsteredPath(name.substring("ironagefurniture:".length()))) return false;
            CompoundNBT properties = state.getCompound("Properties");
            properties.putString("colour", UpholsteryColour.byName(tile.getString("Color")).getName());
            state.put("Properties", properties);
            int replacement = palette.indexOf(state);
            if (replacement < 0) { replacement = palette.size(); palette.add(state); }
            int newBits = Math.max(4, 32 - Integer.numberOfLeadingZeros(palette.size() - 1));
            if (newBits != bits) {
                BitArray expanded = new BitArray(newBits, 4096);
                for (int position = 0; position < 4096; position++) expanded.setAt(position, states.getAt(position));
                states = expanded;
            }
            states.setAt(cell, replacement);
            section.putLongArray("BlockStates", states.getBackingLongArray());
            section.put("Palette", palette);
            return true;
        }
        return false;
    }
    /** Damage was an item colour before flattening, not durability on these items. */
    static boolean migrateItem(CompoundNBT stack) {
        String id = stack.getString("id");
        if (!id.startsWith("ironagefurniture:") || !isUpholsteredPath(id.substring("ironagefurniture:".length()))) return false;
        CompoundNBT tag = stack.getCompound("tag");
        UpholsteryColour colour = tag.contains("Color", 8) ? UpholsteryColour.byName(tag.getString("Color"))
                : stack.contains("Damage", 99) ? UpholsteryColour.byItemMetadata(stack.getInt("Damage")) : UpholsteryColour.RED;
        String path = LegacyPaddedBenchIds.currentId(new net.minecraft.util.ResourceLocation(id)).toString();
        // The right canopy half was never an inventory item.
        path = path.replace("bed_canopy_foot_right_lower_", "bed_canopy_foot_left_lower_");
        boolean changed = !path.equals(id) || !colour.getName().equals(tag.getString("Color")) || stack.contains("Damage");
        if (!changed) return false;
        stack.putString("id", path);
        tag.putString("Color", colour.getName());
        tag.remove("Damage");
        stack.put("tag", tag);
        stack.remove("Damage");
        return true;
    }
}
