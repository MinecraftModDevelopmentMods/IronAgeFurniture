package com.mcmoddev.ironagefurniture.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mcmoddev.ironagefurniture.IronAgeFurnitureConfiguration;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.oredict.OreDictionary;

public final class FoudreBrewingRegistry {
	private static final ResourceLocation STILL = new ResourceLocation("minecraft", "blocks/water_still");
	private static final ResourceLocation FLOWING = new ResourceLocation("minecraft", "blocks/water_flow");
	private static final String AGE_TICKS_TAG = "FoudreAgeTicks";
	private static final int DAY = 20 * 60 * 20;
	private static final int ALE_BREW_TIME = DAY;
	private static final int CIDER_BREW_TIME = DAY + DAY / 2;
	private static final int WINE_BREW_TIME = DAY * 2;
	private static final int MEAD_BREW_TIME = DAY * 2;
	private static final int STANDARD_INGREDIENT_COUNT = 64;
	private static final int HONEY_INGREDIENT_COUNT = 32;
	private static final int COLOR_ALPHA_MASK = 0xFF000000;
	private static final int COLOR_RED_SHIFT = 16;
	private static final int COLOR_GREEN_SHIFT = 8;
	private static final int COLOR_CHANNEL_MASK = 255;
	private static final int COLOR_CHANNEL_MAX = 255;
	private static final float MAX_AGE_DARKENING = 0.36F;
	private static final int ALE_COLOR_FRESH = 0xFFE6B85C;
	private static final int CIDER_COLOR_FRESH = 0xFFD8A83A;
	private static final int WINE_COLOR_FRESH = 0xFF6E173A;
	private static final int MEAD_COLOR_FRESH = 0xFFD6B34E;
	private static final int BERRY_WINE_COLOR_FRESH = 0xFF3E2B77;
	private static final int RICE_WINE_COLOR_FRESH = 0xFFE4DBC0;
	private static final int APPLE_CIDER_COLOR_FRESH = 0xFFD58B2A;
	private static final int PERRY_COLOR_FRESH = 0xFFE0C66A;
	private static final int PEACH_WINE_COLOR_FRESH = 0xFFE28C55;
	private static final int PERSIMMON_WINE_COLOR_FRESH = 0xFFCF6B22;
	private static final int WILD_BERRY_WINE_COLOR_FRESH = 0xFF4B245C;
	private static final int RASPBERRY_WINE_COLOR_FRESH = 0xFF8E1E45;
	private static final int BLUEBERRY_WINE_COLOR_FRESH = 0xFF263A7A;
	private static final int BLACKBERRY_WINE_COLOR_FRESH = 0xFF241633;
	private static final int STRAWBERRY_WINE_COLOR_FRESH = 0xFFC7383E;
	private static final int CRANBERRY_WINE_COLOR_FRESH = 0xFF981B2F;

	public static Fluid ale;
	public static Fluid cider;
	public static Fluid wine;
	public static Fluid mead;
	public static Fluid berryWine;
	public static Fluid riceWine;
	public static Fluid appleCider;
	public static Fluid perry;
	public static Fluid grapeWine;
	public static Fluid peachWine;
	public static Fluid persimmonWine;
	public static Fluid wildBerryWine;
	public static Fluid raspberryWine;
	public static Fluid blueberryWine;
	public static Fluid blackberryWine;
	public static Fluid strawberryWine;
	public static Fluid cranberryWine;

	private static final List<FoudreBrewingRecipe> RECIPES = new ArrayList<FoudreBrewingRecipe>();
	private static final Map<Fluid, AgeProfile> AGE_PROFILES = new HashMap<Fluid, AgeProfile>();
	private static final AgeProfile ALE_PROFILE = new AgeProfile(new String[] { "Fresh", "Conditioned", "Mature" },
		new int[] { 0, DAY, DAY * 3 });
	private static final AgeProfile CIDER_PROFILE = new AgeProfile(
		new String[] { "Fresh", "Settled", "Mature", "Aged" }, new int[] { 0, DAY, DAY * 3, DAY * 7 });
	private static final AgeProfile MEAD_PROFILE = new AgeProfile(
		new String[] { "Fresh", "Young", "Mature", "Aged", "Reserve" },
		new int[] { 0, DAY * 2, DAY * 5, DAY * 10, DAY * 18 });
	private static final AgeProfile WINE_PROFILE = new AgeProfile(
		new String[] { "New", "Young", "Mature", "Aged", "Reserve", "Vintage" },
		new int[] { 0, DAY, DAY * 3, DAY * 7, DAY * 14, DAY * 28 });

	private FoudreBrewingRegistry() {
		throw new IllegalAccessError("This class cannot be instantiated");
	}

	public static void registerFluids() {
		ale = registerFluid("ironagefurniture_ale", "ale", ALE_COLOR_FRESH);
		cider = registerFluid("ironagefurniture_cider", "cider", CIDER_COLOR_FRESH);
		wine = registerFluid("ironagefurniture_wine", "wine", WINE_COLOR_FRESH);
		mead = registerFluid("ironagefurniture_mead", "mead", MEAD_COLOR_FRESH);
		berryWine = registerFluid("ironagefurniture_berry_wine", "berry_wine", BERRY_WINE_COLOR_FRESH);
		riceWine = registerFluid("ironagefurniture_rice_wine", "rice_wine", RICE_WINE_COLOR_FRESH);
		appleCider = registerFluid("ironagefurniture_apple_cider", "apple_cider", APPLE_CIDER_COLOR_FRESH);
		perry = registerFluid("ironagefurniture_perry", "perry", PERRY_COLOR_FRESH);
		grapeWine = registerFluid("ironagefurniture_grape_wine", "grape_wine", WINE_COLOR_FRESH);
		peachWine = registerFluid("ironagefurniture_peach_wine", "peach_wine", PEACH_WINE_COLOR_FRESH);
		persimmonWine = registerFluid("ironagefurniture_persimmon_wine", "persimmon_wine",
			PERSIMMON_WINE_COLOR_FRESH);
		wildBerryWine = registerFluid("ironagefurniture_wild_berry_wine", "wild_berry_wine",
			WILD_BERRY_WINE_COLOR_FRESH);
		raspberryWine = registerFluid("ironagefurniture_raspberry_wine", "raspberry_wine",
			RASPBERRY_WINE_COLOR_FRESH);
		blueberryWine = registerFluid("ironagefurniture_blueberry_wine", "blueberry_wine",
			BLUEBERRY_WINE_COLOR_FRESH);
		blackberryWine = registerFluid("ironagefurniture_blackberry_wine", "blackberry_wine",
			BLACKBERRY_WINE_COLOR_FRESH);
		strawberryWine = registerFluid("ironagefurniture_strawberry_wine", "strawberry_wine",
			STRAWBERRY_WINE_COLOR_FRESH);
		cranberryWine = registerFluid("ironagefurniture_cranberry_wine", "cranberry_wine",
			CRANBERRY_WINE_COLOR_FRESH);
		registerAgeProfiles();
	}

	public static void initRecipes() {
		RECIPES.clear();
		registerFluids();

		addRecipe("ale_wheat", "Ale", ale, new ItemStack(Items.WHEAT), STANDARD_INGREDIENT_COUNT,
			ALE_BREW_TIME);
		addRecipe("cider_apple", "Apple Cider", appleCider, new ItemStack(Items.APPLE),
			STANDARD_INGREDIENT_COUNT, CIDER_BREW_TIME);

		if (IronAgeFurnitureConfiguration.INTEGRATION_HARVESTCRAFT && Loader.isModLoaded("harvestcraft")) {
			addOptionalRecipe("wine_harvestcraft_grape", "Grape Wine", grapeWine, "harvestcraft:grapeitem", 0,
				STANDARD_INGREDIENT_COUNT, WINE_BREW_TIME);
			addOptionalRecipe("ale_harvestcraft_barley", "Ale", ale, "harvestcraft:barleyitem", 0,
				STANDARD_INGREDIENT_COUNT, ALE_BREW_TIME);
			addOptionalRecipe("mead_harvestcraft_honey", "Mead", mead, "harvestcraft:honeyitem", 0,
				HONEY_INGREDIENT_COUNT,
				MEAD_BREW_TIME);
			addOptionalRecipe("rice_wine_harvestcraft_rice", "Rice Wine", riceWine, "harvestcraft:riceitem", 0,
				STANDARD_INGREDIENT_COUNT, WINE_BREW_TIME);
			addOptionalRecipe("berry_wine_harvestcraft_blackberry", "Blackberry Wine", blackberryWine,
				"harvestcraft:blackberryitem", 0, STANDARD_INGREDIENT_COUNT, WINE_BREW_TIME);
			addOptionalRecipe("berry_wine_harvestcraft_blueberry", "Blueberry Wine", blueberryWine,
				"harvestcraft:blueberryitem", 0, STANDARD_INGREDIENT_COUNT, WINE_BREW_TIME);
			addOptionalRecipe("berry_wine_harvestcraft_raspberry", "Raspberry Wine", raspberryWine,
				"harvestcraft:raspberryitem", 0, STANDARD_INGREDIENT_COUNT, WINE_BREW_TIME);
			addOptionalRecipe("berry_wine_harvestcraft_strawberry", "Strawberry Wine", strawberryWine,
				"harvestcraft:strawberryitem", 0, STANDARD_INGREDIENT_COUNT, WINE_BREW_TIME);
			addOptionalRecipe("berry_wine_harvestcraft_cranberry", "Cranberry Wine", cranberryWine,
				"harvestcraft:cranberryitem", 0, STANDARD_INGREDIENT_COUNT, WINE_BREW_TIME);
		}

		if (IronAgeFurnitureConfiguration.INTEGRATION_BIOMESOPLENTY && Loader.isModLoaded("biomesoplenty")) {
			addOptionalRecipe("berry_wine_bop_berries", "Wild Berry Wine", wildBerryWine, "biomesoplenty:berries", 0,
				STANDARD_INGREDIENT_COUNT, WINE_BREW_TIME);
			addOptionalRecipe("cider_bop_pear", "Perry", perry, "biomesoplenty:pear", 0,
				STANDARD_INGREDIENT_COUNT, CIDER_BREW_TIME);
			addOptionalRecipe("wine_bop_peach", "Peach Wine", peachWine, "biomesoplenty:peach", 0,
				STANDARD_INGREDIENT_COUNT, WINE_BREW_TIME);
			addOptionalRecipe("wine_bop_persimmon", "Persimmon Wine", persimmonWine, "biomesoplenty:persimmon", 0,
				STANDARD_INGREDIENT_COUNT, WINE_BREW_TIME);
			addOptionalRecipe("mead_bop_honeycomb", "Mead", mead, "biomesoplenty:filled_honeycomb", 0,
				HONEY_INGREDIENT_COUNT, MEAD_BREW_TIME);
		}

		if (IronAgeFurnitureConfiguration.INTEGRATION_NATURA && Loader.isModLoaded("natura")) {
			addOptionalRecipe("ale_natura_barley", "Ale", ale, "natura:materials", 0, STANDARD_INGREDIENT_COUNT,
				ALE_BREW_TIME);
			addOptionalRecipe("berry_wine_natura_raspberry", "Raspberry Wine", raspberryWine, "natura:edibles", 2,
				STANDARD_INGREDIENT_COUNT, WINE_BREW_TIME);
			addOptionalRecipe("berry_wine_natura_blueberry", "Blueberry Wine", blueberryWine, "natura:edibles", 3,
				STANDARD_INGREDIENT_COUNT, WINE_BREW_TIME);
			addOptionalRecipe("berry_wine_natura_blackberry", "Blackberry Wine", blackberryWine, "natura:edibles", 4,
				STANDARD_INGREDIENT_COUNT, WINE_BREW_TIME);
		}
	}

	public static List<FoudreBrewingRecipe> getRecipes() {
		return Collections.unmodifiableList(RECIPES);
	}

	public static boolean isValidIngredient(ItemStack stack) {
		if (stack == null || stack.stackSize <= 0) {
			return false;
		}

		for (FoudreBrewingRecipe recipe : RECIPES) {
			if (recipe.matchesIngredient(stack)) {
				return true;
			}
		}

		return false;
	}

	public static FoudreBrewingRecipe findMatchingRecipe(FluidStack fluid, ItemStack[] inventory, int capacity) {
		if (fluid == null || fluid.getFluid() != FluidRegistry.WATER || fluid.amount < capacity) {
			return null;
		}

		for (FoudreBrewingRecipe recipe : RECIPES) {
			if (recipe.matches(inventory)) {
				return recipe;
			}
		}

		return null;
	}

	public static FluidStack createBrewedFluid(FoudreBrewingRecipe recipe, int amount) {
		FluidStack fluid = new FluidStack(recipe.getOutput(), amount);
		initializeAge(fluid);
		return fluid;
	}

	public static boolean isAgeable(FluidStack fluid) {
		return getAgeProfile(fluid) != null;
	}

	public static boolean ageFluid(FluidStack fluid, int ticks) {
		AgeProfile profile = getAgeProfile(fluid);

		if (profile == null || ticks <= 0) {
			return false;
		}

		int oldAgeTicks = getAgeTicks(fluid);
		int oldLevel = profile.getLevel(oldAgeTicks);

		if (oldLevel >= profile.getMaxLevel()) {
			return false;
		}

		setAgeTicks(fluid, Math.min(profile.getMaxThreshold(), Math.max(0, oldAgeTicks + ticks)));
		return profile.getLevel(getAgeTicks(fluid)) != oldLevel;
	}

	public static String getAgedFluidName(FluidStack fluid) {
		if (fluid == null || fluid.getFluid() == null) {
			return "";
		}

		AgeProfile profile = getAgeProfile(fluid);
		return profile == null ? fluid.getLocalizedName() : profile.getName(getAgeTicks(fluid)) + " "
			+ fluid.getLocalizedName();
	}

	public static String getAgeLevelName(FluidStack fluid) {
		AgeProfile profile = getAgeProfile(fluid);
		return profile == null ? "" : profile.getName(getAgeTicks(fluid));
	}

	public static String getNextAgeLevelName(FluidStack fluid) {
		AgeProfile profile = getAgeProfile(fluid);
		return profile == null ? "" : profile.getNextName(getAgeTicks(fluid));
	}

	public static int getAgeProgress(FluidStack fluid) {
		AgeProfile profile = getAgeProfile(fluid);
		return profile == null ? 0 : profile.getProgress(getAgeTicks(fluid));
	}

	public static int getAgeProgressTotal(FluidStack fluid) {
		AgeProfile profile = getAgeProfile(fluid);
		return profile == null ? 0 : profile.getProgressTotal(getAgeTicks(fluid));
	}

	private static Fluid registerFluid(String name, String unlocalizedName, int color) {
		Fluid existing = FluidRegistry.getFluid(name);

		if (existing != null) {
			return existing;
		}

		Fluid fluid = new TintedFluid(name, STILL, FLOWING, color).setUnlocalizedName(unlocalizedName)
			.setDensity(1000).setViscosity(1000);
		FluidRegistry.registerFluid(fluid);
		return FluidRegistry.getFluid(name);
	}

	private static void addRecipe(String id, String displayName, Fluid output, ItemStack ingredient, int count,
			int brewTime) {
		if (output != null && ingredient != null && ingredient.getItem() != null) {
			RECIPES.add(new FoudreBrewingRecipe(id, displayName, output, brewTime, ingredient, count));
		}
	}

	private static void addOptionalRecipe(String id, String displayName, Fluid output, String registryName, int meta,
			int count, int brewTime) {
		Item item = Item.getByNameOrId(registryName);

		if (item != null) {
			addRecipe(id, displayName, output, new ItemStack(item, 1, meta), count, brewTime);
		}
	}

	private static void registerAgeProfiles() {
		AGE_PROFILES.clear();
		addAgeProfile(ALE_PROFILE, ale);
		addAgeProfile(CIDER_PROFILE, cider, appleCider, perry);
		addAgeProfile(MEAD_PROFILE, mead);
		addAgeProfile(WINE_PROFILE, wine, berryWine, riceWine, grapeWine, peachWine, persimmonWine, wildBerryWine,
			raspberryWine, blueberryWine, blackberryWine, strawberryWine, cranberryWine);
	}

	private static void addAgeProfile(AgeProfile profile, Fluid... fluids) {
		for (Fluid fluid : fluids) {
			if (fluid != null) {
				AGE_PROFILES.put(fluid, profile);
			}
		}
	}

	private static void initializeAge(FluidStack fluid) {
		if (isAgeable(fluid)) {
			setAgeTicks(fluid, 0);
		}
	}

	private static int getAgeTicks(FluidStack fluid) {
		return fluid != null && fluid.tag != null && fluid.tag.hasKey(AGE_TICKS_TAG, 3)
			? fluid.tag.getInteger(AGE_TICKS_TAG) : 0;
	}

	private static void setAgeTicks(FluidStack fluid, int ageTicks) {
		if (fluid == null) {
			return;
		}

		if (fluid.tag == null) {
			fluid.tag = new NBTTagCompound();
		}

		fluid.tag.setInteger(AGE_TICKS_TAG, ageTicks);
	}

	private static AgeProfile getAgeProfile(FluidStack fluid) {
		return fluid == null ? null : AGE_PROFILES.get(fluid.getFluid());
	}

	private static int getAgedFluidColor(FluidStack fluid, int freshColor) {
		AgeProfile profile = getAgeProfile(fluid);

		if (profile == null || profile.getMaxLevel() <= 0) {
			return freshColor;
		}

		int level = profile.getLevel(getAgeTicks(fluid));

		if (level <= 0) {
			return freshColor;
		}

		float ageRatio = (float)level / (float)profile.getMaxLevel();
		return darkenColor(freshColor, 1.0F - ageRatio * MAX_AGE_DARKENING);
	}

	private static int darkenColor(int color, float multiplier) {
		int alpha = (color & COLOR_ALPHA_MASK) == 0 ? COLOR_ALPHA_MASK : color & COLOR_ALPHA_MASK;
		int red = scaleColorChannel(color >> COLOR_RED_SHIFT & COLOR_CHANNEL_MASK, multiplier);
		int green = scaleColorChannel(color >> COLOR_GREEN_SHIFT & COLOR_CHANNEL_MASK, multiplier);
		int blue = scaleColorChannel(color & COLOR_CHANNEL_MASK, multiplier);
		return alpha | red << COLOR_RED_SHIFT | green << COLOR_GREEN_SHIFT | blue;
	}

	private static int scaleColorChannel(int channel, float multiplier) {
		return Math.max(0, Math.min(COLOR_CHANNEL_MAX, Math.round(channel * multiplier)));
	}

	public static class FoudreBrewingRecipe {
		private final String id;
		private final String displayName;
		private final Fluid output;
		private final int brewTime;
		private final ItemStack ingredient;
		private final int ingredientCount;

		private FoudreBrewingRecipe(String id, String displayName, Fluid output, int brewTime, ItemStack ingredient,
				int ingredientCount) {
			this.id = id;
			this.displayName = displayName;
			this.output = output;
			this.brewTime = brewTime;
			this.ingredient = ingredient;
			this.ingredientCount = ingredientCount;
		}

		public String getId() {
			return this.id;
		}

		public String getDisplayName() {
			return this.displayName;
		}

		public Fluid getOutput() {
			return this.output;
		}

		public int getBrewTime() {
			return this.brewTime;
		}

		public boolean matches(ItemStack[] inventory) {
			return this.countMatchingItems(inventory) >= this.ingredientCount;
		}

		public void consumeIngredients(ItemStack[] inventory) {
			int remaining = this.ingredientCount;

			for (int i = 0; i < inventory.length && remaining > 0; i++) {
				ItemStack stack = inventory[i];

				if (!this.matchesIngredient(stack)) {
					continue;
				}

				int consumed = Math.min(remaining, stack.stackSize);
				stack.stackSize -= consumed;
				remaining -= consumed;

				if (stack.stackSize <= 0) {
					inventory[i] = null;
				}
			}
		}

		private int countMatchingItems(ItemStack[] inventory) {
			int count = 0;

			for (ItemStack stack : inventory) {
				if (this.matchesIngredient(stack)) {
					count += stack.stackSize;
				}
			}

			return count;
		}

		private boolean matchesIngredient(ItemStack stack) {
			return stack != null && stack.stackSize > 0
				&& stack.getItem() == this.ingredient.getItem()
				&& (this.ingredient.getItemDamage() == OreDictionary.WILDCARD_VALUE
					|| stack.getItemDamage() == this.ingredient.getItemDamage());
		}
	}

	private static class TintedFluid extends Fluid {
		private final int color;

		TintedFluid(String fluidName, ResourceLocation still, ResourceLocation flowing, int color) {
			super(fluidName, still, flowing);
			this.color = color;
		}

		@Override
		public int getColor(FluidStack stack) {
			return getAgedFluidColor(stack, this.color);
		}

		@Override
		public int getColor() {
			return this.color;
		}
	}

	private static class AgeProfile {
		private final String[] names;
		private final int[] thresholds;

		AgeProfile(String[] names, int[] thresholds) {
			this.names = names;
			this.thresholds = thresholds;
		}

		String getName(int ageTicks) {
			return this.names[this.getLevel(ageTicks)];
		}

		String getNextName(int ageTicks) {
			int nextLevel = this.getLevel(ageTicks) + 1;
			return nextLevel >= this.names.length ? "" : this.names[nextLevel];
		}

		int getProgress(int ageTicks) {
			int level = this.getLevel(ageTicks);

			if (level >= this.thresholds.length - 1) {
				return 0;
			}

			return Math.max(0, ageTicks - this.thresholds[level]);
		}

		int getProgressTotal(int ageTicks) {
			int level = this.getLevel(ageTicks);

			if (level >= this.thresholds.length - 1) {
				return 0;
			}

			return this.thresholds[level + 1] - this.thresholds[level];
		}

		int getLevel(int ageTicks) {
			int level = 0;

			for (int i = 1; i < this.thresholds.length; i++) {
				if (ageTicks < this.thresholds[i]) {
					break;
				}

				level = i;
			}

			return level;
		}

		int getMaxLevel() {
			return this.thresholds.length - 1;
		}

		int getMaxThreshold() {
			return this.thresholds[this.thresholds.length - 1];
		}
	}
}
