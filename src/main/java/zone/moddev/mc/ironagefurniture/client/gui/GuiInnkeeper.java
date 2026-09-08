package zone.moddev.mc.ironagefurniture.client.gui;

import java.io.IOException;

import zone.moddev.mc.ironagefurniture.api.container.ContainerInnkeeper;
import zone.moddev.mc.ironagefurniture.api.network.InnkeeperActionMessage;
import zone.moddev.mc.ironagefurniture.api.network.IronAgeFurnitureNetwork;
import zone.moddev.mc.ironagefurniture.api.tile.TileEntityHangingInnSign;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;

public class GuiInnkeeper extends GuiContainer {
	private static final int WIDTH = 176;
	private static final int HEIGHT = 224;
	private static final int WOOD_DARK = 0xFF2E170A;
	private static final int WOOD_MID = 0xFF6B3A1B;
	private static final int PANEL = 0xFFD4B789;
	private static final int PANEL_DARK = 0xFFC19761;
	private static final int TEXT = 0xFF2F1C10;
	private final ContainerInnkeeper container;
	private final TileEntityHangingInnSign sign;

	public GuiInnkeeper(TileEntityHangingInnSign sign, InventoryPlayer playerInventory) {
		super(new ContainerInnkeeper(playerInventory, sign));
		this.container = (ContainerInnkeeper)this.inventorySlots;
		this.sign = sign;
		this.xSize = WIDTH;
		this.ySize = HEIGHT;
	}

	@Override
	public void initGui() {
		super.initGui();
		this.buttonList.add(new GuiButton(ContainerInnkeeper.ACTION_SELL, this.guiLeft + 52, this.guiTop + 75, 72, 20, "Sell lot"));
		this.buttonList.add(new GuiButton(ContainerInnkeeper.ACTION_BUY_BOTTLES, this.guiLeft + 8, this.guiTop + 108, 50, 16, "8 Bottles"));
		this.buttonList.add(new GuiButton(ContainerInnkeeper.ACTION_BUY_GLASSES, this.guiLeft + 63, this.guiTop + 108, 50, 16, "4 Glasses"));
		this.buttonList.add(new GuiButton(ContainerInnkeeper.ACTION_BUY_MUGS, this.guiLeft + 118, this.guiTop + 108, 50, 16, "4 Mugs"));
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		IronAgeFurnitureNetwork.channel.sendToServer(new InnkeeperActionMessage(button.id));
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		this.drawCenteredString(this.fontRendererObj, this.sign.getInnName(), this.xSize / 2, 10, TEXT);
		this.drawCenteredString(this.fontRendererObj, "Innkeeper Appraisal", this.xSize / 2, 22, TEXT);
		this.drawCenteredString(this.fontRendererObj, "Daily purse: " + this.container.getPurse(), this.xSize / 2, 32, TEXT);
		int units = this.container.getPreviewUnits();
		String preview = units < 1000 ? "Add a full emerald of value" : "Payout: " + (units / 1000) + " emeralds";
		this.drawCenteredString(this.fontRendererObj, preview, this.xSize / 2, 63, TEXT);
		this.fontRendererObj.drawString("Supplies - 1 emerald", 8, 98, TEXT);
		this.fontRendererObj.drawString("Inventory", 8, 128, TEXT);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		drawRect(this.guiLeft, this.guiTop, this.guiLeft + this.xSize, this.guiTop + this.ySize, WOOD_DARK);
		drawRect(this.guiLeft + 3, this.guiTop + 3, this.guiLeft + this.xSize - 3, this.guiTop + this.ySize - 3, WOOD_MID);
		drawRect(this.guiLeft + 7, this.guiTop + 7, this.guiLeft + this.xSize - 7, this.guiTop + 128, PANEL);
		drawRect(this.guiLeft + 7, this.guiTop + 128, this.guiLeft + this.xSize - 7, this.guiTop + this.ySize - 7, PANEL_DARK);
		for (int i = 0; i < 4; i++) drawSlot(this.guiLeft + 51 + i * 18, this.guiTop + 41);
		for (int row = 0; row < 3; row++) for (int col = 0; col < 9; col++) drawSlot(this.guiLeft + 7 + col * 18, this.guiTop + 137 + row * 18);
		for (int col = 0; col < 9; col++) drawSlot(this.guiLeft + 7 + col * 18, this.guiTop + 197);
	}

	private void drawSlot(int x, int y) {
		drawRect(x, y, x + 18, y + 18, 0xFF3B2111);
		drawRect(x + 1, y + 1, x + 17, y + 17, 0xFF626058);
	}
}
