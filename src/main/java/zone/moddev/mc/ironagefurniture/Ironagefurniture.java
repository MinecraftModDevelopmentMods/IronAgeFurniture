package zone.moddev.mc.ironagefurniture;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
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
import zone.moddev.mc.ironagefurniture.migration.LegacyWorldDataHook;
import net.minecraftforge.fml.config.ModConfig;

@Mod(Ironagefurniture.MODID)
public class Ironagefurniture
{
    public static final String MODID = "ironagefurniture";
    public static final String VERSION = "0.4.0.114041";
    public static final CommonProxy PROXY = DistExecutor.runForDist(() -> zone.moddev.mc.ironagefurniture.proxy.ClientProxy::new, () -> CommonProxy::new);

    public static final ItemGroup IAF_CHAIRS_GROUP = furnitureGroup("chairs", "chair_wood_ironage_classic_dark_oak", Items.OAK_STAIRS);
    public static final ItemGroup IAF_BENCHES_GROUP = furnitureGroup("benches", "chair_wood_ironage_bench_single_dark_oak", Items.OAK_SLAB);
    public static final ItemGroup IAF_BEDS_GROUP = furnitureGroup("beds", "bed_wood_foot_dark_oak", Items.RED_BED);
    public static final ItemGroup IAF_LIGHTS_GROUP = furnitureGroup("lights", "light_metal_ironage_sconce_floor_empty_iron", Items.TORCH);

    /** @deprecated Use the category-specific groups for new furniture. */
    @Deprecated
    public static final ItemGroup IAF_GROUP = IAF_CHAIRS_GROUP;

    private static ItemGroup furnitureGroup(String category, String icon, Item fallback) {
        return new ItemGroup(MODID + "." + category) {
            @Override public ItemStack createIcon() {
                // A disabled furniture family must not leave its tab with an air icon.
                Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(MODID, icon));
                return new ItemStack(item == null || item == Items.AIR
                        || !zone.moddev.mc.ironagefurniture.api.items.FurnitureCreativeVisibility.isVisible(item) ? fallback : item);
            }
        };
    }

	public Ironagefurniture() {
        net.minecraftforge.common.crafting.CraftingHelper.register(
                new zone.moddev.mc.ironagefurniture.api.recipes.SconceMetalCondition.Serializer());
        entities.REGISTER.register(FMLJavaModLoadingContext.get().getModEventBus());

		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, IronAgeFurnitureConfiguration.clientSpec);
		ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, IronAgeFurnitureConfiguration.clientSpec);

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onCommonSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClientSetup);

        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new CreativeModeBreakTracker());
        MinecraftForge.EVENT_BUS.addListener(LegacyWorldDataHook::onServerAboutToStart);
    }

	 private void onCommonSetup(FMLCommonSetupEvent event) {
        PROXY.onSetupCommon();
    }

    private void onClientSetup(FMLClientSetupEvent event) {
        PROXY.onSetupClient();
    }
}
