package zone.moddev.mc.ironagefurniture.api.recipes;

import java.util.List;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.level.Level;
import zone.moddev.mc.ironagefurniture.api.ShieldChairItemData;

/** Keep vanilla matching and autofill, but copy the actual input shield. */
public record ShieldChairRecipe(ShapelessRecipe recipe) implements CraftingRecipe {
    public static final MapCodec<ShieldChairRecipe> CODEC = new ShapelessRecipe.Serializer().codec().xmap(ShieldChairRecipe::new, ShieldChairRecipe::recipe);
    public static final StreamCodec<RegistryFriendlyByteBuf, ShieldChairRecipe> STREAM_CODEC =
            ShapelessRecipe.Serializer.STREAM_CODEC.map(ShieldChairRecipe::new, ShieldChairRecipe::recipe);
    public static final RecipeSerializer<ShieldChairRecipe> SERIALIZER = new RecipeSerializer<>() {
        @Override public MapCodec<ShieldChairRecipe> codec() { return CODEC; }
        @Override public StreamCodec<RegistryFriendlyByteBuf, ShieldChairRecipe> streamCodec() { return STREAM_CODEC; }
    };
    @Override public boolean matches(CraftingInput input, Level world) { return recipe.matches(input, world); }
    @Override public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
        for (ItemStack stack : input.items()) if (stack.is(Items.SHIELD))
            return ShieldChairItemData.withShield(recipe.assemble(input, registries), stack);
        return ItemStack.EMPTY;
    }
    @Override public PlacementInfo placementInfo() { return recipe.placementInfo(); }
    @Override public String group() { return recipe.group(); }
    @Override public CraftingBookCategory category() { return recipe.category(); }
    @Override public boolean showNotification() { return recipe.showNotification(); }
    @Override public List<RecipeDisplay> display() { return recipe.display(); }
    @Override public RecipeSerializer<ShieldChairRecipe> getSerializer() { return SERIALIZER; }
}
