package zone.moddev.mc.ironagefurniture.api.recipes;

import static org.junit.Assert.*;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Bootstrap;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.junit.BeforeClass;
import org.junit.Test;
import zone.moddev.mc.ironagefurniture.api.Enumerations.UpholsteryColour;
import zone.moddev.mc.ironagefurniture.api.UpholsteryColourHelper;

public class CanopyBedUpgradeRecipeTest {
    @BeforeClass public static void bootstrap() { Bootstrap.register(); }

    private InventoryCrafting grid(ItemStack bed, ItemStack planks) {
        InventoryCrafting grid = new InventoryCrafting(new Container() {
            @Override public boolean canInteractWith(EntityPlayer player) { return false; }
        }, 2, 2);
        grid.setInventorySlotContents(0, bed);
        grid.setInventorySlotContents(3, planks);
        return grid;
    }

    private CanopyBedUpgradeRecipe recipe(UpholsteryColour colour) {
        return new CanopyBedUpgradeRecipe(Blocks.BRICK_BLOCK, Blocks.STONEBRICK,
                new ItemStack(Blocks.PLANKS, 1, 2), colour);
    }

    private void checkOutput(CanopyBedUpgradeRecipe recipe, InventoryCrafting grid, UpholsteryColour colour) {
        assertTrue(recipe.matches(grid, null));
        ItemStack result = recipe.getCraftingResult(grid);
        assertEquals(1, result.getCount());
        assertEquals(colour.getItemMetadata(), result.getMetadata());
        assertEquals(colour.getSerializedName(), result.getTagCompound().getString("Color"));
        assertSame(colour, UpholsteryColourHelper.getColour(result));
        assertEquals(recipe.getRecipeOutput().getMetadata(), result.getMetadata());
        assertTrue(ItemStack.areItemStackTagsEqual(recipe.getRecipeOutput(), result));
        assertEquals("minecraft:stonebrick/meta_" + colour.getItemMetadata(), recipe.getGroup());
        for (ItemStack remainder : recipe.getRemainingItems(grid)) assertTrue(remainder.isEmpty());
    }

    @Test public void allColoursAcceptStableNbtAndLegacyMetadataAndShowCorrectPreviews() {
        for (UpholsteryColour colour : UpholsteryColour.values()) {
            CanopyBedUpgradeRecipe recipe = recipe(colour);
            checkOutput(recipe, grid(UpholsteryColourHelper.createStack(Blocks.BRICK_BLOCK, 3, colour),
                    new ItemStack(Blocks.PLANKS, 12, 2)), colour);
            checkOutput(recipe, grid(new ItemStack(Blocks.BRICK_BLOCK, 1, colour.getItemMetadata()),
                    new ItemStack(Blocks.PLANKS, 1, 2)), colour);
            ItemStack preview = recipe.getIngredients().get(0).getMatchingStacks()[0];
            assertSame(colour, UpholsteryColourHelper.getColour(preview));
            assertTrue(recipe.getIngredients().get(0).apply(preview));
            assertFalse(recipe.getIngredients().get(0).isSimple());
        }
    }

    @Test public void storedColourWinsOverConflictingMetadata() {
        for (UpholsteryColour colour : UpholsteryColour.values()) {
            ItemStack bed = UpholsteryColourHelper.createStack(Blocks.BRICK_BLOCK, 1, colour);
            bed.setItemDamage((colour.getItemMetadata() + 1) % 16);
            checkOutput(recipe(colour), grid(bed, new ItemStack(Blocks.PLANKS, 1, 2)), colour);
        }
    }

    @Test public void invalidColourAndInvalidLegacyMetadataFallBackToRed() {
        ItemStack bed = new ItemStack(Blocks.BRICK_BLOCK, 1, UpholsteryColour.BLUE.getItemMetadata());
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("Color", "not_a_colour");
        bed.setTagCompound(tag);
        checkOutput(recipe(UpholsteryColour.RED), grid(bed, new ItemStack(Blocks.PLANKS, 1, 2)), UpholsteryColour.RED);
        assertFalse(recipe(UpholsteryColour.BLUE).matches(grid(bed, new ItemStack(Blocks.PLANKS, 1, 2)), null));
        checkOutput(recipe(UpholsteryColour.RED), grid(new ItemStack(Blocks.BRICK_BLOCK, 1, 99),
                new ItemStack(Blocks.PLANKS, 1, 2)), UpholsteryColour.RED);
    }

    @Test public void rejectsWrongWoodsColoursMissingOrExtraIngredients() {
        CanopyBedUpgradeRecipe recipe = recipe(UpholsteryColour.PINK);
        ItemStack bed = UpholsteryColourHelper.createStack(Blocks.BRICK_BLOCK, 1, UpholsteryColour.PINK);
        assertFalse(recipe.matches(grid(bed, new ItemStack(Blocks.PLANKS, 1, 1)), null));
        assertFalse(recipe.matches(grid(new ItemStack(Blocks.STONE), new ItemStack(Blocks.PLANKS, 1, 2)), null));
        assertFalse(recipe.matches(grid(bed, ItemStack.EMPTY), null));
        InventoryCrafting extra = grid(bed, new ItemStack(Blocks.PLANKS, 1, 2));
        extra.setInventorySlotContents(1, new ItemStack(Blocks.CARPET));
        assertFalse(recipe.matches(extra, null));
        assertTrue(recipe.getCraftingResult(extra).isEmpty());
        assertFalse(recipe.matches(grid(UpholsteryColourHelper.createStack(Blocks.BRICK_BLOCK, 1,
                UpholsteryColour.RED), new ItemStack(Blocks.PLANKS, 1, 2)), null));
    }
}
