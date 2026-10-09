package zone.moddev.mc.ironagefurniture.api.recipes;

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.registries.ForgeRegistryEntry;
import zone.moddev.mc.ironagefurniture.init.PhaseFourRecipes;

/** Cooking larger pieces of meat returns the same tallow quantities as 1.12. */
public final class TallowSmeltingRecipe extends FurnaceRecipe {
    public TallowSmeltingRecipe(ResourceLocation id, String group, Ingredient input, ItemStack output,
            float experience, int time) {
        super(id, group, input, output, experience, time);
    }
    @Override public IRecipeSerializer<?> getSerializer() { return PhaseFourRecipes.tallow_smelting; }
    public static final class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<TallowSmeltingRecipe> {
        private static TallowSmeltingRecipe adapt(FurnaceRecipe recipe) {
            return new TallowSmeltingRecipe(recipe.getId(), recipe.getGroup(), recipe.getIngredients().get(0),
                    recipe.getResultItem().copy(), recipe.getExperience(), recipe.getCookingTime());
        }
        @Override public TallowSmeltingRecipe fromJson(ResourceLocation id, JsonObject json) {
            TallowSmeltingRecipe recipe = adapt(IRecipeSerializer.SMELTING_RECIPE.fromJson(id, json));
            int count = JSONUtils.getAsInt(json, "count", 1);
            if (count < 1 || count > recipe.getResultItem().getMaxStackSize())
                throw new IllegalArgumentException("Invalid tallow output count: " + count);
            recipe.getResultItem().setCount(count);
            return recipe;
        }
        @Override public TallowSmeltingRecipe fromNetwork(ResourceLocation id, PacketBuffer buffer) {
            return adapt(IRecipeSerializer.SMELTING_RECIPE.fromNetwork(id, buffer));
        }
        @Override public void toNetwork(PacketBuffer buffer, TallowSmeltingRecipe recipe) {
            IRecipeSerializer.SMELTING_RECIPE.toNetwork(buffer, recipe);
        }
    }
}
