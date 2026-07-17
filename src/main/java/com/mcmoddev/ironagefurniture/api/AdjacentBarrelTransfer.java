package com.mcmoddev.ironagefurniture.api;

import com.mcmoddev.ironagefurniture.api.tile.TileEntityBarrel;
import com.mcmoddev.ironagefurniture.api.tile.TileEntityFoudre;

import javax.annotation.Nullable;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;

public final class AdjacentBarrelTransfer {
	private AdjacentBarrelTransfer() {
		throw new IllegalAccessError("This class cannot be instantiated");
	}

	public static boolean transfer(World world, BlockPos basePos, EnumFacing facing, IFluidHandler machineHandler,
			boolean barrelToMachine, boolean doTransfer) {
		if (world == null || basePos == null || facing == null || machineHandler == null) {
			return false;
		}

		EnumFacing front = facing.getOpposite();
		BlockPos[] positions = new BlockPos[] {
			basePos.offset(front),
			basePos.offset(facing.rotateYCCW()).offset(front) };

		for (BlockPos pos : positions) {
			TileEntityBarrel barrel = getNormalBarrel(world, pos);

			if (barrel == null) {
				continue;
			}

			IFluidHandler barrelHandler = barrel.getFluidHandler();
			IFluidHandler source = barrelToMachine ? barrelHandler : machineHandler;
			IFluidHandler target = barrelToMachine ? machineHandler : barrelHandler;

			if (transfer(source, target, doTransfer)) {
				if (doTransfer) {
					barrel.markForFluidUpdate();
				}

				return true;
			}
		}

		return false;
	}

	@Nullable
	public static TileEntityBarrel findBarrel(World world, BlockPos basePos, EnumFacing facing) {
		if (world == null || basePos == null || facing == null) {
			return null;
		}

		EnumFacing front = facing.getOpposite();
		TileEntityBarrel barrel = getNormalBarrel(world, basePos.offset(front));

		if (barrel != null) {
			return barrel;
		}

		return getNormalBarrel(world, basePos.offset(facing.rotateYCCW()).offset(front));
	}

	private static boolean transfer(IFluidHandler source, IFluidHandler target, boolean doTransfer) {
		FluidStack transferred = FluidUtil.tryFluidTransfer(target, source, Integer.MAX_VALUE, doTransfer);
		return transferred != null && transferred.getFluid() != null && transferred.amount > 0;
	}

	private static TileEntityBarrel getNormalBarrel(World world, BlockPos pos) {
		TileEntity tileEntity = world.getTileEntity(pos);

		if (!(tileEntity instanceof TileEntityBarrel) || tileEntity instanceof TileEntityFoudre) {
			return null;
		}

		return (TileEntityBarrel)tileEntity;
	}
}
