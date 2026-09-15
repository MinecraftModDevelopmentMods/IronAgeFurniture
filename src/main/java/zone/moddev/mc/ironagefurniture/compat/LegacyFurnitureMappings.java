package zone.moddev.mc.ironagefurniture.compat;

import java.util.Map;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.MissingMappingsEvent;
import zone.moddev.mc.ironagefurniture.Ironagefurniture;

/**
 * Remaps IronAgeFurniture's own retired optional-wood registry names. This does
 * not attempt to make a complete BYG world compatible with BWG.
 */
public final class LegacyFurnitureMappings {
    private static final Map<String, String> BYG_TO_BWG = Map.ofEntries(
            Map.entry("aspen", "aspen"),
            Map.entry("baobab", "baobab"),
            Map.entry("blue_enchanted", "blue_enchanted"),
            Map.entry("cherry", "sakura"),
            Map.entry("cika", "cika"),
            Map.entry("cypress", "cypress"),
            Map.entry("ebony", "ebony"),
            Map.entry("fir", "fir"),
            Map.entry("green_enchanted", "green_enchanted"),
            Map.entry("holly", "holly"),
            Map.entry("ironwood", "ironwood"),
            Map.entry("jacaranda", "jacaranda"),
            Map.entry("mahogany", "mahogany"),
            Map.entry("mangrove", "white_mangrove"),
            Map.entry("maple", "maple"),
            Map.entry("palm", "palm"),
            Map.entry("pine", "pine"),
            Map.entry("rainbow_eucalyptus", "rainbow_eucalyptus"),
            Map.entry("redwood", "redwood"),
            Map.entry("skyris", "skyris"),
            Map.entry("willow", "willow"),
            Map.entry("witch_hazel", "witch_hazel"),
            Map.entry("zelkova", "zelkova"));

    private LegacyFurnitureMappings() {
    }

    public static void registerRuntimeListener() {
        MissingMappingsEvent.BUS.addListener(LegacyFurnitureMappings::onMissingMappings);
    }

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
            T target = registry.getValue(Identifier.fromNamespaceAndPath(
                    Ironagefurniture.MODID, targetPath));
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
        int bygMarker = oldPath.lastIndexOf("_byg_");
        if (bygMarker < 0) {
            return null;
        }
        String oldWood = oldPath.substring(bygMarker + "_byg_".length());
        String newWood = BYG_TO_BWG.get(oldWood);
        return newWood == null ? null
                : oldPath.substring(0, bygMarker) + "_biomeswevegone_" + newWood;
    }
}
