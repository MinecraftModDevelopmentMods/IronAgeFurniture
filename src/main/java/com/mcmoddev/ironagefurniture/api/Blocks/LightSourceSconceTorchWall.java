package com.mcmoddev.ironagefurniture.api.Blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.storage.loot.LootContext.Builder;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mcmoddev.ironagefurniture.BlockObjectHolder;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;

public class LightSourceSconceTorchWall extends LightSourceSconceTorchFloor {
	public LightSourceSconceTorchWall(Properties properties) {
		super(properties);
		
		this.registerDefaultState(this.getStateDefinition().any().setValue(DIRECTION, Direction.NORTH).setValue(WATERLOGGED, false));
        this.generateShapes(this.getStateDefinition().getPossibleStates());
        this.flameParticle = ParticleTypes.FLAME;
	}
	
	@Override
	protected Block UnlitVariant() {
		return BlockObjectHolder.light_metal_ironage_sconce_wall_torch_iron_unlit;
	}
	
	@Override
	protected Block EmptyVariant() {
		return BlockObjectHolder.light_metal_ironage_sconce_wall_empty_iron;
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
	
	public LightSourceSconceTorchWall(float hardness, float blastResistance, SoundType sound, String name) {
		super(Block.Properties.of(Material.METAL).strength(hardness, blastResistance).sound(sound).lightLevel((p_50886_) -> {
		    return 14; }) );

		this.registerDefaultState(this.getStateDefinition().any().setValue(DIRECTION, Direction.NORTH));
		this.generateShapes(this.getStateDefinition().getPossibleStates());
		this.setRegistryName(name);
		this.flameParticle = ParticleTypes.FLAME;
	}
	
	@Override
	public BlockState updateShape(BlockState state, Direction direction, BlockState state2, LevelAccessor level, BlockPos pos1, BlockPos pos2) {
		return direction.getOpposite() == state.getValue(DIRECTION) && !state.canSurvive(level, pos1) ? Blocks.AIR.defaultBlockState() : state;
	}
	
	@Override
	public boolean canSurvive(BlockState state, LevelReader levelReader, BlockPos pos)
	{
	      Direction direction = state.getValue(DIRECTION);
	      BlockPos blockpos = pos.relative(direction.getOpposite());
	      BlockState blockstate = levelReader.getBlockState(blockpos);
	      return blockstate.isFaceSturdy(levelReader, blockpos, direction);
	}
	
	@Override
    public List<ItemStack> getDrops(BlockState state, Builder builder) {
    	List<ItemStack> drops;
    	drops = new ArrayList<ItemStack>();
    	
    	Item item = BlockObjectHolder.light_metal_ironage_sconce_floor_empty_iron.asItem();
    	ItemStack stack = new ItemStack(item, 1); 
    	
    	Item item2 = Blocks.TORCH.asItem();
    	ItemStack stack2 = new ItemStack(item2, 1); 
    	
    	drops.add(stack);
    	drops.add(stack2);
    	
    	return drops;
    }
}