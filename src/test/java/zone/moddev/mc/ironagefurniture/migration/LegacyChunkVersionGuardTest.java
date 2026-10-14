package zone.moddev.mc.ironagefurniture.migration;

import static org.junit.Assert.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import org.junit.Test;

public class LegacyChunkVersionGuardTest {
    @Test public void retainedSidecarsDoNotTurnModernChunksIntoNumericInput() {
        CompoundNBT root = new CompoundNBT();
        for (int version : new int[]{0, 512, 1343}) {
            root.putInt("DataVersion", version);
            assertTrue(LegacyWorldDataHook.isPreFlatteningChunk(root));
        }
        for (int version : new int[]{1451, 1976, 2230, 2586}) {
            root.putInt("DataVersion", version);
            assertFalse(LegacyWorldDataHook.isPreFlatteningChunk(root));
        }
    }
    @Test public void tilesOutsideNumericSectionsDoNotIndexNegativeBlockIds() throws Exception {
        CompoundNBT tile = new CompoundNBT(); tile.putInt("y", 277); tile.putString("id", "minecraft:chest");
        ListNBT tiles = new ListNBT(); tiles.add(tile);
        CompoundNBT level = new CompoundNBT(); level.put("TileEntities", tiles);
        java.lang.reflect.Method ensure = LegacyWorldDataHook.class.getDeclaredMethod(
                "ensurePaddedBenchTileEntities", CompoundNBT.class, boolean.class);
        ensure.setAccessible(true); ensure.invoke(null, level, false);
        assertEquals("minecraft:chest", tiles.getCompound(0).getString("id"));
    }
}
