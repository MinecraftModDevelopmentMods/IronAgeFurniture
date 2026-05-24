package com.mcmoddev.ironagefurniture.api.Blocks;

import java.util.List;

import com.mcmoddev.ironagefurniture.Ironagefurniture;
import com.mcmoddev.ironagefurniture.api.tile.TileEntityWallShelf;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class WallShelf extends BlockHBase {
	public static final PropertyEnum<ShelfSupport> SUPPORT = PropertyEnum.create("support", ShelfSupport.class);

	private static final AxisAlignedBB BOARD_STRAIGHT_NORTH = new AxisAlignedBB(0.0D, 0.625D, 0.25D, 1.0D, 0.8125D, 1.0D);
	private static final AxisAlignedBB BOARD_INNER_CORNER_NORTH = new AxisAlignedBB(0.0D, 0.625D, 0.0D, 1.0D, 0.8125D, 1.0D);
	private static final AxisAlignedBB BOARD_OUTER_CORNER_NORTH = new AxisAlignedBB(0.25D, 0.625D, 0.25D, 1.0D, 0.8125D, 1.0D);
	private static final AxisAlignedBB INTERACTION_STRAIGHT_NORTH = new AxisAlignedBB(0.0D, 0.5D, 0.25D, 1.0D, 1.0D, 1.0D);
	private static final AxisAlignedBB INTERACTION_INNER_CORNER_NORTH = new AxisAlignedBB(0.0D, 0.5D, 0.0D, 1.0D, 1.0D, 1.0D);
	private static final AxisAlignedBB INTERACTION_OUTER_CORNER_NORTH = new AxisAlignedBB(0.25D, 0.5D, 0.25D, 1.0D, 1.0D, 1.0D);
	private static final AxisAlignedBB BRACKET_BACK_NORTH = new AxisAlignedBB(0.4375D, 0.0625D, 0.875D, 0.5625D, 0.625D, 1.0D);
	private static final AxisAlignedBB BRACKET_LOW_NORTH = new AxisAlignedBB(0.375D, 0.0625D, 0.875D, 0.625D, 0.1875D, 1.0D);
	private static final AxisAlignedBB BRACKET_STEP_ONE_NORTH = new AxisAlignedBB(0.4375D, 0.4375D, 0.6875D, 0.5625D, 0.5625D, 0.875D);
	private static final AxisAlignedBB BRACKET_STEP_TWO_NORTH = new AxisAlignedBB(0.4375D, 0.3125D, 0.5D, 0.5625D, 0.4375D, 0.6875D);
	private static final AxisAlignedBB BRACKET_STEP_THREE_NORTH = new AxisAlignedBB(0.4375D, 0.1875D, 0.3125D, 0.5625D, 0.3125D, 0.5D);

	private enum ShelfShape {
		STRAIGHT,
		INNER_LEFT,
		INNER_RIGHT,
		OUTER_LEFT,
		OUTER_RIGHT
	}

	public static enum ShelfSupport implements IStringSerializable {
		STRAIGHT("straight"),
		STRAIGHT_SUPPORT("straight_support"),
		INNER_CORNER("inner_corner"),
		OUTER_CORNER("outer_corner");

		private final String name;

		private ShelfSupport(String name) {
			this.name = name;
		}

		@Override
		public String getName() {
			return this.name;
		}

		@Override
		public String toString() {
			return this.name;
		}
	}

	private static class ShelfRenderState {
		private final EnumFacing facing;
		private final ShelfSupport support;

		private ShelfRenderState(EnumFacing facing, ShelfSupport support) {
			this.facing = facing;
			this.support = support;
		}
	}

	public WallShelf(Material materialIn, String name, float resistance, float hardness) {
		super(materialIn);
		this.blockResistance = resistance;
		this.blockHardness = hardness;
		this.setSoundType(SoundType.WOOD);
		this.setHarvestLevel("axe", 0);
		this.setCreativeTab(Ironagefurniture.ironagefurnitureTab);
		this.setDefaultState(this.blockState.getBaseState()
			.withProperty(FACING, EnumFacing.NORTH)
			.withProperty(SUPPORT, ShelfSupport.STRAIGHT_SUPPORT));
	}

	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
		for (EnumFacing facing : FACING.getAllowedValues()) {
			if (this.canShelfStay(worldIn, pos, facing)) {
				return super.canPlaceBlockAt(worldIn, pos);
			}
		}

		return false;
	}

	@Override
	public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side) {
		return side.getAxis().isHorizontal() && this.canShelfStay(worldIn, pos, side)
			&& super.canPlaceBlockOnSide(worldIn, pos, side);
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing side, float hitX, float hitY,
			float hitZ, int meta, EntityLivingBase placer, ItemStack stack) {
		EnumFacing facing = side.getAxis().isHorizontal() ? side : EnumFacing.NORTH;

		if (!this.canShelfStay(world, pos, facing)) {
			for (EnumFacing candidate : FACING.getAllowedValues()) {
				if (this.canAttachTo(world, pos, candidate)) {
					facing = candidate;
					break;
				}
			}
		}

		return this.getDefaultState().withProperty(FACING, facing)
			.withProperty(SUPPORT, this.getRenderState(world, pos, facing).support);
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		ShelfRenderState renderState = this.getRenderState(worldIn, pos, state.getValue(FACING));
		return state.withProperty(FACING, renderState.facing).withProperty(SUPPORT, renderState.support);
	}

	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
		super.onBlockAdded(worldIn, pos, state);
		this.notifyShelfAndNeighbors(worldIn, pos);
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn) {
		if (!this.canShelfStay(worldIn, pos, state.getValue(FACING))) {
			if (!worldIn.isRemote) {
				this.dropBlockAsItem(worldIn, pos, state, 0);
			}

			worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
			return;
		}

		this.notifyShelfAndNeighbors(worldIn, pos);
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		if (!worldIn.isRemote) {
			TileEntity tileEntity = worldIn.getTileEntity(pos);

			if (tileEntity instanceof TileEntityWallShelf) {
				((TileEntityWallShelf)tileEntity).dropDisplayedItem(worldIn, pos);
			}
		}

		super.breakBlock(worldIn, pos, state);
		this.notifyShelfNeighbors(worldIn, pos);
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (this.isWallShelfItem(heldItem)) {
			if (this.tryPlaceShelfFromShelfClick(worldIn, pos, state, playerIn, hand, heldItem, side,
					hitX, hitY, hitZ)) {
				return true;
			}

			return false;
		}

		if (playerIn.isSneaking()) {
			return false;
		}

		TileEntity tileEntity = worldIn.getTileEntity(pos);

		if (!(tileEntity instanceof TileEntityWallShelf)) {
			return true;
		}

		TileEntityWallShelf shelf = (TileEntityWallShelf)tileEntity;

		if (worldIn.isRemote) {
			return true;
		}

		if (!shelf.hasDisplayedItem() && heldItem != null && heldItem.stackSize > 0) {
			ItemStack displayedItem = heldItem.copy();
			displayedItem.stackSize = 1;
			shelf.setDisplayedItem(displayedItem);

			if (!playerIn.capabilities.isCreativeMode) {
				heldItem.stackSize--;

				if (heldItem.stackSize <= 0) {
					playerIn.setHeldItem(hand, null);
				}
			}

			return true;
		}

		if (shelf.hasDisplayedItem() && (heldItem == null || heldItem.stackSize <= 0)) {
			ItemStack displayedItem = shelf.removeDisplayedItem();

			if (displayedItem != null) {
				if (!playerIn.inventory.addItemStackToInventory(displayedItem)) {
					EntityItem entityItem = new EntityItem(worldIn, pos.getX() + 0.5D, pos.getY() + 0.9D,
						pos.getZ() + 0.5D, displayedItem);
					worldIn.spawnEntity(entityItem);
				}
			}

			return true;
		}

		return true;
	}

	private boolean isWallShelfItem(ItemStack heldItem) {
		return heldItem != null && heldItem.stackSize > 0
			&& heldItem.getItem() instanceof ItemBlock
			&& ((ItemBlock)heldItem.getItem()).getBlock() instanceof WallShelf;
	}

	public boolean tryPlaceShelfFromShelfClick(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		EnumFacing currentFacing = state.getValue(FACING);
		EnumFacing placementSide = this.getShelfPlacementSide(currentFacing, side, hitX, hitZ);

		if (!placementSide.getAxis().isHorizontal()) {
			return false;
		}

		Block shelfBlock = ((ItemBlock)heldItem.getItem()).getBlock();

		if (this.tryPlaceShelfFromShelfClickSide(worldIn, pos, currentFacing, playerIn, hand, heldItem, placementSide,
				hitX, hitY, hitZ, shelfBlock)) {
			return true;
		}

		if (!this.isShelfLateralSide(currentFacing, side)) {
			EnumFacing otherSide = placementSide == this.rotateClockwise(currentFacing)
				? this.rotateCounterClockwise(currentFacing) : this.rotateClockwise(currentFacing);

			if (this.tryPlaceShelfFromShelfClickSide(worldIn, pos, currentFacing, playerIn, hand, heldItem, otherSide,
					hitX, hitY, hitZ, shelfBlock)) {
				return true;
			}
		}

		return false;
	}

	private boolean tryPlaceShelfFromShelfClickSide(World worldIn, BlockPos pos, EnumFacing currentFacing,
			EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing placementSide, float hitX, float hitY,
			float hitZ, Block shelfBlock) {
		if (this.tryPlaceShelf(worldIn, pos.offset(placementSide), currentFacing, shelfBlock, playerIn, hand,
				heldItem, hitX, hitY, hitZ)) {
			return true;
		}

		EnumFacing outerCornerFacing = this.getOuterCornerPlacementFacing(currentFacing, placementSide);

		if (this.tryPlaceShelf(worldIn, pos.offset(placementSide), outerCornerFacing, shelfBlock, playerIn,
				hand, heldItem, hitX, hitY, hitZ)) {
			return true;
		}

		if (this.tryPlaceShelf(worldIn, pos.offset(placementSide).offset(currentFacing.getOpposite()),
				placementSide, shelfBlock, playerIn, hand, heldItem, hitX, hitY, hitZ)) {
			return true;
		}

		return this.tryPlaceShelf(worldIn, pos.offset(placementSide).offset(currentFacing),
			placementSide.getOpposite(), shelfBlock, playerIn, hand, heldItem, hitX, hitY, hitZ);
	}

	private EnumFacing getShelfPlacementSide(EnumFacing shelfFacing, EnumFacing clickedSide, float hitX, float hitZ) {
		if (this.isShelfLateralSide(shelfFacing, clickedSide)) {
			return clickedSide;
		}

		EnumFacing right = this.rotateClockwise(shelfFacing);
		double lateralOffset = (hitX - 0.5F) * right.getFrontOffsetX()
			+ (hitZ - 0.5F) * right.getFrontOffsetZ();

		if (lateralOffset >= 0.0D) {
			return right;
		}

		return this.rotateCounterClockwise(shelfFacing);
	}

	private boolean isShelfLateralSide(EnumFacing shelfFacing, EnumFacing clickedSide) {
		return clickedSide == this.rotateClockwise(shelfFacing)
			|| clickedSide == this.rotateCounterClockwise(shelfFacing);
	}

	private EnumFacing getOuterCornerPlacementFacing(EnumFacing shelfFacing, EnumFacing placementSide) {
		if (placementSide == this.rotateCounterClockwise(shelfFacing)) {
			return this.rotateClockwise(shelfFacing);
		}

		if (placementSide == this.rotateClockwise(shelfFacing)) {
			return shelfFacing.getOpposite();
		}

		return placementSide.getOpposite();
	}

	private boolean tryPlaceShelf(World worldIn, BlockPos placePos, EnumFacing shelfFacing, Block shelfBlock,
			EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, float hitX, float hitY, float hitZ) {
		if (!(shelfBlock instanceof WallShelf)
				|| !worldIn.getBlockState(placePos).getBlock().isReplaceable(worldIn, placePos)
				|| !((WallShelf)shelfBlock).canShelfStay(worldIn, placePos, shelfFacing)
				|| !playerIn.canPlayerEdit(placePos, shelfFacing, heldItem)) {
			return false;
		}

		if (worldIn.isRemote) {
			return true;
		}

		IBlockState placedState = shelfBlock.getStateForPlacement(worldIn, placePos, shelfFacing, hitX, hitY, hitZ,
			heldItem.getMetadata(), playerIn, heldItem);

		if (placedState == null || !worldIn.setBlockState(placePos, placedState, 3)) {
			return false;
		}

		IBlockState actualState = worldIn.getBlockState(placePos);

		if (actualState.getBlock() == shelfBlock) {
			ItemBlock.setTileEntityNBT(worldIn, playerIn, placePos, heldItem);
			shelfBlock.onBlockPlacedBy(worldIn, placePos, actualState, playerIn, heldItem);
		}

		SoundType soundType = shelfBlock.getSoundType(actualState, worldIn, placePos, playerIn);
		worldIn.playSound(playerIn, placePos, soundType.getPlaceSound(), SoundCategory.BLOCKS,
			(soundType.getVolume() + 1.0F) / 2.0F, soundType.getPitch() * 0.8F);

		if (!playerIn.capabilities.isCreativeMode) {
			heldItem.stackSize--;

			if (heldItem.stackSize <= 0) {
				playerIn.setHeldItem(hand, null);
			}
		}

		return true;
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileEntityWallShelf();
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return this.getInteractionBox(state, source, pos);
	}

	@Override
	public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World worldIn, BlockPos pos) {
		return this.getInteractionBox(state, worldIn, pos).offset(pos);
	}

	@Override
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox,
			List<AxisAlignedBB> collidingBoxes, Entity entityIn) {
		ShelfRenderState renderState = this.getRenderState(worldIn, pos, state.getValue(FACING));
		super.addCollisionBoxToList(pos, entityBox, collidingBoxes, this.getBoardBox(renderState));

		if (renderState.support != ShelfSupport.STRAIGHT && renderState.support != ShelfSupport.OUTER_CORNER) {
			this.addRotatedCollisionBox(pos, entityBox, collidingBoxes, BRACKET_BACK_NORTH, renderState.facing);
			this.addRotatedCollisionBox(pos, entityBox, collidingBoxes, BRACKET_LOW_NORTH, renderState.facing);
			this.addRotatedCollisionBox(pos, entityBox, collidingBoxes, BRACKET_STEP_ONE_NORTH, renderState.facing);
			this.addRotatedCollisionBox(pos, entityBox, collidingBoxes, BRACKET_STEP_TWO_NORTH, renderState.facing);
			this.addRotatedCollisionBox(pos, entityBox, collidingBoxes, BRACKET_STEP_THREE_NORTH, renderState.facing);
		}
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta & 3));
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(FACING).getHorizontalIndex();
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { FACING, SUPPORT });
	}

	private boolean canAttachTo(IBlockAccess worldIn, BlockPos pos, EnumFacing facing) {
		BlockPos supportPos = pos.offset(facing.getOpposite());
		return worldIn.getBlockState(supportPos).isSideSolid(worldIn, supportPos, facing);
	}

	private boolean canShelfStay(IBlockAccess worldIn, BlockPos pos, EnumFacing facing) {
		return this.canAttachTo(worldIn, pos, facing) || this.isShelfSupportedOutsideCorner(worldIn, pos, facing);
	}

	private ShelfRenderState getRenderState(IBlockAccess worldIn, BlockPos pos, EnumFacing facing) {
		ShelfShape shape = this.getShelfShape(worldIn, pos, facing);

		switch (shape) {
		case OUTER_LEFT:
			return new ShelfRenderState(this.canAttachTo(worldIn, pos, facing)
				? facing : facing.getOpposite(), ShelfSupport.OUTER_CORNER);
		case OUTER_RIGHT:
			return new ShelfRenderState(this.canAttachTo(worldIn, pos, facing)
				? this.rotateClockwise(facing) : this.rotateCounterClockwise(facing), ShelfSupport.OUTER_CORNER);
		case INNER_LEFT:
			return new ShelfRenderState(facing, ShelfSupport.INNER_CORNER);
		case INNER_RIGHT:
			return new ShelfRenderState(this.rotateClockwise(facing), ShelfSupport.INNER_CORNER);
		default:
			return new ShelfRenderState(facing, this.hasStraightSupport(worldIn, pos, facing)
				? ShelfSupport.STRAIGHT_SUPPORT : ShelfSupport.STRAIGHT);
		}
	}

	private ShelfShape getShelfShape(IBlockAccess worldIn, BlockPos pos, EnumFacing facing) {
		if (!this.canAttachTo(worldIn, pos, facing)) {
			return this.getShelfSupportedOutsideCornerShape(worldIn, pos, facing);
		}

		IBlockState frontState = worldIn.getBlockState(pos.offset(facing));

		if (this.isWallSupportedShelf(frontState, worldIn, pos.offset(facing))) {
			EnumFacing frontFacing = frontState.getValue(FACING);

			if (frontFacing.getAxis() != facing.getAxis()
					&& this.isDifferentShelf(worldIn, pos, facing, frontFacing.getOpposite())) {
				if (this.canAttachTo(worldIn, pos, frontFacing)) {
					return frontFacing == this.rotateCounterClockwise(facing)
						? ShelfShape.INNER_LEFT : ShelfShape.INNER_RIGHT;
				}
			}
		}

		IBlockState backState = worldIn.getBlockState(pos.offset(facing.getOpposite()));

		if (this.isWallSupportedShelf(backState, worldIn, pos.offset(facing.getOpposite()))) {
			EnumFacing backFacing = backState.getValue(FACING);

			if (backFacing.getAxis() != facing.getAxis()
					&& this.isDifferentShelf(worldIn, pos, facing, backFacing)) {
				return backFacing == this.rotateCounterClockwise(facing)
					? ShelfShape.INNER_LEFT : ShelfShape.INNER_RIGHT;
			}
		}

		return ShelfShape.STRAIGHT;
	}

	private boolean isShelfSupportedOutsideCorner(IBlockAccess worldIn, BlockPos pos, EnumFacing facing) {
		return this.getShelfSupportedOutsideCornerShape(worldIn, pos, facing) != ShelfShape.STRAIGHT;
	}

	private ShelfShape getShelfSupportedOutsideCornerShape(IBlockAccess worldIn, BlockPos pos, EnumFacing facing) {
		IBlockState frontState = worldIn.getBlockState(pos.offset(facing));

		if (this.isWallSupportedShelf(frontState, worldIn, pos.offset(facing))) {
			EnumFacing frontFacing = frontState.getValue(FACING);

			if (frontFacing.getAxis() != facing.getAxis()
					&& !this.canAttachTo(worldIn, pos, frontFacing)
					&& this.isDifferentShelf(worldIn, pos, facing, frontFacing.getOpposite())) {
				return frontFacing == this.rotateCounterClockwise(facing)
					? ShelfShape.OUTER_LEFT : ShelfShape.OUTER_RIGHT;
			}
		}

		return ShelfShape.STRAIGHT;
	}

	private boolean isWallSupportedShelf(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		return state.getBlock() == this && this.canAttachTo(worldIn, pos, state.getValue(FACING));
	}

	private boolean isWallSupportedShelfFacing(IBlockAccess worldIn, BlockPos pos, EnumFacing facing) {
		IBlockState state = worldIn.getBlockState(pos);
		return state.getBlock() == this && state.getValue(FACING) == facing && this.canAttachTo(worldIn, pos, facing);
	}

	private boolean isDifferentShelf(IBlockAccess worldIn, BlockPos pos, EnumFacing facing, EnumFacing offset) {
		return !this.isWallSupportedShelfFacing(worldIn, pos.offset(offset), facing);
	}

	private boolean hasStraightSupport(IBlockAccess worldIn, BlockPos pos, EnumFacing facing) {
		boolean hasLeft = this.isWallSupportedShelfFacing(worldIn, pos.offset(this.rotateCounterClockwise(facing)),
			facing);
		boolean hasRight = this.isWallSupportedShelfFacing(worldIn, pos.offset(this.rotateClockwise(facing)), facing);

		if (!hasLeft || !hasRight) {
			return true;
		}

		int runCoordinate = facing.getAxis() == EnumFacing.Axis.Z ? pos.getX() : pos.getZ();
		return Math.floorMod(runCoordinate, 3) == 0;
	}

	private AxisAlignedBB getInteractionBox(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		ShelfRenderState renderState = this.getRenderState(worldIn, pos, state.getValue(FACING));
		return this.getInteractionBox(renderState);
	}

	private AxisAlignedBB getInteractionBox(ShelfRenderState renderState) {
		switch (renderState.support) {
		case INNER_CORNER:
			return this.rotateToFacing(INTERACTION_INNER_CORNER_NORTH, renderState.facing);
		case OUTER_CORNER:
			return this.rotateToFacing(INTERACTION_OUTER_CORNER_NORTH, renderState.facing.getOpposite());
		default:
			return this.rotateToFacing(INTERACTION_STRAIGHT_NORTH, renderState.facing);
		}
	}

	private AxisAlignedBB getBoardBox(ShelfRenderState renderState) {
		switch (renderState.support) {
		case INNER_CORNER:
			return this.rotateToFacing(BOARD_INNER_CORNER_NORTH, renderState.facing);
		case OUTER_CORNER:
			return this.rotateToFacing(BOARD_OUTER_CORNER_NORTH, renderState.facing.getOpposite());
		default:
			return this.rotateToFacing(BOARD_STRAIGHT_NORTH, renderState.facing);
		}
	}

	private void notifyShelfAndNeighbors(World worldIn, BlockPos pos) {
		this.notifyShelf(worldIn, pos);
		this.notifyShelfNeighbors(worldIn, pos);
	}

	private void notifyShelfNeighbors(World worldIn, BlockPos pos) {
		for (EnumFacing direction : EnumFacing.Plane.HORIZONTAL) {
			this.notifyShelf(worldIn, pos.offset(direction));
			this.notifyShelf(worldIn, pos.offset(direction).offset(this.rotateClockwise(direction)));
			this.notifyShelf(worldIn, pos.offset(direction).offset(this.rotateCounterClockwise(direction)));
		}
	}

	private void notifyShelf(World worldIn, BlockPos pos) {
		if (worldIn.isRemote) {
			return;
		}

		IBlockState state = worldIn.getBlockState(pos);

		if (state.getBlock() == this) {
			worldIn.notifyBlockUpdate(pos, state, state, 3);
		}
	}

	private void addRotatedCollisionBox(BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes,
			AxisAlignedBB box, EnumFacing facing) {
		super.addCollisionBoxToList(pos, entityBox, collidingBoxes, this.rotateToFacing(box, facing));
	}

	private AxisAlignedBB rotateToFacing(AxisAlignedBB box, EnumFacing facing) {
		switch (facing) {
		case EAST:
			return this.rotateClockwise(box);
		case SOUTH:
			return this.rotateHalfTurn(box);
		case WEST:
			return this.rotateCounterClockwise(box);
		default:
			return box;
		}
	}

	private AxisAlignedBB rotateClockwise(AxisAlignedBB box) {
		return new AxisAlignedBB(1.0D - box.maxZ, box.minY, box.minX,
			1.0D - box.minZ, box.maxY, box.maxX);
	}

	private AxisAlignedBB rotateHalfTurn(AxisAlignedBB box) {
		return new AxisAlignedBB(1.0D - box.maxX, box.minY, 1.0D - box.maxZ,
			1.0D - box.minX, box.maxY, 1.0D - box.minZ);
	}

	private AxisAlignedBB rotateCounterClockwise(AxisAlignedBB box) {
		return new AxisAlignedBB(box.minZ, box.minY, 1.0D - box.maxX,
			box.maxZ, box.maxY, 1.0D - box.minX);
	}

	private EnumFacing rotateClockwise(EnumFacing facing) {
		switch (facing) {
		case NORTH:
			return EnumFacing.EAST;
		case EAST:
			return EnumFacing.SOUTH;
		case SOUTH:
			return EnumFacing.WEST;
		case WEST:
			return EnumFacing.NORTH;
		default:
			return facing;
		}
	}

	private EnumFacing rotateCounterClockwise(EnumFacing facing) {
		switch (facing) {
		case NORTH:
			return EnumFacing.WEST;
		case WEST:
			return EnumFacing.SOUTH;
		case SOUTH:
			return EnumFacing.EAST;
		case EAST:
			return EnumFacing.NORTH;
		default:
			return facing;
		}
	}
}
