package zone.moddev.mc.ironagefurniture.api.items;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

/** Keeps disabled seating families out of both their tab and creative search. */
public final class FurnitureBlockItem extends BlockItem {
    public FurnitureBlockItem(Block block, Properties properties) { super(block, properties); }
    @Override public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        if (FurnitureCreativeVisibility.isVisible(this)) super.fillItemGroup(group, items);
    }
}
