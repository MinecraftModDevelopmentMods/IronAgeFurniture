package com.mcmoddev.ironagefurniture.proxy;

import com.mcmoddev.ironagefurniture.client.renderer.SeatRenderer;
import com.mcmoddev.ironagefurniture.registers.entities;

import net.minecraft.client.renderer.entity.EntityRenderers;

public class CommonProxy {
    public void onSetupCommon()
    {

    }

    public void onSetupClient() {
    	
    	EntityRenderers.register(entities.SEAT.get(), SeatRenderer::new);
    }
}
