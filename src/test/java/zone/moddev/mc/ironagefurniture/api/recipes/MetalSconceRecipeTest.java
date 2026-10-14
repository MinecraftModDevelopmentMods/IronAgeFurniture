package zone.moddev.mc.ironagefurniture.api.recipes;

import net.minecraft.block.Blocks;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Bootstrap;
import org.junit.Test;
import static org.junit.Assert.*;
import zone.moddev.mc.ironagefurniture.api.SconceMetalData;
import zone.moddev.mc.ironagefurniture.api.enumerations.SconceMetal;

public class MetalSconceRecipeTest {
    @Test public void absentMetalCannotMatchOrProduceAnOutputEvenWithACompleteGrid() {
        Bootstrap.bootStrap();
        NonNullList<Ingredient> ingredients = NonNullList.withSize(9, Ingredient.EMPTY);
        for (int slot : new int[]{0, 1, 2, 3, 6}) ingredients.set(slot, Ingredient.of(Items.GOLD_NUGGET));
        ItemStack result = SconceMetalData.set(new ItemStack(Blocks.IRON_BLOCK, 4), SconceMetal.ADAMANTINE);
        MetalSconceRecipe recipe = new MetalSconceRecipe(new ShapedRecipe(new ResourceLocation("test", "metal"), "",
                3, 3, ingredients, result));
        CraftingInventory grid = new CraftingInventory(new Container(null, 0) {
            @Override public boolean stillValid(PlayerEntity player) { return false; }
        }, 3, 3);
        for (int slot : new int[]{0, 1, 2, 3, 6}) grid.setItem(slot, new ItemStack(Items.GOLD_NUGGET));
        assertFalse(recipe.matches(grid, null));
        assertTrue(recipe.assemble(grid).isEmpty());
        grid.clearContent();
        assertFalse(recipe.matches(grid, null));
        assertTrue(recipe.assemble(grid).isEmpty());
    }
}
