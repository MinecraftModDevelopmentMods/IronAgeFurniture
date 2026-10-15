package zone.moddev.mc.ironagefurniture.api.blocks.lightsource.phasefour;

import static org.junit.Assert.*;
import java.lang.reflect.Proxy;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.server.Bootstrap;
import net.minecraft.world.level.LevelAccessor;
import org.junit.BeforeClass;
import org.junit.Test;
import zone.moddev.mc.ironagefurniture.api.SconceMetalData;
import zone.moddev.mc.ironagefurniture.api.enumerations.SconceMetal;

public class WaterloggedSupportLossTest {
    @BeforeClass public static void bootstrap() { net.minecraft.SharedConstants.tryDetectVersion(); Bootstrap.bootStrap(); }

    @Test public void twinTorchesAndCandleSconcesRequestNormalDestructionInEveryState() {
        for (boolean wall : new boolean[]{false, true}) for (boolean lit : new boolean[]{false, true})
            for (boolean twin : new boolean[]{true, false}) for (int count = twin ? 2 : 1; count <= (twin ? 2 : 4); count++) {
                Block block = new AdditionalSconce("ironagefurniture:support_test", wall, lit, twin, count);
                for (SconceMetal metal : SconceMetal.values())
                    verify(block.defaultBlockState().setValue(SconceMetalData.METAL, metal), wall);
            }
    }

    @Test public void standaloneCandlesRequestNormalDestructionInEveryState() {
        for (boolean wall : new boolean[]{false, true}) for (boolean lit : new boolean[]{false, true})
            verify(new Candle("ironagefurniture:support_test", wall, lit).defaultBlockState(), wall);
    }

    @Test public void rockSaltSconcesRequestNormalDestructionWithEveryMetal() {
        for (boolean wall : new boolean[]{false, true}) {
            Block block = new RockSaltSconce("ironagefurniture:support_test", wall);
            for (SconceMetal metal : SconceMetal.values())
                verify(block.defaultBlockState().setValue(SconceMetalData.METAL, metal), wall);
        }
    }

    private static void verify(BlockState base, boolean wall) {
        LevelAccessor unsupported = (LevelAccessor)Proxy.newProxyInstance(LevelAccessor.class.getClassLoader(), new Class<?>[]{LevelAccessor.class},
                (proxy, method, args) -> {
                    if (method.getName().equals("getBlockState")) return Blocks.AIR.defaultBlockState();
                    if (method.getName().equals("getFluidState")) return Fluids.EMPTY.defaultFluidState();
                    throw new AssertionError("Support check unexpectedly called " + method.getName());
                });
        BlockPos pos = new BlockPos(0, 80, 0);
        for (Direction facing : Direction.Plane.HORIZONTAL) for (boolean wet : new boolean[]{false, true}) {
            BlockState state = base.setValue(Candle.DIRECTION, facing).setValue(Candle.WATERLOGGED, wet);
            Direction support = wall ? facing.getOpposite() : Direction.DOWN;
            assertSame(state.toString(), Blocks.AIR.defaultBlockState(), state.updateShape(support,
                    Blocks.AIR.defaultBlockState(), unsupported, pos, pos.relative(support)));
        }
    }
}
