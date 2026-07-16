package com.mcmoddev.ironagefurniture.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

import com.mcmoddev.ironagefurniture.IronAgeFurnitureConfiguration;

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
	public static final int MAX_DISTILLATION_PASSES = 3;

	private static final ResourceLocation SPIRIT_TEXTURE = new ResourceLocation("minecraft", "blocks/quartz_block_side");
	private static final ResourceLocation STILL = SPIRIT_TEXTURE;
	private static final ResourceLocation FLOWING = SPIRIT_TEXTURE;
	private static final int DAY = 20 * 60 * 20;
	private static final int MIN_DISTILL_TIME = 6000;
	private static final int MINIMUM_REDISTILLED_OUTPUT = Fluid.BUCKET_VOLUME / 4;
	private static final int FIRST_PASS_RATIO = 6;
	private static final int DOUBLE_DISTILL_RATIO = 4;
	private static final int TRIPLE_DISTILL_RATIO = 2;
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
	private static final int VODKA_COLOR = 0xFFEDEAE0;
	private static final int RUM_COLOR = 0xFFD0832E;
	private static final int SPICED_RUM_COLOR = 0xFFB66A2D;
	private static final int COCONUT_RUM_COLOR = 0xFFE6D6A0;
	private static final int PINEAPPLE_RUM_COLOR = 0xFFE0AA38;
	private static final int VANILLA_RUM_COLOR = 0xFFD6A35A;
	private static final int CITRUS_RUM_COLOR = 0xFFD78A2F;
	private static final int MAPLE_RUM_COLOR = 0xFF9F5A27;
	private static final int GINGER_RUM_COLOR = 0xFFC77A30;
	private static final int CORN_WHISKEY_COLOR = 0xFFD29435;
	private static final int GIN_COLOR = 0xFFE1E7CC;
	private static final int CHERRY_BRANDY_COLOR = 0xFF9A2444;
	private static final int PLUM_BRANDY_COLOR = 0xFF6B3460;
	private static final int APRICOT_BRANDY_COLOR = 0xFFE69A58;
	private static final int MANGO_BRANDY_COLOR = 0xFFE3A13A;
	private static final int PINEAPPLE_BRANDY_COLOR = 0xFFE4C348;
	private static final int BANANA_BRANDY_COLOR = 0xFFE2D07A;
	private static final int DATE_BRANDY_COLOR = 0xFF9A5A32;
	private static final int FIG_BRANDY_COLOR = 0xFF7A3F62;
	private static final int GRAPEFRUIT_BRANDY_COLOR = 0xFFE27B56;
	private static final int POMEGRANATE_BRANDY_COLOR = 0xFF9B1D3D;
	private static final int PAPAYA_BRANDY_COLOR = 0xFFE48642;
	private static final int STARFRUIT_BRANDY_COLOR = 0xFFE3C75A;
	private static final int GOOSEBERRY_BRANDY_COLOR = 0xFF97B957;
	private static final int CACTUS_FRUIT_BRANDY_COLOR = 0xFFC55370;
	private static final int MALOBERRY_BRANDY_COLOR = 0xFFD08C35;
	private static final int BLIGHTBERRY_BRANDY_COLOR = 0xFF66762D;
	private static final int DUSKBERRY_BRANDY_COLOR = 0xFF443680;
	private static final int SKYBERRY_BRANDY_COLOR = 0xFF5B8EC2;
	private static final int STINGBERRY_BRANDY_COLOR = 0xFFC43F38;
	private static final int PASS_DARKEN_STEP = 22;
	private static final int COLOR_WHITE = 0xFFFFFFFF;
	private static final float UNAGED_SPIRIT_LIGHTENING = 0.18F;

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
	public static Fluid vodka;
	public static Fluid rum;
	public static Fluid spicedRum;
	public static Fluid coconutRum;
	public static Fluid pineappleRum;
	public static Fluid vanillaRum;
	public static Fluid citrusRum;
	public static Fluid mapleRum;
	public static Fluid gingerRum;
	public static Fluid cornWhiskey;
	public static Fluid gin;
	public static Fluid cherryBrandy;
	public static Fluid plumBrandy;
	public static Fluid apricotBrandy;
	public static Fluid mangoBrandy;
	public static Fluid pineappleBrandy;
	public static Fluid bananaBrandy;
	public static Fluid dateBrandy;
	public static Fluid figBrandy;
	public static Fluid grapefruitBrandy;
	public static Fluid pomegranateBrandy;
	public static Fluid papayaBrandy;
	public static Fluid starfruitBrandy;
	public static Fluid gooseberryBrandy;
	public static Fluid cactusFruitBrandy;
	public static Fluid maloberryBrandy;
	public static Fluid blightberryBrandy;
	public static Fluid duskberryBrandy;
	public static Fluid skyberryBrandy;
	public static Fluid stingberryBrandy;

	private static final Map<Fluid, DistillationRecipe> FIRST_PASS_RECIPES =
		new LinkedHashMap<Fluid, DistillationRecipe>();
	private static final Map<Fluid, SpiritProfile> SPIRIT_PROFILES = new LinkedHashMap<Fluid, SpiritProfile>();
	private static final Map<String, Fluid> EXPANDED_SPIRITS = new LinkedHashMap<String, Fluid>();

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
		vodka = registerSpirit("ironagefurniture_vodka", "vodka", "Vodka", VODKA_COLOR);
		rum = registerSpirit("ironagefurniture_rum", "rum", "Rum", RUM_COLOR);
		spicedRum = registerSpirit("ironagefurniture_spiced_rum", "spiced_rum", "Spiced Rum",
			SPICED_RUM_COLOR);
		coconutRum = registerSpirit("ironagefurniture_coconut_rum", "coconut_rum", "Coconut Rum",
			COCONUT_RUM_COLOR);
		pineappleRum = registerSpirit("ironagefurniture_pineapple_rum", "pineapple_rum", "Pineapple Rum",
			PINEAPPLE_RUM_COLOR);
		vanillaRum = registerSpirit("ironagefurniture_vanilla_rum", "vanilla_rum", "Vanilla Rum",
			VANILLA_RUM_COLOR);
		citrusRum = registerSpirit("ironagefurniture_citrus_rum", "citrus_rum", "Citrus Rum",
			CITRUS_RUM_COLOR);
		mapleRum = registerSpirit("ironagefurniture_maple_rum", "maple_rum", "Maple Rum",
			MAPLE_RUM_COLOR);
		gingerRum = registerSpirit("ironagefurniture_ginger_rum", "ginger_rum", "Ginger Rum",
			GINGER_RUM_COLOR);
		cornWhiskey = registerSpirit("ironagefurniture_corn_whiskey", "corn_whiskey", "Corn Whiskey",
			CORN_WHISKEY_COLOR);
		gin = registerSpirit("ironagefurniture_gin", "gin", "Gin", GIN_COLOR);
		cherryBrandy = registerSpirit("ironagefurniture_cherry_brandy", "cherry_brandy", "Kirsch",
			CHERRY_BRANDY_COLOR);
		plumBrandy = registerSpirit("ironagefurniture_plum_brandy", "plum_brandy", "Slivovitz",
			PLUM_BRANDY_COLOR);
		apricotBrandy = registerSpirit("ironagefurniture_apricot_brandy", "apricot_brandy", "Apricot Brandy",
			APRICOT_BRANDY_COLOR);
		mangoBrandy = registerSpirit("ironagefurniture_mango_brandy", "mango_brandy", "Mango Brandy",
			MANGO_BRANDY_COLOR);
		pineappleBrandy = registerSpirit("ironagefurniture_pineapple_brandy", "pineapple_brandy",
			"Pineapple Brandy", PINEAPPLE_BRANDY_COLOR);
		bananaBrandy = registerSpirit("ironagefurniture_banana_brandy", "banana_brandy", "Banana Brandy",
			BANANA_BRANDY_COLOR);
		dateBrandy = registerSpirit("ironagefurniture_date_brandy", "date_brandy", "Date Brandy",
			DATE_BRANDY_COLOR);
		figBrandy = registerSpirit("ironagefurniture_fig_brandy", "fig_brandy", "Fig Brandy",
			FIG_BRANDY_COLOR);
		grapefruitBrandy = registerSpirit("ironagefurniture_grapefruit_brandy", "grapefruit_brandy",
			"Grapefruit Brandy", GRAPEFRUIT_BRANDY_COLOR);
		pomegranateBrandy = registerSpirit("ironagefurniture_pomegranate_brandy", "pomegranate_brandy",
			"Pomegranate Brandy", POMEGRANATE_BRANDY_COLOR);
		papayaBrandy = registerSpirit("ironagefurniture_papaya_brandy", "papaya_brandy", "Papaya Brandy",
			PAPAYA_BRANDY_COLOR);
		starfruitBrandy = registerSpirit("ironagefurniture_starfruit_brandy", "starfruit_brandy",
			"Starfruit Brandy", STARFRUIT_BRANDY_COLOR);
		gooseberryBrandy = registerSpirit("ironagefurniture_gooseberry_brandy", "gooseberry_brandy",
			"Gooseberry Brandy", GOOSEBERRY_BRANDY_COLOR);
		cactusFruitBrandy = registerSpirit("ironagefurniture_cactus_fruit_brandy", "cactus_fruit_brandy",
			"Cactus Fruit Brandy", CACTUS_FRUIT_BRANDY_COLOR);
		maloberryBrandy = registerSpirit("ironagefurniture_maloberry_brandy", "maloberry_brandy",
			"Maloberry Brandy", MALOBERRY_BRANDY_COLOR);
		blightberryBrandy = registerSpirit("ironagefurniture_blightberry_brandy", "blightberry_brandy",
			"Blightberry Brandy", BLIGHTBERRY_BRANDY_COLOR);
		duskberryBrandy = registerSpirit("ironagefurniture_duskberry_brandy", "duskberry_brandy",
			"Duskberry Brandy", DUSKBERRY_BRANDY_COLOR);
		skyberryBrandy = registerSpirit("ironagefurniture_skyberry_brandy", "skyberry_brandy",
			"Skyberry Brandy", SKYBERRY_BRANDY_COLOR);
		stingberryBrandy = registerSpirit("ironagefurniture_stingberry_brandy", "stingberry_brandy",
			"Stingberry Brandy", STINGBERRY_BRANDY_COLOR);
		registerExpandedSpirits();
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
		addFirstPass(FoudreBrewingRegistry.potatoWash, vodka, "Vodka");
		addFirstPass(FoudreBrewingRegistry.sugarWash, rum, "Rum");
		addFirstPass(FoudreBrewingRegistry.cornMash, cornWhiskey, "Corn Whiskey");
		addFirstPass(FoudreBrewingRegistry.ginMash, gin, "Gin");
		addFirstPass(FoudreBrewingRegistry.spicedRumWash, spicedRum, "Spiced Rum");
		addFirstPass(FoudreBrewingRegistry.coconutRumWash, coconutRum, "Coconut Rum");
		addFirstPass(FoudreBrewingRegistry.pineappleRumWash, pineappleRum, "Pineapple Rum");
		addFirstPass(FoudreBrewingRegistry.vanillaRumWash, vanillaRum, "Vanilla Rum");
		addFirstPass(FoudreBrewingRegistry.citrusRumWash, citrusRum, "Citrus Rum");
		addFirstPass(FoudreBrewingRegistry.mapleRumWash, mapleRum, "Maple Rum");
		addFirstPass(FoudreBrewingRegistry.gingerRumWash, gingerRum, "Ginger Rum");
		addFirstPass(FoudreBrewingRegistry.cherryWine, cherryBrandy, "Kirsch");
		addFirstPass(FoudreBrewingRegistry.plumWine, plumBrandy, "Slivovitz");
		addFirstPass(FoudreBrewingRegistry.apricotWine, apricotBrandy, "Apricot Brandy");
		addFirstPass(FoudreBrewingRegistry.mangoWine, mangoBrandy, "Mango Brandy");
		addFirstPass(FoudreBrewingRegistry.pineappleWine, pineappleBrandy, "Pineapple Brandy");
		addFirstPass(FoudreBrewingRegistry.bananaWine, bananaBrandy, "Banana Brandy");
		addFirstPass(FoudreBrewingRegistry.dateWine, dateBrandy, "Date Brandy");
		addFirstPass(FoudreBrewingRegistry.figWine, figBrandy, "Fig Brandy");
		addFirstPass(FoudreBrewingRegistry.grapefruitWine, grapefruitBrandy, "Grapefruit Brandy");
		addFirstPass(FoudreBrewingRegistry.pomegranateWine, pomegranateBrandy, "Pomegranate Brandy");
		addFirstPass(FoudreBrewingRegistry.papayaWine, papayaBrandy, "Papaya Brandy");
		addFirstPass(FoudreBrewingRegistry.starfruitWine, starfruitBrandy, "Starfruit Brandy");
		addFirstPass(FoudreBrewingRegistry.gooseberryWine, gooseberryBrandy, "Gooseberry Brandy");
		addFirstPass(FoudreBrewingRegistry.cactusFruitWine, cactusFruitBrandy, "Cactus Fruit Brandy");
		addFirstPass(FoudreBrewingRegistry.maloberryWine, maloberryBrandy, "Maloberry Brandy");
		addFirstPass(FoudreBrewingRegistry.blightberryWine, blightberryBrandy, "Blightberry Brandy");
		addFirstPass(FoudreBrewingRegistry.duskberryWine, duskberryBrandy, "Duskberry Brandy");
		addFirstPass(FoudreBrewingRegistry.skyberryWine, skyberryBrandy, "Skyberry Brandy");
		addFirstPass(FoudreBrewingRegistry.stingberryWine, stingberryBrandy, "Stingberry Brandy");
		addExpandedFirstPass(FoudreBrewingRegistry.orangeWine, "orange_brandy");
		addExpandedFirstPass(FoudreBrewingRegistry.lemonWine, "lemon_brandy");
		addExpandedFirstPass(FoudreBrewingRegistry.limeWine, "lime_brandy");
		addExpandedFirstPass(FoudreBrewingRegistry.kiwiWine, "kiwi_brandy");
		addExpandedFirstPass(FoudreBrewingRegistry.dragonfruitWine, "dragonfruit_brandy");
		addExpandedFirstPass(FoudreBrewingRegistry.durianWine, "durian_brandy");
		addExpandedFirstPass(FoudreBrewingRegistry.rhubarbWine, "rhubarb_brandy");
		addExpandedFirstPass(FoudreBrewingRegistry.melonWine, "melon_brandy");
		addExpandedFirstPass(FoudreBrewingRegistry.cantaloupeWine, "cantaloupe_brandy");
		addExpandedFirstPass(FoudreBrewingRegistry.carrotWine, "carrot_brandy");
		addExpandedFirstPass(FoudreBrewingRegistry.parsnipWine, "parsnip_brandy");
		addExpandedFirstPass(FoudreBrewingRegistry.ryeBeer, "rye_whisky");
		addExpandedFirstPass(FoudreBrewingRegistry.oatmealStout, "oat_whisky");
		addExpandedFirstPass(FoudreBrewingRegistry.coffeeStout, "coffee_whisky");
		addExpandedFirstPass(FoudreBrewingRegistry.chocolateStout, "chocolate_whisky");
		addExpandedFirstPass(FoudreBrewingRegistry.pumpkinAle, "pumpkin_whisky");
		addExpandedFirstPass(FoudreBrewingRegistry.chiliBeer, "chili_whisky");
		addExpandedFirstPass(FoudreBrewingRegistry.bananaBeer, "banana_spirit");
		addExpandedFirstPass(FoudreBrewingRegistry.cyser, "apple_honey_spirit");
		addExpandedFirstPass(FoudreBrewingRegistry.pyment, "grape_honey_spirit");
		addExpandedFirstPass(FoudreBrewingRegistry.braggot, "malted_honey_spirit");
		addExpandedFirstPass(FoudreBrewingRegistry.metheglin, "spiced_honey_spirit");
		addExpandedFirstPass(FoudreBrewingRegistry.beetKvass, "beet_spirit");
		addExpandedFirstPass(FoudreBrewingRegistry.tepache, "tepache_spirit");
		addExpandedFirstPass(FoudreBrewingRegistry.turnipWash, "turnip_spirit");
		addExpandedFirstPass(FoudreBrewingRegistry.rutabagaWash, "rutabaga_spirit");
		addExpandedFirstPass(FoudreBrewingRegistry.caneWash, "cachaca");
		addExpandedFirstPass(FoudreBrewingRegistry.orchardMash, "obstler");
		addExpandedFirstPass(FoudreBrewingRegistry.chorusWine, "chorus_spirit");
		addExpandedFirstPass(FoudreBrewingRegistry.potashCider, "potash_brandy");
		for (Map.Entry<String, Fluid> entry : FoudreBrewingRegistry.getMelomelFluids().entrySet()) {
			addExpandedFirstPass(entry.getValue(), entry.getKey() + "_honey_spirit");
		}
	}

	private static void registerExpandedSpirits() {
		EXPANDED_SPIRITS.clear();
		registerExpandedSpirit("orange_brandy", "Orange Brandy", FoudreBrewingRegistry.orangeWine);
		registerExpandedSpirit("lemon_brandy", "Lemon Brandy", FoudreBrewingRegistry.lemonWine);
		registerExpandedSpirit("lime_brandy", "Lime Brandy", FoudreBrewingRegistry.limeWine);
		registerExpandedSpirit("kiwi_brandy", "Kiwi Brandy", FoudreBrewingRegistry.kiwiWine);
		registerExpandedSpirit("dragonfruit_brandy", "Dragonfruit Brandy", FoudreBrewingRegistry.dragonfruitWine);
		registerExpandedSpirit("durian_brandy", "Durian Brandy", FoudreBrewingRegistry.durianWine);
		registerExpandedSpirit("rhubarb_brandy", "Rhubarb Brandy", FoudreBrewingRegistry.rhubarbWine);
		registerExpandedSpirit("melon_brandy", "Melon Brandy", FoudreBrewingRegistry.melonWine);
		registerExpandedSpirit("cantaloupe_brandy", "Cantaloupe Brandy", FoudreBrewingRegistry.cantaloupeWine);
		registerExpandedSpirit("carrot_brandy", "Carrot Brandy", FoudreBrewingRegistry.carrotWine);
		registerExpandedSpirit("parsnip_brandy", "Parsnip Brandy", FoudreBrewingRegistry.parsnipWine);
		registerExpandedSpirit("rye_whisky", "Rye Whisky", FoudreBrewingRegistry.ryeBeer);
		registerExpandedSpirit("oat_whisky", "Oat Whisky", FoudreBrewingRegistry.oatmealStout);
		registerExpandedSpirit("coffee_whisky", "Coffee Whisky", FoudreBrewingRegistry.coffeeStout);
		registerExpandedSpirit("chocolate_whisky", "Chocolate Whisky", FoudreBrewingRegistry.chocolateStout);
		registerExpandedSpirit("pumpkin_whisky", "Pumpkin Whisky", FoudreBrewingRegistry.pumpkinAle);
		registerExpandedSpirit("chili_whisky", "Chili Whisky", FoudreBrewingRegistry.chiliBeer);
		registerExpandedSpirit("banana_spirit", "Banana Spirit", FoudreBrewingRegistry.bananaBeer);
		registerExpandedSpirit("apple_honey_spirit", "Apple Honey Spirit", FoudreBrewingRegistry.cyser);
		registerExpandedSpirit("grape_honey_spirit", "Grape Honey Spirit", FoudreBrewingRegistry.pyment);
		registerExpandedSpirit("malted_honey_spirit", "Malted Honey Spirit", FoudreBrewingRegistry.braggot);
		registerExpandedSpirit("spiced_honey_spirit", "Spiced Honey Spirit", FoudreBrewingRegistry.metheglin);
		registerExpandedSpirit("beet_spirit", "Beet Spirit", FoudreBrewingRegistry.beetKvass);
		registerExpandedSpirit("tepache_spirit", "Tepache Spirit", FoudreBrewingRegistry.tepache);
		registerExpandedSpirit("turnip_spirit", "Turnip Spirit", FoudreBrewingRegistry.turnipWash);
		registerExpandedSpirit("rutabaga_spirit", "Rutabaga Spirit", FoudreBrewingRegistry.rutabagaWash);
		registerExpandedSpirit("cachaca", "Cachaça", FoudreBrewingRegistry.caneWash);
		registerExpandedSpirit("obstler", "Obstler", FoudreBrewingRegistry.orchardMash);
		registerExpandedSpirit("chorus_spirit", "Chorus Spirit", FoudreBrewingRegistry.chorusWine);
		registerExpandedSpirit("potash_brandy", "Potash Brandy", FoudreBrewingRegistry.potashCider);

		for (Map.Entry<String, Fluid> entry : FoudreBrewingRegistry.getMelomelFluids().entrySet()) {
			String spiritName = FoudreBrewingRegistry.getMelomelSpiritName(entry.getKey());
			registerExpandedSpirit(entry.getKey() + "_honey_spirit", spiritName, entry.getValue());
		}
	}

	private static void registerExpandedSpirit(String key, String displayName, Fluid source) {
		if (source == null || displayName == null) {
			return;
		}
		int color = blendColor(source.getColor(), COLOR_WHITE, UNAGED_SPIRIT_LIGHTENING);
		EXPANDED_SPIRITS.put(key,
			registerSpirit("ironagefurniture_" + key, key, displayName, color));
	}

	private static void addExpandedFirstPass(Fluid input, String spiritKey) {
		Fluid output = EXPANDED_SPIRITS.get(spiritKey);
		SpiritProfile profile = SPIRIT_PROFILES.get(output);
		if (profile != null) {
			addFirstPass(input, output, profile.displayName);
		}
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

	public static List<DistillationJeiRecipe> getJeiRecipes() {
		List<DistillationJeiRecipe> recipes = new ArrayList<DistillationJeiRecipe>();

		for (Map.Entry<Fluid, DistillationRecipe> entry : FIRST_PASS_RECIPES.entrySet()) {
			DistillationRecipe recipe = entry.getValue();
			int outputAmount = FIRST_PASS_MIN_INPUT / FIRST_PASS_RATIO;
			FluidStack input = new FluidStack(entry.getKey(), FIRST_PASS_MIN_INPUT);
			// An absent pass tag is the canonical representation of a single-distilled spirit.
			// Keeping the JEI output untagged lets the searchable fluid match this recipe.
			FluidStack output = new FluidStack(recipe.output, outputAmount);
			recipes.add(new DistillationJeiRecipe(input, output, recipe.outputName, FIRST_PASS_RATIO,
				FIRST_PASS_MIN_INPUT, getDistillationTime(FIRST_PASS_MIN_INPUT),
				FoudreBrewingRegistry.isAgeable(input)));
		}

		for (Map.Entry<Fluid, SpiritProfile> entry : SPIRIT_PROFILES.entrySet()) {
			addRedistillationJeiRecipe(recipes, entry.getKey(), entry.getValue().displayName, 1);
			addRedistillationJeiRecipe(recipes, entry.getKey(), entry.getValue().displayName, 2);
		}

		return Collections.unmodifiableList(recipes);
	}

	public static Set<Fluid> getRegisteredSpirits() {
		return Collections.unmodifiableSet(new LinkedHashSet<Fluid>(SPIRIT_PROFILES.keySet()));
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

		if (passes >= MAX_DISTILLATION_PASSES || input.amount < getRedistillationMinInput(passes)) {
			return null;
		}

		int outputAmount = input.amount / getRedistillationRatio(passes);

		if (outputAmount <= 0 || outputAmount > outputSpace) {
			return null;
		}

		SpiritProfile profile = SPIRIT_PROFILES.get(input.getFluid());
		String outputName = profile == null ? input.getLocalizedName() : profile.displayName;
		return new DistillationResult(input.getFluid(), outputName, outputAmount,
			getDistillationTime(input.amount), passes + 1);
	}

	private static int getDistillationTime(int inputAmount) {
		long scaledInputTime = (long)inputAmount * (long)DAY / (long)INPUT_CAPACITY;
		return IronAgeFurnitureConfiguration.scaleDrinkTicks(Math.max((long)MIN_DISTILL_TIME, scaledInputTime));
	}

	private static int getRedistillationRatio(int passes) {
		return passes <= 1 ? DOUBLE_DISTILL_RATIO : TRIPLE_DISTILL_RATIO;
	}

	private static int getRedistillationMinInput(int passes) {
		return getRedistillationRatio(passes) * MINIMUM_REDISTILLED_OUTPUT;
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

	private static void addRedistillationJeiRecipe(List<DistillationJeiRecipe> recipes, Fluid spirit,
			String spiritName, int inputPasses) {
		int ratio = getRedistillationRatio(inputPasses);
		int outputAmount = Fluid.BUCKET_VOLUME;
		int inputAmount = outputAmount * ratio;
		FluidStack input = createSpiritFluid(spirit, inputAmount, inputPasses);
		FluidStack output = createSpiritFluid(spirit, outputAmount, inputPasses + 1);
		String outputName = inputPasses == 1 ? "Double Distilled " + spiritName
			: "Triple Distilled " + spiritName;
		recipes.add(new DistillationJeiRecipe(input, output, outputName, ratio,
			getRedistillationMinInput(inputPasses), getDistillationTime(inputAmount), true));
	}

	private static int blendColor(int first, int second, float secondWeight) {
		float clampedWeight = Math.max(0.0F, Math.min(1.0F, secondWeight));
		float firstWeight = 1.0F - clampedWeight;
		int alpha = COLOR_ALPHA_MASK;
		int red = Math.round((first >> COLOR_RED_SHIFT & COLOR_CHANNEL_MASK) * firstWeight
			+ (second >> COLOR_RED_SHIFT & COLOR_CHANNEL_MASK) * clampedWeight);
		int green = Math.round((first >> COLOR_GREEN_SHIFT & COLOR_CHANNEL_MASK) * firstWeight
			+ (second >> COLOR_GREEN_SHIFT & COLOR_CHANNEL_MASK) * clampedWeight);
		int blue = Math.round((first & COLOR_CHANNEL_MASK) * firstWeight
			+ (second & COLOR_CHANNEL_MASK) * clampedWeight);
		return alpha | red << COLOR_RED_SHIFT | green << COLOR_GREEN_SHIFT | blue;
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

	public static final class DistillationJeiRecipe {
		private final FluidStack input;
		private final FluidStack output;
		private final String outputName;
		private final int ratio;
		private final int minimumInput;
		private final int distillationTime;
		private final boolean acceptsAnyAge;

		private DistillationJeiRecipe(FluidStack input, FluidStack output, String outputName, int ratio,
				int minimumInput, int distillationTime, boolean acceptsAnyAge) {
			this.input = input;
			this.output = output;
			this.outputName = outputName;
			this.ratio = ratio;
			this.minimumInput = minimumInput;
			this.distillationTime = distillationTime;
			this.acceptsAnyAge = acceptsAnyAge;
		}

		public FluidStack getInput() {
			return this.input.copy();
		}

		public FluidStack getOutput() {
			return this.output.copy();
		}

		public String getOutputName() {
			return this.outputName;
		}

		public int getRatio() {
			return this.ratio;
		}

		public int getMinimumInput() {
			return this.minimumInput;
		}

		public int getDistillationTime() {
			return this.distillationTime;
		}

		public boolean acceptsAnyAge() {
			return this.acceptsAnyAge;
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
