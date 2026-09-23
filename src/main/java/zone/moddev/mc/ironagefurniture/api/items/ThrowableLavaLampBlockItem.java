package zone.moddev.mc.ironagefurniture.api.items;

import net.minecraft.util.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.stats.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.block.Block;
import zone.moddev.mc.ironagefurniture.api.entity.ThrownLavaLamp;

public class ThrowableLavaLampBlockItem extends BlockItem {
    public ThrowableLavaLampBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World level, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getHeldItem(hand);
        level.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_EGG_THROW,
                SoundCategory.PLAYERS, 0.5F,
                0.4F / (level.rand.nextFloat() * 0.4F + 0.8F));

        if (!level.isRemote) {
            ThrownLavaLamp lavaLamp = new ThrownLavaLamp(level, player);
            lavaLamp.func_213884_b(stack);
            lavaLamp.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, 1.5F, 1.0F);
            level.addEntity(lavaLamp);
        }

        player.addStat(Stats.ITEM_USED.get(this));
        if (!player.isCreative())
            stack.shrink(1);

        return new ActionResult<>(level.isRemote ? ActionResultType.SUCCESS : ActionResultType.SUCCESS, stack);
    }
}
