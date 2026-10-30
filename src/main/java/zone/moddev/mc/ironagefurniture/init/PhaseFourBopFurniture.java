package zone.moddev.mc.ironagefurniture.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import zone.moddev.mc.ironagefurniture.Ironagefurniture;
import zone.moddev.mc.ironagefurniture.ModItems;

/** Optional furniture preserves this branch's published wood identities. */
public final class PhaseFourBopFurniture {
    public static final DeferredRegister<Block> REGISTER = DeferredRegister.create(Registries.BLOCK, Ironagefurniture.MODID);
    public static final String[] WOODS = zone.moddev.mc.ironagefurniture.compat.OptionalModSupport.isLoadedAtLeast("biomesoplenty", "19.0.0.96")
            ? new String[]{"dead", "fir", "hellbark", "jacaranda", "magic", "mahogany", "palm", "redwood", "umbran", "willow", "empyreal", "maple", "pine"} : new String[]{"dead", "fir", "hellbark", "jacaranda", "magic", "mahogany", "palm", "redwood", "umbran", "willow"};
    static {
        for (String wood : WOODS) PhaseFourFurniture.registerWood("biomesoplenty_" + wood, REGISTER, ModItems.REGISTER);
    }
    private PhaseFourBopFurniture() { }
}
