package com.mcmoddev.ironagefurniture.client.renderer;

import com.mcmoddev.ironagefurniture.api.entity.Seat;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class SeatRenderer extends EntityRenderer<Seat> {
    public SeatRenderer(EntityRendererManager manager) {
        super(manager);
    }

    @Override
    public ResourceLocation getTextureLocation(Seat seatEntity) {
        return null;
    }

    //@Override
    protected void renderNameTag(Seat p_225629_1_, String p_225629_2_, MatrixStack p_225629_3_, IRenderTypeBuffer p_225629_4_, int p_225629_5_) {
    }
}