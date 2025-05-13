package com.mcmoddev.ironagefurniture.client.renderer;

import com.mcmoddev.ironagefurniture.api.entity.Entities;

import net.minecraftforge.client.event.EntityRenderersEvent;

public class ClientHandler {
	 public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event)
	    {
	        event.registerEntityRenderer(Entities.SEAT.get(), SeatRenderer::new);
	    }
}
