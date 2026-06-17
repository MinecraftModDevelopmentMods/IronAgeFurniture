package com.mcmoddev.ironagefurniture.client.gui;

import java.io.IOException;

import com.mcmoddev.ironagefurniture.api.container.ContainerFoudre;
import com.mcmoddev.ironagefurniture.api.DrinkDisplayHelper;
import com.mcmoddev.ironagefurniture.api.network.FoudreBarrelTransferMessage;
import com.mcmoddev.ironagefurniture.api.network.FoudreFlushMessage;
import com.mcmoddev.ironagefurniture.api.network.FoudrePortToggleMessage;
import com.mcmoddev.ironagefurniture.api.network.FoudreSealMessage;
import com.mcmoddev.ironagefurniture.api.network.IronAgeFurnitureNetwork;
import com.mcmoddev.ironagefurniture.api.tile.TileEntityBarrel;
import com.mcmoddev.ironagefurniture.api.tile.TileEntityFoudre;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

public class GuiFoudre extends GuiContainer {
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
	private static final int GAUGE_X = 22;
	private static final int GAUGE_Y = 36;
	private static final int GAUGE_WIDTH = 16;
	private static final int GAUGE_HEIGHT = 54;
	private static final int AMOUNT_TEXT_X = 50;
	private static final int AMOUNT_TEXT_WIDTH = 112;
	private static final int INGREDIENT_X = 76;
	private static final int INGREDIENT_Y = 42;
	private static final int PROGRESS_X = 122;
	private static final int PROGRESS_Y = 56;
	private static final int PROGRESS_WIDTH = 36;
	private static final int PROGRESS_HEIGHT = 8;
	private static final int SEAL_BUTTON_ID = 0;
	private static final int FLUSH_BUTTON_ID = 1;
	private static final int DRAIN_BARREL_BUTTON_ID = 2;
	private static final int FILL_BARREL_BUTTON_ID = 3;
	private static final int INLET_PORT_BUTTON_ID = 4;
	private static final int OUTLET_PORT_BUTTON_ID = 5;
	private static final int SEAL_BUTTON_X = 121;
	private static final int SEAL_BUTTON_Y = 70;
	private static final int SEAL_BUTTON_WIDTH = 40;
	private static final int SEAL_BUTTON_HEIGHT = 20;
	private static final int FLUSH_BUTTON_X = 121;
	private static final int FLUSH_BUTTON_Y = 34;
	private static final int FLUSH_BUTTON_WIDTH = 40;
	private static final int FLUSH_BUTTON_HEIGHT = 20;
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
	private static final int HIDDEN_PANEL_OFFSET = Integer.MIN_VALUE;
	private static final int BARREL_PANEL_WIDTH = 106;
	private static final int BARREL_PANEL_X = -BARREL_PANEL_WIDTH - BARREL_PANEL_GAP;
	private static final int BARREL_PANEL_Y = TOP_PANEL_Y;
	private static final int BARREL_PANEL_HEIGHT = TOP_PANEL_HEIGHT + 4;
	private static final int BARREL_PANEL_TITLE_Y = BARREL_PANEL_Y + 10;
	private static final int BARREL_PANEL_TEXT_X = BARREL_PANEL_X + 36;
	private static final int BARREL_PANEL_TEXT_WIDTH = 62;
	private static final int BARREL_PANEL_FLUID_Y = BARREL_PANEL_Y + 31;
	private static final int BARREL_PANEL_AMOUNT_Y = BARREL_PANEL_Y + 45;
	private static final int BARREL_PANEL_CAPACITY_Y = BARREL_PANEL_AMOUNT_Y + 11;
	private static final int BARREL_PANEL_EMPTY_Y = BARREL_PANEL_Y + 44;
	private static final int BARREL_PANEL_GAUGE_X = BARREL_PANEL_X + 9;
	private static final int BARREL_PANEL_GAUGE_Y = BARREL_PANEL_Y + 31;
	private static final int BARREL_PANEL_GAUGE_WIDTH = 14;
	private static final int BARREL_PANEL_GAUGE_HEIGHT = 48;
	private static final int DRAIN_BARREL_BUTTON_X = BARREL_PANEL_X + 9;
	private static final int FILL_BARREL_BUTTON_X = BARREL_PANEL_X + 57;
	private static final int TRANSFER_BUTTON_BOTTOM_PADDING = 10;
	private static final int TRANSFER_BUTTON_WIDTH = 40;
	private static final int TRANSFER_BUTTON_HEIGHT = 20;
	private static final int TRANSFER_BUTTON_Y = BARREL_PANEL_Y + BARREL_PANEL_HEIGHT - TRANSFER_BUTTON_HEIGHT
		- TRANSFER_BUTTON_BOTTOM_PADDING;
	private static final int PIPE_PANEL_WIDTH = 84;
	private static final int PIPE_PANEL_HEIGHT = 96;
	private static final int PIPE_PANEL_X = BARREL_PANEL_X + (BARREL_PANEL_WIDTH - PIPE_PANEL_WIDTH) / 2;
	private static final int PIPE_PANEL_Y = BARREL_PANEL_Y + BARREL_PANEL_HEIGHT + SIDE_PANEL_VERTICAL_GAP;
	private static final int PIPE_PANEL_TITLE_Y = PIPE_PANEL_Y + 10;
	private static final int PIPE_PANEL_TEXT_X = 10;
	private static final int PIPE_PANEL_TEXT_WIDTH = PIPE_PANEL_WIDTH - PIPE_PANEL_TEXT_X * 2;
	private static final int PIPE_PANEL_INLET_LABEL_Y = PIPE_PANEL_Y + 24;
	private static final int PIPE_PANEL_OUTLET_LABEL_Y = PIPE_PANEL_Y + 56;
	private static final int PIPE_PANEL_BUTTON_WIDTH = 54;
	private static final int PIPE_PANEL_BUTTON_HEIGHT = 20;
	private static final int PIPE_PANEL_BUTTON_X = PIPE_PANEL_X + (PIPE_PANEL_WIDTH - PIPE_PANEL_BUTTON_WIDTH) / 2;
	private static final int PIPE_PANEL_INLET_BUTTON_Y = PIPE_PANEL_Y + 32;
	private static final int PIPE_PANEL_OUTLET_BUTTON_Y = PIPE_PANEL_Y + 64;
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
	private static final int PROGRESS_BORDER = 1;
	private static final int GAUGE_BORDER = 2;
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
	private static final int PROGRESS_TRACK_COLOR = 0xFF3C3325;
	private static final int PROGRESS_FILL_COLOR = 0xFFB8792A;
	private static final int GAUGE_BORDER_COLOR = 0xFF404040;
	private static final int GAUGE_EMPTY_COLOR = 0xFF1F2633;
	private static final int HIGHLIGHT_COLOR = 0x80FFFFFF;
	private static final int BREWED_FLUID_RIPPLE_COLOR = 0x28FFFFFF;
	private static final int BREWED_FLUID_EDGE_SHADOW = 0x30000000;
	private static final int OPAQUE_ALPHA_MASK = 0xFF000000;
	private static final int BREWED_FLUID_RIPPLE_TOP_INSET = 3;
	private static final int BREWED_FLUID_RIPPLE_STEP = 8;

	private final TileEntityFoudre foudre;
	private final InventoryPlayer playerInventory;
	private GuiButton sealButton;
	private GuiButton flushButton;
	private GuiButton drainBarrelButton;
	private GuiButton fillBarrelButton;
	private GuiButton inletPortButton;
	private GuiButton outletPortButton;
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
		this.sealButton = new GuiButton(SEAL_BUTTON_ID, this.guiLeft + SEAL_BUTTON_X,
			this.guiTop + SEAL_BUTTON_Y, SEAL_BUTTON_WIDTH, SEAL_BUTTON_HEIGHT, this.getSealButtonText());
		this.buttonList.add(this.sealButton);
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
		this.inletPortButton = new GuiButton(INLET_PORT_BUTTON_ID, this.guiLeft + PIPE_PANEL_BUTTON_X,
			this.guiTop + PIPE_PANEL_INLET_BUTTON_Y, PIPE_PANEL_BUTTON_WIDTH, PIPE_PANEL_BUTTON_HEIGHT,
			this.getPipeButtonText(true));
		this.buttonList.add(this.inletPortButton);
		this.outletPortButton = new GuiButton(OUTLET_PORT_BUTTON_ID, this.guiLeft + PIPE_PANEL_BUTTON_X,
			this.guiTop + PIPE_PANEL_OUTLET_BUTTON_Y, PIPE_PANEL_BUTTON_WIDTH, PIPE_PANEL_BUTTON_HEIGHT,
			this.getPipeButtonText(false));
		this.buttonList.add(this.outletPortButton);
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
		} else if (button.id == INLET_PORT_BUTTON_ID && button.enabled) {
			this.confirmFlush = false;
			IronAgeFurnitureNetwork.channel.sendToServer(new FoudrePortToggleMessage(this.foudre.getPos(), true));
		} else if (button.id == OUTLET_PORT_BUTTON_ID && button.enabled) {
			this.confirmFlush = false;
			IronAgeFurnitureNetwork.channel.sendToServer(new FoudrePortToggleMessage(this.foudre.getPos(), false));
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		String title = I18n.format(this.foudre.getContainerNameKey());

		if (!this.foudre.getLabel().isEmpty()) {
			title = this.foudre.getLabel();
		}

		this.drawCenteredText(title, TITLE_Y);
		this.fontRendererObj.drawString(this.playerInventory.getDisplayName().getUnformattedText(), INVENTORY_LABEL_X,
			PLAYER_INVENTORY_LABEL_Y, TEXT_DARK);

		FluidStack fluid = this.foudre.getFluid();
		String fluidName = fluid == null ? I18n.format("gui.ironagefurniture.barrel.empty")
			: DrinkDisplayHelper.getDisplayName(fluid);
		this.drawCenteredText(fluidName, FLUID_NAME_Y);

		String amount = this.getAmountText();
		this.drawBoundedCenteredText(amount, AMOUNT_TEXT_X, AMOUNT_TEXT_WIDTH, AMOUNT_Y);

		String status = this.getBrewStatus();

		if (!status.isEmpty()) {
			this.drawCenteredText(status, STATUS_Y);
		}

		this.drawPipePanelForeground();
		this.drawBarrelTransferPanelForeground();
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		this.drawWoodFrame();
		this.drawTopPanel();
		this.drawPipePanel();
		this.drawInventoryPanel();
		this.drawBarrelTransferPanel();
		this.drawGauge();
		this.drawIngredientSlots();
		this.drawPlayerSlots();
		this.drawProgress();
	}

	private void drawWoodFrame() {
		this.drawRect(this.guiLeft, this.guiTop, this.guiLeft + this.xSize, this.guiTop + this.ySize, WOOD_DARK);
		this.drawRect(this.guiLeft + FRAME_OUTER_INSET, this.guiTop + FRAME_OUTER_INSET,
			this.guiLeft + this.xSize - FRAME_OUTER_INSET, this.guiTop + this.ySize - FRAME_OUTER_INSET, WOOD_MID);
		this.drawRect(this.guiLeft + FRAME_INNER_INSET, this.guiTop + FRAME_INNER_INSET,
			this.guiLeft + this.xSize - FRAME_INNER_INSET, this.guiTop + this.ySize - FRAME_INNER_INSET, WOOD_LIGHT);

		for (int y = GRAIN_TOP; y < this.ySize - GRAIN_BOTTOM_INSET; y += GRAIN_STEP) {
			int grain = y % GRAIN_VARIANT_DIVISOR == GRAIN_VARIANT_OFFSET ? WOOD_GRAIN_DARK : WOOD_GRAIN_LIGHT;
			this.drawRect(this.guiLeft + GRAIN_LEFT_INSET, this.guiTop + y,
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
	}

	private void drawBarrelTransferPanel() {
		this.drawOuterPanel(this.getBarrelPanelOffsetX(), BARREL_PANEL_Y, BARREL_PANEL_WIDTH, BARREL_PANEL_HEIGHT);
		this.drawAdjacentBarrelGauge();
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

	private void drawInsetPanel(int x, int y, int width, int height) {
		int left = this.guiLeft + x;
		int top = this.guiTop + y;
		this.drawRect(left, top, left + width, top + height, PANEL_DARK);
		this.drawRect(left + INSET_BORDER, top + INSET_BORDER, left + width - INSET_BORDER,
			top + height - INSET_BORDER, PANEL_MID);
		this.drawRect(left + INSET_FILL, top + INSET_FILL, left + width - INSET_FILL,
			top + height - INSET_FILL, PANEL_LIGHT);
	}

	private void updateButtonState() {
		if (this.sealButton != null) {
			this.sealButton.displayString = this.getSealButtonText();
		}

		if (this.flushButton != null) {
			if (!this.foudre.canFlush()) {
				this.confirmFlush = false;
			}

			this.flushButton.displayString = this.getFlushButtonText();
			this.flushButton.enabled = this.foudre.canFlush();
		}

		if (this.drainBarrelButton != null) {
			this.drainBarrelButton.enabled = this.foudre.canDrainAdjacentBarrel();
		}

		if (this.fillBarrelButton != null) {
			this.fillBarrelButton.enabled = this.foudre.canFillAdjacentBarrel();
		}

		boolean showPipePanel = this.shouldDrawPipePanel();
		boolean hasInlet = this.foudre.hasConnectedInlet();
		boolean hasOutlet = this.foudre.hasConnectedOutlet();
		boolean portsUnlocked = !this.foudre.isSealed();

		if (this.inletPortButton != null) {
			this.inletPortButton.visible = showPipePanel;
			this.inletPortButton.enabled = showPipePanel && hasInlet && portsUnlocked;
			this.inletPortButton.displayString = this.getPipeButtonText(true);
		}

		if (this.outletPortButton != null) {
			this.outletPortButton.visible = showPipePanel;
			this.outletPortButton.enabled = showPipePanel && hasOutlet && portsUnlocked;
			this.outletPortButton.displayString = this.getPipeButtonText(false);
		}
	}

	private void updateButtonPositions() {
		int barrelPanelShift = this.getBarrelPanelShiftX();
		int pipePanelOffset = this.shouldDrawPipePanel() ? this.getPipePanelOffsetX() : 0;

		if (this.drainBarrelButton != null) {
			this.drainBarrelButton.xPosition = this.guiLeft + DRAIN_BARREL_BUTTON_X + barrelPanelShift;
			this.drainBarrelButton.yPosition = this.guiTop + TRANSFER_BUTTON_Y;
		}

		if (this.fillBarrelButton != null) {
			this.fillBarrelButton.xPosition = this.guiLeft + FILL_BARREL_BUTTON_X + barrelPanelShift;
			this.fillBarrelButton.yPosition = this.guiTop + TRANSFER_BUTTON_Y;
		}

		if (this.inletPortButton != null) {
			this.inletPortButton.xPosition = this.guiLeft + pipePanelOffset
				+ (PIPE_PANEL_WIDTH - PIPE_PANEL_BUTTON_WIDTH) / 2;
			this.inletPortButton.yPosition = this.guiTop + PIPE_PANEL_INLET_BUTTON_Y;
		}

		if (this.outletPortButton != null) {
			this.outletPortButton.xPosition = this.guiLeft + pipePanelOffset
				+ (PIPE_PANEL_WIDTH - PIPE_PANEL_BUTTON_WIDTH) / 2;
			this.outletPortButton.yPosition = this.guiTop + PIPE_PANEL_OUTLET_BUTTON_Y;
		}
	}

	private void drawAdjacentBarrelGauge() {
		int left = this.guiLeft + BARREL_PANEL_GAUGE_X + this.getBarrelPanelShiftX();
		int top = this.guiTop + BARREL_PANEL_GAUGE_Y;
		this.drawRect(left - GAUGE_BORDER, top - GAUGE_BORDER,
			left + BARREL_PANEL_GAUGE_WIDTH + GAUGE_BORDER,
			top + BARREL_PANEL_GAUGE_HEIGHT + GAUGE_BORDER, GAUGE_BORDER_COLOR);
		this.drawRect(left, top, left + BARREL_PANEL_GAUGE_WIDTH,
			top + BARREL_PANEL_GAUGE_HEIGHT, GAUGE_EMPTY_COLOR);

		TileEntityBarrel barrel = this.foudre.getAdjacentTransferBarrel();
		FluidStack fluid = barrel == null ? null : barrel.getFluid();

		if (fluid == null || fluid.amount <= 0 || fluid.getFluid() == null || barrel == null) {
			return;
		}

		int fillHeight = Math.max(MIN_DRAWN_FILL,
			fluid.amount * BARREL_PANEL_GAUGE_HEIGHT / barrel.getCapacity());
		int fillTop = top + BARREL_PANEL_GAUGE_HEIGHT - fillHeight;
		this.drawFluid(left, fillTop, BARREL_PANEL_GAUGE_WIDTH, fillHeight, fluid);
		this.drawRect(left, fillTop, left + BARREL_PANEL_GAUGE_WIDTH, fillTop + MIN_DRAWN_FILL,
			HIGHLIGHT_COLOR);
	}

	private String getSealButtonText() {
		return I18n.format(this.foudre.isSealed() ? "gui.ironagefurniture.foudre.open"
			: "gui.ironagefurniture.foudre.seal");
	}

	private String getPipeButtonText(boolean inlet) {
		boolean open = inlet ? this.foudre.isInletOpen() : this.foudre.isOutletOpen();
		return I18n.format(open ? "gui.ironagefurniture.pipe.close" : "gui.ironagefurniture.pipe.open");
	}

	private String getFlushButtonText() {
		return I18n.format(this.confirmFlush ? "gui.ironagefurniture.flush.confirm"
			: "gui.ironagefurniture.foudre.flush");
	}

	private void drawPipePanelForeground() {
		if (!this.shouldDrawPipePanel()) {
			return;
		}

		int pipePanelOffset = this.getPipePanelOffsetX();
		this.drawBoundedCenteredText(I18n.format("gui.ironagefurniture.foudre.pipework"),
			pipePanelOffset + PIPE_PANEL_TEXT_X, PIPE_PANEL_TEXT_WIDTH, PIPE_PANEL_TITLE_Y);
		this.drawBoundedCenteredText(I18n.format(this.foudre.hasConnectedInlet()
				? "gui.ironagefurniture.foudre.inlet" : "gui.ironagefurniture.foudre.no_inlet"),
			pipePanelOffset + PIPE_PANEL_TEXT_X, PIPE_PANEL_TEXT_WIDTH, PIPE_PANEL_INLET_LABEL_Y);
		this.drawBoundedCenteredText(I18n.format(this.foudre.hasConnectedOutlet()
				? "gui.ironagefurniture.foudre.outlet" : "gui.ironagefurniture.foudre.no_outlet"),
			pipePanelOffset + PIPE_PANEL_TEXT_X, PIPE_PANEL_TEXT_WIDTH, PIPE_PANEL_OUTLET_LABEL_Y);
	}

	private void drawBarrelTransferPanelForeground() {
		int barrelPanelShift = this.getBarrelPanelShiftX();
		this.drawBoundedCenteredText(I18n.format("gui.ironagefurniture.transfer.barrel"),
			BARREL_PANEL_X + barrelPanelShift + TEXT_TRIM_PADDING / 2, BARREL_PANEL_WIDTH - TEXT_TRIM_PADDING,
			BARREL_PANEL_TITLE_Y);

		TileEntityBarrel barrel = this.foudre.getAdjacentTransferBarrel();

		if (barrel == null) {
			this.drawBoundedCenteredText(I18n.format("gui.ironagefurniture.transfer.no_barrel"),
				BARREL_PANEL_TEXT_X + barrelPanelShift, BARREL_PANEL_TEXT_WIDTH, BARREL_PANEL_EMPTY_Y);
			return;
		}

		FluidStack fluid = barrel.getFluid();

		if (fluid == null || fluid.getFluid() == null || fluid.amount <= 0) {
			this.drawBoundedCenteredText(I18n.format("gui.ironagefurniture.barrel.empty"),
				BARREL_PANEL_TEXT_X + barrelPanelShift, BARREL_PANEL_TEXT_WIDTH, BARREL_PANEL_FLUID_Y);
		} else {
			this.drawBoundedCenteredText(DrinkDisplayHelper.getDisplayName(fluid),
				BARREL_PANEL_TEXT_X + barrelPanelShift, BARREL_PANEL_TEXT_WIDTH, BARREL_PANEL_FLUID_Y);
		}

		this.drawBoundedCenteredText(Integer.toString(barrel.getFluidAmount()) + " /", BARREL_PANEL_TEXT_X
			+ barrelPanelShift,
			BARREL_PANEL_TEXT_WIDTH, BARREL_PANEL_AMOUNT_Y);
		this.drawBoundedCenteredText(Integer.toString(barrel.getCapacity()), BARREL_PANEL_TEXT_X
			+ barrelPanelShift,
			BARREL_PANEL_TEXT_WIDTH, BARREL_PANEL_CAPACITY_Y);
	}

	private void drawIngredientSlots() {
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
		this.drawRect(left - SLOT_BORDER, top - SLOT_BORDER, left + SLOT_SIZE + SLOT_BORDER,
			top + SLOT_SIZE + SLOT_BORDER, SLOT_BORDER_COLOR);
		this.drawRect(left, top, left + SLOT_SIZE, top + SLOT_SIZE, SLOT_RIM_COLOR);
		this.drawRect(left + SLOT_BORDER, top + SLOT_BORDER, left + SLOT_SIZE, top + SLOT_SIZE, SLOT_SHADOW_COLOR);
		this.drawRect(left + SLOT_INNER_INSET, top + SLOT_INNER_INSET, left + SLOT_SIZE - SLOT_BORDER,
			top + SLOT_SIZE - SLOT_BORDER, SLOT_FILL_COLOR);
	}

	private void drawProgress() {
		int left = this.guiLeft + PROGRESS_X;
		int top = this.guiTop + PROGRESS_Y;
		this.drawRect(left - PROGRESS_BORDER, top - PROGRESS_BORDER, left + PROGRESS_WIDTH + PROGRESS_BORDER,
			top + PROGRESS_HEIGHT + PROGRESS_BORDER, WOOD_DARK);
		this.drawRect(left, top, left + PROGRESS_WIDTH, top + PROGRESS_HEIGHT, PROGRESS_TRACK_COLOR);

		int total = this.getProgressTotal();

		if (total <= 0) {
			return;
		}

		int width = Math.max(MIN_DRAWN_FILL, this.getProgress() * PROGRESS_WIDTH / total);
		this.drawRect(left, top, left + width, top + PROGRESS_HEIGHT, PROGRESS_FILL_COLOR);
		this.drawRect(left, top, left + width, top + MIN_DRAWN_FILL, HIGHLIGHT_COLOR);
	}

	private void drawGauge() {
		int left = this.guiLeft + GAUGE_X;
		int top = this.guiTop + GAUGE_Y;
		this.drawRect(left - GAUGE_BORDER, top - GAUGE_BORDER, left + GAUGE_WIDTH + GAUGE_BORDER,
			top + GAUGE_HEIGHT + GAUGE_BORDER, GAUGE_BORDER_COLOR);
		this.drawRect(left, top, left + GAUGE_WIDTH, top + GAUGE_HEIGHT, GAUGE_EMPTY_COLOR);

		FluidStack fluid = this.foudre.getFluid();

		if (fluid == null || fluid.amount <= 0 || fluid.getFluid() == null) {
			return;
		}

		int fillHeight = Math.max(MIN_DRAWN_FILL, fluid.amount * GAUGE_HEIGHT / this.foudre.getCapacity());
		int fillTop = top + GAUGE_HEIGHT - fillHeight;
		this.drawFluid(left, fillTop, GAUGE_WIDTH, fillHeight, fluid);
		this.drawRect(left, fillTop, left + GAUGE_WIDTH, fillTop + MIN_DRAWN_FILL, HIGHLIGHT_COLOR);
	}

	private void drawFluid(int left, int top, int width, int height, FluidStack fluid) {
		if (DrinkDisplayHelper.shouldDrawTinted(fluid)) {
			this.drawBrewedFluid(left, top, width, height, fluid);
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

	private void drawBrewedFluid(int left, int top, int width, int height, FluidStack fluid) {
		this.drawRect(left, top, left + width, top + height, this.getFluidColor(fluid));
		this.drawRect(left, top, left + MIN_DRAWN_FILL, top + height, BREWED_FLUID_EDGE_SHADOW);
		this.drawRect(left + width - MIN_DRAWN_FILL, top, left + width, top + height, BREWED_FLUID_EDGE_SHADOW);

		for (int y = top + BREWED_FLUID_RIPPLE_TOP_INSET; y < top + height; y += BREWED_FLUID_RIPPLE_STEP) {
			this.drawRect(left + MIN_DRAWN_FILL, y, left + width - MIN_DRAWN_FILL,
				Math.min(y + MIN_DRAWN_FILL, top + height), BREWED_FLUID_RIPPLE_COLOR);
		}
	}

	private String getAmountText() {
		return I18n.format("gui.ironagefurniture.foudre.millibuckets",
			Integer.toString(this.foudre.getFluidAmount()), Integer.toString(this.foudre.getCapacity()));
	}

	private String getBrewStatus() {
		if (this.foudre.getBrewTimeTotal() > 0 && !this.foudre.getBrewRecipeName().isEmpty()) {
			int percent = this.foudre.getBrewTime() * 100 / this.foudre.getBrewTimeTotal();
			return I18n.format("gui.ironagefurniture.foudre.brewing", this.foudre.getBrewRecipeName(),
				Integer.valueOf(percent));
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
}
