package zone.moddev.mc.ironagefurniture.migration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import net.minecraft.util.ResourceLocation;
import org.junit.Test;

public class LegacyPaddedBenchIdsTest {
	private static final List<String> COLOURS = Arrays.asList(
			"red", "orange", "magenta", "light_blue", "yellow", "lime", "pink", "gray",
			"light_gray", "cyan", "purple", "blue", "brown", "green", "white", "black");

	@Test
	public void mapsEveryLegacyBenchColourToTheFlattenedId() {
		String bench = "chair_wood_ironage_bench_padded_single_oak";
		String backBench = "chair_wood_ironage_bench_back_padded_single_oak";
		for (String colour : COLOURS) {
			assertEquals("chair_wood_ironage_bench_padded_" + colour + "_single_oak",
					LegacyPaddedBenchIds.modernPaddedPath(bench, colour));
			assertEquals("chair_wood_ironage_bench_back_padded_" + colour + "_single_oak",
					LegacyPaddedBenchIds.modernPaddedPath(backBench, colour));
		}
	}

	@Test
	public void mapsPreFlatteningBigOakToDarkOak() {
		assertEquals("chair_wood_ironage_bench_padded_blue_single_dark_oak",
				LegacyPaddedBenchIds.modernPaddedPath(
						"chair_wood_ironage_bench_padded_single_big_oak", "blue"));
		assertEquals(new ResourceLocation("ironagefurniture", "chair_wood_ironage_classic_dark_oak"),
				LegacyPaddedBenchIds.currentId(new ResourceLocation(
						"ironagefurniture", "chair_wood_ironage_classic_big_oak")));
		assertEquals("chair_wood_ironage_bench_padded_single_big_oak",
				LegacyPaddedBenchIds.legacyPaddedPath(false, "dark_oak"));
	}

	@Test
	public void retainsCompatibilityIdsUntilTheirColourCanBeRead() {
		ResourceLocation legacy = new ResourceLocation("ironagefurniture",
				"chair_wood_ironage_bench_back_padded_single_big_oak");
		assertEquals(legacy, LegacyPaddedBenchIds.currentId(legacy));
		assertTrue(LegacyPaddedBenchIds.isLegacyPaddedPath(legacy.getPath()));
		assertFalse(LegacyPaddedBenchIds.isLegacyPaddedPath(
				"chair_wood_ironage_bench_back_padded_red_single_dark_oak"));
	}

	@Test
	public void invalidOrMissingColoursFallBackToRed() {
		assertEquals("red", LegacyPaddedBenchIds.normalizeColour(null));
		assertEquals("red", LegacyPaddedBenchIds.normalizeColour("not_a_colour"));
		assertEquals("light_gray", LegacyPaddedBenchIds.normalizeColour("LIGHT_GRAY"));
	}

	@Test
	public void migratesSurvivingOhTheBiomesAddonIdsIntoTheEmbeddedCatalog() {
		ResourceLocation oldId = new ResourceLocation("iafbygaddon",
				"chair_wood_ironage_classic_byg_aspen");
		assertEquals(new ResourceLocation("ironagefurniture",
				"chair_wood_ironage_classic_byg_aspen"),
				LegacyPaddedBenchIds.currentId(oldId));
		assertEquals("chair_wood_ironage_bench_padded_cyan_single_byg_aspen",
				LegacyPaddedBenchIds.modernPaddedPath(new ResourceLocation("iafbygaddon",
						"chair_wood_ironage_bench_padded_single_byg_aspen"), "cyan"));
	}

	@Test
	public void mapsRetiredOhTheBiomesWoodsToDocumentedVisualFallbacks() {
		assertAddonFallback("frozen_oak", "byg_aspen");
		assertAddonFallback("great_oak", "oak");
		assertAddonFallback("hawthorn", "byg_cherry");
		assertAddonFallback("ironwood", "byg_ebony");
		assertAddonFallback("palm", "byg_baobab");
		assertAddonFallback("rowan", "byg_maple");
	}

	private static void assertAddonFallback(String oldWood, String currentWood) {
		String oldPath = "chair_wood_ironage_bench_back_padded_single_byg_" + oldWood;
		String expectedSuffix = "oak".equals(currentWood) ? "_oak" : "_" + currentWood;
		assertEquals("chair_wood_ironage_bench_back_padded_blue_single" + expectedSuffix,
				LegacyPaddedBenchIds.modernPaddedPath(
						new ResourceLocation("iafbygaddon", oldPath), "blue"));
		assertTrue(LegacyPaddedBenchIds.retiredBygFallback(oldPath).contains(oldWood));
	}
}
