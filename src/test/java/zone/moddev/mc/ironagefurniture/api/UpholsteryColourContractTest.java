package zone.moddev.mc.ironagefurniture.api;

import static org.junit.Assert.assertEquals;

import net.minecraft.init.Blocks;
import net.minecraft.init.Bootstrap;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.junit.BeforeClass;
import org.junit.Test;
import zone.moddev.mc.ironagefurniture.api.Enumerations.PaddedBenchColour;
import zone.moddev.mc.ironagefurniture.api.Enumerations.UpholsteryColour;

public class UpholsteryColourContractTest {
    @BeforeClass
    public static void bootstrapMinecraft() {
        Bootstrap.register();
    }

    @Test
    public void allSixteenColoursMatchTheExistingPaddedBenchContract() {
        for (UpholsteryColour colour : UpholsteryColour.values()) {
            PaddedBenchColour existing = PaddedBenchColour.byItemMetadata(colour.getItemMetadata());
            assertEquals(existing.getSerializedName(), colour.getSerializedName());
            assertEquals(existing.getCarpetMetadata(), colour.getCarpetMetadata());
            ItemStack item = UpholsteryColourHelper.createStack(Blocks.PLANKS, 1, colour);
            assertEquals(colour, UpholsteryColourHelper.getColour(item));
            assertEquals(colour.getSerializedName(),
                    item.getTagCompound().getString(UpholsteryColourHelper.COLOUR_TAG));
        }
    }

    @Test
    public void missingAndInvalidColourDataUseRed() {
        ItemStack old = new ItemStack(Blocks.PLANKS);
        assertEquals(UpholsteryColour.RED, UpholsteryColourHelper.getColour(old));
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString(UpholsteryColourHelper.COLOUR_TAG, "not-a-colour");
        old.setTagCompound(tag);
        assertEquals(UpholsteryColour.RED, UpholsteryColourHelper.getColour(old));
    }
}
