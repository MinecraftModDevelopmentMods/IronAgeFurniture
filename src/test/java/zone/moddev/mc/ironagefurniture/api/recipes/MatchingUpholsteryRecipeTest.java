package zone.moddev.mc.ironagefurniture.api.recipes;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Bootstrap;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import org.junit.BeforeClass;
import org.junit.Test;
import zone.moddev.mc.ironagefurniture.api.Enumerations.UpholsteryColour;
import zone.moddev.mc.ironagefurniture.api.UpholsteryColourHelper;

public class MatchingUpholsteryRecipeTest {
    @BeforeClass public static void bootstrap() { Bootstrap.register(); }

    private InventoryCrafting inventory(UpholsteryColour first, UpholsteryColour second) {
        InventoryCrafting inventory = new InventoryCrafting(new Container() {
            @Override public boolean canInteractWith(EntityPlayer player) { return false; }
        }, 2, 1);
        inventory.setInventorySlotContents(0, UpholsteryColourHelper.createStack(Blocks.PLANKS, 1, first));
        inventory.setInventorySlotContents(1, UpholsteryColourHelper.createStack(Blocks.PLANKS, 1, second));
        return inventory;
    }

    @Test public void matchingColoursSurviveDoubleBedRecipe() {
        MatchingUpholsteryRecipe recipe = new MatchingUpholsteryRecipe(Blocks.PLANKS, Blocks.BRICK_BLOCK);
        for (UpholsteryColour colour : UpholsteryColour.values()) {
            InventoryCrafting inventory = inventory(colour, colour);
            assertTrue("colour " + colour, recipe.matches(inventory, null));
            assertSame(colour, UpholsteryColourHelper.getColour(recipe.getCraftingResult(inventory)));
        }
    }

    @Test public void mixedColoursCannotLoseAColourSilently() {
        MatchingUpholsteryRecipe recipe = new MatchingUpholsteryRecipe(Blocks.PLANKS, Blocks.BRICK_BLOCK);
        assertFalse(recipe.matches(inventory(UpholsteryColour.BLUE, UpholsteryColour.RED), null));
    }
}
