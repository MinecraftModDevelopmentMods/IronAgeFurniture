package com.mcmoddev.ironagefurniture.api.Blocks;

import java.util.List;

import com.mcmoddev.ironagefurniture.Ironagefurniture;
import com.mcmoddev.ironagefurniture.api.tile.TileEntityWallShelf;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
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
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class WallShelf extends BlockHBase {
	public static final PropertyInteger CONNECTIONS = PropertyInteger.create("connections", 0, 15);
	public static final PropertyBool SUPPORT = PropertyBool.create("support");

	private static final int LEFT = 1;
	private static final int RIGHT = 2;
	private static final int LEFT_CORNER = 4;
	private static final int RIGHT_CORNER = 8;

	private static final AxisAlignedBB BOARD_NORTH = new AxisAlignedBB(0.0D, 0.625D, 0.25D, 1.0D, 0.8125D, 1.0D);
	private static final AxisAlignedBB INTERACTION_NORTH = new AxisAlignedBB(0.0D, 0.5D, 0.25D, 1.0D, 1.0D, 1.0D);
	private static final AxisAlignedBB BRACKET_BACK_NORTH = new AxisAlignedBB(0.4375D, 0.0625D, 0.875D, 0.5625D, 0.625D, 1.0D);
	private static final AxisAlignedBB BRACKET_LOW_NORTH = new AxisAlignedBB(0.375D, 0.0625D, 0.875D, 0.625D, 0.1875D, 1.0D);
	private static final AxisAlignedBB BRACKET_STEP_ONE_NORTH = new AxisAlignedBB(0.4375D, 0.4375D, 0.6875D, 0.5625D, 0.5625D, 0.875D);
	private static final AxisAlignedBB BRACKET_STEP_TWO_NORTH = new AxisAlignedBB(0.4375D, 0.3125D, 0.5D, 0.5625D, 0.4375D, 0.6875D);
	private static final AxisAlignedBB BRACKET_STEP_THREE_NORTH = new AxisAlignedBB(0.4375D, 0.1875D, 0.3125D, 0.5625D, 0.3125D, 0.5D);

	public WallShelf(Material materialIn, String name, float resistance, float hardness) {
		super(materialIn);
		this.blockResistance = resistance;
		this.blockHardness = hardness;
		this.setSoundType(SoundType.WOOD);
		this.setHarvestLevel("axe", 0);
		this.setCreativeTab(Ironagefurniture.ironagefurnitureTab);
		this.setDefaultState(this.blockState.getBaseState()
			.withProperty(FACING, EnumFacing.NORTH)
			.withProperty(CONNECTIONS, Integer.valueOf(0))
			.withProperty(SUPPORT, Boolean.valueOf(true)));
	}

	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
		for (EnumFacing facing : FACING.getAllowedValues()) {
			if (this.canAttachTo(worldIn, pos, facing)) {
				return super.canPlaceBlockAt(worldIn, pos);
			}
		}

		return false;
	}

	@Override
	public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side) {
		return side.getAxis().isHorizontal() && this.canAttachTo(worldIn, pos, side)
			&& super.canPlaceBlockOnSide(worldIn, pos, side);
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing side, float hitX, float hitY,
			float hitZ, int meta, EntityLivingBase placer, ItemStack stack) {
		EnumFacing facing = side.getAxis().isHorizontal() ? side : EnumFacing.NORTH;

		if (!this.canAttachTo(world, pos, facing)) {
			for (EnumFacing candidate : FACING.getAllowedValues()) {
				if (this.canAttachTo(world, pos, candidate)) {
					facing = candidate;
					break;
				}
			}
		}

		return this.getDefaultState().withProperty(FACING, facing)
			.withProperty(CONNECTIONS, Integer.valueOf(this.getConnectionMask(world, pos, facing)))
			.withProperty(SUPPORT, Boolean.valueOf(this.hasVisibleBracket(world, pos, facing)));
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		EnumFacing facing = state.getValue(FACING);
		int connections = this.getConnectionMask(worldIn, pos, facing);
		boolean shelfSupportedCorner = this.hasShelfCornerSupport(worldIn, pos, facing, this)
			&& !this.canAttachTo(worldIn, pos, facing);

		return state.withProperty(CONNECTIONS, Integer.valueOf(connections))
			.withProperty(SUPPORT, Boolean.valueOf(!shelfSupportedCorner
				&& this.hasVisibleBracket(worldIn, pos, facing, connections)));
	}

	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
		super.onBlockAdded(worldIn, pos, state);
		this.notifyShelfAndNeighbors(worldIn, pos);
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn) {
		if (!this.canStayAt(worldIn, pos, state.getValue(FACING))) {
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
		BlockPos sidePos = pos.offset(placementSide);

		if (this.isShelfFacing(worldIn, sidePos.offset(currentFacing.getOpposite()), placementSide, shelfBlock)
				&& this.tryPlaceShelfCorner(worldIn, sidePos, currentFacing, shelfBlock, playerIn, hand, heldItem,
						placementSide, hitX, hitY, hitZ)) {
			return true;
		}

		if (this.tryPlaceShelf(worldIn, sidePos, currentFacing, shelfBlock, playerIn, hand, heldItem,
				placementSide, hitX, hitY, hitZ)) {
			return true;
		}

		return false;
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

	private boolean tryPlaceShelf(World worldIn, BlockPos placePos, EnumFacing shelfFacing, Block shelfBlock,
			EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing clickedSide, float hitX, float hitY,
			float hitZ) {
		if (!(shelfBlock instanceof WallShelf)
				|| !worldIn.getBlockState(placePos).getBlock().isReplaceable(worldIn, placePos)
				|| !((WallShelf)shelfBlock).canAttachTo(worldIn, placePos, shelfFacing)
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

	private boolean tryPlaceShelfCorner(World worldIn, BlockPos placePos, EnumFacing shelfFacing, Block shelfBlock,
			EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing clickedSide, float hitX, float hitY,
			float hitZ) {
		if (!(shelfBlock instanceof WallShelf)
				|| !worldIn.getBlockState(placePos).getBlock().isReplaceable(worldIn, placePos)
				|| !this.hasShelfCornerSupport(worldIn, placePos, shelfFacing, shelfBlock)
				|| !playerIn.canPlayerEdit(placePos, shelfFacing, heldItem)) {
			return false;
		}

		if (worldIn.isRemote) {
			return true;
		}

		WallShelf shelf = (WallShelf)shelfBlock;
		IBlockState placedState = shelfBlock.getDefaultState().withProperty(FACING, shelfFacing)
			.withProperty(CONNECTIONS, Integer.valueOf(shelf.getConnectionMask(worldIn, placePos, shelfFacing)))
			.withProperty(SUPPORT, Boolean.valueOf(shelf.hasVisibleBracket(worldIn, placePos, shelfFacing)));

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
		super.addCollisionBoxToList(pos, entityBox, collidingBoxes, this.getBoardBox(state, worldIn, pos));

		EnumFacing facing = state.getValue(FACING);
		int connections = this.getConnectionMask(worldIn, pos, facing);

		if (this.hasVisibleBracket(worldIn, pos, facing, connections)) {
			this.addRotatedCollisionBox(pos, entityBox, collidingBoxes, BRACKET_BACK_NORTH, facing);
			this.addRotatedCollisionBox(pos, entityBox, collidingBoxes, BRACKET_LOW_NORTH, facing);
			this.addRotatedCollisionBox(pos, entityBox, collidingBoxes, BRACKET_STEP_ONE_NORTH, facing);
			this.addRotatedCollisionBox(pos, entityBox, collidingBoxes, BRACKET_STEP_TWO_NORTH, facing);
			this.addRotatedCollisionBox(pos, entityBox, collidingBoxes, BRACKET_STEP_THREE_NORTH, facing);
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
		return new BlockStateContainer(this, new IProperty[] { FACING, CONNECTIONS, SUPPORT });
	}

	private boolean canAttachTo(IBlockAccess worldIn, BlockPos pos, EnumFacing facing) {
		BlockPos supportPos = pos.offset(facing.getOpposite());
		return worldIn.getBlockState(supportPos).isSideSolid(worldIn, supportPos, facing);
	}

	private boolean canStayAt(World worldIn, BlockPos pos, EnumFacing facing) {
		return this.canAttachTo(worldIn, pos, facing)
			|| this.hasShelfCornerSupport(worldIn, pos, facing, this);
	}

	private boolean hasShelfCornerSupport(IBlockAccess worldIn, BlockPos pos, EnumFacing facing, Block shelfBlock) {
		BlockPos behindPos = pos.offset(facing.getOpposite());
		IBlockState behindState = worldIn.getBlockState(behindPos);

		if (behindState.getBlock() != shelfBlock) {
			return false;
		}

		EnumFacing behindFacing = behindState.getValue(FACING);

		if (!behindFacing.getAxis().isHorizontal()
				|| behindFacing == facing || behindFacing == facing.getOpposite()) {
			return false;
		}

		return this.isShelfFacing(worldIn, pos.offset(behindFacing.getOpposite()), facing, shelfBlock);
	}

	private int getShelfSupportedCornerMask(IBlockAccess worldIn, BlockPos pos, EnumFacing facing) {
		BlockPos behindPos = pos.offset(facing.getOpposite());
		IBlockState behindState = worldIn.getBlockState(behindPos);

		if (behindState.getBlock() != this) {
			return 0;
		}

		EnumFacing behindFacing = behindState.getValue(FACING);

		if (!this.hasShelfCornerSupport(worldIn, pos, facing, this)) {
			return 0;
		}

		if (behindFacing == this.rotateCounterClockwise(facing)) {
			return LEFT_CORNER;
		}
		if (behindFacing == this.rotateClockwise(facing)) {
			return RIGHT_CORNER;
		}

		return 0;
	}

	private AxisAlignedBB getInteractionBox(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		EnumFacing facing = state.getValue(FACING);
		int connections = this.getConnectionMask(worldIn, pos, facing);
		boolean shelfSupportedCorner = this.hasShelfCornerSupport(worldIn, pos, facing, this)
			&& !this.canAttachTo(worldIn, pos, facing);

		if (shelfSupportedCorner
				&& (connections == (RIGHT | LEFT_CORNER) || connections == (LEFT | RIGHT_CORNER))) {
			return this.rotateToFacing(new AxisAlignedBB(0.0D, INTERACTION_NORTH.minY, 0.0D,
				1.0D, INTERACTION_NORTH.maxY, 1.0D), facing);
		}

		return this.rotateToFacing(INTERACTION_NORTH, facing);
	}

	private AxisAlignedBB getBoardBox(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		EnumFacing facing = state.getValue(FACING);
		int connections = this.getConnectionMask(worldIn, pos, facing);
		boolean shelfSupportedCorner = this.hasShelfCornerSupport(worldIn, pos, facing, this)
			&& !this.canAttachTo(worldIn, pos, facing);
		double minX = this.isConnected(connections, LEFT) || this.isConnected(connections, LEFT_CORNER) ? 0.0D : 0.0625D;
		double maxX = this.isConnected(connections, RIGHT) || this.isConnected(connections, RIGHT_CORNER) ? 1.0D : 0.9375D;
		double minZ = BOARD_NORTH.minZ;

		if (shelfSupportedCorner
				&& (connections == (RIGHT | LEFT_CORNER) || connections == (LEFT | RIGHT_CORNER))) {
			minX = 0.0D;
			maxX = 1.0D;
			minZ = 0.0D;
		}

		AxisAlignedBB board = new AxisAlignedBB(minX, BOARD_NORTH.minY, minZ,
			maxX, BOARD_NORTH.maxY, BOARD_NORTH.maxZ);

		return this.rotateToFacing(board, facing);
	}

	private int getConnectionMask(IBlockAccess worldIn, BlockPos pos, EnumFacing facing) {
		int connections = 0;
		EnumFacing left = this.rotateCounterClockwise(facing);
		EnumFacing right = this.rotateClockwise(facing);

		if (this.isShelfFacing(worldIn, pos.offset(left), facing)) {
			connections |= LEFT;
		}
		if (this.isShelfFacing(worldIn, pos.offset(right), facing)) {
			connections |= RIGHT;
		}
		if (this.hasCornerConnection(worldIn, pos, facing, left)) {
			connections |= LEFT_CORNER;
		}
		if (this.hasCornerConnection(worldIn, pos, facing, right)) {
			connections |= RIGHT_CORNER;
		}

		connections |= this.getShelfSupportedCornerMask(worldIn, pos, facing);

		return connections;
	}

	private boolean hasCornerConnection(IBlockAccess worldIn, BlockPos pos, EnumFacing facing, EnumFacing side) {
		return this.isShelfFacing(worldIn, pos.offset(side), side)
			|| this.isShelfFacing(worldIn, pos.offset(side).offset(facing), side)
			|| this.isShelfFacing(worldIn, pos.offset(side).offset(facing.getOpposite()), side);
	}

	private boolean isShelfFacing(IBlockAccess worldIn, BlockPos pos, EnumFacing facing) {
		return this.isShelfFacing(worldIn, pos, facing, this);
	}

	private boolean isShelfFacing(IBlockAccess worldIn, BlockPos pos, EnumFacing facing, Block shelfBlock) {
		IBlockState state = worldIn.getBlockState(pos);
		return state.getBlock() == shelfBlock && state.getValue(FACING) == facing;
	}

	private boolean hasVisibleBracket(IBlockAccess worldIn, BlockPos pos, EnumFacing facing) {
		return this.hasVisibleBracket(worldIn, pos, facing, this.getConnectionMask(worldIn, pos, facing));
	}

	private boolean hasVisibleBracket(IBlockAccess worldIn, BlockPos pos, EnumFacing facing, int connections) {
		boolean hasLeft = this.isConnected(connections, LEFT) || this.isConnected(connections, LEFT_CORNER);
		boolean hasRight = this.isConnected(connections, RIGHT) || this.isConnected(connections, RIGHT_CORNER);

		if (!hasLeft || !hasRight || this.isConnected(connections, LEFT_CORNER)
				|| this.isConnected(connections, RIGHT_CORNER)) {
			return true;
		}

		int runCoordinate = facing.getAxis() == EnumFacing.Axis.Z ? pos.getX() : pos.getZ();
		return Math.floorMod(runCoordinate, 3) == 0;
	}

	private boolean isConnected(int connections, int mask) {
		return (connections & mask) != 0;
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
