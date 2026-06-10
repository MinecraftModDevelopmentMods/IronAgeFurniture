package com.mcmoddev.ironagefurniture.init;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.mcmoddev.ironagefurniture.BlockObjectHolder;
import com.mcmoddev.ironagefurniture.IronAgeFurnitureConfiguration;
import com.mcmoddev.ironagefurniture.ItemObjectHolder;
import com.mcmoddev.ironagefurniture.api.FurnitureFactory;
import com.mcmoddev.ironagefurniture.api.MetalVariantHelper;
import com.mcmoddev.ironagefurniture.api.MetalVariantHelper.MetalVariant;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
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
		generateIronNuggetRecipes();
		generateChairRecipes();
		generateBedRecipes();
		generateTableRecipes();
		generateCabinetRecipes();
		generateShelfRecipes();
		generateGoldBarsRecipes();
		generateLightRecipes();
		generateOrnamentRecipes();
	}

	private static void generateIronNuggetRecipes() {
		if (!IronAgeFurnitureConfiguration.GENERATE_IRON_NUGGETS || ItemObjectHolder.iron_nugget == null) {
			return;
		}

		if (isBaseMetalsLoaded()) {
			return;
		}

		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ItemObjectHolder.iron_nugget, 9), "ingotIron"));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Items.IRON_INGOT, 1),
			"xxx", "xxx", "xxx", 'x', "nuggetIron"));
	}

	private static boolean isBaseMetalsLoaded() {
		return Loader.isModLoaded("basemetals");
	}

	private static void generateGoldBarsRecipes() {
		if (BlockObjectHolder.gold_bars == null) {
			return;
		}

		OreDictionary.registerOre("barsGold", new ItemStack(BlockObjectHolder.gold_bars));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockObjectHolder.gold_bars, 16),
			"xxx", "xxx", 'x', "ingotGold"));
	}

	private static void generateOrnamentRecipes() {
		if (!IronAgeFurnitureConfiguration.GENERATE_ORNAMENTS) {
			return;
		}

		Object ironSmall = hasIronNuggets() ? "nuggetIron" : "ingotIron";

		if (BlockObjectHolder.ornament_clay != null) {
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockObjectHolder.ornament_clay, 1, 0),
				" x ", "x x", " x ", 'x', Items.CLAY_BALL));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockObjectHolder.ornament_clay, 1, 1),
				"x  ", "xxx", " x ", 'x', Items.CLAY_BALL));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockObjectHolder.ornament_clay, 2, 2),
				"x x", " x ", 'x', Items.CLAY_BALL));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockObjectHolder.ornament_clay, 1, 3),
				" z ", "yxy", " y ", 'x', Blocks.HARDENED_CLAY, 'y', Items.CLAY_BALL, 'z', ironSmall));
		}

		if (BlockObjectHolder.ornament_obsidian != null && BlockObjectHolder.obsidian_chunk != null) {
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BlockObjectHolder.ornament_obsidian, 1, 0),
				BlockObjectHolder.obsidian_chunk, BlockObjectHolder.obsidian_chunk, BlockObjectHolder.obsidian_chunk));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockObjectHolder.ornament_obsidian, 1, 1),
				" x ", "xyx", " x ", 'x', BlockObjectHolder.obsidian_chunk, 'y', ironSmall));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockObjectHolder.ornament_obsidian, 1, 2),
				" x ", "x x", "xxx", 'x', BlockObjectHolder.obsidian_chunk));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockObjectHolder.ornament_obsidian, 1, 3),
				"x x", " y ", " x ", 'x', BlockObjectHolder.obsidian_chunk, 'y', ironSmall));
		}

		if (BlockObjectHolder.ornament_glass_vase != null) {
			for (EnumDyeColor color : EnumDyeColor.values()) {
				GameRegistry.addRecipe(new ShapedOreRecipe(
					new ItemStack(BlockObjectHolder.ornament_glass_vase, 4, color.getMetadata()),
					" x ", "x x", " x ", 'x', new ItemStack(Blocks.STAINED_GLASS, 1, color.getMetadata())));
			}
		}
	}

	private static boolean hasIronNuggets() {
		return net.minecraftforge.oredict.OreDictionary.doesOreNameExist("nuggetIron")
			&& !net.minecraftforge.oredict.OreDictionary.getOres("nuggetIron").isEmpty();
	}

	private static void generateShelfRecipes() {
		if (!IronAgeFurnitureConfiguration.GENERATE_WALL_SHELVES) {
			return;
		}

		final List<String> recipeSuffixes = new ArrayList<String>();

		WoodVariantHelper.forEachEnabledSlabVariant(new WoodVariantHelper.ItemStackVariantConsumer() {
			@Override
			public void accept(String suffix, ItemStack slab) {
				Block shelf = BlockObjectHolder.shelf_wall.get(suffix);

				if (shelf != null) {
					FurnitureFactory.AddWallShelfRecipe(slab, shelf);
					recipeSuffixes.add(suffix);
				}
			}
		});

		List<String> missing = new ArrayList<String>(BlockObjectHolder.shelf_wall.keySet());
		missing.removeAll(recipeSuffixes);

		if (!missing.isEmpty()) {
			Collections.sort(missing);
			throw new IllegalStateException("Missing wall shelf slab recipe mappings: " + missing);
		}
	}

	private static void generateTableRecipes() {
		if (!IronAgeFurnitureConfiguration.GENERATE_DINING_TABLES
				&& !IronAgeFurnitureConfiguration.GENERATE_LOW_TABLES) {
			return;
		}

		final List<String> diningRecipeSuffixes = new ArrayList<String>();
		final List<String> lowRecipeSuffixes = new ArrayList<String>();

		WoodVariantHelper.forEachEnabledSlabVariant(new WoodVariantHelper.ItemStackVariantConsumer() {
			@Override
			public void accept(String suffix, ItemStack slab) {
				if (IronAgeFurnitureConfiguration.GENERATE_DINING_TABLES) {
					Block table = BlockObjectHolder.table_dining.get(suffix);

					if (table != null) {
						FurnitureFactory.AddDiningTableRecipe(slab, table);
						diningRecipeSuffixes.add(suffix);
					}
				}
				if (IronAgeFurnitureConfiguration.GENERATE_LOW_TABLES) {
					Block table = BlockObjectHolder.table_low.get(suffix);

					if (table != null) {
						FurnitureFactory.AddLowTableRecipe(slab, table);
						lowRecipeSuffixes.add(suffix);
					}
				}
			}
		});

		validateSlabRecipeMappings(BlockObjectHolder.table_dining, diningRecipeSuffixes, "dining table");
		validateSlabRecipeMappings(BlockObjectHolder.table_low, lowRecipeSuffixes, "low table");
	}

	private static void generateCabinetRecipes() {
		if (!IronAgeFurnitureConfiguration.GENERATE_WOOD_CABINETS) {
			return;
		}

		final List<String> recipeSuffixes = new ArrayList<String>();

		WoodVariantHelper.forEachEnabledPlankVariant(new WoodVariantHelper.ItemStackVariantConsumer() {
			@Override
			public void accept(String suffix, ItemStack planks) {
				Block cabinet = BlockObjectHolder.cabinet_wood_ironage.get(suffix);

				if (cabinet != null) {
					FurnitureFactory.AddCabinetRecipe(planks, cabinet);
					recipeSuffixes.add(suffix);
				}
			}
		});

		validateSlabRecipeMappings(BlockObjectHolder.cabinet_wood_ironage, recipeSuffixes, "wood cabinet");
	}

	private static void validateSlabRecipeMappings(java.util.Map<String, Block> blocks, List<String> recipeSuffixes,
			String familyName) {
		List<String> missing = new ArrayList<String>(blocks.keySet());
		missing.removeAll(recipeSuffixes);

		if (!missing.isEmpty()) {
			Collections.sort(missing);
			throw new IllegalStateException("Missing " + familyName + " slab recipe mappings: " + missing);
		}
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
			generateSconceRecipes();
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
			generateChainRecipes();
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
		for (MetalVariant metal : MetalVariantHelper.getAvailableVariants()) {
			String ingot = metal.getIngotOreName();

			if (IronAgeFurnitureConfiguration.GENERATE_CANDLES) {
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockObjectHolder.chandelier_candle, 1,
					metal.getMeta()), "x x", " y ", "x x", 'x', BlockObjectHolder.light_metal_ironage_candle_floor,
					'y', ingot));
			}
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockObjectHolder.chandelier_torch, 1,
				metal.getMeta()), "x x", " y ", "x x", 'x', Blocks.TORCH, 'y', ingot));
			if (IronAgeFurnitureConfiguration.GENERATE_GLOW_LAMPS) {
				GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BlockObjectHolder.chandelier_glowstone, 1,
					metal.getMeta()), BlockObjectHolder.light_metal_ironage_block_floor_glow_clear, ingot));
			}
			if (IronAgeFurnitureConfiguration.GENERATE_LAVA_LAMPS) {
				GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BlockObjectHolder.chandelier_lava, 1,
					metal.getMeta()), BlockObjectHolder.light_metal_ironage_block_floor_lava_clear, ingot));
			}
			if (IronAgeFurnitureConfiguration.GENERATE_REDSTONE_LAMPS) {
				GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BlockObjectHolder.chandelier_redstone, 1,
					metal.getMeta()), BlockObjectHolder.light_metal_ironage_block_floor_red_clear, ingot));
			}
			if (IronAgeFurnitureConfiguration.GENERATE_GRAND_CHANDELIERS
					&& IronAgeFurnitureConfiguration.GENERATE_SCONCES
					&& BlockObjectHolder.chandelier_grand_hub != null) {
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockObjectHolder.chandelier_grand_hub, 1,
					metal.getMeta()), " x ", "xyx", " x ",
					'x', new ItemStack(BlockObjectHolder.light_metal_ironage_sconce_floor_empty_iron, 1,
						metal.getMeta()),
					'y', ingot));
			}
		}
	}

	private static void generateSconceRecipes() {
		for (MetalVariant metal : MetalVariantHelper.getAvailableVariants()) {
			Object nugget = getNuggetIngredient(metal);

			if (nugget != null) {
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(
					BlockObjectHolder.light_metal_ironage_sconce_floor_empty_iron, 4, metal.getMeta()),
						"xxx", "x  ", "x  ", 'x', nugget));
			}
		}
	}

	private static Object getNuggetIngredient(MetalVariant metal) {
		if (metal == MetalVariant.GOLD) {
			return Items.GOLD_NUGGET;
		}
		if (hasOre(metal.getNuggetOreName())) {
			return metal.getNuggetOreName();
		}
		return null;
	}

	private static void generateChainRecipes() {
		for (MetalVariant metal : MetalVariantHelper.getAvailableVariants()) {
			Object input;

			if (hasOre(metal.getBarsOreName()) || expectsExternalBarsProvider(metal)) {
				input = metal.getBarsOreName();
			} else if (metal == MetalVariant.IRON) {
				input = Blocks.IRON_BARS;
			} else {
				continue;
			}

			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BlockObjectHolder.chain_top, 3,
				metal.getMeta()), input));
		}
	}

	private static boolean expectsExternalBarsProvider(MetalVariant metal) {
		if (metal == MetalVariant.IRON) {
			return false;
		}
		if (metal == MetalVariant.GOLD) {
			return BlockObjectHolder.gold_bars != null || isBaseMetalsLoaded();
		}
		return isBaseMetalsLoaded();
	}

	private static boolean hasOre(String oreName) {
		return OreDictionary.doesOreNameExist(oreName) && !OreDictionary.getOres(oreName).isEmpty();
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

				if (IronAgeFurnitureConfiguration.GENERATE_DINING_CHAIRS) {
					Block diningChair = BlockObjectHolder.chair_wood_ironage_dining.get(suffix);

					if (diningChair != null) {
						FurnitureFactory.AddDiningChairRecipe(planks, diningChair);
					}
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
