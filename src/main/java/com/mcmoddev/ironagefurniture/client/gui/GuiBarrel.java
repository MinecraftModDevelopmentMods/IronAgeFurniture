package com.mcmoddev.ironagefurniture.client.gui;

import com.mcmoddev.ironagefurniture.api.container.ContainerBarrel;
import com.mcmoddev.ironagefurniture.api.tile.TileEntityBarrel;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class GuiBarrel extends GuiContainer {
	private static final int GAUGE_X = 80;
	private static final int GAUGE_Y = 28;
	private static final int GAUGE_WIDTH = 16;
	private static final int GAUGE_HEIGHT = 64;

	private final TileEntityBarrel barrel;

	public GuiBarrel(TileEntityBarrel barrel) {
		super(new ContainerBarrel(barrel));
		this.barrel = barrel;
		this.xSize = 176;
		this.ySize = 120;
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		String title = I18n.format("container.ironagefurniture.barrel");
		this.fontRendererObj.drawString(title, (this.xSize - this.fontRendererObj.getStringWidth(title)) / 2, 8,
			0x404040);

		FluidStack fluid = this.barrel.getFluid();
		String fluidName = fluid == null ? I18n.format("gui.ironagefurniture.barrel.empty")
			: fluid.getLocalizedName();
		this.fontRendererObj.drawString(fluidName,
			(this.xSize - this.fontRendererObj.getStringWidth(fluidName)) / 2, 96, 0x404040);

		String amount = this.getAmountText();
		this.fontRendererObj.drawString(amount,
			(this.xSize - this.fontRendererObj.getStringWidth(amount)) / 2, 108, 0x404040);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		this.drawRect(this.guiLeft, this.guiTop, this.guiLeft + this.xSize, this.guiTop + this.ySize, 0xFFC6C6C6);
		this.drawRect(this.guiLeft + 4, this.guiTop + 4, this.guiLeft + this.xSize - 4,
			this.guiTop + this.ySize - 4, 0xFFE9E9E9);
		this.drawGauge();
	}

	private void drawGauge() {
		int left = this.guiLeft + GAUGE_X;
		int top = this.guiTop + GAUGE_Y;
		this.drawRect(left - 2, top - 2, left + GAUGE_WIDTH + 2, top + GAUGE_HEIGHT + 2, 0xFF404040);
		this.drawRect(left, top, left + GAUGE_WIDTH, top + GAUGE_HEIGHT, 0xFF1F2633);

		FluidStack fluid = this.barrel.getFluid();

		if (fluid == null || fluid.amount <= 0 || fluid.getFluid() == null) {
			return;
		}

		int fillHeight = Math.max(1, fluid.amount * GAUGE_HEIGHT / this.barrel.getCapacity());
		int fillTop = top + GAUGE_HEIGHT - fillHeight;
		this.drawFluid(left, fillTop, GAUGE_WIDTH, fillHeight, fluid);
		this.drawRect(left, fillTop, left + GAUGE_WIDTH, fillTop + 1, 0x80FFFFFF);
	}

	private void drawFluid(int left, int top, int width, int height, FluidStack fluid) {
		ResourceLocation still = fluid.getFluid().getStill(fluid);

		if (still == null) {
			this.drawRect(left, top, left + width, top + height, this.getFluidColor(fluid));
			return;
		}

		TextureAtlasSprite sprite = this.mc.getTextureMapBlocks().getAtlasSprite(still.toString());
		int color = this.getFluidColor(fluid);
		float red = (float)(color >> 16 & 255) / 255.0F;
		float green = (float)(color >> 8 & 255) / 255.0F;
		float blue = (float)(color & 255) / 255.0F;

		this.mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		GlStateManager.color(red, green, blue, 1.0F);

		for (int drawn = 0; drawn < height; drawn += 16) {
			int segmentHeight = Math.min(16, height - drawn);
			int segmentTop = top + height - drawn - segmentHeight;
			this.drawTexturedModalRect(left, segmentTop, sprite, width, segmentHeight);
		}

		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
	}

	private String getAmountText() {
		int bucketAmount = this.barrel.getFluidAmount() / Fluid.BUCKET_VOLUME;
		int bucketCapacity = this.barrel.getCapacity() / Fluid.BUCKET_VOLUME;
		return I18n.format("gui.ironagefurniture.barrel.buckets", Integer.valueOf(bucketAmount),
			Integer.valueOf(bucketCapacity));
	}

	private int getFluidColor(FluidStack fluid) {
		int color = fluid.getFluid().getColor(fluid);
		return (color & 0xFF000000) == 0 ? color | 0xFF000000 : color;
	}
}
