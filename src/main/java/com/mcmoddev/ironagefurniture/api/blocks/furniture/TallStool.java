package com.mcmoddev.ironagefurniture.api.blocks.furniture;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
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

public class TallStool extends Chair {

	public TallStool(float hardness, float blastResistance, SoundType sound, String name) {
		super(hardness, blastResistance, sound, name);
	}

	 @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult rayTraceResult)
    {
        return Seat.create(world, pos, 0.6, player);
    }

	@Override
	protected void generateShapes(ImmutableList<BlockState> states)
    {
        ImmutableMap.Builder<BlockState, VoxelShape> builder = new ImmutableMap.Builder<>();
        for(BlockState state : states)
        {
        	VoxelShape shapes = Shapes.empty();

    		// tall stool body
        	shapes = Shapes.joinUnoptimized(shapes, getShapes(rotate(Block.box(3, 12, 3, 13, 13, 13), Direction.SOUTH))[state.getValue(DIRECTION).get2DDataValue()], BooleanOp.OR); // chair base

        	//tall stool legs
        	shapes = Shapes.joinUnoptimized(shapes, getShapes(rotate(Block.box(7, 0, 11, 9, 12, 12), Direction.SOUTH))[state.getValue(DIRECTION).get2DDataValue()], BooleanOp.OR);
            shapes = Shapes.joinUnoptimized(shapes, getShapes(rotate(Block.box(10, 0, 5, 13, 12, 8), Direction.SOUTH))[state.getValue(DIRECTION).get2DDataValue()], BooleanOp.OR);
            shapes = Shapes.joinUnoptimized(shapes, getShapes(rotate(Block.box(4, 0, 5,6, 12, 8), Direction.SOUTH))[state.getValue(DIRECTION).get2DDataValue()], BooleanOp.OR);

            builder.put(state, shapes.optimize());
        }

        _shapes = builder.build();
    }
}
