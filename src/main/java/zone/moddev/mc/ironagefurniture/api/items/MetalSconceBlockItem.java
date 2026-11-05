package zone.moddev.mc.ironagefurniture.api.items;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import zone.moddev.mc.ironagefurniture.api.SconceMetalData;
import zone.moddev.mc.ironagefurniture.api.enumerations.SconceMetal;

/** Each metal item places its matching fixed metal block. */
public final class MetalSconceBlockItem extends BlockItem {
    private final SconceMetal metal;
    public MetalSconceBlockItem(Block block, Properties properties) { this(block, properties, SconceMetal.IRON); }
    public MetalSconceBlockItem(Block block, Properties properties, SconceMetal metal) {
        super(block, properties); this.metal = metal;
    }
    public SconceMetal getMetal() { return metal; }
    @Override protected BlockState getStateForPlacement(BlockItemUseContext context) {
        BlockState state = super.getStateForPlacement(context);
        return state == null ? null : SconceMetalData.withMetal(state, SconceMetalData.get(context.getItem()));
    }
    @Override public String getTranslationKey(ItemStack stack) {
        SconceMetal metal = SconceMetalData.get(stack);
        String family = SconceMetalData.ironPath(getBlock().getRegistryName().getPath());
        return "block.ironagefurniture." + family + (metal == SconceMetal.IRON ? "" : "." + metal.getName());
    }
    @Override public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        if (isInGroup(group) && SconceMetalData.available(metal)) items.add(new ItemStack(this));
    }
}
