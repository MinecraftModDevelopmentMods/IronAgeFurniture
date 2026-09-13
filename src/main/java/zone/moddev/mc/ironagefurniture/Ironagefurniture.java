package zone.moddev.mc.ironagefurniture;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import zone.moddev.mc.ironagefurniture.api.entity.Entities;
import zone.moddev.mc.ironagefurniture.client.renderer.ClientHandler;
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
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.slf4j.Logger;

@Mod(Ironagefurniture.MODID)
public class Ironagefurniture
{
    public static final String MODID = "ironagefurniture";
    public static final CommonProxy PROXY = DistExecutor.runForDist(() -> zone.moddev.mc.ironagefurniture.proxy.ClientProxy::new, () -> CommonProxy::new);
    private static final Logger LOGGER = LogUtils.getLogger();
    
	public Ironagefurniture(FMLJavaModLoadingContext context) {
		LOGGER.info("Iron Age Furniture Mod is loading...");
		
        IEventBus modEventBus = context.getModEventBus();
        
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
		
        ModItems.REGISTER.register(modEventBus);
        ModCreativeTab.REGISTER.register(modEventBus);
        Entities.REGISTER.register(modEventBus);
        
        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);
        
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
        	modEventBus.addListener(ClientHandler::onRegisterRenderers);
        });
        
        context.registerConfig(ModConfig.Type.COMMON, IronAgeFurnitureConfiguration.SPEC);
        
    }
    
    private void commonSetup(final FMLCommonSetupEvent event)
    {

    }


    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {

    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {

        }
    }
}
