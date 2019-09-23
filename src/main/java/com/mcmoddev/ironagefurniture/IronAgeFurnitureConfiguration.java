package com.mcmoddev.ironagefurniture;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class IronAgeFurnitureConfiguration {
	public static boolean GENERATE_CHAIRS = true;
	
	public static void init(FMLPreInitializationEvent event) {
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
    	config.load();
    	
		GENERATE_CHAIRS = config.getBoolean("GENERATE_CHAIRS", "options", GENERATE_CHAIRS, "If true, then chairs will be generated");
	}
}
