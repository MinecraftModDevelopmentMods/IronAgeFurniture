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

/** Metals share the existing sconce item ID rather than allocating extra furniture IDs. */
public final class MetalSconceBlockItem extends BlockItem {
    public MetalSconceBlockItem(Block block, Properties properties) { super(block, properties); }
    @Override protected BlockState getStateForPlacement(BlockItemUseContext context) {
        BlockState state = super.getStateForPlacement(context);
        return state == null ? null : state.with(SconceMetalData.METAL, SconceMetalData.get(context.getItem()));
    }
    @Override public String getTranslationKey(ItemStack stack) {
        SconceMetal metal = SconceMetalData.get(stack);
        return super.getTranslationKey() + (metal == SconceMetal.IRON ? "" : "." + metal.getName());
    }
    @Override public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        if (isInGroup(group)) for (SconceMetal metal : SconceMetal.values())
            if (SconceMetalData.available(metal)) items.add(SconceMetalData.create(getBlock(), metal));
    }
}
