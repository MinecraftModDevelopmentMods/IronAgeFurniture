package zone.moddev.mc.ironagefurniture.api.recipes;

import com.google.gson.JsonObject;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.IRecipeFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import zone.moddev.mc.ironagefurniture.api.ShieldChairItemData;

/** A normal shapeless chair recipe which copies the exact shield supplied by the player. */
public final class ShieldChairRecipe extends ShapelessRecipes {
    private final Block chair;

    private ShieldChairRecipe(Block original, Block chair) {
        super("", new ItemStack(chair), NonNullList.from(Ingredient.EMPTY,
                Ingredient.fromItems(Items.SHIELD),
                Ingredient.fromStacks(new ItemStack(original))));
        this.chair = chair;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inventory) {
        for (int slot = 0; slot < inventory.getSizeInventory(); slot++) {
            ItemStack stack = inventory.getStackInSlot(slot);
            if (!stack.isEmpty() && stack.getItem() == Items.SHIELD) {
                return ShieldChairItemData.createChair(chair, stack);
            }
        }
        return ItemStack.EMPTY;
    }

    public static final class Factory implements IRecipeFactory {
        @Override
        public IRecipe parse(JsonContext context, JsonObject json) {
            String resultName = json.getAsJsonObject("result").get("item").getAsString();
            String originalName = json.getAsJsonArray("ingredients").get(1)
                    .getAsJsonObject().get("item").getAsString();
            Block original = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(originalName));
            Block chair = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(resultName));
            if (original == null || chair == null) {
                throw new IllegalArgumentException("Unknown shield chair recipe block: " + originalName
                        + " or " + resultName);
            }
            return new ShieldChairRecipe(original, chair);
        }
    }
}
