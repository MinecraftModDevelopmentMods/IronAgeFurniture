package com.mcmoddev.ironagefurniture.client.render;

import com.mcmoddev.ironagefurniture.api.Blocks.Cabinet;
import com.mcmoddev.ironagefurniture.api.surface.SurfaceSetting;
import com.mcmoddev.ironagefurniture.api.surface.SurfaceSetting.Slot;
import com.mcmoddev.ironagefurniture.api.tile.TileEntityCabinet;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

public class TileEntityCabinetRenderer extends TileEntitySpecialRenderer<TileEntityCabinet> {
	private static final double ITEM_Y = 1.04D;
	@Override
	public void renderTileEntityAt(TileEntityCabinet te, double x, double y, double z, float partialTicks,
			int destroyStage) {
		double itemY = this.getItemYOffset(te);
		double baseX = this.getItemXOffset(te);
		double baseZ = this.getItemZOffset(te);
		SurfaceSetting setting = te.getSurfaceSetting();

		for (Slot slot : Slot.values()) {
			ItemStack itemStack = setting.getItem(slot);

			if (itemStack != null && itemStack.stackSize > 0) {
				double itemX = baseX + setting.getX(slot) - 0.5D;
				double itemZ = baseZ + setting.getZ(slot) - 0.5D;
				SurfaceDisplayRenderHelper.renderSurfaceItem(itemStack, x, y, z, itemX, itemZ, itemY,
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

	private double getItemYOffset(TileEntityCabinet te) {
		if (te.getWorld() == null) {
			return ITEM_Y;
		}

		IBlockState state = te.getWorld().getBlockState(te.getPos());
		Block block = state.getBlock();

		return block instanceof Cabinet ? ((Cabinet)block).getDisplayItemYOffset() : ITEM_Y;
	}

	private double getItemXOffset(TileEntityCabinet te) {
		if (te.getWorld() == null) {
			return 0.5D;
		}

		IBlockState state = te.getWorld().getBlockState(te.getPos());
		Block block = state.getBlock();

		return block instanceof Cabinet ? ((Cabinet)block).getDisplayItemXOffset(state) : 0.5D;
	}

	private double getItemZOffset(TileEntityCabinet te) {
		if (te.getWorld() == null) {
			return 0.5D;
		}

		IBlockState state = te.getWorld().getBlockState(te.getPos());
		Block block = state.getBlock();

		return block instanceof Cabinet ? ((Cabinet)block).getDisplayItemZOffset(state) : 0.5D;
	}

	private double getBlockSurfaceYOffset(TileEntityCabinet te) {
		if (te.getWorld() == null) {
			return 1.0D;
		}

		IBlockState state = te.getWorld().getBlockState(te.getPos());
		Block block = state.getBlock();

		return block instanceof Cabinet ? ((Cabinet)block).getDisplayBlockSurfaceYOffset() : 1.0D;
	}
}
