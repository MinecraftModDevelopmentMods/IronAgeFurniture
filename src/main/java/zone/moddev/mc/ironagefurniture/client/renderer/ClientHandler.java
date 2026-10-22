package zone.moddev.mc.ironagefurniture.client.renderer;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import zone.moddev.mc.ironagefurniture.Ironagefurniture;
import zone.moddev.mc.ironagefurniture.api.entity.Entities;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import zone.moddev.mc.ironagefurniture.api.ShieldChairItemData;
import zone.moddev.mc.ironagefurniture.api.blocks.furniture.ShieldChair;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(modid = Ironagefurniture.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientHandler {
     @SubscribeEvent
     public static void onClientSetup(FMLClientSetupEvent event) {
         event.enqueueWork(() -> {
             var property = ResourceLocation.fromNamespaceAndPath(Ironagefurniture.MODID, "empty_shield_frame");
             for (var item : BuiltInRegistries.ITEM) {
                 if (item instanceof BlockItem blockItem && blockItem.getBlock() instanceof ShieldChair)
                     ItemProperties.register(item, property,
                             (stack, world, entity, seed) -> ShieldChairItemData.getShield(stack).isEmpty() ? 1F : 0F);
             }
         });
     }
	 @SubscribeEvent
	 public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event)
	    {
	        event.registerEntityRenderer(Entities.SEAT.get(), SeatRenderer::new);
	        event.registerEntityRenderer(Entities.FALLING_SHIELD_CHAIR.get(), FallingShieldChairRenderer::new);
	        event.registerBlockEntityRenderer(zone.moddev.mc.ironagefurniture.init.PhaseFourBlockEntities.SHIELD_CHAIR.get(), ShieldChairRenderer::new);
            event.registerBlockEntityRenderer(zone.moddev.mc.ironagefurniture.init.PhaseFourLighting.CANDLE_STORAGE.get(), CandleSconceRenderer::new);
            event.registerBlockEntityRenderer(zone.moddev.mc.ironagefurniture.init.PhaseFourLighting.METAL_FRAME.get(), MetalSconceRenderer::new);
	        event.registerEntityRenderer(Entities.THROWN_LAVA_LAMP.get(), ThrownItemRenderer::new);
            event.registerEntityRenderer(Entities.RELEASED_LAVA_LAMP.get(), ReleasedLavaLampRenderer::new);
	    }
}
