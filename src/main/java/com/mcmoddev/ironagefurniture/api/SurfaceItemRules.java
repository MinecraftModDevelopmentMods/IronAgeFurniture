package com.mcmoddev.ironagefurniture.api;

import java.util.Locale;

import com.mcmoddev.ironagefurniture.BlockObjectHolder;
import com.mcmoddev.ironagefurniture.api.Blocks.Barrel;
import com.mcmoddev.ironagefurniture.api.Blocks.Cabinet;
import com.mcmoddev.ironagefurniture.api.Blocks.Chair;
import com.mcmoddev.ironagefurniture.api.Blocks.DiningTable;
import com.mcmoddev.ironagefurniture.api.Blocks.GlassVaseBlock;
import com.mcmoddev.ironagefurniture.api.Blocks.LightSourceRed;
import com.mcmoddev.ironagefurniture.api.Blocks.MultiBlockBed;
import com.mcmoddev.ironagefurniture.api.Blocks.MultiBlockWoodBed;
import com.mcmoddev.ironagefurniture.api.Blocks.OrnamentBlock;
import com.mcmoddev.ironagefurniture.api.Blocks.WallShelf;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public final class SurfaceItemRules {
	private SurfaceItemRules() {
	}

	public static boolean shouldPlaceAsBlockOnTableLikeSurface(ItemStack heldItem) {
		if (isEmpty(heldItem)) {
			return false;
		}

		return isItemFromBlock(heldItem, BlockObjectHolder.light_metal_ironage_block_floor_glow_clear)
			|| isItemFromBlock(heldItem, BlockObjectHolder.light_metal_ironage_block_floor_red_clear)
			|| isItemFromBlock(heldItem, BlockObjectHolder.light_metal_ironage_block_floor_lava_clear)
			|| isItemFromBlock(heldItem, BlockObjectHolder.light_metal_ironage_candle_floor)
			|| MineralogyCompat.isRockSaltLampItem(heldItem)
			|| isFlowerPotItem(heldItem)
			|| isOrnamentItem(heldItem);
	}

	public static boolean shouldPlaceAsBlockOnCabinetSurface(ItemStack heldItem) {
		return shouldPlaceAsBlockOnTableLikeSurface(heldItem)
			|| isHopperItem(heldItem)
			|| isIronAgeStructuralFurnitureItem(heldItem);
	}

	public static boolean shouldPlaceAsBlockOnShelfLikeSurface(ItemStack heldItem) {
		if (isEmpty(heldItem)) {
			return false;
		}

		Block heldBlock = getHeldItemBlock(heldItem);
		return isItemFromBlock(heldItem, BlockObjectHolder.light_metal_ironage_block_floor_red_clear)
			|| hasRegistryPath(heldBlock, "light_metal_ironage_block_floor_red_clear")
			|| heldBlock instanceof LightSourceRed;
	}

	public static Block getHeldItemBlock(ItemStack heldItem) {
		return heldItem != null && heldItem.getItem() instanceof ItemBlock
			? ((ItemBlock)heldItem.getItem()).getBlock() : null;
	}

	public static boolean hasRegistryPath(Block block, String path) {
		return block != null && block.getRegistryName() != null
			&& path.equals(block.getRegistryName().getResourcePath());
	}

	public static boolean isItemFromBlock(ItemStack heldItem, Block block) {
		return block != null && heldItem != null && heldItem.getItem() == Item.getItemFromBlock(block);
	}

	public static boolean isFlowerPotItem(ItemStack heldItem) {
		return !isEmpty(heldItem) && heldItem.getItem() == Items.FLOWER_POT;
	}

	public static boolean isOrnamentItem(ItemStack heldItem) {
		Block block = getHeldItemBlock(heldItem);
		return block instanceof OrnamentBlock || block instanceof GlassVaseBlock;
	}

	public static boolean isHopperItem(ItemStack heldItem) {
		return getHeldItemBlock(heldItem) == Blocks.HOPPER;
	}

	public static boolean isIronAgeStructuralFurnitureItem(ItemStack heldItem) {
		Block block = getHeldItemBlock(heldItem);
		return block instanceof Cabinet
			|| block instanceof Barrel
			|| block instanceof DiningTable
			|| block instanceof WallShelf
			|| block instanceof Chair
			|| block instanceof MultiBlockBed
			|| block instanceof MultiBlockWoodBed;
	}

	public static boolean isMeal(ItemStack itemStack) {
		if (isEmpty(itemStack) || !(itemStack.getItem() instanceof ItemFood)) {
			return false;
		}

		ResourceLocation registryName = itemStack.getItem().getRegistryName();

		if (registryName == null || !"harvestcraft".equals(registryName.getResourceDomain())) {
			return true;
		}

		String path = registryName.getResourcePath().toLowerCase(Locale.ROOT);
		return !isHarvestCraftDrink(path)
			&& !isHarvestCraftJar(path)
			&& !isHarvestCraftKitchenware(path)
			&& !isHarvestCraftRawIngredient(path);
	}

	private static boolean isHarvestCraftDrink(String path) {
		return containsAny(path, "juice", "smoothie", "coffee", "soda", "cider", "milkshake",
			"hotchocolate", "eggnog", "lemonade", "lemonaide", "limeade", "freshmilk",
			"coconutmilk", "soymilk", "freshwater", "bubblywater", "energydrink", "fruitpunch",
			"ironbrew", "espresso", "chocolatemilk", "pinacolada")
			|| path.contains("teaitem")
			|| path.endsWith("syrupitem")
			|| equalsAny(path, "oliveoilitem", "sesameoilitem", "vinegaritem", "soysauceitem",
				"hotsauceitem", "hoisinsauceitem", "saladdressingitem", "sweetandsoursauceitem");
	}

	private static boolean isHarvestCraftJar(String path) {
		return path.endsWith("jellyitem")
			|| path.endsWith("chutneyitem")
			|| equalsAny(path, "almondbutteritem", "cashewbutteritem", "chestnutbutteritem",
				"peanutbutteritem", "pistachiobutteritem", "honeyitem", "royaljellyitem",
				"caramelitem", "mayoitem", "mustarditem", "nutellaitem", "vegemiteitem");
	}

	private static boolean isHarvestCraftKitchenware(String path) {
		return equalsAny(path, "bakewareitem", "cuttingboarditem", "juiceritem", "mixingbowlitem",
			"mortarpestleitem", "mortarandpestleitem", "potitem", "saucepanitem", "skilletitem");
	}

	private static boolean isHarvestCraftRawIngredient(String path) {
		return path.endsWith("seeditem")
			|| path.endsWith("seedsitem")
			|| containsAny(path, "flouritem", "doughitem", "saltitem", "sugaritem", "spiceitem",
				"powderitem", "stockitem", "coffeebeanitem", "coffeeseeditem");
	}

	private static boolean containsAny(String value, String... matches) {
		for (String match : matches) {
			if (value.contains(match)) {
				return true;
			}
		}

		return false;
	}

	private static boolean equalsAny(String value, String... matches) {
		for (String match : matches) {
			if (value.equals(match)) {
				return true;
			}
		}

		return false;
	}

	private static boolean isEmpty(ItemStack heldItem) {
		return heldItem == null || heldItem.stackSize <= 0;
	}
}
