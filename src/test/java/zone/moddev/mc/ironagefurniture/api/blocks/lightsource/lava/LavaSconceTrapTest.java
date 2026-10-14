package zone.moddev.mc.ironagefurniture.api.blocks.lightsource.lava;

import static org.junit.Assert.*;
import org.junit.Test;

public class LavaSconceTrapTest {
    @Test public void releaseRequiresBothPowerAndAnOpenDrop() {
        assertFalse(LightSourceSconceLavaWall.canRelease(false, false));
        assertFalse(LightSourceSconceLavaWall.canRelease(false, true));
        assertFalse(LightSourceSconceLavaWall.canRelease(true, false));
        assertTrue(LightSourceSconceLavaWall.canRelease(true, true));
    }
}
