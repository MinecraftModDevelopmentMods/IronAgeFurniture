package zone.moddev.mc.ironagefurniture;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class IronAgeFurnitureConfiguration {	
	public static boolean GENERATE_CLASSIC_CHAIRS = true;
	public static boolean GENERATE_DINING_CHAIRS = true;
	public static boolean GENERATE_WINGBACK_CHAIRS = true;
	public static boolean GENERATE_THRONES = true;
	public static boolean GENERATE_CANOPY_BEDS = true;
	public static boolean GENERATE_WOOD_BEDS = true;
	public static boolean GENERATE_DINING_TABLES = true;
	public static boolean GENERATE_LOW_TABLES = true;
	public static boolean GENERATE_WALL_SHELVES = true;
	public static boolean GENERATE_BOTTLE_RACKS = true;
	public static boolean GENERATE_WOOD_CABINETS = true;
	public static boolean GENERATE_WOOD_BARRELS = true;
	public static boolean GENERATE_SIDE_BARRELS = true;
	public static boolean GENERATE_FOUDRES = true;
	public static boolean GENERATE_POT_STILLS = true;
	public static boolean GENERATE_FLUID_BOTTLES = true;
	public static boolean GENERATE_DRINKWARE = true;
	public static boolean GENERATE_INNKEEPERS = true;
	public static boolean GENERATE_IRON_NUGGETS = true;
	public static boolean GENERATE_ORNAMENTS = true;
	public static boolean GENERATE_SHIELD_CHAIRS = true;
	public static boolean GENERATE_SHORT_STOOLS = true;
	public static boolean GENERATE_TALL_STOOLS = true;
	public static boolean GENERATE_WOOD_BENCHES = true;
	public static boolean GENERATE_LIGHTS = true;
	public static boolean GENERATE_SCONCES = true;
	public static boolean GENERATE_CANDLES = true;
	public static boolean GENERATE_GLOW_LAMPS = true;
	public static boolean GENERATE_LAVA_LAMPS = true;
	public static boolean GENERATE_REDSTONE_LAMPS = true;
	public static boolean GENERATE_CHAINS = true;
	public static boolean GENERATE_CHANDELIERS = true;
	public static boolean GENERATE_GRAND_CHANDELIERS = true;
	public static boolean CFM_CONVERSION_RECIPES = true;
	public static boolean INTEGRATION_BIOMESOPLENTY = true;
	public static boolean INTEGRATION_NATURA = true;
	public static boolean INTEGRATION_FORESTRY = true;
	public static boolean INTEGRATION_IMMERSIVEENGINEERING = true;
	public static boolean INTEGRATION_BASEMETALS = true;
	public static boolean INTEGRATION_HARVESTCRAFT = true;
	public static boolean INTEGRATION_MINERALOGY = true;
	public static boolean INTEGRATION_MCA = true;
	public static boolean ENABLE_DRINK_EFFECTS = true;
	public static int DRINK_EFFECT_RECOVERY_TICKS = 1200;
	public static int INN_RADIUS_HORIZONTAL = 16;
	public static int INN_RADIUS_VERTICAL = 8;
	public static int INNKEEPER_DAILY_PURSE = 32;
	public static int DRINK_VALUE_PERCENT = 100;
	public static final int DEFAULT_DRINK_TIME_MULTIPLIER = 10;
	private static final int MIN_DRINK_TIME_MULTIPLIER = 1;
	private static final int MAX_DRINK_TIME_MULTIPLIER = 1200;
	public static int DRINK_TIME_MULTIPLIER = DEFAULT_DRINK_TIME_MULTIPLIER;

	public static int scaleDrinkTicks(int ticks) {
		return scaleDrinkTicks((long)ticks);
	}

	public static int scaleDrinkTicks(long ticks) {
		if (ticks <= 0L) {
			return 0;
		}

		long multiplier = Math.max(MIN_DRINK_TIME_MULTIPLIER, DRINK_TIME_MULTIPLIER);
		long scaledTicks = ticks * multiplier / DEFAULT_DRINK_TIME_MULTIPLIER;

		if (scaledTicks <= 0L) {
			return 1;
		}

		return scaledTicks > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int)scaledTicks;
	}
	
	public static void init(FMLPreInitializationEvent event) {
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
    	config.load();
    	
    	INTEGRATION_BIOMESOPLENTY = config.getBoolean("INTEGRATION_BIOMESOPLENTY", "integration", INTEGRATION_BIOMESOPLENTY, "If true, then furniture for BiomesOPlenty will be generated");
    	INTEGRATION_NATURA = config.getBoolean("INTEGRATION_NATURA", "integration", INTEGRATION_NATURA, "If true, then furniture for Natura will be generated");
    	INTEGRATION_FORESTRY = config.getBoolean("INTEGRATION_FORESTRY", "integration", INTEGRATION_FORESTRY, "If true, then furniture for Forestry will be generated");
    	INTEGRATION_IMMERSIVEENGINEERING = config.getBoolean("INTEGRATION_IMMERSIVEENGINEERING", "integration", INTEGRATION_IMMERSIVEENGINEERING, "If true, then furniture for Immersive Engineerig will be generated");
    	INTEGRATION_BASEMETALS = config.getBoolean("INTEGRATION_BASEMETALS", "integration", INTEGRATION_BASEMETALS, "If true, then Base Metals lighting variants will be generated when Base Metals is loaded");
		INTEGRATION_HARVESTCRAFT = config.getBoolean("INTEGRATION_HARVESTCRAFT", "integration", INTEGRATION_HARVESTCRAFT, "If true, then HarvestCraft foods will use shaped table and shelf display rendering when HarvestCraft is loaded");
		INTEGRATION_MINERALOGY = config.getBoolean("INTEGRATION_MINERALOGY", "integration", INTEGRATION_MINERALOGY, "If true, then Minecraft Mineralogy rock salt lamps can be placed in sconces and grand chandeliers when Mineralogy is loaded");
		INTEGRATION_MCA = config.getBoolean("INTEGRATION_MCA", "integration", INTEGRATION_MCA,
			"If true, nearby adult Minecraft Comes Alive villagers can be assigned to an inn without changing their MCA profession");
    	
    	GENERATE_SHIELD_CHAIRS = config.getBoolean("GENERATE_SHIELD_CHAIRS", "options", GENERATE_SHIELD_CHAIRS, "If true, then shield chairs will be generated");
		GENERATE_CLASSIC_CHAIRS = config.getBoolean("GENERATE_CLASSIC_CHAIRS", "options", GENERATE_CLASSIC_CHAIRS, "If true, then classic chairs will be generated");
		GENERATE_DINING_CHAIRS = config.getBoolean("GENERATE_DINING_CHAIRS", "options", GENERATE_DINING_CHAIRS, "If true, then dining chairs will be generated");
		GENERATE_WINGBACK_CHAIRS = config.getBoolean("GENERATE_WINGBACK_CHAIRS", "options", GENERATE_WINGBACK_CHAIRS, "If true, then wingback chairs will be generated");
		GENERATE_THRONES = config.getBoolean("GENERATE_THRONES", "options", GENERATE_THRONES, "If true, then throne chairs will be generated");
		GENERATE_CANOPY_BEDS = config.getBoolean("GENERATE_CANOPY_BEDS", "options", GENERATE_CANOPY_BEDS, "If true, then canopy beds will be generated");
		GENERATE_WOOD_BEDS = config.getBoolean("GENERATE_WOOD_BEDS", "options", GENERATE_WOOD_BEDS, "If true, then wooden beds will be generated");
		GENERATE_DINING_TABLES = config.getBoolean("GENERATE_DINING_TABLES", "options", GENERATE_DINING_TABLES, "If true, then dining tables will be generated");
		GENERATE_LOW_TABLES = config.getBoolean("GENERATE_LOW_TABLES", "options", GENERATE_LOW_TABLES, "If true, then low tables will be generated");
		GENERATE_WALL_SHELVES = config.getBoolean("GENERATE_WALL_SHELVES", "options", GENERATE_WALL_SHELVES, "If true, then wall shelves will be generated");
		GENERATE_BOTTLE_RACKS = config.getBoolean("GENERATE_BOTTLE_RACKS", "options", GENERATE_BOTTLE_RACKS, "If true, then bottle racks will be generated");
		GENERATE_WOOD_CABINETS = config.getBoolean("GENERATE_WOOD_CABINETS", "options", GENERATE_WOOD_CABINETS, "If true, then wooden cabinets will be generated");
		GENERATE_WOOD_BARRELS = config.getBoolean("GENERATE_WOOD_BARRELS", "options", GENERATE_WOOD_BARRELS, "If true, then wooden barrels will be generated");
		GENERATE_SIDE_BARRELS = config.getBoolean("GENERATE_SIDE_BARRELS", "options", GENERATE_SIDE_BARRELS, "If true, then side barrels will be generated when wooden barrels are enabled");
		GENERATE_FOUDRES = config.getBoolean("GENERATE_FOUDRES", "options", GENERATE_FOUDRES, "If true, then foudres will be generated when wooden barrels are enabled");
		GENERATE_POT_STILLS = config.getBoolean("GENERATE_POT_STILLS", "options", GENERATE_POT_STILLS, "If true, then pot stills will be generated when wooden barrels, foudres, and at least one drink container family are enabled");
		GENERATE_FLUID_BOTTLES = config.getBoolean("GENERATE_FLUID_BOTTLES", "options", GENERATE_FLUID_BOTTLES, "If true, then generic Iron Age fluid bottles will be generated");
		GENERATE_DRINKWARE = config.getBoolean("GENERATE_DRINKWARE", "options", GENERATE_DRINKWARE, "If true, then reusable tankards, glasses, shot glasses, and mugs will be generated");
		GENERATE_INNKEEPERS = config.getBoolean("GENERATE_INNKEEPERS", "options", GENERATE_INNKEEPERS,
			"If true, then Hanging Inn Signs and Innkeeper appraisal are enabled");
		ENABLE_DRINK_EFFECTS = config.getBoolean("ENABLE_DRINK_EFFECTS", "options", ENABLE_DRINK_EFFECTS,
			"If true, drinks apply non-lethal effects based on serving size and Strength");
		DRINK_EFFECT_RECOVERY_TICKS = config.getInt("DRINK_EFFECT_RECOVERY_TICKS", "options",
			DRINK_EFFECT_RECOVERY_TICKS, 100, 72000,
			"Ticks required to recover one Effect serving. 1200 is one Minecraft minute");
		INN_RADIUS_HORIZONTAL = config.getInt("INN_RADIUS_HORIZONTAL", "options", INN_RADIUS_HORIZONTAL,
			4, 64, "Horizontal radius controlled by a Hanging Inn Sign");
		INN_RADIUS_VERTICAL = config.getInt("INN_RADIUS_VERTICAL", "options", INN_RADIUS_VERTICAL,
			2, 32, "Vertical radius controlled by a Hanging Inn Sign");
		INNKEEPER_DAILY_PURSE = config.getInt("INNKEEPER_DAILY_PURSE", "options",
			INNKEEPER_DAILY_PURSE, 1, 256, "Emeralds available to each Innkeeper per Minecraft day");
		DRINK_VALUE_PERCENT = config.getInt("DRINK_VALUE_PERCENT", "options", DRINK_VALUE_PERCENT,
			1, 1000, "Innkeeper appraisal value as a percentage of the default values");
		GENERATE_IRON_NUGGETS = config.getBoolean("GENERATE_IRON_NUGGETS", "options", GENERATE_IRON_NUGGETS, "If true, then iron nuggets will be generated");
		GENERATE_ORNAMENTS = config.getBoolean("GENERATE_ORNAMENTS", "options", GENERATE_ORNAMENTS, "If true, then decorative ornaments will be generated");
	
		GENERATE_SHORT_STOOLS = config.getBoolean("GENERATE_SHORT_STOOLS", "options", GENERATE_SHORT_STOOLS, "If true, then short stools will be generated");
		GENERATE_TALL_STOOLS = config.getBoolean("GENERATE_TALL_STOOLS", "options", GENERATE_TALL_STOOLS, "If true, then tall stools will be generated");
		
		GENERATE_WOOD_BENCHES = config.getBoolean("GENERATE_WOOD_BENCHES", "options", GENERATE_WOOD_BENCHES, "If true, then wooden benches will be generated");

		GENERATE_LIGHTS = config.getBoolean("GENERATE_LIGHTS", "options", GENERATE_LIGHTS, "If true, then lighting blocks and items will be generated");
		GENERATE_SCONCES = config.getBoolean("GENERATE_SCONCES", "options", GENERATE_SCONCES, "If true, then sconces and sconce light variants will be generated");
		GENERATE_CANDLES = config.getBoolean("GENERATE_CANDLES", "options", GENERATE_CANDLES, "If true, then tallow, candles, and candle variants will be generated");
		GENERATE_GLOW_LAMPS = config.getBoolean("GENERATE_GLOW_LAMPS", "options", GENERATE_GLOW_LAMPS, "If true, then glow lamps and glow lamp variants will be generated");
		GENERATE_LAVA_LAMPS = config.getBoolean("GENERATE_LAVA_LAMPS", "options", GENERATE_LAVA_LAMPS, "If true, then lava lamps and lava lamp variants will be generated");
		GENERATE_REDSTONE_LAMPS = config.getBoolean("GENERATE_REDSTONE_LAMPS", "options", GENERATE_REDSTONE_LAMPS, "If true, then redstone lamps and redstone lamp variants will be generated");
		GENERATE_CHAINS = config.getBoolean("GENERATE_CHAINS", "options", GENERATE_CHAINS, "If true, then chain blocks will be generated");
		GENERATE_CHANDELIERS = config.getBoolean("GENERATE_CHANDELIERS", "options", GENERATE_CHANDELIERS, "If true, then chandelier blocks will be generated");
		GENERATE_GRAND_CHANDELIERS = config.getBoolean("GENERATE_GRAND_CHANDELIERS", "options", GENERATE_GRAND_CHANDELIERS, "If true, then grand chandelier blocks will be generated");
		DRINK_TIME_MULTIPLIER = config.getInt("DRINK_TIME_MULTIPLIER", "options", DRINK_TIME_MULTIPLIER,
			MIN_DRINK_TIME_MULTIPLIER, MAX_DRINK_TIME_MULTIPLIER,
			"Base timing multiplier for foudre brewing, drink aging, and pot still distillation. 10 is the default speed, 1 is 10x faster, and 120 is 12x slower.");

		CFM_CONVERSION_RECIPES = config.getBoolean("CFM_CONVERSION_RECIPES", "options", CFM_CONVERSION_RECIPES, "If true, recipes for converting chairs from Crayfish Furniture Mod will be added");
		config.save();
	}
}
