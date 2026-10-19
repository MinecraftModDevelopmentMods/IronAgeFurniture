package zone.moddev.mc.ironagefurniture.init;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredRegister;
import zone.moddev.mc.ironagefurniture.Ironagefurniture;
import zone.moddev.mc.ironagefurniture.ModItems;

/** The 26.1.2 branch keeps its optional BOP furniture in the core namespace. */
public final class PhaseFourBopFurniture {
    public static final DeferredRegister<Block> REGISTER = DeferredRegister.create(
            BuiltInRegistries.BLOCK, Ironagefurniture.MODID);
    public static final String[] WOODS = {"dead", "empyreal", "fir", "hellbark", "jacaranda", "magic",
            "mahogany", "maple", "origin_oak", "palm", "pine", "redwood", "umbran", "willow"};

    static {
        for (String wood : WOODS) {
            PhaseFourFurniture.registerWood("biomesoplenty_" + wood, REGISTER, ModItems.REGISTER);
        }
    }

    private PhaseFourBopFurniture() { }
}
