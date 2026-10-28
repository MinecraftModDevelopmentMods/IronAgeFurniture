package zone.moddev.mc.ironagefurniture.migration;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.BitStorage;
import net.minecraft.resources.Identifier;

/** Old red-only benches may have no tile; finish them before chunk construction. */
public final class LegacyPaddedPaletteMigration {
    private LegacyPaddedPaletteMigration() { }
    static int migrate(CompoundTag level) {
        return migrate(level, net.minecraft.core.registries.BuiltInRegistries.BLOCK::containsKey);
    }
    static int migrate(CompoundTag level, Predicate<Identifier> available) {
        Map<String, CompoundTag> tiles = new HashMap<>();
        ListTag savedTiles = level.getListOrEmpty("TileEntities");
        for (int i = 0; i < savedTiles.size(); i++) {
            CompoundTag tile = savedTiles.getCompoundOrEmpty(i);
            tiles.put(key(tile.getIntOr("x", 0), tile.getIntOr("y", 0), tile.getIntOr("z", 0)), tile);
        }
        Set<CompoundTag> convertedTiles = new HashSet<>();
        int converted = 0;
        ListTag sections = level.getListOrEmpty("Sections");
        for (int s = 0; s < sections.size(); s++) {
            CompoundTag section = sections.getCompoundOrEmpty(s);
            ListTag palette = section.getListOrEmpty("Palette");
            if (palette.isEmpty()) continue;
            boolean legacy = false;
            for (int i = 0; i < palette.size(); i++) legacy |= oldId(palette.getCompoundOrEmpty(i).getStringOr("Name", ""));
            if (!legacy) continue;
            int bits = Math.max(4, 32 - Integer.numberOfLeadingZeros(palette.size() - 1));
            long[] data = section.getLongArray("BlockStates").orElseGet(() -> new long[0]);
            BitStorage states = palette.size() == 1 && data.length == 0 ? new net.minecraft.util.SimpleBitStorage(bits, 4096)
                    : LegacyPaletteData.read(bits, data);
            if (states == null) continue;
            for (int cell = 0; cell < 4096; cell++) {
                CompoundTag old = palette.getCompoundOrEmpty(states.get(cell));
                String id = old.getStringOr("Name", "");
                if (!oldId(id)) continue;
                int x = (level.getIntOr("xPos", 0) << 4) + (cell & 15);
                int y = (section.getIntOr("Y", 0) << 4) + (cell >> 8);
                int z = (level.getIntOr("zPos", 0) << 4) + ((cell >> 4) & 15);
                CompoundTag tile = tiles.get(key(x, y, z));
                String colour = tile == null ? "red" : LegacyPaddedBenchIds.normalizeColour(tile.getStringOr("Color", ""));
                Identifier target = Identifier.fromNamespaceAndPath("ironagefurniture",
                        LegacyPaddedBenchIds.modernPaddedPath(Identifier.parse(id), colour));
                if (!available.test(target)) continue;
                CompoundTag replacement = old.copy();
                replacement.putString("Name", target.toString());
                int index = palette.indexOf(replacement);
                if (index < 0) { index = palette.size(); palette.add(replacement); }
                int width = Math.max(4, 32 - Integer.numberOfLeadingZeros(palette.size() - 1));
                if (width != bits) {
                    BitStorage larger = new net.minecraft.util.SimpleBitStorage(width, 4096);
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
