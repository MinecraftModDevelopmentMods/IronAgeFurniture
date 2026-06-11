package com.mcmoddev.ironagefurniture.api.Blocks;

import java.util.List;

import com.mcmoddev.ironagefurniture.api.tile.TileEntityHalfCabinet;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class HalfCabinet extends Cabinet {
	private static final AxisAlignedBB SOUTH_BOX = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.5D);
	private static final AxisAlignedBB NORTH_BOX = new AxisAlignedBB(0.0D, 0.0D, 0.5D, 1.0D, 1.0D, 1.0D);
	private static final AxisAlignedBB EAST_BOX = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.5D, 1.0D, 1.0D);
	private static final AxisAlignedBB WEST_BOX = new AxisAlignedBB(0.5D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);

	public HalfCabinet(Material materialIn, String name, float resistance, float hardness) {
		super(materialIn, name, resistance, hardness);
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileEntityHalfCabinet();
	}

	@Override
	protected String getContainerName() {
		return "container.ironagefurniture.half_cabinet";
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return getBox(state.getValue(FACING));
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World worldIn, BlockPos pos) {
		return getBox(blockState.getValue(FACING));
	}

	@Override
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox,
			List<AxisAlignedBB> collidingBoxes, Entity entityIn) {
		super.addCollisionBoxToList(pos, entityBox, collidingBoxes, getBox(state.getValue(FACING)));
	}

	@Override
	public double getDisplayItemXOffset(IBlockState state) {
		EnumFacing facing = state.getValue(FACING);

		if (facing == EnumFacing.EAST) {
			return 0.25D;
		}
		if (facing == EnumFacing.WEST) {
			return 0.75D;
		}

		return 0.5D;
	}

	@Override
	public double getDisplayItemZOffset(IBlockState state) {
		EnumFacing facing = state.getValue(FACING);

		if (facing == EnumFacing.SOUTH) {
			return 0.25D;
		}
		if (facing == EnumFacing.NORTH) {
			return 0.75D;
		}

		return 0.5D;
	}

	private static AxisAlignedBB getBox(EnumFacing facing) {
		switch (facing) {
		case NORTH:
			return NORTH_BOX;
		case EAST:
			return EAST_BOX;
		case WEST:
			return WEST_BOX;
		case SOUTH:
		default:
			return SOUTH_BOX;
		}
	}
}
