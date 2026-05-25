package com.mcmoddev.ironagefurniture.client.render;

import com.mcmoddev.ironagefurniture.api.tile.TileEntityDiningTable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemTransformVec3f;
import net.minecraft.item.ItemBlock;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;

public class TileEntityDiningTableRenderer extends TileEntitySpecialRenderer<TileEntityDiningTable> {
	private static final double ITEM_Y = 1.04D;
	private static final float ITEM_SCALE = 0.5F;
	private static final float BLOCK_ITEM_SCALE = 0.55F;

	@Override
	public void renderTileEntityAt(TileEntityDiningTable te, double x, double y, double z, float partialTicks,
			int destroyStage) {
		ItemStack itemStack = te.getDisplayedItem();

		if (itemStack == null || itemStack.stackSize <= 0) {
			return;
		}

		GlStateManager.pushMatrix();

		ItemCameraTransforms.TransformType transformType = ItemCameraTransforms.TransformType.FIXED;

		if (itemStack.getItem() instanceof ItemBlock) {
			ItemTransformVec3f fixedTransform = this.getFixedTransform(itemStack);
			transformType = this.hasTiltedTransform(fixedTransform) ? ItemCameraTransforms.TransformType.NONE
					: ItemCameraTransforms.TransformType.FIXED;
			GlStateManager.translate(x + 0.5D, y + ITEM_Y + this.getBlockItemLift(fixedTransform, transformType),
					z + 0.5D);
			GlStateManager.scale(BLOCK_ITEM_SCALE, BLOCK_ITEM_SCALE, BLOCK_ITEM_SCALE);
		} else {
			GlStateManager.translate(x + 0.5D, y + ITEM_Y, z + 0.5D);
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

	private double getBlockItemLift(ItemTransformVec3f fixedTransform,
			ItemCameraTransforms.TransformType transformType) {
		if (transformType == ItemCameraTransforms.TransformType.NONE) {
			return BLOCK_ITEM_SCALE / 2.0D;
		}

		return BLOCK_ITEM_SCALE * ((fixedTransform.scale.y / 2.0D) - fixedTransform.translation.y);
	}
}
