package com.mcmoddev.ironagefurniture.api;

import javax.annotation.Nullable;

import net.minecraftforge.fluids.FluidStack;

public final class DrinkDisplayHelper {
	private DrinkDisplayHelper() {
		throw new IllegalAccessError("This class cannot be instantiated");
	}

	public static String getDisplayName(@Nullable FluidStack fluid) {
		if (fluid == null || fluid.getFluid() == null) {
			return "";
		}

		if (PotStillDistillingRegistry.isSpirit(fluid)) {
			String age = FoudreBrewingRegistry.getAgeLevelName(fluid);
			String name = PotStillDistillingRegistry.getSpiritDisplayName(fluid);
			return age.isEmpty() ? name : age + " " + name;
		}

		return FoudreBrewingRegistry.getAgedFluidName(fluid);
	}

	public static String getQualityTooltip(@Nullable FluidStack fluid) {
		if (fluid == null || fluid.getFluid() == null) {
			return "";
		}

		if (PotStillDistillingRegistry.isSpirit(fluid)) {
			String age = FoudreBrewingRegistry.getAgeLevelName(fluid);
			String distillation = "Distillation: " + PotStillDistillingRegistry.getPassName(fluid);
			return age.isEmpty() ? distillation : distillation + "; Age: " + age;
		}

		String age = FoudreBrewingRegistry.getAgeLevelName(fluid);
		return age.isEmpty() ? "" : "Age: " + age;
	}

	public static boolean shouldDrawTinted(@Nullable FluidStack fluid) {
		return FoudreBrewingRegistry.isAgeable(fluid) || PotStillDistillingRegistry.isSpirit(fluid);
	}
}
