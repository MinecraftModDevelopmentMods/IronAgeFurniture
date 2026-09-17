package zone.moddev.mc.ironagefurniture.client;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import zone.moddev.mc.ironagefurniture.Ironagefurniture;

/**
 * Exposes integration models only when their owning mod is present. Minecraft
 * 1.21.11 resolves item definitions eagerly, so keeping these assets in the
 * ordinary mod resource pack would produce missing-model noise on a clean
 * installation.
 */
public final class OptionalIntegrationResourcePacks {
    private OptionalIntegrationResourcePacks() {
    }

    public static void register(IEventBus modBus) {
        modBus.addListener(OptionalIntegrationResourcePacks::addPacks);
    }

    private static void addPacks(AddPackFindersEvent event) {
        addIfLoaded(event, "biomesoplenty");
        addIfLoaded(event, "biomeswevegone");
    }

    private static void addIfLoaded(AddPackFindersEvent event, String integrationId) {
        if (!ModList.get().isLoaded(integrationId)) {
            return;
        }

        event.addPackFinders(
                Identifier.fromNamespaceAndPath(
                        Ironagefurniture.MODID,
                        "resourcepacks/ironagefurniture_" + integrationId),
                PackType.CLIENT_RESOURCES,
                Component.literal("IronAgeFurniture " + integrationId + " integration"),
                PackSource.DEFAULT,
                true,
                Pack.Position.TOP);
    }
}
