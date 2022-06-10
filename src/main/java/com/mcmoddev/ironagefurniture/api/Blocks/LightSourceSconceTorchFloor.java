package com.mcmoddev.ironagefurniture.api.Blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootContext.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.InteractionHand;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.plaf.basic.BasicTreeUI.TreeCancelEditingAction;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mcmoddev.ironagefurniture.BlockObjectHolder;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;

public class LightSourceSconceTorchFloor extends LightHolderSconceFloor implements LiquidBlockContainer {
	protected static final int AABB_STANDING_OFFSET = 2;
	protected static final VoxelShape AABB = Block.box(6.0D, 0.0D, 6.0D, 10.0D, 13.0D, 10.0D);
	protected ParticleOptions flameParticle;
	
	@Override
    public List<ItemStack> getDrops(BlockState state, Builder builder) {
    	List<ItemStack> drops;
    	drops = new ArrayList<ItemStack>();
    	
    	Item item = EmptyVariant().asItem();
    	ItemStack stack = new ItemStack(item, 1); 
    	
    	Item item2 = LightDrop().asItem();
    	ItemStack stack2 = new ItemStack(item2, 1); 
    	
    	drops.add(stack);
    	drops.add(stack2);
    	
    	return drops;
    }
	
	protected Block LightDrop() {
		return Blocks.TORCH;
	}
	
	public LightSourceSconceTorchFloor(Properties properties) {
		super(properties);
		
		this.registerDefaultState(this.getStateDefinition().any().setValue(DIRECTION, Direction.NORTH).setValue(WATERLOGGED, false));
        this.generateShapes(this.getStateDefinition().getPossibleStates());
        this.flameParticle = ParticleTypes.FLAME;
	}
	
	public LightSourceSconceTorchFloor(float hardness, float blastResistance, SoundType sound, String name) {
		super(Block.Properties.of(Material.METAL).strength(hardness, blastResistance).sound(sound).lightLevel((p_50886_) -> {
		    return 14; }) );

		this.registerDefaultState(this.getStateDefinition().any().setValue(DIRECTION, Direction.NORTH));
		this.generateShapes(this.getStateDefinition().getPossibleStates());
		this.setRegistryName(name);
		this.flameParticle = ParticleTypes.FLAME;
	}

	@Override
	protected void generateShapes(ImmutableList<BlockState> states) {
		 ImmutableMap.Builder<BlockState, VoxelShape> builder = new ImmutableMap.Builder<>();
	        for(BlockState state : states)
	        {
	        	//VoxelShape shapes = Shapes.empty();
	        
	        	// chair body                                                      X1 Y1 Z1 X2  Y2 Z2
//	        	shapes = Shapes.joinUnoptimized(shapes, getShapes(rotate(Block.box(1, 7, 1, 15, 8, 14), Direction.SOUTH))[state.getValue(DIRECTION).get2DDataValue()], BooleanOp.OR); // chair base
//	        	shapes = Shapes.joinUnoptimized(shapes, getShapes(rotate(Block.box(3, 9, 1, 13, 23, 2), Direction.SOUTH))[state.getValue(DIRECTION).get2DDataValue()], BooleanOp.OR); // chair back
//	        	
//	        	//legs
//	        	shapes = Shapes.joinUnoptimized(shapes, getShapes(rotate(Block.box(2, 0, 12, 3, 8, 13), Direction.SOUTH))[state.getValue(DIRECTION).get2DDataValue()], BooleanOp.OR); //front left leg
//	            shapes = Shapes.joinUnoptimized(shapes, getShapes(rotate(Block.box(13, 0, 12, 14, 8, 13), Direction.SOUTH))[state.getValue(DIRECTION).get2DDataValue()], BooleanOp.OR); // front right leg
//	            shapes = Shapes.joinUnoptimized(shapes, getShapes(rotate(Block.box(1, 0, 1, 3, 22, 3), Direction.SOUTH))[state.getValue(DIRECTION).get2DDataValue()], BooleanOp.OR); // back left leg
//	            shapes = Shapes.joinUnoptimized(shapes, getShapes(rotate(Block.box(13, 0, 1, 15, 22, 3), Direction.SOUTH))[state.getValue(DIRECTION).get2DDataValue()], BooleanOp.OR); // back right leg
	            builder.put(state, AABB);
	        }
	        
	        _shapes = builder.build();
	}
	

	@Override
	public boolean canConnectRedstone(BlockState state, BlockGetter world, BlockPos pos, Direction direction) {
		return true;
	}
	
	public void animateTick(BlockState state, Level level, BlockPos pos, Random random)
	{
		if (HasFlame()) {
		  double d0 = (double)pos.getX() + 0.5D;
	      double d1 = (double)pos.getY() + 0.9D;
	      double d2 = (double)pos.getZ() + 0.5D;
	      
	      level.addParticle(ParticleTypes.SMOKE, d0, d1, d2, 0.0D, 0.0D, 0.0D);
	      level.addParticle(this.flameParticle, d0, d1, d2, 0.0D, 0.0D, 0.0D);
		}
		
		if (CanEx() && !level.hasNeighborSignal(pos)) {
    	  if (state.getValue(BlockStateProperties.WATERLOGGED))
    		  Unlight(state, level, pos);
		}
    }

	private void Unlight(BlockState state, Level world, BlockPos pos) {
		if (CanEx()) {
		world.setBlock(pos, UnlitVariant().defaultBlockState()
				.setValue(DIRECTION, state.getValue(BlockStateProperties.HORIZONTAL_FACING))
				.setValue(WATERLOGGED, state.getValue(BlockStateProperties.WATERLOGGED)), UPDATE_ALL);
		}
	}
	
	@Override
	public boolean placeLiquid(LevelAccessor world, BlockPos pos, BlockState blockState, FluidState fluidState) {
		boolean success = super.placeLiquid(world, pos, blockState, fluidState);
		
		if (!blockState.getValue(BlockStateProperties.WATERLOGGED) && fluidState.getType() == Fluids.WATER) {
	         if (!world.isClientSide()) {
	        	 if (CanEx()) {
		            world.setBlock(pos, UnlitVariant().defaultBlockState()
		    				.setValue(DIRECTION, blockState.getValue(BlockStateProperties.HORIZONTAL_FACING))
		    				.setValue(WATERLOGGED, Boolean.valueOf(true)), UPDATE_ALL);
	        	 }
	            world.scheduleTick(pos, fluidState.getType(), fluidState.getType().getTickDelay(world));
	         }
	    }
		else
		{
			if (CanEx()) {
				world.setBlock(pos, UnlitVariant().defaultBlockState()
	    				.setValue(DIRECTION, blockState.getValue(BlockStateProperties.HORIZONTAL_FACING))
	    				.setValue(WATERLOGGED, blockState.getValue(BlockStateProperties.WATERLOGGED)), UPDATE_ALL);
			}
		}

		return success;
	}
	
	protected Block UnlitVariant() {
		return BlockObjectHolder.light_metal_ironage_sconce_floor_torch_iron_unlit;
	}
	
	protected Block EmptyVariant() {
		return BlockObjectHolder.light_metal_ironage_sconce_floor_empty_iron;
	}
	
	protected boolean CanEx() {
		return true;
	}
	
	protected boolean HasFlame() {
		return true;
	}
	
	@Override
	protected InteractionResult ActivateSconce(BlockState state, Level world, BlockPos pos, Player player,
			InteractionHand hand, BlockHitResult rayTraceResult) {
		
		ItemStack stackInHand = player.getItemInHand(hand);
		
		if (CanEx()) {
			if (stackInHand.is(Items.WATER_BUCKET)) {
			
				world.setBlock(pos, UnlitVariant().defaultBlockState()
						.setValue(DIRECTION, state.getValue(BlockStateProperties.HORIZONTAL_FACING))
						.setValue(WATERLOGGED, state.getValue(BlockStateProperties.WATERLOGGED)), UPDATE_ALL);
				
				return InteractionResult.SUCCESS;
			}
		}
		
		if (stackInHand.is(LightDrop().asItem()) || stackInHand.isEmpty()) {
			world.setBlock(pos, EmptyVariant().defaultBlockState()
					.setValue(DIRECTION, state.getValue(BlockStateProperties.HORIZONTAL_FACING))
					.setValue(WATERLOGGED, state.getValue(BlockStateProperties.WATERLOGGED)), UPDATE_ALL);
			
			if (!player.isCreative()) {
				if (stackInHand.is(LightDrop().asItem()))
					stackInHand.setCount(stackInHand.getCount()+1);
				else 
				{
					Item itemTorch = LightDrop().asItem();
			    	ItemStack stackTorch = new ItemStack(itemTorch, 1); 
			    	
					player.setItemInHand(hand, stackTorch);
				}
				
			}
			
			return InteractionResult.CONSUME_PARTIAL;
		}
		
		return InteractionResult.FAIL;
	}
}
