package zone.moddev.mc.ironagefurniture.fixture.control;

import net.minecraftforge.fml.common.Mod;

import net.minecraftforge.event.server.ServerStartedEvent;

/** Load the same source chunk without IAF to distinguish vanilla upgrade failures. */
@Mod("iafworldcontrol")
public final class WorldControl {
    public WorldControl() { ServerStartedEvent.BUS.addListener(this::started); }
    private void started(ServerStartedEvent event) {
        event.getServer().execute(() -> {
            try {
                event.getServer().overworld().getChunk(Integer.getInteger("iaf.control.x", -7), Integer.getInteger("iaf.control.z", 15));
                System.out.println("IAF_VANILLA_WORLD_CONTROL_FINISHED");
            } catch (Throwable failure) { failure.printStackTrace(); }
            finally { event.getServer().halt(false); }
        });
    }
}
