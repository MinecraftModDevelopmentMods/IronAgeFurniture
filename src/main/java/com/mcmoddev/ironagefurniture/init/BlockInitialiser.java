package com.mcmoddev.ironagefurniture.init;

import java.lang.reflect.Field;

import com.mcmoddev.ironagefurniture.BlockObjectHolder;
import com.mcmoddev.ironagefurniture.IronAgeFurnitureConfiguration;
import com.mcmoddev.ironagefurniture.Ironagefurniture;
import com.mcmoddev.ironagefurniture.api.FurnitureFactory;

import net.minecraft.block.Block;
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
		generateChairs(); // and on the seventh day he was tired of standing around, and so he created chairs.
		generateBeds(); // and then, inevitably, he wanted somewhere nicer to sleep.
		generateTables(); // and then he had somewhere civilized to put dinner.
		generateShelves(); // and then he looked at the wall and saw useful empty space.
		generateLights(); // and then he saw that the vanilla torches were boring and said, let there be light!
	}

	private static void generateShelves() {
		if (!IronAgeFurnitureConfiguration.GENERATE_WALL_SHELVES) {
			return;
		}

		for (String suffix : WoodVariantHelper.getEnabledWoodSuffixes()) {
			BlockObjectHolder.shelf_wall.put(suffix, FurnitureFactory.CreateWallShelf(suffix));
		}
	}

	private static void generateTables() {
		if (!IronAgeFurnitureConfiguration.GENERATE_DINING_TABLES) {
			return;
		}

		for (String suffix : WoodVariantHelper.getEnabledWoodSuffixes()) {
			BlockObjectHolder.table_dining.put(suffix, FurnitureFactory.CreateDiningTable(suffix));
		}
	}

	private static void generateBeds() {
		if (!IronAgeFurnitureConfiguration.GENERATE_CANOPY_BEDS && !IronAgeFurnitureConfiguration.GENERATE_WOOD_BEDS) {
			return;
		}

		for (String suffix : WoodVariantHelper.getEnabledWoodSuffixes()) {
			if (IronAgeFurnitureConfiguration.GENERATE_CANOPY_BEDS) {
				BlockObjectHolder.bed_canopy_single.put(suffix, FurnitureFactory.CreateSingleCanopyBed(suffix));
				Block[] doubleBed = FurnitureFactory.CreateDoubleCanopyBed(suffix);
				BlockObjectHolder.bed_canopy_double_left.put(suffix, doubleBed[0]);
				BlockObjectHolder.bed_canopy_double_right.put(suffix, doubleBed[1]);
			}

			if (IronAgeFurnitureConfiguration.GENERATE_WOOD_BEDS) {
				BlockObjectHolder.bed_wood_single.put(suffix, FurnitureFactory.CreateSingleWoodBed(suffix));
				BlockObjectHolder.bed_wood_double.put(suffix, FurnitureFactory.CreateDoubleWoodBed(suffix));
			}
		}
	}
	
	private static void generateLights() {
		if (!IronAgeFurnitureConfiguration.GENERATE_LIGHTS) {
			return;
		}

		if (IronAgeFurnitureConfiguration.GENERATE_SCONCES) {
			generateSconces();
		}
		if (IronAgeFurnitureConfiguration.GENERATE_GLOW_LAMPS) {
			generateGlowLamps();
		}
		if (IronAgeFurnitureConfiguration.GENERATE_REDSTONE_LAMPS) {
			generateRedstoneLamps();
		}
		if (IronAgeFurnitureConfiguration.GENERATE_LAVA_LAMPS) {
			generateLavaLamps();
		}
		if (IronAgeFurnitureConfiguration.GENERATE_CANDLES) {
			generateCandles();
		}
		if (IronAgeFurnitureConfiguration.GENERATE_CHAINS) {
			BlockObjectHolder.chain_top = FurnitureFactory.CreateChainTop("chain_top");
		}
		if (IronAgeFurnitureConfiguration.GENERATE_CHANDELIERS) {
			generateChandeliers();
		}
	}

	private static void generateSconces() {
		BlockObjectHolder.light_metal_ironage_sconce_floor_empty_iron = FurnitureFactory.CreateIronFloorSconce("light_metal_ironage_sconce_floor_empty_iron");
		BlockObjectHolder.light_metal_ironage_sconce_wall_empty_iron = FurnitureFactory.CreateIronWallSconce("light_metal_ironage_sconce_wall_empty_iron");
		BlockObjectHolder.light_metal_ironage_sconce_floor_torch_iron = FurnitureFactory.CreateIronFloorTorchSconce("light_metal_ironage_sconce_floor_torch_iron");
		BlockObjectHolder.light_metal_ironage_sconce_floor_torch_iron_unlit = FurnitureFactory.CreateIronFloorTorchSconceUnlit("light_metal_ironage_sconce_floor_torch_iron_unlit");
		BlockObjectHolder.light_metal_ironage_sconce_floor_torch_iron_twin = FurnitureFactory.CreateIronFloorTorchSconceTwin("light_metal_ironage_sconce_floor_torch_iron_twin");
		BlockObjectHolder.light_metal_ironage_sconce_floor_torch_iron_twin_unlit = FurnitureFactory.CreateIronFloorTorchSconceTwinUnlit("light_metal_ironage_sconce_floor_torch_iron_twin_unlit");
		BlockObjectHolder.light_metal_ironage_sconce_wall_torch_iron = FurnitureFactory.CreateIronWallTorchSconce("light_metal_ironage_sconce_wall_torch_iron");
		BlockObjectHolder.light_metal_ironage_sconce_wall_torch_iron_unlit = FurnitureFactory.CreateIronWallTorchSconceUnlit("light_metal_ironage_sconce_wall_torch_iron_unlit");
		BlockObjectHolder.light_metal_ironage_sconce_wall_torch_iron_twin = FurnitureFactory.CreateIronWallTorchSconceTwin("light_metal_ironage_sconce_wall_torch_iron_twin");
		BlockObjectHolder.light_metal_ironage_sconce_wall_torch_iron_twin_unlit = FurnitureFactory.CreateIronWallTorchSconceTwinUnlit("light_metal_ironage_sconce_wall_torch_iron_twin_unlit");
		BlockObjectHolder.light_metal_ironage_sconce_floor_redtorch_iron = FurnitureFactory.CreateIronFloorRedTorchSconce("light_metal_ironage_sconce_floor_redtorch_iron");
		BlockObjectHolder.light_metal_ironage_sconce_floor_redtorch_iron_unlit = FurnitureFactory.CreateIronFloorRedTorchSconceUnlit("light_metal_ironage_sconce_floor_redtorch_iron_unlit");
		BlockObjectHolder.light_metal_ironage_sconce_wall_redtorch_iron = FurnitureFactory.CreateIronWallRedTorchSconce("light_metal_ironage_sconce_wall_redtorch_iron");
		BlockObjectHolder.light_metal_ironage_sconce_wall_redtorch_iron_unlit = FurnitureFactory.CreateIronWallRedTorchSconceUnlit("light_metal_ironage_sconce_wall_redtorch_iron_unlit");
	}

	private static void generateGlowLamps() {
		BlockObjectHolder.light_metal_ironage_block_floor_glow_clear = FurnitureFactory.CreateGlowdustLamp("light_metal_ironage_block_floor_glow_clear");
		if (IronAgeFurnitureConfiguration.GENERATE_SCONCES) {
			BlockObjectHolder.light_metal_ironage_sconce_floor_glow_iron = FurnitureFactory.CreateIronFloorGlowSconce("light_metal_ironage_sconce_floor_glow_iron");
			BlockObjectHolder.light_metal_ironage_sconce_wall_glow_iron = FurnitureFactory.CreateIronWallGlowSconce("light_metal_ironage_sconce_wall_glow_iron");
		}
	}

	private static void generateRedstoneLamps() {
		BlockObjectHolder.light_metal_ironage_block_floor_red_clear = FurnitureFactory.CreateRedLamp("light_metal_ironage_block_floor_red_clear");
		BlockObjectHolder.light_metal_ironage_block_floor_red_clear_one = FurnitureFactory.CreateRedLampVariant("light_metal_ironage_block_floor_red_clear_one", 1);
		BlockObjectHolder.light_metal_ironage_block_floor_red_clear_two = FurnitureFactory.CreateRedLampVariant("light_metal_ironage_block_floor_red_clear_two", 2);
		BlockObjectHolder.light_metal_ironage_block_floor_red_clear_three = FurnitureFactory.CreateRedLampVariant("light_metal_ironage_block_floor_red_clear_three", 3);
		BlockObjectHolder.light_metal_ironage_block_floor_red_clear_four = FurnitureFactory.CreateRedLampVariant("light_metal_ironage_block_floor_red_clear_four", 4);
		BlockObjectHolder.light_metal_ironage_block_floor_red_clear_five = FurnitureFactory.CreateRedLampVariant("light_metal_ironage_block_floor_red_clear_five", 5);
		BlockObjectHolder.light_metal_ironage_block_floor_red_clear_six = FurnitureFactory.CreateRedLampVariant("light_metal_ironage_block_floor_red_clear_six", 6);
		BlockObjectHolder.light_metal_ironage_block_floor_red_clear_seven = FurnitureFactory.CreateRedLampVariant("light_metal_ironage_block_floor_red_clear_seven", 7);
		BlockObjectHolder.light_metal_ironage_block_floor_red_clear_eight = FurnitureFactory.CreateRedLampVariant("light_metal_ironage_block_floor_red_clear_eight", 8);
		BlockObjectHolder.light_metal_ironage_block_floor_red_clear_nine = FurnitureFactory.CreateRedLampVariant("light_metal_ironage_block_floor_red_clear_nine", 9);
		BlockObjectHolder.light_metal_ironage_block_floor_red_clear_ten = FurnitureFactory.CreateRedLampVariant("light_metal_ironage_block_floor_red_clear_ten", 10);
		BlockObjectHolder.light_metal_ironage_block_floor_red_clear_eleven = FurnitureFactory.CreateRedLampVariant("light_metal_ironage_block_floor_red_clear_eleven", 11);
		BlockObjectHolder.light_metal_ironage_block_floor_red_clear_twelve = FurnitureFactory.CreateRedLampVariant("light_metal_ironage_block_floor_red_clear_twelve", 12);
		BlockObjectHolder.light_metal_ironage_block_floor_red_clear_thirteen = FurnitureFactory.CreateRedLampVariant("light_metal_ironage_block_floor_red_clear_thirteen", 13);
		BlockObjectHolder.light_metal_ironage_block_floor_red_clear_fourteen = FurnitureFactory.CreateRedLampVariant("light_metal_ironage_block_floor_red_clear_fourteen", 14);
		BlockObjectHolder.light_metal_ironage_block_floor_red_clear_fifteen = FurnitureFactory.CreateRedLampVariant("light_metal_ironage_block_floor_red_clear_fifteen", 15);
		if (IronAgeFurnitureConfiguration.GENERATE_SCONCES) {
			generateRedstoneLampSconces();
		}
	}

	private static void generateRedstoneLampSconces() {
		BlockObjectHolder.light_metal_ironage_sconce_floor_red_iron = FurnitureFactory.CreateIronFloorRedSconce("light_metal_ironage_sconce_floor_red_iron", 0);
		BlockObjectHolder.light_metal_ironage_sconce_floor_red_iron_one = FurnitureFactory.CreateIronFloorRedSconce("light_metal_ironage_sconce_floor_red_iron_one", 1);
		BlockObjectHolder.light_metal_ironage_sconce_floor_red_iron_two = FurnitureFactory.CreateIronFloorRedSconce("light_metal_ironage_sconce_floor_red_iron_two", 2);
		BlockObjectHolder.light_metal_ironage_sconce_floor_red_iron_three = FurnitureFactory.CreateIronFloorRedSconce("light_metal_ironage_sconce_floor_red_iron_three", 3);
		BlockObjectHolder.light_metal_ironage_sconce_floor_red_iron_four = FurnitureFactory.CreateIronFloorRedSconce("light_metal_ironage_sconce_floor_red_iron_four", 4);
		BlockObjectHolder.light_metal_ironage_sconce_floor_red_iron_five = FurnitureFactory.CreateIronFloorRedSconce("light_metal_ironage_sconce_floor_red_iron_five", 5);
		BlockObjectHolder.light_metal_ironage_sconce_floor_red_iron_six = FurnitureFactory.CreateIronFloorRedSconce("light_metal_ironage_sconce_floor_red_iron_six", 6);
		BlockObjectHolder.light_metal_ironage_sconce_floor_red_iron_seven = FurnitureFactory.CreateIronFloorRedSconce("light_metal_ironage_sconce_floor_red_iron_seven", 7);
		BlockObjectHolder.light_metal_ironage_sconce_floor_red_iron_eight = FurnitureFactory.CreateIronFloorRedSconce("light_metal_ironage_sconce_floor_red_iron_eight", 8);
		BlockObjectHolder.light_metal_ironage_sconce_floor_red_iron_nine = FurnitureFactory.CreateIronFloorRedSconce("light_metal_ironage_sconce_floor_red_iron_nine", 9);
		BlockObjectHolder.light_metal_ironage_sconce_floor_red_iron_ten = FurnitureFactory.CreateIronFloorRedSconce("light_metal_ironage_sconce_floor_red_iron_ten", 10);
		BlockObjectHolder.light_metal_ironage_sconce_floor_red_iron_eleven = FurnitureFactory.CreateIronFloorRedSconce("light_metal_ironage_sconce_floor_red_iron_eleven", 11);
		BlockObjectHolder.light_metal_ironage_sconce_floor_red_iron_twelve = FurnitureFactory.CreateIronFloorRedSconce("light_metal_ironage_sconce_floor_red_iron_twelve", 12);
		BlockObjectHolder.light_metal_ironage_sconce_floor_red_iron_thirteen = FurnitureFactory.CreateIronFloorRedSconce("light_metal_ironage_sconce_floor_red_iron_thirteen", 13);
		BlockObjectHolder.light_metal_ironage_sconce_floor_red_iron_fourteen = FurnitureFactory.CreateIronFloorRedSconce("light_metal_ironage_sconce_floor_red_iron_fourteen", 14);
		BlockObjectHolder.light_metal_ironage_sconce_floor_red_iron_fifteen = FurnitureFactory.CreateIronFloorRedSconce("light_metal_ironage_sconce_floor_red_iron_fifteen", 15);
		BlockObjectHolder.light_metal_ironage_sconce_wall_red_iron = FurnitureFactory.CreateIronWallRedSconce("light_metal_ironage_sconce_wall_red_iron", 0);
		BlockObjectHolder.light_metal_ironage_sconce_wall_red_iron_one = FurnitureFactory.CreateIronWallRedSconce("light_metal_ironage_sconce_wall_red_iron_one", 1);
		BlockObjectHolder.light_metal_ironage_sconce_wall_red_iron_two = FurnitureFactory.CreateIronWallRedSconce("light_metal_ironage_sconce_wall_red_iron_two", 2);
		BlockObjectHolder.light_metal_ironage_sconce_wall_red_iron_three = FurnitureFactory.CreateIronWallRedSconce("light_metal_ironage_sconce_wall_red_iron_three", 3);
		BlockObjectHolder.light_metal_ironage_sconce_wall_red_iron_four = FurnitureFactory.CreateIronWallRedSconce("light_metal_ironage_sconce_wall_red_iron_four", 4);
		BlockObjectHolder.light_metal_ironage_sconce_wall_red_iron_five = FurnitureFactory.CreateIronWallRedSconce("light_metal_ironage_sconce_wall_red_iron_five", 5);
		BlockObjectHolder.light_metal_ironage_sconce_wall_red_iron_six = FurnitureFactory.CreateIronWallRedSconce("light_metal_ironage_sconce_wall_red_iron_six", 6);
		BlockObjectHolder.light_metal_ironage_sconce_wall_red_iron_seven = FurnitureFactory.CreateIronWallRedSconce("light_metal_ironage_sconce_wall_red_iron_seven", 7);
		BlockObjectHolder.light_metal_ironage_sconce_wall_red_iron_eight = FurnitureFactory.CreateIronWallRedSconce("light_metal_ironage_sconce_wall_red_iron_eight", 8);
		BlockObjectHolder.light_metal_ironage_sconce_wall_red_iron_nine = FurnitureFactory.CreateIronWallRedSconce("light_metal_ironage_sconce_wall_red_iron_nine", 9);
		BlockObjectHolder.light_metal_ironage_sconce_wall_red_iron_ten = FurnitureFactory.CreateIronWallRedSconce("light_metal_ironage_sconce_wall_red_iron_ten", 10);
		BlockObjectHolder.light_metal_ironage_sconce_wall_red_iron_eleven = FurnitureFactory.CreateIronWallRedSconce("light_metal_ironage_sconce_wall_red_iron_eleven", 11);
		BlockObjectHolder.light_metal_ironage_sconce_wall_red_iron_twelve = FurnitureFactory.CreateIronWallRedSconce("light_metal_ironage_sconce_wall_red_iron_twelve", 12);
		BlockObjectHolder.light_metal_ironage_sconce_wall_red_iron_thirteen = FurnitureFactory.CreateIronWallRedSconce("light_metal_ironage_sconce_wall_red_iron_thirteen", 13);
		BlockObjectHolder.light_metal_ironage_sconce_wall_red_iron_fourteen = FurnitureFactory.CreateIronWallRedSconce("light_metal_ironage_sconce_wall_red_iron_fourteen", 14);
		BlockObjectHolder.light_metal_ironage_sconce_wall_red_iron_fifteen = FurnitureFactory.CreateIronWallRedSconce("light_metal_ironage_sconce_wall_red_iron_fifteen", 15);
	}

	private static void generateLavaLamps() {
		BlockObjectHolder.obsidian_chunk = FurnitureFactory.CreateObsidianChunk("obsidian_chunk");
		BlockObjectHolder.light_metal_ironage_block_floor_lava_clear = FurnitureFactory.CreateLavaLamp("light_metal_ironage_block_floor_lava_clear");
		if (IronAgeFurnitureConfiguration.GENERATE_SCONCES) {
			BlockObjectHolder.light_metal_ironage_sconce_floor_lava_iron = FurnitureFactory.CreateIronFloorLavaSconce("light_metal_ironage_sconce_floor_lava_iron");
			BlockObjectHolder.light_metal_ironage_sconce_wall_lava_iron = FurnitureFactory.CreateIronWallLavaSconce("light_metal_ironage_sconce_wall_lava_iron");
		}
	}

	private static void generateCandles() {
		BlockObjectHolder.light_metal_ironage_candle_floor = FurnitureFactory.CreateCandleFloor("light_metal_ironage_candle_floor");
		BlockObjectHolder.light_metal_ironage_candle_wall = FurnitureFactory.CreateCandleWall("light_metal_ironage_candle_wall");
		BlockObjectHolder.light_metal_ironage_candle_floor_unlit = FurnitureFactory.CreateCandleFloorUnlit("light_metal_ironage_candle_floor_unlit");
		BlockObjectHolder.light_metal_ironage_candle_wall_unlit = FurnitureFactory.CreateCandleWallUnlit("light_metal_ironage_candle_wall_unlit");
		if (IronAgeFurnitureConfiguration.GENERATE_SCONCES) {
			generateCandleSconces();
		}
	}

	private static void generateCandleSconces() {
		BlockObjectHolder.light_metal_ironage_sconce_floor_candle_iron = FurnitureFactory.CreateIronFloorCandleSconce("light_metal_ironage_sconce_floor_candle_iron", 1);
		BlockObjectHolder.light_metal_ironage_sconce_floor_candle_iron_two = FurnitureFactory.CreateIronFloorCandleSconce("light_metal_ironage_sconce_floor_candle_iron_two", 2);
		BlockObjectHolder.light_metal_ironage_sconce_floor_candle_iron_three = FurnitureFactory.CreateIronFloorCandleSconce("light_metal_ironage_sconce_floor_candle_iron_three", 3);
		BlockObjectHolder.light_metal_ironage_sconce_floor_candle_iron_four = FurnitureFactory.CreateIronFloorCandleSconce("light_metal_ironage_sconce_floor_candle_iron_four", 4);
		BlockObjectHolder.light_metal_ironage_sconce_floor_candle_iron_unlit = FurnitureFactory.CreateIronFloorCandleSconceUnlit("light_metal_ironage_sconce_floor_candle_iron_unlit", 1);
		BlockObjectHolder.light_metal_ironage_sconce_floor_candle_iron_two_unlit = FurnitureFactory.CreateIronFloorCandleSconceUnlit("light_metal_ironage_sconce_floor_candle_iron_two_unlit", 2);
		BlockObjectHolder.light_metal_ironage_sconce_floor_candle_iron_three_unlit = FurnitureFactory.CreateIronFloorCandleSconceUnlit("light_metal_ironage_sconce_floor_candle_iron_three_unlit", 3);
		BlockObjectHolder.light_metal_ironage_sconce_floor_candle_iron_four_unlit = FurnitureFactory.CreateIronFloorCandleSconceUnlit("light_metal_ironage_sconce_floor_candle_iron_four_unlit", 4);
		BlockObjectHolder.light_metal_ironage_sconce_wall_candle_iron = FurnitureFactory.CreateIronWallCandleSconce("light_metal_ironage_sconce_wall_candle_iron", 1);
		BlockObjectHolder.light_metal_ironage_sconce_wall_candle_iron_two = FurnitureFactory.CreateIronWallCandleSconce("light_metal_ironage_sconce_wall_candle_iron_two", 2);
		BlockObjectHolder.light_metal_ironage_sconce_wall_candle_iron_three = FurnitureFactory.CreateIronWallCandleSconce("light_metal_ironage_sconce_wall_candle_iron_three", 3);
		BlockObjectHolder.light_metal_ironage_sconce_wall_candle_iron_four = FurnitureFactory.CreateIronWallCandleSconce("light_metal_ironage_sconce_wall_candle_iron_four", 4);
		BlockObjectHolder.light_metal_ironage_sconce_wall_candle_iron_unlit = FurnitureFactory.CreateIronWallCandleSconceUnlit("light_metal_ironage_sconce_wall_candle_iron_unlit", 1);
		BlockObjectHolder.light_metal_ironage_sconce_wall_candle_iron_two_unlit = FurnitureFactory.CreateIronWallCandleSconceUnlit("light_metal_ironage_sconce_wall_candle_iron_two_unlit", 2);
		BlockObjectHolder.light_metal_ironage_sconce_wall_candle_iron_three_unlit = FurnitureFactory.CreateIronWallCandleSconceUnlit("light_metal_ironage_sconce_wall_candle_iron_three_unlit", 3);
		BlockObjectHolder.light_metal_ironage_sconce_wall_candle_iron_four_unlit = FurnitureFactory.CreateIronWallCandleSconceUnlit("light_metal_ironage_sconce_wall_candle_iron_four_unlit", 4);
	}

	private static void generateChandeliers() {
		if (IronAgeFurnitureConfiguration.GENERATE_CANDLES) {
			BlockObjectHolder.chandelier_candle = FurnitureFactory.CreateCandleChandelier("chandelier_candle");
			BlockObjectHolder.chandelier_candle_unlit = FurnitureFactory.CreateCandleChandelierUnlit("chandelier_candle_unlit");
		}
		BlockObjectHolder.chandelier_torch = FurnitureFactory.CreateTorchChandelier("chandelier_torch");
		BlockObjectHolder.chandelier_torch_unlit = FurnitureFactory.CreateTorchChandelierUnlit("chandelier_torch_unlit");
		if (IronAgeFurnitureConfiguration.GENERATE_GLOW_LAMPS) {
			BlockObjectHolder.chandelier_glowstone = FurnitureFactory.CreateGlowstoneChandelier("chandelier_glowstone");
		}
		if (IronAgeFurnitureConfiguration.GENERATE_LAVA_LAMPS) {
			BlockObjectHolder.chandelier_lava = FurnitureFactory.CreateLavaChandelier("chandelier_lava");
		}
		if (IronAgeFurnitureConfiguration.GENERATE_REDSTONE_LAMPS) {
			generateRedstoneChandeliers();
		}
	}

	private static void generateRedstoneChandeliers() {
		BlockObjectHolder.chandelier_redstone = FurnitureFactory.CreateRedstoneChandelier("chandelier_redstone");
		BlockObjectHolder.chandelier_redstone_one = FurnitureFactory.CreateRedstoneChandelierVariant("chandelier_redstone_one", 1);
		BlockObjectHolder.chandelier_redstone_two = FurnitureFactory.CreateRedstoneChandelierVariant("chandelier_redstone_two", 2);
		BlockObjectHolder.chandelier_redstone_three = FurnitureFactory.CreateRedstoneChandelierVariant("chandelier_redstone_three", 3);
		BlockObjectHolder.chandelier_redstone_four = FurnitureFactory.CreateRedstoneChandelierVariant("chandelier_redstone_four", 4);
		BlockObjectHolder.chandelier_redstone_five = FurnitureFactory.CreateRedstoneChandelierVariant("chandelier_redstone_five", 5);
		BlockObjectHolder.chandelier_redstone_six = FurnitureFactory.CreateRedstoneChandelierVariant("chandelier_redstone_six", 6);
		BlockObjectHolder.chandelier_redstone_seven = FurnitureFactory.CreateRedstoneChandelierVariant("chandelier_redstone_seven", 7);
		BlockObjectHolder.chandelier_redstone_eight = FurnitureFactory.CreateRedstoneChandelierVariant("chandelier_redstone_eight", 8);
		BlockObjectHolder.chandelier_redstone_nine = FurnitureFactory.CreateRedstoneChandelierVariant("chandelier_redstone_nine", 9);
		BlockObjectHolder.chandelier_redstone_ten = FurnitureFactory.CreateRedstoneChandelierVariant("chandelier_redstone_ten", 10);
		BlockObjectHolder.chandelier_redstone_eleven = FurnitureFactory.CreateRedstoneChandelierVariant("chandelier_redstone_eleven", 11);
		BlockObjectHolder.chandelier_redstone_twelve = FurnitureFactory.CreateRedstoneChandelierVariant("chandelier_redstone_twelve", 12);
		BlockObjectHolder.chandelier_redstone_thirteen = FurnitureFactory.CreateRedstoneChandelierVariant("chandelier_redstone_thirteen", 13);
		BlockObjectHolder.chandelier_redstone_fourteen = FurnitureFactory.CreateRedstoneChandelierVariant("chandelier_redstone_fourteen", 14);
		BlockObjectHolder.chandelier_redstone_fifteen = FurnitureFactory.CreateRedstoneChandelierVariant("chandelier_redstone_fifteen", 15);
	}
	
	private static void generateChairs() {
		registerWoodFurnitureVariants(IronAgeFurnitureConfiguration.GENERATE_CLASSIC_CHAIRS,
			"chair_wood_ironage_classic_", new WoodBlockFactory() {
				@Override
				public Block create(String name) {
					return FurnitureFactory.CreateWoodChair(name);
				}
			});

		registerWoodFurnitureVariants(IronAgeFurnitureConfiguration.GENERATE_SHIELD_CHAIRS,
			"chair_wood_ironage_shield_", new WoodBlockFactory() {
				@Override
				public Block create(String name) {
					return FurnitureFactory.CreateWoodShieldChair(name);
				}
			});

		registerWoodFurnitureVariants(IronAgeFurnitureConfiguration.GENERATE_SHORT_STOOLS,
			"chair_wood_ironage_stool_short_", new WoodBlockFactory() {
				@Override
				public Block create(String name) {
					return FurnitureFactory.CreateWoodShortStool(name);
				}
			});

		registerWoodFurnitureVariants(IronAgeFurnitureConfiguration.GENERATE_TALL_STOOLS,
			"chair_wood_ironage_stool_tall_", new WoodBlockFactory() {
				@Override
				public Block create(String name) {
					return FurnitureFactory.CreateWoodTallStool(name);
				}
			});

		if (IronAgeFurnitureConfiguration.GENERATE_WOOD_BENCHES) {
			registerWoodFurnitureVariants(true, "chair_wood_ironage_bench_single_", new WoodBlockFactory() {
				@Override
				public Block create(String name) {
					return FurnitureFactory.CreateWoodBench(name);
				}
			});
			registerWoodFurnitureVariants(true, "chair_wood_ironage_bench_padded_single_", new WoodBlockFactory() {
				@Override
				public Block create(String name) {
					return FurnitureFactory.CreateWoodBench(name);
				}
			});
			registerLogBenchVariants(new WoodBlockFactory() {
				@Override
				public Block create(String name) {
					return FurnitureFactory.CreateWoodBench(name);
				}
			});
			registerWoodFurnitureVariants(true, "chair_wood_ironage_bench_back_single_", new WoodBlockFactory() {
				@Override
				public Block create(String name) {
					return FurnitureFactory.CreateWoodBackBench(name);
				}
			});
			registerWoodFurnitureVariants(true, "chair_wood_ironage_bench_back_padded_single_", new WoodBlockFactory() {
				@Override
				public Block create(String name) {
					return FurnitureFactory.CreateWoodBackBench(name);
				}
			});
		}

		generateWingbackAndThroneChairs();
	}

	private static void generateWingbackAndThroneChairs() {
		if (!IronAgeFurnitureConfiguration.GENERATE_CLASSIC_CHAIRS || !IronAgeFurnitureConfiguration.GENERATE_WINGBACK_CHAIRS) {
			return;
		}

		for (String suffix : WoodVariantHelper.getEnabledWoodSuffixes()) {
			if (!Ironagefurniture.BlockRegistry.containsKey("chair_wood_ironage_classic_" + suffix)) {
				continue;
			}

			String wingbackName = "chair_wood_ironage_wingback_" + suffix;
			Block wingback = FurnitureFactory.CreateWoodWingbackChair(wingbackName);
			BlockObjectHolder.chair_wood_ironage_wingback.put(suffix, wingback);

			if (IronAgeFurnitureConfiguration.GENERATE_THRONES) {
				String throneName = "chair_wood_ironage_throne_" + suffix;
				BlockObjectHolder.chair_wood_ironage_throne.put(suffix, FurnitureFactory.CreateWoodThroneChair(throneName));
			}
		}
	}

	private static void registerWoodFurnitureVariants(boolean enabled, String registryPrefix, WoodBlockFactory factory) {
		if (!enabled) {
			return;
		}

		for (String suffix : WoodVariantHelper.getEnabledWoodSuffixes()) {
			String name = registryPrefix + suffix;
			setBlockHolder(name, factory.create(name));
		}
	}

	private static void registerLogBenchVariants(WoodBlockFactory factory) {
		for (String suffix : WoodVariantHelper.getEnabledLogSuffixes()) {
			String name = "chair_wood_ironage_bench_log_single_" + suffix;
			setBlockHolder(name, factory.create(name));
		}
	}

	private static void setBlockHolder(String fieldName, Block block) {
		try {
			Field field = BlockObjectHolder.class.getField(fieldName);
			field.set(null, block);
		}
		catch (NoSuchFieldException e) {
			throw new IllegalStateException("Missing BlockObjectHolder field for generated block " + fieldName, e);
		}
		catch (IllegalAccessException e) {
			throw new IllegalStateException("Unable to assign BlockObjectHolder field for generated block " + fieldName, e);
		}
	}

	private interface WoodBlockFactory {
		Block create(String name);
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
