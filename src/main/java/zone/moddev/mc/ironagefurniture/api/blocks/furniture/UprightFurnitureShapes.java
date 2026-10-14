package zone.moddev.mc.ironagefurniture.api.blocks.furniture;

import net.minecraft.util.Direction;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;

/** These models face south; rotate each cuboid, keeping gaps in composite shapes. */
final class UprightFurnitureShapes {
    private UprightFurnitureShapes() { }
    static VoxelShape rotate(VoxelShape shape, Direction facing) {
        VoxelShape[] result = {VoxelShapes.empty()};
        shape.forAllBoxes((x1, y1, z1, x2, y2, z2) -> {
            VoxelShape box;
            switch (facing) {
                case NORTH: box = VoxelShapes.box(1 - x2, y1, 1 - z2, 1 - x1, y2, 1 - z1); break;
                case EAST: box = VoxelShapes.box(z1, y1, 1 - x2, z2, y2, 1 - x1); break;
                case WEST: box = VoxelShapes.box(1 - z2, y1, x1, 1 - z1, y2, x2); break;
                default: box = VoxelShapes.box(x1, y1, z1, x2, y2, z2);
            }
            result[0] = VoxelShapes.or(result[0], box);
        });
        return result[0];
    }
}
