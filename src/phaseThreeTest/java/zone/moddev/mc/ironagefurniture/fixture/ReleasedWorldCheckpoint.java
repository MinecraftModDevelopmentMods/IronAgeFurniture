package zone.moddev.mc.ironagefurniture.fixture;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;

/** Confirms a disposable world was loaded and saved by the actual Phase 3 release. */
@Mod("ironagefurniturereleasedcheckpoint")
public final class ReleasedWorldCheckpoint {
    public ReleasedWorldCheckpoint() { MinecraftForge.EVENT_BUS.addListener(this::started); }
    private void started(FMLServerStartedEvent event) {
        try {
            String version = ModList.get().getModContainerById("ironagefurniture").get()
                    .getModInfo().getVersion().toString();
            if (!version.equals("0.3.0.114041")) throw new IllegalStateException("Wrong source release: " + version);
            Files.write(Paths.get("phase-four-pass.properties"),
                    ("status=PASS\nsource_version=" + version + "\n").getBytes(StandardCharsets.UTF_8));
        } catch (Exception failure) { throw new IllegalStateException("Released-world checkpoint failed", failure); }
        finally { event.getServer().initiateShutdown(false); }
    }
}
