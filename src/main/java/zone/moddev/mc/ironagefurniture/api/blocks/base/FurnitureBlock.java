package zone.moddev.mc.ironagefurniture.api.blocks.base;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.Direction;
import net.minecraft.util.BlockRenderLayer;
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
    public VoxelShape getRenderShape(BlockState state, IBlockReader reader, BlockPos pos)
    {
        return _shapes.get(state);
    }

	public static VoxelShape rotate(VoxelShape source, Direction direction)
    {
		switch (direction) {
			case WEST:
				return VoxelShapes.create(1.0F - source.getEnd(Direction.Axis.X), source.getStart(Direction.Axis.Y), 1.0F - source.getEnd(Direction.Axis.Z), 1.0F - source.getStart(Direction.Axis.X), source.getEnd(Direction.Axis.Y), 1.0F - source.getStart(Direction.Axis.Z));
			case NORTH:
				return VoxelShapes.create(source.getStart(Direction.Axis.Z), source.getStart(Direction.Axis.Y), 1.0F - source.getEnd(Direction.Axis.X), source.getEnd(Direction.Axis.Z), source.getEnd(Direction.Axis.Y), 1.0F - source.getStart(Direction.Axis.X));
			case SOUTH:
				return VoxelShapes.create(1.0F - source.getEnd(Direction.Axis.Z), source.getStart(Direction.Axis.Y), source.getStart(Direction.Axis.X), 1.0F - source.getStart(Direction.Axis.Z), source.getEnd(Direction.Axis.Y), source.getEnd(Direction.Axis.X));
			default:
				return VoxelShapes.create(source.getStart(Direction.Axis.X), source.getStart(Direction.Axis.Y), source.getStart(Direction.Axis.Z), source.getEnd(Direction.Axis.X), source.getEnd(Direction.Axis.Y), source.getEnd(Direction.Axis.Z));
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
	public boolean eventReceived(BlockState state, World world, BlockPos pos, int id, int type)
    {
        super.eventReceived(state, world, pos, id, type);

        return world.getTileEntity(pos) != null && world.getTileEntity(pos).receiveClientEvent(id, type);
    }

    @Override
    public IFluidState getFluidState(BlockState state)
    {
        return state .get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        super.fillStateContainer(builder);

        builder.add(DIRECTION);
        builder.add(WATERLOGGED);
    }

    protected abstract void generateShapes(ImmutableList<BlockState> states);

    @Override
    public BlockState rotate(BlockState state, Rotation rotation)
    {
        return state .with(DIRECTION, rotation.rotate(state .get(DIRECTION)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror)
    {
        return state.rotate(mirror.toRotation(state .get(DIRECTION)));
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        return this.getDefaultState()
                 .with(WATERLOGGED, context.getWorld().getFluidState(context.getPos()).getFluid() == Fluids.WATER)
                 .with(DIRECTION, context.getPlacementHorizontalFacing());
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    public static VoxelShape[] getShapes(VoxelShape source)
    {
        return new VoxelShape[] { rotate(source, Direction.SOUTH), rotate(source, Direction.WEST), rotate(source, Direction.NORTH), rotate(source, Direction.EAST) };
    }
}
