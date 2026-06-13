package com.mcmoddev.ironagefurniture.api.Items;

import java.util.List;

import javax.annotation.Nullable;

import com.mcmoddev.ironagefurniture.Ironagefurniture;
import com.mcmoddev.ironagefurniture.api.FoudreBrewingRegistry;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStackSimple;

public class ItemFluidBottle extends Item {
	public static final int CAPACITY = Fluid.BUCKET_VOLUME / 4;
	public static final String LABEL_TAG = "BottleLabel";

	public ItemFluidBottle() {
		this.setCreativeTab(Ironagefurniture.ironagefurnitureTab);
		this.setMaxStackSize(16);
	}

	@Override
	public int getItemStackLimit(ItemStack stack) {
		return isFilled(stack) ? 1 : 16;
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
		return new FluidHandlerItemStackSimple(stack, CAPACITY);
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		String label = getBottleLabel(stack);

		if (label != null && !label.isEmpty()) {
			return label + " Bottle";
		}

		FluidStack fluid = getFluid(stack);
		return fluid != null && fluid.getFluid() != null ? FoudreBrewingRegistry.getAgedFluidName(fluid) + " Bottle"
			: super.getItemStackDisplayName(stack);
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
		FluidStack fluid = getFluid(stack);

		if (fluid != null && fluid.getFluid() != null && fluid.amount > 0) {
			tooltip.add(FoudreBrewingRegistry.getAgedFluidName(fluid));
			this.addAgeTooltip(fluid, tooltip);
			tooltip.add(fluid.amount + " / " + CAPACITY + " mB");
		}

		String label = getBottleLabel(stack);

		if (label != null && !label.isEmpty()) {
			tooltip.add(TextFormatting.GRAY + label);
		}
	}

	public static boolean isFilled(ItemStack stack) {
		FluidStack fluid = getFluid(stack);
		return fluid != null && fluid.amount > 0;
	}

	@Nullable
	public static FluidStack getFluid(ItemStack stack) {
		if (stack == null || !stack.hasTagCompound() || !stack.getTagCompound().hasKey(
				FluidHandlerItemStackSimple.FLUID_NBT_KEY, 10)) {
			return null;
		}

		return FluidStack.loadFluidStackFromNBT(stack.getTagCompound().getCompoundTag(
			FluidHandlerItemStackSimple.FLUID_NBT_KEY));
	}

	public static int getFluidColor(ItemStack stack, int fallback) {
		FluidStack fluid = getFluid(stack);
		return fluid != null && fluid.getFluid() != null ? fluid.getFluid().getColor(fluid) : fallback;
	}

	@Nullable
	public static String getBottleLabel(ItemStack stack) {
		if (stack == null || !stack.hasTagCompound() || !stack.getTagCompound().hasKey(LABEL_TAG, 8)) {
			return null;
		}

		return stack.getTagCompound().getString(LABEL_TAG);
	}

	public static void setBottleLabel(ItemStack stack, @Nullable String label) {
		if (stack == null) {
			return;
		}

		if (label == null || label.trim().isEmpty()) {
			if (stack.hasTagCompound()) {
				stack.getTagCompound().removeTag(LABEL_TAG);

				if (stack.getTagCompound().hasNoTags()) {
					stack.setTagCompound(null);
				}
			}

			return;
		}

		NBTTagCompound tag = stack.hasTagCompound() ? stack.getTagCompound() : new NBTTagCompound();
		tag.setString(LABEL_TAG, sanitizeLabel(label));
		stack.setTagCompound(tag);
	}

	public static boolean tryUseWithTank(ItemStack heldItem, IFluidHandler tank, EntityPlayer player,
			EnumHand hand, @Nullable String fillLabel) {
		if (heldItem == null || heldItem.stackSize <= 0 || tank == null || !(heldItem.getItem() instanceof ItemFluidBottle)) {
			return false;
		}

		if (isFilled(heldItem)) {
			return tryEmptyIntoTank(heldItem, tank, player, hand);
		}

		return tryFillFromTank(heldItem, tank, player, hand, fillLabel);
	}

	private static boolean tryFillFromTank(ItemStack heldItem, IFluidHandler tank, EntityPlayer player,
			EnumHand hand, @Nullable String fillLabel) {
		FluidStack drained = tank.drain(CAPACITY, false);

		if (drained == null || drained.getFluid() == null || drained.amount < CAPACITY) {
			return false;
		}

		FluidStack bottleFluid = drained.copy();
		bottleFluid.amount = CAPACITY;
		ItemStack filledBottle = heldItem.copy();
		filledBottle.stackSize = 1;
		writeFluid(filledBottle, bottleFluid);
		setBottleLabel(filledBottle, fillLabel);

		if (player.capabilities.isCreativeMode) {
			if (!player.inventory.addItemStackToInventory(filledBottle.copy())) {
				player.dropItem(filledBottle.copy(), false);
			}
		} else {
			tank.drain(CAPACITY, true);
			heldItem.stackSize--;

			if (heldItem.stackSize <= 0) {
				player.setHeldItem(hand, filledBottle);
			} else if (!player.inventory.addItemStackToInventory(filledBottle)) {
				player.dropItem(filledBottle, false);
			}
		}

		player.playSound(bottleFluid.getFluid().getFillSound(bottleFluid), 1.0F, 1.0F);
		return true;
	}

	private static boolean tryEmptyIntoTank(ItemStack heldItem, IFluidHandler tank, EntityPlayer player,
			EnumHand hand) {
		FluidStack fluid = getFluid(heldItem);

		if (fluid == null || fluid.getFluid() == null || fluid.amount <= 0) {
			return false;
		}

		FluidStack toFill = fluid.copy();
		int filled = tank.fill(toFill, false);

		if (filled < toFill.amount) {
			return false;
		}

		if (player.capabilities.isCreativeMode) {
			tank.fill(toFill, true);
		} else {
			tank.fill(toFill, true);
			heldItem.stackSize--;

			ItemStack emptyBottle = new ItemStack(heldItem.getItem());

			if (heldItem.stackSize <= 0) {
				player.setHeldItem(hand, emptyBottle);
			} else if (!player.inventory.addItemStackToInventory(emptyBottle)) {
				player.dropItem(emptyBottle, false);
			}
		}

		player.playSound(fluid.getFluid().getEmptySound(fluid), 1.0F, 1.0F);
		return true;
	}

	private static void writeFluid(ItemStack stack, FluidStack fluid) {
		NBTTagCompound tag = stack.hasTagCompound() ? stack.getTagCompound() : new NBTTagCompound();
		tag.setTag(FluidHandlerItemStackSimple.FLUID_NBT_KEY, fluid.writeToNBT(new NBTTagCompound()));
		stack.setTagCompound(tag);
	}

	private void addAgeTooltip(FluidStack fluid, List<String> tooltip) {
		String age = FoudreBrewingRegistry.getAgeLevelName(fluid);

		if (!age.isEmpty()) {
			tooltip.add(TextFormatting.GRAY + "Age: " + age);
		}
	}

	private static String sanitizeLabel(String label) {
		String trimmed = label.trim();
		return trimmed.length() > 32 ? trimmed.substring(0, 32) : trimmed;
	}
}
