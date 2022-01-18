package com.mcmoddev.ironagefurniture.client.renderer;

import com.mcmoddev.ironagefurniture.api.entity.Seat;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class SeatRenderer extends EntityRenderer<Seat> {
    public SeatRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected void renderNameTag(Seat entity, Component component, PoseStack stack, MultiBufferSource source, int light) {}
    
    @Override
    public ResourceLocation getTextureLocation(Seat seatEntity) { return null; }
}