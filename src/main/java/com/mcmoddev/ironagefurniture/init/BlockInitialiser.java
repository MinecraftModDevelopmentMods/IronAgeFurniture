package com.mcmoddev.ironagefurniture.init;

import com.mcmoddev.ironagefurniture.IronAgeFurnitureConfiguration;
import com.mcmoddev.ironagefurniture.Ironagefurniture;
import com.mcmoddev.ironagefurniture.Blocks.Chair;

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
		final Chair chair;
		
		chair = (Chair) registerBlock(new Chair(Material.WOOD, "chair_wood_ironageone", (float)5), "chair_wood_ironageone");
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
		return registerBlock(block, name, 64);
	}
}