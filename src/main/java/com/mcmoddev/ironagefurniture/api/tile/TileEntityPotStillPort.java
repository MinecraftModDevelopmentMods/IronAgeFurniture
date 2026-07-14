package com.mcmoddev.ironagefurniture.api.tile;

import javax.annotation.Nullable;

import com.mcmoddev.ironagefurniture.api.PowerAdvantageFluidCompat;
import com.mcmoddev.ironagefurniture.api.Blocks.PotStill;
import com.mcmoddev.ironagefurniture.api.Enumerations.FoudrePart;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

public class TileEntityPotStillPort extends TileEntity implements net.minecraftforge.fluids.IFluidHandler, ITickable {
	private static final int PIPE_TRANSFER_INTERVAL = 8;
	private static final IFluidTankProperties[] EMPTY_PROPERTIES = new IFluidTankProperties[0];
	private static final FluidTankInfo[] EMPTY_TANK_INFO = new FluidTankInfo[0];

	private final IFluidHandler fluidHandler = new PortFluidHandler();

	@Override
	public void update() {
		if (this.world == null || this.world.isRemote || !this.isOutletPort()
				|| (this.world.getTotalWorldTime() + this.getTickOffset()) % PIPE_TRANSFER_INTERVAL != 0) {
			return;
		}

		TileEntityPotStill potStill = this.getController();

		if (potStill == null) {
			return;
		}

		FluidStack available = potStill.drainFromOutletPort(Integer.MAX_VALUE, false);
		int transferred = PowerAdvantageFluidCompat.tryTransferFromOutlet(this.world, this.pos, this.getPortFace(),
			available);

		if (transferred > 0) {
			potStill.drainFromOutletPort(transferred, true);
		}
	}

	@Override
	public int fill(EnumFacing from, FluidStack resource, boolean doFill) {
		TileEntityPotStill potStill = this.getController();
		return potStill != null && this.isInletPort() && this.matchesPortFace(from)
			? potStill.fillFromInletPort(resource, doFill) : 0;
	}

	@Override
	@Nullable
	public FluidStack drain(EnumFacing from, FluidStack resource, boolean doDrain) {
		TileEntityPotStill potStill = this.getController();
		return potStill != null && this.isOutletPort() && this.matchesPortFace(from)
			? potStill.drainFromOutletPort(resource, doDrain) : null;
	}

	@Override
	@Nullable
	public FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain) {
		TileEntityPotStill potStill = this.getController();
		return potStill != null && this.isOutletPort() && this.matchesPortFace(from)
			? potStill.drainFromOutletPort(maxDrain, doDrain) : null;
	}

	@Override
	public boolean canFill(EnumFacing from, Fluid fluid) {
		TileEntityPotStill potStill = this.getController();
		return potStill != null && this.isInletPort() && this.matchesPortFace(from)
			&& potStill.canFillFromInletPort(fluid);
	}

	@Override
	public boolean canDrain(EnumFacing from, Fluid fluid) {
		TileEntityPotStill potStill = this.getController();
		return potStill != null && this.isOutletPort() && this.matchesPortFace(from)
			&& potStill.canDrainFromOutletPort(fluid);
	}

	@Override
	public FluidTankInfo[] getTankInfo(EnumFacing from) {
		TileEntityPotStill potStill = this.getController();
		return potStill == null || !this.matchesPortFace(from) ? EMPTY_TANK_INFO : potStill.getTankInfo(null);
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
			return (T)this.fluidHandler;
		}

		return super.getCapability(capability, facing);
	}

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
		return oldState.getBlock() != newState.getBlock();
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		return super.writeToNBT(compound);
	}

	private boolean isInletPort() {
		IBlockState state = this.getPotStillState();
		return state != null && state.getBlock() instanceof PotStill
			&& PotStill.isInletPart(state.getValue(PotStill.PART), ((PotStill)state.getBlock()).isUpperLayerBlock());
	}

	private boolean isOutletPort() {
		IBlockState state = this.getPotStillState();
		return state != null && state.getBlock() instanceof PotStill
			&& PotStill.isOutletPart(state.getValue(PotStill.PART), ((PotStill)state.getBlock()).isUpperLayerBlock());
	}

	private boolean matchesPortFace(@Nullable EnumFacing from) {
		EnumFacing portFace = this.getPortFace();
		return portFace != null && (from == null || from == portFace || from == portFace.getOpposite());
	}

	private int getTickOffset() {
		return ((this.pos.getZ() & 1) << 2) | ((this.pos.getX() & 1) << 1) | (this.pos.getY() & 1);
	}

	@Nullable
	private EnumFacing getPortFace() {
		IBlockState state = this.getPotStillState();
		return state != null && state.getBlock() instanceof PotStill
			? PotStill.getPortFace(state.getValue(PotStill.FACING)) : null;
	}

	@Nullable
	private IBlockState getPotStillState() {
		if (this.world == null || this.pos == null) {
			return null;
		}

		IBlockState state = this.world.getBlockState(this.pos);
		return state.getBlock() instanceof PotStill ? state : null;
	}

	@Nullable
	private TileEntityPotStill getController() {
		IBlockState state = this.getPotStillState();

		if (state == null) {
			return null;
		}

		PotStill block = (PotStill)state.getBlock();
		FoudrePart part = state.getValue(PotStill.PART);
		BlockPos basePos = PotStill.resolveBasePos(this.pos, state.getValue(PotStill.FACING), part,
			block.isUpperLayerBlock());
		TileEntity tileEntity = this.world.getTileEntity(basePos);
		return tileEntity instanceof TileEntityPotStill ? (TileEntityPotStill)tileEntity : null;
	}

	private final class PortFluidHandler implements IFluidHandler {
		@Override
		public IFluidTankProperties[] getTankProperties() {
			TileEntityPotStill potStill = TileEntityPotStillPort.this.getController();
			return potStill == null ? EMPTY_PROPERTIES : potStill.getFluidHandler().getTankProperties();
		}

		@Override
		public int fill(FluidStack resource, boolean doFill) {
			return TileEntityPotStillPort.this.fill(null, resource, doFill);
		}

		@Override
		@Nullable
		public FluidStack drain(FluidStack resource, boolean doDrain) {
			return TileEntityPotStillPort.this.drain(null, resource, doDrain);
		}

		@Override
		@Nullable
		public FluidStack drain(int maxDrain, boolean doDrain) {
			return TileEntityPotStillPort.this.drain(null, maxDrain, doDrain);
		}
	}
}
