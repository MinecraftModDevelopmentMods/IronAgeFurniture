package com.mcmoddev.ironagefurniture.api.Blocks;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mcmoddev.ironagefurniture.api.Enumerations.BenchType;
import com.mcmoddev.ironagefurniture.api.entity.Seat;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.Level;

public class Bench extends BackBench {

	public Bench(float hardness, float blastResistance, SoundType sound, String name) {
		super(hardness, blastResistance, sound, name);
	}
	
	@Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult rayTraceResult)
    {
        return Seat.create(world, pos, 0.2, player);
    }
	 
	 @Override
	protected void generateShapes(ImmutableList<BlockState> states)
    {
        ImmutableMap.Builder<BlockState, VoxelShape> builder = new ImmutableMap.Builder<>();
        for(BlockState state : states)
        {
        	BenchType type = state.getValue(TYPE);;
        	
        	VoxelShape shapes = Shapes.empty();
        
    		// bench body
        	shapes = Shapes.joinUnoptimized(shapes, getShapes(rotate(Block.box(0, 6, 1, 16, 7, 15), Direction.SOUTH))[state.getValue(DIRECTION).get2DDataValue()], BooleanOp.OR); // chair base
        	
        	switch (type) {
			case SINGLE:
				//bench leg
	        	shapes = Shapes.joinUnoptimized(shapes, getShapes(rotate(Block.box(2, 0, 4, 3, 7, 12), Direction.SOUTH))[state.getValue(DIRECTION).get2DDataValue()], BooleanOp.OR); 
	        	shapes = Shapes.joinUnoptimized(shapes, getShapes(rotate(Block.box(13, 0, 4, 14, 7, 12), Direction.SOUTH))[state.getValue(DIRECTION).get2DDataValue()], BooleanOp.OR);
	        		
				break;

			case LEFT:
				shapes = Shapes.joinUnoptimized(shapes, getShapes(rotate(Block.box(2, 0, 4, 3, 7, 12), Direction.SOUTH))[state.getValue(DIRECTION).get2DDataValue()], BooleanOp.OR);
				break;
				
			case RIGHT:
				shapes = Shapes.joinUnoptimized(shapes, getShapes(rotate(Block.box(13, 0, 4, 14, 7, 12), Direction.SOUTH))[state.getValue(DIRECTION).get2DDataValue()], BooleanOp.OR);
				break;
				
			case MIDDLE:
				break;
				
			default:
				break;
			}
        	
        	//bench cross bar
        	shapes = Shapes.joinUnoptimized(shapes, getShapes(rotate(Block.box(0, 2, 7, 16, 4, 9), Direction.SOUTH))[state.getValue(DIRECTION).get2DDataValue()], BooleanOp.OR); 
        	
            builder.put(state, shapes.optimize());
        }
        
        _shapes = builder.build();
    }
}