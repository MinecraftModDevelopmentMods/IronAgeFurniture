package com.mcmoddev.ironagefurniture.api.Blocks;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mcmoddev.ironagefurniture.api.entity.Seat;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.World;

public class Stool extends Chair {

	public Stool(float hardness, float blastResistance, SoundType sound, String name) {
		super(hardness, blastResistance, sound, name);
	}

	 @Override
	    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult)
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
        
    		// short stool body
        	shapes = VoxelShapes.joinUnoptimized(shapes, getShapes(rotate(Block.box(3, 6, 3, 13, 7, 13), Direction.SOUTH))[state.getValue(DIRECTION).get2DDataValue()], IBooleanFunction.OR); // chair base
        	
        	//short stool legs
        	shapes = VoxelShapes.joinUnoptimized(shapes, getShapes(rotate(Block.box(7, 0, 11, 9, 7, 12), Direction.SOUTH))[state.getValue(DIRECTION).get2DDataValue()], IBooleanFunction.OR); 
            shapes = VoxelShapes.joinUnoptimized(shapes, getShapes(rotate(Block.box(10, 0, 5, 13, 7, 8), Direction.SOUTH))[state.getValue(DIRECTION).get2DDataValue()], IBooleanFunction.OR); 
            shapes = VoxelShapes.joinUnoptimized(shapes, getShapes(rotate(Block.box(4, 0, 5,6, 7, 8), Direction.SOUTH))[state.getValue(DIRECTION).get2DDataValue()], IBooleanFunction.OR); 	
        	
            builder.put(state, shapes.optimize());
        }
        
        _shapes = builder.build();
    }
}
