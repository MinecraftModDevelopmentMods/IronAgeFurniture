package zone.moddev.mc.ironagefurniture.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

public class GuiMechanicalIconButton extends GuiButton {
	public enum Icon {
		SEAL,
		OPEN,
		FLUSH,
		CONFIRM
	}

	private static final int BORDER = 0xFF3B2111;
	private static final int FACE = 0xFFB8792A;
	private static final int FACE_HOVER = 0xFFD6A14B;
	private static final int FACE_DISABLED = 0xFF6E6458;
	private static final int HIGHLIGHT = 0xFFE7C67F;
	private static final int SHADOW = 0xFF6A3A13;
	private Icon icon;

	public GuiMechanicalIconButton(int buttonId, int x, int y, int width, int height, Icon icon) {
		super(buttonId, x, y, width, height, "");
		this.icon = icon;
	}

	public void setIcon(Icon icon) {
		this.icon = icon;
	}

	@Override
	public void drawButton(Minecraft minecraft, int mouseX, int mouseY) {
		if (!this.visible) {
			return;
		}

		this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition
			&& mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
		int faceColor = !this.enabled ? FACE_DISABLED : this.hovered ? FACE_HOVER : FACE;
		drawRect(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + this.height, BORDER);
		drawRect(this.xPosition + 2, this.yPosition + 2, this.xPosition + this.width - 2,
			this.yPosition + this.height - 2, faceColor);
		drawRect(this.xPosition + 2, this.yPosition + 2, this.xPosition + this.width - 2,
			this.yPosition + 3, HIGHLIGHT);
		drawRect(this.xPosition + 2, this.yPosition + this.height - 3, this.xPosition + this.width - 2,
			this.yPosition + this.height - 2, SHADOW);
		this.drawIcon();
		this.mouseDragged(minecraft, mouseX, mouseY);
	}

	private void drawIcon() {
		int centerX = this.xPosition + this.width / 2;
		int centerY = this.yPosition + this.height / 2;
		MechanicalGuiRenderer.drawControlIcon(centerX - 6, centerY - 6,
			MechanicalGuiRenderer.ControlIcon.valueOf(this.icon.name()));
	}
}
