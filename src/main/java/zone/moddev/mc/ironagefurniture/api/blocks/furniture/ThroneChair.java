package zone.moddev.mc.ironagefurniture.api.blocks.furniture;

import net.minecraft.block.Block;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import zone.moddev.mc.ironagefurniture.api.enumerations.ChairPart;

public final class ThroneChair extends MultiBlockChair {
    public ThroneChair(String name) { super(name); }
    @Override public int getChairHeight() { return 3; }
    @Override protected VoxelShape upperShape(ChairPart part) {
        boolean middle = part == ChairPart.MIDDLE;
        double front = middle ? 9 : 8, height = middle ? 16 : 12;
        VoxelShape shape = VoxelShapes.or(Block.makeCuboidShape(0, 0, front, 2, height, 16),
                Block.makeCuboidShape(14, 0, front, 16, height, 16), Block.makeCuboidShape(2, 0, 14, 14, height, 16));
        return middle ? shape : VoxelShapes.or(shape, Block.makeCuboidShape(2, 13, 0, 14, 15, 15));
    }
}
