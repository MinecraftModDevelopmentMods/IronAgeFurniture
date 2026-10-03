package zone.moddev.mc.ironagefurniture.api;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.EnumProperty;
import zone.moddev.mc.ironagefurniture.api.enumerations.UpholsteryColour;

/** Stable item colour data, independent of the flattened block-state palette. */
public final class UpholsteryItemData {
    public static final String COLOR = "Color";
    public static final EnumProperty<UpholsteryColour> COLOUR = EnumProperty.create("colour", UpholsteryColour.class);
    private UpholsteryItemData() { }
    public static UpholsteryColour getColour(ItemStack stack) {
        CompoundNBT tag = stack.getTag();
        return tag != null && tag.contains(COLOR, 8) ? UpholsteryColour.byName(tag.getString(COLOR))
                : UpholsteryColour.RED;
    }
    public static ItemStack create(Block block, UpholsteryColour colour) {
        ItemStack stack = new ItemStack(block);
        stack.getOrCreateTag().putString(COLOR, colour.getName());
        return stack;
    }
    public static ItemStack recolour(ItemStack original, UpholsteryColour colour) {
        ItemStack result = original.copy();
        result.getOrCreateTag().putString(COLOR, colour.getName());
        return result;
    }
}
