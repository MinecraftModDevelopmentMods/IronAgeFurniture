package zone.moddev.mc.ironagefurniture.init;

import zone.moddev.mc.ironagefurniture.client.model.PaddedBenchModelLoader;

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
}
