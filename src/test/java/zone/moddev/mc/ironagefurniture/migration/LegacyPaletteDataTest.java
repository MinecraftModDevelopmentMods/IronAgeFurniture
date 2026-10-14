package zone.moddev.mc.ironagefurniture.migration;

import net.minecraft.util.BitArray;
import org.junit.Test;
import static org.junit.Assert.*;

public class LegacyPaletteDataTest {
    @Test public void allCellsSurviveBothEncodingsAtEveryPaletteWidth() {
        for (int bits = 4; bits <= 12; bits++) {
            int mask = (1 << bits) - 1;
            BitArray padded = new BitArray(bits, 4096);
            long[] dense = new long[(4096 * bits + 63) / 64];
            for (int cell = 0; cell < 4096; cell++) {
                int value = (cell * 71 + 13) & mask;
                padded.set(cell, value);
                int word = cell * bits / 64, offset = cell * bits % 64;
                dense[word] |= (long)value << offset;
                if (offset + bits > 64) dense[word + 1] |= (long)value >>> (64 - offset);
            }
            assertArrayEquals(padded.getRaw(), LegacyPaletteData.read(bits, dense).getRaw());
            assertArrayEquals(padded.getRaw(), LegacyPaletteData.read(bits, padded.getRaw()).getRaw());
        }
    }
    @Test public void incompleteDataIsNotMisreadAsAnEmptySection() {
        assertNull(LegacyPaletteData.read(5, new long[3]));
    }
}
