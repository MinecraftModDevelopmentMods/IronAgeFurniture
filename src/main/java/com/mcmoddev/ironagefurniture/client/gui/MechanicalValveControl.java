package com.mcmoddev.ironagefurniture.client.gui;

import javax.annotation.Nullable;

import com.mcmoddev.ironagefurniture.api.Enumerations.FluidPortMode;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;

public final class MechanicalValveControl {
	public static final int WIDTH = 84;
	public static final int HEIGHT = 96;

	private static final int TITLE_Y = 9;
	private static final int LABEL_X = 7;
	private static final int LABEL_WIDTH = 43;
	private static final int GATE_X = 54;
	private static final int GATE_Y = 20;
	private static final int GATE_WIDTH = 20;
	private static final int GATE_HEIGHT = 66;
	private static final int FLOOD_Y = 29;
	private static final int LOCK_Y = 52;
	private static final int DRAIN_Y = 75;
	private static final int HIT_HALF_HEIGHT = 9;
	private static final float LEVER_MAX_CHANGE_PER_FRAME = 0.125F;
	private static final int TEXT_COLOR = 0xFF2F1C10;
	private static final int DISABLED_TEXT_COLOR = 0xFF806F5E;

	private int x;
	private int y;
	private float leverPosition = Float.NaN;

	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void draw(FontRenderer fontRenderer, FluidPortMode mode, boolean inletAvailable,
			boolean outletAvailable, boolean machineLocked) {
		FluidPortMode shownMode = machineLocked ? FluidPortMode.LOCKED : mode;
		float target = getLeverPosition(shownMode);

		if (Float.isNaN(this.leverPosition)) {
			this.leverPosition = target;
		} else {
			this.leverPosition = MechanicalGuiRenderer.approach(this.leverPosition, target,
				LEVER_MAX_CHANGE_PER_FRAME);
		}

		drawCentered(fontRenderer, I18n.format("gui.ironagefurniture.valve.title"), this.x, WIDTH,
			this.y + TITLE_Y, TEXT_COLOR);
		drawCentered(fontRenderer, I18n.format("gui.ironagefurniture.valve.flood"), this.x + LABEL_X,
			LABEL_WIDTH, this.y + FLOOD_Y - 4, inletAvailable && !machineLocked ? TEXT_COLOR : DISABLED_TEXT_COLOR);
		drawCentered(fontRenderer, I18n.format("gui.ironagefurniture.valve.lock"), this.x + LABEL_X,
			LABEL_WIDTH, this.y + LOCK_Y - 4, TEXT_COLOR);
		drawCentered(fontRenderer, I18n.format("gui.ironagefurniture.valve.drain"), this.x + LABEL_X,
			LABEL_WIDTH, this.y + DRAIN_Y - 4, outletAvailable && !machineLocked ? TEXT_COLOR : DISABLED_TEXT_COLOR);

		MechanicalGuiRenderer.drawValveGate(this.x + GATE_X, this.y + GATE_Y, GATE_WIDTH, GATE_HEIGHT);
		int leverY = this.y + FLOOD_Y + Math.round(this.leverPosition * (DRAIN_Y - FLOOD_Y));
		MechanicalGuiRenderer.drawValveHandle(this.x + GATE_X + GATE_WIDTH / 2, leverY);
	}

	@Nullable
	public FluidPortMode getClickedMode(int mouseX, int mouseY, boolean inletAvailable,
			boolean outletAvailable, boolean machineLocked) {
		if (machineLocked || mouseX < this.x || mouseX >= this.x + WIDTH) {
			return null;
		}

		if (isNear(mouseY, this.y + FLOOD_Y)) {
			return inletAvailable ? FluidPortMode.FLOOD : null;
		}
		if (isNear(mouseY, this.y + LOCK_Y)) {
			return FluidPortMode.LOCKED;
		}
		if (isNear(mouseY, this.y + DRAIN_Y)) {
			return outletAvailable ? FluidPortMode.DRAIN : null;
		}

		return null;
	}

	@Nullable
	public String getHoverText(int mouseX, int mouseY, boolean inletAvailable, boolean outletAvailable,
			boolean machineLocked) {
		if (mouseX < this.x || mouseX >= this.x + WIDTH) {
			return null;
		}

		if (machineLocked && mouseY >= this.y + 18 && mouseY < this.y + HEIGHT) {
			return I18n.format("gui.ironagefurniture.valve.machine_locked");
		}
		if (isNear(mouseY, this.y + FLOOD_Y)) {
			return I18n.format(inletAvailable ? "gui.ironagefurniture.valve.flood_tooltip"
				: "gui.ironagefurniture.valve.no_inlet");
		}
		if (isNear(mouseY, this.y + LOCK_Y)) {
			return I18n.format("gui.ironagefurniture.valve.lock_tooltip");
		}
		if (isNear(mouseY, this.y + DRAIN_Y)) {
			return I18n.format(outletAvailable ? "gui.ironagefurniture.valve.drain_tooltip"
				: "gui.ironagefurniture.valve.no_outlet");
		}

		return null;
	}

	private boolean isNear(int mouseY, int targetY) {
		return mouseY >= targetY - HIT_HALF_HEIGHT && mouseY <= targetY + HIT_HALF_HEIGHT;
	}

	private static float getLeverPosition(FluidPortMode mode) {
		if (mode == FluidPortMode.FLOOD) {
			return 0.0F;
		}
		if (mode == FluidPortMode.DRAIN) {
			return 1.0F;
		}

		return 0.5F;
	}

	private static void drawCentered(FontRenderer fontRenderer, String text, int x, int width, int y, int color) {
		fontRenderer.drawString(text, x + (width - fontRenderer.getStringWidth(text)) / 2, y, color);
	}

}
