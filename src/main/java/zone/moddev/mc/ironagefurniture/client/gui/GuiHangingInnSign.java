package zone.moddev.mc.ironagefurniture.client.gui;

import java.io.IOException;

import zone.moddev.mc.ironagefurniture.api.tile.TileEntityHangingInnSign;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.resources.I18n;
import net.minecraft.network.play.client.CPacketUpdateSign;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiHangingInnSign extends GuiScreen {
	private static final int DONE_BUTTON_ID = 0;
	private static final int LINE_COUNT = 4;
	private static final int MAX_LINE_LENGTH = 32;
	private static final int FIELD_HEIGHT = 20;
	private static final int FIELD_SPACING = 4;
	private static final int MAX_EDITOR_WIDTH = 220;
	private static final int TEXT_COLOR = 0xE6C98F;

	private final TileEntityHangingInnSign sign;
	private final GuiTextField[] lineFields = new GuiTextField[LINE_COUNT];
	private GuiButton doneButton;
	private int selectedLine;
	private int editorLeft;
	private int editorTop;
	private int editorWidth;

	public GuiHangingInnSign(TileEntityHangingInnSign sign) {
		this.sign = sign;
	}

	@Override
	public void initGui() {
		this.buttonList.clear();
		Keyboard.enableRepeatEvents(true);
		this.sign.setEditable(false);
		this.editorWidth = Math.min(MAX_EDITOR_WIDTH, this.width - 48);
		this.editorLeft = (this.width - this.editorWidth) / 2;
		this.editorTop = Math.max(52, this.height / 4);

		for (int line = 0; line < LINE_COUNT; line++) {
			GuiTextField field = new GuiTextField(line, this.fontRendererObj, this.editorLeft,
				this.editorTop + line * (FIELD_HEIGHT + FIELD_SPACING), this.editorWidth, FIELD_HEIGHT);
			field.setMaxStringLength(MAX_LINE_LENGTH);
			field.setText(this.sign.signText[line] == null
				? "" : this.sign.signText[line].getUnformattedText());
			field.setTextColor(TEXT_COLOR);
			this.lineFields[line] = field;
		}

		this.doneButton = this.addButton(new GuiButton(DONE_BUTTON_ID, this.width / 2 - 100,
			this.editorTop + LINE_COUNT * (FIELD_HEIGHT + FIELD_SPACING) + 8,
			I18n.format("gui.done", new Object[0])));
		this.focusLine(0);
	}

	@Override
	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
		this.syncSignText();
		NetHandlerPlayClient connection = this.mc.getConnection();

		if (connection != null) {
			connection.sendPacket(new CPacketUpdateSign(this.sign.getPos(), this.sign.signText));
		}

		this.sign.setEditable(true);
	}

	@Override
	public void updateScreen() {
		for (GuiTextField field : this.lineFields) {
			if (field != null) {
				field.updateCursorCounter();
			}
		}
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.enabled && button.id == DONE_BUTTON_ID) {
			this.syncSignText();
			this.sign.markDirty();
			this.mc.displayGuiScreen(null);
		}
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if (keyCode == Keyboard.KEY_ESCAPE) {
			this.actionPerformed(this.doneButton);
			return;
		}

		if (keyCode == Keyboard.KEY_UP) {
			this.focusLine(this.selectedLine - 1);
			return;
		}

		if (keyCode == Keyboard.KEY_DOWN || keyCode == Keyboard.KEY_RETURN
				|| keyCode == Keyboard.KEY_NUMPADENTER || keyCode == Keyboard.KEY_TAB) {
			int direction = keyCode == Keyboard.KEY_TAB && isShiftKeyDown() ? -1 : 1;
			this.focusLine(this.selectedLine + direction);
			return;
		}

		if (this.lineFields[this.selectedLine].textboxKeyTyped(typedChar, keyCode)) {
			this.syncSignText();
		}
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);

		if (this.mc.currentScreen != this) {
			return;
		}

		int clickedLine = -1;

		for (int line = 0; line < LINE_COUNT; line++) {
			this.lineFields[line].mouseClicked(mouseX, mouseY, mouseButton);

			if (this.lineFields[line].isFocused()) {
				clickedLine = line;
			}
		}

		this.focusLine(clickedLine >= 0 ? clickedLine : this.selectedLine);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		int fieldsBottom = this.editorTop + LINE_COUNT * (FIELD_HEIGHT + FIELD_SPACING) - FIELD_SPACING;
		drawRect(this.editorLeft - 24, this.editorTop - 10, this.editorLeft + this.editorWidth + 8,
			fieldsBottom + 10, 0xA0000000);
		this.drawCenteredString(this.fontRendererObj, I18n.format("sign.edit", new Object[0]),
			this.width / 2, Math.max(20, this.editorTop - 28), 0xFFFFFF);

		for (int line = 0; line < LINE_COUNT; line++) {
			this.drawString(this.fontRendererObj, Integer.toString(line + 1), this.editorLeft - 16,
				this.editorTop + line * (FIELD_HEIGHT + FIELD_SPACING) + 6, 0xA0A0A0);
			this.lineFields[line].drawTextBox();
		}

		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	private void focusLine(int line) {
		this.selectedLine = (line + LINE_COUNT) % LINE_COUNT;

		for (int current = 0; current < LINE_COUNT; current++) {
			this.lineFields[current].setFocused(current == this.selectedLine);
		}
	}

	private void syncSignText() {
		for (int line = 0; line < LINE_COUNT; line++) {
			if (this.lineFields[line] != null) {
				this.sign.signText[line] = new TextComponentString(this.lineFields[line].getText());
			}
		}
	}
}
