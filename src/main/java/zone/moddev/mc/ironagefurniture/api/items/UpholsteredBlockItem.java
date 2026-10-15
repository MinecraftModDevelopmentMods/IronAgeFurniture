package zone.moddev.mc.ironagefurniture.api.items;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.NonNullList;
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
    @Override public void registerBlocks(java.util.Map<Block, Item> map, Item item) {
        if (colour == UpholsteryColour.RED) super.registerBlocks(map, item);
    }
    @Override public void removeFromBlockToItemMap(java.util.Map<Block, Item> map, Item item) {
        if (colour == UpholsteryColour.RED) super.removeFromBlockToItemMap(map, item);
    }
    @Override public String getDescriptionId(ItemStack stack) {
        return super.getDescriptionId() + "." + UpholsteryItemData.getColour(stack).getName();
    }
    @Override public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
        if (allowdedIn(group) && FurnitureCreativeVisibility.isVisible(this)) items.add(new ItemStack(this));
    }
}
