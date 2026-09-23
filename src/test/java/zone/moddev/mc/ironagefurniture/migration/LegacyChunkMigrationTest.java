package zone.moddev.mc.ironagefurniture.migration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.util.BitSet;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import zone.moddev.mc.ironagefurniture.api.enumerations.BenchType;

public class LegacyChunkMigrationTest {
	@Test
	public void normalizesForgeNamespacedVanilla110TileEntities() {
		CompoundNBT level = new CompoundNBT();
		ListNBT tileEntities = new ListNBT();
		CompoundNBT chest = new CompoundNBT();
		chest.putString("id", "minecraft:Chest");
		tileEntities.add(chest);
		CompoundNBT furnace = new CompoundNBT();
		furnace.putString("id", "Furnace");
		tileEntities.add(furnace);
		CompoundNBT modern = new CompoundNBT();
		modern.putString("id", "minecraft:barrel");
		tileEntities.add(modern);
		level.put("TileEntities", tileEntities);

		assertEquals(2, LegacyWorldDataHook.normalizeLegacyVanillaTileEntityIds(level));
		assertEquals("minecraft:chest", tileEntities.getCompound(0).getString("id"));
		assertEquals("minecraft:furnace", tileEntities.getCompound(1).getString("id"));
		assertEquals("minecraft:barrel", tileEntities.getCompound(2).getString("id"));
	}

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
	public void injectsRedDataForReleasedBenchesWithoutColourNbt() {
		CompoundNBT root = legacyChunk(false);
		LegacyWorldDataHook.prepareLegacyChunk(root);

		CompoundNBT level = root.getCompound("Level");
		ListNBT tileEntities = level.getList("TileEntities", 10);
		assertEquals(1, tileEntities.size());
		CompoundNBT tile = tileEntities.getCompound(0);
		assertEquals("minecraft:sign", tile.getString("id"));
		assertTrue(tile.getBoolean("IronAgeFurnitureLegacyPaddedBench"));
		assertEquals("red", tile.getString("Color"));
		assertEquals(35, tile.getInt("x"));
		assertEquals(69, tile.getInt("y"));
		assertEquals(-12, tile.getInt("z"));
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
	public void preservesTheNewStableColourField() {
		CompoundNBT root = legacyChunk(true);
		LegacyWorldDataHook.prepareLegacyChunk(root);

		ListNBT tileEntities = root.getCompound("Level").getList("TileEntities", 10);
		assertEquals(1, tileEntities.size());
		assertEquals("cyan", tileEntities.getCompound(0).getString("Color"));
		assertEquals("minecraft:sign", tileEntities.getCompound(0).getString("id"));
	}

	@Test
	public void usesTheUnnamespacedMarkerFor110DataFixing() {
		CompoundNBT root = legacyChunk(true);
		root.putInt("DataVersion", 512);
		LegacyWorldDataHook.prepareLegacyChunk(root);

		ListNBT tileEntities = root.getCompound("Level").getList("TileEntities", 10);
		assertEquals("Sign", tileEntities.getCompound(0).getString("id"));
		assertEquals("cyan", tileEntities.getCompound(0).getString("Color"));
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

	private static CompoundNBT legacyChunk(boolean includeTileEntity) {
		int blockIndex = (5 << 8) | (4 << 4) | 3;
		byte[] blocks = new byte[4096];
		byte[] add = new byte[2048];
		blocks[blockIndex] = (byte)(PADDED_BLOCK_ID & 0xFF);
		int shift = (blockIndex & 1) * 4;
		add[blockIndex >> 1] = (byte)(add[blockIndex >> 1]
				| ((PADDED_BLOCK_ID >> 8) & 0x0F) << shift);

		CompoundNBT section = new CompoundNBT();
		section.putByte("Y", (byte)4);
		section.putByteArray("Blocks", blocks);
		section.putByteArray("Add", add);
		ListNBT sections = new ListNBT();
		sections.add(section);

		ListNBT tileEntities = new ListNBT();
		if (includeTileEntity) {
			CompoundNBT tile = new CompoundNBT();
			tile.putString("id", "PaddedBenchColour");
			tile.putInt("x", 35);
			tile.putInt("y", 69);
			tile.putInt("z", -12);
			tile.putString("Color", "cyan");
			tileEntities.add(tile);
		}

		CompoundNBT level = new CompoundNBT();
		level.putInt("xPos", 2);
		level.putInt("zPos", -1);
		level.put("Sections", sections);
		level.put("TileEntities", tileEntities);
		CompoundNBT root = new CompoundNBT();
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
