package com.mcmoddev.ironagefurniture.api.Blocks;

import com.mcmoddev.ironagefurniture.Ironagefurniture;
import com.mcmoddev.ironagefurniture.api.tile.TileEntityBarrel;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidUtil;

public class Barrel extends Block {
	private static final AxisAlignedBB BARREL_AABB = new AxisAlignedBB(1.0D / 16.0D, 0.0D, 1.0D / 16.0D,
		15.0D / 16.0D, 1.0D, 15.0D / 16.0D);

	public Barrel(Material materialIn, String name, float resistance, float hardness) {
		super(materialIn);
		this.blockResistance = resistance;
		this.blockHardness = hardness;
		this.setSoundType(SoundType.WOOD);
		this.setHarvestLevel("axe", 0);
		this.setCreativeTab(Ironagefurniture.ironagefurnitureTab);
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (this.isBucketInteractionItem(heldItem)) {
			if (worldIn.isRemote) {
				return true;
			}

			TileEntityBarrel barrel = this.getBarrelEntity(worldIn, pos);

			if (barrel == null) {
				return false;
			}

			if (FluidUtil.interactWithFluidHandler(heldItem, barrel.getFluidHandler(), playerIn)) {
				barrel.markForFluidUpdate();
			}

			return true;
		}

		if (!worldIn.isRemote) {
			playerIn.openGui(Ironagefurniture.instance, Ironagefurniture.GUI_BARREL, worldIn,
				pos.getX(), pos.getY(), pos.getZ());
		}

		return true;
	}

	private boolean isBucketInteractionItem(ItemStack heldItem) {
		if (heldItem == null || heldItem.stackSize <= 0) {
			return false;
		}

		if (heldItem.getItem() == Items.BUCKET) {
			return true;
		}

		ItemStack singleItem = heldItem.copy();
		singleItem.stackSize = 1;
		return FluidUtil.getFluidHandler(singleItem) != null;
	}

	private TileEntityBarrel getBarrelEntity(IBlockAccess worldIn, BlockPos pos) {
		TileEntity tileEntity = worldIn.getTileEntity(pos);
		return tileEntity instanceof TileEntityBarrel ? (TileEntityBarrel)tileEntity : null;
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileEntityBarrel();
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return BARREL_AABB;
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World worldIn, BlockPos pos) {
		return BARREL_AABB;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean hasComparatorInputOverride(IBlockState state) {
		return true;
	}

	@Override
	public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos) {
		TileEntityBarrel barrel = this.getBarrelEntity(worldIn, pos);

		if (barrel == null || barrel.getFluidAmount() <= 0) {
			return 0;
		}

		return MathHelper.floor((float)barrel.getFluidAmount() * 14.0F / (float)barrel.getCapacity()) + 1;
	}
}
