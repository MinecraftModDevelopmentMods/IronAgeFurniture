package zone.moddev.mc.ironagefurniture.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import zone.moddev.mc.ironagefurniture.api.MetalVariantHelper.MetalVariant;

public class MetalVariantToughnessTest {
    @Test public void metalChangesMiningHardnessFromIronSconceBaseline() {
        assertEquals(4.0F, MetalVariant.IRON.scaleHardness(4.0F), 0.001F);
        assertEquals(0.5F, MetalVariant.GOLD.scaleHardness(4.0F), 0.001F);
        assertEquals(6.0F, MetalVariant.ADAMANTINE.scaleHardness(4.0F), 0.001F);
    }

    @Test public void metalChangesExplosionResistanceWithoutChangingIron() {
        float iron = MetalVariant.IRON.scaleResistance(2.0F);
        assertEquals(2.0F, iron, 0.001F);
        assertTrue(MetalVariant.GOLD.scaleResistance(2.0F) < iron);
        assertTrue(MetalVariant.ADAMANTINE.scaleResistance(2.0F) > iron);
    }
}
