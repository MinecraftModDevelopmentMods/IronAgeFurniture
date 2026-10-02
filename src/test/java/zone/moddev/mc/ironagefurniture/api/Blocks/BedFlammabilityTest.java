package zone.moddev.mc.ironagefurniture.api.Blocks;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Bootstrap;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import org.junit.BeforeClass;
import org.junit.Test;

public class BedFlammabilityTest {
    @BeforeClass public static void bootstrap() { Bootstrap.register(); }

    @Test public void everyWoodenBedPartBurnsLikeAChair() {
        for (boolean doubled : new boolean[] { false, true }) {
            assertBurnsLikeChair(new MultiBlockWoodBed(Material.WOOD, "test_wood_bed", 10, 3, doubled));
        }
    }

    @Test public void everyCanopyBedPartBurnsLikeAChair() {
        for (int side : new int[] { MultiBlockBed.SINGLE_SIDE, MultiBlockBed.LEFT_SIDE, MultiBlockBed.RIGHT_SIDE }) {
            assertBurnsLikeChair(new MultiBlockBed(Material.WOOD, "test_canopy_bed", 10, 3, side));
        }
    }

    private static void assertBurnsLikeChair(Block bed) {
        Chair chair = new Chair(Material.WOOD, "test_chair", 10, 1);
        BlockPos pos = new BlockPos(0, 64, 0);
        for (IBlockState state : bed.getBlockState().getValidStates()) {
            for (EnumFacing face : EnumFacing.values()) {
                assertEquals(state + " / " + face, chair.getFlammability(null, pos, face),
                        bed.getFlammability(null, pos, face));
                assertEquals(state + " / " + face, chair.getFireSpreadSpeed(null, pos, face),
                        bed.getFireSpreadSpeed(null, pos, face));
                assertTrue(bed.isFlammable(null, pos, face));
            }
        }
    }
}
