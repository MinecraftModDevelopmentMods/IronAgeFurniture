package com.mcmoddev.ironagefurniture.integration.jei;

import java.util.ArrayList;

import com.mcmoddev.ironagefurniture.BlockObjectHolder;
import com.mcmoddev.ironagefurniture.Ironagefurniture;
import com.mcmoddev.ironagefurniture.api.FoudreBrewingRegistry;
import com.mcmoddev.ironagefurniture.api.container.ContainerFoudre;
import com.mcmoddev.ironagefurniture.api.tile.TileEntityFoudre;
import com.mcmoddev.ironagefurniture.client.gui.GuiFoudre;

import mezz.jei.api.BlankModPlugin;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.transfer.IRecipeTransferRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

@JEIPlugin
public final class IronAgeFurnitureJeiPlugin extends BlankModPlugin {
	public static final String FOUDRE_BREWING_UID = Ironagefurniture.MODID + ".foudre_brewing";

	@Override
	public void register(IModRegistry registry) {
		IGuiHelper guiHelper = registry.getJeiHelpers().getGuiHelper();
		registry.addRecipeCategories(new FoudreRecipeCategory(guiHelper));
		registry.addRecipeHandlers(new FoudreRecipeHandler());
		registry.addRecipes(new ArrayList<FoudreBrewingRegistry.FoudreBrewingRecipe>(
			FoudreBrewingRegistry.getRecipes()));
		registry.addRecipeClickArea(GuiFoudre.class, GuiFoudre.RECIPE_CLICK_X, GuiFoudre.RECIPE_CLICK_Y,
			GuiFoudre.RECIPE_CLICK_WIDTH, GuiFoudre.RECIPE_CLICK_HEIGHT, FOUDRE_BREWING_UID);
		registry.addAdvancedGuiHandlers(new FoudreGuiHandler());

		IRecipeTransferRegistry transferRegistry = registry.getRecipeTransferRegistry();
		transferRegistry.addRecipeTransferHandler(ContainerFoudre.class, FOUDRE_BREWING_UID, 0,
			TileEntityFoudre.INGREDIENT_SLOTS, TileEntityFoudre.INGREDIENT_SLOTS, 36);

		for (Block foudre : BlockObjectHolder.foudre_wood_ironage.values()) {
			if (foudre != null) {
				registry.addRecipeCategoryCraftingItem(new ItemStack(foudre), FOUDRE_BREWING_UID);
			}
		}
	}
}
