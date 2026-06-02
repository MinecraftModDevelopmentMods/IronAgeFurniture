package com.mcmoddev.ironagefurniture.api.Blocks;

import java.util.List;
import java.util.Random;

import com.mcmoddev.ironagefurniture.BlockObjectHolder;
import com.mcmoddev.ironagefurniture.Ironagefurniture;
import com.mcmoddev.ironagefurniture.api.VasePlantHelper;
import com.mcmoddev.ironagefurniture.api.tile.TileEntityWallShelf;
import com.mcmoddev.ironagefurniture.client.particle.CandleFlameParticle;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemRecord;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class WallShelf extends BlockHBase {
	public static final PropertyEnum<ShelfSupport> SUPPORT = PropertyEnum.create("support", ShelfSupport.class);
	public static final PropertyEnum<ShelfContents> CONTENTS = PropertyEnum.create("contents", ShelfContents.class);
	public static final PropertyBool DATA = PropertyBool.create("data");

	private static final AxisAlignedBB BOARD_STRAIGHT_NORTH = new AxisAlignedBB(0.0D, 0.625D, 0.25D, 1.0D, 0.8125D, 1.0D);
	private static final AxisAlignedBB BOARD_INNER_CORNER_NORTH = new AxisAlignedBB(0.0D, 0.625D, 0.0D, 1.0D, 0.8125D, 1.0D);
	private static final AxisAlignedBB BOARD_OUTER_CORNER_NORTH = new AxisAlignedBB(0.0D, 0.625D, 0.25D, 0.75D, 0.8125D, 1.0D);

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

	public static enum ShelfContentKind implements IStringSerializable {
		NONE("none", 0, 0),
		GLOW("glow", 1, 15),
		LAVA("lava", 1, 15),
		CANDLE("candle", 1, 12),
		FLOWER_POT("flower_pot", 2, 0),
		BOOKS("books", 5, 0),
		RECORDS("records", 6, 0);

		private final String name;
		private final int capacity;
		private final int lightLevel;

		private ShelfContentKind(String name, int capacity, int lightLevel) {
			this.name = name;
			this.capacity = capacity;
			this.lightLevel = lightLevel;
		}

		@Override
		public String getName() {
			return this.name;
		}

		public int getCapacity() {
			return this.capacity;
		}

		public int getLightLevel() {
			return this.lightLevel;
		}

		public static ShelfContentKind byName(String name) {
			for (ShelfContentKind kind : values()) {
				if (kind.name.equals(name)) {
					return kind;
				}
			}

			return NONE;
		}

		@Override
		public String toString() {
			return this.name;
		}
	}

	public static enum ShelfContents implements IStringSerializable {
		NONE("none", ShelfContentKind.NONE, 0),
		GLOW("glow", ShelfContentKind.GLOW, 1),
		LAVA("lava", ShelfContentKind.LAVA, 1),
		CANDLE("candle", ShelfContentKind.CANDLE, 1),
		FLOWER_POT("flower_pot", ShelfContentKind.FLOWER_POT, 1),
		BOOKS_1("books_1", ShelfContentKind.BOOKS, 1),
		BOOKS_2("books_2", ShelfContentKind.BOOKS, 2),
		BOOKS_3("books_3", ShelfContentKind.BOOKS, 3),
		BOOKS_4("books_4", ShelfContentKind.BOOKS, 4),
		BOOKS_5("books_5", ShelfContentKind.BOOKS, 5),
		RECORDS_1("records_1", ShelfContentKind.RECORDS, 1),
		RECORDS_2("records_2", ShelfContentKind.RECORDS, 2),
		RECORDS_3("records_3", ShelfContentKind.RECORDS, 3),
		RECORDS_4("records_4", ShelfContentKind.RECORDS, 4),
		RECORDS_5("records_5", ShelfContentKind.RECORDS, 5),
		RECORDS_6("records_6", ShelfContentKind.RECORDS, 6);

		private final String name;
		private final ShelfContentKind kind;
		private final int count;

		private ShelfContents(String name, ShelfContentKind kind, int count) {
			this.name = name;
			this.kind = kind;
			this.count = count;
		}

		@Override
		public String getName() {
			return this.name;
		}

		public int getLightLevel() {
			return this.kind.getLightLevel();
		}

		public static ShelfContents fromKindAndCount(ShelfContentKind kind, int count) {
			if (kind == null || kind == ShelfContentKind.NONE || count <= 0) {
				return NONE;
			}

			for (ShelfContents contents : values()) {
				if (contents.kind == kind && contents.count == count) {
					return contents;
				}

				if (kind == ShelfContentKind.FLOWER_POT && contents.kind == kind) {
					return contents;
				}
			}

			return NONE;
		}

		@Override
		public String toString() {
			return this.name;
		}
	}

	private static class ShelfRenderState {
		private final EnumFacing facing;
		private final ShelfSupport support;

		ShelfRenderState(EnumFacing facing, ShelfSupport support) {
			this.facing = facing;
			this.support = support;
		}

		ShelfRenderState(EnumFacing facing, ShelfSupport support, ShelfRenderState ignored) {
			this(facing, support);
		}
	}

	public WallShelf(Material materialIn, String name, float resistance, float hardness) {
		super(materialIn);
		this.blockResistance = resistance;
		this.blockHardness = hardness;
		this.setSoundType(SoundType.WOOD);
		this.setHarvestLevel("axe", 0);
		this.setCreativeTab(Ironagefurniture.ironagefurnitureTab);
		this.setLightOpacity(0);
		this.setDefaultState(this.blockState.getBaseState()
			.withProperty(FACING, EnumFacing.NORTH)
			.withProperty(SUPPORT, ShelfSupport.STRAIGHT_SUPPORT)
			.withProperty(CONTENTS, ShelfContents.NONE)
			.withProperty(DATA, Boolean.valueOf(false)));
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
			.withProperty(DATA, Boolean.valueOf(false))
			.withProperty(CONTENTS, ShelfContents.NONE)
			.withProperty(SUPPORT, this.getRenderState(world, pos, facing).support);
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		ShelfRenderState renderState = this.getRenderState(worldIn, pos, state.getValue(FACING));
		return state.withProperty(FACING, renderState.facing)
			.withProperty(SUPPORT, renderState.support)
			.withProperty(CONTENTS, this.getShelfContents(worldIn, pos));
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

		this.refreshDisplayBlocker(worldIn, pos);
		this.notifyShelfAndNeighbors(worldIn, pos);
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		if (!worldIn.isRemote) {
			TileEntity tileEntity = worldIn.getTileEntity(pos);

			if (tileEntity instanceof TileEntityWallShelf) {
				TileEntityWallShelf shelf = (TileEntityWallShelf)tileEntity;
				shelf.dropDisplayedItem(worldIn, pos);
				shelf.dropEmbeddedItems(worldIn, pos);
			}

			SurfaceDisplayBlocker.release(worldIn, pos);
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

		ShelfContentKind heldContent = this.getEmbeddedContentKindForItem(heldItem);
		TileEntityWallShelf shelf = this.getShelfEntity(worldIn, pos, false);

		if (shelf != null && shelf.getEmbeddedKind() == ShelfContentKind.FLOWER_POT) {
			if (this.canHandleFlowerPotClick(shelf, heldItem)) {
				if (worldIn.isRemote) {
					return true;
				}

				this.handleFlowerPotClick(worldIn, pos, shelf, playerIn, hand, heldItem);
			}

			return true;
		}

		if (heldContent != ShelfContentKind.NONE) {
			if (worldIn.isRemote) {
				return true;
			}

			if (shelf != null && shelf.hasEmbeddedContent()
					&& !this.canAddEmbeddedContentAt(worldIn, pos, shelf, heldContent)
					&& this.isSameShelfStack(shelf.getLastEmbeddedItem(), heldItem)) {
				this.removeLastEmbeddedItemFromShelf(worldIn, pos, shelf, playerIn);
				return true;
			}

			if (shelf == null || this.canAddEmbeddedContentAt(worldIn, pos, shelf, heldContent)) {
				shelf = this.getShelfEntity(worldIn, pos, true);

				if (shelf != null && this.canAddEmbeddedContentAt(worldIn, pos, shelf, heldContent)
						&& shelf.addEmbeddedContent(heldContent, heldItem)) {

					if (!playerIn.capabilities.isCreativeMode) {
						heldItem.stackSize--;

						if (heldItem.stackSize <= 0) {
							playerIn.setHeldItem(hand, null);
						}
					}
				}
			}

			return true;
		}

		if (this.isDisplayExcluded(heldItem)) {
			return false;
		}

		if (worldIn.isRemote) {
			return true;
		}

		shelf = this.getShelfEntity(worldIn, pos, false);

		if (shelf != null && shelf.hasDisplayedItem()
				&& VasePlantHelper.canHandleVaseClick(shelf.getDisplayedItem(), heldItem)) {
			this.handleDisplayedVaseClick(worldIn, pos, shelf, playerIn, hand, heldItem);
			return true;
		}

		if (shelf != null && shelf.hasDisplayedItem() && this.isSameShelfStack(shelf.getDisplayedItem(), heldItem)) {
			this.removeDisplayedItemFromShelf(worldIn, pos, shelf, playerIn);
			return true;
		}

		if ((shelf == null || !shelf.hasStoredData()) && heldItem != null && heldItem.stackSize > 0) {
			if (!this.reserveDisplaySpace(worldIn, pos, heldItem)) {
				return true;
			}

			shelf = this.getShelfEntity(worldIn, pos, true);

			if (shelf == null) {
				SurfaceDisplayBlocker.release(worldIn, pos);
				return true;
			}

			ItemStack displayedItem = heldItem.copy();
			displayedItem.stackSize = 1;
			shelf.setDisplayedItem(displayedItem, playerIn.getHorizontalFacing());

			if (!playerIn.capabilities.isCreativeMode) {
				heldItem.stackSize--;

				if (heldItem.stackSize <= 0) {
					playerIn.setHeldItem(hand, null);
				}
			}

			return true;
		}

		if (shelf != null && shelf.hasEmbeddedContent() && (heldItem == null || heldItem.stackSize <= 0)) {
			this.removeLastEmbeddedItemFromShelf(worldIn, pos, shelf, playerIn);
			return true;
		}

		if (shelf != null && shelf.hasDisplayedItem() && (heldItem == null || heldItem.stackSize <= 0)) {
			this.removeDisplayedItemFromShelf(worldIn, pos, shelf, playerIn);
			return true;
		}

		return true;
	}

	private boolean isSameShelfStack(ItemStack storedItem, ItemStack heldItem) {
		return storedItem != null && heldItem != null && heldItem.stackSize > 0
			&& storedItem.isItemEqual(heldItem)
			&& ItemStack.areItemStackTagsEqual(storedItem, heldItem);
	}

	private void removeLastEmbeddedItemFromShelf(World worldIn, BlockPos pos, TileEntityWallShelf shelf,
			EntityPlayer playerIn) {
		ItemStack embeddedItem = shelf.removeLastEmbeddedItem();

		if (embeddedItem != null) {
			this.returnShelfItem(worldIn, pos, playerIn, embeddedItem);
		}

		this.removeShelfEntityIfEmpty(worldIn, pos);
	}

	private void removeDisplayedItemFromShelf(World worldIn, BlockPos pos, TileEntityWallShelf shelf,
			EntityPlayer playerIn) {
		ItemStack displayedItem = shelf.removeDisplayedItem();
		SurfaceDisplayBlocker.release(worldIn, pos);

		if (displayedItem != null) {
			this.returnShelfItem(worldIn, pos, playerIn, displayedItem);
		}

		this.removeShelfEntityIfEmpty(worldIn, pos);
	}

	private void handleDisplayedVaseClick(World worldIn, BlockPos pos, TileEntityWallShelf shelf,
			EntityPlayer playerIn, EnumHand hand, ItemStack heldItem) {
		ItemStack displayedItem = shelf.getDisplayedItem();
		ItemStack plant = VasePlantHelper.removePlant(displayedItem);

		if (plant != null) {
			shelf.setDisplayedItem(displayedItem);
			this.returnShelfItem(worldIn, pos, playerIn, plant);
			return;
		}

		if (VasePlantHelper.addPlant(displayedItem, heldItem)) {
			shelf.setDisplayedItem(displayedItem);

			if (!playerIn.capabilities.isCreativeMode) {
				heldItem.stackSize--;

				if (heldItem.stackSize <= 0) {
					playerIn.setHeldItem(hand, null);
				}
			}
		}
	}

	private boolean canHandleFlowerPotClick(TileEntityWallShelf shelf, ItemStack heldItem) {
		ItemStack lastItem = shelf.getLastEmbeddedItem();

		if (lastItem != null && !this.isSameShelfStack(lastItem, shelf.getFirstEmbeddedItem())) {
			return heldItem == null || heldItem.stackSize <= 0 || this.isSameShelfStack(lastItem, heldItem);
		}

		return heldItem == null || heldItem.stackSize <= 0
			|| this.isSameShelfStack(lastItem, heldItem)
			|| this.isFlowerPotPlantItem(heldItem);
	}

	private void handleFlowerPotClick(World worldIn, BlockPos pos, TileEntityWallShelf shelf,
			EntityPlayer playerIn, EnumHand hand, ItemStack heldItem) {
		ItemStack lastItem = shelf.getLastEmbeddedItem();

		if (lastItem != null && !this.isSameShelfStack(lastItem, shelf.getFirstEmbeddedItem())) {
			this.removeLastEmbeddedItemFromShelf(worldIn, pos, shelf, playerIn);
			return;
		}

		if (this.isFlowerPotPlantItem(heldItem) && shelf.addEmbeddedContent(ShelfContentKind.FLOWER_POT, heldItem)) {
			if (!playerIn.capabilities.isCreativeMode) {
				heldItem.stackSize--;

				if (heldItem.stackSize <= 0) {
					playerIn.setHeldItem(hand, null);
				}
			}

			return;
		}

		this.removeLastEmbeddedItemFromShelf(worldIn, pos, shelf, playerIn);
	}

	private void returnShelfItem(World worldIn, BlockPos pos, EntityPlayer playerIn, ItemStack itemStack) {
		if (!playerIn.inventory.addItemStackToInventory(itemStack)) {
			EntityItem entityItem = new EntityItem(worldIn, pos.getX() + 0.5D, pos.getY() + 0.9D,
				pos.getZ() + 0.5D, itemStack);
			worldIn.spawnEntity(entityItem);
		}
	}

	private boolean isDisplayExcluded(ItemStack heldItem) {
		if (heldItem == null || heldItem.stackSize <= 0) {
			return false;
		}

		Block heldBlock = this.getHeldItemBlock(heldItem);
		return this.isItemFromBlock(heldItem, BlockObjectHolder.light_metal_ironage_block_floor_red_clear)
			|| this.hasRegistryPath(heldBlock, "light_metal_ironage_block_floor_red_clear")
			|| heldBlock instanceof LightSourceRed;
	}

	private boolean reserveDisplaySpace(World worldIn, BlockPos pos, ItemStack heldItem) {
		return this.requiresClearBlockAbove(heldItem)
			? SurfaceDisplayBlocker.reserve(worldIn, pos)
			: SurfaceDisplayBlocker.reserveForShelf(worldIn, pos);
	}

	private boolean requiresClearBlockAbove(ItemStack heldItem) {
		Block heldBlock = this.getHeldItemBlock(heldItem);

		if (heldBlock instanceof OrnamentBlock) {
			String modelName = ((OrnamentBlock)heldBlock).getModelName(heldItem.getMetadata());
			return modelName.contains("_urn");
		}

		return false;
	}

	private ShelfContentKind getEmbeddedContentKindForItem(ItemStack heldItem) {
		if (heldItem == null || heldItem.stackSize <= 0) {
			return ShelfContentKind.NONE;
		}

		Block heldBlock = this.getHeldItemBlock(heldItem);

		if (this.isItemFromBlock(heldItem, BlockObjectHolder.light_metal_ironage_block_floor_lava_clear)) {
			return ShelfContentKind.LAVA;
		}

		if (this.hasRegistryPath(heldBlock, "light_metal_ironage_block_floor_lava_clear")
				|| heldBlock instanceof LightSourceLava) {
			return ShelfContentKind.LAVA;
		}

		if (heldBlock instanceof LightSourceRed) {
			return ShelfContentKind.NONE;
		}

		if (this.isItemFromBlock(heldItem, BlockObjectHolder.light_metal_ironage_block_floor_glow_clear)) {
			return ShelfContentKind.GLOW;
		}

		if (this.hasRegistryPath(heldBlock, "light_metal_ironage_block_floor_glow_clear")
				|| heldBlock instanceof LightSourceGlowdust) {
			return ShelfContentKind.GLOW;
		}

		if (this.isItemFromBlock(heldItem, BlockObjectHolder.light_metal_ironage_candle_floor)) {
			return ShelfContentKind.CANDLE;
		}

		if (this.hasRegistryPath(heldBlock, "light_metal_ironage_candle_floor")
				|| heldBlock instanceof LightSourceCandleFloor) {
			return ShelfContentKind.CANDLE;
		}

		if (this.isFlowerPotItem(heldItem)) {
			return ShelfContentKind.FLOWER_POT;
		}

		Item item = heldItem.getItem();

		if (item == Items.BOOK || item == Items.WRITABLE_BOOK || item == Items.WRITTEN_BOOK
				|| item == Items.ENCHANTED_BOOK) {
			return ShelfContentKind.BOOKS;
		}

		if (item instanceof ItemRecord) {
			return ShelfContentKind.RECORDS;
		}

		return ShelfContentKind.NONE;
	}

	private boolean isFlowerPotPlantItem(ItemStack heldItem) {
		Block heldBlock = this.getHeldItemBlock(heldItem);
		return heldBlock == Blocks.RED_FLOWER
			|| heldBlock == Blocks.YELLOW_FLOWER
			|| heldBlock == Blocks.SAPLING
			|| heldBlock == Blocks.BROWN_MUSHROOM
			|| heldBlock == Blocks.RED_MUSHROOM
			|| heldBlock == Blocks.DEADBUSH
			|| heldBlock == Blocks.CACTUS;
	}

	private boolean isFlowerPotItem(ItemStack heldItem) {
		return heldItem != null && heldItem.stackSize > 0
			&& heldItem.getItem() == Items.FLOWER_POT;
	}

	private Block getHeldItemBlock(ItemStack heldItem) {
		return heldItem != null && heldItem.getItem() instanceof ItemBlock
			? ((ItemBlock)heldItem.getItem()).getBlock() : null;
	}

	private boolean hasRegistryPath(Block block, String path) {
		return block != null && block.getRegistryName() != null
			&& path.equals(block.getRegistryName().getResourcePath());
	}

	private boolean isItemFromBlock(ItemStack heldItem, Block block) {
		return block != null && heldItem.getItem() == Item.getItemFromBlock(block);
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
		return state.getValue(DATA).booleanValue();
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return state.getValue(DATA).booleanValue() ? new TileEntityWallShelf() : null;
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
	public float getAmbientOcclusionLightValue(IBlockState state) {
		return state.getValue(CONTENTS) == ShelfContents.NONE ? super.getAmbientOcclusionLightValue(state) : 1.0F;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return this.getBoardBox(state, source, pos);
	}

	@Override
	public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World worldIn, BlockPos pos) {
		return this.getBoardBox(state, worldIn, pos).offset(pos);
	}

	@Override
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox,
			List<AxisAlignedBB> collidingBoxes, Entity entityIn) {
		ShelfRenderState renderState = this.getRenderState(worldIn, pos, state.getValue(FACING));
		super.addCollisionBoxToList(pos, entityBox, collidingBoxes, this.getBoardBox(renderState));
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState()
			.withProperty(FACING, EnumFacing.getHorizontal(meta & 3))
			.withProperty(CONTENTS, ShelfContents.NONE)
			.withProperty(DATA, Boolean.valueOf((meta & 4) != 0));
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		int meta = state.getValue(FACING).getHorizontalIndex();
		return state.getValue(DATA).booleanValue() ? meta | 4 : meta;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { FACING, SUPPORT, CONTENTS, DATA });
	}

	@Override
	public int getLightValue(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		return this.getShelfContents(worldIn, pos).getLightLevel();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
		if (this.getShelfContents(world, pos) != ShelfContents.CANDLE) {
			return;
		}

		ShelfRenderState renderState = this.getRenderState(world, pos, state.getValue(FACING));
		double[] flamePoint = this.rotateShelfPoint(renderState.facing, 8.0D / 16.0D, 8.5D / 16.0D);
		double x = pos.getX() + flamePoint[0];
		double y = pos.getY() + 19.7D / 16.0D;
		double z = pos.getZ() + flamePoint[1];

		if (rand.nextInt(3) == 0) {
			world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x, y + 0.04D, z, 0.0D, 0.0D, 0.0D);
		}

		CandleFlameParticle.spawn(world, x, y, z);
	}

	private double[] rotateShelfPoint(EnumFacing facing, double x, double z) {
		switch (facing) {
		case EAST:
			return new double[] { 1.0D - z, x };
		case SOUTH:
			return new double[] { 1.0D - x, 1.0D - z };
		case WEST:
			return new double[] { z, 1.0D - x };
		default:
			return new double[] { x, z };
		}
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

	private AxisAlignedBB getBoardBox(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		ShelfRenderState renderState = this.getRenderState(worldIn, pos, state.getValue(FACING));
		return this.getBoardBox(renderState);
	}

	private AxisAlignedBB getBoardBox(ShelfRenderState renderState) {
		switch (renderState.support) {
		case INNER_CORNER:
			return this.rotateToFacing(BOARD_INNER_CORNER_NORTH, renderState.facing);
		case OUTER_CORNER:
			return this.rotateToFacing(BOARD_OUTER_CORNER_NORTH, renderState.facing);
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

	private TileEntityWallShelf getShelfEntity(World worldIn, BlockPos pos, boolean create) {
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

		if (tileEntity instanceof TileEntityWallShelf) {
			return (TileEntityWallShelf)tileEntity;
		}

		if (!create || worldIn.isRemote) {
			return null;
		}

		tileEntity = this.createTileEntity(worldIn, state);

		if (tileEntity != null) {
			worldIn.setTileEntity(pos, tileEntity);
		}

		return tileEntity instanceof TileEntityWallShelf ? (TileEntityWallShelf)tileEntity : null;
	}

	private void removeShelfEntityIfEmpty(World worldIn, BlockPos pos) {
		if (worldIn.isRemote) {
			return;
		}

		TileEntity tileEntity = worldIn.getTileEntity(pos);

		if (!(tileEntity instanceof TileEntityWallShelf)) {
			return;
		}

		TileEntityWallShelf shelf = (TileEntityWallShelf)tileEntity;

		if (shelf.hasStoredData()) {
			return;
		}

		SurfaceDisplayBlocker.release(worldIn, pos);

		IBlockState state = worldIn.getBlockState(pos);

		if (state.getBlock() == this && state.getValue(DATA).booleanValue()) {
			worldIn.setBlockState(pos, state.withProperty(DATA, Boolean.valueOf(false)), 2);
		} else {
			worldIn.removeTileEntity(pos);
		}
	}

	private void refreshDisplayBlocker(World worldIn, BlockPos pos) {
		if (worldIn.isRemote) {
			return;
		}

		TileEntityWallShelf shelf = this.getShelfEntity(worldIn, pos, false);

		if (shelf != null && shelf.hasDisplayedItem()) {
			this.reserveDisplaySpace(worldIn, pos, shelf.getDisplayedItem());
		}
	}

	private ShelfContents getShelfContents(IBlockAccess worldIn, BlockPos pos) {
		TileEntity tileEntity = worldIn.getTileEntity(pos);
		return tileEntity instanceof TileEntityWallShelf
			? ((TileEntityWallShelf)tileEntity).getShelfContents() : ShelfContents.NONE;
	}

	private boolean canAddEmbeddedContentAt(IBlockAccess worldIn, BlockPos pos, TileEntityWallShelf shelf,
			ShelfContentKind kind) {
		if (shelf == null) {
			return kind != null && kind != ShelfContentKind.NONE
				&& this.getEmbeddedCapacityForPosition(worldIn, pos, kind) > 0;
		}

		return shelf.canAddEmbeddedContent(kind)
			&& shelf.getEmbeddedCount() < this.getEmbeddedCapacityForPosition(worldIn, pos, kind);
	}

	private int getEmbeddedCapacityForPosition(IBlockAccess worldIn, BlockPos pos, ShelfContentKind kind) {
		if (kind != ShelfContentKind.BOOKS) {
			return kind.getCapacity();
		}

		IBlockState state = worldIn.getBlockState(pos);

		if (state.getBlock() != this) {
			return kind.getCapacity();
		}

		ShelfRenderState renderState = this.getRenderState(worldIn, pos, state.getValue(FACING));

		return renderState.support == ShelfSupport.INNER_CORNER || renderState.support == ShelfSupport.OUTER_CORNER
			? Math.min(4, kind.getCapacity()) : kind.getCapacity();
	}
}
