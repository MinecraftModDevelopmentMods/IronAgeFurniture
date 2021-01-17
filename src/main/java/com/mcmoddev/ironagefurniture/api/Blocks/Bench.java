package com.mcmoddev.ironagefurniture.api.Blocks;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mcmoddev.ironagefurniture.api.entity.Seat;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.World;

public class Bench extends BackBench {

	public Bench(float hardness, float blastResistance, SoundType sound, String name) {
		super(hardness, blastResistance, sound, name);
	}
	
	 @Override
    public boolean onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult)
    {
        return Seat.create(world, pos, 0.2, player);
    }
	 
	 @Override
	protected void generateShapes(ImmutableList<BlockState> states)
    {
        ImmutableMap.Builder<BlockState, VoxelShape> builder = new ImmutableMap.Builder<>();
        for(BlockState state : states)
        {
        	VoxelShape shapes = VoxelShapes.empty();
        
    		// bench body
        	shapes = VoxelShapes.combine(shapes, getShapes(rotate(Block.makeCuboidShape(0, 6, 1, 16, 7, 15), Direction.SOUTH))[state.get(DIRECTION).getHorizontalIndex()], IBooleanFunction.OR); // chair base
        	
        	//bench leg
        	shapes = VoxelShapes.combine(shapes, getShapes(rotate(Block.makeCuboidShape(2, 0, 4, 3, 7, 12), Direction.SOUTH))[state.get(DIRECTION).getHorizontalIndex()], IBooleanFunction.OR); 
        	shapes = VoxelShapes.combine(shapes, getShapes(rotate(Block.makeCuboidShape(13, 0, 4, 14, 7, 12), Direction.SOUTH))[state.get(DIRECTION).getHorizontalIndex()], IBooleanFunction.OR);
        	
        	//bench cross bar
        	shapes = VoxelShapes.combine(shapes, getShapes(rotate(Block.makeCuboidShape(0, 2, 7, 16, 4, 9), Direction.SOUTH))[state.get(DIRECTION).getHorizontalIndex()], IBooleanFunction.OR); 
             	
        	
            builder.put(state, shapes.simplify());
        }
        
        _shapes = builder.build();
    }
//	private static final AxisAlignedBB BBSHORT = new AxisAlignedBB(0.2, 0.0, 0.2, 0.9, 0.45, 0.9);
}