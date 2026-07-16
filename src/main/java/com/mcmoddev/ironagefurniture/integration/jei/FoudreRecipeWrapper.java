package com.mcmoddev.ironagefurniture.integration.jei;

import java.util.Collections;
import java.util.List;

import com.mcmoddev.ironagefurniture.api.FoudreBrewingRegistry.FoudreBrewingRecipe;
import com.mcmoddev.ironagefurniture.api.tile.TileEntityFoudre;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public final class FoudreRecipeWrapper extends BlankRecipeWrapper {
	private static final int RECIPE_WIDTH = 150;
	private static final int NAME_Y = 61;
	private static final int TIME_Y = 72;
	private static final int TICKS_PER_SECOND = 20;
	private static final int SECONDS_PER_MINUTE = 60;

	private final FoudreBrewingRecipe recipe;
	private final List<ItemStack> ingredients;
	private final FluidStack water;
	private final FluidStack output;

	public FoudreRecipeWrapper(FoudreBrewingRecipe recipe) {
		this.recipe = recipe;
		this.ingredients = recipe.getIngredientStacks();
		this.water = new FluidStack(FluidRegistry.WATER, TileEntityFoudre.CAPACITY);
		this.output = new FluidStack(recipe.getOutput(), TileEntityFoudre.CAPACITY);
	}

	@Override
	public void getIngredients(IIngredients recipeIngredients) {
		recipeIngredients.setInputs(ItemStack.class, this.ingredients);
		recipeIngredients.setInput(FluidStack.class, this.water);
		recipeIngredients.setOutput(FluidStack.class, this.output);
	}

	@Override
	public List<ItemStack> getInputs() {
		return this.ingredients;
	}

	@Override
	public List<ItemStack> getOutputs() {
		return Collections.emptyList();
	}

	@Override
	public List<FluidStack> getFluidInputs() {
		return Collections.singletonList(this.water);
	}

	@Override
	public List<FluidStack> getFluidOutputs() {
		return Collections.singletonList(this.output);
	}

	@Override
	public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
		this.drawCenteredTrimmed(minecraft, this.recipe.getDisplayName(), NAME_Y);
		String brewTime = I18n.format("gui.ironagefurniture.jei.brew_time", this.getFormattedBrewTime());
		this.drawCenteredTrimmed(minecraft, brewTime, TIME_Y);
	}

	private void drawCenteredTrimmed(Minecraft minecraft, String text, int y) {
		String fitted = minecraft.fontRendererObj.trimStringToWidth(text, RECIPE_WIDTH);
		int x = (RECIPE_WIDTH - minecraft.fontRendererObj.getStringWidth(fitted)) / 2;
		minecraft.fontRendererObj.drawString(fitted, x, y, 0x404040);
	}

	private String getFormattedBrewTime() {
		int seconds = Math.max(1, (this.recipe.getBrewTime() + TICKS_PER_SECOND - 1) / TICKS_PER_SECOND);
		int minutes = seconds / SECONDS_PER_MINUTE;
		int remainingSeconds = seconds % SECONDS_PER_MINUTE;

		if (minutes <= 0) {
			return Integer.toString(seconds) + " sec";
		}

		if (remainingSeconds <= 0) {
			return Integer.toString(minutes) + " min";
		}

		return Integer.toString(minutes) + " min " + Integer.toString(remainingSeconds) + " sec";
	}
}
