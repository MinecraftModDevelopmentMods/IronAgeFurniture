package zone.moddev.mc.ironagefurniture.api;

import java.util.ArrayList;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import javax.annotation.Nullable;

import zone.moddev.mc.ironagefurniture.IronAgeFurnitureConfiguration;

import net.minecraft.init.Blocks;
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
	public static final String INFUSION_BASE_TAG = "InfusionBase";
	private static final int DAY = 20 * 60 * 20;
	private static final int ALE_BREW_TIME = DAY;
	private static final int CIDER_BREW_TIME = DAY + DAY / 2;
	private static final int WINE_BREW_TIME = DAY * 2;
	private static final int MEAD_BREW_TIME = DAY * 2;
	private static final int WASH_BREW_TIME = DAY + DAY / 2;
	private static final int INFUSION_TIME = DAY * 2;
	private static final int CREAM_BLEND_TIME = DAY;
	private static final int MIN_INFUSION_AMOUNT = Fluid.BUCKET_VOLUME / 4;
	private static final int JEI_INFUSION_AMOUNT = 16 * Fluid.BUCKET_VOLUME;
	private static final int FULL_FOUDRE_AMOUNT = 128 * Fluid.BUCKET_VOLUME;
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
	private static final int ORANGE_WINE_COLOR_FRESH = 0xFFE58A32;
	private static final int LEMON_WINE_COLOR_FRESH = 0xFFE5D75A;
	private static final int LIME_WINE_COLOR_FRESH = 0xFF9EBB43;
	private static final int KIWI_WINE_COLOR_FRESH = 0xFF769B3E;
	private static final int DRAGONFRUIT_WINE_COLOR_FRESH = 0xFFC33F75;
	private static final int DURIAN_WINE_COLOR_FRESH = 0xFFC9B35A;
	private static final int RHUBARB_WINE_COLOR_FRESH = 0xFFB83E52;
	private static final int MELON_WINE_COLOR_FRESH = 0xFFCF5A58;
	private static final int CANTALOUPE_WINE_COLOR_FRESH = 0xFFE7A15B;
	private static final int RYE_BEER_COLOR_FRESH = 0xFFB87832;
	private static final int OATMEAL_STOUT_COLOR_FRESH = 0xFF553421;
	private static final int COFFEE_STOUT_COLOR_FRESH = 0xFF493025;
	private static final int CHOCOLATE_STOUT_COLOR_FRESH = 0xFF4C2B21;
	private static final int PUMPKIN_ALE_COLOR_FRESH = 0xFFC56B27;
	private static final int CHILI_BEER_COLOR_FRESH = 0xFFB54C27;
	private static final int BANANA_BEER_COLOR_FRESH = 0xFFD3AD50;
	private static final int BEET_KVASS_COLOR_FRESH = 0xFF79233A;
	private static final int TEPACHE_COLOR_FRESH = 0xFFD99A35;
	private static final int CARROT_WINE_COLOR_FRESH = 0xFFE08432;
	private static final int PARSNIP_WINE_COLOR_FRESH = 0xFFD8C79A;
	private static final int TURNIP_WASH_COLOR = 0xFFC9C29B;
	private static final int RUTABAGA_WASH_COLOR = 0xFFC6A15C;
	private static final int CANE_WASH_COLOR = 0xFFD8C76B;
	private static final int ORCHARD_MASH_COLOR = 0xFFD1A94E;
	private static final int CHORUS_WINE_COLOR_FRESH = 0xFF9A6BB8;
	private static final int POTASH_CIDER_COLOR_FRESH = 0xFFB5A36A;
	private static final int METHEGLIN_COLOR_FRESH = 0xFFC89B3F;
	private static final float MELOMEL_HONEY_BLEND = 0.45F;
	private static final float LIQUEUR_SPIRIT_BLEND = 0.35F;
	private static final float CREAM_BLEND = 0.68F;
	private static final int MILK_COLOR = 0xFFF4EFE1;
	private static final int PEPPERMINT_COLOR = 0xFFA9D49A;
	private static final int COFFEE_LIQUEUR_COLOR = 0xFF6A4028;
	private static final int CHOCOLATE_LIQUEUR_COLOR = 0xFF63382B;
	private static final int COCONUT_LIQUEUR_COLOR = 0xFFE5D5AE;
	private static final int VANILLA_LIQUEUR_COLOR = 0xFFD8B56B;
	private static final int AMARETTO_COLOR = 0xFFB86C2E;
	private static final int NOCINO_COLOR = 0xFF4D3327;
	private static final int GINGER_LIQUEUR_COLOR = 0xFFD99A43;
	private static final int HONEY_WHISKY_COLOR = 0xFFD79A38;
	private static final int MAPLE_WHISKY_COLOR = 0xFFA7612C;
	private static final int TEA_LIQUEUR_COLOR = 0xFF8B7A42;
	private static final int EGGNOG_COLOR = 0xFFF0D99B;

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
	public static Fluid orangeWine;
	public static Fluid lemonWine;
	public static Fluid limeWine;
	public static Fluid kiwiWine;
	public static Fluid dragonfruitWine;
	public static Fluid durianWine;
	public static Fluid rhubarbWine;
	public static Fluid melonWine;
	public static Fluid cantaloupeWine;
	public static Fluid ryeBeer;
	public static Fluid oatmealStout;
	public static Fluid coffeeStout;
	public static Fluid chocolateStout;
	public static Fluid pumpkinAle;
	public static Fluid chiliBeer;
	public static Fluid bananaBeer;
	public static Fluid cyser;
	public static Fluid pyment;
	public static Fluid braggot;
	public static Fluid metheglin;
	public static Fluid beetKvass;
	public static Fluid tepache;
	public static Fluid carrotWine;
	public static Fluid parsnipWine;
	public static Fluid turnipWash;
	public static Fluid rutabagaWash;
	public static Fluid caneWash;
	public static Fluid orchardMash;
	public static Fluid chorusWine;
	public static Fluid potashCider;
	public static Fluid peppermintSchnapps;
	public static Fluid coffeeLiqueur;
	public static Fluid chocolateLiqueur;
	public static Fluid coconutLiqueur;
	public static Fluid vanillaLiqueur;
	public static Fluid amaretto;
	public static Fluid nocino;
	public static Fluid gingerLiqueur;
	public static Fluid honeyWhiskyLiqueur;
	public static Fluid mapleWhiskyLiqueur;
	public static Fluid teaLiqueur;
	public static Fluid whiskyCreamLiqueur;
	public static Fluid eggnog;

	private static final List<FoudreBrewingRecipe> RECIPES = new ArrayList<FoudreBrewingRecipe>();
	private static final Set<Fluid> REGISTERED_FLUIDS = new LinkedHashSet<Fluid>();
	private static final Map<Fluid, AgeProfile> AGE_PROFILES = new HashMap<Fluid, AgeProfile>();
	private static final Set<Fluid> DISTILLATION_BASES = new HashSet<Fluid>();
	private static final Map<String, Fluid> MELOMEL_FLUIDS = new LinkedHashMap<String, Fluid>();
	private static final Map<String, String> MELOMEL_DISPLAY_NAMES = new LinkedHashMap<String, String>();
	private static final Map<String, String> MELOMEL_SPIRIT_NAMES = new LinkedHashMap<String, String>();
	private static final Map<String, Fluid> LIQUEUR_FLUIDS = new LinkedHashMap<String, Fluid>();
	private static final Map<String, String> LIQUEUR_DISPLAY_NAMES = new LinkedHashMap<String, String>();
	private static final Map<String, Integer> LIQUEUR_COLORS = new LinkedHashMap<String, Integer>();
	private static final Map<String, Fluid> CREAM_LIQUEUR_FLUIDS = new LinkedHashMap<String, Fluid>();
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
		REGISTERED_FLUIDS.clear();
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
		orangeWine = registerFluid("ironagefurniture_orange_wine", "orange_wine", ORANGE_WINE_COLOR_FRESH);
		lemonWine = registerFluid("ironagefurniture_lemon_wine", "lemon_wine", LEMON_WINE_COLOR_FRESH);
		limeWine = registerFluid("ironagefurniture_lime_wine", "lime_wine", LIME_WINE_COLOR_FRESH);
		kiwiWine = registerFluid("ironagefurniture_kiwi_wine", "kiwi_wine", KIWI_WINE_COLOR_FRESH);
		dragonfruitWine = registerFluid("ironagefurniture_dragonfruit_wine", "dragonfruit_wine",
			DRAGONFRUIT_WINE_COLOR_FRESH);
		durianWine = registerFluid("ironagefurniture_durian_wine", "durian_wine", DURIAN_WINE_COLOR_FRESH);
		rhubarbWine = registerFluid("ironagefurniture_rhubarb_wine", "rhubarb_wine", RHUBARB_WINE_COLOR_FRESH);
		melonWine = registerFluid("ironagefurniture_melon_wine", "melon_wine", MELON_WINE_COLOR_FRESH);
		cantaloupeWine = registerFluid("ironagefurniture_cantaloupe_wine", "cantaloupe_wine",
			CANTALOUPE_WINE_COLOR_FRESH);
		ryeBeer = registerFluid("ironagefurniture_rye_beer", "rye_beer", RYE_BEER_COLOR_FRESH);
		oatmealStout = registerFluid("ironagefurniture_oatmeal_stout", "oatmeal_stout",
			OATMEAL_STOUT_COLOR_FRESH);
		coffeeStout = registerFluid("ironagefurniture_coffee_stout", "coffee_stout", COFFEE_STOUT_COLOR_FRESH);
		chocolateStout = registerFluid("ironagefurniture_chocolate_stout", "chocolate_stout",
			CHOCOLATE_STOUT_COLOR_FRESH);
		pumpkinAle = registerFluid("ironagefurniture_pumpkin_ale", "pumpkin_ale", PUMPKIN_ALE_COLOR_FRESH);
		chiliBeer = registerFluid("ironagefurniture_chili_beer", "chili_beer", CHILI_BEER_COLOR_FRESH);
		bananaBeer = registerFluid("ironagefurniture_banana_beer", "banana_beer", BANANA_BEER_COLOR_FRESH);
		cyser = registerFluid("ironagefurniture_cyser", "cyser",
			blendColor(MEAD_COLOR_FRESH, APPLE_CIDER_COLOR_FRESH, MELOMEL_HONEY_BLEND));
		pyment = registerFluid("ironagefurniture_pyment", "pyment",
			blendColor(MEAD_COLOR_FRESH, WINE_COLOR_FRESH, MELOMEL_HONEY_BLEND));
		braggot = registerFluid("ironagefurniture_braggot", "braggot",
			blendColor(MEAD_COLOR_FRESH, ALE_COLOR_FRESH, MELOMEL_HONEY_BLEND));
		metheglin = registerFluid("ironagefurniture_metheglin", "metheglin", METHEGLIN_COLOR_FRESH);
		beetKvass = registerFluid("ironagefurniture_beet_kvass", "beet_kvass", BEET_KVASS_COLOR_FRESH);
		tepache = registerFluid("ironagefurniture_tepache", "tepache", TEPACHE_COLOR_FRESH);
		carrotWine = registerFluid("ironagefurniture_carrot_wine", "carrot_wine", CARROT_WINE_COLOR_FRESH);
		parsnipWine = registerFluid("ironagefurniture_parsnip_wine", "parsnip_wine", PARSNIP_WINE_COLOR_FRESH);
		turnipWash = registerFluid("ironagefurniture_turnip_wash", "turnip_wash", TURNIP_WASH_COLOR);
		rutabagaWash = registerFluid("ironagefurniture_rutabaga_wash", "rutabaga_wash", RUTABAGA_WASH_COLOR);
		caneWash = registerFluid("ironagefurniture_cane_wash", "cane_wash", CANE_WASH_COLOR);
		orchardMash = registerFluid("ironagefurniture_orchard_mash", "orchard_mash", ORCHARD_MASH_COLOR);
		chorusWine = registerFluid("ironagefurniture_chorus_wine", "chorus_wine", CHORUS_WINE_COLOR_FRESH);
		potashCider = registerFluid("ironagefurniture_potash_cider", "potash_cider", POTASH_CIDER_COLOR_FRESH);
		registerMelomelFluids();
		registerInfusionFluids();
		DISTILLATION_BASES.clear();
		addDistillationBases(potatoWash, sugarWash, cornMash, ginMash, spicedRumWash, coconutRumWash,
			pineappleRumWash, vanillaRumWash, citrusRumWash, mapleRumWash, gingerRumWash, turnipWash,
			rutabagaWash, caneWash, orchardMash);
		registerAgeProfiles();
	}

	public static void initRecipes() {
		RECIPES.clear();
		registerFluids();
		registerCatalogueRecipes();

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
		}
	}

	private static void registerCatalogueRecipes() {
		List<ItemStack> aleGrains = ingredientList(new ItemStack(Items.WHEAT));
		List<ItemStack> grains = ingredientList(new ItemStack(Items.WHEAT));
		List<ItemStack> rye = ingredientList();
		List<ItemStack> oats = ingredientList();
		List<ItemStack> honey = ingredientList();
		List<ItemStack> potatoes = ingredientList(new ItemStack(Items.POTATO));
		List<ItemStack> rice = ingredientList();
		List<ItemStack> corn = ingredientList();
		List<ItemStack> coffee = ingredientList();
		List<ItemStack> beet = ingredientList(new ItemStack(Items.BEETROOT));
		List<ItemStack> parsnip = ingredientList();
		List<ItemStack> turnip = ingredientList();
		List<ItemStack> rutabaga = ingredientList();
		List<ItemStack> chili = ingredientList();
		List<ItemStack> meadSpices = ingredientList();
		List<ItemStack> tepacheSpices = ingredientList();
		List<ItemStack> cane = ingredientList(new ItemStack(Items.REEDS));
		Map<String, List<ItemStack>> fruits = createFruitIngredientMap();
		boolean harvestCraftEnabled = IronAgeFurnitureConfiguration.INTEGRATION_HARVESTCRAFT
			&& Loader.isModLoaded("harvestcraft");

		if (harvestCraftEnabled) {
			addOptionalIngredient(aleGrains, "harvestcraft:barleyitem", 0);
			addOptionalIngredient(grains, "harvestcraft:barleyitem", 0);
			addOptionalIngredient(grains, "harvestcraft:ryeitem", 0);
			addOptionalIngredient(grains, "harvestcraft:oatsitem", 0);
			addOptionalIngredient(rye, "harvestcraft:ryeitem", 0);
			addOptionalIngredient(oats, "harvestcraft:oatsitem", 0);
			addOptionalIngredient(honey, "harvestcraft:honeyitem", 0);
			addOptionalIngredient(honey, "harvestcraft:honeycombitem", 0);
			addOptionalIngredient(potatoes, "harvestcraft:sweetpotatoitem", 0);
			addOptionalIngredient(rice, "harvestcraft:riceitem", 0);
			addOptionalIngredient(corn, "harvestcraft:cornitem", 0);
			addOptionalIngredient(coffee, "harvestcraft:coffeebeanitem", 0);
			addOptionalIngredient(beet, "harvestcraft:beetitem", 0);
			addOptionalIngredient(parsnip, "harvestcraft:parsnipitem", 0);
			addOptionalIngredient(turnip, "harvestcraft:turnipitem", 0);
			addOptionalIngredient(rutabaga, "harvestcraft:rutabagaitem", 0);
			addOptionalIngredient(chili, "harvestcraft:chilipepperitem", 0);
			addOptionalIngredient(meadSpices, "harvestcraft:cinnamonitem", 0);
			addOptionalIngredient(meadSpices, "harvestcraft:nutmegitem", 0);
			addOptionalIngredient(meadSpices, "harvestcraft:gingeritem", 0);
			addOptionalIngredient(meadSpices, "harvestcraft:spiceleafitem", 0);
			addOptionalIngredient(tepacheSpices, "harvestcraft:cinnamonitem", 0);
			addOptionalIngredient(tepacheSpices, "harvestcraft:gingeritem", 0);
			addHarvestCraftFruits(fruits);
		}

		if (IronAgeFurnitureConfiguration.INTEGRATION_BIOMESOPLENTY && Loader.isModLoaded("BiomesOPlenty")) {
			addOptionalIngredient(honey, "biomesoplenty:filled_honeycomb", 0);
			addOptionalIngredient(fruits.get("wild_berry"), "biomesoplenty:berries", 0);
			addOptionalIngredient(fruits.get("pear"), "biomesoplenty:pear", 0);
			addOptionalIngredient(fruits.get("peach"), "biomesoplenty:peach", 0);
			addOptionalIngredient(fruits.get("persimmon"), "biomesoplenty:persimmon", 0);
			addOptionalIngredient(turnip, "biomesoplenty:turnip", 0);
			addOptionalIngredient(rice, "biomesoplenty:plant_1", 3);
			addOptionalIngredient(cane, "biomesoplenty:plant_1", 5);
			addOptionalIngredient(meadSpices, "biomesoplenty:flower_1", 0);
		}

		if (IronAgeFurnitureConfiguration.INTEGRATION_NATURA && Loader.isModLoaded("natura")) {
			addOptionalIngredient(aleGrains, "natura:materials", 0);
			addOptionalIngredient(grains, "natura:materials", 0);
			addOptionalIngredient(fruits.get("raspberry"), "natura:edibles", 2);
			addOptionalIngredient(fruits.get("blueberry"), "natura:edibles", 3);
			addOptionalIngredient(fruits.get("blackberry"), "natura:edibles", 4);
			addOptionalIngredient(fruits.get("maloberry"), "natura:edibles", 5);
			addOptionalIngredient(fruits.get("blightberry"), "natura:edibles", 6);
			addOptionalIngredient(fruits.get("duskberry"), "natura:edibles", 7);
			addOptionalIngredient(fruits.get("skyberry"), "natura:edibles", 8);
			addOptionalIngredient(fruits.get("stingberry"), "natura:edibles", 9);
			addOptionalIngredient(fruits.get("potash"), "natura:edibles", 10);
			addOptionalIngredient(fruits.get("cactus_fruit"), "natura:saguaro_fruit_item", 0);
			addOptionalIngredient(fruits.get("cactus_fruit"), "natura:saguaro_fruit", 0);
		}

		addRecipe("ale", "Ale", ale, ALE_BREW_TIME,
			requirement(aleGrains, STANDARD_INGREDIENT_COUNT));
		addRecipe("apple_cider", "Apple Cider", appleCider, CIDER_BREW_TIME,
			requirement(fruits.get("apple"), STANDARD_INGREDIENT_COUNT));
		addRecipe("perry", "Perry", perry, CIDER_BREW_TIME,
			requirement(fruits.get("pear"), STANDARD_INGREDIENT_COUNT));
		addRecipe("mead", "Mead", mead, MEAD_BREW_TIME,
			requirement(honey, HONEY_INGREDIENT_COUNT));
		addRecipe("rice_wine", "Rice Wine", riceWine, WINE_BREW_TIME,
			requirement(rice, STANDARD_INGREDIENT_COUNT));
		addRecipe("potato_wash", "Potato Wash", potatoWash, WASH_BREW_TIME,
			requirement(potatoes, STANDARD_INGREDIENT_COUNT));
		addRecipe("sugar_wash", "Sugar Wash", sugarWash, new ItemStack(Items.SUGAR),
			STANDARD_INGREDIENT_COUNT, WASH_BREW_TIME);
		if (harvestCraftEnabled) {
			addOptionalRecipe("sugar_wash_maple", "Sugar Wash", sugarWash, "harvestcraft:maplesyrupitem", 0,
				HONEY_INGREDIENT_COUNT, WASH_BREW_TIME);
		}
		addRecipe("corn_mash", "Corn Mash", cornMash, WASH_BREW_TIME,
			requirement(corn, STANDARD_INGREDIENT_COUNT));

		addFruitWineRecipes(fruits);
		addRecipe("carrot_wine", "Carrot Wine", carrotWine, WINE_BREW_TIME,
			requirement(new ItemStack(Items.CARROT), STANDARD_INGREDIENT_COUNT));
		addRecipe("parsnip_wine", "Parsnip Wine", parsnipWine, WINE_BREW_TIME,
			requirement(parsnip, STANDARD_INGREDIENT_COUNT));
		addRecipe("rye_beer", "Rye Beer", ryeBeer, ALE_BREW_TIME,
			requirement(rye, STANDARD_INGREDIENT_COUNT));
		addRecipe("oatmeal_stout", "Oatmeal Stout", oatmealStout, ALE_BREW_TIME,
			requirement(grains, 32), requirement(oats, 24),
			requirement(new ItemStack(Items.DYE, 1, 3), SPICE_INGREDIENT_COUNT));
		addRecipe("coffee_stout", "Coffee Stout", coffeeStout, ALE_BREW_TIME,
			requirement(grains, BASE_INGREDIENT_COUNT), requirement(coffee, FLAVOR_INGREDIENT_COUNT));
		addRecipe("chocolate_stout", "Chocolate Stout", chocolateStout, ALE_BREW_TIME,
			requirement(grains, BASE_INGREDIENT_COUNT),
			requirement(new ItemStack(Items.DYE, 1, 3), FLAVOR_INGREDIENT_COUNT));
		addRecipe("pumpkin_ale", "Pumpkin Ale", pumpkinAle, ALE_BREW_TIME,
			requirement(grains, BASE_INGREDIENT_COUNT),
			requirement(new ItemStack(Blocks.PUMPKIN), FLAVOR_INGREDIENT_COUNT));
		addRecipe("chili_beer", "Chili Beer", chiliBeer, ALE_BREW_TIME,
			requirement(grains, BASE_INGREDIENT_COUNT), requirement(chili, SPICE_INGREDIENT_COUNT));
		addRecipe("banana_beer", "Banana Beer", bananaBeer, ALE_BREW_TIME,
			requirement(fruits.get("banana"), BASE_INGREDIENT_COUNT), requirement(grains, FLAVOR_INGREDIENT_COUNT));

		addRecipe("cyser", "Cyser", cyser, MEAD_BREW_TIME,
			requirement(honey, HONEY_INGREDIENT_COUNT),
			requirement(fruits.get("apple"), HONEY_INGREDIENT_COUNT));
		addRecipe("pyment", "Pyment", pyment, MEAD_BREW_TIME,
			requirement(honey, HONEY_INGREDIENT_COUNT),
			requirement(fruits.get("grape"), HONEY_INGREDIENT_COUNT));
		addRecipe("braggot", "Braggot", braggot, MEAD_BREW_TIME,
			requirement(honey, HONEY_INGREDIENT_COUNT), requirement(grains, HONEY_INGREDIENT_COUNT));
		addRecipe("metheglin", "Metheglin", metheglin, MEAD_BREW_TIME,
			requirement(honey, HONEY_INGREDIENT_COUNT), requirement(meadSpices, SPICE_INGREDIENT_COUNT));
		addMelomelRecipes(honey, fruits);

		addRecipe("beet_kvass", "Beet Kvass", beetKvass, ALE_BREW_TIME,
			requirement(beet, BASE_INGREDIENT_COUNT), requirement(new ItemStack(Items.BREAD), FLAVOR_INGREDIENT_COUNT));
		addRecipe("tepache", "Tepache", tepache, CIDER_BREW_TIME,
			requirement(fruits.get("pineapple"), BASE_INGREDIENT_COUNT),
			requirement(new ItemStack(Items.SUGAR), FLAVOR_INGREDIENT_COUNT),
			requirement(tepacheSpices, SPICE_INGREDIENT_COUNT));
		addRecipe("turnip_wash", "Turnip Wash", turnipWash, WASH_BREW_TIME,
			requirement(turnip, STANDARD_INGREDIENT_COUNT));
		addRecipe("rutabaga_wash", "Rutabaga Wash", rutabagaWash, WASH_BREW_TIME,
			requirement(rutabaga, STANDARD_INGREDIENT_COUNT));
		addRecipe("cane_wash", "Cane Wash", caneWash, WASH_BREW_TIME,
			requirement(cane, STANDARD_INGREDIENT_COUNT));
		addRecipe("orchard_mash", "Orchard Mash", orchardMash, WASH_BREW_TIME,
			requirement(fruits.get("apple"), HONEY_INGREDIENT_COUNT),
			requirement(fruits.get("pear"), HONEY_INGREDIENT_COUNT));
		addInfusionRecipes(fruits, honey, coffee, harvestCraftEnabled);
	}

	private static Map<String, List<ItemStack>> createFruitIngredientMap() {
		Map<String, List<ItemStack>> fruits = new LinkedHashMap<String, List<ItemStack>>();
		String[] keys = { "grape", "pear", "peach", "persimmon", "wild_berry", "raspberry", "blueberry",
			"blackberry", "strawberry", "cranberry", "cherry", "plum", "apricot", "mango", "pineapple",
			"banana", "date", "fig", "grapefruit", "pomegranate", "papaya", "starfruit", "gooseberry",
			"cactus_fruit", "orange", "lemon", "lime", "kiwi", "dragonfruit", "durian", "rhubarb",
			"cantaloupe", "maloberry", "blightberry", "duskberry", "skyberry", "stingberry", "potash" };

		fruits.put("apple", ingredientList(new ItemStack(Items.APPLE)));
		fruits.put("melon", ingredientList(new ItemStack(Items.MELON)));
		fruits.put("chorus", ingredientList(new ItemStack(Items.CHORUS_FRUIT)));
		for (String key : keys) {
			fruits.put(key, ingredientList());
		}
		return fruits;
	}

	private static void addHarvestCraftFruits(Map<String, List<ItemStack>> fruits) {
		String[] keys = { "grape", "pear", "peach", "persimmon", "raspberry", "blueberry", "blackberry",
			"strawberry", "cranberry", "cherry", "plum", "apricot", "mango", "pineapple", "banana",
			"date", "fig", "grapefruit", "pomegranate", "papaya", "starfruit", "gooseberry",
			"cactus_fruit", "orange", "lemon", "lime", "kiwi", "dragonfruit", "durian", "rhubarb",
			"cantaloupe" };
		for (String key : keys) {
			String itemName = "cactus_fruit".equals(key) ? "cactusfruit" : key;
			addOptionalIngredient(fruits.get(key), "harvestcraft:" + itemName + "item", 0);
		}
	}

	private static void addFruitWineRecipes(Map<String, List<ItemStack>> fruits) {
		addFruitRecipe("grape", "Grape Wine", grapeWine, fruits, WINE_BREW_TIME);
		addFruitRecipe("peach", "Peach Wine", peachWine, fruits, WINE_BREW_TIME);
		addFruitRecipe("persimmon", "Persimmon Wine", persimmonWine, fruits, WINE_BREW_TIME);
		addFruitRecipe("wild_berry", "Wild Berry Wine", wildBerryWine, fruits, WINE_BREW_TIME);
		addFruitRecipe("raspberry", "Raspberry Wine", raspberryWine, fruits, WINE_BREW_TIME);
		addFruitRecipe("blueberry", "Blueberry Wine", blueberryWine, fruits, WINE_BREW_TIME);
		addFruitRecipe("blackberry", "Blackberry Wine", blackberryWine, fruits, WINE_BREW_TIME);
		addFruitRecipe("strawberry", "Strawberry Wine", strawberryWine, fruits, WINE_BREW_TIME);
		addFruitRecipe("cranberry", "Cranberry Wine", cranberryWine, fruits, WINE_BREW_TIME);
		addFruitRecipe("cherry", "Cherry Wine", cherryWine, fruits, WINE_BREW_TIME);
		addFruitRecipe("plum", "Plum Wine", plumWine, fruits, WINE_BREW_TIME);
		addFruitRecipe("apricot", "Apricot Wine", apricotWine, fruits, WINE_BREW_TIME);
		addFruitRecipe("mango", "Mango Wine", mangoWine, fruits, WINE_BREW_TIME);
		addFruitRecipe("pineapple", "Pineapple Wine", pineappleWine, fruits, WINE_BREW_TIME);
		addFruitRecipe("banana", "Banana Wine", bananaWine, fruits, WINE_BREW_TIME);
		addFruitRecipe("date", "Date Wine", dateWine, fruits, WINE_BREW_TIME);
		addFruitRecipe("fig", "Fig Wine", figWine, fruits, WINE_BREW_TIME);
		addFruitRecipe("grapefruit", "Grapefruit Wine", grapefruitWine, fruits, WINE_BREW_TIME);
		addFruitRecipe("pomegranate", "Pomegranate Wine", pomegranateWine, fruits, WINE_BREW_TIME);
		addFruitRecipe("papaya", "Papaya Wine", papayaWine, fruits, WINE_BREW_TIME);
		addFruitRecipe("starfruit", "Starfruit Wine", starfruitWine, fruits, WINE_BREW_TIME);
		addFruitRecipe("gooseberry", "Gooseberry Wine", gooseberryWine, fruits, WINE_BREW_TIME);
		addFruitRecipe("cactus_fruit", "Cactus Fruit Wine", cactusFruitWine, fruits, WINE_BREW_TIME);
		addFruitRecipe("orange", "Orange Wine", orangeWine, fruits, WINE_BREW_TIME);
		addFruitRecipe("lemon", "Lemon Wine", lemonWine, fruits, WINE_BREW_TIME);
		addFruitRecipe("lime", "Lime Wine", limeWine, fruits, WINE_BREW_TIME);
		addFruitRecipe("kiwi", "Kiwi Wine", kiwiWine, fruits, WINE_BREW_TIME);
		addFruitRecipe("dragonfruit", "Dragonfruit Wine", dragonfruitWine, fruits, WINE_BREW_TIME);
		addFruitRecipe("durian", "Durian Wine", durianWine, fruits, WINE_BREW_TIME);
		addFruitRecipe("rhubarb", "Rhubarb Wine", rhubarbWine, fruits, WINE_BREW_TIME);
		addFruitRecipe("melon", "Melon Wine", melonWine, fruits, WINE_BREW_TIME);
		addFruitRecipe("cantaloupe", "Cantaloupe Wine", cantaloupeWine, fruits, WINE_BREW_TIME);
		addFruitRecipe("maloberry", "Maloberry Wine", maloberryWine, fruits, WINE_BREW_TIME);
		addFruitRecipe("blightberry", "Blightberry Wine", blightberryWine, fruits, WINE_BREW_TIME);
		addFruitRecipe("duskberry", "Duskberry Wine", duskberryWine, fruits, WINE_BREW_TIME);
		addFruitRecipe("skyberry", "Skyberry Wine", skyberryWine, fruits, WINE_BREW_TIME);
		addFruitRecipe("stingberry", "Stingberry Wine", stingberryWine, fruits, WINE_BREW_TIME);
		addFruitRecipe("chorus", "Chorus Wine", chorusWine, fruits, WINE_BREW_TIME);
		addFruitRecipe("potash", "Potash Cider", potashCider, fruits, CIDER_BREW_TIME);
	}

	private static void addFruitRecipe(String key, String name, Fluid output,
			Map<String, List<ItemStack>> fruits, int brewTime) {
		addRecipe(key + "_drink", name, output, brewTime,
			requirement(fruits.get(key), STANDARD_INGREDIENT_COUNT));
	}

	private static void addMelomelRecipes(List<ItemStack> honey, Map<String, List<ItemStack>> fruits) {
		for (Map.Entry<String, Fluid> entry : MELOMEL_FLUIDS.entrySet()) {
			List<ItemStack> fruit = fruits.get(entry.getKey());
			addRecipe(entry.getKey() + "_melomel", getMelomelDisplayName(entry.getKey()), entry.getValue(),
				MEAD_BREW_TIME, requirement(honey, HONEY_INGREDIENT_COUNT),
				requirement(fruit, HONEY_INGREDIENT_COUNT));
		}
	}

	private static void addInfusionRecipes(Map<String, List<ItemStack>> fruits, List<ItemStack> honey,
			List<ItemStack> coffee, boolean harvestCraftEnabled) {
		IngredientRequirement sugar = requirement(new ItemStack(Items.SUGAR), FLAVOR_INGREDIENT_COUNT);
		for (Map.Entry<String, Fluid> entry : LIQUEUR_FLUIDS.entrySet()) {
			List<ItemStack> fruit = fruits.get(entry.getKey());
			addInfusionRecipe(entry.getKey() + "_liqueur", LIQUEUR_DISPLAY_NAMES.get(entry.getKey()),
				PotStillDistillingRegistry.vodka, entry.getValue(), INFUSION_TIME,
				requirement(fruit, FLAVOR_INGREDIENT_COUNT), sugar);
		}

		List<ItemStack> peppermint = ingredientList();
		List<ItemStack> coconut = ingredientList();
		List<ItemStack> vanilla = ingredientList();
		List<ItemStack> almonds = ingredientList();
		List<ItemStack> walnuts = ingredientList();
		List<ItemStack> nocinoSpices = ingredientList();
		List<ItemStack> ginger = ingredientList();
		List<ItemStack> maple = ingredientList();
		List<ItemStack> tea = ingredientList();
		List<ItemStack> dairy = ingredientList(new ItemStack(Items.MILK_BUCKET));
		List<ItemStack> nutmeg = ingredientList();

		if (harvestCraftEnabled) {
			addOptionalIngredient(peppermint, "harvestcraft:peppermintitem", 0);
			addOptionalIngredient(coconut, "harvestcraft:coconutitem", 0);
			addOptionalIngredient(vanilla, "harvestcraft:vanillabeanitem", 0);
			addOptionalIngredient(almonds, "harvestcraft:almonditem", 0);
			addOptionalIngredient(walnuts, "harvestcraft:walnutitem", 0);
			addOptionalIngredient(nocinoSpices, "harvestcraft:cinnamonitem", 0);
			addOptionalIngredient(nocinoSpices, "harvestcraft:nutmegitem", 0);
			addOptionalIngredient(ginger, "harvestcraft:gingeritem", 0);
			addOptionalIngredient(maple, "harvestcraft:maplesyrupitem", 0);
			addOptionalIngredient(tea, "harvestcraft:tealeafitem", 0);
			addOptionalIngredient(dairy, "harvestcraft:freshmilkitem", 0);
			addOptionalIngredient(dairy, "harvestcraft:heavycreamitem", 0);
			addOptionalIngredient(nutmeg, "harvestcraft:nutmegitem", 0);
		}

		addInfusionRecipe("peppermint_schnapps", "Peppermint Schnapps", PotStillDistillingRegistry.vodka,
			peppermintSchnapps, INFUSION_TIME, requirement(peppermint, FLAVOR_INGREDIENT_COUNT), sugar);
		addInfusionRecipe("coffee_liqueur", "Coffee Liqueur", PotStillDistillingRegistry.rum, coffeeLiqueur,
			INFUSION_TIME, requirement(coffee, FLAVOR_INGREDIENT_COUNT), sugar);
		addInfusionRecipe("chocolate_liqueur", "Chocolate Liqueur", PotStillDistillingRegistry.vodka,
			chocolateLiqueur, INFUSION_TIME,
			requirement(new ItemStack(Items.DYE, 1, 3), FLAVOR_INGREDIENT_COUNT), sugar);
		addInfusionRecipe("coconut_liqueur", "Coconut Liqueur", PotStillDistillingRegistry.rum,
			coconutLiqueur, INFUSION_TIME, requirement(coconut, FLAVOR_INGREDIENT_COUNT), sugar);
		addInfusionRecipe("vanilla_liqueur", "Vanilla Liqueur", PotStillDistillingRegistry.rum,
			vanillaLiqueur, INFUSION_TIME, requirement(vanilla, SPICE_INGREDIENT_COUNT), sugar);
		addInfusionRecipe("amaretto", "Amaretto", PotStillDistillingRegistry.brandy, amaretto, INFUSION_TIME,
			requirement(almonds, FLAVOR_INGREDIENT_COUNT), sugar);
		addInfusionRecipe("nocino", "Nocino", PotStillDistillingRegistry.vodka, nocino, INFUSION_TIME,
			requirement(walnuts, FLAVOR_INGREDIENT_COUNT), requirement(nocinoSpices, SPICE_INGREDIENT_COUNT),
			sugar);
		addInfusionRecipe("ginger_liqueur", "Ginger Liqueur", PotStillDistillingRegistry.brandy,
			gingerLiqueur, INFUSION_TIME, requirement(ginger, FLAVOR_INGREDIENT_COUNT), sugar);
		addInfusionRecipe("honey_whisky_liqueur", "Honey Whisky Liqueur", PotStillDistillingRegistry.whisky,
			honeyWhiskyLiqueur, INFUSION_TIME, requirement(honey, FLAVOR_INGREDIENT_COUNT));
		addInfusionRecipe("maple_whisky_liqueur", "Maple Whisky Liqueur", PotStillDistillingRegistry.whisky,
			mapleWhiskyLiqueur, INFUSION_TIME, requirement(maple, FLAVOR_INGREDIENT_COUNT));
		addInfusionRecipe("tea_liqueur", "Tea Liqueur", PotStillDistillingRegistry.vodka, teaLiqueur,
			INFUSION_TIME, requirement(tea, FLAVOR_INGREDIENT_COUNT), sugar);

		addCreamRecipe("cherry", LIQUEUR_FLUIDS.get("cherry"), dairy);
		addCreamRecipe("strawberry", LIQUEUR_FLUIDS.get("strawberry"), dairy);
		addCreamRecipe("raspberry", LIQUEUR_FLUIDS.get("raspberry"), dairy);
		addCreamRecipe("blueberry", LIQUEUR_FLUIDS.get("blueberry"), dairy);
		addCreamRecipe("peach", LIQUEUR_FLUIDS.get("peach"), dairy);
		addCreamRecipe("coffee", coffeeLiqueur, dairy);
		addCreamRecipe("chocolate", chocolateLiqueur, dairy);
		addCreamRecipe("coconut", coconutLiqueur, dairy);
		addCreamRecipe("vanilla", vanillaLiqueur, dairy);

		addInfusionRecipe("whisky_cream_liqueur", "Whisky Cream Liqueur", PotStillDistillingRegistry.whisky,
			whiskyCreamLiqueur, CREAM_BLEND_TIME, requirement(dairy, 1),
			requirement(new ItemStack(Items.SUGAR), FLAVOR_INGREDIENT_COUNT),
			requirement(new ItemStack(Items.DYE, 1, 3), SPICE_INGREDIENT_COUNT));
		addInfusionRecipe("eggnog", "Eggnog", PotStillDistillingRegistry.rum, eggnog, CREAM_BLEND_TIME,
			requirement(dairy, 1), requirement(new ItemStack(Items.EGG), FLAVOR_INGREDIENT_COUNT),
			requirement(new ItemStack(Items.SUGAR), SPICE_INGREDIENT_COUNT));
		addInfusionRecipe("eggnog_nutmeg", "Eggnog", PotStillDistillingRegistry.rum, eggnog, CREAM_BLEND_TIME,
			requirement(dairy, 1), requirement(new ItemStack(Items.EGG), FLAVOR_INGREDIENT_COUNT),
			requirement(new ItemStack(Items.SUGAR), SPICE_INGREDIENT_COUNT),
			requirement(nutmeg, SPICE_INGREDIENT_COUNT));
	}

	private static void addCreamRecipe(String key, Fluid input, List<ItemStack> dairy) {
		Fluid output = CREAM_LIQUEUR_FLUIDS.get(key);
		String displayName = LIQUEUR_DISPLAY_NAMES.get(key + "_cream");
		addInfusionRecipe(key + "_cream_liqueur", displayName, input, output, CREAM_BLEND_TIME,
			requirement(dairy, 1), requirement(new ItemStack(Items.SUGAR), SPICE_INGREDIENT_COUNT));
	}

	private static void registerMelomelFluids() {
		MELOMEL_FLUIDS.clear();
		MELOMEL_DISPLAY_NAMES.clear();
		MELOMEL_SPIRIT_NAMES.clear();
		addMelomelFluid("pear", "Pear", PERRY_COLOR_FRESH);
		addMelomelFluid("peach", "Peach", PEACH_WINE_COLOR_FRESH);
		addMelomelFluid("persimmon", "Persimmon", PERSIMMON_WINE_COLOR_FRESH);
		addMelomelFluid("wild_berry", "Wild Berry", WILD_BERRY_WINE_COLOR_FRESH);
		addMelomelFluid("raspberry", "Raspberry", RASPBERRY_WINE_COLOR_FRESH);
		addMelomelFluid("blueberry", "Blueberry", BLUEBERRY_WINE_COLOR_FRESH);
		addMelomelFluid("blackberry", "Blackberry", BLACKBERRY_WINE_COLOR_FRESH);
		addMelomelFluid("strawberry", "Strawberry", STRAWBERRY_WINE_COLOR_FRESH);
		addMelomelFluid("cranberry", "Cranberry", CRANBERRY_WINE_COLOR_FRESH);
		addMelomelFluid("cherry", "Cherry", CHERRY_WINE_COLOR_FRESH);
		addMelomelFluid("plum", "Plum", PLUM_WINE_COLOR_FRESH);
		addMelomelFluid("apricot", "Apricot", APRICOT_WINE_COLOR_FRESH);
		addMelomelFluid("mango", "Mango", MANGO_WINE_COLOR_FRESH);
		addMelomelFluid("pineapple", "Pineapple", PINEAPPLE_WINE_COLOR_FRESH);
		addMelomelFluid("banana", "Banana", BANANA_WINE_COLOR_FRESH);
		addMelomelFluid("date", "Date", DATE_WINE_COLOR_FRESH);
		addMelomelFluid("fig", "Fig", FIG_WINE_COLOR_FRESH);
		addMelomelFluid("grapefruit", "Grapefruit", GRAPEFRUIT_WINE_COLOR_FRESH);
		addMelomelFluid("pomegranate", "Pomegranate", POMEGRANATE_WINE_COLOR_FRESH);
		addMelomelFluid("papaya", "Papaya", PAPAYA_WINE_COLOR_FRESH);
		addMelomelFluid("starfruit", "Starfruit", STARFRUIT_WINE_COLOR_FRESH);
		addMelomelFluid("gooseberry", "Gooseberry", GOOSEBERRY_WINE_COLOR_FRESH);
		addMelomelFluid("cactus_fruit", "Cactus Fruit", CACTUS_FRUIT_WINE_COLOR_FRESH);
		addMelomelFluid("orange", "Orange", ORANGE_WINE_COLOR_FRESH);
		addMelomelFluid("lemon", "Lemon", LEMON_WINE_COLOR_FRESH);
		addMelomelFluid("lime", "Lime", LIME_WINE_COLOR_FRESH);
		addMelomelFluid("kiwi", "Kiwi", KIWI_WINE_COLOR_FRESH);
		addMelomelFluid("dragonfruit", "Dragonfruit", DRAGONFRUIT_WINE_COLOR_FRESH);
		addMelomelFluid("durian", "Durian", DURIAN_WINE_COLOR_FRESH);
		addMelomelFluid("rhubarb", "Rhubarb", RHUBARB_WINE_COLOR_FRESH);
		addMelomelFluid("melon", "Melon", MELON_WINE_COLOR_FRESH);
		addMelomelFluid("cantaloupe", "Cantaloupe", CANTALOUPE_WINE_COLOR_FRESH);
		addMelomelFluid("maloberry", "Maloberry", MALOBERRY_WINE_COLOR_FRESH);
		addMelomelFluid("blightberry", "Blightberry", BLIGHTBERRY_WINE_COLOR_FRESH);
		addMelomelFluid("duskberry", "Duskberry", DUSKBERRY_WINE_COLOR_FRESH);
		addMelomelFluid("skyberry", "Skyberry", SKYBERRY_WINE_COLOR_FRESH);
		addMelomelFluid("stingberry", "Stingberry", STINGBERRY_WINE_COLOR_FRESH);
		addMelomelFluid("chorus", "Chorus", CHORUS_WINE_COLOR_FRESH);
		addMelomelFluid("potash", "Potash Apple", POTASH_CIDER_COLOR_FRESH);
	}

	private static void addMelomelFluid(String key, String fruitName, int fruitColor) {
		Fluid fluid = registerFluid("ironagefurniture_" + key + "_melomel", key + "_melomel",
			blendColor(MEAD_COLOR_FRESH, fruitColor, MELOMEL_HONEY_BLEND));
		MELOMEL_FLUIDS.put(key, fluid);
		MELOMEL_DISPLAY_NAMES.put(key, fruitName + " Melomel");
		MELOMEL_SPIRIT_NAMES.put(key, fruitName + " Honey Spirit");
	}

	private static void registerInfusionFluids() {
		LIQUEUR_FLUIDS.clear();
		LIQUEUR_DISPLAY_NAMES.clear();
		LIQUEUR_COLORS.clear();
		CREAM_LIQUEUR_FLUIDS.clear();
		Map<String, Integer> fruitColors = createFruitColorMap();
		Map<String, String> fruitNames = createFruitDisplayNameMap();

		for (Map.Entry<String, Integer> entry : fruitColors.entrySet()) {
			String key = entry.getKey();
			String displayName = getFruitLiqueurName(key, fruitNames.get(key));
			String fluidName = getFruitLiqueurId(key);
			int color = blendColor(entry.getValue().intValue(), MILK_COLOR, LIQUEUR_SPIRIT_BLEND);
			Fluid fluid = registerFluid("ironagefurniture_" + fluidName, fluidName, color);
			LIQUEUR_FLUIDS.put(key, fluid);
			LIQUEUR_DISPLAY_NAMES.put(key, displayName);
			LIQUEUR_COLORS.put(key, Integer.valueOf(color));
		}

		peppermintSchnapps = registerSpecialLiqueur("peppermint_schnapps", "Peppermint Schnapps",
			PEPPERMINT_COLOR);
		coffeeLiqueur = registerSpecialLiqueur("coffee_liqueur", "Coffee Liqueur", COFFEE_LIQUEUR_COLOR);
		chocolateLiqueur = registerSpecialLiqueur("chocolate_liqueur", "Chocolate Liqueur",
			CHOCOLATE_LIQUEUR_COLOR);
		coconutLiqueur = registerSpecialLiqueur("coconut_liqueur", "Coconut Liqueur", COCONUT_LIQUEUR_COLOR);
		vanillaLiqueur = registerSpecialLiqueur("vanilla_liqueur", "Vanilla Liqueur", VANILLA_LIQUEUR_COLOR);
		amaretto = registerSpecialLiqueur("amaretto", "Amaretto", AMARETTO_COLOR);
		nocino = registerSpecialLiqueur("nocino", "Nocino", NOCINO_COLOR);
		gingerLiqueur = registerSpecialLiqueur("ginger_liqueur", "Ginger Liqueur", GINGER_LIQUEUR_COLOR);
		honeyWhiskyLiqueur = registerSpecialLiqueur("honey_whisky_liqueur", "Honey Whisky Liqueur",
			HONEY_WHISKY_COLOR);
		mapleWhiskyLiqueur = registerSpecialLiqueur("maple_whisky_liqueur", "Maple Whisky Liqueur",
			MAPLE_WHISKY_COLOR);
		teaLiqueur = registerSpecialLiqueur("tea_liqueur", "Tea Liqueur", TEA_LIQUEUR_COLOR);

		addCreamLiqueurFluid("cherry", "Cherry", getLiqueurColor("cherry"));
		addCreamLiqueurFluid("strawberry", "Strawberry", getLiqueurColor("strawberry"));
		addCreamLiqueurFluid("raspberry", "Raspberry", getLiqueurColor("raspberry"));
		addCreamLiqueurFluid("blueberry", "Blueberry", getLiqueurColor("blueberry"));
		addCreamLiqueurFluid("peach", "Peach", getLiqueurColor("peach"));
		addCreamLiqueurFluid("coffee", "Coffee", COFFEE_LIQUEUR_COLOR);
		addCreamLiqueurFluid("chocolate", "Chocolate", CHOCOLATE_LIQUEUR_COLOR);
		addCreamLiqueurFluid("coconut", "Coconut", COCONUT_LIQUEUR_COLOR);
		addCreamLiqueurFluid("vanilla", "Vanilla", VANILLA_LIQUEUR_COLOR);
		whiskyCreamLiqueur = registerFluid("ironagefurniture_whisky_cream_liqueur", "whisky_cream_liqueur",
			blendColor(HONEY_WHISKY_COLOR, MILK_COLOR, CREAM_BLEND));
		eggnog = registerFluid("ironagefurniture_eggnog", "eggnog", EGGNOG_COLOR);
	}

	private static Fluid registerSpecialLiqueur(String id, String displayName, int color) {
		Fluid fluid = registerFluid("ironagefurniture_" + id, id, color);
		LIQUEUR_DISPLAY_NAMES.put(id, displayName);
		LIQUEUR_COLORS.put(id, Integer.valueOf(color));
		return fluid;
	}

	private static void addCreamLiqueurFluid(String key, String displayName, int baseColor) {
		Fluid fluid = registerFluid("ironagefurniture_" + key + "_cream_liqueur", key + "_cream_liqueur",
			blendColor(baseColor, MILK_COLOR, CREAM_BLEND));
		CREAM_LIQUEUR_FLUIDS.put(key, fluid);
		LIQUEUR_DISPLAY_NAMES.put(key + "_cream", displayName + " Cream Liqueur");
	}

	private static int getLiqueurColor(String key) {
		Integer color = LIQUEUR_COLORS.get(key);
		return color == null ? MEAD_COLOR_FRESH : color.intValue();
	}

	private static String getFruitLiqueurId(String key) {
		if ("apple".equals(key)) {
			return "apple_schnapps";
		}
		if ("peach".equals(key)) {
			return "peach_schnapps";
		}
		if ("lemon".equals(key)) {
			return "limoncello";
		}
		if ("orange".equals(key)) {
			return "triple_sec";
		}
		return key + "_liqueur";
	}

	private static String getFruitLiqueurName(String key, String fruitName) {
		if ("apple".equals(key)) {
			return "Apple Schnapps";
		}
		if ("peach".equals(key)) {
			return "Peach Schnapps";
		}
		if ("lemon".equals(key)) {
			return "Limoncello";
		}
		if ("orange".equals(key)) {
			return "Triple Sec";
		}
		return fruitName + " Liqueur";
	}

	private static Map<String, Integer> createFruitColorMap() {
		Map<String, Integer> colors = new LinkedHashMap<String, Integer>();
		colors.put("apple", Integer.valueOf(APPLE_CIDER_COLOR_FRESH));
		colors.put("grape", Integer.valueOf(WINE_COLOR_FRESH));
		colors.put("pear", Integer.valueOf(PERRY_COLOR_FRESH));
		colors.put("peach", Integer.valueOf(PEACH_WINE_COLOR_FRESH));
		colors.put("persimmon", Integer.valueOf(PERSIMMON_WINE_COLOR_FRESH));
		colors.put("wild_berry", Integer.valueOf(WILD_BERRY_WINE_COLOR_FRESH));
		colors.put("raspberry", Integer.valueOf(RASPBERRY_WINE_COLOR_FRESH));
		colors.put("blueberry", Integer.valueOf(BLUEBERRY_WINE_COLOR_FRESH));
		colors.put("blackberry", Integer.valueOf(BLACKBERRY_WINE_COLOR_FRESH));
		colors.put("strawberry", Integer.valueOf(STRAWBERRY_WINE_COLOR_FRESH));
		colors.put("cranberry", Integer.valueOf(CRANBERRY_WINE_COLOR_FRESH));
		colors.put("cherry", Integer.valueOf(CHERRY_WINE_COLOR_FRESH));
		colors.put("plum", Integer.valueOf(PLUM_WINE_COLOR_FRESH));
		colors.put("apricot", Integer.valueOf(APRICOT_WINE_COLOR_FRESH));
		colors.put("mango", Integer.valueOf(MANGO_WINE_COLOR_FRESH));
		colors.put("pineapple", Integer.valueOf(PINEAPPLE_WINE_COLOR_FRESH));
		colors.put("banana", Integer.valueOf(BANANA_WINE_COLOR_FRESH));
		colors.put("date", Integer.valueOf(DATE_WINE_COLOR_FRESH));
		colors.put("fig", Integer.valueOf(FIG_WINE_COLOR_FRESH));
		colors.put("grapefruit", Integer.valueOf(GRAPEFRUIT_WINE_COLOR_FRESH));
		colors.put("pomegranate", Integer.valueOf(POMEGRANATE_WINE_COLOR_FRESH));
		colors.put("papaya", Integer.valueOf(PAPAYA_WINE_COLOR_FRESH));
		colors.put("starfruit", Integer.valueOf(STARFRUIT_WINE_COLOR_FRESH));
		colors.put("gooseberry", Integer.valueOf(GOOSEBERRY_WINE_COLOR_FRESH));
		colors.put("cactus_fruit", Integer.valueOf(CACTUS_FRUIT_WINE_COLOR_FRESH));
		colors.put("orange", Integer.valueOf(ORANGE_WINE_COLOR_FRESH));
		colors.put("lemon", Integer.valueOf(LEMON_WINE_COLOR_FRESH));
		colors.put("lime", Integer.valueOf(LIME_WINE_COLOR_FRESH));
		colors.put("kiwi", Integer.valueOf(KIWI_WINE_COLOR_FRESH));
		colors.put("dragonfruit", Integer.valueOf(DRAGONFRUIT_WINE_COLOR_FRESH));
		colors.put("durian", Integer.valueOf(DURIAN_WINE_COLOR_FRESH));
		colors.put("rhubarb", Integer.valueOf(RHUBARB_WINE_COLOR_FRESH));
		colors.put("melon", Integer.valueOf(MELON_WINE_COLOR_FRESH));
		colors.put("cantaloupe", Integer.valueOf(CANTALOUPE_WINE_COLOR_FRESH));
		colors.put("maloberry", Integer.valueOf(MALOBERRY_WINE_COLOR_FRESH));
		colors.put("blightberry", Integer.valueOf(BLIGHTBERRY_WINE_COLOR_FRESH));
		colors.put("duskberry", Integer.valueOf(DUSKBERRY_WINE_COLOR_FRESH));
		colors.put("skyberry", Integer.valueOf(SKYBERRY_WINE_COLOR_FRESH));
		colors.put("stingberry", Integer.valueOf(STINGBERRY_WINE_COLOR_FRESH));
		colors.put("chorus", Integer.valueOf(CHORUS_WINE_COLOR_FRESH));
		colors.put("potash", Integer.valueOf(POTASH_CIDER_COLOR_FRESH));
		return colors;
	}

	private static Map<String, String> createFruitDisplayNameMap() {
		Map<String, String> names = new LinkedHashMap<String, String>();
		for (String key : createFruitColorMap().keySet()) {
			String[] words = key.split("_");
			StringBuilder name = new StringBuilder();
			for (String word : words) {
				if (name.length() > 0) {
					name.append(' ');
				}
				name.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1));
			}
			names.put(key, name.toString());
		}
		return names;
	}

	private static String getMelomelDisplayName(String key) {
		String name = MELOMEL_DISPLAY_NAMES.get(key);
		return name == null ? "Melomel" : name;
	}

	public static Map<String, Fluid> getMelomelFluids() {
		return Collections.unmodifiableMap(MELOMEL_FLUIDS);
	}

	public static String getMelomelSpiritName(String key) {
		return MELOMEL_SPIRIT_NAMES.get(key);
	}

	public static List<FoudreBrewingRecipe> getRecipes() {
		return Collections.unmodifiableList(RECIPES);
	}

	public static Set<Fluid> getRegisteredFluids() {
		return Collections.unmodifiableSet(REGISTERED_FLUIDS);
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
		if (fluid == null || fluid.getFluid() == null || fluid.amount <= 0) {
			return null;
		}

		FoudreBrewingRecipe bestRecipe = null;

		for (FoudreBrewingRecipe recipe : RECIPES) {
			if (recipe.matchesFluid(fluid, capacity) && recipe.matches(inventory)
					&& isMoreSpecific(recipe, bestRecipe)) {
				bestRecipe = recipe;
			}
		}

		return bestRecipe;
	}

	public static FoudreBrewingRecipe findPotentialRecipe(FluidStack fluid, ItemStack[] inventory) {
		if (fluid == null || fluid.getFluid() == null || fluid.amount <= 0) {
			return null;
		}

		FoudreBrewingRecipe bestRecipe = null;

		for (FoudreBrewingRecipe recipe : RECIPES) {
			if (recipe.matchesInputFluid(fluid) && recipe.matches(inventory)
					&& isMoreSpecific(recipe, bestRecipe)) {
				bestRecipe = recipe;
			}
		}

		return bestRecipe;
	}

	public static FluidStack createBrewedFluid(FoudreBrewingRecipe recipe, int amount) {
		return recipe == null ? null : recipe.createOutput(new FluidStack(FluidRegistry.WATER, amount), amount);
	}

	public static FluidStack createBrewedFluid(FoudreBrewingRecipe recipe, FluidStack input, int capacity) {
		return recipe == null ? null : recipe.createOutput(input, capacity);
	}

	@Nullable
	public static FluidStack getInfusionBase(@Nullable FluidStack fluid) {
		if (fluid == null || fluid.tag == null || !fluid.tag.hasKey(INFUSION_BASE_TAG, 10)) {
			return null;
		}

		return FluidStack.loadFluidStackFromNBT(fluid.tag.getCompoundTag(INFUSION_BASE_TAG));
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

	public static int getAgeLevel(FluidStack fluid) {
		AgeProfile profile = getAgeProfile(fluid);
		return profile == null ? 0 : profile.getLevel(getAgeTicks(fluid));
	}

	public static int getMaximumAgeLevel(FluidStack fluid) {
		AgeProfile profile = getAgeProfile(fluid);
		return profile == null ? 0 : profile.getMaxLevel();
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
			REGISTERED_FLUIDS.add(existing);
			return existing;
		}

		Fluid fluid = new TintedFluid(name, STILL, FLOWING, color).setUnlocalizedName(unlocalizedName)
			.setDensity(1000).setViscosity(1000);
		FluidRegistry.registerFluid(fluid);
		fluid = FluidRegistry.getFluid(name);
		FluidRegistry.addBucketForFluid(fluid);
		REGISTERED_FLUIDS.add(fluid);
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

		RECIPES.add(new FoudreBrewingRecipe(id, displayName, FluidRegistry.WATER, output, brewTime,
			FULL_FOUDRE_AMOUNT, OutputVolumePolicy.FULL_CAPACITY, OutputMetadataPolicy.FRESH,
			false, recipeRequirements));
	}

	private static void addInfusionRecipe(String id, String displayName, Fluid input, Fluid output, int brewTime,
			IngredientRequirement... requirements) {
		if (input == null || output == null || requirements == null || requirements.length <= 0) {
			return;
		}

		List<IngredientRequirement> recipeRequirements = new ArrayList<IngredientRequirement>();
		for (IngredientRequirement requirement : requirements) {
			if (requirement == null || !requirement.isValid()) {
				return;
			}
			recipeRequirements.add(requirement);
		}

		RECIPES.add(new FoudreBrewingRecipe(id, displayName, input, output, brewTime, MIN_INFUSION_AMOUNT,
			OutputVolumePolicy.PRESERVE_INPUT, OutputMetadataPolicy.INFUSION_PROVENANCE, true,
			recipeRequirements));
	}

	private static void addOptionalRecipe(String id, String displayName, Fluid output, String registryName, int meta,
			int count, int brewTime) {
		addRecipe(id, displayName, output, brewTime, optionalRequirement(registryName, meta, count));
	}

	private static IngredientRequirement requirement(ItemStack ingredient, int count) {
		return new IngredientRequirement(ingredient, count);
	}

	private static IngredientRequirement requirement(List<ItemStack> ingredients, int count) {
		return new IngredientRequirement(ingredients, count);
	}

	private static List<ItemStack> ingredientList(ItemStack... ingredients) {
		List<ItemStack> result = new ArrayList<ItemStack>();
		if (ingredients != null) {
			for (ItemStack ingredient : ingredients) {
				if (ingredient != null && ingredient.getItem() != null) {
					result.add(ingredient.copy());
				}
			}
		}
		return result;
	}

	private static void addOptionalIngredient(List<ItemStack> ingredients, String registryName, int meta) {
		if (ingredients == null) {
			return;
		}

		Item item = Item.getByNameOrId(registryName);
		if (item == null) {
			return;
		}

		ItemStack candidate = new ItemStack(item, 1, meta);
		for (ItemStack ingredient : ingredients) {
			if (ingredient.getItem() == item && ingredient.getItemDamage() == meta) {
				return;
			}
		}
		ingredients.add(candidate);
	}

	private static IngredientRequirement optionalRequirement(String registryName, int meta, int count) {
		Item item = Item.getByNameOrId(registryName);

		if (item != null) {
			return requirement(new ItemStack(item, 1, meta), count);
		}

		return null;
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

	private static boolean isMoreSpecific(FoudreBrewingRecipe recipe, FoudreBrewingRecipe currentBest) {
		return currentBest == null || recipe.getRequirementCount() > currentBest.getRequirementCount()
			|| recipe.getRequirementCount() == currentBest.getRequirementCount()
				&& recipe.getRequiredIngredientCount() > currentBest.getRequiredIngredientCount();
	}

	public static void registerAgeProfiles() {
		AGE_PROFILES.clear();
		addAgeProfile(ALE_PROFILE, ale, ryeBeer, oatmealStout, coffeeStout, chocolateStout, pumpkinAle,
			chiliBeer, bananaBeer, beetKvass);
		addAgeProfile(CIDER_PROFILE, cider, appleCider, perry, tepache, potashCider);
		addAgeProfile(MEAD_PROFILE, mead, cyser, pyment, braggot, metheglin);
		addAgeProfile(MEAD_PROFILE, MELOMEL_FLUIDS.values().toArray(new Fluid[MELOMEL_FLUIDS.size()]));
		addAgeProfile(WINE_PROFILE, wine, berryWine, riceWine, grapeWine, peachWine, persimmonWine, wildBerryWine,
			raspberryWine, blueberryWine, blackberryWine, strawberryWine, cranberryWine, cherryWine, plumWine,
			apricotWine, mangoWine, pineappleWine, bananaWine, dateWine, figWine, grapefruitWine, pomegranateWine,
			papayaWine, starfruitWine, gooseberryWine, cactusFruitWine, maloberryWine, blightberryWine,
			duskberryWine, skyberryWine, stingberryWine, orangeWine, lemonWine, limeWine, kiwiWine,
			dragonfruitWine, durianWine, rhubarbWine, melonWine, cantaloupeWine, carrotWine, parsnipWine,
			chorusWine);
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

	public enum OutputVolumePolicy {
		FULL_CAPACITY,
		PRESERVE_INPUT
	}

	public enum OutputMetadataPolicy {
		FRESH,
		INFUSION_PROVENANCE
	}

	public static class FoudreBrewingRecipe {
		private final String id;
		private final String displayName;
		private final Fluid input;
		private final Fluid output;
		private final int brewTime;
		private final int minimumInputAmount;
		private final OutputVolumePolicy outputVolumePolicy;
		private final OutputMetadataPolicy outputMetadataPolicy;
		private final boolean requiresResealAfterCompletion;
		private final List<IngredientRequirement> requirements;

		FoudreBrewingRecipe(String id, String displayName, Fluid input, Fluid output, int brewTime,
				int minimumInputAmount, OutputVolumePolicy outputVolumePolicy,
				OutputMetadataPolicy outputMetadataPolicy, boolean requiresResealAfterCompletion,
				List<IngredientRequirement> requirements) {
			this.id = id;
			this.displayName = displayName;
			this.input = input;
			this.output = output;
			this.brewTime = brewTime;
			this.minimumInputAmount = minimumInputAmount;
			this.outputVolumePolicy = outputVolumePolicy;
			this.outputMetadataPolicy = outputMetadataPolicy;
			this.requiresResealAfterCompletion = requiresResealAfterCompletion;
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

		public Fluid getInput() {
			return this.input;
		}

		public int getMinimumInputAmount() {
			return this.minimumInputAmount;
		}

		public OutputVolumePolicy getOutputVolumePolicy() {
			return this.outputVolumePolicy;
		}

		public OutputMetadataPolicy getOutputMetadataPolicy() {
			return this.outputMetadataPolicy;
		}

		public boolean requiresResealAfterCompletion() {
			return this.requiresResealAfterCompletion;
		}

		public boolean preservesInputVolume() {
			return this.outputVolumePolicy == OutputVolumePolicy.PRESERVE_INPUT;
		}

		public boolean acceptsSpiritQuality() {
			return PotStillDistillingRegistry.isSpiritFluid(this.input);
		}

		public int getJeiInputAmount() {
			return this.preservesInputVolume() ? JEI_INFUSION_AMOUNT : FULL_FOUDRE_AMOUNT;
		}

		public int getJeiOutputAmount() {
			return this.getJeiInputAmount();
		}

		public int getBrewTime() {
			return IronAgeFurnitureConfiguration.scaleDrinkTicks(this.brewTime);
		}

		public final List<ItemStack> getIngredientStacks() {
			List<ItemStack> ingredients = new ArrayList<ItemStack>();

			for (IngredientRequirement requirement : this.requirements) {
				ItemStack ingredient = requirement.getRepresentative();
				if (ingredient != null) {
					ingredients.add(ingredient);
				}
			}

			return ingredients;
		}

		public final List<List<ItemStack>> getIngredientAlternatives() {
			List<List<ItemStack>> ingredients = new ArrayList<List<ItemStack>>();
			for (IngredientRequirement requirement : this.requirements) {
				ingredients.add(requirement.getAlternatives());
			}
			return ingredients;
		}

		public boolean matches(ItemStack[] inventory) {
			return this.allocateIngredients(inventory) != null;
		}

		public boolean matchesFluid(FluidStack fluid, int capacity) {
			return this.matchesInputFluid(fluid) && fluid.amount >= this.minimumInputAmount
				&& (this.outputVolumePolicy != OutputVolumePolicy.FULL_CAPACITY || fluid.amount >= capacity);
		}

		public boolean matchesInputFluid(FluidStack fluid) {
			return fluid != null && fluid.getFluid() == this.input;
		}

		public FluidStack createOutput(FluidStack inputFluid, int capacity) {
			if (!this.matchesFluid(inputFluid, capacity)) {
				return null;
			}

			int amount = this.outputVolumePolicy == OutputVolumePolicy.PRESERVE_INPUT
				? inputFluid.amount : capacity;
			FluidStack outputFluid = new FluidStack(this.output, amount);
			if (this.outputMetadataPolicy == OutputMetadataPolicy.FRESH) {
				initializeAge(outputFluid);
			} else {
				FluidStack provenance = getInfusionBase(inputFluid);
				if (provenance == null) {
					provenance = copyWithCurrentAgeLevel(inputFluid);
				}
				if (provenance != null && provenance.getFluid() != null) {
					provenance.amount = 1;
					outputFluid.tag = new NBTTagCompound();
					outputFluid.tag.setTag(INFUSION_BASE_TAG,
						provenance.writeToNBT(new NBTTagCompound()));
				}
				DrinkProperties.preserveInputValue(outputFluid, inputFluid, amount, 110);
			}
			return outputFluid;
		}

		public List<ItemStack> consumeIngredients(ItemStack[] inventory) {
			int[] allocation = this.allocateIngredients(inventory);
			List<ItemStack> remainders = new ArrayList<ItemStack>();
			if (allocation == null) {
				return remainders;
			}

			for (int i = 0; i < allocation.length; i++) {
				if (allocation[i] <= 0 || inventory[i] == null) {
					continue;
				}
				ItemStack consumed = inventory[i].copy();
				consumed.stackSize = 1;
				for (int count = 0; count < allocation[i]; count++) {
					ItemStack remainder = getContainerRemainder(consumed);
					if (remainder != null) {
						addRemainder(remainders, remainder);
					}
				}
				inventory[i].stackSize -= allocation[i];
				if (inventory[i].stackSize <= 0) {
					inventory[i] = null;
				}
			}
			return remainders;
		}

		@Nullable
		private static ItemStack getContainerRemainder(ItemStack consumed) {
			if (consumed == null || consumed.getItem() == null) {
				return null;
			}
			if (consumed.getItem() == Items.MILK_BUCKET || isRegistryItem(consumed, "harvestcraft:freshmilkitem")) {
				return new ItemStack(Items.BUCKET);
			}
			if (isRegistryItem(consumed, "harvestcraft:heavycreamitem")) {
				return null;
			}
			if (consumed.getItem().hasContainerItem(consumed)) {
				return consumed.getItem().getContainerItem(consumed);
			}
			return null;
		}

		private static boolean isRegistryItem(ItemStack stack, String registryName) {
			return stack.getItem().getRegistryName() != null
				&& registryName.equals(stack.getItem().getRegistryName().toString());
		}

		private static void addRemainder(List<ItemStack> remainders, ItemStack remainder) {
			if (remainder == null || remainder.stackSize <= 0) {
				return;
			}
			for (ItemStack existing : remainders) {
				if (existing.getItem() == remainder.getItem()
						&& existing.getItemDamage() == remainder.getItemDamage()
						&& ItemStack.areItemStackTagsEqual(existing, remainder)
						&& existing.stackSize < existing.getMaxStackSize()) {
					int moved = Math.min(remainder.stackSize, existing.getMaxStackSize() - existing.stackSize);
					existing.stackSize += moved;
					remainder.stackSize -= moved;
					if (remainder.stackSize <= 0) {
						return;
					}
				}
			}
			remainders.add(remainder.copy());
		}

		public boolean matchesIngredient(ItemStack stack) {
			for (IngredientRequirement requirement : this.requirements) {
				if (requirement.matches(stack)) {
					return true;
				}
			}

			return false;
		}

		private int[] allocateIngredients(ItemStack[] inventory) {
			if (inventory == null || inventory.length <= 0) {
				return null;
			}

			int slotCount = inventory.length;
			int requirementCount = this.requirements.size();
			int source = 0;
			int firstSlot = 1;
			int firstRequirement = firstSlot + slotCount;
			int sink = firstRequirement + requirementCount;
			// A tiny max-flow graph prevents overlapping alternatives from counting one stack twice.
			int[][] capacity = new int[sink + 1][sink + 1];
			int totalRequired = 0;

			for (int slot = 0; slot < slotCount; slot++) {
				ItemStack stack = inventory[slot];
				if (stack == null || stack.stackSize <= 0) {
					continue;
				}
				capacity[source][firstSlot + slot] = stack.stackSize;
				for (int requirement = 0; requirement < requirementCount; requirement++) {
					if (this.requirements.get(requirement).matches(stack)) {
						capacity[firstSlot + slot][firstRequirement + requirement] = stack.stackSize;
					}
				}
			}

			for (int requirement = 0; requirement < requirementCount; requirement++) {
				int count = this.requirements.get(requirement).count;
				capacity[firstRequirement + requirement][sink] = count;
				totalRequired += count;
			}

			int[][] residual = new int[capacity.length][capacity.length];
			for (int i = 0; i < capacity.length; i++) {
				residual[i] = Arrays.copyOf(capacity[i], capacity[i].length);
			}

			int flow = 0;
			int[] parent = new int[capacity.length];
			while (findAugmentingPath(residual, source, sink, parent)) {
				int pathFlow = Integer.MAX_VALUE;
				for (int node = sink; node != source; node = parent[node]) {
					pathFlow = Math.min(pathFlow, residual[parent[node]][node]);
				}
				for (int node = sink; node != source; node = parent[node]) {
					residual[parent[node]][node] -= pathFlow;
					residual[node][parent[node]] += pathFlow;
				}
				flow += pathFlow;
			}

			if (flow != totalRequired) {
				return null;
			}

			int[] allocation = new int[slotCount];
			for (int slot = 0; slot < slotCount; slot++) {
				allocation[slot] = capacity[source][firstSlot + slot] - residual[source][firstSlot + slot];
			}
			return allocation;
		}

		private static boolean findAugmentingPath(int[][] residual, int source, int sink, int[] parent) {
			Arrays.fill(parent, -1);
			parent[source] = source;
			Queue<Integer> queue = new ArrayDeque<Integer>();
			queue.add(Integer.valueOf(source));
			while (!queue.isEmpty()) {
				int node = queue.remove().intValue();
				for (int next = 0; next < residual.length; next++) {
					if (parent[next] >= 0 || residual[node][next] <= 0) {
						continue;
					}
					parent[next] = node;
					if (next == sink) {
						return true;
					}
					queue.add(Integer.valueOf(next));
				}
			}
			return false;
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
		private final List<ItemStack> alternatives;
		private final int count;

		IngredientRequirement(ItemStack ingredient, int count) {
			this(ingredient == null ? Collections.<ItemStack>emptyList() : Arrays.asList(ingredient), count);
		}

		IngredientRequirement(List<ItemStack> ingredients, int count) {
			this.alternatives = new ArrayList<ItemStack>();
			if (ingredients != null) {
				for (ItemStack ingredient : ingredients) {
					if (ingredient != null && ingredient.getItem() != null) {
						this.alternatives.add(ingredient.copy());
					}
				}
			}
			this.count = count;
		}

		private boolean isValid() {
			return !this.alternatives.isEmpty() && this.count > 0;
		}

		private boolean matches(ItemStack stack) {
			if (stack == null || stack.stackSize <= 0 || !this.isValid()) {
				return false;
			}
			for (ItemStack ingredient : this.alternatives) {
				if (stack.getItem() == ingredient.getItem()
						&& (ingredient.getItemDamage() == OreDictionary.WILDCARD_VALUE
							|| stack.getItemDamage() == ingredient.getItemDamage())) {
					return true;
				}
			}
			return false;
		}

		private ItemStack getRepresentative() {
			if (!this.isValid()) {
				return null;
			}
			ItemStack ingredient = this.alternatives.get(0).copy();
			ingredient.stackSize = this.count;
			return ingredient;
		}

		private List<ItemStack> getAlternatives() {
			List<ItemStack> result = new ArrayList<ItemStack>();
			for (ItemStack alternative : this.alternatives) {
				ItemStack ingredient = alternative.copy();
				ingredient.stackSize = this.count;
				result.add(ingredient);
			}
			return result;
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
