package zone.moddev.mc.ironagefurniture.api.items;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import zone.moddev.mc.ironagefurniture.api.UpholsteryItemData;
import zone.moddev.mc.ironagefurniture.api.enumerations.UpholsteryColour;

/** Each colour has its own item; all sixteen still place the same saved block. */
public final class UpholsteredBlockItem extends BlockItem {
    private final UpholsteryColour colour;
    public UpholsteredBlockItem(Block block, Properties properties) { this(block, properties, UpholsteryColour.RED); }
    public UpholsteredBlockItem(Block block, Properties properties, UpholsteryColour colour) {
        super(block, properties); this.colour = colour;
    }
    public UpholsteryColour getColour() { return colour; }
    @Override public void addToBlockToItemMap(java.util.Map<Block, Item> map, Item item) {
        if (colour == UpholsteryColour.RED) super.addToBlockToItemMap(map, item);
    }
    @Override public void removeFromBlockToItemMap(java.util.Map<Block, Item> map, Item item) {
        if (colour == UpholsteryColour.RED) super.removeFromBlockToItemMap(map, item);
    }
    @Override public String getTranslationKey(ItemStack stack) {
        return super.getTranslationKey() + "." + UpholsteryItemData.getColour(stack).getName();
    }
    @Override public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        if (isInGroup(group)) items.add(new ItemStack(this));
    }
}
