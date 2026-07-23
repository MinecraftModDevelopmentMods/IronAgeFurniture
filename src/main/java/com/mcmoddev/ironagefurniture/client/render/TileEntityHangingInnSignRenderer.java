package com.mcmoddev.ironagefurniture.client.render;

import com.mcmoddev.ironagefurniture.api.Blocks.HangingInnSign;
import com.mcmoddev.ironagefurniture.api.tile.TileEntityHangingInnSign;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;

public class TileEntityHangingInnSignRenderer extends TileEntitySpecialRenderer<TileEntityHangingInnSign> {
	@Override
	public void renderTileEntityAt(TileEntityHangingInnSign sign, double x, double y, double z,
			float partialTicks, int destroyStage) {
		if (sign.getWorld() == null) return;
		IBlockState state = sign.getWorld().getBlockState(sign.getPos());
		if (!(state.getBlock() instanceof HangingInnSign)) return;
		EnumFacing facing = state.getValue(HangingInnSign.FACING);
		FontRenderer font = Minecraft.getMinecraft().fontRendererObj;
		String innName = sign.getInnName();
		String keeperName = sign.getKeeperName();
		int widest = Math.max(font.getStringWidth(innName), font.getStringWidth(keeperName));
		float scale = Math.min(0.0105F, 0.75F / Math.max(1, widest));

		GlStateManager.pushMatrix();
		GlStateManager.translate(x + 0.5D, y + 0.47D, z + 0.5D);
		GlStateManager.rotate(180.0F - facing.getHorizontalAngle(), 0.0F, 1.0F, 0.0F);
		GlStateManager.translate(0.0D, 0.0D, -0.132D);
		GlStateManager.scale(-scale, -scale, scale);
		GlStateManager.disableLighting();
		font.drawString(innName, -font.getStringWidth(innName) / 2, keeperName.isEmpty() ? -4 : -9, 0x2F1C10);
		if (!keeperName.isEmpty()) {
			font.drawString(keeperName, -font.getStringWidth(keeperName) / 2, 2, 0x2F1C10);
		}
		GlStateManager.enableLighting();
		GlStateManager.popMatrix();
	}
}
