package com.mcmoddev.ironagefurniture.init;

import com.mcmoddev.ironagefurniture.IronAgeFurnitureConfiguration;
import com.mcmoddev.ironagefurniture.Ironagefurniture;
import com.mcmoddev.ironagefurniture.Blocks.Chair;
import com.mcmoddev.ironagefurniture.Blocks.Stool;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * This class initialises all blocks in ironagefurniture.
 *
 * @author SkyBlade1978
 *
 */
public class BlockInitialiser {
	public static Block chair_wood_ironage_classic_oak;
	public static Block chair_wood_ironage_classic_acacia;
	public static Block chair_wood_ironage_classic_big_oak;
	public static Block chair_wood_ironage_classic_birch;
	public static Block chair_wood_ironage_classic_jungle;
	public static Block chair_wood_ironage_classic_spruce;
	
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
			chair_wood_ironage_classic_oak = registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_oak", (float)10, 1), "chair_wood_ironage_classic_oak");
			chair_wood_ironage_classic_acacia = registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_acacia", (float)10, 1), "chair_wood_ironage_classic_acacia");
			chair_wood_ironage_classic_big_oak = registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_big_oak", (float)10, 1), "chair_wood_ironage_classic_big_oak");
			chair_wood_ironage_classic_birch = registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_birch", (float)10, 1), "chair_wood_ironage_classic_birch");
			chair_wood_ironage_classic_jungle = registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_jungle", (float)10, 1), "chair_wood_ironage_classic_jungle");
			chair_wood_ironage_classic_spruce = registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_spruce", (float)10, 1), "chair_wood_ironage_classic_spruce");
		}
		
		if (IronAgeFurnitureConfiguration.GENERATE_SHIELD_CHAIRS) {
			registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_oak", (float)10, 1), "chair_wood_ironage_shield_oak");
			registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_acacia", (float)10, 1), "chair_wood_ironage_shield_acacia");
			registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_big_oak", (float)10, 1), "chair_wood_ironage_shield_big_oak");
			registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_birch", (float)10, 1), "chair_wood_ironage_shield_birch");
			registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_jungle", (float)10, 1), "chair_wood_ironage_shield_jungle");
			registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_spruce", (float)10, 1), "chair_wood_ironage_shield_spruce");		
		}
		
		if (IronAgeFurnitureConfiguration.GENERATE_SHORT_STOOLS) {
			registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_oak", (float)10, false, 0.25, 1), "chair_wood_ironage_stool_short_oak");
			registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_acacia", (float)10, false, 0.25, 1), "chair_wood_ironage_stool_short_acacia");
			registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_big_oak", (float)10, false, 0.25, 1), "chair_wood_ironage_stool_short_big_oak");
			registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_birch", (float)10, false, 0.25, 1), "chair_wood_ironage_stool_short_birch");
			registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_jungle", (float)10, false, 0.25, 1), "chair_wood_ironage_stool_short_jungle");
			registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_spruce", (float)10, false, 0.25, 1), "chair_wood_ironage_stool_short_spruce");
		}
		
		if (IronAgeFurnitureConfiguration.GENERATE_TALL_STOOLS) {
			registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_acacia", (float)10, true, 0.6, 1), "chair_wood_ironage_stool_tall_acacia");
			registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_big_oak", (float)10, true, 0.6, 1), "chair_wood_ironage_stool_tall_big_oak");
			registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_birch", (float)10, true, 0.6, 1), "chair_wood_ironage_stool_tall_birch");
			registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_jungle", (float)10, true, 0.6, 1), "chair_wood_ironage_stool_tall_jungle");	
			registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_oak", (float)10, true, 0.6, 1), "chair_wood_ironage_stool_tall_oak");
			registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_spruce", (float)10, true, 0.6, 1), "chair_wood_ironage_stool_tall_spruce");
		}
		
		if (IronAgeFurnitureConfiguration.INTEGRATION_BIOMESOPLENTY && Loader.isModLoaded("BiomesOPlenty")) {
			if (IronAgeFurnitureConfiguration.GENERATE_CLASSIC_CHAIRS) {
				registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_biomesoplenty_cherry", (float)10, 1), "chair_wood_ironage_classic_biomesoplenty_cherry");
				registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_biomesoplenty_ebony", (float)10, 1), "chair_wood_ironage_classic_biomesoplenty_ebony");
				registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_biomesoplenty_ethereal", (float)10, 1), "chair_wood_ironage_classic_biomesoplenty_ethereal");
				registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_biomesoplenty_eucalyptus", (float)10, 1), "chair_wood_ironage_classic_biomesoplenty_eucalyptus");
				registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_biomesoplenty_fir", (float)10, 1), "chair_wood_ironage_classic_biomesoplenty_fir");
				registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_biomesoplenty_hellbark", (float)10, 1), "chair_wood_ironage_classic_biomesoplenty_hellbark");
				registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_biomesoplenty_jacaranda", (float)10, 1), "chair_wood_ironage_classic_biomesoplenty_jacaranda");
				registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_biomesoplenty_magic", (float)10, 1), "chair_wood_ironage_classic_biomesoplenty_magic");
				registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_biomesoplenty_mahogany", (float)10, 1), "chair_wood_ironage_classic_biomesoplenty_mahogany");
				registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_biomesoplenty_mangrove", (float)10, 1), "chair_wood_ironage_classic_biomesoplenty_mangrove");	
				registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_biomesoplenty_palm", (float)10, 1), "chair_wood_ironage_classic_biomesoplenty_palm");
				registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_biomesoplenty_pine", (float)10, 1), "chair_wood_ironage_classic_biomesoplenty_pine");
				registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_biomesoplenty_redwood", (float)10, 1), "chair_wood_ironage_classic_biomesoplenty_redwood");
				registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_biomesoplenty_sacred_oak", (float)10, 1), "chair_wood_ironage_classic_biomesoplenty_sacred_oak");	 
				registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_biomesoplenty_umbran", (float)10, 1), "chair_wood_ironage_classic_biomesoplenty_umbran");
				registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_biomesoplenty_willow", (float)10, 1), "chair_wood_ironage_classic_biomesoplenty_willow");
			}
			
			if (IronAgeFurnitureConfiguration.GENERATE_SHIELD_CHAIRS) {
				registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_biomesoplenty_cherry", (float)10, 1), "chair_wood_ironage_shield_biomesoplenty_cherry");
				registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_biomesoplenty_ebony", (float)10, 1), "chair_wood_ironage_shield_biomesoplenty_ebony");
				registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_biomesoplenty_ethereal", (float)10, 1), "chair_wood_ironage_shield_biomesoplenty_ethereal");
				registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_biomesoplenty_eucalyptus", (float)10, 1), "chair_wood_ironage_shield_biomesoplenty_eucalyptus");
				registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_biomesoplenty_fir", (float)10, 1), "chair_wood_ironage_shield_biomesoplenty_fir");
				registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_biomesoplenty_hellbark", (float)10, 1), "chair_wood_ironage_shield_biomesoplenty_hellbark");
				registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_biomesoplenty_jacaranda", (float)10, 1), "chair_wood_ironage_shield_biomesoplenty_jacaranda");
				registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_biomesoplenty_magic", (float)10, 1), "chair_wood_ironage_shield_biomesoplenty_magic");
				registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_biomesoplenty_mahogany", (float)10, 1), "chair_wood_ironage_shield_biomesoplenty_mahogany");
				registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_biomesoplenty_mangrove", (float)10, 1), "chair_wood_ironage_shield_biomesoplenty_mangrove");
				registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_biomesoplenty_palm", (float)10, 1), "chair_wood_ironage_shield_biomesoplenty_palm");
				registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_biomesoplenty_pine", (float)10, 1), "chair_wood_ironage_shield_biomesoplenty_pine");
				registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_biomesoplenty_redwood", (float)10, 1), "chair_wood_ironage_shield_biomesoplenty_redwood");
				registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_biomesoplenty_sacred_oak", (float)10, 1), "chair_wood_ironage_shield_biomesoplenty_sacred_oak");
				registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_biomesoplenty_umbran", (float)10, 1), "chair_wood_ironage_shield_biomesoplenty_umbran");
				registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_biomesoplenty_willow", (float)10, 1), "chair_wood_ironage_shield_biomesoplenty_willow");
			}
			
			if (IronAgeFurnitureConfiguration.GENERATE_SHORT_STOOLS) {
				registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_biomesoplenty_cherry", (float)10, false, 0.25, 1), "chair_wood_ironage_stool_short_biomesoplenty_cherry");
				registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_biomesoplenty_ebony", (float)10, false, 0.25, 1), "chair_wood_ironage_stool_short_biomesoplenty_ebony");
				registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_biomesoplenty_ethereal", (float)10, false, 0.25, 1), "chair_wood_ironage_stool_short_biomesoplenty_ethereal");
				registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_biomesoplenty_eucalyptus", (float)10, false, 0.25, 1), "chair_wood_ironage_stool_short_biomesoplenty_eucalyptus");
				registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_biomesoplenty_fir", (float)10, false, 0.25, 1), "chair_wood_ironage_stool_short_biomesoplenty_fir");
				registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_biomesoplenty_hellbark", (float)10, false, 0.25, 1), "chair_wood_ironage_stool_short_biomesoplenty_hellbark");
				registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_biomesoplenty_jacaranda", (float)10, false, 0.25, 1), "chair_wood_ironage_stool_short_biomesoplenty_jacaranda");	
				registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_biomesoplenty_magic", (float)10, false, 0.25, 1), "chair_wood_ironage_stool_short_biomesoplenty_magic");
				registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_biomesoplenty_mahogany", (float)10, false, 0.25, 1), "chair_wood_ironage_stool_short_biomesoplenty_mahogany");
				registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_biomesoplenty_mangrove", (float)10, false, 0.25, 1), "chair_wood_ironage_stool_short_biomesoplenty_mangrove");
				registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_biomesoplenty_palm", (float)10, false, 0.25, 1), "chair_wood_ironage_stool_short_biomesoplenty_palm");
				registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_biomesoplenty_pine", (float)10, false, 0.25, 1), "chair_wood_ironage_stool_short_biomesoplenty_pine");
				registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_biomesoplenty_redwood", (float)10, false, 0.25, 1), "chair_wood_ironage_stool_short_biomesoplenty_redwood");
				registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_biomesoplenty_sacred_oak", (float)10, false, 0.25, 1), "chair_wood_ironage_stool_short_biomesoplenty_sacred_oak");
				registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_biomesoplenty_umbran", (float)10, false, 0.25, 1), "chair_wood_ironage_stool_short_biomesoplenty_umbran");
				registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_biomesoplenty_willow", (float)10, false, 0.25, 1), "chair_wood_ironage_stool_short_biomesoplenty_willow");
			}
			
			if (IronAgeFurnitureConfiguration.GENERATE_TALL_STOOLS) {
				registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_biomesoplenty_cherry", (float)10, true, 0.6, 1), "chair_wood_ironage_stool_tall_biomesoplenty_cherry");
				registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_biomesoplenty_ebony", (float)10, true, 0.6, 1), "chair_wood_ironage_stool_tall_biomesoplenty_ebony");
				registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_biomesoplenty_ethereal", (float)10, true, 0.6, 1), "chair_wood_ironage_stool_tall_biomesoplenty_ethereal");
				registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_biomesoplenty_eucalyptus", (float)10, true, 0.6, 1), "chair_wood_ironage_stool_tall_biomesoplenty_eucalyptus");
				registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_biomesoplenty_fir", (float)10, true, 0.6, 1), "chair_wood_ironage_stool_tall_biomesoplenty_fir");
				registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_biomesoplenty_hellbark", (float)10, true, 0.6, 1), "chair_wood_ironage_stool_tall_biomesoplenty_hellbark");
				registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_biomesoplenty_jacaranda", (float)10, true, 0.6, 1), "chair_wood_ironage_stool_tall_biomesoplenty_jacaranda");
				registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_biomesoplenty_magic", (float)10, true, 0.6, 1), "chair_wood_ironage_stool_tall_biomesoplenty_magic");
				registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_biomesoplenty_mahogany", (float)10, true, 0.6, 1), "chair_wood_ironage_stool_tall_biomesoplenty_mahogany");
				registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_biomesoplenty_mangrove", (float)10, true, 0.6, 1), "chair_wood_ironage_stool_tall_biomesoplenty_mangrove");
				registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_biomesoplenty_palm", (float)10, true, 0.6, 1), "chair_wood_ironage_stool_tall_biomesoplenty_palm");
				registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_biomesoplenty_pine", (float)10, true, 0.6, 1), "chair_wood_ironage_stool_tall_biomesoplenty_pine");
				registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_biomesoplenty_redwood", (float)10, true, 0.6, 1), "chair_wood_ironage_stool_tall_biomesoplenty_redwood");
				registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_biomesoplenty_sacred_oak", (float)10, true, 0.6, 1), "chair_wood_ironage_stool_tall_biomesoplenty_sacred_oak");
				registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_biomesoplenty_umbran", (float)10, true, 0.6, 1), "chair_wood_ironage_stool_tall_biomesoplenty_umbran");
				registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_biomesoplenty_willow", (float)10, true, 0.6, 1), "chair_wood_ironage_stool_tall_biomesoplenty_willow");
			}
		}
		
		if (IronAgeFurnitureConfiguration.INTEGRATION_NATURA && Loader.isModLoaded("natura")) {
			if (IronAgeFurnitureConfiguration.GENERATE_CLASSIC_CHAIRS) {
				registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_natura_amaranth", (float)10, 1), "chair_wood_ironage_classic_natura_amaranth");
				registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_natura_eucalyptus", (float)10, 1), "chair_wood_ironage_classic_natura_eucalyptus");
				registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_natura_hopseed", (float)10, 1), "chair_wood_ironage_classic_natura_hopseed");
				registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_natura_maple", (float)10, 1), "chair_wood_ironage_classic_natura_maple");
				registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_natura_redwood", (float)10, 1), "chair_wood_ironage_classic_natura_redwood");
				registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_natura_sakura", (float)10, 1), "chair_wood_ironage_classic_natura_sakura");
				registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_natura_silverbell", (float)10, 1), "chair_wood_ironage_classic_natura_silverbell");
				registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_natura_tiger", (float)10, 1), "chair_wood_ironage_classic_natura_tiger");
				registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_natura_willow", (float)10, 1), "chair_wood_ironage_classic_natura_willow");
				registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_natura_bloodwood", (float)10, 1), "chair_wood_ironage_classic_natura_bloodwood");
				registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_natura_darkwood", (float)10, 1), "chair_wood_ironage_classic_natura_darkwood");
				registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_natura_fusewood", (float)10, 1), "chair_wood_ironage_classic_natura_fusewood");
				registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_natura_ghostwood", (float)10, 1), "chair_wood_ironage_classic_natura_ghostwood");
			}
			
			if (IronAgeFurnitureConfiguration.GENERATE_SHIELD_CHAIRS) {
				registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_natura_amaranth", (float)10, 1), "chair_wood_ironage_shield_natura_amaranth");
				registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_natura_eucalyptus", (float)10, 1), "chair_wood_ironage_shield_natura_eucalyptus");
				registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_natura_hopseed", (float)10, 1), "chair_wood_ironage_shield_natura_hopseed");
				registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_natura_maple", (float)10, 1), "chair_wood_ironage_shield_natura_maple");
				registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_natura_redwood", (float)10, 1), "chair_wood_ironage_shield_natura_redwood");
				registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_natura_sakura", (float)10, 1), "chair_wood_ironage_shield_natura_sakura");
				registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_natura_silverbell", (float)10, 1), "chair_wood_ironage_shield_natura_silverbell");
				registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_natura_tiger", (float)10, 1), "chair_wood_ironage_shield_natura_tiger");
				registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_natura_willow", (float)10, 1), "chair_wood_ironage_shield_natura_willow");
				registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_natura_bloodwood", (float)10, 1), "chair_wood_ironage_shield_natura_bloodwood");
				registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_natura_darkwood", (float)10, 1), "chair_wood_ironage_shield_natura_darkwood");
				registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_natura_fusewood", (float)10, 1), "chair_wood_ironage_shield_natura_fusewood");
				registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_natura_ghostwood", (float)10, 1), "chair_wood_ironage_shield_natura_ghostwood");
			}
			
			if (IronAgeFurnitureConfiguration.GENERATE_SHORT_STOOLS) {
				registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_natura_amaranth", (float)10, false, 0.25, 1), "chair_wood_ironage_stool_short_natura_amaranth");
				registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_natura_eucalyptus", (float)10, false, 0.25, 1), "chair_wood_ironage_stool_short_natura_eucalyptus");
				registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_natura_hopseed", (float)10, false, 0.25, 1), "chair_wood_ironage_stool_short_natura_hopseed");
				registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_natura_maple", (float)10, false, 0.25, 1), "chair_wood_ironage_stool_short_natura_maple");
				registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_natura_redwood", (float)10, false, 0.25, 1), "chair_wood_ironage_stool_short_natura_redwood");
				registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_natura_sakura", (float)10, false, 0.25, 1), "chair_wood_ironage_stool_short_natura_sakura");
				registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_natura_silverbell", (float)10, false, 0.25, 1), "chair_wood_ironage_stool_short_natura_silverbell");
				registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_natura_tiger", (float)10, false, 0.25, 1), "chair_wood_ironage_stool_short_natura_tiger");
				registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_natura_willow", (float)10, false, 0.25, 1), "chair_wood_ironage_stool_short_natura_willow");
				registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_natura_bloodwood", (float)10, false, 0.25, 1), "chair_wood_ironage_stool_short_natura_bloodwood");
				registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_natura_darkwood", (float)10, false, 0.25, 1), "chair_wood_ironage_stool_short_natura_darkwood");
				registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_natura_fusewood", (float)10, false, 0.25, 1), "chair_wood_ironage_stool_short_natura_fusewood");
				registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_natura_ghostwood", (float)10, false, 0.25, 1), "chair_wood_ironage_stool_short_natura_ghostwood");
			}
			
			if (IronAgeFurnitureConfiguration.GENERATE_TALL_STOOLS) {
				registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_natura_amaranth", (float)10, true, 0.6, 1), "chair_wood_ironage_stool_tall_natura_amaranth");
				registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_natura_eucalyptus", (float)10, true, 0.6, 1), "chair_wood_ironage_stool_tall_natura_eucalyptus");
				registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_natura_hopseed", (float)10, true, 0.6, 1), "chair_wood_ironage_stool_tall_natura_hopseed");
				registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_natura_maple", (float)10, true, 0.6, 1), "chair_wood_ironage_stool_tall_natura_maple");
				registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_natura_redwood", (float)10, true, 0.6, 1), "chair_wood_ironage_stool_tall_natura_redwood");
				registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_natura_sakura", (float)10, true, 0.6, 1), "chair_wood_ironage_stool_tall_natura_sakura");
				registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_natura_silverbell", (float)10, true, 0.6, 1), "chair_wood_ironage_stool_tall_natura_silverbell");
				registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_natura_tiger", (float)10, true, 0.6, 1), "chair_wood_ironage_stool_tall_natura_tiger");
				registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_natura_willow", (float)10, true, 0.6, 1), "chair_wood_ironage_stool_tall_natura_willow");
				registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_natura_bloodwood", (float)10, true, 0.6, 1), "chair_wood_ironage_stool_tall_natura_bloodwood");
				registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_natura_darkwood", (float)10, true, 0.6, 1), "chair_wood_ironage_stool_tall_natura_darkwood");
				registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_natura_fusewood", (float)10, true, 0.6, 1), "chair_wood_ironage_stool_tall_natura_fusewood");
				registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_natura_ghostwood", (float)10, true, 0.6, 1), "chair_wood_ironage_stool_tall_natura_ghostwood");
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