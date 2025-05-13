package com.mcmoddev.ironagefurniture;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import com.mcmoddev.ironagefurniture.api.entity.Entities;
import com.mcmoddev.ironagefurniture.client.renderer.ClientHandler;
import com.mcmoddev.ironagefurniture.init.ModVanillaBackBench;
import com.mcmoddev.ironagefurniture.init.ModVanillaChairs;
import com.mcmoddev.ironagefurniture.init.ModVanillaShieldChairs;
import com.mcmoddev.ironagefurniture.init.ModVanillaStools;
import com.mcmoddev.ironagefurniture.init.ModVanillaTallStools;
import com.mcmoddev.ironagefurniture.proxy.CommonProxy;
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
    public static final String VERSION = "0.2.5.0";
    public static final CommonProxy PROXY = DistExecutor.runForDist(() -> com.mcmoddev.ironagefurniture.proxy.ClientProxy::new, () -> CommonProxy::new);
    private static final Logger LOGGER = LogUtils.getLogger();
    
	public Ironagefurniture(FMLJavaModLoadingContext context) {
		LOGGER.info("Iron Age Furniture Mod is loading...");
		
        IEventBus modEventBus = context.getModEventBus();
        
        ModVanillaChairs.REGISTER.register(modEventBus);
        ModVanillaShieldChairs.REGISTER.register(modEventBus);
		ModVanillaStools.REGISTER.register(modEventBus);
		ModVanillaTallStools.REGISTER.register(modEventBus);
		ModVanillaBackBench.REGISTER.register(modEventBus);
		
        ModItems.REGISTER.register(modEventBus);
        Entities.REGISTER.register(modEventBus);
        
        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);
        
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
        	modEventBus.addListener(ClientHandler::onRegisterRenderers);
        	modEventBus.addListener(ClientHandler::onRegisterCreativeTab);
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
