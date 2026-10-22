package zone.moddev.mc.ironagefurniture.init;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredRegister;
import zone.moddev.mc.ironagefurniture.Ironagefurniture;
import zone.moddev.mc.ironagefurniture.ModItems;

/** Treated-wood furniture accompanies the existing optional IE registrations. */
public final class PhaseFourIeFurniture {
    public static final DeferredRegister<Block> REGISTER = DeferredRegister.create(
            BuiltInRegistries.BLOCK, Ironagefurniture.MODID);
    public static final String WOOD = "immersiveengineering_treated_wood";
    static {
        PhaseFourFurniture.registerWood(WOOD, REGISTER, ModItems.REGISTER);
    }
    private PhaseFourIeFurniture() { }
}
