package com.mcmoddev.ironagefurniture;

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
	public static boolean GENERATE_WOOD_CABINETS = true;
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
		GENERATE_WOOD_CABINETS = config.getBoolean("GENERATE_WOOD_CABINETS", "options", GENERATE_WOOD_CABINETS, "If true, then wooden cabinets will be generated");
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

		CFM_CONVERSION_RECIPES = config.getBoolean("CFM_CONVERSION_RECIPES", "options", CFM_CONVERSION_RECIPES, "If true, recipes for converting chairs from Crayfish Furniture Mod will be added");
		config.save();
	}
}
