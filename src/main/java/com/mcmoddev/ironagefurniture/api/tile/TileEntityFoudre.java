package com.mcmoddev.ironagefurniture.api.tile;

import java.util.List;

import javax.annotation.Nullable;

import com.mcmoddev.ironagefurniture.api.FoudreBrewingRegistry;
import com.mcmoddev.ironagefurniture.api.FoudreBrewingRegistry.FoudreBrewingRecipe;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

public class TileEntityFoudre extends TileEntityBarrel implements IInventory, ITickable {
	public static final int CAPACITY = 128 * Fluid.BUCKET_VOLUME;
	public static final int INGREDIENT_SLOTS = 4;
	public static final int FIELD_BREW_TIME = 0;
	public static final int FIELD_BREW_TIME_TOTAL = 1;
	public static final int FIELD_AGE_PROGRESS = 2;
	public static final int FIELD_AGE_PROGRESS_TOTAL = 3;
	public static final int FIELD_SEALED = 4;

	private static final String LABEL_TAG = "FoudreLabel";
	private static final String SEALED_TAG = "Sealed";
	private static final String ITEMS_TAG = "Ingredients";
	private static final String BREW_TIME_TAG = "BrewTime";
	private static final String BREW_TIME_TOTAL_TAG = "BrewTimeTotal";
	private static final String BREW_RECIPE_TAG = "BrewRecipe";
	private static final String BREW_RECIPE_NAME_TAG = "BrewRecipeName";

	private final ItemStack[] inventory = new ItemStack[INGREDIENT_SLOTS];
	private final IFluidHandler sealedFluidHandler = new SealedFoudreFluidHandler();
	private String label = "";
	private boolean sealed;
	private int brewTime;
	private int brewTimeTotal;
	private int clientAgeProgress;
	private int clientAgeProgressTotal;
	private String brewRecipeId = "";
	private String brewRecipeName = "";

	public TileEntityFoudre() {
		super(CAPACITY);
	}

	@Override
	public String getContainerNameKey() {
		return "container.ironagefurniture.foudre";
	}

	@Override
	public void update() {
		if (this.world == null || this.world.isRemote) {
			return;
		}

		FoudreBrewingRecipe recipe = FoudreBrewingRegistry.findMatchingRecipe(this.getFluid(), this.inventory,
			this.getCapacity());

		if (recipe == null) {
			this.resetBrewing();
			if (this.sealed) {
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
			recipe.consumeIngredients(this.inventory);
			this.setFluid(FoudreBrewingRegistry.createBrewedFluid(recipe, this.getCapacity()));
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

		if (opening) {
			FoudreBrewingRegistry.resetAgeProgressToCurrentLevel(this.getFluidDirect());
		}

		this.markForFluidUpdate();
	}

	public void toggleSealed() {
		this.setSealed(!this.sealed);
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

	@Nullable
	public String getBottleLabel() {
		return this.label.isEmpty() ? null : this.label;
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
		this.markForFluidUpdate();
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		this.label = compound.hasKey(LABEL_TAG, 8) ? sanitizeLabel(compound.getString(LABEL_TAG)) : "";
		this.sealed = compound.getBoolean(SEALED_TAG);
		this.brewTime = compound.getInteger(BREW_TIME_TAG);
		this.brewTimeTotal = compound.getInteger(BREW_TIME_TOTAL_TAG);
		this.brewRecipeId = compound.hasKey(BREW_RECIPE_TAG, 8) ? compound.getString(BREW_RECIPE_TAG) : "";
		this.brewRecipeName = compound.hasKey(BREW_RECIPE_NAME_TAG, 8) ? compound.getString(BREW_RECIPE_NAME_TAG)
			: "";
		this.clear();

		NBTTagList list = compound.getTagList(ITEMS_TAG, 10);

		for (int i = 0; i < list.tagCount(); i++) {
			NBTTagCompound itemTag = list.getCompoundTagAt(i);
			int slot = itemTag.getByte("Slot") & 255;

			if (slot >= 0 && slot < this.inventory.length) {
				this.inventory[slot] = ItemStack.loadItemStackFromNBT(itemTag);
			}
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);

		if (!this.label.isEmpty()) {
			compound.setString(LABEL_TAG, this.label);
		}
		compound.setBoolean(SEALED_TAG, this.sealed);

		compound.setInteger(BREW_TIME_TAG, this.brewTime);
		compound.setInteger(BREW_TIME_TOTAL_TAG, this.brewTimeTotal);

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
	public boolean hasCapability(Capability<?> capability, net.minecraft.util.EnumFacing facing) {
		return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T getCapability(Capability<T> capability, net.minecraft.util.EnumFacing facing) {
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			return (T)this.sealedFluidHandler;
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
			break;
		default:
			break;
		}
	}

	@Override
	public int getFieldCount() {
		return 5;
	}

	@Override
	public void clear() {
		for (int i = 0; i < this.inventory.length; i++) {
			this.inventory[i] = null;
		}
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

		if (!FoudreBrewingRegistry.isAgeable(fluid)) {
			return;
		}

		if (FoudreBrewingRegistry.ageFluid(fluid, 1)) {
			this.markForFluidUpdate();
		} else if (this.world.getTotalWorldTime() % 20L == 0L) {
			this.markDirty();
		}
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

	private static String sanitizeLabel(@Nullable String label) {
		if (label == null) {
			return "";
		}

		String trimmed = label.trim();
		return trimmed.length() > 32 ? trimmed.substring(0, 32) : trimmed;
	}
}
