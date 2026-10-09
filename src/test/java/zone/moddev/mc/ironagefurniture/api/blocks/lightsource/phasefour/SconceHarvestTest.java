package zone.moddev.mc.ironagefurniture.api.blocks.lightsource.phasefour;

import static org.junit.Assert.*;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Bootstrap;
import net.minecraftforge.common.ToolType;
import org.junit.BeforeClass;
import org.junit.Test;
import zone.moddev.mc.ironagefurniture.api.SconceMetalData;
import zone.moddev.mc.ironagefurniture.api.blocks.base.LightHolderSconce;
import zone.moddev.mc.ironagefurniture.api.enumerations.SconceMetal;

public class SconceHarvestTest {
    @BeforeClass public static void bootstrap() { Bootstrap.register(); }

    @Test public void twinTorchesAndEveryCandleCountAllowDropsWithoutRequiringATool() {
        for (boolean wall : new boolean[]{false, true}) for (boolean lit : new boolean[]{false, true}) {
            verify(new AdditionalSconce("ironagefurniture:harvest_test", wall, lit, true, 2));
            for (int count = 1; count <= 4; count++)
                verify(new AdditionalSconce("ironagefurniture:harvest_test", wall, lit, false, count));
        }
    }

    @Test public void rockSaltFramesAllowDropsWithoutRequiringATool() {
        for (boolean wall : new boolean[]{false, true}) verify(new RockSaltSconce("ironagefurniture:harvest_test", wall));
    }

    private static void verify(LightHolderSconce block) {
        for (SconceMetal metal : SconceMetal.values()) for (Direction facing : Direction.Plane.HORIZONTAL)
            for (boolean wet : new boolean[]{false, true}) {
                BlockState state = block.getDefaultState().with(SconceMetalData.METAL, metal)
                        .with(Candle.DIRECTION, facing).with(Candle.WATERLOGGED, wet);
                // Eligibility is deliberately independent of the player's held tool.
                assertTrue(state.toString(), block.canHarvestBlock(state, null, BlockPos.ZERO, null));
                assertEquals(ToolType.PICKAXE, block.getHarvestTool(state));
                assertEquals(metal.harvestLevel(), block.getHarvestLevel(state));
                assertEquals(metal.hardness(1), block.getBlockHardness(state, null, BlockPos.ZERO), 0.00001F);
            }
    }
}
