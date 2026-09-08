package zone.moddev.mc.ironagefurniture.integration.jei;

import zone.moddev.mc.ironagefurniture.api.FoudreBrewingRegistry.FoudreBrewingRecipe;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

public final class FoudreRecipeHandler implements IRecipeHandler<FoudreBrewingRecipe> {
	@Override
	public Class<FoudreBrewingRecipe> getRecipeClass() {
		return FoudreBrewingRecipe.class;
	}

	@Override
	public String getRecipeCategoryUid() {
		return IronAgeFurnitureJeiPlugin.FOUDRE_BREWING_UID;
	}

	@Override
	public String getRecipeCategoryUid(FoudreBrewingRecipe recipe) {
		return this.getRecipeCategoryUid();
	}

	@Override
	public IRecipeWrapper getRecipeWrapper(FoudreBrewingRecipe recipe) {
		return new FoudreRecipeWrapper(recipe);
	}

	@Override
	public boolean isRecipeValid(FoudreBrewingRecipe recipe) {
		return recipe != null && recipe.getOutput() != null && !recipe.getIngredientStacks().isEmpty();
	}
}
