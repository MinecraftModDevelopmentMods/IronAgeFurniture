package zone.moddev.mc.ironagefurniture.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import zone.moddev.mc.ironagefurniture.Ironagefurniture;
import zone.moddev.mc.ironagefurniture.ModItems;

/** Loaded only with BOP; use the same furniture and persistent wood suffixes. */
public final class PhaseFourBopFurniture {
    public static final DeferredRegister<Block> REGISTER = DeferredRegister.create(Registries.BLOCK, Ironagefurniture.MODID);
    static {
        for (String wood : new String[] {"dead", "empyreal", "fir", "hellbark", "jacaranda", "magic", "mahogany",
                "maple", "palm", "pine", "redwood", "umbran", "willow", "origin_oak"}) {
            PhaseFourFurniture.registerWood("biomesoplenty_" + wood, REGISTER, ModItems.REGISTER);
        }
    }
    private PhaseFourBopFurniture() { }
}
