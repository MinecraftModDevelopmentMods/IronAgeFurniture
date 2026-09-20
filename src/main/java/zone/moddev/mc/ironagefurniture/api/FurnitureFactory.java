package zone.moddev.mc.ironagefurniture.api;

import zone.moddev.mc.ironagefurniture.Ironagefurniture;
import zone.moddev.mc.ironagefurniture.api.Blocks.BackBench;
import zone.moddev.mc.ironagefurniture.api.Blocks.Bench;
import zone.moddev.mc.ironagefurniture.api.Blocks.Chair;
import zone.moddev.mc.ironagefurniture.api.Blocks.LightHolderSconceFloor;
import zone.moddev.mc.ironagefurniture.api.Blocks.LightHolderSconceWall;
import zone.moddev.mc.ironagefurniture.api.Blocks.LightSourceGlowdust;
import zone.moddev.mc.ironagefurniture.api.Blocks.LightSourceLava;
import zone.moddev.mc.ironagefurniture.api.Blocks.LightSourceRed;
import zone.moddev.mc.ironagefurniture.api.Blocks.LightSourceSconceGlowFloor;
import zone.moddev.mc.ironagefurniture.api.Blocks.LightSourceSconceGlowWall;
import zone.moddev.mc.ironagefurniture.api.Blocks.LightSourceSconceLavaFloor;
import zone.moddev.mc.ironagefurniture.api.Blocks.LightSourceSconceLavaWall;
import zone.moddev.mc.ironagefurniture.api.Blocks.LightSourceSconceRedFloor;
import zone.moddev.mc.ironagefurniture.api.Blocks.LightSourceSconceRedTorchFloor;
import zone.moddev.mc.ironagefurniture.api.Blocks.LightSourceSconceRedTorchFloorUnlit;
import zone.moddev.mc.ironagefurniture.api.Blocks.LightSourceSconceRedTorchWall;
import zone.moddev.mc.ironagefurniture.api.Blocks.LightSourceSconceRedTorchWallUnlit;
import zone.moddev.mc.ironagefurniture.api.Blocks.LightSourceSconceRedWall;
import zone.moddev.mc.ironagefurniture.api.Blocks.LightSourceSconceTorchFloor;
import zone.moddev.mc.ironagefurniture.api.Blocks.LightSourceSconceTorchFloorUnlit;
import zone.moddev.mc.ironagefurniture.api.Blocks.LightSourceSconceTorchWall;
import zone.moddev.mc.ironagefurniture.api.Blocks.LightSourceSconceTorchWallUnlit;
import zone.moddev.mc.ironagefurniture.api.Blocks.ObsideanLump;
import zone.moddev.mc.ironagefurniture.api.Blocks.PaddedBackBench;
import zone.moddev.mc.ironagefurniture.api.Blocks.PaddedBench;
import zone.moddev.mc.ironagefurniture.api.Blocks.Stool;
import zone.moddev.mc.ironagefurniture.api.Enumerations.PaddedBenchColour;
import zone.moddev.mc.ironagefurniture.api.Items.ItemBlockPaddedBench;
import zone.moddev.mc.ironagefurniture.api.Items.ItemBlockThrowableLavaLamp;
import zone.moddev.mc.ironagefurniture.init.ItemInitialiser;

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
import net.minecraftforge.oredict.OreDictionary;

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
		for (PaddedBenchColour colour : PaddedBenchColour.values()) {
			GameRegistry.addRecipe(new ShapelessOreRecipe(
					new ItemStack(chairOut, 1, colour.getItemMetadata()),
					new ItemStack(chairIn, 1),
					new ItemStack(Blocks.CARPET, 1, colour.getCarpetMetadata())));
		}
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
		if (!OreDictionary.doesOreNameExist("nuggetIron")
				|| OreDictionary.getOres("nuggetIron").isEmpty()) {
			ironInput = "ingotIron";
			outputCount = 32;
		}
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(sconce, outputCount),
				"xxx", "x  ", "x  ", 'x', ironInput));
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

	public static Block CreateWoodPaddedBench(String name, float resistance, float hardness) {
		return registerPaddedBlock(
				new PaddedBench(Material.WOOD, name, resistance, false, 0.25, hardness), name);
	}

	public static Block CreateWoodPaddedBackBench(String name, float resistance, float hardness) {
		return registerPaddedBlock(
				new PaddedBackBench(Material.WOOD, name, resistance, false, 0.25, hardness), name);
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

	public static Block CreateWoodPaddedBench(String name) {
		return CreateWoodPaddedBench(name, 10, 1);
	}

	public static Block CreateWoodPaddedBackBench(String name) {
		return CreateWoodPaddedBackBench(name, 10, 1);
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

	public static Block CreateIronWallSconce(String name) {
		return registerBlockWithoutItem(new LightHolderSconceWall(Material.IRON, name, 10, 1), name);
	}

	public static Block CreateIronFloorSconce(String name) {
		return registerBlock(new LightHolderSconceFloor(Material.IRON, name, 10, 1), name, 64);
	}

	public static Block CreateIronFloorTorchSconce(String name) {
		return registerBlockWithoutItem(new LightSourceSconceTorchFloor(Material.IRON, name, 10, 1), name);
	}

	public static Block CreateIronFloorTorchSconceUnlit(String name) {
		return registerBlockWithoutItem(new LightSourceSconceTorchFloorUnlit(Material.IRON, name, 10, 1), name);
	}

	public static Block CreateIronWallTorchSconce(String name) {
		return registerBlockWithoutItem(new LightSourceSconceTorchWall(Material.IRON, name, 10, 1), name);
	}

	public static Block CreateIronWallTorchSconceUnlit(String name) {
		return registerBlockWithoutItem(new LightSourceSconceTorchWallUnlit(Material.IRON, name, 10, 1), name);
	}

	public static Block CreateIronFloorRedTorchSconce(String name) {
		return registerBlockWithoutItem(new LightSourceSconceRedTorchFloor(Material.IRON, name, 10, 1), name);
	}

	public static Block CreateIronFloorRedTorchSconceUnlit(String name) {
		return registerBlockWithoutItem(new LightSourceSconceRedTorchFloorUnlit(Material.IRON, name, 10, 1), name);
	}

	public static Block CreateIronWallRedTorchSconce(String name) {
		return registerBlockWithoutItem(new LightSourceSconceRedTorchWall(Material.IRON, name, 10, 1), name);
	}

	public static Block CreateIronWallRedTorchSconceUnlit(String name) {
		return registerBlockWithoutItem(new LightSourceSconceRedTorchWallUnlit(Material.IRON, name, 10, 1), name);
	}

	public static Block CreateGlowdustLamp(String name) {
		return registerBlock(new LightSourceGlowdust(Material.GLASS, name, 1.5F, 0.3F), name, 64);
	}

	public static Block CreateIronFloorGlowSconce(String name) {
		return registerBlockWithoutItem(new LightSourceSconceGlowFloor(Material.IRON, name, 10, 1), name);
	}

	public static Block CreateIronWallGlowSconce(String name) {
		return registerBlockWithoutItem(new LightSourceSconceGlowWall(Material.IRON, name, 10, 1), name);
	}

	public static Block CreateLavaLamp(String name) {
		return registerBlock(new LightSourceLava(Material.GLASS, name, 1.5F, 0.3F), name, 64);
	}

	public static Block CreateIronFloorLavaSconce(String name) {
		return registerBlockWithoutItem(new LightSourceSconceLavaFloor(Material.IRON, name, 10, 1), name);
	}

	public static Block CreateIronWallLavaSconce(String name) {
		return registerBlockWithoutItem(new LightSourceSconceLavaWall(Material.IRON, name, 10, 1), name);
	}

	public static Block CreateRedLamp(String name) {
		return createRedLamp(name, 0, true);
	}

	public static Block CreateRedLampVariant(String name, int lightLevel) {
		return createRedLamp(name, lightLevel, false);
	}

	private static Block createRedLamp(String name, int lightLevel, boolean registerItem) {
		return registerBlock(new LightSourceRed(Material.GLASS, name, 1.5F, 0.3F, lightLevel),
				name, 64, registerItem);
	}

	public static Block CreateIronFloorRedSconce(String name, int lightLevel) {
		return registerBlockWithoutItem(new LightSourceSconceRedFloor(Material.IRON, name, 10, 1, lightLevel), name);
	}

	public static Block CreateIronWallRedSconce(String name, int lightLevel) {
		return registerBlockWithoutItem(new LightSourceSconceRedWall(Material.IRON, name, 10, 1, lightLevel), name);
	}

	public static Block CreateObsidianChunk(String name) {
		return registerBlock(new ObsideanLump(Material.ROCK, name, 10, 1), name, 64);
	}

	private static Block registerBlock(Block block, String name, int maxStackSize, boolean registerItem) {
		GameRegistry.register(block.setRegistryName(Ironagefurniture.MODID, name));
		block.setUnlocalizedName(Ironagefurniture.MODID + "." + name);
		block.setCreativeTab(Ironagefurniture.ironagefurnitureTab);
		if (registerItem) {
			ItemBlock itemBlock = block instanceof LightSourceLava
					? new ItemBlockThrowableLavaLamp(block) : new ItemBlock(block);
			itemBlock.setMaxStackSize(maxStackSize);
			ItemInitialiser.RegisterItem(itemBlock, name);
		}
		Ironagefurniture.BlockRegistry.put(name, block);
		return block;
    }

	private static Block registerPaddedBlock(Block block, String name) {
		GameRegistry.register(block.setRegistryName(Ironagefurniture.MODID, name));
		block.setUnlocalizedName(Ironagefurniture.MODID + "." + name);
		block.setCreativeTab(Ironagefurniture.ironagefurnitureTab);
		ItemBlock itemBlock = new ItemBlockPaddedBench(block);
		itemBlock.setMaxStackSize(16);
		ItemInitialiser.RegisterItem(itemBlock, name);
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
