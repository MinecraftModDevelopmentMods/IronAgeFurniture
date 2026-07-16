package com.mcmoddev.ironagefurniture.integration.jei;

import com.mcmoddev.ironagefurniture.api.tile.TileEntityFoudre;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiFluidStackGroup;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.resources.I18n;

public final class FoudreRecipeCategory extends BlankRecipeCategory<FoudreRecipeWrapper> {
	private static final int WIDTH = 150;
	private static final int HEIGHT = 105;
	private static final int INGREDIENT_X = 42;
	private static final int INGREDIENT_Y = 10;
	private static final int SLOT_STEP = 18;
	private static final int WATER_X = 7;
	private static final int OUTPUT_X = 127;
	private static final int FLUID_Y = 7;
	private static final int FLUID_WIDTH = 16;
	private static final int FLUID_HEIGHT = 48;
	private static final int FLUID_BORDER = 2;
	private static final int ARROW_LEFT = 84;
	private static final int ARROW_RIGHT = 113;
	private static final int ARROW_Y = 31;
	private static final int COLOR_DARK_WOOD = 0xFF3B2111;
	private static final int COLOR_BRASS = 0xFFD39B36;
	private static final int COLOR_GAUGE = 0xFF1F2633;

	private final IDrawable background;
	private final IDrawable slot;
	private final String title;

	public FoudreRecipeCategory(IGuiHelper guiHelper) {
		this.background = guiHelper.createBlankDrawable(WIDTH, HEIGHT);
		this.slot = guiHelper.getSlotDrawable();
		this.title = I18n.format("gui.ironagefurniture.jei.foudre_brewing");
	}

	@Override
	public String getUid() {
		return IronAgeFurnitureJeiPlugin.FOUDRE_BREWING_UID;
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
		this.slot.draw(minecraft, INGREDIENT_X, INGREDIENT_Y);
		this.slot.draw(minecraft, INGREDIENT_X + SLOT_STEP, INGREDIENT_Y);
		this.slot.draw(minecraft, INGREDIENT_X, INGREDIENT_Y + SLOT_STEP);
		this.slot.draw(minecraft, INGREDIENT_X + SLOT_STEP, INGREDIENT_Y + SLOT_STEP);
		this.drawGaugeFrame(WATER_X);
		this.drawGaugeFrame(OUTPUT_X);
		Gui.drawRect(ARROW_LEFT, ARROW_Y - 1, ARROW_RIGHT - 4, ARROW_Y + 2, COLOR_BRASS);
		Gui.drawRect(ARROW_RIGHT - 8, ARROW_Y - 5, ARROW_RIGHT - 4, ARROW_Y + 6, COLOR_BRASS);
		Gui.drawRect(ARROW_RIGHT - 4, ARROW_Y - 3, ARROW_RIGHT, ARROW_Y + 4, COLOR_BRASS);
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, FoudreRecipeWrapper recipeWrapper,
			IIngredients ingredients) {
		IGuiItemStackGroup itemStacks = recipeLayout.getItemStacks();
		itemStacks.init(0, true, INGREDIENT_X, INGREDIENT_Y);
		itemStacks.init(1, true, INGREDIENT_X + SLOT_STEP, INGREDIENT_Y);
		itemStacks.init(2, true, INGREDIENT_X, INGREDIENT_Y + SLOT_STEP);
		itemStacks.init(3, true, INGREDIENT_X + SLOT_STEP, INGREDIENT_Y + SLOT_STEP);
		itemStacks.set(ingredients);

		IGuiFluidStackGroup fluids = recipeLayout.getFluidStacks();
		fluids.init(0, true, WATER_X, FLUID_Y, FLUID_WIDTH, FLUID_HEIGHT,
			TileEntityFoudre.CAPACITY, true, null);
		fluids.init(1, false, OUTPUT_X, FLUID_Y, FLUID_WIDTH, FLUID_HEIGHT,
			TileEntityFoudre.CAPACITY, true, null);
		fluids.set(ingredients);
	}

	private void drawGaugeFrame(int x) {
		Gui.drawRect(x - FLUID_BORDER, FLUID_Y - FLUID_BORDER, x + FLUID_WIDTH + FLUID_BORDER,
			FLUID_Y + FLUID_HEIGHT + FLUID_BORDER, COLOR_DARK_WOOD);
		Gui.drawRect(x, FLUID_Y, x + FLUID_WIDTH, FLUID_Y + FLUID_HEIGHT, COLOR_GAUGE);
	}
}
