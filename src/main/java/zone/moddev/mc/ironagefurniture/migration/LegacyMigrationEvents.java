package zone.moddev.mc.ironagefurniture.migration;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.WeakHashMap;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
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
	private static final Map<World, Map<BlockPos, String>> PENDING_BENCHES = new WeakHashMap<>();

	private LegacyMigrationEvents() {
	}

	static synchronized void queueBench(World world, BlockPos pos, String colour) {
		PENDING_BENCHES.computeIfAbsent(world, ignored -> new LinkedHashMap<>())
				.put(pos.toImmutable(), LegacyPaddedBenchIds.normalizeColour(colour));
	}

	@SubscribeEvent
	public static void onWorldTick(TickEvent.WorldTickEvent event) {
		if (event.phase != TickEvent.Phase.END || event.world.isRemote) {
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
					event.world.getDimension().getType().getId());
		}
	}

	private static boolean convertBench(World world, BlockPos pos, String colour) {
		BlockState legacyState = world.getBlockState(pos);
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
		BlockState targetState = target.getDefaultState();
		if (legacyState.has(FurnitureBlock.DIRECTION) && targetState.has(FurnitureBlock.DIRECTION)) {
			targetState = targetState.with(FurnitureBlock.DIRECTION,
					legacyState.get(FurnitureBlock.DIRECTION));
		}
		if (legacyState.has(BackBench.TYPE) && targetState.has(BackBench.TYPE)) {
			targetState = targetState.with(BackBench.TYPE, legacyState.get(BackBench.TYPE));
		}
		if (targetState.has(FurnitureBlock.WATERLOGGED)) {
			targetState = targetState.with(FurnitureBlock.WATERLOGGED, Boolean.FALSE);
		}
		return world.setBlockState(pos, targetState, 3);
	}

	@SubscribeEvent
	public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
		migrateInventory(event.getPlayer().inventory);
		migrateInventory(event.getPlayer().getInventoryEnderChest());
	}

	private static void migrateInventory(IInventory inventory) {
		for (int slot = 0; slot < inventory.getSizeInventory(); ++slot) {
			ItemStack original = inventory.getStackInSlot(slot);
			ItemStack migrated = LegacyPaddedItemMigration.migrate(original);
			if (migrated != original) {
				inventory.setInventorySlotContents(slot, migrated);
			}
		}
	}

	@SubscribeEvent
	public static void onEntityJoin(EntityJoinWorldEvent event) {
		if (!event.getWorld().isRemote && event.getEntity() instanceof ItemEntity) {
			ItemEntity item = (ItemEntity)event.getEntity();
			ItemStack migrated = LegacyPaddedItemMigration.migrate(item.getItem());
			if (migrated != item.getItem()) {
				item.setItem(migrated);
			}
		}
	}

}
