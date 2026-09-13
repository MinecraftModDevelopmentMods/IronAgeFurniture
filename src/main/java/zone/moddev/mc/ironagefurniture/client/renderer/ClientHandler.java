package zone.moddev.mc.ironagefurniture.client.renderer;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import zone.moddev.mc.ironagefurniture.Ironagefurniture;
import zone.moddev.mc.ironagefurniture.api.entity.Entities;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(modid = Ironagefurniture.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientHandler {
	 @SubscribeEvent
	 public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event)
	    {
	        event.registerEntityRenderer(Entities.SEAT.get(), SeatRenderer::new);
	    }
}
