package zone.moddev.mc.ironagefurniture.api;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import zone.moddev.mc.ironagefurniture.init.ModVanillaLights;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.level.block.BreakBlockEvent;

/**
 * Associates a falling lava light with the creative-mode support break that
 * caused it. This avoids treating an unrelated nearby creative player as the
 * cause of a survival player's falling lamp.
 */
public class CreativeModeBreakTracker {
	private static final long RECENT_BREAK_TICKS = 200L;
	private static final Map<Level, List<BreakRecord>> BREAKS = new WeakHashMap<>();

	@SubscribeEvent
	public void onBlockBreak(BreakBlockEvent event) {
		Player player = event.getPlayer();

		if (!(event.getLevel() instanceof Level level) || level.isClientSide() || player == null || !player.getAbilities().instabuild)
			return;

		if (isNearLavaFallingLight(level, event.getPos()))
			remember(level, event.getPos());
	}

	public static boolean shouldSuppressFallingLavaBreak(Level level, BlockPos landingPos) {
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

	private static void remember(Level level, BlockPos pos) {
		BREAKS.computeIfAbsent(level, ignored -> new ArrayList<>())
			.add(new BreakRecord(pos.immutable(), level.getGameTime()));
	}

	private static boolean isNearLavaFallingLight(Level level, BlockPos pos) {
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
		return block == ModVanillaLights.light_metal_ironage_block_floor_lava_clear.get()
			|| block == ModVanillaLights.light_metal_ironage_sconce_floor_lava_iron.get()
			|| block == ModVanillaLights.light_metal_ironage_sconce_wall_lava_iron.get();
	}

	private record BreakRecord(BlockPos pos, long time) {
	}
}
