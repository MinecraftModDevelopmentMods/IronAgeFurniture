package zone.moddev.mc.ironagefurniture.proxy;

import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import zone.moddev.mc.ironagefurniture.client.renderer.ShieldChairRenderer;
import zone.moddev.mc.ironagefurniture.client.renderer.FallingShieldChairRenderer;
import zone.moddev.mc.ironagefurniture.client.renderer.ReleasedLavaLampRenderer;
import zone.moddev.mc.ironagefurniture.client.renderer.SeatRenderer;
import zone.moddev.mc.ironagefurniture.client.renderer.ThrownLavaLampRenderer;
import zone.moddev.mc.ironagefurniture.registers.entities;

public class ClientProxy extends CommonProxy {

    @Override
    public void onSetupClient() {
        EntityRenderers.register(entities.SEAT.get(), SeatRenderer::new);
        EntityRenderers.register(entities.THROWN_LAVA_LAMP.get(), ThrownLavaLampRenderer::new);
        EntityRenderers.register(entities.FALLING_SHIELD_CHAIR.get(), FallingShieldChairRenderer::new);
        EntityRenderers.register(entities.RELEASED_LAVA_LAMP.get(), ReleasedLavaLampRenderer::new);
        BlockEntityRenderers.register(zone.moddev.mc.ironagefurniture.init.PhaseFourTileEntities.shield_chair, ShieldChairRenderer::new);
    }
}
