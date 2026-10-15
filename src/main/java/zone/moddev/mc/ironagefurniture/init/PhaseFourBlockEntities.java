package zone.moddev.mc.ironagefurniture.init;

import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import zone.moddev.mc.ironagefurniture.Ironagefurniture;
import zone.moddev.mc.ironagefurniture.api.tile.ShieldChairBlockEntity;

/** Add-ons extend the valid blocks using NeoForge's BlockEntityTypeAddBlocksEvent. */
public final class PhaseFourBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> REGISTER = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, Ironagefurniture.MODID);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ShieldChairBlockEntity>> SHIELD_CHAIR =
            REGISTER.register("shield_chair", () -> new BlockEntityType<>(ShieldChairBlockEntity::new, coreShieldChairs()));
    private static Set<Block> coreShieldChairs() {
        return ModVanillaShieldChairs.REGISTER.getEntries().stream().map(DeferredHolder::get).collect(Collectors.toSet());
    }
    private PhaseFourBlockEntities() { }
}
