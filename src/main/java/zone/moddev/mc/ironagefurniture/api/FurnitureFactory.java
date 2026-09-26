package zone.moddev.mc.ironagefurniture.api;

import zone.moddev.mc.ironagefurniture.BlockObjectHolder;
import zone.moddev.mc.ironagefurniture.IronAgeFurnitureConfiguration;
import zone.moddev.mc.ironagefurniture.Ironagefurniture;
import zone.moddev.mc.ironagefurniture.api.Blocks.BackBench;
import zone.moddev.mc.ironagefurniture.api.Blocks.Bench;
import zone.moddev.mc.ironagefurniture.api.Blocks.Chair;
import zone.moddev.mc.ironagefurniture.api.Blocks.LightSourceCandleFloor;
import zone.moddev.mc.ironagefurniture.api.Blocks.LightSourceCandleFloorUnlit;
import zone.moddev.mc.ironagefurniture.api.Blocks.LightSourceCandleWall;
import zone.moddev.mc.ironagefurniture.api.Blocks.LightSourceCandleWallUnlit;
import zone.moddev.mc.ironagefurniture.api.Blocks.LightSourceSconceCandleFloor;
import zone.moddev.mc.ironagefurniture.api.Blocks.LightSourceSconceCandleFloorUnlit;
import zone.moddev.mc.ironagefurniture.api.Blocks.LightSourceSconceCandleWall;
import zone.moddev.mc.ironagefurniture.api.Blocks.LightSourceSconceCandleWallUnlit;
import zone.moddev.mc.ironagefurniture.api.Blocks.LightSourceSconceRockSaltFloor;
import zone.moddev.mc.ironagefurniture.api.Blocks.LightSourceSconceRockSaltWall;
import zone.moddev.mc.ironagefurniture.api.Blocks.LightSourceSconceTorchFloorTwin;
import zone.moddev.mc.ironagefurniture.api.Blocks.LightSourceSconceTorchFloorTwinUnlit;
import zone.moddev.mc.ironagefurniture.api.Blocks.LightSourceSconceTorchWallTwin;
import zone.moddev.mc.ironagefurniture.api.Blocks.LightSourceSconceTorchWallTwinUnlit;
import zone.moddev.mc.ironagefurniture.api.Blocks.MultiBlockBed;
import zone.moddev.mc.ironagefurniture.api.Blocks.MultiBlockChair;
import zone.moddev.mc.ironagefurniture.api.Blocks.MultiBlockWoodBed;
import zone.moddev.mc.ironagefurniture.api.Blocks.ShieldChair;
import zone.moddev.mc.ironagefurniture.api.Blocks.ThroneChair;
import zone.moddev.mc.ironagefurniture.api.Blocks.WingbackChair;
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
import zone.moddev.mc.ironagefurniture.api.Enumerations.UpholsteryColour;
import zone.moddev.mc.ironagefurniture.api.Items.ItemBlockUpholsteredFurniture;
import zone.moddev.mc.ironagefurniture.api.Items.ItemBlockMetalVariant;
import zone.moddev.mc.ironagefurniture.api.Items.ItemBlockPaddedBench;
import zone.moddev.mc.ironagefurniture.api.Items.ItemBlockThrowableLavaLamp;
import zone.moddev.mc.ironagefurniture.init.ItemInitialiser;
import zone.moddev.mc.ironagefurniture.api.recipes.BedRecolourRecipe;
import zone.moddev.mc.ironagefurniture.api.recipes.MatchingUpholsteryRecipe;
import zone.moddev.mc.ironagefurniture.api.recipes.ShieldChairRecipe;

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
		if (chair.getRegistryName() != null && chair.getRegistryName().getResourcePath().startsWith("chair_wood_ironage_classic_")) {
			String suffix = chair.getRegistryName().getResourcePath().substring("chair_wood_ironage_classic_".length());
			Block wingback = BlockObjectHolder.chair_wood_ironage_wingback.get(suffix);
			if (wingback != null) {
				AddWingbackChairRecipe(planks, chair, wingback);
				Block throne = BlockObjectHolder.chair_wood_ironage_throne.get(suffix);
				if (throne != null) AddThroneChairRecipe(planks, wingback, throne);
			}
		}
	}

	public static void AddWingbackChairRecipe(ItemStack planks, Block chairIn, Block chairOut) {
		for (UpholsteryColour colour : UpholsteryColour.values()) {
			GameRegistry.addRecipe(new ShapedOreRecipe(UpholsteryColourHelper.createStack(chairOut, 1, colour), "z", "x", "y",
				'z', new ItemStack(Blocks.CARPET, 1, colour.getCarpetMetadata()), 'x', planks, 'y', chairIn));
		}
	}

	public static void AddThroneChairRecipe(ItemStack planks, Block chairIn, Block chairOut) {
		AddWingbackChairRecipe(planks, chairIn, chairOut);
	}

	public static void AddSingleCanopyBedRecipe(ItemStack planks, Block bed) {
		for (UpholsteryColour colour : UpholsteryColour.values()) {
			GameRegistry.addRecipe(new ShapelessOreRecipe(UpholsteryColourHelper.createStack(bed, 1, colour),
				Items.BED, planks, new ItemStack(Blocks.CARPET, 1, colour.getCarpetMetadata())));
		}
	}

	public static void AddDoubleCanopyBedRecipe(Block singleBed, Block doubleBed) {
		GameRegistry.addRecipe(new MatchingUpholsteryRecipe(singleBed, doubleBed));
	}

	public static void AddSingleWoodBedRecipe(ItemStack planks, Block bed) {
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(bed, 1), Items.BED, planks));
	}

	public static void AddDoubleWoodBedRecipe(Block singleBed, Block doubleBed) {
		GameRegistry.addRecipe(new MatchingUpholsteryRecipe(singleBed, doubleBed));
	}

	public static void AddBedRecolourRecipe(Block bed) {
		GameRegistry.addRecipe(new BedRecolourRecipe(bed));
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
					PaddedBenchColourHelper.createStack(chairOut, 1, colour),
					new ItemStack(chairIn, 1),
					new ItemStack(Blocks.CARPET, 1, colour.getCarpetMetadata())));
		}
	}

	public static void AddTallStoolRecipe(ItemStack planks, Block stool) {
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(stool, 1), " x ", "yyy","yyy", 'x', planks, 'y', "stickWood"));
	}

	public static void AddShieldChairRecipe(Block chairIn, Block chairOut) {
		GameRegistry.addRecipe(new ShieldChairRecipe(chairIn, chairOut));
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
		return registerBlock(new ShieldChair(Material.WOOD, name, resistance, hardness), name);
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

	public static Block CreateWoodWingbackChair(String name) {
		return registerBlock(new WingbackChair(Material.WOOD, name, 10, 2), name);
	}

	public static Block CreateWoodThroneChair(String name) {
		return registerBlock(new ThroneChair(Material.WOOD, name, 10, 3), name);
	}

	public static Block CreateSingleCanopyBed(String suffix) {
		String name = "bed_canopy_foot_lower_" + suffix;
		MultiBlockBed bed = new MultiBlockBed(Material.WOOD, name, 10, 3, MultiBlockBed.SINGLE_SIDE);
		Block registeredBed = registerBlock(bed, name, 1);
		bed.setSingleBlock(registeredBed);
		return registeredBed;
	}

	public static Block[] CreateDoubleCanopyBed(String suffix) {
		String leftName = "bed_canopy_foot_left_lower_" + suffix;
		String rightName = "bed_canopy_foot_right_lower_" + suffix;
		MultiBlockBed leftBed = new MultiBlockBed(Material.WOOD, leftName, 10, 6, MultiBlockBed.LEFT_SIDE);
		MultiBlockBed rightBed = new MultiBlockBed(Material.WOOD, rightName, 10, 6, MultiBlockBed.RIGHT_SIDE);
		Block leftBlock = registerBlock(leftBed, leftName, 1);
		Block rightBlock = registerBlockWithoutItem(rightBed, rightName);
		leftBed.setDoubleBlocks(leftBlock, rightBlock);
		rightBed.setDoubleBlocks(leftBlock, rightBlock);
		return new Block[] { leftBlock, rightBlock };
	}

	public static Block CreateSingleWoodBed(String suffix) {
		String name = "bed_wood_foot_" + suffix;
		return registerBlock(new MultiBlockWoodBed(Material.WOOD, name, 10, 3, false), name, 1);
	}

	public static Block CreateDoubleWoodBed(String suffix) {
		String name = "bed_wood_foot_left_" + suffix;
		return registerBlock(new MultiBlockWoodBed(Material.WOOD, name, 10, 6, true), name, 1);
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

	public static Block CreateIronFloorTorchSconceTwin(String name) {
		return registerBlockWithoutItem(new LightSourceSconceTorchFloorTwin(Material.IRON, name, 10, 1), name);
	}

	public static Block CreateIronFloorTorchSconceTwinUnlit(String name) {
		return registerBlockWithoutItem(new LightSourceSconceTorchFloorTwinUnlit(Material.IRON, name, 10, 1), name);
	}

	public static Block CreateIronWallTorchSconce(String name) {
		return registerBlockWithoutItem(new LightSourceSconceTorchWall(Material.IRON, name, 10, 1), name);
	}

	public static Block CreateIronWallTorchSconceUnlit(String name) {
		return registerBlockWithoutItem(new LightSourceSconceTorchWallUnlit(Material.IRON, name, 10, 1), name);
	}

	public static Block CreateIronWallTorchSconceTwin(String name) {
		return registerBlockWithoutItem(new LightSourceSconceTorchWallTwin(Material.IRON, name, 10, 1), name);
	}

	public static Block CreateIronWallTorchSconceTwinUnlit(String name) {
		return registerBlockWithoutItem(new LightSourceSconceTorchWallTwinUnlit(Material.IRON, name, 10, 1), name);
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

	public static Block CreateIronFloorRockSaltSconce(String name) {
		return registerBlockWithoutItem(new LightSourceSconceRockSaltFloor(Material.IRON, name, 10, 1), name);
	}

	public static Block CreateIronWallRockSaltSconce(String name) {
		return registerBlockWithoutItem(new LightSourceSconceRockSaltWall(Material.IRON, name, 10, 1), name);
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

	public static Block CreateCandleFloor(String name) {
		return registerBlock(new LightSourceCandleFloor(Material.CIRCUITS, name, 1, 0.1F), name, 64);
	}

	public static Block CreateCandleWall(String name) {
		return registerBlockWithoutItem(new LightSourceCandleWall(Material.CIRCUITS, name, 1, 0.1F), name);
	}

	public static Block CreateCandleFloorUnlit(String name) {
		return registerBlockWithoutItem(new LightSourceCandleFloorUnlit(Material.CIRCUITS, name, 1, 0.1F), name);
	}

	public static Block CreateCandleWallUnlit(String name) {
		return registerBlockWithoutItem(new LightSourceCandleWallUnlit(Material.CIRCUITS, name, 1, 0.1F), name);
	}

	public static Block CreateIronFloorCandleSconce(String name, int candleCount) {
		return registerBlockWithoutItem(new LightSourceSconceCandleFloor(Material.IRON, name, 10, 1, candleCount), name);
	}

	public static Block CreateIronFloorCandleSconceUnlit(String name, int candleCount) {
		return registerBlockWithoutItem(new LightSourceSconceCandleFloorUnlit(Material.IRON, name, 10, 1, candleCount), name);
	}

	public static Block CreateIronWallCandleSconce(String name, int candleCount) {
		return registerBlockWithoutItem(new LightSourceSconceCandleWall(Material.IRON, name, 10, 1, candleCount), name);
	}

	public static Block CreateIronWallCandleSconceUnlit(String name, int candleCount) {
		return registerBlockWithoutItem(new LightSourceSconceCandleWallUnlit(Material.IRON, name, 10, 1, candleCount), name);
	}

	private static Block registerBlock(Block block, String name, int maxStackSize, boolean registerItem) {
		GameRegistry.register(block.setRegistryName(Ironagefurniture.MODID, name));
		block.setUnlocalizedName(Ironagefurniture.MODID + "." + name);
		block.setCreativeTab(Ironagefurniture.ironagefurnitureTab);
		if (registerItem) {
			ItemBlock itemBlock = block instanceof MultiBlockBed || block instanceof MultiBlockWoodBed
					|| block instanceof MultiBlockChair ? new ItemBlockUpholsteredFurniture(block)
					: MetalVariantHelper.isMetalVariantBlock(block) ? new ItemBlockMetalVariant(block)
					: block instanceof LightSourceLava ? new ItemBlockThrowableLavaLamp(block) : new ItemBlock(block);
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
