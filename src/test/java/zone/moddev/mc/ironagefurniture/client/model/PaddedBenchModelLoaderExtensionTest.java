package zone.moddev.mc.ironagefurniture.client.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import net.minecraft.util.ResourceLocation;

public class PaddedBenchModelLoaderExtensionTest {
	@Test
	public void acceptsRegisteredAddonNamespaceOnlyForPaddedModels() {
		PaddedBenchModelLoader.registerNamespace("iafbygaddon");

		assertTrue(PaddedBenchModelLoader.INSTANCE.accepts(new ResourceLocation(
				"iafbygaddon", "models/block/padded/test_bench")));
		assertTrue(PaddedBenchModelLoader.INSTANCE.accepts(new ResourceLocation(
				"iafbygaddon", "models/item/padded/red/test_bench")));
		assertFalse(PaddedBenchModelLoader.INSTANCE.accepts(new ResourceLocation(
				"iafbygaddon", "models/block/test_bench")));
		assertFalse(PaddedBenchModelLoader.INSTANCE.accepts(new ResourceLocation(
				"anotheraddon", "models/block/padded/test_bench")));
	}
}
