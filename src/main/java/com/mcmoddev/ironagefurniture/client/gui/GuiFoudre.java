package com.mcmoddev.ironagefurniture.client.gui;

import java.awt.Rectangle;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.mcmoddev.ironagefurniture.api.container.ContainerFoudre;
import com.mcmoddev.ironagefurniture.api.DrinkDisplayHelper;
import com.mcmoddev.ironagefurniture.api.Enumerations.FluidPortMode;
import com.mcmoddev.ironagefurniture.api.network.FoudreBarrelTransferMessage;
import com.mcmoddev.ironagefurniture.api.network.FoudreFlushMessage;
import com.mcmoddev.ironagefurniture.api.network.FoudrePortModeMessage;
import com.mcmoddev.ironagefurniture.api.network.FoudreSealMessage;
import com.mcmoddev.ironagefurniture.api.network.IronAgeFurnitureNetwork;
import com.mcmoddev.ironagefurniture.api.tile.TileEntityBarrel;
import com.mcmoddev.ironagefurniture.api.tile.TileEntityFoudre;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Loader;

public class GuiFoudre extends GuiContainer {
	public static final int RECIPE_CLICK_X = 124;
	public static final int RECIPE_CLICK_Y = 36;
	public static final int RECIPE_CLICK_WIDTH = 32;
	public static final int RECIPE_CLICK_HEIGHT = 32;
	private static final int GUI_WIDTH = 176;
	private static final int GUI_HEIGHT = 218;
	private static final int TEXT_TRIM_PADDING = 24;
	private static final int PLAYER_SLOT_X = 8;
	private static final int INVENTORY_LABEL_X = 14;
	private static final int PLAYER_INVENTORY_LABEL_Y = 124;
	private static final int PLAYER_INVENTORY_Y = 134;
	private static final int PLAYER_HOTBAR_Y = 192;
	private static final int PLAYER_INVENTORY_COLUMNS = 9;
	private static final int PLAYER_INVENTORY_ROWS = 3;
	private static final int FILL_DIAL_X = 17;
	private static final int FILL_DIAL_Y = 36;
	private static final int PROCESS_DIAL_X = RECIPE_CLICK_X;
	private static final int PROCESS_DIAL_Y = RECIPE_CLICK_Y;
	private static final int DIAL_SIZE = 32;
	private static final int FLUID_GAUGE_X = 55;
	private static final int FLUID_GAUGE_Y = 40;
	private static final int FLUID_GAUGE_WIDTH = 10;
	private static final int FLUID_GAUGE_HEIGHT = 36;
	private static final int FLUID_GAUGE_BORDER = 2;
	private static final int INGREDIENT_X = 78;
	private static final int INGREDIENT_Y = 42;
	private static final int SEAL_BUTTON_ID = 0;
	private static final int FLUSH_BUTTON_ID = 1;
	private static final int DRAIN_BARREL_BUTTON_ID = 2;
	private static final int FILL_BARREL_BUTTON_ID = 3;
	private static final int SEAL_BUTTON_X = 143;
	private static final int SEAL_BUTTON_Y = 70;
	private static final int SEAL_BUTTON_WIDTH = 18;
	private static final int SEAL_BUTTON_HEIGHT = 18;
	private static final int FLUSH_BUTTON_X = 121;
	private static final int FLUSH_BUTTON_Y = 70;
	private static final int FLUSH_BUTTON_WIDTH = 18;
	private static final int FLUSH_BUTTON_HEIGHT = 18;
	private static final int TITLE_Y = 12;
	private static final int STATUS_Y = 24;
	private static final int FLUID_NAME_Y = 91;
	private static final int AMOUNT_Y = 101;
	private static final int TOP_PANEL_X = 9;
	private static final int TOP_PANEL_Y = 7;
	private static final int TOP_PANEL_WIDTH = 158;
	private static final int TOP_PANEL_HEIGHT = 108;
	private static final int BARREL_PANEL_GAP = 6;
	private static final int SIDE_PANEL_VERTICAL_GAP = 3;
	private static final int BARREL_PANEL_WIDTH = AdjacentBarrelPanel.WIDTH;
	private static final int BARREL_PANEL_X = -BARREL_PANEL_WIDTH - BARREL_PANEL_GAP;
	private static final int BARREL_PANEL_Y = TOP_PANEL_Y;
	private static final int BARREL_PANEL_HEIGHT = AdjacentBarrelPanel.HEIGHT;
	private static final int DRAIN_BARREL_BUTTON_X = BARREL_PANEL_X + AdjacentBarrelPanel.DRAIN_BUTTON_X;
	private static final int FILL_BARREL_BUTTON_X = BARREL_PANEL_X + AdjacentBarrelPanel.FILL_BUTTON_X;
	private static final int TRANSFER_BUTTON_WIDTH = AdjacentBarrelPanel.BUTTON_WIDTH;
	private static final int TRANSFER_BUTTON_HEIGHT = AdjacentBarrelPanel.BUTTON_HEIGHT;
	private static final int TRANSFER_BUTTON_Y = BARREL_PANEL_Y + AdjacentBarrelPanel.BUTTON_Y;
	private static final int PIPE_PANEL_WIDTH = 84;
	private static final int PIPE_PANEL_HEIGHT = 96;
	private static final int PIPE_PANEL_Y = BARREL_PANEL_Y + BARREL_PANEL_HEIGHT + SIDE_PANEL_VERTICAL_GAP;
	private static final int INVENTORY_PANEL_X = 7;
	private static final int INVENTORY_PANEL_Y = 118;
	private static final int INVENTORY_PANEL_WIDTH = 162;
	private static final int INVENTORY_PANEL_HEIGHT = 93;
	private static final int FRAME_OUTER_INSET = 3;
	private static final int FRAME_INNER_INSET = 6;
	private static final int GRAIN_LEFT_INSET = 7;
	private static final int GRAIN_TOP = 10;
	private static final int GRAIN_BOTTOM_INSET = 8;
	private static final int GRAIN_STEP = 8;
	private static final int GRAIN_VARIANT_DIVISOR = 16;
	private static final int GRAIN_VARIANT_OFFSET = 10;
	private static final int INSET_BORDER = 2;
	private static final int INSET_FILL = 4;
	private static final int SLOT_SIZE = 16;
	private static final int SLOT_STEP = 18;
	private static final int SLOT_BORDER = 1;
	private static final int SLOT_INNER_INSET = 2;
	private static final int MIN_DRAWN_FILL = 1;
	private static final int TEXTURE_TILE_SIZE = 16;
	private static final int COLOR_CHANNEL_MAX = 255;
	private static final float FULL_COLOR = 1.0F;
	private static final int WOOD_DARK = 0xFF2E170A;
	private static final int WOOD_MID = 0xFF6B3A1B;
	private static final int WOOD_LIGHT = 0xFF9A6230;
	private static final int PANEL_DARK = 0xFF5A3219;
	private static final int PANEL_LIGHT = 0xFFD4B789;
	private static final int PANEL_MID = 0xFFC19761;
	private static final int TEXT_DARK = 0xFF2F1C10;
	private static final int WOOD_GRAIN_DARK = 0x286B3A1B;
	private static final int WOOD_GRAIN_LIGHT = 0x289A6230;
	private static final int SLOT_BORDER_COLOR = 0xFF3B2111;
	private static final int SLOT_RIM_COLOR = 0xFF8A6743;
	private static final int SLOT_SHADOW_COLOR = 0xFF4D4A43;
	private static final int SLOT_FILL_COLOR = 0xFF626058;
	private static final int BRASS_DARK = 0xFF7A4813;
	private static final int BRASS_LIGHT = 0xFFE2B85D;
	private static final int GAUGE_EMPTY_COLOR = 0xFF1F2633;
	private static final int HIGHLIGHT_COLOR = 0x80FFFFFF;
	private static final int BREWED_FLUID_RIPPLE_COLOR = 0x28FFFFFF;
	private static final int BREWED_FLUID_EDGE_SHADOW = 0x30000000;
	private static final int OPAQUE_ALPHA_MASK = 0xFF000000;
	private static final int BREWED_FLUID_RIPPLE_TOP_INSET = 3;
	private static final int BREWED_FLUID_RIPPLE_STEP = 8;

	private final TileEntityFoudre foudre;
	private final InventoryPlayer playerInventory;
	private GuiMechanicalIconButton sealButton;
	private GuiMechanicalIconButton flushButton;
	private GuiButton drainBarrelButton;
	private GuiButton fillBarrelButton;
	private final MechanicalDial fillDial = new MechanicalDial();
	private final MechanicalDial processDial = new MechanicalDial();
	private final MechanicalValveControl valveControl = new MechanicalValveControl();
	private boolean confirmFlush;

	public GuiFoudre(TileEntityFoudre foudre, InventoryPlayer playerInventory) {
		super(new ContainerFoudre(playerInventory, foudre));
		this.foudre = foudre;
		this.playerInventory = playerInventory;
		this.xSize = GUI_WIDTH;
		this.ySize = GUI_HEIGHT;
	}

	@Override
	public void initGui() {
		super.initGui();
		int barrelPanelShift = this.getBarrelPanelShiftX();
		this.sealButton = new GuiMechanicalIconButton(SEAL_BUTTON_ID, this.guiLeft + SEAL_BUTTON_X,
			this.guiTop + SEAL_BUTTON_Y, SEAL_BUTTON_WIDTH, SEAL_BUTTON_HEIGHT,
			GuiMechanicalIconButton.Icon.SEAL);
		this.buttonList.add(this.sealButton);
		this.flushButton = new GuiMechanicalIconButton(FLUSH_BUTTON_ID, this.guiLeft + FLUSH_BUTTON_X,
			this.guiTop + FLUSH_BUTTON_Y, FLUSH_BUTTON_WIDTH, FLUSH_BUTTON_HEIGHT,
			GuiMechanicalIconButton.Icon.FLUSH);
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
		if (button.id == SEAL_BUTTON_ID && button.enabled) {
			this.confirmFlush = false;
			IronAgeFurnitureNetwork.channel.sendToServer(new FoudreSealMessage(this.foudre.getPos()));
		} else if (button.id == FLUSH_BUTTON_ID && button.enabled) {
			if (!this.confirmFlush) {
				this.confirmFlush = true;
				return;
			}

			this.confirmFlush = false;
			IronAgeFurnitureNetwork.channel.sendToServer(new FoudreFlushMessage(this.foudre.getPos()));
		} else if (button.id == DRAIN_BARREL_BUTTON_ID && button.enabled) {
			this.confirmFlush = false;
			IronAgeFurnitureNetwork.channel.sendToServer(new FoudreBarrelTransferMessage(this.foudre.getPos(),
				false));
		} else if (button.id == FILL_BARREL_BUTTON_ID && button.enabled) {
			this.confirmFlush = false;
			IronAgeFurnitureNetwork.channel.sendToServer(new FoudreBarrelTransferMessage(this.foudre.getPos(), true));
		}
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		if (mouseButton == 0 && this.shouldDrawPipePanel()) {
			this.positionValveControl();
			FluidPortMode mode = this.valveControl.getClickedMode(mouseX, mouseY,
				this.foudre.hasConnectedInlet(), this.foudre.hasConnectedOutlet(), this.foudre.isSealed());

			if (mode != null) {
				this.confirmFlush = false;
				IronAgeFurnitureNetwork.channel.sendToServer(new FoudrePortModeMessage(this.foudre.getPos(), mode));
				return;
			}
		}

		super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		String title = I18n.format(this.foudre.getContainerNameKey());

		if (!this.foudre.getLabel().isEmpty()) {
			title = this.foudre.getLabel();
		}

		this.drawFittedCenteredText(title, TEXT_TRIM_PADDING / 2, this.xSize - TEXT_TRIM_PADDING, TITLE_Y);
		this.fontRendererObj.drawString(this.playerInventory.getDisplayName().getUnformattedText(), INVENTORY_LABEL_X,
			PLAYER_INVENTORY_LABEL_Y, TEXT_DARK);

		FluidStack fluid = this.foudre.getFluid();
		String fluidName = fluid == null ? I18n.format("gui.ironagefurniture.barrel.empty")
			: DrinkDisplayHelper.getDisplayName(fluid);
		this.drawFittedCenteredText(fluidName, TEXT_TRIM_PADDING / 2, this.xSize - TEXT_TRIM_PADDING, FLUID_NAME_Y);

		String amount = this.getAmountText();
		this.drawFittedCenteredText(amount, TEXT_TRIM_PADDING / 2, this.xSize - TEXT_TRIM_PADDING, AMOUNT_Y);

		String status = this.getBrewStatus();

		if (!status.isEmpty()) {
			this.drawFittedCenteredText(status, TEXT_TRIM_PADDING / 2, this.xSize - TEXT_TRIM_PADDING, STATUS_Y);
		}

		this.drawBarrelTransferPanelForeground();
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		this.drawWoodFrame();
		this.drawTopPanel();
		this.drawPipePanel();
		this.drawInventoryPanel();
		this.drawBarrelTransferPanel();
		this.drawFluidGauge();
		this.drawMechanicalDials();
		this.drawIngredientSlots();
		this.drawPlayerSlots();
	}

	private void drawWoodFrame() {
		Gui.drawRect(this.guiLeft, this.guiTop, this.guiLeft + this.xSize, this.guiTop + this.ySize, WOOD_DARK);
		Gui.drawRect(this.guiLeft + FRAME_OUTER_INSET, this.guiTop + FRAME_OUTER_INSET,
			this.guiLeft + this.xSize - FRAME_OUTER_INSET, this.guiTop + this.ySize - FRAME_OUTER_INSET, WOOD_MID);
		Gui.drawRect(this.guiLeft + FRAME_INNER_INSET, this.guiTop + FRAME_INNER_INSET,
			this.guiLeft + this.xSize - FRAME_INNER_INSET, this.guiTop + this.ySize - FRAME_INNER_INSET, WOOD_LIGHT);

		for (int y = GRAIN_TOP; y < this.ySize - GRAIN_BOTTOM_INSET; y += GRAIN_STEP) {
			int grain = y % GRAIN_VARIANT_DIVISOR == GRAIN_VARIANT_OFFSET ? WOOD_GRAIN_DARK : WOOD_GRAIN_LIGHT;
			Gui.drawRect(this.guiLeft + GRAIN_LEFT_INSET, this.guiTop + y,
				this.guiLeft + this.xSize - GRAIN_LEFT_INSET, this.guiTop + y + MIN_DRAWN_FILL, grain);
		}
	}

	private void drawTopPanel() {
		this.drawInsetPanel(TOP_PANEL_X, TOP_PANEL_Y, TOP_PANEL_WIDTH, TOP_PANEL_HEIGHT);
	}

	private void drawInventoryPanel() {
		this.drawInsetPanel(INVENTORY_PANEL_X, INVENTORY_PANEL_Y, INVENTORY_PANEL_WIDTH, INVENTORY_PANEL_HEIGHT);
	}

	private void drawPipePanel() {
		if (!this.shouldDrawPipePanel()) {
			return;
		}

		this.drawOuterPanel(this.getPipePanelOffsetX(), PIPE_PANEL_Y, PIPE_PANEL_WIDTH, PIPE_PANEL_HEIGHT);
		this.positionValveControl();
		this.valveControl.draw(this.fontRendererObj, this.foudre.getPortMode(), this.foudre.hasConnectedInlet(),
			this.foudre.hasConnectedOutlet(), this.foudre.isSealed());
	}

	private void positionValveControl() {
		this.valveControl.setPosition(this.guiLeft + this.getPipePanelOffsetX(), this.guiTop + PIPE_PANEL_Y);
	}

	private void drawMechanicalTooltips(int mouseX, int mouseY) {
		List<String> tooltip = new ArrayList<String>();

		if (this.isPointWithin(mouseX, mouseY, FILL_DIAL_X, FILL_DIAL_Y, DIAL_SIZE, DIAL_SIZE)) {
			FluidStack fluid = this.foudre.getFluid();
			tooltip.add(I18n.format("gui.ironagefurniture.dial.fill"));
			tooltip.add(fluid == null ? I18n.format("gui.ironagefurniture.barrel.empty")
				: DrinkDisplayHelper.getDisplayName(fluid));
			tooltip.add(this.getAmountText());
		} else if (this.isPointWithin(mouseX, mouseY, PROCESS_DIAL_X, PROCESS_DIAL_Y, DIAL_SIZE, DIAL_SIZE)) {
			int total = this.getProgressTotal();
			tooltip.add(I18n.format("gui.ironagefurniture.dial.process"));
			tooltip.add(total <= 0 ? I18n.format("gui.ironagefurniture.dial.idle") : this.getBrewStatus());

			if (Loader.isModLoaded("JEI")) {
				tooltip.add(I18n.format("gui.ironagefurniture.jei.view_recipes"));
			}
		} else if (this.sealButton != null && this.sealButton.isMouseOver()) {
			tooltip.add(this.getSealButtonText());
			tooltip.add(I18n.format(this.foudre.isSealed() ? "gui.ironagefurniture.tooltip.open"
				: "gui.ironagefurniture.tooltip.seal"));
		} else if (this.flushButton != null && this.flushButton.isMouseOver()) {
			tooltip.add(this.getFlushButtonText());
			tooltip.add(I18n.format(this.confirmFlush ? "gui.ironagefurniture.tooltip.flush_confirm"
				: "gui.ironagefurniture.tooltip.flush"));
		} else if (this.shouldDrawPipePanel()) {
			this.positionValveControl();
			String valveTooltip = this.valveControl.getHoverText(mouseX, mouseY, this.foudre.hasConnectedInlet(),
				this.foudre.hasConnectedOutlet(), this.foudre.isSealed());

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

	private void drawBarrelTransferPanel() {
		AdjacentBarrelPanel.drawBackground(this.guiLeft + this.getBarrelPanelOffsetX(),
			this.guiTop + BARREL_PANEL_Y);
		this.drawAdjacentBarrelGauge();
	}

	private void drawOuterPanel(int x, int y, int width, int height) {
		int left = this.guiLeft + x;
		int top = this.guiTop + y;
		Gui.drawRect(left, top, left + width, top + height, WOOD_DARK);
		Gui.drawRect(left + FRAME_OUTER_INSET, top + FRAME_OUTER_INSET,
			left + width - FRAME_OUTER_INSET, top + height - FRAME_OUTER_INSET, WOOD_MID);
		Gui.drawRect(left + FRAME_INNER_INSET, top + FRAME_INNER_INSET,
			left + width - FRAME_INNER_INSET, top + height - FRAME_INNER_INSET, PANEL_LIGHT);
	}

	private void drawInsetPanel(int x, int y, int width, int height) {
		int left = this.guiLeft + x;
		int top = this.guiTop + y;
		Gui.drawRect(left, top, left + width, top + height, PANEL_DARK);
		Gui.drawRect(left + INSET_BORDER, top + INSET_BORDER, left + width - INSET_BORDER,
			top + height - INSET_BORDER, PANEL_MID);
		Gui.drawRect(left + INSET_FILL, top + INSET_FILL, left + width - INSET_FILL,
			top + height - INSET_FILL, PANEL_LIGHT);
	}

	private void updateButtonState() {
		if (this.sealButton != null) {
			this.sealButton.setIcon(this.foudre.isSealed() ? GuiMechanicalIconButton.Icon.OPEN
				: GuiMechanicalIconButton.Icon.SEAL);
		}

		if (this.flushButton != null) {
			if (!this.foudre.canFlush()) {
				this.confirmFlush = false;
			}

			this.flushButton.setIcon(this.confirmFlush ? GuiMechanicalIconButton.Icon.CONFIRM
				: GuiMechanicalIconButton.Icon.FLUSH);
			this.flushButton.enabled = this.foudre.canFlush();
		}

		if (this.drainBarrelButton != null) {
			this.drainBarrelButton.enabled = this.foudre.canDrainAdjacentBarrel();
		}

		if (this.fillBarrelButton != null) {
			this.fillBarrelButton.enabled = this.foudre.canFillAdjacentBarrel();
		}

	}

	private void updateButtonPositions() {
		int barrelPanelShift = this.getBarrelPanelShiftX();
		this.sealButton.xPosition = this.guiLeft + SEAL_BUTTON_X;
		this.sealButton.yPosition = this.guiTop + SEAL_BUTTON_Y;
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

	private void drawAdjacentBarrelGauge() {
		TileEntityBarrel barrel = this.foudre.getAdjacentTransferBarrel();
		FluidStack fluid = barrel == null ? null : barrel.getFluid();
		int fillWidth = AdjacentBarrelPanel.getGaugeFillWidth(barrel);

		if (fluid == null || fluid.amount <= 0 || fluid.getFluid() == null || fillWidth <= 0) {
			return;
		}

		int left = this.guiLeft + this.getBarrelPanelOffsetX() + AdjacentBarrelPanel.GAUGE_X;
		int top = this.guiTop + BARREL_PANEL_Y + AdjacentBarrelPanel.GAUGE_Y;
		this.drawFluid(left, top, fillWidth, AdjacentBarrelPanel.GAUGE_HEIGHT, fluid);
		Gui.drawRect(left, top, left + fillWidth, top + MIN_DRAWN_FILL,
			HIGHLIGHT_COLOR);
	}

	private String getSealButtonText() {
		return I18n.format(this.foudre.isSealed() ? "gui.ironagefurniture.foudre.open"
			: "gui.ironagefurniture.foudre.seal");
	}

	private String getFlushButtonText() {
		return I18n.format(this.confirmFlush ? "gui.ironagefurniture.flush.confirm"
			: "gui.ironagefurniture.foudre.flush");
	}

	private void drawBarrelTransferPanelForeground() {
		int barrelPanelShift = this.getBarrelPanelShiftX();
		AdjacentBarrelPanel.drawForeground(this.fontRendererObj, BARREL_PANEL_X + barrelPanelShift,
			BARREL_PANEL_Y, this.foudre.getAdjacentTransferBarrel());
	}

	private void drawIngredientSlots() {
		int trayLeft = this.guiLeft + INGREDIENT_X - 4;
		int trayTop = this.guiTop + INGREDIENT_Y - 4;
		int trayRight = trayLeft + SLOT_SIZE * 2 + SLOT_STEP - SLOT_SIZE + 8;
		int trayBottom = trayTop + SLOT_SIZE * 2 + SLOT_STEP - SLOT_SIZE + 8;
		Gui.drawRect(trayLeft, trayTop, trayRight, trayBottom, BRASS_DARK);
		Gui.drawRect(trayLeft + 2, trayTop + 2, trayRight - 2, trayBottom - 2, BRASS_LIGHT);
		Gui.drawRect(trayLeft + 4, trayTop + 4, trayRight - 4, trayBottom - 4, PANEL_DARK);
		this.drawSlot(INGREDIENT_X, INGREDIENT_Y);
		this.drawSlot(INGREDIENT_X + SLOT_STEP, INGREDIENT_Y);
		this.drawSlot(INGREDIENT_X, INGREDIENT_Y + SLOT_STEP);
		this.drawSlot(INGREDIENT_X + SLOT_STEP, INGREDIENT_Y + SLOT_STEP);
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
		Gui.drawRect(left - SLOT_BORDER, top - SLOT_BORDER, left + SLOT_SIZE + SLOT_BORDER,
			top + SLOT_SIZE + SLOT_BORDER, SLOT_BORDER_COLOR);
		Gui.drawRect(left, top, left + SLOT_SIZE, top + SLOT_SIZE, SLOT_RIM_COLOR);
		Gui.drawRect(left + SLOT_BORDER, top + SLOT_BORDER, left + SLOT_SIZE, top + SLOT_SIZE, SLOT_SHADOW_COLOR);
		Gui.drawRect(left + SLOT_INNER_INSET, top + SLOT_INNER_INSET, left + SLOT_SIZE - SLOT_BORDER,
			top + SLOT_SIZE - SLOT_BORDER, SLOT_FILL_COLOR);
	}

	private void drawMechanicalDials() {
		FluidStack fluid = this.foudre.getFluid();
		float fill = (float)this.foudre.getFluidAmount() / (float)this.foudre.getCapacity();
		int fluidColor = fluid == null || fluid.getFluid() == null ? GAUGE_EMPTY_COLOR : this.getFluidColor(fluid);
		int progressTotal = this.getProgressTotal();
		float process = progressTotal <= 0 ? 0.0F : (float)this.getProgress() / (float)progressTotal;

		this.fillDial.draw(this.guiLeft + FILL_DIAL_X, this.guiTop + FILL_DIAL_Y, DIAL_SIZE, fill,
			fluidColor, fluid != null);
		this.processDial.draw(this.guiLeft + PROCESS_DIAL_X, this.guiTop + PROCESS_DIAL_Y, DIAL_SIZE, process,
			BRASS_LIGHT, progressTotal > 0);
	}

	private void drawFluidGauge() {
		int left = this.guiLeft + FLUID_GAUGE_X;
		int top = this.guiTop + FLUID_GAUGE_Y;
		Gui.drawRect(left - FLUID_GAUGE_BORDER, top - FLUID_GAUGE_BORDER,
			left + FLUID_GAUGE_WIDTH + FLUID_GAUGE_BORDER,
			top + FLUID_GAUGE_HEIGHT + FLUID_GAUGE_BORDER, SLOT_BORDER_COLOR);
		Gui.drawRect(left, top, left + FLUID_GAUGE_WIDTH, top + FLUID_GAUGE_HEIGHT, GAUGE_EMPTY_COLOR);

		FluidStack fluid = this.foudre.getFluid();

		if (fluid == null || fluid.getFluid() == null || fluid.amount <= 0 || this.foudre.getCapacity() <= 0) {
			return;
		}

		int fillHeight = Math.min(FLUID_GAUGE_HEIGHT, Math.max(MIN_DRAWN_FILL,
			fluid.amount * FLUID_GAUGE_HEIGHT / this.foudre.getCapacity()));
		int fillTop = top + FLUID_GAUGE_HEIGHT - fillHeight;
		this.drawFluid(left, fillTop, FLUID_GAUGE_WIDTH, fillHeight, fluid);
		Gui.drawRect(left, fillTop, left + FLUID_GAUGE_WIDTH, fillTop + MIN_DRAWN_FILL, HIGHLIGHT_COLOR);
	}

	private void drawFluid(int left, int top, int width, int height, FluidStack fluid) {
		if (DrinkDisplayHelper.shouldDrawTinted(fluid)) {
			this.drawBrewedFluid(left, top, width, height, fluid);
			return;
		}

		ResourceLocation still = fluid.getFluid().getStill(fluid);

		if (still == null) {
			Gui.drawRect(left, top, left + width, top + height, this.getFluidColor(fluid));
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

	private void drawBrewedFluid(int left, int top, int width, int height, FluidStack fluid) {
		Gui.drawRect(left, top, left + width, top + height, this.getFluidColor(fluid));
		Gui.drawRect(left, top, left + MIN_DRAWN_FILL, top + height, BREWED_FLUID_EDGE_SHADOW);
		Gui.drawRect(left + width - MIN_DRAWN_FILL, top, left + width, top + height, BREWED_FLUID_EDGE_SHADOW);

		for (int y = top + BREWED_FLUID_RIPPLE_TOP_INSET; y < top + height; y += BREWED_FLUID_RIPPLE_STEP) {
			Gui.drawRect(left + MIN_DRAWN_FILL, y, left + width - MIN_DRAWN_FILL,
				Math.min(y + MIN_DRAWN_FILL, top + height), BREWED_FLUID_RIPPLE_COLOR);
		}
	}

	private String getAmountText() {
		return I18n.format("gui.ironagefurniture.foudre.millibuckets",
			Integer.toString(this.foudre.getFluidAmount()), Integer.toString(this.foudre.getCapacity()));
	}

	private String getBrewStatus() {
		if (this.foudre.isInfusionComplete()) {
			return I18n.format("gui.ironagefurniture.foudre.infusion_complete");
		}

		if (this.foudre.getBrewTimeTotal() > 0 && !this.foudre.getBrewRecipeName().isEmpty()) {
			int percent = this.foudre.getBrewTime() * 100 / this.foudre.getBrewTimeTotal();
			return I18n.format("gui.ironagefurniture.foudre.brewing", this.foudre.getBrewRecipeName(),
				Integer.valueOf(percent));
		}

		if (!this.foudre.isSealed() && !this.foudre.getPotentialRecipeName().isEmpty()) {
			return I18n.format("gui.ironagefurniture.foudre.open_recipe",
				this.foudre.getPotentialRecipeName());
		}

		if (!this.foudre.isSealed() || this.foudre.getAgeProgressTotal() <= 0
				|| this.foudre.getNextAgeLevelName().isEmpty()) {
			return I18n.format(this.foudre.isSealed() ? "gui.ironagefurniture.foudre.sealed"
				: "gui.ironagefurniture.foudre.open_state");
		}

		int percent = this.foudre.getAgeProgress() * 100 / this.foudre.getAgeProgressTotal();
		return I18n.format("gui.ironagefurniture.foudre.aging", this.foudre.getNextAgeLevelName(),
			Integer.valueOf(percent));
	}

	private int getProgress() {
		return this.foudre.getBrewTimeTotal() > 0 ? this.foudre.getBrewTime() : this.foudre.getAgeProgress();
	}

	private int getProgressTotal() {
		return this.foudre.getBrewTimeTotal() > 0 ? this.foudre.getBrewTimeTotal()
			: this.foudre.getAgeProgressTotal();
	}

	private int getFluidColor(FluidStack fluid) {
		int color = fluid.getFluid().getColor(fluid);
		return (color & OPAQUE_ALPHA_MASK) == 0 ? color | OPAQUE_ALPHA_MASK : color;
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

	private boolean shouldDrawPipePanel() {
		return this.foudre.hasConnectedPipework();
	}

	private int getPipePanelOffsetX() {
		return this.getBarrelPanelOffsetX() + (BARREL_PANEL_WIDTH - PIPE_PANEL_WIDTH) / 2;
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

	public final List<Rectangle> getGuiExtraAreas() {
		List<Rectangle> areas = new ArrayList<Rectangle>();
		int barrelPanelX = this.guiLeft + this.getBarrelPanelOffsetX();
		areas.add(new Rectangle(barrelPanelX, this.guiTop + BARREL_PANEL_Y, BARREL_PANEL_WIDTH,
			BARREL_PANEL_HEIGHT));

		if (this.shouldDrawPipePanel()) {
			areas.add(new Rectangle(this.guiLeft + this.getPipePanelOffsetX(), this.guiTop + PIPE_PANEL_Y,
				PIPE_PANEL_WIDTH, PIPE_PANEL_HEIGHT));
		}

		return areas;
	}
}
