package zone.moddev.mc.ironagefurniture.api.Items;

import javax.annotation.Nullable;

import zone.moddev.mc.ironagefurniture.ItemObjectHolder;
import zone.moddev.mc.ironagefurniture.api.FoudreBrewingRegistry;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStackSimple;

/** Shared fill, pour, label, and NBT behavior for bottles and reusable drinkware. */
public final class DrinkContainerHelper {
	public static final String BOTTLE_LABEL_TAG = "BottleLabel";
	public static final String SERVING_LABEL_TAG = "ServingLabel";
	private static final String FLUID_PRODUCER_TAG = "ProducerLabel";

	private DrinkContainerHelper() {
		throw new IllegalAccessError("This class cannot be instantiated");
	}

	public static boolean isTankInteractionItem(ItemStack stack) {
		if (stack == null || stack.stackSize <= 0) {
			return false;
		}

		return stack.getItem() == Items.GLASS_BOTTLE
			|| stack.getItem() instanceof ItemFluidBottle
			|| stack.getItem() instanceof ItemDrinkware;
	}

	public static boolean isFilled(ItemStack stack) {
		FluidStack fluid = getFluid(stack);
		return fluid != null && fluid.getFluid() != null && fluid.amount > 0;
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

	public static void writeFluid(ItemStack stack, FluidStack fluid) {
		if (stack == null || fluid == null || fluid.getFluid() == null || fluid.amount <= 0) {
			return;
		}

		NBTTagCompound tag = stack.hasTagCompound() ? stack.getTagCompound() : new NBTTagCompound();
		tag.setTag(FluidHandlerItemStackSimple.FLUID_NBT_KEY, fluid.writeToNBT(new NBTTagCompound()));
		stack.setTagCompound(tag);
	}

	public static void normalizeFluidAge(ItemStack stack) {
		FluidStack fluid = getFluid(stack);

		if (fluid != null && FoudreBrewingRegistry.resetAgeProgressToCurrentLevel(fluid)) {
			writeFluid(stack, fluid);
		}
	}

	@Nullable
	public static String getContainerLabel(ItemStack stack) {
		String key = getLabelKey(stack);

		if (key == null || !stack.hasTagCompound() || !stack.getTagCompound().hasKey(key, 8)) {
			return null;
		}

		return stack.getTagCompound().getString(key);
	}

	public static void setContainerLabel(ItemStack stack, @Nullable String label) {
		String key = getLabelKey(stack);

		if (stack == null || key == null) {
			return;
		}

		if (label == null || label.trim().isEmpty()) {
			if (stack.hasTagCompound()) {
				stack.getTagCompound().removeTag(key);

				if (stack.getTagCompound().hasNoTags()) {
					stack.setTagCompound(null);
				}
			}

			return;
		}

		NBTTagCompound tag = stack.hasTagCompound() ? stack.getTagCompound() : new NBTTagCompound();
		tag.setString(key, sanitizeLabel(label));
		stack.setTagCompound(tag);
	}

	public static boolean tryUseWithTank(ItemStack heldItem, IFluidHandler tank, EntityPlayer player,
			EnumHand hand, @Nullable String fillLabel) {
		if (heldItem == null || heldItem.stackSize <= 0 || tank == null || player == null) {
			return false;
		}

		if ((heldItem.getItem() instanceof ItemFluidBottle || heldItem.getItem() instanceof ItemDrinkware)
				&& isFilled(heldItem)) {
			return tryEmptyIntoTank(heldItem, tank, player, hand);
		}

		if (heldItem.getItem() == Items.GLASS_BOTTLE
				|| heldItem.getItem() instanceof ItemFluidBottle
				|| heldItem.getItem() instanceof ItemDrinkware) {
			return tryFillFromTank(heldItem, tank, player, hand, fillLabel);
		}

		return false;
	}

	public static ItemStack createEmptyContainer(ItemStack filledStack) {
		if (filledStack == null) {
			return null;
		}

		if (filledStack.getItem() instanceof ItemDrinkware) {
			return new ItemStack(filledStack.getItem(), 1, filledStack.getMetadata());
		}

		if (filledStack.getItem() instanceof ItemFluidBottle) {
			return new ItemStack(Items.GLASS_BOTTLE);
		}

		return null;
	}

	public static boolean canFillDisplayedDrinkware(ItemStack displayedItem, ItemStack heldItem) {
		if (displayedItem == null || !(displayedItem.getItem() instanceof ItemDrinkware)
				|| isFilled(displayedItem) || heldItem == null || heldItem.stackSize <= 0
				|| (!(heldItem.getItem() instanceof ItemFluidBottle)
					&& !(heldItem.getItem() instanceof ItemDrinkware))) {
			return false;
		}

		FluidStack fluid = getFluid(heldItem);
		int capacity = ((ItemDrinkware)displayedItem.getItem()).getCapacity(displayedItem);
		return capacity > 0 && fluid != null && fluid.amount >= capacity
			&& FoudreBrewingRegistry.isBottleable(fluid);
	}

	@Nullable
	public static ItemStack fillDisplayedDrinkware(ItemStack displayedItem, ItemStack heldItem,
			EntityPlayer player, EnumHand hand) {
		if (!canFillDisplayedDrinkware(displayedItem, heldItem) || player == null) {
			return null;
		}

		int capacity = ((ItemDrinkware)displayedItem.getItem()).getCapacity(displayedItem);
		FluidStack sourceFluid = getFluid(heldItem);
		ItemStack filledDrinkware = displayedItem.copy();
		filledDrinkware.stackSize = 1;
		FluidStack serving = sourceFluid.copy();
		serving.amount = capacity;
		writeFluid(filledDrinkware, serving);
		setContainerLabel(filledDrinkware, getContainerLabel(heldItem));

		if (!player.capabilities.isCreativeMode) {
			ItemStack remainder = sourceFluid.amount == capacity
				? createEmptyContainer(heldItem) : createPartiallyFilledContainer(heldItem,
					sourceFluid, sourceFluid.amount - capacity);
			heldItem.stackSize--;

			if (heldItem.stackSize <= 0) {
				player.setHeldItem(hand, remainder);
			} else {
				giveOrDrop(player, remainder);
			}
		}

		player.playSound(sourceFluid.getFluid().getEmptySound(sourceFluid), 1.0F, 1.0F);
		return filledDrinkware;
	}

	public static void giveOrDrop(EntityPlayer player, @Nullable ItemStack stack) {
		if (player == null || stack == null || stack.stackSize <= 0) {
			return;
		}

		if (!player.inventory.addItemStackToInventory(stack)) {
			player.dropItem(stack, false);
		}
	}

	private static boolean tryFillFromTank(ItemStack heldItem, IFluidHandler tank, EntityPlayer player,
			EnumHand hand, @Nullable String fillLabel) {
		int capacity = getCapacityForEmptyContainer(heldItem);

		if (capacity <= 0) {
			return false;
		}

		FluidStack drained = tank.drain(capacity, false);

		if (drained == null || drained.getFluid() == null || drained.amount < capacity
				|| !FoudreBrewingRegistry.isBottleable(drained)) {
			return false;
		}

		ItemStack filledContainer = createFilledContainer(heldItem);

		if (filledContainer == null) {
			return false;
		}

		FluidStack containerFluid = FoudreBrewingRegistry.copyWithCurrentAgeLevel(drained);
		containerFluid.amount = capacity;
		writeFluid(filledContainer, containerFluid);
		String producer = fillLabel == null || fillLabel.trim().isEmpty()
			? getFluidProducer(containerFluid) : fillLabel;
		setContainerLabel(filledContainer, producer);

		tank.drain(capacity, true);
		heldItem.stackSize--;

		if (heldItem.stackSize <= 0) {
			player.setHeldItem(hand, null);
		}

		giveOrDrop(player, filledContainer);
		player.playSound(containerFluid.getFluid().getFillSound(containerFluid), 1.0F, 1.0F);
		return true;
	}

	private static boolean tryEmptyIntoTank(ItemStack heldItem, IFluidHandler tank, EntityPlayer player,
			EnumHand hand) {
		FluidStack fluid = getFluid(heldItem);

		if (fluid == null || fluid.getFluid() == null || fluid.amount <= 0) {
			return false;
		}

		FluidStack toFill = fluid.copy();
		String producer = getContainerLabel(heldItem);
		setFluidProducer(toFill, producer);
		int filled = tank.fill(toFill, false);

		if (filled < toFill.amount) {
			return false;
		}

		tank.fill(toFill, true);
		heldItem.stackSize--;
		ItemStack emptyContainer = createEmptyContainer(heldItem);

		if (heldItem.stackSize <= 0) {
			player.setHeldItem(hand, emptyContainer);
		} else {
			giveOrDrop(player, emptyContainer);
		}

		player.playSound(fluid.getFluid().getEmptySound(fluid), 1.0F, 1.0F);
		return true;
	}

	private static int getCapacityForEmptyContainer(ItemStack stack) {
		if (stack.getItem() == Items.GLASS_BOTTLE || stack.getItem() instanceof ItemFluidBottle) {
			return ItemFluidBottle.CAPACITY;
		}

		return stack.getItem() instanceof ItemDrinkware ? ((ItemDrinkware)stack.getItem()).getCapacity(stack) : 0;
	}

	private static ItemStack createFilledContainer(ItemStack emptyStack) {
		if (emptyStack.getItem() == Items.GLASS_BOTTLE) {
			return ItemObjectHolder.fluid_bottle == null ? null : new ItemStack(ItemObjectHolder.fluid_bottle);
		}

		if (emptyStack.getItem() instanceof ItemFluidBottle || emptyStack.getItem() instanceof ItemDrinkware) {
			return new ItemStack(emptyStack.getItem(), 1, emptyStack.getMetadata());
		}

		return null;
	}

	private static ItemStack createPartiallyFilledContainer(ItemStack sourceStack, FluidStack sourceFluid,
			int remainingAmount) {
		ItemStack remainder = sourceStack.copy();
		remainder.stackSize = 1;
		FluidStack remainingFluid = sourceFluid.copy();
		remainingFluid.amount = remainingAmount;
		writeFluid(remainder, remainingFluid);
		return remainder;
	}

	@Nullable
	private static String getLabelKey(ItemStack stack) {
		if (stack == null) {
			return null;
		}

		return stack.getItem() instanceof ItemDrinkware ? SERVING_LABEL_TAG
			: stack.getItem() instanceof ItemFluidBottle ? BOTTLE_LABEL_TAG : null;
	}

	@Nullable
	private static String getFluidProducer(FluidStack fluid) {
		return fluid != null && fluid.tag != null && fluid.tag.hasKey(FLUID_PRODUCER_TAG, 8)
			? fluid.tag.getString(FLUID_PRODUCER_TAG) : null;
	}

	private static void setFluidProducer(FluidStack fluid, @Nullable String label) {
		if (fluid == null || label == null || label.trim().isEmpty()) {
			return;
		}

		if (fluid.tag == null) {
			fluid.tag = new NBTTagCompound();
		}

		fluid.tag.setString(FLUID_PRODUCER_TAG, sanitizeLabel(label));
	}

	private static String sanitizeLabel(String label) {
		String trimmed = label.trim();
		return trimmed.length() > 32 ? trimmed.substring(0, 32) : trimmed;
	}
}
