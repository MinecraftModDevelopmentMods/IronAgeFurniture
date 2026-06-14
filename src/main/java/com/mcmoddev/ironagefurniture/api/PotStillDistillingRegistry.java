package com.mcmoddev.ironagefurniture.api;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public final class PotStillDistillingRegistry {
	public static final String DISTILLATION_PASSES_TAG = "DistillationPasses";
	public static final int INPUT_CAPACITY = 128 * Fluid.BUCKET_VOLUME;
	public static final int OUTPUT_CAPACITY = 24 * Fluid.BUCKET_VOLUME;
	public static final int FIRST_PASS_MIN_INPUT = 6000;
	public static final int REDISTILL_MIN_INPUT = 2000;
	public static final int MAX_DISTILLATION_PASSES = 3;

	private static final ResourceLocation SPIRIT_TEXTURE = new ResourceLocation("minecraft", "blocks/quartz_block_side");
	private static final ResourceLocation STILL = SPIRIT_TEXTURE;
	private static final ResourceLocation FLOWING = SPIRIT_TEXTURE;
	private static final int DAY = 20 * 60 * 20;
	private static final int MIN_DISTILL_TIME = 6000;
	private static final int FIRST_PASS_RATIO = 6;
	private static final int REDISTILL_RATIO = 2;
	private static final int COLOR_ALPHA_MASK = 0xFF000000;
	private static final int COLOR_RED_SHIFT = 16;
	private static final int COLOR_GREEN_SHIFT = 8;
	private static final int COLOR_CHANNEL_MASK = 255;
	private static final int WHISKY_COLOR = 0xFFD58A28;
	private static final int BRANDY_COLOR = 0xFFB96A2D;
	private static final int APPLE_BRANDY_COLOR = 0xFFE2A53E;
	private static final int PEAR_BRANDY_COLOR = 0xFFE8C86A;
	private static final int HONEY_SPIRIT_COLOR = 0xFFE4B83D;
	private static final int RICE_SPIRIT_COLOR = 0xFFEDE4D2;
	private static final int PEACH_BRANDY_COLOR = 0xFFE69A5D;
	private static final int PERSIMMON_BRANDY_COLOR = 0xFFD8782A;
	private static final int BERRY_BRANDY_COLOR = 0xFF5B3C86;
	private static final int WILD_BERRY_BRANDY_COLOR = 0xFF65306F;
	private static final int RASPBERRY_BRANDY_COLOR = 0xFFA52850;
	private static final int BLUEBERRY_BRANDY_COLOR = 0xFF35509A;
	private static final int BLACKBERRY_BRANDY_COLOR = 0xFF2F1C43;
	private static final int STRAWBERRY_BRANDY_COLOR = 0xFFD84247;
	private static final int CRANBERRY_BRANDY_COLOR = 0xFFAA2438;
	private static final int PASS_DARKEN_STEP = 22;

	public static Fluid whisky;
	public static Fluid brandy;
	public static Fluid appleBrandy;
	public static Fluid pearBrandy;
	public static Fluid honeySpirit;
	public static Fluid riceSpirit;
	public static Fluid peachBrandy;
	public static Fluid persimmonBrandy;
	public static Fluid berryBrandy;
	public static Fluid wildBerryBrandy;
	public static Fluid raspberryBrandy;
	public static Fluid blueberryBrandy;
	public static Fluid blackberryBrandy;
	public static Fluid strawberryBrandy;
	public static Fluid cranberryBrandy;

	private static final Map<Fluid, DistillationRecipe> FIRST_PASS_RECIPES = new HashMap<Fluid, DistillationRecipe>();
	private static final Map<Fluid, SpiritProfile> SPIRIT_PROFILES = new HashMap<Fluid, SpiritProfile>();

	private PotStillDistillingRegistry() {
		throw new IllegalAccessError("This class cannot be instantiated");
	}

	public static void registerFluids() {
		whisky = registerSpirit("ironagefurniture_whisky", "whisky", "Whisky", WHISKY_COLOR);
		brandy = registerSpirit("ironagefurniture_brandy", "brandy", "Brandy", BRANDY_COLOR);
		appleBrandy = registerSpirit("ironagefurniture_apple_brandy", "apple_brandy", "Apple Brandy",
			APPLE_BRANDY_COLOR);
		pearBrandy = registerSpirit("ironagefurniture_pear_brandy", "pear_brandy", "Pear Brandy",
			PEAR_BRANDY_COLOR);
		honeySpirit = registerSpirit("ironagefurniture_honey_spirit", "honey_spirit", "Honey Spirit",
			HONEY_SPIRIT_COLOR);
		riceSpirit = registerSpirit("ironagefurniture_rice_spirit", "rice_spirit", "Rice Spirit",
			RICE_SPIRIT_COLOR);
		peachBrandy = registerSpirit("ironagefurniture_peach_brandy", "peach_brandy", "Peach Brandy",
			PEACH_BRANDY_COLOR);
		persimmonBrandy = registerSpirit("ironagefurniture_persimmon_brandy", "persimmon_brandy",
			"Persimmon Brandy", PERSIMMON_BRANDY_COLOR);
		berryBrandy = registerSpirit("ironagefurniture_berry_brandy", "berry_brandy", "Berry Brandy",
			BERRY_BRANDY_COLOR);
		wildBerryBrandy = registerSpirit("ironagefurniture_wild_berry_brandy", "wild_berry_brandy",
			"Wild Berry Brandy", WILD_BERRY_BRANDY_COLOR);
		raspberryBrandy = registerSpirit("ironagefurniture_raspberry_brandy", "raspberry_brandy",
			"Raspberry Brandy", RASPBERRY_BRANDY_COLOR);
		blueberryBrandy = registerSpirit("ironagefurniture_blueberry_brandy", "blueberry_brandy",
			"Blueberry Brandy", BLUEBERRY_BRANDY_COLOR);
		blackberryBrandy = registerSpirit("ironagefurniture_blackberry_brandy", "blackberry_brandy",
			"Blackberry Brandy", BLACKBERRY_BRANDY_COLOR);
		strawberryBrandy = registerSpirit("ironagefurniture_strawberry_brandy", "strawberry_brandy",
			"Strawberry Brandy", STRAWBERRY_BRANDY_COLOR);
		cranberryBrandy = registerSpirit("ironagefurniture_cranberry_brandy", "cranberry_brandy",
			"Cranberry Brandy", CRANBERRY_BRANDY_COLOR);
		FoudreBrewingRegistry.registerAgeProfiles();
	}

	public static void initRecipes() {
		registerFluids();
		FIRST_PASS_RECIPES.clear();
		addFirstPass(FoudreBrewingRegistry.ale, whisky, "Whisky");
		addFirstPass(FoudreBrewingRegistry.wine, brandy, "Brandy");
		addFirstPass(FoudreBrewingRegistry.grapeWine, brandy, "Brandy");
		addFirstPass(FoudreBrewingRegistry.cider, appleBrandy, "Apple Brandy");
		addFirstPass(FoudreBrewingRegistry.appleCider, appleBrandy, "Apple Brandy");
		addFirstPass(FoudreBrewingRegistry.perry, pearBrandy, "Pear Brandy");
		addFirstPass(FoudreBrewingRegistry.mead, honeySpirit, "Honey Spirit");
		addFirstPass(FoudreBrewingRegistry.riceWine, riceSpirit, "Rice Spirit");
		addFirstPass(FoudreBrewingRegistry.berryWine, berryBrandy, "Berry Brandy");
		addFirstPass(FoudreBrewingRegistry.peachWine, peachBrandy, "Peach Brandy");
		addFirstPass(FoudreBrewingRegistry.persimmonWine, persimmonBrandy, "Persimmon Brandy");
		addFirstPass(FoudreBrewingRegistry.wildBerryWine, wildBerryBrandy, "Wild Berry Brandy");
		addFirstPass(FoudreBrewingRegistry.raspberryWine, raspberryBrandy, "Raspberry Brandy");
		addFirstPass(FoudreBrewingRegistry.blueberryWine, blueberryBrandy, "Blueberry Brandy");
		addFirstPass(FoudreBrewingRegistry.blackberryWine, blackberryBrandy, "Blackberry Brandy");
		addFirstPass(FoudreBrewingRegistry.strawberryWine, strawberryBrandy, "Strawberry Brandy");
		addFirstPass(FoudreBrewingRegistry.cranberryWine, cranberryBrandy, "Cranberry Brandy");
	}

	@Nullable
	public static DistillationResult findResult(@Nullable FluidStack input, int outputSpace) {
		if (input == null || input.getFluid() == null || input.amount <= 0) {
			return null;
		}

		if (isSpirit(input)) {
			return createRedistillationResult(input, outputSpace);
		}

		DistillationRecipe recipe = FIRST_PASS_RECIPES.get(input.getFluid());

		if (recipe == null || input.amount < FIRST_PASS_MIN_INPUT) {
			return null;
		}

		int outputAmount = input.amount / FIRST_PASS_RATIO;

		if (outputAmount <= 0 || outputAmount > outputSpace) {
			return null;
		}

		return new DistillationResult(recipe.output, recipe.outputName, outputAmount,
			getDistillationTime(input.amount), 1);
	}

	public static boolean canAcceptInput(@Nullable FluidStack input) {
		if (input == null || input.getFluid() == null || input.amount <= 0) {
			return false;
		}

		return FIRST_PASS_RECIPES.containsKey(input.getFluid()) || isRedistillableSpirit(input);
	}

	public static boolean canMixInputs(@Nullable FluidStack existing, @Nullable FluidStack incoming) {
		if (existing == null || existing.amount <= 0) {
			return canAcceptInput(incoming);
		}
		if (incoming == null || incoming.getFluid() == null || existing.getFluid() != incoming.getFluid()) {
			return false;
		}
		if (isSpirit(existing) || isSpirit(incoming)) {
			return getDistillationPasses(existing) == getDistillationPasses(incoming)
				&& getDistillationPasses(incoming) < MAX_DISTILLATION_PASSES;
		}
		return canAcceptInput(incoming);
	}

	public static boolean isSpirit(@Nullable FluidStack fluid) {
		return fluid != null && SPIRIT_PROFILES.containsKey(fluid.getFluid());
	}

	public static boolean isSpiritFluid(@Nullable Fluid fluid) {
		return fluid != null && SPIRIT_PROFILES.containsKey(fluid);
	}

	public static int getDistillationPasses(@Nullable FluidStack fluid) {
		if (!isSpirit(fluid)) {
			return 0;
		}

		if (fluid.tag == null || !fluid.tag.hasKey(DISTILLATION_PASSES_TAG, 3)) {
			return 1;
		}

		return Math.max(1, Math.min(MAX_DISTILLATION_PASSES,
			fluid.tag.getInteger(DISTILLATION_PASSES_TAG)));
	}

	public static String getPassName(@Nullable FluidStack fluid) {
		switch (getDistillationPasses(fluid)) {
		case 2:
			return "Double";
		case 3:
			return "Triple";
		default:
			return "Single";
		}
	}

	public static String getSpiritDisplayName(@Nullable FluidStack fluid) {
		if (fluid == null || fluid.getFluid() == null) {
			return "";
		}

		SpiritProfile profile = SPIRIT_PROFILES.get(fluid.getFluid());
		String name = profile == null ? fluid.getLocalizedName() : profile.displayName;
		int passes = getDistillationPasses(fluid);

		if (passes == 2) {
			return "Double Distilled " + name;
		}
		if (passes == 3) {
			return "Triple Distilled " + name;
		}

		return name;
	}

	public static FluidStack createSpiritFluid(Fluid spirit, int amount, int passes) {
		FluidStack output = new FluidStack(spirit, amount);
		setDistillationPasses(output, passes);
		return output;
	}

	private static DistillationResult createRedistillationResult(FluidStack input, int outputSpace) {
		int passes = getDistillationPasses(input);

		if (passes >= MAX_DISTILLATION_PASSES || input.amount < REDISTILL_MIN_INPUT) {
			return null;
		}

		int outputAmount = input.amount / REDISTILL_RATIO;

		if (outputAmount <= 0 || outputAmount > outputSpace) {
			return null;
		}

		SpiritProfile profile = SPIRIT_PROFILES.get(input.getFluid());
		String outputName = profile == null ? input.getLocalizedName() : profile.displayName;
		return new DistillationResult(input.getFluid(), outputName, outputAmount,
			getDistillationTime(input.amount), passes + 1);
	}

	private static int getDistillationTime(int inputAmount) {
		return Math.max(MIN_DISTILL_TIME, inputAmount * DAY / INPUT_CAPACITY);
	}

	private static void setDistillationPasses(FluidStack fluid, int passes) {
		if (fluid.tag == null) {
			fluid.tag = new NBTTagCompound();
		}

		fluid.tag.setInteger(DISTILLATION_PASSES_TAG, Math.max(1, Math.min(MAX_DISTILLATION_PASSES, passes)));
	}

	private static boolean isRedistillableSpirit(FluidStack input) {
		return isSpirit(input) && getDistillationPasses(input) < MAX_DISTILLATION_PASSES;
	}

	private static Fluid registerSpirit(String registryName, String unlocalizedName, String displayName, int color) {
		Fluid existing = FluidRegistry.getFluid(registryName);
		Fluid fluid = existing != null ? existing
			: new TintedSpiritFluid(registryName, STILL, FLOWING, color).setUnlocalizedName(unlocalizedName)
				.setDensity(900).setViscosity(800);

		if (existing == null) {
			FluidRegistry.registerFluid(fluid);
			fluid = FluidRegistry.getFluid(registryName);
		}

		FluidRegistry.addBucketForFluid(fluid);
		SPIRIT_PROFILES.put(fluid, new SpiritProfile(displayName));
		return fluid;
	}

	private static void addFirstPass(Fluid input, Fluid output, String outputName) {
		if (input != null && output != null) {
			FIRST_PASS_RECIPES.put(input, new DistillationRecipe(output, outputName));
		}
	}

	private static int darkenForPass(int color, int passes) {
		if (passes <= 1) {
			return color;
		}

		int alpha = (color & COLOR_ALPHA_MASK) == 0 ? COLOR_ALPHA_MASK : color & COLOR_ALPHA_MASK;
		int red = Math.max(0, (color >> COLOR_RED_SHIFT & COLOR_CHANNEL_MASK) - PASS_DARKEN_STEP * (passes - 1));
		int green = Math.max(0, (color >> COLOR_GREEN_SHIFT & COLOR_CHANNEL_MASK) - PASS_DARKEN_STEP * (passes - 1));
		int blue = Math.max(0, (color & COLOR_CHANNEL_MASK) - PASS_DARKEN_STEP * (passes - 1));
		return alpha | red << COLOR_RED_SHIFT | green << COLOR_GREEN_SHIFT | blue;
	}

	public static final class DistillationResult {
		private final Fluid output;
		private final String outputName;
		private final int outputAmount;
		private final int distillationTime;
		private final int outputPasses;

		private DistillationResult(Fluid output, String outputName, int outputAmount, int distillationTime,
				int outputPasses) {
			this.output = output;
			this.outputName = outputName;
			this.outputAmount = outputAmount;
			this.distillationTime = distillationTime;
			this.outputPasses = outputPasses;
		}

		public Fluid getOutput() {
			return this.output;
		}

		public String getOutputName() {
			return this.outputName;
		}

		public int getOutputAmount() {
			return this.outputAmount;
		}

		public int getDistillationTime() {
			return this.distillationTime;
		}

		public int getOutputPasses() {
			return this.outputPasses;
		}

		public FluidStack createOutputStack() {
			return createSpiritFluid(this.output, this.outputAmount, this.outputPasses);
		}
	}

	private static final class DistillationRecipe {
		private final Fluid output;
		private final String outputName;

		private DistillationRecipe(Fluid output, String outputName) {
			this.output = output;
			this.outputName = outputName;
		}
	}

	private static final class SpiritProfile {
		private final String displayName;

		private SpiritProfile(String displayName) {
			this.displayName = displayName;
		}
	}

	private static final class TintedSpiritFluid extends Fluid {
		private final int color;

		private TintedSpiritFluid(String fluidName, ResourceLocation still, ResourceLocation flowing, int color) {
			super(fluidName, still, flowing);
			this.color = color;
		}

		@Override
		public int getColor(FluidStack stack) {
			return FoudreBrewingRegistry.getAgedFluidColor(stack,
				darkenForPass(this.color, getDistillationPasses(stack)));
		}

		@Override
		public int getColor() {
			return this.color;
		}
	}
}
