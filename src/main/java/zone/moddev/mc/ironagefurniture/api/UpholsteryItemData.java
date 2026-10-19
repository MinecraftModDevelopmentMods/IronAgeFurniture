package zone.moddev.mc.ironagefurniture.api;

import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
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
        return UpholsteryColour.byName(stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY)
                .copyTag().getString(COLOR).orElse("red"));
    }

    public static Identifier itemId(Identifier block, UpholsteryColour colour) {
        return colour == UpholsteryColour.RED ? block
                : Identifier.fromNamespaceAndPath(block.getNamespace(), block.getPath() + "_" + colour.getSerializedName());
    }

    public static ItemStack create(Block block, UpholsteryColour colour) {
        return new ItemStack(BuiltInRegistries.ITEM.getValue(itemId(BuiltInRegistries.BLOCK.getKey(block), colour)));
    }

    public static ItemStack recolour(ItemStack original, UpholsteryColour colour) {
        if (!(original.getItem() instanceof UpholsteredBlockItem item)) return original.copy();
        ItemStack result = original.transmuteCopy(create(item.getBlock(), colour).getItem());
        removeLegacyField(result);
        return result;
    }

    public static void removeLegacyField(ItemStack stack) {
        CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> tag.remove(COLOR));
    }
}
