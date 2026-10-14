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
    @BeforeClass public static void bootstrapMinecraft() { Bootstrap.bootStrap(); }

    private static BedRecolourRecipe recipe() {
        return new BedRecolourRecipe(new ShapelessRecipe(new ResourceLocation("ironagefurniture:test_recolour"), "",
                new ItemStack(Items.BLACK_BED), NonNullList.of(Ingredient.EMPTY,
                        Ingredient.of(Items.RED_BED, Items.WHITE_BED), Ingredient.of(Items.BLACK_CARPET))));
    }

    private static CraftingInventory grid() {
        return new CraftingInventory(new Container(null, 0) {
            @Override public boolean stillValid(PlayerEntity player) { return false; }
        }, 3, 3);
    }

    @Test public void manuallyRecoloursWithoutAcceptingExtraOrWrongIngredients() {
        BedRecolourRecipe recipe = recipe();
        CraftingInventory grid = grid();
        grid.setItem(0, new ItemStack(Items.WHITE_BED));
        grid.setItem(8, new ItemStack(Items.BLACK_CARPET));
        assertTrue(recipe.matches(grid, null));
        assertEquals(Items.BLACK_BED, recipe.assemble(grid).getItem());
        assertEquals(1, grid.getItem(0).getCount());
        assertTrue(recipe.getRemainingItems(grid).stream().allMatch(ItemStack::isEmpty));
        grid.setItem(4, new ItemStack(Items.STICK));
        assertFalse(recipe.matches(grid, null));
        grid.setItem(4, ItemStack.EMPTY);
        grid.setItem(8, new ItemStack(Items.BLUE_CARPET));
        assertFalse(recipe.matches(grid, null));
    }

    @Test public void isNotLearnedByTheRecipeBook() {
        BedRecolourRecipe recipe = recipe();
        assertTrue(recipe.isSpecial());
        RecipeBook book = new RecipeBook();
        book.add(recipe);
        assertFalse(book.contains(recipe));
    }

    @Test public void clientSynchronizationKeepsManualCraftingAndBookExclusion() {
        BedRecolourRecipe original = recipe();
        PacketBuffer buffer = new PacketBuffer(Unpooled.buffer());
        try {
            BedRecolourRecipe.Serializer serializer = new BedRecolourRecipe.Serializer();
            serializer.toNetwork(buffer, original);
            BedRecolourRecipe restored = serializer.fromNetwork(original.getId(), buffer);
            assertEquals(original.getId(), restored.getId());
            assertEquals(original.getGroup(), restored.getGroup());
            assertEquals(2, restored.getIngredients().size());
            assertTrue(restored.isSpecial());
            assertTrue(ItemStack.matches(original.getResultItem(), restored.getResultItem()));
            CraftingInventory grid = grid();
            grid.setItem(0, new ItemStack(Items.RED_BED));
            grid.setItem(1, new ItemStack(Items.BLACK_CARPET));
            assertTrue(restored.matches(grid, null));
        } finally { buffer.release(); }
    }
}
