package zone.moddev.mc.ironagefurniture.api.Blocks;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;

public class BackBenchExtensionTest {
	@Test
	public void recognisesBenchImplementationsFromAddonNamespaces() {
		TestBackBench bench = new TestBackBench();
		bench.setRegistryName(new ResourceLocation("iafbygaddon", "test_back_bench"));

		assertTrue(bench.isCompatible(bench.getDefaultState()));
		assertFalse(bench.isCompatible(Blocks.PLANKS.getDefaultState()));
	}

	private static final class TestBackBench extends BackBench {
		private TestBackBench() {
			super(Material.WOOD, "test_back_bench", 10, false, 0.25, 1);
		}

		private boolean isCompatible(net.minecraft.block.state.IBlockState state) {
			return isCompatibleBench(state);
		}
	}
}
