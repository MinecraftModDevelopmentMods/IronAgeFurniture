package zone.moddev.mc.ironagefurniture.client.renderer;

import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

/**
 * Marker for the client setup boundary. Forge 66 reads the cutout render type
 * from the block model JSON, so no runtime layer registration is required.
 */
public final class LightRendering {
    private LightRendering() {
    }

    public static void clientSetup(FMLClientSetupEvent event) {
        // Render layers are declared by the light models.
    }
}
