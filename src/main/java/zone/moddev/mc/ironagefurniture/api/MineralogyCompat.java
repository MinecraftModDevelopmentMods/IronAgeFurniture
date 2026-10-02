package zone.moddev.mc.ironagefurniture.api;

import zone.moddev.mc.ironagefurniture.IronAgeFurnitureConfiguration;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.LoaderState;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

/** Looks up Mineralogy's lamp only when that mod is active. */
public final class MineralogyCompat {
    private static final String MODID = "mineralogy";
    private static final ResourceLocation ROCK_SALT_LAMP_ID = new ResourceLocation(MODID, "rocksaltlamp");

    private MineralogyCompat() { }

    public static boolean isEnabled() {
        return IronAgeFurnitureConfiguration.INTEGRATION_MINERALOGY
                && Loader.instance().hasReachedState(LoaderState.LOADING)
                && Loader.isModLoaded(MODID);
    }

    public static Block getRockSaltLampBlock() {
        if (!isEnabled()) return null;
        Block block = ForgeRegistries.BLOCKS.getValue(ROCK_SALT_LAMP_ID);
        return block == null || block == Blocks.AIR ? null : block;
    }

    public static boolean isRockSaltLampItem(ItemStack stack) {
        Block block = getRockSaltLampBlock();
        return stack != null && !stack.isEmpty() && block != null
                && stack.getItem() == Item.getItemFromBlock(block);
    }
}
