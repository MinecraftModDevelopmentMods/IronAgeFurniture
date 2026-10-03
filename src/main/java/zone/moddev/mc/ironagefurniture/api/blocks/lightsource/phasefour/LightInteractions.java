package zone.moddev.mc.ironagefurniture.api.blocks.lightsource.phasefour;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import zone.moddev.mc.ironagefurniture.api.blocks.base.FurnitureBlock;

/** Common state changes and item returns for the new sconce contents. */
public final class LightInteractions {
    private LightInteractions() { }
    public static BlockState replacement(BlockState old, Block block) {
        return block.getDefaultState().with(FurnitureBlock.DIRECTION, old.get(FurnitureBlock.DIRECTION))
                .with(FurnitureBlock.WATERLOGGED, old.get(FurnitureBlock.WATERLOGGED));
    }
    public static void replace(World world, BlockPos pos, BlockState old, Block block) {
        world.setBlockState(pos, replacement(old, block), 3);
    }
    public static void give(PlayerEntity player, Hand hand, Item item, int count) {
        if (player.isCreative()) return;
        ItemStack held = player.getHeldItem(hand);
        if (held.isEmpty()) {
            player.setHeldItem(hand, new ItemStack(item, count));
            return;
        }
        if (held.getItem() == item) {
            int added = Math.min(count, held.getMaxStackSize() - held.getCount());
            held.grow(added);
            count -= added;
        }
        if (count > 0) {
            ItemStack remainder = new ItemStack(item, count);
            if (!player.inventory.addItemStackToInventory(remainder)) player.dropItem(remainder, false);
        }
    }
}
