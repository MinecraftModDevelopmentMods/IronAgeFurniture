package zone.moddev.mc.ironagefurniture.init;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredRegister;
import zone.moddev.mc.ironagefurniture.Ironagefurniture;
import zone.moddev.mc.ironagefurniture.ModItems;

/** Optional BWG furniture retains its historical core namespace. */
public final class PhaseFourBwgFurniture {
    public static final DeferredRegister<Block> REGISTER = DeferredRegister.create(
            BuiltInRegistries.BLOCK, Ironagefurniture.MODID);
    public static final String[] WOODS = {"aspen", "baobab", "blue_enchanted", "cika", "cypress", "ebony",
            "fir", "florus", "green_enchanted", "holly", "ironwood", "jacaranda", "mahogany", "maple",
            "palm", "pine", "rainbow_eucalyptus", "redwood", "sakura", "skyris", "spirit",
            "white_mangrove", "willow", "witch_hazel", "zelkova"};
    static {
        for (String wood : WOODS)
            PhaseFourFurniture.registerWood("biomeswevegone_" + wood, REGISTER, ModItems.REGISTER);
    }
    private PhaseFourBwgFurniture() { }
}
