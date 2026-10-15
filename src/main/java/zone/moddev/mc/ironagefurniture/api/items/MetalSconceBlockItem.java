package zone.moddev.mc.ironagefurniture.api.items;

import java.util.Map;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import zone.moddev.mc.ironagefurniture.api.SconceMetalData;
import zone.moddev.mc.ironagefurniture.api.enumerations.SconceMetal;

/** Normal metal-specific items place the same persistent sconce frame. */
public final class MetalSconceBlockItem extends BlockItem {
    private final SconceMetal metal;
    public MetalSconceBlockItem(Block block, Properties properties, SconceMetal metal) {
        super(block, properties);
        this.metal = metal;
    }
    public SconceMetal getMetal() { return metal; }
    @Override public void registerBlocks(Map<Block, Item> map, Item item) {
        if (metal == SconceMetal.IRON) super.registerBlocks(map, item);
    }
    @Override protected BlockState getPlacementState(BlockPlaceContext context) {
        BlockState state = super.getPlacementState(context);
        return state == null ? null : state.setValue(SconceMetalData.METAL, metal);
    }
}
