package com.mcmoddev.ironagefurniture.api.Blocks;

import java.util.List;

import com.mcmoddev.ironagefurniture.BlockObjectHolder;
import com.mcmoddev.ironagefurniture.Ironagefurniture;
import com.mcmoddev.ironagefurniture.api.SurfaceItemRules;
import com.mcmoddev.ironagefurniture.api.VasePlantHelper;
import com.mcmoddev.ironagefurniture.api.Items.DrinkContainerHelper;
import com.mcmoddev.ironagefurniture.api.surface.SurfaceSettingInteraction;
import com.mcmoddev.ironagefurniture.api.surface.SurfaceSettingInteraction.Result;
import com.mcmoddev.ironagefurniture.api.tile.TileEntityCabinet;
import com.mcmoddev.ironagefurniture.api.tile.TileEntityGlassVase;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.ILockableContainer;
import net.minecraft.world.World;

public class Cabinet extends Block {
	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	public static final PropertyBool OPEN = PropertyBool.create("open");
	public static final PropertyEnum<CabinetType> TYPE = PropertyEnum.create("type", CabinetType.class);
	public static final PropertyEnum<VerticalCabinetType> VERTICAL = PropertyEnum.create("vertical",
		VerticalCabinetType.class);
	private boolean updatingCabinetJoin;

	public static enum CabinetType implements IStringSerializable {
		SINGLE("single"),
		LEFT("left"),
		RIGHT("right");

		private final String name;

		private CabinetType(String name) {
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

	public static enum VerticalCabinetType implements IStringSerializable {
		SINGLE("single"),
		LOWER("lower"),
		UPPER("upper");

		private final String name;

		private VerticalCabinetType(String name) {
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

	public Cabinet(Material materialIn, String name, float resistance, float hardness) {
		super(materialIn);
		this.blockResistance = resistance;
		this.blockHardness = hardness;
		this.setSoundType(SoundType.WOOD);
		this.setHarvestLevel("axe", 0);
		this.setCreativeTab(Ironagefurniture.ironagefurnitureTab);
		this.setDefaultState(this.blockState.getBaseState()
			.withProperty(FACING, EnumFacing.SOUTH)
			.withProperty(OPEN, Boolean.FALSE)
			.withProperty(TYPE, CabinetType.SINGLE)
			.withProperty(VERTICAL, VerticalCabinetType.SINGLE));
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY,
			float hitZ, int meta, EntityLivingBase placer, ItemStack stack) {
		EnumFacing cabinetFacing = placer == null ? EnumFacing.SOUTH : placer.getHorizontalFacing().getOpposite();
		return this.getDefaultState()
			.withProperty(FACING, cabinetFacing)
			.withProperty(OPEN, Boolean.FALSE)
			.withProperty(TYPE, CabinetType.SINGLE)
			.withProperty(VERTICAL, VerticalCabinetType.SINGLE);
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack) {
		if (!worldIn.isRemote) {
			TileEntityCabinet cabinet = this.getCabinetEntity(worldIn, pos);

			if (cabinet != null && stack != null && stack.hasDisplayName()) {
				cabinet.setCustomName(stack.getDisplayName());
			}

			if (placer != null && placer.isSneaking()) {
				this.blockPotentialJoins(worldIn, pos, state);
			} else if (!this.tryCreateVerticalJoin(worldIn, pos, state)) {
				this.tryCreateHorizontalJoin(worldIn, pos, state);
			}

			this.updateCabinetAndNeighbors(worldIn, pos);
		}
	}

	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
		super.onBlockAdded(worldIn, pos, state);

		if (!worldIn.isRemote && !this.updatingCabinetJoin) {
			this.updateCabinetAndNeighbors(worldIn, pos);
		}
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn) {
		if (!worldIn.isRemote && !this.updatingCabinetJoin) {
			this.updateCabinetAndNeighbors(worldIn, pos);
		}
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (playerIn.isSneaking()) {
			return false;
		}

		if (side == EnumFacing.UP && this.handleSurfaceActivation(worldIn, pos, state, playerIn, hand,
				heldItem, hitX, hitZ)) {
			return true;
		}

		if (side == EnumFacing.UP && this.isDisplayExcluded(heldItem)) {
			return false;
		}

		if (!worldIn.isRemote) {
			ILockableContainer container = this.getLockableContainer(worldIn, pos);

			if (container != null) {
				playerIn.displayGUIChest(container);
			}
		}

		return true;
	}

	private boolean handleSurfaceActivation(World worldIn, BlockPos pos, IBlockState state,
			EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, float hitX, float hitZ) {
		float settingHitX = (float)(hitX - this.getDisplayItemXOffset(state) + 0.5D);
		float settingHitZ = (float)(hitZ - this.getDisplayItemZOffset(state) + 0.5D);
		boolean canRetrievePlacedBlock = this.canRetrievePlacedBlockAbove(worldIn, pos, playerIn, heldItem);
		TileEntityCabinet cabinet = this.getCabinetEntity(worldIn, pos);
		boolean hasDisplayedItem = cabinet != null && cabinet.hasDisplayedItem();
		boolean hasHeldItem = heldItem != null && heldItem.stackSize > 0;

		if (this.isDisplayExcluded(heldItem) && !canRetrievePlacedBlock) {
			return false;
		}

		if (!hasDisplayedItem && !canRetrievePlacedBlock && !hasHeldItem) {
			return false;
		}

		if (worldIn.isRemote) {
			return hasDisplayedItem
				? SurfaceSettingInteraction.handle(cabinet, playerIn, hand, heldItem, settingHitX, settingHitZ,
					this.getSurfaceSettingAxis(state), true) != Result.NOT_HANDLED
				: canRetrievePlacedBlock || hasHeldItem;
		}

		if (cabinet == null) {
			return false;
		}

		if (!cabinet.hasDisplayedItem() && canRetrievePlacedBlock
				&& this.tryRetrievePlacedBlockAbove(worldIn, pos, playerIn, heldItem)) {
			return true;
		}

		if (!cabinet.hasDisplayedItem() && hasHeldItem) {
			if (!SurfaceDisplayBlocker.reserve(worldIn, pos)) {
				return true;
			}
		}

		Result result = SurfaceSettingInteraction.handle(cabinet, playerIn, hand, heldItem,
			settingHitX, settingHitZ, this.getSurfaceSettingAxis(state), true);

		if (result == Result.HANDLED_AND_EMPTIED) {
			SurfaceDisplayBlocker.release(worldIn, pos);
		}
		if (result != Result.NOT_HANDLED) {
			return true;
		}

		return false;
	}

	protected EnumFacing.Axis getSurfaceSettingAxis(IBlockState state) {
		return null;
	}

	protected boolean isDisplayExcluded(ItemStack heldItem) {
		return SurfaceItemRules.shouldPlaceAsBlockOnCabinetSurface(heldItem);
	}

	protected boolean isFlowerPotItem(ItemStack heldItem) {
		return SurfaceItemRules.isFlowerPotItem(heldItem);
	}

	private boolean canRetrieveDisplayedItem(TileEntityCabinet cabinet, ItemStack heldItem) {
		if (heldItem == null || heldItem.stackSize <= 0) {
			return true;
		}

		ItemStack displayedItem = cabinet.getDisplayedItem();
		return displayedItem != null && displayedItem.stackSize > 0
			&& this.isSameItemStack(displayedItem, heldItem);
	}

	private void handleDisplayedVaseClick(World worldIn, BlockPos pos, TileEntityCabinet cabinet,
			EntityPlayer playerIn, EnumHand hand, ItemStack heldItem) {
		ItemStack displayedItem = cabinet.getDisplayedItem();
		ItemStack plant = VasePlantHelper.removePlant(displayedItem);

		if (plant != null) {
			cabinet.setDisplayedItem(displayedItem);
			this.returnItem(worldIn, pos, playerIn, plant);
			return;
		}

		if (VasePlantHelper.addPlant(displayedItem, heldItem)) {
			cabinet.setDisplayedItem(displayedItem);

			if (!playerIn.capabilities.isCreativeMode) {
				heldItem.stackSize--;

				if (heldItem.stackSize <= 0) {
					playerIn.setHeldItem(hand, null);
				}
			}
		}
	}

	private void returnItem(World worldIn, BlockPos pos, EntityPlayer playerIn, ItemStack itemStack) {
		if (itemStack != null && itemStack.stackSize > 0
				&& !playerIn.inventory.addItemStackToInventory(itemStack)) {
			EntityItem entityItem = new EntityItem(worldIn, pos.getX() + 0.5D, pos.getY() + 1.1D,
				pos.getZ() + 0.5D, itemStack);
			worldIn.spawnEntity(entityItem);
		}
	}

	private boolean canRetrievePlacedBlockAbove(World worldIn, BlockPos pos, EntityPlayer playerIn,
			ItemStack heldItem) {
		if (this.isPottedGlassVaseAbove(worldIn, pos)
				&& (heldItem == null || heldItem.stackSize <= 0 || VasePlantHelper.isPlantItem(heldItem))) {
			return false;
		}

		ItemStack placedBlockItem = this.getPlacedBlockItem(worldIn, pos, playerIn);

		if (placedBlockItem == null || placedBlockItem.stackSize <= 0) {
			return false;
		}

		return heldItem == null || heldItem.stackSize <= 0
			|| this.isSameItemStack(placedBlockItem, heldItem);
	}

	public boolean tryRetrievePlacedBlockAbove(World worldIn, BlockPos pos, EntityPlayer playerIn, ItemStack heldItem) {
		TileEntityCabinet cabinet = this.getCabinetEntity(worldIn, pos);

		if (cabinet != null && cabinet.hasDisplayedItem()) {
			return false;
		}

		if (!this.canRetrievePlacedBlockAbove(worldIn, pos, playerIn, heldItem)) {
			return false;
		}

		if (worldIn.isRemote) {
			return true;
		}

		return this.retrievePlacedBlockAbove(worldIn, pos, playerIn, heldItem);
	}

	private boolean retrievePlacedBlockAbove(World worldIn, BlockPos pos, EntityPlayer playerIn, ItemStack heldItem) {
		BlockPos abovePos = pos.up();
		ItemStack placedBlockItem = this.getPlacedBlockItem(worldIn, pos, playerIn);

		if (placedBlockItem == null || placedBlockItem.stackSize <= 0
				|| (heldItem != null && heldItem.stackSize > 0 && !this.isSameItemStack(placedBlockItem, heldItem))) {
			return false;
		}

		List<ItemStack> drops = this.getPlacedBlockDrops(worldIn, pos, playerIn);

		if (drops.isEmpty() || !worldIn.setBlockState(abovePos, Blocks.AIR.getDefaultState(), 3)) {
			return false;
		}

		for (ItemStack drop : drops) {
			if (drop != null && drop.stackSize > 0 && !playerIn.inventory.addItemStackToInventory(drop)) {
				EntityItem entityItem = new EntityItem(worldIn, pos.getX() + 0.5D, pos.getY() + 1.1D,
					pos.getZ() + 0.5D, drop);
				worldIn.spawnEntity(entityItem);
			}
		}

		return true;
	}

	private ItemStack getPlacedBlockItem(World worldIn, BlockPos pos, EntityPlayer playerIn) {
		BlockPos abovePos = pos.up();
		IBlockState aboveState = worldIn.getBlockState(abovePos);
		Block aboveBlock = aboveState.getBlock();

		if (aboveBlock == Blocks.AIR || aboveBlock == BlockObjectHolder.surface_display_blocker) {
			return null;
		}

		if (aboveBlock instanceof GlassVaseBlock) {
			return new ItemStack(aboveBlock, 1, aboveBlock.getMetaFromState(aboveState));
		}

		if (aboveBlock == Blocks.FLOWER_POT) {
			return new ItemStack(Items.FLOWER_POT, 1);
		}

		if (aboveBlock.hasTileEntity(aboveState)) {
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

	private List<ItemStack> getPlacedBlockDrops(World worldIn, BlockPos pos, EntityPlayer playerIn) {
		BlockPos abovePos = pos.up();
		IBlockState aboveState = worldIn.getBlockState(abovePos);
		Block aboveBlock = aboveState.getBlock();

		if (aboveBlock instanceof GlassVaseBlock) {
			return aboveBlock.getDrops(worldIn, abovePos, aboveState, 0);
		}

		if (aboveBlock == Blocks.FLOWER_POT) {
			return aboveBlock.getDrops(worldIn, abovePos, aboveState, 0);
		}

		List<ItemStack> drops = new java.util.ArrayList<ItemStack>();
		ItemStack placedBlockItem = this.getPlacedBlockItem(worldIn, pos, playerIn);

		if (placedBlockItem != null) {
			drops.add(placedBlockItem);
		}

		return drops;
	}

	private boolean isPottedGlassVaseAbove(World worldIn, BlockPos pos) {
		BlockPos abovePos = pos.up();
		IBlockState aboveState = worldIn.getBlockState(abovePos);

		if (!(aboveState.getBlock() instanceof GlassVaseBlock)) {
			return false;
		}

		TileEntity tileEntity = worldIn.getTileEntity(abovePos);
		return tileEntity instanceof TileEntityGlassVase
			&& ((TileEntityGlassVase)tileEntity).hasPlant();
	}

	protected boolean isSameItemStack(ItemStack storedItem, ItemStack heldItem) {
		return storedItem != null && heldItem != null
			&& storedItem.isItemEqual(heldItem)
			&& ItemStack.areItemStackTagsEqual(storedItem, heldItem);
	}

	private ILockableContainer getLockableContainer(World worldIn, BlockPos pos) {
		IBlockState state = worldIn.getBlockState(pos);
		TileEntityCabinet cabinet = this.getCabinetEntity(worldIn, pos);

		if (state.getBlock() != this || cabinet == null) {
			return null;
		}

		EnumFacing joinDirection = this.findJoinDirection(worldIn, pos, state);

		if (joinDirection == null) {
			joinDirection = this.findVerticalJoinDirection(worldIn, pos, state);
		}

		if (joinDirection == null) {
			return cabinet;
		}

		TileEntityCabinet other = this.getCabinetEntity(worldIn, pos.offset(joinDirection));

		if (other == null) {
			return cabinet;
		}

		if (joinDirection != EnumFacing.UP && joinDirection != EnumFacing.DOWN) {
			return this.getTypeForJoinDirection(state.getValue(FACING), joinDirection) == CabinetType.RIGHT
				? new InventoryLargeChest(this.getContainerName(), other, cabinet)
				: new InventoryLargeChest(this.getContainerName(), cabinet, other);
		}

		return joinDirection == EnumFacing.DOWN
			? new InventoryLargeChest(this.getContainerName(), other, cabinet)
			: new InventoryLargeChest(this.getContainerName(), cabinet, other);
	}

	protected String getContainerName() {
		return "container.ironagefurniture.cabinet";
	}

	private void blockPotentialJoins(World worldIn, BlockPos pos, IBlockState state) {
		EnumFacing facing = state.getValue(FACING);
		this.blockJoinToAdjacent(worldIn, pos, state, facing.rotateY());
		this.blockJoinToAdjacent(worldIn, pos, state, facing.rotateYCCW());
		this.blockJoinToAdjacent(worldIn, pos, state, EnumFacing.UP);
		this.blockJoinToAdjacent(worldIn, pos, state, EnumFacing.DOWN);
	}

	private void blockJoinToAdjacent(World worldIn, BlockPos pos, IBlockState state, EnumFacing direction) {
		if (!this.isSameJoinFamily(worldIn, pos, direction, state.getValue(FACING))) {
			return;
		}

		this.setConnectionBlocked(worldIn, pos, direction, true);
		this.setConnectionBlocked(worldIn, pos.offset(direction), direction.getOpposite(), true);
	}

	private void updateCabinetAndNeighbors(World worldIn, BlockPos pos) {
		this.updateCabinetState(worldIn, pos);

		for (EnumFacing facing : EnumFacing.values()) {
			BlockPos neighborPos = pos.offset(facing);
			IBlockState neighborState = worldIn.getBlockState(neighborPos);

			if (neighborState.getBlock() == this) {
				this.updateCabinetState(worldIn, neighborPos);
			}
		}
	}

	private void updateCabinetState(World worldIn, BlockPos pos) {
		IBlockState state = worldIn.getBlockState(pos);

		if (state.getBlock() != this) {
			return;
		}

		this.clearOrphanedBlockedConnections(worldIn, pos);
		this.clearInvalidVerticalJoin(worldIn, pos, state);
		EnumFacing joinDirection = this.findJoinDirection(worldIn, pos, state);
		CabinetType type = joinDirection == null ? CabinetType.SINGLE
			: this.getTypeForJoinDirection(state.getValue(FACING), joinDirection);

		if (state.getValue(TYPE) != type) {
			worldIn.setBlockState(pos, state.withProperty(TYPE, type), 3);
		} else {
			worldIn.notifyBlockUpdate(pos, state, state, 3);
		}
	}

	private EnumFacing findJoinDirection(World worldIn, BlockPos pos, IBlockState state) {
		EnumFacing currentJoin = this.getCurrentJoinDirection(state);

		if (currentJoin != null && this.canMaintainHorizontalJoinWith(worldIn, pos, currentJoin, state)) {
			return currentJoin;
		}

		return null;
	}

	private boolean tryCreateHorizontalJoin(World worldIn, BlockPos pos, IBlockState state) {
		if (state.getBlock() != this || state.getValue(TYPE) != CabinetType.SINGLE
				|| this.findVerticalJoinDirection(worldIn, pos, state) != null) {
			return false;
		}

		EnumFacing joinDirection = this.findNewHorizontalJoinDirection(worldIn, pos, state);

		if (joinDirection == null) {
			return false;
		}

		this.setHorizontalJoin(worldIn, pos, state, joinDirection);
		return true;
	}

	private EnumFacing findNewHorizontalJoinDirection(World worldIn, BlockPos pos, IBlockState state) {
		EnumFacing facing = state.getValue(FACING);
		EnumFacing left = facing.rotateY();

		if (this.canCreateHorizontalJoinWith(worldIn, pos, left, state)) {
			return left;
		}

		EnumFacing right = facing.rotateYCCW();
		return this.canCreateHorizontalJoinWith(worldIn, pos, right, state) ? right : null;
	}

	private boolean canMaintainHorizontalJoinWith(World worldIn, BlockPos pos, EnumFacing direction,
			IBlockState state) {
		if (!this.canUseHorizontalJoin(worldIn, pos, direction, state)) {
			return false;
		}

		IBlockState neighborState = worldIn.getBlockState(pos.offset(direction));
		return this.getCurrentJoinDirection(neighborState) == direction.getOpposite();
	}

	private boolean canCreateHorizontalJoinWith(World worldIn, BlockPos pos, EnumFacing direction,
			IBlockState state) {
		if (!this.canUseHorizontalJoin(worldIn, pos, direction, state)) {
			return false;
		}

		IBlockState neighborState = worldIn.getBlockState(pos.offset(direction));
		return state.getValue(TYPE) == CabinetType.SINGLE
			&& neighborState.getValue(TYPE) == CabinetType.SINGLE;
	}

	private boolean canUseHorizontalJoin(World worldIn, BlockPos pos, EnumFacing direction, IBlockState state) {
		if (direction == EnumFacing.UP || direction == EnumFacing.DOWN) {
			return false;
		}

		if (!this.isSameJoinFamily(worldIn, pos, direction, state.getValue(FACING))) {
			return false;
		}

		if (this.isConnectionBlocked(worldIn, pos, direction)
				|| this.isConnectionBlocked(worldIn, pos.offset(direction), direction.getOpposite())) {
			return false;
		}

		IBlockState neighborState = worldIn.getBlockState(pos.offset(direction));

		return this.findVerticalJoinDirection(worldIn, pos, state) == null
			&& this.findVerticalJoinDirection(worldIn, pos.offset(direction), neighborState) == null;
	}

	private void setHorizontalJoin(World worldIn, BlockPos pos, IBlockState state, EnumFacing joinDirection) {
		BlockPos neighborPos = pos.offset(joinDirection);
		IBlockState neighborState = worldIn.getBlockState(neighborPos);
		this.updatingCabinetJoin = true;

		try {
			worldIn.setBlockState(pos,
				state.withProperty(TYPE, this.getTypeForJoinDirection(state.getValue(FACING), joinDirection)), 3);
			worldIn.setBlockState(neighborPos, neighborState.withProperty(TYPE,
				this.getTypeForJoinDirection(state.getValue(FACING), joinDirection.getOpposite())), 3);
		} finally {
			this.updatingCabinetJoin = false;
		}

		this.updateCabinetAndNeighbors(worldIn, pos);
	}

	private boolean tryCreateVerticalJoin(World worldIn, BlockPos pos, IBlockState state) {
		if (state.getBlock() != this || state.getValue(TYPE) != CabinetType.SINGLE) {
			return false;
		}

		EnumFacing joinDirection = this.canCreateVerticalJoinWith(worldIn, pos, EnumFacing.DOWN, state)
			? EnumFacing.DOWN : null;

		if (joinDirection == null && this.canCreateVerticalJoinWith(worldIn, pos, EnumFacing.UP, state)) {
			joinDirection = EnumFacing.UP;
		}

		if (joinDirection == null) {
			return false;
		}

		this.setVerticalJoin(worldIn, pos, joinDirection);
		return true;
	}

	private boolean canCreateVerticalJoinWith(IBlockAccess worldIn, BlockPos pos, EnumFacing direction,
			IBlockState state) {
		if (direction != EnumFacing.UP && direction != EnumFacing.DOWN) {
			return false;
		}
		if (state.getBlock() != this || state.getValue(TYPE) != CabinetType.SINGLE) {
			return false;
		}
		if (!this.isSameJoinFamily(worldIn, pos, direction, state.getValue(FACING))) {
			return false;
		}
		if (this.isConnectionBlocked(worldIn, pos, direction)
				|| this.isConnectionBlocked(worldIn, pos.offset(direction), direction.getOpposite())) {
			return false;
		}

		TileEntityCabinet cabinet = this.getCabinetEntity(worldIn, pos);
		TileEntityCabinet neighbor = this.getCabinetEntity(worldIn, pos.offset(direction));
		IBlockState neighborState = worldIn.getBlockState(pos.offset(direction));

		if (cabinet == null || neighbor == null || cabinet.getVerticalJoinDirection() != null
				|| neighbor.getVerticalJoinDirection() != null || neighborState.getValue(TYPE) != CabinetType.SINGLE) {
			return false;
		}

		BlockPos lowerPos = direction == EnumFacing.UP ? pos : pos.down();
		return !this.isSameJoinFamily(worldIn, lowerPos, EnumFacing.DOWN, state.getValue(FACING));
	}

	private void setVerticalJoin(World worldIn, BlockPos pos, EnumFacing joinDirection) {
		this.setVerticalJoinDirection(worldIn, pos, joinDirection);
		this.setVerticalJoinDirection(worldIn, pos.offset(joinDirection), joinDirection.getOpposite());
		worldIn.notifyBlockUpdate(pos, worldIn.getBlockState(pos), worldIn.getBlockState(pos), 3);
		worldIn.notifyBlockUpdate(pos.offset(joinDirection), worldIn.getBlockState(pos.offset(joinDirection)),
			worldIn.getBlockState(pos.offset(joinDirection)), 3);
	}

	private EnumFacing findVerticalJoinDirection(IBlockAccess worldIn, BlockPos pos, IBlockState state) {
		return this.getValidatedVerticalJoinDirection(worldIn, pos, state);
	}

	private EnumFacing getValidatedVerticalJoinDirection(IBlockAccess worldIn, BlockPos pos, IBlockState state) {
		TileEntityCabinet cabinet = this.getCabinetEntity(worldIn, pos);
		EnumFacing direction = cabinet == null ? null : cabinet.getVerticalJoinDirection();

		if (direction != EnumFacing.UP && direction != EnumFacing.DOWN) {
			return null;
		}

		return this.canMaintainVerticalJoinWith(worldIn, pos, direction, state) ? direction : null;
	}

	private VerticalCabinetType getVerticalType(IBlockAccess worldIn, BlockPos pos, IBlockState state) {
		EnumFacing direction = this.getValidatedVerticalJoinDirection(worldIn, pos, state);

		if (direction == EnumFacing.DOWN) {
			return VerticalCabinetType.UPPER;
		}
		if (direction == EnumFacing.UP) {
			return VerticalCabinetType.LOWER;
		}

		return VerticalCabinetType.SINGLE;
	}

	private boolean canMaintainVerticalJoinWith(IBlockAccess worldIn, BlockPos pos, EnumFacing direction,
			IBlockState state) {
		if (direction != EnumFacing.UP && direction != EnumFacing.DOWN) {
			return false;
		}
		if (state.getBlock() != this || state.getValue(TYPE) != CabinetType.SINGLE) {
			return false;
		}
		if (!this.isSameJoinFamily(worldIn, pos, direction, state.getValue(FACING))) {
			return false;
		}
		if (this.isConnectionBlocked(worldIn, pos, direction)
				|| this.isConnectionBlocked(worldIn, pos.offset(direction), direction.getOpposite())) {
			return false;
		}

		TileEntityCabinet neighbor = this.getCabinetEntity(worldIn, pos.offset(direction));
		IBlockState neighborState = worldIn.getBlockState(pos.offset(direction));
		return neighbor != null && neighborState.getValue(TYPE) == CabinetType.SINGLE
			&& neighbor.getVerticalJoinDirection() == direction.getOpposite();
	}

	private boolean isSameJoinFamily(IBlockAccess worldIn, BlockPos pos, EnumFacing direction, EnumFacing facing) {
		IBlockState neighborState = worldIn.getBlockState(pos.offset(direction));
		return neighborState.getBlock() == this && neighborState.getValue(FACING) == facing;
	}

	private CabinetType getTypeForJoinDirection(EnumFacing facing, EnumFacing joinDirection) {
		return joinDirection == facing.rotateY() ? CabinetType.RIGHT : CabinetType.LEFT;
	}

	private EnumFacing getCurrentJoinDirection(IBlockState state) {
		CabinetType type = state.getValue(TYPE);
		EnumFacing facing = state.getValue(FACING);

		if (type == CabinetType.LEFT) {
			return facing.rotateYCCW();
		}
		if (type == CabinetType.RIGHT) {
			return facing.rotateY();
		}

		return null;
	}

	private boolean isConnectionBlocked(IBlockAccess worldIn, BlockPos pos, EnumFacing direction) {
		TileEntity tileEntity = worldIn.getTileEntity(pos);

		return tileEntity instanceof TileEntityCabinet
			&& ((TileEntityCabinet)tileEntity).isConnectionBlocked(direction);
	}

	private void setConnectionBlocked(World worldIn, BlockPos pos, EnumFacing direction, boolean blocked) {
		TileEntityCabinet cabinet = this.getCabinetEntity(worldIn, pos);

		if (cabinet != null) {
			cabinet.setConnectionBlocked(direction, blocked);
		}
	}

	private void setVerticalJoinDirection(World worldIn, BlockPos pos, EnumFacing direction) {
		TileEntityCabinet cabinet = this.getCabinetEntity(worldIn, pos);

		if (cabinet != null) {
			cabinet.setVerticalJoinDirection(direction);
		}
	}

	private void clearInvalidVerticalJoin(World worldIn, BlockPos pos, IBlockState state) {
		TileEntityCabinet cabinet = this.getCabinetEntity(worldIn, pos);

		if (cabinet == null || cabinet.getVerticalJoinDirection() == null
				|| this.getValidatedVerticalJoinDirection(worldIn, pos, state) != null) {
			return;
		}

		EnumFacing direction = cabinet.getVerticalJoinDirection();
		cabinet.setVerticalJoinDirection(null);

		TileEntityCabinet neighbor = this.getCabinetEntity(worldIn, pos.offset(direction));

		if (neighbor != null && neighbor.getVerticalJoinDirection() == direction.getOpposite()) {
			neighbor.setVerticalJoinDirection(null);
		}
	}

	private void clearOrphanedBlockedConnections(World worldIn, BlockPos pos) {
		TileEntity tileEntity = worldIn.getTileEntity(pos);

		if (!(tileEntity instanceof TileEntityCabinet)) {
			return;
		}

		TileEntityCabinet cabinet = (TileEntityCabinet)tileEntity;
		IBlockState state = worldIn.getBlockState(pos);

		for (EnumFacing direction : EnumFacing.values()) {
			if (cabinet.isConnectionBlocked(direction)
					&& !this.isSameJoinFamily(worldIn, pos, direction, state.getValue(FACING))) {
				cabinet.setConnectionBlocked(direction, false);
			}
		}
	}

	private TileEntityCabinet getCabinetEntity(IBlockAccess worldIn, BlockPos pos) {
		TileEntity tileEntity = worldIn.getTileEntity(pos);
		return tileEntity instanceof TileEntityCabinet ? (TileEntityCabinet)tileEntity : null;
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileEntityCabinet();
	}

	@Override
	public boolean eventReceived(IBlockState state, World worldIn, BlockPos pos, int id, int param) {
		super.eventReceived(state, worldIn, pos, id, param);
		TileEntity tileEntity = worldIn.getTileEntity(pos);
		return tileEntity != null && tileEntity.receiveClientEvent(id, param);
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		if (!worldIn.isRemote) {
			TileEntity tileEntity = worldIn.getTileEntity(pos);

			if (tileEntity instanceof TileEntityCabinet) {
				TileEntityCabinet cabinet = (TileEntityCabinet)tileEntity;
				cabinet.dropDisplayedItem(worldIn, pos);
				InventoryHelper.dropInventoryItems(worldIn, pos, cabinet);
			}

			SurfaceDisplayBlocker.release(worldIn, pos);
		}

		super.breakBlock(worldIn, pos, state);

		if (!worldIn.isRemote) {
			for (EnumFacing facing : EnumFacing.values()) {
				BlockPos neighborPos = pos.offset(facing);
				IBlockState neighborState = worldIn.getBlockState(neighborPos);

				if (neighborState.getBlock() == this) {
					this.updateCabinetState(worldIn, neighborPos);
				}
			}
		}
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isSideSolid(IBlockState baseState, IBlockAccess world, BlockPos pos, EnumFacing side) {
		return side == EnumFacing.UP || super.isSideSolid(baseState, world, pos, side);
	}

	public double getDisplayItemYOffset() {
		return 1.04D;
	}

	public double getDisplayItemXOffset(IBlockState state) {
		return 0.5D;
	}

	public double getDisplayItemZOffset(IBlockState state) {
		return 0.5D;
	}

	public double getDisplayBlockSurfaceYOffset() {
		return 1.0D;
	}

	@Override
	public boolean hasComparatorInputOverride(IBlockState state) {
		return true;
	}

	@Override
	public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos) {
		ILockableContainer container = this.getLockableContainer(worldIn, pos);
		return container instanceof IInventory ? Container.calcRedstoneFromInventory((IInventory)container) : 0;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		EnumFacing facing = EnumFacing.getHorizontal(meta & 3);
		int typeMeta = (meta >> 2) & 3;
		CabinetType type = typeMeta == 1 ? CabinetType.LEFT : (typeMeta == 2 ? CabinetType.RIGHT : CabinetType.SINGLE);
		return this.getDefaultState().withProperty(FACING, facing).withProperty(OPEN, Boolean.FALSE)
			.withProperty(TYPE, type);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		int meta = state.getValue(FACING).getHorizontalIndex();
		CabinetType type = state.getValue(TYPE);

		if (type == CabinetType.LEFT) {
			meta |= 4;
		} else if (type == CabinetType.RIGHT) {
			meta |= 8;
		}

		return meta;
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		return state.withProperty(OPEN, Boolean.valueOf(this.isCabinetOpen(worldIn, pos)))
			.withProperty(VERTICAL, this.getVerticalType(worldIn, pos, state));
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { FACING, OPEN, TYPE, VERTICAL });
	}

	private boolean isCabinetOpen(IBlockAccess worldIn, BlockPos pos) {
		TileEntity tileEntity = worldIn.getTileEntity(pos);
		return tileEntity instanceof TileEntityCabinet && ((TileEntityCabinet)tileEntity).isVisuallyOpen();
	}

	@Override
	public IBlockState withRotation(IBlockState state, Rotation rot) {
		return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
	}

	@Override
	public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
		return state.withRotation(mirrorIn.toRotation(state.getValue(FACING)));
	}
}
