package com.mcmoddev.ironagefurniture.api.tile;

import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class TileEntityBarrel extends TileEntity {
	public static final int CAPACITY = 16 * Fluid.BUCKET_VOLUME;

	private final FluidTank tank;

	public TileEntityBarrel() {
		this.tank = new BarrelFluidTank(this, CAPACITY);
		this.tank.setTileEntity(this);
	}

	public IFluidHandler getFluidHandler() {
		return this.tank;
	}

	@Nullable
	public FluidStack getFluid() {
		FluidStack fluid = this.tank.getFluid();
		return fluid == null ? null : fluid.copy();
	}

	public int getFluidAmount() {
		return this.tank.getFluidAmount();
	}

	public int getCapacity() {
		return this.tank.getCapacity();
	}

	public boolean isEmpty() {
		return this.getFluidAmount() <= 0;
	}

	public boolean isUsableByPlayer(EntityPlayer player) {
		return this.world.getTileEntity(this.pos) != this ? false
			: player.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D,
				(double)this.pos.getZ() + 0.5D) <= 64.0D;
	}

	public static boolean isAllowedFluid(@Nullable FluidStack fluid) {
		return fluid != null && fluid.getFluid() != null;
	}

	public void markForFluidUpdate() {
		this.markDirty();

		if (this.world != null && !this.world.isRemote && this.pos != null) {
			IBlockState state = this.world.getBlockState(this.pos);
			this.world.notifyBlockUpdate(this.pos, state, state, 3);
			this.world.updateComparatorOutputLevel(this.pos, state.getBlock());
		}
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY
			|| super.hasCapability(capability, facing);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			return (T)this.tank;
		}

		return super.getCapability(capability, facing);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);

		if (compound.hasKey("Tank", 10)) {
			this.tank.readFromNBT(compound.getCompoundTag("Tank"));
		} else {
			this.tank.setFluid(null);
		}

		this.sanitizeFluid();
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setTag("Tank", this.tank.writeToNBT(new NBTTagCompound()));
		return compound;
	}

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
		return oldState.getBlock() != newState.getBlock();
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(this.pos, 0, this.getUpdateTag());
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		this.readFromNBT(pkt.getNbtCompound());
		this.refreshRender();
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		return this.writeToNBT(new NBTTagCompound());
	}

	private void sanitizeFluid() {
		FluidStack fluid = this.tank.getFluid();

		if (fluid == null) {
			return;
		}

		if (!isAllowedFluid(fluid) || fluid.amount <= 0) {
			this.tank.setFluid(null);
			return;
		}

		if (fluid.amount > this.tank.getCapacity()) {
			fluid.amount = this.tank.getCapacity();
		}
	}

	private void refreshRender() {
		if (this.world != null && this.world.isRemote && this.pos != null) {
			this.world.markBlockRangeForRenderUpdate(this.pos, this.pos);
		}
	}

	private static class BarrelFluidTank extends FluidTank {
		private final TileEntityBarrel owner;

		BarrelFluidTank(TileEntityBarrel owner, int capacity) {
			super(capacity);
			this.owner = owner;
		}

		@Override
		public boolean canFillFluidType(FluidStack fluid) {
			return TileEntityBarrel.isAllowedFluid(fluid);
		}

		@Override
		protected void onContentsChanged() {
			this.owner.markForFluidUpdate();
		}
	}
}
