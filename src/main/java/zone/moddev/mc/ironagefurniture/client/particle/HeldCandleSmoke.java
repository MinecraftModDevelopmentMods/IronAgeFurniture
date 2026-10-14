package zone.moddev.mc.ironagefurniture.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import zone.moddev.mc.ironagefurniture.Ironagefurniture;
import zone.moddev.mc.ironagefurniture.init.PhaseFourLighting;

/** Sparse smoke above the held flame, without changing world lighting. */
@Mod.EventBusSubscriber(modid = Ironagefurniture.MODID, value = Dist.CLIENT)
public final class HeldCandleSmoke {
    private HeldCandleSmoke() { }
    @SubscribeEvent public static void tick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null || minecraft.player == null) return;
        for (PlayerEntity player : minecraft.level.players()) {
            if (player.isInvisible() || player.tickCount % 8 != 0) continue;
            for (Hand hand : Hand.values()) {
                if (player.getItemInHand(hand).getItem() != PhaseFourLighting.candle(false, true).asItem()) continue;
                boolean right = (hand == Hand.MAIN_HAND) == (player.getMainArm() == HandSide.RIGHT);
                boolean first = player == minecraft.player && minecraft.options.getCameraType().isFirstPerson();
                Vector3d offset = smokeOffset(right, first, first ? player.yRot : player.yBodyRot,
                        player.xRot, player.isShiftKeyDown());
                minecraft.level.addParticle(ParticleTypes.SMOKE, player.getX() + offset.x,
                        player.getY() + player.getEyeHeight() + offset.y, player.getZ() + offset.z, 0, .015, 0);
            }
        }
    }
    public static Vector3d smokeOffset(boolean right, boolean first, float yawDegrees, float pitchDegrees, boolean sneaking) {
        double lateral = (right ? -1 : 1) * (first ? .70 : .27);
        double forward = first ? .75 : .32;
        // The model's raised first-person flame is near eye height. Eye height
        // already includes crouching; only third-person hands need another offset.
        double height = first ? .04 : -.36 - (sneaking ? .16 : 0);
        double pitch = first ? Math.toRadians(pitchDegrees) : 0;
        double horizontal = forward * Math.cos(pitch) + height * Math.sin(pitch);
        double y = height * Math.cos(pitch) - forward * Math.sin(pitch);
        double yaw = Math.toRadians(yawDegrees);
        return new Vector3d(Math.cos(yaw) * lateral - Math.sin(yaw) * horizontal, y,
                Math.sin(yaw) * lateral + Math.cos(yaw) * horizontal);
    }
}
