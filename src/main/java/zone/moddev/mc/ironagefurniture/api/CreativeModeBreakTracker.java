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
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.level.BlockEvent;

/**
 * Associates a falling lava light with the creative-mode support break that
 * caused it. This avoids treating an unrelated nearby creative player as the
 * cause of a survival player's falling lamp.
 */
public class CreativeModeBreakTracker {
	private static final long RECENT_BREAK_TICKS = 200L;
	private static final Map<Level, List<BreakRecord>> BREAKS = new WeakHashMap<>();

	@SubscribeEvent
	public void onBlockBreak(BlockEvent.BreakEvent event) {
		Player player = event.getPlayer();

		if (!(event.getLevel() instanceof Level level) || level.isClientSide || player == null)
			return;

		// A later survival break at the same support must not inherit earlier Creative authority.
		List<BreakRecord> records = BREAKS.get(level);
		long now = level.getGameTime();
		if (records != null) records.removeIf(record -> now - record.time > RECENT_BREAK_TICKS || record.pos.equals(event.getPos()));
		if (player.isCreative() && isNearLavaFallingLight(level, event.getPos()))
			remember(level, event.getPos());
	}

	/** Snapshot the exact synchronous support-break cause into every released vial. */
	public static boolean isCreativeSupportBreak(Level level, BlockPos support) {
		List<BreakRecord> records = BREAKS.get(level);
		if (records == null) return false;
		long now = level.getGameTime();
		return records.stream().anyMatch(record -> record.time == now && record.pos.equals(support));
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

			// Released holders already carry their own causal snapshot. Their record must
			// never protect a different standalone lamp that happens to land nearby later.
			if (record.standalone && Math.abs(record.pos.getX() - landingPos.getX()) <= 1
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
			.add(new BreakRecord(pos.immutable(), level.getGameTime(),
				level.getBlockState(pos.above()).is(ModVanillaLights.light_metal_ironage_block_floor_lava_clear.get())));
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
			|| block instanceof zone.moddev.mc.ironagefurniture.api.blocks.lightsource.lava.LightSourceSconceLavaFloor
			|| block instanceof zone.moddev.mc.ironagefurniture.api.blocks.lightsource.lava.LightSourceSconceLavaWall;
	}

	private record BreakRecord(BlockPos pos, long time, boolean standalone) {
	}
}
