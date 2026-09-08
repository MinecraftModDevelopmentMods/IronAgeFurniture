package zone.moddev.mc.ironagefurniture.api;

import java.util.ArrayList;
import java.util.List;

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
		StringBuilder result = new StringBuilder();
		for (String line : lines) {
			if (line == null || line.isEmpty()) {
				continue;
			}
			if (result.length() > 0) {
				result.append("; ");
			}
			result.append(line);
		}
		return result.toString();
	}

	public static String[] getQualityTooltipLines(@Nullable FluidStack fluid) {
		if (fluid == null || fluid.getFluid() == null) {
			return new String[0];
		}

		List<String> lines = new ArrayList<String>();
		if (PotStillDistillingRegistry.isSpirit(fluid)) {
			String age = getVisibleAgeName(fluid);
			lines.add("Distillation: " + PotStillDistillingRegistry.getPassName(fluid));
			if (!age.isEmpty()) {
				lines.add("Age: " + age);
			}
		} else {
			String age = getVisibleAgeName(fluid);
			if (!age.isEmpty()) {
				lines.add("Age: " + age);
			}
		}

		FluidStack infusionBase = FoudreBrewingRegistry.getInfusionBase(fluid);
		if (infusionBase != null && infusionBase.getFluid() != null) {
			lines.add("Made with " + getDisplayName(infusionBase));
		}
		return lines.toArray(new String[lines.size()]);
	}

	public static boolean shouldDrawTinted(@Nullable FluidStack fluid) {
		return FoudreBrewingRegistry.isAgeable(fluid) || PotStillDistillingRegistry.isSpirit(fluid)
			|| FoudreBrewingRegistry.getInfusionBase(fluid) != null;
	}

	private static String getVisibleAgeName(@Nullable FluidStack fluid) {
		String age = FoudreBrewingRegistry.getAgeLevelName(fluid);
		return HIDDEN_BASE_AGE.equals(age) ? "" : age;
	}
}
