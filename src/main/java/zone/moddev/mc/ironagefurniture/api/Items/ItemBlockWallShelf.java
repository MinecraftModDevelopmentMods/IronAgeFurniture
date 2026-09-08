package zone.moddev.mc.ironagefurniture.api.Items;

import zone.moddev.mc.ironagefurniture.api.Blocks.WallShelf;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBlockWallShelf extends ItemBlock {
	public ItemBlockWallShelf(Block block) {
		super(block);
	}

	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		IBlockState state = worldIn.getBlockState(pos);

		if (state.getBlock() instanceof WallShelf && this.block instanceof WallShelf
				&& ((WallShelf)state.getBlock()).tryPlaceShelfFromShelfClick(worldIn, pos, state, playerIn, hand,
						stack, facing, hitX, hitY, hitZ)) {
			return EnumActionResult.SUCCESS;
		}

		return super.onItemUse(stack, playerIn, worldIn, pos, hand, facing, hitX, hitY, hitZ);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side, EntityPlayer player,
			ItemStack stack) {
		IBlockState state = worldIn.getBlockState(pos);

		if (state.getBlock() instanceof WallShelf && this.block instanceof WallShelf) {
			return true;
		}

		return super.canPlaceBlockOnSide(worldIn, pos, side, player, stack);
	}
}
