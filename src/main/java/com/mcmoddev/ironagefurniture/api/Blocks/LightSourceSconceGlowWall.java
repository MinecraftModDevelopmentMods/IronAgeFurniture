package com.mcmoddev.ironagefurniture.api.Blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.storage.loot.LootContext.Builder;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mcmoddev.ironagefurniture.BlockObjectHolder;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;

public class LightSourceSconceGlowWall extends LightSourceSconceTorchWall {
	public LightSourceSconceGlowWall(Properties properties) {
		super(properties);
		
		this.registerDefaultState(this.getStateDefinition().any().setValue(DIRECTION, Direction.NORTH).setValue(WATERLOGGED, false));
        this.generateShapes(this.getStateDefinition().getPossibleStates());
	}
	
	@Override
	protected boolean HasFlame() {
		return false;
	}
	
	@Override
	protected boolean CanEx() {
		return false;
	}
	
	@Override
	protected Block LightDrop() {
		return BlockObjectHolder.light_metal_ironage_block_floor_glow_clear;
	}
	
	@Override
	protected void generateShapes(ImmutableList<BlockState> states) {
		 ImmutableMap.Builder<BlockState, VoxelShape> builder = new ImmutableMap.Builder<>();
	        for(BlockState state : states)
	        {
	        	VoxelShape shapes = Shapes.empty();
	        
	        	// sconce torch                                                    X1 Y1 Z1 X2  Y2 Z2
	        	shapes = Shapes.joinUnoptimized(shapes, getShapes(rotate(Block.box(5, 10, 8, 11, 11, 16), Direction.SOUTH))[state.getValue(DIRECTION).get2DDataValue()], BooleanOp.OR); // sconce holder
	        	shapes = Shapes.joinUnoptimized(shapes, getShapes(rotate(Block.box(6, 1, 9, 10, 13, 13), Direction.SOUTH))[state.getValue(DIRECTION).get2DDataValue()], BooleanOp.OR); // torch
	        	
//	        	//legs
//	        	shapes = Shapes.joinUnoptimized(shapes, getShapes(rotate(Block.box(2, 0, 12, 3, 8, 13), Direction.SOUTH))[state.getValue(DIRECTION).get2DDataValue()], BooleanOp.OR); //front left leg
//	            shapes = Shapes.joinUnoptimized(shapes, getShapes(rotate(Block.box(13, 0, 12, 14, 8, 13), Direction.SOUTH))[state.getValue(DIRECTION).get2DDataValue()], BooleanOp.OR); // front right leg
//	            shapes = Shapes.joinUnoptimized(shapes, getShapes(rotate(Block.box(1, 0, 1, 3, 22, 3), Direction.SOUTH))[state.getValue(DIRECTION).get2DDataValue()], BooleanOp.OR); // back left leg
//	            shapes = Shapes.joinUnoptimized(shapes, getShapes(rotate(Block.box(13, 0, 1, 15, 22, 3), Direction.SOUTH))[state.getValue(DIRECTION).get2DDataValue()], BooleanOp.OR); // back right leg
	            
	            builder.put(state, shapes.optimize());
	        }
	        
	        _shapes = builder.build();
	}
	
	public LightSourceSconceGlowWall(float hardness, float blastResistance, SoundType sound, String name) {
		super(Block.Properties.of(Material.METAL).strength(hardness, blastResistance).sound(sound).lightLevel((p_50886_) -> {
		    return 14; }) );

		this.registerDefaultState(this.getStateDefinition().any().setValue(DIRECTION, Direction.NORTH));
		this.generateShapes(this.getStateDefinition().getPossibleStates());
		this.setRegistryName(name);
	}
}