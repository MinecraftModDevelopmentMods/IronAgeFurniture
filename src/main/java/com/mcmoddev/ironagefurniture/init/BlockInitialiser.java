package com.mcmoddev.ironagefurniture.init;

import com.mcmoddev.ironagefurniture.BlockObjectHolder;
import com.mcmoddev.ironagefurniture.IronAgeFurnitureConfiguration;
import com.mcmoddev.ironagefurniture.Ironagefurniture;
import com.mcmoddev.ironagefurniture.api.FurnitureFactory;

import net.minecraft.block.Block;
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
			BlockObjectHolder.chair_wood_ironage_classic_oak = FurnitureFactory.CreateWoodChair("chair_wood_ironage_classic_oak");
			BlockObjectHolder.chair_wood_ironage_classic_acacia = FurnitureFactory.CreateWoodChair("chair_wood_ironage_classic_acacia"); 
			BlockObjectHolder.chair_wood_ironage_classic_big_oak = FurnitureFactory.CreateWoodChair("chair_wood_ironage_classic_big_oak");
			BlockObjectHolder.chair_wood_ironage_classic_birch = FurnitureFactory.CreateWoodChair("chair_wood_ironage_classic_birch");
			BlockObjectHolder.chair_wood_ironage_classic_jungle = FurnitureFactory.CreateWoodChair("chair_wood_ironage_classic_jungle");
			BlockObjectHolder.chair_wood_ironage_classic_spruce = FurnitureFactory.CreateWoodChair("chair_wood_ironage_classic_spruce");
		}
		
		if (IronAgeFurnitureConfiguration.GENERATE_SHIELD_CHAIRS) {
			BlockObjectHolder.chair_wood_ironage_shield_oak = FurnitureFactory.CreateWoodShieldChair("chair_wood_ironage_shield_oak"); 
			BlockObjectHolder.chair_wood_ironage_shield_acacia = FurnitureFactory.CreateWoodShieldChair("chair_wood_ironage_shield_acacia"); 
			BlockObjectHolder.chair_wood_ironage_shield_big_oak = FurnitureFactory.CreateWoodShieldChair("chair_wood_ironage_shield_big_oak"); 
			BlockObjectHolder.chair_wood_ironage_shield_birch = FurnitureFactory.CreateWoodShieldChair("chair_wood_ironage_shield_birch"); 
			BlockObjectHolder.chair_wood_ironage_shield_jungle = FurnitureFactory.CreateWoodShieldChair("chair_wood_ironage_shield_jungle");
			BlockObjectHolder.chair_wood_ironage_shield_spruce = FurnitureFactory.CreateWoodShieldChair("chair_wood_ironage_shield_spruce");
		}
		
		if (IronAgeFurnitureConfiguration.GENERATE_SHORT_STOOLS) {
			BlockObjectHolder.chair_wood_ironage_stool_short_oak =  FurnitureFactory.CreateWoodShortStool("chair_wood_ironage_stool_short_oak");
			BlockObjectHolder.chair_wood_ironage_stool_short_acacia = FurnitureFactory.CreateWoodShortStool("chair_wood_ironage_stool_short_acacia");
			BlockObjectHolder.chair_wood_ironage_stool_short_big_oak = FurnitureFactory.CreateWoodShortStool("chair_wood_ironage_stool_short_big_oak");
			BlockObjectHolder.chair_wood_ironage_stool_short_birch =  FurnitureFactory.CreateWoodShortStool("chair_wood_ironage_stool_short_birch");
			BlockObjectHolder.chair_wood_ironage_stool_short_jungle = FurnitureFactory.CreateWoodShortStool("chair_wood_ironage_stool_short_jungle");
			BlockObjectHolder.chair_wood_ironage_stool_short_spruce = FurnitureFactory.CreateWoodShortStool("chair_wood_ironage_stool_short_spruce");
		}
		
		if (IronAgeFurnitureConfiguration.GENERATE_TALL_STOOLS) {
			BlockObjectHolder.chair_wood_ironage_stool_tall_acacia = FurnitureFactory.CreateWoodTallStool("chair_wood_ironage_stool_tall_acacia");
			BlockObjectHolder.chair_wood_ironage_stool_tall_big_oak = FurnitureFactory.CreateWoodTallStool("chair_wood_ironage_stool_tall_big_oak");
			BlockObjectHolder.chair_wood_ironage_stool_tall_birch = FurnitureFactory.CreateWoodTallStool("chair_wood_ironage_stool_tall_birch");
			BlockObjectHolder.chair_wood_ironage_stool_tall_jungle = FurnitureFactory.CreateWoodTallStool("chair_wood_ironage_stool_tall_jungle");
			BlockObjectHolder.chair_wood_ironage_stool_tall_oak = FurnitureFactory.CreateWoodTallStool("chair_wood_ironage_stool_tall_oak");
			BlockObjectHolder.chair_wood_ironage_stool_tall_spruce = FurnitureFactory.CreateWoodTallStool("chair_wood_ironage_stool_tall_spruce");
		}
		

		
		if (IronAgeFurnitureConfiguration.INTEGRATION_IMMERSIVEENGINEERING && Loader.isModLoaded("immersiveengineering")) {
			if (IronAgeFurnitureConfiguration.GENERATE_CLASSIC_CHAIRS) {
				BlockObjectHolder.chair_wood_ironage_classic_immersiveengineering_treatedWood = FurnitureFactory.CreateWoodChair("chair_wood_ironage_classic_immersiveengineering_treatedWood");
			}
			
			if (IronAgeFurnitureConfiguration.GENERATE_SHIELD_CHAIRS) {
				BlockObjectHolder.chair_wood_ironage_shield_immersiveengineering_treatedWood = FurnitureFactory.CreateWoodShieldChair("chair_wood_ironage_shield_immersiveengineering_treatedWood");
			}
			
			if (IronAgeFurnitureConfiguration.GENERATE_SHORT_STOOLS) {
				BlockObjectHolder.chair_wood_ironage_stool_short_immersiveengineering_treatedWood =  FurnitureFactory.CreateWoodShortStool("chair_wood_ironage_stool_short_immersiveengineering_treatedWood");
			}
			
			if (IronAgeFurnitureConfiguration.GENERATE_TALL_STOOLS) {
				BlockObjectHolder.chair_wood_ironage_stool_tall_immersiveengineering_treatedWood = FurnitureFactory.CreateWoodTallStool("chair_wood_ironage_stool_tall_immersiveengineering_treatedWood");
			}
		}
		
		if (IronAgeFurnitureConfiguration.INTEGRATION_BIOMESOPLENTY && Loader.isModLoaded("BiomesOPlenty")) {
			if (IronAgeFurnitureConfiguration.GENERATE_CLASSIC_CHAIRS) {
				BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_cherry         = FurnitureFactory.CreateWoodChair("chair_wood_ironage_classic_biomesoplenty_cherry");
				BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_ebony          = FurnitureFactory.CreateWoodChair("chair_wood_ironage_classic_biomesoplenty_ebony");
				BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_ethereal       = FurnitureFactory.CreateWoodChair("chair_wood_ironage_classic_biomesoplenty_ethereal");
				BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_eucalyptus     = FurnitureFactory.CreateWoodChair("chair_wood_ironage_classic_biomesoplenty_eucalyptus");
				BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_fir            = FurnitureFactory.CreateWoodChair("chair_wood_ironage_classic_biomesoplenty_fir");
				BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_hellbark       = FurnitureFactory.CreateWoodChair("chair_wood_ironage_classic_biomesoplenty_hellbark");
				BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_jacaranda      = FurnitureFactory.CreateWoodChair("chair_wood_ironage_classic_biomesoplenty_jacaranda");
				BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_magic          = FurnitureFactory.CreateWoodChair("chair_wood_ironage_classic_biomesoplenty_magic");
				BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_mahogany       = FurnitureFactory.CreateWoodChair("chair_wood_ironage_classic_biomesoplenty_mahogany");
				BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_mangrove       = FurnitureFactory.CreateWoodChair("chair_wood_ironage_classic_biomesoplenty_mangrove");	
				BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_palm           = FurnitureFactory.CreateWoodChair("chair_wood_ironage_classic_biomesoplenty_palm");
				BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_pine           = FurnitureFactory.CreateWoodChair("chair_wood_ironage_classic_biomesoplenty_pine");
				BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_redwood        = FurnitureFactory.CreateWoodChair("chair_wood_ironage_classic_biomesoplenty_redwood");
				BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_sacred_oak     = FurnitureFactory.CreateWoodChair("chair_wood_ironage_classic_biomesoplenty_sacred_oak");	 
				BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_umbran         = FurnitureFactory.CreateWoodChair("chair_wood_ironage_classic_biomesoplenty_umbran");
				BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_willow         = FurnitureFactory.CreateWoodChair("chair_wood_ironage_classic_biomesoplenty_willow");
			}
			
			if (IronAgeFurnitureConfiguration.GENERATE_SHIELD_CHAIRS) {
				BlockObjectHolder.chair_wood_ironage_shield_biomesoplenty_cherry		= 	FurnitureFactory.CreateWoodShieldChair("chair_wood_ironage_shield_biomesoplenty_cherry");
				BlockObjectHolder.chair_wood_ironage_shield_biomesoplenty_ebony			= 	FurnitureFactory.CreateWoodShieldChair("chair_wood_ironage_shield_biomesoplenty_ebony");
				BlockObjectHolder.chair_wood_ironage_shield_biomesoplenty_ethereal		= 	FurnitureFactory.CreateWoodShieldChair("chair_wood_ironage_shield_biomesoplenty_ethereal");
				BlockObjectHolder.chair_wood_ironage_shield_biomesoplenty_eucalyptus	= 	FurnitureFactory.CreateWoodShieldChair("chair_wood_ironage_shield_biomesoplenty_eucalyptus");
				BlockObjectHolder.chair_wood_ironage_shield_biomesoplenty_fir			= 	FurnitureFactory.CreateWoodShieldChair("chair_wood_ironage_shield_biomesoplenty_fir");
				BlockObjectHolder.chair_wood_ironage_shield_biomesoplenty_hellbark		= 	FurnitureFactory.CreateWoodShieldChair("chair_wood_ironage_shield_biomesoplenty_hellbark");
				BlockObjectHolder.chair_wood_ironage_shield_biomesoplenty_jacaranda		= 	FurnitureFactory.CreateWoodShieldChair("chair_wood_ironage_shield_biomesoplenty_jacaranda");
				BlockObjectHolder.chair_wood_ironage_shield_biomesoplenty_magic			= 	FurnitureFactory.CreateWoodShieldChair("chair_wood_ironage_shield_biomesoplenty_magic");
				BlockObjectHolder.chair_wood_ironage_shield_biomesoplenty_mahogany		= 	FurnitureFactory.CreateWoodShieldChair("chair_wood_ironage_shield_biomesoplenty_mahogany");
				BlockObjectHolder.chair_wood_ironage_shield_biomesoplenty_mangrove		= 	FurnitureFactory.CreateWoodShieldChair("chair_wood_ironage_shield_biomesoplenty_mangrove");
				BlockObjectHolder.chair_wood_ironage_shield_biomesoplenty_palm			= 	FurnitureFactory.CreateWoodShieldChair("chair_wood_ironage_shield_biomesoplenty_palm");
				BlockObjectHolder.chair_wood_ironage_shield_biomesoplenty_pine			= 	FurnitureFactory.CreateWoodShieldChair("chair_wood_ironage_shield_biomesoplenty_pine");
				BlockObjectHolder.chair_wood_ironage_shield_biomesoplenty_redwood		= 	FurnitureFactory.CreateWoodShieldChair("chair_wood_ironage_shield_biomesoplenty_redwood");
				BlockObjectHolder.chair_wood_ironage_shield_biomesoplenty_sacred_oak	= 	FurnitureFactory.CreateWoodShieldChair("chair_wood_ironage_shield_biomesoplenty_sacred_oak");
				BlockObjectHolder.chair_wood_ironage_shield_biomesoplenty_umbran		= 	FurnitureFactory.CreateWoodShieldChair("chair_wood_ironage_shield_biomesoplenty_umbran");
				BlockObjectHolder.chair_wood_ironage_shield_biomesoplenty_willow		= 	FurnitureFactory.CreateWoodShieldChair("chair_wood_ironage_shield_biomesoplenty_willow");
			}
			
			if (IronAgeFurnitureConfiguration.GENERATE_SHORT_STOOLS) {
				BlockObjectHolder.chair_wood_ironage_stool_short_biomesoplenty_cherry			= FurnitureFactory.CreateWoodShortStool("chair_wood_ironage_stool_short_biomesoplenty_cherry");
				BlockObjectHolder.chair_wood_ironage_stool_short_biomesoplenty_ebony			= FurnitureFactory.CreateWoodShortStool("chair_wood_ironage_stool_short_biomesoplenty_ebony");
				BlockObjectHolder.chair_wood_ironage_stool_short_biomesoplenty_ethereal			= FurnitureFactory.CreateWoodShortStool("chair_wood_ironage_stool_short_biomesoplenty_ethereal");
				BlockObjectHolder.chair_wood_ironage_stool_short_biomesoplenty_eucalyptus		= FurnitureFactory.CreateWoodShortStool("chair_wood_ironage_stool_short_biomesoplenty_eucalyptus");
				BlockObjectHolder.chair_wood_ironage_stool_short_biomesoplenty_fir				= FurnitureFactory.CreateWoodShortStool("chair_wood_ironage_stool_short_biomesoplenty_fir");
				BlockObjectHolder.chair_wood_ironage_stool_short_biomesoplenty_hellbark			= FurnitureFactory.CreateWoodShortStool("chair_wood_ironage_stool_short_biomesoplenty_hellbark");
				BlockObjectHolder.chair_wood_ironage_stool_short_biomesoplenty_jacaranda		= FurnitureFactory.CreateWoodShortStool("chair_wood_ironage_stool_short_biomesoplenty_jacaranda");
				BlockObjectHolder.chair_wood_ironage_stool_short_biomesoplenty_magic			= FurnitureFactory.CreateWoodShortStool("chair_wood_ironage_stool_short_biomesoplenty_magic");
				BlockObjectHolder.chair_wood_ironage_stool_short_biomesoplenty_mahogany			= FurnitureFactory.CreateWoodShortStool("chair_wood_ironage_stool_short_biomesoplenty_mahogany");
				BlockObjectHolder.chair_wood_ironage_stool_short_biomesoplenty_mangrove			= FurnitureFactory.CreateWoodShortStool("chair_wood_ironage_stool_short_biomesoplenty_mangrove");
				BlockObjectHolder.chair_wood_ironage_stool_short_biomesoplenty_palm				= FurnitureFactory.CreateWoodShortStool("chair_wood_ironage_stool_short_biomesoplenty_palm");
				BlockObjectHolder.chair_wood_ironage_stool_short_biomesoplenty_pine				= FurnitureFactory.CreateWoodShortStool("chair_wood_ironage_stool_short_biomesoplenty_pine");
				BlockObjectHolder.chair_wood_ironage_stool_short_biomesoplenty_redwood			= FurnitureFactory.CreateWoodShortStool("chair_wood_ironage_stool_short_biomesoplenty_redwood");
				BlockObjectHolder.chair_wood_ironage_stool_short_biomesoplenty_sacred_oak		= FurnitureFactory.CreateWoodShortStool("chair_wood_ironage_stool_short_biomesoplenty_sacred_oak");
				BlockObjectHolder.chair_wood_ironage_stool_short_biomesoplenty_umbran			= FurnitureFactory.CreateWoodShortStool("chair_wood_ironage_stool_short_biomesoplenty_umbran");
				BlockObjectHolder.chair_wood_ironage_stool_short_biomesoplenty_willow			= FurnitureFactory.CreateWoodShortStool("chair_wood_ironage_stool_short_biomesoplenty_willow");
			}
			
			if (IronAgeFurnitureConfiguration.GENERATE_TALL_STOOLS) {
				BlockObjectHolder.chair_wood_ironage_stool_tall_biomesoplenty_cherry			=	FurnitureFactory.CreateWoodTallStool("chair_wood_ironage_stool_tall_biomesoplenty_cherry");
				BlockObjectHolder.chair_wood_ironage_stool_tall_biomesoplenty_ebony				=	FurnitureFactory.CreateWoodTallStool("chair_wood_ironage_stool_tall_biomesoplenty_ebony");
				BlockObjectHolder.chair_wood_ironage_stool_tall_biomesoplenty_ethereal			=	FurnitureFactory.CreateWoodTallStool("chair_wood_ironage_stool_tall_biomesoplenty_ethereal");
				BlockObjectHolder.chair_wood_ironage_stool_tall_biomesoplenty_eucalyptus		=	FurnitureFactory.CreateWoodTallStool("chair_wood_ironage_stool_tall_biomesoplenty_eucalyptus");
				BlockObjectHolder.chair_wood_ironage_stool_tall_biomesoplenty_fir				=	FurnitureFactory.CreateWoodTallStool("chair_wood_ironage_stool_tall_biomesoplenty_fir");
				BlockObjectHolder.chair_wood_ironage_stool_tall_biomesoplenty_hellbark			=	FurnitureFactory.CreateWoodTallStool("chair_wood_ironage_stool_tall_biomesoplenty_hellbark");
				BlockObjectHolder.chair_wood_ironage_stool_tall_biomesoplenty_jacaranda			=	FurnitureFactory.CreateWoodTallStool("chair_wood_ironage_stool_tall_biomesoplenty_jacaranda");
				BlockObjectHolder.chair_wood_ironage_stool_tall_biomesoplenty_magic				=	FurnitureFactory.CreateWoodTallStool("chair_wood_ironage_stool_tall_biomesoplenty_magic");
				BlockObjectHolder.chair_wood_ironage_stool_tall_biomesoplenty_mahogany			=	FurnitureFactory.CreateWoodTallStool("chair_wood_ironage_stool_tall_biomesoplenty_mahogany");
				BlockObjectHolder.chair_wood_ironage_stool_tall_biomesoplenty_mangrove			=	FurnitureFactory.CreateWoodTallStool("chair_wood_ironage_stool_tall_biomesoplenty_mangrove");
				BlockObjectHolder.chair_wood_ironage_stool_tall_biomesoplenty_palm				=	FurnitureFactory.CreateWoodTallStool("chair_wood_ironage_stool_tall_biomesoplenty_palm");
				BlockObjectHolder.chair_wood_ironage_stool_tall_biomesoplenty_pine				=	FurnitureFactory.CreateWoodTallStool("chair_wood_ironage_stool_tall_biomesoplenty_pine");
				BlockObjectHolder.chair_wood_ironage_stool_tall_biomesoplenty_redwood			=	FurnitureFactory.CreateWoodTallStool("chair_wood_ironage_stool_tall_biomesoplenty_redwood");
				BlockObjectHolder.chair_wood_ironage_stool_tall_biomesoplenty_sacred_oak		=	FurnitureFactory.CreateWoodTallStool("chair_wood_ironage_stool_tall_biomesoplenty_sacred_oak");
				BlockObjectHolder.chair_wood_ironage_stool_tall_biomesoplenty_umbran			=	FurnitureFactory.CreateWoodTallStool("chair_wood_ironage_stool_tall_biomesoplenty_umbran");
				BlockObjectHolder.chair_wood_ironage_stool_tall_biomesoplenty_willow			=	FurnitureFactory.CreateWoodTallStool("chair_wood_ironage_stool_tall_biomesoplenty_willow");
			}
		}
		
		if (IronAgeFurnitureConfiguration.INTEGRATION_NATURA && Loader.isModLoaded("natura")) {
			if (IronAgeFurnitureConfiguration.GENERATE_CLASSIC_CHAIRS) {
				BlockObjectHolder.chair_wood_ironage_classic_natura_amaranth		= FurnitureFactory.CreateWoodChair("chair_wood_ironage_classic_natura_amaranth");
				BlockObjectHolder.chair_wood_ironage_classic_natura_eucalyptus		= FurnitureFactory.CreateWoodChair("chair_wood_ironage_classic_natura_eucalyptus");
				BlockObjectHolder.chair_wood_ironage_classic_natura_hopseed			= FurnitureFactory.CreateWoodChair("chair_wood_ironage_classic_natura_hopseed");
				BlockObjectHolder.chair_wood_ironage_classic_natura_maple			= FurnitureFactory.CreateWoodChair("chair_wood_ironage_classic_natura_maple");
				BlockObjectHolder.chair_wood_ironage_classic_natura_redwood			= FurnitureFactory.CreateWoodChair("chair_wood_ironage_classic_natura_redwood");
				BlockObjectHolder.chair_wood_ironage_classic_natura_sakura			= FurnitureFactory.CreateWoodChair("chair_wood_ironage_classic_natura_sakura");
				BlockObjectHolder.chair_wood_ironage_classic_natura_silverbell		= FurnitureFactory.CreateWoodChair("chair_wood_ironage_classic_natura_silverbell");
				BlockObjectHolder.chair_wood_ironage_classic_natura_tiger			= FurnitureFactory.CreateWoodChair("chair_wood_ironage_classic_natura_tiger");
				BlockObjectHolder.chair_wood_ironage_classic_natura_willow			= FurnitureFactory.CreateWoodChair("chair_wood_ironage_classic_natura_willow");
				BlockObjectHolder.chair_wood_ironage_classic_natura_bloodwood		= FurnitureFactory.CreateWoodChair("chair_wood_ironage_classic_natura_bloodwood");
				BlockObjectHolder.chair_wood_ironage_classic_natura_darkwood		= FurnitureFactory.CreateWoodChair("chair_wood_ironage_classic_natura_darkwood");
				BlockObjectHolder.chair_wood_ironage_classic_natura_fusewood		= FurnitureFactory.CreateWoodChair("chair_wood_ironage_classic_natura_fusewood");
				BlockObjectHolder.chair_wood_ironage_classic_natura_ghostwood		= FurnitureFactory.CreateWoodChair("chair_wood_ironage_classic_natura_ghostwood");
			}
			
			if (IronAgeFurnitureConfiguration.GENERATE_SHIELD_CHAIRS) {
				BlockObjectHolder.chair_wood_ironage_shield_natura_amaranth			=	FurnitureFactory.CreateWoodShieldChair("chair_wood_ironage_shield_natura_amaranth");
				BlockObjectHolder.chair_wood_ironage_shield_natura_eucalyptus		=	FurnitureFactory.CreateWoodShieldChair("chair_wood_ironage_shield_natura_eucalyptus");
				BlockObjectHolder.chair_wood_ironage_shield_natura_hopseed			=	FurnitureFactory.CreateWoodShieldChair("chair_wood_ironage_shield_natura_hopseed");
				BlockObjectHolder.chair_wood_ironage_shield_natura_maple			=	FurnitureFactory.CreateWoodShieldChair("chair_wood_ironage_shield_natura_maple");
				BlockObjectHolder.chair_wood_ironage_shield_natura_redwood			=	FurnitureFactory.CreateWoodShieldChair("chair_wood_ironage_shield_natura_redwood");
				BlockObjectHolder.chair_wood_ironage_shield_natura_sakura			=	FurnitureFactory.CreateWoodShieldChair("chair_wood_ironage_shield_natura_sakura");
				BlockObjectHolder.chair_wood_ironage_shield_natura_silverbell		=	FurnitureFactory.CreateWoodShieldChair("chair_wood_ironage_shield_natura_silverbell");
				BlockObjectHolder.chair_wood_ironage_shield_natura_tiger			=	FurnitureFactory.CreateWoodShieldChair("chair_wood_ironage_shield_natura_tiger");
				BlockObjectHolder.chair_wood_ironage_shield_natura_willow			=	FurnitureFactory.CreateWoodShieldChair("chair_wood_ironage_shield_natura_willow");
				BlockObjectHolder.chair_wood_ironage_shield_natura_bloodwood		=	FurnitureFactory.CreateWoodShieldChair("chair_wood_ironage_shield_natura_bloodwood");
				BlockObjectHolder.chair_wood_ironage_shield_natura_darkwood			=	FurnitureFactory.CreateWoodShieldChair("chair_wood_ironage_shield_natura_darkwood");
				BlockObjectHolder.chair_wood_ironage_shield_natura_fusewood			=	FurnitureFactory.CreateWoodShieldChair("chair_wood_ironage_shield_natura_fusewood");
				BlockObjectHolder.chair_wood_ironage_shield_natura_ghostwood		=	FurnitureFactory.CreateWoodShieldChair("chair_wood_ironage_shield_natura_ghostwood");
			}
			
			if (IronAgeFurnitureConfiguration.GENERATE_SHORT_STOOLS) {
				BlockObjectHolder.chair_wood_ironage_stool_short_natura_amaranth		= FurnitureFactory.CreateWoodShortStool("chair_wood_ironage_stool_short_natura_amaranth");
				BlockObjectHolder.chair_wood_ironage_stool_short_natura_eucalyptus		= FurnitureFactory.CreateWoodShortStool("chair_wood_ironage_stool_short_natura_eucalyptus");
				BlockObjectHolder.chair_wood_ironage_stool_short_natura_hopseed			= FurnitureFactory.CreateWoodShortStool("chair_wood_ironage_stool_short_natura_hopseed");
				BlockObjectHolder.chair_wood_ironage_stool_short_natura_maple			= FurnitureFactory.CreateWoodShortStool("chair_wood_ironage_stool_short_natura_maple");
				BlockObjectHolder.chair_wood_ironage_stool_short_natura_redwood			= FurnitureFactory.CreateWoodShortStool("chair_wood_ironage_stool_short_natura_redwood");
				BlockObjectHolder.chair_wood_ironage_stool_short_natura_sakura			= FurnitureFactory.CreateWoodShortStool("chair_wood_ironage_stool_short_natura_sakura");;
				BlockObjectHolder.chair_wood_ironage_stool_short_natura_silverbell		= FurnitureFactory.CreateWoodShortStool("chair_wood_ironage_stool_short_natura_silverbell");
				BlockObjectHolder.chair_wood_ironage_stool_short_natura_tiger			= FurnitureFactory.CreateWoodShortStool("chair_wood_ironage_stool_short_natura_tiger");
				BlockObjectHolder.chair_wood_ironage_stool_short_natura_willow			= FurnitureFactory.CreateWoodShortStool("chair_wood_ironage_stool_short_natura_willow");
				BlockObjectHolder.chair_wood_ironage_stool_short_natura_bloodwood		= FurnitureFactory.CreateWoodShortStool("chair_wood_ironage_stool_short_natura_bloodwood");
				BlockObjectHolder.chair_wood_ironage_stool_short_natura_darkwood		= FurnitureFactory.CreateWoodShortStool("chair_wood_ironage_stool_short_natura_darkwood");
				BlockObjectHolder.chair_wood_ironage_stool_short_natura_fusewood		= FurnitureFactory.CreateWoodShortStool("chair_wood_ironage_stool_short_natura_fusewood");
				BlockObjectHolder.chair_wood_ironage_stool_short_natura_ghostwood		= FurnitureFactory.CreateWoodShortStool("chair_wood_ironage_stool_short_natura_ghostwood");
			}
			
			if (IronAgeFurnitureConfiguration.GENERATE_TALL_STOOLS) {
				BlockObjectHolder.chair_wood_ironage_stool_tall_natura_amaranth			=	FurnitureFactory.CreateWoodTallStool("chair_wood_ironage_stool_tall_natura_amaranth");
				BlockObjectHolder.chair_wood_ironage_stool_tall_natura_eucalyptus		=	FurnitureFactory.CreateWoodTallStool("chair_wood_ironage_stool_tall_natura_eucalyptus");
				BlockObjectHolder.chair_wood_ironage_stool_tall_natura_hopseed			=	FurnitureFactory.CreateWoodTallStool("chair_wood_ironage_stool_tall_natura_hopseed");
				BlockObjectHolder.chair_wood_ironage_stool_tall_natura_maple			=	FurnitureFactory.CreateWoodTallStool("chair_wood_ironage_stool_tall_natura_maple");
				BlockObjectHolder.chair_wood_ironage_stool_tall_natura_redwood			=	FurnitureFactory.CreateWoodTallStool("chair_wood_ironage_stool_tall_natura_redwood");
				BlockObjectHolder.chair_wood_ironage_stool_tall_natura_sakura			=	FurnitureFactory.CreateWoodTallStool("chair_wood_ironage_stool_tall_natura_sakura");
				BlockObjectHolder.chair_wood_ironage_stool_tall_natura_silverbell		=	FurnitureFactory.CreateWoodTallStool("chair_wood_ironage_stool_tall_natura_silverbell");
				BlockObjectHolder.chair_wood_ironage_stool_tall_natura_tiger			=	FurnitureFactory.CreateWoodTallStool("chair_wood_ironage_stool_tall_natura_tiger");
				BlockObjectHolder.chair_wood_ironage_stool_tall_natura_willow			=	FurnitureFactory.CreateWoodTallStool("chair_wood_ironage_stool_tall_natura_willow");
				BlockObjectHolder.chair_wood_ironage_stool_tall_natura_bloodwood		=	FurnitureFactory.CreateWoodTallStool("chair_wood_ironage_stool_tall_natura_bloodwood");
				BlockObjectHolder.chair_wood_ironage_stool_tall_natura_darkwood			=	FurnitureFactory.CreateWoodTallStool("chair_wood_ironage_stool_tall_natura_darkwood");
				BlockObjectHolder.chair_wood_ironage_stool_tall_natura_fusewood			=	FurnitureFactory.CreateWoodTallStool("chair_wood_ironage_stool_tall_natura_fusewood");
				BlockObjectHolder.chair_wood_ironage_stool_tall_natura_ghostwood		=	FurnitureFactory.CreateWoodTallStool("chair_wood_ironage_stool_tall_natura_ghostwood");
			}
		}
		
		if (IronAgeFurnitureConfiguration.INTEGRATION_FORESTRY && Loader.isModLoaded("forestry")) {
			if (IronAgeFurnitureConfiguration.GENERATE_CLASSIC_CHAIRS) {
				BlockObjectHolder.chair_wood_ironage_classic_forestry_acacia = FurnitureFactory.CreateWoodChair("chair_wood_ironage_classic_forestry_acacia");
				BlockObjectHolder.chair_wood_ironage_classic_forestry_balsa = FurnitureFactory.CreateWoodChair("chair_wood_ironage_classic_forestry_balsa");
				BlockObjectHolder.chair_wood_ironage_classic_forestry_baobab = FurnitureFactory.CreateWoodChair("chair_wood_ironage_classic_forestry_baobab");
				BlockObjectHolder.chair_wood_ironage_classic_forestry_cherry = FurnitureFactory.CreateWoodChair("chair_wood_ironage_classic_forestry_cherry");
				BlockObjectHolder.chair_wood_ironage_classic_forestry_chestnut = FurnitureFactory.CreateWoodChair("chair_wood_ironage_classic_forestry_chestnut");
				BlockObjectHolder.chair_wood_ironage_classic_forestry_citrus = FurnitureFactory.CreateWoodChair("chair_wood_ironage_classic_forestry_citrus");
				BlockObjectHolder.chair_wood_ironage_classic_forestry_cocobolo = FurnitureFactory.CreateWoodChair("chair_wood_ironage_classic_forestry_cocobolo");
				BlockObjectHolder.chair_wood_ironage_classic_forestry_ebony = FurnitureFactory.CreateWoodChair("chair_wood_ironage_classic_forestry_ebony");
				BlockObjectHolder.chair_wood_ironage_classic_forestry_giganteum = FurnitureFactory.CreateWoodChair("chair_wood_ironage_classic_forestry_giganteum");
				BlockObjectHolder.chair_wood_ironage_classic_forestry_greenheart = FurnitureFactory.CreateWoodChair("chair_wood_ironage_classic_forestry_greenheart");
				BlockObjectHolder.chair_wood_ironage_classic_forestry_ipe = FurnitureFactory.CreateWoodChair("chair_wood_ironage_classic_forestry_ipe");
				BlockObjectHolder.chair_wood_ironage_classic_forestry_kapok = FurnitureFactory.CreateWoodChair("chair_wood_ironage_classic_forestry_kapok");
				BlockObjectHolder.chair_wood_ironage_classic_forestry_larch = FurnitureFactory.CreateWoodChair("chair_wood_ironage_classic_forestry_larch");
				BlockObjectHolder.chair_wood_ironage_classic_forestry_lime = FurnitureFactory.CreateWoodChair("chair_wood_ironage_classic_forestry_lime");
				BlockObjectHolder.chair_wood_ironage_classic_forestry_mahoe = FurnitureFactory.CreateWoodChair("chair_wood_ironage_classic_forestry_mahoe");
				BlockObjectHolder.chair_wood_ironage_classic_forestry_mahogany = FurnitureFactory.CreateWoodChair("chair_wood_ironage_classic_forestry_mahogany");
				BlockObjectHolder.chair_wood_ironage_classic_forestry_maple = FurnitureFactory.CreateWoodChair("chair_wood_ironage_classic_forestry_maple");
				BlockObjectHolder.chair_wood_ironage_classic_forestry_padauk = FurnitureFactory.CreateWoodChair("chair_wood_ironage_classic_forestry_padauk");
				BlockObjectHolder.chair_wood_ironage_classic_forestry_palm = FurnitureFactory.CreateWoodChair("chair_wood_ironage_classic_forestry_palm");
				BlockObjectHolder.chair_wood_ironage_classic_forestry_papaya = FurnitureFactory.CreateWoodChair("chair_wood_ironage_classic_forestry_papaya");
				BlockObjectHolder.chair_wood_ironage_classic_forestry_pine = FurnitureFactory.CreateWoodChair("chair_wood_ironage_classic_forestry_pine");
				BlockObjectHolder.chair_wood_ironage_classic_forestry_plum = FurnitureFactory.CreateWoodChair("chair_wood_ironage_classic_forestry_plum");
				BlockObjectHolder.chair_wood_ironage_classic_forestry_poplar = FurnitureFactory.CreateWoodChair("chair_wood_ironage_classic_forestry_poplar");
				BlockObjectHolder.chair_wood_ironage_classic_forestry_sequoia = FurnitureFactory.CreateWoodChair("chair_wood_ironage_classic_forestry_sequoia");
				BlockObjectHolder.chair_wood_ironage_classic_forestry_teak = FurnitureFactory.CreateWoodChair("chair_wood_ironage_classic_forestry_teak");
				BlockObjectHolder.chair_wood_ironage_classic_forestry_walnut = FurnitureFactory.CreateWoodChair("chair_wood_ironage_classic_forestry_walnut");
				BlockObjectHolder.chair_wood_ironage_classic_forestry_wenge = FurnitureFactory.CreateWoodChair("chair_wood_ironage_classic_forestry_wenge");
				BlockObjectHolder.chair_wood_ironage_classic_forestry_willow = FurnitureFactory.CreateWoodChair("chair_wood_ironage_classic_forestry_willow");
				BlockObjectHolder.chair_wood_ironage_classic_forestry_zebrawood = FurnitureFactory.CreateWoodChair("chair_wood_ironage_classic_forestry_zebrawood");
			}
			
			if (IronAgeFurnitureConfiguration.GENERATE_SHIELD_CHAIRS) {
				BlockObjectHolder.chair_wood_ironage_shield_forestry_acacia = FurnitureFactory.CreateWoodShieldChair("chair_wood_ironage_shield_forestry_acacia");
				BlockObjectHolder.chair_wood_ironage_shield_forestry_balsa = FurnitureFactory.CreateWoodShieldChair("chair_wood_ironage_shield_forestry_balsa");
				BlockObjectHolder.chair_wood_ironage_shield_forestry_baobab = FurnitureFactory.CreateWoodShieldChair("chair_wood_ironage_shield_forestry_baobab");
				BlockObjectHolder.chair_wood_ironage_shield_forestry_cherry = FurnitureFactory.CreateWoodShieldChair("chair_wood_ironage_shield_forestry_cherry");
				BlockObjectHolder.chair_wood_ironage_shield_forestry_chestnut = FurnitureFactory.CreateWoodShieldChair("chair_wood_ironage_shield_forestry_chestnut");
				BlockObjectHolder.chair_wood_ironage_shield_forestry_citrus = FurnitureFactory.CreateWoodShieldChair("chair_wood_ironage_shield_forestry_citrus");
				BlockObjectHolder.chair_wood_ironage_shield_forestry_cocobolo = FurnitureFactory.CreateWoodShieldChair("chair_wood_ironage_shield_forestry_cocobolo");
				BlockObjectHolder.chair_wood_ironage_shield_forestry_ebony = FurnitureFactory.CreateWoodShieldChair("chair_wood_ironage_shield_forestry_ebony");
				BlockObjectHolder.chair_wood_ironage_shield_forestry_giganteum = FurnitureFactory.CreateWoodShieldChair("chair_wood_ironage_shield_forestry_giganteum");
				BlockObjectHolder.chair_wood_ironage_shield_forestry_greenheart = FurnitureFactory.CreateWoodShieldChair("chair_wood_ironage_shield_forestry_greenheart");
				BlockObjectHolder.chair_wood_ironage_shield_forestry_ipe = FurnitureFactory.CreateWoodShieldChair("chair_wood_ironage_shield_forestry_ipe");
				BlockObjectHolder.chair_wood_ironage_shield_forestry_kapok = FurnitureFactory.CreateWoodShieldChair("chair_wood_ironage_shield_forestry_kapok");
				BlockObjectHolder.chair_wood_ironage_shield_forestry_larch = FurnitureFactory.CreateWoodShieldChair("chair_wood_ironage_shield_forestry_larch");
				BlockObjectHolder.chair_wood_ironage_shield_forestry_lime = FurnitureFactory.CreateWoodShieldChair("chair_wood_ironage_shield_forestry_lime");
				BlockObjectHolder.chair_wood_ironage_shield_forestry_mahoe = FurnitureFactory.CreateWoodShieldChair("chair_wood_ironage_shield_forestry_mahoe");
				BlockObjectHolder.chair_wood_ironage_shield_forestry_mahogany = FurnitureFactory.CreateWoodShieldChair("chair_wood_ironage_shield_forestry_mahogany");
				BlockObjectHolder.chair_wood_ironage_shield_forestry_maple = FurnitureFactory.CreateWoodShieldChair("chair_wood_ironage_shield_forestry_maple");
				BlockObjectHolder.chair_wood_ironage_shield_forestry_padauk = FurnitureFactory.CreateWoodShieldChair("chair_wood_ironage_shield_forestry_padauk");
				BlockObjectHolder.chair_wood_ironage_shield_forestry_palm = FurnitureFactory.CreateWoodShieldChair("chair_wood_ironage_shield_forestry_palm");
				BlockObjectHolder.chair_wood_ironage_shield_forestry_papaya = FurnitureFactory.CreateWoodShieldChair("chair_wood_ironage_shield_forestry_papaya");
				BlockObjectHolder.chair_wood_ironage_shield_forestry_pine = FurnitureFactory.CreateWoodShieldChair("chair_wood_ironage_shield_forestry_pine");
				BlockObjectHolder.chair_wood_ironage_shield_forestry_plum = FurnitureFactory.CreateWoodShieldChair("chair_wood_ironage_shield_forestry_plum");
				BlockObjectHolder.chair_wood_ironage_shield_forestry_poplar = FurnitureFactory.CreateWoodShieldChair("chair_wood_ironage_shield_forestry_poplar");
				BlockObjectHolder.chair_wood_ironage_shield_forestry_sequoia = FurnitureFactory.CreateWoodShieldChair("chair_wood_ironage_shield_forestry_sequoia");
				BlockObjectHolder.chair_wood_ironage_shield_forestry_teak = FurnitureFactory.CreateWoodShieldChair("chair_wood_ironage_shield_forestry_teak");
				BlockObjectHolder.chair_wood_ironage_shield_forestry_walnut = FurnitureFactory.CreateWoodShieldChair("chair_wood_ironage_shield_forestry_walnut");
				BlockObjectHolder.chair_wood_ironage_shield_forestry_wenge = FurnitureFactory.CreateWoodShieldChair("chair_wood_ironage_shield_forestry_wenge");
				BlockObjectHolder.chair_wood_ironage_shield_forestry_willow = FurnitureFactory.CreateWoodShieldChair("chair_wood_ironage_shield_forestry_willow");
				BlockObjectHolder.chair_wood_ironage_shield_forestry_zebrawood = FurnitureFactory.CreateWoodShieldChair("chair_wood_ironage_shield_forestry_zebrawood");
			}
			
			if (IronAgeFurnitureConfiguration.GENERATE_SHORT_STOOLS) {
				BlockObjectHolder.chair_wood_ironage_stool_short_forestry_acacia = FurnitureFactory.CreateWoodShortStool("chair_wood_ironage_stool_short_forestry_acacia");
				BlockObjectHolder.chair_wood_ironage_stool_short_forestry_balsa = FurnitureFactory.CreateWoodShortStool("chair_wood_ironage_stool_short_forestry_balsa");
				BlockObjectHolder.chair_wood_ironage_stool_short_forestry_baobab = FurnitureFactory.CreateWoodShortStool("chair_wood_ironage_stool_short_forestry_baobab");
				BlockObjectHolder.chair_wood_ironage_stool_short_forestry_cherry = FurnitureFactory.CreateWoodShortStool("chair_wood_ironage_stool_short_forestry_cherry");
				BlockObjectHolder.chair_wood_ironage_stool_short_forestry_chestnut = FurnitureFactory.CreateWoodShortStool("chair_wood_ironage_stool_short_forestry_chestnut");
				BlockObjectHolder.chair_wood_ironage_stool_short_forestry_citrus = FurnitureFactory.CreateWoodShortStool("chair_wood_ironage_stool_short_forestry_citrus");
				BlockObjectHolder.chair_wood_ironage_stool_short_forestry_cocobolo = FurnitureFactory.CreateWoodShortStool("chair_wood_ironage_stool_short_forestry_cocobolo");
				BlockObjectHolder.chair_wood_ironage_stool_short_forestry_ebony = FurnitureFactory.CreateWoodShortStool("chair_wood_ironage_stool_short_forestry_ebony");
				BlockObjectHolder.chair_wood_ironage_stool_short_forestry_giganteum = FurnitureFactory.CreateWoodShortStool("chair_wood_ironage_stool_short_forestry_giganteum");
				BlockObjectHolder.chair_wood_ironage_stool_short_forestry_greenheart = FurnitureFactory.CreateWoodShortStool("chair_wood_ironage_stool_short_forestry_greenheart");
				BlockObjectHolder.chair_wood_ironage_stool_short_forestry_ipe = FurnitureFactory.CreateWoodShortStool("chair_wood_ironage_stool_short_forestry_ipe");
				BlockObjectHolder.chair_wood_ironage_stool_short_forestry_kapok = FurnitureFactory.CreateWoodShortStool("chair_wood_ironage_stool_short_forestry_kapok");
				BlockObjectHolder.chair_wood_ironage_stool_short_forestry_larch = FurnitureFactory.CreateWoodShortStool("chair_wood_ironage_stool_short_forestry_larch");
				BlockObjectHolder.chair_wood_ironage_stool_short_forestry_lime = FurnitureFactory.CreateWoodShortStool("chair_wood_ironage_stool_short_forestry_lime");
				BlockObjectHolder.chair_wood_ironage_stool_short_forestry_mahoe = FurnitureFactory.CreateWoodShortStool("chair_wood_ironage_stool_short_forestry_mahoe");
				BlockObjectHolder.chair_wood_ironage_stool_short_forestry_mahogany = FurnitureFactory.CreateWoodShortStool("chair_wood_ironage_stool_short_forestry_mahogany");
				BlockObjectHolder.chair_wood_ironage_stool_short_forestry_maple = FurnitureFactory.CreateWoodShortStool("chair_wood_ironage_stool_short_forestry_maple");
				BlockObjectHolder.chair_wood_ironage_stool_short_forestry_padauk = FurnitureFactory.CreateWoodShortStool("chair_wood_ironage_stool_short_forestry_padauk");
				BlockObjectHolder.chair_wood_ironage_stool_short_forestry_palm = FurnitureFactory.CreateWoodShortStool("chair_wood_ironage_stool_short_forestry_palm");
				BlockObjectHolder.chair_wood_ironage_stool_short_forestry_papaya = FurnitureFactory.CreateWoodShortStool("chair_wood_ironage_stool_short_forestry_papaya");
				BlockObjectHolder.chair_wood_ironage_stool_short_forestry_pine = FurnitureFactory.CreateWoodShortStool("chair_wood_ironage_stool_short_forestry_pine");
				BlockObjectHolder.chair_wood_ironage_stool_short_forestry_plum = FurnitureFactory.CreateWoodShortStool("chair_wood_ironage_stool_short_forestry_plum");
				BlockObjectHolder.chair_wood_ironage_stool_short_forestry_poplar = FurnitureFactory.CreateWoodShortStool("chair_wood_ironage_stool_short_forestry_poplar");
				BlockObjectHolder.chair_wood_ironage_stool_short_forestry_sequoia = FurnitureFactory.CreateWoodShortStool("chair_wood_ironage_stool_short_forestry_sequoia");
				BlockObjectHolder.chair_wood_ironage_stool_short_forestry_teak = FurnitureFactory.CreateWoodShortStool("chair_wood_ironage_stool_short_forestry_teak");
				BlockObjectHolder.chair_wood_ironage_stool_short_forestry_walnut = FurnitureFactory.CreateWoodShortStool("chair_wood_ironage_stool_short_forestry_walnut");
				BlockObjectHolder.chair_wood_ironage_stool_short_forestry_wenge = FurnitureFactory.CreateWoodShortStool("chair_wood_ironage_stool_short_forestry_wenge");
				BlockObjectHolder.chair_wood_ironage_stool_short_forestry_willow = FurnitureFactory.CreateWoodShortStool("chair_wood_ironage_stool_short_forestry_willow");
				BlockObjectHolder.chair_wood_ironage_stool_short_forestry_zebrawood = FurnitureFactory.CreateWoodShortStool("chair_wood_ironage_stool_short_forestry_zebrawood");
			}
			
			if (IronAgeFurnitureConfiguration.GENERATE_TALL_STOOLS) {
				BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_acacia = FurnitureFactory.CreateWoodTallStool("chair_wood_ironage_stool_tall_forestry_acacia");
				BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_balsa = FurnitureFactory.CreateWoodTallStool("chair_wood_ironage_stool_tall_forestry_balsa");
				BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_baobab = FurnitureFactory.CreateWoodTallStool("chair_wood_ironage_stool_tall_forestry_baobab");
				BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_cherry = FurnitureFactory.CreateWoodTallStool("chair_wood_ironage_stool_tall_forestry_cherry");
				BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_chestnut = FurnitureFactory.CreateWoodTallStool("chair_wood_ironage_stool_tall_forestry_chestnut");
				BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_citrus = FurnitureFactory.CreateWoodTallStool("chair_wood_ironage_stool_tall_forestry_citrus");
				BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_cocobolo = FurnitureFactory.CreateWoodTallStool("chair_wood_ironage_stool_tall_forestry_cocobolo");
				BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_ebony = FurnitureFactory.CreateWoodTallStool("chair_wood_ironage_stool_tall_forestry_ebony");
				BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_giganteum = FurnitureFactory.CreateWoodTallStool("chair_wood_ironage_stool_tall_forestry_giganteum");
				BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_greenheart = FurnitureFactory.CreateWoodTallStool("chair_wood_ironage_stool_tall_forestry_greenheart");
				BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_ipe = FurnitureFactory.CreateWoodTallStool("chair_wood_ironage_stool_tall_forestry_ipe");
				BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_kapok = FurnitureFactory.CreateWoodTallStool("chair_wood_ironage_stool_tall_forestry_kapok");
				BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_larch = FurnitureFactory.CreateWoodTallStool("chair_wood_ironage_stool_tall_forestry_larch");
				BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_lime = FurnitureFactory.CreateWoodTallStool("chair_wood_ironage_stool_tall_forestry_lime");
				BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_mahoe = FurnitureFactory.CreateWoodTallStool("chair_wood_ironage_stool_tall_forestry_mahoe");
				BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_mahogany = FurnitureFactory.CreateWoodTallStool("chair_wood_ironage_stool_tall_forestry_mahogany");
				BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_maple = FurnitureFactory.CreateWoodTallStool("chair_wood_ironage_stool_tall_forestry_maple");
				BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_padauk = FurnitureFactory.CreateWoodTallStool("chair_wood_ironage_stool_tall_forestry_padauk");
				BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_palm = FurnitureFactory.CreateWoodTallStool("chair_wood_ironage_stool_tall_forestry_palm");
				BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_papaya = FurnitureFactory.CreateWoodTallStool("chair_wood_ironage_stool_tall_forestry_papaya");
				BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_pine = FurnitureFactory.CreateWoodTallStool("chair_wood_ironage_stool_tall_forestry_pine");
				BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_plum = FurnitureFactory.CreateWoodTallStool("chair_wood_ironage_stool_tall_forestry_plum");
				BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_poplar = FurnitureFactory.CreateWoodTallStool("chair_wood_ironage_stool_tall_forestry_poplar");
				BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_sequoia = FurnitureFactory.CreateWoodTallStool("chair_wood_ironage_stool_tall_forestry_sequoia");
				BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_teak = FurnitureFactory.CreateWoodTallStool("chair_wood_ironage_stool_tall_forestry_teak");
				BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_walnut = FurnitureFactory.CreateWoodTallStool("chair_wood_ironage_stool_tall_forestry_walnut");
				BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_wenge = FurnitureFactory.CreateWoodTallStool("chair_wood_ironage_stool_tall_forestry_wenge");
				BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_willow = FurnitureFactory.CreateWoodTallStool("chair_wood_ironage_stool_tall_forestry_willow");
				BlockObjectHolder.chair_wood_ironage_stool_tall_forestry_zebrawood = FurnitureFactory.CreateWoodTallStool("chair_wood_ironage_stool_tall_forestry_zebrawood");
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