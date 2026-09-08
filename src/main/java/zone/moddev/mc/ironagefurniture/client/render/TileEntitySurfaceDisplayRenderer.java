package zone.moddev.mc.ironagefurniture.client.render;

import zone.moddev.mc.ironagefurniture.api.surface.SurfaceSetting;
import zone.moddev.mc.ironagefurniture.api.surface.SurfaceSetting.Slot;
import zone.moddev.mc.ironagefurniture.api.tile.TileEntitySurfaceDisplay;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

public class TileEntitySurfaceDisplayRenderer extends TileEntitySpecialRenderer<TileEntitySurfaceDisplay> {
	@Override
	public void renderTileEntityAt(TileEntitySurfaceDisplay tileEntity, double x, double y, double z,
			float partialTicks, int destroyStage) {
		SurfaceSetting setting = tileEntity.getSurfaceSetting();

		for (Slot slot : Slot.values()) {
			ItemStack itemStack = setting.getItem(slot);

			if (itemStack != null && itemStack.stackSize > 0) {
				SurfaceDisplayRenderHelper.renderSurfaceItem(itemStack, x, y, z,
					setting.getX(slot), setting.getZ(slot), 0.0D, 0.0D,
					this.getYaw(setting.getFacing(slot)));
			}
		}
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
