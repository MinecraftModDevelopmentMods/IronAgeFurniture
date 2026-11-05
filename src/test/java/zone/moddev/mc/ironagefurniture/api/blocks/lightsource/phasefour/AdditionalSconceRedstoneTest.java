package zone.moddev.mc.ironagefurniture.api.blocks.lightsource.phasefour;

import static org.junit.Assert.*;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.registry.Bootstrap;
import org.junit.BeforeClass;
import org.junit.Test;
import zone.moddev.mc.ironagefurniture.api.SconceMetalData;
import zone.moddev.mc.ironagefurniture.api.enumerations.SconceMetal;

public class AdditionalSconceRedstoneTest {
    @BeforeClass public static void bootstrap() { Bootstrap.register(); }

    @Test public void waterloggedTwinTorchesFollowTheSignalInBothDirections() {
        for (boolean wall : new boolean[]{false, true}) for (boolean lit : new boolean[]{false, true}) {
            AdditionalSconce sconce = sconce(wall, lit, true, 2);
            assertTrue(sconce.litAfterRedstone(true, true));
            assertFalse(sconce.litAfterRedstone(true, false));
        }
    }

    @Test public void dryTwinTorchesStayLitAfterLosingTheSignal() {
        for (boolean wall : new boolean[]{false, true}) for (boolean lit : new boolean[]{false, true}) {
            AdditionalSconce sconce = sconce(wall, lit, true, 2);
            assertTrue(sconce.litAfterRedstone(false, true));
            assertEquals(lit, sconce.litAfterRedstone(false, false));
        }
    }

    @Test public void waterloggedCandlesFollowPowerButDryCandlesStayLit() {
        for (boolean wall : new boolean[]{false, true}) for (boolean lit : new boolean[]{false, true})
            for (int count = 1; count <= 4; count++) {
                AdditionalSconce sconce = sconce(wall, lit, false, count);
                assertTrue(sconce.litAfterRedstone(true, true));
                assertFalse(sconce.litAfterRedstone(true, false));
                assertTrue(sconce.litAfterRedstone(false, true));
                assertEquals(lit, sconce.litAfterRedstone(false, false));
            }
    }

    @Test public void lightSwitchingKeepsWaterFacingAndEveryMetal() {
        for (boolean wall : new boolean[]{false, true}) for (boolean lit : new boolean[]{false, true})
            for (boolean twin : new boolean[]{false, true})
                for (int count = twin ? 2 : 1; count <= (twin ? 2 : 4); count++) {
            AdditionalSconce source = sconce(wall, lit, twin, count);
            AdditionalSconce target = sconce(wall, !lit, twin, count);
            for (Direction facing : Direction.Plane.HORIZONTAL) for (SconceMetal metal : SconceMetal.values()) {
                BlockState state = source.getDefaultState().with(Candle.WATERLOGGED, true)
                        .with(Candle.DIRECTION, facing);
                state = SconceMetalData.withMetal(state, metal);
                BlockState replaced = LightInteractions.replacement(state, target);
                assertTrue(replaced.get(Candle.WATERLOGGED));
                assertEquals(facing, replaced.get(Candle.DIRECTION));
                assertEquals(metal, SconceMetalData.get(replaced));
                assertEquals(SconceMetalData.withMetal(target.getDefaultState(), metal).getBlock(), replaced.getBlock());
                assertEquals(count, target.contentsCount());
            }
        }
    }

    private static AdditionalSconce sconce(boolean wall, boolean lit, boolean twin, int count) {
        return SconceTestFamily.create(name -> new AdditionalSconce(name, wall, lit, twin, count));
    }
}
