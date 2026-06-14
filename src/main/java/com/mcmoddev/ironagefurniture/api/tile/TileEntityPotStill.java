package com.mcmoddev.ironagefurniture.api.tile;

import java.util.List;

import javax.annotation.Nullable;

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
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.FluidTankPropertiesWrapper;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

public class TileEntityPotStill extends TileEntity implements IInventory, ITickable {
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
	private static final String INPUT_ITEM_TAG = "InputTank";
	private static final String OUTPUT_ITEM_TAG = "OutputTank";

	private final FluidTank inputTank = new PotStillTank(this, INPUT_CAPACITY, true);
	private final FluidTank outputTank = new PotStillTank(this, OUTPUT_CAPACITY, false);
	private final IFluidHandler fluidHandler = new PotStillFluidHandler();
	private final ItemStack[] inventory = new ItemStack[FUEL_SLOTS];
	private int distillTime;
	private int distillTimeTotal;
	private FluidStack batchInput;
	private FluidStack batchOutput;
	private String batchName = "";

	public TileEntityPotStill() {
		this.inputTank.setTileEntity(this);
		this.outputTank.setTileEntity(this);
	}

	@Override
	public void update() {
		if (this.world == null || this.world.isRemote || !this.isDistilling()) {
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

	public boolean canStartDistillation() {
		return this.getStartResult() != null && this.getFuelTicksAvailable() >= this.getStartResult()
			.getDistillationTime();
	}

	public boolean startDistillation() {
		DistillationResult result = this.getStartResult();

		if (result == null || this.getFuelTicksAvailable() < result.getDistillationTime()) {
			return false;
		}

		this.consumeFuelTicks(result.getDistillationTime());
		this.batchInput = this.inputTank.getFluid() == null ? null : this.inputTank.getFluid().copy();
		this.inputTank.setFluid(null);
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

	public void ruinBatch() {
		this.inputTank.setFluid(null);
		this.outputTank.setFluid(null);
		this.batchInput = null;
		this.batchOutput = null;
		this.batchName = "";
		this.distillTime = 0;
		this.distillTimeTotal = 0;
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

		if (tag.hasNoTags()) {
			stack.setTagCompound(null);
		} else {
			stack.setTagCompound(tag);
		}
	}

	public void readFromItemStack(ItemStack stack) {
		this.inputTank.setFluid(loadFluidFromItem(stack, INPUT_ITEM_TAG));
		this.outputTank.setFluid(loadFluidFromItem(stack, OUTPUT_ITEM_TAG));
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
		if (this.isDistilling() || this.outputTank.getFluidAmount() > 0) {
			return null;
		}

		return PotStillDistillingRegistry.findResult(this.inputTank.getFluid(), this.outputTank.getCapacity());
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
			if (TileEntityPotStill.this.isDistilling()
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
