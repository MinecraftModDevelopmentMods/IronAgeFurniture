package zone.moddev.mc.ironagefurniture.client.gui;

import zone.moddev.mc.ironagefurniture.api.DrinkDisplayHelper;
import zone.moddev.mc.ironagefurniture.api.tile.TileEntityBarrel;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fluids.FluidStack;

public final class AdjacentBarrelPanel {
	public static final int WIDTH = 106;
	public static final int HEIGHT = 112;
	public static final int DRAIN_BUTTON_X = 9;
	public static final int FILL_BUTTON_X = 57;
	public static final int BUTTON_Y = 84;
	public static final int BUTTON_WIDTH = 40;
	public static final int BUTTON_HEIGHT = 20;
	public static final int GAUGE_X = 12;
	public static final int GAUGE_Y = 48;
	public static final int GAUGE_WIDTH = 82;
	public static final int GAUGE_HEIGHT = 12;

	private static final int TITLE_Y = 8;
	private static final int NAME_X = 10;
	private static final int NAME_Y = 25;
	private static final int NAME_WIDTH = WIDTH - NAME_X * 2;
	private static final int SINGLE_LINE_NAME_OFFSET = 5;
	private static final int NAME_LINE_HEIGHT = 10;
	private static final int AMOUNT_Y = 65;
	private static final int FRAME_OUTER_INSET = 3;
	private static final int FRAME_INNER_INSET = 6;
	private static final int HOOP_TOP_Y = 20;
	private static final int HOOP_BOTTOM_Y = 77;
	private static final int HOOP_HEIGHT = 3;
	private static final int[] STAVE_X = { 24, 52, 80 };
	private static final int WOOD_DARK = 0xFF2E170A;
	private static final int WOOD_MID = 0xFF6B3A1B;
	private static final int WOOD_LIGHT = 0xFFD4B789;
	private static final int WOOD_EDGE = 0x406B3A1B;
	private static final int STAVE_LINE = 0x385A3219;
	private static final int HOOP_DARK = 0xFF51483F;
	private static final int HOOP_LIGHT = 0xFF8B8175;
	private static final int GAUGE_BORDER = 2;
	private static final int GAUGE_BORDER_COLOR = 0xFF404040;
	private static final int GAUGE_EMPTY_COLOR = 0xFF1F2633;
	private static final int TEXT_DARK = 0xFF2F1C10;

	private AdjacentBarrelPanel() {
		throw new IllegalAccessError("This class cannot be instantiated");
	}

	public static void drawBackground(int left, int top) {
		Gui.drawRect(left, top, left + WIDTH, top + HEIGHT, WOOD_DARK);
		Gui.drawRect(left + FRAME_OUTER_INSET, top + FRAME_OUTER_INSET,
			left + WIDTH - FRAME_OUTER_INSET, top + HEIGHT - FRAME_OUTER_INSET, WOOD_MID);
		Gui.drawRect(left + FRAME_INNER_INSET, top + FRAME_INNER_INSET,
			left + WIDTH - FRAME_INNER_INSET, top + HEIGHT - FRAME_INNER_INSET, WOOD_LIGHT);

		Gui.drawRect(left + FRAME_INNER_INSET, top + FRAME_INNER_INSET,
			left + FRAME_INNER_INSET + 3, top + HEIGHT - FRAME_INNER_INSET, WOOD_EDGE);
		Gui.drawRect(left + WIDTH - FRAME_INNER_INSET - 3, top + FRAME_INNER_INSET,
			left + WIDTH - FRAME_INNER_INSET, top + HEIGHT - FRAME_INNER_INSET, WOOD_EDGE);

		for (int staveX : STAVE_X) {
			Gui.drawRect(left + staveX, top + FRAME_INNER_INSET, left + staveX + 1,
				top + HEIGHT - FRAME_INNER_INSET, STAVE_LINE);
		}

		drawHoop(left, top + HOOP_TOP_Y);
		drawHoop(left, top + HOOP_BOTTOM_Y);
		drawGaugeBackground(left, top);
	}

	public static void drawForeground(FontRenderer fontRenderer, int left, int top,
			TileEntityBarrel barrel) {
		drawFittedCenteredText(fontRenderer, I18n.format("gui.ironagefurniture.transfer.barrel"),
			left + NAME_X, NAME_WIDTH, top + TITLE_Y);

		if (barrel == null) {
			drawDrinkName(fontRenderer, I18n.format("gui.ironagefurniture.transfer.no_barrel"), left, top);
			return;
		}

		FluidStack fluid = barrel.getFluid();
		String fluidName = fluid == null || fluid.getFluid() == null || fluid.amount <= 0
			? I18n.format("gui.ironagefurniture.barrel.empty") : DrinkDisplayHelper.getDisplayName(fluid);
		drawDrinkName(fontRenderer, fluidName, left, top);
		drawFittedCenteredText(fontRenderer,
			Integer.toString(barrel.getFluidAmount()) + " / " + Integer.toString(barrel.getCapacity()),
			left + NAME_X, NAME_WIDTH, top + AMOUNT_Y);
	}

	public static int getGaugeFillWidth(TileEntityBarrel barrel) {
		if (barrel == null || barrel.getCapacity() <= 0 || barrel.getFluidAmount() <= 0) {
			return 0;
		}

		return Math.min(GAUGE_WIDTH, Math.max(1,
			barrel.getFluidAmount() * GAUGE_WIDTH / barrel.getCapacity()));
	}

	private static void drawGaugeBackground(int left, int top) {
		int gaugeLeft = left + GAUGE_X;
		int gaugeTop = top + GAUGE_Y;
		Gui.drawRect(gaugeLeft - GAUGE_BORDER, gaugeTop - GAUGE_BORDER,
			gaugeLeft + GAUGE_WIDTH + GAUGE_BORDER, gaugeTop + GAUGE_HEIGHT + GAUGE_BORDER,
			GAUGE_BORDER_COLOR);
		Gui.drawRect(gaugeLeft, gaugeTop, gaugeLeft + GAUGE_WIDTH, gaugeTop + GAUGE_HEIGHT,
			GAUGE_EMPTY_COLOR);
	}

	private static void drawHoop(int left, int top) {
		Gui.drawRect(left + FRAME_INNER_INSET, top, left + WIDTH - FRAME_INNER_INSET,
			top + HOOP_HEIGHT, HOOP_DARK);
		Gui.drawRect(left + FRAME_INNER_INSET + 1, top, left + WIDTH - FRAME_INNER_INSET - 1,
			top + 1, HOOP_LIGHT);
	}

	private static void drawDrinkName(FontRenderer fontRenderer, String name, int left, int top) {
		String[] lines = splitName(fontRenderer, name);
		int firstLineY = top + NAME_Y + (lines.length == 1 ? SINGLE_LINE_NAME_OFFSET : 0);

		for (int i = 0; i < lines.length; i++) {
			drawFittedCenteredText(fontRenderer, lines[i], left + NAME_X, NAME_WIDTH,
				firstLineY + i * NAME_LINE_HEIGHT);
		}
	}

	private static String[] splitName(FontRenderer fontRenderer, String name) {
		if (fontRenderer.getStringWidth(name) <= NAME_WIDTH || name.indexOf(' ') < 0) {
			return new String[] { name };
		}

		String[] words = name.split(" ");
		int bestBreak = 1;
		int bestWidth = Integer.MAX_VALUE;

		for (int i = 1; i < words.length; i++) {
			int widestLine = Math.max(fontRenderer.getStringWidth(join(words, 0, i)),
				fontRenderer.getStringWidth(join(words, i, words.length)));

			if (widestLine < bestWidth) {
				bestWidth = widestLine;
				bestBreak = i;
			}
		}

		return new String[] { join(words, 0, bestBreak), join(words, bestBreak, words.length) };
	}

	private static String join(String[] words, int start, int end) {
		StringBuilder builder = new StringBuilder();

		for (int i = start; i < end; i++) {
			if (builder.length() > 0) {
				builder.append(' ');
			}

			builder.append(words[i]);
		}

		return builder.toString();
	}

	private static void drawFittedCenteredText(FontRenderer fontRenderer, String text, int x, int width, int y) {
		int textWidth = fontRenderer.getStringWidth(text);

		if (textWidth <= width) {
			fontRenderer.drawString(text, x + (width - textWidth) / 2, y, TEXT_DARK);
			return;
		}

		float scale = (float)width / (float)textWidth;
		GlStateManager.pushMatrix();
		GlStateManager.translate(x + width / 2.0F, y, 0.0F);
		GlStateManager.scale(scale, scale, 1.0F);
		fontRenderer.drawString(text, -textWidth / 2, 0, TEXT_DARK);
		GlStateManager.popMatrix();
	}
}
