package com.mcmoddev.ironagefurniture.api;

import javax.annotation.Nullable;

import net.minecraftforge.fluids.FluidStack;

public final class DrinkDisplayHelper {
	private static final String HIDDEN_BASE_AGE = "New";

	private DrinkDisplayHelper() {
		throw new IllegalAccessError("This class cannot be instantiated");
	}

	public static String getDisplayName(@Nullable FluidStack fluid) {
		if (fluid == null || fluid.getFluid() == null) {
			return "";
		}

		if (PotStillDistillingRegistry.isSpirit(fluid)) {
			String age = getVisibleAgeName(fluid);
			String name = PotStillDistillingRegistry.getSpiritDisplayName(fluid);
			return age.isEmpty() ? name : age + " " + name;
		}

		return FoudreBrewingRegistry.getAgedFluidName(fluid);
	}

	public static String getQualityTooltip(@Nullable FluidStack fluid) {
		String[] lines = getQualityTooltipLines(fluid);

		if (lines.length <= 0) {
			return "";
		}

		if (lines.length == 1) {
			return lines[0];
		}

		return lines[0] + "; " + lines[1];
	}

	public static String[] getQualityTooltipLines(@Nullable FluidStack fluid) {
		if (fluid == null || fluid.getFluid() == null) {
			return new String[0];
		}

		if (PotStillDistillingRegistry.isSpirit(fluid)) {
			String age = getVisibleAgeName(fluid);
			String distillation = "Distillation: " + PotStillDistillingRegistry.getPassName(fluid);

			if (age.isEmpty()) {
				return new String[] { distillation };
			}

			return new String[] { distillation, "Age: " + age };
		}

		String age = getVisibleAgeName(fluid);
		return age.isEmpty() ? new String[0] : new String[] { "Age: " + age };
	}

	public static boolean shouldDrawTinted(@Nullable FluidStack fluid) {
		return FoudreBrewingRegistry.isAgeable(fluid) || PotStillDistillingRegistry.isSpirit(fluid);
	}

	private static String getVisibleAgeName(@Nullable FluidStack fluid) {
		String age = FoudreBrewingRegistry.getAgeLevelName(fluid);
		return HIDDEN_BASE_AGE.equals(age) ? "" : age;
	}
}
