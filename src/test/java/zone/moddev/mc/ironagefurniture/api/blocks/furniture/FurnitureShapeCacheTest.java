package zone.moddev.mc.ironagefurniture.api.blocks.furniture;

import static org.junit.Assert.*;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.server.Bootstrap;
import org.junit.Test;
import zone.moddev.mc.ironagefurniture.api.blocks.base.FurnitureBlock;
import zone.moddev.mc.ironagefurniture.api.enumerations.ChairPart;

public class FurnitureShapeCacheTest {
    @Test public void reusedShapesMatchTheOriginalCuboidsForEveryState() {
        net.minecraft.SharedConstants.tryDetectVersion(); Bootstrap.bootStrap();
        for (Block block : new Block[]{new WoodenBed("cache_wood", false), new WoodenBed("cache_double", true),
                new CanopyBed("cache_canopy", false), new CanopyBed("cache_double_canopy", true),
                new WingbackChair("cache_wingback"), new ThroneChair("cache_throne")}) {
            Map<String, VoxelShape> cached = new HashMap<>();
            for (BlockState state : block.getStateDefinition().getPossibleStates()) {
                Direction direction = state.getValue(FurnitureBlock.DIRECTION);
                String part = block instanceof MultiBlockChair ? state.getValue(MultiBlockChair.PART).name()
                        : ((FurnitureBed)block).upper(state) ? "canopy" : ((FurnitureBed)block).head(state) ? "head" : "foot";
                String key = part + direction;
                VoxelShape actual = block.getShape(state, null, BlockPos.ZERO, CollisionContext.empty());
                if (cached.containsKey(key)) assertSame(key, cached.get(key), actual);
                else {
                    VoxelShape original;
                    if (block instanceof MultiBlockChair) original = state.getValue(MultiBlockChair.PART) == ChairPart.LOWER
                            ? Shapes.or(Block.box(1, 6, 2, 15, 8, 15), Block.box(0, 0, 2, 2, 13, 15),
                                    Block.box(14, 0, 2, 16, 13, 15), Block.box(2, 0, 14, 14, 16, 16))
                            : ((MultiBlockChair)block).upperShape(state.getValue(MultiBlockChair.PART));
                    else original = part.equals("canopy") ? Block.box(0, 13, 0, 16, 16, 16)
                            : Block.box(0, 5, part.equals("head") ? 1 : 0, 16, 10, part.equals("head") ? 16 : 15);
                    assertFalse(key, Shapes.joinIsNotEmpty(actual, UprightFurnitureShapes.rotate(original, direction),
                            BooleanOp.NOT_SAME));
                    cached.put(key, actual);
                }
            }
        }
    }
}
