package zone.moddev.mc.ironagefurniture.init;

import java.util.Map;

import zone.moddev.mc.ironagefurniture.Ironagefurniture;
import zone.moddev.mc.ironagefurniture.api.Enumerations.PaddedBenchColour;
import zone.moddev.mc.ironagefurniture.api.Items.ItemBlockPaddedBench;
import zone.moddev.mc.ironagefurniture.api.PaddedBenchColourHelper;
import zone.moddev.mc.ironagefurniture.client.model.PaddedBenchModelLoader;

import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public final class ClientModelInitialiser {
	private static boolean registered;

	private ClientModelInitialiser() {
		throw new IllegalAccessError("This class cannot be instantiated");
	}

	public static void registerPaddedBenchModels() {
		if (registered) {
			return;
		}
		registered = true;
		ModelLoaderRegistry.registerLoader(PaddedBenchModelLoader.INSTANCE);
	}

	public static void registerPaddedBenchItemModels() {
		for (Map.Entry<String, Item> entry : Ironagefurniture.ItemRegistry.entrySet()) {
			if (!(entry.getValue() instanceof ItemBlockPaddedBench)) {
				continue;
			}
			registerPaddedBenchItemModel(Ironagefurniture.MODID, entry.getKey(), entry.getValue());
		}
	}

	/**
	 * Registers all colour variants for one padded-bench item. Add-ons should
	 * call this method from their client {@code ModelRegistryEvent} handler.
	 * Models remain in the add-on's own resource namespace.
	 *
	 * @param namespace add-on resource namespace
	 * @param itemName registry path and base block-model name
	 * @param item padded-bench block item
	 */
	public static void registerPaddedBenchItemModel(String namespace, final String itemName,
			final Item item) {
		if (!(item instanceof ItemBlockPaddedBench)) {
			throw new IllegalArgumentException("Padded bench item must use ItemBlockPaddedBench");
		}

		final String modelNamespace = new ResourceLocation(namespace, itemName).getNamespace();
		PaddedBenchModelLoader.registerNamespace(modelNamespace);
		ResourceLocation[] variants = new ResourceLocation[PaddedBenchColour.values().length];
		int index = 0;
		for (PaddedBenchColour colour : PaddedBenchColour.values()) {
			variants[index++] = itemModelLocation(modelNamespace, itemName, colour);
		}
		ModelBakery.registerItemVariants(item, variants);
		ModelLoader.setCustomMeshDefinition(item, new ItemMeshDefinition() {
			@Override
			public ModelResourceLocation getModelLocation(ItemStack stack) {
				return itemModelLocation(modelNamespace, itemName,
						PaddedBenchColourHelper.getColour(stack));
			}
		});
	}

	public static ModelResourceLocation itemModelLocation(String itemName,
			PaddedBenchColour colour) {
		return itemModelLocation(Ironagefurniture.MODID, itemName, colour);
	}

	/**
	 * Builds the coloured inventory-model location for a core or add-on item.
	 *
	 * @param namespace resource namespace which owns the model
	 * @param itemName base block-model name
	 * @param colour upholstery colour
	 * @return model location used by the item mesh definition
	 */
	public static ModelResourceLocation itemModelLocation(String namespace, String itemName,
			PaddedBenchColour colour) {
		PaddedBenchColour safeColour = colour == null ? PaddedBenchColour.RED : colour;
		return new ModelResourceLocation(namespace + ":padded/"
				+ safeColour.getSerializedName() + "/" + itemName, "inventory");
	}
}
