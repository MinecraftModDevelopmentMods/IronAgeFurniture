package com.mcmoddev.ironagefurniture.client.render;

import com.mcmoddev.ironagefurniture.api.tile.TileEntityDiningTable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;

public class TileEntityDiningTableRenderer extends TileEntitySpecialRenderer<TileEntityDiningTable> {
	@Override
	public void renderTileEntityAt(TileEntityDiningTable te, double x, double y, double z, float partialTicks,
			int destroyStage) {
		ItemStack itemStack = te.getDisplayedItem();

		if (itemStack == null || itemStack.stackSize <= 0) {
			return;
		}

		GlStateManager.pushMatrix();
		GlStateManager.translate(x + 0.5D, y + 1.04D, z + 0.5D);
		GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
		GlStateManager.scale(0.5F, 0.5F, 0.5F);
		Minecraft.getMinecraft().getRenderItem().renderItem(itemStack, ItemCameraTransforms.TransformType.FIXED);
		GlStateManager.popMatrix();
	}
}
