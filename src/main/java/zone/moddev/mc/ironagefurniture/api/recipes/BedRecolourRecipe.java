package zone.moddev.mc.ironagefurniture.api.recipes;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import zone.moddev.mc.ironagefurniture.api.UpholsteryItemData;
import zone.moddev.mc.ironagefurniture.api.blocks.furniture.FurnitureBed;
import zone.moddev.mc.ironagefurniture.api.enumerations.UpholsteryColour;
import zone.moddev.mc.ironagefurniture.api.items.UpholsteredBlockItem;

/** Manual recolouring never competes with construction in the recipe book. */
public final class BedRecolourRecipe extends CustomRecipe {
    public static final BedRecolourRecipe INSTANCE = new BedRecolourRecipe();
    public static final RecipeSerializer<BedRecolourRecipe> SERIALIZER = new RecipeSerializer<>(MapCodec.unit(INSTANCE), StreamCodec.unit(INSTANCE));
    private record Inputs(ItemStack bed, UpholsteryColour colour) { }
    private Inputs inputs(CraftingInput input) {
        if (input.ingredientCount() != 2) return null;
        ItemStack bed = ItemStack.EMPTY;
        UpholsteryColour colour = null;
        for (ItemStack stack : input.items()) {
            if (stack.isEmpty()) continue;
            if (stack.getItem() instanceof UpholsteredBlockItem item && item.getBlock() instanceof FurnitureBed) {
                if (!bed.isEmpty()) return null;
                bed = stack;
            } else {
                var id = BuiltInRegistries.ITEM.getKey(stack.getItem());
                if (!id.getNamespace().equals("minecraft") || !id.getPath().endsWith("_carpet") || colour != null) return null;
                String name = id.getPath().substring(0, id.getPath().length() - "_carpet".length());
                for (UpholsteryColour candidate : UpholsteryColour.values()) if (candidate.getName().equals(name)) colour = candidate;
                if (colour == null) return null;
            }
        }
        return bed.isEmpty() || colour == null ? null : new Inputs(bed, colour);
    }
    @Override public boolean matches(CraftingInput input, Level world) { return inputs(input) != null; }
    @Override public ItemStack assemble(CraftingInput input) {
        Inputs found = inputs(input);
        return found == null ? ItemStack.EMPTY : UpholsteryItemData.recolour(found.bed, found.colour).copyWithCount(1);
    }
    @Override public RecipeSerializer<BedRecolourRecipe> getSerializer() { return SERIALIZER; }
}
