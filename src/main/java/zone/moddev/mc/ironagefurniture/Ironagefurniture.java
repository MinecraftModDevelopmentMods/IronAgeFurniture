package zone.moddev.mc.ironagefurniture;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import zone.moddev.mc.ironagefurniture.init.*;
import zone.moddev.mc.ironagefurniture.api.entity.Entities;

/** Forge entry point; persistent furniture stays registered when disabled. */
@Mod(Ironagefurniture.MODID)
public final class Ironagefurniture {
    public static final String MODID = "ironagefurniture";
    public Ironagefurniture() {
        FMLJavaModLoadingContext context = FMLJavaModLoadingContext.get();
        IEventBus bus = context.getModEventBus();
        ModVanillaChairs.REGISTER.register(bus);
        ModVanillaShieldChairs.REGISTER.register(bus);
        ModVanillaStools.REGISTER.register(bus);
        ModVanillaTallStools.REGISTER.register(bus);
        ModVanillaBench.REGISTER.register(bus);
        ModVanillaBackBench.REGISTER.register(bus);
        ModVanillaLights.REGISTER.register(bus);
        ModVanillaLogBench.REGISTER.register(bus);
        ModVanillaPaddedBench.REGISTER.register(bus);
        ModVanillaPaddedBackBench.REGISTER.register(bus);
        PhaseFourFurniture.REGISTER.register(bus);
        if (net.minecraftforge.fml.ModList.get().isLoaded("biomesoplenty")) {
            ModBOPBlocks.REGISTER.register(bus);
            PhaseFourBopFurniture.REGISTER.register(bus);
        }
        if (net.minecraftforge.fml.ModList.get().isLoaded("byg")) {
            ModBYGBlocks.REGISTER.register(bus);
            PhaseFourBygFurniture.REGISTER.register(bus);
        }
        if (net.minecraftforge.fml.ModList.get().isLoaded("immersiveengineering")) {
            ModIEBlocks.REGISTER.register(bus);
            PhaseFourIeFurniture.REGISTER.register(bus);
        }
        PhaseFourLighting.BLOCKS.register(bus);
        PhaseFourLighting.BLOCK_ENTITIES.register(bus);
        PhaseFourBlockEntities.REGISTER.register(bus);
        bus.addListener(PhaseFourRecipes::registerConditions);
        PhaseFourRecipes.SERIALIZERS.register(bus);
        ModItems.REGISTER.register(bus);
        ModCreativeTab.register(bus);
        Entities.REGISTER.register(bus);
        zone.moddev.mc.ironagefurniture.compat.LegacyFurnitureMappings.registerAliases(bus);
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(
                new zone.moddev.mc.ironagefurniture.api.CreativeModeBreakTracker());
        bus.addListener(zone.moddev.mc.ironagefurniture.compat.FurnitureConfigurationReload::configReloaded);
        zone.moddev.mc.ironagefurniture.compat.FurnitureConfigurationSync.register();
        net.minecraftforge.fml.ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, IronAgeFurnitureConfiguration.SYNCED_SPEC);
        if (FMLEnvironment.dist == Dist.CLIENT) {
            zone.moddev.mc.ironagefurniture.client.OptionalIntegrationResourcePacks.register(bus);
            bus.addListener(zone.moddev.mc.ironagefurniture.client.renderer.LightRendering::clientSetup);
        }
    }
}
