package zone.moddev.mc.ironagefurniture.api.items;

import java.util.Map;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import zone.moddev.mc.ironagefurniture.api.SconceMetalData;
import zone.moddev.mc.ironagefurniture.api.enumerations.SconceMetal;

/** The existing item IDs now place the matching fixed-metal frame. */
public final class MetalSconceBlockItem extends BlockItem {
    private final SconceMetal metal;
    public MetalSconceBlockItem(Block block, Properties properties, SconceMetal metal) {
        // A shattered lava lamp must not burn away its surviving metal frame.
        super(block, properties.fireResistant());
        this.metal = metal;
    }
    public SconceMetal getMetal() { return metal; }
    @Override public void registerBlocks(Map<Block, Item> map, Item item) {
        super.registerBlocks(map, item);
    }
    @Override protected BlockState getPlacementState(BlockPlaceContext context) {
        BlockState state = super.getPlacementState(context);
        return state == null ? null : SconceMetalData.withMetal(state, metal);
    }
}
