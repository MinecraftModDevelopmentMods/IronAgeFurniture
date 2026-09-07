package com.mcmoddev.ironagefurniture.init;

import com.mcmoddev.ironagefurniture.Ironagefurniture;
import com.mcmoddev.ironagefurniture.api.Items.ItemBlockGlassVase;
import com.mcmoddev.ironagefurniture.api.Items.ItemBlockMetalVariant;
import com.mcmoddev.ironagefurniture.api.Items.ItemBlockOrnament;
import com.mcmoddev.ironagefurniture.api.Items.ItemDrinkware;
import com.mcmoddev.ironagefurniture.api.Items.ItemFluidBottle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public final class ClientItemInitialiser {
    private static final int FLUID_BOTTLE_FLUID_TINT_INDEX = 0;
    private static final int DRINKWARE_MATERIAL_TINT_INDEX = 0;
    private static final int DRINKWARE_FLUID_TINT_INDEX = 1;
    private static final int DRINKWARE_GLASS_FILTERED_FLUID_TINT_INDEX = 2;
    private static final int DEFAULT_ITEM_TINT_COLOR = 0xFFFFFFFF;

    private ClientItemInitialiser() {
    }

    public static void registerItemRenders() {
        for (String name : Ironagefurniture.ItemRegistry.keySet()) {
            Item item = Ironagefurniture.ItemRegistry.get(name);
            if (item instanceof ItemFluidBottle) {
                registerFluidBottleMesher(item);
                registerFluidBottleColors(item);
            } else if (item instanceof ItemDrinkware) {
                registerDrinkwareMesher(item);
                registerDrinkwareColors(item);
            } else if (item instanceof ItemBlockOrnament) {
                ItemBlockOrnament ornament = (ItemBlockOrnament)item;
                for (int meta = 0; meta < ornament.getVariantCount(); meta++) {
                    registerItemMesherModel(item, meta, ornament.getModelName(meta));
                }
            } else if (item instanceof ItemBlockGlassVase) {
                ItemBlockGlassVase vase = (ItemBlockGlassVase)item;
                for (int meta = 0; meta < vase.getVariantCount(); meta++) {
                    registerItemMesherModel(item, meta, vase.getModelName(meta));
                }
            } else if (item instanceof ItemBlockMetalVariant) {
                ItemBlockMetalVariant metalItem = (ItemBlockMetalVariant)item;
                for (int index = 0; index < metalItem.getVariantCount(); index++) {
                    int meta = metalItem.getVariantMeta(index);
                    registerItemMesherModel(item, meta, metalItem.getModelName(meta));
                }
            } else {
                registerItemMesherModel(item, 0, name);
            }
        }
    }

    public static void registerItemModels() {
        for (String name : Ironagefurniture.ItemRegistry.keySet()) {
            Item item = Ironagefurniture.ItemRegistry.get(name);
            if (item instanceof ItemFluidBottle) {
                registerFluidBottleLoader(item);
            } else if (item instanceof ItemDrinkware) {
                registerDrinkwareLoader(item);
            } else if (item instanceof ItemBlockOrnament) {
                ItemBlockOrnament ornament = (ItemBlockOrnament)item;
                for (int meta = 0; meta < ornament.getVariantCount(); meta++) {
                    registerItemLoaderModel(item, meta, ornament.getModelName(meta));
                }
            } else if (item instanceof ItemBlockGlassVase) {
                ItemBlockGlassVase vase = (ItemBlockGlassVase)item;
                for (int meta = 0; meta < vase.getVariantCount(); meta++) {
                    registerItemLoaderModel(item, meta, vase.getModelName(meta));
                }
            } else if (item instanceof ItemBlockMetalVariant) {
                ItemBlockMetalVariant metalItem = (ItemBlockMetalVariant)item;
                for (int index = 0; index < metalItem.getVariantCount(); index++) {
                    int meta = metalItem.getVariantMeta(index);
                    registerItemLoaderModel(item, meta, metalItem.getModelName(meta));
                }
            } else {
                registerItemLoaderModel(item, 0, name);
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
                        ? ItemFluidBottle.getFluidColor(stack, DEFAULT_ITEM_TINT_COLOR)
                        : DEFAULT_ITEM_TINT_COLOR;
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
        for (int index = 0; index < variants.length; index++) {
            variants[index] = new ResourceLocation(
                    Ironagefurniture.MODID, ItemDrinkware.getModelNames().get(index));
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
