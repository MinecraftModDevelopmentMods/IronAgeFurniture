package com.mcmoddev.ironagefurniture.api.Blocks;

import java.util.Random;

import com.mcmoddev.ironagefurniture.BlockObjectHolder;
import com.mcmoddev.ironagefurniture.api.tile.TileEntityDiningTable;
import com.mcmoddev.ironagefurniture.api.tile.TileEntityWallShelf;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class SurfaceDisplayBlocker extends Block {

	public SurfaceDisplayBlocker(Material materialIn, String name) {
		super(materialIn);
		this.setBlockUnbreakable();
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
			return true;
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

		if (worldIn.getBlockState(blockerPos).getBlock() == BlockObjectHolder.surface_display_blocker) {
			worldIn.setBlockState(blockerPos, Blocks.AIR.getDefaultState(), 2);
		}
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
		return false;
	}

	@Override
	public RayTraceResult collisionRayTrace(IBlockState blockState, World worldIn, BlockPos pos, Vec3d start,
			Vec3d end) {
		return null;
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
		if (!worldIn.isRemote && !this.hasValidDisplayedItemOwner(worldIn, pos)) {
			worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 2);
		}
	}

	private boolean hasValidDisplayedItemOwner(World worldIn, BlockPos pos) {
		BlockPos surfacePos = pos.down();
		IBlockState surfaceState = worldIn.getBlockState(surfacePos);
		Block surfaceBlock = surfaceState.getBlock();

		if (!(surfaceBlock instanceof DiningTable) && !(surfaceBlock instanceof WallShelf)) {
			return false;
		}

		TileEntity tileEntity = worldIn.getTileEntity(surfacePos);

		if (tileEntity instanceof TileEntityDiningTable) {
			return ((TileEntityDiningTable)tileEntity).hasDisplayedItem();
		}

		return tileEntity instanceof TileEntityWallShelf
			&& ((TileEntityWallShelf)tileEntity).hasDisplayedItem();
	}
}
