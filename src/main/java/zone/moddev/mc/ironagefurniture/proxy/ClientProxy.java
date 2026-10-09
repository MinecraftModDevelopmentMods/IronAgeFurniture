package zone.moddev.mc.ironagefurniture.proxy;

import net.minecraftforge.fml.client.registry.RenderingRegistry;
import zone.moddev.mc.ironagefurniture.client.renderer.SeatRenderer;
import zone.moddev.mc.ironagefurniture.client.renderer.ThrownLavaLampRenderer;
import zone.moddev.mc.ironagefurniture.client.renderer.ShieldChairRenderer;
import zone.moddev.mc.ironagefurniture.client.renderer.FallingShieldChairRenderer;
import zone.moddev.mc.ironagefurniture.client.renderer.ReleasedLavaLampRenderer;
import zone.moddev.mc.ironagefurniture.api.tile.ShieldChairTileEntity;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ClientProxy extends CommonProxy {

    @Override
    public void onSetupClient() {
        RenderingRegistry.registerEntityRenderingHandler(zone.moddev.mc.ironagefurniture.api.entity.Seat.class, SeatRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(zone.moddev.mc.ironagefurniture.api.entity.ThrownLavaLamp.class,
                ThrownLavaLampRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(zone.moddev.mc.ironagefurniture.api.entity.FallingShieldChair.class,
                FallingShieldChairRenderer::new);
        ClientRegistry.bindTileEntitySpecialRenderer(ShieldChairTileEntity.class, new ShieldChairRenderer());
        RenderingRegistry.registerEntityRenderingHandler(zone.moddev.mc.ironagefurniture.api.entity.ReleasedLavaLamp.class,
                ReleasedLavaLampRenderer::new);
    }
}
