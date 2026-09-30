package zone.moddev.mc.ironagefurniture;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class IronAgeFurnitureConfiguration {	
	public static boolean GENERATE_CLASSIC_CHAIRS = true;
	public static boolean GENERATE_WINGBACK_CHAIRS = true;
	public static boolean GENERATE_THRONES = true;
	public static boolean GENERATE_CANOPY_BEDS = true;
	public static boolean GENERATE_WOOD_BEDS = true;
	public static boolean GENERATE_SHIELD_CHAIRS = true;
	public static boolean GENERATE_SHORT_STOOLS = true;
	public static boolean GENERATE_TALL_STOOLS = true;
	public static boolean GENERATE_WOOD_BENCHES = true;
	public static boolean GENERATE_LIGHTS = true;
	public static boolean GENERATE_SCONCES = true;
	public static boolean GENERATE_GLOW_LAMPS = true;
	public static boolean GENERATE_LAVA_LAMPS = true;
	public static boolean GENERATE_REDSTONE_LAMPS = true;
	public static boolean CFM_CONVERSION_RECIPES = true;
	public static boolean INTEGRATION_BIOMESOPLENTY = true;
	public static boolean INTEGRATION_NATURA = true;
	public static boolean INTEGRATION_FORESTRY = true;
	public static boolean INTEGRATION_IMMERSIVEENGINEERING = true;
	public static boolean INTEGRATION_BASEMETALS = true;
	public static boolean INTEGRATION_MINERALOGY = true;
	
	public static void init(FMLPreInitializationEvent event) {
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
    	config.load();
    	
    	INTEGRATION_BIOMESOPLENTY = config.getBoolean("INTEGRATION_BIOMESOPLENTY", "integration", INTEGRATION_BIOMESOPLENTY, "If true, then furniture for BiomesOPlenty will be generated");
    	INTEGRATION_NATURA = config.getBoolean("INTEGRATION_NATURA", "integration", INTEGRATION_NATURA, "If true, then furniture for Natura will be generated");
    	INTEGRATION_FORESTRY = config.getBoolean("INTEGRATION_FORESTRY", "integration", INTEGRATION_FORESTRY, "If true, then furniture for Forestry will be generated");
    	INTEGRATION_IMMERSIVEENGINEERING = config.getBoolean("INTEGRATION_IMMERSIVEENGINEERING", "integration", INTEGRATION_IMMERSIVEENGINEERING, "If true, then furniture for Immersive Engineerig will be generated");
		INTEGRATION_BASEMETALS = config.getBoolean("INTEGRATION_BASEMETALS", "integration", INTEGRATION_BASEMETALS, "Enable Base Metals sconce materials when the mod is installed");
		INTEGRATION_MINERALOGY = config.getBoolean("INTEGRATION_MINERALOGY", "integration", INTEGRATION_MINERALOGY, "Enable rock-salt sconces when Mineralogy is installed");
    	
    	GENERATE_SHIELD_CHAIRS = config.getBoolean("GENERATE_SHIELD_CHAIRS", "options", GENERATE_SHIELD_CHAIRS, "If true, then shield chairs will be generated");
		GENERATE_CLASSIC_CHAIRS = config.getBoolean("GENERATE_CLASSIC_CHAIRS", "options", GENERATE_CLASSIC_CHAIRS, "If true, then classic chairs will be generated");
		GENERATE_WINGBACK_CHAIRS = config.getBoolean("GENERATE_WINGBACK_CHAIRS", "options", GENERATE_WINGBACK_CHAIRS, "If true, wingback chairs will be generated for available classic-chair woods");
		GENERATE_THRONES = config.getBoolean("GENERATE_THRONES", "options", GENERATE_THRONES, "If true, thrones will be generated for available wingback-chair woods");
		GENERATE_CANOPY_BEDS = config.getBoolean("GENERATE_CANOPY_BEDS", "options", GENERATE_CANOPY_BEDS, "If true, canopy beds will be generated for available woods");
		GENERATE_WOOD_BEDS = config.getBoolean("GENERATE_WOOD_BEDS", "options", GENERATE_WOOD_BEDS, "If true, wooden beds will be generated for available woods");
	
		GENERATE_SHORT_STOOLS = config.getBoolean("GENERATE_SHORT_STOOLS", "options", GENERATE_SHORT_STOOLS, "If true, then short stools will be generated");
		GENERATE_TALL_STOOLS = config.getBoolean("GENERATE_TALL_STOOLS", "options", GENERATE_TALL_STOOLS, "If true, then tall stools will be generated");
		GENERATE_WOOD_BENCHES = config.getBoolean("GENERATE_WOOD_BENCHES", "options", GENERATE_WOOD_BENCHES, "If true, then wooden benches will be generated");
		GENERATE_LIGHTS = config.getBoolean("GENERATE_LIGHTS", "options", GENERATE_LIGHTS, "If true, then lighting blocks and items will be generated");
		GENERATE_SCONCES = config.getBoolean("GENERATE_SCONCES", "options", GENERATE_SCONCES, "If true, then iron sconces will be generated");
		GENERATE_GLOW_LAMPS = config.getBoolean("GENERATE_GLOW_LAMPS", "options", GENERATE_GLOW_LAMPS, "If true, then glow lamps will be generated");
		GENERATE_LAVA_LAMPS = config.getBoolean("GENERATE_LAVA_LAMPS", "options", GENERATE_LAVA_LAMPS, "If true, then lava lamps will be generated");
		GENERATE_REDSTONE_LAMPS = config.getBoolean("GENERATE_REDSTONE_LAMPS", "options", GENERATE_REDSTONE_LAMPS, "If true, then redstone lamps will be generated");
		
		CFM_CONVERSION_RECIPES = config.getBoolean("CFM_CONVERSION_RECIPES", "options", CFM_CONVERSION_RECIPES, "If true, recipes for converting chairs from Crayfish Furniture Mod will be added");
		config.save();
	}
}
