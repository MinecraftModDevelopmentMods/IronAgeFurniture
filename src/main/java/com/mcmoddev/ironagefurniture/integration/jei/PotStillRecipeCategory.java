package com.mcmoddev.ironagefurniture.integration.jei;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiFluidStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.resources.I18n;

public final class PotStillRecipeCategory extends BlankRecipeCategory<PotStillRecipeWrapper> {
	private static final int WIDTH = 150;
	private static final int HEIGHT = 105;
	private static final int INPUT_X = 7;
	private static final int OUTPUT_X = 127;
	private static final int FLUID_Y = 7;
	private static final int FLUID_WIDTH = 16;
	private static final int FLUID_HEIGHT = 48;
	private static final int FLUID_BORDER = 2;
	private static final int ARROW_LEFT = 50;
	private static final int ARROW_RIGHT = 100;
	private static final int ARROW_Y = 31;
	private static final int COLOR_DARK_WOOD = 0xFF3B2111;
	private static final int COLOR_BRASS = 0xFFD39B36;
	private static final int COLOR_GAUGE = 0xFF1F2633;

	private final IDrawable background;
	private final String title;

	public PotStillRecipeCategory(IGuiHelper guiHelper) {
		this.background = guiHelper.createBlankDrawable(WIDTH, HEIGHT);
		this.title = I18n.format("gui.ironagefurniture.jei.pot_still_distilling");
	}

	@Override
	public String getUid() {
		return IronAgeFurnitureJeiPlugin.POT_STILL_DISTILLING_UID;
	}

	@Override
	public String getTitle() {
		return this.title;
	}

	@Override
	public IDrawable getBackground() {
		return this.background;
	}

	@Override
	public void drawExtras(Minecraft minecraft) {
		this.drawGaugeFrame(INPUT_X);
		this.drawGaugeFrame(OUTPUT_X);
		Gui.drawRect(ARROW_LEFT, ARROW_Y - 1, ARROW_RIGHT - 4, ARROW_Y + 2, COLOR_BRASS);
		Gui.drawRect(ARROW_RIGHT - 8, ARROW_Y - 5, ARROW_RIGHT - 4, ARROW_Y + 6, COLOR_BRASS);
		Gui.drawRect(ARROW_RIGHT - 4, ARROW_Y - 3, ARROW_RIGHT, ARROW_Y + 4, COLOR_BRASS);
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, PotStillRecipeWrapper recipeWrapper,
			IIngredients ingredients) {
		IGuiFluidStackGroup fluids = recipeLayout.getFluidStacks();
		fluids.init(0, true, INPUT_X, FLUID_Y, FLUID_WIDTH, FLUID_HEIGHT,
			recipeWrapper.getFluidInputs().get(0).amount, true, null);
		fluids.init(1, false, OUTPUT_X, FLUID_Y, FLUID_WIDTH, FLUID_HEIGHT,
			recipeWrapper.getFluidOutputs().get(0).amount, true, null);
		fluids.set(ingredients);
	}

	private void drawGaugeFrame(int x) {
		Gui.drawRect(x - FLUID_BORDER, FLUID_Y - FLUID_BORDER, x + FLUID_WIDTH + FLUID_BORDER,
			FLUID_Y + FLUID_HEIGHT + FLUID_BORDER, COLOR_DARK_WOOD);
		Gui.drawRect(x, FLUID_Y, x + FLUID_WIDTH, FLUID_Y + FLUID_HEIGHT, COLOR_GAUGE);
	}
}
