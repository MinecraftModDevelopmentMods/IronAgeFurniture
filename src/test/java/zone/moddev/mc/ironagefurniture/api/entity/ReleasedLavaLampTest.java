package zone.moddev.mc.ironagefurniture.api.entity;

import static org.junit.Assert.*;
import org.junit.Test;

public class ReleasedLavaLampTest {
    @Test public void collisionResumesOnlyWhenTheVialClearsTheHolder() {
        assertFalse(ReleasedLavaLamp.hasClearedHolder(80 + 5.0 / 16.0, 80));
        assertFalse(ReleasedLavaLamp.hasClearedHolder(80 + 3.0 / 16.0, 80));
        assertTrue(ReleasedLavaLamp.hasClearedHolder(80 + 2.99 / 16.0, 80));
    }
}
