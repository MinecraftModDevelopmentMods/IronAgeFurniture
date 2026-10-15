package zone.moddev.mc.ironagefurniture.api.blocks.lightsource.phasefour;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import zone.moddev.mc.ironagefurniture.api.blocks.base.FurnitureBlock;

/** Common state changes and item returns for the new sconce contents. */
public final class LightInteractions {
    private LightInteractions() { }
    public static BlockState replacement(BlockState old, Block block) {
        return zone.moddev.mc.ironagefurniture.api.SconceMetalData.preserve(old,
                block.defaultBlockState().setValue(FurnitureBlock.DIRECTION, old.getValue(FurnitureBlock.DIRECTION))
                .setValue(FurnitureBlock.WATERLOGGED, old.getValue(FurnitureBlock.WATERLOGGED)));
    }
    public static void replace(Level world, BlockPos pos, BlockState old, Block block) {
        world.setBlock(pos, replacement(old, block), 3);
    }
    public static void give(Player player, InteractionHand hand, Item item, int count) {
        if (player.isCreative()) return;
        ItemStack held = player.getItemInHand(hand);
        if (held.isEmpty()) {
            player.setItemInHand(hand, new ItemStack(item, count));
            return;
        }
        if (held.getItem() == item) {
            int added = Math.min(count, held.getMaxStackSize() - held.getCount());
            held.grow(added);
            count -= added;
        }
        if (count > 0) {
            ItemStack remainder = new ItemStack(item, count);
            if (!player.getInventory().add(remainder)) player.drop(remainder, false);
        }
    }
}
