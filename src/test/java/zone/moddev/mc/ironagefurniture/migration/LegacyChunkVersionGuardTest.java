package zone.moddev.mc.ironagefurniture.migration;

import static org.junit.Assert.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import org.junit.Test;

public class LegacyChunkVersionGuardTest {
    @Test public void retainedSidecarsDoNotTurnModernChunksIntoNumericInput() {
        CompoundTag root = new CompoundTag();
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
        CompoundTag tile = new CompoundTag(); tile.putInt("y", 277); tile.putString("id", "minecraft:chest");
        ListTag tiles = new ListTag(); tiles.add(tile);
        CompoundTag level = new CompoundTag(); level.put("TileEntities", tiles);
        java.lang.reflect.Method ensure = LegacyWorldDataHook.class.getDeclaredMethod(
                "ensurePaddedBenchTileEntities", CompoundTag.class, boolean.class);
        ensure.setAccessible(true); ensure.invoke(null, level, false);
        assertEquals("minecraft:chest", tiles.getCompound(0).getString("id"));
    }
}
