package zone.moddev.mc.ironagefurniture.api.items;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import zone.moddev.mc.ironagefurniture.api.SconceMetalData;
import zone.moddev.mc.ironagefurniture.api.enumerations.SconceMetal;

/** Distinct metal items share the existing sconce block and its metal state. */
public final class MetalSconceBlockItem extends BlockItem {
    private final SconceMetal metal;
    public MetalSconceBlockItem(Block block, Properties properties) { this(block, properties, SconceMetal.IRON); }
    public MetalSconceBlockItem(Block block, Properties properties, SconceMetal metal) {
        super(block, properties); this.metal = metal;
    }
    public SconceMetal getMetal() { return metal; }
    @Override public void addToBlockToItemMap(java.util.Map<Block, Item> map, Item item) {
        if (metal == SconceMetal.IRON) super.addToBlockToItemMap(map, item);
    }
    @Override public void removeFromBlockToItemMap(java.util.Map<Block, Item> map, Item item) {
        if (metal == SconceMetal.IRON) super.removeFromBlockToItemMap(map, item);
    }
    @Override protected BlockState getStateForPlacement(BlockItemUseContext context) {
        BlockState state = super.getStateForPlacement(context);
        return state == null ? null : state.with(SconceMetalData.METAL, SconceMetalData.get(context.getItem()));
    }
    @Override public String getTranslationKey(ItemStack stack) {
        SconceMetal metal = SconceMetalData.get(stack);
        return super.getTranslationKey() + (metal == SconceMetal.IRON ? "" : "." + metal.getName());
    }
    @Override public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        if (isInGroup(group) && SconceMetalData.available(metal)) items.add(new ItemStack(this));
    }
}
