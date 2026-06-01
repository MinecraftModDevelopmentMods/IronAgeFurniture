package com.mcmoddev.ironagefurniture.client.render;

import com.mcmoddev.ironagefurniture.api.tile.TileEntityGlassVase;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;

public class TileEntityGlassVaseRenderer extends TileEntitySpecialRenderer<TileEntityGlassVase> {
	@Override
	public void renderTileEntityAt(TileEntityGlassVase te, double x, double y, double z, float partialTicks,
			int destroyStage) {
		if (te.getPlant() != null) {
			SurfaceDisplayRenderHelper.renderPottedPlant(te.getPlant(), x, y, z, 0.0D);
		}
	}
}
