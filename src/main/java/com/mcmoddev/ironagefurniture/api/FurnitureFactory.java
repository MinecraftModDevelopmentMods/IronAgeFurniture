package com.mcmoddev.ironagefurniture.api;

import com.mcmoddev.ironagefurniture.Ironagefurniture;
import com.mcmoddev.ironagefurniture.api.Blocks.Bench;
import com.mcmoddev.ironagefurniture.api.Blocks.Chair;
import com.mcmoddev.ironagefurniture.api.Blocks.Stool;
import com.mcmoddev.ironagefurniture.init.ItemInitialiser;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCarpet;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class FurnitureFactory {

	public static void AddClassicChairRecipe(ItemStack planks, Block chair) {
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(chair, 1), "x  ", "xxx", "y y", 'x', planks, 'y', "stickWood"));
	}
	
	public static void AddShortStoolRecipe(ItemStack planks, Block stool) {
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(stool, 1), " x ", "yyy", 'x', planks, 'y', "stickWood"));
	}
	
	public static void AddBenchRecipe(ItemStack planks, Block bench) {
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(bench, 1), "xx", "yy", 'x', planks, 'y', "stickWood"));
	}
	
	public static void AddPaddedBenchRecipe(Block chairIn, Block chairOut) {
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(chairOut, 1), new ItemStack(chairIn,1), new ItemStack(Blocks.CARPET, 1, 0)));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(chairOut, 1), new ItemStack(chairIn,1), new ItemStack(Blocks.CARPET, 1, 1)));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(chairOut, 1), new ItemStack(chairIn,1), new ItemStack(Blocks.CARPET, 1, 2)));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(chairOut, 1), new ItemStack(chairIn,1), new ItemStack(Blocks.CARPET, 1, 3)));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(chairOut, 1), new ItemStack(chairIn,1), new ItemStack(Blocks.CARPET, 1, 4)));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(chairOut, 1), new ItemStack(chairIn,1), new ItemStack(Blocks.CARPET, 1, 5)));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(chairOut, 1), new ItemStack(chairIn,1), new ItemStack(Blocks.CARPET, 1, 6)));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(chairOut, 1), new ItemStack(chairIn,1), new ItemStack(Blocks.CARPET, 1, 7)));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(chairOut, 1), new ItemStack(chairIn,1), new ItemStack(Blocks.CARPET, 1, 8)));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(chairOut, 1), new ItemStack(chairIn,1), new ItemStack(Blocks.CARPET, 1, 9)));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(chairOut, 1), new ItemStack(chairIn,1), new ItemStack(Blocks.CARPET, 1, 10)));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(chairOut, 1), new ItemStack(chairIn,1), new ItemStack(Blocks.CARPET, 1, 11)));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(chairOut, 1), new ItemStack(chairIn,1), new ItemStack(Blocks.CARPET, 1, 12)));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(chairOut, 1), new ItemStack(chairIn,1), new ItemStack(Blocks.CARPET, 1, 13)));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(chairOut, 1), new ItemStack(chairIn,1), new ItemStack(Blocks.CARPET, 1, 14)));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(chairOut, 1), new ItemStack(chairIn,1), new ItemStack(Blocks.CARPET, 1, 15)));
	}
	
	public static void AddTallStoolRecipe(ItemStack planks, Block stool) {
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(stool, 1), " x ", "yyy","yyy", 'x', planks, 'y', "stickWood"));
	}
	
	public static void AddShieldChairRecipe(Block chairIn, Block chairOut) {
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(chairOut, 1), new ItemStack(chairIn,1), new ItemStack(Items.SHIELD,1)));
	}
	
	public static void AddChairConversionRecipe(Block chairIn, Block chairOut) {
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(chairOut, 1), new ItemStack(chairIn,1)));
	}
	
	public static Block CreateWoodShieldChair(String name, float resistance, float hardness) {
		return registerBlock(new Chair(Material.WOOD, name, resistance, hardness), name);
	}
	
	public static Block CreateWoodShieldChair(String name) {
		return CreateWoodShieldChair(name, 10, 1);
	}
	
	public static Block CreateWoodShortStool(String name, float resistance, float hardness) {
		return registerBlock(new Stool(Material.WOOD, name, resistance, false, 0.25, hardness), name);
	}
	
	public static Block CreateWoodBench(String name, float resistance, float hardness) {
		return registerBlock(new Bench(Material.WOOD, name, resistance, false, 0.25, hardness), name);
	}
	
	public static Block CreateWoodShortStool(String name) {
		return CreateWoodShortStool(name, 10, 1);
	}
	
	public static Block CreateWoodBench(String name) {
		return CreateWoodBench(name, 10, 1);
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
