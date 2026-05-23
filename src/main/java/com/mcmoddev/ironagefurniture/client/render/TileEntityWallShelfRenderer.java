package com.mcmoddev.ironagefurniture.client.render;

import com.mcmoddev.ironagefurniture.api.Blocks.WallShelf;
import com.mcmoddev.ironagefurniture.api.tile.TileEntityWallShelf;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;

public class TileEntityWallShelfRenderer extends TileEntitySpecialRenderer<TileEntityWallShelf> {
	@Override
	public void renderTileEntityAt(TileEntityWallShelf te, double x, double y, double z, float partialTicks,
			int destroyStage) {
		ItemStack itemStack = te.getDisplayedItem();

		if (itemStack == null || itemStack.stackSize <= 0) {
			return;
		}

		EnumFacing facing = this.getFacing(te);
		double itemX = 0.5D - facing.getFrontOffsetX() * 0.125D;
		double itemZ = 0.5D - facing.getFrontOffsetZ() * 0.125D;

		GlStateManager.pushMatrix();
		GlStateManager.translate(x + itemX, y + 0.86D, z + itemZ);
		GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
		GlStateManager.scale(0.45F, 0.45F, 0.45F);
		Minecraft.getMinecraft().getRenderItem().renderItem(itemStack, ItemCameraTransforms.TransformType.FIXED);
		GlStateManager.popMatrix();
	}

	private EnumFacing getFacing(TileEntityWallShelf te) {
		World world = te.getWorld();
		BlockPos pos = te.getPos();

		if (world != null && pos != null) {
			IBlockState state = world.getBlockState(pos);

			if (state.getBlock() instanceof WallShelf) {
				return state.getValue(WallShelf.FACING);
			}
		}

		return EnumFacing.NORTH;
	}
}
