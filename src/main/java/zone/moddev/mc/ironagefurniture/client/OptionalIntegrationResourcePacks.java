package zone.moddev.mc.ironagefurniture.client;

import java.nio.file.Path;
import java.util.Optional;

import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackSelectionConfig;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.PathPackResources;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.Pack.Position;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.fml.ModList;
import zone.moddev.mc.ironagefurniture.Ironagefurniture;

/** Exposes optional wood models only while their provider is installed. */
public final class OptionalIntegrationResourcePacks {
    private OptionalIntegrationResourcePacks() {
    }

    public static void register(net.minecraftforge.eventbus.api.IEventBus bus) {
        bus.addListener(OptionalIntegrationResourcePacks::addPacks);
    }

    private static void addPacks(AddPackFindersEvent event) {
        if (event.getPackType() != PackType.CLIENT_RESOURCES) {
            return;
        }

        addIfLoaded(event, "biomesoplenty");
    }

    private static void addIfLoaded(AddPackFindersEvent event, String integrationId) {
        if (!ModList.get().isLoaded(integrationId)) {
            return;
        }

        event.addRepositorySource(acceptor -> {
            Path root = ModList.get().getModFileById(Ironagefurniture.MODID).getFile()
                    .findResource("resourcepacks", "ironagefurniture_" + integrationId);
            var supplier = new PathPackResources.PathResourcesSupplier(root);
            var info = new PackLocationInfo(
                    "mod:" + Ironagefurniture.MODID + "_" + integrationId,
                    Component.literal("IronAgeFurniture " + integrationId + " integration"),
                    PackSource.DEFAULT,
                    Optional.empty());
            Pack pack = Pack.readMetaAndCreate(
                    info,
                    supplier,
                    PackType.CLIENT_RESOURCES,
                    new PackSelectionConfig(true, Position.TOP, false));
            if (pack == null) {
                throw new IllegalStateException("Could not load optional resource pack " + root);
            }
            acceptor.accept(pack);
        });
    }
}
