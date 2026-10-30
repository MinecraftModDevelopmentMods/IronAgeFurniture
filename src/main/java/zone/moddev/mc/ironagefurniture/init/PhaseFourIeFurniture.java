package zone.moddev.mc.ironagefurniture.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import zone.moddev.mc.ironagefurniture.Ironagefurniture;
import zone.moddev.mc.ironagefurniture.ModItems;

/** The original treated-wood family remains an optional core integration. */
public final class PhaseFourIeFurniture {
    public static final DeferredRegister<Block> REGISTER = DeferredRegister.create(Registries.BLOCK, Ironagefurniture.MODID);
    static { PhaseFourFurniture.registerWood("immersiveengineering_treated_wood", REGISTER, ModItems.REGISTER); }
    private PhaseFourIeFurniture() { }
}
