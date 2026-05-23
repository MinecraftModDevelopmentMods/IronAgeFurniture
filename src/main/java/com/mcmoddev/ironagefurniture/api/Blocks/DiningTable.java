package com.mcmoddev.ironagefurniture.api.Blocks;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.mcmoddev.ironagefurniture.Ironagefurniture;
import com.mcmoddev.ironagefurniture.api.tile.TileEntityDiningTable;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class DiningTable extends Block {
	public static final PropertyInteger CONNECTIONS = PropertyInteger.create("connections", 0, 15);

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
		this.setDefaultState(this.blockState.getBaseState().withProperty(CONNECTIONS, Integer.valueOf(0)));
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

		return this.getDefaultState().withProperty(CONNECTIONS, Integer.valueOf(this.getConnectionMask(world, pos)));
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
		if (this.requiresSupport(state) && !this.hasSupport(worldIn, pos)) {
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

		TileEntity tileEntity = worldIn.getTileEntity(pos);

		if (!(tileEntity instanceof TileEntityDiningTable)) {
			return true;
		}

		TileEntityDiningTable table = (TileEntityDiningTable)tileEntity;

		if (worldIn.isRemote) {
			return true;
		}

		if (!table.hasDisplayedItem() && heldItem != null && heldItem.stackSize > 0) {
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

		if (table.hasDisplayedItem() && (heldItem == null || heldItem.stackSize <= 0)) {
			ItemStack displayedItem = table.removeDisplayedItem();

			if (displayedItem != null) {
				if (!playerIn.inventory.addItemStackToInventory(displayedItem)) {
					EntityItem entityItem = new EntityItem(worldIn, pos.getX() + 0.5D, pos.getY() + 1.1D,
						pos.getZ() + 0.5D, displayedItem);
					worldIn.spawnEntity(entityItem);
				}
			}

			return true;
		}

		return true;
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileEntityDiningTable();
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
		return this.getTopBoundingBox(state);
	}

	@Override
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox,
			List<AxisAlignedBB> collidingBoxes, Entity entityIn) {
		super.addCollisionBoxToList(pos, entityBox, collidingBoxes, this.getTopBoundingBox(state));

		int connections = state.getValue(CONNECTIONS).intValue();

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
		return this.getDefaultState().withProperty(CONNECTIONS, Integer.valueOf(meta & 15));
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(CONNECTIONS).intValue() & 15;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { CONNECTIONS });
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

		if (state.getValue(CONNECTIONS).intValue() != connections) {
			worldIn.setBlockState(pos, state.withProperty(CONNECTIONS, Integer.valueOf(connections)), 3);
		}
	}

	private int getConnectionMask(World worldIn, BlockPos pos) {
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

	private boolean connectsTo(World worldIn, BlockPos pos, EnumFacing direction) {
		BlockPos neighborPos = pos.offset(direction);

		return worldIn.getBlockState(neighborPos).getBlock() == this
			&& !this.isConnectionBlocked(worldIn, pos, direction)
			&& !this.isConnectionBlocked(worldIn, neighborPos, direction.getOpposite());
	}

	private AxisAlignedBB getTopBoundingBox(IBlockState state) {
		int connections = state.getValue(CONNECTIONS).intValue();
		double minX = this.isConnected(connections, WEST) ? 0.0D : TOP_STANDALONE.minX;
		double maxX = this.isConnected(connections, EAST) ? 1.0D : TOP_STANDALONE.maxX;
		double minZ = this.isConnected(connections, NORTH) ? 0.0D : TOP_STANDALONE.minZ;
		double maxZ = this.isConnected(connections, SOUTH) ? 1.0D : TOP_STANDALONE.maxZ;

		return new AxisAlignedBB(minX, TOP_STANDALONE.minY, minZ, maxX, TOP_STANDALONE.maxY, maxZ);
	}

	private boolean isConnected(int connections, int mask) {
		return (connections & mask) != 0;
	}

	private boolean requiresSupport(IBlockState state) {
		return this.requiresSupport(state.getValue(CONNECTIONS).intValue());
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

	private boolean isConnectionBlocked(World worldIn, BlockPos pos, EnumFacing direction) {
		TileEntity tileEntity = worldIn.getTileEntity(pos);

		return tileEntity instanceof TileEntityDiningTable
			&& ((TileEntityDiningTable)tileEntity).isConnectionBlocked(direction);
	}

	private void setConnectionBlocked(World worldIn, BlockPos pos, EnumFacing direction, boolean blocked) {
		TileEntity tileEntity = worldIn.getTileEntity(pos);

		if (tileEntity instanceof TileEntityDiningTable) {
			((TileEntityDiningTable)tileEntity).setConnectionBlocked(direction, blocked);
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
}
