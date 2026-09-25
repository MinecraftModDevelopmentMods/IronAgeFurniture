package zone.moddev.mc.ironagefurniture.migration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.util.BitSet;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import zone.moddev.mc.ironagefurniture.api.enumerations.BenchType;

public class LegacyChunkMigrationTest {
	private static final int PADDED_BLOCK_ID = 300;

	@Before
	public void activateSyntheticLegacyMapping() throws Exception {
		bitSet("LEGACY_IAF_BLOCK_IDS").set(PADDED_BLOCK_ID);
		bitSet("LEGACY_PADDED_BLOCK_IDS").set(PADDED_BLOCK_ID);
		setActive(true);
	}

	@After
	public void clearSyntheticLegacyMapping() throws Exception {
		bitSet("LEGACY_IAF_BLOCK_IDS").clear();
		bitSet("LEGACY_PADDED_BLOCK_IDS").clear();
		setActive(false);
	}

	@Test
	public void normalizesForgeNamespacedVanilla110BlockEntities() {
		CompoundTag level = new CompoundTag();
		ListTag blockEntities = new ListTag();
		CompoundTag chest = new CompoundTag();
		chest.putString("id", "minecraft:Chest");
		blockEntities.add(chest);
		CompoundTag furnace = new CompoundTag();
		furnace.putString("id", "Furnace");
		blockEntities.add(furnace);
		CompoundTag modern = new CompoundTag();
		modern.putString("id", "minecraft:barrel");
		blockEntities.add(modern);
		level.put("TileEntities", blockEntities);

		assertEquals(2, LegacyWorldDataHook.normalizeLegacyVanillaBlockEntityIds(level));
		assertEquals("minecraft:chest", blockEntities.getCompound(0).getString("id"));
		assertEquals("minecraft:furnace", blockEntities.getCompound(1).getString("id"));
		assertEquals("minecraft:barrel", blockEntities.getCompound(2).getString("id"));
	}

	@Test
	public void injectsRedDataForReleasedBenchesWithoutColourNbt() {
		CompoundTag root = legacyChunk(false);
		LegacyWorldDataHook.prepareLegacyChunk(root);

		CompoundTag level = root.getCompound("Level");
		ListTag blockEntities = level.getList("TileEntities", 10);
		assertEquals(1, blockEntities.size());
		CompoundTag blockEntity = blockEntities.getCompound(0);
		assertEquals("minecraft:sign", blockEntity.getString("id"));
		assertTrue(blockEntity.getBoolean("IronAgeFurnitureLegacyPaddedBench"));
		assertEquals("red", blockEntity.getString("Color"));
		assertEquals(35, blockEntity.getInt("x"));
		assertEquals(69, blockEntity.getInt("y"));
		assertEquals(-12, blockEntity.getInt("z"));
		assertTrue(level.getBoolean("TerrainPopulated"));
		assertTrue(level.getBoolean("LightPopulated"));

		LegacyWorldDataHook.finalizeLegacyChunk(root);
		assertEquals("full", level.getString("Status"));
		assertFalse(level.contains("IronAgeFurnitureLegacyPreserveChunk"));
		assertEquals("ironagefurniture:padded_bench_colour",
				level.getList("TileEntities", 10).getCompound(0).getString("id"));
		assertFalse(level.getList("TileEntities", 10).getCompound(0)
				.contains("IronAgeFurnitureLegacyPaddedBench"));
	}

	@Test
	public void preservesTheStableColourField() {
		CompoundTag root = legacyChunk(true);
		LegacyWorldDataHook.prepareLegacyChunk(root);

		ListTag blockEntities = root.getCompound("Level").getList("TileEntities", 10);
		assertEquals(1, blockEntities.size());
		assertEquals("cyan", blockEntities.getCompound(0).getString("Color"));
		assertEquals("minecraft:sign", blockEntities.getCompound(0).getString("id"));
	}

	@Test
	public void usesTheUnnamespacedMarkerFor110DataFixing() {
		CompoundTag root = legacyChunk(true);
		root.putInt("DataVersion", 512);
		LegacyWorldDataHook.prepareLegacyChunk(root);

		ListTag blockEntities = root.getCompound("Level").getList("TileEntities", 10);
		assertEquals("Sign", blockEntities.getCompound(0).getString("id"));
		assertEquals("cyan", blockEntities.getCompound(0).getString("Color"));
	}

	@Test
	public void decodesEveryLegacyBenchShape() {
		for (int meta = 0; meta < 4; ++meta) {
			assertEquals(BenchType.SINGLE, LegacyWorldDataHook.benchType(meta));
			assertEquals(BenchType.MIDDLE, LegacyWorldDataHook.benchType(meta + 4));
			assertEquals(BenchType.LEFT, LegacyWorldDataHook.benchType(meta + 8));
			assertEquals(BenchType.RIGHT, LegacyWorldDataHook.benchType(meta + 12));
		}
	}

	@Test
	public void decodesEveryLegacyHorizontalFacingAndMasksShapeBits() {
		Direction[] expected = {
				Direction.SOUTH, Direction.WEST, Direction.NORTH, Direction.EAST
		};
		for (int meta = 0; meta < 16; ++meta) {
			assertEquals(expected[meta & 3], LegacyWorldDataHook.direction(meta));
		}
	}

	@Test
	public void finalizationIsSafeToRepeat() {
		CompoundTag root = legacyChunk(false);
		LegacyWorldDataHook.prepareLegacyChunk(root);
		LegacyWorldDataHook.finalizeLegacyChunk(root);
		LegacyWorldDataHook.finalizeLegacyChunk(root);

		CompoundTag level = root.getCompound("Level");
		assertEquals("full", level.getString("Status"));
		assertFalse(level.contains("IronAgeFurnitureLegacyPreserveChunk"));
		assertEquals(1, level.getList("TileEntities", 10).size());
		assertEquals("ironagefurniture:padded_bench_colour",
				level.getList("TileEntities", 10).getCompound(0).getString("id"));
	}

	@Test
	public void restoresMarkedBlockEntityAfterThe118ChunkLayoutConversion() {
		CompoundTag legacyRoot = legacyChunk(true);
		LegacyWorldDataHook.prepareLegacyChunk(legacyRoot);

		CompoundTag modernRoot = new CompoundTag();
		modernRoot.put("block_entities",
				legacyRoot.getCompound("Level").getList("TileEntities", 10).copy());
		LegacyWorldDataHook.finalizeLegacyChunk(modernRoot);
		LegacyWorldDataHook.finalizeLegacyChunk(modernRoot);

		ListTag blockEntities = modernRoot.getList("block_entities", 10);
		assertEquals(1, blockEntities.size());
		assertEquals("ironagefurniture:padded_bench_colour",
				blockEntities.getCompound(0).getString("id"));
		assertEquals("cyan", blockEntities.getCompound(0).getString("Color"));
		assertFalse(blockEntities.getCompound(0).contains("IronAgeFurnitureLegacyPaddedBench"));
	}

	private static CompoundTag legacyChunk(boolean includeBlockEntity) {
		int blockIndex = (5 << 8) | (4 << 4) | 3;
		byte[] blocks = new byte[4096];
		byte[] add = new byte[2048];
		blocks[blockIndex] = (byte)(PADDED_BLOCK_ID & 0xFF);
		int shift = (blockIndex & 1) * 4;
		add[blockIndex >> 1] = (byte)(add[blockIndex >> 1]
				| ((PADDED_BLOCK_ID >> 8) & 0x0F) << shift);

		CompoundTag section = new CompoundTag();
		section.putByte("Y", (byte)4);
		section.putByteArray("Blocks", blocks);
		section.putByteArray("Add", add);
		ListTag sections = new ListTag();
		sections.add(section);

		ListTag blockEntities = new ListTag();
		if (includeBlockEntity) {
			CompoundTag blockEntity = new CompoundTag();
			blockEntity.putString("id", "PaddedBenchColour");
			blockEntity.putInt("x", 35);
			blockEntity.putInt("y", 69);
			blockEntity.putInt("z", -12);
			blockEntity.putString("Color", "cyan");
			blockEntities.add(blockEntity);
		}

		CompoundTag level = new CompoundTag();
		level.putInt("xPos", 2);
		level.putInt("zPos", -1);
		level.put("Sections", sections);
		level.put("TileEntities", blockEntities);
		CompoundTag root = new CompoundTag();
		root.putInt("DataVersion", 1343);
		root.put("Level", level);
		return root;
	}

	private static BitSet bitSet(String fieldName) throws Exception {
		Field field = LegacyWorldDataHook.class.getDeclaredField(fieldName);
		field.setAccessible(true);
		return (BitSet)field.get(null);
	}

	private static void setActive(boolean active) throws Exception {
		Field field = LegacyWorldDataHook.class.getDeclaredField("legacyWorldActive");
		field.setAccessible(true);
		field.setBoolean(null, active);
	}
}
