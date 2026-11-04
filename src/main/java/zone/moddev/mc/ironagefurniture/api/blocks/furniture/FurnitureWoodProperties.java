package zone.moddev.mc.ironagefurniture.api.blocks.furniture;

import net.minecraft.core.Registry;
import net.minecraft.world.level.block.Block;

/** Keep the fire resistance of nether woods without depending on a provider. */
final class FurnitureWoodProperties {
    private FurnitureWoodProperties() { }
    static boolean fireproof(Block block) {
        return isFireproof(Registry.BLOCK.getKey(block).getPath());
    }
    static boolean isFireproof(String path) {
        return path.endsWith("_crimson") || path.endsWith("_warped") || path.endsWith("_biomesoplenty_hellbark");
    }
}
