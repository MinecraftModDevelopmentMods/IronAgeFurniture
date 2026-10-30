package zone.moddev.mc.ironagefurniture.api.recipes;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.level.Level;
import zone.moddev.mc.ironagefurniture.api.ShieldChairItemData;

/** Keep native matching and recipe-book ingredients, copying the actual input shield. */
public record ShieldChairRecipe(ShapelessRecipe recipe) implements CraftingRecipe {
    public static final RecipeSerializer<ShieldChairRecipe> SERIALIZER = new RecipeSerializer<>() {
        private final ShapelessRecipe.Serializer nativeSerializer = new ShapelessRecipe.Serializer();
        @Override public ShieldChairRecipe fromJson(net.minecraft.resources.ResourceLocation id, com.google.gson.JsonObject json) {
            return new ShieldChairRecipe(nativeSerializer.fromJson(id, json));
        }
        @Override public ShieldChairRecipe fromNetwork(net.minecraft.resources.ResourceLocation id, net.minecraft.network.FriendlyByteBuf buffer) {
            return new ShieldChairRecipe(nativeSerializer.fromNetwork(id, buffer));
        }
        @Override public void toNetwork(net.minecraft.network.FriendlyByteBuf buffer, ShieldChairRecipe recipe) {
            nativeSerializer.toNetwork(buffer, recipe.recipe());
        }
    };
    @Override public net.minecraft.resources.ResourceLocation getId() { return recipe.getId(); }

    @Override public boolean matches(CraftingContainer input, Level world) { return recipe.matches(input, world); }
    @Override public ItemStack assemble(CraftingContainer input, net.minecraft.core.RegistryAccess registries) {
        for (int slot = 0; slot < input.getContainerSize(); slot++) {
            ItemStack stack = input.getItem(slot);
            if (stack.is(Items.SHIELD)) return ShieldChairItemData.withShield(recipe.assemble(input, registries), stack);
        }
        return ItemStack.EMPTY;
    }
    @Override public boolean canCraftInDimensions(int width, int height) { return recipe.canCraftInDimensions(width, height); }
    @Override public ItemStack getResultItem(net.minecraft.core.RegistryAccess registries) { return recipe.getResultItem(registries); }
    @Override public NonNullList<Ingredient> getIngredients() { return recipe.getIngredients(); }
    @Override public String getGroup() { return recipe.getGroup(); }
    @Override public CraftingBookCategory category() { return recipe.category(); }
    @Override public boolean showNotification() { return recipe.showNotification(); }
    @Override public RecipeSerializer<ShieldChairRecipe> getSerializer() { return SERIALIZER; }
}
