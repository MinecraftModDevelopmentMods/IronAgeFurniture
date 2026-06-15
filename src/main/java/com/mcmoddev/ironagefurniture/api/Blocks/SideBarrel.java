package com.mcmoddev.ironagefurniture.api.Blocks;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class SideBarrel extends Barrel {
	public static final PropertyDirection FACING = BlockHorizontal.FACING;
	private static final AxisAlignedBB NORTH_SOUTH_AABB = new AxisAlignedBB(1.0D / 16.0D, 0.0D, 0.0D,
		15.0D / 16.0D, 1.0D, 1.0D);
	private static final AxisAlignedBB EAST_WEST_AABB = new AxisAlignedBB(0.0D, 0.0D, 1.0D / 16.0D,
		1.0D, 1.0D, 15.0D / 16.0D);

	public SideBarrel(Material materialIn, String name, float resistance, float hardness) {
		super(materialIn, name, resistance, hardness);
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.SOUTH));
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY,
			float hitZ, int meta, EntityLivingBase placer, ItemStack stack) {
		EnumFacing barrelFacing = placer == null ? EnumFacing.SOUTH : placer.getHorizontalFacing();
		return this.getDefaultState().withProperty(FACING, barrelFacing);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta & 3));
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return ((EnumFacing)state.getValue(FACING)).getHorizontalIndex();
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { FACING });
	}

	@Override
	public IBlockState withRotation(IBlockState state, Rotation rot) {
		return state.withProperty(FACING, rot.rotate((EnumFacing)state.getValue(FACING)));
	}

	@Override
	public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
		return state.withRotation(mirrorIn.toRotation((EnumFacing)state.getValue(FACING)));
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return this.getBox(state);
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World worldIn, BlockPos pos) {
		return this.getBox(blockState);
	}

	private AxisAlignedBB getBox(IBlockState state) {
		EnumFacing facing = (EnumFacing)state.getValue(FACING);
		return facing.getAxis() == EnumFacing.Axis.X ? EAST_WEST_AABB : NORTH_SOUTH_AABB;
	}
}
