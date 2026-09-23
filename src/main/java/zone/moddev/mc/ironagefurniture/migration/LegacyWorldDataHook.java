package zone.moddev.mc.ironagefurniture.migration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.BitSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.mojang.datafixers.Dynamic;
import cpw.mods.modlauncher.api.INameMappingService;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.fixes.BlockStateFlatteningMap;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import zone.moddev.mc.ironagefurniture.Ironagefurniture;
import zone.moddev.mc.ironagefurniture.api.blocks.base.FurnitureBlock;
import zone.moddev.mc.ironagefurniture.api.blocks.furniture.BackBench;
import zone.moddev.mc.ironagefurniture.api.enumerations.BenchType;

/** Converts pre-flattening Iron Age Furniture block IDs before vanilla chunk datafixing. */
public final class LegacyWorldDataHook {
	private static final Logger LOGGER = LogManager.getLogger();
	private static final BitSet LEGACY_IAF_BLOCK_IDS = new BitSet();
	private static final BitSet LEGACY_PADDED_BLOCK_IDS = new BitSet();
	private static final Map<String, String> LEGACY_VANILLA_TILE_ENTITY_IDS = new HashMap<>();
	private static final String PRESERVE_CHUNK_MARKER = "IronAgeFurnitureLegacyPreserveChunk";
	private static final String PADDED_TILE_MARKER = "IronAgeFurnitureLegacyPaddedBench";
	private static final String SIDECAR_NAME = "ironagefurniture_legacy_registry.dat";
	private static volatile boolean legacyWorldActive;

	static {
		LEGACY_VANILLA_TILE_ENTITY_IDS.put("Airportal", "end_portal");
		LEGACY_VANILLA_TILE_ENTITY_IDS.put("Banner", "banner");
		LEGACY_VANILLA_TILE_ENTITY_IDS.put("Beacon", "beacon");
		LEGACY_VANILLA_TILE_ENTITY_IDS.put("Cauldron", "brewing_stand");
		LEGACY_VANILLA_TILE_ENTITY_IDS.put("Chest", "chest");
		LEGACY_VANILLA_TILE_ENTITY_IDS.put("Comparator", "comparator");
		LEGACY_VANILLA_TILE_ENTITY_IDS.put("Control", "command_block");
		LEGACY_VANILLA_TILE_ENTITY_IDS.put("DLDetector", "daylight_detector");
		LEGACY_VANILLA_TILE_ENTITY_IDS.put("Dropper", "dropper");
		LEGACY_VANILLA_TILE_ENTITY_IDS.put("EnchantTable", "enchanting_table");
		LEGACY_VANILLA_TILE_ENTITY_IDS.put("EndGateway", "end_gateway");
		LEGACY_VANILLA_TILE_ENTITY_IDS.put("EnderChest", "ender_chest");
		LEGACY_VANILLA_TILE_ENTITY_IDS.put("FlowerPot", "flower_pot");
		LEGACY_VANILLA_TILE_ENTITY_IDS.put("Furnace", "furnace");
		LEGACY_VANILLA_TILE_ENTITY_IDS.put("Hopper", "hopper");
		LEGACY_VANILLA_TILE_ENTITY_IDS.put("MobSpawner", "mob_spawner");
		LEGACY_VANILLA_TILE_ENTITY_IDS.put("Music", "noteblock");
		LEGACY_VANILLA_TILE_ENTITY_IDS.put("Piston", "piston");
		LEGACY_VANILLA_TILE_ENTITY_IDS.put("RecordPlayer", "jukebox");
		LEGACY_VANILLA_TILE_ENTITY_IDS.put("Sign", "sign");
		LEGACY_VANILLA_TILE_ENTITY_IDS.put("Skull", "skull");
		LEGACY_VANILLA_TILE_ENTITY_IDS.put("Structure", "structure_block");
		LEGACY_VANILLA_TILE_ENTITY_IDS.put("Trap", "dispenser");
	}

	private LegacyWorldDataHook() {
	}

	public static void onServerAboutToStart(FMLServerAboutToStartEvent event) {
		File levelDat = event.getServer().getStorageSource()
				.getFile(event.getServer().getLevelIdName(), "level.dat");
		prepareLegacyWorld(levelDat);
	}

	private static synchronized void prepareLegacyWorld(File levelDat) {
		legacyWorldActive = false;
		if (!levelDat.isFile()) {
			return;
		}

		try (FileInputStream input = new FileInputStream(levelDat)) {
			CompoundNBT root = CompressedStreamTools.readCompressed(input);
			if (root.contains("FML", 10)) {
				CompoundNBT registries = root.getCompound("FML").getCompound("Registries");
				if (registries.contains("minecraft:blocks", 10)) {
					CompoundNBT blocks = registries.getCompound("minecraft:blocks");
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
			try (FileInputStream input = new FileInputStream(sidecar)) {
				install(levelDat.getParentFile(), CompressedStreamTools.readCompressed(input).getCompound("Blocks"));
			} catch (IOException e) {
				LOGGER.warn("Could not read legacy Iron Age Furniture registry sidecar '{}'", sidecar, e);
			}
		}
	}

	private static void install(File worldDirectory, CompoundNBT blockSnapshot) {
		int mappedStates = installLegacyBlockStates(blockSnapshot);
		legacyWorldActive = mappedStates > 0;
		if (legacyWorldActive) {
			LOGGER.info("Prepared {} legacy Iron Age Furniture block states from '{}'", mappedStates,
					worldDirectory);
		}
	}

	private static void writeSidecar(File worldDirectory, CompoundNBT blockSnapshot) {
		File sidecar = sidecar(worldDirectory);
		if (sidecar.isFile()) {
			return;
		}
		File parent = sidecar.getParentFile();
		File temporary = new File(parent, SIDECAR_NAME + ".tmp");
		try {
			Files.createDirectories(parent.toPath());
			CompoundNBT root = new CompoundNBT();
			root.put("Blocks", blockSnapshot.copy());
			try (FileOutputStream output = new FileOutputStream(temporary)) {
				CompressedStreamTools.writeCompressed(root, output);
			}
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
	public static void prepareLegacyChunk(CompoundNBT root) {
		if (!legacyWorldActive || root == null || !root.contains("Level", 10)) {
			return;
		}

		CompoundNBT level = root.getCompound("Level");
		LegacyPaddedItemMigration.migrateChunkContents(level);
		if (!containsLegacyIafBlock(level)) {
			return;
		}
		ensurePaddedBenchTileEntities(level, root.getInt("DataVersion") < 704);
		level.putBoolean("TerrainPopulated", true);
		level.putBoolean("LightPopulated", true);
		level.putBoolean(PRESERVE_CHUNK_MARKER, true);
	}

	/** Called by the chunk-loader coremod after vanilla datafixing. */
	public static CompoundNBT finalizeLegacyChunk(CompoundNBT root) {
		if (root == null || !root.contains("Level", 10)) {
			return root;
		}
		CompoundNBT level = root.getCompound("Level");
		if (legacyWorldActive) {
			normalizeLegacyVanillaTileEntityIds(level);
		}
		if (level.getBoolean(PRESERVE_CHUNK_MARKER)) {
			restorePaddedBenchTileEntityIds(level);
			level.putString("Status", "full");
			level.remove(PRESERVE_CHUNK_MARKER);
		}
		return root;
	}

	/*
	 * Forge 1.10 registry processing can turn vanilla IDs such as "Chest" into
	 * "minecraft:Chest" before Mojang's legacy-ID fixer runs. The fixer only
	 * recognises the un-namespaced spelling, so restore its intended final IDs.
	 */
	static int normalizeLegacyVanillaTileEntityIds(CompoundNBT level) {
		int changed = 0;
		ListNBT tileEntities = level.getList("TileEntities", 10);
		for (int index = 0; index < tileEntities.size(); ++index) {
			CompoundNBT tileEntity = tileEntities.getCompound(index);
			String id = tileEntity.getString("id");
			String legacyPath = id.startsWith("minecraft:") ? id.substring("minecraft:".length()) : id;
			String modernPath = LEGACY_VANILLA_TILE_ENTITY_IDS.get(legacyPath);
			if (modernPath != null) {
				tileEntity.putString("id", "minecraft:" + modernPath);
				++changed;
			}
		}
		return changed;
	}

	private static int installLegacyBlockStates(CompoundNBT blockSnapshot) {
		LEGACY_IAF_BLOCK_IDS.clear();
		LEGACY_PADDED_BLOCK_IDS.clear();
		Map<ResourceLocation, Integer> iafIds = new HashMap<>();
		Map<String, Integer> fallbackCounts = new HashMap<>();
		ListNBT savedIds = blockSnapshot.getList("ids", 10);
		int highestStateId = 0;
		for (int index = 0; index < savedIds.size(); ++index) {
			CompoundNBT savedId = savedIds.getCompound(index);
			String key = savedId.getString("K");
			ResourceLocation id = new ResourceLocation(key);
			if (!LegacyPaddedBenchIds.isLegacyNamespace(id.getNamespace())) {
				continue;
			}
			int numericId = savedId.getInt("V");
			iafIds.put(id, numericId);
			String fallback = LegacyPaddedBenchIds.retiredBygFallback(id.getPath());
			if (fallback != null) {
				fallbackCounts.put(fallback, fallbackCounts.getOrDefault(fallback, 0) + 1);
			}
			highestStateId = Math.max(highestStateId, (numericId << 4) | 15);
		}
		Dynamic<?>[] legacyStates = expandFlatteningTable(highestStateId + 1);
		int mapped = 0;
		for (Map.Entry<ResourceLocation, Integer> entry : iafIds.entrySet()) {
			ResourceLocation oldId = entry.getKey();
			Block block = resolveCurrentBlock(oldId);
			if (block == null) {
				LOGGER.warn("Legacy Iron Age Furniture block '{}' has no supported 1.15 replacement", oldId);
				continue;
			}
			LEGACY_IAF_BLOCK_IDS.set(entry.getValue());
			if (LegacyPaddedBenchIds.isLegacyPaddedPath(oldId.getPath())) {
				LEGACY_PADDED_BLOCK_IDS.set(entry.getValue());
			}
			for (int meta = 0; meta < 16; ++meta) {
				String stateNbt = NBTUtil.writeBlockState(legacyState(block, meta)).toString();
				int stateId = (entry.getValue() << 4) | meta;
				legacyStates[stateId] = BlockStateFlatteningMap.parse(stateNbt);
				++mapped;
			}
		}
		if (!fallbackCounts.isEmpty()) {
			LOGGER.info("Legacy Oh The Biomes furniture uses these visual fallbacks: {}", fallbackCounts);
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

	private static boolean containsLegacyIafBlock(CompoundNBT level) {
		ListNBT sections = level.getList("Sections", 10);
		for (int sectionIndex = 0; sectionIndex < sections.size(); ++sectionIndex) {
			CompoundNBT section = sections.getCompound(sectionIndex);
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

	private static void ensurePaddedBenchTileEntities(CompoundNBT level, boolean legacyTileEntityIds) {
		ListNBT tileEntities = level.getList("TileEntities", 10);
		Set<Long> occupied = new HashSet<>();
		for (int index = 0; index < tileEntities.size(); ++index) {
			CompoundNBT tileEntity = tileEntities.getCompound(index);
			occupied.add(positionKey(tileEntity.getInt("x"), tileEntity.getInt("y"), tileEntity.getInt("z")));
			int blockId = getLegacyBlockId(level, tileEntity.getInt("x"), tileEntity.getInt("y"),
					tileEntity.getInt("z"));
			if (LEGACY_PADDED_BLOCK_IDS.get(blockId)) {
				markPaddedBenchTileEntity(tileEntity, legacyTileEntityIds);
			}
		}

		int chunkX = level.getInt("xPos");
		int chunkZ = level.getInt("zPos");
		ListNBT sections = level.getList("Sections", 10);
		for (int sectionIndex = 0; sectionIndex < sections.size(); ++sectionIndex) {
			CompoundNBT section = sections.getCompound(sectionIndex);
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
				CompoundNBT tileEntity = new CompoundNBT();
				markPaddedBenchTileEntity(tileEntity, legacyTileEntityIds);
				tileEntity.putInt("x", x);
				tileEntity.putInt("y", y);
				tileEntity.putInt("z", z);
				tileEntity.putString(LegacyPaddedBenchIds.COLOR_TAG, "red");
				tileEntities.add(tileEntity);
			}
		}
		level.put("TileEntities", tileEntities);
	}

	private static void markPaddedBenchTileEntity(CompoundNBT tileEntity, boolean legacyTileEntityIds) {
		// A sign is present in every vanilla schema crossed by a 1.12-to-1.14
		// upgrade and its data fixer preserves fields it does not recognise. The
		// private marker prevents this temporary identity from escaping the chunk
		// conversion; finalizeLegacyChunk restores the actual registered ID before
		// Minecraft constructs block entities.
		tileEntity.putString("id", legacyTileEntityIds ? "Sign" : "minecraft:sign");
		tileEntity.putBoolean(PADDED_TILE_MARKER, true);
	}

	private static void restorePaddedBenchTileEntityIds(CompoundNBT level) {
		ListNBT tileEntities = level.getList("TileEntities", 10);
		for (int index = 0; index < tileEntities.size(); ++index) {
			CompoundNBT tileEntity = tileEntities.getCompound(index);
			if (!tileEntity.getBoolean(PADDED_TILE_MARKER)) {
				continue;
			}
			tileEntity.putString("id", LegacyPaddedBenchIds.TILE_ENTITY_ID.toString());
			tileEntity.remove(PADDED_TILE_MARKER);
		}
	}

	private static long positionKey(int x, int y, int z) {
		return ((long)x & 0x3FFFFFFL) << 38 | ((long)z & 0x3FFFFFFL) << 12 | (long)y & 0xFFFL;
	}

	private static int getLegacyBlockId(CompoundNBT level, int x, int y, int z) {
		if (y < 0 || y > 255) {
			return -1;
		}
		ListNBT sections = level.getList("Sections", 10);
		for (int sectionIndex = 0; sectionIndex < sections.size(); ++sectionIndex) {
			CompoundNBT section = sections.getCompound(sectionIndex);
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
			String fieldName = ObfuscationReflectionHelper.remapName(INameMappingService.Domain.FIELD,
					"field_199200_b");
			Field valuesField = BlockStateFlatteningMap.class.getDeclaredField(fieldName);
			valuesField.setAccessible(true);
			Field modifiersField = Field.class.getDeclaredField("modifiers");
			modifiersField.setAccessible(true);
			modifiersField.setInt(valuesField, valuesField.getModifiers() & ~Modifier.FINAL);
			Dynamic<?>[] current = (Dynamic<?>[])valuesField.get(null);
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
