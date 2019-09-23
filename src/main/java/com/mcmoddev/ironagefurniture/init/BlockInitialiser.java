package com.mcmoddev.ironagefurniture.init;

import com.mcmoddev.ironagefurniture.IronAgeFurnitureConfiguration;
import com.mcmoddev.ironagefurniture.Ironagefurniture;
import com.mcmoddev.ironagefurniture.Blocks.Chair;
import com.mcmoddev.ironagefurniture.Blocks.Stool;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
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
		if (IronAgeFurnitureConfiguration.GENERATE_CHAIRS) {
			generateChairs();
		}
	}
	
	private static void generateChairs() {
		Chair chair;
		chair = (Chair) registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_acacia", (float)5), "chair_wood_ironage_classic_acacia");
		chair = (Chair) registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_big_oak", (float)5), "chair_wood_ironage_classic_big_oak");
		chair = (Chair) registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_birch", (float)5), "chair_wood_ironage_classic_birch");
		chair = (Chair) registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_cherry", (float)5), "chair_wood_ironage_classic_cherry");
		chair = (Chair) registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_ebony", (float)5), "chair_wood_ironage_classic_ebony");
		chair = (Chair) registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_ethereal", (float)5), "chair_wood_ironage_classic_ethereal");
		chair = (Chair) registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_eucalyptus", (float)5), "chair_wood_ironage_classic_eucalyptus");
		chair = (Chair) registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_fir", (float)5), "chair_wood_ironage_classic_fir");
		chair = (Chair) registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_hellbark", (float)5), "chair_wood_ironage_classic_hellbark");
		chair = (Chair) registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_jacaranda", (float)5), "chair_wood_ironage_classic_jacaranda");
		chair = (Chair) registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_jungle", (float)5), "chair_wood_ironage_classic_jungle");
		chair = (Chair) registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_magic", (float)5), "chair_wood_ironage_classic_magic");
		chair = (Chair) registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_mahogany", (float)5), "chair_wood_ironage_classic_mahogany");
		chair = (Chair) registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_mangrove", (float)5), "chair_wood_ironage_classic_mangrove");
		chair = (Chair) registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_oak", (float)5), "chair_wood_ironage_classic_oak");
		chair = (Chair) registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_palm", (float)5), "chair_wood_ironage_classic_palm");
		chair = (Chair) registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_pine", (float)5), "chair_wood_ironage_classic_pine");
		chair = (Chair) registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_redwood", (float)5), "chair_wood_ironage_classic_redwood");
		chair = (Chair) registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_sacred_oak", (float)5), "chair_wood_ironage_classic_sacred_oak");
		chair = (Chair) registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_spruce", (float)5), "chair_wood_ironage_classic_spruce");
		chair = (Chair) registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_umbran", (float)5), "chair_wood_ironage_classic_umbran");
		chair = (Chair) registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_classic_willow", (float)5), "chair_wood_ironage_classic_willow");
		chair = (Chair) registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_acacia", (float)5), "chair_wood_ironage_shield_acacia");
		chair = (Chair) registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_big_oak", (float)5), "chair_wood_ironage_shield_big_oak");
		chair = (Chair) registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_birch", (float)5), "chair_wood_ironage_shield_birch");
		chair = (Chair) registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_cherry", (float)5), "chair_wood_ironage_shield_cherry");
		chair = (Chair) registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_ebony", (float)5), "chair_wood_ironage_shield_ebony");
		chair = (Chair) registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_ethereal", (float)5), "chair_wood_ironage_shield_ethereal");
		chair = (Chair) registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_eucalyptus", (float)5), "chair_wood_ironage_shield_eucalyptus");
		chair = (Chair) registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_fir", (float)5), "chair_wood_ironage_shield_fir");
		chair = (Chair) registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_hellbark", (float)5), "chair_wood_ironage_shield_hellbark");
		chair = (Chair) registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_jacaranda", (float)5), "chair_wood_ironage_shield_jacaranda");
		chair = (Chair) registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_jungle", (float)5), "chair_wood_ironage_shield_jungle");
		chair = (Chair) registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_magic", (float)5), "chair_wood_ironage_shield_magic");
		chair = (Chair) registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_mahogany", (float)5), "chair_wood_ironage_shield_mahogany");
		chair = (Chair) registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_mangrove", (float)5), "chair_wood_ironage_shield_mangrove");
		chair = (Chair) registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_oak", (float)5), "chair_wood_ironage_shield_oak");
		chair = (Chair) registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_palm", (float)5), "chair_wood_ironage_shield_palm");
		chair = (Chair) registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_pine", (float)5), "chair_wood_ironage_shield_pine");
		chair = (Chair) registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_redwood", (float)5), "chair_wood_ironage_shield_redwood");
		chair = (Chair) registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_sacred_oak", (float)5), "chair_wood_ironage_shield_sacred_oak");
		chair = (Chair) registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_spruce", (float)5), "chair_wood_ironage_shield_spruce");
		chair = (Chair) registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_umbran", (float)5), "chair_wood_ironage_shield_umbran");
		chair = (Chair) registerBlock(new Chair(Material.WOOD, "chair_wood_ironage_shield_willow", (float)5), "chair_wood_ironage_shield_willow");
		chair = (Chair) registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_acacia", (float)5, false, 0.25), "chair_wood_ironage_stool_short_acacia");
		chair = (Chair) registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_big_oak", (float)5, false, 0.25), "chair_wood_ironage_stool_short_big_oak");
		chair = (Chair) registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_birch", (float)5, false, 0.25), "chair_wood_ironage_stool_short_birch");
		chair = (Chair) registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_cherry", (float)5, false, 0.25), "chair_wood_ironage_stool_short_cherry");
		chair = (Chair) registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_ebony", (float)5, false, 0.25), "chair_wood_ironage_stool_short_ebony");
		chair = (Chair) registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_ethereal", (float)5, false, 0.25), "chair_wood_ironage_stool_short_ethereal");
		chair = (Chair) registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_eucalyptus", (float)5, false, 0.25), "chair_wood_ironage_stool_short_eucalyptus");
		chair = (Chair) registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_fir", (float)5, false, 0.25), "chair_wood_ironage_stool_short_fir");
		chair = (Chair) registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_hellbark", (float)5, false, 0.25), "chair_wood_ironage_stool_short_hellbark");
		chair = (Chair) registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_jacaranda", (float)5, false, 0.25), "chair_wood_ironage_stool_short_jacaranda");
		chair = (Chair) registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_jungle", (float)5, false, 0.25), "chair_wood_ironage_stool_short_jungle");
		chair = (Chair) registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_magic", (float)5, false, 0.25), "chair_wood_ironage_stool_short_magic");
		chair = (Chair) registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_mahogany", (float)5, false, 0.25), "chair_wood_ironage_stool_short_mahogany");
		chair = (Chair) registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_mangrove", (float)5, false, 0.25), "chair_wood_ironage_stool_short_mangrove");
		chair = (Chair) registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_oak", (float)5, false, 0.25), "chair_wood_ironage_stool_short_oak");
		chair = (Chair) registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_palm", (float)5, false, 0.25), "chair_wood_ironage_stool_short_palm");
		chair = (Chair) registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_pine", (float)5, false, 0.25), "chair_wood_ironage_stool_short_pine");
		chair = (Chair) registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_redwood", (float)5, false, 0.25), "chair_wood_ironage_stool_short_redwood");
		chair = (Chair) registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_sacred_oak", (float)5, false, 0.25), "chair_wood_ironage_stool_short_sacred_oak");
		chair = (Chair) registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_spruce", (float)5, false, 0.25), "chair_wood_ironage_stool_short_spruce");
		chair = (Chair) registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_umbran", (float)5, false, 0.25), "chair_wood_ironage_stool_short_umbran");
		chair = (Chair) registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_short_willow", (float)5, false, 0.25), "chair_wood_ironage_stool_short_willow");
		chair = (Chair) registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_acacia", (float)5, true, 0.6), "chair_wood_ironage_stool_tall_acacia");
		chair = (Chair) registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_big_oak", (float)5, true, 0.6), "chair_wood_ironage_stool_tall_big_oak");
		chair = (Chair) registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_birch", (float)5, true, 0.6), "chair_wood_ironage_stool_tall_birch");
		chair = (Chair) registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_cherry", (float)5, true, 0.6), "chair_wood_ironage_stool_tall_cherry");
		chair = (Chair) registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_ebony", (float)5, true, 0.6), "chair_wood_ironage_stool_tall_ebony");
		chair = (Chair) registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_ethereal", (float)5, true, 0.6), "chair_wood_ironage_stool_tall_ethereal");
		chair = (Chair) registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_eucalyptus", (float)5, true, 0.6), "chair_wood_ironage_stool_tall_eucalyptus");
		chair = (Chair) registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_fir", (float)5, true, 0.6), "chair_wood_ironage_stool_tall_fir");
		chair = (Chair) registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_hellbark", (float)5, true, 0.6), "chair_wood_ironage_stool_tall_hellbark");
		chair = (Chair) registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_jacaranda", (float)5, true, 0.6), "chair_wood_ironage_stool_tall_jacaranda");
		chair = (Chair) registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_jungle", (float)5, true, 0.6), "chair_wood_ironage_stool_tall_jungle");
		chair = (Chair) registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_magic", (float)5, true, 0.6), "chair_wood_ironage_stool_tall_magic");
		chair = (Chair) registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_mahogany", (float)5, true, 0.6), "chair_wood_ironage_stool_tall_mahogany");
		chair = (Chair) registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_mangrove", (float)5, true, 0.6), "chair_wood_ironage_stool_tall_mangrove");
		chair = (Chair) registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_oak", (float)5, true, 0.6), "chair_wood_ironage_stool_tall_oak");
		chair = (Chair) registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_palm", (float)5, true, 0.6), "chair_wood_ironage_stool_tall_palm");
		chair = (Chair) registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_pine", (float)5, true, 0.6), "chair_wood_ironage_stool_tall_pine");
		chair = (Chair) registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_redwood", (float)5, true, 0.6), "chair_wood_ironage_stool_tall_redwood");
		chair = (Chair) registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_sacred_oak", (float)5, true, 0.6), "chair_wood_ironage_stool_tall_sacred_oak");
		chair = (Chair) registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_spruce", (float)5, true, 0.6), "chair_wood_ironage_stool_tall_spruce");
		chair = (Chair) registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_umbran", (float)5, true, 0.6), "chair_wood_ironage_stool_tall_umbran");
		chair = (Chair) registerBlock(new Stool(Material.WOOD, "chair_wood_ironage_stool_tall_willow", (float)5, true, 0.6), "chair_wood_ironage_stool_tall_willow");
		
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