package com.mcmoddev.ironagefurniture.proxy;

import com.mcmoddev.ironagefurniture.api.entity.Entities;
import com.mcmoddev.ironagefurniture.client.renderer.SeatRenderer;

import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy {

    @Override
    public void onSetupClient() {
        super.onSetupClient();

        RenderingRegistry.registerEntityRenderingHandler(Entities.SEAT, SeatRenderer::new);
    }

}
