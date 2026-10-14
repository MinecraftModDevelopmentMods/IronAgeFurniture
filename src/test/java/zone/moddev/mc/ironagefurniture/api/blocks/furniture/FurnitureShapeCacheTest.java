package zone.moddev.mc.ironagefurniture.api.blocks.furniture;

import static org.junit.Assert.*;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.registry.Bootstrap;
import org.junit.Test;
import zone.moddev.mc.ironagefurniture.api.blocks.base.FurnitureBlock;
import zone.moddev.mc.ironagefurniture.api.enumerations.ChairPart;

public class FurnitureShapeCacheTest {
    @Test public void reusedShapesMatchTheOriginalCuboidsForEveryState() {
        Bootstrap.bootStrap();
        for (Block block : new Block[]{new WoodenBed("cache_wood", false), new WoodenBed("cache_double", true),
                new CanopyBed("cache_canopy", false), new CanopyBed("cache_double_canopy", true),
                new WingbackChair("cache_wingback"), new ThroneChair("cache_throne")}) {
            Map<String, VoxelShape> cached = new HashMap<>();
            for (BlockState state : block.getStateDefinition().getPossibleStates()) {
                Direction direction = state.getValue(FurnitureBlock.DIRECTION);
                String part = block instanceof MultiBlockChair ? state.getValue(MultiBlockChair.PART).name()
                        : ((FurnitureBed)block).upper(state) ? "canopy" : ((FurnitureBed)block).head(state) ? "head" : "foot";
                String key = part + direction;
                VoxelShape actual = block.getShape(state, null, BlockPos.ZERO, ISelectionContext.empty());
                if (cached.containsKey(key)) assertSame(key, cached.get(key), actual);
                else {
                    VoxelShape original;
                    if (block instanceof MultiBlockChair) original = state.getValue(MultiBlockChair.PART) == ChairPart.LOWER
                            ? VoxelShapes.or(Block.box(1, 6, 2, 15, 8, 15), Block.box(0, 0, 2, 2, 13, 15),
                                    Block.box(14, 0, 2, 16, 13, 15), Block.box(2, 0, 14, 14, 16, 16))
                            : ((MultiBlockChair)block).upperShape(state.getValue(MultiBlockChair.PART));
                    else original = part.equals("canopy") ? Block.box(0, 13, 0, 16, 16, 16)
                            : Block.box(0, 5, part.equals("head") ? 1 : 0, 16, 10, part.equals("head") ? 16 : 15);
                    assertFalse(key, VoxelShapes.joinIsNotEmpty(actual, UprightFurnitureShapes.rotate(original, direction),
                            IBooleanFunction.NOT_SAME));
                    cached.put(key, actual);
                }
            }
        }
    }
}
