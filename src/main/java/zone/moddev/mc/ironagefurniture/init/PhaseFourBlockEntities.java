package zone.moddev.mc.ironagefurniture.init;

import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import zone.moddev.mc.ironagefurniture.Ironagefurniture;
import zone.moddev.mc.ironagefurniture.api.blocks.furniture.ShieldChair;
import zone.moddev.mc.ironagefurniture.api.tile.ShieldChairBlockEntity;

/** Optional provider chairs belong to the same persistent core block-entity type. */
public final class PhaseFourBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> REGISTER =
            DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, Ironagefurniture.MODID);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ShieldChairBlockEntity>> SHIELD_CHAIR =
            REGISTER.register("shield_chair", () -> BlockEntityType.Builder
                    .of(ShieldChairBlockEntity::new, shieldChairs()).build(null));

    private static Block[] shieldChairs() {
        Set<Block> chairs = ModVanillaShieldChairs.REGISTER.getEntries().stream()
                .map(DeferredHolder::get).collect(Collectors.toSet());
        if (ModList.get().isLoaded("biomesoplenty"))
            ModBOPBlocks.REGISTER.getEntries().stream().map(DeferredHolder::get)
                    .filter(ShieldChair.class::isInstance).forEach(chairs::add);
        if (ModList.get().isLoaded("biomeswevegone"))
            ModBWGBlocks.REGISTER.getEntries().stream().map(DeferredHolder::get)
                    .filter(ShieldChair.class::isInstance).forEach(chairs::add);
        if (ModList.get().isLoaded("immersiveengineering"))
            ModIEBlocks.REGISTER.getEntries().stream().map(DeferredHolder::get)
                    .filter(ShieldChair.class::isInstance).forEach(chairs::add);
        return chairs.toArray(Block[]::new);
    }

    private PhaseFourBlockEntities() { }
}
