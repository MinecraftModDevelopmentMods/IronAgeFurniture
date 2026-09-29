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
                if (stack == null || stack.getItem() != candle) {
                    continue;
                }
                spawnSmoke(player, hand);
            }
        }
    }

    private static void spawnSmoke(EntityPlayer player, EnumHand hand) {
        boolean rightHand = (hand == EnumHand.MAIN_HAND)
                == (player.getPrimaryHand() == EnumHandSide.RIGHT);
        double lateral = rightHand ? -0.27D : 0.27D;
        double yaw = Math.toRadians(player.renderYawOffset);
        double x = player.posX + Math.cos(yaw) * lateral - Math.sin(yaw) * 0.32D;
        double y = player.posY + player.getEyeHeight() - 0.36D - (player.isSneaking() ? 0.16D : 0.0D);
        double z = player.posZ + Math.sin(yaw) * lateral + Math.cos(yaw) * 0.32D;
        player.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL,
                x, y, z, 0.0D, 0.015D, 0.0D);
    }
}
