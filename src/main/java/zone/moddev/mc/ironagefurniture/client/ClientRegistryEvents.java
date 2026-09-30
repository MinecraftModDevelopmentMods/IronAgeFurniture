package zone.moddev.mc.ironagefurniture.client;

import zone.moddev.mc.ironagefurniture.Ironagefurniture;
import zone.moddev.mc.ironagefurniture.init.ClientModelInitialiser;
import zone.moddev.mc.ironagefurniture.api.Items.ItemBlockMetalVariant;

import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
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
		ClientModelInitialiser.registerUpholsteryItemModels();
		for (Item item : Ironagefurniture.ItemRegistry.values()) {
			if (item instanceof ItemBlockMetalVariant) {
				ItemBlockMetalVariant metal = (ItemBlockMetalVariant)item;
				for (int index = 0; index < metal.getVariantCount(); index++) {
					int meta = metal.getVariantMeta(index);
					ModelLoader.setCustomModelResourceLocation(item, meta,
							new ModelResourceLocation(Ironagefurniture.MODID + ":" + metal.getModelName(meta), "inventory"));
				}
			}
		}
	}
}
