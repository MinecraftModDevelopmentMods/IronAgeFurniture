package com.mcmoddev.ironagefurniture.client.render;

import com.mcmoddev.ironagefurniture.api.Items.ItemDrinkware;
import com.mcmoddev.ironagefurniture.api.tile.TileEntitySurfaceDisplay;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

public class TileEntitySurfaceDisplayRenderer extends TileEntitySpecialRenderer<TileEntitySurfaceDisplay> {
	@Override
	public void renderTileEntityAt(TileEntitySurfaceDisplay tileEntity, double x, double y, double z,
			float partialTicks, int destroyStage) {
		ItemStack itemStack = tileEntity.getDisplayedItem();

		if (itemStack == null || itemStack.stackSize <= 0 || !(itemStack.getItem() instanceof ItemDrinkware)) {
			return;
		}

		SurfaceDisplayRenderHelper.renderDrinkware(itemStack, x, y, z, 0.5D, 0.5D, 0.0D,
			this.getYaw(tileEntity.getDisplayedFacing()), 1.0F);
	}

	private float getYaw(EnumFacing facing) {
		switch (facing) {
		case EAST:
			return 90.0F;
		case SOUTH:
			return 180.0F;
		case WEST:
			return 270.0F;
		default:
			return 0.0F;
		}
	}
}
