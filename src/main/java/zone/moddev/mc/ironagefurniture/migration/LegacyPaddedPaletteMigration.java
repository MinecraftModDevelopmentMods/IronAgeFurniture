package zone.moddev.mc.ironagefurniture.migration;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.BitStorage;
import net.minecraft.resources.ResourceLocation;

/** Old red-only benches may have no tile; finish them before chunk construction. */
public final class LegacyPaddedPaletteMigration {
    private LegacyPaddedPaletteMigration() { }
    static int migrate(CompoundTag level) {
        return migrate(level, net.minecraft.core.registries.BuiltInRegistries.BLOCK::containsKey);
    }
    static int migrate(CompoundTag level, Predicate<ResourceLocation> available) {
        Map<String, CompoundTag> tiles = new HashMap<>();
        ListTag savedTiles = LegacyNbt.list(level, "TileEntities");
        for (int i = 0; i < savedTiles.size(); i++) {
            CompoundTag tile = LegacyNbt.compound(savedTiles, i);
            tiles.put(key(LegacyNbt.integer(tile, "x", 0), LegacyNbt.integer(tile, "y", 0), LegacyNbt.integer(tile, "z", 0)), tile);
        }
        Set<CompoundTag> convertedTiles = new HashSet<>();
        int converted = 0;
        ListTag sections = LegacyNbt.list(level, "Sections");
        for (int s = 0; s < sections.size(); s++) {
            CompoundTag section = LegacyNbt.compound(sections, s);
            ListTag palette = LegacyNbt.list(section, "Palette");
            if (palette.isEmpty()) continue;
            boolean legacy = false;
            for (int i = 0; i < palette.size(); i++) legacy |= oldId(LegacyNbt.string(LegacyNbt.compound(palette, i), "Name", ""));
            if (!legacy) continue;
            int bits = Math.max(4, 32 - Integer.numberOfLeadingZeros(palette.size() - 1));
            long[] data = section.getLongArray("BlockStates");
            BitStorage states = palette.size() == 1 && data.length == 0 ? new net.minecraft.util.SimpleBitStorage(bits, 4096)
                    : LegacyPaletteData.read(bits, data);
            if (states == null) continue;
            for (int cell = 0; cell < 4096; cell++) {
                CompoundTag old = LegacyNbt.compound(palette, states.get(cell));
                String id = LegacyNbt.string(old, "Name", "");
                if (!oldId(id)) continue;
                int x = (LegacyNbt.integer(level, "xPos", 0) << 4) + (cell & 15);
                int y = (LegacyNbt.integer(section, "Y", 0) << 4) + (cell >> 8);
                int z = (LegacyNbt.integer(level, "zPos", 0) << 4) + ((cell >> 4) & 15);
                CompoundTag tile = tiles.get(key(x, y, z));
                String colour = tile == null ? "red" : LegacyPaddedBenchIds.normalizeColour(LegacyNbt.string(tile, "Color", ""));
                ResourceLocation target = new ResourceLocation("ironagefurniture",
                        LegacyPaddedBenchIds.modernPaddedPath(new ResourceLocation(id), colour));
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
