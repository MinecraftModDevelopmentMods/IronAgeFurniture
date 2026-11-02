package zone.moddev.mc.ironagefurniture.client;

import java.nio.file.Path;
import java.util.Optional;

import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraftforge.resource.PathResourcePack;
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
        addIfLoaded(event, "byg");
        addIfLoaded(event, "immersiveengineering");
    }

    private static void addIfLoaded(AddPackFindersEvent event, String integrationId) {
        if (!ModList.get().isLoaded(integrationId)) {
            return;
        }

        event.addRepositorySource((acceptor, nativeConstructor) -> {
            Path root = ModList.get().getModFileById(Ironagefurniture.MODID).getFile()
                    .findResource("resourcepacks", "ironagefurniture_" + integrationId);
            String id = "mod:" + Ironagefurniture.MODID + "_" + integrationId;
            Pack pack = Pack.create(id, true, () -> new PathResourcePack(id, root),
                    (packId, title, required, supplier, metadata, position, source, hidden) ->
                            new Pack(packId, new net.minecraft.network.chat.TextComponent(
                                    "IronAgeFurniture " + integrationId + " integration"), required,
                                    supplier, metadata, PackType.CLIENT_RESOURCES, position, source, hidden),
                    Position.TOP, PackSource.DEFAULT);
            if (pack == null) {
                throw new IllegalStateException("Could not load optional resource pack " + root);
            }
            acceptor.accept(pack);
        });
    }
}
