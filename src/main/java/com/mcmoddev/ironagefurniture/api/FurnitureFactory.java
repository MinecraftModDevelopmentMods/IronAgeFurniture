package com.mcmoddev.ironagefurniture.api;

import com.mcmoddev.ironagefurniture.Ironagefurniture;
import com.mcmoddev.ironagefurniture.api.Blocks.BackBench;
import com.mcmoddev.ironagefurniture.api.Blocks.Bench;
import com.mcmoddev.ironagefurniture.api.Blocks.Chair;
import com.mcmoddev.ironagefurniture.api.Blocks.Stool;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

public class FurnitureFactory {	
	public static Block CreateWoodShieldChair(String name, float resistance, float hardness) {
		return registerBlock(new Chair(Material.WOOD, name, resistance, hardness), name);
	}
	
	public static Block CreateWoodShieldChair(String name) {
		return CreateWoodShieldChair(name, 10, 1);
	}
	
	public static Block CreateWoodShortStool(String name, float resistance, float hardness) {
		return registerBlock(new Stool(Material.WOOD, name, resistance, false, 0.25, hardness), name);
	}
	
	public static Block CreateWoodShortStool(String name) {
		return CreateWoodShortStool(name, 10, 1);
	}
	
	public static Block CreateWoodChair(String name, float resistance, float hardness) {
		return  registerBlock(new Chair(Material.WOOD, name, resistance, hardness), name);
	}
	
	public static Block CreateWoodChair(String name) {
		return CreateWoodChair(name, 10, 1);
	}
	
	public static Block CreateWoodTallStool(String name, float resistance, float hardness) {
		return registerBlock(new Stool(Material.WOOD, name, resistance, true, 0.6, hardness), name);
	}
	
	public static Block CreateWoodTallStool(String name) {
		return registerBlock(new Stool(Material.WOOD, name, 10, true, 0.6, 1), name);
	}
	
	public static Block CreateWoodBench(String name, float resistance, float hardness) {
		return registerBlock(new Bench(Material.WOOD, name, resistance, false, 0.25, hardness), name);
	}
	
	public static Block CreateWoodBackBench(String name, float resistance, float hardness) {
		return registerBlock(new BackBench(Material.WOOD, name, resistance, false, 0.25, hardness), name);
	}
	
	public static Block CreateWoodBench(String name) {
		return CreateWoodBench(name, 10, 1);
	}
	
	public static Block CreateWoodBackBench(String name) {
		return CreateWoodBackBench(name, 10, 1);
	}
    private static Block registerBlock(Block block, String name, int maxStackSize) {
    	block.setTranslationKey(Ironagefurniture.MODID + "." + name);
		block.setRegistryName(name);
		block.setCreativeTab(Ironagefurniture.ironagefurnitureTab);
		ItemBlock itemBlock = new ItemBlock(block);
		
		itemBlock.setMaxStackSize(maxStackSize);
		
		RegisterItem(itemBlock, name, maxStackSize);
		Ironagefurniture.BlockRegistry.put(name, block);
		
		return block;
    }
    
	private static Block registerBlock(Block block, String name) {
		return registerBlock(block, name, 16);
	}
	
	public static Item RegisterItem(Item item, String name, int maxStackSize) {
		String itemName = Ironagefurniture.MODID + "." + name;

		item.setTranslationKey(itemName);
		item.setRegistryName(name);
		item.setMaxStackSize(maxStackSize);
		
		Ironagefurniture.ItemRegistry.put(name, item);

		return item;
	}
}
