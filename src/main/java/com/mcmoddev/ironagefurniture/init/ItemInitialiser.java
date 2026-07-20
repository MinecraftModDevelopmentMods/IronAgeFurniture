package com.mcmoddev.ironagefurniture.init;

import com.mcmoddev.ironagefurniture.Ironagefurniture;
import com.mcmoddev.ironagefurniture.IronAgeFurnitureConfiguration;
import com.mcmoddev.ironagefurniture.ItemObjectHolder;
import com.mcmoddev.ironagefurniture.api.Items.ItemFluidBottle;
import com.mcmoddev.ironagefurniture.api.Items.ItemDrinkware;
import com.mcmoddev.ironagefurniture.api.Items.ItemBlockGlassVase;
import com.mcmoddev.ironagefurniture.api.Items.ItemBlockMetalVariant;
import com.mcmoddev.ironagefurniture.api.Items.ItemBlockOrnament;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

public class ItemInitialiser {
	private static final int FLUID_BOTTLE_FLUID_TINT_INDEX = 0;
	private static final int DRINKWARE_MATERIAL_TINT_INDEX = 0;
	private static final int DRINKWARE_FLUID_TINT_INDEX = 1;
	private static final int DRINKWARE_GLASS_FILTERED_FLUID_TINT_INDEX = 2;
	private static final int DEFAULT_ITEM_TINT_COLOR = 0xFFFFFFFF;

	public static void init() {
		if (IronAgeFurnitureConfiguration.GENERATE_IRON_NUGGETS) {
			ItemObjectHolder.iron_nugget = RegisterItem(new Item().setCreativeTab(Ironagefurniture.ironagefurnitureTab), "iron_nugget");
			OreDictionary.registerOre("nuggetIron", new ItemStack(ItemObjectHolder.iron_nugget));
		}

		if (IronAgeFurnitureConfiguration.GENERATE_LIGHTS && IronAgeFurnitureConfiguration.GENERATE_CANDLES) {
			ItemObjectHolder.tallow = RegisterItem(new Item().setCreativeTab(Ironagefurniture.ironagefurnitureTab), "tallow");
		}

		if (IronAgeFurnitureConfiguration.GENERATE_FLUID_BOTTLES) {
			ItemObjectHolder.fluid_bottle = RegisterItem(new ItemFluidBottle(), "fluid_bottle");
		}

		if (IronAgeFurnitureConfiguration.GENERATE_DRINKWARE) {
			ItemObjectHolder.drinkware = RegisterItem(new ItemDrinkware()
				.setCreativeTab(Ironagefurniture.ironagefurnitureTab), "drinkware");
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
			if (i instanceof ItemFluidBottle) {
				registerFluidBottleMesher(i);
				registerFluidBottleColors(i);
			} else if (i instanceof ItemDrinkware) {
				registerDrinkwareMesher(i);
				registerDrinkwareColors(i);
			} else if (i instanceof ItemBlockOrnament) {
				ItemBlockOrnament ornament = (ItemBlockOrnament)i;

				for (int meta = 0; meta < ornament.getVariantCount(); meta++) {
					registerItemMesherModel(i, meta, ornament.getModelName(meta));
				}
			} else if (i instanceof ItemBlockGlassVase) {
				ItemBlockGlassVase vase = (ItemBlockGlassVase)i;

				for (int meta = 0; meta < vase.getVariantCount(); meta++) {
					registerItemMesherModel(i, meta, vase.getModelName(meta));
				}
			} else if (i instanceof ItemBlockMetalVariant) {
				ItemBlockMetalVariant metalItem = (ItemBlockMetalVariant)i;

				for (int index = 0; index < metalItem.getVariantCount(); index++) {
					int meta = metalItem.getVariantMeta(index);
					registerItemMesherModel(i, meta, metalItem.getModelName(meta));
				}
			} else {
				registerItemMesherModel(i, 0, name);
			}
    	}
    }

	public static void RegisterItemModels() {

		for(String name : Ironagefurniture.ItemRegistry.keySet()){
			Item i = Ironagefurniture.ItemRegistry.get(name);
			if (i instanceof ItemFluidBottle) {
				registerFluidBottleLoader(i);
			} else if (i instanceof ItemDrinkware) {
				registerDrinkwareLoader(i);
			} else if (i instanceof ItemBlockOrnament) {
				ItemBlockOrnament ornament = (ItemBlockOrnament)i;

				for (int meta = 0; meta < ornament.getVariantCount(); meta++) {
					registerItemLoaderModel(i, meta, ornament.getModelName(meta));
				}
			} else if (i instanceof ItemBlockGlassVase) {
				ItemBlockGlassVase vase = (ItemBlockGlassVase)i;

				for (int meta = 0; meta < vase.getVariantCount(); meta++) {
					registerItemLoaderModel(i, meta, vase.getModelName(meta));
				}
			} else if (i instanceof ItemBlockMetalVariant) {
				ItemBlockMetalVariant metalItem = (ItemBlockMetalVariant)i;

				for (int index = 0; index < metalItem.getVariantCount(); index++) {
					int meta = metalItem.getVariantMeta(index);
					registerItemLoaderModel(i, meta, metalItem.getModelName(meta));
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

	private static void registerFluidBottleMesher(Item item) {
		final ModelResourceLocation emptyModel = getModelResourceLocation("fluid_bottle");
		final ModelResourceLocation filledModel = getModelResourceLocation("fluid_bottle_filled");
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, new ItemMeshDefinition() {
			@Override
			public ModelResourceLocation getModelLocation(ItemStack stack) {
				return ItemFluidBottle.isFilled(stack) ? filledModel : emptyModel;
			}
		});
	}

	private static void registerFluidBottleLoader(Item item) {
		final ModelResourceLocation emptyModel = getModelResourceLocation("fluid_bottle");
		final ModelResourceLocation filledModel = getModelResourceLocation("fluid_bottle_filled");
		ModelBakery.registerItemVariants(item, new ResourceLocation(Ironagefurniture.MODID, "fluid_bottle"),
			new ResourceLocation(Ironagefurniture.MODID, "fluid_bottle_filled"));
		ModelLoader.setCustomMeshDefinition(item, new ItemMeshDefinition() {
			@Override
			public ModelResourceLocation getModelLocation(ItemStack stack) {
				return ItemFluidBottle.isFilled(stack) ? filledModel : emptyModel;
			}
		});
	}

	private static void registerFluidBottleColors(Item item) {
		Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new IItemColor() {
			@Override
			public int getColorFromItemstack(ItemStack stack, int tintIndex) {
				return tintIndex == FLUID_BOTTLE_FLUID_TINT_INDEX
					? ItemFluidBottle.getFluidColor(stack, DEFAULT_ITEM_TINT_COLOR) : DEFAULT_ITEM_TINT_COLOR;
			}
		}, item);
	}

	private static void registerDrinkwareMesher(Item item) {
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, new ItemMeshDefinition() {
			@Override
			public ModelResourceLocation getModelLocation(ItemStack stack) {
				return getModelResourceLocation(((ItemDrinkware)stack.getItem()).getModelName(stack));
			}
		});
	}

	private static void registerDrinkwareLoader(Item item) {
		ResourceLocation[] variants = new ResourceLocation[ItemDrinkware.getModelNames().size()];

		for (int i = 0; i < variants.length; i++) {
			variants[i] = new ResourceLocation(Ironagefurniture.MODID, ItemDrinkware.getModelNames().get(i));
		}

		ModelBakery.registerItemVariants(item, variants);
		ModelLoader.setCustomMeshDefinition(item, new ItemMeshDefinition() {
			@Override
			public ModelResourceLocation getModelLocation(ItemStack stack) {
				return getModelResourceLocation(((ItemDrinkware)stack.getItem()).getModelName(stack));
			}
		});
	}

	private static void registerDrinkwareColors(Item item) {
		Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new IItemColor() {
			@Override
			public int getColorFromItemstack(ItemStack stack, int tintIndex) {
				ItemDrinkware drinkware = (ItemDrinkware)stack.getItem();
				return tintIndex == DRINKWARE_MATERIAL_TINT_INDEX ? drinkware.getMaterialTint(stack)
					: tintIndex == DRINKWARE_FLUID_TINT_INDEX ? drinkware.getFluidTint(stack)
					: tintIndex == DRINKWARE_GLASS_FILTERED_FLUID_TINT_INDEX
						? drinkware.getGlassFilteredFluidTint(stack) : DEFAULT_ITEM_TINT_COLOR;
			}
		}, item);
	}
}
