package zone.moddev.mc.ironagefurniture.migration;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.BitArray;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

/** Old red-only benches may have no tile; finish them before chunk construction. */
public final class LegacyPaddedPaletteMigration {
    private LegacyPaddedPaletteMigration() { }
    static int migrate(CompoundNBT level) {
        return migrate(level, ForgeRegistries.BLOCKS::containsKey);
    }
    static int migrate(CompoundNBT level, Predicate<ResourceLocation> available) {
        Map<String, CompoundNBT> tiles = new HashMap<>();
        ListNBT savedTiles = level.getList("TileEntities", 10);
        for (int i = 0; i < savedTiles.size(); i++) {
            CompoundNBT tile = savedTiles.getCompound(i);
            tiles.put(key(tile.getInt("x"), tile.getInt("y"), tile.getInt("z")), tile);
        }
        Set<CompoundNBT> convertedTiles = new HashSet<>();
        int converted = 0;
        ListNBT sections = level.getList("Sections", 10);
        for (int s = 0; s < sections.size(); s++) {
            CompoundNBT section = sections.getCompound(s);
            ListNBT palette = section.getList("Palette", 10);
            if (palette.isEmpty()) continue;
            boolean legacy = false;
            for (int i = 0; i < palette.size(); i++) legacy |= oldId(palette.getCompound(i).getString("Name"));
            if (!legacy) continue;
            int bits = Math.max(4, 32 - Integer.numberOfLeadingZeros(palette.size() - 1));
            long[] data = section.getLongArray("BlockStates");
            BitArray states = palette.size() == 1 && data.length == 0 ? new BitArray(bits, 4096)
                    : LegacyPaletteData.read(bits, data);
            if (states == null) continue;
            for (int cell = 0; cell < 4096; cell++) {
                CompoundNBT old = palette.getCompound(states.get(cell));
                String id = old.getString("Name");
                if (!oldId(id)) continue;
                int x = (level.getInt("xPos") << 4) + (cell & 15);
                int y = ((section.getByte("Y") & 255) << 4) + (cell >> 8);
                int z = (level.getInt("zPos") << 4) + ((cell >> 4) & 15);
                CompoundNBT tile = tiles.get(key(x, y, z));
                String colour = tile == null ? "red" : LegacyPaddedBenchIds.normalizeColour(tile.getString("Color"));
                ResourceLocation target = new ResourceLocation("ironagefurniture",
                        LegacyPaddedBenchIds.modernPaddedPath(new ResourceLocation(id), colour));
                if (!available.test(target)) continue;
                CompoundNBT replacement = old.copy();
                replacement.putString("Name", target.toString());
                int index = palette.indexOf(replacement);
                if (index < 0) { index = palette.size(); palette.add(replacement); }
                int width = Math.max(4, 32 - Integer.numberOfLeadingZeros(palette.size() - 1));
                if (width != bits) {
                    BitArray larger = new BitArray(width, 4096);
                    for (int i = 0; i < 4096; i++) larger.set(i, states.get(i));
                    states = larger; bits = width;
                }
                states.set(cell, index);
                if (tile != null) convertedTiles.add(tile);
                converted++;
            }
            section.put("Palette", palette);
            section.putLongArray("BlockStates", states.getRaw());
        }
        // Their colour is now in the block identity; other tiles remain untouched.
        savedTiles.removeIf(convertedTiles::contains);
        return converted;
    }
    private static boolean oldId(String id) {
        int separator = id.indexOf(':');
        return separator > 0 && LegacyPaddedBenchIds.isLegacyNamespace(id.substring(0, separator))
                && LegacyPaddedBenchIds.isLegacyPaddedPath(id.substring(separator + 1));
    }
    private static String key(int x, int y, int z) { return x + ":" + y + ":" + z; }
}
