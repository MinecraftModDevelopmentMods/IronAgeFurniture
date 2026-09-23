package zone.moddev.mc.ironagefurniture.migration;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import zone.moddev.mc.ironagefurniture.Ironagefurniture;

/** Keeps old shared padded-bench items loadable so their colour NBT can be migrated. */
@Mod.EventBusSubscriber(modid = Ironagefurniture.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class LegacyItemMappings {
	private LegacyItemMappings() {
	}

	@SubscribeEvent
	public static void remapMissingItems(RegistryEvent.MissingMappings<Item> event) {
		for (RegistryEvent.MissingMappings.Mapping<Item> mapping : event.getMappings()) {
			if (!Ironagefurniture.MODID.equals(mapping.key.getNamespace())
					|| !LegacyPaddedBenchIds.isLegacyPaddedPath(mapping.key.getPath())) {
				continue;
			}
			ResourceLocation targetId = new ResourceLocation(Ironagefurniture.MODID,
					LegacyPaddedBenchIds.modernPaddedPath(mapping.key.getPath(), "red"));
			Item target = ForgeRegistries.ITEMS.getValue(targetId);
			if (target != null) {
				mapping.remap(target);
			} else {
				mapping.warn();
			}
		}
	}
}
