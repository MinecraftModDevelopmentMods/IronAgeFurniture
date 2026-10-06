package zone.moddev.mc.ironagefurniture.api.recipes;

import static org.junit.Assert.*;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.RecipeBook;
import net.minecraft.item.crafting.ShapelessRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Bootstrap;
import org.junit.BeforeClass;
import org.junit.Test;

public class BedRecolourRecipeTest {
    @BeforeClass public static void bootstrapMinecraft() { Bootstrap.register(); }

    private static BedRecolourRecipe recipe() {
        return new BedRecolourRecipe(new ShapelessRecipe(new ResourceLocation("ironagefurniture:test_recolour"), "",
                new ItemStack(Items.BLACK_BED), NonNullList.from(Ingredient.EMPTY,
                        Ingredient.fromItems(Items.RED_BED, Items.WHITE_BED), Ingredient.fromItems(Items.BLACK_CARPET))));
    }

    private static CraftingInventory grid() {
        return new CraftingInventory(new Container(null, 0) {
            @Override public boolean canInteractWith(PlayerEntity player) { return false; }
        }, 3, 3);
    }

    @Test public void manuallyRecoloursWithoutAcceptingExtraOrWrongIngredients() {
        BedRecolourRecipe recipe = recipe();
        CraftingInventory grid = grid();
        grid.setInventorySlotContents(0, new ItemStack(Items.WHITE_BED));
        grid.setInventorySlotContents(8, new ItemStack(Items.BLACK_CARPET));
        assertTrue(recipe.matches(grid, null));
        assertEquals(Items.BLACK_BED, recipe.getCraftingResult(grid).getItem());
        assertEquals(1, grid.getStackInSlot(0).getCount());
        assertTrue(recipe.getRemainingItems(grid).stream().allMatch(ItemStack::isEmpty));
        grid.setInventorySlotContents(4, new ItemStack(Items.STICK));
        assertFalse(recipe.matches(grid, null));
        grid.setInventorySlotContents(4, ItemStack.EMPTY);
        grid.setInventorySlotContents(8, new ItemStack(Items.BLUE_CARPET));
        assertFalse(recipe.matches(grid, null));
    }

    @Test public void isNotLearnedByTheRecipeBook() {
        BedRecolourRecipe recipe = recipe();
        assertTrue(recipe.isDynamic());
        RecipeBook book = new RecipeBook();
        book.unlock(recipe);
        assertFalse(book.isUnlocked(recipe));
    }

    @Test public void clientSynchronizationKeepsManualCraftingAndBookExclusion() {
        BedRecolourRecipe original = recipe();
        PacketBuffer buffer = new PacketBuffer(Unpooled.buffer());
        try {
            BedRecolourRecipe.Serializer serializer = new BedRecolourRecipe.Serializer();
            serializer.write(buffer, original);
            BedRecolourRecipe restored = serializer.read(original.getId(), buffer);
            assertEquals(original.getId(), restored.getId());
            assertEquals(original.getGroup(), restored.getGroup());
            assertEquals(2, restored.getIngredients().size());
            assertTrue(restored.isDynamic());
            assertTrue(ItemStack.areItemStacksEqual(original.getRecipeOutput(), restored.getRecipeOutput()));
            CraftingInventory grid = grid();
            grid.setInventorySlotContents(0, new ItemStack(Items.RED_BED));
            grid.setInventorySlotContents(1, new ItemStack(Items.BLACK_CARPET));
            assertTrue(restored.matches(grid, null));
        } finally { buffer.release(); }
    }
}
