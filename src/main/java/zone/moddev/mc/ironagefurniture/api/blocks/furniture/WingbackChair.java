package zone.moddev.mc.ironagefurniture.api.blocks.furniture;

import net.minecraft.block.Block;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import zone.moddev.mc.ironagefurniture.api.enumerations.ChairPart;

public final class WingbackChair extends MultiBlockChair {
    public WingbackChair(String name) { super(name); }
    @Override public int getChairHeight() { return 2; }
    @Override protected VoxelShape upperShape(ChairPart part) {
        return VoxelShapes.or(Block.makeCuboidShape(0, 0, 8, 2, 12, 16),
                Block.makeCuboidShape(14, 0, 8, 16, 12, 16), Block.makeCuboidShape(2, 0, 14, 14, 12, 16));
    }
}
