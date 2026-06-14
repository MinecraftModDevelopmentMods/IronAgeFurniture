package com.mcmoddev.ironagefurniture.client.gui;

import java.io.IOException;

import com.mcmoddev.ironagefurniture.api.DrinkDisplayHelper;
import com.mcmoddev.ironagefurniture.api.container.ContainerPotStill;
import com.mcmoddev.ironagefurniture.api.network.IronAgeFurnitureNetwork;
import com.mcmoddev.ironagefurniture.api.network.PotStillFlushMessage;
import com.mcmoddev.ironagefurniture.api.network.PotStillStartMessage;
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
	private static final int GAUGE_X = 20;
	private static final int GAUGE_Y = 38;
	private static final int GAUGE_WIDTH = 136;
	private static final int GAUGE_HEIGHT = 46;
	private static final int FUEL_SLOT_X = 50;
	private static final int FUEL_SLOT_Y = 113;
	private static final int START_BUTTON_ID = 0;
	private static final int START_BUTTON_X = 78;
	private static final int START_BUTTON_Y = 111;
	private static final int START_BUTTON_WIDTH = 50;
	private static final int START_BUTTON_HEIGHT = 20;
	private static final int FLUSH_BUTTON_ID = 1;
	private static final int FLUSH_BUTTON_X = 130;
	private static final int FLUSH_BUTTON_Y = 111;
	private static final int FLUSH_BUTTON_WIDTH = 36;
	private static final int FLUSH_BUTTON_HEIGHT = 20;
	private static final int TITLE_Y = 12;
	private static final int STATUS_Y = 24;
	private static final int CONTENTS_TEXT_Y = 88;
	private static final int AMOUNT_TEXT_Y = 100;
	private static final int GAUGE_TEXT_X = 17;
	private static final int GAUGE_TEXT_WIDTH = 142;
	private static final int TOP_PANEL_X = 9;
	private static final int TOP_PANEL_Y = 7;
	private static final int TOP_PANEL_WIDTH = 158;
	private static final int TOP_PANEL_HEIGHT = 128;
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
	private static final int TEXT_TRIM_PADDING = 16;
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
		this.startButton = new GuiButton(START_BUTTON_ID, this.guiLeft + START_BUTTON_X,
			this.guiTop + START_BUTTON_Y, START_BUTTON_WIDTH, START_BUTTON_HEIGHT,
			I18n.format("gui.ironagefurniture.pot_still.start"));
		this.buttonList.add(this.startButton);
		this.flushButton = new GuiButton(FLUSH_BUTTON_ID, this.guiLeft + FLUSH_BUTTON_X,
			this.guiTop + FLUSH_BUTTON_Y, FLUSH_BUTTON_WIDTH, FLUSH_BUTTON_HEIGHT,
			I18n.format("gui.ironagefurniture.pot_still.flush"));
		this.buttonList.add(this.flushButton);
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.updateButtonState();
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == START_BUTTON_ID && button.enabled) {
			IronAgeFurnitureNetwork.channel.sendToServer(new PotStillStartMessage(this.potStill.getPos()));
		} else if (button.id == FLUSH_BUTTON_ID && button.enabled) {
			IronAgeFurnitureNetwork.channel.sendToServer(new PotStillFlushMessage(this.potStill.getPos()));
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		this.drawCenteredText(I18n.format(this.potStill.getContainerNameKey()), TITLE_Y);
		this.drawCenteredText(this.getStatusText(), STATUS_Y);
		this.drawBoundedCenteredText(this.getContentsText(), GAUGE_TEXT_X, GAUGE_TEXT_WIDTH, CONTENTS_TEXT_Y);
		this.drawBoundedCenteredText(this.getAmountText(), GAUGE_TEXT_X, GAUGE_TEXT_WIDTH, AMOUNT_TEXT_Y);
		this.fontRendererObj.drawString(this.playerInventory.getDisplayName().getUnformattedText(), INVENTORY_LABEL_X,
			PLAYER_INVENTORY_LABEL_Y, TEXT_DARK);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		this.drawWoodFrame();
		this.drawInsetPanel(TOP_PANEL_X, TOP_PANEL_Y, TOP_PANEL_WIDTH, TOP_PANEL_HEIGHT);
		this.drawInsetPanel(INVENTORY_PANEL_X, INVENTORY_PANEL_Y, INVENTORY_PANEL_WIDTH, INVENTORY_PANEL_HEIGHT);
		this.drawGauge();
		this.drawSlot(FUEL_SLOT_X, FUEL_SLOT_Y);
		this.drawPlayerSlots();
	}

	private void updateButtonState() {
		if (this.startButton != null) {
			this.startButton.enabled = this.potStill.getField(TileEntityPotStill.FIELD_CAN_START) > 0
				&& this.potStill.getField(TileEntityPotStill.FIELD_ACTIVE) == 0;
		}

		if (this.flushButton != null) {
			this.flushButton.enabled = this.potStill.canFlush();
		}
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

	private void drawCenteredText(String text, int y) {
		this.drawBoundedCenteredText(text, TEXT_TRIM_PADDING / 2, this.xSize - TEXT_TRIM_PADDING, y);
	}

	private void drawBoundedCenteredText(String text, int x, int width, int y) {
		String trimmed = this.fontRendererObj.trimStringToWidth(text, width);
		this.fontRendererObj.drawString(trimmed, x + (width - this.fontRendererObj.getStringWidth(trimmed)) / 2, y,
			TEXT_DARK);
	}

	private int getPlayerSlotX(int column) {
		return PLAYER_SLOT_X + column * SLOT_STEP;
	}
}
