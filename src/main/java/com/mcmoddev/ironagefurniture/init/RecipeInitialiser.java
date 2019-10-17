package com.mcmoddev.ironagefurniture.init;

import com.mcmoddev.ironagefurniture.BlockObjectHolder;
import com.mcmoddev.ironagefurniture.IronAgeFurnitureConfiguration;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

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
	
	private static void AddClassicChairRecipe(ItemStack planks, Block chair) {
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(chair, 1), "x  ", "xxx", "y y", 'x', planks, 'y', "stickWood"));
	}
	
	private static void AddShortStoolRecipe(ItemStack planks, Block stool) {
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(stool, 1), " x ", "yyy", 'x', planks, 'y', "stickWood"));
	}
	
	private static void AddTallStoolRecipe(ItemStack planks, Block stool) {
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(stool, 1), " x ", "yyy","yyy", 'x', planks, 'y', "stickWood"));
	}
	
	private static void AddShieldChairRecipe(Block chairIn, Block chairOut) {
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(chairOut, 1), new ItemStack(chairIn,1), new ItemStack(Items.SHIELD,1)));
	}
	
	private static void generateChairRecipes() {
		if (IronAgeFurnitureConfiguration.GENERATE_CLASSIC_CHAIRS) {
			AddClassicChairRecipe(new ItemStack(Blocks.PLANKS, 1, 0), BlockObjectHolder.chair_wood_ironage_classic_oak);
			AddClassicChairRecipe(new ItemStack(Blocks.PLANKS, 1, 1), BlockObjectHolder.chair_wood_ironage_classic_spruce);
			AddClassicChairRecipe(new ItemStack(Blocks.PLANKS, 1, 2), BlockObjectHolder.chair_wood_ironage_classic_birch);
			AddClassicChairRecipe(new ItemStack(Blocks.PLANKS, 1, 3), BlockObjectHolder.chair_wood_ironage_classic_jungle);
			AddClassicChairRecipe(new ItemStack(Blocks.PLANKS, 1, 4), BlockObjectHolder.chair_wood_ironage_classic_acacia);
			AddClassicChairRecipe(new ItemStack(Blocks.PLANKS, 1, 5), BlockObjectHolder.chair_wood_ironage_classic_big_oak);
		}
		
		if (IronAgeFurnitureConfiguration.GENERATE_SHIELD_CHAIRS) {
			AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_oak, BlockObjectHolder.chair_wood_ironage_shield_oak);
			AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_spruce, BlockObjectHolder.chair_wood_ironage_shield_spruce);
			AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_birch, BlockObjectHolder.chair_wood_ironage_shield_birch);
			AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_jungle, BlockObjectHolder.chair_wood_ironage_shield_jungle);
			AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_acacia, BlockObjectHolder.chair_wood_ironage_shield_acacia);
			AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_big_oak, BlockObjectHolder.chair_wood_ironage_shield_big_oak);	
		}
		
		if (IronAgeFurnitureConfiguration.GENERATE_SHORT_STOOLS) {
			AddShortStoolRecipe(new ItemStack(Blocks.PLANKS, 1, 0), BlockObjectHolder.chair_wood_ironage_stool_short_oak);
			AddShortStoolRecipe(new ItemStack(Blocks.PLANKS, 1, 1), BlockObjectHolder.chair_wood_ironage_stool_short_spruce);
			AddShortStoolRecipe(new ItemStack(Blocks.PLANKS, 1, 2), BlockObjectHolder.chair_wood_ironage_stool_short_birch);
			AddShortStoolRecipe(new ItemStack(Blocks.PLANKS, 1, 3), BlockObjectHolder.chair_wood_ironage_stool_short_jungle);
			AddShortStoolRecipe(new ItemStack(Blocks.PLANKS, 1, 4), BlockObjectHolder.chair_wood_ironage_stool_short_acacia);
			AddShortStoolRecipe(new ItemStack(Blocks.PLANKS, 1, 5), BlockObjectHolder.chair_wood_ironage_stool_short_big_oak);
		}
		
		if (IronAgeFurnitureConfiguration.GENERATE_TALL_STOOLS) {
			AddTallStoolRecipe(new ItemStack(Blocks.PLANKS, 1, 0), BlockObjectHolder.chair_wood_ironage_stool_short_oak);
			AddTallStoolRecipe(new ItemStack(Blocks.PLANKS, 1, 1), BlockObjectHolder.chair_wood_ironage_stool_short_spruce);
			AddTallStoolRecipe(new ItemStack(Blocks.PLANKS, 1, 2), BlockObjectHolder.chair_wood_ironage_stool_short_birch);
			AddTallStoolRecipe(new ItemStack(Blocks.PLANKS, 1, 3), BlockObjectHolder.chair_wood_ironage_stool_short_jungle);
			AddTallStoolRecipe(new ItemStack(Blocks.PLANKS, 1, 4), BlockObjectHolder.chair_wood_ironage_stool_short_acacia);
			AddTallStoolRecipe(new ItemStack(Blocks.PLANKS, 1, 5), BlockObjectHolder.chair_wood_ironage_stool_short_big_oak);
		}
		
		if (IronAgeFurnitureConfiguration.INTEGRATION_BIOMESOPLENTY && Loader.isModLoaded("BiomesOPlenty")) {
			Block planks = Block.getBlockFromName("BiomesOPlenty:planks_0");
			
			ItemStack SACRED_OAK = new ItemStack(planks, 1, 0);
			ItemStack CHERRY = new ItemStack(planks, 1, 1);
			ItemStack UMBRAN = new ItemStack(planks, 1, 2);
			ItemStack FIR = new ItemStack(planks, 1, 3);
			ItemStack ETHEREAL = new ItemStack(planks, 1, 4);
			ItemStack MAGIC = new ItemStack(planks, 1, 5);
			ItemStack MANGROVE = new ItemStack(planks, 1, 6);
			ItemStack PALM = new ItemStack(planks, 1, 7);
			ItemStack REDWOOD = new ItemStack(planks, 1, 8);
			ItemStack WILLOW = new ItemStack(planks, 1, 9);
			ItemStack PINE = new ItemStack(planks, 1, 10);
			ItemStack HELLBARK = new ItemStack(planks, 1, 11);
			ItemStack JACARANDA = new ItemStack(planks, 1, 12);
			ItemStack MAHOGANY = new ItemStack(planks, 1, 13);
			ItemStack EBONY = new ItemStack(planks, 1, 14);
			ItemStack EUCALYPTUS = new ItemStack(planks, 1, 15);
			
			if (IronAgeFurnitureConfiguration.GENERATE_CLASSIC_CHAIRS) {
				AddClassicChairRecipe(CHERRY, BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_cherry);
				AddClassicChairRecipe(EBONY, BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_ebony);
				AddClassicChairRecipe(ETHEREAL, BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_ethereal);
				AddClassicChairRecipe(EUCALYPTUS, BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_eucalyptus);
				AddClassicChairRecipe(FIR, BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_fir);
				AddClassicChairRecipe(HELLBARK, BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_hellbark);
				AddClassicChairRecipe(JACARANDA, BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_jacaranda);
				AddClassicChairRecipe(MAGIC, BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_magic);
				AddClassicChairRecipe(MAHOGANY, BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_mahogany);
				AddClassicChairRecipe(MANGROVE, BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_mangrove);
				AddClassicChairRecipe(PALM, BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_palm);
				AddClassicChairRecipe(PINE, BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_pine);
				AddClassicChairRecipe(REDWOOD, BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_redwood);
				AddClassicChairRecipe(SACRED_OAK, BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_sacred_oak);
				AddClassicChairRecipe(UMBRAN, BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_umbran);
				AddClassicChairRecipe(WILLOW, BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_willow);
			}
			
			if (IronAgeFurnitureConfiguration.GENERATE_SHIELD_CHAIRS) {
				AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_cherry, BlockObjectHolder.chair_wood_ironage_shield_biomesoplenty_cherry);
				AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_ebony, BlockObjectHolder.chair_wood_ironage_shield_biomesoplenty_ebony);
				AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_ethereal, BlockObjectHolder.chair_wood_ironage_shield_biomesoplenty_ethereal);
				AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_eucalyptus, BlockObjectHolder.chair_wood_ironage_shield_biomesoplenty_eucalyptus);
				AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_fir, BlockObjectHolder.chair_wood_ironage_shield_biomesoplenty_fir);
				AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_hellbark, BlockObjectHolder.chair_wood_ironage_shield_biomesoplenty_hellbark);
				AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_jacaranda, BlockObjectHolder.chair_wood_ironage_shield_biomesoplenty_jacaranda);
				AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_magic, BlockObjectHolder.chair_wood_ironage_shield_biomesoplenty_magic);
				AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_mahogany, BlockObjectHolder.chair_wood_ironage_shield_biomesoplenty_mahogany);
				AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_mangrove, BlockObjectHolder.chair_wood_ironage_shield_biomesoplenty_mangrove);
				AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_palm, BlockObjectHolder.chair_wood_ironage_shield_biomesoplenty_palm);
				AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_pine, BlockObjectHolder.chair_wood_ironage_shield_biomesoplenty_pine);
				AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_redwood, BlockObjectHolder.chair_wood_ironage_shield_biomesoplenty_redwood);
				AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_sacred_oak, BlockObjectHolder.chair_wood_ironage_shield_biomesoplenty_sacred_oak);
				AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_umbran, BlockObjectHolder.chair_wood_ironage_shield_biomesoplenty_umbran);
				AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_willow, BlockObjectHolder.chair_wood_ironage_shield_biomesoplenty_willow);

			}
			
			if (IronAgeFurnitureConfiguration.GENERATE_SHORT_STOOLS) {
				AddShortStoolRecipe(CHERRY, BlockObjectHolder.chair_wood_ironage_stool_short_biomesoplenty_cherry);
				AddShortStoolRecipe(EBONY, BlockObjectHolder.chair_wood_ironage_stool_short_biomesoplenty_ebony);
				AddShortStoolRecipe(ETHEREAL, BlockObjectHolder.chair_wood_ironage_stool_short_biomesoplenty_ethereal);
				AddShortStoolRecipe(EUCALYPTUS, BlockObjectHolder.chair_wood_ironage_stool_short_biomesoplenty_eucalyptus);
				AddShortStoolRecipe(FIR, BlockObjectHolder.chair_wood_ironage_stool_short_biomesoplenty_fir);
				AddShortStoolRecipe(HELLBARK, BlockObjectHolder.chair_wood_ironage_stool_short_biomesoplenty_hellbark);
				AddShortStoolRecipe(JACARANDA, BlockObjectHolder.chair_wood_ironage_stool_short_biomesoplenty_jacaranda);
				AddShortStoolRecipe(MAGIC, BlockObjectHolder.chair_wood_ironage_stool_short_biomesoplenty_magic);
				AddShortStoolRecipe(MAHOGANY, BlockObjectHolder.chair_wood_ironage_stool_short_biomesoplenty_mahogany);
				AddShortStoolRecipe(MANGROVE, BlockObjectHolder.chair_wood_ironage_stool_short_biomesoplenty_mangrove);
				AddShortStoolRecipe(PALM, BlockObjectHolder.chair_wood_ironage_stool_short_biomesoplenty_palm);
				AddShortStoolRecipe(PINE, BlockObjectHolder.chair_wood_ironage_stool_short_biomesoplenty_pine);
				AddShortStoolRecipe(REDWOOD, BlockObjectHolder.chair_wood_ironage_stool_short_biomesoplenty_redwood);
				AddShortStoolRecipe(SACRED_OAK, BlockObjectHolder.chair_wood_ironage_stool_short_biomesoplenty_sacred_oak);
				AddShortStoolRecipe(UMBRAN, BlockObjectHolder.chair_wood_ironage_stool_short_biomesoplenty_umbran);
				AddShortStoolRecipe(WILLOW, BlockObjectHolder.chair_wood_ironage_stool_short_biomesoplenty_willow);
			}
			
			if (IronAgeFurnitureConfiguration.GENERATE_TALL_STOOLS) {
				AddTallStoolRecipe(CHERRY, BlockObjectHolder.chair_wood_ironage_stool_tall_biomesoplenty_cherry);
				AddTallStoolRecipe(EBONY, BlockObjectHolder.chair_wood_ironage_stool_tall_biomesoplenty_ebony);
				AddTallStoolRecipe(ETHEREAL, BlockObjectHolder.chair_wood_ironage_stool_tall_biomesoplenty_ethereal);
				AddTallStoolRecipe(EUCALYPTUS, BlockObjectHolder.chair_wood_ironage_stool_tall_biomesoplenty_eucalyptus);
				AddTallStoolRecipe(FIR, BlockObjectHolder.chair_wood_ironage_stool_tall_biomesoplenty_fir);
				AddTallStoolRecipe(HELLBARK, BlockObjectHolder.chair_wood_ironage_stool_tall_biomesoplenty_hellbark);
				AddTallStoolRecipe(JACARANDA, BlockObjectHolder.chair_wood_ironage_stool_tall_biomesoplenty_jacaranda);
				AddTallStoolRecipe(MAGIC, BlockObjectHolder.chair_wood_ironage_stool_tall_biomesoplenty_magic);
				AddTallStoolRecipe(MAHOGANY, BlockObjectHolder.chair_wood_ironage_stool_tall_biomesoplenty_mahogany);
				AddTallStoolRecipe(MANGROVE, BlockObjectHolder.chair_wood_ironage_stool_tall_biomesoplenty_mangrove);
				AddTallStoolRecipe(PALM, BlockObjectHolder.chair_wood_ironage_stool_tall_biomesoplenty_palm);
				AddTallStoolRecipe(PINE, BlockObjectHolder.chair_wood_ironage_stool_tall_biomesoplenty_pine);
				AddTallStoolRecipe(REDWOOD, BlockObjectHolder.chair_wood_ironage_stool_tall_biomesoplenty_redwood);
				AddTallStoolRecipe(SACRED_OAK, BlockObjectHolder.chair_wood_ironage_stool_tall_biomesoplenty_sacred_oak);
				AddTallStoolRecipe(UMBRAN, BlockObjectHolder.chair_wood_ironage_stool_tall_biomesoplenty_umbran);
				AddTallStoolRecipe(WILLOW, BlockObjectHolder.chair_wood_ironage_stool_tall_biomesoplenty_willow);
			}
		}
		
		if (IronAgeFurnitureConfiguration.INTEGRATION_NATURA && Loader.isModLoaded("natura")) {
			Block planks = Block.getBlockFromName("natura:overworld_planks");
			
			ItemStack MAPLE = new ItemStack(planks, 1, 0);
			ItemStack SILVERBELL = new ItemStack(planks, 1, 1);
			ItemStack AMARANTH = new ItemStack(planks, 1, 2);
			ItemStack TIGER = new ItemStack(planks, 1, 3);
			ItemStack WILLOW = new ItemStack(planks, 1, 4);
			ItemStack EUCALYPTUS = new ItemStack(planks, 1, 5);
			ItemStack HOPSEED = new ItemStack(planks, 1, 6);
			ItemStack SAKURA = new ItemStack(planks, 1, 7);
			ItemStack REDWOOD = new ItemStack(planks, 1, 8);
			ItemStack GHOSTWOOD = new ItemStack(planks, 1, 0);
			ItemStack BLOODWOOD = new ItemStack(planks, 1, 1);
			ItemStack FUSEWOOD = new ItemStack(planks, 1, 3);
			ItemStack DARKWOOD = new ItemStack(planks, 1, 2);
			
			if (IronAgeFurnitureConfiguration.GENERATE_CLASSIC_CHAIRS) {
				AddClassicChairRecipe(MAPLE, BlockObjectHolder.chair_wood_ironage_classic_natura_maple);
				AddClassicChairRecipe(SILVERBELL, BlockObjectHolder.chair_wood_ironage_classic_natura_silverbell);
				AddClassicChairRecipe(AMARANTH, BlockObjectHolder.chair_wood_ironage_classic_natura_amaranth);
				AddClassicChairRecipe(TIGER, BlockObjectHolder.chair_wood_ironage_classic_natura_tiger);
				AddClassicChairRecipe(WILLOW, BlockObjectHolder.chair_wood_ironage_classic_natura_willow);
				AddClassicChairRecipe(EUCALYPTUS, BlockObjectHolder.chair_wood_ironage_classic_natura_eucalyptus);
				AddClassicChairRecipe(HOPSEED, BlockObjectHolder.chair_wood_ironage_classic_natura_hopseed);
				AddClassicChairRecipe(SAKURA, BlockObjectHolder.chair_wood_ironage_classic_natura_sakura);
				AddClassicChairRecipe(REDWOOD, BlockObjectHolder.chair_wood_ironage_classic_natura_redwood);
				AddClassicChairRecipe(GHOSTWOOD, BlockObjectHolder.chair_wood_ironage_classic_natura_ghostwood);
				AddClassicChairRecipe(BLOODWOOD, BlockObjectHolder.chair_wood_ironage_classic_natura_bloodwood);
				AddClassicChairRecipe(FUSEWOOD, BlockObjectHolder.chair_wood_ironage_classic_natura_fusewood);
				AddClassicChairRecipe(DARKWOOD, BlockObjectHolder.chair_wood_ironage_classic_natura_darkwood);
			}
			
			if (IronAgeFurnitureConfiguration.GENERATE_SHIELD_CHAIRS) {
				AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_natura_maple, BlockObjectHolder.chair_wood_ironage_shield_natura_maple);
				AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_natura_silverbell, BlockObjectHolder.chair_wood_ironage_shield_natura_silverbell);
				AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_natura_amaranth, BlockObjectHolder.chair_wood_ironage_shield_natura_amaranth);
				AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_natura_tiger, BlockObjectHolder.chair_wood_ironage_shield_natura_tiger);
				AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_natura_willow, BlockObjectHolder.chair_wood_ironage_shield_natura_willow);
				AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_natura_eucalyptus, BlockObjectHolder.chair_wood_ironage_shield_natura_eucalyptus);
				AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_natura_hopseed, BlockObjectHolder.chair_wood_ironage_shield_natura_hopseed);
				AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_natura_sakura, BlockObjectHolder.chair_wood_ironage_shield_natura_sakura);
				AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_natura_redwood, BlockObjectHolder.chair_wood_ironage_shield_natura_redwood);
				AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_natura_ghostwood, BlockObjectHolder.chair_wood_ironage_shield_natura_ghostwood);
				AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_natura_bloodwood, BlockObjectHolder.chair_wood_ironage_shield_natura_bloodwood );
				AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_natura_fusewood, BlockObjectHolder.chair_wood_ironage_shield_natura_fusewood);
				AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_natura_darkwood, BlockObjectHolder.chair_wood_ironage_shield_natura_darkwood);
			}
			
			if (IronAgeFurnitureConfiguration.GENERATE_SHORT_STOOLS) {
				AddShortStoolRecipe(MAPLE, BlockObjectHolder.chair_wood_ironage_stool_short_natura_maple);
				AddShortStoolRecipe(SILVERBELL, BlockObjectHolder.chair_wood_ironage_stool_short_natura_silverbell);
				AddShortStoolRecipe(AMARANTH, BlockObjectHolder.chair_wood_ironage_stool_short_natura_amaranth);
				AddShortStoolRecipe(TIGER, BlockObjectHolder.chair_wood_ironage_stool_short_natura_tiger);
				AddShortStoolRecipe(WILLOW, BlockObjectHolder.chair_wood_ironage_stool_short_natura_willow);
				AddShortStoolRecipe(EUCALYPTUS, BlockObjectHolder.chair_wood_ironage_stool_short_natura_eucalyptus);
				AddShortStoolRecipe(HOPSEED, BlockObjectHolder.chair_wood_ironage_stool_short_natura_hopseed);
				AddShortStoolRecipe(SAKURA, BlockObjectHolder.chair_wood_ironage_stool_short_natura_sakura);
				AddShortStoolRecipe(REDWOOD, BlockObjectHolder.chair_wood_ironage_stool_short_natura_redwood);
				AddShortStoolRecipe(GHOSTWOOD, BlockObjectHolder.chair_wood_ironage_stool_short_natura_ghostwood);
				AddShortStoolRecipe(BLOODWOOD, BlockObjectHolder.chair_wood_ironage_stool_short_natura_bloodwood);
				AddShortStoolRecipe(FUSEWOOD, BlockObjectHolder.chair_wood_ironage_stool_short_natura_fusewood);
				AddShortStoolRecipe(DARKWOOD, BlockObjectHolder.chair_wood_ironage_stool_short_natura_darkwood);
			}
			
			if (IronAgeFurnitureConfiguration.GENERATE_TALL_STOOLS) {
				AddTallStoolRecipe(MAPLE, BlockObjectHolder.chair_wood_ironage_stool_tall_natura_maple);
				AddTallStoolRecipe(SILVERBELL, BlockObjectHolder.chair_wood_ironage_stool_tall_natura_silverbell);
				AddTallStoolRecipe(AMARANTH, BlockObjectHolder.chair_wood_ironage_stool_tall_natura_amaranth);
				AddTallStoolRecipe(TIGER, BlockObjectHolder.chair_wood_ironage_stool_tall_natura_tiger);
				AddTallStoolRecipe(WILLOW, BlockObjectHolder.chair_wood_ironage_stool_tall_natura_willow);
				AddTallStoolRecipe(EUCALYPTUS, BlockObjectHolder.chair_wood_ironage_stool_tall_natura_eucalyptus);
				AddTallStoolRecipe(HOPSEED, BlockObjectHolder.chair_wood_ironage_stool_tall_natura_hopseed);
				AddTallStoolRecipe(SAKURA, BlockObjectHolder.chair_wood_ironage_stool_tall_natura_sakura);
				AddTallStoolRecipe(REDWOOD, BlockObjectHolder.chair_wood_ironage_stool_tall_natura_redwood);
				AddTallStoolRecipe(GHOSTWOOD, BlockObjectHolder.chair_wood_ironage_stool_tall_natura_ghostwood);
				AddTallStoolRecipe(BLOODWOOD, BlockObjectHolder.chair_wood_ironage_stool_tall_natura_bloodwood);
				AddTallStoolRecipe(FUSEWOOD, BlockObjectHolder.chair_wood_ironage_stool_tall_natura_fusewood);
				AddTallStoolRecipe(DARKWOOD, BlockObjectHolder.chair_wood_ironage_stool_tall_natura_darkwood);
			}
		}
	}
}
