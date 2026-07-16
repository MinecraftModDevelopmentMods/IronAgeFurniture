package com.mcmoddev.ironagefurniture.integration.jei;

import com.mcmoddev.ironagefurniture.api.PotStillDistillingRegistry.DistillationJeiRecipe;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

public final class PotStillRecipeHandler implements IRecipeHandler<DistillationJeiRecipe> {
	@Override
	public Class<DistillationJeiRecipe> getRecipeClass() {
		return DistillationJeiRecipe.class;
	}

	@Override
	public String getRecipeCategoryUid() {
		return IronAgeFurnitureJeiPlugin.POT_STILL_DISTILLING_UID;
	}

	@Override
	public String getRecipeCategoryUid(DistillationJeiRecipe recipe) {
		return this.getRecipeCategoryUid();
	}

	@Override
	public IRecipeWrapper getRecipeWrapper(DistillationJeiRecipe recipe) {
		return new PotStillRecipeWrapper(recipe);
	}

	@Override
	public boolean isRecipeValid(DistillationJeiRecipe recipe) {
		return recipe != null && recipe.getInput().getFluid() != null && recipe.getOutput().getFluid() != null;
	}
}
