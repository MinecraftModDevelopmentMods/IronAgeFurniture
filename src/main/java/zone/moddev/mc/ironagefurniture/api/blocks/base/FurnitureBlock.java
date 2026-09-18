package zone.moddev.mc.ironagefurniture.api.blocks.base;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.Direction;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.block.Block;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.block.BlockState;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.fluid.IFluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.shapes.VoxelShape;
import com.mojang.datafixers.util.Pair;

public abstract class FurnitureBlock extends Block implements IWaterLoggable {
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final DirectionProperty DIRECTION = BlockStateProperties.HORIZONTAL_FACING;

    public ImmutableMap<BlockState, VoxelShape> _shapes;

	public FurnitureBlock(Properties properties) {
		super(properties);
		// TODO Auto-generated constructor stub
	}

	@Override
    public VoxelShape getShape(BlockState state, IBlockReader reader, BlockPos pos, ISelectionContext context)
    {
        return _shapes.get(state);
    }

    @Override
    public VoxelShape getOcclusionShape(BlockState state, IBlockReader reader, BlockPos pos)
    {
        return _shapes.get(state);
    }

	public static VoxelShape rotate(VoxelShape source, Direction direction)
    {
		switch (direction) {
			case WEST:
				return VoxelShapes.box(1.0F - source.max(Direction.Axis.X), source.min(Direction.Axis.Y), 1.0F - source.max(Direction.Axis.Z), 1.0F - source.min(Direction.Axis.X), source.max(Direction.Axis.Y), 1.0F - source.min(Direction.Axis.Z));
			case NORTH:
				return VoxelShapes.box(source.min(Direction.Axis.Z), source.min(Direction.Axis.Y), 1.0F - source.max(Direction.Axis.X), source.max(Direction.Axis.Z), source.max(Direction.Axis.Y), 1.0F - source.min(Direction.Axis.X));
			case SOUTH:
				return VoxelShapes.box(1.0F - source.max(Direction.Axis.Z), source.min(Direction.Axis.Y), source.min(Direction.Axis.X), 1.0F - source.min(Direction.Axis.Z), source.max(Direction.Axis.Y), source.max(Direction.Axis.X));
			default:
				return VoxelShapes.box(source.min(Direction.Axis.X), source.min(Direction.Axis.Y), source.min(Direction.Axis.Z), source.max(Direction.Axis.X), source.max(Direction.Axis.Y), source.max(Direction.Axis.Z));
		}
    }

	public static Pair<Double, Double> rotate(double x, double z, Direction direction)
    {
		switch (direction) {
			case WEST:
				return Pair.of(1.0F - x, 1.0F - z);
			case NORTH:
				return Pair.of(z, 1.0F - x);
			case SOUTH:
				return Pair.of(1.0F - z, x);
			default:
				return Pair.of(x, z);
		}
    }

	@Override
    public boolean triggerEvent(BlockState state, World world, BlockPos pos, int id, int type)
    {
        super.triggerEvent(state, world, pos, id, type);

        return world.getBlockEntity(pos) != null && world.getBlockEntity(pos).triggerEvent(id, type);
    }

    @Override
    public IFluidState getFluidState(BlockState state)
    {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder)
    {
        super.createBlockStateDefinition(builder);

        builder.add(DIRECTION);
        builder.add(WATERLOGGED);
    }

    protected abstract void generateShapes(ImmutableList<BlockState> states);

    @Override
    public BlockState rotate(BlockState state, Rotation rotation)
    {
        return state.setValue(DIRECTION, rotation.rotate(state.getValue(DIRECTION)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror)
    {
        return state.rotate(mirror.getRotation(state.getValue(DIRECTION)));
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        return this.defaultBlockState()
				.setValue(WATERLOGGED, context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER)
				.setValue(DIRECTION, context.getHorizontalDirection());
    }

    public static VoxelShape[] getShapes(VoxelShape source)
    {
        return new VoxelShape[] { rotate(source, Direction.SOUTH), rotate(source, Direction.WEST), rotate(source, Direction.NORTH), rotate(source, Direction.EAST) };
    }
}
