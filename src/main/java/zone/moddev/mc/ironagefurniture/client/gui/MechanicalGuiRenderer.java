package zone.moddev.mc.ironagefurniture.client.gui;

import org.lwjgl.opengl.GL11;

import zone.moddev.mc.ironagefurniture.Ironagefurniture;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

public final class MechanicalGuiRenderer {
	public enum ControlIcon {
		SEAL,
		OPEN,
		FLUSH,
		CONFIRM
	}

	public static final int DIAL_SOURCE_SIZE = 40;
	public static final int VALVE_GATE_WIDTH = 24;
	public static final int VALVE_GATE_HEIGHT = 70;
	public static final int VALVE_HANDLE_WIDTH = 20;
	public static final int VALVE_HANDLE_HEIGHT = 7;

	private static final ResourceLocation INSTRUMENTS = new ResourceLocation(Ironagefurniture.MODID,
		"textures/gui/mechanical_instruments.png");
	private static final int ATLAS_SIZE = 128;
	private static final int DIAL_U = 0;
	private static final int DIAL_V = 0;
	private static final int NEEDLE_U = 42;
	private static final int NEEDLE_V = 0;
	private static final int NEEDLE_WIDTH = 5;
	private static final int NEEDLE_HEIGHT = 18;
	private static final int VALVE_GATE_U = 48;
	private static final int VALVE_GATE_V = 0;
	private static final int VALVE_HANDLE_U = 72;
	private static final int VALVE_HANDLE_V = 0;
	private static final int CONTROL_ICON_U = 72;
	private static final int CONTROL_ICON_V = 10;
	private static final int CONTROL_ICON_SIZE = 12;
	private static final int CONTROL_ICON_STEP = 14;
	private static final int HUB_DARK = 0xFF2A170C;
	private static final int HUB_LIGHT = 0xFFC68A2A;
	private static final int HUB_IDLE = 0xFF786F61;
	private static final float FULL_COLOR = 1.0F;

	private MechanicalGuiRenderer() {
		throw new IllegalAccessError("This class cannot be instantiated");
	}

	public static float approach(float current, float target, float maximumChange) {
		if (current < target) {
			return Math.min(target, current + maximumChange);
		}

		return Math.max(target, current - maximumChange);
	}

	public static void drawDial(int x, int y, int size, float value, int centerColor, boolean enabled) {
		float clampedValue = Math.max(0.0F, Math.min(1.0F, value));
		bindAtlas();
		drawTexture(x, y, size, size, DIAL_U, DIAL_V, DIAL_SOURCE_SIZE, DIAL_SOURCE_SIZE);

		int centerX = x + size / 2;
		int centerY = y + size / 2 + Math.max(1, size / 16);
		int markerRadius = Math.max(2, size / 11);
		drawCircle(centerX, centerY, markerRadius + 1, HUB_DARK);
		drawCircle(centerX, centerY, markerRadius, enabled ? ensureOpaque(centerColor) : HUB_IDLE);
		drawNeedle(centerX, centerY, size, clampedValue);
		drawCircle(centerX, centerY, Math.max(1, size / 18), HUB_LIGHT);
	}

	public static void drawValveGate(int x, int y, int width, int height) {
		bindAtlas();
		drawTexture(x, y, width, height, VALVE_GATE_U, VALVE_GATE_V, VALVE_GATE_WIDTH, VALVE_GATE_HEIGHT);
	}

	public static void drawValveHandle(int centerX, int centerY) {
		bindAtlas();
		drawTexture(centerX - VALVE_HANDLE_WIDTH / 2, centerY - VALVE_HANDLE_HEIGHT / 2,
			VALVE_HANDLE_WIDTH, VALVE_HANDLE_HEIGHT, VALVE_HANDLE_U, VALVE_HANDLE_V,
			VALVE_HANDLE_WIDTH, VALVE_HANDLE_HEIGHT);
	}

	public static void drawControlIcon(int x, int y, ControlIcon icon) {
		bindAtlas();
		drawTexture(x, y, CONTROL_ICON_SIZE, CONTROL_ICON_SIZE,
			CONTROL_ICON_U + icon.ordinal() * CONTROL_ICON_STEP, CONTROL_ICON_V,
			CONTROL_ICON_SIZE, CONTROL_ICON_SIZE);
	}

	private static void drawNeedle(float centerX, float centerY, int dialSize, float value) {
		float angle = (float)Math.toRadians(-120.0F + value * 240.0F);
		float length = dialSize * 0.32F;
		float tail = Math.max(2.0F, dialSize * 0.07F);
		float halfWidth = Math.max(1.0F, dialSize * 0.035F);
		float sin = (float)Math.sin(angle);
		float cos = (float)Math.cos(angle);
		float[] localX = { -halfWidth, halfWidth, halfWidth, -halfWidth };
		float[] localY = { tail, tail, -length, -length };
		float[] u = { NEEDLE_U, NEEDLE_U + NEEDLE_WIDTH, NEEDLE_U + NEEDLE_WIDTH, NEEDLE_U };
		float[] v = { NEEDLE_V + NEEDLE_HEIGHT, NEEDLE_V + NEEDLE_HEIGHT, NEEDLE_V, NEEDLE_V };

		bindAtlas();
		VertexBuffer buffer = Tessellator.getInstance().getBuffer();
		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

		for (int i = 0; i < 4; i++) {
			float rotatedX = localX[i] * cos - localY[i] * sin;
			float rotatedY = localX[i] * sin + localY[i] * cos;
			buffer.pos(centerX + rotatedX, centerY + rotatedY, 100.0D)
				.tex(u[i] / ATLAS_SIZE, v[i] / ATLAS_SIZE).endVertex();
		}

		Tessellator.getInstance().draw();
	}

	private static void drawTexture(int x, int y, int width, int height, int u, int v,
			int sourceWidth, int sourceHeight) {
		float minU = (float)u / ATLAS_SIZE;
		float maxU = (float)(u + sourceWidth) / ATLAS_SIZE;
		float minV = (float)v / ATLAS_SIZE;
		float maxV = (float)(v + sourceHeight) / ATLAS_SIZE;
		VertexBuffer buffer = Tessellator.getInstance().getBuffer();
		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		buffer.pos(x, y + height, 90.0D).tex(minU, maxV).endVertex();
		buffer.pos(x + width, y + height, 90.0D).tex(maxU, maxV).endVertex();
		buffer.pos(x + width, y, 90.0D).tex(maxU, minV).endVertex();
		buffer.pos(x, y, 90.0D).tex(minU, minV).endVertex();
		Tessellator.getInstance().draw();
	}

	private static void bindAtlas() {
		Minecraft.getMinecraft().getTextureManager().bindTexture(INSTRUMENTS);
		GlStateManager.enableTexture2D();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager.color(FULL_COLOR, FULL_COLOR, FULL_COLOR, FULL_COLOR);
	}

	private static void drawCircle(int centerX, int centerY, int radius, int color) {
		for (int y = -radius; y <= radius; y++) {
			int halfWidth = (int)Math.floor(Math.sqrt(radius * radius - y * y));
			Gui.drawRect(centerX - halfWidth, centerY + y, centerX + halfWidth + 1, centerY + y + 1, color);
		}
	}

	private static int ensureOpaque(int color) {
		return (color & 0xFF000000) == 0 ? color | 0xFF000000 : color;
	}
}
