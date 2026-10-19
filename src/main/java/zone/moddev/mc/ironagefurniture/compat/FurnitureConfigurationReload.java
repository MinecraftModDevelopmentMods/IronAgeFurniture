package zone.moddev.mc.ironagefurniture.compat;

import java.util.List;
import net.minecraft.server.MinecraftServer;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import zone.moddev.mc.ironagefurniture.IronAgeFurnitureConfiguration;
import zone.moddev.mc.ironagefurniture.Ironagefurniture;

/** Synced settings load after the first datapack pass; apply them before play begins. */
@EventBusSubscriber(modid = Ironagefurniture.MODID)
public final class FurnitureConfigurationReload {
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void started(ServerStartedEvent event) {
        if (List.of(IronAgeFurnitureConfiguration.CLASSIC_CHAIRS, IronAgeFurnitureConfiguration.SHIELD_CHAIRS,
                IronAgeFurnitureConfiguration.STOOLS, IronAgeFurnitureConfiguration.BENCHES,
                IronAgeFurnitureConfiguration.WINGBACKS, IronAgeFurnitureConfiguration.THRONES,
                IronAgeFurnitureConfiguration.WOOD_BEDS, IronAgeFurnitureConfiguration.CANOPY_BEDS,
                IronAgeFurnitureConfiguration.LIGHTS, IronAgeFurnitureConfiguration.BASE_METALS,
                IronAgeFurnitureConfiguration.MINERALOGY).stream().anyMatch(setting -> !IronAgeFurnitureConfiguration.enabled(setting)))
            reload(event.getServer());
    }
    public static void configReloaded(ModConfigEvent.Reloading event) {
        if (event.getConfig().getSpec() != IronAgeFurnitureConfiguration.SYNCED_SPEC) return;
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server != null) server.execute(() -> reload(server));
    }
    private static void reload(MinecraftServer server) {
        org.apache.logging.log4j.LogManager.getLogger().info("Reloading furniture recipes to apply server settings");
        server.reloadResources(server.getPackRepository().getSelectedIds()).join();
    }
    private FurnitureConfigurationReload() { }
}
