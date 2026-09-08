package zone.moddev.mc.ironagefurniture.api;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.UniversalBucket;
import net.minecraftforge.fluids.capability.IFluidHandler;

public final class FluidContainerTransfer {
	private FluidContainerTransfer() {
		throw new IllegalAccessError("This class cannot be instantiated");
	}

	public static boolean isBucketInteractionItem(ItemStack heldItem) {
		if (heldItem == null || heldItem.stackSize <= 0) {
			return false;
		}

		if (heldItem.getItem() == Items.BUCKET || isUniversalBucket(heldItem)) {
			return true;
		}

		ItemStack singleItem = heldItem.copy();
		singleItem.stackSize = 1;
		return FluidUtil.getFluidHandler(singleItem) != null;
	}

	public static boolean tryUseBucketWithTank(ItemStack heldItem, IFluidHandler tank, EntityPlayer player,
			EnumHand hand) {
		if (heldItem == null || heldItem.stackSize <= 0 || tank == null) {
			return false;
		}

		if (heldItem.getItem() == Items.BUCKET) {
			return tryFillBucketFromTank(heldItem, tank, player, hand);
		}

		if (isUniversalBucket(heldItem)) {
			return tryEmptyUniversalBucketIntoTank(heldItem, tank, player, hand);
		}

		return false;
	}

	private static boolean tryFillBucketFromTank(ItemStack heldItem, IFluidHandler tank, EntityPlayer player,
			EnumHand hand) {
		FluidStack drained = tank.drain(Fluid.BUCKET_VOLUME, false);

		if (drained == null || drained.getFluid() == null || drained.amount < Fluid.BUCKET_VOLUME) {
			return false;
		}

		FluidStack bucketFluid = FoudreBrewingRegistry.copyWithCurrentAgeLevel(drained);
		bucketFluid.amount = Fluid.BUCKET_VOLUME;
		ItemStack filledBucket = createFilledBucket(bucketFluid);

		if (filledBucket == null) {
			return false;
		}

		tank.drain(bucketFluid, true);
		heldItem.stackSize--;
		replaceOneHeldItem(player, hand, heldItem, filledBucket);
		player.playSound(bucketFluid.getFluid().getFillSound(bucketFluid), 1.0F, 1.0F);
		return true;
	}

	private static boolean tryEmptyUniversalBucketIntoTank(ItemStack heldItem, IFluidHandler tank, EntityPlayer player,
			EnumHand hand) {
		FluidStack contained = getUniversalBucketFluid(heldItem);

		if (contained == null || contained.getFluid() == null || contained.amount <= 0) {
			return false;
		}

		FluidStack toFill = contained.copy();
		int filled = tank.fill(toFill, false);

		if (filled < toFill.amount) {
			return false;
		}

		tank.fill(toFill, true);
		heldItem.stackSize--;
		replaceOneHeldItem(player, hand, heldItem, new ItemStack(Items.BUCKET));
		player.playSound(toFill.getFluid().getEmptySound(toFill), 1.0F, 1.0F);
		return true;
	}

	@Nullable
	private static ItemStack createFilledBucket(FluidStack fluid) {
		Fluid fluidType = fluid.getFluid();

		if (fluidType == FluidRegistry.WATER) {
			return new ItemStack(Items.WATER_BUCKET);
		}
		if (fluidType == FluidRegistry.LAVA) {
			return new ItemStack(Items.LAVA_BUCKET);
		}
		if ("milk".equals(fluidType.getName())) {
			return new ItemStack(Items.MILK_BUCKET);
		}
		if (!FluidRegistry.isUniversalBucketEnabled() || !FluidRegistry.getBucketFluids().contains(fluidType)
				|| ForgeModContainer.getInstance().universalBucket == null) {
			return null;
		}

		ItemStack bucket = UniversalBucket.getFilledBucket(ForgeModContainer.getInstance().universalBucket, fluidType);
		NBTTagCompound tag = fluid.writeToNBT(new NBTTagCompound());
		bucket.setTagCompound(tag);
		return bucket;
	}

	@Nullable
	private static FluidStack getUniversalBucketFluid(ItemStack stack) {
		if (!isUniversalBucket(stack)) {
			return null;
		}

		return ForgeModContainer.getInstance().universalBucket.getFluid(stack);
	}

	private static boolean isUniversalBucket(ItemStack stack) {
		return stack != null && ForgeModContainer.getInstance().universalBucket != null
			&& stack.getItem() == ForgeModContainer.getInstance().universalBucket;
	}

	private static void replaceOneHeldItem(EntityPlayer player, EnumHand hand, ItemStack heldItem,
			ItemStack replacement) {
		if (heldItem.stackSize <= 0) {
			player.setHeldItem(hand, replacement);
		} else {
			giveOrDrop(player, replacement);
		}
	}

	private static void giveOrDrop(EntityPlayer player, ItemStack stack) {
		if (stack == null || stack.stackSize <= 0) {
			return;
		}

		if (!player.inventory.addItemStackToInventory(stack)) {
			player.dropItem(stack, false);
		}
	}
}
