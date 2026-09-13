package zone.moddev.mc.ironagefurniture.compat;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.MissingMappingsEvent;
import zone.moddev.mc.ironagefurniture.Ironagefurniture;

/**
 * Remaps IronAgeFurniture's retired Biomes O' Plenty cherry furniture to the
 * equivalent vanilla-cherry entries.
 */
@Mod.EventBusSubscriber(modid = Ironagefurniture.MODID)
public final class LegacyFurnitureMappings {
    private LegacyFurnitureMappings() {
    }

    @SubscribeEvent
    public static void onMissingMappings(MissingMappingsEvent event) {
        remap(event, ForgeRegistries.Keys.BLOCKS, ForgeRegistries.BLOCKS);
        remap(event, ForgeRegistries.Keys.ITEMS, ForgeRegistries.ITEMS);
    }

    private static <T> void remap(MissingMappingsEvent event,
            ResourceKey<Registry<T>> registryKey, IForgeRegistry<T> registry) {
        for (MissingMappingsEvent.Mapping<T> mapping
                : event.getMappings(registryKey, Ironagefurniture.MODID)) {
            String targetPath = targetPath(mapping.getKey().getPath());
            if (targetPath == null) {
                continue;
            }
            T target = registry.getValue(new ResourceLocation(Ironagefurniture.MODID, targetPath));
            if (target != null) {
                mapping.remap(target);
            }
        }
    }

    static String targetPath(String oldPath) {
        String bopCherry = "_biomesoplenty_cherry";
        if (oldPath.endsWith(bopCherry)) {
            return oldPath.substring(0, oldPath.length() - bopCherry.length()) + "_cherry";
        }
        return null;
    }
}
