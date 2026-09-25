package zone.moddev.mc.ironagefurniture.migration;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.WeakHashMap;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import zone.moddev.mc.ironagefurniture.Ironagefurniture;
import zone.moddev.mc.ironagefurniture.api.blocks.base.FurnitureBlock;
import zone.moddev.mc.ironagefurniture.api.blocks.furniture.BackBench;

/** Completes colour-aware conversion after a legacy chunk or item has loaded. */
@Mod.EventBusSubscriber(modid = Ironagefurniture.MODID)
public final class LegacyMigrationEvents {
	private static final Logger LOGGER = LogManager.getLogger();
	private static final Map<Level, Map<BlockPos, String>> PENDING_BENCHES = new WeakHashMap<>();

	private LegacyMigrationEvents() {
	}

	static synchronized void queueBench(Level level, BlockPos pos, String colour) {
		PENDING_BENCHES.computeIfAbsent(level, ignored -> new LinkedHashMap<>())
				.put(pos.immutable(), LegacyPaddedBenchIds.normalizeColour(colour));
	}

	@SubscribeEvent
	public static void onWorldTick(TickEvent.WorldTickEvent event) {
		if (event.phase != TickEvent.Phase.END || event.world.isClientSide) {
			return;
		}
		Map<BlockPos, String> pending;
		synchronized (LegacyMigrationEvents.class) {
			pending = PENDING_BENCHES.remove(event.world);
		}
		if (pending == null || pending.isEmpty()) {
			return;
		}
		int converted = 0;
		for (Map.Entry<BlockPos, String> entry : pending.entrySet()) {
			if (convertBench(event.world, entry.getKey(), entry.getValue())) {
				++converted;
			}
		}
		if (converted > 0) {
			LOGGER.info("Converted {} pre-flattening coloured padded benches in dimension {}", converted,
					event.world.dimension().location());
		}
	}

	private static boolean convertBench(Level level, BlockPos pos, String colour) {
		BlockState legacyState = level.getBlockState(pos);
		if (!(legacyState.getBlock() instanceof LegacyPaddedFurnitureBlock)) {
			return false;
		}
		String oldPath = ((LegacyPaddedFurnitureBlock)legacyState.getBlock()).getLegacyPath();
		ResourceLocation targetId = new ResourceLocation(Ironagefurniture.MODID,
				LegacyPaddedBenchIds.modernPaddedPath(oldPath, colour));
		Block target = ForgeRegistries.BLOCKS.getValue(targetId);
		if (target == null) {
			LOGGER.warn("Could not convert legacy padded bench '{}' at {} because '{}' is not registered",
					oldPath, pos, targetId);
			return false;
		}
		BlockState targetState = target.defaultBlockState();
		if (legacyState.hasProperty(FurnitureBlock.DIRECTION) && targetState.hasProperty(FurnitureBlock.DIRECTION)) {
			targetState = targetState.setValue(FurnitureBlock.DIRECTION,
					legacyState.getValue(FurnitureBlock.DIRECTION));
		}
		if (legacyState.hasProperty(BackBench.TYPE) && targetState.hasProperty(BackBench.TYPE)) {
			targetState = targetState.setValue(BackBench.TYPE, legacyState.getValue(BackBench.TYPE));
		}
		if (targetState.hasProperty(FurnitureBlock.WATERLOGGED)) {
			targetState = targetState.setValue(FurnitureBlock.WATERLOGGED, Boolean.FALSE);
		}
		return level.setBlock(pos, targetState, 3);
	}

	@SubscribeEvent
	public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
		migrateInventory(event.getPlayer().getInventory());
		migrateInventory(event.getPlayer().getEnderChestInventory());
	}

	private static void migrateInventory(Container inventory) {
		for (int slot = 0; slot < inventory.getContainerSize(); ++slot) {
			ItemStack original = inventory.getItem(slot);
			ItemStack migrated = LegacyPaddedItemMigration.migrate(original);
			if (migrated != original) {
				inventory.setItem(slot, migrated);
			}
		}
	}

	@SubscribeEvent
	public static void onEntityJoin(EntityJoinWorldEvent event) {
		if (!event.getWorld().isClientSide && event.getEntity() instanceof ItemEntity) {
			ItemEntity item = (ItemEntity)event.getEntity();
			ItemStack migrated = LegacyPaddedItemMigration.migrate(item.getItem());
			if (migrated != item.getItem()) {
				item.setItem(migrated);
			}
		}
	}
}
