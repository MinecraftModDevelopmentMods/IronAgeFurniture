package zone.moddev.mc.ironagefurniture.api.items;

import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import zone.moddev.mc.ironagefurniture.api.enumerations.UpholsteryColour;

/** Sixteen normal inventory items place one persistent, colour-aware block. */
public final class UpholsteredBlockItem extends BlockItem {
    private final UpholsteryColour colour;

    public UpholsteredBlockItem(Block block, Properties properties, UpholsteryColour colour) {
        super(block, properties);
        this.colour = colour;
    }

    public UpholsteryColour getColour() { return colour; }

    @Override public String getDescriptionId() {
        // Native 1.19.4 BlockItem otherwise uses the single block's red name for all sixteen items.
        return Util.makeDescriptionId("block", BuiltInRegistries.ITEM.getKey(this));
    }

    @Override public void registerBlocks(java.util.Map<Block, Item> map, Item item) {
        if (colour == UpholsteryColour.RED) super.registerBlocks(map, item);
    }
}
