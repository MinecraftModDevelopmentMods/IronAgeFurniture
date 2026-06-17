package com.mcmoddev.ironagefurniture.api.Items;

import java.util.List;

import javax.annotation.Nullable;

import com.mcmoddev.ironagefurniture.ItemObjectHolder;
import com.mcmoddev.ironagefurniture.api.DrinkDisplayHelper;
import com.mcmoddev.ironagefurniture.api.FoudreBrewingRegistry;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStackSimple;

public class ItemFluidBottle extends Item {
	public static final int CAPACITY = Fluid.BUCKET_VOLUME / 4;
	public static final String LABEL_TAG = "BottleLabel";

	public ItemFluidBottle() {
		this.setMaxStackSize(64);
	}

	@Override
	public int getItemStackLimit(ItemStack stack) {
		return 64;
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
		return new FluidHandlerItemStackSimple(stack, CAPACITY);
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		FluidStack fluid = getFluid(stack);
		return fluid != null && fluid.getFluid() != null ? DrinkDisplayHelper.getDisplayName(fluid) + " Bottle"
			: super.getItemStackDisplayName(stack);
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
		EntityPlayer player = entityLiving instanceof EntityPlayer ? (EntityPlayer)entityLiving : null;

		if (!isFilled(stack) || !FoudreBrewingRegistry.isDrinkable(getFluid(stack))) {
			return stack;
		}

		if (player != null) {
			player.addStat(StatList.getObjectUseStats(this));
		}

		if (player == null || !player.capabilities.isCreativeMode) {
			stack.stackSize--;

			if (stack.stackSize <= 0) {
				return new ItemStack(Items.GLASS_BOTTLE);
			}

			if (player != null) {
				giveOrDrop(player, new ItemStack(Items.GLASS_BOTTLE));
			}
		}

		return stack;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 32;
	}

	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		return EnumAction.DRINK;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn,
			EnumHand hand) {
		if (!isFilled(itemStackIn) || !FoudreBrewingRegistry.isDrinkable(getFluid(itemStackIn))) {
			return new ActionResult<ItemStack>(EnumActionResult.PASS, itemStackIn);
		}

		playerIn.setActiveHand(hand);
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStackIn);
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if (!worldIn.isRemote) {
			normalizeFluidAge(stack);
		}
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
		FluidStack fluid = getFluid(stack);

		if (fluid != null && fluid.getFluid() != null && fluid.amount > 0) {
			tooltip.add(DrinkDisplayHelper.getDisplayName(fluid));
			this.addQualityTooltip(fluid, tooltip);
			tooltip.add(fluid.amount + " / " + CAPACITY + " mB");
		}

		String label = getBottleLabel(stack);

		if (label != null && !label.isEmpty()) {
			tooltip.add(TextFormatting.GRAY + "Bottled at " + label);
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
		if (heldItem == null || heldItem.stackSize <= 0 || tank == null) {
			return false;
		}

		if (heldItem.getItem() instanceof ItemFluidBottle && isFilled(heldItem)) {
			return tryEmptyIntoTank(heldItem, tank, player, hand);
		}

		if (heldItem.getItem() == Items.GLASS_BOTTLE) {
			return tryFillFromTank(heldItem, tank, player, hand, fillLabel);
		}

		return false;
	}

	private static boolean tryFillFromTank(ItemStack heldItem, IFluidHandler tank, EntityPlayer player,
			EnumHand hand, @Nullable String fillLabel) {
		if (ItemObjectHolder.fluid_bottle == null) {
			return false;
		}

		FluidStack drained = tank.drain(CAPACITY, false);

		if (drained == null || drained.getFluid() == null || drained.amount < CAPACITY) {
			return false;
		}

		if (!FoudreBrewingRegistry.isBottleable(drained)) {
			return false;
		}

		FluidStack bottleFluid = FoudreBrewingRegistry.copyWithCurrentAgeLevel(drained);
		bottleFluid.amount = CAPACITY;
		ItemStack filledBottle = new ItemStack(ItemObjectHolder.fluid_bottle);
		writeFluid(filledBottle, bottleFluid);
		setBottleLabel(filledBottle, fillLabel);

		tank.drain(CAPACITY, true);
		heldItem.stackSize--;

		if (heldItem.stackSize <= 0) {
			player.setHeldItem(hand, null);
		}

		giveOrDrop(player, filledBottle);

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

		tank.fill(toFill, true);
		heldItem.stackSize--;

		ItemStack emptyBottle = new ItemStack(Items.GLASS_BOTTLE);

		if (heldItem.stackSize <= 0) {
			player.setHeldItem(hand, emptyBottle);
		} else {
			giveOrDrop(player, emptyBottle);
		}

		player.playSound(fluid.getFluid().getEmptySound(fluid), 1.0F, 1.0F);
		return true;
	}

	private static void writeFluid(ItemStack stack, FluidStack fluid) {
		NBTTagCompound tag = stack.hasTagCompound() ? stack.getTagCompound() : new NBTTagCompound();
		tag.setTag(FluidHandlerItemStackSimple.FLUID_NBT_KEY, fluid.writeToNBT(new NBTTagCompound()));
		stack.setTagCompound(tag);
	}

	private static void normalizeFluidAge(ItemStack stack) {
		FluidStack fluid = getFluid(stack);

		if (fluid != null && FoudreBrewingRegistry.resetAgeProgressToCurrentLevel(fluid)) {
			writeFluid(stack, fluid);
		}
	}

	private void addQualityTooltip(FluidStack fluid, List<String> tooltip) {
		for (String line : DrinkDisplayHelper.getQualityTooltipLines(fluid)) {
			if (!line.isEmpty()) {
				tooltip.add(TextFormatting.GRAY + line);
			}
		}
	}

	private static void giveOrDrop(EntityPlayer player, @Nullable ItemStack stack) {
		if (stack == null || stack.stackSize <= 0) {
			return;
		}

		if (!player.inventory.addItemStackToInventory(stack)) {
			player.dropItem(stack, false);
		}
	}

	private static String sanitizeLabel(String label) {
		String trimmed = label.trim();
		return trimmed.length() > 32 ? trimmed.substring(0, 32) : trimmed;
	}
}
