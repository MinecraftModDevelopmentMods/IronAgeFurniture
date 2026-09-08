package zone.moddev.mc.ironagefurniture.api.tile;

import java.util.List;

import javax.annotation.Nullable;

import zone.moddev.mc.ironagefurniture.api.AdjacentBarrelTransfer;
import zone.moddev.mc.ironagefurniture.api.Blocks.Foudre;
import zone.moddev.mc.ironagefurniture.api.Enumerations.FluidPortMode;
import zone.moddev.mc.ironagefurniture.api.Enumerations.FoudrePart;
import zone.moddev.mc.ironagefurniture.api.FoudreBrewingRegistry;
import zone.moddev.mc.ironagefurniture.api.FoudreBrewingRegistry.FoudreBrewingRecipe;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
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
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

public class TileEntityFoudre extends TileEntityBarrel implements IInventory, ITickable {
	public static final int CAPACITY = 128 * Fluid.BUCKET_VOLUME;
	public static final int INGREDIENT_SLOTS = 4;
	public static final int FIELD_BREW_TIME = 0;
	public static final int FIELD_BREW_TIME_TOTAL = 1;
	public static final int FIELD_AGE_PROGRESS = 2;
	public static final int FIELD_AGE_PROGRESS_TOTAL = 3;
	public static final int FIELD_SEALED = 4;
	public static final int FIELD_INFUSION_COMPLETE = 5;

	private static final String LABEL_TAG = "FoudreLabel";
	private static final String SEALED_TAG = "Sealed";
	private static final String ITEMS_TAG = "Ingredients";
	private static final String BREW_TIME_TAG = "BrewTime";
	private static final String BREW_TIME_TOTAL_TAG = "BrewTimeTotal";
	private static final String BREW_RECIPE_TAG = "BrewRecipe";
	private static final String BREW_RECIPE_NAME_TAG = "BrewRecipeName";
	private static final String INFUSION_COMPLETE_TAG = "InfusionComplete";
	private static final String PORTS_CONFIGURED_TAG = "PortsConfigured";
	private static final String INLET_OPEN_TAG = "InletOpen";
	private static final String OUTLET_OPEN_TAG = "OutletOpen";
	private static final String PORT_MODE_TAG = "PortMode";
	private static final int PORT_CONNECTION_CHECK_INTERVAL = 20;

	private final ItemStack[] inventory = new ItemStack[INGREDIENT_SLOTS];
	private final IFluidHandler sealedFluidHandler = new SealedFoudreFluidHandler();
	private final IItemHandler itemHandler = new InvWrapper(this);
	private String label = "";
	private boolean sealed;
	private FluidPortMode portMode = FluidPortMode.LOCKED;
	private int brewTime;
	private int brewTimeTotal;
	private int clientAgeProgress;
	private int clientAgeProgressTotal;
	private String brewRecipeId = "";
	private String brewRecipeName = "";
	private boolean infusionComplete;
	private String potentialRecipeName = "";
	private boolean potentialRecipeDirty = true;

	public TileEntityFoudre() {
		super(CAPACITY);
	}

	@Override
	public String getContainerNameKey() {
		return "container.ironagefurniture.foudre";
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		return super.isUsableByPlayer(player);
	}

	@Override
	public void update() {
		if (this.world == null || this.world.isRemote) {
			return;
		}

		this.lockDisconnectedPort();

		if (!this.sealed) {
			this.resetBrewing();
			return;
		}
		if (this.infusionComplete) {
			this.resetBrewing();
			return;
		}

		FoudreBrewingRecipe recipe = FoudreBrewingRegistry.findMatchingRecipe(this.getFluid(), this.inventory,
			this.getCapacity());

		if (recipe == null) {
			this.resetBrewing();
			if (this.shouldAge()) {
				this.updateAging();
			}
			return;
		}

		if (!recipe.getId().equals(this.brewRecipeId)) {
			this.brewRecipeId = recipe.getId();
			this.brewRecipeName = recipe.getDisplayName();
			this.brewTime = 0;
			this.brewTimeTotal = recipe.getBrewTime();
			this.markForFluidUpdate();
		}

		this.brewTime++;

		if (this.brewTime >= this.brewTimeTotal) {
			FluidStack output = FoudreBrewingRegistry.createBrewedFluid(recipe, this.getFluidDirect(),
				this.getCapacity());
			List<ItemStack> remainders = recipe.consumeIngredients(this.inventory);
			this.setFluid(output);
			this.infusionComplete = recipe.requiresResealAfterCompletion();
			this.storeOrDropRemainders(remainders);
			this.resetBrewing();
			this.markForFluidUpdate();
		} else if (this.brewTime % 20 == 0) {
			this.markDirty();
		}
	}

	public String getLabel() {
		return this.label;
	}

	public void setLabel(@Nullable String label) {
		String sanitized = sanitizeLabel(label);

		if (sanitized.equals(this.label)) {
			return;
		}

		this.label = sanitized;
		this.markForFluidUpdate();
	}

	public boolean isSealed() {
		return this.sealed;
	}

	public void setSealed(boolean sealed) {
		if (this.sealed == sealed) {
			return;
		}

		boolean opening = this.sealed && !sealed;
		this.sealed = sealed;

		if (sealed) {
			this.portMode = FluidPortMode.LOCKED;
		}

		if (opening) {
			FoudreBrewingRegistry.resetAgeProgressToCurrentLevel(this.getFluidDirect());
			this.infusionComplete = false;
		}

		this.markForFluidUpdate();
	}

	public void toggleSealed() {
		this.setSealed(!this.sealed);
	}

	public boolean isInfusionComplete() {
		return this.infusionComplete;
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

		if (requested == FluidPortMode.FLOOD && (this.sealed || !this.hasConnectedInlet())) {
			return false;
		}
		if (requested == FluidPortMode.DRAIN && (this.sealed || !this.hasConnectedOutlet())) {
			return false;
		}
		if (this.portMode == requested) {
			return true;
		}

		this.portMode = requested;
		this.markForFluidUpdate();
		return true;
	}

	public boolean canFlush() {
		return !this.sealed && this.getFluidAmount() > 0 && this.brewTimeTotal <= 0;
	}

	public boolean flush() {
		if (!this.canFlush()) {
			return false;
		}

		this.setFluid(null);
		this.resetBrewing();
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

	@Nullable
	public TileEntityBarrel getAdjacentTransferBarrel() {
		if (this.world == null || this.pos == null) {
			return null;
		}

		IBlockState state = this.world.getBlockState(this.pos);

		if (!(state.getBlock() instanceof Foudre)) {
			return null;
		}

		return AdjacentBarrelTransfer.findBarrel(this.world, this.pos, state.getValue(Foudre.FACING));
	}

	@Nullable
	public String getBottleLabel() {
		return this.label.isEmpty() ? null : this.label;
	}

	public boolean canFillFromInletPort(@Nullable Fluid fluid) {
		return !this.sealed && this.isInletOpen() && fluid != null
			&& this.sealedFluidHandler.fill(new FluidStack(fluid, 1), false) > 0;
	}

	public int fillFromInletPort(@Nullable FluidStack resource, boolean doFill) {
		return !this.sealed && this.isInletOpen() ? this.sealedFluidHandler.fill(resource, doFill) : 0;
	}

	public boolean canDrainFromOutletPort(@Nullable Fluid fluid) {
		return !this.sealed && this.isOutletOpen() && fluid != null
			&& this.sealedFluidHandler.drain(new FluidStack(fluid, 1), false) != null;
	}

	@Nullable
	public FluidStack drainFromOutletPort(@Nullable FluidStack resource, boolean doDrain) {
		return !this.sealed && this.isOutletOpen() ? this.sealedFluidHandler.drain(resource, doDrain) : null;
	}

	@Nullable
	public FluidStack drainFromOutletPort(int maxDrain, boolean doDrain) {
		return !this.sealed && this.isOutletOpen() ? this.sealedFluidHandler.drain(maxDrain, doDrain) : null;
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

	public int getBrewTime() {
		return this.brewTime;
	}

	public int getBrewTimeTotal() {
		return this.brewTimeTotal;
	}

	public String getBrewRecipeName() {
		return this.brewRecipeName;
	}

	public String getPotentialRecipeName() {
		if (this.potentialRecipeDirty) {
			FoudreBrewingRecipe recipe = FoudreBrewingRegistry.findPotentialRecipe(this.getFluidDirect(),
				this.inventory);
			this.potentialRecipeName = recipe == null ? "" : recipe.getDisplayName();
			this.potentialRecipeDirty = false;
		}

		return this.potentialRecipeName;
	}

	@Override
	public void markForFluidUpdate() {
		this.potentialRecipeDirty = true;
		super.markForFluidUpdate();
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

	public String getAgeLevelName() {
		return FoudreBrewingRegistry.getAgeLevelName(this.getFluid());
	}

	public void dropIngredients(World worldIn, BlockPos pos) {
		for (int i = 0; i < this.inventory.length; i++) {
			ItemStack stack = this.inventory[i];

			if (stack != null && stack.stackSize > 0) {
				Block.spawnAsEntity(worldIn, pos, stack);
				this.inventory[i] = null;
			}
		}

		this.markDirty();
	}

	public void addIngredientDrops(List<ItemStack> drops) {
		for (ItemStack stack : this.inventory) {
			if (stack != null && stack.stackSize > 0) {
				drops.add(stack.copy());
			}
		}
	}

	@Override
	public void writeToItemStack(ItemStack stack) {
		super.writeToItemStack(stack);

		if (stack == null) {
			return;
		}

		NBTTagCompound tag = stack.hasTagCompound() ? stack.getTagCompound() : new NBTTagCompound();

		if (this.sealed) {
			tag.setBoolean(SEALED_TAG, true);
		} else {
			tag.removeTag(SEALED_TAG);
		}

		tag.removeTag(INLET_OPEN_TAG);
		tag.removeTag(OUTLET_OPEN_TAG);
		tag.removeTag(PORTS_CONFIGURED_TAG);
		tag.removeTag(PORT_MODE_TAG);

		if (this.label.isEmpty()) {
			tag.removeTag(LABEL_TAG);

			if (tag.hasNoTags()) {
				stack.setTagCompound(null);
			} else {
				stack.setTagCompound(tag);
			}

			return;
		}

		tag.setString(LABEL_TAG, this.label);
		stack.setTagCompound(tag);
	}

	@Override
	public void readFromItemStack(ItemStack stack) {
		super.readFromItemStack(stack);
		this.label = stack != null && stack.hasTagCompound() && stack.getTagCompound().hasKey(LABEL_TAG, 8)
			? sanitizeLabel(stack.getTagCompound().getString(LABEL_TAG)) : "";
		this.sealed = stack != null && stack.hasTagCompound() && stack.getTagCompound().getBoolean(SEALED_TAG);
		this.infusionComplete = false;
		this.portMode = FluidPortMode.LOCKED;
		this.sanitizePortState();
		this.markForFluidUpdate();
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		this.label = compound.hasKey(LABEL_TAG, 8) ? sanitizeLabel(compound.getString(LABEL_TAG)) : "";
		this.sealed = compound.getBoolean(SEALED_TAG);
		this.portMode = readPortMode(compound);
		this.sanitizePortState();
		this.brewTime = compound.getInteger(BREW_TIME_TAG);
		this.brewTimeTotal = compound.getInteger(BREW_TIME_TOTAL_TAG);
		this.brewRecipeId = compound.hasKey(BREW_RECIPE_TAG, 8) ? compound.getString(BREW_RECIPE_TAG) : "";
		this.brewRecipeName = compound.hasKey(BREW_RECIPE_NAME_TAG, 8) ? compound.getString(BREW_RECIPE_NAME_TAG)
			: "";
		this.infusionComplete = compound.getBoolean(INFUSION_COMPLETE_TAG);
		this.clear();

		NBTTagList list = compound.getTagList(ITEMS_TAG, 10);

		for (int i = 0; i < list.tagCount(); i++) {
			NBTTagCompound itemTag = list.getCompoundTagAt(i);
			int slot = itemTag.getByte("Slot") & 255;

			if (slot >= 0 && slot < this.inventory.length) {
				this.inventory[slot] = ItemStack.loadItemStackFromNBT(itemTag);
			}
		}

		this.potentialRecipeDirty = true;
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);

		if (!this.label.isEmpty()) {
			compound.setString(LABEL_TAG, this.label);
		}
		compound.setBoolean(SEALED_TAG, this.sealed);
		compound.setByte(PORT_MODE_TAG, (byte)this.portMode.getId());
		compound.removeTag(PORTS_CONFIGURED_TAG);
		compound.removeTag(INLET_OPEN_TAG);
		compound.removeTag(OUTLET_OPEN_TAG);

		compound.setInteger(BREW_TIME_TAG, this.brewTime);
		compound.setInteger(BREW_TIME_TOTAL_TAG, this.brewTimeTotal);
		compound.setBoolean(INFUSION_COMPLETE_TAG, this.infusionComplete);

		if (!this.brewRecipeId.isEmpty()) {
			compound.setString(BREW_RECIPE_TAG, this.brewRecipeId);
		}
		if (!this.brewRecipeName.isEmpty()) {
			compound.setString(BREW_RECIPE_NAME_TAG, this.brewRecipeName);
		}

		NBTTagList list = new NBTTagList();

		for (int i = 0; i < this.inventory.length; i++) {
			ItemStack stack = this.inventory[i];

			if (stack != null && stack.stackSize > 0) {
				NBTTagCompound itemTag = new NBTTagCompound();
				itemTag.setByte("Slot", (byte)i);
				stack.writeToNBT(itemTag);
				list.appendTag(itemTag);
			}
		}

		if (list.tagCount() > 0) {
			compound.setTag(ITEMS_TAG, list);
		}

		return compound;
	}

	@Override
	public int getSizeInventory() {
		return this.inventory.length;
	}

	@Override
	@Nullable
	public ItemStack getStackInSlot(int index) {
		return this.isValidSlot(index) ? this.inventory[index] : null;
	}

	@Override
	@Nullable
	public ItemStack decrStackSize(int index, int count) {
		if (this.sealed) {
			return null;
		}

		ItemStack stack = ItemStackHelper.getAndSplit(this.inventory, index, count);

		if (stack != null) {
			this.markForFluidUpdate();
		}

		return stack;
	}

	@Override
	@Nullable
	public ItemStack removeStackFromSlot(int index) {
		if (this.sealed) {
			return null;
		}

		ItemStack stack = ItemStackHelper.getAndRemove(this.inventory, index);

		if (stack != null) {
			this.markForFluidUpdate();
		}

		return stack;
	}

	@Override
	public void setInventorySlotContents(int index, @Nullable ItemStack stack) {
		if (this.sealed || !this.isValidSlot(index)) {
			return;
		}

		this.inventory[index] = stack;

		if (stack != null && stack.stackSize > this.getInventoryStackLimit()) {
			stack.stackSize = this.getInventoryStackLimit();
		}

		this.markForFluidUpdate();
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
		return !this.sealed && FoudreBrewingRegistry.isValidIngredient(stack);
	}

	@Override
	public IFluidHandler getFluidHandler() {
		return this.sealedFluidHandler;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY
			|| capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			return (T)(facing == null ? this.sealedFluidHandler : new SidedFoudreFluidHandler());
		}

		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return (T)this.itemHandler;
		}

		return super.getCapability(capability, facing);
	}

	@Override
	public int getField(int id) {
		switch (id) {
		case FIELD_BREW_TIME:
			return this.brewTime;
		case FIELD_BREW_TIME_TOTAL:
			return this.brewTimeTotal;
		case FIELD_AGE_PROGRESS:
			return this.getAgeProgress();
		case FIELD_AGE_PROGRESS_TOTAL:
			return this.getAgeProgressTotal();
		case FIELD_SEALED:
			return this.sealed ? 1 : 0;
		case FIELD_INFUSION_COMPLETE:
			return this.infusionComplete ? 1 : 0;
		default:
			return 0;
		}
	}

	@Override
	public void setField(int id, int value) {
		switch (id) {
		case FIELD_BREW_TIME:
			this.brewTime = value;
			break;
		case FIELD_BREW_TIME_TOTAL:
			this.brewTimeTotal = value;
			break;
		case FIELD_AGE_PROGRESS:
			this.clientAgeProgress = value;
			break;
		case FIELD_AGE_PROGRESS_TOTAL:
			this.clientAgeProgressTotal = value;
			break;
		case FIELD_SEALED:
			this.sealed = value != 0;
			this.sanitizePortState();
			break;
		case FIELD_INFUSION_COMPLETE:
			this.infusionComplete = value != 0;
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
		return from == null ? super.fill(null, resource, doFill) : 0;
	}

	@Override
	@Nullable
	public FluidStack drain(EnumFacing from, FluidStack resource, boolean doDrain) {
		return from == null ? super.drain(null, resource, doDrain) : null;
	}

	@Override
	@Nullable
	public FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain) {
		return from == null ? super.drain(null, maxDrain, doDrain) : null;
	}

	@Override
	public boolean canFill(EnumFacing from, Fluid fluid) {
		return from == null && super.canFill(null, fluid);
	}

	@Override
	public boolean canDrain(EnumFacing from, Fluid fluid) {
		return from == null && super.canDrain(null, fluid);
	}

	@Override
	public FluidTankInfo[] getTankInfo(EnumFacing from) {
		return from == null ? super.getTankInfo(null) : new FluidTankInfo[0];
	}

	@Override
	public void clear() {
		for (int i = 0; i < this.inventory.length; i++) {
			this.inventory[i] = null;
		}
	}

	private void storeOrDropRemainders(List<ItemStack> remainders) {
		if (remainders == null || remainders.isEmpty()) {
			return;
		}

		for (ItemStack remainder : remainders) {
			if (remainder == null || remainder.stackSize <= 0) {
				continue;
			}
			for (int slot = 0; slot < this.inventory.length && remainder.stackSize > 0; slot++) {
				ItemStack existing = this.inventory[slot];
				if (existing == null) {
					this.inventory[slot] = remainder.copy();
					remainder.stackSize = 0;
					break;
				}
				if (existing.getItem() == remainder.getItem()
						&& existing.getItemDamage() == remainder.getItemDamage()
						&& ItemStack.areItemStackTagsEqual(existing, remainder)
						&& existing.stackSize < existing.getMaxStackSize()) {
					int moved = Math.min(remainder.stackSize, existing.getMaxStackSize() - existing.stackSize);
					existing.stackSize += moved;
					remainder.stackSize -= moved;
				}
			}
			if (remainder.stackSize > 0) {
				Block.spawnAsEntity(this.world, this.pos, remainder);
			}
		}
		this.markDirty();
	}

	private void resetBrewing() {
		if (this.brewTime == 0 && this.brewTimeTotal == 0 && this.brewRecipeId.isEmpty()
				&& this.brewRecipeName.isEmpty()) {
			return;
		}

		this.brewTime = 0;
		this.brewTimeTotal = 0;
		this.brewRecipeId = "";
		this.brewRecipeName = "";
		this.markForFluidUpdate();
	}

	private void updateAging() {
		FluidStack fluid = this.getFluidDirect();

		if (!FoudreBrewingRegistry.canAgeFurther(fluid)) {
			return;
		}

		if (FoudreBrewingRegistry.ageFluid(fluid, 1)) {
			this.markForFluidUpdate();
		} else if (this.world.getTotalWorldTime() % 20L == 0L) {
			this.markDirty();
		}
	}

	private boolean shouldAge() {
		return this.sealed && FoudreBrewingRegistry.canAgeFurther(this.getFluidDirect());
	}

	private void sanitizePortState() {
		if (this.sealed) {
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
			this.markForFluidUpdate();
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

		if (!(state.getBlock() instanceof Foudre)) {
			return false;
		}

		FoudrePart part = inlet ? FoudrePart.BACK_LEFT : FoudrePart.BACK_RIGHT;
		BlockPos portPos = Foudre.resolvePartPos(this.pos, state.getValue(Foudre.FACING), part, inlet);
		BlockPos attachmentPos = portPos.offset(Foudre.getPortFace(state.getValue(Foudre.FACING)));
		TileEntity tileEntity = this.world.getTileEntity(attachmentPos);

		if (tileEntity instanceof net.minecraftforge.fluids.IFluidHandler) {
			return true;
		}

		Block block = this.world.getBlockState(attachmentPos).getBlock();
		return block != null && block.getRegistryName() != null
			&& block.getRegistryName().toString().endsWith("fluid_pipe_terminal");
	}

	private boolean transferAdjacentBarrel(boolean barrelToFoudre, boolean doTransfer) {
		if (this.world == null || this.pos == null || doTransfer && this.world.isRemote) {
			return false;
		}

		IBlockState state = this.world.getBlockState(this.pos);

		if (!(state.getBlock() instanceof Foudre)) {
			return false;
		}

		boolean transferred = AdjacentBarrelTransfer.transfer(this.world, this.pos, state.getValue(Foudre.FACING),
			this.getFluidHandler(), barrelToFoudre, doTransfer);

		if (transferred && doTransfer) {
			this.markForFluidUpdate();
		}

		return transferred;
	}

	private boolean isValidSlot(int index) {
		return index >= 0 && index < this.inventory.length;
	}

	private final class SealedFoudreFluidHandler implements IFluidHandler {
		@Override
		public IFluidTankProperties[] getTankProperties() {
			return TileEntityFoudre.super.getFluidHandler().getTankProperties();
		}

		@Override
		public int fill(FluidStack resource, boolean doFill) {
			return TileEntityFoudre.this.sealed ? 0
				: TileEntityFoudre.super.getFluidHandler().fill(resource, doFill);
		}

		@Override
		@Nullable
		public FluidStack drain(FluidStack resource, boolean doDrain) {
			return TileEntityFoudre.this.sealed ? null
				: TileEntityFoudre.super.getFluidHandler().drain(resource, doDrain);
		}

		@Override
		@Nullable
		public FluidStack drain(int maxDrain, boolean doDrain) {
			return TileEntityFoudre.this.sealed ? null
				: TileEntityFoudre.super.getFluidHandler().drain(maxDrain, doDrain);
		}
	}

	private final class SidedFoudreFluidHandler implements IFluidHandler {
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

	private static String sanitizeLabel(@Nullable String label) {
		if (label == null) {
			return "";
		}

		String trimmed = label.trim();
		return trimmed.length() > 32 ? trimmed.substring(0, 32) : trimmed;
	}
}
