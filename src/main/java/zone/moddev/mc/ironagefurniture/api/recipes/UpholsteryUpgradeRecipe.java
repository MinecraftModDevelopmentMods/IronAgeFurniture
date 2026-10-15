package zone.moddev.mc.ironagefurniture.api.recipes;

import com.google.gson.JsonObject;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistryEntry;
import zone.moddev.mc.ironagefurniture.api.UpholsteryItemData;
import zone.moddev.mc.ironagefurniture.api.enumerations.UpholsteryColour;
import zone.moddev.mc.ironagefurniture.api.items.UpholsteredBlockItem;
import zone.moddev.mc.ironagefurniture.init.PhaseFourRecipes;

/** Canopies and double beds use the supplied singles' colour, not a recipe default. */
public final class UpholsteryUpgradeRecipe extends ShapelessRecipe {
    public UpholsteryUpgradeRecipe(ShapelessRecipe recipe) {
        super(recipe.getId(), recipe.getGroup(), recipe.getResultItem(), recipe.getIngredients());
    }
    @Override public boolean matches(CraftingContainer inventory, Level world) {
        if (!super.matches(inventory, world)) return false;
        UpholsteryColour expected = UpholsteryItemData.getColour(getResultItem());
        for (int slot = 0; slot < inventory.getContainerSize(); slot++) {
            ItemStack input = inventory.getItem(slot);
            if (input.getItem() instanceof UpholsteredBlockItem && UpholsteryItemData.getColour(input) != expected)
                return false;
        }
        return true;
    }
    @Override public ItemStack assemble(CraftingContainer inventory) {
        ItemStack result = getResultItem().copy();
        ItemStack first = ItemStack.EMPTY;
        for (int slot = 0; slot < inventory.getContainerSize(); slot++) {
            ItemStack input = inventory.getItem(slot);
            if (input.getItem() instanceof UpholsteredBlockItem) {
                if (UpholsteryItemData.getColour(input) != UpholsteryItemData.getColour(result)) return ItemStack.EMPTY;
                if (first.isEmpty()) first = input;
            }
        }
        if (first.isEmpty()) return ItemStack.EMPTY;
        if (first.hasTag()) result.setTag(first.getTag().copy());
        return UpholsteryItemData.recolour(result, UpholsteryItemData.getColour(first));
    }
    @Override public RecipeSerializer<?> getSerializer() { return PhaseFourRecipes.upholstery_upgrade; }
    public static final class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>>
            implements RecipeSerializer<UpholsteryUpgradeRecipe> {
        private final ShapelessRecipe.Serializer vanilla = new ShapelessRecipe.Serializer();
        @Override public UpholsteryUpgradeRecipe fromJson(ResourceLocation id, JsonObject json) {
            return new UpholsteryUpgradeRecipe(vanilla.fromJson(id, json));
        }
        @Override public UpholsteryUpgradeRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
            return new UpholsteryUpgradeRecipe(vanilla.fromNetwork(id, buffer));
        }
        @Override public void toNetwork(FriendlyByteBuf buffer, UpholsteryUpgradeRecipe recipe) { vanilla.toNetwork(buffer, recipe); }
    }
}
