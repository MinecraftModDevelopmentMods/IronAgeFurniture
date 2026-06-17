package com.mcmoddev.ironagefurniture.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

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
	private static final ResourceLocation DRINK_TEXTURE = new ResourceLocation("minecraft", "blocks/quartz_block_side");
	private static final ResourceLocation STILL = DRINK_TEXTURE;
	private static final ResourceLocation FLOWING = DRINK_TEXTURE;
	private static final String AGE_TICKS_TAG = "FoudreAgeTicks";
	private static final int DAY = 20 * 60 * 20;
	private static final int ALE_BREW_TIME = DAY;
	private static final int CIDER_BREW_TIME = DAY + DAY / 2;
	private static final int WINE_BREW_TIME = DAY * 2;
	private static final int MEAD_BREW_TIME = DAY * 2;
	private static final int WASH_BREW_TIME = DAY + DAY / 2;
	private static final int STANDARD_INGREDIENT_COUNT = 64;
	private static final int HONEY_INGREDIENT_COUNT = 32;
	private static final int BASE_INGREDIENT_COUNT = 48;
	private static final int FLAVOR_INGREDIENT_COUNT = 16;
	private static final int SPICE_INGREDIENT_COUNT = 8;
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
	private static final int POTATO_WASH_COLOR = 0xFFC9B86A;
	private static final int SUGAR_WASH_COLOR = 0xFFE3C36A;
	private static final int CORN_MASH_COLOR = 0xFFE0B442;
	private static final int GIN_MASH_COLOR = 0xFFBFD6A0;
	private static final int SPICED_RUM_WASH_COLOR = 0xFFC47A32;
	private static final int COCONUT_RUM_WASH_COLOR = 0xFFEAD7A4;
	private static final int PINEAPPLE_RUM_WASH_COLOR = 0xFFE7B947;
	private static final int VANILLA_RUM_WASH_COLOR = 0xFFE0B46C;
	private static final int CITRUS_RUM_WASH_COLOR = 0xFFE0A23C;
	private static final int MAPLE_RUM_WASH_COLOR = 0xFFB97530;
	private static final int GINGER_RUM_WASH_COLOR = 0xFFD89538;
	private static final int CHERRY_WINE_COLOR_FRESH = 0xFF8B1835;
	private static final int PLUM_WINE_COLOR_FRESH = 0xFF5A254E;
	private static final int APRICOT_WINE_COLOR_FRESH = 0xFFE08A4B;
	private static final int MANGO_WINE_COLOR_FRESH = 0xFFE39A2A;
	private static final int PINEAPPLE_WINE_COLOR_FRESH = 0xFFE4C23F;
	private static final int BANANA_WINE_COLOR_FRESH = 0xFFE5D178;
	private static final int DATE_WINE_COLOR_FRESH = 0xFF8B4B25;
	private static final int FIG_WINE_COLOR_FRESH = 0xFF6A3155;
	private static final int GRAPEFRUIT_WINE_COLOR_FRESH = 0xFFE2734D;
	private static final int POMEGRANATE_WINE_COLOR_FRESH = 0xFF8C1235;
	private static final int PAPAYA_WINE_COLOR_FRESH = 0xFFE47F37;
	private static final int STARFRUIT_WINE_COLOR_FRESH = 0xFFE2C54C;
	private static final int GOOSEBERRY_WINE_COLOR_FRESH = 0xFF8DAE4A;
	private static final int CACTUS_FRUIT_WINE_COLOR_FRESH = 0xFFB44662;
	private static final int MALOBERRY_WINE_COLOR_FRESH = 0xFFD1842D;
	private static final int BLIGHTBERRY_WINE_COLOR_FRESH = 0xFF5B6A24;
	private static final int DUSKBERRY_WINE_COLOR_FRESH = 0xFF332B73;
	private static final int SKYBERRY_WINE_COLOR_FRESH = 0xFF4B83B8;
	private static final int STINGBERRY_WINE_COLOR_FRESH = 0xFFB8322A;

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
	public static Fluid potatoWash;
	public static Fluid sugarWash;
	public static Fluid cornMash;
	public static Fluid ginMash;
	public static Fluid spicedRumWash;
	public static Fluid coconutRumWash;
	public static Fluid pineappleRumWash;
	public static Fluid vanillaRumWash;
	public static Fluid citrusRumWash;
	public static Fluid mapleRumWash;
	public static Fluid gingerRumWash;
	public static Fluid cherryWine;
	public static Fluid plumWine;
	public static Fluid apricotWine;
	public static Fluid mangoWine;
	public static Fluid pineappleWine;
	public static Fluid bananaWine;
	public static Fluid dateWine;
	public static Fluid figWine;
	public static Fluid grapefruitWine;
	public static Fluid pomegranateWine;
	public static Fluid papayaWine;
	public static Fluid starfruitWine;
	public static Fluid gooseberryWine;
	public static Fluid cactusFruitWine;
	public static Fluid maloberryWine;
	public static Fluid blightberryWine;
	public static Fluid duskberryWine;
	public static Fluid skyberryWine;
	public static Fluid stingberryWine;

	private static final List<FoudreBrewingRecipe> RECIPES = new ArrayList<FoudreBrewingRecipe>();
	private static final Map<Fluid, AgeProfile> AGE_PROFILES = new HashMap<Fluid, AgeProfile>();
	private static final Set<Fluid> DISTILLATION_BASES = new HashSet<Fluid>();
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
	private static final AgeProfile SPIRIT_PROFILE = new AgeProfile(
		new String[] { "New", "Rested", "Mature", "Aged", "Reserve", "Vintage" },
		new int[] { 0, DAY * 2, DAY * 5, DAY * 10, DAY * 20, DAY * 40 });

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
		potatoWash = registerFluid("ironagefurniture_potato_wash", "potato_wash", POTATO_WASH_COLOR);
		sugarWash = registerFluid("ironagefurniture_sugar_wash", "sugar_wash", SUGAR_WASH_COLOR);
		cornMash = registerFluid("ironagefurniture_corn_mash", "corn_mash", CORN_MASH_COLOR);
		ginMash = registerFluid("ironagefurniture_gin_mash", "gin_mash", GIN_MASH_COLOR);
		spicedRumWash = registerFluid("ironagefurniture_spiced_rum_wash", "spiced_rum_wash",
			SPICED_RUM_WASH_COLOR);
		coconutRumWash = registerFluid("ironagefurniture_coconut_rum_wash", "coconut_rum_wash",
			COCONUT_RUM_WASH_COLOR);
		pineappleRumWash = registerFluid("ironagefurniture_pineapple_rum_wash", "pineapple_rum_wash",
			PINEAPPLE_RUM_WASH_COLOR);
		vanillaRumWash = registerFluid("ironagefurniture_vanilla_rum_wash", "vanilla_rum_wash",
			VANILLA_RUM_WASH_COLOR);
		citrusRumWash = registerFluid("ironagefurniture_citrus_rum_wash", "citrus_rum_wash",
			CITRUS_RUM_WASH_COLOR);
		mapleRumWash = registerFluid("ironagefurniture_maple_rum_wash", "maple_rum_wash",
			MAPLE_RUM_WASH_COLOR);
		gingerRumWash = registerFluid("ironagefurniture_ginger_rum_wash", "ginger_rum_wash",
			GINGER_RUM_WASH_COLOR);
		cherryWine = registerFluid("ironagefurniture_cherry_wine", "cherry_wine", CHERRY_WINE_COLOR_FRESH);
		plumWine = registerFluid("ironagefurniture_plum_wine", "plum_wine", PLUM_WINE_COLOR_FRESH);
		apricotWine = registerFluid("ironagefurniture_apricot_wine", "apricot_wine", APRICOT_WINE_COLOR_FRESH);
		mangoWine = registerFluid("ironagefurniture_mango_wine", "mango_wine", MANGO_WINE_COLOR_FRESH);
		pineappleWine = registerFluid("ironagefurniture_pineapple_wine", "pineapple_wine",
			PINEAPPLE_WINE_COLOR_FRESH);
		bananaWine = registerFluid("ironagefurniture_banana_wine", "banana_wine", BANANA_WINE_COLOR_FRESH);
		dateWine = registerFluid("ironagefurniture_date_wine", "date_wine", DATE_WINE_COLOR_FRESH);
		figWine = registerFluid("ironagefurniture_fig_wine", "fig_wine", FIG_WINE_COLOR_FRESH);
		grapefruitWine = registerFluid("ironagefurniture_grapefruit_wine", "grapefruit_wine",
			GRAPEFRUIT_WINE_COLOR_FRESH);
		pomegranateWine = registerFluid("ironagefurniture_pomegranate_wine", "pomegranate_wine",
			POMEGRANATE_WINE_COLOR_FRESH);
		papayaWine = registerFluid("ironagefurniture_papaya_wine", "papaya_wine", PAPAYA_WINE_COLOR_FRESH);
		starfruitWine = registerFluid("ironagefurniture_starfruit_wine", "starfruit_wine",
			STARFRUIT_WINE_COLOR_FRESH);
		gooseberryWine = registerFluid("ironagefurniture_gooseberry_wine", "gooseberry_wine",
			GOOSEBERRY_WINE_COLOR_FRESH);
		cactusFruitWine = registerFluid("ironagefurniture_cactus_fruit_wine", "cactus_fruit_wine",
			CACTUS_FRUIT_WINE_COLOR_FRESH);
		maloberryWine = registerFluid("ironagefurniture_maloberry_wine", "maloberry_wine",
			MALOBERRY_WINE_COLOR_FRESH);
		blightberryWine = registerFluid("ironagefurniture_blightberry_wine", "blightberry_wine",
			BLIGHTBERRY_WINE_COLOR_FRESH);
		duskberryWine = registerFluid("ironagefurniture_duskberry_wine", "duskberry_wine",
			DUSKBERRY_WINE_COLOR_FRESH);
		skyberryWine = registerFluid("ironagefurniture_skyberry_wine", "skyberry_wine",
			SKYBERRY_WINE_COLOR_FRESH);
		stingberryWine = registerFluid("ironagefurniture_stingberry_wine", "stingberry_wine",
			STINGBERRY_WINE_COLOR_FRESH);
		DISTILLATION_BASES.clear();
		addDistillationBases(potatoWash, sugarWash, cornMash, ginMash, spicedRumWash, coconutRumWash,
			pineappleRumWash, vanillaRumWash, citrusRumWash, mapleRumWash, gingerRumWash);
		registerAgeProfiles();
	}

	public static void initRecipes() {
		RECIPES.clear();
		registerFluids();

		addRecipe("ale_wheat", "Ale", ale, new ItemStack(Items.WHEAT), STANDARD_INGREDIENT_COUNT,
			ALE_BREW_TIME);
		addRecipe("cider_apple", "Apple Cider", appleCider, new ItemStack(Items.APPLE),
			STANDARD_INGREDIENT_COUNT, CIDER_BREW_TIME);
		addRecipe("potato_wash_vanilla_potato", "Potato Wash", potatoWash, new ItemStack(Items.POTATO),
			STANDARD_INGREDIENT_COUNT, WASH_BREW_TIME);
		addRecipe("sugar_wash_vanilla_sugar", "Sugar Wash", sugarWash, new ItemStack(Items.SUGAR),
			STANDARD_INGREDIENT_COUNT, WASH_BREW_TIME);

		if (IronAgeFurnitureConfiguration.INTEGRATION_HARVESTCRAFT && Loader.isModLoaded("harvestcraft")) {
			addRecipe("gin_mash_harvestcraft_barley_spice_ginger", "Gin Mash", ginMash, WASH_BREW_TIME,
				optionalRequirement("harvestcraft:barleyitem", 0, BASE_INGREDIENT_COUNT),
				optionalRequirement("harvestcraft:spiceleafitem", 0, SPICE_INGREDIENT_COUNT),
				optionalRequirement("harvestcraft:gingeritem", 0, SPICE_INGREDIENT_COUNT));
			addRecipe("gin_mash_harvestcraft_barley_tea_citrus", "Gin Mash", ginMash, WASH_BREW_TIME,
				optionalRequirement("harvestcraft:barleyitem", 0, BASE_INGREDIENT_COUNT),
				optionalRequirement("harvestcraft:tealeafitem", 0, SPICE_INGREDIENT_COUNT),
				optionalRequirement("harvestcraft:lemonitem", 0, SPICE_INGREDIENT_COUNT));
			addRecipe("gin_mash_harvestcraft_potato_spice_lime", "Gin Mash", ginMash, WASH_BREW_TIME,
				requirement(new ItemStack(Items.POTATO), BASE_INGREDIENT_COUNT),
				optionalRequirement("harvestcraft:spiceleafitem", 0, SPICE_INGREDIENT_COUNT),
				optionalRequirement("harvestcraft:limeitem", 0, SPICE_INGREDIENT_COUNT));
			addRecipe("rum_wash_harvestcraft_spiced", "Spiced Rum Wash", spicedRumWash, WASH_BREW_TIME,
				requirement(new ItemStack(Items.SUGAR), BASE_INGREDIENT_COUNT),
				optionalRequirement("harvestcraft:cinnamonitem", 0, SPICE_INGREDIENT_COUNT),
				optionalRequirement("harvestcraft:nutmegitem", 0, SPICE_INGREDIENT_COUNT));
			addRecipe("rum_wash_harvestcraft_coconut", "Coconut Rum Wash", coconutRumWash, WASH_BREW_TIME,
				requirement(new ItemStack(Items.SUGAR), BASE_INGREDIENT_COUNT),
				optionalRequirement("harvestcraft:coconutitem", 0, FLAVOR_INGREDIENT_COUNT));
			addRecipe("rum_wash_harvestcraft_pineapple", "Pineapple Rum Wash", pineappleRumWash, WASH_BREW_TIME,
				requirement(new ItemStack(Items.SUGAR), BASE_INGREDIENT_COUNT),
				optionalRequirement("harvestcraft:pineappleitem", 0, FLAVOR_INGREDIENT_COUNT));
			addRecipe("rum_wash_harvestcraft_vanilla", "Vanilla Rum Wash", vanillaRumWash, WASH_BREW_TIME,
				requirement(new ItemStack(Items.SUGAR), BASE_INGREDIENT_COUNT),
				optionalRequirement("harvestcraft:vanillabeanitem", 0, SPICE_INGREDIENT_COUNT));
			addRecipe("rum_wash_harvestcraft_citrus_lemon_lime", "Citrus Rum Wash", citrusRumWash, WASH_BREW_TIME,
				requirement(new ItemStack(Items.SUGAR), BASE_INGREDIENT_COUNT),
				optionalRequirement("harvestcraft:lemonitem", 0, SPICE_INGREDIENT_COUNT),
				optionalRequirement("harvestcraft:limeitem", 0, SPICE_INGREDIENT_COUNT));
			addRecipe("rum_wash_harvestcraft_citrus_orange", "Citrus Rum Wash", citrusRumWash, WASH_BREW_TIME,
				requirement(new ItemStack(Items.SUGAR), BASE_INGREDIENT_COUNT),
				optionalRequirement("harvestcraft:orangeitem", 0, FLAVOR_INGREDIENT_COUNT));
			addRecipe("rum_wash_harvestcraft_citrus_grapefruit", "Citrus Rum Wash", citrusRumWash, WASH_BREW_TIME,
				requirement(new ItemStack(Items.SUGAR), BASE_INGREDIENT_COUNT),
				optionalRequirement("harvestcraft:grapefruititem", 0, FLAVOR_INGREDIENT_COUNT));
			addRecipe("rum_wash_harvestcraft_maple", "Maple Rum Wash", mapleRumWash, WASH_BREW_TIME,
				requirement(new ItemStack(Items.SUGAR), BASE_INGREDIENT_COUNT),
				optionalRequirement("harvestcraft:maplesyrupitem", 0, SPICE_INGREDIENT_COUNT));
			addRecipe("rum_wash_harvestcraft_ginger", "Ginger Rum Wash", gingerRumWash, WASH_BREW_TIME,
				requirement(new ItemStack(Items.SUGAR), BASE_INGREDIENT_COUNT),
				optionalRequirement("harvestcraft:gingeritem", 0, SPICE_INGREDIENT_COUNT));
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
			addOptionalRecipe("potato_wash_harvestcraft_sweet_potato", "Potato Wash", potatoWash,
				"harvestcraft:sweetpotatoitem", 0, STANDARD_INGREDIENT_COUNT, WASH_BREW_TIME);
			addOptionalRecipe("sugar_wash_harvestcraft_maple_syrup", "Sugar Wash", sugarWash,
				"harvestcraft:maplesyrupitem", 0, HONEY_INGREDIENT_COUNT, WASH_BREW_TIME);
			addOptionalRecipe("corn_mash_harvestcraft_corn", "Corn Mash", cornMash, "harvestcraft:cornitem", 0,
				STANDARD_INGREDIENT_COUNT, WASH_BREW_TIME);
			addOptionalRecipe("wine_harvestcraft_cherry", "Cherry Wine", cherryWine, "harvestcraft:cherryitem", 0,
				STANDARD_INGREDIENT_COUNT, WINE_BREW_TIME);
			addOptionalRecipe("wine_harvestcraft_plum", "Plum Wine", plumWine, "harvestcraft:plumitem", 0,
				STANDARD_INGREDIENT_COUNT, WINE_BREW_TIME);
			addOptionalRecipe("wine_harvestcraft_apricot", "Apricot Wine", apricotWine, "harvestcraft:apricotitem",
				0, STANDARD_INGREDIENT_COUNT, WINE_BREW_TIME);
			addOptionalRecipe("wine_harvestcraft_mango", "Mango Wine", mangoWine, "harvestcraft:mangoitem", 0,
				STANDARD_INGREDIENT_COUNT, WINE_BREW_TIME);
			addOptionalRecipe("wine_harvestcraft_pineapple", "Pineapple Wine", pineappleWine,
				"harvestcraft:pineappleitem", 0, STANDARD_INGREDIENT_COUNT, WINE_BREW_TIME);
			addOptionalRecipe("wine_harvestcraft_banana", "Banana Wine", bananaWine, "harvestcraft:bananaitem", 0,
				STANDARD_INGREDIENT_COUNT, WINE_BREW_TIME);
			addOptionalRecipe("wine_harvestcraft_date", "Date Wine", dateWine, "harvestcraft:dateitem", 0,
				STANDARD_INGREDIENT_COUNT, WINE_BREW_TIME);
			addOptionalRecipe("wine_harvestcraft_fig", "Fig Wine", figWine, "harvestcraft:figitem", 0,
				STANDARD_INGREDIENT_COUNT, WINE_BREW_TIME);
			addOptionalRecipe("wine_harvestcraft_grapefruit", "Grapefruit Wine", grapefruitWine,
				"harvestcraft:grapefruititem", 0, STANDARD_INGREDIENT_COUNT, WINE_BREW_TIME);
			addOptionalRecipe("wine_harvestcraft_pomegranate", "Pomegranate Wine", pomegranateWine,
				"harvestcraft:pomegranateitem", 0, STANDARD_INGREDIENT_COUNT, WINE_BREW_TIME);
			addOptionalRecipe("wine_harvestcraft_papaya", "Papaya Wine", papayaWine, "harvestcraft:papayaitem", 0,
				STANDARD_INGREDIENT_COUNT, WINE_BREW_TIME);
			addOptionalRecipe("wine_harvestcraft_starfruit", "Starfruit Wine", starfruitWine,
				"harvestcraft:starfruititem", 0, STANDARD_INGREDIENT_COUNT, WINE_BREW_TIME);
			addOptionalRecipe("wine_harvestcraft_gooseberry", "Gooseberry Wine", gooseberryWine,
				"harvestcraft:gooseberryitem", 0, STANDARD_INGREDIENT_COUNT, WINE_BREW_TIME);
			addOptionalRecipe("wine_harvestcraft_cactus_fruit", "Cactus Fruit Wine", cactusFruitWine,
				"harvestcraft:cactusfruititem", 0, STANDARD_INGREDIENT_COUNT, WINE_BREW_TIME);
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
			addOptionalRecipe("sugar_wash_bop_river_cane", "Sugar Wash", sugarWash, "biomesoplenty:rivercane", 0,
				STANDARD_INGREDIENT_COUNT, WASH_BREW_TIME);
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
			addOptionalRecipe("wine_natura_maloberry", "Maloberry Wine", maloberryWine, "natura:edibles", 5,
				STANDARD_INGREDIENT_COUNT, WINE_BREW_TIME);
			addOptionalRecipe("wine_natura_blightberry", "Blightberry Wine", blightberryWine, "natura:edibles", 6,
				STANDARD_INGREDIENT_COUNT, WINE_BREW_TIME);
			addOptionalRecipe("wine_natura_duskberry", "Duskberry Wine", duskberryWine, "natura:edibles", 7,
				STANDARD_INGREDIENT_COUNT, WINE_BREW_TIME);
			addOptionalRecipe("wine_natura_skyberry", "Skyberry Wine", skyberryWine, "natura:edibles", 8,
				STANDARD_INGREDIENT_COUNT, WINE_BREW_TIME);
			addOptionalRecipe("wine_natura_stingberry", "Stingberry Wine", stingberryWine, "natura:edibles", 9,
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

		FoudreBrewingRecipe bestRecipe = null;

		for (FoudreBrewingRecipe recipe : RECIPES) {
			if (recipe.matches(inventory) && isMoreSpecific(recipe, bestRecipe)) {
				bestRecipe = recipe;
			}
		}

		return bestRecipe;
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

	public static boolean canAgeFurther(FluidStack fluid) {
		AgeProfile profile = getAgeProfile(fluid);
		return profile != null && profile.getLevel(getAgeTicks(fluid)) < profile.getMaxLevel();
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

	public static boolean resetAgeProgressToCurrentLevel(FluidStack fluid) {
		AgeProfile profile = getAgeProfile(fluid);

		if (profile == null) {
			return false;
		}

		int oldAgeTicks = getAgeTicks(fluid);
		int resetAgeTicks = profile.getThreshold(profile.getLevel(oldAgeTicks));

		if (oldAgeTicks == resetAgeTicks && (resetAgeTicks != 0 || !hasExplicitAgeTicks(fluid))) {
			return false;
		}

		setAgeTicks(fluid, resetAgeTicks);
		return true;
	}

	public static FluidStack copyWithCurrentAgeLevel(FluidStack fluid) {
		if (fluid == null) {
			return null;
		}

		FluidStack copy = fluid.copy();
		resetAgeProgressToCurrentLevel(copy);
		return copy;
	}

	public static boolean isDistillationBase(@Nullable FluidStack fluid) {
		return fluid != null && isDistillationBaseFluid(fluid.getFluid());
	}

	public static boolean isDistillationBaseFluid(@Nullable Fluid fluid) {
		return fluid != null && DISTILLATION_BASES.contains(fluid);
	}

	public static boolean isBottleable(@Nullable FluidStack fluid) {
		return fluid != null && fluid.getFluid() != null && !isDistillationBase(fluid);
	}

	public static boolean isDrinkable(@Nullable FluidStack fluid) {
		return isBottleable(fluid);
	}

	private static Fluid registerFluid(String name, String unlocalizedName, int color) {
		Fluid existing = FluidRegistry.getFluid(name);

		if (existing != null) {
			FluidRegistry.addBucketForFluid(existing);
			return existing;
		}

		Fluid fluid = new TintedFluid(name, STILL, FLOWING, color).setUnlocalizedName(unlocalizedName)
			.setDensity(1000).setViscosity(1000);
		FluidRegistry.registerFluid(fluid);
		fluid = FluidRegistry.getFluid(name);
		FluidRegistry.addBucketForFluid(fluid);
		return fluid;
	}

	private static void addDistillationBases(Fluid... fluids) {
		for (Fluid fluid : fluids) {
			if (fluid != null) {
				DISTILLATION_BASES.add(fluid);
			}
		}
	}

	private static void addRecipe(String id, String displayName, Fluid output, ItemStack ingredient, int count,
			int brewTime) {
		addRecipe(id, displayName, output, brewTime, requirement(ingredient, count));
	}

	private static void addRecipe(String id, String displayName, Fluid output, int brewTime,
			IngredientRequirement... requirements) {
		if (output == null || requirements == null || requirements.length <= 0) {
			return;
		}

		List<IngredientRequirement> recipeRequirements = new ArrayList<IngredientRequirement>();

		for (IngredientRequirement requirement : requirements) {
			if (requirement == null || !requirement.isValid()) {
				return;
			}

			recipeRequirements.add(requirement);
		}

		RECIPES.add(new FoudreBrewingRecipe(id, displayName, output, brewTime, recipeRequirements));
	}

	private static void addOptionalRecipe(String id, String displayName, Fluid output, String registryName, int meta,
			int count, int brewTime) {
		addRecipe(id, displayName, output, brewTime, optionalRequirement(registryName, meta, count));
	}

	private static IngredientRequirement requirement(ItemStack ingredient, int count) {
		return new IngredientRequirement(ingredient, count);
	}

	private static IngredientRequirement optionalRequirement(String registryName, int meta, int count) {
		Item item = Item.getByNameOrId(registryName);

		if (item != null) {
			return requirement(new ItemStack(item, 1, meta), count);
		}

		return null;
	}

	private static boolean isMoreSpecific(FoudreBrewingRecipe recipe, FoudreBrewingRecipe currentBest) {
		return currentBest == null || recipe.getRequirementCount() > currentBest.getRequirementCount()
			|| recipe.getRequirementCount() == currentBest.getRequirementCount()
				&& recipe.getRequiredIngredientCount() > currentBest.getRequiredIngredientCount();
	}

	public static void registerAgeProfiles() {
		AGE_PROFILES.clear();
		addAgeProfile(ALE_PROFILE, ale);
		addAgeProfile(CIDER_PROFILE, cider, appleCider, perry);
		addAgeProfile(MEAD_PROFILE, mead);
		addAgeProfile(WINE_PROFILE, wine, berryWine, riceWine, grapeWine, peachWine, persimmonWine, wildBerryWine,
			raspberryWine, blueberryWine, blackberryWine, strawberryWine, cranberryWine, cherryWine, plumWine,
			apricotWine, mangoWine, pineappleWine, bananaWine, dateWine, figWine, grapefruitWine, pomegranateWine,
			papayaWine, starfruitWine, gooseberryWine, cactusFruitWine, maloberryWine, blightberryWine,
			duskberryWine, skyberryWine, stingberryWine);
		addAgeProfile(SPIRIT_PROFILE, PotStillDistillingRegistry.whisky, PotStillDistillingRegistry.brandy,
			PotStillDistillingRegistry.appleBrandy, PotStillDistillingRegistry.pearBrandy,
			PotStillDistillingRegistry.honeySpirit, PotStillDistillingRegistry.riceSpirit,
			PotStillDistillingRegistry.peachBrandy, PotStillDistillingRegistry.persimmonBrandy,
			PotStillDistillingRegistry.berryBrandy, PotStillDistillingRegistry.wildBerryBrandy,
			PotStillDistillingRegistry.raspberryBrandy, PotStillDistillingRegistry.blueberryBrandy,
			PotStillDistillingRegistry.blackberryBrandy, PotStillDistillingRegistry.strawberryBrandy,
			PotStillDistillingRegistry.cranberryBrandy, PotStillDistillingRegistry.vodka,
			PotStillDistillingRegistry.rum, PotStillDistillingRegistry.spicedRum,
			PotStillDistillingRegistry.coconutRum, PotStillDistillingRegistry.pineappleRum,
			PotStillDistillingRegistry.vanillaRum, PotStillDistillingRegistry.citrusRum,
			PotStillDistillingRegistry.mapleRum, PotStillDistillingRegistry.gingerRum,
			PotStillDistillingRegistry.cornWhiskey, PotStillDistillingRegistry.gin,
			PotStillDistillingRegistry.cherryBrandy, PotStillDistillingRegistry.plumBrandy,
			PotStillDistillingRegistry.apricotBrandy, PotStillDistillingRegistry.mangoBrandy,
			PotStillDistillingRegistry.pineappleBrandy, PotStillDistillingRegistry.bananaBrandy,
			PotStillDistillingRegistry.dateBrandy, PotStillDistillingRegistry.figBrandy,
			PotStillDistillingRegistry.grapefruitBrandy, PotStillDistillingRegistry.pomegranateBrandy,
			PotStillDistillingRegistry.papayaBrandy, PotStillDistillingRegistry.starfruitBrandy,
			PotStillDistillingRegistry.gooseberryBrandy, PotStillDistillingRegistry.cactusFruitBrandy,
			PotStillDistillingRegistry.maloberryBrandy, PotStillDistillingRegistry.blightberryBrandy,
			PotStillDistillingRegistry.duskberryBrandy, PotStillDistillingRegistry.skyberryBrandy,
			PotStillDistillingRegistry.stingberryBrandy);
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

		if (ageTicks <= 0) {
			if (fluid.tag != null) {
				fluid.tag.removeTag(AGE_TICKS_TAG);

				if (fluid.tag.hasNoTags()) {
					fluid.tag = null;
				}
			}

			return;
		}

		if (fluid.tag == null) {
			fluid.tag = new NBTTagCompound();
		}

		fluid.tag.setInteger(AGE_TICKS_TAG, ageTicks);
	}

	private static boolean hasExplicitAgeTicks(FluidStack fluid) {
		return fluid != null && fluid.tag != null && fluid.tag.hasKey(AGE_TICKS_TAG, 3);
	}

	private static AgeProfile getAgeProfile(FluidStack fluid) {
		if (fluid == null || fluid.getFluid() == null) {
			return null;
		}

		AgeProfile profile = AGE_PROFILES.get(fluid.getFluid());
		return profile != null ? profile : PotStillDistillingRegistry.isSpiritFluid(fluid.getFluid())
			? SPIRIT_PROFILE : null;
	}

	public static int getAgedFluidColor(FluidStack fluid, int freshColor) {
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
		private final List<IngredientRequirement> requirements;

		FoudreBrewingRecipe(String id, String displayName, Fluid output, int brewTime,
				List<IngredientRequirement> requirements) {
			this.id = id;
			this.displayName = displayName;
			this.output = output;
			this.brewTime = brewTime;
			this.requirements = requirements;
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
			return IronAgeFurnitureConfiguration.scaleDrinkTicks(this.brewTime);
		}

		public boolean matches(ItemStack[] inventory) {
			for (IngredientRequirement requirement : this.requirements) {
				if (this.countMatchingItems(inventory, requirement) < requirement.count) {
					return false;
				}
			}

			return true;
		}

		public void consumeIngredients(ItemStack[] inventory) {
			for (IngredientRequirement requirement : this.requirements) {
				this.consumeIngredient(inventory, requirement);
			}
		}

		public boolean matchesIngredient(ItemStack stack) {
			for (IngredientRequirement requirement : this.requirements) {
				if (requirement.matches(stack)) {
					return true;
				}
			}

			return false;
		}

		private void consumeIngredient(ItemStack[] inventory, IngredientRequirement requirement) {
			int remaining = requirement.count;

			for (int i = 0; i < inventory.length && remaining > 0; i++) {
				ItemStack stack = inventory[i];

				if (!requirement.matches(stack)) {
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

		private int countMatchingItems(ItemStack[] inventory, IngredientRequirement requirement) {
			int count = 0;

			for (ItemStack stack : inventory) {
				if (requirement.matches(stack)) {
					count += stack.stackSize;
				}
			}

			return count;
		}

		private int getRequirementCount() {
			return this.requirements.size();
		}

		private int getRequiredIngredientCount() {
			int count = 0;

			for (IngredientRequirement requirement : this.requirements) {
				count += requirement.count;
			}

			return count;
		}
	}

	private static final class IngredientRequirement {
		private final ItemStack ingredient;
		private final int count;

		IngredientRequirement(ItemStack ingredient, int count) {
			this.ingredient = ingredient;
			this.count = count;
		}

		private boolean isValid() {
			return this.ingredient != null && this.ingredient.getItem() != null && this.count > 0;
		}

		private boolean matches(ItemStack stack) {
			return stack != null && stack.stackSize > 0 && this.isValid()
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

			return Math.max(0, ageTicks - this.getThreshold(level));
		}

		int getProgressTotal(int ageTicks) {
			int level = this.getLevel(ageTicks);

			if (level >= this.thresholds.length - 1) {
				return 0;
			}

			return this.getThreshold(level + 1) - this.getThreshold(level);
		}

		int getLevel(int ageTicks) {
			int level = 0;

			for (int i = 1; i < this.thresholds.length; i++) {
				if (ageTicks < this.getThreshold(i)) {
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
			return this.getThreshold(this.thresholds.length - 1);
		}

		int getThreshold(int level) {
			int clampedLevel = Math.max(0, Math.min(this.thresholds.length - 1, level));
			return IronAgeFurnitureConfiguration.scaleDrinkTicks(this.thresholds[clampedLevel]);
		}
	}
}
