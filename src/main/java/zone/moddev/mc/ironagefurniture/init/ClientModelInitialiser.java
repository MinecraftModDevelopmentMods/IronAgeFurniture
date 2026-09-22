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
			final String itemName = entry.getKey();
			final Item item = entry.getValue();
			ResourceLocation[] variants = new ResourceLocation[PaddedBenchColour.values().length];
			int index = 0;
			for (PaddedBenchColour colour : PaddedBenchColour.values()) {
				variants[index++] = itemModelLocation(itemName, colour);
			}
			ModelBakery.registerItemVariants(item, variants);
			ModelLoader.setCustomMeshDefinition(item, new ItemMeshDefinition() {
				@Override
				public ModelResourceLocation getModelLocation(ItemStack stack) {
					return itemModelLocation(itemName, PaddedBenchColourHelper.getColour(stack));
				}
			});
		}
	}

	public static ModelResourceLocation itemModelLocation(String itemName,
			PaddedBenchColour colour) {
		PaddedBenchColour safeColour = colour == null ? PaddedBenchColour.RED : colour;
		return new ModelResourceLocation(Ironagefurniture.MODID + ":padded/"
				+ safeColour.getSerializedName() + "/" + itemName, "inventory");
	}
}
