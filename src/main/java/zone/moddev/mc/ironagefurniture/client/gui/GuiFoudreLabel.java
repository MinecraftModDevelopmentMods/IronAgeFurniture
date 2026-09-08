package zone.moddev.mc.ironagefurniture.client.gui;

import java.io.IOException;

import org.lwjgl.input.Keyboard;

import zone.moddev.mc.ironagefurniture.api.network.FoudreLabelMessage;
import zone.moddev.mc.ironagefurniture.api.network.IronAgeFurnitureNetwork;
import zone.moddev.mc.ironagefurniture.api.tile.TileEntityFoudre;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;

public class GuiFoudreLabel extends GuiScreen {
	private final TileEntityFoudre foudre;
	private GuiTextField labelField;

	public GuiFoudreLabel(TileEntityFoudre foudre) {
		this.foudre = foudre;
	}

	@Override
	public void initGui() {
		Keyboard.enableRepeatEvents(true);
		int centerX = this.width / 2;
		int centerY = this.height / 2;
		this.buttonList.clear();
		this.addButton(new GuiButton(0, centerX - 40, centerY + 34, 80, 20,
			I18n.format("gui.done")));
		this.labelField = new GuiTextField(0, this.fontRendererObj, centerX - 80, centerY - 8, 160, 20);
		this.labelField.setMaxStringLength(32);
		this.labelField.setText(this.foudre.getLabel());
		this.labelField.setFocused(true);
		this.labelField.setCanLoseFocus(false);
	}

	@Override
	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	@Override
	public void updateScreen() {
		this.labelField.updateCursorCounter();
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		String title = I18n.format("gui.ironagefurniture.foudre.label");
		this.drawCenteredString(this.fontRendererObj, title, this.width / 2, this.height / 2 - 36, 0xFFFFFF);
		this.labelField.drawTextBox();
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if (keyCode == Keyboard.KEY_RETURN || keyCode == Keyboard.KEY_NUMPADENTER) {
			this.submitLabel();
			return;
		}

		if (keyCode == Keyboard.KEY_ESCAPE) {
			this.closeLabelScreen();
			return;
		}

		this.labelField.textboxKeyTyped(typedChar, keyCode);
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		this.labelField.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 0) {
			this.submitLabel();
		}
	}

	private void submitLabel() {
		IronAgeFurnitureNetwork.channel.sendToServer(new FoudreLabelMessage(this.foudre.getPos(),
			this.labelField.getText()));
		this.closeLabelScreen();
	}

	private void closeLabelScreen() {
		if (this.mc.player != null) {
			this.mc.player.closeScreen();
		} else {
			this.mc.displayGuiScreen(null);
		}
	}
}
