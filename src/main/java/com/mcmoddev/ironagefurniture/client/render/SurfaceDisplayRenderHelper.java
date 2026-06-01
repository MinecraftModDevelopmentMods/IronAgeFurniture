package com.mcmoddev.ironagefurniture.client.render;

import com.mcmoddev.ironagefurniture.api.Items.ItemBlockGlassVase;
import com.mcmoddev.ironagefurniture.api.Items.ItemBlockOrnament;
import com.mcmoddev.ironagefurniture.api.VasePlantHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.Items;
import net.minecraft.item.ItemRecord;
import net.minecraft.item.ItemStack;

import org.lwjgl.opengl.GL11;

public final class SurfaceDisplayRenderHelper {
	private SurfaceDisplayRenderHelper() {
	}

	public static boolean renderSpecialSurfaceItem(ItemStack itemStack, double x, double y, double z,
			double itemX, double itemZ, double surfaceY, double blockSurfaceY, float yaw) {
		if (itemStack == null || itemStack.stackSize <= 0) {
			return false;
		}

		if (isOrnament(itemStack)) {
			renderOrnament(itemStack, x, y, z, itemX, itemZ, blockSurfaceY, yaw);
			renderVasePlant(itemStack, x, y, z, itemX, itemZ, blockSurfaceY, yaw);
			return true;
		}

		if (!isBook(itemStack) && !isRecord(itemStack)) {
			return false;
		}

		GlStateManager.pushMatrix();
		GlStateManager.translate(x + itemX, y + surfaceY + 0.012D, z + itemZ);
		GlStateManager.rotate(yaw, 0.0F, 1.0F, 0.0F);
		GlStateManager.disableTexture2D();
		GlStateManager.disableLighting();

		if (isBook(itemStack)) {
			renderClosedBook(itemStack);
		} else {
			renderRecord(itemStack);
		}

		GlStateManager.enableLighting();
		GlStateManager.enableTexture2D();
		GlStateManager.popMatrix();
		return true;
	}

	public static void renderPottedPlant(ItemStack plantStack, double x, double y, double z, double surfaceY) {
		if (plantStack == null || plantStack.stackSize <= 0) {
			return;
		}

		GlStateManager.pushMatrix();
		GlStateManager.translate(x + 0.5D, y + surfaceY + 0.46D, z + 0.5D);
		GlStateManager.scale(0.52F, 0.52F, 0.52F);
		Minecraft.getMinecraft().getRenderItem().renderItem(plantStack,
			ItemCameraTransforms.TransformType.FIXED);
		GlStateManager.popMatrix();
	}

	private static void renderVasePlant(ItemStack vaseStack, double x, double y, double z, double itemX,
			double itemZ, double surfaceY, float yaw) {
		ItemStack plantStack = VasePlantHelper.getPlant(vaseStack);

		if (plantStack == null || plantStack.stackSize <= 0) {
			return;
		}

		GlStateManager.pushMatrix();
		GlStateManager.translate(x + itemX, y + surfaceY + 0.44D, z + itemZ);
		GlStateManager.rotate(yaw, 0.0F, 1.0F, 0.0F);
		GlStateManager.scale(0.42F, 0.42F, 0.42F);
		Minecraft.getMinecraft().getRenderItem().renderItem(plantStack,
			ItemCameraTransforms.TransformType.FIXED);
		GlStateManager.popMatrix();
	}

	private static boolean isBook(ItemStack itemStack) {
		return itemStack.getItem() == Items.BOOK
			|| itemStack.getItem() == Items.WRITABLE_BOOK
			|| itemStack.getItem() == Items.WRITTEN_BOOK
			|| itemStack.getItem() == Items.ENCHANTED_BOOK;
	}

	private static boolean isRecord(ItemStack itemStack) {
		return itemStack.getItem() instanceof ItemRecord;
	}

	private static boolean isOrnament(ItemStack itemStack) {
		return itemStack.getItem() instanceof ItemBlockOrnament
			|| itemStack.getItem() instanceof ItemBlockGlassVase;
	}

	private static void renderOrnament(ItemStack itemStack, double x, double y, double z, double itemX,
			double itemZ, double surfaceY, float yaw) {
		float scale = getOrnamentScale(itemStack);

		GlStateManager.pushMatrix();
		GlStateManager.translate(x + itemX, y + surfaceY + 0.5D * scale, z + itemZ);
		GlStateManager.rotate(yaw, 0.0F, 1.0F, 0.0F);
		GlStateManager.scale(scale, scale, scale);
		Minecraft.getMinecraft().getRenderItem().renderItem(itemStack,
			ItemCameraTransforms.TransformType.NONE);
		GlStateManager.popMatrix();
	}

	private static float getOrnamentScale(ItemStack itemStack) {
		if (itemStack.getItem() instanceof ItemBlockGlassVase) {
			return 0.62F;
		}

		if (itemStack.getItem() instanceof ItemBlockOrnament) {
			String modelName = ((ItemBlockOrnament)itemStack.getItem()).getModelName(itemStack.getMetadata());

			if (modelName.contains("_urn")) {
				return 0.78F;
			}
		}

		return 0.62F;
	}

	private static void renderClosedBook(ItemStack itemStack) {
		float[] cover = itemStack.getItem() == Items.ENCHANTED_BOOK
			? new float[] { 0.35F, 0.08F, 0.52F }
			: new float[] { 0.45F, 0.12F, 0.08F };

		drawCuboid(-0.24D, 0.0D, -0.18D, 0.24D, 0.052D, 0.18D, cover[0], cover[1], cover[2]);
		drawCuboid(-0.20D, 0.054D, -0.14D, 0.22D, 0.066D, 0.14D, 0.78F, 0.70F, 0.50F);
		drawCuboid(-0.24D, 0.066D, -0.18D, 0.24D, 0.078D, 0.18D, cover[0], cover[1], cover[2]);
		drawCuboid(-0.23D, 0.080D, -0.015D, 0.20D, 0.086D, 0.015D, 0.86F, 0.62F, 0.16F);
		drawCuboid(-0.015D, 0.080D, -0.16D, 0.015D, 0.086D, 0.16D, 0.86F, 0.62F, 0.16F);
	}

	private static void renderRecord(ItemStack itemStack) {
		int hash = itemStack.getItem().getUnlocalizedName().hashCode();
		float labelR = 0.25F + ((hash >> 16) & 3) * 0.16F;
		float labelG = 0.25F + ((hash >> 8) & 3) * 0.16F;
		float labelB = 0.25F + (hash & 3) * 0.16F;

		drawCuboid(-0.25D, 0.0D, -0.25D, 0.25D, 0.035D, 0.25D, 0.03F, 0.03F, 0.035F);
		drawCuboid(-0.12D, 0.038D, -0.12D, 0.12D, 0.050D, 0.12D, labelR, labelG, labelB);
		drawCuboid(-0.035D, 0.052D, -0.035D, 0.035D, 0.058D, 0.035D, 0.02F, 0.02F, 0.02F);
	}

	private static void drawCuboid(double minX, double minY, double minZ, double maxX, double maxY,
			double maxZ, float r, float g, float b) {
		Tessellator tessellator = Tessellator.getInstance();
		VertexBuffer renderer = tessellator.getBuffer();
		renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);

		addFace(renderer, minX, minY, minZ, maxX, minY, minZ, maxX, maxY, minZ, minX, maxY, minZ, r, g, b);
		addFace(renderer, minX, minY, maxZ, minX, maxY, maxZ, maxX, maxY, maxZ, maxX, minY, maxZ, r, g, b);
		addFace(renderer, minX, minY, minZ, minX, minY, maxZ, maxX, minY, maxZ, maxX, minY, minZ, r, g, b);
		addFace(renderer, minX, maxY, minZ, maxX, maxY, minZ, maxX, maxY, maxZ, minX, maxY, maxZ, r, g, b);
		addFace(renderer, minX, minY, minZ, minX, maxY, minZ, minX, maxY, maxZ, minX, minY, maxZ, r, g, b);
		addFace(renderer, maxX, minY, minZ, maxX, minY, maxZ, maxX, maxY, maxZ, maxX, maxY, minZ, r, g, b);

		tessellator.draw();
	}

	private static void addFace(VertexBuffer renderer, double x1, double y1, double z1, double x2, double y2,
			double z2, double x3, double y3, double z3, double x4, double y4, double z4, float r, float g, float b) {
		renderer.pos(x1, y1, z1).color(r, g, b, 1.0F).endVertex();
		renderer.pos(x2, y2, z2).color(r, g, b, 1.0F).endVertex();
		renderer.pos(x3, y3, z3).color(r, g, b, 1.0F).endVertex();
		renderer.pos(x4, y4, z4).color(r, g, b, 1.0F).endVertex();
	}
}
