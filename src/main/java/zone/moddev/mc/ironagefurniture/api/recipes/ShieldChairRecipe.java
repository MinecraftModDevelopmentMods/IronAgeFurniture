package zone.moddev.mc.ironagefurniture.api.recipes;

import com.google.gson.JsonObject;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;
import zone.moddev.mc.ironagefurniture.api.ShieldChairItemData;
import zone.moddev.mc.ironagefurniture.init.PhaseFourRecipes;

/** The usual chair upgrade, but the supplied shield is copied without losing NBT. */
public final class ShieldChairRecipe extends ShapelessRecipe {
    public ShieldChairRecipe(ShapelessRecipe recipe) {
        super(recipe.getId(), recipe.getGroup(), recipe.getResultItem(), recipe.getIngredients());
    }

    @Override public ItemStack assemble(CraftingContainer inventory) {
        for (int slot = 0; slot < inventory.getContainerSize(); slot++) {
            ItemStack shield = inventory.getItem(slot);
            if (shield.getItem() == Items.SHIELD) {
                return ShieldChairItemData.createChair(Block.byItem(getResultItem().getItem()), shield);
            }
        }
        return ItemStack.EMPTY;
    }

    @Override public RecipeSerializer<?> getSerializer() { return PhaseFourRecipes.shield_chair; }

    public static final class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>>
            implements RecipeSerializer<ShieldChairRecipe> {
        private final ShapelessRecipe.Serializer vanilla = new ShapelessRecipe.Serializer();

        @Override public ShieldChairRecipe fromJson(ResourceLocation id, JsonObject json) {
            return new ShieldChairRecipe(vanilla.fromJson(id, json));
        }
        @Override public ShieldChairRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
            return new ShieldChairRecipe(vanilla.fromNetwork(id, buffer));
        }
        @Override public void toNetwork(FriendlyByteBuf buffer, ShieldChairRecipe recipe) { vanilla.toNetwork(buffer, recipe); }
    }
}
