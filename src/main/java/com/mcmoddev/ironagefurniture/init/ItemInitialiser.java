package com.mcmoddev.ironagefurniture.init;

import com.mcmoddev.ironagefurniture.Ironagefurniture;
import com.mcmoddev.ironagefurniture.IronAgeFurnitureConfiguration;
import com.mcmoddev.ironagefurniture.ItemObjectHolder;
import com.mcmoddev.ironagefurniture.api.Items.ItemBlockGlassVase;
import com.mcmoddev.ironagefurniture.api.Items.ItemBlockOrnament;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

public class ItemInitialiser {
	public static void init() {
		if (IronAgeFurnitureConfiguration.GENERATE_IRON_NUGGETS) {
			ItemObjectHolder.iron_nugget = RegisterItem(new Item().setCreativeTab(Ironagefurniture.ironagefurnitureTab), "iron_nugget");
			OreDictionary.registerOre("nuggetIron", new ItemStack(ItemObjectHolder.iron_nugget));
		}

		if (IronAgeFurnitureConfiguration.GENERATE_LIGHTS && IronAgeFurnitureConfiguration.GENERATE_CANDLES) {
			ItemObjectHolder.tallow = RegisterItem(new Item().setCreativeTab(Ironagefurniture.ironagefurnitureTab), "tallow");
		}
	}

	public static Item RegisterItem(Item item, String name) {
		GameRegistry.register(item.setRegistryName(Ironagefurniture.MODID, name));
		Ironagefurniture.ItemRegistry.put(name, item);
		item.setUnlocalizedName(Ironagefurniture.MODID + "." + name);
		return item;
	}
	
	public static void RegisterItemRenders() {

		for(String name : Ironagefurniture.ItemRegistry.keySet()){
			Item i = Ironagefurniture.ItemRegistry.get(name);
			if (i instanceof ItemBlockOrnament) {
				ItemBlockOrnament ornament = (ItemBlockOrnament)i;

				for (int meta = 0; meta < ornament.getVariantCount(); meta++) {
					registerItemMesherModel(i, meta, ornament.getModelName(meta));
				}
			} else if (i instanceof ItemBlockGlassVase) {
				ItemBlockGlassVase vase = (ItemBlockGlassVase)i;

				for (int meta = 0; meta < vase.getVariantCount(); meta++) {
					registerItemMesherModel(i, meta, vase.getModelName(meta));
				}
			} else {
				registerItemMesherModel(i, 0, name);
			}
    	}
    }

	public static void RegisterItemModels() {

		for(String name : Ironagefurniture.ItemRegistry.keySet()){
			Item i = Ironagefurniture.ItemRegistry.get(name);
			if (i instanceof ItemBlockOrnament) {
				ItemBlockOrnament ornament = (ItemBlockOrnament)i;

				for (int meta = 0; meta < ornament.getVariantCount(); meta++) {
					registerItemLoaderModel(i, meta, ornament.getModelName(meta));
				}
			} else if (i instanceof ItemBlockGlassVase) {
				ItemBlockGlassVase vase = (ItemBlockGlassVase)i;

				for (int meta = 0; meta < vase.getVariantCount(); meta++) {
					registerItemLoaderModel(i, meta, vase.getModelName(meta));
				}
			} else {
				registerItemLoaderModel(i, 0, name);
			}
		}
	}

	private static void registerItemMesherModel(Item item, int meta, String modelName) {
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
				.register(item, meta, getModelResourceLocation(modelName));
	}

	private static void registerItemLoaderModel(Item item, int meta, String modelName) {
		ModelLoader.setCustomModelResourceLocation(item, meta, getModelResourceLocation(modelName));
	}

	private static ModelResourceLocation getModelResourceLocation(String modelName) {
		return new ModelResourceLocation(Ironagefurniture.MODID + ":" + modelName, "inventory");
	}
}
