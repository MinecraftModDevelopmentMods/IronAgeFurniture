package zone.moddev.mc.ironagefurniture.client.particle;

import zone.moddev.mc.ironagefurniture.BlockObjectHolder;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.EnumParticleTypes;
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
        // The first-person hand sits farther from the player's eye than the
        // world-space hand; separate offsets put smoke above each flame.
        double lateral = (rightHand ? -1.0D : 1.0D) * (firstPerson ? 0.70D : 0.27D);
        double forward = firstPerson ? 0.75D : 0.32D;
        double yaw = Math.toRadians(firstPerson ? player.rotationYaw : player.renderYawOffset);
        double x = player.posX + Math.cos(yaw) * lateral - Math.sin(yaw) * forward;
        double y = player.posY + player.getEyeHeight()
                - (firstPerson ? 0.38D : 0.36D) - (player.isSneaking() ? 0.16D : 0.0D);
        double z = player.posZ + Math.sin(yaw) * lateral + Math.cos(yaw) * forward;
        player.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL,
                x, y, z, 0.0D, 0.015D, 0.0D);
    }
}
