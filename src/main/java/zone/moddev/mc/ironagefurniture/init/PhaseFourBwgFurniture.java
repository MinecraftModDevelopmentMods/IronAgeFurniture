package zone.moddev.mc.ironagefurniture.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import zone.moddev.mc.ironagefurniture.Ironagefurniture;
import zone.moddev.mc.ironagefurniture.ModItems;

/** Optional furniture preserves this branch's published wood identities. */
public final class PhaseFourBwgFurniture {
    public static final DeferredRegister<Block> REGISTER = DeferredRegister.create(Registries.BLOCK, Ironagefurniture.MODID);
    public static final String[] WOODS = {"aspen", "baobab", "blue_enchanted", "cika", "cypress", "ebony", "fir", "florus", "green_enchanted", "holly", "ironwood", "jacaranda", "mahogany", "maple", "palm", "pine", "rainbow_eucalyptus", "redwood", "sakura", "skyris", "white_mangrove", "willow", "witch_hazel", "zelkova"};
    static {
        for (String wood : WOODS) PhaseFourFurniture.registerWood("biomeswevegone_" + wood, REGISTER, ModItems.REGISTER);

    }
    private PhaseFourBwgFurniture() { }
}
