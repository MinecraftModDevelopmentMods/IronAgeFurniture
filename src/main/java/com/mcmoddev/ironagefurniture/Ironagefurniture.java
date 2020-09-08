package com.mcmoddev.ironagefurniture;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraftforge.fml.config.ModConfig;

@Mod(Ironagefurniture.MODID)
public class Ironagefurniture
{
    public static final String MODID = "ironagefurniture";
    public static final String VERSION = "0.1.0";
    

	private static final Logger LOGGER = LogManager.getLogger();
	
	// new stuff
		public Ironagefurniture() {
			ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, IronAgeFurnitureConfiguration.clientSpec);
			
	        // Register the setup method for modloading
	        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
	        // Register the enqueueIMC method for modloading
	        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
	        // Register the processIMC method for modloading
	        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
	        // Register the doClientStuff method for modloading
	        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

	        // Register ourselves for server and other game events we are interested in
	        MinecraftForge.EVENT_BUS.register(this);
	    }

		private void setup(final FMLCommonSetupEvent event)
	    {
	        // example preinit code
	        LOGGER.info("HELLO FROM PREINIT");
	        //
	   
	    }

	    private void doClientStuff(final FMLClientSetupEvent event) {
	        // do something that can only be done on the client
	    }

	    private void enqueueIMC(final InterModEnqueueEvent event)
	    {

	    }

	    private void processIMC(final InterModProcessEvent event)
	    {

	    }
}
