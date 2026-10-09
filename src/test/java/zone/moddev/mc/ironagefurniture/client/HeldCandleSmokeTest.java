package zone.moddev.mc.ironagefurniture.client;

import static org.junit.Assert.*;
import net.minecraft.util.math.Vec3d;
import org.junit.Test;
import zone.moddev.mc.ironagefurniture.client.particle.HeldCandleSmoke;

public class HeldCandleSmokeTest {
    @Test public void smokeIsAboveTheFirstPersonTipAndOutsideTheCentreOfView() {
        Vec3d right = HeldCandleSmoke.smokeOffset(true, true, 0, 0, false);
        Vec3d left = HeldCandleSmoke.smokeOffset(false, true, 0, 0, false);
        assertEquals(.04, right.y, 1e-10);
        assertEquals(.75, right.z, 1e-10);
        assertEquals(-right.x, left.x, 1e-10);
        assertTrue(Math.abs(right.x) > .5);
        assertEquals(right.y, HeldCandleSmoke.smokeOffset(true, true, 0, 0, true).y, 1e-10);
    }
    @Test public void smokeFollowsCameraPitchAndThirdPersonCrouching() {
        Vec3d down = HeldCandleSmoke.smokeOffset(true, true, 90, 90, false);
        assertEquals(-.75, down.y, 1e-10);
        assertEquals(.04, Math.abs(down.x), 1e-10);
        Vec3d normal = HeldCandleSmoke.smokeOffset(true, false, 0, 0, false);
        Vec3d crouched = HeldCandleSmoke.smokeOffset(true, false, 0, 90, true);
        assertEquals(normal.y - .16, crouched.y, 1e-10);
        assertEquals(normal.z, crouched.z, 1e-10);
    }
}
