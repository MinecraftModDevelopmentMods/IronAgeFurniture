package zone.moddev.mc.ironagefurniture.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import zone.moddev.mc.ironagefurniture.api.MetalVariantHelper.MetalVariant;

public class MetalVariantToughnessTest {
	@Test
	public void metalChangesMiningHardnessWithoutChangingIron() {
		float iron = MetalVariant.IRON.scaleHardness(1.0F);
		assertEquals(1.0F, iron, 0.001F);
		assertTrue(MetalVariant.GOLD.scaleHardness(1.0F) < iron);
		assertTrue(MetalVariant.ADAMANTINE.scaleHardness(1.0F) > iron);
	}

	@Test
	public void metalChangesExplosionResistanceWithoutChangingIron() {
		float iron = MetalVariant.IRON.scaleResistance(2.0F);
		assertEquals(2.0F, iron, 0.001F);
		assertTrue(MetalVariant.GOLD.scaleResistance(2.0F) < iron);
		assertTrue(MetalVariant.ADAMANTINE.scaleResistance(2.0F) > iron);
	}
}
