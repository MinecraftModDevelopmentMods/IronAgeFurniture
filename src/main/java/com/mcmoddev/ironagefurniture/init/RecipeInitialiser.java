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
		
		if (IronAgeFurnitureConfiguration.INTEGRATION_NATURA && Loader.isModLoaded("immersiveengineering")) {
			Block wood = Block.getBlockFromName("immersiveengineering:treatedWood");
			
	
			ItemStack HORIZONTAL = new ItemStack(wood, 1, 0);
			ItemStack VERTICAL = new ItemStack(wood, 1, 1);
			ItemStack PACKAGED = new ItemStack(wood, 1, 2);
			
			if (IronAgeFurnitureConfiguration.GENERATE_CLASSIC_CHAIRS) {
				AddClassicChairRecipe(HORIZONTAL, BlockObjectHolder.chair_wood_ironage_classic_immersiveengineering_treatedWood);
				AddClassicChairRecipe(VERTICAL, BlockObjectHolder.chair_wood_ironage_classic_immersiveengineering_treatedWood);
				AddClassicChairRecipe(PACKAGED, BlockObjectHolder.chair_wood_ironage_classic_immersiveengineering_treatedWood);
				
			}
			
			if (IronAgeFurnitureConfiguration.GENERATE_SHIELD_CHAIRS) {
				AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_immersiveengineering_treatedWood, BlockObjectHolder.chair_wood_ironage_shield_immersiveengineering_treatedWood);			
			}
			
			if (IronAgeFurnitureConfiguration.GENERATE_SHORT_STOOLS) {
				AddShortStoolRecipe(HORIZONTAL, BlockObjectHolder.chair_wood_ironage_stool_short_immersiveengineering_treatedWood);
				AddShortStoolRecipe(VERTICAL, BlockObjectHolder.chair_wood_ironage_stool_short_immersiveengineering_treatedWood);
				AddShortStoolRecipe(PACKAGED, BlockObjectHolder.chair_wood_ironage_stool_short_immersiveengineering_treatedWood);
				
			}
			
			if (IronAgeFurnitureConfiguration.GENERATE_TALL_STOOLS) {
				AddTallStoolRecipe(HORIZONTAL, BlockObjectHolder.chair_wood_ironage_stool_tall_immersiveengineering_treatedWood);
				AddTallStoolRecipe(VERTICAL, BlockObjectHolder.chair_wood_ironage_stool_tall_immersiveengineering_treatedWood);
				AddTallStoolRecipe(PACKAGED, BlockObjectHolder.chair_wood_ironage_stool_tall_immersiveengineering_treatedWood);
			}
		}
		
		if (IronAgeFurnitureConfiguration.INTEGRATION_NATURA && Loader.isModLoaded("forestry")) {
			Block wood = Block.getBlockFromName("forestry:planks.0");
			
	
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
			ItemStack MAHOE = new ItemStack(wood, 1, 16);
			ItemStack POPLAR = new ItemStack(wood, 1, 17);
			ItemStack PALM = new ItemStack(wood, 1, 18);
			ItemStack PAPAYA = new ItemStack(wood, 1, 19);
			ItemStack PINE = new ItemStack(wood, 1, 20);
			ItemStack PLUM = new ItemStack(wood, 1, 21);
			ItemStack MAPLE = new ItemStack(wood, 1, 22);
			ItemStack CITRUS = new ItemStack(wood, 1, 23);
			ItemStack GIGANTEUM = new ItemStack(wood, 1, 24);
			ItemStack IPE = new ItemStack(wood, 1, 25);
			ItemStack PADAUK = new ItemStack(wood, 1, 26);
			ItemStack COCOBOLO = new ItemStack(wood, 1, 27);
			ItemStack ZEBRAWOOD = new ItemStack(wood, 1, 28);

			
			if (IronAgeFurnitureConfiguration.GENERATE_CLASSIC_CHAIRS) {
				AddClassicChairRecipe(ACACIA, BlockObjectHolder.chair_wood_ironage_classic_forestry_acacia);
				AddClassicChairRecipe(BALSA, BlockObjectHolder.chair_wood_ironage_classic_forestry_balsa);
				AddClassicChairRecipe(BAOBAB, BlockObjectHolder.chair_wood_ironage_classic_forestry_baobab);
				AddClassicChairRecipe(CHERRY, BlockObjectHolder.chair_wood_ironage_classic_forestry_cherry);
				AddClassicChairRecipe(CHESTNUT, BlockObjectHolder.chair_wood_ironage_classic_forestry_chestnut);
				AddClassicChairRecipe(CITRUS, BlockObjectHolder.chair_wood_ironage_classic_forestry_citrus);
				AddClassicChairRecipe(COCOBOLO, BlockObjectHolder.chair_wood_ironage_classic_forestry_cocobolo);
				AddClassicChairRecipe(EBONY, BlockObjectHolder.chair_wood_ironage_classic_forestry_ebony);
				AddClassicChairRecipe(GIGANTEUM, BlockObjectHolder.chair_wood_ironage_classic_forestry_giganteum);
				AddClassicChairRecipe(GREENHEART, BlockObjectHolder.chair_wood_ironage_classic_forestry_greenheart);
				AddClassicChairRecipe(IPE, BlockObjectHolder.chair_wood_ironage_classic_forestry_ipe);
				AddClassicChairRecipe(KAPOK, BlockObjectHolder.chair_wood_ironage_classic_forestry_kapok);
				AddClassicChairRecipe(LARCH, BlockObjectHolder.chair_wood_ironage_classic_forestry_larch);
				AddClassicChairRecipe(LIME, BlockObjectHolder.chair_wood_ironage_classic_forestry_lime);
				AddClassicChairRecipe(MAHOE, BlockObjectHolder.chair_wood_ironage_classic_forestry_mahoe);
				AddClassicChairRecipe(MAHOGANY, BlockObjectHolder.chair_wood_ironage_classic_forestry_mahogany);
				AddClassicChairRecipe(MAPLE, BlockObjectHolder.chair_wood_ironage_classic_forestry_maple);
				AddClassicChairRecipe(PADAUK, BlockObjectHolder.chair_wood_ironage_classic_forestry_padauk);
				AddClassicChairRecipe(PALM, BlockObjectHolder.chair_wood_ironage_classic_forestry_palm);
				AddClassicChairRecipe(PAPAYA, BlockObjectHolder.chair_wood_ironage_classic_forestry_papaya);
				AddClassicChairRecipe(PINE, BlockObjectHolder.chair_wood_ironage_classic_forestry_pine);
				AddClassicChairRecipe(PLUM, BlockObjectHolder.chair_wood_ironage_classic_forestry_plum);
				AddClassicChairRecipe(POPLAR, BlockObjectHolder.chair_wood_ironage_classic_forestry_poplar);
				AddClassicChairRecipe(SEQUOIA, BlockObjectHolder.chair_wood_ironage_classic_forestry_sequoia);
				AddClassicChairRecipe(TEAK, BlockObjectHolder.chair_wood_ironage_classic_forestry_teak);
				AddClassicChairRecipe(WALNUT, BlockObjectHolder.chair_wood_ironage_classic_forestry_walnut);
				AddClassicChairRecipe(WENGE, BlockObjectHolder.chair_wood_ironage_classic_forestry_wenge);
				AddClassicChairRecipe(WILLOW, BlockObjectHolder.chair_wood_ironage_classic_forestry_willow);
				AddClassicChairRecipe(ZEBRAWOOD, BlockObjectHolder.chair_wood_ironage_classic_forestry_zebrawood);

				
			}
			
			if (IronAgeFurnitureConfiguration.GENERATE_SHIELD_CHAIRS) {
				AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_forestry_acacia, BlockObjectHolder.chair_wood_ironage_shield_forestry_acacia);
				AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_forestry_balsa, BlockObjectHolder.chair_wood_ironage_shield_forestry_balsa);
				AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_forestry_baobab, BlockObjectHolder.chair_wood_ironage_shield_forestry_baobab);
				AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_forestry_cherry, BlockObjectHolder.chair_wood_ironage_shield_forestry_cherry);
				AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_forestry_chestnut, BlockObjectHolder.chair_wood_ironage_shield_forestry_chestnut);
				AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_forestry_citrus, BlockObjectHolder.chair_wood_ironage_shield_forestry_citrus);
				AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_forestry_cocobolo, BlockObjectHolder.chair_wood_ironage_shield_forestry_cocobolo);
				AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_forestry_ebony, BlockObjectHolder.chair_wood_ironage_shield_forestry_ebony);
				AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_forestry_giganteum, BlockObjectHolder.chair_wood_ironage_shield_forestry_giganteum);
				AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_forestry_greenheart, BlockObjectHolder.chair_wood_ironage_shield_forestry_greenheart);
				AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_forestry_ipe, BlockObjectHolder.chair_wood_ironage_shield_forestry_ipe);
				AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_forestry_kapok, BlockObjectHolder.chair_wood_ironage_shield_forestry_kapok);
				AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_forestry_larch, BlockObjectHolder.chair_wood_ironage_shield_forestry_larch);
				AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_forestry_lime, BlockObjectHolder.chair_wood_ironage_shield_forestry_lime);
				AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_forestry_mahoe, BlockObjectHolder.chair_wood_ironage_shield_forestry_mahoe);
				AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_forestry_mahogany, BlockObjectHolder.chair_wood_ironage_shield_forestry_mahogany);
				AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_forestry_maple, BlockObjectHolder.chair_wood_ironage_shield_forestry_maple);
				AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_forestry_padauk, BlockObjectHolder.chair_wood_ironage_shield_forestry_padauk);
				AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_forestry_palm, BlockObjectHolder.chair_wood_ironage_shield_forestry_palm);
				AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_forestry_papaya, BlockObjectHolder.chair_wood_ironage_shield_forestry_papaya);
				AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_forestry_pine, BlockObjectHolder.chair_wood_ironage_shield_forestry_pine);
				AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_forestry_plum, BlockObjectHolder.chair_wood_ironage_shield_forestry_plum);
				AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_forestry_poplar, BlockObjectHolder.chair_wood_ironage_shield_forestry_poplar);
				AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_forestry_sequoia, BlockObjectHolder.chair_wood_ironage_shield_forestry_sequoia);
				AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_forestry_teak, BlockObjectHolder.chair_wood_ironage_shield_forestry_teak);
				AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_forestry_walnut, BlockObjectHolder.chair_wood_ironage_shield_forestry_walnut);
				AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_forestry_wenge, BlockObjectHolder.chair_wood_ironage_shield_forestry_wenge);
				AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_forestry_willow, BlockObjectHolder.chair_wood_ironage_shield_forestry_willow);
				AddShieldChairRecipe(BlockObjectHolder.chair_wood_ironage_classic_forestry_zebrawood, BlockObjectHolder.chair_wood_ironage_shield_forestry_zebrawood);
			
			}
			
			if (IronAgeFurnitureConfiguration.GENERATE_SHORT_STOOLS) {
				AddShortStoolRecipe(ACACIA, BlockObjectHolder.chair_wood_ironage_stool_short_forestry_acacia);
				AddShortStoolRecipe(BALSA, BlockObjectHolder.chair_wood_ironage_stool_short_forestry_balsa);
				AddShortStoolRecipe(BAOBAB, BlockObjectHolder.chair_wood_ironage_stool_short_forestry_baobab);
				AddShortStoolRecipe(CHERRY, BlockObjectHolder.chair_wood_ironage_stool_short_forestry_cherry);
				AddShortStoolRecipe(CHESTNUT, BlockObjectHolder.chair_wood_ironage_stool_short_forestry_chestnut);
				AddShortStoolRecipe(CITRUS, BlockObjectHolder.chair_wood_ironage_stool_short_forestry_citrus);
				AddShortStoolRecipe(COCOBOLO, BlockObjectHolder.chair_wood_ironage_stool_short_forestry_cocobolo);
				AddShortStoolRecipe(EBONY, BlockObjectHolder.chair_wood_ironage_stool_short_forestry_ebony);
				AddShortStoolRecipe(GIGANTEUM, BlockObjectHolder.chair_wood_ironage_stool_short_forestry_giganteum);
				AddShortStoolRecipe(GREENHEART, BlockObjectHolder.chair_wood_ironage_stool_short_forestry_greenheart);
				AddShortStoolRecipe(IPE, BlockObjectHolder.chair_wood_ironage_stool_short_forestry_ipe);
				AddShortStoolRecipe(KAPOK, BlockObjectHolder.chair_wood_ironage_stool_short_forestry_kapok);
				AddShortStoolRecipe(LARCH, BlockObjectHolder.chair_wood_ironage_stool_short_forestry_larch);
				AddShortStoolRecipe(LIME, BlockObjectHolder.chair_wood_ironage_stool_short_forestry_lime);
				AddShortStoolRecipe(MAHOE, BlockObjectHolder.chair_wood_ironage_stool_short_forestry_mahoe);
				AddShortStoolRecipe(MAHOGANY, BlockObjectHolder.chair_wood_ironage_stool_short_forestry_mahogany);
				AddShortStoolRecipe(MAPLE, BlockObjectHolder.chair_wood_ironage_stool_short_forestry_maple);
				AddShortStoolRecipe(PADAUK, BlockObjectHolder.chair_wood_ironage_stool_short_forestry_padauk);
				AddShortStoolRecipe(PALM, BlockObjectHolder.chair_wood_ironage_stool_short_forestry_palm);
				AddShortStoolRecipe(PAPAYA, BlockObjectHolder.chair_wood_ironage_stool_short_forestry_papaya);
				AddShortStoolRecipe(PINE, BlockObjectHolder.chair_wood_ironage_stool_short_forestry_pine);
				AddShortStoolRecipe(PLUM, BlockObjectHolder.chair_wood_ironage_stool_short_forestry_plum);
				AddShortStoolRecipe(POPLAR, BlockObjectHolder.chair_wood_ironage_stool_short_forestry_poplar);
				AddShortStoolRecipe(SEQUOIA, BlockObjectHolder.chair_wood_ironage_stool_short_forestry_sequoia);
				AddShortStoolRecipe(TEAK, BlockObjectHolder.chair_wood_ironage_stool_short_forestry_teak);
				AddShortStoolRecipe(WALNUT, BlockObjectHolder.chair_wood_ironage_stool_short_forestry_walnut);
				AddShortStoolRecipe(WENGE, BlockObjectHolder.chair_wood_ironage_stool_short_forestry_wenge);
				AddShortStoolRecipe(WILLOW, BlockObjectHolder.chair_wood_ironage_stool_short_forestry_willow);
				AddShortStoolRecipe(ZEBRAWOOD, BlockObjectHolder.chair_wood_ironage_stool_short_forestry_zebrawood);

				
			}
			
			if (IronAgeFurnitureConfiguration.GENERATE_TALL_STOOLS) {
				AddTallStoolRecipe(ACACIA, BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_acacia);
				AddTallStoolRecipe(BALSA, BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_balsa);
				AddTallStoolRecipe(BAOBAB, BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_baobab);
				AddTallStoolRecipe(CHERRY, BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_cherry);
				AddTallStoolRecipe(CHESTNUT, BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_chestnut);
				AddTallStoolRecipe(CITRUS, BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_citrus);
				AddTallStoolRecipe(COCOBOLO, BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_cocobolo);
				AddTallStoolRecipe(EBONY, BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_ebony);
				AddTallStoolRecipe(GIGANTEUM, BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_giganteum);
				AddTallStoolRecipe(GREENHEART, BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_greenheart);
				AddTallStoolRecipe(IPE, BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_ipe);
				AddTallStoolRecipe(KAPOK, BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_kapok);
				AddTallStoolRecipe(LARCH, BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_larch);
				AddTallStoolRecipe(LIME, BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_lime);
				AddTallStoolRecipe(MAHOE, BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_mahoe);
				AddTallStoolRecipe(MAHOGANY, BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_mahogany);
				AddTallStoolRecipe(MAPLE, BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_maple);
				AddTallStoolRecipe(PADAUK, BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_padauk);
				AddTallStoolRecipe(PALM, BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_palm);
				AddTallStoolRecipe(PAPAYA, BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_papaya);
				AddTallStoolRecipe(PINE, BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_pine);
				AddTallStoolRecipe(PLUM, BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_plum);
				AddTallStoolRecipe(POPLAR, BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_poplar);
				AddTallStoolRecipe(SEQUOIA, BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_sequoia);
				AddTallStoolRecipe(TEAK, BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_teak);
				AddTallStoolRecipe(WALNUT, BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_walnut);
				AddTallStoolRecipe(WENGE, BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_wenge);
				AddTallStoolRecipe(WILLOW, BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_willow);
				AddTallStoolRecipe(ZEBRAWOOD, BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_zebrawood);

			}
		}
	}
}
