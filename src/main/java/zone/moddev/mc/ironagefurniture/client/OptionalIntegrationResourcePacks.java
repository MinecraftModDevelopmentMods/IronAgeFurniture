package zone.moddev.mc.ironagefurniture.client;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import zone.moddev.mc.ironagefurniture.Ironagefurniture;

/** Keep optional wood models out of the resource manager when their provider is absent. */
public final class OptionalIntegrationResourcePacks {
    private OptionalIntegrationResourcePacks() { }

    public static void register(IEventBus modBus) {
        modBus.addListener(OptionalIntegrationResourcePacks::addPacks);
    }

    private static void addPacks(AddPackFindersEvent event) {
        addIfLoaded(event, "biomesoplenty");
        addIfLoaded(event, "biomeswevegone");
        addIfLoaded(event, "immersiveengineering");
    }

    private static void addIfLoaded(AddPackFindersEvent event, String modId) {
        if (!ModList.get().isLoaded(modId)) return;
        event.addPackFinders(ResourceLocation.fromNamespaceAndPath(Ironagefurniture.MODID,
                        "resourcepacks/ironagefurniture_" + modId), PackType.CLIENT_RESOURCES,
                Component.literal("Iron Age Furniture " + modId + " integration"),
                PackSource.DEFAULT, true, Pack.Position.TOP);
    }
}
