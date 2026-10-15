package zone.moddev.mc.ironagefurniture.api.items;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.NonNullList;

/** Keeps disabled seating families out of both their tab and creative search. */
public final class FurnitureBlockItem extends BlockItem {
    public FurnitureBlockItem(Block block, Properties properties) { super(block, properties); }
    @Override public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
        if (FurnitureCreativeVisibility.isVisible(this)) super.fillItemCategory(group, items);
    }
}
