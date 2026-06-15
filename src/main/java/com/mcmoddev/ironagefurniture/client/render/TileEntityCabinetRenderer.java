package com.mcmoddev.ironagefurniture.client.render;

import com.mcmoddev.ironagefurniture.api.Blocks.Cabinet;
import com.mcmoddev.ironagefurniture.api.tile.TileEntityCabinet;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemTransformVec3f;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

public class TileEntityCabinetRenderer extends TileEntitySpecialRenderer<TileEntityCabinet> {
	private static final double ITEM_Y = 1.04D;
	private static final float ITEM_SCALE = 0.5F;
	private static final float BLOCK_ITEM_SCALE = 0.55F;

	@Override
	public void renderTileEntityAt(TileEntityCabinet te, double x, double y, double z, float partialTicks,
			int destroyStage) {
		ItemStack itemStack = te.getDisplayedItem();

		if (itemStack == null || itemStack.stackSize <= 0) {
			return;
		}

		double itemY = this.getItemYOffset(te);
		double itemX = this.getItemXOffset(te);
		double itemZ = this.getItemZOffset(te);
		float yaw = SurfaceDisplayRenderHelper.isBook(itemStack) ? this.getYaw(te.getDisplayedItemFacing()) : 0.0F;

		if (SurfaceDisplayRenderHelper.renderSpecialSurfaceItem(itemStack, x, y, z, itemX, itemZ, itemY,
				this.getBlockSurfaceYOffset(te), yaw)) {
			return;
		}

		GlStateManager.pushMatrix();

		ItemCameraTransforms.TransformType transformType = ItemCameraTransforms.TransformType.FIXED;

		if (itemStack.getItem() instanceof ItemBlock) {
			ItemTransformVec3f fixedTransform = this.getFixedTransform(itemStack);
			transformType = this.hasTiltedTransform(fixedTransform) ? ItemCameraTransforms.TransformType.NONE
					: ItemCameraTransforms.TransformType.FIXED;
			GlStateManager.translate(x + itemX, y + itemY + this.getBlockItemLift(fixedTransform, transformType),
					z + itemZ);
			GlStateManager.scale(BLOCK_ITEM_SCALE, BLOCK_ITEM_SCALE, BLOCK_ITEM_SCALE);
		} else {
			GlStateManager.translate(x + itemX, y + itemY, z + itemZ);
			GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
			GlStateManager.scale(ITEM_SCALE, ITEM_SCALE, ITEM_SCALE);
		}

		Minecraft.getMinecraft().getRenderItem().renderItem(itemStack, transformType);
		GlStateManager.popMatrix();
	}

	private ItemTransformVec3f getFixedTransform(ItemStack itemStack) {
		IBakedModel model = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(itemStack);
		return model.getItemCameraTransforms().getTransform(ItemCameraTransforms.TransformType.FIXED);
	}

	private boolean hasTiltedTransform(ItemTransformVec3f transform) {
		return Math.abs(transform.rotation.x) > 0.001F || Math.abs(transform.rotation.z) > 0.001F;
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

	private double getBlockItemLift(ItemTransformVec3f fixedTransform,
			ItemCameraTransforms.TransformType transformType) {
		if (transformType == ItemCameraTransforms.TransformType.NONE) {
			return BLOCK_ITEM_SCALE / 2.0D;
		}

		return BLOCK_ITEM_SCALE * ((fixedTransform.scale.y / 2.0D) - fixedTransform.translation.y);
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
