package zone.moddev.mc.ironagefurniture.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import zone.moddev.mc.ironagefurniture.api.CandleInventory;
import zone.moddev.mc.ironagefurniture.IronAgeFurnitureConfiguration;
import zone.moddev.mc.ironagefurniture.Ironagefurniture;
import zone.moddev.mc.ironagefurniture.init.PhaseFourLighting;

/** Sparse smoke above the held flame, without changing world lighting. */
@EventBusSubscriber(modid = Ironagefurniture.MODID, value = Dist.CLIENT)
public final class HeldCandleEffects {
    private HeldCandleEffects() { }
    @SubscribeEvent public static void tick(ClientTickEvent.Post event) {
        if (!IronAgeFurnitureConfiguration.heldCandleEffects()) return;
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null || minecraft.player == null) return;
        for (Player player : minecraft.level.players()) {
            if (player.isInvisible() || player.tickCount % 2 != 0) continue;
            for (InteractionHand hand : InteractionHand.values()) {
                if (!CandleInventory.accepts(player.getItemInHand(hand))) continue;
                boolean right = (hand == InteractionHand.MAIN_HAND) == (player.getMainArm() == HumanoidArm.RIGHT);
                boolean first = player == minecraft.player && minecraft.options.getCameraType().isFirstPerson();
                Vec3 offset = smokeOffset(right, first, first ? player.getYRot() : player.yBodyRot,
                        player.getXRot(), player.isShiftKeyDown());
                minecraft.level.addParticle(ParticleTypes.SMALL_FLAME, player.getX() + offset.x,
                        player.getY() + player.getEyeHeight() + offset.y, player.getZ() + offset.z, 0, 0, 0);
                if (player.tickCount % 8 == 0) minecraft.level.addParticle(ParticleTypes.SMOKE, player.getX() + offset.x,
                        player.getY() + player.getEyeHeight() + offset.y + .04, player.getZ() + offset.z, 0, .015, 0);
            }
        }
    }
    public static Vec3 smokeOffset(boolean right, boolean first, float yawDegrees, float pitchDegrees, boolean sneaking) {
        double lateral = (right ? -1 : 1) * (first ? .70 : .27);
        double forward = first ? .75 : .32;
        // The model's raised first-person flame is near eye height. Eye height
        // already includes crouching; only third-person hands need another offset.
        double height = first ? .04 : -.36 - (sneaking ? .16 : 0);
        double pitch = first ? Math.toRadians(pitchDegrees) : 0;
        double horizontal = forward * Math.cos(pitch) + height * Math.sin(pitch);
        double y = height * Math.cos(pitch) - forward * Math.sin(pitch);
        double yaw = Math.toRadians(yawDegrees);
        return new Vec3(Math.cos(yaw) * lateral - Math.sin(yaw) * horizontal, y,
                Math.sin(yaw) * lateral + Math.cos(yaw) * horizontal);
    }
}
