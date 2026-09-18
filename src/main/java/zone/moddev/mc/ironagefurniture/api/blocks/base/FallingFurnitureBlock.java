package zone.moddev.mc.ironagefurniture.api.blocks.base;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FallingBlock;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

/**
 * Forge 1.16 exposes landing callbacks through FallingBlock rather than the
 * later Fallable interface. This preserves the shared furniture behavior
 * without making every FurnitureBlock fall.
 */
public abstract class FallingFurnitureBlock extends FallingBlock implements IWaterLoggable {
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final DirectionProperty DIRECTION = BlockStateProperties.HORIZONTAL_FACING;

    public ImmutableMap<BlockState, VoxelShape> _shapes;

    protected FallingFurnitureBlock(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader reader, BlockPos pos, ISelectionContext context) {
        return _shapes.get(state);
    }

    @Override
    public VoxelShape getOcclusionShape(BlockState state, IBlockReader reader, BlockPos pos) {
        return _shapes.get(state);
    }

    @Override
    public boolean triggerEvent(BlockState state, World world, BlockPos pos, int id, int type) {
        super.triggerEvent(state, world, pos, id, type);
        return world.getBlockEntity(pos) != null && world.getBlockEntity(pos).triggerEvent(id, type);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(DIRECTION, WATERLOGGED);
    }

    protected abstract void generateShapes(ImmutableList<BlockState> states);

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(DIRECTION, rotation.rotate(state.getValue(DIRECTION)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(DIRECTION)));
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return defaultBlockState()
                .setValue(WATERLOGGED,
                        context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER)
                .setValue(DIRECTION, context.getHorizontalDirection());
    }

    public static VoxelShape rotate(VoxelShape source, Direction direction) {
        return FurnitureBlock.rotate(source, direction);
    }

    public static VoxelShape[] getShapes(VoxelShape source) {
        return new VoxelShape[] {
                rotate(source, Direction.SOUTH),
                rotate(source, Direction.WEST),
                rotate(source, Direction.NORTH),
                rotate(source, Direction.EAST)
        };
    }
}
