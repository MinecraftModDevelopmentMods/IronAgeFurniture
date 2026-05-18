package com.mcmoddev.ironagefurniture.api;

import com.mcmoddev.ironagefurniture.Ironagefurniture;
import com.mcmoddev.ironagefurniture.api.Blocks.BackBench;
import com.mcmoddev.ironagefurniture.api.Blocks.Bench;
import com.mcmoddev.ironagefurniture.api.Blocks.Chair;
import com.mcmoddev.ironagefurniture.api.Blocks.LightHolderSconceFloor;
import com.mcmoddev.ironagefurniture.api.Blocks.LightHolderSconceWall;
import com.mcmoddev.ironagefurniture.api.Blocks.LightSourceCandleFloor;
import com.mcmoddev.ironagefurniture.api.Blocks.LightSourceCandleFloorUnlit;
import com.mcmoddev.ironagefurniture.api.Blocks.LightSourceCandleWall;
import com.mcmoddev.ironagefurniture.api.Blocks.LightSourceCandleWallUnlit;
import com.mcmoddev.ironagefurniture.api.Blocks.LightSourceSconceCandleFloor;
import com.mcmoddev.ironagefurniture.api.Blocks.LightSourceSconceCandleFloorUnlit;
import com.mcmoddev.ironagefurniture.api.Blocks.LightSourceSconceCandleWall;
import com.mcmoddev.ironagefurniture.api.Blocks.LightSourceSconceCandleWallUnlit;
import com.mcmoddev.ironagefurniture.api.Blocks.LightSourceSconceRedTorchFloor;
import com.mcmoddev.ironagefurniture.api.Blocks.LightSourceSconceRedTorchFloorUnlit;
import com.mcmoddev.ironagefurniture.api.Blocks.LightSourceSconceRedTorchWall;
import com.mcmoddev.ironagefurniture.api.Blocks.LightSourceSconceRedTorchWallUnlit;
import com.mcmoddev.ironagefurniture.api.Blocks.LightSourceSconceTorchFloor;
import com.mcmoddev.ironagefurniture.api.Blocks.LightSourceSconceTorchFloorUnlit;
import com.mcmoddev.ironagefurniture.api.Blocks.LightSourceSconceTorchWall;
import com.mcmoddev.ironagefurniture.api.Blocks.LightSourceSconceTorchWallUnlit;
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
import net.minecraftforge.oredict.OreDictionary;
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
	
	public static void AddBackBenchRecipe(ItemStack planks, Block bench) {
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(bench, 1), "x ", "xx", "yy", 'x', planks, 'y', "stickWood"));
	}
	
	public static void AddLogBenchRecipe(ItemStack log, Block bench) {
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(bench, 6), "xxx", 'x', log));
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

	public static void AddIronSconceRecipe(Block sconce) {
		Object ironInput = "nuggetIron";
		int outputCount = 5;

		if (!OreDictionary.doesOreNameExist("nuggetIron") || OreDictionary.getOres("nuggetIron").isEmpty()) {
			ironInput = "ingotIron";
			outputCount = 32;
		}

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(sconce, outputCount), "xxx", "x  ", "x  ", 'x', ironInput));
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
	
	public static Block CreateWoodBackBench(String name, float resistance, float hardness) {
		return registerBlock(new BackBench(Material.WOOD, name, resistance, false, 0.25, hardness), name);
	}
	
	public static Block CreateWoodShortStool(String name) {
		return CreateWoodShortStool(name, 10, 1);
	}
	
	public static Block CreateWoodBench(String name) {
		return CreateWoodBench(name, 10, 1);
	}
	
	public static Block CreateWoodBackBench(String name) {
		return CreateWoodBackBench(name, 10, 1);
	}
	
	public static Block CreateWoodChair(String name, float resistance, float hardness) {
		return  registerBlock(new Chair(Material.WOOD, name, resistance, hardness), name);
	}
	
	public static Block CreateIronWallSconce(String name) {
		return CreateIronWallSconce(name, 10, 1);
	}
	
	public static Block CreateIronFloorSconce(String name) {
		return CreateIronFloorSconce(name, 10, 1);
	}
	
	public static Block CreateIronWallSconce(String name, float resistance, float hardness) {
		return registerBlockWithoutItem(new LightHolderSconceWall(Material.WOOD, name, resistance, hardness), name);
	}
	
	public static Block CreateIronFloorSconce(String name, float resistance, float hardness) {
		return registerBlock(new LightHolderSconceFloor(Material.WOOD, name, resistance, hardness), name, 64);
	}
	
	public static Block CreateIronFloorTorchSconce(String name) {
		return CreateIronFloorTorchSconce(name, 10, 1);
	}
	
	public static Block CreateIronFloorTorchSconce(String name, float resistance, float hardness) {
		return registerBlockWithoutItem(new LightSourceSconceTorchFloor(Material.WOOD, name, resistance, hardness), name);
	}

	public static Block CreateIronFloorTorchSconceUnlit(String name) {
		return CreateIronFloorTorchSconceUnlit(name, 10, 1);
	}

	public static Block CreateIronFloorTorchSconceUnlit(String name, float resistance, float hardness) {
		return registerBlockWithoutItem(new LightSourceSconceTorchFloorUnlit(Material.WOOD, name, resistance, hardness), name);
	}

	public static Block CreateIronWallTorchSconce(String name) {
		return CreateIronWallTorchSconce(name, 10, 1);
	}

	public static Block CreateIronWallTorchSconce(String name, float resistance, float hardness) {
		return registerBlockWithoutItem(new LightSourceSconceTorchWall(Material.WOOD, name, resistance, hardness), name);
	}

	public static Block CreateIronWallTorchSconceUnlit(String name) {
		return CreateIronWallTorchSconceUnlit(name, 10, 1);
	}

	public static Block CreateIronWallTorchSconceUnlit(String name, float resistance, float hardness) {
		return registerBlockWithoutItem(new LightSourceSconceTorchWallUnlit(Material.WOOD, name, resistance, hardness), name);
	}

	public static Block CreateIronFloorRedTorchSconce(String name) {
		return CreateIronFloorRedTorchSconce(name, 10, 1);
	}

	public static Block CreateIronFloorRedTorchSconce(String name, float resistance, float hardness) {
		return registerBlockWithoutItem(new LightSourceSconceRedTorchFloor(Material.WOOD, name, resistance, hardness), name);
	}

	public static Block CreateIronFloorRedTorchSconceUnlit(String name) {
		return CreateIronFloorRedTorchSconceUnlit(name, 10, 1);
	}

	public static Block CreateIronFloorRedTorchSconceUnlit(String name, float resistance, float hardness) {
		return registerBlockWithoutItem(new LightSourceSconceRedTorchFloorUnlit(Material.WOOD, name, resistance, hardness), name);
	}

	public static Block CreateIronWallRedTorchSconce(String name) {
		return CreateIronWallRedTorchSconce(name, 10, 1);
	}

	public static Block CreateIronWallRedTorchSconce(String name, float resistance, float hardness) {
		return registerBlockWithoutItem(new LightSourceSconceRedTorchWall(Material.WOOD, name, resistance, hardness), name);
	}

	public static Block CreateIronWallRedTorchSconceUnlit(String name) {
		return CreateIronWallRedTorchSconceUnlit(name, 10, 1);
	}

	public static Block CreateIronWallRedTorchSconceUnlit(String name, float resistance, float hardness) {
		return registerBlockWithoutItem(new LightSourceSconceRedTorchWallUnlit(Material.WOOD, name, resistance, hardness), name);
	}

	public static Block CreateCandleFloor(String name) {
		return CreateCandleFloor(name, 1, 0.1F);
	}

	public static Block CreateCandleFloor(String name, float resistance, float hardness) {
		return registerBlock(new LightSourceCandleFloor(Material.CIRCUITS, name, resistance, hardness), name, 64);
	}

	public static Block CreateCandleWall(String name) {
		return CreateCandleWall(name, 1, 0.1F);
	}

	public static Block CreateCandleWall(String name, float resistance, float hardness) {
		return registerBlockWithoutItem(new LightSourceCandleWall(Material.CIRCUITS, name, resistance, hardness), name);
	}

	public static Block CreateCandleFloorUnlit(String name) {
		return CreateCandleFloorUnlit(name, 1, 0.1F);
	}

	public static Block CreateCandleFloorUnlit(String name, float resistance, float hardness) {
		return registerBlockWithoutItem(new LightSourceCandleFloorUnlit(Material.CIRCUITS, name, resistance, hardness), name);
	}

	public static Block CreateCandleWallUnlit(String name) {
		return CreateCandleWallUnlit(name, 1, 0.1F);
	}

	public static Block CreateCandleWallUnlit(String name, float resistance, float hardness) {
		return registerBlockWithoutItem(new LightSourceCandleWallUnlit(Material.CIRCUITS, name, resistance, hardness), name);
	}

	public static Block CreateIronFloorCandleSconce(String name, int candleCount) {
		return CreateIronFloorCandleSconce(name, 10, 1, candleCount);
	}

	public static Block CreateIronFloorCandleSconce(String name, float resistance, float hardness, int candleCount) {
		return registerBlockWithoutItem(new LightSourceSconceCandleFloor(Material.WOOD, name, resistance, hardness, candleCount), name);
	}

	public static Block CreateIronFloorCandleSconceUnlit(String name, int candleCount) {
		return CreateIronFloorCandleSconceUnlit(name, 10, 1, candleCount);
	}

	public static Block CreateIronFloorCandleSconceUnlit(String name, float resistance, float hardness, int candleCount) {
		return registerBlockWithoutItem(new LightSourceSconceCandleFloorUnlit(Material.WOOD, name, resistance, hardness, candleCount), name);
	}

	public static Block CreateIronWallCandleSconce(String name, int candleCount) {
		return CreateIronWallCandleSconce(name, 10, 1, candleCount);
	}

	public static Block CreateIronWallCandleSconce(String name, float resistance, float hardness, int candleCount) {
		return registerBlockWithoutItem(new LightSourceSconceCandleWall(Material.WOOD, name, resistance, hardness, candleCount), name);
	}

	public static Block CreateIronWallCandleSconceUnlit(String name, int candleCount) {
		return CreateIronWallCandleSconceUnlit(name, 10, 1, candleCount);
	}

	public static Block CreateIronWallCandleSconceUnlit(String name, float resistance, float hardness, int candleCount) {
		return registerBlockWithoutItem(new LightSourceSconceCandleWallUnlit(Material.WOOD, name, resistance, hardness, candleCount), name);
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
    private static Block registerBlock(Block block, String name, int maxStackSize, boolean registerItem) {
    	GameRegistry.register(block.setRegistryName(Ironagefurniture.MODID, name));
    	block.setUnlocalizedName(Ironagefurniture.MODID + "." + name);
		
		if (registerItem) {
			ItemBlock itemBlock = new ItemBlock(block);
			itemBlock.setMaxStackSize(maxStackSize);
			ItemInitialiser.RegisterItem(itemBlock, name);
		}
		Ironagefurniture.BlockRegistry.put(name, block);
		
		return block;
    }
    
    private static Block registerBlockWithoutItem(Block block, String name) {
		return registerBlock(block, name, 16, false);
	}

    private static Block registerBlock(Block block, String name, int maxStackSize) {
		return registerBlock(block, name, maxStackSize, true);
	}

	private static Block registerBlock(Block block, String name) {
		return registerBlock(block, name, 16);
	}
}
