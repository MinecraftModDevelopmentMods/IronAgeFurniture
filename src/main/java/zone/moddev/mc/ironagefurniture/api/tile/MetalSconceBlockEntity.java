package zone.moddev.mc.ironagefurniture.api.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import zone.moddev.mc.ironagefurniture.init.PhaseFourLighting;

/** Supplies the installed metal's block model without keeping a ticking inventory. */
public final class MetalSconceBlockEntity extends BlockEntity {
    public MetalSconceBlockEntity(BlockPos pos, BlockState state) { super(PhaseFourLighting.METAL_FRAME.get(), pos, state); }
}
