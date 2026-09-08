package zone.moddev.mc.ironagefurniture.api.Items;

import zone.moddev.mc.ironagefurniture.api.Blocks.GrandChandelierHub;
import zone.moddev.mc.ironagefurniture.api.MetalVariantHelper.MetalVariant;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBlockGrandChandelier extends ItemBlockMetalVariant {
	public ItemBlockGrandChandelier(Block block) {
		super(block);
	}

	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (stack == null || stack.stackSize <= 0 || facing != EnumFacing.DOWN) {
			return EnumActionResult.FAIL;
		}

		BlockPos placePos = getPlacementPos(worldIn, pos, facing);

		if (!canPlaceGrandChandelier(worldIn, placePos, playerIn, stack, facing)) {
			return EnumActionResult.FAIL;
		}

		if (!worldIn.isRemote) {
			MetalVariant metal = MetalVariant.byMeta(stack.getMetadata());
			GrandChandelierHub.placeStructure(worldIn, placePos, metal);

			IBlockState placedState = worldIn.getBlockState(placePos);
			SoundType soundType = this.block.getSoundType(placedState, worldIn, placePos, playerIn);
			worldIn.playSound(playerIn, placePos, soundType.getPlaceSound(), SoundCategory.BLOCKS,
				(soundType.getVolume() + 1.0F) / 2.0F, soundType.getPitch() * 0.8F);

			if (!playerIn.capabilities.isCreativeMode) {
				stack.stackSize--;

				if (stack.stackSize <= 0) {
					playerIn.setHeldItem(hand, null);
				}
			}
		}

		return EnumActionResult.SUCCESS;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side, EntityPlayer player,
			ItemStack stack) {
		if (side != EnumFacing.DOWN) {
			return false;
		}

		return canPlaceGrandChandelier(worldIn, getPlacementPos(worldIn, pos, side), player, stack, side);
	}

	private static BlockPos getPlacementPos(World world, BlockPos pos, EnumFacing facing) {
		IBlockState clickedState = world.getBlockState(pos);
		return clickedState.getBlock().isReplaceable(world, pos) ? pos : pos.offset(facing);
	}

	private static boolean canPlaceGrandChandelier(World world, BlockPos hubPos, EntityPlayer player,
			ItemStack stack, EnumFacing side) {
		if (!GrandChandelierHub.canPlaceStructureAt(world, hubPos)) {
			return false;
		}

		if (player == null) {
			return true;
		}

		if (!player.canPlayerEdit(hubPos, side, stack)) {
			return false;
		}

		for (EnumFacing facing : EnumFacing.HORIZONTALS) {
			if (!player.canPlayerEdit(hubPos.offset(facing), side, stack)) {
				return false;
			}
		}

		return true;
	}
}
