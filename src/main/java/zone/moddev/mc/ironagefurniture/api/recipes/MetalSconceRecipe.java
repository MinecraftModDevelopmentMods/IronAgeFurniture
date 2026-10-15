package zone.moddev.mc.ironagefurniture.api.recipes;

import com.google.gson.JsonObject;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistryEntry;
import zone.moddev.mc.ironagefurniture.api.SconceMetalData;
import zone.moddev.mc.ironagefurniture.api.enumerations.SconceMetal;
import zone.moddev.mc.ironagefurniture.init.PhaseFourRecipes;

/** An empty or disabled metal tag must never become an ingredient-free recipe. */
public final class MetalSconceRecipe extends ShapedRecipe {
    private final SconceMetal metal;
    public MetalSconceRecipe(ShapedRecipe recipe) {
        super(recipe.getId(), recipe.getGroup(), recipe.getWidth(), recipe.getHeight(),
                recipe.getIngredients(), recipe.getResultItem());
        metal = SconceMetalData.get(recipe.getResultItem());
    }
    @Override public boolean matches(CraftingContainer grid, Level world) {
        return SconceMetalData.available(metal) && super.matches(grid, world);
    }
    @Override public ItemStack assemble(CraftingContainer grid) {
        return SconceMetalData.available(metal) ? super.assemble(grid) : ItemStack.EMPTY;
    }
    @Override public RecipeSerializer<?> getSerializer() { return PhaseFourRecipes.metal_sconce; }

    public static final class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>>
            implements RecipeSerializer<MetalSconceRecipe> {
        private final ShapedRecipe.Serializer vanilla = new ShapedRecipe.Serializer();
        @Override public MetalSconceRecipe fromJson(ResourceLocation id, JsonObject json) {
            return new MetalSconceRecipe(vanilla.fromJson(id, json));
        }
        @Override public MetalSconceRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
            return new MetalSconceRecipe(vanilla.fromNetwork(id, buffer));
        }
        @Override public void toNetwork(FriendlyByteBuf buffer, MetalSconceRecipe recipe) {
            vanilla.toNetwork(buffer, recipe);
        }
    }
}
