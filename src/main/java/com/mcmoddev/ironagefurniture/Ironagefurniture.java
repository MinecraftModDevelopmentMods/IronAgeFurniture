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
import com.mcmoddev.ironagefurniture.proxy.CommonProxy;
import net.minecraftforge.fml.config.ModConfig;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.CreativeModeTabEvent;
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
    
//    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
//    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
//    
//	public static final CreativeModeTab IAF_GROUP = new CreativeModeTab(MODID) {
//	    @Override
//	    public ItemStack makeIcon() {
//	        return new ItemStack(BlockObjectHolder.chair_wood_ironage_classic_dark_oak);
//	    }
//	};
//	
	public Ironagefurniture(FMLJavaModLoadingContext context) {
		
        IEventBus modEventBus = context.getModEventBus();
        
        ModVanillaChairs.REGISTER.register(modEventBus);
        ModItems.REGISTER.register(modEventBus);
        Entities.REGISTER.register(modEventBus);
        
        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);

        modEventBus.addListener(this::addCreative);
        
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
        	modEventBus.addListener(ClientHandler::onRegisterRenderers);
        });
        
        context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
        
    }
    
    private void commonSetup(final FMLCommonSetupEvent event)
    {
        // Some common setup code
//        LOGGER.info("HELLO FROM COMMON SETUP");
//
//        if (Config.logDirtBlock)
//            LOGGER.info("DIRT BLOCK >> {}", ForgeRegistries.BLOCKS.getKey(Blocks.DIRT));
//
//        LOGGER.info(Config.magicNumberIntroduction + Config.magicNumber);
//
//        Config.items.forEach((item) -> LOGGER.info("ITEM >> {}", item.toString()));
    }

    private void addCreative(CreativeModeTabEvent.BuildContents event)
    {
//        if (event.getTab() == CreativeModeTabs.BUILDING_BLOCKS)
//            event.accept(EXAMPLE_BLOCK_ITEM);
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            // Some client setup code
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }
    }
}
