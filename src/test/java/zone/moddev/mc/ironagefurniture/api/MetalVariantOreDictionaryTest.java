package zone.moddev.mc.ironagefurniture.api;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import zone.moddev.mc.ironagefurniture.api.MetalVariantHelper.MetalVariant;

public class MetalVariantOreDictionaryTest {
	@Test
	public void coldIronUsesBaseMetalsRegistryCapitalization() {
		assertEquals("ingotColdiron", MetalVariant.COLDIRON.getIngotOreName());
		assertEquals("nuggetColdiron", MetalVariant.COLDIRON.getNuggetOreName());
		assertEquals("barsColdiron", MetalVariant.COLDIRON.getBarsOreName());
	}

	@Test
	public void starSteelUsesBaseMetalsRegistryCapitalization() {
		assertEquals("ingotStarsteel", MetalVariant.STARSTEEL.getIngotOreName());
		assertEquals("nuggetStarsteel", MetalVariant.STARSTEEL.getNuggetOreName());
		assertEquals("barsStarsteel", MetalVariant.STARSTEEL.getBarsOreName());
	}
}
