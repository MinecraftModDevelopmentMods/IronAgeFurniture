package zone.moddev.mc.ironagefurniture.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;

import org.junit.BeforeClass;
import org.junit.Test;

import zone.moddev.mc.ironagefurniture.api.Enumerations.UpholsteryColour;
import zone.moddev.mc.ironagefurniture.api.tile.TileEntityUpholstery;
import net.minecraft.init.Blocks;
import net.minecraft.init.Bootstrap;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class UpholsteryColourTest {
	@BeforeClass public static void bootstrap() {
		Bootstrap.register();
		GameRegistry.registerTileEntity(TileEntityUpholstery.class, "ironagefurniture:upholstery_colour_test");
	}

	@Test public void everyCarpetColourMapsToAUniqueItemVariant() {
		boolean[] used = new boolean[16];
		for (int carpet = 0; carpet < 16; carpet++) {
			UpholsteryColour colour = UpholsteryColour.byCarpetMetadata(carpet);
			assertEquals(carpet, colour.getCarpetMetadata());
			int itemMeta = colour.getItemMetadata();
			assertFalse(used[itemMeta]);
			used[itemMeta] = true;
			assertSame(colour, UpholsteryColour.byItemMetadata(itemMeta));
		}
		assertSame(UpholsteryColour.RED, UpholsteryColour.byItemMetadata(0));
		assertSame(UpholsteryColour.LIGHT_GRAY, UpholsteryColour.byCarpetMetadata(8));
		assertEquals("silver", UpholsteryColour.LIGHT_GRAY.getTextureName());
	}

	@Test public void legacyItemsAndTileEntitiesDefaultToRed() {
		assertSame(UpholsteryColour.RED, UpholsteryColourHelper.getColour(new ItemStack(Blocks.PLANKS)));
		TileEntityUpholstery tile = new TileEntityUpholstery();
		tile.readFromNBT(new NBTTagCompound());
		assertSame(UpholsteryColour.RED, tile.getColour());
		assertFalse(ITickable.class.isAssignableFrom(TileEntityUpholstery.class));
	}

	@Test public void coloursRoundTripThroughItemAndWorldNbt() {
		for (UpholsteryColour colour : UpholsteryColour.values()) {
			ItemStack stack = UpholsteryColourHelper.createStack(Blocks.PLANKS, 1, colour);
			assertSame(colour, UpholsteryColourHelper.getColour(stack));
			assertEquals(colour.getItemMetadata(), stack.getMetadata());
			TileEntityUpholstery tile = new TileEntityUpholstery();
			tile.setColour(colour);
			NBTTagCompound nbt = tile.writeToNBT(new NBTTagCompound());
			TileEntityUpholstery loaded = new TileEntityUpholstery();
			loaded.readFromNBT(nbt);
			assertSame(colour, loaded.getColour());
		}
	}

	@Test public void invalidDataFallsBackToRedWithoutErasingUnrelatedNbt() {
		ItemStack stack = new ItemStack(Blocks.PLANKS);
		NBTTagCompound tag = new NBTTagCompound();
		tag.setString("Owner", "fixture");
		stack.setTagCompound(tag);
		UpholsteryColourHelper.setColour(stack, UpholsteryColour.BLUE);
		assertEquals("fixture", stack.getTagCompound().getString("Owner"));
		stack.getTagCompound().setString("Color", "invalid");
		assertSame(UpholsteryColour.RED, UpholsteryColourHelper.getColour(stack));
		TileEntityUpholstery tile = new TileEntityUpholstery();
		tile.readFromNBT(stack.getTagCompound());
		assertSame(UpholsteryColour.RED, tile.getColour());
	}
}
