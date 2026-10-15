package zone.moddev.mc.ironagefurniture.api.items;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.NonNullList;
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
    @Override public void registerBlocks(java.util.Map<Block, Item> map, Item item) {
        if (metal == SconceMetal.IRON) super.registerBlocks(map, item);
    }
    @Override public void removeFromBlockToItemMap(java.util.Map<Block, Item> map, Item item) {
        if (metal == SconceMetal.IRON) super.removeFromBlockToItemMap(map, item);
    }
    @Override protected BlockState getPlacementState(BlockPlaceContext context) {
        BlockState state = super.getPlacementState(context);
        return state == null ? null : state.setValue(SconceMetalData.METAL, SconceMetalData.get(context.getItemInHand()));
    }
    @Override public String getDescriptionId(ItemStack stack) {
        SconceMetal metal = SconceMetalData.get(stack);
        return super.getDescriptionId() + (metal == SconceMetal.IRON ? "" : "." + metal.getSerializedName());
    }
    @Override public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
        if (allowdedIn(group) && SconceMetalData.available(metal)) items.add(new ItemStack(this));
    }
}
