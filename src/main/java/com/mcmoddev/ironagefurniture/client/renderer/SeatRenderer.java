package com.mcmoddev.ironagefurniture.client.renderer;

import com.mcmoddev.ironagefurniture.api.entity.Seat;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.resources.ResourceLocation;

public class SeatRenderer extends EntityRenderer<Seat> {
    public SeatRenderer(EntityRenderDispatcher manager) {
        super(manager);
    }

    @Override
    public ResourceLocation getTextureLocation(Seat seatEntity) {
        return null;
    }

    //@Override
    protected void renderNameTag(Seat p_225629_1_, String p_225629_2_, PoseStack p_225629_3_, MultiBufferSource p_225629_4_, int p_225629_5_) {
    }
}