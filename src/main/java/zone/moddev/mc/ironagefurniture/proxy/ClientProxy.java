package zone.moddev.mc.ironagefurniture.proxy;

import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import zone.moddev.mc.ironagefurniture.client.renderer.SeatRenderer;
import zone.moddev.mc.ironagefurniture.registers.entities;

public class ClientProxy extends CommonProxy {

    @Override
    public void onSetupClient() {
        EntityRenderers.register(entities.SEAT.get(), SeatRenderer::new);
        EntityRenderers.register(entities.THROWN_LAVA_LAMP.get(), ThrownItemRenderer::new);
    }
}
