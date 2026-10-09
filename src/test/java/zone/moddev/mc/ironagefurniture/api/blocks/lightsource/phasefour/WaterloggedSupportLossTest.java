package zone.moddev.mc.ironagefurniture.api.blocks.lightsource.phasefour;

import static org.junit.Assert.*;
import java.lang.reflect.Proxy;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Bootstrap;
import net.minecraft.world.IWorld;
import org.junit.BeforeClass;
import org.junit.Test;
import zone.moddev.mc.ironagefurniture.api.SconceMetalData;
import zone.moddev.mc.ironagefurniture.api.enumerations.SconceMetal;

public class WaterloggedSupportLossTest {
    @BeforeClass public static void bootstrap() { Bootstrap.register(); }

    @Test public void twinTorchesAndCandleSconcesRequestNormalDestructionInEveryState() {
        for (boolean wall : new boolean[]{false, true}) for (boolean lit : new boolean[]{false, true})
            for (boolean twin : new boolean[]{true, false}) for (int count = twin ? 2 : 1; count <= (twin ? 2 : 4); count++) {
                Block block = new AdditionalSconce("ironagefurniture:support_test", wall, lit, twin, count);
                for (SconceMetal metal : SconceMetal.values())
                    verify(block.getDefaultState().with(SconceMetalData.METAL, metal), wall);
            }
    }

    @Test public void standaloneCandlesRequestNormalDestructionInEveryState() {
        for (boolean wall : new boolean[]{false, true}) for (boolean lit : new boolean[]{false, true})
            verify(new Candle("ironagefurniture:support_test", wall, lit).getDefaultState(), wall);
    }

    @Test public void rockSaltSconcesRequestNormalDestructionWithEveryMetal() {
        for (boolean wall : new boolean[]{false, true}) {
            Block block = new RockSaltSconce("ironagefurniture:support_test", wall);
            for (SconceMetal metal : SconceMetal.values())
                verify(block.getDefaultState().with(SconceMetalData.METAL, metal), wall);
        }
    }

    private static void verify(BlockState base, boolean wall) {
        IWorld unsupported = (IWorld)Proxy.newProxyInstance(IWorld.class.getClassLoader(), new Class<?>[]{IWorld.class},
                (proxy, method, args) -> {
                    if (method.getName().equals("getBlockState")) return Blocks.AIR.getDefaultState();
                    if (method.getName().equals("getFluidState")) return Fluids.EMPTY.getDefaultState();
                    throw new AssertionError("Support check unexpectedly called " + method.getName());
                });
        BlockPos pos = new BlockPos(0, 80, 0);
        for (Direction facing : Direction.Plane.HORIZONTAL) for (boolean wet : new boolean[]{false, true}) {
            BlockState state = base.with(Candle.DIRECTION, facing).with(Candle.WATERLOGGED, wet);
            Direction support = wall ? facing.getOpposite() : Direction.DOWN;
            assertSame(state.toString(), Blocks.AIR.getDefaultState(), state.updatePostPlacement(support,
                    Blocks.AIR.getDefaultState(), unsupported, pos, pos.offset(support)));
        }
    }
}
