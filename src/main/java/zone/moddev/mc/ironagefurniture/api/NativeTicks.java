package zone.moddev.mc.ironagefurniture.api;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

/** Uses the separate block and liquid tick queues provided by Minecraft 1.17.1. */
public final class NativeTicks {
    private NativeTicks() { }
    public static void schedule(LevelAccessor level, BlockPos pos, Block block, int delay) {
        level.getBlockTicks().scheduleTick(pos, block, delay);
    }
    public static void schedule(LevelAccessor level, BlockPos pos, Fluid fluid, int delay) {
        level.getLiquidTicks().scheduleTick(pos, fluid, delay);
    }
}
