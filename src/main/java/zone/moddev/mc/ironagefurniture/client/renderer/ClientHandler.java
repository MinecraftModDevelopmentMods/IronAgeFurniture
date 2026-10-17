package zone.moddev.mc.ironagefurniture.client.renderer;

import net.minecraftforge.api.distmarker.Dist;
import zone.moddev.mc.ironagefurniture.Ironagefurniture;
import zone.moddev.mc.ironagefurniture.api.entity.Entities;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraftforge.client.event.EntityRenderersEvent;
public class ClientHandler {
	 public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event)
	    {
	        event.registerEntityRenderer(Entities.SEAT.get(), SeatRenderer::new);
	        event.registerEntityRenderer(Entities.THROWN_LAVA_LAMP.get(), ThrownItemRenderer::new);
            event.registerBlockEntityRenderer(zone.moddev.mc.ironagefurniture.init.PhaseFourLighting.CANDLE_STORAGE.get(), CandleSconceRenderer::new);
            event.registerBlockEntityRenderer(zone.moddev.mc.ironagefurniture.init.PhaseFourLighting.METAL_FRAME.get(), MetalSconceRenderer::new);
            event.registerBlockEntityRenderer(zone.moddev.mc.ironagefurniture.init.PhaseFourBlockEntities.SHIELD_CHAIR.get(), ShieldChairRenderer::new);
            event.registerEntityRenderer(zone.moddev.mc.ironagefurniture.api.entity.Entities.FALLING_SHIELD_CHAIR.get(), FallingShieldChairRenderer::new);
            event.registerEntityRenderer(zone.moddev.mc.ironagefurniture.api.entity.Entities.RELEASED_LAVA_LAMP.get(), ReleasedLavaLampRenderer::new);
	    }
}
