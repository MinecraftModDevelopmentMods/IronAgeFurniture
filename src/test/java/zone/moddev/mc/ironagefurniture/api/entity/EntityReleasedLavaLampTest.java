package zone.moddev.mc.ironagefurniture.api.entity;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class EntityReleasedLavaLampTest {
    @Test
    public void collisionReturnsOnlyAfterTheVialClearsItsHolder() {
        int sourceY = 64;
        assertFalse(EntityReleasedLavaLamp.hasClearedHolder(64.3125D, sourceY));
        assertFalse(EntityReleasedLavaLamp.hasClearedHolder(64.1875D, sourceY));
        assertTrue(EntityReleasedLavaLamp.hasClearedHolder(64.18D, sourceY));
        assertTrue(EntityReleasedLavaLamp.hasClearedHolder(63.5D, sourceY));
    }
}
