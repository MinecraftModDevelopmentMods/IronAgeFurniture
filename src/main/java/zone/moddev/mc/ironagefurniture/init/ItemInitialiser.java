package zone.moddev.mc.ironagefurniture.init;

import zone.moddev.mc.ironagefurniture.Ironagefurniture;
import zone.moddev.mc.ironagefurniture.IronAgeFurnitureConfiguration;
import zone.moddev.mc.ironagefurniture.ItemObjectHolder;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemInitialiser {
	public static void init() {
		if (IronAgeFurnitureConfiguration.GENERATE_LIGHTS && IronAgeFurnitureConfiguration.GENERATE_CANDLES) {
			ItemObjectHolder.tallow = RegisterItem(
				new Item().setCreativeTab(Ironagefurniture.ironagefurnitureTab), "tallow");
		}
	}
	public static Item RegisterItem(Item item, String name) {
		GameRegistry.register(item.setRegistryName(Ironagefurniture.MODID, name));
		Ironagefurniture.ItemRegistry.put(name, item);
		item.setUnlocalizedName(Ironagefurniture.MODID + "." + name);
		return item;
	}

}
