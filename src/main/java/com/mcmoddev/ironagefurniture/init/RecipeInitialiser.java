package com.mcmoddev.ironagefurniture.init;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.mcmoddev.ironagefurniture.BlockObjectHolder;
import com.mcmoddev.ironagefurniture.IronAgeFurnitureConfiguration;
import com.mcmoddev.ironagefurniture.ItemObjectHolder;
import com.mcmoddev.ironagefurniture.api.FurnitureFactory;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class RecipeInitialiser {
	protected RecipeInitialiser() {
		throw new IllegalAccessError("This class cannot be instansiated");
	}

	/**
	 *
	 */
	public static void init() {
		generateChairRecipes();
		generateBedRecipes();
		generateLightRecipes();
	}

	private static void generateBedRecipes() {
		addSingleBedRecipes();

		if (IronAgeFurnitureConfiguration.GENERATE_WOOD_BEDS) {
			List<String> suffixes = new ArrayList<String>(BlockObjectHolder.bed_wood_single.keySet());
			Collections.sort(suffixes);

			for (String suffix : suffixes) {
				Block singleBed = BlockObjectHolder.bed_wood_single.get(suffix);
				Block doubleBed = BlockObjectHolder.bed_wood_double.get(suffix);

				if (singleBed != null && doubleBed != null) {
					FurnitureFactory.AddDoubleWoodBedRecipe(singleBed, doubleBed);
				}
			}
		}

		if (IronAgeFurnitureConfiguration.GENERATE_CANOPY_BEDS) {
			List<String> suffixes = new ArrayList<String>(BlockObjectHolder.bed_canopy_single.keySet());
			Collections.sort(suffixes);

			for (String suffix : suffixes) {
				Block singleBed = BlockObjectHolder.bed_canopy_single.get(suffix);
				Block doubleBed = BlockObjectHolder.bed_canopy_double_left.get(suffix);

				if (singleBed != null && doubleBed != null) {
					FurnitureFactory.AddDoubleCanopyBedRecipe(singleBed, doubleBed);
				}
			}
		}
	}

	private static void addSingleBedRecipes() {
		if (!IronAgeFurnitureConfiguration.GENERATE_WOOD_BEDS && !IronAgeFurnitureConfiguration.GENERATE_CANOPY_BEDS) {
			return;
		}

		WoodVariantHelper.forEachEnabledPlankVariant(new WoodVariantHelper.ItemStackVariantConsumer() {
			@Override
			public void accept(String suffix, ItemStack planks) {
				if (IronAgeFurnitureConfiguration.GENERATE_WOOD_BEDS) {
					Block woodBed = BlockObjectHolder.bed_wood_single.get(suffix);

					if (woodBed != null) {
						FurnitureFactory.AddSingleWoodBedRecipe(planks, woodBed);
					}
				}

				if (IronAgeFurnitureConfiguration.GENERATE_CANOPY_BEDS) {
					Block canopyBed = BlockObjectHolder.bed_canopy_single.get(suffix);

					if (canopyBed != null) {
						FurnitureFactory.AddSingleCanopyBedRecipe(planks, canopyBed);
					}
				}
			}
		});
	}
	
	private static void generateLightRecipes() {
		if (!IronAgeFurnitureConfiguration.GENERATE_LIGHTS) {
			return;
		}

		if (IronAgeFurnitureConfiguration.GENERATE_SCONCES) {
			FurnitureFactory.AddIronSconceRecipe(BlockObjectHolder.light_metal_ironage_sconce_floor_empty_iron);
		}
		if (IronAgeFurnitureConfiguration.GENERATE_CANDLES) {
			GameRegistry.addSmelting(Items.COOKED_PORKCHOP, new ItemStack(ItemObjectHolder.tallow, 3), 0.1F);
			GameRegistry.addSmelting(Items.COOKED_BEEF, new ItemStack(ItemObjectHolder.tallow, 2), 0.1F);
			GameRegistry.addSmelting(Items.COOKED_MUTTON, new ItemStack(ItemObjectHolder.tallow, 2), 0.1F);
			GameRegistry.addSmelting(Items.COOKED_RABBIT, new ItemStack(ItemObjectHolder.tallow, 1), 0.1F);
			GameRegistry.addSmelting(Items.COOKED_CHICKEN, new ItemStack(ItemObjectHolder.tallow, 1), 0.1F);
			GameRegistry.addSmelting(Items.ROTTEN_FLESH, new ItemStack(ItemObjectHolder.tallow, 1), 0.1F);
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BlockObjectHolder.light_metal_ironage_candle_floor, 8),
				new ItemStack(ItemObjectHolder.tallow, 1), Items.STRING));
		}
		if (IronAgeFurnitureConfiguration.GENERATE_CHAINS) {
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BlockObjectHolder.chain_top, 3),
				Blocks.IRON_BARS));
		}
		if (IronAgeFurnitureConfiguration.GENERATE_GLOW_LAMPS) {
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BlockObjectHolder.light_metal_ironage_block_floor_glow_clear, 1),
				Items.GLOWSTONE_DUST, Items.GLASS_BOTTLE));
		}
		if (IronAgeFurnitureConfiguration.GENERATE_REDSTONE_LAMPS) {
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BlockObjectHolder.light_metal_ironage_block_floor_red_clear, 1),
				Items.REDSTONE, Items.GLASS_BOTTLE));
		}
		if (IronAgeFurnitureConfiguration.GENERATE_LAVA_LAMPS) {
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockObjectHolder.light_metal_ironage_block_floor_lava_clear, 8),
				"yyy", "yxy", "yyy", 'y', Items.GLASS_BOTTLE, 'x', Items.LAVA_BUCKET));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Blocks.OBSIDIAN, 1),
				"yyy", "yyy", "yyy", 'y', BlockObjectHolder.obsidian_chunk));
		}
		if (IronAgeFurnitureConfiguration.GENERATE_CHANDELIERS) {
			generateChandelierRecipes();
		}
	}

	private static void generateChandelierRecipes() {
		if (IronAgeFurnitureConfiguration.GENERATE_CANDLES) {
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockObjectHolder.chandelier_candle, 1),
				"x x", " y ", "x x", 'x', BlockObjectHolder.light_metal_ironage_candle_floor, 'y', Items.IRON_INGOT));
		}
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockObjectHolder.chandelier_torch, 1),
			"x x", " y ", "x x", 'x', Blocks.TORCH, 'y', Items.IRON_INGOT));
		if (IronAgeFurnitureConfiguration.GENERATE_GLOW_LAMPS) {
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BlockObjectHolder.chandelier_glowstone, 1),
				BlockObjectHolder.light_metal_ironage_block_floor_glow_clear, Items.IRON_INGOT));
		}
		if (IronAgeFurnitureConfiguration.GENERATE_LAVA_LAMPS) {
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BlockObjectHolder.chandelier_lava, 1),
				BlockObjectHolder.light_metal_ironage_block_floor_lava_clear, Items.IRON_INGOT));
		}
		if (IronAgeFurnitureConfiguration.GENERATE_REDSTONE_LAMPS) {
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BlockObjectHolder.chandelier_redstone, 1),
				BlockObjectHolder.light_metal_ironage_block_floor_red_clear, Items.IRON_INGOT));
		}
	}

	
	private static void generateChairRecipes() {
		addCfmConversionRecipes();
		addPlankBasedWoodFurnitureRecipes();
		addSuffixBasedWoodFurnitureRecipes();
		addLogBenchRecipes();
	}

	private static void addCfmConversionRecipes() {
		if (!IronAgeFurnitureConfiguration.CFM_CONVERSION_RECIPES || !Loader.isModLoaded("cfm")) {
			return;
		}

		addChairConversionRecipe("cfm:chair_oak", "chair_wood_ironage_classic_oak");
		addChairConversionRecipe("cfm:chair_spruce", "chair_wood_ironage_classic_spruce");
		addChairConversionRecipe("cfm:chair_birch", "chair_wood_ironage_classic_birch");
		addChairConversionRecipe("cfm:chair_jungle", "chair_wood_ironage_classic_jungle");
		addChairConversionRecipe("cfm:chair_acacia", "chair_wood_ironage_classic_acacia");
		addChairConversionRecipe("cfm:chair_big_oak", "chair_wood_ironage_classic_big_oak");
	}

	private static void addPlankBasedWoodFurnitureRecipes() {
		WoodVariantHelper.forEachEnabledPlankVariant(new WoodVariantHelper.ItemStackVariantConsumer() {
			@Override
			public void accept(String suffix, ItemStack planks) {
				if (IronAgeFurnitureConfiguration.GENERATE_CLASSIC_CHAIRS) {
					FurnitureFactory.AddClassicChairRecipe(planks, getBlockHolder("chair_wood_ironage_classic_" + suffix));
				}

				if (IronAgeFurnitureConfiguration.GENERATE_SHORT_STOOLS) {
					Block stool = getBlockHolder("chair_wood_ironage_stool_short_" + suffix);

					if (stool != null) {
						FurnitureFactory.AddShortStoolRecipe(planks, stool);
					}
				}

				if (IronAgeFurnitureConfiguration.GENERATE_TALL_STOOLS) {
					Block stool = getBlockHolder("chair_wood_ironage_stool_tall_" + suffix);

					if (stool != null) {
						FurnitureFactory.AddTallStoolRecipe(planks, stool);
					}
				}

				if (IronAgeFurnitureConfiguration.GENERATE_WOOD_BENCHES) {
					Block bench = getBlockHolder("chair_wood_ironage_bench_single_" + suffix);
					Block backBench = getBlockHolder("chair_wood_ironage_bench_back_single_" + suffix);

					if (bench != null) {
						FurnitureFactory.AddBenchRecipe(planks, bench);
					}
					if (backBench != null) {
						FurnitureFactory.AddBackBenchRecipe(planks, backBench);
					}
				}
			}
		});
	}

	private static void addSuffixBasedWoodFurnitureRecipes() {
		for (String suffix : WoodVariantHelper.getEnabledWoodSuffixes()) {
			Block classicChair = getBlockHolder("chair_wood_ironage_classic_" + suffix);

			if (IronAgeFurnitureConfiguration.GENERATE_SHIELD_CHAIRS) {
				Block shieldChair = getBlockHolder("chair_wood_ironage_shield_" + suffix);

				if (classicChair != null && shieldChair != null) {
					FurnitureFactory.AddShieldChairRecipe(classicChair, shieldChair);
				}
			}

			if (IronAgeFurnitureConfiguration.GENERATE_WOOD_BENCHES) {
				Block bench = getBlockHolder("chair_wood_ironage_bench_single_" + suffix);
				Block paddedBench = getBlockHolder("chair_wood_ironage_bench_padded_single_" + suffix);
				Block backBench = getBlockHolder("chair_wood_ironage_bench_back_single_" + suffix);
				Block paddedBackBench = getBlockHolder("chair_wood_ironage_bench_back_padded_single_" + suffix);

				if (bench != null && paddedBench != null) {
					FurnitureFactory.AddPaddedBenchRecipe(bench, paddedBench);
				}
				if (backBench != null && paddedBackBench != null) {
					FurnitureFactory.AddPaddedBenchRecipe(backBench, paddedBackBench);
				}
			}
		}
	}

	private static void addLogBenchRecipes() {
		if (!IronAgeFurnitureConfiguration.GENERATE_WOOD_BENCHES) {
			return;
		}

		WoodVariantHelper.forEachEnabledLogVariant(new WoodVariantHelper.ItemStackVariantConsumer() {
			@Override
			public void accept(String suffix, ItemStack log) {
				Block logBench = getBlockHolder("chair_wood_ironage_bench_log_single_" + suffix);

				if (logBench != null) {
					FurnitureFactory.AddLogBenchRecipe(log, logBench);
				}
			}
		});
	}

	private static void addChairConversionRecipe(String sourceName, String outputFieldName) {
		Block source = Block.getBlockFromName(sourceName);
		Block output = getBlockHolder(outputFieldName);

		if (source != null && output != null) {
			FurnitureFactory.AddChairConversionRecipe(source, output);
		}
	}

	private static Block getBlockHolder(String fieldName) {
		try {
			Field field = BlockObjectHolder.class.getField(fieldName);
			return (Block) field.get(null);
		}
		catch (NoSuchFieldException e) {
			throw new IllegalStateException("Missing BlockObjectHolder field for generated block " + fieldName, e);
		}
		catch (IllegalAccessException e) {
			throw new IllegalStateException("Unable to read BlockObjectHolder field for generated block " + fieldName, e);
		}
	}
}
