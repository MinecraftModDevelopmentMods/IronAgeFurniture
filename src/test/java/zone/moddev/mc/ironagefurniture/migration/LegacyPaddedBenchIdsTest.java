package zone.moddev.mc.ironagefurniture.migration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import net.minecraft.resources.ResourceLocation;
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
	public void importsTheLegacyBygAddonNamespace() {
		ResourceLocation addon = new ResourceLocation("iafbygaddon",
				"chair_wood_ironage_classic_byg_aspen");
		assertEquals(new ResourceLocation("ironagefurniture",
				"chair_wood_ironage_classic_byg_aspen"), LegacyPaddedBenchIds.currentId(addon));
		assertTrue(LegacyPaddedBenchIds.isLegacyNamespace(addon.getNamespace()));
	}

	@Test
	public void mapsRetiredBygWoodsToDocumentedVisualFallbacks() {
		assertBygFallback("frozen_oak", "byg_aspen");
		assertBygFallback("great_oak", "oak");
		assertBygFallback("hawthorn", "byg_cherry");
		assertBygFallback("ironwood", "byg_ebony");
		assertBygFallback("palm", "byg_baobab");
		assertBygFallback("rowan", "byg_maple");
	}

	private static void assertBygFallback(String legacyWood, String targetWood) {
		ResourceLocation legacy = new ResourceLocation("iafbygaddon",
				"chair_wood_ironage_bench_padded_single_byg_" + legacyWood);
		assertEquals(new ResourceLocation("ironagefurniture",
				"chair_wood_ironage_bench_padded_single_" + targetWood),
				LegacyPaddedBenchIds.currentId(legacy));
	}
}
