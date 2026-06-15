package com.mcmoddev.ironagefurniture.client.render;

import com.mcmoddev.ironagefurniture.api.Blocks.WallShelf;
import com.mcmoddev.ironagefurniture.api.tile.TileEntityWallShelf;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemTransformVec3f;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;

public class TileEntityWallShelfRenderer extends TileEntitySpecialRenderer<TileEntityWallShelf> {
	private static final double ITEM_Y = 0.86D;
	private static final double SHELF_TOP_Y = 0.8125D;
	private static final float ITEM_SCALE = 0.45F;
	private static final float BLOCK_ITEM_SCALE = 0.35F;

	@Override
	public void renderTileEntityAt(TileEntityWallShelf te, double x, double y, double z, float partialTicks,
			int destroyStage) {
		ItemStack itemStack = te.getDisplayedItem();

		EnumFacing facing = this.getFacing(te);
		double itemX = 0.5D - facing.getFrontOffsetX() * 0.125D;
		double itemZ = 0.5D - facing.getFrontOffsetZ() * 0.125D;

		if (te.getEmbeddedKind() == WallShelf.ShelfContentKind.FLOWER_POT && te.getLastEmbeddedItem() != null
				&& !this.isSameStack(te.getLastEmbeddedItem(), te.getFirstEmbeddedItem())) {
			SurfaceDisplayRenderHelper.renderPottedPlant(te.getLastEmbeddedItem(), x, y, z, SHELF_TOP_Y);
		}

		if (itemStack == null || itemStack.stackSize <= 0) {
			return;
		}

		float yaw = SurfaceDisplayRenderHelper.isBook(itemStack)
			? this.getYaw(te.getDisplayedItemFacing())
			: this.getYaw(facing);

		if (SurfaceDisplayRenderHelper.renderSpecialSurfaceItem(itemStack, x, y, z, itemX, itemZ, ITEM_Y,
				SHELF_TOP_Y, yaw)) {
			return;
		}

		GlStateManager.pushMatrix();

		ItemCameraTransforms.TransformType transformType = ItemCameraTransforms.TransformType.FIXED;

		if (itemStack.getItem() instanceof ItemBlock) {
			ItemTransformVec3f fixedTransform = this.getFixedTransform(itemStack);
			transformType = this.hasTiltedTransform(fixedTransform) ? ItemCameraTransforms.TransformType.NONE
					: ItemCameraTransforms.TransformType.FIXED;
			GlStateManager.translate(x + itemX, y + SHELF_TOP_Y + this.getBlockItemLift(fixedTransform, transformType)
					+ 0.01D, z + itemZ);
			GlStateManager.scale(BLOCK_ITEM_SCALE, BLOCK_ITEM_SCALE, BLOCK_ITEM_SCALE);
		} else {
			GlStateManager.translate(x + itemX, y + ITEM_Y, z + itemZ);
			GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
			GlStateManager.scale(ITEM_SCALE, ITEM_SCALE, ITEM_SCALE);
		}

		Minecraft.getMinecraft().getRenderItem().renderItem(itemStack, transformType);
		GlStateManager.popMatrix();
	}

	private boolean isSameStack(ItemStack first, ItemStack second) {
		return first != null && second != null && first.isItemEqual(second)
			&& ItemStack.areItemStackTagsEqual(first, second);
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
