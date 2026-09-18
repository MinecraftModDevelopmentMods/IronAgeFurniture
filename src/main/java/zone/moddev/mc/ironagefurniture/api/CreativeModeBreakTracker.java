package zone.moddev.mc.ironagefurniture.api;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.Direction;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraft.block.Block;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import zone.moddev.mc.ironagefurniture.BlockObjectHolder;

/**
 * Associates a falling lava light with the creative-mode support break that
 * caused it. This avoids suppressing a survival lamp merely because another
 * creative player happens to be nearby.
 */
public final class CreativeModeBreakTracker {
    private static final long RECENT_BREAK_TICKS = 200L;
    private static final Map<World, List<BreakRecord>> BREAKS = new WeakHashMap<>();

    @SubscribeEvent
    public void onBlockBreak(BlockEvent.BreakEvent event) {
        PlayerEntity player = event.getPlayer();

        if (!(event.getWorld() instanceof World) || player == null || !player.isCreative())
            return;

        World level = (World) event.getWorld();
        if (!level.isClientSide && isNearLavaFallingLight(level, event.getPos()))
            remember(level, event.getPos());
    }

    public static boolean shouldSuppressFallingLavaBreak(World level, BlockPos landingPos) {
        List<BreakRecord> breaks = BREAKS.get(level);
        if (breaks == null)
            return false;

        long now = level.getGameTime();
        Iterator<BreakRecord> iterator = breaks.iterator();
        while (iterator.hasNext()) {
            BreakRecord record = iterator.next();
            if (now - record.time > RECENT_BREAK_TICKS) {
                iterator.remove();
                continue;
            }

            if (Math.abs(record.pos.getX() - landingPos.getX()) <= 1
                    && Math.abs(record.pos.getZ() - landingPos.getZ()) <= 1
                    && record.pos.getY() >= landingPos.getY()) {
                iterator.remove();
                return true;
            }
        }

        return false;
    }

    private static void remember(World level, BlockPos pos) {
        BREAKS.computeIfAbsent(level, ignored -> new ArrayList<>())
                .add(new BreakRecord(pos.immutable(), level.getGameTime()));
    }

    private static boolean isNearLavaFallingLight(World level, BlockPos pos) {
        if (isLavaFallingLight(level.getBlockState(pos.above()).getBlock())
                || isLavaFallingLight(level.getBlockState(pos.below()).getBlock()))
            return true;

        for (Direction direction : Direction.Plane.HORIZONTAL) {
            if (isLavaFallingLight(level.getBlockState(pos.relative(direction)).getBlock()))
                return true;
        }
        return false;
    }

    private static boolean isLavaFallingLight(Block block) {
        return block == BlockObjectHolder.light_metal_ironage_block_floor_lava_clear
                || block == BlockObjectHolder.light_metal_ironage_sconce_floor_lava_iron
                || block == BlockObjectHolder.light_metal_ironage_sconce_wall_lava_iron;
    }

    private static final class BreakRecord {
        private final BlockPos pos;
        private final long time;

        private BreakRecord(BlockPos pos, long time) {
            this.pos = pos;
            this.time = time;
        }
    }
}
