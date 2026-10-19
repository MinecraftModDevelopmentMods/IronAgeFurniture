package zone.moddev.mc.ironagefurniture;

import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import zone.moddev.mc.ironagefurniture.api.CreativeModeBreakTracker;
import zone.moddev.mc.ironagefurniture.api.entity.Entities;
import zone.moddev.mc.ironagefurniture.compat.LegacyFurnitureMappings;
import zone.moddev.mc.ironagefurniture.init.ModCreativeTab;
import zone.moddev.mc.ironagefurniture.init.PhaseFourFurniture;
import zone.moddev.mc.ironagefurniture.init.PhaseFourLighting;
import zone.moddev.mc.ironagefurniture.init.ModBOPBlocks;
import zone.moddev.mc.ironagefurniture.init.PhaseFourBopFurniture;
import zone.moddev.mc.ironagefurniture.client.OptionalIntegrationResourcePacks;
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
		
        PhaseFourFurniture.REGISTER.register(modEventBus);
        if (ModList.get().isLoaded("biomesoplenty")) {
            ModBOPBlocks.REGISTER.register(modEventBus);
            PhaseFourBopFurniture.REGISTER.register(modEventBus);
        }
        OptionalIntegrationResourcePacks.register(modEventBus);
        PhaseFourLighting.BLOCKS.register(modEventBus);
        PhaseFourLighting.BLOCK_ENTITIES.register(modEventBus);
        zone.moddev.mc.ironagefurniture.init.PhaseFourBlockEntities.REGISTER.register(modEventBus);
        zone.moddev.mc.ironagefurniture.init.PhaseFourRecipes.CONDITIONS.register(modEventBus);
        zone.moddev.mc.ironagefurniture.init.PhaseFourRecipes.SERIALIZERS.register(modEventBus);
        ModItems.REGISTER.register(modEventBus);
        ModCreativeTab.REGISTER.register(modEventBus);
        Entities.REGISTER.register(modEventBus);
        LegacyFurnitureMappings.registerAliases(modEventBus);
        
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(zone.moddev.mc.ironagefurniture.compat.FurnitureConfigurationReload::configReloaded);

        NeoForge.EVENT_BUS.register(this);
		NeoForge.EVENT_BUS.register(new CreativeModeBreakTracker());
        
        // 26.1.2 synchronizes gameplay settings through its world/server config;
        // the newer global SYNCED config type does not exist on this loader.
        modContainer.registerConfig(ModConfig.Type.SERVER, IronAgeFurnitureConfiguration.SYNCED_SPEC);
        
    }
    
    private void commonSetup(final FMLCommonSetupEvent event)
    {

    }


    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {

    }

}
