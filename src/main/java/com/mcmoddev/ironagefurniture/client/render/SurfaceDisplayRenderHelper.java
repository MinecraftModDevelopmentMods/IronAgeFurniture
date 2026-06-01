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

		boolean book = isBook(itemStack);
		double displayY = blockSurfaceY + (book ? -0.003D : 0.002D);
		GlStateManager.pushMatrix();
		GlStateManager.translate(x + itemX, y + displayY, z + itemZ);
		GlStateManager.rotate(yaw, 0.0F, 1.0F, 0.0F);
		GlStateManager.disableLighting();

		if (book) {
			renderClosedBook(itemStack);
		} else {
			GlStateManager.disableTexture2D();
			renderRecord(itemStack);
			GlStateManager.enableTexture2D();
		}

		GlStateManager.enableLighting();
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

	public static void renderGlassVasePlant(ItemStack plantStack, double x, double y, double z, double itemX,
			double itemZ, double surfaceY, float yaw) {
		if (plantStack == null || plantStack.stackSize <= 0) {
			return;
		}

		GlStateManager.pushMatrix();
		GlStateManager.translate(x + itemX, y + surfaceY, z + itemZ);
		GlStateManager.rotate(yaw, 0.0F, 1.0F, 0.0F);

		renderVaseStem();

		GlStateManager.translate(0.0D, 0.40D, 0.0D);
		GlStateManager.scale(0.56F, 0.56F, 0.56F);
		Minecraft.getMinecraft().getRenderItem().renderItem(plantStack,
			ItemCameraTransforms.TransformType.FIXED);
		GlStateManager.popMatrix();
	}

	private static void renderVasePlant(ItemStack vaseStack, double x, double y, double z, double itemX,
			double itemZ, double surfaceY, float yaw) {
		ItemStack plantStack = VasePlantHelper.getPlant(vaseStack);

		renderGlassVasePlant(plantStack, x, y, z, itemX, itemZ, surfaceY, yaw);
	}

	public static boolean isBook(ItemStack itemStack) {
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
			return 0.83F;
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
		float[] cover = getBookCoverColor(itemStack);
		float[] shadow = darken(cover, 0.42F);
		float[] pages = new float[] { 0.78F, 0.72F, 0.52F };
		float[] pageLines = new float[] { 0.48F, 0.40F, 0.25F };
		float[] gold = new float[] { 0.86F, 0.58F, 0.12F };

		GlStateManager.disableTexture2D();
		GlStateManager.disableCull();
		drawCuboid(-0.31D, 0.000D, -0.22D, 0.31D, 0.026D, 0.22D, shadow[0], shadow[1], shadow[2]);
		drawCuboid(-0.235D, 0.028D, -0.178D, 0.270D, 0.098D, 0.178D, pages[0], pages[1], pages[2]);
		drawCuboid(-0.31D, 0.100D, -0.22D, 0.31D, 0.136D, 0.22D, cover[0], cover[1], cover[2]);
		drawCuboid(-0.330D, 0.028D, -0.22D, -0.310D, 0.098D, 0.22D, shadow[0], shadow[1], shadow[2]);

		drawCuboid(0.270D, 0.034D, -0.165D, 0.286D, 0.096D, 0.165D, pageLines[0], pageLines[1],
			pageLines[2]);
		drawCuboid(0.232D, 0.034D, -0.165D, 0.248D, 0.096D, 0.165D, pageLines[0], pageLines[1],
			pageLines[2]);

		drawBookTopPattern(gold);
		GlStateManager.enableCull();
		GlStateManager.enableTexture2D();
	}

	private static void renderVaseStem() {
		GlStateManager.disableTexture2D();
		GlStateManager.disableLighting();
		GlStateManager.disableDepth();

		drawCuboid(-0.012D, 0.05D, -0.012D, 0.012D, 0.40D, 0.012D, 0.08F, 0.34F, 0.08F);
		drawCuboid(-0.075D, 0.19D, -0.008D, -0.012D, 0.25D, 0.008D, 0.08F, 0.30F, 0.07F);
		drawCuboid(0.012D, 0.29D, -0.008D, 0.070D, 0.35D, 0.008D, 0.08F, 0.30F, 0.07F);

		GlStateManager.enableDepth();
		GlStateManager.enableLighting();
		GlStateManager.enableTexture2D();
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

	private static float[] getBookCoverColor(ItemStack itemStack) {
		if (itemStack.getItem() == Items.ENCHANTED_BOOK) {
			return new float[] { 0.18F, 0.07F, 0.30F };
		}

		if (itemStack.getItem() == Items.WRITABLE_BOOK) {
			return new float[] { 0.04F, 0.22F, 0.10F };
		}

		if (itemStack.getItem() == Items.WRITTEN_BOOK) {
			return new float[] { 0.05F, 0.10F, 0.30F };
		}

		return new float[] { 0.38F, 0.07F, 0.04F };
	}

	private static float[] darken(float[] color, float amount) {
		return new float[] { color[0] * amount, color[1] * amount, color[2] * amount };
	}

	private static void drawBookTopPattern(float[] gold) {
		double y1 = 0.137D;
		double y2 = 0.143D;

		drawCuboid(-0.245D, y1, -0.165D, 0.220D, y2, -0.145D, gold[0], gold[1], gold[2]);
		drawCuboid(-0.245D, y1, 0.145D, 0.220D, y2, 0.165D, gold[0], gold[1], gold[2]);
		drawCuboid(-0.245D, y1, -0.165D, -0.225D, y2, 0.165D, gold[0], gold[1], gold[2]);
		drawCuboid(0.200D, y1, -0.165D, 0.220D, y2, 0.165D, gold[0], gold[1], gold[2]);

		drawCuboid(-0.120D, y1, -0.075D, 0.100D, y2, -0.055D, gold[0], gold[1], gold[2]);
		drawCuboid(-0.120D, y1, 0.055D, 0.100D, y2, 0.075D, gold[0], gold[1], gold[2]);
		drawCuboid(-0.120D, y1, -0.075D, -0.100D, y2, 0.075D, gold[0], gold[1], gold[2]);
		drawCuboid(0.080D, y1, -0.075D, 0.100D, y2, 0.075D, gold[0], gold[1], gold[2]);

		drawCuboid(-0.300D, y1, -0.170D, -0.282D, y2, 0.170D, gold[0], gold[1], gold[2]);
		drawCuboid(-0.318D, y1, -0.080D, -0.264D, y2, -0.060D, gold[0], gold[1], gold[2]);
		drawCuboid(-0.318D, y1, 0.060D, -0.264D, y2, 0.080D, gold[0], gold[1], gold[2]);
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
