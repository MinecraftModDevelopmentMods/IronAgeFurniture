package com.mcmoddev.ironagefurniture.init;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class RecipeInitialiser {
	protected RecipeInitialiser() {
		throw new IllegalAccessError("This class cannot be instansiated");
	}

	/**
	 *
	 */
	public static void init() {
		generateChairRecipes();
	}
	
	private static void generateChairRecipes() {
		GameRegistry.addRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_classic_oak, 1), "*  ", "***", "* *", '*', new ItemStack(Blocks.PLANKS, 1, 0));
		GameRegistry.addRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_classic_spruce, 1), "*  ", "***", "* *", '*', new ItemStack(Blocks.PLANKS, 1, 1));
		GameRegistry.addRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_classic_birch, 1), "*  ", "***", "* *", '*', new ItemStack(Blocks.PLANKS, 1, 2));
		GameRegistry.addRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_classic_jungle, 1), "*  ", "***", "* *", '*', new ItemStack(Blocks.PLANKS, 1, 3));
		GameRegistry.addRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_classic_acacia, 1), "*  ", "***", "* *", '*', new ItemStack(Blocks.PLANKS, 1, 4));
		GameRegistry.addRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_classic_big_oak, 1), "*  ", "***", "* *", '*', new ItemStack(Blocks.PLANKS, 1, 5));
	
	}
}
