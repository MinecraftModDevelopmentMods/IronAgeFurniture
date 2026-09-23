package zone.moddev.mc.ironagefurniture.proxy;

import net.minecraftforge.fml.client.registry.RenderingRegistry;
import zone.moddev.mc.ironagefurniture.client.renderer.SeatRenderer;
import zone.moddev.mc.ironagefurniture.client.renderer.ThrownLavaLampRenderer;

public class ClientProxy extends CommonProxy {

    @Override
    public void onSetupClient() {
        RenderingRegistry.registerEntityRenderingHandler(zone.moddev.mc.ironagefurniture.api.entity.Seat.class, SeatRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(zone.moddev.mc.ironagefurniture.api.entity.ThrownLavaLamp.class,
                ThrownLavaLampRenderer::new);
    }
}
