package com.mcmoddev.ironagefurniture.api.Blocks;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public abstract class FurnitureBlock extends Block implements SimpleWaterloggedBlock {
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final DirectionProperty DIRECTION = BlockStateProperties.HORIZONTAL_FACING;
    
    public ImmutableMap<BlockState, VoxelShape> _shapes;
    
	public FurnitureBlock(Properties properties) {
		super(properties);
		// TODO Auto-generated constructor stub
	}
	
	@Override
    public VoxelShape getShape(BlockState state, BlockGetter reader, BlockPos pos, CollisionContext context)
    {
        return _shapes.get(state);
    }

    @Override
    public VoxelShape getOcclusionShape(BlockState state, BlockGetter reader, BlockPos pos)
    {
        return _shapes.get(state);
    }
    
	public static VoxelShape rotate(VoxelShape source, Direction direction)
    {    	
    	switch(direction)
        {
            case WEST:                
                return Shapes.box(1.0F - source.max(Direction.Axis.X), source.min(Direction.Axis.Y), 1.0F - source.max(Direction.Axis.Z), 1.0F - source.min(Direction.Axis.X), source.max(Direction.Axis.Y), 1.0F - source.min(Direction.Axis.Z));
            case NORTH:
                return Shapes.box(source.min(Direction.Axis.Z), source.min(Direction.Axis.Y), 1.0F - source.max(Direction.Axis.X), source.max(Direction.Axis.Z), source.max(Direction.Axis.Y), 1.0F - source.min(Direction.Axis.X));
            case SOUTH:
                return Shapes.box(1.0F - source.max(Direction.Axis.Z), source.min(Direction.Axis.Y), source.min(Direction.Axis.X), 1.0F - source.min(Direction.Axis.Z), source.max(Direction.Axis.Y), source.max(Direction.Axis.X));
            default:
            	return Shapes.box(source.min(Direction.Axis.X), source.min(Direction.Axis.Y), source.min(Direction.Axis.Z), source.max(Direction.Axis.X), source.max(Direction.Axis.Y), source.max(Direction.Axis.Z));
        }	
    }
	
	@Override
    public boolean triggerEvent(BlockState state, Level world, BlockPos pos, int id, int type)
    {
        super.triggerEvent(state, world, pos, id, type);

        return world.getBlockEntity(pos) != null && world.getBlockEntity(pos).triggerEvent(id, type);
    }

    @Override
    public FluidState getFluidState(BlockState state)
    {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }
    
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
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
    public BlockState getStateForPlacement(BlockPlaceContext context)
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
