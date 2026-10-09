package zone.moddev.mc.ironagefurniture.api.recipes;

import com.google.gson.JsonObject;
import net.minecraft.block.Block;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.ShapelessRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;
import zone.moddev.mc.ironagefurniture.api.ShieldChairItemData;
import zone.moddev.mc.ironagefurniture.init.PhaseFourRecipes;

/** The usual chair upgrade, but the supplied shield is copied without losing NBT. */
public final class ShieldChairRecipe extends ShapelessRecipe {
    public ShieldChairRecipe(ShapelessRecipe recipe) {
        super(recipe.getId(), recipe.getGroup(), recipe.getRecipeOutput(), recipe.getIngredients());
    }

    @Override public ItemStack getCraftingResult(CraftingInventory inventory) {
        for (int slot = 0; slot < inventory.getSizeInventory(); slot++) {
            ItemStack shield = inventory.getStackInSlot(slot);
            if (shield.getItem() == Items.SHIELD) {
                return ShieldChairItemData.createChair(Block.getBlockFromItem(getRecipeOutput().getItem()), shield);
            }
        }
        return ItemStack.EMPTY;
    }

    @Override public IRecipeSerializer<?> getSerializer() { return PhaseFourRecipes.shield_chair; }

    public static final class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>>
            implements IRecipeSerializer<ShieldChairRecipe> {
        private final ShapelessRecipe.Serializer vanilla = new ShapelessRecipe.Serializer();

        @Override public ShieldChairRecipe read(ResourceLocation id, JsonObject json) {
            return new ShieldChairRecipe(vanilla.read(id, json));
        }
        @Override public ShieldChairRecipe read(ResourceLocation id, PacketBuffer buffer) {
            return new ShieldChairRecipe(vanilla.read(id, buffer));
        }
        @Override public void write(PacketBuffer buffer, ShieldChairRecipe recipe) { vanilla.write(buffer, recipe); }
    }
}
