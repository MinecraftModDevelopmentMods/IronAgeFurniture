package zone.moddev.mc.ironagefurniture.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.junit.BeforeClass;
import org.junit.Test;

import zone.moddev.mc.ironagefurniture.api.Enumerations.PaddedBenchColour;

import net.minecraft.init.Blocks;
import net.minecraft.init.Bootstrap;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class PaddedBenchColourHelperTest {
	@BeforeClass
	public static void bootstrapMinecraftRegistries() {
		Bootstrap.register();
	}

	@Test
	public void writesAndReadsEveryStableColourName() {
		for (PaddedBenchColour colour : PaddedBenchColour.values()) {
			ItemStack stack = PaddedBenchColourHelper.createStack(Blocks.PLANKS, 1, colour);
			assertEquals(colour.getItemMetadata(), stack.getMetadata());
			assertEquals(colour.getSerializedName(),
					stack.getTagCompound().getString(PaddedBenchColourHelper.COLOUR_TAG));
			assertSame(colour, PaddedBenchColourHelper.getColour(stack));
		}
	}

	@Test
	public void fallsBackToMetadataOnlyWhenColourTagIsMissing() {
		ItemStack metadataOnly = new ItemStack(Blocks.PLANKS, 1,
				PaddedBenchColour.CYAN.getItemMetadata());
		assertSame(PaddedBenchColour.CYAN, PaddedBenchColourHelper.getColour(metadataOnly));

		NBTTagCompound invalid = new NBTTagCompound();
		invalid.setString(PaddedBenchColourHelper.COLOUR_TAG, "not_a_colour");
		metadataOnly.setTagCompound(invalid);
		assertSame(PaddedBenchColour.RED, PaddedBenchColourHelper.getColour(metadataOnly));
	}

	@Test
	public void preservesUnrelatedItemData() {
		ItemStack stack = new ItemStack(Blocks.PLANKS, 3,
				PaddedBenchColour.PURPLE.getItemMetadata());
		NBTTagCompound tag = new NBTTagCompound();
		tag.setString("Owner", "fixture");
		stack.setTagCompound(tag);

		PaddedBenchColourHelper.setColour(stack, PaddedBenchColour.PURPLE);

		assertEquals("fixture", stack.getTagCompound().getString("Owner"));
		assertEquals("purple", stack.getTagCompound().getString(PaddedBenchColourHelper.COLOUR_TAG));
	}
}

