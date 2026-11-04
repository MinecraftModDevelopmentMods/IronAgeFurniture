package zone.moddev.mc.ironagefurniture.api.items;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import zone.moddev.mc.ironagefurniture.init.ModCreativeTab;

/** Keep native category and search contents consistent with server settings. */
public class FurnitureBlockItem extends BlockItem {
    public FurnitureBlockItem(Block block, Properties properties) {
        super(block, properties.tab(ModCreativeTab.forBlock(block)));
    }
    @Override public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> output) {
        if (ModCreativeTab.visible(this) && (tab == CreativeModeTab.TAB_SEARCH || tab == getItemCategory()))
            output.add(getDefaultInstance());
    }
}
