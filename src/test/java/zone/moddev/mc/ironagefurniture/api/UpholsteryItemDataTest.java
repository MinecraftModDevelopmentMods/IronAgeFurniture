package zone.moddev.mc.ironagefurniture.api;

import static org.junit.Assert.*;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.registry.Bootstrap;
import org.junit.BeforeClass;
import org.junit.Test;
import zone.moddev.mc.ironagefurniture.api.enumerations.UpholsteryColour;

public class UpholsteryItemDataTest {
    @BeforeClass public static void bootstrap() { Bootstrap.register(); }

    @Test public void everyColourHasTheSameLegacyMetadataAndStableName() {
        String[] names = {"red", "orange", "magenta", "light_blue", "yellow", "lime", "pink", "gray",
                "light_gray", "cyan", "purple", "blue", "brown", "green", "white", "black"};
        int[] dyes = {14, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 0, 15};
        for (int index = 0; index < names.length; index++) {
            UpholsteryColour colour = UpholsteryColour.byItemMetadata(index);
            assertEquals(names[index], colour.getName());
            assertEquals(index, colour.getItemMetadata());
            assertEquals(dyes[index], colour.getDyeMetadata());
            assertEquals(colour, UpholsteryColour.byName(names[index]));
            ItemStack stack = UpholsteryItemData.create(Blocks.OAK_PLANKS, colour);
            assertEquals(colour, UpholsteryItemData.getColour(ItemStack.read(stack.write(new CompoundNBT()))));
        }
    }
    @Test public void missingOrInvalidDataIsRedAndCopiesKeepOtherItemData() {
        ItemStack original = new ItemStack(Blocks.OAK_PLANKS, 5);
        original.getOrCreateTag().putString("OtherModsData", "keep");
        assertEquals(UpholsteryColour.RED, UpholsteryItemData.getColour(original));
        original.getTag().putString("Color", "unknown");
        assertEquals(UpholsteryColour.RED, UpholsteryItemData.getColour(original));
        ItemStack pink = UpholsteryItemData.recolour(original, UpholsteryColour.PINK);
        assertEquals(5, pink.getCount());
        assertEquals("keep", pink.getTag().getString("OtherModsData"));
        assertEquals("unknown", original.getTag().getString("Color"));
        assertEquals(UpholsteryColour.PINK, UpholsteryItemData.getColour(pink));
        assertEquals(UpholsteryColour.RED, UpholsteryColour.byItemMetadata(16));
        assertEquals(UpholsteryColour.RED, UpholsteryColour.byItemMetadata(-1));
    }
}
