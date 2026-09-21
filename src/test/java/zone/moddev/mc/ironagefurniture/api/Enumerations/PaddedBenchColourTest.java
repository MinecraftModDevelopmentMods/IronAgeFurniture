package zone.moddev.mc.ironagefurniture.api.Enumerations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class PaddedBenchColourTest {
	@Test
	public void itemMetadataContractKeepsLegacyRedAtZero() {
		PaddedBenchColour[] expected = {
				PaddedBenchColour.RED,
				PaddedBenchColour.ORANGE,
				PaddedBenchColour.MAGENTA,
				PaddedBenchColour.LIGHT_BLUE,
				PaddedBenchColour.YELLOW,
				PaddedBenchColour.LIME,
				PaddedBenchColour.PINK,
				PaddedBenchColour.GRAY,
				PaddedBenchColour.LIGHT_GRAY,
				PaddedBenchColour.CYAN,
				PaddedBenchColour.PURPLE,
				PaddedBenchColour.BLUE,
				PaddedBenchColour.BROWN,
				PaddedBenchColour.GREEN,
				PaddedBenchColour.WHITE,
				PaddedBenchColour.BLACK
		};

		for (int metadata = 0; metadata < expected.length; metadata++) {
			assertSame(expected[metadata], PaddedBenchColour.byItemMetadata(metadata));
			assertEquals(metadata, expected[metadata].getItemMetadata());
		}
	}

	@Test
	public void everyVanillaCarpetMetadataMapsToItsStableItemMetadata() {
		int[] expectedItemMetadata = { 14, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 0, 15 };
		for (int carpetMetadata = 0; carpetMetadata < expectedItemMetadata.length; carpetMetadata++) {
			PaddedBenchColour colour = PaddedBenchColour.byCarpetMetadata(carpetMetadata);
			assertEquals(expectedItemMetadata[carpetMetadata], colour.getItemMetadata());
			assertEquals(carpetMetadata, colour.getCarpetMetadata());
		}
	}

	@Test
	public void namesRoundTripAndInvalidValuesFallBackToRed() {
		for (PaddedBenchColour colour : PaddedBenchColour.values()) {
			assertSame(colour, PaddedBenchColour.byName(colour.getSerializedName()));
		}
		assertSame(PaddedBenchColour.RED, PaddedBenchColour.byName(null));
		assertSame(PaddedBenchColour.RED, PaddedBenchColour.byName("not_a_colour"));
		assertSame(PaddedBenchColour.RED, PaddedBenchColour.byItemMetadata(-1));
		assertSame(PaddedBenchColour.RED, PaddedBenchColour.byItemMetadata(16));
		assertSame(PaddedBenchColour.RED, PaddedBenchColour.byCarpetMetadata(-1));
		assertSame(PaddedBenchColour.RED, PaddedBenchColour.byCarpetMetadata(16));
	}

	@Test
	public void creativeInventoryContainsEveryColourExactlyOnce() {
		PaddedBenchColour[] creativeOrder = PaddedBenchColour.creativeOrder();
		Set<PaddedBenchColour> colours = new HashSet<PaddedBenchColour>(Arrays.asList(creativeOrder));
		assertEquals(16, creativeOrder.length);
		assertEquals(16, colours.size());
		assertEquals(new HashSet<PaddedBenchColour>(Arrays.asList(PaddedBenchColour.values())), colours);
	}

	@Test
	public void legacyLightGrayTextureUsesSilverResourceName() {
		assertEquals("silver", PaddedBenchColour.LIGHT_GRAY.getLegacyTextureName());
	}
}

