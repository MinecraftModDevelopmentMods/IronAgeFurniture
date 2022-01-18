package com.mcmoddev.ironagefurniture;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import com.mcmoddev.ironagefurniture.proxy.CommonProxy;
import com.mcmoddev.ironagefurniture.registers.entities;
import net.minecraftforge.fml.config.ModConfig;

@Mod(Ironagefurniture.MODID)
public class Ironagefurniture
{
    public static final String MODID = "ironagefurniture";
    public static final String VERSION = "0.2.0";
    public static final CommonProxy PROXY = DistExecutor.runForDist(() -> com.mcmoddev.ironagefurniture.proxy.ClientProxy::new, () -> CommonProxy::new);

	public static final CreativeModeTab IAF_GROUP = new CreativeModeTab(MODID) {
	    @Override
	    public ItemStack makeIcon() {
	        return new ItemStack(BlockObjectHolder.chair_wood_ironage_classic_dark_oak);
	    }
	};
	
	public Ironagefurniture() {
        entities.REGISTER.register(FMLJavaModLoadingContext.get().getModEventBus());
		
		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, IronAgeFurnitureConfiguration.clientSpec);
		ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, IronAgeFurnitureConfiguration.clientSpec);
		
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onCommonSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClientSetup);
		
        MinecraftForge.EVENT_BUS.register(this);
    }

	 private void onCommonSetup(FMLCommonSetupEvent event) {
        PROXY.onSetupCommon();
    }

    private void onClientSetup(FMLClientSetupEvent event) {
        PROXY.onSetupClient();
    }
}
