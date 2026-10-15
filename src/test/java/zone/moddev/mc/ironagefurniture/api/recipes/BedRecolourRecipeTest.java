package zone.moddev.mc.ironagefurniture.api.recipes;

import static org.junit.Assert.*;
import io.netty.buffer.Unpooled;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.stats.RecipeBook;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.Bootstrap;
import org.junit.BeforeClass;
import org.junit.Test;

public class BedRecolourRecipeTest {
    @BeforeClass public static void bootstrapMinecraft() { net.minecraft.SharedConstants.tryDetectVersion(); Bootstrap.bootStrap(); }

    private static BedRecolourRecipe recipe() {
        return new BedRecolourRecipe(new ShapelessRecipe(new ResourceLocation("ironagefurniture:test_recolour"), "",
                new ItemStack(Items.BLACK_BED), NonNullList.of(Ingredient.EMPTY,
                        Ingredient.of(Items.RED_BED, Items.WHITE_BED), Ingredient.of(Items.BLACK_CARPET))));
    }

    private static CraftingContainer grid() {
        return new CraftingContainer(new AbstractContainerMenu(null, 0) {
            @Override public boolean stillValid(Player player) { return false; }
        }, 3, 3);
    }

    @Test public void manuallyRecoloursWithoutAcceptingExtraOrWrongIngredients() {
        BedRecolourRecipe recipe = recipe();
        CraftingContainer grid = grid();
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
        FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());
        try {
            BedRecolourRecipe.Serializer serializer = new BedRecolourRecipe.Serializer();
            serializer.toNetwork(buffer, original);
            BedRecolourRecipe restored = serializer.fromNetwork(original.getId(), buffer);
            assertEquals(original.getId(), restored.getId());
            assertEquals(original.getGroup(), restored.getGroup());
            assertEquals(2, restored.getIngredients().size());
            assertTrue(restored.isSpecial());
            assertTrue(ItemStack.matches(original.getResultItem(), restored.getResultItem()));
            CraftingContainer grid = grid();
            grid.setItem(0, new ItemStack(Items.RED_BED));
            grid.setItem(1, new ItemStack(Items.BLACK_CARPET));
            assertTrue(restored.matches(grid, null));
        } finally { buffer.release(); }
    }
}
