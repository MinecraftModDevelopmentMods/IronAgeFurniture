package com.mcmoddev.ironagefurniture.client.render;

import java.util.Locale;

import org.lwjgl.opengl.GL11;

import com.mcmoddev.ironagefurniture.api.Blocks.BottleRack;
import com.mcmoddev.ironagefurniture.api.tile.TileEntityBottleRack;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityBottleRackRenderer extends TileEntitySpecialRenderer<TileEntityBottleRack> {
	private static final double[] SLOT_X = new double[] { 0.225D, 0.5D, 0.775D };
	private static final double[] SLOT_Y = new double[] { 0.775D, 0.5D, 0.225D };
	private static final double SLOT_DEPTH = 0.690D;
	private static ResourceLocation whiteTexture;

	@Override
	public void renderTileEntityAt(TileEntityBottleRack te, double x, double y, double z, float partialTicks,
			int destroyStage) {
		EnumFacing facing = this.getFacing(te);

		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);
		GlStateManager.disableCull();
		GlStateManager.enableRescaleNormal();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.bindWhiteTexture();

		for (int slot = 0; slot < TileEntityBottleRack.SLOT_COUNT; slot++) {
			ItemStack bottle = te.getBottle(slot);

			if (bottle != null && bottle.stackSize > 0) {
				this.renderBottleInSlot(bottle, facing, slot);
			}
		}

		GlStateManager.disableRescaleNormal();
		GlStateManager.enableCull();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.popMatrix();
	}

	private void renderBottleInSlot(ItemStack bottle, EnumFacing facing, int slot) {
		int row = slot / 3;
		int column = slot % 3;
		double[] point = this.rotateRackPoint(facing, SLOT_X[column], SLOT_DEPTH);

		GlStateManager.pushMatrix();
		GlStateManager.translate(point[0], SLOT_Y[row], point[1]);
		GlStateManager.rotate(this.getYaw(facing), 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(slot % 2 == 0 ? -12.0F : 12.0F, 0.0F, 0.0F, 1.0F);
		GlStateManager.scale(0.82D, 0.82D, 0.82D);
		this.renderBottleShape(this.getBottleColor(bottle), this.getCapColor(bottle));
		GlStateManager.popMatrix();
	}

	private void renderBottleShape(float[] liquid, float[] cap) {
		float[] glass = new float[] { 0.75F, 0.92F, 0.86F };
		float[] shadow = this.darken(liquid, 0.52F);
		float[] label = new float[] { 0.86F, 0.78F, 0.60F };

		this.drawCuboid(-0.130D, -0.046D, -0.024D, 0.040D, 0.046D, 0.024D,
			shadow[0], shadow[1], shadow[2]);
		this.drawCuboid(-0.118D, -0.036D, -0.020D, 0.038D, 0.036D, 0.020D,
			liquid[0], liquid[1], liquid[2]);
		this.drawCuboid(-0.022D, -0.038D, -0.022D, 0.018D, 0.038D, -0.025D,
			label[0], label[1], label[2]);
		this.drawCuboid(0.036D, -0.022D, -0.016D, 0.118D, 0.022D, 0.016D,
			this.darken(liquid, 0.85F));
		this.drawCuboid(0.112D, -0.018D, -0.014D, 0.154D, 0.018D, 0.014D,
			cap[0], cap[1], cap[2]);

		this.beginGlassLayer();
		this.drawCuboid(-0.140D, -0.054D, -0.030D, 0.044D, 0.054D, 0.030D,
			glass[0], glass[1], glass[2], 0.24F);
		this.drawCuboid(0.040D, -0.028D, -0.022D, 0.122D, 0.028D, 0.022D,
			glass[0], glass[1], glass[2], 0.20F);
		this.endGlassLayer();
	}

	private float[] getBottleColor(ItemStack stack) {
		Item item = stack.getItem();

		if (item == Items.GLASS_BOTTLE) {
			return new float[] { 0.75F, 0.88F, 0.78F };
		}

		if (item == Items.EXPERIENCE_BOTTLE) {
			return new float[] { 0.34F, 0.86F, 0.30F };
		}

		if (item == Items.POTIONITEM || item == Items.SPLASH_POTION || item == Items.LINGERING_POTION) {
			return this.colorFromInt(PotionUtils.getPotionColorFromEffectList(PotionUtils.getEffectsFromStack(stack)));
		}

		ResourceLocation registryName = item.getRegistryName();

		if (registryName != null) {
			return this.getHarvestCraftBottleColor(registryName.getResourcePath().toLowerCase(Locale.ROOT));
		}

		return new float[] { 0.58F, 0.24F, 0.14F };
	}

	private float[] getCapColor(ItemStack stack) {
		Item item = stack.getItem();

		if (item == Items.GLASS_BOTTLE || item == Items.POTIONITEM || item == Items.SPLASH_POTION
				|| item == Items.LINGERING_POTION) {
			return new float[] { 0.56F, 0.40F, 0.20F };
		}

		return new float[] { 0.26F, 0.16F, 0.09F };
	}

	private float[] getHarvestCraftBottleColor(String itemName) {
		if (this.containsAny(itemName, "rootbeer", "cola", "soysauce", "hoisin")) {
			return new float[] { 0.30F, 0.14F, 0.07F };
		}
		if (this.containsAny(itemName, "grape", "plum", "blackberry", "blueberry")) {
			return new float[] { 0.36F, 0.15F, 0.58F };
		}
		if (this.containsAny(itemName, "cherry", "strawberry", "cranberry", "raspberry", "hotsauce")) {
			return new float[] { 0.72F, 0.12F, 0.12F };
		}
		if (this.containsAny(itemName, "orange", "lemon", "lime", "mango", "peach", "pineapple")) {
			return new float[] { 0.92F, 0.58F, 0.16F };
		}
		if (this.containsAny(itemName, "pear", "apple", "cider")) {
			return new float[] { 0.72F, 0.50F, 0.16F };
		}
		if (this.containsAny(itemName, "water")) {
			return new float[] { 0.32F, 0.64F, 0.94F };
		}
		if (this.containsAny(itemName, "oil")) {
			return new float[] { 0.88F, 0.76F, 0.24F };
		}
		if (this.containsAny(itemName, "vinegar")) {
			return new float[] { 0.86F, 0.76F, 0.52F };
		}

		return new float[] { 0.56F, 0.24F, 0.12F };
	}

	private float[] colorFromInt(int color) {
		float r = (float)(color >> 16 & 255) / 255.0F;
		float g = (float)(color >> 8 & 255) / 255.0F;
		float b = (float)(color & 255) / 255.0F;
		return new float[] { Math.max(r, 0.08F), Math.max(g, 0.08F), Math.max(b, 0.08F) };
	}

	private boolean containsAny(String value, String... matches) {
		for (String match : matches) {
			if (value.contains(match)) {
				return true;
			}
		}

		return false;
	}

	private float[] darken(float[] color, float factor) {
		return new float[] { color[0] * factor, color[1] * factor, color[2] * factor };
	}

	private double[] rotateRackPoint(EnumFacing facing, double localX, double localZ) {
		switch (facing) {
		case EAST:
			return new double[] { 1.0D - localZ, localX };
		case SOUTH:
			return new double[] { 1.0D - localX, 1.0D - localZ };
		case WEST:
			return new double[] { localZ, 1.0D - localX };
		default:
			return new double[] { localX, localZ };
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

	private EnumFacing getFacing(TileEntityBottleRack te) {
		World world = te.getWorld();
		BlockPos pos = te.getPos();

		if (world != null && pos != null) {
			IBlockState state = world.getBlockState(pos);

			if (state.getBlock() instanceof BottleRack) {
				return state.getValue(BottleRack.FACING);
			}
		}

		return EnumFacing.NORTH;
	}

	private void bindWhiteTexture() {
		if (whiteTexture == null) {
			DynamicTexture texture = new DynamicTexture(1, 1);
			texture.getTextureData()[0] = 0xFFFFFFFF;
			texture.updateDynamicTexture();
			whiteTexture = Minecraft.getMinecraft().getTextureManager()
				.getDynamicTextureLocation("ironagefurniture_bottle_rack", texture);
		}

		Minecraft.getMinecraft().getTextureManager().bindTexture(whiteTexture);
	}

	private void drawCuboid(double minX, double minY, double minZ, double maxX, double maxY,
			double maxZ, float[] color) {
		this.drawCuboid(minX, minY, minZ, maxX, maxY, maxZ, color[0], color[1], color[2]);
	}

	private void drawCuboid(double minX, double minY, double minZ, double maxX, double maxY,
			double maxZ, float r, float g, float b) {
		this.drawCuboid(minX, minY, minZ, maxX, maxY, maxZ, r, g, b, 1.0F);
	}

	private void drawCuboid(double minX, double minY, double minZ, double maxX, double maxY,
			double maxZ, float r, float g, float b, float alpha) {
		Tessellator tessellator = Tessellator.getInstance();
		VertexBuffer renderer = tessellator.getBuffer();
		renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);

		this.addFace(renderer, minX, minY, minZ, maxX, minY, minZ, maxX, maxY, minZ, minX, maxY, minZ,
			r, g, b, alpha, 0.0F, 0.0F, -1.0F);
		this.addFace(renderer, minX, minY, maxZ, minX, maxY, maxZ, maxX, maxY, maxZ, maxX, minY, maxZ,
			r, g, b, alpha, 0.0F, 0.0F, 1.0F);
		this.addFace(renderer, minX, minY, minZ, minX, minY, maxZ, maxX, minY, maxZ, maxX, minY, minZ,
			r, g, b, alpha, 0.0F, -1.0F, 0.0F);
		this.addFace(renderer, minX, maxY, minZ, maxX, maxY, minZ, maxX, maxY, maxZ, minX, maxY, maxZ,
			r, g, b, alpha, 0.0F, 1.0F, 0.0F);
		this.addFace(renderer, minX, minY, minZ, minX, maxY, minZ, minX, maxY, maxZ, minX, minY, maxZ,
			r, g, b, alpha, -1.0F, 0.0F, 0.0F);
		this.addFace(renderer, maxX, minY, minZ, maxX, minY, maxZ, maxX, maxY, maxZ, maxX, maxY, minZ,
			r, g, b, alpha, 1.0F, 0.0F, 0.0F);

		tessellator.draw();
	}

	private void addFace(VertexBuffer renderer, double x1, double y1, double z1, double x2, double y2,
			double z2, double x3, double y3, double z3, double x4, double y4, double z4, float r,
			float g, float b, float alpha, float normalX, float normalY, float normalZ) {
		renderer.pos(x1, y1, z1).tex(0.0D, 0.0D).color(r, g, b, alpha)
			.normal(normalX, normalY, normalZ).endVertex();
		renderer.pos(x2, y2, z2).tex(1.0D, 0.0D).color(r, g, b, alpha)
			.normal(normalX, normalY, normalZ).endVertex();
		renderer.pos(x3, y3, z3).tex(1.0D, 1.0D).color(r, g, b, alpha)
			.normal(normalX, normalY, normalZ).endVertex();
		renderer.pos(x4, y4, z4).tex(0.0D, 1.0D).color(r, g, b, alpha)
			.normal(normalX, normalY, normalZ).endVertex();
	}

	private void beginGlassLayer() {
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager.depthMask(false);
	}

	private void endGlassLayer() {
		GlStateManager.depthMask(true);
		GL11.glDisable(GL11.GL_BLEND);
	}
}
