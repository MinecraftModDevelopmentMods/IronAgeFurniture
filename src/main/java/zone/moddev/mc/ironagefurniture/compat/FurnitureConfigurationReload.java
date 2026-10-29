package zone.moddev.mc.ironagefurniture.compat;

import java.util.List;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.server.ServerLifecycleHooks;
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
        // Correcting a newly loaded 1.20.6 config can fire Reloading before the
        // overworld exists. ServerStarted applies those settings once levels are
        // available; an early datapack reload would otherwise dereference it.
        if (server == null || server.overworld() == null) return;
        server.execute(() -> {
            if (ServerLifecycleHooks.getCurrentServer() == server && server.isRunning()
                    && !server.isStopped() && server.overworld() != null) reload(server);
        });
    }
    private static void reload(MinecraftServer server) {
        org.apache.logging.log4j.LogManager.getLogger().info("Reloading furniture recipes to apply server settings");
        server.reloadResources(server.getPackRepository().getSelectedIds()).join();
        FurnitureConfigurationSync.broadcast(server);
    }
    private FurnitureConfigurationReload() { }
}
