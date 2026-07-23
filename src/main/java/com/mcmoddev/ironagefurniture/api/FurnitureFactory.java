package com.mcmoddev.ironagefurniture.api;

import com.mcmoddev.ironagefurniture.BlockObjectHolder;
import com.mcmoddev.ironagefurniture.IronAgeFurnitureConfiguration;
import com.mcmoddev.ironagefurniture.Ironagefurniture;
import com.mcmoddev.ironagefurniture.api.Blocks.BackBench;
import com.mcmoddev.ironagefurniture.api.Blocks.Barrel;
import com.mcmoddev.ironagefurniture.api.Blocks.Bench;
import com.mcmoddev.ironagefurniture.api.Blocks.BottleRack;
import com.mcmoddev.ironagefurniture.api.Blocks.Cabinet;
import com.mcmoddev.ironagefurniture.api.Blocks.ChainTop;
import com.mcmoddev.ironagefurniture.api.Blocks.Chair;
import com.mcmoddev.ironagefurniture.api.Blocks.DiningChair;
import com.mcmoddev.ironagefurniture.api.Blocks.DiningTable;
import com.mcmoddev.ironagefurniture.api.Blocks.Foudre;
import com.mcmoddev.ironagefurniture.api.Blocks.GlassVaseBlock;
import com.mcmoddev.ironagefurniture.api.Blocks.GoldBars;
import com.mcmoddev.ironagefurniture.api.Blocks.GrandChandelierHub;
import com.mcmoddev.ironagefurniture.api.Blocks.GrandChandelierSconce;
import com.mcmoddev.ironagefurniture.api.Blocks.HalfCabinet;
import com.mcmoddev.ironagefurniture.api.Blocks.HangingInnSign;
import com.mcmoddev.ironagefurniture.api.Blocks.LightHolderSconceFloor;
import com.mcmoddev.ironagefurniture.api.Blocks.LightHolderSconceHanging;
import com.mcmoddev.ironagefurniture.api.Blocks.LightHolderSconceWall;
import com.mcmoddev.ironagefurniture.api.Blocks.LightSourceChandelierCandle;
import com.mcmoddev.ironagefurniture.api.Blocks.LightSourceChandelierCandleUnlit;
import com.mcmoddev.ironagefurniture.api.Blocks.LightSourceChandelierGlowstone;
import com.mcmoddev.ironagefurniture.api.Blocks.LightSourceChandelierLava;
import com.mcmoddev.ironagefurniture.api.Blocks.LightSourceChandelierRedstone;
import com.mcmoddev.ironagefurniture.api.Blocks.LightSourceChandelierTorch;
import com.mcmoddev.ironagefurniture.api.Blocks.LightSourceChandelierTorchUnlit;
import com.mcmoddev.ironagefurniture.api.Blocks.LightSourceCandleFloor;
import com.mcmoddev.ironagefurniture.api.Blocks.LightSourceCandleFloorUnlit;
import com.mcmoddev.ironagefurniture.api.Blocks.LightSourceCandleWall;
import com.mcmoddev.ironagefurniture.api.Blocks.LightSourceCandleWallUnlit;
import com.mcmoddev.ironagefurniture.api.Blocks.LightSourceGlowdust;
import com.mcmoddev.ironagefurniture.api.Blocks.LightSourceLava;
import com.mcmoddev.ironagefurniture.api.Blocks.LightSourceRed;
import com.mcmoddev.ironagefurniture.api.Blocks.LightSourceSconceCandleFloor;
import com.mcmoddev.ironagefurniture.api.Blocks.LightSourceSconceCandleFloorUnlit;
import com.mcmoddev.ironagefurniture.api.Blocks.LightSourceSconceCandleWall;
import com.mcmoddev.ironagefurniture.api.Blocks.LightSourceSconceCandleWallUnlit;
import com.mcmoddev.ironagefurniture.api.Blocks.LightSourceSconceGlowFloor;
import com.mcmoddev.ironagefurniture.api.Blocks.LightSourceSconceGlowWall;
import com.mcmoddev.ironagefurniture.api.Blocks.LightSourceSconceLavaFloor;
import com.mcmoddev.ironagefurniture.api.Blocks.LightSourceSconceLavaWall;
import com.mcmoddev.ironagefurniture.api.Blocks.LightSourceSconceRedFloor;
import com.mcmoddev.ironagefurniture.api.Blocks.LightSourceSconceRedWall;
import com.mcmoddev.ironagefurniture.api.Blocks.LightSourceSconceRedTorchFloor;
import com.mcmoddev.ironagefurniture.api.Blocks.LightSourceSconceRedTorchFloorUnlit;
import com.mcmoddev.ironagefurniture.api.Blocks.LightSourceSconceRedTorchWall;
import com.mcmoddev.ironagefurniture.api.Blocks.LightSourceSconceRedTorchWallUnlit;
import com.mcmoddev.ironagefurniture.api.Blocks.LightSourceSconceRockSaltFloor;
import com.mcmoddev.ironagefurniture.api.Blocks.LightSourceSconceRockSaltWall;
import com.mcmoddev.ironagefurniture.api.Blocks.LightSourceSconceTorchFloor;
import com.mcmoddev.ironagefurniture.api.Blocks.LightSourceSconceTorchFloorTwin;
import com.mcmoddev.ironagefurniture.api.Blocks.LightSourceSconceTorchFloorTwinUnlit;
import com.mcmoddev.ironagefurniture.api.Blocks.LightSourceSconceTorchFloorUnlit;
import com.mcmoddev.ironagefurniture.api.Blocks.LightSourceSconceTorchWall;
import com.mcmoddev.ironagefurniture.api.Blocks.LightSourceSconceTorchWallTwin;
import com.mcmoddev.ironagefurniture.api.Blocks.LightSourceSconceTorchWallTwinUnlit;
import com.mcmoddev.ironagefurniture.api.Blocks.LightSourceSconceTorchWallUnlit;
import com.mcmoddev.ironagefurniture.api.Blocks.LowTable;
import com.mcmoddev.ironagefurniture.api.Blocks.MultiBlockBed;
import com.mcmoddev.ironagefurniture.api.Blocks.MultiBlockChair;
import com.mcmoddev.ironagefurniture.api.Blocks.MultiBlockWoodBed;
import com.mcmoddev.ironagefurniture.api.Blocks.ObsideanLump;
import com.mcmoddev.ironagefurniture.api.Blocks.OrnamentBlock;
import com.mcmoddev.ironagefurniture.api.Blocks.PotStill;
import com.mcmoddev.ironagefurniture.api.Blocks.SideBarrel;
import com.mcmoddev.ironagefurniture.api.Blocks.Stool;
import com.mcmoddev.ironagefurniture.api.Blocks.SurfaceDisplayBlocker;
import com.mcmoddev.ironagefurniture.api.Blocks.ThroneChair;
import com.mcmoddev.ironagefurniture.api.Blocks.WallShelf;
import com.mcmoddev.ironagefurniture.api.Blocks.WingbackChair;
import com.mcmoddev.ironagefurniture.api.Items.ItemBlockBarrel;
import com.mcmoddev.ironagefurniture.api.Items.ItemBlockBottleRack;
import com.mcmoddev.ironagefurniture.api.Items.ItemBlockGlassVase;
import com.mcmoddev.ironagefurniture.api.Items.ItemBlockGrandChandelier;
import com.mcmoddev.ironagefurniture.api.Items.ItemBlockMetalVariant;
import com.mcmoddev.ironagefurniture.api.Items.ItemBlockOrnament;
import com.mcmoddev.ironagefurniture.api.Items.ItemBlockPotStill;
import com.mcmoddev.ironagefurniture.api.Items.ItemBlockThrowableLavaLamp;
import com.mcmoddev.ironagefurniture.api.Items.ItemBlockWallShelf;
import com.mcmoddev.ironagefurniture.api.recipes.FoudreRecipe;
import com.mcmoddev.ironagefurniture.api.recipes.PotStillRecipe;
import com.mcmoddev.ironagefurniture.api.recipes.SideBarrelRecipe;
import com.mcmoddev.ironagefurniture.api.recipes.UprightBarrelRecipe;
import com.mcmoddev.ironagefurniture.init.ItemInitialiser;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class FurnitureFactory {
	private static final int WOOD_FURNITURE_FIRE_SPREAD_SPEED = 5;
	private static final int WOOD_FURNITURE_FLAMMABILITY = 20;

	public static void AddClassicChairRecipe(ItemStack planks, Block chair) {
		if (chair == null) {
			return;
		}

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(chair, 1), "x  ", "xxx", "y y", 'x', planks, 'y', "stickWood"));
		AddDerivedTallChairRecipes(planks, chair);
	}

	public static void AddDiningChairRecipe(ItemStack planks, Block chair) {
		if (chair == null) {
			return;
		}

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(chair, 1), "x x", "xxx", "y y", 'x', planks, 'y', "stickWood"));
	}

	public static void AddWingbackChairRecipe(ItemStack planks, Block chairIn, Block chairOut) {
		for (int i = 0; i < 16; i++) {
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(chairOut, 1), "z", "x", "y",
				'z', new ItemStack(Blocks.CARPET, 1, i), 'x', planks, 'y', chairIn));
		}
	}

	public static void AddThroneChairRecipe(ItemStack planks, Block chairIn, Block chairOut) {
		for (int i = 0; i < 16; i++) {
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(chairOut, 1), "z", "x", "y",
				'z', new ItemStack(Blocks.CARPET, 1, i), 'x', planks, 'y', chairIn));
		}
	}

	public static void AddSingleCanopyBedRecipe(ItemStack planks, Block bed) {
		for (int i = 0; i < 16; i++) {
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(bed, 1),
				Items.BED, planks, new ItemStack(Blocks.CARPET, 1, i)));
		}
	}

	public static void AddDoubleCanopyBedRecipe(Block singleBed, Block doubleBed) {
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(doubleBed, 1),
			new ItemStack(singleBed, 1), new ItemStack(singleBed, 1)));
	}

	public static void AddSingleWoodBedRecipe(ItemStack planks, Block bed) {
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(bed, 1), Items.BED, planks));
	}

	public static void AddDoubleWoodBedRecipe(Block singleBed, Block doubleBed) {
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(doubleBed, 1),
			new ItemStack(singleBed, 1), new ItemStack(singleBed, 1)));
	}

	public static void AddDiningTableRecipe(ItemStack slab, Block table) {
		if (table == null) {
			return;
		}

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(table, 1), "xxx", "y y",
			'x', slab, 'y', "stickWood"));
	}

	public static void AddLowTableRecipe(ItemStack slab, Block table) {
		if (table == null) {
			return;
		}

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(table, 1), "xxx", " x ", "y y",
			'x', slab, 'y', "stickWood"));
	}

	public static void AddWallShelfRecipe(ItemStack slab, Block shelf) {
		if (shelf == null) {
			return;
		}

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(shelf, 3), "xxx", " y ",
			'x', slab, 'y', "stickWood"));
	}

	public static void AddBottleRackRecipe(ItemStack slab, Block rack) {
		if (rack == null) {
			return;
		}

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(rack, 2), "x x", " y ", "x x",
			'x', slab, 'y', "stickWood"));
	}

	public static void AddCabinetRecipe(ItemStack planks, Block cabinet) {
		if (cabinet == null) {
			return;
		}

		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(cabinet, 1), Blocks.CHEST, "nuggetIron", planks));
	}

	public static void AddHalfCabinetRecipe(Block cabinet, Block halfCabinet) {
		if (cabinet == null || halfCabinet == null) {
			return;
		}

		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(halfCabinet, 2), new ItemStack(cabinet, 1)));
	}

	public static void AddBarrelRecipe(ItemStack planks, Block barrel) {
		if (barrel == null) {
			return;
		}

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(barrel, 1), "xxx", "y y", "xxx",
			'x', planks, 'y', "nuggetIron"));
	}

	public static void AddSideBarrelRecipe(Block barrel, Block sideBarrel) {
		if (barrel == null || sideBarrel == null) {
			return;
		}

		GameRegistry.addRecipe(new SideBarrelRecipe(barrel, sideBarrel));
	}

	public static void AddUprightBarrelRecipe(Block sideBarrel, Block barrel) {
		if (sideBarrel == null || barrel == null) {
			return;
		}

		GameRegistry.addRecipe(new UprightBarrelRecipe(sideBarrel, barrel));
	}

	public static void AddFoudreRecipe(Block barrel, Block foudre) {
		if (barrel == null || foudre == null) {
			return;
		}

		GameRegistry.addRecipe(new FoudreRecipe(barrel, foudre));
	}

	public static void AddPotStillRecipe(Block foudre, Block potStill) {
		if (foudre == null || potStill == null) {
			return;
		}

		GameRegistry.addRecipe(new PotStillRecipe(foudre, potStill));
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

	private static void AddDerivedTallChairRecipes(ItemStack planks, Block classicChair) {
		if (!IronAgeFurnitureConfiguration.GENERATE_CLASSIC_CHAIRS || !IronAgeFurnitureConfiguration.GENERATE_WINGBACK_CHAIRS
			|| classicChair == null || classicChair.getRegistryName() == null) {
			return;
		}

		String name = classicChair.getRegistryName().getResourcePath();
		String prefix = "chair_wood_ironage_classic_";

		if (!name.startsWith(prefix)) {
			return;
		}

		String suffix = name.substring(prefix.length());
		Block wingback = BlockObjectHolder.chair_wood_ironage_wingback.get(suffix);

		if (wingback == null) {
			return;
		}

		AddWingbackChairRecipe(planks, classicChair, wingback);

		if (IronAgeFurnitureConfiguration.GENERATE_THRONES) {
			Block throne = BlockObjectHolder.chair_wood_ironage_throne.get(suffix);

			if (throne != null) {
				AddThroneChairRecipe(planks, wingback, throne);
			}
		}
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
		return registerBlock(new Chair(Material.WOOD, name, resistance, hardness), name);
	}

	public static Block CreateWoodDiningChair(String suffix, float resistance, float hardness) {
		String name = "chair_wood_ironage_dining_" + suffix;
		return registerBlock(new DiningChair(Material.WOOD, name, resistance, hardness), name);
	}

	public static Block CreateWoodDiningChair(String suffix) {
		return CreateWoodDiningChair(suffix, 10, 1);
	}

	public static Block CreateWoodWingbackChair(String name) {
		return CreateWoodWingbackChair(name, 10, 2);
	}

	public static Block CreateWoodWingbackChair(String name, float resistance, float hardness) {
		return registerBlock(new WingbackChair(Material.WOOD, name, resistance, hardness), name);
	}

	public static Block CreateWoodThroneChair(String name) {
		return CreateWoodThroneChair(name, 10, 3);
	}

	public static Block CreateWoodThroneChair(String name, float resistance, float hardness) {
		return registerBlock(new ThroneChair(Material.WOOD, name, resistance, hardness), name);
	}

	public static Block CreateSingleCanopyBed(String suffix, float resistance, float hardness) {
		String name = "bed_canopy_foot_lower_" + suffix;
		MultiBlockBed bed = new MultiBlockBed(Material.WOOD, name, resistance, hardness, MultiBlockBed.SINGLE_SIDE);
		Block registeredBed = registerBlock(bed, name, 1);
		bed.setSingleBlock(registeredBed);
		return registeredBed;
	}

	public static Block CreateSingleCanopyBed(String suffix) {
		return CreateSingleCanopyBed(suffix, 10, 3);
	}

	public static Block[] CreateDoubleCanopyBed(String suffix, float resistance, float hardness) {
		String leftName = "bed_canopy_foot_left_lower_" + suffix;
		String rightName = "bed_canopy_foot_right_lower_" + suffix;
		MultiBlockBed leftBed = new MultiBlockBed(Material.WOOD, leftName, resistance, hardness, MultiBlockBed.LEFT_SIDE);
		MultiBlockBed rightBed = new MultiBlockBed(Material.WOOD, rightName, resistance, hardness, MultiBlockBed.RIGHT_SIDE);
		Block leftBlock = registerBlock(leftBed, leftName, 1);
		Block rightBlock = registerBlockWithoutItem(rightBed, rightName);

		leftBed.setDoubleBlocks(leftBlock, rightBlock);
		rightBed.setDoubleBlocks(leftBlock, rightBlock);

		return new Block[] { leftBlock, rightBlock };
	}

	public static Block[] CreateDoubleCanopyBed(String suffix) {
		return CreateDoubleCanopyBed(suffix, 10, 6);
	}

	public static Block CreateSingleWoodBed(String suffix, float resistance, float hardness) {
		String name = "bed_wood_foot_" + suffix;
		return registerBlock(new MultiBlockWoodBed(Material.WOOD, name, resistance, hardness, false), name, 1);
	}

	public static Block CreateSingleWoodBed(String suffix) {
		return CreateSingleWoodBed(suffix, 10, 3);
	}

	public static Block CreateDoubleWoodBed(String suffix, float resistance, float hardness) {
		String name = "bed_wood_foot_left_" + suffix;
		return registerBlock(new MultiBlockWoodBed(Material.WOOD, name, resistance, hardness, true), name, 1);
	}

	public static Block CreateDoubleWoodBed(String suffix) {
		return CreateDoubleWoodBed(suffix, 10, 6);
	}

	public static Block CreateDiningTable(String suffix, float resistance, float hardness) {
		String name = "table_dining_" + suffix;
		return registerBlock(new DiningTable(Material.WOOD, name, resistance, hardness), name, 16);
	}

	public static Block CreateDiningTable(String suffix) {
		return CreateDiningTable(suffix, 10, 1);
	}

	public static Block CreateLowTable(String suffix, float resistance, float hardness) {
		String name = "table_low_" + suffix;
		return registerBlock(new LowTable(Material.WOOD, name, resistance, hardness), name, 16);
	}

	public static Block CreateLowTable(String suffix) {
		return CreateLowTable(suffix, 10, 1);
	}

	public static Block CreateWallShelf(String suffix, float resistance, float hardness) {
		String name = "shelf_wall_" + suffix;
		return registerBlock(new WallShelf(Material.WOOD, name, resistance, hardness), name, 16);
	}

	public static Block CreateWallShelf(String suffix) {
		return CreateWallShelf(suffix, 10, 1);
	}

	public static Block CreateBottleRack(String suffix, float resistance, float hardness) {
		String name = "bottle_rack_wood_ironage_" + suffix;
		return registerBlock(new BottleRack(Material.WOOD, name, resistance, hardness), name, 16);
	}

	public static Block CreateBottleRack(String suffix) {
		return CreateBottleRack(suffix, 10, 1);
	}

	public static Block CreateWoodCabinet(String suffix, float resistance, float hardness) {
		String name = "cabinet_wood_ironage_" + suffix;
		return registerBlock(new Cabinet(Material.WOOD, name, resistance, hardness), name, 64);
	}

	public static Block CreateWoodCabinet(String suffix) {
		return CreateWoodCabinet(suffix, 10, 1.5F);
	}

	public static Block CreateHalfWoodCabinet(String suffix, float resistance, float hardness) {
		String name = "half_cabinet_wood_ironage_" + suffix;
		return registerBlock(new HalfCabinet(Material.WOOD, name, resistance, hardness), name, 64);
	}

	public static Block CreateHalfWoodCabinet(String suffix) {
		return CreateHalfWoodCabinet(suffix, 10, 1.5F);
	}

	public static Block CreateWoodBarrel(String suffix, float resistance, float hardness) {
		String name = "barrel_wood_ironage_" + suffix;
		return registerBlock(new Barrel(Material.WOOD, name, resistance, hardness), name, 16);
	}

	public static Block CreateWoodBarrel(String suffix) {
		return CreateWoodBarrel(suffix, 10, 1.5F);
	}

	public static Block CreateSideWoodBarrel(String suffix, float resistance, float hardness) {
		String name = "side_barrel_wood_ironage_" + suffix;
		return registerBlock(new SideBarrel(Material.WOOD, name, resistance, hardness), name, 16);
	}

	public static Block CreateSideWoodBarrel(String suffix) {
		return CreateSideWoodBarrel(suffix, 10, 1.5F);
	}

	public static Block[] CreateWoodFoudre(String suffix, float resistance, float hardness) {
		String lowerName = "foudre_wood_ironage_" + suffix;
		String upperName = lowerName + "_upper";
		Foudre lower = new Foudre(Material.WOOD, lowerName, resistance, hardness, false);
		Foudre upper = new Foudre(Material.WOOD, upperName, resistance, hardness, true);
		lower.setCompanionBlocks(lower, upper);
		upper.setCompanionBlocks(lower, upper);
		return new Block[] { registerBlock(lower, lowerName, 1), registerBlockWithoutItem(upper, upperName) };
	}

	public static Block[] CreateWoodFoudre(String suffix) {
		return CreateWoodFoudre(suffix, 10, 1.5F);
	}

	public static Block[] CreateWoodPotStill(String suffix, float resistance, float hardness) {
		String lowerName = "pot_still_wood_ironage_" + suffix;
		String upperName = lowerName + "_upper";
		PotStill lower = new PotStill(Material.WOOD, lowerName, resistance, hardness, false);
		PotStill upper = new PotStill(Material.WOOD, upperName, resistance, hardness, true);
		lower.setCompanionBlocks(lower, upper);
		upper.setCompanionBlocks(lower, upper);
		return new Block[] { registerBlock(lower, lowerName, 1), registerBlockWithoutItem(upper, upperName) };
	}

	public static Block[] CreateWoodPotStill(String suffix) {
		return CreateWoodPotStill(suffix, 10, 1.5F);
	}

	public static Block CreateSurfaceDisplayBlocker(String name) {
		return registerBlockWithoutItem(new SurfaceDisplayBlocker(Material.BARRIER, name), name);
	}

	public static Block CreateHangingInnSign() {
		String name = "hanging_inn_sign";
		return registerBlock(new HangingInnSign(name), name, 16);
	}

	public static Block CreateClayOrnaments(String name) {
		return registerBlock(new OrnamentBlock(Material.CLAY, name,
			new String[] { "clay_vase", "clay_jug", "clay_bowl", "banded_clay_urn" },
			new String[] { "ornament_clay_vase", "ornament_clay_jug", "ornament_clay_bowl",
				"ornament_clay_banded_urn" },
			new AxisAlignedBB[] {
				new AxisAlignedBB(3.0D / 16.0D, 0.0D, 3.0D / 16.0D, 13.0D / 16.0D, 15.0D / 16.0D, 13.0D / 16.0D),
				new AxisAlignedBB(2.0D / 16.0D, 0.0D, 3.0D / 16.0D, 14.0D / 16.0D, 13.0D / 16.0D, 13.0D / 16.0D),
				new AxisAlignedBB(3.0D / 16.0D, 0.0D, 3.0D / 16.0D, 13.0D / 16.0D, 5.0D / 16.0D, 13.0D / 16.0D),
				new AxisAlignedBB(3.0D / 16.0D, 0.0D, 3.0D / 16.0D, 13.0D / 16.0D, 16.0D / 16.0D, 13.0D / 16.0D)
			}, 5, 0.6F, SoundType.STONE, "pickaxe"), name, 16);
	}

	public static Block CreateObsidianOrnaments(String name) {
		return registerBlock(new OrnamentBlock(Material.ROCK, name,
			new String[] { "obsidian_shard_cluster", "obsidian_idol", "obsidian_urn", "obsidian_chalice" },
			new String[] { "ornament_obsidian_shard_cluster", "ornament_obsidian_idol",
				"ornament_obsidian_urn", "ornament_obsidian_chalice" },
			new AxisAlignedBB[] {
				new AxisAlignedBB(4.0D / 16.0D, 0.0D, 4.0D / 16.0D, 12.0D / 16.0D, 9.0D / 16.0D, 12.0D / 16.0D),
				new AxisAlignedBB(4.0D / 16.0D, 0.0D, 4.0D / 16.0D, 12.0D / 16.0D, 14.0D / 16.0D, 12.0D / 16.0D),
				new AxisAlignedBB(3.0D / 16.0D, 0.0D, 3.0D / 16.0D, 13.0D / 16.0D, 16.0D / 16.0D, 13.0D / 16.0D),
				new AxisAlignedBB(5.0D / 16.0D, 0.0D, 5.0D / 16.0D, 11.0D / 16.0D, 12.0D / 16.0D, 11.0D / 16.0D)
			}, 10, 1.2F, SoundType.STONE, "pickaxe"), name, 16);
	}

	public static Block CreateGlassVaseOrnaments(String name) {
		return registerBlock(new GlassVaseBlock(Material.GLASS, name, 1, 0.3F), name, 16);
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

	public static Block CreateIronFloorTorchSconceTwin(String name) {
		return CreateIronFloorTorchSconceTwin(name, 10, 1);
	}

	public static Block CreateIronFloorTorchSconceTwin(String name, float resistance, float hardness) {
		return registerBlockWithoutItem(new LightSourceSconceTorchFloorTwin(Material.WOOD, name, resistance, hardness), name);
	}

	public static Block CreateIronFloorTorchSconceTwinUnlit(String name) {
		return CreateIronFloorTorchSconceTwinUnlit(name, 10, 1);
	}

	public static Block CreateIronFloorTorchSconceTwinUnlit(String name, float resistance, float hardness) {
		return registerBlockWithoutItem(new LightSourceSconceTorchFloorTwinUnlit(Material.WOOD, name, resistance, hardness), name);
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

	public static Block CreateIronWallTorchSconceTwin(String name) {
		return CreateIronWallTorchSconceTwin(name, 10, 1);
	}

	public static Block CreateIronWallTorchSconceTwin(String name, float resistance, float hardness) {
		return registerBlockWithoutItem(new LightSourceSconceTorchWallTwin(Material.WOOD, name, resistance, hardness), name);
	}

	public static Block CreateIronWallTorchSconceTwinUnlit(String name) {
		return CreateIronWallTorchSconceTwinUnlit(name, 10, 1);
	}

	public static Block CreateIronWallTorchSconceTwinUnlit(String name, float resistance, float hardness) {
		return registerBlockWithoutItem(new LightSourceSconceTorchWallTwinUnlit(Material.WOOD, name, resistance, hardness), name);
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

	public static Block CreateGlowdustLamp(String name) {
		return CreateGlowdustLamp(name, 1.5F, 0.3F);
	}

	public static Block CreateGlowdustLamp(String name, float resistance, float hardness) {
		return registerBlock(new LightSourceGlowdust(Material.GLASS, name, resistance, hardness), name, 64);
	}

	public static Block CreateRedLamp(String name) {
		return CreateRedLamp(name, 0, true, 1.5F, 0.3F);
	}

	public static Block CreateRedLampVariant(String name, int lightLevel) {
		return CreateRedLamp(name, lightLevel, false, 1.5F, 0.3F);
	}

	private static Block CreateRedLamp(String name, int lightLevel, boolean registerItem, float resistance, float hardness) {
		return registerBlock(new LightSourceRed(Material.GLASS, name, resistance, hardness, lightLevel), name, 64, registerItem);
	}

	public static Block CreateIronFloorGlowSconce(String name) {
		return CreateIronFloorGlowSconce(name, 10, 1);
	}

	public static Block CreateIronFloorGlowSconce(String name, float resistance, float hardness) {
		return registerBlockWithoutItem(new LightSourceSconceGlowFloor(Material.WOOD, name, resistance, hardness), name);
	}

	public static Block CreateIronWallGlowSconce(String name) {
		return CreateIronWallGlowSconce(name, 10, 1);
	}

	public static Block CreateIronWallGlowSconce(String name, float resistance, float hardness) {
		return registerBlockWithoutItem(new LightSourceSconceGlowWall(Material.WOOD, name, resistance, hardness), name);
	}

	public static Block CreateIronFloorRockSaltSconce(String name) {
		return CreateIronFloorRockSaltSconce(name, 10, 1);
	}

	public static Block CreateIronFloorRockSaltSconce(String name, float resistance, float hardness) {
		return registerBlockWithoutItem(new LightSourceSconceRockSaltFloor(Material.WOOD, name, resistance, hardness), name);
	}

	public static Block CreateIronWallRockSaltSconce(String name) {
		return CreateIronWallRockSaltSconce(name, 10, 1);
	}

	public static Block CreateIronWallRockSaltSconce(String name, float resistance, float hardness) {
		return registerBlockWithoutItem(new LightSourceSconceRockSaltWall(Material.WOOD, name, resistance, hardness), name);
	}

	public static Block CreateIronFloorRedSconce(String name, int lightLevel) {
		return CreateIronFloorRedSconce(name, 10, 1, lightLevel);
	}

	public static Block CreateIronFloorRedSconce(String name, float resistance, float hardness, int lightLevel) {
		return registerBlockWithoutItem(new LightSourceSconceRedFloor(Material.WOOD, name, resistance, hardness, lightLevel), name);
	}

	public static Block CreateIronWallRedSconce(String name, int lightLevel) {
		return CreateIronWallRedSconce(name, 10, 1, lightLevel);
	}

	public static Block CreateIronWallRedSconce(String name, float resistance, float hardness, int lightLevel) {
		return registerBlockWithoutItem(new LightSourceSconceRedWall(Material.WOOD, name, resistance, hardness, lightLevel), name);
	}

	public static Block CreateLavaLamp(String name) {
		return CreateLavaLamp(name, 1.5F, 0.3F);
	}

	public static Block CreateLavaLamp(String name, float resistance, float hardness) {
		return registerBlock(new LightSourceLava(Material.GLASS, name, resistance, hardness), name, 64);
	}

	public static Block CreateIronFloorLavaSconce(String name) {
		return CreateIronFloorLavaSconce(name, 10, 1);
	}

	public static Block CreateIronFloorLavaSconce(String name, float resistance, float hardness) {
		return registerBlockWithoutItem(new LightSourceSconceLavaFloor(Material.WOOD, name, resistance, hardness), name);
	}

	public static Block CreateIronWallLavaSconce(String name) {
		return CreateIronWallLavaSconce(name, 10, 1);
	}

	public static Block CreateIronWallLavaSconce(String name, float resistance, float hardness) {
		return registerBlockWithoutItem(new LightSourceSconceLavaWall(Material.WOOD, name, resistance, hardness), name);
	}

	public static Block CreateObsidianChunk(String name) {
		return CreateObsidianChunk(name, 10, 1);
	}

	public static Block CreateObsidianChunk(String name, float resistance, float hardness) {
		return registerBlock(new ObsideanLump(Material.ROCK, name, resistance, hardness), name, 64);
	}

	public static Block CreateCandleFloor(String name) {
		return CreateCandleFloor(name, 1, 0.1F);
	}

	public static Block CreateCandleFloor(String name, float resistance, float hardness) {
		return registerBlock(new LightSourceCandleFloor(Material.CIRCUITS, name, resistance, hardness), name, 64);
	}

	public static Block CreateChainTop(String name) {
		return CreateChainTop(name, 10, 1);
	}

	public static Block CreateChainTop(String name, float resistance, float hardness) {
		return registerBlock(new ChainTop(Material.IRON, name, resistance, hardness), name, 64);
	}

	public static Block CreateGoldBars(String name) {
		return registerBlock(new GoldBars(), name, 64);
	}

	public static Block CreateCandleChandelier(String name) {
		return CreateCandleChandelier(name, 10, 1);
	}

	public static Block CreateCandleChandelier(String name, float resistance, float hardness) {
		return registerBlock(new LightSourceChandelierCandle(Material.IRON, name, resistance, hardness), name, 64);
	}

	public static Block CreateCandleChandelierUnlit(String name) {
		return CreateCandleChandelierUnlit(name, 10, 1);
	}

	public static Block CreateCandleChandelierUnlit(String name, float resistance, float hardness) {
		return registerBlockWithoutItem(new LightSourceChandelierCandleUnlit(Material.IRON, name, resistance, hardness), name);
	}

	public static Block CreateTorchChandelier(String name) {
		return CreateTorchChandelier(name, 10, 1);
	}

	public static Block CreateTorchChandelier(String name, float resistance, float hardness) {
		return registerBlock(new LightSourceChandelierTorch(Material.IRON, name, resistance, hardness), name, 64);
	}

	public static Block CreateTorchChandelierUnlit(String name) {
		return CreateTorchChandelierUnlit(name, 10, 1);
	}

	public static Block CreateTorchChandelierUnlit(String name, float resistance, float hardness) {
		return registerBlockWithoutItem(new LightSourceChandelierTorchUnlit(Material.IRON, name, resistance, hardness), name);
	}

	public static Block CreateGlowstoneChandelier(String name) {
		return CreateGlowstoneChandelier(name, 10, 1);
	}

	public static Block CreateGlowstoneChandelier(String name, float resistance, float hardness) {
		return registerBlock(new LightSourceChandelierGlowstone(Material.GLASS, name, resistance, hardness), name, 64);
	}

	public static Block CreateLavaChandelier(String name) {
		return CreateLavaChandelier(name, 10, 1);
	}

	public static Block CreateLavaChandelier(String name, float resistance, float hardness) {
		return registerBlock(new LightSourceChandelierLava(Material.GLASS, name, resistance, hardness), name, 64);
	}

	public static Block CreateRedstoneChandelier(String name) {
		return CreateRedstoneChandelier(name, 0, true, 10, 1);
	}

	public static Block CreateRedstoneChandelierVariant(String name, int lightLevel) {
		return CreateRedstoneChandelier(name, lightLevel, false, 10, 1);
	}

	private static Block CreateRedstoneChandelier(String name, int lightLevel, boolean registerItem, float resistance, float hardness) {
		return registerBlock(new LightSourceChandelierRedstone(Material.GLASS, name, resistance, hardness, lightLevel), name, 64, registerItem);
	}

	public static Block CreateGrandChandelierHub(String name) {
		return CreateGrandChandelierHub(name, 50, 5);
	}

	public static Block CreateGrandChandelierHub(String name, float resistance, float hardness) {
		return registerBlock(new GrandChandelierHub(Material.IRON, name, resistance, hardness), name, 16);
	}

	public static Block CreateGrandChandelierSconce(String name) {
		return CreateGrandChandelierSconce(name, 50, 5);
	}

	public static Block CreateGrandChandelierSconce(String name, float resistance, float hardness) {
		return registerBlockWithoutItem(new GrandChandelierSconce(Material.IRON, name, resistance, hardness), name);
	}

	public static Block CreateIronHangingSconce(String name) {
		return CreateIronHangingSconce(name, 10, 1);
	}

	public static Block CreateIronHangingSconce(String name, float resistance, float hardness) {
		return registerBlockWithoutItem(new LightHolderSconceHanging(Material.IRON, name, resistance, hardness), name);
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
		registerWoodFurnitureFireInfo(block);
		
		if (registerItem) {
			ItemBlock itemBlock = block instanceof GlassVaseBlock ? new ItemBlockGlassVase(block)
				: block instanceof OrnamentBlock ? new ItemBlockOrnament(block)
				: block instanceof PotStill ? new ItemBlockPotStill(block)
				: block instanceof Barrel ? new ItemBlockBarrel(block)
				: block instanceof BottleRack ? new ItemBlockBottleRack(block)
				: block instanceof WallShelf ? new ItemBlockWallShelf(block)
				: block instanceof GrandChandelierHub ? new ItemBlockGrandChandelier(block)
				: MetalVariantHelper.isMetalVariantBlock(block) ? new ItemBlockMetalVariant(block)
				: block instanceof LightSourceLava ? new ItemBlockThrowableLavaLamp(block) : new ItemBlock(block);
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

	private static void registerWoodFurnitureFireInfo(Block block) {
		if (block instanceof Chair || block instanceof MultiBlockChair || block instanceof MultiBlockBed
				|| block instanceof MultiBlockWoodBed || block instanceof DiningTable || block instanceof WallShelf
				|| block instanceof BottleRack || block instanceof Barrel || block instanceof PotStill
				|| block instanceof HangingInnSign) {
			Blocks.FIRE.setFireInfo(block, WOOD_FURNITURE_FIRE_SPREAD_SPEED, WOOD_FURNITURE_FLAMMABILITY);
		}
	}
}
