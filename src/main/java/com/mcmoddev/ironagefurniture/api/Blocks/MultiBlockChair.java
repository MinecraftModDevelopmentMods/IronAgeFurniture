package com.mcmoddev.ironagefurniture.api.Blocks;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import com.google.common.collect.Lists;
import com.mcmoddev.ironagefurniture.api.Enumerations.ChairPart;
import com.mcmoddev.ironagefurniture.api.entity.Seat;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class MultiBlockChair extends Chair {
	public static final PropertyEnum<ChairPart> PART = PropertyEnum.<ChairPart>create("part", ChairPart.class);

	protected static final AxisAlignedBB LOWER_BB = new AxisAlignedBB(0.0625D, 0.375D, 0.125D, 0.9375D, 0.5D, 0.9375D);
	private static final AxisAlignedBB LEFT_ARM_COLLISION_BB = new AxisAlignedBB(0.0D, 0.0D, 0.125D, 0.125D, 0.8125D, 0.9375D);
	private static final AxisAlignedBB RIGHT_ARM_COLLISION_BB = new AxisAlignedBB(0.875D, 0.0D, 0.125D, 1.0D, 0.8125D, 0.9375D);
	private static final AxisAlignedBB LOWER_BACK_COLLISION_BB = new AxisAlignedBB(0.125D, 0.0D, 0.875D, 0.875D, 1.0D, 1.0D);
	private static final AxisAlignedBB[] LOWER_COLLISION_BOXES = new AxisAlignedBB[] {
		LOWER_BB,
		LEFT_ARM_COLLISION_BB,
		RIGHT_ARM_COLLISION_BB,
		LOWER_BACK_COLLISION_BB
	};
	private static final Set<BlockPos> REMOVING_PARTS = new HashSet<BlockPos>();

	public MultiBlockChair(Material materialIn, String name, float resistance, float hardness) {
		super(materialIn, name, resistance, hardness);
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.SOUTH)
			.withProperty(PART, ChairPart.LOWER));
	}

	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
		if (!super.canPlaceBlockAt(worldIn, pos)) {
			return false;
		}

		for (int i = 1; i < this.getChairHeight(); i++) {
			if (!worldIn.isAirBlock(pos.up(i))) {
				return false;
			}
		}

		return true;
	}

	@Override
	public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side) {
		return this.canPlaceBlockAt(worldIn, pos);
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY,
			float hitZ, int meta, EntityLivingBase placer, ItemStack stack) {
		return super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, stack)
			.withProperty(PART, ChairPart.LOWER);
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack) {
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);

		if (!worldIn.isRemote) {
			EnumFacing facing = state.getValue(FACING);

			for (int i = 1; i < this.getChairHeight(); i++) {
				worldIn.setBlockState(pos.up(i), this.getDefaultState().withProperty(FACING, facing)
					.withProperty(PART, this.getPartForOffset(i)), 3);
			}
		}
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (state.getValue(PART) == ChairPart.LOWER) {
			return super.onBlockActivated(worldIn, pos, state, playerIn, hand, heldItem, side, hitX, hitY, hitZ);
		}

		BlockPos lowerPos = this.getLowerPos(pos, state);
		IBlockState lowerState = worldIn.getBlockState(lowerPos);

		if (lowerState.getBlock() == this && lowerState.getValue(PART) == ChairPart.LOWER) {
			return super.onBlockActivated(worldIn, lowerPos, lowerState, playerIn, hand, heldItem, side, hitX, hitY, hitZ);
		}

		return false;
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		if (!worldIn.isRemote && !REMOVING_PARTS.contains(pos)) {
			this.removeOtherParts(worldIn, pos, state);
		}

		super.breakBlock(worldIn, pos, state);
	}

	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		return Lists.newArrayList(new ItemStack(Item.getItemFromBlock(this)));
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Item.getItemFromBlock(this);
	}

	@Override
	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
		return new ItemStack(Item.getItemFromBlock(this));
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return this.rotateBoundingBox(this.getPartBoundingBox(state.getValue(PART)), state.getValue(FACING));
	}

	@Override
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox,
			List<AxisAlignedBB> collidingBoxes, Entity entityIn) {
		if (!(entityIn instanceof Seat)) {
			this.addRotatedCollisionBoxes(state, pos, entityBox, collidingBoxes);
		}
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState()
			.withProperty(FACING, EnumFacing.getHorizontal(meta & 3))
			.withProperty(PART, ChairPart.byMetadata(meta >> 2));
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		int meta = ((EnumFacing)state.getValue(FACING)).getHorizontalIndex();
		meta += ((ChairPart)state.getValue(PART)).ordinal() * 4;
		return meta;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { FACING, PART });
	}

	protected abstract int getChairHeight();

	protected abstract AxisAlignedBB getUpperPartBoundingBox(ChairPart part);

	protected abstract AxisAlignedBB[] getUpperPartCollisionBoxes(ChairPart part);

	protected AxisAlignedBB[] getLowerCollisionBoxes() {
		return LOWER_COLLISION_BOXES;
	}

	protected AxisAlignedBB rotateBoundingBox(AxisAlignedBB bb, EnumFacing facing) {
		switch (facing) {
		case NORTH:
			return new AxisAlignedBB(1.0D - bb.maxX, bb.minY, 1.0D - bb.maxZ, 1.0D - bb.minX, bb.maxY, 1.0D - bb.minZ);
		case EAST:
			return new AxisAlignedBB(bb.minZ, bb.minY, 1.0D - bb.maxX, bb.maxZ, bb.maxY, 1.0D - bb.minX);
		case WEST:
			return new AxisAlignedBB(1.0D - bb.maxZ, bb.minY, bb.minX, 1.0D - bb.minZ, bb.maxY, bb.maxX);
		default:
			return bb;
		}
	}

	private BlockPos getLowerPos(BlockPos pos, IBlockState state) {
		return pos.down(this.getPartOffset(state.getValue(PART)));
	}

	private ChairPart getPartForOffset(int offset) {
		if (offset <= 0) {
			return ChairPart.LOWER;
		}

		if (this.getChairHeight() <= 2 || offset >= this.getChairHeight() - 1) {
			return ChairPart.UPPER;
		}

		return ChairPart.MIDDLE;
	}

	private int getPartOffset(ChairPart part) {
		if (part == ChairPart.LOWER) {
			return 0;
		}

		if (part == ChairPart.UPPER) {
			return Math.max(1, this.getChairHeight() - 1);
		}

		return 1;
	}

	private AxisAlignedBB getPartBoundingBox(ChairPart part) {
		if (part == ChairPart.LOWER) {
			return LOWER_BB;
		}

		return this.getUpperPartBoundingBox(part);
	}

	private AxisAlignedBB[] getPartCollisionBoxes(ChairPart part) {
		if (part == ChairPart.LOWER) {
			return this.getLowerCollisionBoxes();
		}

		return this.getUpperPartCollisionBoxes(part);
	}

	private void addRotatedCollisionBoxes(IBlockState state, BlockPos pos, AxisAlignedBB entityBox,
			List<AxisAlignedBB> collidingBoxes) {
		EnumFacing facing = state.getValue(FACING);

		for (AxisAlignedBB bb : this.getPartCollisionBoxes(state.getValue(PART))) {
			super.addCollisionBoxToList(pos, entityBox, collidingBoxes, this.rotateBoundingBox(bb, facing));
		}
	}

	private void removeOtherParts(World worldIn, BlockPos pos, IBlockState state) {
		BlockPos lowerPos = this.getLowerPos(pos, state);
		Set<BlockPos> structurePositions = new HashSet<BlockPos>();

		for (int i = 0; i < this.getChairHeight(); i++) {
			structurePositions.add(lowerPos.up(i));
		}

		REMOVING_PARTS.addAll(structurePositions);

		try {
			for (int i = 0; i < this.getChairHeight(); i++) {
				BlockPos partPos = lowerPos.up(i);

				if (!partPos.equals(pos) && this.isExpectedPart(worldIn, partPos, i)) {
					worldIn.setBlockToAir(partPos);
				}
			}
		} finally {
			REMOVING_PARTS.removeAll(structurePositions);
		}
	}

	private boolean isExpectedPart(World worldIn, BlockPos pos, int index) {
		IBlockState state = worldIn.getBlockState(pos);

		return state.getBlock() == this
			&& state.getValue(PART) == this.getPartForOffset(index);
	}
}
