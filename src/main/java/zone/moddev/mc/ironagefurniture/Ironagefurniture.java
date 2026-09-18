package zone.moddev.mc.ironagefurniture;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import zone.moddev.mc.ironagefurniture.proxy.CommonProxy;
import zone.moddev.mc.ironagefurniture.registers.entities;
import zone.moddev.mc.ironagefurniture.api.CreativeModeBreakTracker;
import net.minecraftforge.fml.config.ModConfig;

@Mod(Ironagefurniture.MODID)
public class Ironagefurniture
{
    public static final String MODID = "ironagefurniture";
    public static final String VERSION = "0.3.0.116051";
    public static final CommonProxy PROXY = DistExecutor.runForDist(() -> zone.moddev.mc.ironagefurniture.proxy.ClientProxy::new, () -> CommonProxy::new);

	public static final ItemGroup IAF_GROUP = new ItemGroup(MODID) {
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
        MinecraftForge.EVENT_BUS.register(new CreativeModeBreakTracker());
    }

	 private void onCommonSetup(FMLCommonSetupEvent event) {
        PROXY.onSetupCommon();
    }

    private void onClientSetup(FMLClientSetupEvent event) {
        PROXY.onSetupClient();
    }
}
