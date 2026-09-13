package zone.moddev.mc.ironagefurniture;

import net.minecraftforge.eventbus.api.bus.BusGroup;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import zone.moddev.mc.ironagefurniture.api.entity.Entities;
import zone.moddev.mc.ironagefurniture.client.renderer.ClientHandler;
import zone.moddev.mc.ironagefurniture.client.renderer.LightRendering;
import zone.moddev.mc.ironagefurniture.client.OptionalIntegrationResourcePacks;
import zone.moddev.mc.ironagefurniture.compat.LegacyFurnitureMappings;
import zone.moddev.mc.ironagefurniture.init.ModBOPBlocks;
import zone.moddev.mc.ironagefurniture.init.ModBWGBlocks;
import zone.moddev.mc.ironagefurniture.init.ModCreativeTab;
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
import zone.moddev.mc.ironagefurniture.proxy.CommonProxy;
import net.minecraftforge.fml.config.ModConfig;

import com.mojang.logging.LogUtils;
import net.minecraftforge.api.distmarker.Dist;
import org.slf4j.Logger;

@Mod(Ironagefurniture.MODID)
public class Ironagefurniture
{
    public static final String MODID = "ironagefurniture";
    public static final CommonProxy PROXY = DistExecutor.runForDist(() -> zone.moddev.mc.ironagefurniture.proxy.ClientProxy::new, () -> CommonProxy::new);
    private static final Logger LOGGER = LogUtils.getLogger();
    
	public Ironagefurniture(FMLJavaModLoadingContext context) {
		LOGGER.info("Iron Age Furniture Mod is loading...");
		
        BusGroup modBusGroup = context.getModBusGroup();
        
        ModVanillaChairs.REGISTER.register(modBusGroup);
        ModVanillaShieldChairs.REGISTER.register(modBusGroup);
		ModVanillaStools.REGISTER.register(modBusGroup);
		ModVanillaTallStools.REGISTER.register(modBusGroup);
		ModVanillaBench.REGISTER.register(modBusGroup);
		ModVanillaBackBench.REGISTER.register(modBusGroup);
		ModVanillaLights.REGISTER.register(modBusGroup);
		ModVanillaLogBench.REGISTER.register(modBusGroup);
		ModVanillaPaddedBench.REGISTER.register(modBusGroup);
		ModVanillaPaddedBackBench.REGISTER.register(modBusGroup);
		
		if (ModList.get().isLoaded("biomesoplenty")) {
			LOGGER.info("Iron Age Furniture Biomes O Plenty Integration is loading...");
			ModBOPBlocks.REGISTER.register(modBusGroup);
		}

		if (ModList.get().isLoaded("biomeswevegone")) {
			LOGGER.info("Iron Age Furniture Oh The Biomes We've Gone integration is loading...");
			ModBWGBlocks.REGISTER.register(modBusGroup);
		}
		
        ModItems.REGISTER.register(modBusGroup);
        ModCreativeTab.REGISTER.register(modBusGroup);
        Entities.REGISTER.register(modBusGroup);
        
        FMLCommonSetupEvent.getBus(modBusGroup).addListener(this::commonSetup);
        LegacyFurnitureMappings.registerRuntimeListener();
        
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
			OptionalIntegrationResourcePacks.register();
			net.minecraftforge.client.event.EntityRenderersEvent.RegisterRenderers.BUS
					.addListener(ClientHandler::onRegisterRenderers);
			FMLClientSetupEvent.getBus(modBusGroup).addListener(LightRendering::clientSetup);
        });
        
        context.registerConfig(ModConfig.Type.COMMON, IronAgeFurnitureConfiguration.SPEC);
        
    }
    
    private void commonSetup(final FMLCommonSetupEvent event)
    {

    }
}
