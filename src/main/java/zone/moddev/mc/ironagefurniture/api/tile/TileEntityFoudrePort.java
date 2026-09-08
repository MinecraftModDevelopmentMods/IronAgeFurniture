package zone.moddev.mc.ironagefurniture.api.tile;

import javax.annotation.Nullable;

import zone.moddev.mc.ironagefurniture.api.PowerAdvantageFluidCompat;
import zone.moddev.mc.ironagefurniture.api.Blocks.Foudre;
import zone.moddev.mc.ironagefurniture.api.Enumerations.FoudrePart;

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

public class TileEntityFoudrePort extends TileEntity implements net.minecraftforge.fluids.IFluidHandler, ITickable {
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

		TileEntityFoudre foudre = this.getController();

		if (foudre == null) {
			return;
		}

		FluidStack available = foudre.drainFromOutletPort(Integer.MAX_VALUE, false);
		int transferred = PowerAdvantageFluidCompat.tryTransferFromOutlet(this.world, this.pos, this.getPortFace(),
			available);

		if (transferred > 0) {
			foudre.drainFromOutletPort(transferred, true);
		}
	}

	@Override
	public int fill(EnumFacing from, FluidStack resource, boolean doFill) {
		TileEntityFoudre foudre = this.getController();
		return foudre != null && this.isInletPort() && this.matchesPortFace(from) ? foudre.fillFromInletPort(resource,
			doFill) : 0;
	}

	@Override
	@Nullable
	public FluidStack drain(EnumFacing from, FluidStack resource, boolean doDrain) {
		TileEntityFoudre foudre = this.getController();
		return foudre != null && this.isOutletPort() && this.matchesPortFace(from)
			? foudre.drainFromOutletPort(resource, doDrain) : null;
	}

	@Override
	@Nullable
	public FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain) {
		TileEntityFoudre foudre = this.getController();
		return foudre != null && this.isOutletPort() && this.matchesPortFace(from)
			? foudre.drainFromOutletPort(maxDrain, doDrain) : null;
	}

	@Override
	public boolean canFill(EnumFacing from, Fluid fluid) {
		TileEntityFoudre foudre = this.getController();
		return foudre != null && this.isInletPort() && this.matchesPortFace(from)
			&& foudre.canFillFromInletPort(fluid);
	}

	@Override
	public boolean canDrain(EnumFacing from, Fluid fluid) {
		TileEntityFoudre foudre = this.getController();
		return foudre != null && this.isOutletPort() && this.matchesPortFace(from)
			&& foudre.canDrainFromOutletPort(fluid);
	}

	@Override
	public FluidTankInfo[] getTankInfo(EnumFacing from) {
		TileEntityFoudre foudre = this.getController();
		return foudre == null || !this.matchesPortFace(from) ? EMPTY_TANK_INFO : foudre.getTankInfo(null);
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
		IBlockState state = this.getFoudreState();
		return state != null && state.getBlock() instanceof Foudre
			&& Foudre.isInletPart(state.getValue(Foudre.PART), ((Foudre)state.getBlock()).isUpperLayerBlock());
	}

	private boolean isOutletPort() {
		IBlockState state = this.getFoudreState();
		return state != null && state.getBlock() instanceof Foudre
			&& Foudre.isOutletPart(state.getValue(Foudre.PART), ((Foudre)state.getBlock()).isUpperLayerBlock());
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
		IBlockState state = this.getFoudreState();
		return state != null && state.getBlock() instanceof Foudre ? Foudre.getPortFace(state.getValue(Foudre.FACING))
			: null;
	}

	@Nullable
	private IBlockState getFoudreState() {
		if (this.world == null || this.pos == null) {
			return null;
		}

		IBlockState state = this.world.getBlockState(this.pos);
		return state.getBlock() instanceof Foudre ? state : null;
	}

	@Nullable
	private TileEntityFoudre getController() {
		IBlockState state = this.getFoudreState();

		if (state == null) {
			return null;
		}

		Foudre block = (Foudre)state.getBlock();
		FoudrePart part = state.getValue(Foudre.PART);
		BlockPos basePos = Foudre.resolveBasePos(this.pos, state.getValue(Foudre.FACING), part,
			block.isUpperLayerBlock());
		TileEntity tileEntity = this.world.getTileEntity(basePos);
		return tileEntity instanceof TileEntityFoudre ? (TileEntityFoudre)tileEntity : null;
	}

	private final class PortFluidHandler implements IFluidHandler {
		@Override
		public IFluidTankProperties[] getTankProperties() {
			TileEntityFoudre foudre = TileEntityFoudrePort.this.getController();
			return foudre == null ? EMPTY_PROPERTIES : foudre.getFluidHandler().getTankProperties();
		}

		@Override
		public int fill(FluidStack resource, boolean doFill) {
			return TileEntityFoudrePort.this.fill(null, resource, doFill);
		}

		@Override
		@Nullable
		public FluidStack drain(FluidStack resource, boolean doDrain) {
			return TileEntityFoudrePort.this.drain(null, resource, doDrain);
		}

		@Override
		@Nullable
		public FluidStack drain(int maxDrain, boolean doDrain) {
			return TileEntityFoudrePort.this.drain(null, maxDrain, doDrain);
		}
	}
}
