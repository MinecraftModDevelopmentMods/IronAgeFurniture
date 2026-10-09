package zone.moddev.mc.ironagefurniture.api.recipes;

import com.google.gson.JsonObject;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.ShapelessRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;
import zone.moddev.mc.ironagefurniture.api.UpholsteryItemData;
import zone.moddev.mc.ironagefurniture.api.enumerations.UpholsteryColour;
import zone.moddev.mc.ironagefurniture.api.items.UpholsteredBlockItem;
import zone.moddev.mc.ironagefurniture.init.PhaseFourRecipes;

/** Canopies and double beds use the supplied singles' colour, not a recipe default. */
public final class UpholsteryUpgradeRecipe extends ShapelessRecipe {
    public UpholsteryUpgradeRecipe(ShapelessRecipe recipe) {
        super(recipe.getId(), recipe.getGroup(), recipe.getRecipeOutput(), recipe.getIngredients());
    }
    @Override public boolean matches(CraftingInventory inventory, World world) {
        if (!super.matches(inventory, world)) return false;
        UpholsteryColour expected = UpholsteryItemData.getColour(getRecipeOutput());
        for (int slot = 0; slot < inventory.getSizeInventory(); slot++) {
            ItemStack input = inventory.getStackInSlot(slot);
            if (input.getItem() instanceof UpholsteredBlockItem && UpholsteryItemData.getColour(input) != expected)
                return false;
        }
        return true;
    }
    @Override public ItemStack getCraftingResult(CraftingInventory inventory) {
        ItemStack result = getRecipeOutput().copy();
        ItemStack first = ItemStack.EMPTY;
        for (int slot = 0; slot < inventory.getSizeInventory(); slot++) {
            ItemStack input = inventory.getStackInSlot(slot);
            if (input.getItem() instanceof UpholsteredBlockItem) {
                if (UpholsteryItemData.getColour(input) != UpholsteryItemData.getColour(result)) return ItemStack.EMPTY;
                if (first.isEmpty()) first = input;
            }
        }
        if (first.isEmpty()) return ItemStack.EMPTY;
        if (first.hasTag()) result.setTag(first.getTag().copy());
        return UpholsteryItemData.recolour(result, UpholsteryItemData.getColour(first));
    }
    @Override public IRecipeSerializer<?> getSerializer() { return PhaseFourRecipes.upholstery_upgrade; }
    public static final class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>>
            implements IRecipeSerializer<UpholsteryUpgradeRecipe> {
        private final ShapelessRecipe.Serializer vanilla = new ShapelessRecipe.Serializer();
        @Override public UpholsteryUpgradeRecipe read(ResourceLocation id, JsonObject json) {
            return new UpholsteryUpgradeRecipe(vanilla.read(id, json));
        }
        @Override public UpholsteryUpgradeRecipe read(ResourceLocation id, PacketBuffer buffer) {
            return new UpholsteryUpgradeRecipe(vanilla.read(id, buffer));
        }
        @Override public void write(PacketBuffer buffer, UpholsteryUpgradeRecipe recipe) { vanilla.write(buffer, recipe); }
    }
}
