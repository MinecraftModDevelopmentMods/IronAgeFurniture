package zone.moddev.mc.ironagefurniture.client;

import zone.moddev.mc.ironagefurniture.Ironagefurniture;
import zone.moddev.mc.ironagefurniture.init.ClientModelInitialiser;

import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Registers item model mappings at the point required by Forge 1.12's model lifecycle.
 */
@Mod.EventBusSubscriber(modid = Ironagefurniture.MODID, value = Side.CLIENT)
public final class ClientRegistryEvents {
	private ClientRegistryEvents() {
		throw new IllegalAccessError("This class cannot be instantiated");
	}

	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event) {
		ClientModelInitialiser.registerPaddedBenchItemModels();
	}
}
