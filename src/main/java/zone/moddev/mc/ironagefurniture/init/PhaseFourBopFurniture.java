package zone.moddev.mc.ironagefurniture.init;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredRegister;
import zone.moddev.mc.ironagefurniture.Ironagefurniture;
import zone.moddev.mc.ironagefurniture.ModItems;

/** Optional BOP furniture retains its historical core namespace. */
public final class PhaseFourBopFurniture {
    public static final DeferredRegister<Block> REGISTER = DeferredRegister.create(
            BuiltInRegistries.BLOCK, Ironagefurniture.MODID);
    public static final String[] WOODS = {"dead", "empyreal", "fir", "hellbark", "jacaranda", "magic",
            "mahogany", "maple", "palm", "pine", "redwood", "umbran", "willow"};
    static {
        for (String wood : WOODS)
            PhaseFourFurniture.registerWood("biomesoplenty_" + wood, REGISTER, ModItems.REGISTER);
    }
    private PhaseFourBopFurniture() { }
}
