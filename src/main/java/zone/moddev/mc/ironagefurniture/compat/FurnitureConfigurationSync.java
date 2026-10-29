package zone.moddev.mc.ironagefurniture.compat;

import java.util.List;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.ForgeConfigSpec;
import zone.moddev.mc.ironagefurniture.IronAgeFurnitureConfiguration;
import zone.moddev.mc.ironagefurniture.Ironagefurniture;
import zone.moddev.mc.ironagefurniture.client.FurnitureConfigurationClient;

/** Native 1.20.6 synchronizes SERVER files at login, but not during play. */
public final class FurnitureConfigurationSync {
    private static List<ForgeConfigSpec.BooleanValue> values() {
        return List.of(IronAgeFurnitureConfiguration.CLASSIC_CHAIRS, IronAgeFurnitureConfiguration.SHIELD_CHAIRS,
                IronAgeFurnitureConfiguration.STOOLS, IronAgeFurnitureConfiguration.BENCHES,
                IronAgeFurnitureConfiguration.WINGBACKS, IronAgeFurnitureConfiguration.THRONES,
                IronAgeFurnitureConfiguration.WOOD_BEDS, IronAgeFurnitureConfiguration.CANOPY_BEDS,
                IronAgeFurnitureConfiguration.LIGHTS, IronAgeFurnitureConfiguration.BASE_METALS,
                IronAgeFurnitureConfiguration.MINERALOGY, IronAgeFurnitureConfiguration.FORCE_CFM_CONVERSION);
    }
    private static final net.minecraftforge.network.SimpleChannel CHANNEL = net.minecraftforge.network.ChannelBuilder
            .named(ResourceLocation.fromNamespaceAndPath(Ironagefurniture.MODID, "server_configuration"))
            .networkProtocolVersion(1).simpleChannel();
    public static void register() {
        CHANNEL.messageBuilder(Snapshot.class, net.minecraftforge.network.NetworkDirection.PLAY_TO_CLIENT)
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
        CHANNEL.send(new Snapshot(mask), net.minecraftforge.network.PacketDistributor.ALL.noArg());
    }
    private static void received(Snapshot snapshot, net.minecraftforge.event.network.CustomPayloadEvent.Context context) {
        context.enqueueWork(() -> {
            if (!IronAgeFurnitureConfiguration.SYNCED_SPEC.isLoaded())
                throw new IllegalStateException("Furniture play settings require the initial native SERVER configuration");
            // The integrated host already shares its server's spec. Remote native
            // SERVER specs are in-memory; set() updates caches without events or disk writes.
            if (!context.getConnection().isMemoryConnection()) {
                List<ForgeConfigSpec.BooleanValue> settings = values();
                for (int index = 0; index < settings.size(); index++)
                    settings.get(index).set((snapshot.mask() & (1 << index)) != 0);
            }
            FurnitureConfigurationClient.refresh();
        });
    }
    public record Snapshot(int mask) implements CustomPacketPayload {
        public static final Type<Snapshot> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Ironagefurniture.MODID, "server_configuration"));
        public static final StreamCodec<FriendlyByteBuf, Snapshot> STREAM_CODEC =
                ByteBufCodecs.VAR_INT.map(Snapshot::new, Snapshot::mask).cast();
        public Snapshot {
            if ((mask & ~0xfff) != 0) throw new IllegalArgumentException("Only the twelve furniture gameplay settings may be synchronized");
        }
        @Override public Type<Snapshot> type() { return TYPE; }
    }
    private FurnitureConfigurationSync() { }
}
