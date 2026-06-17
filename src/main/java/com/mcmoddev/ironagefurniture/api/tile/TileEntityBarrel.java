package com.mcmoddev.ironagefurniture.api.tile;

import javax.annotation.Nullable;

import com.mcmoddev.ironagefurniture.api.FoudreBrewingRegistry;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

public class TileEntityBarrel extends TileEntity implements ITickable, net.minecraftforge.fluids.IFluidHandler {
	public static final int CAPACITY = 16 * Fluid.BUCKET_VOLUME;
	public static final int FIELD_AGE_PROGRESS = 0;
	public static final int FIELD_AGE_PROGRESS_TOTAL = 1;
	public static final int FIELD_SEALED = 2;
	public static final String TANK_TAG = "Tank";

	private static final String SEALED_TAG = "Sealed";

	private final FluidTank tank;
	private final IFluidHandler sealedFluidHandler = new SealedBarrelFluidHandler();
	private boolean sealed;
	private int clientAgeProgress;
	private int clientAgeProgressTotal;

	public TileEntityBarrel() {
		this(CAPACITY);
	}

	protected TileEntityBarrel(int capacity) {
		this.tank = new BarrelFluidTank(this, capacity);
		this.tank.setTileEntity(this);
	}

	@Override
	public void update() {
		if (this.world == null || this.world.isRemote || !this.shouldAge()) {
			return;
		}

		FluidStack fluid = this.getFluidDirect();

		if (FoudreBrewingRegistry.ageFluid(fluid, 1)) {
			this.markForFluidUpdate();
		} else if (this.world.getTotalWorldTime() % 20L == 0L) {
			this.markDirty();
		}
	}

	public IFluidHandler getFluidHandler() {
		return this.sealedFluidHandler;
	}

	@Nullable
	public FluidStack getFluid() {
		FluidStack fluid = this.tank.getFluid();
		return fluid == null ? null : fluid.copy();
	}

	@Nullable
	protected FluidStack getFluidDirect() {
		return this.tank.getFluid();
	}

	public int getFluidAmount() {
		return this.tank.getFluidAmount();
	}

	public int getCapacity() {
		return this.tank.getCapacity();
	}

	protected void setFluid(@Nullable FluidStack fluid) {
		this.tank.setFluid(fluid == null ? null : fluid.copy());
		this.sanitizeFluid();
		this.markForFluidUpdate();
	}

	public String getContainerNameKey() {
		return "container.ironagefurniture.barrel";
	}

	public boolean isEmpty() {
		return this.getFluidAmount() <= 0;
	}

	public boolean isSealed() {
		return this.sealed && !this.isEmpty();
	}

	public boolean canSeal() {
		return !this.sealed && this.getFluidAmount() > 0;
	}

	public void setSealed(boolean sealed) {
		if (sealed && !this.sealed && !this.canSeal()) {
			return;
		}

		if (this.sealed == sealed) {
			return;
		}

		boolean opening = this.sealed && !sealed;
		this.sealed = sealed;

		if (opening) {
			FoudreBrewingRegistry.resetAgeProgressToCurrentLevel(this.getFluidDirect());
		}

		this.markForFluidUpdate();
	}

	public void toggleSealed() {
		this.setSealed(!this.sealed);
	}

	public int getAgeProgress() {
		if (this.world != null && this.world.isRemote) {
			return this.clientAgeProgress;
		}

		return FoudreBrewingRegistry.getAgeProgress(this.getFluidDirect());
	}

	public int getAgeProgressTotal() {
		if (this.world != null && this.world.isRemote) {
			return this.clientAgeProgressTotal;
		}

		return FoudreBrewingRegistry.getAgeProgressTotal(this.getFluidDirect());
	}

	public String getNextAgeLevelName() {
		return FoudreBrewingRegistry.getNextAgeLevelName(this.getFluid());
	}

	public boolean isUsableByPlayer(EntityPlayer player) {
		return this.world.getTileEntity(this.pos) != this ? false
			: player.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D,
				(double)this.pos.getZ() + 0.5D) <= 64.0D;
	}

	public static boolean isAllowedFluid(@Nullable FluidStack fluid) {
		return fluid != null && fluid.getFluid() != null;
	}

	@Nullable
	public static FluidStack getFluidFromItemStack(ItemStack stack) {
		NBTTagCompound tankTag = getTankTag(stack);
		return tankTag == null ? null : FluidStack.loadFluidStackFromNBT(tankTag);
	}

	public void writeToItemStack(ItemStack stack) {
		if (stack == null) {
			return;
		}

		NBTTagCompound tag = stack.hasTagCompound() ? stack.getTagCompound() : new NBTTagCompound();

		if (this.isEmpty()) {
			removeTankTag(stack);
			tag = stack.hasTagCompound() ? stack.getTagCompound() : new NBTTagCompound();
		} else {
			tag.setTag(TANK_TAG, this.tank.writeToNBT(new NBTTagCompound()));
		}

		if (this.sealed && !this.isEmpty()) {
			tag.setBoolean(SEALED_TAG, true);
		} else {
			tag.removeTag(SEALED_TAG);
		}

		if (tag.hasNoTags()) {
			stack.setTagCompound(null);
			return;
		}

		stack.setTagCompound(tag);
	}

	public void readFromItemStack(ItemStack stack) {
		NBTTagCompound tankTag = getTankTag(stack);

		if (tankTag == null) {
			this.tank.setFluid(null);
		} else {
			this.tank.readFromNBT(tankTag);
		}

		this.sanitizeFluid();
		this.sealed = stack != null && stack.hasTagCompound() && stack.getTagCompound().getBoolean(SEALED_TAG);
		this.clearSealIfEmpty();
		this.markForFluidUpdate();
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
			return (T)this.getFluidHandler();
		}

		return super.getCapability(capability, facing);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);

		if (compound.hasKey(TANK_TAG, 10)) {
			this.tank.readFromNBT(compound.getCompoundTag(TANK_TAG));
		} else {
			this.tank.setFluid(null);
		}

		this.sanitizeFluid();
		this.sealed = compound.getBoolean(SEALED_TAG);
		this.clearSealIfEmpty();
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setTag(TANK_TAG, this.tank.writeToNBT(new NBTTagCompound()));
		compound.setBoolean(SEALED_TAG, this.sealed && !this.isEmpty());
		return compound;
	}

	public int getField(int id) {
		switch (id) {
		case FIELD_AGE_PROGRESS:
			return this.getAgeProgress();
		case FIELD_AGE_PROGRESS_TOTAL:
			return this.getAgeProgressTotal();
		case FIELD_SEALED:
			return this.isSealed() ? 1 : 0;
		default:
			return 0;
		}
	}

	public void setField(int id, int value) {
		switch (id) {
		case FIELD_AGE_PROGRESS:
			this.clientAgeProgress = value;
			break;
		case FIELD_AGE_PROGRESS_TOTAL:
			this.clientAgeProgressTotal = value;
			break;
		case FIELD_SEALED:
			this.sealed = value != 0;
			break;
		default:
			break;
		}
	}

	public int getFieldCount() {
		return 3;
	}

	@Override
	public int fill(EnumFacing from, FluidStack resource, boolean doFill) {
		return this.getFluidHandler().fill(resource, doFill);
	}

	@Override
	@Nullable
	public FluidStack drain(EnumFacing from, FluidStack resource, boolean doDrain) {
		return this.getFluidHandler().drain(resource, doDrain);
	}

	@Override
	@Nullable
	public FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain) {
		return this.getFluidHandler().drain(maxDrain, doDrain);
	}

	@Override
	public boolean canFill(EnumFacing from, Fluid fluid) {
		return fluid != null && this.getFluidHandler().fill(new FluidStack(fluid, 1), false) > 0;
	}

	@Override
	public boolean canDrain(EnumFacing from, Fluid fluid) {
		return fluid != null && this.getFluidHandler().drain(new FluidStack(fluid, 1), false) != null;
	}

	@Override
	public FluidTankInfo[] getTankInfo(EnumFacing from) {
		FluidStack fluid = this.getFluidDirect();
		return new FluidTankInfo[] {
			new FluidTankInfo(fluid == null ? null : fluid.copy(), this.getCapacity()) };
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
			this.clearSealIfEmpty();
			return;
		}

		if (!isAllowedFluid(fluid) || fluid.amount <= 0) {
			this.tank.setFluid(null);
			this.clearSealIfEmpty();
			return;
		}

		if (fluid.amount > this.tank.getCapacity()) {
			fluid.amount = this.tank.getCapacity();
		}
	}

	private void clearSealIfEmpty() {
		if (this.sealed && this.getFluidAmount() <= 0) {
			this.sealed = false;
		}
	}

	private void refreshRender() {
		if (this.world != null && this.world.isRemote && this.pos != null) {
			this.world.markBlockRangeForRenderUpdate(this.pos, this.pos);
		}
	}

	private boolean shouldAge() {
		return this.isSealed() && FoudreBrewingRegistry.canAgeFurther(this.getFluidDirect());
	}

	@Nullable
	private static NBTTagCompound getTankTag(ItemStack stack) {
		return stack != null && stack.hasTagCompound() && stack.getTagCompound().hasKey(TANK_TAG, 10)
			? stack.getTagCompound().getCompoundTag(TANK_TAG) : null;
	}

	private static void removeTankTag(ItemStack stack) {
		if (stack == null || !stack.hasTagCompound()) {
			return;
		}

		stack.getTagCompound().removeTag(TANK_TAG);

		if (stack.getTagCompound().hasNoTags()) {
			stack.setTagCompound(null);
		}
	}

	private final class SealedBarrelFluidHandler implements IFluidHandler {
		@Override
		public IFluidTankProperties[] getTankProperties() {
			return TileEntityBarrel.this.tank.getTankProperties();
		}

		@Override
		public int fill(FluidStack resource, boolean doFill) {
			boolean staleSeal = TileEntityBarrel.this.sealed && TileEntityBarrel.this.isEmpty();

			if (TileEntityBarrel.this.sealed && !staleSeal) {
				return 0;
			}

			int filled = TileEntityBarrel.this.tank.fill(resource, doFill);

			if (doFill && staleSeal && filled > 0) {
				TileEntityBarrel.this.sealed = false;
				TileEntityBarrel.this.markForFluidUpdate();
			}

			return filled;
		}

		@Override
		@Nullable
		public FluidStack drain(FluidStack resource, boolean doDrain) {
			return TileEntityBarrel.this.sealed ? null : TileEntityBarrel.this.tank.drain(resource, doDrain);
		}

		@Override
		@Nullable
		public FluidStack drain(int maxDrain, boolean doDrain) {
			return TileEntityBarrel.this.sealed ? null : TileEntityBarrel.this.tank.drain(maxDrain, doDrain);
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
