package zone.moddev.mc.ironagefurniture.client.particle;

import zone.moddev.mc.ironagefurniture.BlockObjectHolder;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/** Sparse, cosmetic smoke for lit candles carried by visible players. */
@SideOnly(Side.CLIENT)
public final class HeldCandleSmoke {
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }

        Minecraft minecraft = Minecraft.getMinecraft();
        if (minecraft.world == null || minecraft.player == null
                || BlockObjectHolder.light_metal_ironage_candle_floor == null) {
            return;
        }

        Item candle = Item.getItemFromBlock(BlockObjectHolder.light_metal_ironage_candle_floor);
        for (EntityPlayer player : minecraft.world.playerEntities) {
            if (player.isInvisible() || player.ticksExisted % 8 != 0) {
                continue;
            }
            for (EnumHand hand : EnumHand.values()) {
                ItemStack stack = player.getHeldItem(hand);
                if (stack.isEmpty() || stack.getItem() != candle) {
                    continue;
                }
                spawnSmoke(minecraft, player, hand);
            }
        }
    }

    private static void spawnSmoke(Minecraft minecraft, EntityPlayer player, EnumHand hand) {
        boolean rightHand = (hand == EnumHand.MAIN_HAND)
                == (player.getPrimaryHand() == EnumHandSide.RIGHT);
        boolean firstPerson = player == minecraft.player && minecraft.gameSettings.thirdPersonView == 0;
        Vec3d offset = smokeOffset(rightHand, firstPerson,
                firstPerson ? player.rotationYaw : player.renderYawOffset, player.rotationPitch,
                player.isSneaking());
        double x = player.posX + offset.x;
        double y = player.posY + player.getEyeHeight() + offset.y;
        double z = player.posZ + offset.z;
        player.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL,
                x, y, z, 0.0D, 0.015D, 0.0D);
    }

    static Vec3d smokeOffset(boolean rightHand, boolean firstPerson, float yawDegrees,
            float pitchDegrees, boolean sneaking) {
        double lateral = (rightHand ? -1.0D : 1.0D) * (firstPerson ? 0.70D : 0.27D);
        double forward = firstPerson ? 0.75D : 0.32D;
        // The held model raises its flame almost to eye height. Keep smoke just
        // above that tip, and rotate with the camera when looking up or down.
        // Eye height already follows first-person crouching; only the visible
        // third-person hand needs its additional crouch offset.
        double height = firstPerson ? 0.04D : -0.36D - (sneaking ? 0.16D : 0.0D);
        double pitch = firstPerson ? Math.toRadians(pitchDegrees) : 0.0D;
        double horizontal = forward * Math.cos(pitch) + height * Math.sin(pitch);
        double y = height * Math.cos(pitch) - forward * Math.sin(pitch);
        double yaw = Math.toRadians(yawDegrees);
        return new Vec3d(Math.cos(yaw) * lateral - Math.sin(yaw) * horizontal,
                y, Math.sin(yaw) * lateral + Math.cos(yaw) * horizontal);
    }
}
