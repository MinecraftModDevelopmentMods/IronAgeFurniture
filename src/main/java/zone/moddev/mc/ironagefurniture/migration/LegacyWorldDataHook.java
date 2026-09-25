package zone.moddev.mc.ironagefurniture.migration;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.BitSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.mojang.serialization.Dynamic;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.datafix.fixes.BlockStateData;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraftforge.fmlserverevents.FMLServerAboutToStartEvent;
import net.minecraftforge.registries.ForgeRegistries;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import zone.moddev.mc.ironagefurniture.api.blocks.base.FurnitureBlock;
import zone.moddev.mc.ironagefurniture.api.blocks.furniture.BackBench;
import zone.moddev.mc.ironagefurniture.api.enumerations.BenchType;

/** Converts pre-flattening Iron Age Furniture block IDs before vanilla chunk datafixing. */
public final class LegacyWorldDataHook {
	private static final Logger LOGGER = LogManager.getLogger();
	private static final BitSet LEGACY_IAF_BLOCK_IDS = new BitSet();
	private static final BitSet LEGACY_PADDED_BLOCK_IDS = new BitSet();
	private static final Map<String, String> LEGACY_VANILLA_BLOCK_ENTITY_IDS = new HashMap<>();
	private static final String PRESERVE_CHUNK_MARKER = "IronAgeFurnitureLegacyPreserveChunk";
	private static final String PADDED_BLOCK_ENTITY_MARKER = "IronAgeFurnitureLegacyPaddedBench";
	private static final String SIDECAR_NAME = "ironagefurniture_legacy_registry.dat";
	private static volatile boolean legacyWorldActive;

	static {
		LEGACY_VANILLA_BLOCK_ENTITY_IDS.put("Airportal", "end_portal");
		LEGACY_VANILLA_BLOCK_ENTITY_IDS.put("Banner", "banner");
		LEGACY_VANILLA_BLOCK_ENTITY_IDS.put("Beacon", "beacon");
		LEGACY_VANILLA_BLOCK_ENTITY_IDS.put("Cauldron", "brewing_stand");
		LEGACY_VANILLA_BLOCK_ENTITY_IDS.put("Chest", "chest");
		LEGACY_VANILLA_BLOCK_ENTITY_IDS.put("Comparator", "comparator");
		LEGACY_VANILLA_BLOCK_ENTITY_IDS.put("Control", "command_block");
		LEGACY_VANILLA_BLOCK_ENTITY_IDS.put("DLDetector", "daylight_detector");
		LEGACY_VANILLA_BLOCK_ENTITY_IDS.put("Dropper", "dropper");
		LEGACY_VANILLA_BLOCK_ENTITY_IDS.put("EnchantTable", "enchanting_table");
		LEGACY_VANILLA_BLOCK_ENTITY_IDS.put("EndGateway", "end_gateway");
		LEGACY_VANILLA_BLOCK_ENTITY_IDS.put("EnderChest", "ender_chest");
		LEGACY_VANILLA_BLOCK_ENTITY_IDS.put("FlowerPot", "flower_pot");
		LEGACY_VANILLA_BLOCK_ENTITY_IDS.put("Furnace", "furnace");
		LEGACY_VANILLA_BLOCK_ENTITY_IDS.put("Hopper", "hopper");
		LEGACY_VANILLA_BLOCK_ENTITY_IDS.put("MobSpawner", "mob_spawner");
		LEGACY_VANILLA_BLOCK_ENTITY_IDS.put("Music", "noteblock");
		LEGACY_VANILLA_BLOCK_ENTITY_IDS.put("Piston", "piston");
		LEGACY_VANILLA_BLOCK_ENTITY_IDS.put("RecordPlayer", "jukebox");
		LEGACY_VANILLA_BLOCK_ENTITY_IDS.put("Sign", "sign");
		LEGACY_VANILLA_BLOCK_ENTITY_IDS.put("Skull", "skull");
		LEGACY_VANILLA_BLOCK_ENTITY_IDS.put("Structure", "structure_block");
		LEGACY_VANILLA_BLOCK_ENTITY_IDS.put("Trap", "dispenser");
	}

	private LegacyWorldDataHook() {
	}

	public static void onServerAboutToStart(FMLServerAboutToStartEvent event) {
		if (legacyWorldActive) {
			return;
		}
		File levelDat = event.getServer().getWorldPath(LevelResource.LEVEL_DATA_FILE).toFile();
		prepareLegacyWorld(levelDat);
	}

	/**
	 * Captures the Forge 1.10/1.12 registry snapshot while Minecraft is still
	 * reading the original level data. Later lifecycle events see Forge's rewritten
	 * snapshot and can no longer recover the old numeric block IDs.
	 */
	public static synchronized void captureLegacyWorld(LevelStorageSource.LevelStorageAccess levelAccess,
			CompoundTag root) {
		if (levelAccess == null || root == null || !root.contains("FML", 10)) {
			return;
		}
		CompoundTag forgeData = root.getCompound("FML");
		if (!forgeData.contains("Registries", 10)) {
			return;
		}
		CompoundTag registries = forgeData.getCompound("Registries");
		if (!registries.contains("minecraft:blocks", 10)) {
			return;
		}
		File worldDirectory = levelAccess.getWorldDir().toFile();
		CompoundTag blocks = registries.getCompound("minecraft:blocks");
		install(worldDirectory, blocks);
		writeSidecar(worldDirectory, blocks);
	}

	private static synchronized void prepareLegacyWorld(File levelDat) {
		legacyWorldActive = false;
		if (!levelDat.isFile()) {
			return;
		}

		try {
			CompoundTag root = NbtIo.readCompressed(levelDat);
			if (root.contains("FML", 10)) {
				CompoundTag registries = root.getCompound("FML").getCompound("Registries");
				if (registries.contains("minecraft:blocks", 10)) {
					CompoundTag blocks = registries.getCompound("minecraft:blocks");
					install(levelDat.getParentFile(), blocks);
					writeSidecar(levelDat.getParentFile(), blocks);
					return;
				}
			}
		} catch (IOException e) {
			LOGGER.warn("Could not inspect '{}' for legacy Iron Age Furniture registry data", levelDat, e);
			return;
		}

		File sidecar = sidecar(levelDat.getParentFile());
		if (sidecar.isFile()) {
			try {
				install(levelDat.getParentFile(), NbtIo.readCompressed(sidecar).getCompound("Blocks"));
			} catch (IOException e) {
				LOGGER.warn("Could not read legacy Iron Age Furniture registry sidecar '{}'", sidecar, e);
			}
		}
	}

	private static void install(File worldDirectory, CompoundTag blockSnapshot) {
		int mappedStates = installLegacyBlockStates(blockSnapshot);
		legacyWorldActive = mappedStates > 0;
		if (legacyWorldActive) {
			LOGGER.info("Prepared {} legacy Iron Age Furniture block states from '{}'", mappedStates,
					worldDirectory);
		}
	}

	private static void writeSidecar(File worldDirectory, CompoundTag blockSnapshot) {
		File sidecar = sidecar(worldDirectory);
		if (sidecar.isFile()) {
			return;
		}
		File parent = sidecar.getParentFile();
		File temporary = new File(parent, SIDECAR_NAME + ".tmp");
		try {
			Files.createDirectories(parent.toPath());
			CompoundTag root = new CompoundTag();
			root.put("Blocks", blockSnapshot.copy());
			NbtIo.writeCompressed(root, temporary);
			try {
				Files.move(temporary.toPath(), sidecar.toPath(), StandardCopyOption.ATOMIC_MOVE);
			} catch (AtomicMoveNotSupportedException e) {
				Files.move(temporary.toPath(), sidecar.toPath(), StandardCopyOption.REPLACE_EXISTING);
			}
		} catch (IOException e) {
			LOGGER.warn("Could not preserve legacy Iron Age Furniture registry data in '{}'", sidecar, e);
		}
	}

	private static File sidecar(File worldDirectory) {
		return new File(new File(worldDirectory, "data"), SIDECAR_NAME);
	}

	/** Called by the chunk-loader coremod immediately before vanilla datafixing. */
	public static void prepareLegacyChunk(CompoundTag root) {
		if (!legacyWorldActive || root == null || !root.contains("Level", 10)) {
			return;
		}

		CompoundTag level = root.getCompound("Level");
		LegacyPaddedItemMigration.migrateChunkContents(level);
		if (!containsLegacyIafBlock(level)) {
			return;
		}
		ensurePaddedBenchBlockEntities(level, root.getInt("DataVersion") < 704);
		level.putBoolean("TerrainPopulated", true);
		level.putBoolean("LightPopulated", true);
		level.putBoolean(PRESERVE_CHUNK_MARKER, true);
	}

	/** Called by the chunk-loader coremod after vanilla datafixing. */
	public static CompoundTag finalizeLegacyChunk(CompoundTag root) {
		if (root == null || !root.contains("Level", 10)) {
			return root;
		}
		CompoundTag level = root.getCompound("Level");
		if (legacyWorldActive) {
			normalizeLegacyVanillaBlockEntityIds(level);
		}
		if (level.getBoolean(PRESERVE_CHUNK_MARKER)) {
			restorePaddedBenchBlockEntityIds(level);
			level.putString("Status", "full");
			level.remove(PRESERVE_CHUNK_MARKER);
		}
		return root;
	}

	static int normalizeLegacyVanillaBlockEntityIds(CompoundTag level) {
		int changed = 0;
		ListTag blockEntities = level.getList("TileEntities", 10);
		for (int index = 0; index < blockEntities.size(); ++index) {
			CompoundTag blockEntity = blockEntities.getCompound(index);
			String id = blockEntity.getString("id");
			String legacyPath = id.startsWith("minecraft:") ? id.substring("minecraft:".length()) : id;
			String modernPath = LEGACY_VANILLA_BLOCK_ENTITY_IDS.get(legacyPath);
			if (modernPath != null) {
				blockEntity.putString("id", "minecraft:" + modernPath);
				++changed;
			}
		}
		return changed;
	}

	private static int installLegacyBlockStates(CompoundTag blockSnapshot) {
		LEGACY_IAF_BLOCK_IDS.clear();
		LEGACY_PADDED_BLOCK_IDS.clear();
		Map<ResourceLocation, Integer> iafIds = new HashMap<>();
		ListTag savedIds = blockSnapshot.getList("ids", 10);
		int highestStateId = 0;
		for (int index = 0; index < savedIds.size(); ++index) {
			CompoundTag savedId = savedIds.getCompound(index);
			ResourceLocation id = new ResourceLocation(savedId.getString("K"));
			if (!LegacyPaddedBenchIds.isLegacyNamespace(id.getNamespace())) {
				continue;
			}
			int numericId = savedId.getInt("V");
			iafIds.put(id, numericId);
			highestStateId = Math.max(highestStateId, (numericId << 4) | 15);
		}
		Dynamic<?>[] legacyStates = expandFlatteningTable(highestStateId + 1);
		int mapped = 0;
		for (Map.Entry<ResourceLocation, Integer> entry : iafIds.entrySet()) {
			ResourceLocation oldId = entry.getKey();
			Block block = resolveCurrentBlock(oldId);
			if (block == null) {
				LOGGER.warn("Legacy Iron Age Furniture block '{}' has no supported 1.17 replacement", oldId);
				continue;
			}
			LEGACY_IAF_BLOCK_IDS.set(entry.getValue());
			if (LegacyPaddedBenchIds.isLegacyPaddedPath(oldId.getPath())) {
				LEGACY_PADDED_BLOCK_IDS.set(entry.getValue());
			}
			for (int meta = 0; meta < 16; ++meta) {
				String stateNbt = NbtUtils.writeBlockState(legacyState(block, meta)).toString();
				int stateId = (entry.getValue() << 4) | meta;
				legacyStates[stateId] = BlockStateData.parse(stateNbt);
				++mapped;
			}
		}
		return mapped;
	}

	private static Block resolveCurrentBlock(ResourceLocation oldId) {
		ResourceLocation target = LegacyPaddedBenchIds.currentId(oldId);
		return ForgeRegistries.BLOCKS.containsKey(target) ? ForgeRegistries.BLOCKS.getValue(target) : null;
	}

	static BlockState legacyState(Block block, int meta) {
		BlockState state = block.defaultBlockState();
		if (state.hasProperty(FurnitureBlock.DIRECTION)) {
			state = state.setValue(FurnitureBlock.DIRECTION, direction(meta));
		}
		if (state.hasProperty(FurnitureBlock.WATERLOGGED)) {
			state = state.setValue(FurnitureBlock.WATERLOGGED, Boolean.FALSE);
		}
		if (block instanceof BackBench && state.hasProperty(BackBench.TYPE)) {
			state = state.setValue(BackBench.TYPE, benchType(meta));
		}
		return state;
	}

	static Direction direction(int meta) {
		return Direction.from2DDataValue(meta & 3);
	}

	static BenchType benchType(int meta) {
		switch ((meta & 15) >> 2) {
			case 1: return BenchType.MIDDLE;
			case 2: return BenchType.LEFT;
			case 3: return BenchType.RIGHT;
			default: return BenchType.SINGLE;
		}
	}

	private static boolean containsLegacyIafBlock(CompoundTag level) {
		ListTag sections = level.getList("Sections", 10);
		for (int sectionIndex = 0; sectionIndex < sections.size(); ++sectionIndex) {
			CompoundTag section = sections.getCompound(sectionIndex);
			byte[] blocks = section.getByteArray("Blocks");
			if (blocks.length != 4096) {
				continue;
			}
			byte[] add = section.getByteArray("Add");
			for (int blockIndex = 0; blockIndex < blocks.length; ++blockIndex) {
				if (LEGACY_IAF_BLOCK_IDS.get(blockId(blocks, add, blockIndex))) {
					return true;
				}
			}
		}
		return false;
	}

	private static void ensurePaddedBenchBlockEntities(CompoundTag level, boolean legacyBlockEntityIds) {
		ListTag blockEntities = level.getList("TileEntities", 10);
		Set<Long> occupied = new HashSet<>();
		for (int index = 0; index < blockEntities.size(); ++index) {
			CompoundTag blockEntity = blockEntities.getCompound(index);
			occupied.add(positionKey(blockEntity.getInt("x"), blockEntity.getInt("y"), blockEntity.getInt("z")));
			int blockId = getLegacyBlockId(level, blockEntity.getInt("x"), blockEntity.getInt("y"),
					blockEntity.getInt("z"));
			if (LEGACY_PADDED_BLOCK_IDS.get(blockId)) {
				markPaddedBenchBlockEntity(blockEntity, legacyBlockEntityIds);
			}
		}

		int chunkX = level.getInt("xPos");
		int chunkZ = level.getInt("zPos");
		ListTag sections = level.getList("Sections", 10);
		for (int sectionIndex = 0; sectionIndex < sections.size(); ++sectionIndex) {
			CompoundTag section = sections.getCompound(sectionIndex);
			byte[] blocks = section.getByteArray("Blocks");
			if (blocks.length != 4096) {
				continue;
			}
			byte[] add = section.getByteArray("Add");
			int sectionY = section.getByte("Y") & 0xFF;
			for (int blockIndex = 0; blockIndex < blocks.length; ++blockIndex) {
				if (!LEGACY_PADDED_BLOCK_IDS.get(blockId(blocks, add, blockIndex))) {
					continue;
				}
				int x = (chunkX << 4) + (blockIndex & 15);
				int y = (sectionY << 4) + ((blockIndex >> 8) & 15);
				int z = (chunkZ << 4) + ((blockIndex >> 4) & 15);
				if (!occupied.add(positionKey(x, y, z))) {
					continue;
				}
				CompoundTag blockEntity = new CompoundTag();
				markPaddedBenchBlockEntity(blockEntity, legacyBlockEntityIds);
				blockEntity.putInt("x", x);
				blockEntity.putInt("y", y);
				blockEntity.putInt("z", z);
				blockEntity.putString(LegacyPaddedBenchIds.COLOR_TAG, "red");
				blockEntities.add(blockEntity);
			}
		}
		level.put("TileEntities", blockEntities);
	}

	private static void markPaddedBenchBlockEntity(CompoundTag blockEntity, boolean legacyBlockEntityIds) {
		blockEntity.putString("id", legacyBlockEntityIds ? "Sign" : "minecraft:sign");
		blockEntity.putBoolean(PADDED_BLOCK_ENTITY_MARKER, true);
	}

	private static void restorePaddedBenchBlockEntityIds(CompoundTag level) {
		ListTag blockEntities = level.getList("TileEntities", 10);
		for (int index = 0; index < blockEntities.size(); ++index) {
			CompoundTag blockEntity = blockEntities.getCompound(index);
			if (!blockEntity.getBoolean(PADDED_BLOCK_ENTITY_MARKER)) {
				continue;
			}
			blockEntity.putString("id", LegacyPaddedBenchIds.BLOCK_ENTITY_ID.toString());
			blockEntity.remove(PADDED_BLOCK_ENTITY_MARKER);
		}
	}

	private static long positionKey(int x, int y, int z) {
		return ((long)x & 0x3FFFFFFL) << 38 | ((long)z & 0x3FFFFFFL) << 12 | (long)y & 0xFFFL;
	}

	private static int getLegacyBlockId(CompoundTag level, int x, int y, int z) {
		if (y < 0 || y > 255) {
			return -1;
		}
		ListTag sections = level.getList("Sections", 10);
		for (int sectionIndex = 0; sectionIndex < sections.size(); ++sectionIndex) {
			CompoundTag section = sections.getCompound(sectionIndex);
			if ((section.getByte("Y") & 0xFF) != y >> 4) {
				continue;
			}
			byte[] blocks = section.getByteArray("Blocks");
			if (blocks.length != 4096) {
				return -1;
			}
			int index = ((y & 15) << 8) | ((z & 15) << 4) | (x & 15);
			return blockId(blocks, section.getByteArray("Add"), index);
		}
		return -1;
	}

	private static int blockId(byte[] blocks, byte[] add, int index) {
		int highBits = add.length == 2048 ? (add[index >> 1] >> ((index & 1) * 4)) & 0x0F : 0;
		return (blocks[index] & 0xFF) | (highBits << 8);
	}

	@SuppressWarnings("unchecked")
	private static Dynamic<?>[] expandFlatteningTable(int requiredLength) {
		try {
			Field valuesField = null;
			Dynamic<?>[] current = null;
			for (Field candidate : BlockStateData.class.getDeclaredFields()) {
				if (!candidate.getType().equals(Dynamic[].class)) {
					continue;
				}
				candidate.setAccessible(true);
				Dynamic<?>[] values = (Dynamic<?>[])candidate.get(null);
				if (values.length >= 4096) {
					valuesField = candidate;
					current = values;
					break;
				}
			}
			if (valuesField == null) {
				throw new NoSuchFieldException("Minecraft legacy block-state table");
			}
			if (current.length >= requiredLength) {
				return current;
			}
			Dynamic<?>[] expanded = Arrays.copyOf(current, requiredLength);
			valuesField.set(null, expanded);
			return expanded;
		} catch (ReflectiveOperationException e) {
			throw new IllegalStateException("Could not expand Minecraft's legacy block-state flattening table", e);
		}
	}
}
