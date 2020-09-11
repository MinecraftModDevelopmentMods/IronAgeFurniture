package com.mcmoddev.ironagefurniture.api.Blocks;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mcmoddev.ironagefurniture.api.entity.Seat;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

public class Stool extends Chair {

	boolean tall = false;
	
	public Stool(float hardness, float blastResistance, SoundType sound, String name, boolean tall) {
		super(hardness, blastResistance, sound, name);
		
		this.tall = tall;
	}
	
	@Override
	protected ImmutableMap<BlockState, VoxelShape> generateShapes(ImmutableList<BlockState> states)
    {
        ImmutableMap.Builder<BlockState, VoxelShape> builder = new ImmutableMap.Builder<>();
        for(BlockState state : states)
        {
        	VoxelShape shapes = VoxelShapes.empty();
        
        	// chair body
        	shapes = VoxelShapes.combine(shapes, getShapes(rotate(Block.makeCuboidShape(0, 7, 0, 16, 8, 14), Direction.SOUTH))[state.get(DIRECTION).getHorizontalIndex()], IBooleanFunction.OR); // chair base
        	
        	//legs
        	shapes = VoxelShapes.combine(shapes, getShapes(rotate(Block.makeCuboidShape(1, 0, 12, 2, 8, 13), Direction.SOUTH))[state.get(DIRECTION).getHorizontalIndex()], IBooleanFunction.OR); //front left leg
            shapes = VoxelShapes.combine(shapes, getShapes(rotate(Block.makeCuboidShape(14, 0, 12, 15, 8, 13), Direction.SOUTH))[state.get(DIRECTION).getHorizontalIndex()], IBooleanFunction.OR); // front right leg
            shapes = VoxelShapes.combine(shapes, getShapes(rotate(Block.makeCuboidShape(0, 0, 0, 2, 22, 2), Direction.SOUTH))[state.get(DIRECTION).getHorizontalIndex()], IBooleanFunction.OR); // back left leg
            //shapes = VoxelShapes.combine(shapes, getShapes(rotate(Block.makeCuboidShape(14, 0, 0, 16, 22, 2), Direction.SOUTH))[state.get(DIRECTION).getHorizontalIndex()], IBooleanFunction.OR); // back right leg
            
            builder.put(state, shapes.simplify());
        }
        
        return builder.build();
    }
}
