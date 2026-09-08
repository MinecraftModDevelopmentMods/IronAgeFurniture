package zone.moddev.mc.ironagefurniture.client.gui;

import java.io.IOException;

import zone.moddev.mc.ironagefurniture.api.container.ContainerBarrel;
import zone.moddev.mc.ironagefurniture.api.DrinkDisplayHelper;
import zone.moddev.mc.ironagefurniture.api.network.BarrelSealMessage;
import zone.moddev.mc.ironagefurniture.api.network.IronAgeFurnitureNetwork;
import zone.moddev.mc.ironagefurniture.api.tile.TileEntityBarrel;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

public class GuiBarrel extends GuiContainer {
	private static final int GUI_WIDTH = 176;
	private static final int GUI_HEIGHT = 132;
	private static final int TEXT_TRIM_PADDING = 24;
	private static final int GAUGE_X = 80;
	private static final int GAUGE_Y = 36;
	private static final int GAUGE_WIDTH = 16;
	private static final int GAUGE_HEIGHT = 64;
	private static final int TOP_PANEL_X = 9;
	private static final int TOP_PANEL_Y = 7;
	private static final int TOP_PANEL_WIDTH = 158;
	private static final int TOP_PANEL_HEIGHT = 118;
	private static final int TITLE_Y = 12;
	private static final int STATUS_Y = 24;
	private static final int FLUID_NAME_Y = 103;
	private static final int AMOUNT_Y = 115;
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
	private static final int GAUGE_BORDER = 2;
	private static final int COLOR_CHANNEL_MAX = 255;
	private static final int FULL_ALPHA_MASK = 0xFF000000;
	private static final int MIN_DRAWN_FILL = 1;
	private static final int TEXTURE_TILE_SIZE = 16;
	private static final int BREWED_FLUID_RIPPLE_COLOR = 0x28FFFFFF;
	private static final int BREWED_FLUID_EDGE_SHADOW = 0x30000000;
	private static final int BREWED_FLUID_RIPPLE_TOP_INSET = 3;
	private static final int BREWED_FLUID_RIPPLE_STEP = 8;
	private static final float FULL_COLOR = 1.0F;
	private static final int SEAL_BUTTON_ID = 0;
	private static final int SEAL_BUTTON_X = 112;
	private static final int SEAL_BUTTON_Y = 56;
	private static final int SEAL_BUTTON_WIDTH = 48;
	private static final int SEAL_BUTTON_HEIGHT = 20;
	private static final int WOOD_DARK = 0xFF2E170A;
	private static final int WOOD_MID = 0xFF6B3A1B;
	private static final int WOOD_LIGHT = 0xFF9A6230;
	private static final int PANEL_DARK = 0xFF5A3219;
	private static final int PANEL_LIGHT = 0xFFD4B789;
	private static final int PANEL_MID = 0xFFC19761;
	private static final int TEXT_DARK = 0xFF2F1C10;
	private static final int WOOD_GRAIN_DARK = 0x286B3A1B;
	private static final int WOOD_GRAIN_LIGHT = 0x289A6230;
	private static final int GAUGE_BORDER_COLOR = 0xFF404040;
	private static final int GAUGE_EMPTY_COLOR = 0xFF1F2633;
	private static final int HIGHLIGHT_COLOR = 0x80FFFFFF;

	private final TileEntityBarrel barrel;
	private GuiButton sealButton;

	public GuiBarrel(TileEntityBarrel barrel) {
		super(new ContainerBarrel(barrel));
		this.barrel = barrel;
		this.xSize = GUI_WIDTH;
		this.ySize = GUI_HEIGHT;
	}

	@Override
	public void initGui() {
		super.initGui();
		this.sealButton = new GuiButton(SEAL_BUTTON_ID, this.guiLeft + SEAL_BUTTON_X,
			this.guiTop + SEAL_BUTTON_Y, SEAL_BUTTON_WIDTH, SEAL_BUTTON_HEIGHT, this.getSealButtonText());
		this.buttonList.add(this.sealButton);
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
		if (button.id == SEAL_BUTTON_ID && button.enabled) {
			IronAgeFurnitureNetwork.channel.sendToServer(new BarrelSealMessage(this.barrel.getPos(),
				!this.barrel.isSealed()));
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		String title = I18n.format(this.barrel.getContainerNameKey());
		this.drawCenteredText(title, TITLE_Y);

		String status = this.getStatusText();
		this.drawCenteredText(status, STATUS_Y);

		FluidStack fluid = this.barrel.getFluid();
		String fluidName = fluid == null ? I18n.format("gui.ironagefurniture.barrel.empty")
			: DrinkDisplayHelper.getDisplayName(fluid);
		this.drawCenteredText(fluidName, FLUID_NAME_Y);

		String amount = this.getAmountText();
		this.drawCenteredText(amount, AMOUNT_Y);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		this.drawWoodFrame();
		this.drawInsetPanel(TOP_PANEL_X, TOP_PANEL_Y, TOP_PANEL_WIDTH, TOP_PANEL_HEIGHT);
		this.drawGauge();
	}

	private void drawWoodFrame() {
		Gui.drawRect(this.guiLeft, this.guiTop, this.guiLeft + this.xSize, this.guiTop + this.ySize, WOOD_DARK);
		Gui.drawRect(this.guiLeft + FRAME_OUTER_INSET, this.guiTop + FRAME_OUTER_INSET,
			this.guiLeft + this.xSize - FRAME_OUTER_INSET, this.guiTop + this.ySize - FRAME_OUTER_INSET, WOOD_MID);
		Gui.drawRect(this.guiLeft + FRAME_INNER_INSET, this.guiTop + FRAME_INNER_INSET,
			this.guiLeft + this.xSize - FRAME_INNER_INSET, this.guiTop + this.ySize - FRAME_INNER_INSET,
			WOOD_LIGHT);

		for (int y = GRAIN_TOP; y < this.ySize - GRAIN_BOTTOM_INSET; y += GRAIN_STEP) {
			int grain = y % GRAIN_VARIANT_DIVISOR == GRAIN_VARIANT_OFFSET ? WOOD_GRAIN_DARK : WOOD_GRAIN_LIGHT;
			Gui.drawRect(this.guiLeft + GRAIN_LEFT_INSET, this.guiTop + y,
				this.guiLeft + this.xSize - GRAIN_LEFT_INSET, this.guiTop + y + MIN_DRAWN_FILL, grain);
		}
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
		if (this.sealButton == null) {
			return;
		}

		this.sealButton.displayString = this.getSealButtonText();
		this.sealButton.enabled = this.barrel.isSealed() || this.barrel.canSeal();
	}

	private void drawGauge() {
		int left = this.guiLeft + GAUGE_X;
		int top = this.guiTop + GAUGE_Y;
		Gui.drawRect(left - GAUGE_BORDER, top - GAUGE_BORDER, left + GAUGE_WIDTH + GAUGE_BORDER,
			top + GAUGE_HEIGHT + GAUGE_BORDER, GAUGE_BORDER_COLOR);
		Gui.drawRect(left, top, left + GAUGE_WIDTH, top + GAUGE_HEIGHT, GAUGE_EMPTY_COLOR);

		FluidStack fluid = this.barrel.getFluid();

		if (fluid == null || fluid.amount <= 0 || fluid.getFluid() == null) {
			return;
		}

		int fillHeight = Math.max(1, fluid.amount * GAUGE_HEIGHT / this.barrel.getCapacity());
		int fillTop = top + GAUGE_HEIGHT - fillHeight;
		this.drawFluid(left, fillTop, GAUGE_WIDTH, fillHeight, fluid);
		Gui.drawRect(left, fillTop, left + GAUGE_WIDTH, fillTop + MIN_DRAWN_FILL, HIGHLIGHT_COLOR);
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
		return I18n.format("gui.ironagefurniture.barrel.millibuckets",
			Integer.toString(this.barrel.getFluidAmount()), Integer.toString(this.barrel.getCapacity()));
	}

	private String getStatusText() {
		if (!this.barrel.isSealed() || this.barrel.getAgeProgressTotal() <= 0
				|| this.barrel.getNextAgeLevelName().isEmpty()) {
			return I18n.format(this.barrel.isSealed() ? "gui.ironagefurniture.barrel.sealed"
				: "gui.ironagefurniture.barrel.open_state");
		}

		int percent = this.barrel.getAgeProgress() * 100 / this.barrel.getAgeProgressTotal();
		return I18n.format("gui.ironagefurniture.barrel.aging", this.barrel.getNextAgeLevelName(),
			Integer.valueOf(percent));
	}

	private String getSealButtonText() {
		return I18n.format(this.barrel.isSealed() ? "gui.ironagefurniture.barrel.open"
			: "gui.ironagefurniture.barrel.seal");
	}

	private int getFluidColor(FluidStack fluid) {
		int color = fluid.getFluid().getColor(fluid);
		return (color & FULL_ALPHA_MASK) == 0 ? color | FULL_ALPHA_MASK : color;
	}

	private void drawCenteredText(String text, int y) {
		String trimmed = this.fontRendererObj.trimStringToWidth(text, this.xSize - TEXT_TRIM_PADDING);
		this.fontRendererObj.drawString(trimmed,
			(this.xSize - this.fontRendererObj.getStringWidth(trimmed)) / 2, y, TEXT_DARK);
	}
}
