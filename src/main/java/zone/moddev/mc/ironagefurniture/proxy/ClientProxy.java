package zone.moddev.mc.ironagefurniture.proxy;

import net.minecraftforge.fml.client.registry.RenderingRegistry;
import zone.moddev.mc.ironagefurniture.client.renderer.SeatRenderer;
import zone.moddev.mc.ironagefurniture.client.renderer.ThrownLavaLampRenderer;
import zone.moddev.mc.ironagefurniture.registers.entities;
import zone.moddev.mc.ironagefurniture.client.renderer.ShieldChairRenderer;
import zone.moddev.mc.ironagefurniture.client.renderer.FallingShieldChairRenderer;
import zone.moddev.mc.ironagefurniture.client.renderer.ReleasedLavaLampRenderer;
import zone.moddev.mc.ironagefurniture.api.tile.ShieldChairTileEntity;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ClientProxy extends CommonProxy {

    @Override
    public void onSetupClient() {
        RenderingRegistry.registerEntityRenderingHandler(entities.SEAT.get(), SeatRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(entities.THROWN_LAVA_LAMP.get(),
                ThrownLavaLampRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(entities.FALLING_SHIELD_CHAIR.get(),
                FallingShieldChairRenderer::new);
        ClientRegistry.bindTileEntityRenderer(zone.moddev.mc.ironagefurniture.init.PhaseFourTileEntities.shield_chair, ShieldChairRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(entities.RELEASED_LAVA_LAMP.get(),
                ReleasedLavaLampRenderer::new);
    }
}
