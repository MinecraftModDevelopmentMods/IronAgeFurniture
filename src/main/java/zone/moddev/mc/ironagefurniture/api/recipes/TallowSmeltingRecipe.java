package zone.moddev.mc.ironagefurniture.api.recipes;

import com.google.gson.JsonObject;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.util.GsonHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.registries.ForgeRegistryEntry;
import zone.moddev.mc.ironagefurniture.init.PhaseFourRecipes;

/** Cooking larger pieces of meat returns the same tallow quantities as 1.12. */
public final class TallowSmeltingRecipe extends SmeltingRecipe {
    public TallowSmeltingRecipe(ResourceLocation id, String group, Ingredient input, ItemStack output,
            float experience, int time) {
        super(id, group, input, output, experience, time);
    }
    @Override public RecipeSerializer<?> getSerializer() { return PhaseFourRecipes.tallow_smelting; }
    public static final class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<TallowSmeltingRecipe> {
        private static TallowSmeltingRecipe adapt(SmeltingRecipe recipe) {
            return new TallowSmeltingRecipe(recipe.getId(), recipe.getGroup(), recipe.getIngredients().get(0),
                    recipe.getResultItem().copy(), recipe.getExperience(), recipe.getCookingTime());
        }
        @Override public TallowSmeltingRecipe fromJson(ResourceLocation id, JsonObject json) {
            TallowSmeltingRecipe recipe = adapt(RecipeSerializer.SMELTING_RECIPE.fromJson(id, json));
            int count = GsonHelper.getAsInt(json, "count", 1);
            if (count < 1 || count > recipe.getResultItem().getMaxStackSize())
                throw new IllegalArgumentException("Invalid tallow output count: " + count);
            recipe.getResultItem().setCount(count);
            return recipe;
        }
        @Override public TallowSmeltingRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
            return adapt(RecipeSerializer.SMELTING_RECIPE.fromNetwork(id, buffer));
        }
        @Override public void toNetwork(FriendlyByteBuf buffer, TallowSmeltingRecipe recipe) {
            RecipeSerializer.SMELTING_RECIPE.toNetwork(buffer, recipe);
        }
    }
}
