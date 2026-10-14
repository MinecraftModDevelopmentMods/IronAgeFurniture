package zone.moddev.mc.ironagefurniture.api.blocks.furniture;

import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;

/** Nether-wood furniture keeps the fire resistance of its planks. */
final class FurnitureWoodProperties {
    private FurnitureWoodProperties() { }

    static boolean fireproof(Block block) {
        ResourceLocation id = block.getRegistryName();
        if (id == null) return false;
        for (String wood : zone.moddev.mc.ironagefurniture.init.resources.vanilla.VANILLA_NETHER_WOOD_TYPES)
            if (id.getPath().endsWith("_" + wood)) return true;
        return false;
    }
}
