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
import zone.moddev.mc.ironagefurniture.api.Blocks.Stool;
import zone.moddev.mc.ironagefurniture.api.Items.ItemBlockThrowableLavaLamp;
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
		block.setTranslationKey(Ironagefurniture.MODID + "." + name);
		block.setRegistryName(name);
		block.setCreativeTab(Ironagefurniture.ironagefurnitureTab);
		if (registerItem) {
			ItemBlock itemBlock = block instanceof LightSourceLava
					? new ItemBlockThrowableLavaLamp(block) : new ItemBlock(block);
			RegisterItem(itemBlock, name, maxStackSize);
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

	public static Item RegisterItem(Item item, String name, int maxStackSize) {
		String itemName = Ironagefurniture.MODID + "." + name;

		item.setTranslationKey(itemName);
		item.setRegistryName(name);
		item.setMaxStackSize(maxStackSize);

		Ironagefurniture.ItemRegistry.put(name, item);

		return item;
	}
}
