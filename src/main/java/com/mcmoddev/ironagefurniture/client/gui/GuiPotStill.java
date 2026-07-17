package com.mcmoddev.ironagefurniture.client.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.mcmoddev.ironagefurniture.api.DrinkDisplayHelper;
import com.mcmoddev.ironagefurniture.api.Enumerations.FluidPortMode;
import com.mcmoddev.ironagefurniture.api.container.ContainerPotStill;
import com.mcmoddev.ironagefurniture.api.network.IronAgeFurnitureNetwork;
import com.mcmoddev.ironagefurniture.api.network.PotStillBarrelTransferMessage;
import com.mcmoddev.ironagefurniture.api.network.PotStillFlushMessage;
import com.mcmoddev.ironagefurniture.api.network.PotStillPortModeMessage;
import com.mcmoddev.ironagefurniture.api.network.PotStillStartMessage;
import com.mcmoddev.ironagefurniture.api.tile.TileEntityBarrel;
import com.mcmoddev.ironagefurniture.api.tile.TileEntityPotStill;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

public class GuiPotStill extends GuiContainer {
	private static final int GUI_WIDTH = 176;
	private static final int GUI_HEIGHT = 238;
	private static final int PLAYER_SLOT_X = 8;
	private static final int PLAYER_INVENTORY_Y = 154;
	private static final int PLAYER_HOTBAR_Y = 212;
	private static final int PLAYER_INVENTORY_LABEL_Y = 141;
	private static final int INVENTORY_LABEL_X = 14;
	private static final int PLAYER_INVENTORY_COLUMNS = 9;
	private static final int PLAYER_INVENTORY_ROWS = 3;
	private static final int GAUGE_X = 53;
	private static final int GAUGE_Y = 38;
	private static final int GAUGE_WIDTH = 70;
	private static final int GAUGE_HEIGHT = 46;
	public static final int RECIPE_CLICK_X = GAUGE_X;
	public static final int RECIPE_CLICK_Y = GAUGE_Y;
	public static final int RECIPE_CLICK_WIDTH = GAUGE_WIDTH;
	public static final int RECIPE_CLICK_HEIGHT = GAUGE_HEIGHT;
	private static final int CHARGE_DIAL_X = 16;
	private static final int CHARGE_DIAL_Y = 43;
	private static final int DISTILL_DIAL_X = 128;
	private static final int DISTILL_DIAL_Y = 43;
	private static final int DIAL_SIZE = 32;
	private static final int FUEL_SLOT_X = 50;
	private static final int FUEL_SLOT_Y = 115;
	private static final int START_BUTTON_ID = 0;
	private static final int START_BUTTON_X = 70;
	private static final int START_BUTTON_Y = 113;
	private static final int START_BUTTON_WIDTH = 50;
	private static final int START_BUTTON_HEIGHT = 20;
	private static final int FLUSH_BUTTON_ID = 1;
	private static final int DRAIN_BARREL_BUTTON_ID = 2;
	private static final int FILL_BARREL_BUTTON_ID = 3;
	private static final int FLUSH_BUTTON_X = 122;
	private static final int FLUSH_BUTTON_Y = 113;
	private static final int FLUSH_BUTTON_WIDTH = 40;
	private static final int FLUSH_BUTTON_HEIGHT = 20;
	private static final int TITLE_Y = 12;
	private static final int STATUS_Y = 24;
	private static final int CONTENTS_TEXT_Y = 89;
	private static final int AMOUNT_TEXT_Y = 102;
	private static final int GAUGE_TEXT_X = 13;
	private static final int GAUGE_TEXT_WIDTH = 150;
	private static final int TOP_PANEL_X = 9;
	private static final int TOP_PANEL_Y = 7;
	private static final int TOP_PANEL_WIDTH = 158;
	private static final int TOP_PANEL_HEIGHT = 128;
	private static final int BARREL_PANEL_GAP = 6;
	private static final int SIDE_PANEL_VERTICAL_GAP = 3;
	private static final int BARREL_PANEL_Y = TOP_PANEL_Y;
	private static final int BARREL_PANEL_WIDTH = AdjacentBarrelPanel.WIDTH;
	private static final int BARREL_PANEL_X = -BARREL_PANEL_WIDTH - BARREL_PANEL_GAP;
	private static final int BARREL_PANEL_HEIGHT = AdjacentBarrelPanel.HEIGHT;
	private static final int DRAIN_BARREL_BUTTON_X = BARREL_PANEL_X + AdjacentBarrelPanel.DRAIN_BUTTON_X;
	private static final int FILL_BARREL_BUTTON_X = BARREL_PANEL_X + AdjacentBarrelPanel.FILL_BUTTON_X;
	private static final int TRANSFER_BUTTON_WIDTH = AdjacentBarrelPanel.BUTTON_WIDTH;
	private static final int TRANSFER_BUTTON_HEIGHT = AdjacentBarrelPanel.BUTTON_HEIGHT;
	private static final int TRANSFER_BUTTON_Y = BARREL_PANEL_Y + AdjacentBarrelPanel.BUTTON_Y;
	private static final int PIPE_PANEL_WIDTH = 84;
	private static final int PIPE_PANEL_HEIGHT = 96;
	private static final int PIPE_PANEL_X = BARREL_PANEL_X + (BARREL_PANEL_WIDTH - PIPE_PANEL_WIDTH) / 2;
	private static final int PIPE_PANEL_Y = BARREL_PANEL_Y + BARREL_PANEL_HEIGHT + SIDE_PANEL_VERTICAL_GAP;
	private static final int INVENTORY_PANEL_X = 7;
	private static final int INVENTORY_PANEL_Y = 138;
	private static final int INVENTORY_PANEL_WIDTH = 162;
	private static final int INVENTORY_PANEL_HEIGHT = 93;
	private static final int FRAME_OUTER_INSET = 3;
	private static final int FRAME_INNER_INSET = 6;
	private static final int INSET_BORDER = 2;
	private static final int INSET_FILL = 4;
	private static final int SLOT_SIZE = 16;
	private static final int SLOT_STEP = 18;
	private static final int SLOT_BORDER = 1;
	private static final int SLOT_INNER_INSET = 2;
	private static final int GAUGE_BORDER = 2;
	private static final int PRODUCT_LAYER_SIDE_INSET = 2;
	private static final int MIN_DRAWN_FILL = 1;
	private static final int TEXTURE_TILE_SIZE = 16;
	private static final int COLOR_CHANNEL_MAX = 255;
	private static final int FULL_ALPHA_MASK = 0xFF000000;
	private static final float FULL_COLOR = 1.0F;
	private static final int WOOD_DARK = 0xFF2E170A;
	private static final int WOOD_MID = 0xFF6B3A1B;
	private static final int WOOD_LIGHT = 0xFF9A6230;
	private static final int PANEL_DARK = 0xFF5A3219;
	private static final int PANEL_LIGHT = 0xFFD4B789;
	private static final int PANEL_MID = 0xFFC19761;
	private static final int TEXT_DARK = 0xFF2F1C10;
	private static final int SLOT_BORDER_COLOR = 0xFF3B2111;
	private static final int SLOT_RIM_COLOR = 0xFF8A6743;
	private static final int SLOT_SHADOW_COLOR = 0xFF4D4A43;
	private static final int SLOT_FILL_COLOR = 0xFF626058;
	private static final int BRASS_LIGHT = 0xFFE2B85D;
	private static final int GAUGE_BORDER_COLOR = 0xFF404040;
	private static final int GAUGE_EMPTY_COLOR = 0xFF1F2633;
	private static final int HIGHLIGHT_COLOR = 0x80FFFFFF;
	private static final int ACTIVE_WASTE_OVERLAY_COLOR = 0x50000000;
	private static final int SEPARATION_LINE_COLOR = 0xCCF0D08A;
	private static final int TINTED_FLUID_RIPPLE_COLOR = 0x28FFFFFF;
	private static final int TINTED_FLUID_EDGE_SHADOW = 0x30000000;
	private static final int TINTED_FLUID_RIPPLE_TOP_INSET = 3;
	private static final int TINTED_FLUID_RIPPLE_STEP = 8;

	private final TileEntityPotStill potStill;
	private final InventoryPlayer playerInventory;
	private GuiButton startButton;
	private GuiButton flushButton;
	private GuiButton drainBarrelButton;
	private GuiButton fillBarrelButton;
	private final MechanicalDial chargeDial = new MechanicalDial();
	private final MechanicalDial distillDial = new MechanicalDial();
	private final MechanicalValveControl valveControl = new MechanicalValveControl();
	private boolean confirmFlush;

	public GuiPotStill(TileEntityPotStill potStill, InventoryPlayer playerInventory) {
		super(new ContainerPotStill(playerInventory, potStill));
		this.potStill = potStill;
		this.playerInventory = playerInventory;
		this.xSize = GUI_WIDTH;
		this.ySize = GUI_HEIGHT;
	}

	@Override
	public void initGui() {
		super.initGui();
		int barrelPanelShift = this.getBarrelPanelShiftX();
		this.startButton = new GuiButton(START_BUTTON_ID, this.guiLeft + START_BUTTON_X,
			this.guiTop + START_BUTTON_Y, START_BUTTON_WIDTH, START_BUTTON_HEIGHT,
			I18n.format("gui.ironagefurniture.pot_still.start"));
		this.buttonList.add(this.startButton);
		this.flushButton = new GuiButton(FLUSH_BUTTON_ID, this.guiLeft + FLUSH_BUTTON_X,
			this.guiTop + FLUSH_BUTTON_Y, FLUSH_BUTTON_WIDTH, FLUSH_BUTTON_HEIGHT, this.getFlushButtonText());
		this.buttonList.add(this.flushButton);
		this.drainBarrelButton = new GuiButton(DRAIN_BARREL_BUTTON_ID, this.guiLeft + DRAIN_BARREL_BUTTON_X
			+ barrelPanelShift,
			this.guiTop + TRANSFER_BUTTON_Y, TRANSFER_BUTTON_WIDTH, TRANSFER_BUTTON_HEIGHT,
			I18n.format("gui.ironagefurniture.transfer.drain"));
		this.buttonList.add(this.drainBarrelButton);
		this.fillBarrelButton = new GuiButton(FILL_BARREL_BUTTON_ID, this.guiLeft + FILL_BARREL_BUTTON_X
			+ barrelPanelShift,
			this.guiTop + TRANSFER_BUTTON_Y, TRANSFER_BUTTON_WIDTH, TRANSFER_BUTTON_HEIGHT,
			I18n.format("gui.ironagefurniture.transfer.fill"));
		this.buttonList.add(this.fillBarrelButton);
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.updateButtonPositions();
		this.updateButtonState();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.drawMechanicalTooltips(mouseX, mouseY);
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == START_BUTTON_ID && button.enabled) {
			this.confirmFlush = false;
			IronAgeFurnitureNetwork.channel.sendToServer(new PotStillStartMessage(this.potStill.getPos()));
		} else if (button.id == FLUSH_BUTTON_ID && button.enabled) {
			if (!this.confirmFlush) {
				this.confirmFlush = true;
				return;
			}

			this.confirmFlush = false;
			IronAgeFurnitureNetwork.channel.sendToServer(new PotStillFlushMessage(this.potStill.getPos()));
		} else if (button.id == DRAIN_BARREL_BUTTON_ID && button.enabled) {
			this.confirmFlush = false;
			IronAgeFurnitureNetwork.channel.sendToServer(new PotStillBarrelTransferMessage(this.potStill.getPos(),
				false));
		} else if (button.id == FILL_BARREL_BUTTON_ID && button.enabled) {
			this.confirmFlush = false;
			IronAgeFurnitureNetwork.channel.sendToServer(new PotStillBarrelTransferMessage(this.potStill.getPos(),
				true));
		}
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		if (mouseButton == 0 && this.shouldDrawPipePanel()) {
			this.positionValveControl();
			FluidPortMode mode = this.valveControl.getClickedMode(mouseX, mouseY,
				this.potStill.hasConnectedInlet(), this.potStill.hasConnectedOutlet(), this.potStill.isDistilling());

			if (mode != null) {
				this.confirmFlush = false;
				IronAgeFurnitureNetwork.channel.sendToServer(new PotStillPortModeMessage(this.potStill.getPos(), mode));
				return;
			}
		}

		super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		this.drawFittedCenteredText(I18n.format(this.potStill.getContainerNameKey()), GAUGE_TEXT_X,
			GAUGE_TEXT_WIDTH, TITLE_Y);
		this.drawFittedCenteredText(this.getStatusText(), GAUGE_TEXT_X, GAUGE_TEXT_WIDTH, STATUS_Y);
		this.drawFittedCenteredText(this.getContentsText(), GAUGE_TEXT_X, GAUGE_TEXT_WIDTH, CONTENTS_TEXT_Y);
		this.drawFittedCenteredText(this.getAmountText(), GAUGE_TEXT_X, GAUGE_TEXT_WIDTH, AMOUNT_TEXT_Y);
		this.fontRendererObj.drawString(this.playerInventory.getDisplayName().getUnformattedText(), INVENTORY_LABEL_X,
			PLAYER_INVENTORY_LABEL_Y, TEXT_DARK);
		this.drawBarrelTransferPanelForeground();
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		this.drawWoodFrame();
		this.drawInsetPanel(TOP_PANEL_X, TOP_PANEL_Y, TOP_PANEL_WIDTH, TOP_PANEL_HEIGHT);
		this.drawInsetPanel(INVENTORY_PANEL_X, INVENTORY_PANEL_Y, INVENTORY_PANEL_WIDTH, INVENTORY_PANEL_HEIGHT);
		this.drawPipePanel();
		this.drawBarrelTransferPanel();
		this.drawGauge();
		this.drawMechanicalDials();
		this.drawSlot(FUEL_SLOT_X, FUEL_SLOT_Y);
		this.drawPlayerSlots();
	}

	private void updateButtonState() {
		if (this.startButton != null) {
			this.startButton.enabled = this.potStill.getField(TileEntityPotStill.FIELD_CAN_START) > 0
				&& this.potStill.getField(TileEntityPotStill.FIELD_ACTIVE) == 0;
		}

		if (this.flushButton != null) {
			if (!this.potStill.canFlush()) {
				this.confirmFlush = false;
			}

			this.flushButton.displayString = this.getFlushButtonText();
			this.flushButton.enabled = this.potStill.canFlush();
		}

		if (this.drainBarrelButton != null) {
			this.drainBarrelButton.enabled = this.potStill.canDrainAdjacentBarrel();
		}

		if (this.fillBarrelButton != null) {
			this.fillBarrelButton.enabled = this.potStill.canFillAdjacentBarrel();
		}

	}

	private void updateButtonPositions() {
		int barrelPanelShift = this.getBarrelPanelShiftX();
		this.startButton.xPosition = this.guiLeft + START_BUTTON_X;
		this.startButton.yPosition = this.guiTop + START_BUTTON_Y;
		this.flushButton.xPosition = this.guiLeft + FLUSH_BUTTON_X;
		this.flushButton.yPosition = this.guiTop + FLUSH_BUTTON_Y;

		if (this.drainBarrelButton != null) {
			this.drainBarrelButton.xPosition = this.guiLeft + DRAIN_BARREL_BUTTON_X + barrelPanelShift;
			this.drainBarrelButton.yPosition = this.guiTop + TRANSFER_BUTTON_Y;
		}

		if (this.fillBarrelButton != null) {
			this.fillBarrelButton.xPosition = this.guiLeft + FILL_BARREL_BUTTON_X + barrelPanelShift;
			this.fillBarrelButton.yPosition = this.guiTop + TRANSFER_BUTTON_Y;
		}
	}

	private String getFlushButtonText() {
		return I18n.format(this.confirmFlush ? "gui.ironagefurniture.flush.confirm"
			: "gui.ironagefurniture.pot_still.flush");
	}

	private void drawBarrelTransferPanelForeground() {
		int barrelPanelShift = this.getBarrelPanelShiftX();
		AdjacentBarrelPanel.drawForeground(this.fontRendererObj, BARREL_PANEL_X + barrelPanelShift,
			BARREL_PANEL_Y, this.potStill.getAdjacentTransferBarrel());
	}

	private void drawWoodFrame() {
		this.drawRect(this.guiLeft, this.guiTop, this.guiLeft + this.xSize, this.guiTop + this.ySize, WOOD_DARK);
		this.drawRect(this.guiLeft + FRAME_OUTER_INSET, this.guiTop + FRAME_OUTER_INSET,
			this.guiLeft + this.xSize - FRAME_OUTER_INSET, this.guiTop + this.ySize - FRAME_OUTER_INSET, WOOD_MID);
		this.drawRect(this.guiLeft + FRAME_INNER_INSET, this.guiTop + FRAME_INNER_INSET,
			this.guiLeft + this.xSize - FRAME_INNER_INSET, this.guiTop + this.ySize - FRAME_INNER_INSET,
			WOOD_LIGHT);
	}

	private void drawInsetPanel(int x, int y, int width, int height) {
		int left = this.guiLeft + x;
		int top = this.guiTop + y;
		this.drawRect(left, top, left + width, top + height, PANEL_DARK);
		this.drawRect(left + INSET_BORDER, top + INSET_BORDER, left + width - INSET_BORDER,
			top + height - INSET_BORDER, PANEL_MID);
		this.drawRect(left + INSET_FILL, top + INSET_FILL, left + width - INSET_FILL,
			top + height - INSET_FILL, PANEL_LIGHT);
	}

	private void drawBarrelTransferPanel() {
		AdjacentBarrelPanel.drawBackground(this.guiLeft + this.getBarrelPanelOffsetX(),
			this.guiTop + BARREL_PANEL_Y);
		this.drawAdjacentBarrelGauge();
	}

	private void drawPipePanel() {
		if (!this.shouldDrawPipePanel()) {
			return;
		}

		this.drawOuterPanel(this.getPipePanelOffsetX(), PIPE_PANEL_Y, PIPE_PANEL_WIDTH, PIPE_PANEL_HEIGHT);
		this.positionValveControl();
		this.valveControl.draw(this.fontRendererObj, this.potStill.getPortMode(),
			this.potStill.hasConnectedInlet(), this.potStill.hasConnectedOutlet(), this.potStill.isDistilling());
	}

	private void positionValveControl() {
		this.valveControl.setPosition(this.guiLeft + this.getPipePanelOffsetX(), this.guiTop + PIPE_PANEL_Y);
	}

	private void drawOuterPanel(int x, int y, int width, int height) {
		int left = this.guiLeft + x;
		int top = this.guiTop + y;
		this.drawRect(left, top, left + width, top + height, WOOD_DARK);
		this.drawRect(left + FRAME_OUTER_INSET, top + FRAME_OUTER_INSET,
			left + width - FRAME_OUTER_INSET, top + height - FRAME_OUTER_INSET, WOOD_MID);
		this.drawRect(left + FRAME_INNER_INSET, top + FRAME_INNER_INSET,
			left + width - FRAME_INNER_INSET, top + height - FRAME_INNER_INSET, PANEL_LIGHT);
	}

	private void drawAdjacentBarrelGauge() {
		TileEntityBarrel barrel = this.potStill.getAdjacentTransferBarrel();
		FluidStack fluid = barrel == null ? null : barrel.getFluid();
		int fillWidth = AdjacentBarrelPanel.getGaugeFillWidth(barrel);

		if (fluid == null || fluid.amount <= 0 || fluid.getFluid() == null || fillWidth <= 0) {
			return;
		}

		int left = this.guiLeft + this.getBarrelPanelOffsetX() + AdjacentBarrelPanel.GAUGE_X;
		int top = this.guiTop + BARREL_PANEL_Y + AdjacentBarrelPanel.GAUGE_Y;
		this.drawFluid(left, top, fillWidth, AdjacentBarrelPanel.GAUGE_HEIGHT, fluid);
		this.drawRect(left, top, left + fillWidth, top + MIN_DRAWN_FILL,
			HIGHLIGHT_COLOR);
	}

	private void drawPlayerSlots() {
		for (int row = 0; row < PLAYER_INVENTORY_ROWS; row++) {
			for (int column = 0; column < PLAYER_INVENTORY_COLUMNS; column++) {
				this.drawSlot(this.getPlayerSlotX(column), PLAYER_INVENTORY_Y + row * SLOT_STEP);
			}
		}

		for (int column = 0; column < PLAYER_INVENTORY_COLUMNS; column++) {
			this.drawSlot(this.getPlayerSlotX(column), PLAYER_HOTBAR_Y);
		}
	}

	private void drawSlot(int x, int y) {
		int left = this.guiLeft + x;
		int top = this.guiTop + y;
		this.drawRect(left - SLOT_BORDER, top - SLOT_BORDER, left + SLOT_SIZE + SLOT_BORDER,
			top + SLOT_SIZE + SLOT_BORDER, SLOT_BORDER_COLOR);
		this.drawRect(left, top, left + SLOT_SIZE, top + SLOT_SIZE, SLOT_RIM_COLOR);
		this.drawRect(left + SLOT_BORDER, top + SLOT_BORDER, left + SLOT_SIZE, top + SLOT_SIZE, SLOT_SHADOW_COLOR);
		this.drawRect(left + SLOT_INNER_INSET, top + SLOT_INNER_INSET, left + SLOT_SIZE - SLOT_BORDER,
			top + SLOT_SIZE - SLOT_BORDER, SLOT_FILL_COLOR);
	}

	private void drawGauge() {
		int left = this.guiLeft + GAUGE_X;
		int top = this.guiTop + GAUGE_Y;
		this.drawRect(left - GAUGE_BORDER, top - GAUGE_BORDER, left + GAUGE_WIDTH + GAUGE_BORDER,
			top + GAUGE_HEIGHT + GAUGE_BORDER, GAUGE_BORDER_COLOR);
		this.drawRect(left, top, left + GAUGE_WIDTH, top + GAUGE_HEIGHT, GAUGE_EMPTY_COLOR);

		FluidStack baseFluid = this.getGaugeBaseFluid();
		int baseAmount = this.getGaugeBaseAmount();

		if (baseFluid != null && baseFluid.getFluid() != null && baseAmount > 0) {
			int fillHeight = this.getGaugeFillHeight(baseAmount);
			int fillTop = top + GAUGE_HEIGHT - fillHeight;
			this.drawFluid(left, fillTop, GAUGE_WIDTH, fillHeight, baseFluid);
			this.drawRect(left, fillTop, left + GAUGE_WIDTH, fillTop + MIN_DRAWN_FILL, HIGHLIGHT_COLOR);

			if (this.potStill.isDistilling()) {
				this.drawRect(left, fillTop, left + GAUGE_WIDTH, top + GAUGE_HEIGHT, ACTIVE_WASTE_OVERLAY_COLOR);
			}
		}

		FluidStack productFluid = this.getGaugeProductFluid();
		int productAmount = this.getGaugeProductAmount();

		if (productFluid == null || productFluid.getFluid() == null || productAmount <= 0) {
			return;
		}

		int productHeight = this.getGaugeFillHeight(productAmount);
		int productTop = top + GAUGE_HEIGHT - productHeight;
		int productLeft = left + PRODUCT_LAYER_SIDE_INSET;
		int productWidth = GAUGE_WIDTH - PRODUCT_LAYER_SIDE_INSET * 2;
		this.drawFluid(productLeft, productTop, productWidth, productHeight, productFluid);
		this.drawRect(productLeft, productTop, productLeft + productWidth, productTop + MIN_DRAWN_FILL,
			SEPARATION_LINE_COLOR);
	}

	private void drawMechanicalDials() {
		FluidStack chargeFluid = this.getChargeFluid();
		int chargeAmount = this.getChargeAmount();
		int chargeCapacity = this.getChargeCapacity();
		float charge = chargeCapacity <= 0 ? 0.0F : (float)chargeAmount / (float)chargeCapacity;
		int chargeColor = chargeFluid == null || chargeFluid.getFluid() == null ? GAUGE_EMPTY_COLOR
			: this.getFluidColor(chargeFluid);
		int distillTotal = this.potStill.getDistillTimeTotal();
		float distill = distillTotal <= 0 ? 0.0F
			: (float)this.potStill.getDistillTime() / (float)distillTotal;

		this.chargeDial.draw(this.guiLeft + CHARGE_DIAL_X, this.guiTop + CHARGE_DIAL_Y, DIAL_SIZE, charge,
			chargeColor, chargeAmount > 0);
		this.distillDial.draw(this.guiLeft + DISTILL_DIAL_X, this.guiTop + DISTILL_DIAL_Y, DIAL_SIZE, distill,
			BRASS_LIGHT, this.potStill.isDistilling());
	}

	private void drawMechanicalTooltips(int mouseX, int mouseY) {
		List<String> tooltip = new ArrayList<String>();

		if (this.isPointWithin(mouseX, mouseY, CHARGE_DIAL_X, CHARGE_DIAL_Y, DIAL_SIZE, DIAL_SIZE)) {
			FluidStack fluid = this.getChargeFluid();
			tooltip.add(I18n.format("gui.ironagefurniture.dial.charge"));
			tooltip.add(fluid == null ? I18n.format("gui.ironagefurniture.barrel.empty")
				: DrinkDisplayHelper.getDisplayName(fluid));
			tooltip.add(this.getChargeAmount() + " / " + this.getChargeCapacity() + " mB");
		} else if (this.isPointWithin(mouseX, mouseY, DISTILL_DIAL_X, DISTILL_DIAL_Y, DIAL_SIZE, DIAL_SIZE)) {
			tooltip.add(I18n.format("gui.ironagefurniture.dial.distill"));
			tooltip.add(this.potStill.isDistilling() ? this.getStatusText()
				: I18n.format("gui.ironagefurniture.dial.idle"));
		} else if (this.shouldDrawPipePanel()) {
			this.positionValveControl();
			String valveTooltip = this.valveControl.getHoverText(mouseX, mouseY,
				this.potStill.hasConnectedInlet(), this.potStill.hasConnectedOutlet(), this.potStill.isDistilling());

			if (valveTooltip != null) {
				tooltip.add(valveTooltip);
			}
		}

		if (!tooltip.isEmpty()) {
			this.drawHoveringText(tooltip, mouseX, mouseY);
		}
	}

	private boolean isPointWithin(int mouseX, int mouseY, int x, int y, int width, int height) {
		int left = this.guiLeft + x;
		int top = this.guiTop + y;
		return mouseX >= left && mouseX < left + width && mouseY >= top && mouseY < top + height;
	}

	private void drawFluid(int left, int top, int width, int height, FluidStack fluid) {
		if (DrinkDisplayHelper.shouldDrawTinted(fluid)) {
			this.drawTintedFluid(left, top, width, height, fluid);
			return;
		}

		ResourceLocation still = fluid.getFluid().getStill(fluid);

		if (still == null) {
			this.drawRect(left, top, left + width, top + height, this.getFluidColor(fluid));
			return;
		}

		TextureAtlasSprite sprite = this.mc.getTextureMapBlocks().getAtlasSprite(still.toString());
		int color = this.getFluidColor(fluid);
		float red = (float)(color >> 16 & COLOR_CHANNEL_MAX) / (float)COLOR_CHANNEL_MAX;
		float green = (float)(color >> 8 & COLOR_CHANNEL_MAX) / (float)COLOR_CHANNEL_MAX;
		float blue = (float)(color & COLOR_CHANNEL_MAX) / (float)COLOR_CHANNEL_MAX;

		this.mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		GlStateManager.color(red, green, blue, FULL_COLOR);

		for (int drawn = 0; drawn < height; drawn += TEXTURE_TILE_SIZE) {
			int segmentHeight = Math.min(TEXTURE_TILE_SIZE, height - drawn);
			int segmentTop = top + height - drawn - segmentHeight;
			this.drawTexturedModalRect(left, segmentTop, sprite, width, segmentHeight);
		}

		GlStateManager.color(FULL_COLOR, FULL_COLOR, FULL_COLOR, FULL_COLOR);
	}

	private void drawTintedFluid(int left, int top, int width, int height, FluidStack fluid) {
		this.drawRect(left, top, left + width, top + height, this.getFluidColor(fluid));
		this.drawRect(left, top, left + MIN_DRAWN_FILL, top + height, TINTED_FLUID_EDGE_SHADOW);
		this.drawRect(left + width - MIN_DRAWN_FILL, top, left + width, top + height,
			TINTED_FLUID_EDGE_SHADOW);

		for (int y = top + TINTED_FLUID_RIPPLE_TOP_INSET; y < top + height; y += TINTED_FLUID_RIPPLE_STEP) {
			this.drawRect(left + MIN_DRAWN_FILL, y, left + width - MIN_DRAWN_FILL,
				Math.min(y + MIN_DRAWN_FILL, top + height), TINTED_FLUID_RIPPLE_COLOR);
		}
	}

	private String getStatusText() {
		if (this.potStill.isDistilling()) {
			int percent = this.potStill.getDistillTimeTotal() <= 0 ? 0
				: this.potStill.getDistillTime() * 100 / this.potStill.getDistillTimeTotal();
			return I18n.format("gui.ironagefurniture.pot_still.distilling_short", Integer.valueOf(percent));
		}

		return I18n.format("gui.ironagefurniture.pot_still.idle");
	}

	private String getContentsText() {
		FluidStack fluid = this.getGaugeProductFluid();

		if (fluid == null || fluid.getFluid() == null) {
			fluid = this.getGaugeBaseFluid();
		}

		return fluid == null || fluid.getFluid() == null ? I18n.format("gui.ironagefurniture.barrel.empty")
			: DrinkDisplayHelper.getDisplayName(fluid);
	}

	private String getAmountText() {
		if (this.potStill.isDistilling()) {
			return this.getGaugeProductAmount() + " / " + this.potStill.getBatchOutputAmount() + " mB";
		}

		int amount = this.potStill.getOutputAmount() > 0 ? this.potStill.getOutputAmount()
			: this.potStill.getInputAmount();
		return amount + " mB";
	}

	private FluidStack getChargeFluid() {
		if (this.potStill.isDistilling()) {
			return this.potStill.getBatchInputFluid();
		}
		if (this.potStill.getOutputAmount() > 0) {
			return this.potStill.getOutputFluid();
		}

		return this.potStill.getInputFluid();
	}

	private int getChargeAmount() {
		if (this.potStill.isDistilling()) {
			return this.potStill.getBatchInputAmount();
		}
		return this.potStill.getOutputAmount() > 0 ? this.potStill.getOutputAmount()
			: this.potStill.getInputAmount();
	}

	private int getChargeCapacity() {
		// The interface represents one vessel; the smaller output tank is an internal implementation detail.
		return TileEntityPotStill.INPUT_CAPACITY;
	}

	private FluidStack getGaugeBaseFluid() {
		return this.potStill.isDistilling() ? this.potStill.getBatchInputFluid() : this.potStill.getInputFluid();
	}

	private int getGaugeBaseAmount() {
		return this.potStill.isDistilling() ? this.potStill.getBatchInputAmount() : this.potStill.getInputAmount();
	}

	private FluidStack getGaugeProductFluid() {
		return this.potStill.isDistilling() ? this.potStill.getBatchOutputFluid() : this.potStill.getOutputFluid();
	}

	private int getGaugeProductAmount() {
		if (!this.potStill.isDistilling()) {
			return this.potStill.getOutputAmount();
		}

		int total = this.potStill.getDistillTimeTotal();

		if (total <= 0) {
			return 0;
		}

		return this.potStill.getBatchOutputAmount() * this.potStill.getDistillTime() / total;
	}

	private int getGaugeFillHeight(int amount) {
		return Math.min(GAUGE_HEIGHT, Math.max(MIN_DRAWN_FILL,
			amount * GAUGE_HEIGHT / TileEntityPotStill.INPUT_CAPACITY));
	}

	private int getFluidColor(FluidStack fluid) {
		int color = fluid.getFluid().getColor(fluid);
		return (color & FULL_ALPHA_MASK) == 0 ? color | FULL_ALPHA_MASK : color;
	}

	private void drawBoundedCenteredText(String text, int x, int width, int y) {
		String trimmed = this.fontRendererObj.trimStringToWidth(text, width);
		this.fontRendererObj.drawString(trimmed, x + (width - this.fontRendererObj.getStringWidth(trimmed)) / 2, y,
			TEXT_DARK);
	}

	private void drawFittedCenteredText(String text, int x, int width, int y) {
		int textWidth = this.fontRendererObj.getStringWidth(text);

		if (textWidth <= width) {
			this.fontRendererObj.drawString(text, x + (width - textWidth) / 2, y, TEXT_DARK);
			return;
		}

		float scale = (float)width / (float)textWidth;
		GlStateManager.pushMatrix();
		GlStateManager.translate(x + width / 2.0F, y, 0.0F);
		GlStateManager.scale(scale, scale, 1.0F);
		this.fontRendererObj.drawString(text, -textWidth / 2, 0, TEXT_DARK);
		GlStateManager.popMatrix();
	}

	private int getPlayerSlotX(int column) {
		return PLAYER_SLOT_X + column * SLOT_STEP;
	}

	private int getBarrelPanelOffsetX() {
		int leftOffset = -BARREL_PANEL_WIDTH - BARREL_PANEL_GAP;

		if (this.guiLeft + leftOffset >= 0) {
			return leftOffset;
		}

		int rightOffset = GUI_WIDTH + BARREL_PANEL_GAP;
		int preferredScreenX = this.guiLeft + rightOffset;

		if (preferredScreenX + BARREL_PANEL_WIDTH <= this.width) {
			return rightOffset;
		}

		int clampedScreenX = Math.max(0, Math.min(preferredScreenX, this.width - BARREL_PANEL_WIDTH));
		return clampedScreenX - this.guiLeft;
	}

	private int getBarrelPanelShiftX() {
		return this.getBarrelPanelOffsetX() - BARREL_PANEL_X;
	}

	private boolean shouldDrawPipePanel() {
		return this.potStill.hasConnectedPipework();
	}

	private int getPipePanelOffsetX() {
		return this.getBarrelPanelOffsetX() + (BARREL_PANEL_WIDTH - PIPE_PANEL_WIDTH) / 2;
	}
}
