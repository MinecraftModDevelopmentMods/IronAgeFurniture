package com.mcmoddev.ironagefurniture.init;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mcmoddev.ironagefurniture.IronAgeFurnitureConfiguration;
import com.mcmoddev.ironagefurniture.Ironagefurniture;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class RecipeInitialiser {
	private static Logger logger;
	
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
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_classic_oak, 1), "x  ", "xxx", "y y", 'x', new ItemStack(Blocks.PLANKS, 1, 0), 'y', "stickWood"));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_classic_spruce, 1), "x  ", "xxx", "y y", 'x', new ItemStack(Blocks.PLANKS, 1, 1), 'y', "stickWood"));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_classic_birch, 1), "x  ", "xxx", "y y", 'x', new ItemStack(Blocks.PLANKS, 1, 2), 'y', "stickWood"));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_classic_jungle, 1), "x  ", "xxx", "y y", 'x', new ItemStack(Blocks.PLANKS, 1, 3), 'y', "stickWood"));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_classic_acacia, 1), "x  ", "xxx", "y y", 'x', new ItemStack(Blocks.PLANKS, 1, 4), 'y', "stickWood"));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_classic_big_oak, 1), "x  ", "xxx", "y y", 'x', new ItemStack(Blocks.PLANKS, 1, 5), 'y', "stickWood"));
		}
		
		if (IronAgeFurnitureConfiguration.GENERATE_SHIELD_CHAIRS) {
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_shield_oak, 1), new ItemStack(BlockInitialiser.chair_wood_ironage_classic_oak,1), new ItemStack(Items.SHIELD,1)));
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_shield_spruce, 1), new ItemStack(BlockInitialiser.chair_wood_ironage_classic_oak,1), new ItemStack(Items.SHIELD,1)));
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_shield_birch, 1), new ItemStack(BlockInitialiser.chair_wood_ironage_classic_birch,1), new ItemStack(Items.SHIELD,1)));
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_shield_jungle, 1), new ItemStack(BlockInitialiser.chair_wood_ironage_classic_jungle,1), new ItemStack(Items.SHIELD,1)));
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_shield_acacia, 1), new ItemStack(BlockInitialiser.chair_wood_ironage_classic_acacia,1), new ItemStack(Items.SHIELD,1)));
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_shield_big_oak, 1), new ItemStack(BlockInitialiser.chair_wood_ironage_classic_big_oak,1), new ItemStack(Items.SHIELD,1)));
		}
		
		if (IronAgeFurnitureConfiguration.GENERATE_SHORT_STOOLS) {
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_stool_short_oak, 1), " x ", "yyy", 'x', new ItemStack(Blocks.PLANKS, 1, 0), 'y', "stickWood"));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_stool_short_spruce, 1), " x ", "yyy", 'x', new ItemStack(Blocks.PLANKS, 1, 1), 'y', "stickWood"));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_stool_short_birch, 1), " x ", "yyy", 'x', new ItemStack(Blocks.PLANKS, 1, 2), 'y', "stickWood"));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_stool_short_jungle, 1), " x ", "yyy", 'x', new ItemStack(Blocks.PLANKS, 1, 3), 'y', "stickWood"));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_stool_short_acacia, 1), " x ", "yyy", 'x', new ItemStack(Blocks.PLANKS, 1, 4), 'y', "stickWood"));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_stool_short_big_oak, 1), " x ", "yyy", 'x', new ItemStack(Blocks.PLANKS, 1, 5), 'y', "stickWood"));
		}
		
		if (IronAgeFurnitureConfiguration.GENERATE_TALL_STOOLS) {
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_stool_tall_oak, 1), " x ", "yyy","yyy", 'x', new ItemStack(Blocks.PLANKS, 1, 0), 'y', "stickWood"));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_stool_tall_spruce, 1), " x ", "yyy","yyy", 'x', new ItemStack(Blocks.PLANKS, 1, 1), 'y', "stickWood"));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_stool_tall_birch, 1), " x ", "yyy","yyy", 'x', new ItemStack(Blocks.PLANKS, 1, 2), 'y', "stickWood"));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_stool_tall_jungle, 1), " x ", "yyy","yyy", 'x', new ItemStack(Blocks.PLANKS, 1, 3), 'y', "stickWood"));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_stool_tall_acacia, 1), " x ", "yyy","yyy", 'x', new ItemStack(Blocks.PLANKS, 1, 4), 'y', "stickWood"));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_stool_tall_big_oak, 1), " x ", "yyy","yyy", 'x', new ItemStack(Blocks.PLANKS, 1, 5), 'y', "stickWood"));
		}
		
		if (IronAgeFurnitureConfiguration.INTEGRATION_BIOMESOPLENTY && Loader.isModLoaded("BiomesOPlenty")) {
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
			
			if (IronAgeFurnitureConfiguration.GENERATE_CLASSIC_CHAIRS) {
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_classic_biomesoplenty_cherry, 1), "x  ", "xxx", "y y", 'x', CHERRY, 'y', "stickWood"));
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_classic_biomesoplenty_ebony, 1), "x  ", "xxx", "y y", 'x', EBONY, 'y', "stickWood"));
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_classic_biomesoplenty_ethereal, 1), "x  ", "xxx", "y y", 'x', ETHEREAL, 'y', "stickWood"));
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_classic_biomesoplenty_eucalyptus, 1), "x  ", "xxx", "y y", 'x', EUCALYPTUS, 'y', "stickWood"));
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_classic_biomesoplenty_fir, 1), "x  ", "xxx", "y y", 'x', FIR, 'y', "stickWood"));
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_classic_biomesoplenty_hellbark, 1), "x  ", "xxx", "y y", 'x', HELLBARK, 'y', "stickWood"));
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_classic_biomesoplenty_jacaranda, 1), "x  ", "xxx", "y y", 'x', JACARANDA, 'y', "stickWood"));
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_classic_biomesoplenty_magic, 1), "x  ", "xxx", "y y", 'x', MAGIC, 'y', "stickWood"));
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_classic_biomesoplenty_mahogany, 1), "x  ", "xxx", "y y", 'x', MAHOGANY, 'y', "stickWood"));
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_classic_biomesoplenty_mangrove, 1), "x  ", "xxx", "y y", 'x', MANGROVE, 'y', "stickWood"));
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_classic_biomesoplenty_palm, 1), "x  ", "xxx", "y y", 'x', PALM, 'y', "stickWood"));
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_classic_biomesoplenty_pine, 1), "x  ", "xxx", "y y", 'x', PINE, 'y', "stickWood"));
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_classic_biomesoplenty_redwood, 1), "x  ", "xxx", "y y", 'x', REDWOOD, 'y', "stickWood"));
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_classic_biomesoplenty_sacred_oak, 1), "x  ", "xxx", "y y", 'x', SACRED_OAK, 'y', "stickWood"));
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_classic_biomesoplenty_umbran, 1), "x  ", "xxx", "y y", 'x', UMBRAN, 'y', "stickWood"));
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_classic_biomesoplenty_willow, 1), "x  ", "xxx", "y y", 'x', WILLOW, 'y', "stickWood"));
			}
			
			if (IronAgeFurnitureConfiguration.GENERATE_SHIELD_CHAIRS) {
				GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_shield_biomesoplenty_cherry, 1), new ItemStack(BlockInitialiser.chair_wood_ironage_classic_biomesoplenty_cherry,1), new ItemStack(Items.SHIELD,1)));
				GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_shield_biomesoplenty_ebony, 1), new ItemStack(BlockInitialiser.chair_wood_ironage_classic_biomesoplenty_ebony,1), new ItemStack(Items.SHIELD,1)));
				GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_shield_biomesoplenty_ethereal, 1), new ItemStack(BlockInitialiser.chair_wood_ironage_classic_biomesoplenty_ethereal,1), new ItemStack(Items.SHIELD,1)));
				GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_shield_biomesoplenty_eucalyptus, 1), new ItemStack(BlockInitialiser.chair_wood_ironage_classic_biomesoplenty_eucalyptus,1), new ItemStack(Items.SHIELD,1)));
				GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_shield_biomesoplenty_fir, 1), new ItemStack(BlockInitialiser.chair_wood_ironage_classic_biomesoplenty_fir,1), new ItemStack(Items.SHIELD,1)));
				GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_shield_biomesoplenty_hellbark , 1), new ItemStack(BlockInitialiser.chair_wood_ironage_classic_biomesoplenty_hellbark ,1), new ItemStack(Items.SHIELD,1)));
				GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_shield_biomesoplenty_jacaranda, 1), new ItemStack(BlockInitialiser.chair_wood_ironage_classic_biomesoplenty_jacaranda,1), new ItemStack(Items.SHIELD,1)));
				GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_shield_biomesoplenty_magic, 1), new ItemStack(BlockInitialiser.chair_wood_ironage_classic_biomesoplenty_magic,1), new ItemStack(Items.SHIELD,1)));
				GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_shield_biomesoplenty_mahogany, 1), new ItemStack(BlockInitialiser.chair_wood_ironage_classic_biomesoplenty_mahogany,1), new ItemStack(Items.SHIELD,1)));
				GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_shield_biomesoplenty_mangrove, 1), new ItemStack(BlockInitialiser.chair_wood_ironage_classic_biomesoplenty_mangrove,1), new ItemStack(Items.SHIELD,1)));
				GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_shield_biomesoplenty_palm , 1), new ItemStack(BlockInitialiser.chair_wood_ironage_classic_biomesoplenty_palm ,1), new ItemStack(Items.SHIELD,1)));
				GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_shield_biomesoplenty_pine , 1), new ItemStack(BlockInitialiser.chair_wood_ironage_classic_biomesoplenty_pine ,1), new ItemStack(Items.SHIELD,1)));
				GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_shield_biomesoplenty_redwood, 1), new ItemStack(BlockInitialiser.chair_wood_ironage_classic_biomesoplenty_redwood,1), new ItemStack(Items.SHIELD,1)));
				GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_shield_biomesoplenty_sacred_oak, 1), new ItemStack(BlockInitialiser.chair_wood_ironage_classic_biomesoplenty_sacred_oak,1), new ItemStack(Items.SHIELD,1)));
				GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_shield_biomesoplenty_umbran, 1), new ItemStack(BlockInitialiser.chair_wood_ironage_classic_biomesoplenty_umbran,1), new ItemStack(Items.SHIELD,1)));
				GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_shield_biomesoplenty_willow, 1), new ItemStack(BlockInitialiser.chair_wood_ironage_classic_biomesoplenty_willow,1), new ItemStack(Items.SHIELD,1)));
			}
			
			if (IronAgeFurnitureConfiguration.GENERATE_SHORT_STOOLS) {
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_stool_short_biomesoplenty_cherry, 1), " x ", "yyy", 'x', CHERRY, 'y', "stickWood"));
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_stool_short_biomesoplenty_ebony, 1), " x ", "yyy", 'x', EBONY, 'y', "stickWood"));
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_stool_short_biomesoplenty_ethereal, 1), " x ", "yyy", 'x', ETHEREAL, 'y', "stickWood"));
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_stool_short_biomesoplenty_eucalyptus, 1), " x ", "yyy", 'x', EUCALYPTUS, 'y', "stickWood"));
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_stool_short_biomesoplenty_fir, 1), " x ", "yyy", 'x', FIR, 'y', "stickWood"));
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_stool_short_biomesoplenty_hellbark, 1), " x ", "yyy", 'x', HELLBARK, 'y', "stickWood"));
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_stool_short_biomesoplenty_jacaranda, 1), " x ", "yyy", 'x', JACARANDA, 'y', "stickWood"));
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_stool_short_biomesoplenty_magic, 1), " x ", "yyy", 'x', MAGIC, 'y', "stickWood"));
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_stool_short_biomesoplenty_mahogany, 1), " x ", "yyy", 'x', MAHOGANY, 'y', "stickWood"));
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_stool_short_biomesoplenty_mangrove, 1), " x ", "yyy", 'x', MANGROVE, 'y', "stickWood"));
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_stool_short_biomesoplenty_palm, 1), " x ", "yyy", 'x', PALM, 'y', "stickWood"));
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_stool_short_biomesoplenty_pine, 1), " x ", "yyy", 'x', PINE, 'y', "stickWood"));
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_stool_short_biomesoplenty_redwood, 1), " x ", "yyy", 'x', REDWOOD, 'y', "stickWood"));
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_stool_short_biomesoplenty_sacred_oak, 1), " x ", "yyy", 'x', SACRED_OAK, 'y', "stickWood"));
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_stool_short_biomesoplenty_umbran, 1), " x ", "yyy", 'x', UMBRAN, 'y', "stickWood"));
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_stool_short_biomesoplenty_willow, 1), " x ", "yyy", 'x', WILLOW, 'y', "stickWood"));
			}
			
			if (IronAgeFurnitureConfiguration.GENERATE_TALL_STOOLS) {
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_stool_tall_biomesoplenty_cherry, 1), " x ", "yyy", "yyy", 'x', CHERRY, 'y', "stickWood"));
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_stool_tall_biomesoplenty_ebony, 1), " x ", "yyy", "yyy", 'x', EBONY, 'y', "stickWood"));
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_stool_tall_biomesoplenty_ethereal, 1), " x ", "yyy", "yyy", 'x', ETHEREAL, 'y', "stickWood"));
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_stool_tall_biomesoplenty_eucalyptus, 1), " x ", "yyy", "yyy", 'x', EUCALYPTUS, 'y', "stickWood"));
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_stool_tall_biomesoplenty_fir, 1), " x ", "yyy", "yyy", 'x', FIR, 'y', "stickWood"));
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_stool_tall_biomesoplenty_hellbark, 1), " x ", "yyy", "yyy", 'x', HELLBARK, 'y', "stickWood"));
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_stool_tall_biomesoplenty_jacaranda, 1), " x ", "yyy", "yyy", 'x', JACARANDA, 'y', "stickWood"));
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_stool_tall_biomesoplenty_magic, 1), " x ", "yyy", "yyy", 'x', MAGIC, 'y', "stickWood"));
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_stool_tall_biomesoplenty_mahogany, 1), " x ", "yyy", "yyy", 'x', MAHOGANY, 'y', "stickWood"));
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_stool_tall_biomesoplenty_mangrove, 1), " x ", "yyy", "yyy", 'x', MANGROVE, 'y', "stickWood"));
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_stool_tall_biomesoplenty_palm, 1), " x ", "yyy", "yyy", 'x', PALM, 'y', "stickWood"));
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_stool_tall_biomesoplenty_pine, 1), " x ", "yyy", "yyy", 'x', PINE, 'y', "stickWood"));
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_stool_tall_biomesoplenty_redwood, 1), " x ", "yyy", "yyy", 'x', REDWOOD, 'y', "stickWood"));
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_stool_tall_biomesoplenty_sacred_oak, 1), " x ", "yyy", "yyy", 'x', SACRED_OAK, 'y', "stickWood"));
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_stool_tall_biomesoplenty_umbran, 1), " x ", "yyy", "yyy", 'x', UMBRAN, 'y', "stickWood"));
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_stool_tall_biomesoplenty_willow, 1), " x ", "yyy", "yyy", 'x', WILLOW, 'y', "stickWood"));
			}
		}
		
		if (IronAgeFurnitureConfiguration.INTEGRATION_NATURA && Loader.isModLoaded("natura")) {
			ItemStack MAPLE = new ItemStack(Block.getBlockFromName("natura:overworld_planks"), 1, 0);
			ItemStack SILVERBELL = new ItemStack(Block.getBlockFromName("natura:overworld_planks"), 1, 1);
			ItemStack AMARANTH = new ItemStack(Block.getBlockFromName("natura:overworld_planks"), 1, 2);
			ItemStack TIGER = new ItemStack(Block.getBlockFromName("natura:overworld_planks"), 1, 3);
			ItemStack WILLOW = new ItemStack(Block.getBlockFromName("natura:overworld_planks"), 1, 4);
			ItemStack EUCALYPTUS = new ItemStack(Block.getBlockFromName("natura:overworld_planks"), 1, 5);
			ItemStack HOPSEED = new ItemStack(Block.getBlockFromName("natura:overworld_planks"), 1, 6);
			ItemStack SAKURA = new ItemStack(Block.getBlockFromName("natura:overworld_planks"), 1, 7);
			ItemStack REDWOOD = new ItemStack(Block.getBlockFromName("natura:overworld_planks"), 1, 8);
			ItemStack GHOSTWOOD = new ItemStack(Block.getBlockFromName("natura:nether_planks"), 1, 0);
			ItemStack BLOODWOOD = new ItemStack(Block.getBlockFromName("natura:nether_planks"), 1, 1);
			ItemStack FUSEWOOD = new ItemStack(Block.getBlockFromName("natura:nether_planks"), 1, 3);
			ItemStack DARKWOOD = new ItemStack(Block.getBlockFromName("natura:nether_planks"), 1, 2);
			
			
			
			if (IronAgeFurnitureConfiguration.GENERATE_CLASSIC_CHAIRS) {
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_classic_natura_maple, 1), "x  ", "xxx", "y y", 'x', MAPLE, 'y', "stickWood"));
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_classic_natura_silverbell, 1), "x  ", "xxx", "y y", 'x', SILVERBELL, 'y', "stickWood"));
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_classic_natura_amaranth, 1), "x  ", "xxx", "y y", 'x', AMARANTH, 'y', "stickWood"));
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_classic_natura_tiger, 1), "x  ", "xxx", "y y", 'x', TIGER, 'y', "stickWood"));
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_classic_natura_willow, 1), "x  ", "xxx", "y y", 'x', WILLOW, 'y', "stickWood"));
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_classic_natura_eucalyptus, 1), "x  ", "xxx", "y y", 'x', EUCALYPTUS, 'y', "stickWood"));
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_classic_natura_hopseed, 1), "x  ", "xxx", "y y", 'x', HOPSEED , 'y', "stickWood"));
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_classic_natura_sakura, 1), "x  ", "xxx", "y y", 'x', SAKURA, 'y', "stickWood"));
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_classic_natura_redwood, 1), "x  ", "xxx", "y y", 'x', REDWOOD, 'y', "stickWood"));
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_classic_natura_ghostwood, 1), "x  ", "xxx", "y y", 'x', GHOSTWOOD , 'y', "stickWood"));
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_classic_natura_bloodwood, 1), "x  ", "xxx", "y y", 'x', BLOODWOOD , 'y', "stickWood"));
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_classic_natura_fusewood, 1), "x  ", "xxx", "y y", 'x', FUSEWOOD, 'y', "stickWood"));
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_classic_natura_darkwood, 1), "x  ", "xxx", "y y", 'x', DARKWOOD, 'y', "stickWood"));
			}
			
			if (IronAgeFurnitureConfiguration.GENERATE_SHIELD_CHAIRS) {
				GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_shield_natura_maple, 1), new ItemStack(BlockInitialiser.chair_wood_ironage_classic_natura_maple,1), new ItemStack(Items.SHIELD,1)));
				GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_shield_natura_silverbell, 1), new ItemStack(BlockInitialiser.chair_wood_ironage_classic_natura_silverbell,1), new ItemStack(Items.SHIELD,1)));
				GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_shield_natura_amaranth, 1), new ItemStack(BlockInitialiser.chair_wood_ironage_classic_natura_amaranth,1), new ItemStack(Items.SHIELD,1)));
				GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_shield_natura_tiger, 1), new ItemStack(BlockInitialiser.chair_wood_ironage_classic_natura_tiger,1), new ItemStack(Items.SHIELD,1)));
				GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_shield_natura_willow, 1), new ItemStack(BlockInitialiser.chair_wood_ironage_classic_natura_willow,1), new ItemStack(Items.SHIELD,1)));
				GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_shield_natura_eucalyptus, 1), new ItemStack(BlockInitialiser.chair_wood_ironage_classic_natura_eucalyptus,1), new ItemStack(Items.SHIELD,1)));
				GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_shield_natura_hopseed, 1), new ItemStack(BlockInitialiser.chair_wood_ironage_classic_natura_hopseed,1), new ItemStack(Items.SHIELD,1)));
				GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_shield_natura_sakura, 1), new ItemStack(BlockInitialiser.chair_wood_ironage_classic_natura_sakura,1), new ItemStack(Items.SHIELD,1)));
				GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_shield_natura_redwood, 1), new ItemStack(BlockInitialiser.chair_wood_ironage_classic_natura_redwood,1), new ItemStack(Items.SHIELD,1)));
				GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_shield_natura_ghostwood, 1), new ItemStack(BlockInitialiser.chair_wood_ironage_classic_natura_ghostwood,1), new ItemStack(Items.SHIELD,1)));
				GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_shield_natura_bloodwood, 1), new ItemStack(BlockInitialiser.chair_wood_ironage_classic_natura_bloodwood,1), new ItemStack(Items.SHIELD,1)));
				GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_shield_natura_fusewood, 1), new ItemStack(BlockInitialiser.chair_wood_ironage_classic_natura_fusewood,1), new ItemStack(Items.SHIELD,1)));
				GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_shield_natura_darkwood, 1), new ItemStack(BlockInitialiser.chair_wood_ironage_classic_natura_darkwood,1), new ItemStack(Items.SHIELD,1)));
			}
			
			if (IronAgeFurnitureConfiguration.GENERATE_SHORT_STOOLS) {
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_stool_short_natura_maple, 1), " x ", "yyy", 'x', MAPLE, 'y', "stickWood"));
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_stool_short_natura_silverbell, 1), " x ", "yyy", 'x', SILVERBELL, 'y', "stickWood"));
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_stool_short_natura_amaranth, 1), " x ", "yyy", 'x', AMARANTH, 'y', "stickWood"));
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_stool_short_natura_tiger, 1), " x ", "yyy", 'x', TIGER, 'y', "stickWood"));
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_stool_short_natura_willow, 1), " x ", "yyy", 'x', WILLOW, 'y', "stickWood"));
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_stool_short_natura_eucalyptus, 1), " x ", "yyy", 'x', EUCALYPTUS, 'y', "stickWood"));
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_stool_short_natura_hopseed, 1), " x ", "yyy", 'x', HOPSEED, 'y', "stickWood"));
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_stool_short_natura_sakura, 1), " x ", "yyy", 'x', SAKURA, 'y', "stickWood"));
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_stool_short_natura_redwood, 1), " x ", "yyy", 'x', REDWOOD, 'y', "stickWood"));
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_stool_short_natura_ghostwood, 1), " x ", "yyy", 'x', GHOSTWOOD , 'y', "stickWood"));
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_stool_short_natura_bloodwood, 1), " x ", "yyy", 'x', BLOODWOOD , 'y', "stickWood"));
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_stool_short_natura_fusewood, 1), " x ", "yyy", 'x', FUSEWOOD, 'y', "stickWood"));
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_stool_short_natura_darkwood, 1), " x ", "yyy", 'x', DARKWOOD, 'y', "stickWood"));
			}
			
			if (IronAgeFurnitureConfiguration.GENERATE_TALL_STOOLS) {
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_stool_tall_natura_maple, 1), " x ", "yyy", "yyy", 'x', MAPLE, 'y', "stickWood"));
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_stool_tall_natura_silverbell, 1), " x ", "yyy", "yyy", 'x', SILVERBELL, 'y', "stickWood"));
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_stool_tall_natura_amaranth, 1), " x ", "yyy", "yyy", 'x', AMARANTH, 'y', "stickWood"));
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_stool_tall_natura_tiger, 1), " x ", "yyy", "yyy", 'x', TIGER, 'y', "stickWood"));
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_stool_tall_natura_willow, 1), " x ", "yyy", "yyy", 'x', WILLOW, 'y', "stickWood"));
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_stool_tall_natura_eucalyptus, 1), " x ", "yyy", "yyy", 'x', EUCALYPTUS, 'y', "stickWood"));
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_stool_tall_natura_hopseed, 1), " x ", "yyy", "yyy", 'x', HOPSEED, 'y', "stickWood"));
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_stool_tall_natura_sakura, 1), " x ", "yyy", "yyy", 'x', SAKURA, 'y', "stickWood"));
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_stool_tall_natura_redwood, 1), " x ", "yyy", "yyy", 'x', REDWOOD, 'y', "stickWood"));
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_stool_tall_natura_ghostwood, 1), " x ", "yyy", "yyy", 'x', GHOSTWOOD , 'y', "stickWood"));
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_stool_tall_natura_bloodwood, 1), " x ", "yyy", "yyy", 'x', BLOODWOOD , 'y', "stickWood"));
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_stool_tall_natura_fusewood, 1), " x ", "yyy", "yyy", 'x', FUSEWOOD, 'y', "stickWood"));
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockInitialiser.chair_wood_ironage_stool_tall_natura_darkwood, 1), " x ", "yyy", "yyy", 'x', DARKWOOD, 'y', "stickWood"));
			}
		}
	}
}
