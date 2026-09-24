package zone.moddev.mc.ironagefurniture.api.items;

import net.minecraft.util.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.stats.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.ActionResult;
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
    public ActionResult<ItemStack> use(World level, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getItemInHand(hand);
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.EGG_THROW,
                SoundCategory.PLAYERS, 0.5F,
                0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));

        if (!level.isClientSide) {
            ThrownLavaLamp lavaLamp = new ThrownLavaLamp(level, player);
            lavaLamp.setItem(stack);
            lavaLamp.shootFromRotation(player, player.xRot, player.yRot, 0.0F, 1.5F, 1.0F);
            level.addFreshEntity(lavaLamp);
        }

        player.awardStat(Stats.ITEM_USED.get(this));
        if (!player.isCreative())
            stack.shrink(1);

        return ActionResult.sidedSuccess(stack, level.isClientSide());
    }
}
