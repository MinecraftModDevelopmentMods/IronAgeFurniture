package com.mcmoddev.ironagefurniture.api.Blocks;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.mcmoddev.ironagefurniture.BlockObjectHolder;
import com.mcmoddev.ironagefurniture.Ironagefurniture;
import com.mcmoddev.ironagefurniture.api.tile.TileEntityDiningTable;

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
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class DiningTable extends Block {
	public static final PropertyInteger CONNECTIONS = PropertyInteger.create("connections", 0, 15);
	public static final PropertyBool DATA = PropertyBool.create("data");

	private static final int NORTH = 1;
	private static final int EAST = 2;
	private static final int SOUTH = 4;
	private static final int WEST = 8;

	private static final AxisAlignedBB TOP_STANDALONE = new AxisAlignedBB(0.0625D, 0.8125D, 0.0625D, 0.9375D, 1.0D, 0.9375D);
	private static final AxisAlignedBB LEG_NORTH_WEST = new AxisAlignedBB(0.125D, 0.0D, 0.125D, 0.25D, 0.8125D, 0.25D);
	private static final AxisAlignedBB LEG_NORTH_EAST = new AxisAlignedBB(0.75D, 0.0D, 0.125D, 0.875D, 0.8125D, 0.25D);
	private static final AxisAlignedBB LEG_SOUTH_WEST = new AxisAlignedBB(0.125D, 0.0D, 0.75D, 0.25D, 0.8125D, 0.875D);
	private static final AxisAlignedBB LEG_SOUTH_EAST = new AxisAlignedBB(0.75D, 0.0D, 0.75D, 0.875D, 0.8125D, 0.875D);
	private static final Map<UUID, EnumFacing> PENDING_SNEAK_PLACEMENT_SIDES = new HashMap<UUID, EnumFacing>();

	public DiningTable(Material materialIn, String name, float resistance, float hardness) {
		super(materialIn);
		this.blockResistance = resistance;
		this.blockHardness = hardness;
		this.setSoundType(SoundType.WOOD);
		this.setHarvestLevel("axe", 0);
		this.setCreativeTab(Ironagefurniture.ironagefurnitureTab);
		this.setDefaultState(this.blockState.getBaseState()
			.withProperty(CONNECTIONS, Integer.valueOf(0))
			.withProperty(DATA, Boolean.valueOf(false)));
	}

	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
		int connections = this.getConnectionMask(worldIn, pos);
		return super.canPlaceBlockAt(worldIn, pos)
			&& (this.hasSupport(worldIn, pos) || !this.requiresSupport(connections));
	}

	@Override
	public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side) {
		return side == EnumFacing.UP && this.canPlaceBlockAt(worldIn, pos);
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY,
			float hitZ, int meta, EntityLivingBase placer, ItemStack stack) {
		if (placer != null) {
			if (placer.isSneaking() && facing.getAxis().isHorizontal()) {
				PENDING_SNEAK_PLACEMENT_SIDES.put(placer.getUniqueID(), facing);
			} else {
				PENDING_SNEAK_PLACEMENT_SIDES.remove(placer.getUniqueID());
			}
		}

		return this.getDefaultState();
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack) {
		if (!worldIn.isRemote && placer != null) {
			EnumFacing clickedSide = PENDING_SNEAK_PLACEMENT_SIDES.remove(placer.getUniqueID());

			if (clickedSide != null && clickedSide.getAxis().isHorizontal()) {
				this.blockConnectionBetween(worldIn, pos, clickedSide.getOpposite());
			}
		}
	}

	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
		super.onBlockAdded(worldIn, pos, state);

		if (!worldIn.isRemote) {
			this.updateTableAndNeighbors(worldIn, pos);
		}
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn) {
		if (this.requiresSupport(this.getConnectionMask(worldIn, pos)) && !this.hasSupport(worldIn, pos)) {
			this.dropBlockAsItem(worldIn, pos, state, 0);
			worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
			return;
		}

		if (!worldIn.isRemote) {
			this.updateTableAndNeighbors(worldIn, pos);
		}
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		if (!worldIn.isRemote) {
			TileEntity tileEntity = worldIn.getTileEntity(pos);

			if (tileEntity instanceof TileEntityDiningTable) {
				((TileEntityDiningTable)tileEntity).dropDisplayedItem(worldIn, pos);
			}

			SurfaceDisplayBlocker.release(worldIn, pos);
		}

		super.breakBlock(worldIn, pos, state);

		if (!worldIn.isRemote) {
			this.updateHorizontalNeighbors(worldIn, pos);
		}
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (playerIn.isSneaking()) {
			return false;
		}

		boolean canRetrievePlacedBlock = this.canRetrievePlacedBlockAbove(worldIn, pos, playerIn, heldItem);

		if (this.isDisplayExcluded(heldItem) && !canRetrievePlacedBlock) {
			return false;
		}

		if (worldIn.isRemote) {
			return true;
		}

		TileEntityDiningTable table = this.getTableEntity(worldIn, pos, false);

		if (table != null && table.hasDisplayedItem() && this.canRetrieveDisplayedItem(table, heldItem)) {
			ItemStack displayedItem = table.removeDisplayedItem();
			SurfaceDisplayBlocker.release(worldIn, pos);

			if (displayedItem != null) {
				if (!playerIn.inventory.addItemStackToInventory(displayedItem)) {
					EntityItem entityItem = new EntityItem(worldIn, pos.getX() + 0.5D, pos.getY() + 1.1D,
						pos.getZ() + 0.5D, displayedItem);
					worldIn.spawnEntity(entityItem);
				}
			}

			this.removeTableEntityIfEmpty(worldIn, pos);
			return true;
		}

		if ((table == null || !table.hasDisplayedItem()) && canRetrievePlacedBlock
				&& this.tryRetrievePlacedBlockAbove(worldIn, pos, playerIn, heldItem)) {
			return true;
		}

		if ((table == null || !table.hasDisplayedItem()) && heldItem != null && heldItem.stackSize > 0) {
			if (!SurfaceDisplayBlocker.reserve(worldIn, pos)) {
				return true;
			}

			table = this.getTableEntity(worldIn, pos, true);

			if (table == null) {
				SurfaceDisplayBlocker.release(worldIn, pos);
				return true;
			}

			ItemStack displayedItem = heldItem.copy();
			displayedItem.stackSize = 1;
			table.setDisplayedItem(displayedItem);

			if (!playerIn.capabilities.isCreativeMode) {
				heldItem.stackSize--;

				if (heldItem.stackSize <= 0) {
					playerIn.setHeldItem(hand, null);
				}
			}

			return true;
		}

		return true;
	}

	private boolean isDisplayExcluded(ItemStack heldItem) {
		if (heldItem == null || heldItem.stackSize <= 0) {
			return false;
		}

		return this.isItemFromBlock(heldItem, BlockObjectHolder.light_metal_ironage_block_floor_glow_clear)
			|| this.isItemFromBlock(heldItem, BlockObjectHolder.light_metal_ironage_block_floor_red_clear)
			|| this.isItemFromBlock(heldItem, BlockObjectHolder.light_metal_ironage_block_floor_lava_clear)
			|| this.isItemFromBlock(heldItem, BlockObjectHolder.light_metal_ironage_candle_floor);
	}

	private boolean isItemFromBlock(ItemStack heldItem, Block block) {
		return block != null && heldItem.getItem() == Item.getItemFromBlock(block);
	}

	private boolean canRetrieveDisplayedItem(TileEntityDiningTable table, ItemStack heldItem) {
		if (heldItem == null || heldItem.stackSize <= 0) {
			return true;
		}

		ItemStack displayedItem = table.getDisplayedItem();
		return displayedItem != null && displayedItem.stackSize > 0
			&& this.isSameItemStack(displayedItem, heldItem);
	}

	private boolean canRetrievePlacedBlockAbove(World worldIn, BlockPos pos, EntityPlayer playerIn, ItemStack heldItem) {
		ItemStack placedBlockItem = this.getPlacedBlockItem(worldIn, pos, playerIn);

		if (placedBlockItem == null || placedBlockItem.stackSize <= 0) {
			return false;
		}

		return heldItem == null || heldItem.stackSize <= 0
			|| this.isSameItemStack(placedBlockItem, heldItem);
	}

	public boolean tryRetrievePlacedBlockAbove(World worldIn, BlockPos pos, EntityPlayer playerIn, ItemStack heldItem) {
		TileEntityDiningTable table = this.getTableEntity(worldIn, pos, false);

		if (table != null && table.hasDisplayedItem()) {
			return false;
		}

		if (!this.canRetrievePlacedBlockAbove(worldIn, pos, playerIn, heldItem)) {
			return false;
		}

		if (worldIn.isRemote) {
			return true;
		}

		if (this.retrievePlacedBlockAbove(worldIn, pos, playerIn, heldItem)) {
			this.removeTableEntityIfEmpty(worldIn, pos);
			return true;
		}

		return false;
	}

	private boolean retrievePlacedBlockAbove(World worldIn, BlockPos pos, EntityPlayer playerIn, ItemStack heldItem) {
		BlockPos abovePos = pos.up();
		ItemStack placedBlockItem = this.getPlacedBlockItem(worldIn, pos, playerIn);

		if (placedBlockItem == null || placedBlockItem.stackSize <= 0
				|| (heldItem != null && heldItem.stackSize > 0 && !this.isSameItemStack(placedBlockItem, heldItem))) {
			return false;
		}

		if (!worldIn.setBlockState(abovePos, Blocks.AIR.getDefaultState(), 3)) {
			return false;
		}

		if (!playerIn.inventory.addItemStackToInventory(placedBlockItem)) {
			EntityItem entityItem = new EntityItem(worldIn, pos.getX() + 0.5D, pos.getY() + 1.1D,
				pos.getZ() + 0.5D, placedBlockItem);
			worldIn.spawnEntity(entityItem);
		}

		return true;
	}

	private ItemStack getPlacedBlockItem(World worldIn, BlockPos pos, EntityPlayer playerIn) {
		BlockPos abovePos = pos.up();
		IBlockState aboveState = worldIn.getBlockState(abovePos);
		Block aboveBlock = aboveState.getBlock();

		if (aboveBlock == Blocks.AIR || aboveBlock == BlockObjectHolder.surface_display_blocker
				|| aboveBlock.hasTileEntity(aboveState)) {
			return null;
		}

		RayTraceResult target = new RayTraceResult(new Vec3d(abovePos.getX() + 0.5D, abovePos.getY() + 0.5D,
			abovePos.getZ() + 0.5D), EnumFacing.UP, abovePos);
		ItemStack placedBlockItem = aboveBlock.getPickBlock(aboveState, target, worldIn, abovePos, playerIn);

		if (placedBlockItem != null) {
			placedBlockItem = placedBlockItem.copy();
			placedBlockItem.stackSize = 1;
		}

		return placedBlockItem;
	}

	private boolean isSameItemStack(ItemStack storedItem, ItemStack heldItem) {
		return storedItem != null && heldItem != null
			&& storedItem.isItemEqual(heldItem)
			&& ItemStack.areItemStackTagsEqual(storedItem, heldItem);
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return state.getValue(DATA).booleanValue();
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return state.getValue(DATA).booleanValue() ? new TileEntityDiningTable() : null;
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
		return this.getTopBoundingBox(this.getConnectionMask(source, pos));
	}

	@Override
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox,
			List<AxisAlignedBB> collidingBoxes, Entity entityIn) {
		int connections = this.getConnectionMask(worldIn, pos);
		super.addCollisionBoxToList(pos, entityBox, collidingBoxes, this.getTopBoundingBox(connections));

		if (!this.isConnected(connections, NORTH) && !this.isConnected(connections, WEST)) {
			super.addCollisionBoxToList(pos, entityBox, collidingBoxes, LEG_NORTH_WEST);
		}
		if (!this.isConnected(connections, NORTH) && !this.isConnected(connections, EAST)) {
			super.addCollisionBoxToList(pos, entityBox, collidingBoxes, LEG_NORTH_EAST);
		}
		if (!this.isConnected(connections, SOUTH) && !this.isConnected(connections, WEST)) {
			super.addCollisionBoxToList(pos, entityBox, collidingBoxes, LEG_SOUTH_WEST);
		}
		if (!this.isConnected(connections, SOUTH) && !this.isConnected(connections, EAST)) {
			super.addCollisionBoxToList(pos, entityBox, collidingBoxes, LEG_SOUTH_EAST);
		}
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(DATA, Boolean.valueOf((meta & 1) != 0));
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(DATA).booleanValue() ? 1 : 0;
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		return state.withProperty(CONNECTIONS, Integer.valueOf(this.getConnectionMask(worldIn, pos)));
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { CONNECTIONS, DATA });
	}

	private boolean hasSupport(World worldIn, BlockPos pos) {
		BlockPos supportPos = pos.down();
		return worldIn.getBlockState(supportPos).isSideSolid(worldIn, supportPos, EnumFacing.UP);
	}

	private void updateTableAndNeighbors(World worldIn, BlockPos pos) {
		this.updateTableState(worldIn, pos);
		this.updateHorizontalNeighbors(worldIn, pos);
	}

	private void updateHorizontalNeighbors(World worldIn, BlockPos pos) {
		for (EnumFacing facing : EnumFacing.Plane.HORIZONTAL) {
			BlockPos neighborPos = pos.offset(facing);
			IBlockState neighborState = worldIn.getBlockState(neighborPos);

			if (neighborState.getBlock() == this) {
				this.updateTableState(worldIn, neighborPos);
			}
		}
	}

	private void updateTableState(World worldIn, BlockPos pos) {
		IBlockState state = worldIn.getBlockState(pos);

		if (state.getBlock() != this) {
			return;
		}

		this.clearOrphanedBlockedConnections(worldIn, pos);
		int connections = this.getConnectionMask(worldIn, pos);

		if (this.requiresSupport(connections) && !this.hasSupport(worldIn, pos)) {
			this.dropBlockAsItem(worldIn, pos, state, 0);
			worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
			return;
		}

		this.removeTableEntityIfEmpty(worldIn, pos);
		worldIn.notifyBlockUpdate(pos, state, state, 3);
	}

	private int getConnectionMask(IBlockAccess worldIn, BlockPos pos) {
		int connections = 0;

		if (this.connectsTo(worldIn, pos, EnumFacing.NORTH)) {
			connections |= NORTH;
		}
		if (this.connectsTo(worldIn, pos, EnumFacing.EAST)) {
			connections |= EAST;
		}
		if (this.connectsTo(worldIn, pos, EnumFacing.SOUTH)) {
			connections |= SOUTH;
		}
		if (this.connectsTo(worldIn, pos, EnumFacing.WEST)) {
			connections |= WEST;
		}

		return connections;
	}

	private boolean connectsTo(IBlockAccess worldIn, BlockPos pos, EnumFacing direction) {
		BlockPos neighborPos = pos.offset(direction);

		return worldIn.getBlockState(neighborPos).getBlock() == this
			&& !this.isConnectionBlocked(worldIn, pos, direction)
			&& !this.isConnectionBlocked(worldIn, neighborPos, direction.getOpposite());
	}

	private AxisAlignedBB getTopBoundingBox(int connections) {
		double minX = this.isConnected(connections, WEST) ? 0.0D : TOP_STANDALONE.minX;
		double maxX = this.isConnected(connections, EAST) ? 1.0D : TOP_STANDALONE.maxX;
		double minZ = this.isConnected(connections, NORTH) ? 0.0D : TOP_STANDALONE.minZ;
		double maxZ = this.isConnected(connections, SOUTH) ? 1.0D : TOP_STANDALONE.maxZ;

		return new AxisAlignedBB(minX, TOP_STANDALONE.minY, minZ, maxX, TOP_STANDALONE.maxY, maxZ);
	}

	private boolean isConnected(int connections, int mask) {
		return (connections & mask) != 0;
	}

	private boolean requiresSupport(int connections) {
		boolean north = this.isConnected(connections, NORTH);
		boolean east = this.isConnected(connections, EAST);
		boolean south = this.isConnected(connections, SOUTH);
		boolean west = this.isConnected(connections, WEST);

		return (!north && !west) || (!north && !east) || (!south && !west) || (!south && !east);
	}

	private void blockConnectionBetween(World worldIn, BlockPos pos, EnumFacing direction) {
		BlockPos neighborPos = pos.offset(direction);

		if (worldIn.getBlockState(neighborPos).getBlock() != this) {
			return;
		}

		this.setConnectionBlocked(worldIn, pos, direction, true);
		this.setConnectionBlocked(worldIn, neighborPos, direction.getOpposite(), true);
		this.updateTableState(worldIn, pos);
		this.updateTableState(worldIn, neighborPos);
	}

	private boolean isConnectionBlocked(IBlockAccess worldIn, BlockPos pos, EnumFacing direction) {
		TileEntity tileEntity = worldIn.getTileEntity(pos);

		return tileEntity instanceof TileEntityDiningTable
			&& ((TileEntityDiningTable)tileEntity).isConnectionBlocked(direction);
	}

	private void setConnectionBlocked(World worldIn, BlockPos pos, EnumFacing direction, boolean blocked) {
		TileEntityDiningTable table = this.getTableEntity(worldIn, pos, blocked);

		if (table != null) {
			table.setConnectionBlocked(direction, blocked);
			this.removeTableEntityIfEmpty(worldIn, pos);
		}
	}

	private void clearOrphanedBlockedConnections(World worldIn, BlockPos pos) {
		TileEntity tileEntity = worldIn.getTileEntity(pos);

		if (!(tileEntity instanceof TileEntityDiningTable)) {
			return;
		}

		TileEntityDiningTable table = (TileEntityDiningTable)tileEntity;

		for (EnumFacing direction : EnumFacing.Plane.HORIZONTAL) {
			if (table.isConnectionBlocked(direction)
				&& worldIn.getBlockState(pos.offset(direction)).getBlock() != this) {
				table.setConnectionBlocked(direction, false);
			}
		}
	}

	private TileEntityDiningTable getTableEntity(World worldIn, BlockPos pos, boolean create) {
		IBlockState state = worldIn.getBlockState(pos);

		if (state.getBlock() != this) {
			return null;
		}

		if (!state.getValue(DATA).booleanValue()) {
			if (!create || worldIn.isRemote
					|| !worldIn.setBlockState(pos, state.withProperty(DATA, Boolean.valueOf(true)), 2)) {
				return null;
			}

			state = worldIn.getBlockState(pos);
		}

		TileEntity tileEntity = worldIn.getTileEntity(pos);

		if (tileEntity instanceof TileEntityDiningTable) {
			return (TileEntityDiningTable)tileEntity;
		}

		if (!create || worldIn.isRemote) {
			return null;
		}

		tileEntity = this.createTileEntity(worldIn, state);

		if (tileEntity != null) {
			worldIn.setTileEntity(pos, tileEntity);
		}

		return tileEntity instanceof TileEntityDiningTable ? (TileEntityDiningTable)tileEntity : null;
	}

	private void removeTableEntityIfEmpty(World worldIn, BlockPos pos) {
		if (worldIn.isRemote) {
			return;
		}

		TileEntity tileEntity = worldIn.getTileEntity(pos);

		if (!(tileEntity instanceof TileEntityDiningTable)) {
			return;
		}

		TileEntityDiningTable table = (TileEntityDiningTable)tileEntity;

		if (!table.hasDisplayedItem()) {
			SurfaceDisplayBlocker.release(worldIn, pos);
		}

		if (table.hasStoredData()) {
			return;
		}

		IBlockState state = worldIn.getBlockState(pos);

		if (state.getBlock() == this && state.getValue(DATA).booleanValue()) {
			worldIn.setBlockState(pos, state.withProperty(DATA, Boolean.valueOf(false)), 2);
		} else {
			worldIn.removeTileEntity(pos);
		}
	}
}
