package zone.moddev.mc.ironagefurniture.init;

import java.util.Map;

import zone.moddev.mc.ironagefurniture.Ironagefurniture;
import zone.moddev.mc.ironagefurniture.api.Enumerations.PaddedBenchColour;
import zone.moddev.mc.ironagefurniture.api.Items.ItemBlockPaddedBench;
import zone.moddev.mc.ironagefurniture.client.model.PaddedBenchModelLoader;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
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
			for (PaddedBenchColour colour : PaddedBenchColour.values()) {
				ModelLoader.setCustomModelResourceLocation(entry.getValue(), colour.getItemMetadata(),
						new ModelResourceLocation(Ironagefurniture.MODID + ":padded/"
								+ colour.getSerializedName() + "/" + entry.getKey(), "inventory"));
			}
		}
	}
}
