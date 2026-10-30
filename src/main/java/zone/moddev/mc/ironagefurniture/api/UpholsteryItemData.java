package zone.moddev.mc.ironagefurniture.api;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import zone.moddev.mc.ironagefurniture.api.enumerations.UpholsteryColour;
import zone.moddev.mc.ironagefurniture.api.items.UpholsteredBlockItem;

/** Ordinary item identities select colours; the old Color field is import-only. */
public final class UpholsteryItemData {
    public static final String COLOR = "Color";
    public static final EnumProperty<UpholsteryColour> COLOUR = EnumProperty.create("colour", UpholsteryColour.class);
    private UpholsteryItemData() { }

    public static UpholsteryColour getColour(ItemStack stack) {
        if (stack.getItem() instanceof UpholsteredBlockItem item) return item.getColour();
        return UpholsteryColour.byName(stack.hasTag() ? stack.getTag().getString(COLOR) : "");
    }

    public static ResourceLocation itemId(ResourceLocation block, UpholsteryColour colour) {
        return colour == UpholsteryColour.RED ? block
                : new ResourceLocation(block.getNamespace(), block.getPath() + "_" + colour.getSerializedName());
    }

    public static ItemStack create(Block block, UpholsteryColour colour) {
        return new ItemStack(BuiltInRegistries.ITEM.get(itemId(BuiltInRegistries.BLOCK.getKey(block), colour)));
    }

    public static ItemStack recolour(ItemStack original, UpholsteryColour colour) {
        if (!(original.getItem() instanceof UpholsteredBlockItem item)) return original.copy();
        net.minecraft.nbt.CompoundTag saved = original.save(new net.minecraft.nbt.CompoundTag());
        saved.putString("id", BuiltInRegistries.ITEM.getKey(create(item.getBlock(), colour).getItem()).toString());
        ItemStack result = ItemStack.of(saved);
        removeLegacyField(result);
        return result;
    }

    public static void removeLegacyField(ItemStack stack) {
        if (stack.hasTag()) stack.getTag().remove(COLOR);
    }
}
