package com.mcmoddev.ironagefurniture.init;

import com.mcmoddev.ironagefurniture.BlockObjectHolder;
import com.mcmoddev.ironagefurniture.IronAgeFurnitureConfiguration;
import com.mcmoddev.ironagefurniture.api.FurnitureFactory;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;

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
		
		if (IronAgeFurnitureConfiguration.CFM_CONVERSION_RECIPES && Loader.isModLoaded("cfm")) {
			FurnitureFactory.AddChairConversionRecipe(Block.getBlockFromName("cfm:chair_oak"), BlockObjectHolder.chair_wood_ironage_classic_oak);
			FurnitureFactory.AddChairConversionRecipe(Block.getBlockFromName("cfm:chair_spruce"), BlockObjectHolder.chair_wood_ironage_classic_spruce);
			FurnitureFactory.AddChairConversionRecipe(Block.getBlockFromName("cfm:chair_birch"), BlockObjectHolder.chair_wood_ironage_classic_birch);
			FurnitureFactory.AddChairConversionRecipe(Block.getBlockFromName("cfm:chair_jungle"), BlockObjectHolder.chair_wood_ironage_classic_jungle);
			FurnitureFactory.AddChairConversionRecipe(Block.getBlockFromName("cfm:chair_acacia"), BlockObjectHolder.chair_wood_ironage_classic_acacia);
			FurnitureFactory.AddChairConversionRecipe(Block.getBlockFromName("cfm:chair_big_oak"), BlockObjectHolder.chair_wood_ironage_classic_big_oak);
		}
		
		
		if (IronAgeFurnitureConfiguration.GENERATE_CLASSIC_CHAIRS) {
			FurnitureFactory.AddClassicChairRecipe(new ItemStack(Blocks.PLANKS, 1, 0), BlockObjectHolder.chair_wood_ironage_classic_oak);
			FurnitureFactory.AddClassicChairRecipe(new ItemStack(Blocks.PLANKS, 1, 1), BlockObjectHolder.chair_wood_ironage_classic_spruce);
			FurnitureFactory.AddClassicChairRecipe(new ItemStack(Blocks.PLANKS, 1, 2), BlockObjectHolder.chair_wood_ironage_classic_birch);
			FurnitureFactory.AddClassicChairRecipe(new ItemStack(Blocks.PLANKS, 1, 3), BlockObjectHolder.chair_wood_ironage_classic_jungle);
			FurnitureFactory.AddClassicChairRecipe(new ItemStack(Blocks.PLANKS, 1, 4), BlockObjectHolder.chair_wood_ironage_classic_acacia);
			FurnitureFactory.AddClassicChairRecipe(new ItemStack(Blocks.PLANKS, 1, 5), BlockObjectHolder.chair_wood_ironage_classic_big_oak);
		}
		
		if (IronAgeFurnitureConfiguration.GENERATE_SHIELD_CHAIRS) {
			FurnitureFactory.AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_oak, BlockObjectHolder.chair_wood_ironage_shield_oak);
			FurnitureFactory.AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_spruce, BlockObjectHolder.chair_wood_ironage_shield_spruce);
			FurnitureFactory.AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_birch, BlockObjectHolder.chair_wood_ironage_shield_birch);
			FurnitureFactory.AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_jungle, BlockObjectHolder.chair_wood_ironage_shield_jungle);
			FurnitureFactory.AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_acacia, BlockObjectHolder.chair_wood_ironage_shield_acacia);
			FurnitureFactory.AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_big_oak, BlockObjectHolder.chair_wood_ironage_shield_big_oak);	
		}
		
		if (IronAgeFurnitureConfiguration.GENERATE_SHORT_STOOLS) {
			FurnitureFactory.AddShortStoolRecipe(new ItemStack(Blocks.PLANKS, 1, 0), BlockObjectHolder.chair_wood_ironage_stool_short_oak);
			FurnitureFactory.AddShortStoolRecipe(new ItemStack(Blocks.PLANKS, 1, 1), BlockObjectHolder.chair_wood_ironage_stool_short_spruce);
			FurnitureFactory.AddShortStoolRecipe(new ItemStack(Blocks.PLANKS, 1, 2), BlockObjectHolder.chair_wood_ironage_stool_short_birch);
			FurnitureFactory.AddShortStoolRecipe(new ItemStack(Blocks.PLANKS, 1, 3), BlockObjectHolder.chair_wood_ironage_stool_short_jungle);
			FurnitureFactory.AddShortStoolRecipe(new ItemStack(Blocks.PLANKS, 1, 4), BlockObjectHolder.chair_wood_ironage_stool_short_acacia);
			FurnitureFactory.AddShortStoolRecipe(new ItemStack(Blocks.PLANKS, 1, 5), BlockObjectHolder.chair_wood_ironage_stool_short_big_oak);
		}
		
		if (IronAgeFurnitureConfiguration.GENERATE_TALL_STOOLS) {
			FurnitureFactory.AddTallStoolRecipe(new ItemStack(Blocks.PLANKS, 1, 0), BlockObjectHolder.chair_wood_ironage_stool_tall_oak);
			FurnitureFactory.AddTallStoolRecipe(new ItemStack(Blocks.PLANKS, 1, 1), BlockObjectHolder.chair_wood_ironage_stool_tall_spruce);
			FurnitureFactory.AddTallStoolRecipe(new ItemStack(Blocks.PLANKS, 1, 2), BlockObjectHolder.chair_wood_ironage_stool_tall_birch);
			FurnitureFactory.AddTallStoolRecipe(new ItemStack(Blocks.PLANKS, 1, 3), BlockObjectHolder.chair_wood_ironage_stool_tall_jungle);
			FurnitureFactory.AddTallStoolRecipe(new ItemStack(Blocks.PLANKS, 1, 4), BlockObjectHolder.chair_wood_ironage_stool_tall_acacia);
			FurnitureFactory.AddTallStoolRecipe(new ItemStack(Blocks.PLANKS, 1, 5), BlockObjectHolder.chair_wood_ironage_stool_tall_big_oak);
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
				FurnitureFactory.AddClassicChairRecipe(CHERRY, BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_cherry);
				FurnitureFactory.AddClassicChairRecipe(EBONY, BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_ebony);
				FurnitureFactory.AddClassicChairRecipe(ETHEREAL, BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_ethereal);
				FurnitureFactory.AddClassicChairRecipe(EUCALYPTUS, BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_eucalyptus);
				FurnitureFactory.AddClassicChairRecipe(FIR, BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_fir);
				FurnitureFactory.AddClassicChairRecipe(HELLBARK, BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_hellbark);
				FurnitureFactory.AddClassicChairRecipe(JACARANDA, BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_jacaranda);
				FurnitureFactory.AddClassicChairRecipe(MAGIC, BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_magic);
				FurnitureFactory.AddClassicChairRecipe(MAHOGANY, BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_mahogany);
				FurnitureFactory.AddClassicChairRecipe(MANGROVE, BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_mangrove);
				FurnitureFactory.AddClassicChairRecipe(PALM, BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_palm);
				FurnitureFactory.AddClassicChairRecipe(PINE, BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_pine);
				FurnitureFactory.AddClassicChairRecipe(REDWOOD, BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_redwood);
				FurnitureFactory.AddClassicChairRecipe(SACRED_OAK, BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_sacred_oak);
				FurnitureFactory.AddClassicChairRecipe(UMBRAN, BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_umbran);
				FurnitureFactory.AddClassicChairRecipe(WILLOW, BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_willow);
			}
			
			if (IronAgeFurnitureConfiguration.GENERATE_SHIELD_CHAIRS) {
				FurnitureFactory.AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_cherry, BlockObjectHolder.chair_wood_ironage_shield_biomesoplenty_cherry);
				FurnitureFactory.AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_ebony, BlockObjectHolder.chair_wood_ironage_shield_biomesoplenty_ebony);
				FurnitureFactory.AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_ethereal, BlockObjectHolder.chair_wood_ironage_shield_biomesoplenty_ethereal);
				FurnitureFactory.AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_eucalyptus, BlockObjectHolder.chair_wood_ironage_shield_biomesoplenty_eucalyptus);
				FurnitureFactory.AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_fir, BlockObjectHolder.chair_wood_ironage_shield_biomesoplenty_fir);
				FurnitureFactory.AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_hellbark, BlockObjectHolder.chair_wood_ironage_shield_biomesoplenty_hellbark);
				FurnitureFactory.AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_jacaranda, BlockObjectHolder.chair_wood_ironage_shield_biomesoplenty_jacaranda);
				FurnitureFactory.AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_magic, BlockObjectHolder.chair_wood_ironage_shield_biomesoplenty_magic);
				FurnitureFactory.AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_mahogany, BlockObjectHolder.chair_wood_ironage_shield_biomesoplenty_mahogany);
				FurnitureFactory.AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_mangrove, BlockObjectHolder.chair_wood_ironage_shield_biomesoplenty_mangrove);
				FurnitureFactory.AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_palm, BlockObjectHolder.chair_wood_ironage_shield_biomesoplenty_palm);
				FurnitureFactory.AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_pine, BlockObjectHolder.chair_wood_ironage_shield_biomesoplenty_pine);
				FurnitureFactory.AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_redwood, BlockObjectHolder.chair_wood_ironage_shield_biomesoplenty_redwood);
				FurnitureFactory.AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_sacred_oak, BlockObjectHolder.chair_wood_ironage_shield_biomesoplenty_sacred_oak);
				FurnitureFactory.AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_umbran, BlockObjectHolder.chair_wood_ironage_shield_biomesoplenty_umbran);
				FurnitureFactory.AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_willow, BlockObjectHolder.chair_wood_ironage_shield_biomesoplenty_willow);

			}
			
			if (IronAgeFurnitureConfiguration.GENERATE_SHORT_STOOLS) {
				FurnitureFactory.AddShortStoolRecipe(CHERRY, BlockObjectHolder.chair_wood_ironage_stool_short_biomesoplenty_cherry);
				FurnitureFactory.AddShortStoolRecipe(EBONY, BlockObjectHolder.chair_wood_ironage_stool_short_biomesoplenty_ebony);
				FurnitureFactory.AddShortStoolRecipe(ETHEREAL, BlockObjectHolder.chair_wood_ironage_stool_short_biomesoplenty_ethereal);
				FurnitureFactory.AddShortStoolRecipe(EUCALYPTUS, BlockObjectHolder.chair_wood_ironage_stool_short_biomesoplenty_eucalyptus);
				FurnitureFactory.AddShortStoolRecipe(FIR, BlockObjectHolder.chair_wood_ironage_stool_short_biomesoplenty_fir);
				FurnitureFactory.AddShortStoolRecipe(HELLBARK, BlockObjectHolder.chair_wood_ironage_stool_short_biomesoplenty_hellbark);
				FurnitureFactory.AddShortStoolRecipe(JACARANDA, BlockObjectHolder.chair_wood_ironage_stool_short_biomesoplenty_jacaranda);
				FurnitureFactory.AddShortStoolRecipe(MAGIC, BlockObjectHolder.chair_wood_ironage_stool_short_biomesoplenty_magic);
				FurnitureFactory.AddShortStoolRecipe(MAHOGANY, BlockObjectHolder.chair_wood_ironage_stool_short_biomesoplenty_mahogany);
				FurnitureFactory.AddShortStoolRecipe(MANGROVE, BlockObjectHolder.chair_wood_ironage_stool_short_biomesoplenty_mangrove);
				FurnitureFactory.AddShortStoolRecipe(PALM, BlockObjectHolder.chair_wood_ironage_stool_short_biomesoplenty_palm);
				FurnitureFactory.AddShortStoolRecipe(PINE, BlockObjectHolder.chair_wood_ironage_stool_short_biomesoplenty_pine);
				FurnitureFactory.AddShortStoolRecipe(REDWOOD, BlockObjectHolder.chair_wood_ironage_stool_short_biomesoplenty_redwood);
				FurnitureFactory.AddShortStoolRecipe(SACRED_OAK, BlockObjectHolder.chair_wood_ironage_stool_short_biomesoplenty_sacred_oak);
				FurnitureFactory.AddShortStoolRecipe(UMBRAN, BlockObjectHolder.chair_wood_ironage_stool_short_biomesoplenty_umbran);
				FurnitureFactory.AddShortStoolRecipe(WILLOW, BlockObjectHolder.chair_wood_ironage_stool_short_biomesoplenty_willow);
			}
			
			if (IronAgeFurnitureConfiguration.GENERATE_TALL_STOOLS) {
				FurnitureFactory.AddTallStoolRecipe(CHERRY, BlockObjectHolder.chair_wood_ironage_stool_tall_biomesoplenty_cherry);
				FurnitureFactory.AddTallStoolRecipe(EBONY, BlockObjectHolder.chair_wood_ironage_stool_tall_biomesoplenty_ebony);
				FurnitureFactory.AddTallStoolRecipe(ETHEREAL, BlockObjectHolder.chair_wood_ironage_stool_tall_biomesoplenty_ethereal);
				FurnitureFactory.AddTallStoolRecipe(EUCALYPTUS, BlockObjectHolder.chair_wood_ironage_stool_tall_biomesoplenty_eucalyptus);
				FurnitureFactory.AddTallStoolRecipe(FIR, BlockObjectHolder.chair_wood_ironage_stool_tall_biomesoplenty_fir);
				FurnitureFactory.AddTallStoolRecipe(HELLBARK, BlockObjectHolder.chair_wood_ironage_stool_tall_biomesoplenty_hellbark);
				FurnitureFactory.AddTallStoolRecipe(JACARANDA, BlockObjectHolder.chair_wood_ironage_stool_tall_biomesoplenty_jacaranda);
				FurnitureFactory.AddTallStoolRecipe(MAGIC, BlockObjectHolder.chair_wood_ironage_stool_tall_biomesoplenty_magic);
				FurnitureFactory.AddTallStoolRecipe(MAHOGANY, BlockObjectHolder.chair_wood_ironage_stool_tall_biomesoplenty_mahogany);
				FurnitureFactory.AddTallStoolRecipe(MANGROVE, BlockObjectHolder.chair_wood_ironage_stool_tall_biomesoplenty_mangrove);
				FurnitureFactory.AddTallStoolRecipe(PALM, BlockObjectHolder.chair_wood_ironage_stool_tall_biomesoplenty_palm);
				FurnitureFactory.AddTallStoolRecipe(PINE, BlockObjectHolder.chair_wood_ironage_stool_tall_biomesoplenty_pine);
				FurnitureFactory.AddTallStoolRecipe(REDWOOD, BlockObjectHolder.chair_wood_ironage_stool_tall_biomesoplenty_redwood);
				FurnitureFactory.AddTallStoolRecipe(SACRED_OAK, BlockObjectHolder.chair_wood_ironage_stool_tall_biomesoplenty_sacred_oak);
				FurnitureFactory.AddTallStoolRecipe(UMBRAN, BlockObjectHolder.chair_wood_ironage_stool_tall_biomesoplenty_umbran);
				FurnitureFactory.AddTallStoolRecipe(WILLOW, BlockObjectHolder.chair_wood_ironage_stool_tall_biomesoplenty_willow);
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
			
			Block netherPlanks = Block.getBlockFromName("natura:nether_planks");
			
			ItemStack GHOSTWOOD = new ItemStack(netherPlanks, 1, 0);
			ItemStack BLOODWOOD = new ItemStack(netherPlanks, 1, 1);
			ItemStack FUSEWOOD = new ItemStack(netherPlanks, 1, 3);
			ItemStack DARKWOOD = new ItemStack(netherPlanks, 1, 2);
			
			if (IronAgeFurnitureConfiguration.GENERATE_CLASSIC_CHAIRS) {
				FurnitureFactory.AddClassicChairRecipe(MAPLE, BlockObjectHolder.chair_wood_ironage_classic_natura_maple);
				FurnitureFactory.AddClassicChairRecipe(SILVERBELL, BlockObjectHolder.chair_wood_ironage_classic_natura_silverbell);
				FurnitureFactory.AddClassicChairRecipe(AMARANTH, BlockObjectHolder.chair_wood_ironage_classic_natura_amaranth);
				FurnitureFactory.AddClassicChairRecipe(TIGER, BlockObjectHolder.chair_wood_ironage_classic_natura_tiger);
				FurnitureFactory.AddClassicChairRecipe(WILLOW, BlockObjectHolder.chair_wood_ironage_classic_natura_willow);
				FurnitureFactory.AddClassicChairRecipe(EUCALYPTUS, BlockObjectHolder.chair_wood_ironage_classic_natura_eucalyptus);
				FurnitureFactory.AddClassicChairRecipe(HOPSEED, BlockObjectHolder.chair_wood_ironage_classic_natura_hopseed);
				FurnitureFactory.AddClassicChairRecipe(SAKURA, BlockObjectHolder.chair_wood_ironage_classic_natura_sakura);
				FurnitureFactory.AddClassicChairRecipe(REDWOOD, BlockObjectHolder.chair_wood_ironage_classic_natura_redwood);
				FurnitureFactory.AddClassicChairRecipe(GHOSTWOOD, BlockObjectHolder.chair_wood_ironage_classic_natura_ghostwood);
				FurnitureFactory.AddClassicChairRecipe(BLOODWOOD, BlockObjectHolder.chair_wood_ironage_classic_natura_bloodwood);
				FurnitureFactory.AddClassicChairRecipe(FUSEWOOD, BlockObjectHolder.chair_wood_ironage_classic_natura_fusewood);
				FurnitureFactory.AddClassicChairRecipe(DARKWOOD, BlockObjectHolder.chair_wood_ironage_classic_natura_darkwood);
			}
			
			if (IronAgeFurnitureConfiguration.GENERATE_SHIELD_CHAIRS) {
				FurnitureFactory.AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_natura_maple, BlockObjectHolder.chair_wood_ironage_shield_natura_maple);
				FurnitureFactory.AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_natura_silverbell, BlockObjectHolder.chair_wood_ironage_shield_natura_silverbell);
				FurnitureFactory.AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_natura_amaranth, BlockObjectHolder.chair_wood_ironage_shield_natura_amaranth);
				FurnitureFactory.AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_natura_tiger, BlockObjectHolder.chair_wood_ironage_shield_natura_tiger);
				FurnitureFactory.AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_natura_willow, BlockObjectHolder.chair_wood_ironage_shield_natura_willow);
				FurnitureFactory.AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_natura_eucalyptus, BlockObjectHolder.chair_wood_ironage_shield_natura_eucalyptus);
				FurnitureFactory.AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_natura_hopseed, BlockObjectHolder.chair_wood_ironage_shield_natura_hopseed);
				FurnitureFactory.AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_natura_sakura, BlockObjectHolder.chair_wood_ironage_shield_natura_sakura);
				FurnitureFactory.AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_natura_redwood, BlockObjectHolder.chair_wood_ironage_shield_natura_redwood);
				FurnitureFactory.AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_natura_ghostwood, BlockObjectHolder.chair_wood_ironage_shield_natura_ghostwood);
				FurnitureFactory.AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_natura_bloodwood, BlockObjectHolder.chair_wood_ironage_shield_natura_bloodwood );
				FurnitureFactory.AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_natura_fusewood, BlockObjectHolder.chair_wood_ironage_shield_natura_fusewood);
				FurnitureFactory.AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_natura_darkwood, BlockObjectHolder.chair_wood_ironage_shield_natura_darkwood);
			}
			
			if (IronAgeFurnitureConfiguration.GENERATE_SHORT_STOOLS) {
				FurnitureFactory.AddShortStoolRecipe(MAPLE, BlockObjectHolder.chair_wood_ironage_stool_short_natura_maple);
				FurnitureFactory.AddShortStoolRecipe(SILVERBELL, BlockObjectHolder.chair_wood_ironage_stool_short_natura_silverbell);
				FurnitureFactory.AddShortStoolRecipe(AMARANTH, BlockObjectHolder.chair_wood_ironage_stool_short_natura_amaranth);
				FurnitureFactory.AddShortStoolRecipe(TIGER, BlockObjectHolder.chair_wood_ironage_stool_short_natura_tiger);
				FurnitureFactory.AddShortStoolRecipe(WILLOW, BlockObjectHolder.chair_wood_ironage_stool_short_natura_willow);
				FurnitureFactory.AddShortStoolRecipe(EUCALYPTUS, BlockObjectHolder.chair_wood_ironage_stool_short_natura_eucalyptus);
				FurnitureFactory.AddShortStoolRecipe(HOPSEED, BlockObjectHolder.chair_wood_ironage_stool_short_natura_hopseed);
				FurnitureFactory.AddShortStoolRecipe(SAKURA, BlockObjectHolder.chair_wood_ironage_stool_short_natura_sakura);
				FurnitureFactory.AddShortStoolRecipe(REDWOOD, BlockObjectHolder.chair_wood_ironage_stool_short_natura_redwood);
				FurnitureFactory.AddShortStoolRecipe(GHOSTWOOD, BlockObjectHolder.chair_wood_ironage_stool_short_natura_ghostwood);
				FurnitureFactory.AddShortStoolRecipe(BLOODWOOD, BlockObjectHolder.chair_wood_ironage_stool_short_natura_bloodwood);
				FurnitureFactory.AddShortStoolRecipe(FUSEWOOD, BlockObjectHolder.chair_wood_ironage_stool_short_natura_fusewood);
				FurnitureFactory.AddShortStoolRecipe(DARKWOOD, BlockObjectHolder.chair_wood_ironage_stool_short_natura_darkwood);
			}
			
			if (IronAgeFurnitureConfiguration.GENERATE_TALL_STOOLS) {
				FurnitureFactory.AddTallStoolRecipe(MAPLE, BlockObjectHolder.chair_wood_ironage_stool_tall_natura_maple);
				FurnitureFactory.AddTallStoolRecipe(SILVERBELL, BlockObjectHolder.chair_wood_ironage_stool_tall_natura_silverbell);
				FurnitureFactory.AddTallStoolRecipe(AMARANTH, BlockObjectHolder.chair_wood_ironage_stool_tall_natura_amaranth);
				FurnitureFactory.AddTallStoolRecipe(TIGER, BlockObjectHolder.chair_wood_ironage_stool_tall_natura_tiger);
				FurnitureFactory.AddTallStoolRecipe(WILLOW, BlockObjectHolder.chair_wood_ironage_stool_tall_natura_willow);
				FurnitureFactory.AddTallStoolRecipe(EUCALYPTUS, BlockObjectHolder.chair_wood_ironage_stool_tall_natura_eucalyptus);
				FurnitureFactory.AddTallStoolRecipe(HOPSEED, BlockObjectHolder.chair_wood_ironage_stool_tall_natura_hopseed);
				FurnitureFactory.AddTallStoolRecipe(SAKURA, BlockObjectHolder.chair_wood_ironage_stool_tall_natura_sakura);
				FurnitureFactory.AddTallStoolRecipe(REDWOOD, BlockObjectHolder.chair_wood_ironage_stool_tall_natura_redwood);
				FurnitureFactory.AddTallStoolRecipe(GHOSTWOOD, BlockObjectHolder.chair_wood_ironage_stool_tall_natura_ghostwood);
				FurnitureFactory.AddTallStoolRecipe(BLOODWOOD, BlockObjectHolder.chair_wood_ironage_stool_tall_natura_bloodwood);
				FurnitureFactory.AddTallStoolRecipe(FUSEWOOD, BlockObjectHolder.chair_wood_ironage_stool_tall_natura_fusewood);
				FurnitureFactory.AddTallStoolRecipe(DARKWOOD, BlockObjectHolder.chair_wood_ironage_stool_tall_natura_darkwood);
			}
		}
		
		if (IronAgeFurnitureConfiguration.INTEGRATION_IMMERSIVEENGINEERING && Loader.isModLoaded("immersiveengineering")) {
			Block wood = Block.getBlockFromName("immersiveengineering:treatedWood");
			
	
			ItemStack HORIZONTAL = new ItemStack(wood, 1, 0);
			ItemStack VERTICAL = new ItemStack(wood, 1, 1);
			ItemStack PACKAGED = new ItemStack(wood, 1, 2);
			
			if (IronAgeFurnitureConfiguration.GENERATE_CLASSIC_CHAIRS) {
				FurnitureFactory.AddClassicChairRecipe(HORIZONTAL, BlockObjectHolder.chair_wood_ironage_classic_immersiveengineering_treatedWood);
				FurnitureFactory.AddClassicChairRecipe(VERTICAL, BlockObjectHolder.chair_wood_ironage_classic_immersiveengineering_treatedWood);
				FurnitureFactory.AddClassicChairRecipe(PACKAGED, BlockObjectHolder.chair_wood_ironage_classic_immersiveengineering_treatedWood);
				
			}
			
			if (IronAgeFurnitureConfiguration.GENERATE_SHIELD_CHAIRS) {
				FurnitureFactory.AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_immersiveengineering_treatedWood, BlockObjectHolder.chair_wood_ironage_shield_immersiveengineering_treatedWood);			
			}
			
			if (IronAgeFurnitureConfiguration.GENERATE_SHORT_STOOLS) {
				FurnitureFactory.AddShortStoolRecipe(HORIZONTAL, BlockObjectHolder.chair_wood_ironage_stool_short_immersiveengineering_treatedWood);
				FurnitureFactory.AddShortStoolRecipe(VERTICAL, BlockObjectHolder.chair_wood_ironage_stool_short_immersiveengineering_treatedWood);
				FurnitureFactory.AddShortStoolRecipe(PACKAGED, BlockObjectHolder.chair_wood_ironage_stool_short_immersiveengineering_treatedWood);
				
			}
			
			if (IronAgeFurnitureConfiguration.GENERATE_TALL_STOOLS) {
				FurnitureFactory.AddTallStoolRecipe(HORIZONTAL, BlockObjectHolder.chair_wood_ironage_stool_tall_immersiveengineering_treatedWood);
				FurnitureFactory.AddTallStoolRecipe(VERTICAL, BlockObjectHolder.chair_wood_ironage_stool_tall_immersiveengineering_treatedWood);
				FurnitureFactory.AddTallStoolRecipe(PACKAGED, BlockObjectHolder.chair_wood_ironage_stool_tall_immersiveengineering_treatedWood);
			}
		}
		
		if (IronAgeFurnitureConfiguration.INTEGRATION_FORESTRY && Loader.isModLoaded("forestry")) {
			Block wood = Block.getBlockFromName("forestry:planks.0");
			Block wood1 = Block.getBlockFromName("forestry:planks.1");
	
			ItemStack LARCH = new ItemStack(wood, 1, 0);
			ItemStack TEAK = new ItemStack(wood, 1, 1);
			ItemStack ACACIA = new ItemStack(wood, 1, 2);
			ItemStack LIME = new ItemStack(wood, 1, 3);
			ItemStack CHESTNUT = new ItemStack(wood, 1, 4);
			ItemStack WENGE = new ItemStack(wood, 1, 5);
			ItemStack BAOBAB = new ItemStack(wood, 1, 6);
			ItemStack SEQUOIA = new ItemStack(wood, 1, 7);
			ItemStack KAPOK = new ItemStack(wood, 1, 8);
			ItemStack EBONY = new ItemStack(wood, 1, 9);
			ItemStack MAHOGANY = new ItemStack(wood, 1, 10);
			ItemStack BALSA = new ItemStack(wood, 1, 11);
			ItemStack WILLOW = new ItemStack(wood, 1, 12);
			ItemStack WALNUT = new ItemStack(wood, 1, 13);
			ItemStack GREENHEART = new ItemStack(wood, 1, 14);
			ItemStack CHERRY = new ItemStack(wood, 1, 15);
			ItemStack MAHOE = new ItemStack(wood1, 1, 0);
			ItemStack POPLAR = new ItemStack(wood1, 1, 1);
			ItemStack PALM = new ItemStack(wood1, 1, 2);
			ItemStack PAPAYA = new ItemStack(wood1, 1, 3);
			ItemStack PINE = new ItemStack(wood1, 1, 4);
			ItemStack PLUM = new ItemStack(wood1, 1, 5);
			ItemStack MAPLE = new ItemStack(wood1, 1, 6);
			ItemStack CITRUS = new ItemStack(wood1, 1, 7);
			ItemStack GIGANTEUM = new ItemStack(wood1, 1, 8);
			ItemStack IPE = new ItemStack(wood1, 1, 9);
			ItemStack PADAUK = new ItemStack(wood1, 1, 10);
			ItemStack COCOBOLO = new ItemStack(wood1, 1, 11);
			ItemStack ZEBRAWOOD = new ItemStack(wood1, 1, 12);

			
			if (IronAgeFurnitureConfiguration.GENERATE_CLASSIC_CHAIRS) {
				FurnitureFactory.AddClassicChairRecipe(ACACIA, BlockObjectHolder.chair_wood_ironage_classic_forestry_acacia);
				FurnitureFactory.AddClassicChairRecipe(BALSA, BlockObjectHolder.chair_wood_ironage_classic_forestry_balsa);
				FurnitureFactory.AddClassicChairRecipe(BAOBAB, BlockObjectHolder.chair_wood_ironage_classic_forestry_baobab);
				FurnitureFactory.AddClassicChairRecipe(CHERRY, BlockObjectHolder.chair_wood_ironage_classic_forestry_cherry);
				FurnitureFactory.AddClassicChairRecipe(CHESTNUT, BlockObjectHolder.chair_wood_ironage_classic_forestry_chestnut);
				FurnitureFactory.AddClassicChairRecipe(CITRUS, BlockObjectHolder.chair_wood_ironage_classic_forestry_citrus);
				FurnitureFactory.AddClassicChairRecipe(COCOBOLO, BlockObjectHolder.chair_wood_ironage_classic_forestry_cocobolo);
				FurnitureFactory.AddClassicChairRecipe(EBONY, BlockObjectHolder.chair_wood_ironage_classic_forestry_ebony);
				FurnitureFactory.AddClassicChairRecipe(GIGANTEUM, BlockObjectHolder.chair_wood_ironage_classic_forestry_giganteum);
				FurnitureFactory.AddClassicChairRecipe(GREENHEART, BlockObjectHolder.chair_wood_ironage_classic_forestry_greenheart);
				FurnitureFactory.AddClassicChairRecipe(IPE, BlockObjectHolder.chair_wood_ironage_classic_forestry_ipe);
				FurnitureFactory.AddClassicChairRecipe(KAPOK, BlockObjectHolder.chair_wood_ironage_classic_forestry_kapok);
				FurnitureFactory.AddClassicChairRecipe(LARCH, BlockObjectHolder.chair_wood_ironage_classic_forestry_larch);
				FurnitureFactory.AddClassicChairRecipe(LIME, BlockObjectHolder.chair_wood_ironage_classic_forestry_lime);
				FurnitureFactory.AddClassicChairRecipe(MAHOE, BlockObjectHolder.chair_wood_ironage_classic_forestry_mahoe);
				FurnitureFactory.AddClassicChairRecipe(MAHOGANY, BlockObjectHolder.chair_wood_ironage_classic_forestry_mahogany);
				FurnitureFactory.AddClassicChairRecipe(MAPLE, BlockObjectHolder.chair_wood_ironage_classic_forestry_maple);
				FurnitureFactory.AddClassicChairRecipe(PADAUK, BlockObjectHolder.chair_wood_ironage_classic_forestry_padauk);
				FurnitureFactory.AddClassicChairRecipe(PALM, BlockObjectHolder.chair_wood_ironage_classic_forestry_palm);
				FurnitureFactory.AddClassicChairRecipe(PAPAYA, BlockObjectHolder.chair_wood_ironage_classic_forestry_papaya);
				FurnitureFactory.AddClassicChairRecipe(PINE, BlockObjectHolder.chair_wood_ironage_classic_forestry_pine);
				FurnitureFactory.AddClassicChairRecipe(PLUM, BlockObjectHolder.chair_wood_ironage_classic_forestry_plum);
				FurnitureFactory.AddClassicChairRecipe(POPLAR, BlockObjectHolder.chair_wood_ironage_classic_forestry_poplar);
				FurnitureFactory.AddClassicChairRecipe(SEQUOIA, BlockObjectHolder.chair_wood_ironage_classic_forestry_sequoia);
				FurnitureFactory.AddClassicChairRecipe(TEAK, BlockObjectHolder.chair_wood_ironage_classic_forestry_teak);
				FurnitureFactory.AddClassicChairRecipe(WALNUT, BlockObjectHolder.chair_wood_ironage_classic_forestry_walnut);
				FurnitureFactory.AddClassicChairRecipe(WENGE, BlockObjectHolder.chair_wood_ironage_classic_forestry_wenge);
				FurnitureFactory.AddClassicChairRecipe(WILLOW, BlockObjectHolder.chair_wood_ironage_classic_forestry_willow);
				FurnitureFactory.AddClassicChairRecipe(ZEBRAWOOD, BlockObjectHolder.chair_wood_ironage_classic_forestry_zebrawood);

				
			}
			
			if (IronAgeFurnitureConfiguration.GENERATE_SHIELD_CHAIRS) {
				FurnitureFactory.AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_forestry_acacia, BlockObjectHolder.chair_wood_ironage_shield_forestry_acacia);
				FurnitureFactory.AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_forestry_balsa, BlockObjectHolder.chair_wood_ironage_shield_forestry_balsa);
				FurnitureFactory.AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_forestry_baobab, BlockObjectHolder.chair_wood_ironage_shield_forestry_baobab);
				FurnitureFactory.AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_forestry_cherry, BlockObjectHolder.chair_wood_ironage_shield_forestry_cherry);
				FurnitureFactory.AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_forestry_chestnut, BlockObjectHolder.chair_wood_ironage_shield_forestry_chestnut);
				FurnitureFactory.AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_forestry_citrus, BlockObjectHolder.chair_wood_ironage_shield_forestry_citrus);
				FurnitureFactory.AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_forestry_cocobolo, BlockObjectHolder.chair_wood_ironage_shield_forestry_cocobolo);
				FurnitureFactory.AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_forestry_ebony, BlockObjectHolder.chair_wood_ironage_shield_forestry_ebony);
				FurnitureFactory.AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_forestry_giganteum, BlockObjectHolder.chair_wood_ironage_shield_forestry_giganteum);
				FurnitureFactory.AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_forestry_greenheart, BlockObjectHolder.chair_wood_ironage_shield_forestry_greenheart);
				FurnitureFactory.AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_forestry_ipe, BlockObjectHolder.chair_wood_ironage_shield_forestry_ipe);
				FurnitureFactory.AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_forestry_kapok, BlockObjectHolder.chair_wood_ironage_shield_forestry_kapok);
				FurnitureFactory.AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_forestry_larch, BlockObjectHolder.chair_wood_ironage_shield_forestry_larch);
				FurnitureFactory.AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_forestry_lime, BlockObjectHolder.chair_wood_ironage_shield_forestry_lime);
				FurnitureFactory.AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_forestry_mahoe, BlockObjectHolder.chair_wood_ironage_shield_forestry_mahoe);
				FurnitureFactory.AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_forestry_mahogany, BlockObjectHolder.chair_wood_ironage_shield_forestry_mahogany);
				FurnitureFactory.AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_forestry_maple, BlockObjectHolder.chair_wood_ironage_shield_forestry_maple);
				FurnitureFactory.AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_forestry_padauk, BlockObjectHolder.chair_wood_ironage_shield_forestry_padauk);
				FurnitureFactory.AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_forestry_palm, BlockObjectHolder.chair_wood_ironage_shield_forestry_palm);
				FurnitureFactory.AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_forestry_papaya, BlockObjectHolder.chair_wood_ironage_shield_forestry_papaya);
				FurnitureFactory.AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_forestry_pine, BlockObjectHolder.chair_wood_ironage_shield_forestry_pine);
				FurnitureFactory.AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_forestry_plum, BlockObjectHolder.chair_wood_ironage_shield_forestry_plum);
				FurnitureFactory.AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_forestry_poplar, BlockObjectHolder.chair_wood_ironage_shield_forestry_poplar);
				FurnitureFactory.AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_forestry_sequoia, BlockObjectHolder.chair_wood_ironage_shield_forestry_sequoia);
				FurnitureFactory.AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_forestry_teak, BlockObjectHolder.chair_wood_ironage_shield_forestry_teak);
				FurnitureFactory.AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_forestry_walnut, BlockObjectHolder.chair_wood_ironage_shield_forestry_walnut);
				FurnitureFactory.AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_forestry_wenge, BlockObjectHolder.chair_wood_ironage_shield_forestry_wenge);
				FurnitureFactory.AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_forestry_willow, BlockObjectHolder.chair_wood_ironage_shield_forestry_willow);
				FurnitureFactory.AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_forestry_zebrawood, BlockObjectHolder.chair_wood_ironage_shield_forestry_zebrawood);
			
			}
			
			if (IronAgeFurnitureConfiguration.GENERATE_SHORT_STOOLS) {
				FurnitureFactory.AddShortStoolRecipe(ACACIA, BlockObjectHolder.chair_wood_ironage_stool_short_forestry_acacia);
				FurnitureFactory.AddShortStoolRecipe(BALSA, BlockObjectHolder.chair_wood_ironage_stool_short_forestry_balsa);
				FurnitureFactory.AddShortStoolRecipe(BAOBAB, BlockObjectHolder.chair_wood_ironage_stool_short_forestry_baobab);
				FurnitureFactory.AddShortStoolRecipe(CHERRY, BlockObjectHolder.chair_wood_ironage_stool_short_forestry_cherry);
				FurnitureFactory.AddShortStoolRecipe(CHESTNUT, BlockObjectHolder.chair_wood_ironage_stool_short_forestry_chestnut);
				FurnitureFactory.AddShortStoolRecipe(CITRUS, BlockObjectHolder.chair_wood_ironage_stool_short_forestry_citrus);
				FurnitureFactory.AddShortStoolRecipe(COCOBOLO, BlockObjectHolder.chair_wood_ironage_stool_short_forestry_cocobolo);
				FurnitureFactory.AddShortStoolRecipe(EBONY, BlockObjectHolder.chair_wood_ironage_stool_short_forestry_ebony);
				FurnitureFactory.AddShortStoolRecipe(GIGANTEUM, BlockObjectHolder.chair_wood_ironage_stool_short_forestry_giganteum);
				FurnitureFactory.AddShortStoolRecipe(GREENHEART, BlockObjectHolder.chair_wood_ironage_stool_short_forestry_greenheart);
				FurnitureFactory.AddShortStoolRecipe(IPE, BlockObjectHolder.chair_wood_ironage_stool_short_forestry_ipe);
				FurnitureFactory.AddShortStoolRecipe(KAPOK, BlockObjectHolder.chair_wood_ironage_stool_short_forestry_kapok);
				FurnitureFactory.AddShortStoolRecipe(LARCH, BlockObjectHolder.chair_wood_ironage_stool_short_forestry_larch);
				FurnitureFactory.AddShortStoolRecipe(LIME, BlockObjectHolder.chair_wood_ironage_stool_short_forestry_lime);
				FurnitureFactory.AddShortStoolRecipe(MAHOE, BlockObjectHolder.chair_wood_ironage_stool_short_forestry_mahoe);
				FurnitureFactory.AddShortStoolRecipe(MAHOGANY, BlockObjectHolder.chair_wood_ironage_stool_short_forestry_mahogany);
				FurnitureFactory.AddShortStoolRecipe(MAPLE, BlockObjectHolder.chair_wood_ironage_stool_short_forestry_maple);
				FurnitureFactory.AddShortStoolRecipe(PADAUK, BlockObjectHolder.chair_wood_ironage_stool_short_forestry_padauk);
				FurnitureFactory.AddShortStoolRecipe(PALM, BlockObjectHolder.chair_wood_ironage_stool_short_forestry_palm);
				FurnitureFactory.AddShortStoolRecipe(PAPAYA, BlockObjectHolder.chair_wood_ironage_stool_short_forestry_papaya);
				FurnitureFactory.AddShortStoolRecipe(PINE, BlockObjectHolder.chair_wood_ironage_stool_short_forestry_pine);
				FurnitureFactory.AddShortStoolRecipe(PLUM, BlockObjectHolder.chair_wood_ironage_stool_short_forestry_plum);
				FurnitureFactory.AddShortStoolRecipe(POPLAR, BlockObjectHolder.chair_wood_ironage_stool_short_forestry_poplar);
				FurnitureFactory.AddShortStoolRecipe(SEQUOIA, BlockObjectHolder.chair_wood_ironage_stool_short_forestry_sequoia);
				FurnitureFactory.AddShortStoolRecipe(TEAK, BlockObjectHolder.chair_wood_ironage_stool_short_forestry_teak);
				FurnitureFactory.AddShortStoolRecipe(WALNUT, BlockObjectHolder.chair_wood_ironage_stool_short_forestry_walnut);
				FurnitureFactory.AddShortStoolRecipe(WENGE, BlockObjectHolder.chair_wood_ironage_stool_short_forestry_wenge);
				FurnitureFactory.AddShortStoolRecipe(WILLOW, BlockObjectHolder.chair_wood_ironage_stool_short_forestry_willow);
				FurnitureFactory.AddShortStoolRecipe(ZEBRAWOOD, BlockObjectHolder.chair_wood_ironage_stool_short_forestry_zebrawood);

				
			}
			
			if (IronAgeFurnitureConfiguration.GENERATE_TALL_STOOLS) {
				FurnitureFactory.AddTallStoolRecipe(ACACIA, BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_acacia);
				FurnitureFactory.AddTallStoolRecipe(BALSA, BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_balsa);
				FurnitureFactory.AddTallStoolRecipe(BAOBAB, BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_baobab);
				FurnitureFactory.AddTallStoolRecipe(CHERRY, BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_cherry);
				FurnitureFactory.AddTallStoolRecipe(CHESTNUT, BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_chestnut);
				FurnitureFactory.AddTallStoolRecipe(CITRUS, BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_citrus);
				FurnitureFactory.AddTallStoolRecipe(COCOBOLO, BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_cocobolo);
				FurnitureFactory.AddTallStoolRecipe(EBONY, BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_ebony);
				FurnitureFactory.AddTallStoolRecipe(GIGANTEUM, BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_giganteum);
				FurnitureFactory.AddTallStoolRecipe(GREENHEART, BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_greenheart);
				FurnitureFactory.AddTallStoolRecipe(IPE, BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_ipe);
				FurnitureFactory.AddTallStoolRecipe(KAPOK, BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_kapok);
				FurnitureFactory.AddTallStoolRecipe(LARCH, BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_larch);
				FurnitureFactory.AddTallStoolRecipe(LIME, BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_lime);
				FurnitureFactory.AddTallStoolRecipe(MAHOE, BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_mahoe);
				FurnitureFactory.AddTallStoolRecipe(MAHOGANY, BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_mahogany);
				FurnitureFactory.AddTallStoolRecipe(MAPLE, BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_maple);
				FurnitureFactory.AddTallStoolRecipe(PADAUK, BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_padauk);
				FurnitureFactory.AddTallStoolRecipe(PALM, BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_palm);
				FurnitureFactory.AddTallStoolRecipe(PAPAYA, BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_papaya);
				FurnitureFactory.AddTallStoolRecipe(PINE, BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_pine);
				FurnitureFactory.AddTallStoolRecipe(PLUM, BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_plum);
				FurnitureFactory.AddTallStoolRecipe(POPLAR, BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_poplar);
				FurnitureFactory.AddTallStoolRecipe(SEQUOIA, BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_sequoia);
				FurnitureFactory.AddTallStoolRecipe(TEAK, BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_teak);
				FurnitureFactory.AddTallStoolRecipe(WALNUT, BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_walnut);
				FurnitureFactory.AddTallStoolRecipe(WENGE, BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_wenge);
				FurnitureFactory.AddTallStoolRecipe(WILLOW, BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_willow);
				FurnitureFactory.AddTallStoolRecipe(ZEBRAWOOD, BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_zebrawood);

			}
		}
	}
}
