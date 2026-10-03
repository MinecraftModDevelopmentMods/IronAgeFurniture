package zone.moddev.mc.ironagefurniture.api.items;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import zone.moddev.mc.ironagefurniture.api.UpholsteryItemData;
import zone.moddev.mc.ironagefurniture.api.enumerations.UpholsteryColour;

/** Sixteen inventory variants share one item ID and keep their colour in NBT. */
public final class UpholsteredBlockItem extends BlockItem {
    public UpholsteredBlockItem(Block block, Properties properties) { super(block, properties); }
    @Override public String getTranslationKey(ItemStack stack) {
        return super.getTranslationKey() + "." + UpholsteryItemData.getColour(stack).getName();
    }
    @Override public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        if (isInGroup(group)) for (UpholsteryColour colour : UpholsteryColour.values())
            items.add(UpholsteryItemData.create(getBlock(), colour));
    }
}
