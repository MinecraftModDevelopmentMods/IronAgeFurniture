package zone.moddev.mc.ironagefurniture.api.Blocks;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class LightSourceLavaLandingTest {
    @Test
    public void releasedLampUsesCreativeStateCapturedWhenItLeavesTheSconce() {
        assertTrue(LightSourceLava.shouldPreserveLanding(Boolean.TRUE, false));
        assertFalse(LightSourceLava.shouldPreserveLanding(Boolean.FALSE, true));
    }

    @Test
    public void ordinaryFallingLampKeepsItsExistingCreativeBreakPolicy() {
        assertTrue(LightSourceLava.shouldPreserveLanding(null, true));
        assertFalse(LightSourceLava.shouldPreserveLanding(null, false));
    }
}
