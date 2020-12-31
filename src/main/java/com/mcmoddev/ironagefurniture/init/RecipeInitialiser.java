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
		
		if (IronAgeFurnitureConfiguration.GENERATE_WOOD_BENCHES) {
			FurnitureFactory.AddBenchRecipe(new ItemStack(Blocks.PLANKS, 1, 0), BlockObjectHolder.chair_wood_ironage_bench_single_oak);
			FurnitureFactory.AddBenchRecipe(new ItemStack(Blocks.PLANKS, 1, 1), BlockObjectHolder.chair_wood_ironage_bench_single_spruce);
			FurnitureFactory.AddBenchRecipe(new ItemStack(Blocks.PLANKS, 1, 2), BlockObjectHolder.chair_wood_ironage_bench_single_birch);
			FurnitureFactory.AddBenchRecipe(new ItemStack(Blocks.PLANKS, 1, 3), BlockObjectHolder.chair_wood_ironage_bench_single_jungle);
			FurnitureFactory.AddBenchRecipe(new ItemStack(Blocks.PLANKS, 1, 4), BlockObjectHolder.chair_wood_ironage_bench_single_acacia);
			FurnitureFactory.AddBenchRecipe(new ItemStack(Blocks.PLANKS, 1, 5), BlockObjectHolder.chair_wood_ironage_bench_single_big_oak);
			
			FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_single_oak, BlockObjectHolder.chair_wood_ironage_bench_padded_single_oak);
			FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_single_spruce, BlockObjectHolder.chair_wood_ironage_bench_padded_single_spruce);
			FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_single_birch, BlockObjectHolder.chair_wood_ironage_bench_padded_single_birch);
			FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_single_jungle, BlockObjectHolder.chair_wood_ironage_bench_padded_single_jungle);
			FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_single_acacia, BlockObjectHolder.chair_wood_ironage_bench_padded_single_acacia);
			FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_single_big_oak, BlockObjectHolder.chair_wood_ironage_bench_padded_single_oak);
		
			FurnitureFactory.AddLogBenchRecipe(new ItemStack(Blocks.LOG, 1, 0), BlockObjectHolder.chair_wood_ironage_bench_log_single_oak);
			FurnitureFactory.AddLogBenchRecipe(new ItemStack(Blocks.LOG, 1, 1), BlockObjectHolder.chair_wood_ironage_bench_log_single_spruce);
			FurnitureFactory.AddLogBenchRecipe(new ItemStack(Blocks.LOG, 1, 2), BlockObjectHolder.chair_wood_ironage_bench_log_single_birch);
			FurnitureFactory.AddLogBenchRecipe(new ItemStack(Blocks.LOG, 1, 3), BlockObjectHolder.chair_wood_ironage_bench_log_single_jungle);
			FurnitureFactory.AddLogBenchRecipe(new ItemStack(Blocks.LOG2, 1, 0), BlockObjectHolder.chair_wood_ironage_bench_log_single_acacia);
			FurnitureFactory.AddLogBenchRecipe(new ItemStack(Blocks.LOG2, 1, 1), BlockObjectHolder.chair_wood_ironage_bench_log_single_big_oak);
		
			FurnitureFactory.AddBackBenchRecipe(new ItemStack(Blocks.PLANKS, 1, 0), BlockObjectHolder.chair_wood_ironage_bench_back_single_oak);
			FurnitureFactory.AddBackBenchRecipe(new ItemStack(Blocks.PLANKS, 1, 1), BlockObjectHolder.chair_wood_ironage_bench_back_single_spruce);
			FurnitureFactory.AddBackBenchRecipe(new ItemStack(Blocks.PLANKS, 1, 2), BlockObjectHolder.chair_wood_ironage_bench_back_single_birch);
			FurnitureFactory.AddBackBenchRecipe(new ItemStack(Blocks.PLANKS, 1, 3), BlockObjectHolder.chair_wood_ironage_bench_back_single_jungle);
			FurnitureFactory.AddBackBenchRecipe(new ItemStack(Blocks.PLANKS, 1, 4), BlockObjectHolder.chair_wood_ironage_bench_back_single_acacia);
			FurnitureFactory.AddBackBenchRecipe(new ItemStack(Blocks.PLANKS, 1, 5), BlockObjectHolder.chair_wood_ironage_bench_back_single_big_oak);
			
			FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_back_single_oak, BlockObjectHolder.chair_wood_ironage_bench_back_padded_single_oak);
			FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_back_single_spruce, BlockObjectHolder.chair_wood_ironage_bench_back_padded_single_spruce);
			FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_back_single_birch, BlockObjectHolder.chair_wood_ironage_bench_back_padded_single_birch);
			FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_back_single_jungle, BlockObjectHolder.chair_wood_ironage_bench_back_padded_single_jungle);
			FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_back_single_acacia, BlockObjectHolder.chair_wood_ironage_bench_back_padded_single_acacia);
			FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_back_single_big_oak, BlockObjectHolder.chair_wood_ironage_bench_back_padded_single_big_oak);
		
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
			
			Block logs0 = Block.getBlockFromName("BiomesOPlenty:log_0");
			
			ItemStack SACRED_OAK_LOG = new ItemStack(logs0, 1, 4);
			ItemStack CHERRY_LOG = new ItemStack(logs0, 1, 5);
			ItemStack UMBRAN_LOG = new ItemStack(logs0, 1, 6);
			ItemStack FIR_LOG = new ItemStack(logs0, 1, 7);
			
			Block logs1 = Block.getBlockFromName("BiomesOPlenty:log_1");
			
			ItemStack ETHEREAL_LOG = new ItemStack(logs1, 1, 4);
			ItemStack MAGIC_LOG = new ItemStack(logs1, 1, 5);
			ItemStack MANGROVE_LOG = new ItemStack(logs1, 1, 6);
			ItemStack PALM_LOG = new ItemStack(logs1, 1, 7);
			
			Block logs2 = Block.getBlockFromName("BiomesOPlenty:log_2");
			
			ItemStack REDWOOD_LOG = new ItemStack(logs2, 1, 4);
			ItemStack WILLOW_LOG = new ItemStack(logs2, 1, 5);
			ItemStack PINE_LOG = new ItemStack(logs2, 1, 6);
			ItemStack HELLBARK_LOG = new ItemStack(logs2, 1, 7);
			
			Block logs3 = Block.getBlockFromName("BiomesOPlenty:log_3");
			
			ItemStack JACARANDA_LOG = new ItemStack(logs3, 1, 4);
			ItemStack MAHOGANY_LOG = new ItemStack(logs3, 1, 5);
			ItemStack EBONY_LOG = new ItemStack(logs3, 1, 6);
			ItemStack EUCALYPTUS_LOG = new ItemStack(logs3, 1, 7);
			
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
			
			if (IronAgeFurnitureConfiguration.GENERATE_WOOD_BENCHES) {
				FurnitureFactory.AddBenchRecipe(CHERRY, BlockObjectHolder.chair_wood_ironage_bench_single_biomesoplenty_cherry);
				FurnitureFactory.AddBenchRecipe(EBONY, BlockObjectHolder.chair_wood_ironage_bench_single_biomesoplenty_ebony);
				FurnitureFactory.AddBenchRecipe(ETHEREAL, BlockObjectHolder.chair_wood_ironage_bench_single_biomesoplenty_ethereal);
				FurnitureFactory.AddBenchRecipe(EUCALYPTUS, BlockObjectHolder.chair_wood_ironage_bench_single_biomesoplenty_eucalyptus);
				FurnitureFactory.AddBenchRecipe(FIR, BlockObjectHolder.chair_wood_ironage_bench_single_biomesoplenty_fir);
				FurnitureFactory.AddBenchRecipe(HELLBARK, BlockObjectHolder.chair_wood_ironage_bench_single_biomesoplenty_hellbark);
				FurnitureFactory.AddBenchRecipe(JACARANDA, BlockObjectHolder.chair_wood_ironage_bench_single_biomesoplenty_jacaranda);
				FurnitureFactory.AddBenchRecipe(MAGIC, BlockObjectHolder.chair_wood_ironage_bench_single_biomesoplenty_magic);
				FurnitureFactory.AddBenchRecipe(MAHOGANY, BlockObjectHolder.chair_wood_ironage_bench_single_biomesoplenty_mahogany);
				FurnitureFactory.AddBenchRecipe(MANGROVE, BlockObjectHolder.chair_wood_ironage_bench_single_biomesoplenty_mangrove);
				FurnitureFactory.AddBenchRecipe(PALM, BlockObjectHolder.chair_wood_ironage_bench_single_biomesoplenty_palm);
				FurnitureFactory.AddBenchRecipe(PINE, BlockObjectHolder.chair_wood_ironage_bench_single_biomesoplenty_pine);
				FurnitureFactory.AddBenchRecipe(REDWOOD, BlockObjectHolder.chair_wood_ironage_bench_single_biomesoplenty_redwood);
				FurnitureFactory.AddBenchRecipe(SACRED_OAK, BlockObjectHolder.chair_wood_ironage_bench_single_biomesoplenty_sacred_oak);
				FurnitureFactory.AddBenchRecipe(UMBRAN, BlockObjectHolder.chair_wood_ironage_bench_single_biomesoplenty_umbran);
				FurnitureFactory.AddBenchRecipe(WILLOW, BlockObjectHolder.chair_wood_ironage_bench_single_biomesoplenty_willow);
			
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_single_biomesoplenty_cherry, BlockObjectHolder.chair_wood_ironage_bench_padded_single_biomesoplenty_cherry);
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_single_biomesoplenty_ebony, BlockObjectHolder.chair_wood_ironage_bench_padded_single_biomesoplenty_ebony);
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_single_biomesoplenty_ethereal, BlockObjectHolder.chair_wood_ironage_bench_padded_single_biomesoplenty_ethereal);
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_single_biomesoplenty_eucalyptus, BlockObjectHolder.chair_wood_ironage_bench_padded_single_biomesoplenty_eucalyptus);
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_single_biomesoplenty_fir, BlockObjectHolder.chair_wood_ironage_bench_padded_single_biomesoplenty_fir);
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_single_biomesoplenty_hellbark, BlockObjectHolder.chair_wood_ironage_bench_padded_single_biomesoplenty_hellbark);
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_single_biomesoplenty_jacaranda, BlockObjectHolder.chair_wood_ironage_bench_padded_single_biomesoplenty_jacaranda);
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_single_biomesoplenty_magic, BlockObjectHolder.chair_wood_ironage_bench_padded_single_biomesoplenty_magic);
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_single_biomesoplenty_mahogany, BlockObjectHolder.chair_wood_ironage_bench_padded_single_biomesoplenty_mahogany);
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_single_biomesoplenty_mangrove, BlockObjectHolder.chair_wood_ironage_bench_padded_single_biomesoplenty_mangrove);
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_single_biomesoplenty_palm, BlockObjectHolder.chair_wood_ironage_bench_padded_single_biomesoplenty_palm);
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_single_biomesoplenty_pine, BlockObjectHolder.chair_wood_ironage_bench_padded_single_biomesoplenty_pine);
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_single_biomesoplenty_redwood, BlockObjectHolder.chair_wood_ironage_bench_padded_single_biomesoplenty_redwood);
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_single_biomesoplenty_sacred_oak, BlockObjectHolder.chair_wood_ironage_bench_padded_single_biomesoplenty_sacred_oak);
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_single_biomesoplenty_umbran, BlockObjectHolder.chair_wood_ironage_bench_padded_single_biomesoplenty_umbran);
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_single_biomesoplenty_willow, BlockObjectHolder.chair_wood_ironage_bench_padded_single_biomesoplenty_willow);	
			
				FurnitureFactory.AddLogBenchRecipe(CHERRY_LOG, BlockObjectHolder.chair_wood_ironage_bench_log_single_biomesoplenty_cherry);
				FurnitureFactory.AddLogBenchRecipe(EBONY_LOG, BlockObjectHolder.chair_wood_ironage_bench_log_single_biomesoplenty_ebony);
				FurnitureFactory.AddLogBenchRecipe(ETHEREAL_LOG, BlockObjectHolder.chair_wood_ironage_bench_log_single_biomesoplenty_ethereal);
				FurnitureFactory.AddLogBenchRecipe(EUCALYPTUS_LOG, BlockObjectHolder.chair_wood_ironage_bench_log_single_biomesoplenty_eucalyptus);
				FurnitureFactory.AddLogBenchRecipe(FIR_LOG, BlockObjectHolder.chair_wood_ironage_bench_log_single_biomesoplenty_fir);
				FurnitureFactory.AddLogBenchRecipe(HELLBARK_LOG, BlockObjectHolder.chair_wood_ironage_bench_log_single_biomesoplenty_hellbark);
				FurnitureFactory.AddLogBenchRecipe(JACARANDA_LOG, BlockObjectHolder.chair_wood_ironage_bench_log_single_biomesoplenty_jacaranda);
				FurnitureFactory.AddLogBenchRecipe(MAGIC_LOG, BlockObjectHolder.chair_wood_ironage_bench_log_single_biomesoplenty_magic);
				FurnitureFactory.AddLogBenchRecipe(MAHOGANY_LOG, BlockObjectHolder.chair_wood_ironage_bench_log_single_biomesoplenty_mahogany);
				FurnitureFactory.AddLogBenchRecipe(MANGROVE_LOG, BlockObjectHolder.chair_wood_ironage_bench_log_single_biomesoplenty_mangrove);
				FurnitureFactory.AddLogBenchRecipe(PALM_LOG, BlockObjectHolder.chair_wood_ironage_bench_log_single_biomesoplenty_palm);
				FurnitureFactory.AddLogBenchRecipe(PINE_LOG, BlockObjectHolder.chair_wood_ironage_bench_log_single_biomesoplenty_pine);
				FurnitureFactory.AddLogBenchRecipe(REDWOOD_LOG, BlockObjectHolder.chair_wood_ironage_bench_log_single_biomesoplenty_redwood);
				FurnitureFactory.AddLogBenchRecipe(SACRED_OAK_LOG, BlockObjectHolder.chair_wood_ironage_bench_log_single_biomesoplenty_sacred_oak);
				FurnitureFactory.AddLogBenchRecipe(UMBRAN_LOG, BlockObjectHolder.chair_wood_ironage_bench_log_single_biomesoplenty_umbran);
				FurnitureFactory.AddLogBenchRecipe(WILLOW_LOG, BlockObjectHolder.chair_wood_ironage_bench_log_single_biomesoplenty_willow);
				
				FurnitureFactory.AddBackBenchRecipe(CHERRY, BlockObjectHolder.chair_wood_ironage_bench_back_single_biomesoplenty_cherry);
				FurnitureFactory.AddBackBenchRecipe(EBONY, BlockObjectHolder.chair_wood_ironage_bench_back_single_biomesoplenty_ebony);
				FurnitureFactory.AddBackBenchRecipe(ETHEREAL, BlockObjectHolder.chair_wood_ironage_bench_back_single_biomesoplenty_ethereal);
				FurnitureFactory.AddBackBenchRecipe(EUCALYPTUS, BlockObjectHolder.chair_wood_ironage_bench_back_single_biomesoplenty_eucalyptus);
				FurnitureFactory.AddBackBenchRecipe(FIR, BlockObjectHolder.chair_wood_ironage_bench_back_single_biomesoplenty_fir);
				FurnitureFactory.AddBackBenchRecipe(HELLBARK, BlockObjectHolder.chair_wood_ironage_bench_back_single_biomesoplenty_hellbark);
				FurnitureFactory.AddBackBenchRecipe(JACARANDA, BlockObjectHolder.chair_wood_ironage_bench_back_single_biomesoplenty_jacaranda);
				FurnitureFactory.AddBackBenchRecipe(MAGIC, BlockObjectHolder.chair_wood_ironage_bench_back_single_biomesoplenty_magic);
				FurnitureFactory.AddBackBenchRecipe(MAHOGANY, BlockObjectHolder.chair_wood_ironage_bench_back_single_biomesoplenty_mahogany);
				FurnitureFactory.AddBackBenchRecipe(MANGROVE, BlockObjectHolder.chair_wood_ironage_bench_back_single_biomesoplenty_mangrove);
				FurnitureFactory.AddBackBenchRecipe(PALM, BlockObjectHolder.chair_wood_ironage_bench_back_single_biomesoplenty_palm);
				FurnitureFactory.AddBackBenchRecipe(PINE, BlockObjectHolder.chair_wood_ironage_bench_back_single_biomesoplenty_pine);
				FurnitureFactory.AddBackBenchRecipe(REDWOOD, BlockObjectHolder.chair_wood_ironage_bench_back_single_biomesoplenty_redwood);
				FurnitureFactory.AddBackBenchRecipe(SACRED_OAK, BlockObjectHolder.chair_wood_ironage_bench_back_single_biomesoplenty_sacred_oak);
				FurnitureFactory.AddBackBenchRecipe(UMBRAN, BlockObjectHolder.chair_wood_ironage_bench_back_single_biomesoplenty_umbran);
				FurnitureFactory.AddBackBenchRecipe(WILLOW, BlockObjectHolder.chair_wood_ironage_bench_back_single_biomesoplenty_willow);
				
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_back_single_biomesoplenty_cherry, BlockObjectHolder.chair_wood_ironage_bench_back_padded_single_biomesoplenty_cherry);
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_back_single_biomesoplenty_ebony, BlockObjectHolder.chair_wood_ironage_bench_back_padded_single_biomesoplenty_ebony);
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_back_single_biomesoplenty_ethereal, BlockObjectHolder.chair_wood_ironage_bench_back_padded_single_biomesoplenty_ethereal);
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_back_single_biomesoplenty_eucalyptus, BlockObjectHolder.chair_wood_ironage_bench_back_padded_single_biomesoplenty_eucalyptus);
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_back_single_biomesoplenty_fir, BlockObjectHolder.chair_wood_ironage_bench_back_padded_single_biomesoplenty_fir);
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_back_single_biomesoplenty_hellbark, BlockObjectHolder.chair_wood_ironage_bench_back_padded_single_biomesoplenty_hellbark);
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_back_single_biomesoplenty_jacaranda, BlockObjectHolder.chair_wood_ironage_bench_back_padded_single_biomesoplenty_jacaranda);
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_back_single_biomesoplenty_magic, BlockObjectHolder.chair_wood_ironage_bench_back_padded_single_biomesoplenty_magic);
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_back_single_biomesoplenty_mahogany, BlockObjectHolder.chair_wood_ironage_bench_back_padded_single_biomesoplenty_mahogany);
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_back_single_biomesoplenty_mangrove, BlockObjectHolder.chair_wood_ironage_bench_back_padded_single_biomesoplenty_mangrove);
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_back_single_biomesoplenty_palm, BlockObjectHolder.chair_wood_ironage_bench_back_padded_single_biomesoplenty_palm);
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_back_single_biomesoplenty_pine, BlockObjectHolder.chair_wood_ironage_bench_back_padded_single_biomesoplenty_pine);
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_back_single_biomesoplenty_redwood, BlockObjectHolder.chair_wood_ironage_bench_back_padded_single_biomesoplenty_redwood);
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_back_single_biomesoplenty_sacred_oak, BlockObjectHolder.chair_wood_ironage_bench_back_padded_single_biomesoplenty_sacred_oak);
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_back_single_biomesoplenty_umbran, BlockObjectHolder.chair_wood_ironage_bench_back_padded_single_biomesoplenty_umbran);
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_back_single_biomesoplenty_willow, BlockObjectHolder.chair_wood_ironage_bench_back_padded_single_biomesoplenty_willow);
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
			
			Block logs = Block.getBlockFromName("natura:overworld_logs");
			
			ItemStack MAPLE_LOG = new ItemStack(logs, 1, 0);
			ItemStack SILVERBELL_LOG = new ItemStack(logs, 1, 1);
			ItemStack AMARANTH_LOG = new ItemStack(logs, 1, 2);
			ItemStack TIGER_LOG = new ItemStack(logs, 1, 3);
			
			Block logs2 = Block.getBlockFromName("natura:overworld_logs2");
			
			ItemStack WILLOW_LOG = new ItemStack(logs2, 1, 0);
			ItemStack EUCALYPTUS_LOG = new ItemStack(logs2, 1, 1);
			ItemStack HOPSEED_LOG = new ItemStack(logs2, 1, 2);
			ItemStack SAKURA_LOG = new ItemStack(logs2, 1, 3);
			
			Block logs3 = Block.getBlockFromName("natura:nether_logs");
			
			ItemStack GHOSTWOOD_LOG = new ItemStack(logs3, 1, 0);
			ItemStack FUSEWOOD_LOG = new ItemStack(logs3, 1, 2);
			ItemStack DARKWOOD_LOG = new ItemStack(logs3, 1, 1);
			
			Block logs4 = Block.getBlockFromName("natura:nether_logs2");
			
			ItemStack BLOODWOOD_LOG = new ItemStack(logs4, 1, 0);
			
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
			
			if (IronAgeFurnitureConfiguration.GENERATE_WOOD_BENCHES) {
				FurnitureFactory.AddBenchRecipe(MAPLE, BlockObjectHolder.chair_wood_ironage_bench_single_natura_maple);
				FurnitureFactory.AddBenchRecipe(SILVERBELL, BlockObjectHolder.chair_wood_ironage_bench_single_natura_silverbell);
				FurnitureFactory.AddBenchRecipe(AMARANTH, BlockObjectHolder.chair_wood_ironage_bench_single_natura_amaranth);
				FurnitureFactory.AddBenchRecipe(TIGER, BlockObjectHolder.chair_wood_ironage_bench_single_natura_tiger);
				FurnitureFactory.AddBenchRecipe(WILLOW, BlockObjectHolder.chair_wood_ironage_bench_single_natura_willow);
				FurnitureFactory.AddBenchRecipe(EUCALYPTUS, BlockObjectHolder.chair_wood_ironage_bench_single_natura_eucalyptus);
				FurnitureFactory.AddBenchRecipe(HOPSEED, BlockObjectHolder.chair_wood_ironage_bench_single_natura_hopseed);
				FurnitureFactory.AddBenchRecipe(SAKURA, BlockObjectHolder.chair_wood_ironage_bench_single_natura_sakura);
				FurnitureFactory.AddBenchRecipe(REDWOOD, BlockObjectHolder.chair_wood_ironage_bench_single_natura_redwood);
				FurnitureFactory.AddBenchRecipe(GHOSTWOOD, BlockObjectHolder.chair_wood_ironage_bench_single_natura_ghostwood);
				FurnitureFactory.AddBenchRecipe(BLOODWOOD, BlockObjectHolder.chair_wood_ironage_bench_single_natura_bloodwood);
				FurnitureFactory.AddBenchRecipe(FUSEWOOD, BlockObjectHolder.chair_wood_ironage_bench_single_natura_fusewood);
				FurnitureFactory.AddBenchRecipe(DARKWOOD, BlockObjectHolder.chair_wood_ironage_bench_single_natura_darkwood);
			
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_single_natura_eucalyptus, BlockObjectHolder.chair_wood_ironage_bench_padded_single_natura_eucalyptus);
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_single_natura_amaranth, BlockObjectHolder.chair_wood_ironage_bench_padded_single_natura_amaranth);
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_single_natura_hopseed, BlockObjectHolder.chair_wood_ironage_bench_padded_single_natura_hopseed);
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_single_natura_maple, BlockObjectHolder.chair_wood_ironage_bench_padded_single_natura_maple);
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_single_natura_redwood, BlockObjectHolder.chair_wood_ironage_bench_padded_single_natura_redwood);
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_single_natura_sakura, BlockObjectHolder.chair_wood_ironage_bench_padded_single_natura_sakura);
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_single_natura_silverbell, BlockObjectHolder.chair_wood_ironage_bench_padded_single_natura_silverbell);
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_single_natura_tiger, BlockObjectHolder.chair_wood_ironage_bench_padded_single_natura_tiger);
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_single_natura_willow, BlockObjectHolder.chair_wood_ironage_bench_padded_single_natura_willow);
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_single_natura_bloodwood, BlockObjectHolder.chair_wood_ironage_bench_padded_single_natura_bloodwood);
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_single_natura_darkwood, BlockObjectHolder.chair_wood_ironage_bench_padded_single_natura_darkwood);
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_single_natura_fusewood, BlockObjectHolder.chair_wood_ironage_bench_padded_single_natura_fusewood);
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_single_natura_ghostwood, BlockObjectHolder.chair_wood_ironage_bench_padded_single_natura_ghostwood);
			
				FurnitureFactory.AddLogBenchRecipe(MAPLE_LOG, BlockObjectHolder.chair_wood_ironage_bench_log_single_natura_maple);
				FurnitureFactory.AddLogBenchRecipe(SILVERBELL_LOG, BlockObjectHolder.chair_wood_ironage_bench_log_single_natura_silverbell);
				FurnitureFactory.AddLogBenchRecipe(AMARANTH_LOG, BlockObjectHolder.chair_wood_ironage_bench_log_single_natura_amaranth);
				FurnitureFactory.AddLogBenchRecipe(TIGER_LOG, BlockObjectHolder.chair_wood_ironage_bench_log_single_natura_tiger);
				FurnitureFactory.AddLogBenchRecipe(WILLOW_LOG, BlockObjectHolder.chair_wood_ironage_bench_log_single_natura_willow);
				FurnitureFactory.AddLogBenchRecipe(EUCALYPTUS_LOG, BlockObjectHolder.chair_wood_ironage_bench_log_single_natura_eucalyptus);
				FurnitureFactory.AddLogBenchRecipe(HOPSEED_LOG, BlockObjectHolder.chair_wood_ironage_bench_log_single_natura_hopseed);
				FurnitureFactory.AddLogBenchRecipe(SAKURA_LOG, BlockObjectHolder.chair_wood_ironage_bench_log_single_natura_sakura);
				FurnitureFactory.AddLogBenchRecipe(GHOSTWOOD_LOG, BlockObjectHolder.chair_wood_ironage_bench_log_single_natura_ghostwood);
				FurnitureFactory.AddLogBenchRecipe(BLOODWOOD_LOG, BlockObjectHolder.chair_wood_ironage_bench_log_single_natura_bloodwood);
				FurnitureFactory.AddLogBenchRecipe(FUSEWOOD_LOG, BlockObjectHolder.chair_wood_ironage_bench_log_single_natura_fusewood);
				FurnitureFactory.AddLogBenchRecipe(DARKWOOD_LOG, BlockObjectHolder.chair_wood_ironage_bench_log_single_natura_darkwood);
			
				FurnitureFactory.AddBackBenchRecipe(MAPLE, BlockObjectHolder.chair_wood_ironage_bench_back_single_natura_maple);
				FurnitureFactory.AddBackBenchRecipe(SILVERBELL, BlockObjectHolder.chair_wood_ironage_bench_back_single_natura_silverbell);
				FurnitureFactory.AddBackBenchRecipe(AMARANTH, BlockObjectHolder.chair_wood_ironage_bench_back_single_natura_amaranth);
				FurnitureFactory.AddBackBenchRecipe(TIGER, BlockObjectHolder.chair_wood_ironage_bench_back_single_natura_tiger);
				FurnitureFactory.AddBackBenchRecipe(WILLOW, BlockObjectHolder.chair_wood_ironage_bench_back_single_natura_willow);
				FurnitureFactory.AddBackBenchRecipe(EUCALYPTUS, BlockObjectHolder.chair_wood_ironage_bench_back_single_natura_eucalyptus);
				FurnitureFactory.AddBackBenchRecipe(HOPSEED, BlockObjectHolder.chair_wood_ironage_bench_back_single_natura_hopseed);
				FurnitureFactory.AddBackBenchRecipe(SAKURA, BlockObjectHolder.chair_wood_ironage_bench_back_single_natura_sakura);
				FurnitureFactory.AddBackBenchRecipe(GHOSTWOOD, BlockObjectHolder.chair_wood_ironage_bench_back_single_natura_ghostwood);
				FurnitureFactory.AddBackBenchRecipe(BLOODWOOD, BlockObjectHolder.chair_wood_ironage_bench_back_single_natura_bloodwood);
				FurnitureFactory.AddBackBenchRecipe(FUSEWOOD, BlockObjectHolder.chair_wood_ironage_bench_back_single_natura_fusewood);
				FurnitureFactory.AddBackBenchRecipe(DARKWOOD, BlockObjectHolder.chair_wood_ironage_bench_back_single_natura_darkwood);
			
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_back_single_natura_eucalyptus, BlockObjectHolder.chair_wood_ironage_bench_back_padded_single_natura_eucalyptus);
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_back_single_natura_amaranth, BlockObjectHolder.chair_wood_ironage_bench_back_padded_single_natura_amaranth);
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_back_single_natura_hopseed, BlockObjectHolder.chair_wood_ironage_bench_back_padded_single_natura_hopseed);
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_back_single_natura_maple, BlockObjectHolder.chair_wood_ironage_bench_back_padded_single_natura_maple);
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_back_single_natura_redwood, BlockObjectHolder.chair_wood_ironage_bench_back_padded_single_natura_redwood);
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_back_single_natura_sakura, BlockObjectHolder.chair_wood_ironage_bench_back_padded_single_natura_sakura);
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_back_single_natura_silverbell, BlockObjectHolder.chair_wood_ironage_bench_back_padded_single_natura_silverbell);
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_back_single_natura_tiger, BlockObjectHolder.chair_wood_ironage_bench_back_padded_single_natura_tiger);
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_back_single_natura_willow, BlockObjectHolder.chair_wood_ironage_bench_back_padded_single_natura_willow);
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_back_single_natura_bloodwood, BlockObjectHolder.chair_wood_ironage_bench_back_padded_single_natura_bloodwood);
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_back_single_natura_darkwood, BlockObjectHolder.chair_wood_ironage_bench_back_padded_single_natura_darkwood);
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_back_single_natura_fusewood, BlockObjectHolder.chair_wood_ironage_bench_back_padded_single_natura_fusewood);
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_back_single_natura_ghostwood, BlockObjectHolder.chair_wood_ironage_bench_back_padded_single_natura_ghostwood);
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
			
			if (IronAgeFurnitureConfiguration.GENERATE_WOOD_BENCHES) {
				FurnitureFactory.AddBenchRecipe(HORIZONTAL, BlockObjectHolder.chair_wood_ironage_bench_single_immersiveengineering_treatedWood);
				FurnitureFactory.AddBenchRecipe(VERTICAL, BlockObjectHolder.chair_wood_ironage_bench_single_immersiveengineering_treatedWood);
				FurnitureFactory.AddBenchRecipe(PACKAGED, BlockObjectHolder.chair_wood_ironage_bench_single_immersiveengineering_treatedWood);
				
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_single_immersiveengineering_treatedWood, BlockObjectHolder.chair_wood_ironage_bench_padded_single_immersiveengineering_treatedWood);
				FurnitureFactory.AddBackBenchRecipe(PACKAGED, BlockObjectHolder.chair_wood_ironage_bench_back_single_immersiveengineering_treatedWood);
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_back_single_immersiveengineering_treatedWood, BlockObjectHolder.chair_wood_ironage_bench_back_padded_single_immersiveengineering_treatedWood);
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

			Block logs = Block.getBlockFromName("forestry:logs.0");
			
			ItemStack LARCH_LOG = new ItemStack(logs, 1, 0);
			ItemStack TEAK_LOG = new ItemStack(logs, 1, 1);
			ItemStack ACACIA_LOG = new ItemStack(logs, 1, 2);
			ItemStack LIME_LOG = new ItemStack(logs, 1, 3);
			
			Block logs1 = Block.getBlockFromName("forestry:logs.1");
			
			ItemStack CHESTNUT_LOG = new ItemStack(logs1, 1, 0);
			ItemStack WENGE_LOG = new ItemStack(logs1, 1, 1);
			ItemStack BAOBAB_LOG = new ItemStack(logs1, 1, 2);
			ItemStack SEQUOIA_LOG = new ItemStack(logs1, 1, 3);
			
			Block logs2 = Block.getBlockFromName("forestry:logs.2");
			
			ItemStack KAPOK_LOG = new ItemStack(logs2, 1, 0);
			ItemStack EBONY_LOG = new ItemStack(logs2, 1, 1);
			ItemStack MAHOGANY_LOG = new ItemStack(logs2, 1, 2);
			ItemStack BALSA_LOG = new ItemStack(logs2, 1, 3);
			
			Block logs3 = Block.getBlockFromName("forestry:logs.3");
			
			ItemStack WILLOW_LOG = new ItemStack(logs3, 1, 0);
			ItemStack WALNUT_LOG = new ItemStack(logs3, 1, 1);
			ItemStack GREENHEART_LOG = new ItemStack(logs3, 1, 2);
			ItemStack CHERRY_LOG = new ItemStack(logs3, 1, 3);
			
			Block logs4 = Block.getBlockFromName("forestry:logs.4");
			
			ItemStack MAHOE_LOG = new ItemStack(logs4, 1, 0);
			ItemStack POPLAR_LOG = new ItemStack(logs4, 1, 1);
			ItemStack PALM_LOG = new ItemStack(logs4, 1, 2);
			ItemStack PAPAYA_LOG = new ItemStack(logs4, 1, 3);
			
			Block logs5 = Block.getBlockFromName("forestry:logs.5");
			
			ItemStack PINE_LOG = new ItemStack(logs5, 1, 0);
			ItemStack PLUM_LOG = new ItemStack(logs5, 1, 1);
			ItemStack MAPLE_LOG = new ItemStack(logs5, 1, 2);
			ItemStack CITRUS_LOG = new ItemStack(logs5, 1, 3);
			
			Block logs6 = Block.getBlockFromName("forestry:logs.6");
			
			ItemStack GIGANTEUM_LOG = new ItemStack(logs6, 1, 0);
			ItemStack IPE_LOG = new ItemStack(logs6, 1, 1);
			ItemStack PADAUK_LOG = new ItemStack(logs6, 1, 2);
			ItemStack COCOBOLO_LOG = new ItemStack(logs6, 1, 3);
			
			Block logs7 = Block.getBlockFromName("forestry:logs.7");
			
			ItemStack ZEBRAWOOD_LOG = new ItemStack(logs7, 1, 0);
			
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
			
			if (IronAgeFurnitureConfiguration.GENERATE_WOOD_BENCHES) {
				FurnitureFactory.AddBenchRecipe(ACACIA, BlockObjectHolder.chair_wood_ironage_bench_single_forestry_acacia);
				FurnitureFactory.AddBenchRecipe(BALSA, BlockObjectHolder.chair_wood_ironage_bench_single_forestry_balsa);
				FurnitureFactory.AddBenchRecipe(BAOBAB, BlockObjectHolder.chair_wood_ironage_bench_single_forestry_baobab);
				FurnitureFactory.AddBenchRecipe(CHERRY, BlockObjectHolder.chair_wood_ironage_bench_single_forestry_cherry);
				FurnitureFactory.AddBenchRecipe(CHESTNUT, BlockObjectHolder.chair_wood_ironage_bench_single_forestry_chestnut);
				FurnitureFactory.AddBenchRecipe(CITRUS, BlockObjectHolder.chair_wood_ironage_bench_single_forestry_citrus);
				FurnitureFactory.AddBenchRecipe(COCOBOLO, BlockObjectHolder.chair_wood_ironage_bench_single_forestry_cocobolo);
				FurnitureFactory.AddBenchRecipe(EBONY, BlockObjectHolder.chair_wood_ironage_bench_single_forestry_ebony);
				FurnitureFactory.AddBenchRecipe(GIGANTEUM, BlockObjectHolder.chair_wood_ironage_bench_single_forestry_giganteum);
				FurnitureFactory.AddBenchRecipe(GREENHEART, BlockObjectHolder.chair_wood_ironage_bench_single_forestry_greenheart);
				FurnitureFactory.AddBenchRecipe(IPE, BlockObjectHolder.chair_wood_ironage_bench_single_forestry_ipe);
				FurnitureFactory.AddBenchRecipe(KAPOK, BlockObjectHolder.chair_wood_ironage_bench_single_forestry_kapok);
				FurnitureFactory.AddBenchRecipe(LARCH, BlockObjectHolder.chair_wood_ironage_bench_single_forestry_larch);
				FurnitureFactory.AddBenchRecipe(LIME, BlockObjectHolder.chair_wood_ironage_bench_single_forestry_lime);
				FurnitureFactory.AddBenchRecipe(MAHOE, BlockObjectHolder.chair_wood_ironage_bench_single_forestry_mahoe);
				FurnitureFactory.AddBenchRecipe(MAHOGANY, BlockObjectHolder.chair_wood_ironage_bench_single_forestry_mahogany);
				FurnitureFactory.AddBenchRecipe(MAPLE, BlockObjectHolder.chair_wood_ironage_bench_single_forestry_maple);
				FurnitureFactory.AddBenchRecipe(PADAUK, BlockObjectHolder.chair_wood_ironage_bench_single_forestry_padauk);
				FurnitureFactory.AddBenchRecipe(PALM, BlockObjectHolder.chair_wood_ironage_bench_single_forestry_palm);
				FurnitureFactory.AddBenchRecipe(PAPAYA, BlockObjectHolder.chair_wood_ironage_bench_single_forestry_papaya);
				FurnitureFactory.AddBenchRecipe(PINE, BlockObjectHolder.chair_wood_ironage_bench_single_forestry_pine);
				FurnitureFactory.AddBenchRecipe(PLUM, BlockObjectHolder.chair_wood_ironage_bench_single_forestry_plum);
				FurnitureFactory.AddBenchRecipe(POPLAR, BlockObjectHolder.chair_wood_ironage_bench_single_forestry_poplar);
				FurnitureFactory.AddBenchRecipe(SEQUOIA, BlockObjectHolder.chair_wood_ironage_bench_single_forestry_sequoia);
				FurnitureFactory.AddBenchRecipe(TEAK, BlockObjectHolder.chair_wood_ironage_bench_single_forestry_teak);
				FurnitureFactory.AddBenchRecipe(WALNUT, BlockObjectHolder.chair_wood_ironage_bench_single_forestry_walnut);
				FurnitureFactory.AddBenchRecipe(WENGE, BlockObjectHolder.chair_wood_ironage_bench_single_forestry_wenge);
				FurnitureFactory.AddBenchRecipe(WILLOW, BlockObjectHolder.chair_wood_ironage_bench_single_forestry_willow);
				FurnitureFactory.AddBenchRecipe(ZEBRAWOOD, BlockObjectHolder.chair_wood_ironage_bench_single_forestry_zebrawood);
			
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_single_forestry_acacia, BlockObjectHolder.chair_wood_ironage_bench_padded_single_forestry_acacia);
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_single_forestry_balsa, BlockObjectHolder.chair_wood_ironage_bench_padded_single_forestry_balsa);
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_single_forestry_baobab, BlockObjectHolder.chair_wood_ironage_bench_padded_single_forestry_baobab);
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_single_forestry_cherry, BlockObjectHolder.chair_wood_ironage_bench_padded_single_forestry_cherry);
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_single_forestry_chestnut, BlockObjectHolder.chair_wood_ironage_bench_padded_single_forestry_chestnut);
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_single_forestry_citrus, BlockObjectHolder.chair_wood_ironage_bench_padded_single_forestry_citrus);
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_single_forestry_cocobolo, BlockObjectHolder.chair_wood_ironage_bench_padded_single_forestry_cocobolo);
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_single_forestry_ebony, BlockObjectHolder.chair_wood_ironage_bench_padded_single_forestry_ebony);
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_single_forestry_giganteum, BlockObjectHolder.chair_wood_ironage_bench_padded_single_forestry_giganteum);
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_single_forestry_greenheart, BlockObjectHolder.chair_wood_ironage_bench_padded_single_forestry_greenheart);
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_single_forestry_ipe, BlockObjectHolder.chair_wood_ironage_bench_padded_single_forestry_ipe);
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_single_forestry_kapok, BlockObjectHolder.chair_wood_ironage_bench_padded_single_forestry_kapok);
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_single_forestry_larch, BlockObjectHolder.chair_wood_ironage_bench_padded_single_forestry_larch);
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_single_forestry_lime, BlockObjectHolder.chair_wood_ironage_bench_padded_single_forestry_lime);
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_single_forestry_mahoe, BlockObjectHolder.chair_wood_ironage_bench_padded_single_forestry_mahoe);
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_single_forestry_mahogany, BlockObjectHolder.chair_wood_ironage_bench_padded_single_forestry_mahogany);
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_single_forestry_maple, BlockObjectHolder.chair_wood_ironage_bench_padded_single_forestry_maple);
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_single_forestry_padauk, BlockObjectHolder.chair_wood_ironage_bench_padded_single_forestry_padauk);
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_single_forestry_palm, BlockObjectHolder.chair_wood_ironage_bench_padded_single_forestry_palm);
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_single_forestry_papaya, BlockObjectHolder.chair_wood_ironage_bench_padded_single_forestry_papaya);
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_single_forestry_pine, BlockObjectHolder.chair_wood_ironage_bench_padded_single_forestry_pine);
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_single_forestry_plum, BlockObjectHolder.chair_wood_ironage_bench_padded_single_forestry_plum);
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_single_forestry_poplar, BlockObjectHolder.chair_wood_ironage_bench_padded_single_forestry_poplar);
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_single_forestry_sequoia, BlockObjectHolder.chair_wood_ironage_bench_padded_single_forestry_sequoia);
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_single_forestry_teak, BlockObjectHolder.chair_wood_ironage_bench_padded_single_forestry_teak);
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_single_forestry_walnut, BlockObjectHolder.chair_wood_ironage_bench_padded_single_forestry_walnut);
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_single_forestry_wenge, BlockObjectHolder.chair_wood_ironage_bench_padded_single_forestry_wenge);
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_single_forestry_willow, BlockObjectHolder.chair_wood_ironage_bench_padded_single_forestry_willow);
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_single_forestry_zebrawood, BlockObjectHolder.chair_wood_ironage_bench_padded_single_forestry_zebrawood);
			
				FurnitureFactory.AddLogBenchRecipe(ACACIA_LOG, BlockObjectHolder.chair_wood_ironage_bench_log_single_forestry_acacia);
				FurnitureFactory.AddLogBenchRecipe(BALSA_LOG, BlockObjectHolder.chair_wood_ironage_bench_log_single_forestry_balsa);
				FurnitureFactory.AddLogBenchRecipe(BAOBAB_LOG, BlockObjectHolder.chair_wood_ironage_bench_log_single_forestry_baobab);
				FurnitureFactory.AddLogBenchRecipe(CHERRY_LOG, BlockObjectHolder.chair_wood_ironage_bench_log_single_forestry_cherry);
				FurnitureFactory.AddLogBenchRecipe(CHESTNUT_LOG, BlockObjectHolder.chair_wood_ironage_bench_log_single_forestry_chestnut);
				FurnitureFactory.AddLogBenchRecipe(CITRUS_LOG, BlockObjectHolder.chair_wood_ironage_bench_log_single_forestry_citrus);
				FurnitureFactory.AddLogBenchRecipe(COCOBOLO_LOG, BlockObjectHolder.chair_wood_ironage_bench_log_single_forestry_cocobolo);
				FurnitureFactory.AddLogBenchRecipe(EBONY_LOG, BlockObjectHolder.chair_wood_ironage_bench_log_single_forestry_ebony);
				FurnitureFactory.AddLogBenchRecipe(GIGANTEUM_LOG, BlockObjectHolder.chair_wood_ironage_bench_log_single_forestry_giganteum);
				FurnitureFactory.AddLogBenchRecipe(GREENHEART_LOG, BlockObjectHolder.chair_wood_ironage_bench_log_single_forestry_greenheart);
				FurnitureFactory.AddLogBenchRecipe(IPE_LOG, BlockObjectHolder.chair_wood_ironage_bench_log_single_forestry_ipe);
				FurnitureFactory.AddLogBenchRecipe(KAPOK_LOG, BlockObjectHolder.chair_wood_ironage_bench_log_single_forestry_kapok);
				FurnitureFactory.AddLogBenchRecipe(LARCH_LOG, BlockObjectHolder.chair_wood_ironage_bench_log_single_forestry_larch);
				FurnitureFactory.AddLogBenchRecipe(LIME_LOG, BlockObjectHolder.chair_wood_ironage_bench_log_single_forestry_lime);
				FurnitureFactory.AddLogBenchRecipe(MAHOE_LOG, BlockObjectHolder.chair_wood_ironage_bench_log_single_forestry_mahoe);
				FurnitureFactory.AddLogBenchRecipe(MAHOGANY_LOG, BlockObjectHolder.chair_wood_ironage_bench_log_single_forestry_mahogany);
				FurnitureFactory.AddLogBenchRecipe(MAPLE_LOG, BlockObjectHolder.chair_wood_ironage_bench_log_single_forestry_maple);
				FurnitureFactory.AddLogBenchRecipe(PADAUK_LOG, BlockObjectHolder.chair_wood_ironage_bench_log_single_forestry_padauk);
				FurnitureFactory.AddLogBenchRecipe(PALM_LOG, BlockObjectHolder.chair_wood_ironage_bench_log_single_forestry_palm);
				FurnitureFactory.AddLogBenchRecipe(PAPAYA_LOG, BlockObjectHolder.chair_wood_ironage_bench_log_single_forestry_papaya);
				FurnitureFactory.AddLogBenchRecipe(PINE_LOG, BlockObjectHolder.chair_wood_ironage_bench_log_single_forestry_pine);
				FurnitureFactory.AddLogBenchRecipe(PLUM_LOG, BlockObjectHolder.chair_wood_ironage_bench_log_single_forestry_plum);
				FurnitureFactory.AddLogBenchRecipe(POPLAR_LOG, BlockObjectHolder.chair_wood_ironage_bench_log_single_forestry_poplar);
				FurnitureFactory.AddLogBenchRecipe(SEQUOIA_LOG, BlockObjectHolder.chair_wood_ironage_bench_log_single_forestry_sequoia);
				FurnitureFactory.AddLogBenchRecipe(TEAK_LOG, BlockObjectHolder.chair_wood_ironage_bench_log_single_forestry_teak);
				FurnitureFactory.AddLogBenchRecipe(WALNUT_LOG, BlockObjectHolder.chair_wood_ironage_bench_log_single_forestry_walnut);
				FurnitureFactory.AddLogBenchRecipe(WENGE_LOG, BlockObjectHolder.chair_wood_ironage_bench_log_single_forestry_wenge);
				FurnitureFactory.AddLogBenchRecipe(WILLOW_LOG, BlockObjectHolder.chair_wood_ironage_bench_log_single_forestry_willow);
				FurnitureFactory.AddLogBenchRecipe(ZEBRAWOOD_LOG, BlockObjectHolder.chair_wood_ironage_bench_log_single_forestry_zebrawood);
			
				FurnitureFactory.AddBackBenchRecipe(ACACIA, BlockObjectHolder.chair_wood_ironage_bench_back_single_forestry_acacia);
				FurnitureFactory.AddBackBenchRecipe(BALSA, BlockObjectHolder.chair_wood_ironage_bench_back_single_forestry_balsa);
				FurnitureFactory.AddBackBenchRecipe(BAOBAB, BlockObjectHolder.chair_wood_ironage_bench_back_single_forestry_baobab);
				FurnitureFactory.AddBackBenchRecipe(CHERRY, BlockObjectHolder.chair_wood_ironage_bench_back_single_forestry_cherry);
				FurnitureFactory.AddBackBenchRecipe(CHESTNUT, BlockObjectHolder.chair_wood_ironage_bench_back_single_forestry_chestnut);
				FurnitureFactory.AddBackBenchRecipe(CITRUS, BlockObjectHolder.chair_wood_ironage_bench_back_single_forestry_citrus);
				FurnitureFactory.AddBackBenchRecipe(COCOBOLO, BlockObjectHolder.chair_wood_ironage_bench_back_single_forestry_cocobolo);
				FurnitureFactory.AddBackBenchRecipe(EBONY, BlockObjectHolder.chair_wood_ironage_bench_back_single_forestry_ebony);
				FurnitureFactory.AddBackBenchRecipe(GIGANTEUM, BlockObjectHolder.chair_wood_ironage_bench_back_single_forestry_giganteum);
				FurnitureFactory.AddBackBenchRecipe(GREENHEART, BlockObjectHolder.chair_wood_ironage_bench_back_single_forestry_greenheart);
				FurnitureFactory.AddBackBenchRecipe(IPE, BlockObjectHolder.chair_wood_ironage_bench_back_single_forestry_ipe);
				FurnitureFactory.AddBackBenchRecipe(KAPOK, BlockObjectHolder.chair_wood_ironage_bench_back_single_forestry_kapok);
				FurnitureFactory.AddBackBenchRecipe(LARCH, BlockObjectHolder.chair_wood_ironage_bench_back_single_forestry_larch);
				FurnitureFactory.AddBackBenchRecipe(LIME, BlockObjectHolder.chair_wood_ironage_bench_back_single_forestry_lime);
				FurnitureFactory.AddBackBenchRecipe(MAHOE, BlockObjectHolder.chair_wood_ironage_bench_back_single_forestry_mahoe);
				FurnitureFactory.AddBackBenchRecipe(MAHOGANY, BlockObjectHolder.chair_wood_ironage_bench_back_single_forestry_mahogany);
				FurnitureFactory.AddBackBenchRecipe(MAPLE, BlockObjectHolder.chair_wood_ironage_bench_back_single_forestry_maple);
				FurnitureFactory.AddBackBenchRecipe(PADAUK, BlockObjectHolder.chair_wood_ironage_bench_back_single_forestry_padauk);
				FurnitureFactory.AddBackBenchRecipe(PALM, BlockObjectHolder.chair_wood_ironage_bench_back_single_forestry_palm);
				FurnitureFactory.AddBackBenchRecipe(PAPAYA, BlockObjectHolder.chair_wood_ironage_bench_back_single_forestry_papaya);
				FurnitureFactory.AddBackBenchRecipe(PINE, BlockObjectHolder.chair_wood_ironage_bench_back_single_forestry_pine);
				FurnitureFactory.AddBackBenchRecipe(PLUM, BlockObjectHolder.chair_wood_ironage_bench_back_single_forestry_plum);
				FurnitureFactory.AddBackBenchRecipe(POPLAR, BlockObjectHolder.chair_wood_ironage_bench_back_single_forestry_poplar);
				FurnitureFactory.AddBackBenchRecipe(SEQUOIA, BlockObjectHolder.chair_wood_ironage_bench_back_single_forestry_sequoia);
				FurnitureFactory.AddBackBenchRecipe(TEAK, BlockObjectHolder.chair_wood_ironage_bench_back_single_forestry_teak);
				FurnitureFactory.AddBackBenchRecipe(WALNUT, BlockObjectHolder.chair_wood_ironage_bench_back_single_forestry_walnut);
				FurnitureFactory.AddBackBenchRecipe(WENGE, BlockObjectHolder.chair_wood_ironage_bench_back_single_forestry_wenge);
				FurnitureFactory.AddBackBenchRecipe(WILLOW, BlockObjectHolder.chair_wood_ironage_bench_back_single_forestry_willow);
				FurnitureFactory.AddBackBenchRecipe(ZEBRAWOOD, BlockObjectHolder.chair_wood_ironage_bench_back_single_forestry_zebrawood);
				
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_back_single_forestry_acacia, BlockObjectHolder.chair_wood_ironage_bench_back_padded_single_forestry_acacia);
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_back_single_forestry_balsa, BlockObjectHolder.chair_wood_ironage_bench_back_padded_single_forestry_balsa);
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_back_single_forestry_baobab, BlockObjectHolder.chair_wood_ironage_bench_back_padded_single_forestry_baobab);
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_back_single_forestry_cherry, BlockObjectHolder.chair_wood_ironage_bench_back_padded_single_forestry_cherry);
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_back_single_forestry_chestnut, BlockObjectHolder.chair_wood_ironage_bench_back_padded_single_forestry_chestnut);
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_back_single_forestry_citrus, BlockObjectHolder.chair_wood_ironage_bench_back_padded_single_forestry_citrus);
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_back_single_forestry_cocobolo, BlockObjectHolder.chair_wood_ironage_bench_back_padded_single_forestry_cocobolo);
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_back_single_forestry_ebony, BlockObjectHolder.chair_wood_ironage_bench_back_padded_single_forestry_ebony);
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_back_single_forestry_giganteum, BlockObjectHolder.chair_wood_ironage_bench_back_padded_single_forestry_giganteum);
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_back_single_forestry_greenheart, BlockObjectHolder.chair_wood_ironage_bench_back_padded_single_forestry_greenheart);
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_back_single_forestry_ipe, BlockObjectHolder.chair_wood_ironage_bench_back_padded_single_forestry_ipe);
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_back_single_forestry_kapok, BlockObjectHolder.chair_wood_ironage_bench_back_padded_single_forestry_kapok);
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_back_single_forestry_larch, BlockObjectHolder.chair_wood_ironage_bench_back_padded_single_forestry_larch);
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_back_single_forestry_lime, BlockObjectHolder.chair_wood_ironage_bench_back_padded_single_forestry_lime);
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_back_single_forestry_mahoe, BlockObjectHolder.chair_wood_ironage_bench_back_padded_single_forestry_mahoe);
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_back_single_forestry_mahogany, BlockObjectHolder.chair_wood_ironage_bench_back_padded_single_forestry_mahogany);
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_back_single_forestry_maple, BlockObjectHolder.chair_wood_ironage_bench_back_padded_single_forestry_maple);
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_back_single_forestry_padauk, BlockObjectHolder.chair_wood_ironage_bench_back_padded_single_forestry_padauk);
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_back_single_forestry_palm, BlockObjectHolder.chair_wood_ironage_bench_back_padded_single_forestry_palm);
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_back_single_forestry_papaya, BlockObjectHolder.chair_wood_ironage_bench_back_padded_single_forestry_papaya);
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_back_single_forestry_pine, BlockObjectHolder.chair_wood_ironage_bench_back_padded_single_forestry_pine);
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_back_single_forestry_plum, BlockObjectHolder.chair_wood_ironage_bench_back_padded_single_forestry_plum);
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_back_single_forestry_poplar, BlockObjectHolder.chair_wood_ironage_bench_back_padded_single_forestry_poplar);
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_back_single_forestry_sequoia, BlockObjectHolder.chair_wood_ironage_bench_back_padded_single_forestry_sequoia);
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_back_single_forestry_teak, BlockObjectHolder.chair_wood_ironage_bench_back_padded_single_forestry_teak);
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_back_single_forestry_walnut, BlockObjectHolder.chair_wood_ironage_bench_back_padded_single_forestry_walnut);
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_back_single_forestry_wenge, BlockObjectHolder.chair_wood_ironage_bench_back_padded_single_forestry_wenge);
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_back_single_forestry_willow, BlockObjectHolder.chair_wood_ironage_bench_back_padded_single_forestry_willow);
				FurnitureFactory.AddPaddedBenchRecipe(BlockObjectHolder.chair_wood_ironage_bench_back_single_forestry_zebrawood, BlockObjectHolder.chair_wood_ironage_bench_back_padded_single_forestry_zebrawood);
			}
		}
	}
}
