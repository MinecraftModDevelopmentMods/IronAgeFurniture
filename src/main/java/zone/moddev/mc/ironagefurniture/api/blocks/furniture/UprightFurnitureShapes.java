package zone.moddev.mc.ironagefurniture.api.blocks.furniture;

import net.minecraft.core.Direction;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;

/** These models face south; rotate each cuboid, keeping gaps in composite shapes. */
public final class UprightFurnitureShapes {
    private UprightFurnitureShapes() { }
    public static VoxelShape rotate(VoxelShape shape, Direction facing) {
        VoxelShape[] result = {Shapes.empty()};
        shape.forAllBoxes((x1, y1, z1, x2, y2, z2) -> {
            VoxelShape box;
            switch (facing) {
                case NORTH: box = Shapes.box(1 - x2, y1, 1 - z2, 1 - x1, y2, 1 - z1); break;
                case EAST: box = Shapes.box(z1, y1, 1 - x2, z2, y2, 1 - x1); break;
                case WEST: box = Shapes.box(1 - z2, y1, x1, 1 - z1, y2, x2); break;
                default: box = Shapes.box(x1, y1, z1, x2, y2, z2);
            }
            result[0] = Shapes.or(result[0], box);
        });
        return result[0];
    }
}
