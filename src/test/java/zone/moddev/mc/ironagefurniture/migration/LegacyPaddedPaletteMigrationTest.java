package zone.moddev.mc.ironagefurniture.migration;

import static org.junit.Assert.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.BitArray;
import org.junit.Test;

public class LegacyPaddedPaletteMigrationTest {
    @Test public void tilesAndTilelessBenchesKeepColourAndStateThroughPaletteGrowth() {
        CompoundNBT level = new CompoundNBT();
        level.putInt("xPos", -13); level.putInt("zPos", 17);
        ListNBT palette = new ListNBT();
        for (int i = 0; i < 16; i++) {
            CompoundNBT state = new CompoundNBT();
            state.putString("Name", i == 1 ? "ironagefurniture:chair_wood_ironage_bench_back_padded_single_big_oak"
                    : "minecraft:test_" + i);
            CompoundNBT properties = new CompoundNBT();
            properties.putString("facing", "east"); properties.putString("type", "left");
            properties.putString("waterlogged", "false"); state.put("Properties", properties); palette.add(state);
        }
        BitArray cells = new BitArray(4, 4096);
        ListNBT tiles = new ListNBT();
        String[] colours = {"red","orange","magenta","light_blue","yellow","lime","pink","gray",
                "light_gray","cyan","purple","blue","brown","green","white","black"};
        for (int i = 0; i < 18; i++) {
            cells.set(i, 1);
            if (i == 16) continue;
            CompoundNBT tile = new CompoundNBT();
            tile.putInt("x", -208 + (i & 15)); tile.putInt("y", 80); tile.putInt("z", 272 + (i >> 4));
            tile.putString("id", "ironagefurniture:padded_bench_colour");
            tile.putString("Color", i == 17 ? "invalid" : colours[i]); tiles.add(tile);
        }
        CompoundNBT section = new CompoundNBT(); section.putByte("Y", (byte)5);
        section.put("Palette", palette); section.putLongArray("BlockStates", cells.getRaw());
        ListNBT sections = new ListNBT(); sections.add(section);
        level.put("Sections", sections); level.put("TileEntities", tiles);
        assertEquals(18, LegacyPaddedPaletteMigration.migrate(level, id -> true));
        assertTrue(tiles.isEmpty());
        BitArray actual = LegacyPaletteData.read(5, section.getLongArray("BlockStates"));
        for (int i = 0; i < 18; i++) {
            CompoundNBT state = palette.getCompound(actual.get(i));
            assertEquals("ironagefurniture:chair_wood_ironage_bench_back_padded_" + (i < 16 ? colours[i] : "red")
                    + "_single_dark_oak", state.getString("Name"));
            assertEquals(palette.getCompound(1).getCompound("Properties"), state.getCompound("Properties"));
        }
        for (int i = 18; i < 4096; i++) assertEquals(0, actual.get(i));
        CompoundNBT saved = level.copy();
        assertEquals(0, LegacyPaddedPaletteMigration.migrate(level, id -> true));
        assertEquals(saved, level);
    }
}
