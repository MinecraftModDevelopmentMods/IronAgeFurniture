package com.mcmoddev.ironagefurniture.init;

import com.mcmoddev.ironagefurniture.BlockObjectHolder;
import com.mcmoddev.ironagefurniture.IronAgeFurnitureConfiguration;
import com.mcmoddev.ironagefurniture.Ironagefurniture;
import com.mcmoddev.ironagefurniture.Blocks.Chair;
import com.mcmoddev.ironagefurniture.Blocks.Stool;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * This class initialises all blocks in ironagefurniture.
 *
 * @author SkyBlade1978
 *
 */
public class BlockInitialiser {
	
	
	protected BlockInitialiser() {
		throw new IllegalAccessError("This class cannot be instansiated");
	}

	/**
	 *
	 */
	public static void init() {
		generateChairs();
	}
	
	private static void generateChairs() {
		if (IronAgeFurnitureConfiguration.GENERATE_CLASSIC_CHAIRS) {
			BlockObjectHolder.chair_wood_ironage_classic_oak = registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_oak", (float)10, 1), "chair_wood_ironage_classic_oak");
			BlockObjectHolder.chair_wood_ironage_classic_acacia = registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_acacia", (float)10, 1), "chair_wood_ironage_classic_acacia");
			BlockObjectHolder.chair_wood_ironage_classic_big_oak = registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_big_oak", (float)10, 1), "chair_wood_ironage_classic_big_oak");
			BlockObjectHolder.chair_wood_ironage_classic_birch = registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_birch", (float)10, 1), "chair_wood_ironage_classic_birch");
			BlockObjectHolder.chair_wood_ironage_classic_jungle = registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_jungle", (float)10, 1), "chair_wood_ironage_classic_jungle");
			BlockObjectHolder.chair_wood_ironage_classic_spruce = registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_spruce", (float)10, 1), "chair_wood_ironage_classic_spruce");
		}
		
		if (IronAgeFurnitureConfiguration.GENERATE_SHIELD_CHAIRS) {
			BlockObjectHolder.chair_wood_ironage_shield_oak = registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_oak", (float)10, 1), "chair_wood_ironage_shield_oak");
			BlockObjectHolder.chair_wood_ironage_shield_acacia = registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_acacia", (float)10, 1), "chair_wood_ironage_shield_acacia");
			BlockObjectHolder.chair_wood_ironage_shield_big_oak = registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_big_oak", (float)10, 1), "chair_wood_ironage_shield_big_oak");
			BlockObjectHolder.chair_wood_ironage_shield_birch = registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_birch", (float)10, 1), "chair_wood_ironage_shield_birch");
			BlockObjectHolder.chair_wood_ironage_shield_jungle = registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_jungle", (float)10, 1), "chair_wood_ironage_shield_jungle");
			BlockObjectHolder.chair_wood_ironage_shield_spruce = registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_spruce", (float)10, 1), "chair_wood_ironage_shield_spruce");		
		}
		
		if (IronAgeFurnitureConfiguration.GENERATE_SHORT_STOOLS) {
			BlockObjectHolder.chair_wood_ironage_stool_short_oak = registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_oak", (float)10, false, 0.25, 1), "chair_wood_ironage_stool_short_oak");
			BlockObjectHolder.chair_wood_ironage_stool_short_acacia =registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_acacia", (float)10, false, 0.25, 1), "chair_wood_ironage_stool_short_acacia");
			BlockObjectHolder.chair_wood_ironage_stool_short_big_oak = registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_big_oak", (float)10, false, 0.25, 1), "chair_wood_ironage_stool_short_big_oak");
			BlockObjectHolder.chair_wood_ironage_stool_short_birch = registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_birch", (float)10, false, 0.25, 1), "chair_wood_ironage_stool_short_birch");
			BlockObjectHolder.chair_wood_ironage_stool_short_jungle = registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_jungle", (float)10, false, 0.25, 1), "chair_wood_ironage_stool_short_jungle");
			BlockObjectHolder.chair_wood_ironage_stool_short_spruce = registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_spruce", (float)10, false, 0.25, 1), "chair_wood_ironage_stool_short_spruce");
		}
		
		if (IronAgeFurnitureConfiguration.GENERATE_TALL_STOOLS) {
			BlockObjectHolder.chair_wood_ironage_stool_tall_acacia = registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_acacia", (float)10, true, 0.6, 1), "chair_wood_ironage_stool_tall_acacia");
			BlockObjectHolder.chair_wood_ironage_stool_tall_big_oak = registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_big_oak", (float)10, true, 0.6, 1), "chair_wood_ironage_stool_tall_big_oak");
			BlockObjectHolder.chair_wood_ironage_stool_tall_birch = registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_birch", (float)10, true, 0.6, 1), "chair_wood_ironage_stool_tall_birch");
			BlockObjectHolder.chair_wood_ironage_stool_tall_jungle = registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_jungle", (float)10, true, 0.6, 1), "chair_wood_ironage_stool_tall_jungle");	
			BlockObjectHolder.chair_wood_ironage_stool_tall_oak = registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_oak", (float)10, true, 0.6, 1), "chair_wood_ironage_stool_tall_oak");
			BlockObjectHolder.chair_wood_ironage_stool_tall_spruce = registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_spruce", (float)10, true, 0.6, 1), "chair_wood_ironage_stool_tall_spruce");
		}
		

		
		if (IronAgeFurnitureConfiguration.INTEGRATION_IMMERSIVEENGINEERING && Loader.isModLoaded("immersiveengineering")) {
			if (IronAgeFurnitureConfiguration.GENERATE_CLASSIC_CHAIRS) {
				BlockObjectHolder.chair_wood_ironage_classic_immersiveengineering_treatedWood = registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_immersiveengineering_treatedWood", (float)10, 1), "chair_wood_ironage_classic_immersiveengineering_treatedWood");			
			}
			
			if (IronAgeFurnitureConfiguration.GENERATE_SHIELD_CHAIRS) {
				BlockObjectHolder.chair_wood_ironage_shield_immersiveengineering_treatedWood = registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_immersiveengineering_treatedWood", (float)10, 1), "chair_wood_ironage_shield_immersiveengineering_treatedWood");
			}
			
			if (IronAgeFurnitureConfiguration.GENERATE_SHORT_STOOLS) {
				BlockObjectHolder.chair_wood_ironage_stool_short_immersiveengineering_treatedWood = registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_immersiveengineering_treatedWood", (float)10, false, 0.25, 1), "chair_wood_ironage_stool_short_immersiveengineering_treatedWood");
			}
			
			if (IronAgeFurnitureConfiguration.GENERATE_TALL_STOOLS) {
				BlockObjectHolder.chair_wood_ironage_stool_tall_immersiveengineering_treatedWood = registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_immersiveengineering_treatedWood", (float)10, true, 0.6, 1), "chair_wood_ironage_stool_tall_immersiveengineering_treatedWood");
			}
		}
		
		if (IronAgeFurnitureConfiguration.INTEGRATION_BIOMESOPLENTY && Loader.isModLoaded("BiomesOPlenty")) {
			if (IronAgeFurnitureConfiguration.GENERATE_CLASSIC_CHAIRS) {
				BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_cherry         = registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_biomesoplenty_cherry", (float)10, 1), "chair_wood_ironage_classic_biomesoplenty_cherry");
				BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_ebony          = registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_biomesoplenty_ebony", (float)10, 1), "chair_wood_ironage_classic_biomesoplenty_ebony");
				BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_ethereal       = registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_biomesoplenty_ethereal", (float)10, 1), "chair_wood_ironage_classic_biomesoplenty_ethereal");
				BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_eucalyptus     = registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_biomesoplenty_eucalyptus", (float)10, 1), "chair_wood_ironage_classic_biomesoplenty_eucalyptus");
				BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_fir            = registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_biomesoplenty_fir", (float)10, 1), "chair_wood_ironage_classic_biomesoplenty_fir");
				BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_hellbark       = registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_biomesoplenty_hellbark", (float)10, 1), "chair_wood_ironage_classic_biomesoplenty_hellbark");
				BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_jacaranda      = registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_biomesoplenty_jacaranda", (float)10, 1), "chair_wood_ironage_classic_biomesoplenty_jacaranda");
				BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_magic          = registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_biomesoplenty_magic", (float)10, 1), "chair_wood_ironage_classic_biomesoplenty_magic");
				BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_mahogany       = registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_biomesoplenty_mahogany", (float)10, 1), "chair_wood_ironage_classic_biomesoplenty_mahogany");
				BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_mangrove       = registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_biomesoplenty_mangrove", (float)10, 1), "chair_wood_ironage_classic_biomesoplenty_mangrove");	
				BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_palm           = registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_biomesoplenty_palm", (float)10, 1), "chair_wood_ironage_classic_biomesoplenty_palm");
				BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_pine           = registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_biomesoplenty_pine", (float)10, 1), "chair_wood_ironage_classic_biomesoplenty_pine");
				BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_redwood        = registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_biomesoplenty_redwood", (float)10, 1), "chair_wood_ironage_classic_biomesoplenty_redwood");
				BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_sacred_oak     = registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_biomesoplenty_sacred_oak", (float)10, 1), "chair_wood_ironage_classic_biomesoplenty_sacred_oak");	 
				BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_umbran         = registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_biomesoplenty_umbran", (float)10, 1), "chair_wood_ironage_classic_biomesoplenty_umbran");
				BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_willow         = registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_biomesoplenty_willow", (float)10, 1), "chair_wood_ironage_classic_biomesoplenty_willow");
			}
			
			if (IronAgeFurnitureConfiguration.GENERATE_SHIELD_CHAIRS) {
				BlockObjectHolder.chair_wood_ironage_shield_biomesoplenty_cherry		= 	registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_biomesoplenty_cherry", (float)10, 1), "chair_wood_ironage_shield_biomesoplenty_cherry");
				BlockObjectHolder.chair_wood_ironage_shield_biomesoplenty_ebony			= 	registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_biomesoplenty_ebony", (float)10, 1), "chair_wood_ironage_shield_biomesoplenty_ebony");
				BlockObjectHolder.chair_wood_ironage_shield_biomesoplenty_ethereal		= 	registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_biomesoplenty_ethereal", (float)10, 1), "chair_wood_ironage_shield_biomesoplenty_ethereal");
				BlockObjectHolder.chair_wood_ironage_shield_biomesoplenty_eucalyptus	= 	registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_biomesoplenty_eucalyptus", (float)10, 1), "chair_wood_ironage_shield_biomesoplenty_eucalyptus");
				BlockObjectHolder.chair_wood_ironage_shield_biomesoplenty_fir			= 	registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_biomesoplenty_fir", (float)10, 1), "chair_wood_ironage_shield_biomesoplenty_fir");
				BlockObjectHolder.chair_wood_ironage_shield_biomesoplenty_hellbark		= 	registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_biomesoplenty_hellbark", (float)10, 1), "chair_wood_ironage_shield_biomesoplenty_hellbark");
				BlockObjectHolder.chair_wood_ironage_shield_biomesoplenty_jacaranda		= 	registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_biomesoplenty_jacaranda", (float)10, 1), "chair_wood_ironage_shield_biomesoplenty_jacaranda");
				BlockObjectHolder.chair_wood_ironage_shield_biomesoplenty_magic			= 	registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_biomesoplenty_magic", (float)10, 1), "chair_wood_ironage_shield_biomesoplenty_magic");
				BlockObjectHolder.chair_wood_ironage_shield_biomesoplenty_mahogany		= 	registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_biomesoplenty_mahogany", (float)10, 1), "chair_wood_ironage_shield_biomesoplenty_mahogany");
				BlockObjectHolder.chair_wood_ironage_shield_biomesoplenty_mangrove		= 	registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_biomesoplenty_mangrove", (float)10, 1), "chair_wood_ironage_shield_biomesoplenty_mangrove");
				BlockObjectHolder.chair_wood_ironage_shield_biomesoplenty_palm			= 	registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_biomesoplenty_palm", (float)10, 1), "chair_wood_ironage_shield_biomesoplenty_palm");
				BlockObjectHolder.chair_wood_ironage_shield_biomesoplenty_pine			= 	registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_biomesoplenty_pine", (float)10, 1), "chair_wood_ironage_shield_biomesoplenty_pine");
				BlockObjectHolder.chair_wood_ironage_shield_biomesoplenty_redwood		= 	registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_biomesoplenty_redwood", (float)10, 1), "chair_wood_ironage_shield_biomesoplenty_redwood");
				BlockObjectHolder.chair_wood_ironage_shield_biomesoplenty_sacred_oak	= 	registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_biomesoplenty_sacred_oak", (float)10, 1), "chair_wood_ironage_shield_biomesoplenty_sacred_oak");
				BlockObjectHolder.chair_wood_ironage_shield_biomesoplenty_umbran		= 	registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_biomesoplenty_umbran", (float)10, 1), "chair_wood_ironage_shield_biomesoplenty_umbran");
				BlockObjectHolder.chair_wood_ironage_shield_biomesoplenty_willow		= 	registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_biomesoplenty_willow", (float)10, 1), "chair_wood_ironage_shield_biomesoplenty_willow");
			}
			
			if (IronAgeFurnitureConfiguration.GENERATE_SHORT_STOOLS) {
				BlockObjectHolder.chair_wood_ironage_stool_short_biomesoplenty_cherry			= registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_biomesoplenty_cherry", (float)10, false, 0.25, 1), "chair_wood_ironage_stool_short_biomesoplenty_cherry");
				BlockObjectHolder.chair_wood_ironage_stool_short_biomesoplenty_ebony			= registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_biomesoplenty_ebony", (float)10, false, 0.25, 1), "chair_wood_ironage_stool_short_biomesoplenty_ebony");
				BlockObjectHolder.chair_wood_ironage_stool_short_biomesoplenty_ethereal			= registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_biomesoplenty_ethereal", (float)10, false, 0.25, 1), "chair_wood_ironage_stool_short_biomesoplenty_ethereal");
				BlockObjectHolder.chair_wood_ironage_stool_short_biomesoplenty_eucalyptus		= registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_biomesoplenty_eucalyptus", (float)10, false, 0.25, 1), "chair_wood_ironage_stool_short_biomesoplenty_eucalyptus");
				BlockObjectHolder.chair_wood_ironage_stool_short_biomesoplenty_fir				= registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_biomesoplenty_fir", (float)10, false, 0.25, 1), "chair_wood_ironage_stool_short_biomesoplenty_fir");
				BlockObjectHolder.chair_wood_ironage_stool_short_biomesoplenty_hellbark			= registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_biomesoplenty_hellbark", (float)10, false, 0.25, 1), "chair_wood_ironage_stool_short_biomesoplenty_hellbark");
				BlockObjectHolder.chair_wood_ironage_stool_short_biomesoplenty_jacaranda		= registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_biomesoplenty_jacaranda", (float)10, false, 0.25, 1), "chair_wood_ironage_stool_short_biomesoplenty_jacaranda");	
				BlockObjectHolder.chair_wood_ironage_stool_short_biomesoplenty_magic			= registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_biomesoplenty_magic", (float)10, false, 0.25, 1), "chair_wood_ironage_stool_short_biomesoplenty_magic");
				BlockObjectHolder.chair_wood_ironage_stool_short_biomesoplenty_mahogany			= registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_biomesoplenty_mahogany", (float)10, false, 0.25, 1), "chair_wood_ironage_stool_short_biomesoplenty_mahogany");
				BlockObjectHolder.chair_wood_ironage_stool_short_biomesoplenty_mangrove			= registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_biomesoplenty_mangrove", (float)10, false, 0.25, 1), "chair_wood_ironage_stool_short_biomesoplenty_mangrove");
				BlockObjectHolder.chair_wood_ironage_stool_short_biomesoplenty_palm				= registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_biomesoplenty_palm", (float)10, false, 0.25, 1), "chair_wood_ironage_stool_short_biomesoplenty_palm");
				BlockObjectHolder.chair_wood_ironage_stool_short_biomesoplenty_pine				= registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_biomesoplenty_pine", (float)10, false, 0.25, 1), "chair_wood_ironage_stool_short_biomesoplenty_pine");
				BlockObjectHolder.chair_wood_ironage_stool_short_biomesoplenty_redwood			= registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_biomesoplenty_redwood", (float)10, false, 0.25, 1), "chair_wood_ironage_stool_short_biomesoplenty_redwood");
				BlockObjectHolder.chair_wood_ironage_stool_short_biomesoplenty_sacred_oak		= registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_biomesoplenty_sacred_oak", (float)10, false, 0.25, 1), "chair_wood_ironage_stool_short_biomesoplenty_sacred_oak");
				BlockObjectHolder.chair_wood_ironage_stool_short_biomesoplenty_umbran			= registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_biomesoplenty_umbran", (float)10, false, 0.25, 1), "chair_wood_ironage_stool_short_biomesoplenty_umbran");
				BlockObjectHolder.chair_wood_ironage_stool_short_biomesoplenty_willow			= registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_biomesoplenty_willow", (float)10, false, 0.25, 1), "chair_wood_ironage_stool_short_biomesoplenty_willow");
			}
			
			if (IronAgeFurnitureConfiguration.GENERATE_TALL_STOOLS) {
				BlockObjectHolder.chair_wood_ironage_stool_tall_biomesoplenty_cherry			=	registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_biomesoplenty_cherry", (float)10, true, 0.6, 1), "chair_wood_ironage_stool_tall_biomesoplenty_cherry");
				BlockObjectHolder.chair_wood_ironage_stool_tall_biomesoplenty_ebony				=	registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_biomesoplenty_ebony", (float)10, true, 0.6, 1), "chair_wood_ironage_stool_tall_biomesoplenty_ebony");
				BlockObjectHolder.chair_wood_ironage_stool_tall_biomesoplenty_ethereal			=	registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_biomesoplenty_ethereal", (float)10, true, 0.6, 1), "chair_wood_ironage_stool_tall_biomesoplenty_ethereal");
				BlockObjectHolder.chair_wood_ironage_stool_tall_biomesoplenty_eucalyptus		=	registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_biomesoplenty_eucalyptus", (float)10, true, 0.6, 1), "chair_wood_ironage_stool_tall_biomesoplenty_eucalyptus");
				BlockObjectHolder.chair_wood_ironage_stool_tall_biomesoplenty_fir				=	registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_biomesoplenty_fir", (float)10, true, 0.6, 1), "chair_wood_ironage_stool_tall_biomesoplenty_fir");
				BlockObjectHolder.chair_wood_ironage_stool_tall_biomesoplenty_hellbark			=	registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_biomesoplenty_hellbark", (float)10, true, 0.6, 1), "chair_wood_ironage_stool_tall_biomesoplenty_hellbark");
				BlockObjectHolder.chair_wood_ironage_stool_tall_biomesoplenty_jacaranda			=	registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_biomesoplenty_jacaranda", (float)10, true, 0.6, 1), "chair_wood_ironage_stool_tall_biomesoplenty_jacaranda");
				BlockObjectHolder.chair_wood_ironage_stool_tall_biomesoplenty_magic				=	registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_biomesoplenty_magic", (float)10, true, 0.6, 1), "chair_wood_ironage_stool_tall_biomesoplenty_magic");
				BlockObjectHolder.chair_wood_ironage_stool_tall_biomesoplenty_mahogany			=	registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_biomesoplenty_mahogany", (float)10, true, 0.6, 1), "chair_wood_ironage_stool_tall_biomesoplenty_mahogany");
				BlockObjectHolder.chair_wood_ironage_stool_tall_biomesoplenty_mangrove			=	registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_biomesoplenty_mangrove", (float)10, true, 0.6, 1), "chair_wood_ironage_stool_tall_biomesoplenty_mangrove");
				BlockObjectHolder.chair_wood_ironage_stool_tall_biomesoplenty_palm				=	registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_biomesoplenty_palm", (float)10, true, 0.6, 1), "chair_wood_ironage_stool_tall_biomesoplenty_palm");
				BlockObjectHolder.chair_wood_ironage_stool_tall_biomesoplenty_pine				=	registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_biomesoplenty_pine", (float)10, true, 0.6, 1), "chair_wood_ironage_stool_tall_biomesoplenty_pine");
				BlockObjectHolder.chair_wood_ironage_stool_tall_biomesoplenty_redwood			=	registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_biomesoplenty_redwood", (float)10, true, 0.6, 1), "chair_wood_ironage_stool_tall_biomesoplenty_redwood");
				BlockObjectHolder.chair_wood_ironage_stool_tall_biomesoplenty_sacred_oak		=	registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_biomesoplenty_sacred_oak", (float)10, true, 0.6, 1), "chair_wood_ironage_stool_tall_biomesoplenty_sacred_oak");
				BlockObjectHolder.chair_wood_ironage_stool_tall_biomesoplenty_umbran			=	registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_biomesoplenty_umbran", (float)10, true, 0.6, 1), "chair_wood_ironage_stool_tall_biomesoplenty_umbran");
				BlockObjectHolder.chair_wood_ironage_stool_tall_biomesoplenty_willow			=	registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_biomesoplenty_willow", (float)10, true, 0.6, 1), "chair_wood_ironage_stool_tall_biomesoplenty_willow");
			}
		}
		
		if (IronAgeFurnitureConfiguration.INTEGRATION_NATURA && Loader.isModLoaded("natura")) {
			if (IronAgeFurnitureConfiguration.GENERATE_CLASSIC_CHAIRS) {
				BlockObjectHolder.chair_wood_ironage_classic_natura_amaranth		= registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_natura_amaranth", (float)10, 1), "chair_wood_ironage_classic_natura_amaranth");
				BlockObjectHolder.chair_wood_ironage_classic_natura_eucalyptus		= registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_natura_eucalyptus", (float)10, 1), "chair_wood_ironage_classic_natura_eucalyptus");
				BlockObjectHolder.chair_wood_ironage_classic_natura_hopseed			= registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_natura_hopseed", (float)10, 1), "chair_wood_ironage_classic_natura_hopseed");
				BlockObjectHolder.chair_wood_ironage_classic_natura_maple			= registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_natura_maple", (float)10, 1), "chair_wood_ironage_classic_natura_maple");
				BlockObjectHolder.chair_wood_ironage_classic_natura_redwood			= registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_natura_redwood", (float)10, 1), "chair_wood_ironage_classic_natura_redwood");
				BlockObjectHolder.chair_wood_ironage_classic_natura_sakura			= registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_natura_sakura", (float)10, 1), "chair_wood_ironage_classic_natura_sakura");
				BlockObjectHolder.chair_wood_ironage_classic_natura_silverbell		= registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_natura_silverbell", (float)10, 1), "chair_wood_ironage_classic_natura_silverbell");
				BlockObjectHolder.chair_wood_ironage_classic_natura_tiger			= registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_natura_tiger", (float)10, 1), "chair_wood_ironage_classic_natura_tiger");
				BlockObjectHolder.chair_wood_ironage_classic_natura_willow			= registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_natura_willow", (float)10, 1), "chair_wood_ironage_classic_natura_willow");
				BlockObjectHolder.chair_wood_ironage_classic_natura_bloodwood		= registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_natura_bloodwood", (float)10, 1), "chair_wood_ironage_classic_natura_bloodwood");
				BlockObjectHolder.chair_wood_ironage_classic_natura_darkwood		= registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_natura_darkwood", (float)10, 1), "chair_wood_ironage_classic_natura_darkwood");
				BlockObjectHolder.chair_wood_ironage_classic_natura_fusewood		= registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_natura_fusewood", (float)10, 1), "chair_wood_ironage_classic_natura_fusewood");
				BlockObjectHolder.chair_wood_ironage_classic_natura_ghostwood		= registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_natura_ghostwood", (float)10, 1), "chair_wood_ironage_classic_natura_ghostwood");
			}
			
			if (IronAgeFurnitureConfiguration.GENERATE_SHIELD_CHAIRS) {
				BlockObjectHolder.chair_wood_ironage_shield_natura_amaranth			=	registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_natura_amaranth", (float)10, 1), "chair_wood_ironage_shield_natura_amaranth");
				BlockObjectHolder.chair_wood_ironage_shield_natura_eucalyptus		=	registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_natura_eucalyptus", (float)10, 1), "chair_wood_ironage_shield_natura_eucalyptus");
				BlockObjectHolder.chair_wood_ironage_shield_natura_hopseed			=	registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_natura_hopseed", (float)10, 1), "chair_wood_ironage_shield_natura_hopseed");
				BlockObjectHolder.chair_wood_ironage_shield_natura_maple			=	registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_natura_maple", (float)10, 1), "chair_wood_ironage_shield_natura_maple");
				BlockObjectHolder.chair_wood_ironage_shield_natura_redwood			=	registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_natura_redwood", (float)10, 1), "chair_wood_ironage_shield_natura_redwood");
				BlockObjectHolder.chair_wood_ironage_shield_natura_sakura			=	registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_natura_sakura", (float)10, 1), "chair_wood_ironage_shield_natura_sakura");
				BlockObjectHolder.chair_wood_ironage_shield_natura_silverbell		=	registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_natura_silverbell", (float)10, 1), "chair_wood_ironage_shield_natura_silverbell");
				BlockObjectHolder.chair_wood_ironage_shield_natura_tiger			=	registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_natura_tiger", (float)10, 1), "chair_wood_ironage_shield_natura_tiger");
				BlockObjectHolder.chair_wood_ironage_shield_natura_willow			=	registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_natura_willow", (float)10, 1), "chair_wood_ironage_shield_natura_willow");
				BlockObjectHolder.chair_wood_ironage_shield_natura_bloodwood		=	registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_natura_bloodwood", (float)10, 1), "chair_wood_ironage_shield_natura_bloodwood");
				BlockObjectHolder.chair_wood_ironage_shield_natura_darkwood			=	registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_natura_darkwood", (float)10, 1), "chair_wood_ironage_shield_natura_darkwood");
				BlockObjectHolder.chair_wood_ironage_shield_natura_fusewood			=	registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_natura_fusewood", (float)10, 1), "chair_wood_ironage_shield_natura_fusewood");
				BlockObjectHolder.chair_wood_ironage_shield_natura_ghostwood		=	registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_natura_ghostwood", (float)10, 1), "chair_wood_ironage_shield_natura_ghostwood");
			}
			
			if (IronAgeFurnitureConfiguration.GENERATE_SHORT_STOOLS) {
				BlockObjectHolder.chair_wood_ironage_stool_short_natura_amaranth		= registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_natura_amaranth", (float)10, false, 0.25, 1), "chair_wood_ironage_stool_short_natura_amaranth");
				BlockObjectHolder.chair_wood_ironage_stool_short_natura_eucalyptus		= registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_natura_eucalyptus", (float)10, false, 0.25, 1), "chair_wood_ironage_stool_short_natura_eucalyptus");
				BlockObjectHolder.chair_wood_ironage_stool_short_natura_hopseed			= registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_natura_hopseed", (float)10, false, 0.25, 1), "chair_wood_ironage_stool_short_natura_hopseed");
				BlockObjectHolder.chair_wood_ironage_stool_short_natura_maple			= registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_natura_maple", (float)10, false, 0.25, 1), "chair_wood_ironage_stool_short_natura_maple");
				BlockObjectHolder.chair_wood_ironage_stool_short_natura_redwood			= registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_natura_redwood", (float)10, false, 0.25, 1), "chair_wood_ironage_stool_short_natura_redwood");
				BlockObjectHolder.chair_wood_ironage_stool_short_natura_sakura			= registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_natura_sakura", (float)10, false, 0.25, 1), "chair_wood_ironage_stool_short_natura_sakura");
				BlockObjectHolder.chair_wood_ironage_stool_short_natura_silverbell		= registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_natura_silverbell", (float)10, false, 0.25, 1), "chair_wood_ironage_stool_short_natura_silverbell");
				BlockObjectHolder.chair_wood_ironage_stool_short_natura_tiger			= registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_natura_tiger", (float)10, false, 0.25, 1), "chair_wood_ironage_stool_short_natura_tiger");
				BlockObjectHolder.chair_wood_ironage_stool_short_natura_willow			= registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_natura_willow", (float)10, false, 0.25, 1), "chair_wood_ironage_stool_short_natura_willow");
				BlockObjectHolder.chair_wood_ironage_stool_short_natura_bloodwood		= registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_natura_bloodwood", (float)10, false, 0.25, 1), "chair_wood_ironage_stool_short_natura_bloodwood");
				BlockObjectHolder.chair_wood_ironage_stool_short_natura_darkwood		= registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_natura_darkwood", (float)10, false, 0.25, 1), "chair_wood_ironage_stool_short_natura_darkwood");
				BlockObjectHolder.chair_wood_ironage_stool_short_natura_fusewood		= registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_natura_fusewood", (float)10, false, 0.25, 1), "chair_wood_ironage_stool_short_natura_fusewood");
				BlockObjectHolder.chair_wood_ironage_stool_short_natura_ghostwood		= registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_natura_ghostwood", (float)10, false, 0.25, 1), "chair_wood_ironage_stool_short_natura_ghostwood");
			}
			
			if (IronAgeFurnitureConfiguration.GENERATE_TALL_STOOLS) {
				BlockObjectHolder.chair_wood_ironage_stool_tall_natura_amaranth			=	registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_natura_amaranth", (float)10, true, 0.6, 1), "chair_wood_ironage_stool_tall_natura_amaranth");
				BlockObjectHolder.chair_wood_ironage_stool_tall_natura_eucalyptus		=	registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_natura_eucalyptus", (float)10, true, 0.6, 1), "chair_wood_ironage_stool_tall_natura_eucalyptus");
				BlockObjectHolder.chair_wood_ironage_stool_tall_natura_hopseed			=	registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_natura_hopseed", (float)10, true, 0.6, 1), "chair_wood_ironage_stool_tall_natura_hopseed");
				BlockObjectHolder.chair_wood_ironage_stool_tall_natura_maple			=	registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_natura_maple", (float)10, true, 0.6, 1), "chair_wood_ironage_stool_tall_natura_maple");
				BlockObjectHolder.chair_wood_ironage_stool_tall_natura_redwood			=	registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_natura_redwood", (float)10, true, 0.6, 1), "chair_wood_ironage_stool_tall_natura_redwood");
				BlockObjectHolder.chair_wood_ironage_stool_tall_natura_sakura			=	registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_natura_sakura", (float)10, true, 0.6, 1), "chair_wood_ironage_stool_tall_natura_sakura");
				BlockObjectHolder.chair_wood_ironage_stool_tall_natura_silverbell		=	registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_natura_silverbell", (float)10, true, 0.6, 1), "chair_wood_ironage_stool_tall_natura_silverbell");
				BlockObjectHolder.chair_wood_ironage_stool_tall_natura_tiger			=	registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_natura_tiger", (float)10, true, 0.6, 1), "chair_wood_ironage_stool_tall_natura_tiger");
				BlockObjectHolder.chair_wood_ironage_stool_tall_natura_willow			=	registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_natura_willow", (float)10, true, 0.6, 1), "chair_wood_ironage_stool_tall_natura_willow");
				BlockObjectHolder.chair_wood_ironage_stool_tall_natura_bloodwood		=	registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_natura_bloodwood", (float)10, true, 0.6, 1), "chair_wood_ironage_stool_tall_natura_bloodwood");
				BlockObjectHolder.chair_wood_ironage_stool_tall_natura_darkwood			=	registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_natura_darkwood", (float)10, true, 0.6, 1), "chair_wood_ironage_stool_tall_natura_darkwood");
				BlockObjectHolder.chair_wood_ironage_stool_tall_natura_fusewood			=	registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_natura_fusewood", (float)10, true, 0.6, 1), "chair_wood_ironage_stool_tall_natura_fusewood");
				BlockObjectHolder.chair_wood_ironage_stool_tall_natura_ghostwood		=	registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_natura_ghostwood", (float)10, true, 0.6, 1), "chair_wood_ironage_stool_tall_natura_ghostwood");
			}
		}
		
		if (IronAgeFurnitureConfiguration.INTEGRATION_FORESTRY && Loader.isModLoaded("forestry")) {
			if (IronAgeFurnitureConfiguration.GENERATE_CLASSIC_CHAIRS) {
				BlockObjectHolder.chair_wood_ironage_classic_forestry_acacia = registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_forestry_acacia", (float)10, 1), "chair_wood_ironage_classic_forestry_acacia");
				BlockObjectHolder.chair_wood_ironage_classic_forestry_balsa = registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_forestry_balsa", (float)10, 1), "chair_wood_ironage_classic_forestry_balsa");
				BlockObjectHolder.chair_wood_ironage_classic_forestry_baobab = registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_forestry_baobab", (float)10, 1), "chair_wood_ironage_classic_forestry_baobab");
				BlockObjectHolder.chair_wood_ironage_classic_forestry_cherry = registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_forestry_cherry", (float)10, 1), "chair_wood_ironage_classic_forestry_cherry");
				BlockObjectHolder.chair_wood_ironage_classic_forestry_chestnut = registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_forestry_chestnut", (float)10, 1), "chair_wood_ironage_classic_forestry_chestnut");
				BlockObjectHolder.chair_wood_ironage_classic_forestry_citrus = registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_forestry_citrus", (float)10, 1), "chair_wood_ironage_classic_forestry_citrus");
				BlockObjectHolder.chair_wood_ironage_classic_forestry_cocobolo = registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_forestry_cocobolo", (float)10, 1), "chair_wood_ironage_classic_forestry_cocobolo");
				BlockObjectHolder.chair_wood_ironage_classic_forestry_ebony = registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_forestry_ebony", (float)10, 1), "chair_wood_ironage_classic_forestry_ebony");
				BlockObjectHolder.chair_wood_ironage_classic_forestry_giganteum = registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_forestry_giganteum", (float)10, 1), "chair_wood_ironage_classic_forestry_giganteum");
				BlockObjectHolder.chair_wood_ironage_classic_forestry_greenheart = registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_forestry_greenheart", (float)10, 1), "chair_wood_ironage_classic_forestry_greenheart");
				BlockObjectHolder.chair_wood_ironage_classic_forestry_ipe = registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_forestry_ipe", (float)10, 1), "chair_wood_ironage_classic_forestry_ipe");
				BlockObjectHolder.chair_wood_ironage_classic_forestry_kapok = registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_forestry_kapok", (float)10, 1), "chair_wood_ironage_classic_forestry_kapok");
				BlockObjectHolder.chair_wood_ironage_classic_forestry_larch = registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_forestry_larch", (float)10, 1), "chair_wood_ironage_classic_forestry_larch");
				BlockObjectHolder.chair_wood_ironage_classic_forestry_lime = registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_forestry_lime", (float)10, 1), "chair_wood_ironage_classic_forestry_lime");
				BlockObjectHolder.chair_wood_ironage_classic_forestry_mahoe = registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_forestry_mahoe", (float)10, 1), "chair_wood_ironage_classic_forestry_mahoe");
				BlockObjectHolder.chair_wood_ironage_classic_forestry_mahogany = registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_forestry_mahogany", (float)10, 1), "chair_wood_ironage_classic_forestry_mahogany");
				BlockObjectHolder.chair_wood_ironage_classic_forestry_maple = registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_forestry_maple", (float)10, 1), "chair_wood_ironage_classic_forestry_maple");
				BlockObjectHolder.chair_wood_ironage_classic_forestry_padauk = registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_forestry_padauk", (float)10, 1), "chair_wood_ironage_classic_forestry_padauk");
				BlockObjectHolder.chair_wood_ironage_classic_forestry_palm = registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_forestry_palm", (float)10, 1), "chair_wood_ironage_classic_forestry_palm");
				BlockObjectHolder.chair_wood_ironage_classic_forestry_papaya = registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_forestry_papaya", (float)10, 1), "chair_wood_ironage_classic_forestry_papaya");
				BlockObjectHolder.chair_wood_ironage_classic_forestry_pine = registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_forestry_pine", (float)10, 1), "chair_wood_ironage_classic_forestry_pine");
				BlockObjectHolder.chair_wood_ironage_classic_forestry_plum = registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_forestry_plum", (float)10, 1), "chair_wood_ironage_classic_forestry_plum");
				BlockObjectHolder.chair_wood_ironage_classic_forestry_poplar = registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_forestry_poplar", (float)10, 1), "chair_wood_ironage_classic_forestry_poplar");
				BlockObjectHolder.chair_wood_ironage_classic_forestry_sequoia = registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_forestry_sequoia", (float)10, 1), "chair_wood_ironage_classic_forestry_sequoia");
				BlockObjectHolder.chair_wood_ironage_classic_forestry_teak = registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_forestry_teak", (float)10, 1), "chair_wood_ironage_classic_forestry_teak");
				BlockObjectHolder.chair_wood_ironage_classic_forestry_walnut = registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_forestry_walnut", (float)10, 1), "chair_wood_ironage_classic_forestry_walnut");
				BlockObjectHolder.chair_wood_ironage_classic_forestry_wenge = registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_forestry_wenge", (float)10, 1), "chair_wood_ironage_classic_forestry_wenge");
				BlockObjectHolder.chair_wood_ironage_classic_forestry_willow = registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_forestry_willow", (float)10, 1), "chair_wood_ironage_classic_forestry_willow");
				BlockObjectHolder.chair_wood_ironage_classic_forestry_zebrawood = registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_forestry_zebrawood", (float)10, 1), "chair_wood_ironage_classic_forestry_zebrawood");
			}
			
			if (IronAgeFurnitureConfiguration.GENERATE_SHIELD_CHAIRS) {
				BlockObjectHolder.chair_wood_ironage_shield_forestry_acacia = registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_forestry_acacia", (float)10, 1), "chair_wood_ironage_shield_forestry_acacia");
				BlockObjectHolder.chair_wood_ironage_shield_forestry_balsa = registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_forestry_balsa", (float)10, 1), "chair_wood_ironage_shield_forestry_balsa");
				BlockObjectHolder.chair_wood_ironage_shield_forestry_baobab = registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_forestry_baobab", (float)10, 1), "chair_wood_ironage_shield_forestry_baobab");
				BlockObjectHolder.chair_wood_ironage_shield_forestry_cherry = registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_forestry_cherry", (float)10, 1), "chair_wood_ironage_shield_forestry_cherry");
				BlockObjectHolder.chair_wood_ironage_shield_forestry_chestnut = registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_forestry_chestnut", (float)10, 1), "chair_wood_ironage_shield_forestry_chestnut");
				BlockObjectHolder.chair_wood_ironage_shield_forestry_citrus = registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_forestry_citrus", (float)10, 1), "chair_wood_ironage_shield_forestry_citrus");
				BlockObjectHolder.chair_wood_ironage_shield_forestry_cocobolo = registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_forestry_cocobolo", (float)10, 1), "chair_wood_ironage_shield_forestry_cocobolo");
				BlockObjectHolder.chair_wood_ironage_shield_forestry_ebony = registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_forestry_ebony", (float)10, 1), "chair_wood_ironage_shield_forestry_ebony");
				BlockObjectHolder.chair_wood_ironage_shield_forestry_giganteum = registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_forestry_giganteum", (float)10, 1), "chair_wood_ironage_shield_forestry_giganteum");
				BlockObjectHolder.chair_wood_ironage_shield_forestry_greenheart = registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_forestry_greenheart", (float)10, 1), "chair_wood_ironage_shield_forestry_greenheart");
				BlockObjectHolder.chair_wood_ironage_shield_forestry_ipe = registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_forestry_ipe", (float)10, 1), "chair_wood_ironage_shield_forestry_ipe");
				BlockObjectHolder.chair_wood_ironage_shield_forestry_kapok = registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_forestry_kapok", (float)10, 1), "chair_wood_ironage_shield_forestry_kapok");
				BlockObjectHolder.chair_wood_ironage_shield_forestry_larch = registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_forestry_larch", (float)10, 1), "chair_wood_ironage_shield_forestry_larch");
				BlockObjectHolder.chair_wood_ironage_shield_forestry_lime = registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_forestry_lime", (float)10, 1), "chair_wood_ironage_shield_forestry_lime");
				BlockObjectHolder.chair_wood_ironage_shield_forestry_mahoe = registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_forestry_mahoe", (float)10, 1), "chair_wood_ironage_shield_forestry_mahoe");
				BlockObjectHolder.chair_wood_ironage_shield_forestry_mahogany = registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_forestry_mahogany", (float)10, 1), "chair_wood_ironage_shield_forestry_mahogany");
				BlockObjectHolder.chair_wood_ironage_shield_forestry_maple = registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_forestry_maple", (float)10, 1), "chair_wood_ironage_shield_forestry_maple");
				BlockObjectHolder.chair_wood_ironage_shield_forestry_padauk = registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_forestry_padauk", (float)10, 1), "chair_wood_ironage_shield_forestry_padauk");
				BlockObjectHolder.chair_wood_ironage_shield_forestry_palm = registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_forestry_palm", (float)10, 1), "chair_wood_ironage_shield_forestry_palm");
				BlockObjectHolder.chair_wood_ironage_shield_forestry_papaya = registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_forestry_papaya", (float)10, 1), "chair_wood_ironage_shield_forestry_papaya");
				BlockObjectHolder.chair_wood_ironage_shield_forestry_pine = registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_forestry_pine", (float)10, 1), "chair_wood_ironage_shield_forestry_pine");
				BlockObjectHolder.chair_wood_ironage_shield_forestry_plum = registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_forestry_plum", (float)10, 1), "chair_wood_ironage_shield_forestry_plum");
				BlockObjectHolder.chair_wood_ironage_shield_forestry_poplar = registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_forestry_poplar", (float)10, 1), "chair_wood_ironage_shield_forestry_poplar");
				BlockObjectHolder.chair_wood_ironage_shield_forestry_sequoia = registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_forestry_sequoia", (float)10, 1), "chair_wood_ironage_shield_forestry_sequoia");
				BlockObjectHolder.chair_wood_ironage_shield_forestry_teak = registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_forestry_teak", (float)10, 1), "chair_wood_ironage_shield_forestry_teak");
				BlockObjectHolder.chair_wood_ironage_shield_forestry_walnut = registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_forestry_walnut", (float)10, 1), "chair_wood_ironage_shield_forestry_walnut");
				BlockObjectHolder.chair_wood_ironage_shield_forestry_wenge = registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_forestry_wenge", (float)10, 1), "chair_wood_ironage_shield_forestry_wenge");
				BlockObjectHolder.chair_wood_ironage_shield_forestry_willow = registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_forestry_willow", (float)10, 1), "chair_wood_ironage_shield_forestry_willow");
				BlockObjectHolder.chair_wood_ironage_shield_forestry_zebrawood = registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_forestry_zebrawood", (float)10, 1), "chair_wood_ironage_shield_forestry_zebrawood");
			}
			
			if (IronAgeFurnitureConfiguration.GENERATE_SHORT_STOOLS) {
				BlockObjectHolder.chair_wood_ironage_stool_short_forestry_acacia = registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_forestry_acacia", (float)10, false, 0.25, 1), "chair_wood_ironage_stool_short_forestry_acacia");
				BlockObjectHolder.chair_wood_ironage_stool_short_forestry_balsa = registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_forestry_balsa", (float)10, false, 0.25, 1), "chair_wood_ironage_stool_short_forestry_balsa");
				BlockObjectHolder.chair_wood_ironage_stool_short_forestry_baobab = registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_forestry_baobab", (float)10, false, 0.25, 1), "chair_wood_ironage_stool_short_forestry_baobab");
				BlockObjectHolder.chair_wood_ironage_stool_short_forestry_cherry = registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_forestry_cherry", (float)10, false, 0.25, 1), "chair_wood_ironage_stool_short_forestry_cherry");
				BlockObjectHolder.chair_wood_ironage_stool_short_forestry_chestnut = registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_forestry_chestnut", (float)10, false, 0.25, 1), "chair_wood_ironage_stool_short_forestry_chestnut");
				BlockObjectHolder.chair_wood_ironage_stool_short_forestry_citrus = registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_forestry_citrus", (float)10, false, 0.25, 1), "chair_wood_ironage_stool_short_forestry_citrus");
				BlockObjectHolder.chair_wood_ironage_stool_short_forestry_cocobolo = registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_forestry_cocobolo", (float)10, false, 0.25, 1), "chair_wood_ironage_stool_short_forestry_cocobolo");
				BlockObjectHolder.chair_wood_ironage_stool_short_forestry_ebony = registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_forestry_ebony", (float)10, false, 0.25, 1), "chair_wood_ironage_stool_short_forestry_ebony");
				BlockObjectHolder.chair_wood_ironage_stool_short_forestry_giganteum = registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_forestry_giganteum", (float)10, false, 0.25, 1), "chair_wood_ironage_stool_short_forestry_giganteum");
				BlockObjectHolder.chair_wood_ironage_stool_short_forestry_greenheart = registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_forestry_greenheart", (float)10, false, 0.25, 1), "chair_wood_ironage_stool_short_forestry_greenheart");
				BlockObjectHolder.chair_wood_ironage_stool_short_forestry_ipe = registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_forestry_ipe", (float)10, false, 0.25, 1), "chair_wood_ironage_stool_short_forestry_ipe");
				BlockObjectHolder.chair_wood_ironage_stool_short_forestry_kapok = registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_forestry_kapok", (float)10, false, 0.25, 1), "chair_wood_ironage_stool_short_forestry_kapok");
				BlockObjectHolder.chair_wood_ironage_stool_short_forestry_larch = registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_forestry_larch", (float)10, false, 0.25, 1), "chair_wood_ironage_stool_short_forestry_larch");
				BlockObjectHolder.chair_wood_ironage_stool_short_forestry_lime = registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_forestry_lime", (float)10, false, 0.25, 1), "chair_wood_ironage_stool_short_forestry_lime");
				BlockObjectHolder.chair_wood_ironage_stool_short_forestry_mahoe = registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_forestry_mahoe", (float)10, false, 0.25, 1), "chair_wood_ironage_stool_short_forestry_mahoe");
				BlockObjectHolder.chair_wood_ironage_stool_short_forestry_mahogany = registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_forestry_mahogany", (float)10, false, 0.25, 1), "chair_wood_ironage_stool_short_forestry_mahogany");
				BlockObjectHolder.chair_wood_ironage_stool_short_forestry_maple = registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_forestry_maple", (float)10, false, 0.25, 1), "chair_wood_ironage_stool_short_forestry_maple");
				BlockObjectHolder.chair_wood_ironage_stool_short_forestry_padauk = registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_forestry_padauk", (float)10, false, 0.25, 1), "chair_wood_ironage_stool_short_forestry_padauk");
				BlockObjectHolder.chair_wood_ironage_stool_short_forestry_palm = registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_forestry_palm", (float)10, false, 0.25, 1), "chair_wood_ironage_stool_short_forestry_palm");
				BlockObjectHolder.chair_wood_ironage_stool_short_forestry_papaya = registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_forestry_papaya", (float)10, false, 0.25, 1), "chair_wood_ironage_stool_short_forestry_papaya");
				BlockObjectHolder.chair_wood_ironage_stool_short_forestry_pine = registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_forestry_pine", (float)10, false, 0.25, 1), "chair_wood_ironage_stool_short_forestry_pine");
				BlockObjectHolder.chair_wood_ironage_stool_short_forestry_plum = registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_forestry_plum", (float)10, false, 0.25, 1), "chair_wood_ironage_stool_short_forestry_plum");
				BlockObjectHolder.chair_wood_ironage_stool_short_forestry_poplar = registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_forestry_poplar", (float)10, false, 0.25, 1), "chair_wood_ironage_stool_short_forestry_poplar");
				BlockObjectHolder.chair_wood_ironage_stool_short_forestry_sequoia = registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_forestry_sequoia", (float)10, false, 0.25, 1), "chair_wood_ironage_stool_short_forestry_sequoia");
				BlockObjectHolder.chair_wood_ironage_stool_short_forestry_teak = registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_forestry_teak", (float)10, false, 0.25, 1), "chair_wood_ironage_stool_short_forestry_teak");
				BlockObjectHolder.chair_wood_ironage_stool_short_forestry_walnut = registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_forestry_walnut", (float)10, false, 0.25, 1), "chair_wood_ironage_stool_short_forestry_walnut");
				BlockObjectHolder.chair_wood_ironage_stool_short_forestry_wenge = registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_forestry_wenge", (float)10, false, 0.25, 1), "chair_wood_ironage_stool_short_forestry_wenge");
				BlockObjectHolder.chair_wood_ironage_stool_short_forestry_willow = registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_forestry_willow", (float)10, false, 0.25, 1), "chair_wood_ironage_stool_short_forestry_willow");
				BlockObjectHolder.chair_wood_ironage_stool_short_forestry_zebrawood = registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_forestry_zebrawood", (float)10, false, 0.25, 1), "chair_wood_ironage_stool_short_forestry_zebrawood");		
			}
			
			if (IronAgeFurnitureConfiguration.GENERATE_TALL_STOOLS) {
				BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_acacia = registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_forestry_acacia", (float)10, true, 0.6, 1), "chair_wood_ironage_stool_tall_forestry_acacia");
				BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_balsa = registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_forestry_balsa", (float)10, true, 0.6, 1), "chair_wood_ironage_stool_tall_forestry_balsa");
				BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_baobab = registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_forestry_baobab", (float)10, true, 0.6, 1), "chair_wood_ironage_stool_tall_forestry_baobab");
				BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_cherry = registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_forestry_cherry", (float)10, true, 0.6, 1), "chair_wood_ironage_stool_tall_forestry_cherry");
				BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_chestnut = registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_forestry_chestnut", (float)10, true, 0.6, 1), "chair_wood_ironage_stool_tall_forestry_chestnut");
				BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_citrus = registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_forestry_citrus", (float)10, true, 0.6, 1), "chair_wood_ironage_stool_tall_forestry_citrus");
				BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_cocobolo = registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_forestry_cocobolo", (float)10, true, 0.6, 1), "chair_wood_ironage_stool_tall_forestry_cocobolo");
				BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_ebony = registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_forestry_ebony", (float)10, true, 0.6, 1), "chair_wood_ironage_stool_tall_forestry_ebony");
				BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_giganteum = registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_forestry_giganteum", (float)10, true, 0.6, 1), "chair_wood_ironage_stool_tall_forestry_giganteum");
				BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_greenheart = registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_forestry_greenheart", (float)10, true, 0.6, 1), "chair_wood_ironage_stool_tall_forestry_greenheart");
				BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_ipe = registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_forestry_ipe", (float)10, true, 0.6, 1), "chair_wood_ironage_stool_tall_forestry_ipe");
				BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_kapok = registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_forestry_kapok", (float)10, true, 0.6, 1), "chair_wood_ironage_stool_tall_forestry_kapok");
				BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_larch = registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_forestry_larch", (float)10, true, 0.6, 1), "chair_wood_ironage_stool_tall_forestry_larch");
				BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_lime = registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_forestry_lime", (float)10, true, 0.6, 1), "chair_wood_ironage_stool_tall_forestry_lime");
				BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_mahoe = registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_forestry_mahoe", (float)10, true, 0.6, 1), "chair_wood_ironage_stool_tall_forestry_mahoe");
				BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_mahogany = registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_forestry_mahogany", (float)10, true, 0.6, 1), "chair_wood_ironage_stool_tall_forestry_mahogany");
				BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_maple = registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_forestry_maple", (float)10, true, 0.6, 1), "chair_wood_ironage_stool_tall_forestry_maple");
				BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_padauk = registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_forestry_padauk", (float)10, true, 0.6, 1), "chair_wood_ironage_stool_tall_forestry_padauk");
				BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_palm = registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_forestry_palm", (float)10, true, 0.6, 1), "chair_wood_ironage_stool_tall_forestry_palm");
				BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_papaya = registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_forestry_papaya", (float)10, true, 0.6, 1), "chair_wood_ironage_stool_tall_forestry_papaya");
				BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_pine = registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_forestry_pine", (float)10, true, 0.6, 1), "chair_wood_ironage_stool_tall_forestry_pine");
				BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_plum = registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_forestry_plum", (float)10, true, 0.6, 1), "chair_wood_ironage_stool_tall_forestry_plum");
				BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_poplar = registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_forestry_poplar", (float)10, true, 0.6, 1), "chair_wood_ironage_stool_tall_forestry_poplar");
				BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_sequoia = registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_forestry_sequoia", (float)10, true, 0.6, 1), "chair_wood_ironage_stool_tall_forestry_sequoia");
				BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_teak = registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_forestry_teak", (float)10, true, 0.6, 1), "chair_wood_ironage_stool_tall_forestry_teak");
				BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_walnut = registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_forestry_walnut", (float)10, true, 0.6, 1), "chair_wood_ironage_stool_tall_forestry_walnut");
				BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_wenge = registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_forestry_wenge", (float)10, true, 0.6, 1), "chair_wood_ironage_stool_tall_forestry_wenge");
				BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_willow = registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_forestry_willow", (float)10, true, 0.6, 1), "chair_wood_ironage_stool_tall_forestry_willow");
				BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_zebrawood = registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_forestry_zebrawood", (float)10, true, 0.6, 1), "chair_wood_ironage_stool_tall_forestry_zebrawood");
			}
		}
	}
	
    private static Block registerBlock(Block block, String name, int maxStackSize) {
    	GameRegistry.register(block.setRegistryName(Ironagefurniture.MODID, name));
    	block.setUnlocalizedName(Ironagefurniture.MODID + "." + name);
		
		ItemBlock itemBlock = new ItemBlock(block);
		
		itemBlock.setMaxStackSize(maxStackSize);
		
		ItemInitialiser.RegisterItem(itemBlock, name);
		Ironagefurniture.BlockRegistry.put(name, block);
		
		return block;
    }
    
	private static Block registerBlock(Block block, String name) {
		return registerBlock(block, name, 16);
	}
}