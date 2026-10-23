package zone.moddev.mc.ironagefurniture.migration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.BitSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.mojang.serialization.Dynamic;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraft.world.level.storage.LevelStorageSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import zone.moddev.mc.ironagefurniture.Ironagefurniture;
import zone.moddev.mc.ironagefurniture.api.blocks.base.FurnitureBlock;
import zone.moddev.mc.ironagefurniture.api.blocks.furniture.BackBench;
import zone.moddev.mc.ironagefurniture.api.enumerations.BenchType;

/** Converts pre-flattening Iron Age Furniture block IDs before vanilla chunk datafixing. */
@net.neoforged.fml.common.EventBusSubscriber(modid = Ironagefurniture.MODID)
public final class LegacyWorldDataHook {
	private static final Logger LOGGER = LogManager.getLogger();
	private static final BitSet LEGACY_IAF_BLOCK_IDS = new BitSet();
	private static final BitSet LEGACY_PADDED_BLOCK_IDS = new BitSet();
    private static final BitSet LEGACY_CFM_BLOCK_IDS = new BitSet();
	private static final Map<String, String> LEGACY_VANILLA_TILE_ENTITY_IDS = new HashMap<>();
	private static final String PRESERVE_CHUNK_MARKER = "IronAgeFurnitureLegacyPreserveChunk";
	private static final String PADDED_TILE_MARKER = "IronAgeFurnitureLegacyPaddedBench";
	private static final String SIDECAR_NAME = "ironagefurniture_legacy_registry.dat";
	private static volatile boolean legacyWorldActive;
    private static final Map<Integer, Dynamic<?>> ORIGINAL_STATES = new HashMap<>();
    private static final Map<Integer, Dynamic<?>> INSTALLED_STATES = new HashMap<>();

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

    @net.neoforged.bus.api.SubscribeEvent
    public static synchronized void stopped(net.neoforged.neoforge.event.server.ServerStoppedEvent event) {
        restoreInstalledStates();
        LEGACY_IAF_BLOCK_IDS.clear(); LEGACY_PADDED_BLOCK_IDS.clear(); LEGACY_CFM_BLOCK_IDS.clear();
        legacyWorldActive = false;
    }
    private static void restoreInstalledStates() {
        Dynamic<?>[] states = zone.moddev.mc.ironagefurniture.mixin.BlockStateDataAccessor.iaf$getLegacyStateMap();
        INSTALLED_STATES.forEach((index, installed) -> {
            // Another migration mod may own an entry now; do not overwrite its work.
            if (states[index] == installed) states[index] = ORIGINAL_STATES.get(index);
        });
        INSTALLED_STATES.clear(); ORIGINAL_STATES.clear();
    }

    /** Save readers call this before vanilla can discard old item subtype data. */
    public static void preparePlayerData(CompoundTag player) {
        if (player == null) return;
        LegacyPaddedItemMigration.migrateChunkContents(player);
        CfmChairMigration.migratePlayerData(player);
        LegacyRecipeBookMigration.prepare(player);
    }

    public static void prepareLevelPlayerData(CompoundTag root) {
        if (root != null && LegacyNbt.compound(root, "Data").contains("Player"))
            preparePlayerData(LegacyNbt.compound(LegacyNbt.compound(root, "Data"), "Player"));
    }

	@net.neoforged.bus.api.SubscribeEvent
    public static void onServerAboutToStart(net.neoforged.neoforge.event.server.ServerAboutToStartEvent event) {
		if (legacyWorldActive) {
			return;
		}
		File levelDat = event.getServer().getWorldPath(LevelResource.LEVEL_DATA_FILE).toFile();
		prepareLegacyWorld(levelDat);
	}

	/**
	 * Captures the pre-flattening Forge registry snapshot while Minecraft is still
	 * reading the original level data. By the normal server lifecycle events,
	 * Forge has already rewritten that data into its current format.
	 */
	public static synchronized void captureLegacyLevelData(CompoundTag root, LevelStorageSource.LevelDirectory levelSave) {
		if (levelSave == null || root == null) {
			return;
		}
		// Forge 1.12 used the upper-case key. Current Forge uses lower-case
		// "fml", whose snapshot contains current palette IDs and must not be fed
		// to the pre-flattening block-state table.
		if (!root.contains("FML")) {
			return;
		}
		CompoundTag forgeData = LegacyNbt.compound(root, "FML");
		if (!forgeData.contains("Registries")) {
			return;
		}
		CompoundTag registries = LegacyNbt.compound(forgeData, "Registries");
		if (!registries.contains("minecraft:blocks")) {
			return;
		}
		File worldDirectory = levelSave.path().toFile();
		CompoundTag blocks = LegacyNbt.compound(registries, "minecraft:blocks");
		install(worldDirectory, blocks);
		writeSidecar(worldDirectory, blocks);
	}

	private static synchronized void prepareLegacyWorld(File levelDat) {
		legacyWorldActive = false;
		if (!levelDat.isFile()) {
			return;
		}

		try (FileInputStream input = new FileInputStream(levelDat)) {
			CompoundTag root = NbtIo.readCompressed(input, net.minecraft.nbt.NbtAccounter.unlimitedHeap());
			if (root.contains("FML")) {
				CompoundTag registries = LegacyNbt.compound(LegacyNbt.compound(root, "FML"), "Registries");
				if (registries.contains("minecraft:blocks")) {
					CompoundTag blocks = LegacyNbt.compound(registries, "minecraft:blocks");
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
				install(levelDat.getParentFile(), LegacyNbt.compound(NbtIo.readCompressed(input, net.minecraft.nbt.NbtAccounter.unlimitedHeap()), "Blocks"));
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
			try (FileOutputStream output = new FileOutputStream(temporary)) {
				NbtIo.writeCompressed(root, output);
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

    /** Called before vanilla reads the separately saved entity-region stacks. */
    public static void prepareEntityData(CompoundTag root) {
        if (root == null) return;
        // Since 1.17, saved entities are no longer inside the terrain chunk.
        // Convert their stored stacks before an absent provider becomes air.
        CfmChairMigration.migrateEntityData(root);
        LegacyPaddedItemMigration.migrateChunkContents(root);
    }

	/** Called by the chunk-loader coremod immediately before vanilla datafixing. */
	public static void prepareLegacyChunk(CompoundTag root) {
		if (root == null || !root.contains("Level")) {
			return;
		}

		CompoundTag level = LegacyNbt.compound(root, "Level");
        CfmChairMigration.prepareChunk(level);
		boolean migratedItems = LegacyPaddedItemMigration.migrateChunkContents(level);
        if (!legacyWorldActive || !isPreFlatteningChunk(root)) return;
        // A chest or dropped item may live in a chunk with no placed IAF
        // blocks. Preserve that chunk too, rather than regenerate its contents
        // while upgrading an old, not-yet-lighted chunk.
		if (!containsLegacyIafBlock(level) && !migratedItems && !LegacyPaddedItemMigration.containsFurnitureItems(level)) {
			return;
		}
		ensurePaddedBenchTileEntities(level, LegacyNbt.integer(root, "DataVersion", 0) < 704);
        CfmChairMigration.recordLegacyBlocks(countLegacyCfmBlocks(level));
		LegacyUpholsteryMigration.prepareTiles(level, LegacyNbt.integer(root, "DataVersion", 0) < 704);
		level.putBoolean("TerrainPopulated", true);
		level.putBoolean("LightPopulated", true);
		level.putBoolean(PRESERVE_CHUNK_MARKER, true);
	}

	/** Called by the chunk-loader coremod after vanilla datafixing. */
	public static CompoundTag finalizeLegacyChunk(CompoundTag root) {
		if (root == null || !root.contains("Level")) {
			return root;
		}
		CompoundTag level = LegacyNbt.compound(root, "Level");
		LegacyPaddedItemMigration.migrateChunkContents(level);
		NativeLegacyBridge.restoreTileColours(level);
		if (legacyWorldActive) {
			normalizeLegacyVanillaTileEntityIds(level);
		}
		if (LegacyNbt.bool(level, PRESERVE_CHUNK_MARKER, false)) {
			restorePaddedBenchTileEntityIds(level);
			LegacyUpholsteryMigration.finishTiles(level);
			level.putString("Status", "full");
			level.remove(PRESERVE_CHUNK_MARKER);
		}
        LegacyPaddedPaletteMigration.migrate(level);
		return root;
	}

    /** Give vanilla's pre-704 schema the IDs it expects before it reads inventories. */
    public static void prepareVanillaTileEntitySchema(CompoundTag root) {
        if (root == null || LegacyNbt.integer(root, "DataVersion", 0) >= 704 || !root.contains("Level")) return;
        // Some optional migration hooks rename vanilla containers early. The
        // old schema knows "Chest", not "minecraft:chest", and otherwise loses
        // its Items before the later ID-renaming fixer can handle them.
        for (Tag value : LegacyNbt.list(LegacyNbt.compound(root, "Level"), "TileEntities")) {
            CompoundTag tile = (CompoundTag)value;
            String id = LegacyNbt.string(tile, "id", "");
            for (Map.Entry<String, String> entry : LEGACY_VANILLA_TILE_ENTITY_IDS.entrySet()) {
                if (id.equals("minecraft:" + entry.getValue()) || id.equals("minecraft:" + entry.getKey())) {
                    tile.putString("id", entry.getKey());
                    break;
                }
            }
        }
    }

	/*
	 * Forge 1.10 registry processing can turn vanilla IDs such as "Chest" into
	 * "minecraft:Chest" before Mojang's legacy-ID fixer runs. The fixer only
	 * recognises the un-namespaced spelling, so restore its intended final IDs.
	 */
	static int normalizeLegacyVanillaTileEntityIds(CompoundTag level) {
		int changed = 0;
		ListTag tileEntities = LegacyNbt.list(level, "TileEntities");
		for (int index = 0; index < tileEntities.size(); ++index) {
			CompoundTag tileEntity = LegacyNbt.compound(tileEntities, index);
			String id = LegacyNbt.string(tileEntity, "id", "");
			String legacyPath = id.startsWith("minecraft:") ? id.substring("minecraft:".length()) : id;
			String modernPath = LEGACY_VANILLA_TILE_ENTITY_IDS.get(legacyPath);
			if (modernPath != null) {
				tileEntity.putString("id", "minecraft:" + modernPath);
				++changed;
			}
		}
		return changed;
	}

	private static int installLegacyBlockStates(CompoundTag blockSnapshot) {
        restoreInstalledStates();
		LEGACY_IAF_BLOCK_IDS.clear();
		LEGACY_PADDED_BLOCK_IDS.clear();
        LEGACY_CFM_BLOCK_IDS.clear();
		Map<ResourceLocation, Integer> iafIds = new HashMap<>();
		ListTag savedIds = LegacyNbt.list(blockSnapshot, "ids");
		int highestStateId = 0;
		for (int index = 0; index < savedIds.size(); ++index) {
			CompoundTag savedId = LegacyNbt.compound(savedIds, index);
			String key = LegacyNbt.string(savedId, "K", "");
			if (!key.startsWith("ironagefurniture:") && !key.startsWith("iafbygaddon:")
					&& CfmChairMigration.legacyTarget(key) == null) {
				continue;
			}
			ResourceLocation id = new ResourceLocation(key);
			if (!LegacyPaddedBenchIds.isLegacyNamespace(id.getNamespace()) && CfmChairMigration.legacyTarget(key) == null) {
				continue;
			}
			int numericId = LegacyNbt.integer(savedId, "V", 0);
			iafIds.put(id, numericId);
			highestStateId = Math.max(highestStateId, (numericId << 4) | 15);
		}
		Dynamic<?>[] legacyStates = expandFlatteningTable(highestStateId + 1);
		int mapped = 0;
		for (Map.Entry<ResourceLocation, Integer> entry : iafIds.entrySet()) {
			ResourceLocation oldId = entry.getKey();
            String cfmTarget = CfmChairMigration.legacyTarget(oldId.toString());
			Block block = cfmTarget == null ? resolveCurrentBlock(oldId)
                    : net.minecraft.core.registries.BuiltInRegistries.BLOCK.get(new ResourceLocation(cfmTarget));
			if (block == null) {
				LOGGER.warn("Legacy Iron Age Furniture block '{}' has no supported 1.20.6 replacement", oldId);
				continue;
			}
			LEGACY_IAF_BLOCK_IDS.set(entry.getValue());
            if (cfmTarget != null) LEGACY_CFM_BLOCK_IDS.set(entry.getValue());
			if (LegacyPaddedBenchIds.isLegacyPaddedPath(oldId.getPath())) {
				LEGACY_PADDED_BLOCK_IDS.set(entry.getValue());
			}
			for (int meta = 0; meta < 16; ++meta) {
				CompoundTag oldState = NbtUtils.writeBlockState(legacyState(oldId, block, meta));
                // Native 1.20.6 already emits Name/Properties. Never replace that
                // valid identity with an absent newer-codec id value.
                if (oldState.contains("id")) oldState.putString("Name", LegacyNbt.string(oldState, "id", ""));
                if (LegacyPaddedBenchIds.isLegacyPaddedPath(oldId.getPath()) || oldId.getPath().contains("candle")) oldState.putString("Name", oldId.toString());
                oldState.remove("id");
                if (oldState.contains("properties")) { oldState.put("Properties", LegacyNbt.compound(oldState, "properties")); oldState.remove("properties"); }
				int stateId = (entry.getValue() << 4) | meta;
                if (!ORIGINAL_STATES.containsKey(stateId)) ORIGINAL_STATES.put(stateId, legacyStates[stateId]);
                Dynamic<?> installed = new Dynamic<>(net.minecraft.nbt.NbtOps.INSTANCE, oldState);
                INSTALLED_STATES.put(stateId, installed);
				legacyStates[stateId] = installed;
				++mapped;
			}
		}
		return mapped;
	}

    private static int countLegacyCfmBlocks(CompoundTag level) {
        int count = 0;
        for (net.minecraft.nbt.Tag entry : LegacyNbt.list(level, "Sections")) {
            CompoundTag section = (CompoundTag)entry;
            byte[] blocks = section.getByteArray("Blocks"), add = section.getByteArray("Add");
            if (blocks.length != 4096) continue;
            for (int index = 0; index < 4096; index++) if (LEGACY_CFM_BLOCK_IDS.get(blockId(blocks, add, index))) count++;
        }
        return count;
    }

	private static Block resolveCurrentBlock(ResourceLocation oldId) {
        if (oldId.getPath().startsWith("light_metal_ironage_") && oldId.getPath().contains("candle")) {
            boolean wall = oldId.getPath().contains("_wall");
            if (!wall && oldId.getPath().startsWith("light_metal_ironage_candle_")) return net.minecraft.world.level.block.Blocks.CANDLE;
            return wall ? zone.moddev.mc.ironagefurniture.init.PhaseFourLighting.WALL_CANDLES.get()
                    : zone.moddev.mc.ironagefurniture.init.PhaseFourLighting.FLOOR_CANDLES.get();
        }
		ResourceLocation target = LegacyPaddedBenchIds.currentId(oldId);
        if (LegacyPaddedBenchIds.isLegacyPaddedPath(oldId.getPath()))
            target = new ResourceLocation("ironagefurniture", LegacyPaddedBenchIds.modernPaddedPath(oldId, "red"));
		return net.minecraft.core.registries.BuiltInRegistries.BLOCK.containsKey(target) ? net.minecraft.core.registries.BuiltInRegistries.BLOCK.get(target) : null;
	}

	static BlockState legacyState(ResourceLocation oldId, Block block, int meta) {
		BlockState state = block.defaultBlockState();
        String oldPath = oldId.getPath();
        if (oldPath.startsWith("light_metal_ironage_") && oldPath.contains("candle")) {
            int count = oldPath.contains("_four") ? 4 : oldPath.contains("_three") ? 3 : oldPath.contains("_two") ? 2 : 1;
            if (block instanceof zone.moddev.mc.ironagefurniture.api.blocks.lightsource.phasefour.CandleSconceBlock)
                state = state.setValue(zone.moddev.mc.ironagefurniture.api.blocks.lightsource.phasefour.CandleSconceBlock.COUNT, count)
                        .setValue(zone.moddev.mc.ironagefurniture.api.blocks.lightsource.phasefour.CandleSconceBlock.LIT, !oldPath.endsWith("_unlit"));
            else state = state.setValue(net.minecraft.world.level.block.CandleBlock.CANDLES, count)
                    .setValue(net.minecraft.world.level.block.CandleBlock.LIT, !oldPath.endsWith("_unlit"));
        }
		if (state.hasProperty(FurnitureBlock.DIRECTION)) {
			state = state.setValue(FurnitureBlock.DIRECTION, direction(meta));
		}
		if (state.hasProperty(FurnitureBlock.WATERLOGGED)) {
			state = state.setValue(FurnitureBlock.WATERLOGGED, Boolean.FALSE);
		}
		if (block instanceof BackBench && state.hasProperty(BackBench.TYPE)) {
			state = state.setValue(BackBench.TYPE, benchType(meta));
		}
		if (block instanceof zone.moddev.mc.ironagefurniture.api.blocks.furniture.WoodenBed) {
			state = state.setValue(net.minecraft.world.level.block.state.properties.BlockStateProperties.BED_PART,
					(meta & 4) == 0 ? net.minecraft.world.level.block.state.properties.BedPart.FOOT : net.minecraft.world.level.block.state.properties.BedPart.HEAD)
					.setValue(zone.moddev.mc.ironagefurniture.api.blocks.furniture.FurnitureBed.SIDE,
					(meta & 8) == 0 || !((zone.moddev.mc.ironagefurniture.api.blocks.furniture.WoodenBed) block).isDoubleBed()
							? zone.moddev.mc.ironagefurniture.api.enumerations.WoodBedSide.LEFT
							: zone.moddev.mc.ironagefurniture.api.enumerations.WoodBedSide.RIGHT);
		} else if (block instanceof zone.moddev.mc.ironagefurniture.api.blocks.furniture.CanopyBed) {
			state = state.setValue(zone.moddev.mc.ironagefurniture.api.blocks.furniture.FurnitureBed.PART,
                    (meta & 8) == 0 ? net.minecraft.world.level.block.state.properties.BedPart.FOOT : net.minecraft.world.level.block.state.properties.BedPart.HEAD)
					.setValue(zone.moddev.mc.ironagefurniture.api.blocks.furniture.FurnitureBed.UPPER, (meta & 4) != 0)
					.setValue(zone.moddev.mc.ironagefurniture.api.blocks.furniture.FurnitureBed.SIDE,
					net.minecraft.core.registries.BuiltInRegistries.BLOCK.getKey(block).getPath().startsWith("bed_canopy_foot_right_")
							? zone.moddev.mc.ironagefurniture.api.enumerations.WoodBedSide.RIGHT
							: zone.moddev.mc.ironagefurniture.api.enumerations.WoodBedSide.LEFT);
		} else if (block instanceof zone.moddev.mc.ironagefurniture.api.blocks.furniture.MultiBlockChair) {
			int part = (meta & 15) >> 2;
			state = state.setValue(zone.moddev.mc.ironagefurniture.api.blocks.furniture.MultiBlockChair.PART,
					part == 2 ? zone.moddev.mc.ironagefurniture.api.enumerations.ChairPart.UPPER
							: part == 1 ? zone.moddev.mc.ironagefurniture.api.enumerations.ChairPart.MIDDLE
							: zone.moddev.mc.ironagefurniture.api.enumerations.ChairPart.LOWER);
		}
        if (block instanceof zone.moddev.mc.ironagefurniture.api.blocks.furniture.FurnitureBed)
            state = state.setValue(FurnitureBlock.DIRECTION, state.getValue(FurnitureBlock.DIRECTION).getOpposite());
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
		ListTag sections = LegacyNbt.list(level, "Sections");
		for (int sectionIndex = 0; sectionIndex < sections.size(); ++sectionIndex) {
			CompoundTag section = LegacyNbt.compound(sections, sectionIndex);
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

	private static void ensurePaddedBenchTileEntities(CompoundTag level, boolean legacyTileEntityIds) {
		ListTag tileEntities = LegacyNbt.list(level, "TileEntities");
		Set<Long> occupied = new HashSet<>();
		for (int index = 0; index < tileEntities.size(); ++index) {
			CompoundTag tileEntity = LegacyNbt.compound(tileEntities, index);
			occupied.add(positionKey(LegacyNbt.integer(tileEntity, "x", 0), LegacyNbt.integer(tileEntity, "y", 0), LegacyNbt.integer(tileEntity, "z", 0)));
			int blockId = getLegacyBlockId(level, LegacyNbt.integer(tileEntity, "x", 0), LegacyNbt.integer(tileEntity, "y", 0),
					LegacyNbt.integer(tileEntity, "z", 0));
			if (blockId >= 0 && LEGACY_PADDED_BLOCK_IDS.get(blockId)) {
				markPaddedBenchTileEntity(tileEntity, legacyTileEntityIds);
			}
		}

		int chunkX = LegacyNbt.integer(level, "xPos", 0);
		int chunkZ = LegacyNbt.integer(level, "zPos", 0);
		ListTag sections = LegacyNbt.list(level, "Sections");
		for (int sectionIndex = 0; sectionIndex < sections.size(); ++sectionIndex) {
			CompoundTag section = LegacyNbt.compound(sections, sectionIndex);
			byte[] blocks = section.getByteArray("Blocks");
			if (blocks.length != 4096) {
				continue;
			}
			byte[] add = section.getByteArray("Add");
			int sectionY = LegacyNbt.byteValue(section, "Y", (byte)0) & 0xFF;
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
				CompoundTag tileEntity = new CompoundTag();
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

    static boolean isPreFlatteningChunk(CompoundTag root) {
        // A retained registry sidecar does not make a newly saved palette chunk
        // pre-flattening. Numeric tile lookups are only valid in the old format.
        return LegacyNbt.integer(root, "DataVersion", 0) < 1451;
    }

	private static void markPaddedBenchTileEntity(CompoundTag tileEntity, boolean legacyTileEntityIds) {
		NativeLegacyBridge.preserveTileColour(tileEntity);
		// A sign is present in every vanilla schema crossed by a 1.12-to-1.14
		// upgrade and its data fixer preserves fields it does not recognise. The
		// private marker prevents this temporary identity from escaping the chunk
		// conversion; finalizeLegacyChunk restores the actual registered ID before
		// Minecraft constructs block entities.
		tileEntity.putString("id", legacyTileEntityIds ? "Sign" : "minecraft:sign");
		tileEntity.putBoolean(PADDED_TILE_MARKER, true);
	}

	private static void restorePaddedBenchTileEntityIds(CompoundTag level) {
		ListTag tileEntities = LegacyNbt.list(level, "TileEntities");
		for (int index = 0; index < tileEntities.size(); ++index) {
			CompoundTag tileEntity = LegacyNbt.compound(tileEntities, index);
			if (!LegacyNbt.bool(tileEntity, PADDED_TILE_MARKER, false)) {
				continue;
			}
			tileEntity.putString("id", LegacyPaddedBenchIds.BLOCK_ENTITY_ID.toString());
			tileEntity.remove(PADDED_TILE_MARKER);
		}
	}

	private static long positionKey(int x, int y, int z) {
		return ((long)x & 0x3FFFFFFL) << 38 | ((long)z & 0x3FFFFFFL) << 12 | (long)y & 0xFFFL;
	}

	private static int getLegacyBlockId(CompoundTag level, int x, int y, int z) {
		if (y < 0 || y > 255) {
			return -1;
		}
		ListTag sections = LegacyNbt.list(level, "Sections");
		for (int sectionIndex = 0; sectionIndex < sections.size(); ++sectionIndex) {
			CompoundTag section = LegacyNbt.compound(sections, sectionIndex);
			if ((LegacyNbt.byteValue(section, "Y", (byte)0) & 0xFF) != y >> 4) {
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

    private static Dynamic<?>[] expandFlatteningTable(int length) {
        Dynamic<?>[] map = zone.moddev.mc.ironagefurniture.mixin.BlockStateDataAccessor.iaf$getLegacyStateMap();
        if (map.length < length) throw new IllegalStateException("Legacy state index exceeds Minecraft's supported block range");
        return map;
    }
}
