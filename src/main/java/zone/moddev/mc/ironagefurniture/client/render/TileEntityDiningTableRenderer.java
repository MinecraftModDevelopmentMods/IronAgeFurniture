package zone.moddev.mc.ironagefurniture.client.render;

import zone.moddev.mc.ironagefurniture.api.Blocks.DiningTable;
import zone.moddev.mc.ironagefurniture.api.surface.SurfaceSetting;
import zone.moddev.mc.ironagefurniture.api.surface.SurfaceSetting.Slot;
import zone.moddev.mc.ironagefurniture.api.tile.TileEntityDiningTable;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

public class TileEntityDiningTableRenderer extends TileEntitySpecialRenderer<TileEntityDiningTable> {
	private static final double ITEM_Y = 1.04D;
	@Override
	public void renderTileEntityAt(TileEntityDiningTable te, double x, double y, double z, float partialTicks,
			int destroyStage) {
		double itemY = this.getItemYOffset(te);
		if (te.getEmbeddedFlowerPotPlant() != null) {
			SurfaceDisplayRenderHelper.renderPottedPlant(te.getEmbeddedFlowerPotPlant(), x, y, z, itemY);
		}

		SurfaceSetting setting = te.getSurfaceSetting();

		for (Slot slot : Slot.values()) {
			ItemStack itemStack = setting.getItem(slot);

			if (itemStack != null && itemStack.stackSize > 0) {
				SurfaceDisplayRenderHelper.renderSurfaceItem(itemStack, x, y, z,
					setting.getX(slot), setting.getZ(slot), itemY,
					this.getBlockSurfaceYOffset(te), this.getYaw(setting.getFacing(slot)));
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

	private double getItemYOffset(TileEntityDiningTable te) {
		if (te.getWorld() == null) {
			return ITEM_Y;
		}

		IBlockState state = te.getWorld().getBlockState(te.getPos());
		Block block = state.getBlock();

		return block instanceof DiningTable ? ((DiningTable)block).getDisplayItemYOffset() : ITEM_Y;
	}

	private double getBlockSurfaceYOffset(TileEntityDiningTable te) {
		if (te.getWorld() == null) {
			return 1.0D;
		}

		IBlockState state = te.getWorld().getBlockState(te.getPos());
		Block block = state.getBlock();

		return block instanceof DiningTable ? ((DiningTable)block).getDisplayBlockSurfaceYOffset() : 1.0D;
	}
}
