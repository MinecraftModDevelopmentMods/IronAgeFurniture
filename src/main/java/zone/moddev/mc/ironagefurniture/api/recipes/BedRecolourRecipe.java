package zone.moddev.mc.ironagefurniture.api.recipes;

import com.google.gson.JsonObject;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;
import zone.moddev.mc.ironagefurniture.init.PhaseFourRecipes;

/** Carpet recolouring stays available at the crafting table, not in the recipe book. */
public final class BedRecolourRecipe extends ShapelessRecipe {
    public BedRecolourRecipe(ShapelessRecipe recipe) {
        super(recipe.getId(), recipe.getGroup(), recipe.getResultItem(), recipe.getIngredients());
    }

    // Vanilla also ignores previously learned entries when loading a player's book.
    @Override public boolean isSpecial() { return true; }
    @Override public RecipeSerializer<?> getSerializer() { return PhaseFourRecipes.bed_recolour; }

    public static final class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>>
            implements RecipeSerializer<BedRecolourRecipe> {
        private final ShapelessRecipe.Serializer vanilla = new ShapelessRecipe.Serializer();

        @Override public BedRecolourRecipe fromJson(ResourceLocation id, JsonObject json) {
            return new BedRecolourRecipe(vanilla.fromJson(id, json));
        }
        @Override public BedRecolourRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
            return new BedRecolourRecipe(vanilla.fromNetwork(id, buffer));
        }
        @Override public void toNetwork(FriendlyByteBuf buffer, BedRecolourRecipe recipe) { vanilla.toNetwork(buffer, recipe); }
    }
}
