package zone.moddev.mc.ironagefurniture.api.tile;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;

import org.junit.BeforeClass;
import org.junit.Test;

import zone.moddev.mc.ironagefurniture.api.Enumerations.PaddedBenchColour;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class TileEntityPaddedBenchTest {
	@BeforeClass
	public static void registerTileEntityForNbtRoundTrip() {
		GameRegistry.registerTileEntity(TileEntityPaddedBench.class,
				"ironagefurniture:padded_bench_colour_test");
	}

	@Test
	public void missingAndInvalidColourDataRemainLegacyRed() {
		TileEntityPaddedBench tile = new TileEntityPaddedBench();
		tile.readFromNBT(new NBTTagCompound());
		assertSame(PaddedBenchColour.RED, tile.getColour());

		NBTTagCompound invalid = new NBTTagCompound();
		invalid.setString("Color", "invalid");
		tile.readFromNBT(invalid);
		assertSame(PaddedBenchColour.RED, tile.getColour());
	}

	@Test
	public void everyColourRoundTripsThroughStableStringNbt() {
		for (PaddedBenchColour colour : PaddedBenchColour.values()) {
			TileEntityPaddedBench source = new TileEntityPaddedBench();
			source.setColour(colour);
			NBTTagCompound compound = source.writeToNBT(new NBTTagCompound());
			assertEquals(colour.getSerializedName(), compound.getString("Color"));

			TileEntityPaddedBench loaded = new TileEntityPaddedBench();
			loaded.readFromNBT(compound);
			assertSame(colour, loaded.getColour());
		}
	}

	@Test
	public void colourStorageDoesNotTick() {
		assertFalse(ITickable.class.isAssignableFrom(TileEntityPaddedBench.class));
	}
}
