package zone.moddev.mc.ironagefurniture.migration;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
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
			if (!LegacyPaddedBenchIds.isLegacyNamespace(mapping.key.getNamespace())) {
				continue;
			}
			ResourceLocation targetId = LegacyPaddedBenchIds.isLegacyPaddedPath(mapping.key.getPath())
					? new ResourceLocation(Ironagefurniture.MODID,
							LegacyPaddedBenchIds.modernPaddedPath(mapping.key, "red"))
					: LegacyPaddedBenchIds.currentId(mapping.key);
			Item target = ForgeRegistries.ITEMS.getValue(targetId);
			if (target != null) {
				mapping.remap(target);
			} else {
				mapping.warn();
			}
		}
	}
}
