package zone.moddev.mc.ironagefurniture.api.Blocks;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class LightSourceLavaLandingTest {
    @Test public void redstoneReleaseUsesCapturedModeInsteadOfAnUnrelatedCreativeBreak() {
        assertTrue(LightSourceLava.shouldPreserveLanding(Boolean.TRUE, false));
        assertFalse(LightSourceLava.shouldPreserveLanding(Boolean.FALSE, true));
    }

    @Test public void ordinaryFallingLampsKeepTheirExistingCreativeBreakPolicy() {
        assertTrue(LightSourceLava.shouldPreserveLanding(null, true));
        assertFalse(LightSourceLava.shouldPreserveLanding(null, false));
    }
}
