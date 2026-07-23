package com.mcmoddev.ironagefurniture.api.Blocks;

import java.util.Random;

import com.mcmoddev.ironagefurniture.BlockObjectHolder;
import com.mcmoddev.ironagefurniture.api.Items.DrinkContainerHelper;
import com.mcmoddev.ironagefurniture.api.Items.ItemDrinkware;
import com.mcmoddev.ironagefurniture.api.tile.TileEntityCabinet;
import com.mcmoddev.ironagefurniture.api.tile.TileEntityDiningTable;
import com.mcmoddev.ironagefurniture.api.tile.TileEntitySurfaceDisplay;
import com.mcmoddev.ironagefurniture.api.tile.TileEntityWallShelf;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class SurfaceDisplayBlocker extends Block {
	public static final PropertyBool STANDALONE = PropertyBool.create("standalone");
	private static final AxisAlignedBB DISPLAY_SELECTION_AABB =
		new AxisAlignedBB(0.25D, 0.0D, 0.25D, 0.75D, 0.75D, 0.75D);

	public SurfaceDisplayBlocker(Material materialIn, String name) {
		super(materialIn);
		this.setBlockUnbreakable();
		this.setDefaultState(this.blockState.getBaseState().withProperty(STANDALONE, Boolean.FALSE));
	}

	public static boolean reserve(World worldIn, BlockPos surfacePos) {
		return reserve(worldIn, surfacePos, false);
	}

	public static boolean reserveForShelf(World worldIn, BlockPos surfacePos) {
		return reserve(worldIn, surfacePos, true);
	}

	private static boolean reserve(World worldIn, BlockPos surfacePos, boolean allowShelfAbove) {
		if (worldIn.isRemote) {
			return true;
		}

		if (BlockObjectHolder.surface_display_blocker == null) {
			return false;
		}

		BlockPos blockerPos = surfacePos.up();
		IBlockState blockerState = worldIn.getBlockState(blockerPos);
		Block blocker = blockerState.getBlock();

		if (blocker == BlockObjectHolder.surface_display_blocker) {
			return !blockerState.getValue(STANDALONE).booleanValue();
		}

		if (allowShelfAbove && blocker instanceof WallShelf) {
			return true;
		}

		if (!blocker.isReplaceable(worldIn, blockerPos)) {
			return false;
		}

		return worldIn.setBlockState(blockerPos, BlockObjectHolder.surface_display_blocker.getDefaultState(), 2);
	}

	public static void release(World worldIn, BlockPos surfacePos) {
		if (worldIn.isRemote || BlockObjectHolder.surface_display_blocker == null) {
			return;
		}

		BlockPos blockerPos = surfacePos.up();

		IBlockState blockerState = worldIn.getBlockState(blockerPos);

		if (blockerState.getBlock() == BlockObjectHolder.surface_display_blocker
				&& !blockerState.getValue(STANDALONE).booleanValue()) {
			worldIn.setBlockState(blockerPos, Blocks.AIR.getDefaultState(), 2);
		}
	}

	public static boolean placeDrinkware(World worldIn, BlockPos surfacePos, ItemStack itemStack,
			EnumFacing displayedFacing) {
		if (!(BlockObjectHolder.surface_display_blocker instanceof SurfaceDisplayBlocker)
				|| itemStack == null || itemStack.stackSize <= 0
				|| !(itemStack.getItem() instanceof ItemDrinkware)
				|| !worldIn.getBlockState(surfacePos).isSideSolid(worldIn, surfacePos, EnumFacing.UP)) {
			return false;
		}

		BlockPos displayPos = surfacePos.up();
		IBlockState existingState = worldIn.getBlockState(displayPos);

		if (!existingState.getBlock().isReplaceable(worldIn, displayPos)) {
			return false;
		}

		if (worldIn.isRemote) {
			return true;
		}

		SurfaceDisplayBlocker blocker = (SurfaceDisplayBlocker)BlockObjectHolder.surface_display_blocker;
		IBlockState displayState = blocker.getDefaultState().withProperty(STANDALONE, Boolean.TRUE);

		if (!worldIn.setBlockState(displayPos, displayState, 3)) {
			return false;
		}

		TileEntity tileEntity = worldIn.getTileEntity(displayPos);

		if (!(tileEntity instanceof TileEntitySurfaceDisplay)) {
			worldIn.setBlockToAir(displayPos);
			return false;
		}

		ItemStack displayedItem = itemStack.copy();
		displayedItem.stackSize = 1;
		((TileEntitySurfaceDisplay)tileEntity).setDisplayedItem(displayedItem, displayedFacing);
		return true;
	}

	@Override
	public boolean isReplaceable(IBlockAccess worldIn, BlockPos pos) {
		return false;
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
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.INVISIBLE;
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World worldIn, BlockPos pos) {
		return NULL_AABB;
	}

	@Override
	public boolean canCollideCheck(IBlockState state, boolean hitIfLiquid) {
		return state.getValue(STANDALONE).booleanValue();
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return state.getValue(STANDALONE).booleanValue() ? DISPLAY_SELECTION_AABB : FULL_BLOCK_AABB;
	}

	@Override
	public RayTraceResult collisionRayTrace(IBlockState blockState, World worldIn, BlockPos pos, Vec3d start,
			Vec3d end) {
		return blockState.getValue(STANDALONE).booleanValue()
			? super.collisionRayTrace(blockState, worldIn, pos, start, end) : null;
	}

	@Override
	public int quantityDropped(Random random) {
		return 0;
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return null;
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn) {
		if (!worldIn.isRemote && !this.hasValidDisplayedItemOwner(worldIn, pos, state)) {
			worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 2);
		}
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (!state.getValue(STANDALONE).booleanValue() || playerIn.isSneaking()) {
			return false;
		}

		TileEntity tileEntity = worldIn.getTileEntity(pos);

		if (!(tileEntity instanceof TileEntitySurfaceDisplay)) {
			return false;
		}

		TileEntitySurfaceDisplay display = (TileEntitySurfaceDisplay)tileEntity;
		ItemStack displayedItem = display.getDisplayedItem();
		boolean canFill = DrinkContainerHelper.canFillDisplayedDrinkware(displayedItem, heldItem);
		boolean canRetrieve = this.canRetrieveDisplayedItem(displayedItem, heldItem);

		if (!canFill && !canRetrieve) {
			return false;
		}

		if (worldIn.isRemote) {
			return true;
		}

		if (canFill) {
			ItemStack filledItem = DrinkContainerHelper.fillDisplayedDrinkware(displayedItem, heldItem,
				playerIn, hand);

			if (filledItem != null) {
				display.setDisplayedItem(filledItem, display.getDisplayedFacing());
			}

			return true;
		}

		ItemStack removedItem = display.removeDisplayedItem();
		worldIn.setBlockToAir(pos);
		DrinkContainerHelper.giveOrDrop(playerIn, removedItem);
		return true;
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return state.getValue(STANDALONE).booleanValue();
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return state.getValue(STANDALONE).booleanValue() ? new TileEntitySurfaceDisplay() : null;
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		if (!worldIn.isRemote && state.getValue(STANDALONE).booleanValue()) {
			TileEntity tileEntity = worldIn.getTileEntity(pos);

			if (tileEntity instanceof TileEntitySurfaceDisplay) {
				ItemStack displayedItem = ((TileEntitySurfaceDisplay)tileEntity).removeDisplayedItem();

				if (displayedItem != null) {
					Block.spawnAsEntity(worldIn, pos, displayedItem);
				}
			}
		}

		super.breakBlock(worldIn, pos, state);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(STANDALONE, Boolean.valueOf((meta & 1) != 0));
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(STANDALONE).booleanValue() ? 1 : 0;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { STANDALONE });
	}

	private boolean hasValidDisplayedItemOwner(World worldIn, BlockPos pos, IBlockState state) {
		if (state.getValue(STANDALONE).booleanValue()) {
			TileEntity tileEntity = worldIn.getTileEntity(pos);
			BlockPos surfacePos = pos.down();
			return tileEntity instanceof TileEntitySurfaceDisplay
				&& ((TileEntitySurfaceDisplay)tileEntity).hasDisplayedItem()
				&& worldIn.getBlockState(surfacePos).isSideSolid(worldIn, surfacePos, EnumFacing.UP);
		}

		BlockPos surfacePos = pos.down();
		IBlockState surfaceState = worldIn.getBlockState(surfacePos);
		Block surfaceBlock = surfaceState.getBlock();

		if (!(surfaceBlock instanceof DiningTable) && !(surfaceBlock instanceof WallShelf)
				&& !(surfaceBlock instanceof Cabinet)) {
			return false;
		}

		TileEntity tileEntity = worldIn.getTileEntity(surfacePos);

		if (tileEntity instanceof TileEntityDiningTable) {
			return ((TileEntityDiningTable)tileEntity).hasDisplayedItem();
		}

		if (tileEntity instanceof TileEntityCabinet) {
			return ((TileEntityCabinet)tileEntity).hasDisplayedItem();
		}

		return tileEntity instanceof TileEntityWallShelf
			&& ((TileEntityWallShelf)tileEntity).hasDisplayedItem();
	}

	private boolean canRetrieveDisplayedItem(ItemStack displayedItem, ItemStack heldItem) {
		if (displayedItem == null || displayedItem.stackSize <= 0) {
			return false;
		}

		if (heldItem == null || heldItem.stackSize <= 0) {
			return true;
		}

		return ItemStack.areItemsEqual(displayedItem, heldItem)
			&& ItemStack.areItemStackTagsEqual(displayedItem, heldItem);
	}
}
