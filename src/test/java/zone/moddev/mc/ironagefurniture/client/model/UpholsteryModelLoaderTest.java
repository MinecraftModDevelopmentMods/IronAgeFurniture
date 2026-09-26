package zone.moddev.mc.ironagefurniture.client.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import net.minecraft.util.ResourceLocation;

public class UpholsteryModelLoaderTest {
	@Test public void acceptsThePathsForgeActuallyUses() {
		assertTrue(UpholsteryModelLoader.INSTANCE.accepts(new ResourceLocation(
				"ironagefurniture", "models/block/upholstered/bed_canopy_foot_lower_oak")));
		assertTrue(UpholsteryModelLoader.INSTANCE.accepts(new ResourceLocation(
				"ironagefurniture", "models/block/upholstered/bed_wood_foot_oak")));
		assertTrue(UpholsteryModelLoader.INSTANCE.accepts(new ResourceLocation(
				"ironagefurniture", "models/item/upholstered/blue/chair_wood_ironage_wingback_oak")));
		assertFalse(UpholsteryModelLoader.INSTANCE.accepts(new ResourceLocation(
				"ironagefurniture", "models/block/bed_canopy_foot_lower_oak")));
	}

	@Test public void resolvesEveryInventoryShapeWithoutInventingModelNames() {
		assertEquals("bed_wood_single_inventory_oak",
				UpholsteryModelLoader.inventoryModelName("bed_wood_foot_oak"));
		assertEquals("bed_wood_double_inventory_oak",
				UpholsteryModelLoader.inventoryModelName("bed_wood_foot_left_oak"));
		assertEquals("bed_canopy_single_inventory_oak",
				UpholsteryModelLoader.inventoryModelName("bed_canopy_foot_lower_oak"));
		assertEquals("bed_canopy_double_inventory_oak",
				UpholsteryModelLoader.inventoryModelName("bed_canopy_foot_left_lower_oak"));
		assertEquals("chair_wood_ironage_throne_oak_inventory",
				UpholsteryModelLoader.inventoryModelName("chair_wood_ironage_throne_oak"));
	}
}
