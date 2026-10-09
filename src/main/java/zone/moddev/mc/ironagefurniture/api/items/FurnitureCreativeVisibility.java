package zone.moddev.mc.ironagefurniture.api.items;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import zone.moddev.mc.ironagefurniture.IronAgeFurnitureConfiguration;

/** Applies loaded creative-visibility settings without removing saved registry entries. */
public final class FurnitureCreativeVisibility {
    private FurnitureCreativeVisibility() { }
    public static boolean isVisible(Item item) {
        if (item.getGroup() == null || item.getRegistryName() == null) return false;
        String path = item instanceof BlockItem ? ((BlockItem)item).getBlock().getRegistryName().getPath()
                : item.getRegistryName().getPath();
        IronAgeFurnitureConfiguration.Client config = IronAgeFurnitureConfiguration.CLIENT;
        if (path.contains("_biomesoplenty_") && !config.INTEGRATION_BIOMESOPLENTY.get()) return false;
        if (path.contains("_immersiveengineering_") && !config.INTEGRATION_IMMERSIVEENGINEERING.get()) return false;
        if (path.startsWith("chair_wood_ironage_bench_")) return config.GENERATE_BENCHES.get();
        if (path.startsWith("chair_wood_ironage_classic_")) return config.GENERATE_CLASSIC_CHAIRS.get();
        if (path.startsWith("chair_wood_ironage_shield_")) return config.GENERATE_SHIELD_CHAIRS.get();
        if (path.startsWith("chair_wood_ironage_stool_short_")) return config.GENERATE_SHORT_STOOLS.get();
        if (path.startsWith("chair_wood_ironage_stool_tall_")) return config.GENERATE_TALL_STOOLS.get();
        if (path.startsWith("chair_wood_ironage_wingback_")) return config.GENERATE_CLASSIC_CHAIRS.get() && config.GENERATE_WINGBACK_CHAIRS.get();
        if (path.startsWith("chair_wood_ironage_throne_")) return config.GENERATE_CLASSIC_CHAIRS.get() && config.GENERATE_WINGBACK_CHAIRS.get() && config.GENERATE_THRONES.get();
        if (path.startsWith("bed_wood_")) return config.GENERATE_WOOD_BEDS.get();
        if (path.startsWith("bed_canopy_")) return config.GENERATE_CANOPY_BEDS.get();
        return true;
    }
}
