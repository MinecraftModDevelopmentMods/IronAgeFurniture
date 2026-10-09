package zone.moddev.mc.ironagefurniture.api;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.EnumProperty;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import zone.moddev.mc.ironagefurniture.api.items.UpholsteredBlockItem;
import zone.moddev.mc.ironagefurniture.api.enumerations.UpholsteryColour;

/** Colour-specific items, with support for the old portable Color field. */
public final class UpholsteryItemData {
    public static final String COLOR = "Color";
    public static final EnumProperty<UpholsteryColour> COLOUR = EnumProperty.create("colour", UpholsteryColour.class);
    private UpholsteryItemData() { }
    public static UpholsteryColour getColour(ItemStack stack) {
        if (stack.getItem() instanceof UpholsteredBlockItem) {
            UpholsteryColour fixed = ((UpholsteredBlockItem)stack.getItem()).getColour();
            if (fixed != UpholsteryColour.RED) return fixed;
        }
        CompoundNBT tag = stack.getTag();
        return tag != null && tag.contains(COLOR, 8) ? UpholsteryColour.byName(tag.getString(COLOR))
                : UpholsteryColour.RED;
    }
    public static ItemStack create(Block block, UpholsteryColour colour) {
        net.minecraft.item.Item item = ForgeRegistries.ITEMS.getValue(itemId(block.getRegistryName(), colour));
        if (item instanceof UpholsteredBlockItem) return new ItemStack(item);
        ItemStack stack = new ItemStack(block);
        stack.getOrCreateTag().putString(COLOR, colour.getName());
        return stack;
    }
    public static ItemStack recolour(ItemStack original, UpholsteryColour colour) {
        if (original.getItem() instanceof UpholsteredBlockItem) {
            ItemStack result = create(((UpholsteredBlockItem)original.getItem()).getBlock(), colour);
            result.setCount(original.getCount());
            if (original.hasTag()) result.setTag(original.getTag().copy());
            removeLegacyField(result);
            return result;
        }
        ItemStack result = original.copy();
        result.getOrCreateTag().putString(COLOR, colour.getName());
        return result;
    }
    public static ResourceLocation itemId(ResourceLocation block, UpholsteryColour colour) {
        return colour == UpholsteryColour.RED ? block
                : new ResourceLocation(block.getNamespace(), block.getPath() + "_" + colour.getName());
    }
    public static void removeLegacyField(ItemStack stack) {
        if (!stack.hasTag()) return;
        stack.getTag().remove(COLOR);
        if (stack.getTag().isEmpty()) stack.setTag(null);
    }
}
