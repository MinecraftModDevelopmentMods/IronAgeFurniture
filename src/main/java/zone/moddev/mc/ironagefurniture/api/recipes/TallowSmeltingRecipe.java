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
                    recipe.getRecipeOutput().copy(), recipe.getExperience(), recipe.getCookTime());
        }
        @Override public TallowSmeltingRecipe read(ResourceLocation id, JsonObject json) {
            TallowSmeltingRecipe recipe = adapt(IRecipeSerializer.SMELTING.read(id, json));
            int count = JSONUtils.getInt(json, "count", 1);
            if (count < 1 || count > recipe.getRecipeOutput().getMaxStackSize())
                throw new IllegalArgumentException("Invalid tallow output count: " + count);
            recipe.getRecipeOutput().setCount(count);
            return recipe;
        }
        @Override public TallowSmeltingRecipe read(ResourceLocation id, PacketBuffer buffer) {
            return adapt(IRecipeSerializer.SMELTING.read(id, buffer));
        }
        @Override public void write(PacketBuffer buffer, TallowSmeltingRecipe recipe) {
            IRecipeSerializer.SMELTING.write(buffer, recipe);
        }
    }
}
