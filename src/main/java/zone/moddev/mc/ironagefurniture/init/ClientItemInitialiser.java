package zone.moddev.mc.ironagefurniture.init;

import zone.moddev.mc.ironagefurniture.Ironagefurniture;
import zone.moddev.mc.ironagefurniture.api.Items.ItemBlockPaddedBench;
import zone.moddev.mc.ironagefurniture.api.Items.ItemBlockMetalVariant;
import zone.moddev.mc.ironagefurniture.api.Items.ItemBlockUpholsteredFurniture;
import zone.moddev.mc.ironagefurniture.api.Blocks.ShieldChair;
import zone.moddev.mc.ironagefurniture.api.ShieldChairItemData;
import zone.moddev.mc.ironagefurniture.api.Enumerations.UpholsteryColour;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/** Client-only item model registration for legacy Forge. */
@SideOnly(Side.CLIENT)
public final class ClientItemInitialiser {

    private ClientItemInitialiser() {
        throw new IllegalAccessError("This class cannot be instantiated");
    }

    public static void registerItemRenders() {
        for (String name : Ironagefurniture.ItemRegistry.keySet()) {
            Item item = Ironagefurniture.ItemRegistry.get(name);
            if (item instanceof ItemBlockPaddedBench) {
                // The colour-aware loader supplies these models. A generic
                // registration here would replace every colour with one item model.
                continue;
            }
			if (item instanceof ItemBlockUpholsteredFurniture) {
				for (UpholsteryColour colour : UpholsteryColour.values()) {
					Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item,
						colour.getItemMetadata(), model("upholstered/" + colour.getSerializedName() + "/" + name));
				}
				continue;
			}
			if (item instanceof ItemBlockMetalVariant) {
				ItemBlockMetalVariant metal = (ItemBlockMetalVariant)item;
				for (int i = 0; i < metal.getVariantCount(); i++) {
					int meta = metal.getVariantMeta(i);
					Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, meta, model(metal.getModelName(meta)));
				}
				continue;
			}
			if (isShieldChair(item)) {
				Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, shieldMesh(name));
				continue;
			}
            ModelResourceLocation model = new ModelResourceLocation(
                    Ironagefurniture.MODID + ":" + name, "inventory");
            Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, 0, model);
        }
    }

	public static void registerPhaseFourItemModels() {
		for (String name : Ironagefurniture.ItemRegistry.keySet()) {
			Item item = Ironagefurniture.ItemRegistry.get(name);
			if (item instanceof ItemBlockMetalVariant) {
				ItemBlockMetalVariant metal = (ItemBlockMetalVariant)item;
				for (int i = 0; i < metal.getVariantCount(); i++) {
					int meta = metal.getVariantMeta(i);
					ModelLoader.setCustomModelResourceLocation(item, meta, model(metal.getModelName(meta)));
				}
			} else if (isShieldChair(item)) {
				ModelBakery.registerItemVariants(item,
					new ResourceLocation(Ironagefurniture.MODID, name),
					new ResourceLocation(Ironagefurniture.MODID, "shield_chair_filled/" + name));
				ModelLoader.setCustomMeshDefinition(item, shieldMesh(name));
			}
		}
	}

	private static boolean isShieldChair(Item item) {
		return item instanceof ItemBlock && ((ItemBlock)item).getBlock() instanceof ShieldChair;
	}

	private static ItemMeshDefinition shieldMesh(final String name) {
		final ModelResourceLocation frame = model(name);
		final ModelResourceLocation filled = model("shield_chair_filled/" + name);
		return new ItemMeshDefinition() {
			@Override public ModelResourceLocation getModelLocation(ItemStack stack) {
				return ShieldChairItemData.isEmptyFrame(stack) ? frame : filled;
			}
		};
	}

	private static ModelResourceLocation model(String name) {
		return new ModelResourceLocation(Ironagefurniture.MODID + ":" + name, "inventory");
	}
}
