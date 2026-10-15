package zone.moddev.mc.ironagefurniture.fixture;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fmlserverevents.FMLServerStartedEvent;

/** Confirms a disposable fixture was loaded and saved by the chosen published release. */
@Mod("ironagefurniturereleasedcheckpoint")
public final class ReleasedWorldCheckpoint {
    public ReleasedWorldCheckpoint() { MinecraftForge.EVENT_BUS.addListener(this::started); }
    private void started(FMLServerStartedEvent event) {
        try {
            String version = ModList.get().isLoaded("ironagefurniture") ? ModList.get().getModContainerById("ironagefurniture").get()
                    .getModInfo().getVersion().toString() : "none";
            String expected = System.getProperty("iaf.checkpoint.expectedVersion", "0.3.0.117011");
            if (!version.equals(expected)) throw new IllegalStateException("Wrong source release: " + version);
            if (Boolean.getBoolean("iaf.checkpoint.metalTerrainControl")) {
                net.minecraft.server.level.ServerLevel world = event.getServer().getLevel(net.minecraft.world.level.Level.OVERWORLD);
                java.util.List<net.minecraft.core.BlockPos> chunks = new java.util.ArrayList<>();
                for (int x = 64; x <= 77; x++) for (int z = 63; z <= 75; z++) {
                    net.minecraft.core.BlockPos pos = new net.minecraft.core.BlockPos(x * 16, 80, z * 16);
                    world.setChunkForced(x, z, true);
                    world.getChunk(pos);
                    chunks.add(pos);
                }
                MinecraftForge.EVENT_BUS.addListener((net.minecraftforge.event.TickEvent.ServerTickEvent tick) -> {
                    if (tick.phase != net.minecraftforge.event.TickEvent.Phase.END || !chunks.stream().allMatch(world::isPositionTickingWithEntitiesLoaded)) return;
                    try {
                        String support = world.getBlockState(new net.minecraft.core.BlockPos(1024, 79, 1069)).getBlock().getRegistryName().toString();
                        Files.write(Paths.get("phase-four-pass.properties"), ("status=PASS\nsource_version="+version+"\nsupport="+support+"\n").getBytes(StandardCharsets.UTF_8));
                    } catch (Exception failure) { throw new IllegalStateException("Terrain control failed", failure); }
                    finally {event.getServer().halt(false);}
                });
                return;
            }
            Files.write(Paths.get("phase-four-pass.properties"),
                    ("status=PASS\nsource_version=" + version + "\n").getBytes(StandardCharsets.UTF_8));
        } catch (Exception failure) {
            event.getServer().halt(false);
            throw new IllegalStateException("Released-world checkpoint failed", failure);
        }
        finally { if (!Boolean.getBoolean("iaf.checkpoint.metalTerrainControl")) event.getServer().halt(false); }
    }
}
