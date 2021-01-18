package com.mcmoddev.ironagefurniture.api.Blocks;

import java.util.concurrent.atomic.AtomicReference;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mcmoddev.ironagefurniture.api.entity.Seat;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;


public class Chair extends Block
{
    public ImmutableMap<BlockState, VoxelShape> _shapes;
    
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final DirectionProperty DIRECTION = BlockStateProperties.HORIZONTAL_FACING;
    
    public static VoxelShape setMaxHeight(VoxelShape source, double height)
    {
        AtomicReference<VoxelShape> ar = new AtomicReference<>(VoxelShapes.empty());
        
        source.forEachBox((x1, y1, z1, x2, y2, z2) -> { ar.set(VoxelShapes.combine(ar.get(), VoxelShapes.create(x1, y1, z1, x2, height, z2), IBooleanFunction.OR));});
        
        return ar.get().simplify();
    }

    public static VoxelShape limitHorizontal(VoxelShape source)
    {
        AtomicReference<VoxelShape> ar = new AtomicReference<>(VoxelShapes.empty());
        
        source.forEachBox((x1, y1, z1, x2, y2, z2) -> { ar.set(VoxelShapes.combine(ar.get(), VoxelShapes.create(limit(x1), y1, limit(z1), limit(x2), y2, limit(z2)), IBooleanFunction.OR));});
        
        return ar.get().simplify();
    }

    public static VoxelShape[] getShapes(VoxelShape source)
    {      
        return new VoxelShape[] { rotate(source, Direction.SOUTH), rotate(source, Direction.WEST), rotate(source, Direction.NORTH), rotate(source, Direction.EAST) };
    }


    public static VoxelShape rotate(VoxelShape source, Direction direction)
    {    	
    	switch(direction)
        {
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

    private static double limit(double value)
    {
        return Math.max(0.0, Math.min(1.0, value));
    }

    @Override
    public BlockRenderLayer getRenderLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public int getComparatorInputOverride(BlockState state, World world, BlockPos pos)
    {
        return Container.calcRedstone(world.getTileEntity(pos));
        
    }

    @Override
    public boolean hasComparatorInputOverride(BlockState state)
    {
        return this.hasTileEntity(state);
    }

    @Override
    public void onReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving)
    {
        if(state.getBlock() != newState.getBlock())
        {
            if(world.getTileEntity(pos) instanceof IInventory)
            {
                InventoryHelper.dropInventoryItems(world, pos, (IInventory) world.getTileEntity(pos));
                world.updateComparatorOutputLevel(pos, this);
            }
        }
        
        super.onReplaced(state, world, pos, newState, isMoving);
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
        return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        super.fillStateContainer(builder);
        
        builder.add(DIRECTION);
        builder.add(WATERLOGGED);
    }
    
    public Chair(Properties properties)
    {
        super(properties);
        this.setDefaultState(this.getStateContainer().getBaseState().with(DIRECTION, Direction.NORTH).with(WATERLOGGED, false));
        
        this.generateShapes(this.getStateContainer().getValidStates());
    }

    public Chair(float hardness, float blastResistance, SoundType sound, String name) {
		super(Block.Properties.create(Material.WOOD).harvestTool(ToolType.AXE)
				.hardnessAndResistance(hardness, blastResistance).sound(sound));
		
		this.setDefaultState(this.getStateContainer().getBaseState().with(DIRECTION, Direction.NORTH));
		
		this.generateShapes(this.getStateContainer().getValidStates());
		
		this.setRegistryName(name);
	}

    protected void generateShapes(ImmutableList<BlockState> states)
    {
        ImmutableMap.Builder<BlockState, VoxelShape> builder = new ImmutableMap.Builder<>();
        for(BlockState state : states)
        {
        	VoxelShape shapes = VoxelShapes.empty();
        
        	// chair body
        	shapes = VoxelShapes.combine(shapes, getShapes(rotate(Block.makeCuboidShape(1, 7, 1, 15, 8, 14), Direction.SOUTH))[state.get(DIRECTION).getHorizontalIndex()], IBooleanFunction.OR); // chair base
        	shapes = VoxelShapes.combine(shapes, getShapes(rotate(Block.makeCuboidShape(3, 9, 1, 13, 23, 2), Direction.SOUTH))[state.get(DIRECTION).getHorizontalIndex()], IBooleanFunction.OR); // chair back
        	
        	//legs
        	shapes = VoxelShapes.combine(shapes, getShapes(rotate(Block.makeCuboidShape(1, 0, 12, 2, 8, 13), Direction.SOUTH))[state.get(DIRECTION).getHorizontalIndex()], IBooleanFunction.OR); //front left leg
            shapes = VoxelShapes.combine(shapes, getShapes(rotate(Block.makeCuboidShape(14, 0, 12, 15, 8, 13), Direction.SOUTH))[state.get(DIRECTION).getHorizontalIndex()], IBooleanFunction.OR); // front right leg
            shapes = VoxelShapes.combine(shapes, getShapes(rotate(Block.makeCuboidShape(1, 0, 1, 3, 22, 3), Direction.SOUTH))[state.get(DIRECTION).getHorizontalIndex()], IBooleanFunction.OR); // back left leg
            shapes = VoxelShapes.combine(shapes, getShapes(rotate(Block.makeCuboidShape(13, 0, 1, 15, 22, 3), Direction.SOUTH))[state.get(DIRECTION).getHorizontalIndex()], IBooleanFunction.OR); // back right leg
            
            builder.put(state, shapes.simplify());
        }
        
        _shapes = builder.build();
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

    @Override
    public boolean onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult)
    {
        return Seat.create(world, pos, 0.3, player);
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        return this.getDefaultState().with(WATERLOGGED, context.getWorld().getFluidState(context.getPos()).getFluid() == Fluids.WATER).with(DIRECTION, context.getPlacementHorizontalFacing());
    }
    
    @Override
    public BlockState rotate(BlockState state, Rotation rotation)
    {
        return state.with(DIRECTION, rotation.rotate(state.get(DIRECTION)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror)
    {
        return state.rotate(mirror.toRotation(state.get(DIRECTION)));
    }
}