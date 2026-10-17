package zone.moddev.mc.ironagefurniture.init;

import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import zone.moddev.mc.ironagefurniture.Ironagefurniture;
import zone.moddev.mc.ironagefurniture.api.tile.ShieldChairBlockEntity;

/** Shield-chair subclasses supplied by wood add-ons share this storage type. */
public final class PhaseFourBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> REGISTER = DeferredRegister.create(net.minecraft.core.registries.Registries.BLOCK_ENTITY_TYPE, Ironagefurniture.MODID);
    public static final RegistryObject<BlockEntityType<ShieldChairBlockEntity>> SHIELD_CHAIR =
            REGISTER.register("shield_chair", () -> new BlockEntityType<>(ShieldChairBlockEntity::new, coreShieldChairs()) {
                @Override public boolean isValid(net.minecraft.world.level.block.state.BlockState state) {
                    return state.getBlock() instanceof zone.moddev.mc.ironagefurniture.api.blocks.furniture.ShieldChair;
                }
            });
    private static Set<Block> coreShieldChairs() {
        return ModVanillaShieldChairs.REGISTER.getEntries().stream().map(RegistryObject::get).collect(Collectors.toSet());
    }
    private PhaseFourBlockEntities() { }
}
