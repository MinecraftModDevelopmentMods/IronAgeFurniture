package zone.moddev.mc.ironagefurniture.api;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;
import zone.moddev.mc.ironagefurniture.IronAgeFurnitureConfiguration;

/** Registry lookup keeps Mineralogy optional; no Mineralogy classes are linked. */
public final class MineralogyCompat {
    private static final ResourceLocation LAMP = new ResourceLocation("mineralogy", "rocksaltlamp");
    private MineralogyCompat() { }
    public static boolean isEnabled() {
        return IronAgeFurnitureConfiguration.CLIENT.INTEGRATION_MINERALOGY.get() && getRockSaltLamp() != null;
    }
    public static Block getRockSaltLamp() {
        if (!ModList.get().isLoaded("mineralogy")) return null;
        Block block = ForgeRegistries.BLOCKS.getValue(LAMP);
        return block == null || block == Blocks.AIR ? null : block;
    }
    public static boolean isRockSaltLamp(ItemStack stack) {
        Block block = getRockSaltLamp();
        return isEnabled() && block != null && stack.getItem() == block.asItem();
    }
}
