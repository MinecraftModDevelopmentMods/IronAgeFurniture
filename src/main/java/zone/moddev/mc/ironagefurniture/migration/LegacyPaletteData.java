package zone.moddev.mc.ironagefurniture.migration;

import net.minecraft.util.BitArray;

/** Reads both palette encodings; saved 1.16 chunks always use padded longs. */
final class LegacyPaletteData {
    private LegacyPaletteData() { }

    static BitArray read(int bits, long[] data) {
        int perLong = 64 / bits;
        if (data.length == (4096 + perLong - 1) / perLong) return new BitArray(bits, 4096, data);
        if (data.length != (4096 * bits + 63) / 64) return null;
        BitArray result = new BitArray(bits, 4096);
        long mask = (1L << bits) - 1;
        for (int cell = 0; cell < 4096; cell++) {
            int word = cell * bits / 64, offset = cell * bits % 64;
            long value = data[word] >>> offset;
            if (offset + bits > 64) value |= data[word + 1] << (64 - offset);
            result.set(cell, (int)(value & mask));
        }
        return result;
    }
}
