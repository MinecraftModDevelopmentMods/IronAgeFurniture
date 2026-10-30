package zone.moddev.mc.ironagefurniture.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import zone.moddev.mc.ironagefurniture.Ironagefurniture;
import zone.moddev.mc.ironagefurniture.ModItems;

/** Optional furniture preserves this branch's published wood identities. */
public final class PhaseFourBygFurniture {
    public static final DeferredRegister<Block> REGISTER = DeferredRegister.create(Registries.BLOCK, Ironagefurniture.MODID);
    public static final String[] WOODS = {"aspen", "baobab", "blue_enchanted", "bulbis", "cherry", "cika", "cypress", "ebony", "embur", "ether", "fir", "green_enchanted", "holly", "jacaranda", "lament", "mahogany", "mangrove", "maple", "nightshade", "palm", "pine", "rainbow_eucalyptus", "redwood", "skyris", "willow", "witch_hazel", "zelkova"};
    static {
        for (String wood : WOODS) PhaseFourFurniture.registerWood("byg_" + wood, REGISTER, ModItems.REGISTER);
    }
    private PhaseFourBygFurniture() { }
}
