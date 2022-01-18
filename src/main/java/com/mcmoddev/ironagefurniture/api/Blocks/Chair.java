package com.mcmoddev.ironagefurniture.api.Blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mcmoddev.ironagefurniture.api.entity.Seat;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext.Builder;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ActionResultType;
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


import net.minecraft.block.AbstractBlock.Properties;

public class Chair extends Block
{
    public ImmutableMap<BlockState, VoxelShape> _shapes;
    
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final DirectionProperty DIRECTION = BlockStateProperties.HORIZONTAL_FACING;
    
    
    @Override
    public List<ItemStack> getDrops(BlockState state, Builder builder) {
    	List<ItemStack> drops;
    	
    	Item item = state.getBlock().asItem();
    	ItemStack stack = new ItemStack(item, 1); 
    	drops = new ArrayList<ItemStack>();
    	drops.add(stack);
    	
    	return drops;
    }
    
    public static VoxelShape setMaxHeight(VoxelShape source, double height)
    {
        AtomicReference<VoxelShape> ar = new AtomicReference<>(VoxelShapes.empty());
        
        source.forAllBoxes((x1, y1, z1, x2, y2, z2) -> { ar.set(VoxelShapes.joinUnoptimized(ar.get(), VoxelShapes.box(x1, y1, z1, x2, height, z2), IBooleanFunction.OR));});
        
        return ar.get().optimize();
    }

    public static VoxelShape limitHorizontal(VoxelShape source)
    {
        AtomicReference<VoxelShape> ar = new AtomicReference<>(VoxelShapes.empty());
        
        source.forAllBoxes((x1, y1, z1, x2, y2, z2) -> { ar.set(VoxelShapes.joinUnoptimized(ar.get(), VoxelShapes.box(limit(x1), y1, limit(z1), limit(x2), y2, limit(z2)), IBooleanFunction.OR));});
        
        return ar.get().optimize();
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
                return VoxelShapes.box(1.0F - source.max(Direction.Axis.X), source.min(Direction.Axis.Y), 1.0F - source.max(Direction.Axis.Z), 1.0F - source.min(Direction.Axis.X), source.max(Direction.Axis.Y), 1.0F - source.min(Direction.Axis.Z));
            case NORTH:
                return VoxelShapes.box(source.min(Direction.Axis.Z), source.min(Direction.Axis.Y), 1.0F - source.max(Direction.Axis.X), source.max(Direction.Axis.Z), source.max(Direction.Axis.Y), 1.0F - source.min(Direction.Axis.X));
            case SOUTH:
                return VoxelShapes.box(1.0F - source.max(Direction.Axis.Z), source.min(Direction.Axis.Y), source.min(Direction.Axis.X), 1.0F - source.min(Direction.Axis.Z), source.max(Direction.Axis.Y), source.max(Direction.Axis.X));
            default:
            	return VoxelShapes.box(source.min(Direction.Axis.X), source.min(Direction.Axis.Y), source.min(Direction.Axis.Z), source.max(Direction.Axis.X), source.max(Direction.Axis.Y), source.max(Direction.Axis.Z));
        }	
    }

    private static double limit(double value)
    {
        return Math.max(0.0, Math.min(1.0, value));
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, World world, BlockPos pos)
    {
        return Container.getRedstoneSignalFromBlockEntity(world.getBlockEntity(pos));
        
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state)
    {
        return this.hasTileEntity(state);
    }

    @Override
    public void onRemove(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving)
    {
        if(state.getBlock() != newState.getBlock())
        {
            if(world.getBlockEntity(pos) instanceof IInventory)
            {
                InventoryHelper.dropContents(world, pos, (IInventory) world.getBlockEntity(pos));
                world.updateNeighbourForOutputSignal(pos, this);
            }
        }
        
        super.onRemove(state, world, pos, newState, isMoving);
    }

    @Override
    public boolean triggerEvent(BlockState state, World world, BlockPos pos, int id, int type)
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
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder)
    {
        super.createBlockStateDefinition(builder);
        
        builder.add(DIRECTION);
        builder.add(WATERLOGGED);
    }
    
    public Chair(Properties properties)
    {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(DIRECTION, Direction.NORTH).setValue(WATERLOGGED, false));
        
        this.generateShapes(this.getStateDefinition().getPossibleStates());
    }

    public Chair(float hardness, float blastResistance, SoundType sound, String name) {
		super(Block.Properties.of(Material.WOOD).harvestTool(ToolType.AXE)
				.strength(hardness, blastResistance).sound(sound));
		
		this.registerDefaultState(this.getStateDefinition().any().setValue(DIRECTION, Direction.NORTH));
		
		this.generateShapes(this.getStateDefinition().getPossibleStates());
		
		this.setRegistryName(name);
	}

    protected void generateShapes(ImmutableList<BlockState> states)
    {
        ImmutableMap.Builder<BlockState, VoxelShape> builder = new ImmutableMap.Builder<>();
        for(BlockState state : states)
        {
        	VoxelShape shapes = VoxelShapes.empty();
        
        	// chair body
        	shapes = VoxelShapes.joinUnoptimized(shapes, getShapes(rotate(Block.box(1, 7, 1, 15, 8, 14), Direction.SOUTH))[state.getValue(DIRECTION).get2DDataValue()], IBooleanFunction.OR); // chair base
        	shapes = VoxelShapes.joinUnoptimized(shapes, getShapes(rotate(Block.box(3, 9, 1, 13, 23, 2), Direction.SOUTH))[state.getValue(DIRECTION).get2DDataValue()], IBooleanFunction.OR); // chair back
        	
        	//legs
        	shapes = VoxelShapes.joinUnoptimized(shapes, getShapes(rotate(Block.box(2, 0, 12, 3, 8, 13), Direction.SOUTH))[state.getValue(DIRECTION).get2DDataValue()], IBooleanFunction.OR); //front left leg
            shapes = VoxelShapes.joinUnoptimized(shapes, getShapes(rotate(Block.box(13, 0, 12, 14, 8, 13), Direction.SOUTH))[state.getValue(DIRECTION).get2DDataValue()], IBooleanFunction.OR); // front right leg
            shapes = VoxelShapes.joinUnoptimized(shapes, getShapes(rotate(Block.box(1, 0, 1, 3, 22, 3), Direction.SOUTH))[state.getValue(DIRECTION).get2DDataValue()], IBooleanFunction.OR); // back left leg
            shapes = VoxelShapes.joinUnoptimized(shapes, getShapes(rotate(Block.box(13, 0, 1, 15, 22, 3), Direction.SOUTH))[state.getValue(DIRECTION).get2DDataValue()], IBooleanFunction.OR); // back right leg
            
            builder.put(state, shapes.optimize());
        }
        
        _shapes = builder.build();
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

    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult)
    {
        return Seat.create(world, pos, 0.3, player);
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        return this.defaultBlockState().setValue(WATERLOGGED, context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER).setValue(DIRECTION, context.getHorizontalDirection());
    }
    
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
}