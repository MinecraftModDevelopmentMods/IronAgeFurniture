package com.mcmoddev.ironagefurniture.api;

import java.text.DecimalFormat;
import java.util.List;

import javax.annotation.Nullable;

import com.mcmoddev.ironagefurniture.IronAgeFurnitureConfiguration;
import com.mcmoddev.ironagefurniture.api.drink.DrinkEffectHandler;
import com.mcmoddev.ironagefurniture.api.Items.DrinkContainerHelper;
import com.mcmoddev.ironagefurniture.api.Items.ItemBlockBarrel;
import com.mcmoddev.ironagefurniture.api.Items.ItemDrinkware;
import com.mcmoddev.ironagefurniture.api.Items.ItemFluidBottle;
import com.mcmoddev.ironagefurniture.api.tile.TileEntityBarrel;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fluids.FluidStack;

/** Shared Strength, nourishment, and appraisal rules for every serving vessel. */
public final class DrinkProperties {
	public static final String VALUE_PER_BUCKET_TAG = "ProductionValuePerBucket";
	private static final int VALUE_SCALE = 1000;
	private static final int BUCKET_VOLUME = 1000;
	private static final DecimalFormat SERVING_FORMAT = new DecimalFormat("0.##");
	private static final int[] AGE_VALUE_PERCENT = {100, 125, 175, 250, 400, 700};

	private DrinkProperties() {
		throw new IllegalAccessError("This class cannot be instantiated");
	}

	public static void consume(EntityPlayer player, @Nullable FluidStack fluid, int amount) {
		if (player == null || player.getEntityWorld().isRemote || fluid == null || !FoudreBrewingRegistry.isDrinkable(fluid)) {
			return;
		}

		int nourishment = getNourishment(fluid, amount);
		if (nourishment > 0) {
			player.getFoodStats().addStats(nourishment, Math.min(0.6F, nourishment * 0.1F));
		}

		if (IronAgeFurnitureConfiguration.ENABLE_DRINK_EFFECTS) {
			DrinkEffectHandler.addServings(player, getEffectServings(fluid, amount));
		}
	}

	public static void addTooltip(@Nullable FluidStack fluid, int amount, List<String> tooltip) {
		if (fluid == null || !FoudreBrewingRegistry.isDrinkable(fluid)) {
			return;
		}

		double strength = getStrength(fluid);
		tooltip.add(TextFormatting.GRAY + "Strength: " + getStrengthBand(strength));
		tooltip.add(TextFormatting.GRAY + "Effect: " + SERVING_FORMAT.format(getEffectServings(fluid, amount))
			+ " servings");
		int nourishment = getNourishment(fluid, amount);
		if (nourishment > 0) {
			tooltip.add(TextFormatting.GRAY + "Nourishment: " + nourishment);
		}
		int units = getValueUnits(fluid, amount);
		if (units > 0) {
			tooltip.add(TextFormatting.GOLD + "Innkeeper value: " + formatValue(units));
		}
	}

	public static double getStrength(@Nullable FluidStack fluid) {
		if (fluid == null || fluid.getFluid() == null) {
			return 0.0D;
		}

		FluidStack infusionBase = FoudreBrewingRegistry.getInfusionBase(fluid);
		if (infusionBase != null) {
			String name = getName(fluid);
			return getStrength(infusionBase) * (isCreamProduct(name) ? 0.4D : 0.65D);
		}
		if (PotStillDistillingRegistry.isSpirit(fluid)) {
			switch (PotStillDistillingRegistry.getDistillationPasses(fluid)) {
			case 3:
				return 16.0D;
			case 2:
				return 12.0D;
			default:
				return 8.0D;
			}
		}

		String name = getName(fluid);
		if (containsAny(name, "mead", "melomel", "cyser", "pyment", "braggot", "metheglin")) {
			return 2.8D;
		}
		if (name.contains("wine")) {
			return 2.4D;
		}
		if (containsAny(name, "cider", "perry", "tepache")) {
			return 1.2D;
		}
		return 1.0D;
	}

	public static double getEffectServings(@Nullable FluidStack fluid, int amount) {
		return Math.max(0, amount) * getStrength(fluid) / 250.0D;
	}

	public static String getStrengthBand(double strength) {
		if (strength <= 1.1D) return "Mild";
		if (strength <= 2.0D) return "Full";
		if (strength <= 4.0D) return "Robust";
		if (strength <= 8.0D) return "Strong";
		if (strength <= 12.0D) return "Very Strong";
		return "Exceptional";
	}

	public static int getNourishment(@Nullable FluidStack fluid, int amount) {
		if (fluid == null || fluid.getFluid() == null || amount <= 0) {
			return 0;
		}
		String name = getName(fluid);
		int perBottle = 0;
		if (containsAny(name, "eggnog", "cream_liqueur")) {
			perBottle = 4;
		} else if (containsAny(name, "ale", "beer", "stout", "kvass", "tepache")) {
			perBottle = 3;
		} else if (containsAny(name, "mead", "melomel", "cyser", "pyment", "braggot", "metheglin")) {
			perBottle = 2;
		}
		return perBottle == 0 ? 0 : perBottle * amount / 250;
	}

	public static int getValueUnits(@Nullable FluidStack fluid, int amount) {
		if (fluid == null || fluid.getFluid() == null || amount <= 0) {
			return 0;
		}
		long units = (long)getValuePerBucket(fluid) * (long)amount
			* IronAgeFurnitureConfiguration.DRINK_VALUE_PERCENT / 100L / BUCKET_VOLUME;
		return (int)Math.min(Integer.MAX_VALUE, Math.max(0L, units));
	}

	public static int getValuePerBucket(@Nullable FluidStack fluid) {
		if (fluid == null || fluid.getFluid() == null) {
			return 0;
		}
		String name = getName(fluid);
		int base;
		if (fluid.tag != null && fluid.tag.hasKey(VALUE_PER_BUCKET_TAG, 3)) {
			base = Math.max(0, fluid.tag.getInteger(VALUE_PER_BUCKET_TAG));
		} else if (PotStillDistillingRegistry.isSpirit(fluid)) {
			base = 700;
			int passes = PotStillDistillingRegistry.getDistillationPasses(fluid);
			if (passes == 2) base = base * 44 / 10;
			if (passes >= 3) base = base * 88 / 10;
		} else if (FoudreBrewingRegistry.getInfusionBase(fluid) != null) {
			base = Math.max(350, getValuePerBucket(FoudreBrewingRegistry.getInfusionBase(fluid)) + 240);
			if (isCreamProduct(name)) base += 160;
		} else if (containsAny(name, "mead", "melomel", "cyser", "pyment", "braggot", "metheglin")) {
			base = 170;
		} else if (name.contains("wine")) {
			base = 150;
		} else if (containsAny(name, "cider", "perry", "tepache")) {
			base = 100;
		} else {
			base = 80;
		}

		int ageLevel = Math.min(AGE_VALUE_PERCENT.length - 1, FoudreBrewingRegistry.getAgeLevel(fluid));
		base = base * AGE_VALUE_PERCENT[ageLevel] / 100;
		return base;
	}

	public static void setValuePerBucket(FluidStack fluid, int value) {
		if (fluid == null || fluid.getFluid() == null) return;
		if (fluid.tag == null) fluid.tag = new NBTTagCompound();
		fluid.tag.setInteger(VALUE_PER_BUCKET_TAG, Math.max(0, value));
	}

	public static void preserveInputValue(FluidStack output, FluidStack input, int outputAmount, int premiumPercent) {
		if (output == null || input == null || outputAmount <= 0) return;
		long inputTotal = (long)getValuePerBucket(input) * (long)input.amount;
		long outputPerBucket = inputTotal * Math.max(100, premiumPercent) / 100L / outputAmount;
		setValuePerBucket(output, (int)Math.min(Integer.MAX_VALUE, outputPerBucket));
	}

	public static String formatValue(int units) {
		if (units >= VALUE_SCALE && units % VALUE_SCALE == 0) {
			return (units / VALUE_SCALE) + " emerald" + (units == VALUE_SCALE ? "" : "s");
		}
		return SERVING_FORMAT.format(units / (double)VALUE_SCALE) + " emeralds";
	}

	@Nullable
	public static FluidStack getContainedFluid(ItemStack stack) {
		if (stack == null || stack.stackSize <= 0) return null;
		FluidStack fluid = DrinkContainerHelper.getFluid(stack);
		return fluid != null ? fluid : TileEntityBarrel.getFluidFromItemStack(stack);
	}

	public static boolean isAppraisable(ItemStack stack) {
		FluidStack fluid = getContainedFluid(stack);
		return fluid != null && FoudreBrewingRegistry.isDrinkable(fluid)
			&& (stack.getItem() instanceof ItemFluidBottle || stack.getItem() instanceof ItemDrinkware
				|| stack.getItem() instanceof ItemBlockBarrel);
	}

	public static int getStackValueUnits(ItemStack stack) {
		FluidStack fluid = getContainedFluid(stack);
		if (fluid == null) return 0;
		long value = (long)getValueUnits(fluid, fluid.amount) * Math.max(1, stack.stackSize);
		return (int)Math.min(Integer.MAX_VALUE, value);
	}

	@Nullable
	public static ItemStack createEmptyReturn(ItemStack filled) {
		if (filled == null || filled.stackSize <= 0) return null;
		ItemStack empty;
		if (filled.getItem() instanceof ItemFluidBottle) {
			empty = new ItemStack(Items.GLASS_BOTTLE, filled.stackSize);
		} else if (filled.getItem() instanceof ItemDrinkware) {
			empty = new ItemStack(filled.getItem(), filled.stackSize, filled.getMetadata());
		} else if (filled.getItem() instanceof ItemBlockBarrel) {
			empty = filled.copy();
			NBTTagCompound tag = empty.getTagCompound();
			if (tag != null) {
				tag.removeTag(TileEntityBarrel.TANK_TAG);
				tag.removeTag("Sealed");
				if (tag.hasNoTags()) empty.setTagCompound(null);
			}
		} else {
			return null;
		}
		return empty;
	}

	private static String getName(FluidStack fluid) {
		return fluid.getFluid().getName().toLowerCase(java.util.Locale.ROOT);
	}

	private static boolean isCreamProduct(String name) {
		return name.contains("cream") || name.contains("eggnog");
	}

	private static boolean containsAny(String value, String... needles) {
		for (String needle : needles) if (value.contains(needle)) return true;
		return false;
	}
}
