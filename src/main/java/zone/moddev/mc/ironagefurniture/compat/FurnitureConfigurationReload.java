package zone.moddev.mc.ironagefurniture.compat;

import java.util.List;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.server.ServerLifecycleHooks;
import zone.moddev.mc.ironagefurniture.IronAgeFurnitureConfiguration;
import zone.moddev.mc.ironagefurniture.Ironagefurniture;

/** Synced settings load after the first datapack pass; apply them before play begins. */
public final class FurnitureConfigurationReload {
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
