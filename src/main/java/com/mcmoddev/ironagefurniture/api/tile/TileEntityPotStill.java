package com.mcmoddev.ironagefurniture.api.tile;

import java.util.List;

import javax.annotation.Nullable;

import com.mcmoddev.ironagefurniture.api.AdjacentBarrelTransfer;
import com.mcmoddev.ironagefurniture.api.Blocks.PotStill;
import com.mcmoddev.ironagefurniture.api.Enumerations.FluidPortMode;
import com.mcmoddev.ironagefurniture.api.Enumerations.FoudrePart;
import com.mcmoddev.ironagefurniture.api.PotStillDistillingRegistry;
import com.mcmoddev.ironagefurniture.api.PotStillDistillingRegistry.DistillationResult;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
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
import net.minecraftforge.fluids.capability.FluidTankPropertiesWrapper;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

public class TileEntityPotStill extends TileEntity implements IInventory, ITickable,
		net.minecraftforge.fluids.IFluidHandler {
	public static final int INPUT_CAPACITY = PotStillDistillingRegistry.INPUT_CAPACITY;
	public static final int OUTPUT_CAPACITY = PotStillDistillingRegistry.OUTPUT_CAPACITY;
	public static final int FUEL_SLOT = 0;
	public static final int FUEL_SLOTS = 1;
	public static final int FIELD_DISTILL_TIME = 0;
	public static final int FIELD_DISTILL_TIME_TOTAL = 1;
	public static final int FIELD_CAN_START = 2;
	public static final int FIELD_ACTIVE = 3;
	public static final int FIELD_INPUT_AMOUNT = 4;
	public static final int FIELD_OUTPUT_AMOUNT = 5;

	private static final String INPUT_TANK_TAG = "InputTank";
	private static final String OUTPUT_TANK_TAG = "OutputTank";
	private static final String FUEL_TAG = "Fuel";
	private static final String DISTILL_TIME_TAG = "DistillTime";
	private static final String DISTILL_TIME_TOTAL_TAG = "DistillTimeTotal";
	private static final String BATCH_INPUT_TAG = "BatchInput";
	private static final String BATCH_OUTPUT_TAG = "BatchOutput";
	private static final String BATCH_NAME_TAG = "BatchName";
	private static final String PORTS_CONFIGURED_TAG = "PortsConfigured";
	private static final String INLET_OPEN_TAG = "InletOpen";
	private static final String OUTLET_OPEN_TAG = "OutletOpen";
	private static final String PORT_MODE_TAG = "PortMode";
	private static final int PORT_CONNECTION_CHECK_INTERVAL = 20;
	private static final String INPUT_ITEM_TAG = "InputTank";
	private static final String OUTPUT_ITEM_TAG = "OutputTank";

	private final FluidTank inputTank = new PotStillTank(this, INPUT_CAPACITY, true);
	private final FluidTank outputTank = new PotStillTank(this, OUTPUT_CAPACITY, false);
	private final IFluidHandler fluidHandler = new PotStillFluidHandler();
	private final IItemHandler itemHandler = new InvWrapper(this);
	private final ItemStack[] inventory = new ItemStack[FUEL_SLOTS];
	private int distillTime;
	private int distillTimeTotal;
	private FluidStack batchInput;
	private FluidStack batchOutput;
	private String batchName = "";
	private FluidPortMode portMode = FluidPortMode.LOCKED;

	public TileEntityPotStill() {
		this.inputTank.setTileEntity(this);
		this.outputTank.setTileEntity(this);
	}

	@Override
	public void update() {
		if (this.world == null || this.world.isRemote) {
			return;
		}

		this.lockDisconnectedPort();

		if (!this.isDistilling()) {
			return;
		}

		this.distillTime++;

		if (this.distillTime >= this.distillTimeTotal) {
			this.finishDistillation();
		} else if (this.distillTime % 20 == 0) {
			this.markDirty();
		}
	}

	public String getContainerNameKey() {
		return "container.ironagefurniture.pot_still";
	}

	public IFluidHandler getFluidHandler() {
		return this.fluidHandler;
	}

	@Nullable
	public FluidStack getInputFluid() {
		FluidStack fluid = this.inputTank.getFluid();
		return fluid == null ? null : fluid.copy();
	}

	@Nullable
	public FluidStack getOutputFluid() {
		FluidStack fluid = this.outputTank.getFluid();
		return fluid == null ? null : fluid.copy();
	}

	@Nullable
	public FluidStack getBatchInputFluid() {
		return this.batchInput == null ? null : this.batchInput.copy();
	}

	@Nullable
	public FluidStack getBatchOutputFluid() {
		return this.batchOutput == null ? null : this.batchOutput.copy();
	}

	public int getBatchInputAmount() {
		return this.batchInput == null ? 0 : this.batchInput.amount;
	}

	public int getBatchOutputAmount() {
		return this.batchOutput == null ? 0 : this.batchOutput.amount;
	}

	public int getInputAmount() {
		return this.inputTank.getFluidAmount();
	}

	public int getOutputAmount() {
		return this.outputTank.getFluidAmount();
	}

	public int getInputCapacity() {
		return this.inputTank.getCapacity();
	}

	public int getOutputCapacity() {
		return this.outputTank.getCapacity();
	}

	public int getDistillTime() {
		return this.distillTime;
	}

	public int getDistillTimeTotal() {
		return this.distillTimeTotal;
	}

	public String getBatchName() {
		return this.batchName;
	}

	public boolean isDistilling() {
		return this.distillTimeTotal > 0 && this.batchOutput != null;
	}

	public boolean isInletOpen() {
		return this.portMode.opensInlet();
	}

	public void setInletOpen(boolean inletOpen) {
		if (inletOpen) {
			this.setPortMode(FluidPortMode.FLOOD);
		} else if (this.portMode == FluidPortMode.FLOOD) {
			this.setPortMode(FluidPortMode.LOCKED);
		}
	}

	public void toggleInletOpen() {
		this.setInletOpen(!this.isInletOpen());
	}

	public boolean isOutletOpen() {
		return this.portMode.opensOutlet();
	}

	public void setOutletOpen(boolean outletOpen) {
		if (outletOpen) {
			this.setPortMode(FluidPortMode.DRAIN);
		} else if (this.portMode == FluidPortMode.DRAIN) {
			this.setPortMode(FluidPortMode.LOCKED);
		}
	}

	public void toggleOutletOpen() {
		this.setOutletOpen(!this.isOutletOpen());
	}

	public FluidPortMode getPortMode() {
		return this.portMode;
	}

	public boolean setPortMode(@Nullable FluidPortMode mode) {
		FluidPortMode requested = mode == null ? FluidPortMode.LOCKED : mode;

		if (requested == FluidPortMode.FLOOD && (this.isDistilling() || !this.hasConnectedInlet())) {
			return false;
		}
		if (requested == FluidPortMode.DRAIN && (this.isDistilling() || !this.hasConnectedOutlet())) {
			return false;
		}
		if (this.portMode == requested) {
			return true;
		}

		this.portMode = requested;
		this.markForUpdate();
		return true;
	}

	public boolean canStartDistillation() {
		return this.getStartResult() != null && this.getFuelTicksAvailable() >= this.getStartResult()
			.getDistillationTime();
	}

	public boolean startDistillation() {
		DistillationResult result = this.getStartResult();
		boolean useOutputTank = this.shouldUseOutputForStart();
		FluidStack startFluid = useOutputTank ? this.outputTank.getFluid() : this.inputTank.getFluid();

		if (result == null || this.getFuelTicksAvailable() < result.getDistillationTime()) {
			return false;
		}

		this.consumeFuelTicks(result.getDistillationTime());
		this.portMode = FluidPortMode.LOCKED;
		this.batchInput = startFluid == null ? null : startFluid.copy();

		if (useOutputTank) {
			this.outputTank.setFluid(null);
		} else {
			this.inputTank.setFluid(null);
		}

		this.batchOutput = result.createOutputStack();
		this.batchName = result.getOutputName();
		this.distillTime = 0;
		this.distillTimeTotal = result.getDistillationTime();
		this.markForUpdate();
		return true;
	}

	public boolean canFlush() {
		return !this.isDistilling() && (this.inputTank.getFluidAmount() > 0
			|| this.outputTank.getFluidAmount() > 0);
	}

	public boolean flush() {
		if (!this.canFlush()) {
			return false;
		}

		this.inputTank.setFluid(null);
		this.outputTank.setFluid(null);
		this.markForUpdate();
		return true;
	}

	public boolean canDrainAdjacentBarrel() {
		return this.transferAdjacentBarrel(true, false);
	}

	public boolean drainAdjacentBarrel() {
		return this.transferAdjacentBarrel(true, true);
	}

	public boolean canFillAdjacentBarrel() {
		return this.transferAdjacentBarrel(false, false);
	}

	public boolean fillAdjacentBarrel() {
		return this.transferAdjacentBarrel(false, true);
	}

	public boolean canFillFromInletPort(@Nullable Fluid fluid) {
		return this.isInletOpen() && !this.isDistilling() && fluid != null
			&& this.fluidHandler.fill(new FluidStack(fluid, 1), false) > 0;
	}

	public int fillFromInletPort(@Nullable FluidStack resource, boolean doFill) {
		return this.isInletOpen() && !this.isDistilling() && resource != null && resource.getFluid() != null
			? this.fluidHandler.fill(resource, doFill) : 0;
	}

	public boolean canDrainFromOutletPort(@Nullable Fluid fluid) {
		if (!this.isOutletOpen() || this.isDistilling() || fluid == null) {
			return false;
		}

		FluidStack output = this.outputTank.getFluid();

		if (output != null && output.amount > 0) {
			return output.getFluid() == fluid;
		}

		FluidStack input = this.inputTank.getFluid();
		return input != null && input.amount > 0 && input.getFluid() == fluid;
	}

	@Nullable
	public FluidStack drainFromOutletPort(@Nullable FluidStack resource, boolean doDrain) {
		if (!this.isOutletOpen() || this.isDistilling() || resource == null || resource.getFluid() == null) {
			return null;
		}

		FluidStack output = this.outputTank.getFluid();

		if (output != null && output.amount > 0) {
			return output.getFluid() == resource.getFluid()
				? this.outputTank.drain(resource.amount, doDrain) : null;
		}

		FluidStack input = this.inputTank.getFluid();
		return input != null && input.getFluid() == resource.getFluid()
			? this.inputTank.drain(resource.amount, doDrain) : null;
	}

	@Nullable
	public FluidStack drainFromOutletPort(int maxDrain, boolean doDrain) {
		if (!this.isOutletOpen() || this.isDistilling() || maxDrain <= 0) {
			return null;
		}

		FluidStack output = this.outputTank.drain(maxDrain, doDrain);

		if (output != null && output.amount > 0) {
			return output;
		}

		return this.inputTank.drain(maxDrain, doDrain);
	}

	public boolean hasConnectedInlet() {
		return this.hasConnectedPort(true);
	}

	public boolean hasConnectedOutlet() {
		return this.hasConnectedPort(false);
	}

	public boolean hasConnectedPipework() {
		return this.hasConnectedInlet() || this.hasConnectedOutlet();
	}

	@Nullable
	public TileEntityBarrel getAdjacentTransferBarrel() {
		if (this.world == null || this.pos == null) {
			return null;
		}

		IBlockState state = this.world.getBlockState(this.pos);

		if (!(state.getBlock() instanceof PotStill)) {
			return null;
		}

		return AdjacentBarrelTransfer.findBarrel(this.world, this.pos, state.getValue(PotStill.FACING));
	}

	public void ruinBatch() {
		this.inputTank.setFluid(null);
		this.outputTank.setFluid(null);
		this.batchInput = null;
		this.batchOutput = null;
		this.batchName = "";
		this.distillTime = 0;
		this.distillTimeTotal = 0;
		this.portMode = FluidPortMode.LOCKED;
		this.clear();
		this.markForUpdate();
	}

	public void addIdleDrops(List<ItemStack> drops) {
		if (this.isDistilling()) {
			return;
		}

		for (ItemStack stack : this.inventory) {
			if (stack != null && stack.stackSize > 0) {
				drops.add(stack.copy());
			}
		}
	}

	public void writeToItemStack(ItemStack stack) {
		if (stack == null || this.isDistilling()) {
			return;
		}

		NBTTagCompound tag = stack.hasTagCompound() ? stack.getTagCompound() : new NBTTagCompound();

		if (this.inputTank.getFluidAmount() > 0) {
			tag.setTag(INPUT_ITEM_TAG, this.inputTank.writeToNBT(new NBTTagCompound()));
		} else {
			tag.removeTag(INPUT_ITEM_TAG);
		}

		if (this.outputTank.getFluidAmount() > 0) {
			tag.setTag(OUTPUT_ITEM_TAG, this.outputTank.writeToNBT(new NBTTagCompound()));
		} else {
			tag.removeTag(OUTPUT_ITEM_TAG);
		}

		tag.removeTag(INLET_OPEN_TAG);
		tag.removeTag(OUTLET_OPEN_TAG);
		tag.removeTag(PORTS_CONFIGURED_TAG);
		tag.removeTag(PORT_MODE_TAG);

		if (tag.hasNoTags()) {
			stack.setTagCompound(null);
		} else {
			stack.setTagCompound(tag);
		}
	}

	public void readFromItemStack(ItemStack stack) {
		this.inputTank.setFluid(loadFluidFromItem(stack, INPUT_ITEM_TAG));
		this.outputTank.setFluid(loadFluidFromItem(stack, OUTPUT_ITEM_TAG));
		this.portMode = FluidPortMode.LOCKED;
		this.markForUpdate();
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		return this.world.getTileEntity(this.pos) != this ? false
			: player.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D,
				(double)this.pos.getZ() + 0.5D) <= 64.0D;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY
			|| capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY
			|| super.hasCapability(capability, facing);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			return (T)(facing == null ? this.fluidHandler : new SidedPotStillFluidHandler());
		}

		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return (T)this.itemHandler;
		}

		return super.getCapability(capability, facing);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		this.inputTank.readFromNBT(compound.getCompoundTag(INPUT_TANK_TAG));
		this.outputTank.readFromNBT(compound.getCompoundTag(OUTPUT_TANK_TAG));
		this.distillTime = compound.getInteger(DISTILL_TIME_TAG);
		this.distillTimeTotal = compound.getInteger(DISTILL_TIME_TOTAL_TAG);
		this.batchInput = compound.hasKey(BATCH_INPUT_TAG, 10)
			? FluidStack.loadFluidStackFromNBT(compound.getCompoundTag(BATCH_INPUT_TAG)) : null;
		this.batchOutput = compound.hasKey(BATCH_OUTPUT_TAG, 10)
			? FluidStack.loadFluidStackFromNBT(compound.getCompoundTag(BATCH_OUTPUT_TAG)) : null;
		this.batchName = compound.hasKey(BATCH_NAME_TAG, 8) ? compound.getString(BATCH_NAME_TAG) : "";
		this.portMode = readPortMode(compound);
		this.clear();

		if (compound.hasKey(FUEL_TAG, 10)) {
			this.inventory[FUEL_SLOT] = ItemStack.loadItemStackFromNBT(compound.getCompoundTag(FUEL_TAG));
		}

		if (this.batchOutput == null) {
			this.batchInput = null;
			this.distillTime = 0;
			this.distillTimeTotal = 0;
			this.batchName = "";
		}

		this.sanitizePortState();
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setTag(INPUT_TANK_TAG, this.inputTank.writeToNBT(new NBTTagCompound()));
		compound.setTag(OUTPUT_TANK_TAG, this.outputTank.writeToNBT(new NBTTagCompound()));
		compound.setInteger(DISTILL_TIME_TAG, this.distillTime);
		compound.setInteger(DISTILL_TIME_TOTAL_TAG, this.distillTimeTotal);

		if (this.batchInput != null) {
			compound.setTag(BATCH_INPUT_TAG, this.batchInput.writeToNBT(new NBTTagCompound()));
		}
		if (this.batchOutput != null) {
			compound.setTag(BATCH_OUTPUT_TAG, this.batchOutput.writeToNBT(new NBTTagCompound()));
		}
		if (!this.batchName.isEmpty()) {
			compound.setString(BATCH_NAME_TAG, this.batchName);
		}
		compound.setByte(PORT_MODE_TAG, (byte)this.portMode.getId());
		compound.removeTag(PORTS_CONFIGURED_TAG);
		compound.removeTag(INLET_OPEN_TAG);
		compound.removeTag(OUTLET_OPEN_TAG);
		if (this.inventory[FUEL_SLOT] != null) {
			NBTTagCompound fuelTag = new NBTTagCompound();
			this.inventory[FUEL_SLOT].writeToNBT(fuelTag);
			compound.setTag(FUEL_TAG, fuelTag);
		}

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

	@Override
	public int getSizeInventory() {
		return FUEL_SLOTS;
	}

	@Override
	@Nullable
	public ItemStack getStackInSlot(int index) {
		return index == FUEL_SLOT ? this.inventory[FUEL_SLOT] : null;
	}

	@Override
	@Nullable
	public ItemStack decrStackSize(int index, int count) {
		if (this.isDistilling() || index != FUEL_SLOT) {
			return null;
		}

		ItemStack stack = ItemStackHelper.getAndSplit(this.inventory, index, count);

		if (stack != null) {
			this.markForUpdate();
		}

		return stack;
	}

	@Override
	@Nullable
	public ItemStack removeStackFromSlot(int index) {
		if (this.isDistilling() || index != FUEL_SLOT) {
			return null;
		}

		ItemStack stack = ItemStackHelper.getAndRemove(this.inventory, index);

		if (stack != null) {
			this.markForUpdate();
		}

		return stack;
	}

	@Override
	public void setInventorySlotContents(int index, @Nullable ItemStack stack) {
		if (this.isDistilling() || index != FUEL_SLOT) {
			return;
		}

		this.inventory[FUEL_SLOT] = stack;

		if (stack != null && stack.stackSize > this.getInventoryStackLimit()) {
			stack.stackSize = this.getInventoryStackLimit();
		}

		this.markForUpdate();
	}

	@Override
	public String getName() {
		return this.getContainerNameKey();
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public void openInventory(EntityPlayer player) {
	}

	@Override
	public void closeInventory(EntityPlayer player) {
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return !this.isDistilling() && index == FUEL_SLOT && TileEntityFurnace.getItemBurnTime(stack) > 0;
	}

	@Override
	public int getField(int id) {
		switch (id) {
		case FIELD_DISTILL_TIME:
			return this.distillTime;
		case FIELD_DISTILL_TIME_TOTAL:
			return this.distillTimeTotal;
		case FIELD_CAN_START:
			return this.canStartDistillation() ? 1 : 0;
		case FIELD_ACTIVE:
			return this.isDistilling() ? 1 : 0;
		case FIELD_INPUT_AMOUNT:
			return this.getInputAmount();
		case FIELD_OUTPUT_AMOUNT:
			return this.getOutputAmount();
		default:
			return 0;
		}
	}

	@Override
	public void setField(int id, int value) {
		switch (id) {
		case FIELD_DISTILL_TIME:
			this.distillTime = value;
			break;
		case FIELD_DISTILL_TIME_TOTAL:
			this.distillTimeTotal = value;
			break;
		default:
			break;
		}
	}

	@Override
	public int getFieldCount() {
		return 6;
	}

	@Override
	public int fill(EnumFacing from, FluidStack resource, boolean doFill) {
		return from == null ? this.fluidHandler.fill(resource, doFill) : 0;
	}

	@Override
	@Nullable
	public FluidStack drain(EnumFacing from, FluidStack resource, boolean doDrain) {
		return from == null ? this.fluidHandler.drain(resource, doDrain) : null;
	}

	@Override
	@Nullable
	public FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain) {
		return from == null ? this.fluidHandler.drain(maxDrain, doDrain) : null;
	}

	@Override
	public boolean canFill(EnumFacing from, Fluid fluid) {
		return from == null && fluid != null && this.fluidHandler.fill(new FluidStack(fluid, 1), false) > 0;
	}

	@Override
	public boolean canDrain(EnumFacing from, Fluid fluid) {
		return from == null && fluid != null && this.fluidHandler.drain(new FluidStack(fluid, 1), false) != null;
	}

	@Override
	public FluidTankInfo[] getTankInfo(EnumFacing from) {
		return from == null ? new FluidTankInfo[] {
			new FluidTankInfo(this.inputTank),
			new FluidTankInfo(this.outputTank) } : new FluidTankInfo[0];
	}

	@Override
	public void clear() {
		this.inventory[FUEL_SLOT] = null;
	}

	public void markForUpdate() {
		this.markDirty();

		if (this.world != null && !this.world.isRemote && this.pos != null) {
			IBlockState state = this.world.getBlockState(this.pos);
			this.world.notifyBlockUpdate(this.pos, state, state, 3);
			this.world.updateComparatorOutputLevel(this.pos, state.getBlock());
		}
	}

	@Nullable
	public static FluidStack getInputFromItemStack(ItemStack stack) {
		return loadFluidFromItem(stack, INPUT_ITEM_TAG);
	}

	@Nullable
	public static FluidStack getOutputFromItemStack(ItemStack stack) {
		return loadFluidFromItem(stack, OUTPUT_ITEM_TAG);
	}

	private DistillationResult getStartResult() {
		if (this.isDistilling()) {
			return null;
		}

		if (this.inputTank.getFluidAmount() > 0) {
			if (this.outputTank.getFluidAmount() > 0) {
				return null;
			}

			return PotStillDistillingRegistry.findResult(this.inputTank.getFluid(), this.outputTank.getCapacity());
		}

		if (this.shouldUseOutputForStart()) {
			return PotStillDistillingRegistry.findResult(this.outputTank.getFluid(), this.outputTank.getCapacity());
		}

		return null;
	}

	private boolean shouldUseOutputForStart() {
		return this.inputTank.getFluidAmount() <= 0 && this.outputTank.getFluidAmount() > 0
			&& PotStillDistillingRegistry.isSpirit(this.outputTank.getFluid());
	}

	private boolean transferAdjacentBarrel(boolean barrelToStill, boolean doTransfer) {
		if (this.world == null || this.pos == null || doTransfer && this.world.isRemote) {
			return false;
		}

		IBlockState state = this.world.getBlockState(this.pos);

		if (!(state.getBlock() instanceof PotStill)) {
			return false;
		}

		boolean transferred = AdjacentBarrelTransfer.transfer(this.world, this.pos, state.getValue(PotStill.FACING),
			this.getFluidHandler(), barrelToStill, doTransfer);

		if (transferred && doTransfer) {
			this.markForUpdate();
		}

		return transferred;
	}

	private void finishDistillation() {
		this.outputTank.setFluid(this.batchOutput == null ? null : this.batchOutput.copy());
		this.batchInput = null;
		this.batchOutput = null;
		this.batchName = "";
		this.distillTime = 0;
		this.distillTimeTotal = 0;
		this.markForUpdate();
	}

	private void sanitizePortState() {
		if (this.isDistilling()) {
			this.portMode = FluidPortMode.LOCKED;
		}
	}

	private void lockDisconnectedPort() {
		if (this.world.getTotalWorldTime() % PORT_CONNECTION_CHECK_INTERVAL != 0L) {
			return;
		}

		if (this.portMode == FluidPortMode.FLOOD && !this.hasConnectedInlet()
				|| this.portMode == FluidPortMode.DRAIN && !this.hasConnectedOutlet()) {
			this.portMode = FluidPortMode.LOCKED;
			this.markForUpdate();
		}
	}

	private static FluidPortMode readPortMode(NBTTagCompound compound) {
		if (compound.hasKey(PORT_MODE_TAG, 1)) {
			return FluidPortMode.fromId(compound.getByte(PORT_MODE_TAG));
		}

		if (!compound.getBoolean(PORTS_CONFIGURED_TAG)) {
			return FluidPortMode.LOCKED;
		}

		return FluidPortMode.fromLegacy(compound.getBoolean(INLET_OPEN_TAG),
			compound.getBoolean(OUTLET_OPEN_TAG));
	}

	private boolean hasConnectedPort(boolean inlet) {
		if (this.world == null || this.pos == null) {
			return false;
		}

		IBlockState state = this.world.getBlockState(this.pos);

		if (!(state.getBlock() instanceof PotStill)) {
			return false;
		}

		FoudrePart part = inlet ? FoudrePart.BACK_LEFT : FoudrePart.BACK_RIGHT;
		BlockPos portPos = PotStill.resolvePartPos(this.pos, state.getValue(PotStill.FACING), part, inlet);
		BlockPos attachmentPos = portPos.offset(PotStill.getPortFace(state.getValue(PotStill.FACING)));
		TileEntity tileEntity = this.world.getTileEntity(attachmentPos);

		if (tileEntity instanceof net.minecraftforge.fluids.IFluidHandler
				|| tileEntity != null && tileEntity.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY,
					PotStill.getPortFace(state.getValue(PotStill.FACING)).getOpposite())) {
			return true;
		}

		Block block = this.world.getBlockState(attachmentPos).getBlock();
		return block != null && block.getRegistryName() != null
			&& block.getRegistryName().toString().endsWith("fluid_pipe_terminal");
	}

	private int getFuelTicksAvailable() {
		ItemStack stack = this.inventory[FUEL_SLOT];
		return stack == null ? 0 : TileEntityFurnace.getItemBurnTime(stack) * stack.stackSize;
	}

	private void consumeFuelTicks(int ticks) {
		ItemStack stack = this.inventory[FUEL_SLOT];

		if (stack == null || ticks <= 0) {
			return;
		}

		int burnTime = TileEntityFurnace.getItemBurnTime(stack);

		if (burnTime <= 0) {
			return;
		}

		int consumed = Math.min(stack.stackSize, (ticks + burnTime - 1) / burnTime);
		stack.stackSize -= consumed;

		if (stack.stackSize <= 0) {
			this.inventory[FUEL_SLOT] = null;
		}
	}

	private void refreshRender() {
		if (this.world != null && this.world.isRemote && this.pos != null) {
			this.world.markBlockRangeForRenderUpdate(this.pos, this.pos);
		}
	}

	@Nullable
	private static FluidStack loadFluidFromItem(ItemStack stack, String tagName) {
		if (stack == null || !stack.hasTagCompound() || !stack.getTagCompound().hasKey(tagName, 10)) {
			return null;
		}

		return FluidStack.loadFluidStackFromNBT(stack.getTagCompound().getCompoundTag(tagName));
	}

	private final class PotStillFluidHandler implements IFluidHandler {
		@Override
		public IFluidTankProperties[] getTankProperties() {
			return new IFluidTankProperties[] {
				new FluidTankPropertiesWrapper(TileEntityPotStill.this.inputTank),
				new FluidTankPropertiesWrapper(TileEntityPotStill.this.outputTank) };
		}

		@Override
		public int fill(FluidStack resource, boolean doFill) {
			if (resource == null || resource.getFluid() == null || TileEntityPotStill.this.isDistilling()
					|| TileEntityPotStill.this.outputTank.getFluidAmount() > 0
					|| !PotStillDistillingRegistry.canMixInputs(TileEntityPotStill.this.inputTank.getFluid(),
						resource)) {
				return 0;
			}

			return TileEntityPotStill.this.inputTank.fill(resource, doFill);
		}

		@Override
		@Nullable
		public FluidStack drain(FluidStack resource, boolean doDrain) {
			if (TileEntityPotStill.this.isDistilling() || resource == null || resource.getFluid() == null) {
				return null;
			}

			FluidStack output = TileEntityPotStill.this.outputTank.getFluid();

			if (output != null && output.getFluid() == resource.getFluid()) {
				return TileEntityPotStill.this.outputTank.drain(resource.amount, doDrain);
			}

			FluidStack input = TileEntityPotStill.this.inputTank.getFluid();
			return input != null && input.getFluid() == resource.getFluid()
				? TileEntityPotStill.this.inputTank.drain(resource.amount, doDrain) : null;
		}

		@Override
		@Nullable
		public FluidStack drain(int maxDrain, boolean doDrain) {
			if (TileEntityPotStill.this.isDistilling() || maxDrain <= 0) {
				return null;
			}

			FluidStack output = TileEntityPotStill.this.outputTank.drain(maxDrain, doDrain);

			if (output != null && output.amount > 0) {
				return output;
			}

			return TileEntityPotStill.this.inputTank.drain(maxDrain, doDrain);
		}
	}

	private final class SidedPotStillFluidHandler implements IFluidHandler {
		@Override
		public IFluidTankProperties[] getTankProperties() {
			return new IFluidTankProperties[0];
		}

		@Override
		public int fill(FluidStack resource, boolean doFill) {
			return 0;
		}

		@Override
		@Nullable
		public FluidStack drain(FluidStack resource, boolean doDrain) {
			return null;
		}

		@Override
		@Nullable
		public FluidStack drain(int maxDrain, boolean doDrain) {
			return null;
		}
	}

	private static final class PotStillTank extends FluidTank {
		private final TileEntityPotStill owner;
		private final boolean input;

		private PotStillTank(TileEntityPotStill owner, int capacity, boolean input) {
			super(capacity);
			this.owner = owner;
			this.input = input;
		}

		@Override
		public int fill(FluidStack resource, boolean doFill) {
			if (!this.canFillFluidType(resource)) {
				return 0;
			}

			FluidStack existing = this.getFluid();

			if (existing == null || existing.isFluidEqual(resource)) {
				return super.fill(resource, doFill);
			}

			if (existing.getFluid() != resource.getFluid()) {
				return 0;
			}

			int filled = Math.min(this.getCapacity() - existing.amount, resource.amount);

			if (doFill && filled > 0) {
				existing.amount += filled;
				this.onContentsChanged();
			}

			return filled;
		}

		@Override
		public boolean canFill() {
			return this.input && !this.owner.isDistilling();
		}

		@Override
		public boolean canDrain() {
			return !this.owner.isDistilling();
		}

		@Override
		public boolean canFillFluidType(FluidStack fluid) {
			return this.input && !this.owner.isDistilling() && PotStillDistillingRegistry.canMixInputs(
				this.getFluid(), fluid);
		}

		@Override
		protected void onContentsChanged() {
			this.owner.markForUpdate();
		}
	}
}
