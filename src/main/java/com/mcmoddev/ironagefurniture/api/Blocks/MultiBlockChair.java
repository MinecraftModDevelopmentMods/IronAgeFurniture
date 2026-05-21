package com.mcmoddev.ironagefurniture.api.Blocks;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
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

public class MultiBlockChair extends Chair {
	private static final AxisAlignedBB LOWER_BB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
	private static final AxisAlignedBB WINGBACK_UPPER_BB = new AxisAlignedBB(0.0D, 0.0D, 0.4375D, 1.0D, 0.75D, 1.0D);
	private static final AxisAlignedBB THRONE_MIDDLE_BB = new AxisAlignedBB(0.0D, 0.0D, 0.5625D, 1.0D, 1.0D, 1.0D);
	private static final AxisAlignedBB THRONE_UPPER_BB = new AxisAlignedBB(0.0D, 0.0D, 0.4375D, 1.0D, 0.9375D, 1.0D);
	private static final Set<BlockPos> REMOVING_PARTS = new HashSet<BlockPos>();

	private final int height;
	private final int partIndex;
	private Block[] parts;

	public MultiBlockChair(Material materialIn, String name, float resistance, float hardness, int height, int partIndex) {
		super(materialIn, name, resistance, hardness);
		this.height = height;
		this.partIndex = partIndex;
	}

	public MultiBlockChair setParts(Block[] parts) {
		this.parts = parts;
		return this;
	}

	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
		if (this.partIndex != 0 || !super.canPlaceBlockAt(worldIn, pos)) {
			return false;
		}

		for (int i = 1; i < this.height; i++) {
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
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack) {
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);

		if (!worldIn.isRemote && this.partIndex == 0 && this.parts != null) {
			EnumFacing facing = state.getValue(FACING);

			for (int i = 1; i < this.height; i++) {
				worldIn.setBlockState(pos.up(i), this.parts[i].getDefaultState().withProperty(FACING, facing), 3);
			}
		}
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (this.partIndex == 0) {
			return super.onBlockActivated(worldIn, pos, state, playerIn, hand, heldItem, side, hitX, hitY, hitZ);
		}

		BlockPos lowerPos = this.getLowerPos(pos);
		IBlockState lowerState = worldIn.getBlockState(lowerPos);
		Block lowerBlock = this.getBaseBlock();

		if (lowerState.getBlock() instanceof MultiBlockChair && lowerState.getBlock() == lowerBlock) {
			return lowerBlock.onBlockActivated(worldIn, lowerPos, lowerState, playerIn, hand, heldItem, side, hitX, hitY, hitZ);
		}

		return false;
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		if (!worldIn.isRemote && !REMOVING_PARTS.contains(pos)) {
			this.removeOtherParts(worldIn, pos);
		}

		super.breakBlock(worldIn, pos, state);
	}

	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		return Lists.newArrayList(new ItemStack(Item.getItemFromBlock(this.getBaseBlock())));
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Item.getItemFromBlock(this.getBaseBlock());
	}

	@Override
	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
		return new ItemStack(Item.getItemFromBlock(this.getBaseBlock()));
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return this.rotateBoundingBox(this.getPartBoundingBox(), state.getValue(FACING));
	}

	@Override
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox,
			List<AxisAlignedBB> collidingBoxes, Entity entityIn) {
		if (this.partIndex == 0) {
			super.addCollisionBoxToList(state, worldIn, pos, entityBox, collidingBoxes, entityIn);
		}
	}

	private Block getBaseBlock() {
		if (this.parts != null && this.parts.length > 0 && this.parts[0] != null) {
			return this.parts[0];
		}

		return this;
	}

	private BlockPos getLowerPos(BlockPos pos) {
		return pos.down(this.partIndex);
	}

	private AxisAlignedBB getPartBoundingBox() {
		if (this.partIndex == 0) {
			return LOWER_BB;
		}

		if (this.height == 2) {
			return WINGBACK_UPPER_BB;
		}

		if (this.partIndex == 1) {
			return THRONE_MIDDLE_BB;
		}

		return THRONE_UPPER_BB;
	}

	private AxisAlignedBB rotateBoundingBox(AxisAlignedBB bb, EnumFacing facing) {
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

	private void removeOtherParts(World worldIn, BlockPos pos) {
		BlockPos lowerPos = this.getLowerPos(pos);
		Set<BlockPos> structurePositions = new HashSet<BlockPos>();

		for (int i = 0; i < this.height; i++) {
			structurePositions.add(lowerPos.up(i));
		}

		REMOVING_PARTS.addAll(structurePositions);

		try {
			for (int i = 0; i < this.height; i++) {
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
		return this.parts != null
			&& index >= 0
			&& index < this.parts.length
			&& worldIn.getBlockState(pos).getBlock() == this.parts[index];
	}
}
