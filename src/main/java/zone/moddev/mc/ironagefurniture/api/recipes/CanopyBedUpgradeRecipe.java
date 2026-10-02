package zone.moddev.mc.ironagefurniture.api.recipes;

import com.google.gson.JsonObject;
import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IRecipeFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import zone.moddev.mc.ironagefurniture.api.Enumerations.UpholsteryColour;
import zone.moddev.mc.ironagefurniture.api.UpholsteryColourHelper;

/** Adds a canopy without changing the wooden bed's upholstery or wood. */
public final class CanopyBedUpgradeRecipe extends ShapelessOreRecipe {
    public CanopyBedUpgradeRecipe(Block source, Block result, ItemStack planks, UpholsteryColour colour) {
        super(new ResourceLocation(result.getRegistryName() + "/meta_" + colour.getItemMetadata()),
                UpholsteryColourHelper.createStack(result, 1, colour),
                new ColourIngredient(source, colour), Ingredient.fromStacks(planks));
        // A legacy item's damage can disagree with its authoritative Color NBT.
        // Crafting must check the full stack rather than just its packed item ID.
        this.isSimple = false;
    }

    @Override public ItemStack getCraftingResult(InventoryCrafting inventory) {
        return matches(inventory, null) ? getRecipeOutput().copy() : ItemStack.EMPTY;
    }

    private static final class ColourIngredient extends Ingredient {
        private final Item source;
        private final UpholsteryColour colour;

        ColourIngredient(Block source, UpholsteryColour colour) {
            super(UpholsteryColourHelper.createStack(source, 1, colour));
            this.source = Item.getItemFromBlock(source);
            this.colour = colour;
        }

        @Override public boolean apply(ItemStack stack) {
            return stack != null && !stack.isEmpty() && stack.getItem() == source
                    && UpholsteryColourHelper.getColour(stack) == colour;
        }

        @Override public boolean isSimple() { return false; }
    }

    public static final class Factory implements IRecipeFactory {
        @Override public IRecipe parse(JsonContext context, JsonObject json) {
            if (json.getAsJsonArray("ingredients").size() != 2)
                throw new IllegalArgumentException("A canopy upgrade needs one wooden bed and one plank");
            JsonObject bed = json.getAsJsonArray("ingredients").get(0).getAsJsonObject();
            ItemStack output = CraftingHelper.getItemStack(json.getAsJsonObject("result"), context);
            Block source = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(bed.get("item").getAsString()));
            Block result = Block.getBlockFromItem(output.getItem());
            if (source == null || Item.getItemFromBlock(source) == null || result == null
                    || output.isEmpty() || output.getCount() != 1)
                throw new IllegalArgumentException("Unknown bed in canopy upgrade: " + json);
            UpholsteryColour colour = UpholsteryColourHelper.getColour(output);
            ItemStack planks = CraftingHelper.getItemStack(
                    json.getAsJsonArray("ingredients").get(1).getAsJsonObject(), context);
            return new CanopyBedUpgradeRecipe(source, result, planks, colour);
        }
    }
}
