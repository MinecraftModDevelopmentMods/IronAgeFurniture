package zone.moddev.mc.ironagefurniture;

import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import zone.moddev.mc.ironagefurniture.api.CreativeModeBreakTracker;
import zone.moddev.mc.ironagefurniture.api.entity.Entities;
import zone.moddev.mc.ironagefurniture.compat.LegacyFurnitureMappings;
import zone.moddev.mc.ironagefurniture.init.ModBOPBlocks;
import zone.moddev.mc.ironagefurniture.init.ModBWGBlocks;
import zone.moddev.mc.ironagefurniture.init.ModCreativeTab;
import zone.moddev.mc.ironagefurniture.init.ModIEBlocks;
import zone.moddev.mc.ironagefurniture.init.ModVanillaBackBench;
import zone.moddev.mc.ironagefurniture.init.ModVanillaBench;
import zone.moddev.mc.ironagefurniture.init.ModVanillaChairs;
import zone.moddev.mc.ironagefurniture.init.ModVanillaLights;
import zone.moddev.mc.ironagefurniture.init.ModVanillaLogBench;
import zone.moddev.mc.ironagefurniture.init.ModVanillaPaddedBackBench;
import zone.moddev.mc.ironagefurniture.init.ModVanillaPaddedBench;
import zone.moddev.mc.ironagefurniture.init.ModVanillaShieldChairs;
import zone.moddev.mc.ironagefurniture.init.ModVanillaStools;
import zone.moddev.mc.ironagefurniture.init.ModVanillaTallStools;
import net.neoforged.fml.config.ModConfig;

import com.mojang.logging.LogUtils;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.bus.api.SubscribeEvent;
import org.slf4j.Logger;

@Mod(Ironagefurniture.MODID)
public class Ironagefurniture
{
    public static final String MODID = "ironagefurniture";
    private static final Logger LOGGER = LogUtils.getLogger();
    
	public Ironagefurniture(IEventBus modEventBus, ModContainer modContainer) {
		LOGGER.info("Iron Age Furniture Mod is loading...");
        
        ModVanillaChairs.REGISTER.register(modEventBus);
        ModVanillaShieldChairs.REGISTER.register(modEventBus);
		ModVanillaStools.REGISTER.register(modEventBus);
		ModVanillaTallStools.REGISTER.register(modEventBus);
		ModVanillaBench.REGISTER.register(modEventBus);
		ModVanillaBackBench.REGISTER.register(modEventBus);
		ModVanillaLights.REGISTER.register(modEventBus);
		ModVanillaLogBench.REGISTER.register(modEventBus);
		ModVanillaPaddedBench.REGISTER.register(modEventBus);
		ModVanillaPaddedBackBench.REGISTER.register(modEventBus);
		
		if (ModList.get().isLoaded("biomesoplenty")) {
			LOGGER.info("Iron Age Furniture Biomes O Plenty Integration is loading...");
			ModBOPBlocks.REGISTER.register(modEventBus);
		}

		if (ModList.get().isLoaded("biomeswevegone")) {
			LOGGER.info("Iron Age Furniture Oh The Biomes We've Gone integration is loading...");
			ModBWGBlocks.REGISTER.register(modEventBus);
		}

		if (ModList.get().isLoaded("immersiveengineering")) {
			LOGGER.info("Iron Age Furniture Immersive Engineering integration is loading...");
			ModIEBlocks.REGISTER.register(modEventBus);
		}
		
        ModItems.REGISTER.register(modEventBus);
        ModCreativeTab.REGISTER.register(modEventBus);
        Entities.REGISTER.register(modEventBus);
        LegacyFurnitureMappings.registerAliases(modEventBus);
        
        modEventBus.addListener(this::commonSetup);

        NeoForge.EVENT_BUS.register(this);
		NeoForge.EVENT_BUS.register(new CreativeModeBreakTracker());
        
        modContainer.registerConfig(ModConfig.Type.COMMON, IronAgeFurnitureConfiguration.SPEC);
        
    }
    
    private void commonSetup(final FMLCommonSetupEvent event)
    {

    }


    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {

    }

}
