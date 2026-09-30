package zone.moddev.mc.ironagefurniture.api.recipes;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Bootstrap;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import org.junit.BeforeClass;
import org.junit.Test;
import zone.moddev.mc.ironagefurniture.api.Enumerations.UpholsteryColour;
import zone.moddev.mc.ironagefurniture.api.UpholsteryColourHelper;

public class BedRecolourRecipeTest {
    @BeforeClass public static void bootstrap() { Bootstrap.register(); }

    private InventoryCrafting inventory(UpholsteryColour previous, ItemStack carpet) {
        InventoryCrafting inventory = new InventoryCrafting(new Container() {
            @Override public boolean canInteractWith(EntityPlayer player) { return false; }
        }, 2, 1);
        inventory.setInventorySlotContents(0, UpholsteryColourHelper.createStack(Blocks.PLANKS, 1, previous));
        inventory.setInventorySlotContents(1, carpet);
        return inventory;
    }

    @Test public void anyExistingColourCanBeReplacedByEachCarpetColour() {
        BedRecolourRecipe recipe = new BedRecolourRecipe(Blocks.PLANKS);
        for (UpholsteryColour colour : UpholsteryColour.values()) {
            InventoryCrafting inventory = inventory(UpholsteryColour.BLUE,
                    new ItemStack(Blocks.CARPET, 1, colour.getCarpetMetadata()));
            assertTrue("bed ingredient " + colour,
                    recipe.getIngredients().get(0).apply(inventory.getStackInSlot(0)));
            assertTrue("carpet ingredient " + colour,
                    recipe.getIngredients().get(1).apply(inventory.getStackInSlot(1)));
            assertTrue("carpet " + colour + " did not match", recipe.matches(inventory, null));
            assertSame(colour, UpholsteryColourHelper.getColour(recipe.getCraftingResult(inventory)));
        }
    }

    @Test public void requiresExactlyOneBedAndOneValidCarpet() {
        BedRecolourRecipe recipe = new BedRecolourRecipe(Blocks.PLANKS);
        assertFalse(recipe.matches(inventory(UpholsteryColour.RED, new ItemStack(Blocks.WOOL)), null));
        assertFalse(recipe.matches(inventory(UpholsteryColour.RED, new ItemStack(Blocks.CARPET, 1, 16)), null));
    }
}
