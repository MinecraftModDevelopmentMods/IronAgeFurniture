package com.mcmoddev.ironagefurniture.init;

import com.mcmoddev.ironagefurniture.IronAgeFurnitureConfiguration;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
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
		if (IronAgeFurnitureConfiguration.GENERATE_CLASSIC_CHAIRS) {
			GameRegistry.addRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_classic_oak, 1), "*  ", "***", "* *", '*', new ItemStack(Blocks.PLANKS, 1, 0));
			GameRegistry.addRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_classic_spruce, 1), "*  ", "***", "* *", '*', new ItemStack(Blocks.PLANKS, 1, 1));
			GameRegistry.addRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_classic_birch, 1), "*  ", "***", "* *", '*', new ItemStack(Blocks.PLANKS, 1, 2));
			GameRegistry.addRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_classic_jungle, 1), "*  ", "***", "* *", '*', new ItemStack(Blocks.PLANKS, 1, 3));
			GameRegistry.addRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_classic_acacia, 1), "*  ", "***", "* *", '*', new ItemStack(Blocks.PLANKS, 1, 4));
			GameRegistry.addRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_classic_big_oak, 1), "*  ", "***", "* *", '*', new ItemStack(Blocks.PLANKS, 1, 5));
		}
		
		if (IronAgeFurnitureConfiguration.GENERATE_SHIELD_CHAIRS) {
					
		}
		
		if (IronAgeFurnitureConfiguration.GENERATE_SHORT_STOOLS) {
			
		}
		
		if (IronAgeFurnitureConfiguration.GENERATE_TALL_STOOLS) {
			
		}
		
		if (IronAgeFurnitureConfiguration.INTEGRATION_BIOMESOPLENTY && Loader.isModLoaded("BiomesOPlenty")) {
			if (IronAgeFurnitureConfiguration.GENERATE_CLASSIC_CHAIRS) {
				ItemStack SACRED_OAK = new ItemStack(Block.getBlockFromName("BiomesOPlenty:planks_0"), 1, 0);
				ItemStack CHERRY = new ItemStack(Block.getBlockFromName("BiomesOPlenty:planks_0"), 1, 1);
				ItemStack UMBRAN = new ItemStack(Block.getBlockFromName("BiomesOPlenty:planks_0"), 1, 2);
				ItemStack FIR = new ItemStack(Block.getBlockFromName("BiomesOPlenty:planks_0"), 1, 3);
				ItemStack ETHEREAL = new ItemStack(Block.getBlockFromName("BiomesOPlenty:planks_0"), 1, 4);
				ItemStack MAGIC = new ItemStack(Block.getBlockFromName("BiomesOPlenty:planks_0"), 1, 5);
				ItemStack MANGROVE = new ItemStack(Block.getBlockFromName("BiomesOPlenty:planks_0"), 1, 6);
				ItemStack PALM = new ItemStack(Block.getBlockFromName("BiomesOPlenty:planks_0"), 1, 7);
				ItemStack REDWOOD = new ItemStack(Block.getBlockFromName("BiomesOPlenty:planks_0"), 1, 8);
				ItemStack WILLOW = new ItemStack(Block.getBlockFromName("BiomesOPlenty:planks_0"), 1, 9);
				ItemStack PINE = new ItemStack(Block.getBlockFromName("BiomesOPlenty:planks_0"), 1, 10);
				ItemStack HELLBARK = new ItemStack(Block.getBlockFromName("BiomesOPlenty:planks_0"), 1, 11);
				ItemStack JACARANDA = new ItemStack(Block.getBlockFromName("BiomesOPlenty:planks_0"), 1, 12);
				ItemStack MAHOGANY = new ItemStack(Block.getBlockFromName("BiomesOPlenty:planks_0"), 1, 13);
				ItemStack EBONY = new ItemStack(Block.getBlockFromName("BiomesOPlenty:planks_0"), 1, 14);
				ItemStack EUCALYPTUS = new ItemStack(Block.getBlockFromName("BiomesOPlenty:planks_0"), 1, 15);
				
				GameRegistry.addRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_classic_biomesoplenty_cherry, 1), "*  ", "***", "* *", '*', CHERRY);
				GameRegistry.addRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_classic_biomesoplenty_ebony, 1), "*  ", "***", "* *", '*', EBONY);
				GameRegistry.addRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_classic_biomesoplenty_ethereal, 1), "*  ", "***", "* *", '*', ETHEREAL);
				GameRegistry.addRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_classic_biomesoplenty_eucalyptus, 1), "*  ", "***", "* *", '*', EUCALYPTUS);
				GameRegistry.addRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_classic_biomesoplenty_fir, 1), "*  ", "***", "* *", '*', FIR);
				GameRegistry.addRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_classic_biomesoplenty_hellbark, 1), "*  ", "***", "* *", '*', HELLBARK);
				GameRegistry.addRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_classic_biomesoplenty_jacaranda, 1), "*  ", "***", "* *", '*', JACARANDA);
				GameRegistry.addRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_classic_biomesoplenty_magic, 1), "*  ", "***", "* *", '*', MAGIC);
				GameRegistry.addRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_classic_biomesoplenty_mahogany, 1), "*  ", "***", "* *", '*', MAHOGANY);
				GameRegistry.addRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_classic_biomesoplenty_mangrove, 1), "*  ", "***", "* *", '*', MANGROVE);
				GameRegistry.addRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_classic_biomesoplenty_palm, 1), "*  ", "***", "* *", '*', PALM);
				GameRegistry.addRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_classic_biomesoplenty_pine, 1), "*  ", "***", "* *", '*', PINE);
				GameRegistry.addRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_classic_biomesoplenty_redwood, 1), "*  ", "***", "* *", '*', REDWOOD);
				GameRegistry.addRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_classic_biomesoplenty_sacred_oak, 1), "*  ", "***", "* *", '*', SACRED_OAK);
				GameRegistry.addRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_classic_biomesoplenty_umbran, 1), "*  ", "***", "* *", '*', UMBRAN);
				GameRegistry.addRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_classic_biomesoplenty_willow, 1), "*  ", "***", "* *", '*', WILLOW);
			}
			
			if (IronAgeFurnitureConfiguration.GENERATE_SHIELD_CHAIRS) {
				
			}
			
			if (IronAgeFurnitureConfiguration.GENERATE_SHORT_STOOLS) {
				
			}
			
			if (IronAgeFurnitureConfiguration.GENERATE_TALL_STOOLS) {
				
			}
		}
		
		if (IronAgeFurnitureConfiguration.INTEGRATION_NATURA && Loader.isModLoaded("natura")) {
			if (IronAgeFurnitureConfiguration.GENERATE_CLASSIC_CHAIRS) {
				
			}
			
			if (IronAgeFurnitureConfiguration.GENERATE_SHIELD_CHAIRS) {
				
			}
			
			if (IronAgeFurnitureConfiguration.GENERATE_SHORT_STOOLS) {
				
			}
			
			if (IronAgeFurnitureConfiguration.GENERATE_TALL_STOOLS) {
				
			}
		}
	}
}
