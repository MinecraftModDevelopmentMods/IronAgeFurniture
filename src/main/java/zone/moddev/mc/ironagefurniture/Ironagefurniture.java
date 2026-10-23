package zone.moddev.mc.ironagefurniture;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.bus.BusGroup;
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
    public Ironagefurniture(FMLJavaModLoadingContext context) {
        BusGroup bus = context.getModBusGroup();
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
        if (net.minecraftforge.fml.ModList.isLoaded("biomesoplenty")) {
            ModBOPBlocks.REGISTER.register(bus);
            PhaseFourBopFurniture.REGISTER.register(bus);
        }
        PhaseFourLighting.BLOCKS.register(bus);
        PhaseFourLighting.BLOCK_ENTITIES.register(bus);
        PhaseFourBlockEntities.REGISTER.register(bus);
        PhaseFourRecipes.CONDITIONS.register(bus);
        PhaseFourRecipes.SERIALIZERS.register(bus);
        ModItems.REGISTER.register(bus);
        ModCreativeTab.REGISTER.register(bus);
        Entities.REGISTER.register(bus);
        zone.moddev.mc.ironagefurniture.compat.LegacyFurnitureMappings.registerAliases(bus);
        net.minecraftforge.event.level.BlockEvent.BreakEvent.BUS.addListener(
                new zone.moddev.mc.ironagefurniture.api.CreativeModeBreakTracker()::onBlockBreak);
        net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock.BUS.addListener(
                zone.moddev.mc.ironagefurniture.api.blocks.furniture.ShieldChair::allowEmptyHandRemoval);
        net.minecraftforge.event.entity.player.PlayerEvent.ItemCraftedEvent.BUS.addListener(
                zone.moddev.mc.ironagefurniture.api.recipes.FurnitureUpgradeDiscovery::crafted);
        net.minecraftforge.event.server.ServerAboutToStartEvent.BUS.addListener(
                zone.moddev.mc.ironagefurniture.migration.LegacyWorldDataHook::onServerAboutToStart);
        net.minecraftforge.event.server.ServerStoppedEvent.BUS.addListener(
                zone.moddev.mc.ironagefurniture.migration.LegacyWorldDataHook::stopped);
        net.minecraftforge.event.server.ServerStartedEvent.BUS.addListener(
                zone.moddev.mc.ironagefurniture.compat.FurnitureConfigurationReload::started);
        net.minecraftforge.fml.event.config.ModConfigEvent.Reloading.getBus(bus).addListener(
                zone.moddev.mc.ironagefurniture.compat.FurnitureConfigurationReload::configReloaded);
        context.registerConfig(ModConfig.Type.SERVER, IronAgeFurnitureConfiguration.SYNCED_SPEC);
        if (FMLEnvironment.dist == Dist.CLIENT) {
            zone.moddev.mc.ironagefurniture.client.OptionalIntegrationResourcePacks.register();
            net.minecraftforge.client.event.EntityRenderersEvent.RegisterRenderers.BUS.addListener(
                    zone.moddev.mc.ironagefurniture.client.renderer.ClientHandler::onRegisterRenderers);
        }
    }
}
