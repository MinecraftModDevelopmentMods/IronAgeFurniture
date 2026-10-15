package zone.moddev.mc.ironagefurniture.api.recipes;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.Bootstrap;
import org.junit.Test;
import static org.junit.Assert.*;
import zone.moddev.mc.ironagefurniture.api.SconceMetalData;
import zone.moddev.mc.ironagefurniture.api.enumerations.SconceMetal;

public class MetalSconceRecipeTest {
    @Test public void absentMetalCannotMatchOrProduceAnOutputEvenWithACompleteGrid() {
        net.minecraft.SharedConstants.tryDetectVersion(); Bootstrap.bootStrap();
        NonNullList<Ingredient> ingredients = NonNullList.withSize(9, Ingredient.EMPTY);
        for (int slot : new int[]{0, 1, 2, 3, 6}) ingredients.set(slot, Ingredient.of(Items.GOLD_NUGGET));
        ItemStack result = SconceMetalData.set(new ItemStack(Blocks.IRON_BLOCK, 4), SconceMetal.ADAMANTINE);
        MetalSconceRecipe recipe = new MetalSconceRecipe(new ShapedRecipe(new ResourceLocation("test", "metal"), "",
                3, 3, ingredients, result));
        CraftingContainer grid = new CraftingContainer(new AbstractContainerMenu(null, 0) {
            @Override public boolean stillValid(Player player) { return false; }
        }, 3, 3);
        for (int slot : new int[]{0, 1, 2, 3, 6}) grid.setItem(slot, new ItemStack(Items.GOLD_NUGGET));
        assertFalse(recipe.matches(grid, null));
        assertTrue(recipe.assemble(grid).isEmpty());
        grid.clearContent();
        assertFalse(recipe.matches(grid, null));
        assertTrue(recipe.assemble(grid).isEmpty());
    }
}
