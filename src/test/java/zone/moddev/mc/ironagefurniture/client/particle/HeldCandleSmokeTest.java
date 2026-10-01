package zone.moddev.mc.ironagefurniture.client.particle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.util.math.Vec3d;
import org.junit.Test;

public class HeldCandleSmokeTest {
    private static final double EPSILON = 0.000001D;

    @Test
    public void firstPersonSmokeStartsAboveBothHeldFlameTips() throws Exception {
        InputStream input = getClass().getResourceAsStream(
                "/assets/ironagefurniture/models/item/light_metal_ironage_candle_floor.json");
        assertNotNull(input);
        JsonObject model;
        try (InputStreamReader reader = new InputStreamReader(input, StandardCharsets.UTF_8)) {
            model = new JsonParser().parse(reader).getAsJsonObject();
        }
        double flameTop = 0.0D;
        for (JsonElement element : model.getAsJsonArray("elements")) {
            JsonObject part = element.getAsJsonObject();
            if (part.get("name").getAsString().startsWith("held_candle_flame_")) {
                flameTop = Math.max(flameTop, part.getAsJsonArray("to").get(1).getAsDouble());
            }
        }
        assertTrue("The carried candle must have flame geometry", flameTop > 0.0D);
        for (boolean rightHand : new boolean[] { true, false }) {
            String transform = "firstperson_" + (rightHand ? "right" : "left") + "hand";
            double translation = model.getAsJsonObject("display").getAsJsonObject(transform)
                    .getAsJsonArray("translation").get(1).getAsDouble() / 16.0D;
            // Vanilla's idle hand is 0.52 below the eye; model coordinates are
            // centred at eight pixels before the item's display translation.
            double tipFromEye = -0.52D + translation + (flameTop - 8.0D) / 16.0D;
            double smokeFromEye = HeldCandleSmoke.smokeOffset(rightHand, true, 0.0F, 0.0F, false).y;
            assertTrue("Smoke must clear the flame, not start in the candle shaft",
                    smokeFromEye >= tipFromEye + 0.05D);
        }
    }

    @Test
    public void firstPersonSmokeStaysBesideTheViewForBothHandsAndAllViewingAngles() {
        for (boolean rightHand : new boolean[] { true, false }) {
            double expectedLateral = rightHand ? -0.70D : 0.70D;
            for (float yawDegrees : new float[] { 0.0F, 90.0F, 180.0F, 270.0F }) {
                for (float pitchDegrees : new float[] { -90.0F, -45.0F, 0.0F, 45.0F, 90.0F }) {
                    Vec3d offset = HeldCandleSmoke.smokeOffset(rightHand, true, yawDegrees, pitchDegrees, false);
                    double yaw = Math.toRadians(yawDegrees);
                    double pitch = Math.toRadians(pitchDegrees);
                    double lateral = offset.x * Math.cos(yaw) + offset.z * Math.sin(yaw);
                    double horizontal = -offset.x * Math.sin(yaw) + offset.z * Math.cos(yaw);
                    double cameraHeight = offset.y * Math.cos(pitch) + horizontal * Math.sin(pitch);
                    double cameraForward = horizontal * Math.cos(pitch) - offset.y * Math.sin(pitch);
                    assertEquals(expectedLateral, lateral, EPSILON);
                    assertEquals(0.04D, cameraHeight, EPSILON);
                    assertEquals(0.75D, cameraForward, EPSILON);
                }
            }
        }
    }

    @Test
    public void crouchingDoesNotLowerFirstPersonSmokeTwice() {
        for (boolean rightHand : new boolean[] { true, false }) {
            for (float pitchDegrees : new float[] { -90.0F, -45.0F, 0.0F, 45.0F, 90.0F }) {
                Vec3d standing = HeldCandleSmoke.smokeOffset(rightHand, true, 90.0F, pitchDegrees, false);
                Vec3d crouching = HeldCandleSmoke.smokeOffset(rightHand, true, 90.0F, pitchDegrees, true);
                assertEquals(standing.x, crouching.x, EPSILON);
                assertEquals(standing.y, crouching.y, EPSILON);
                assertEquals(standing.z, crouching.z, EPSILON);
            }
        }
    }

    @Test
    public void thirdPersonKeepsItsExistingHandPositionRegardlessOfHeadPitch() {
        for (boolean rightHand : new boolean[] { true, false }) {
            for (float yawDegrees : new float[] { 0.0F, 90.0F, 180.0F, 270.0F }) {
                double yaw = Math.toRadians(yawDegrees);
                double lateral = rightHand ? -0.27D : 0.27D;
                for (float pitchDegrees : new float[] { -90.0F, 0.0F, 90.0F }) {
                    Vec3d offset = HeldCandleSmoke.smokeOffset(rightHand, false, yawDegrees, pitchDegrees, false);
                    assertEquals(Math.cos(yaw) * lateral - Math.sin(yaw) * 0.32D, offset.x, EPSILON);
                    assertEquals(-0.36D, offset.y, EPSILON);
                    assertEquals(Math.sin(yaw) * lateral + Math.cos(yaw) * 0.32D, offset.z, EPSILON);
                    Vec3d crouching = HeldCandleSmoke.smokeOffset(rightHand, false, yawDegrees, pitchDegrees, true);
                    assertEquals(offset.x, crouching.x, EPSILON);
                    assertEquals(-0.52D, crouching.y, EPSILON);
                    assertEquals(offset.z, crouching.z, EPSILON);
                }
            }
        }
    }
}
