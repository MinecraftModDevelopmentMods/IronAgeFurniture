package zone.moddev.mc.ironagefurniture.compat;

import java.util.List;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.ForgeConfigSpec;
import zone.moddev.mc.ironagefurniture.IronAgeFurnitureConfiguration;
import zone.moddev.mc.ironagefurniture.Ironagefurniture;
import zone.moddev.mc.ironagefurniture.client.FurnitureConfigurationClient;

/** Native 1.20.1 synchronizes SERVER files at login, but not during play. */
public final class FurnitureConfigurationSync {
    private static List<ForgeConfigSpec.BooleanValue> values() {
        return List.of(IronAgeFurnitureConfiguration.CLASSIC_CHAIRS, IronAgeFurnitureConfiguration.SHIELD_CHAIRS,
                IronAgeFurnitureConfiguration.STOOLS, IronAgeFurnitureConfiguration.BENCHES,
                IronAgeFurnitureConfiguration.WINGBACKS, IronAgeFurnitureConfiguration.THRONES,
                IronAgeFurnitureConfiguration.WOOD_BEDS, IronAgeFurnitureConfiguration.CANOPY_BEDS,
                IronAgeFurnitureConfiguration.LIGHTS, IronAgeFurnitureConfiguration.BASE_METALS,
                IronAgeFurnitureConfiguration.MINERALOGY, IronAgeFurnitureConfiguration.FORCE_CFM_CONVERSION);
    }
    private static final net.minecraftforge.network.simple.SimpleChannel CHANNEL = net.minecraftforge.network.NetworkRegistry.newSimpleChannel(
            new ResourceLocation(Ironagefurniture.MODID, "server_configuration"), () -> "1", "1"::equals, "1"::equals);
    public static void register() {
        CHANNEL.messageBuilder(Snapshot.class, 0, net.minecraftforge.network.NetworkDirection.PLAY_TO_CLIENT)
                .encoder((snapshot, buffer) -> buffer.writeVarInt(snapshot.mask()))
                .decoder(buffer -> new Snapshot(buffer.readVarInt()))
                .consumerMainThread(FurnitureConfigurationSync::received).add();
    }
    public static void broadcast(MinecraftServer server) {
        if (server.getPlayerCount() == 0) return;
        int mask = 0;
        List<ForgeConfigSpec.BooleanValue> settings = values();
        for (int index = 0; index < settings.size(); index++)
            if (IronAgeFurnitureConfiguration.enabled(settings.get(index))) mask |= 1 << index;
        CHANNEL.send(net.minecraftforge.network.PacketDistributor.ALL.noArg(), new Snapshot(mask));
    }
    private static void received(Snapshot snapshot, java.util.function.Supplier<net.minecraftforge.network.NetworkEvent.Context> supplied) {
        var context = supplied.get();
        context.setPacketHandled(true);
        context.enqueueWork(() -> {
            if (!IronAgeFurnitureConfiguration.SYNCED_SPEC.isLoaded())
                throw new IllegalStateException("Furniture play settings require the initial native SERVER configuration");
            // The integrated host already shares its server's spec. Remote native
            // SERVER specs are in-memory; set() updates caches without events or disk writes.
            if (!context.getNetworkManager().isMemoryConnection()) {
                List<ForgeConfigSpec.BooleanValue> settings = values();
                for (int index = 0; index < settings.size(); index++)
                    settings.get(index).set((snapshot.mask() & (1 << index)) != 0);
            }
            FurnitureConfigurationClient.refresh();
        });
    }
    public record Snapshot(int mask) {
        public Snapshot {
            if ((mask & ~0xfff) != 0) throw new IllegalArgumentException("Only the twelve furniture gameplay settings may be synchronized");
        }
    }
    private FurnitureConfigurationSync() { }
}
