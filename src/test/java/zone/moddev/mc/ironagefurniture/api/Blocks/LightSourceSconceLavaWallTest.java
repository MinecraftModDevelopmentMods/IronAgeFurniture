package zone.moddev.mc.ironagefurniture.api.Blocks;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class LightSourceSconceLavaWallTest {
    @Test public void releasesOnlyWhenPoweredWithAnAirBlockImmediatelyBelow() {
        assertFalse(LightSourceSconceLavaWall.canRelease(false, false));
        assertFalse(LightSourceSconceLavaWall.canRelease(false, true));
        assertFalse(LightSourceSconceLavaWall.canRelease(true, false));
        assertTrue(LightSourceSconceLavaWall.canRelease(true, true));
    }
}
