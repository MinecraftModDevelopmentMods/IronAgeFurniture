package zone.moddev.mc.ironagefurniture.compat;

import java.util.List;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import zone.moddev.mc.ironagefurniture.IronAgeFurnitureConfiguration;
import zone.moddev.mc.ironagefurniture.Ironagefurniture;
import zone.moddev.mc.ironagefurniture.client.FurnitureConfigurationClient;

/** Native 1.20.6 synchronizes SERVER files at login, but not during play. */
public final class FurnitureConfigurationSync {
    private static List<ModConfigSpec.BooleanValue> values() {
        return List.of(IronAgeFurnitureConfiguration.CLASSIC_CHAIRS, IronAgeFurnitureConfiguration.SHIELD_CHAIRS,
                IronAgeFurnitureConfiguration.STOOLS, IronAgeFurnitureConfiguration.BENCHES,
                IronAgeFurnitureConfiguration.WINGBACKS, IronAgeFurnitureConfiguration.THRONES,
                IronAgeFurnitureConfiguration.WOOD_BEDS, IronAgeFurnitureConfiguration.CANOPY_BEDS,
                IronAgeFurnitureConfiguration.LIGHTS, IronAgeFurnitureConfiguration.BASE_METALS,
                IronAgeFurnitureConfiguration.MINERALOGY, IronAgeFurnitureConfiguration.FORCE_CFM_CONVERSION);
    }
    public static void register(IEventBus bus) { bus.addListener(FurnitureConfigurationSync::registerPayload); }
    private static void registerPayload(RegisterPayloadHandlersEvent event) {
        event.registrar("1").playToClient(Snapshot.TYPE, Snapshot.STREAM_CODEC, FurnitureConfigurationSync::received);
    }
    public static void broadcast(MinecraftServer server) {
        if (server.getPlayerCount() == 0) return;
        int mask = 0;
        List<ModConfigSpec.BooleanValue> settings = values();
        for (int index = 0; index < settings.size(); index++)
            if (IronAgeFurnitureConfiguration.enabled(settings.get(index))) mask |= 1 << index;
        PacketDistributor.sendToAllPlayers(new Snapshot(mask));
    }
    private static void received(Snapshot snapshot, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (!IronAgeFurnitureConfiguration.SYNCED_SPEC.isLoaded())
                throw new IllegalStateException("Furniture play settings require the initial native SERVER configuration");
            // The integrated host already shares its server's spec. Remote native
            // SERVER specs are in-memory; set() updates caches without events or disk writes.
            if (!context.connection().isMemoryConnection()) {
                List<ModConfigSpec.BooleanValue> settings = values();
                for (int index = 0; index < settings.size(); index++)
                    settings.get(index).set((snapshot.mask() & (1 << index)) != 0);
            }
            FurnitureConfigurationClient.refresh();
        });
    }
    public record Snapshot(int mask) implements CustomPacketPayload {
        public static final Type<Snapshot> TYPE = new Type<>(new ResourceLocation(Ironagefurniture.MODID, "server_configuration"));
        public static final StreamCodec<FriendlyByteBuf, Snapshot> STREAM_CODEC =
                ByteBufCodecs.VAR_INT.map(Snapshot::new, Snapshot::mask).cast();
        public Snapshot {
            if ((mask & ~0xfff) != 0) throw new IllegalArgumentException("Only the twelve furniture gameplay settings may be synchronized");
        }
        @Override public Type<Snapshot> type() { return TYPE; }
    }
    private FurnitureConfigurationSync() { }
}
