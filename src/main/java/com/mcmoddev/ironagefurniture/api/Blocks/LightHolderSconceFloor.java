package com.mcmoddev.ironagefurniture.api.Blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LightBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mcmoddev.ironagefurniture.BlockObjectHolder;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;

public class LightHolderSconceFloor extends LightHolderSconce {
	public LightHolderSconceFloor(Properties properties) {
		super(properties);
		
		this.registerDefaultState(this.getStateDefinition().any().setValue(DIRECTION, Direction.NORTH).setValue(WATERLOGGED, false));
        this.generateShapes(this.getStateDefinition().getPossibleStates());
		// TODO Auto-generated constructor stub
	}
	
	public LightHolderSconceFloor(float hardness, float blastResistance, SoundType sound, String name) {
		super(Block.Properties.of(Material.METAL).strength(hardness, blastResistance).sound(sound));

		this.registerDefaultState(this.getStateDefinition().any().setValue(DIRECTION, Direction.NORTH));
		this.generateShapes(this.getStateDefinition().getPossibleStates());
		this.setRegistryName(name);
	}

	public BlockState updateShape(BlockState state, Direction direction, BlockState state2, LevelAccessor levelAccessor, BlockPos pos, BlockPos pos2)
	{
		return direction == Direction.DOWN && !this.canSurvive(state, levelAccessor, pos) ? 
				Blocks.AIR.defaultBlockState() : 
				super.updateShape(state, direction, state2, levelAccessor, pos, pos2);
	}
	
	public boolean canSurvive(BlockState state, LevelReader levelReader, BlockPos pos)
	{
		return canSupportCenter(levelReader, pos.below(), Direction.UP);
	}
	
	@Override
	protected void generateShapes(ImmutableList<BlockState> states) {
		 ImmutableMap.Builder<BlockState, VoxelShape> builder = new ImmutableMap.Builder<>();
	        for(BlockState state : states)
	        {
	        	VoxelShape shapes = Shapes.empty();
	        
	        	// sconce                                                          X1 Y1 Z1 X2  Y2 Z2
	        	shapes = Shapes.joinUnoptimized(shapes, getShapes(rotate(Block.box(5, 10, 5, 11, 11, 11), Direction.SOUTH))[state.getValue(DIRECTION).get2DDataValue()], BooleanOp.OR); // sconce holder
	        	shapes = Shapes.joinUnoptimized(shapes, getShapes(rotate(Block.box(7, 0, 3, 9, 11, 5), Direction.SOUTH))[state.getValue(DIRECTION).get2DDataValue()], BooleanOp.OR); // sconce stand

	            builder.put(state, shapes.optimize());
	        }
	        
	        _shapes = builder.build();
	}

	protected Block GetWallVariant() {
		return BlockObjectHolder.light_metal_ironage_sconce_wall_empty_iron;
	}
	
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		
	      LevelReader levelreader = context.getLevel();
	      BlockPos blockpos = context.getClickedPos();
	      
	      if (canSupportCenter(levelreader, blockpos.below(), Direction.UP))
	    	  return super.getStateForPlacement(context);	      
	    
	      BlockState target = levelreader.getBlockState(blockpos);
	      
	      boolean waterlogged = false;
	      
	      if (target.getBlock() == Blocks.WATER) {
	    	  waterlogged = true;
	      }
	      
	      BlockState blockstate = GetWallVariant().defaultBlockState();
	      Direction[] adirection = context.getNearestLookingDirections();

	      for(Direction direction : adirection) {
	         if (direction.getAxis().isHorizontal()) {
	            Direction direction1 = direction.getOpposite();
	            blockstate = blockstate.setValue(DIRECTION, direction1).setValue(WATERLOGGED, Boolean.valueOf(waterlogged));
	            if (blockstate.canSurvive(levelreader, blockpos)) {
	               return blockstate;
	            }
	         }
	      }

	      return null;
	}
	
	@Override
	public boolean canPlaceLiquid(BlockGetter p_56301_, BlockPos p_56302_, BlockState p_56303_, Fluid p_56304_) {
		return true;
	}
	
	protected Block GetTorchVariant() {
		return BlockObjectHolder.light_metal_ironage_sconce_floor_torch_iron;
	}
	
	protected Block GetUnlitTorchVariant() {
		return BlockObjectHolder.light_metal_ironage_sconce_floor_torch_iron_unlit;
	}
	
	@Override
	protected InteractionResult ActivateSconce(BlockState state, Level world, BlockPos pos, Player player,
			InteractionHand hand, BlockHitResult rayTraceResult) {
		
		ItemStack stackInHand = player.getItemInHand(hand);
		
		if (stackInHand.is(Blocks.TORCH.asItem())) {
			Block torchSconce = GetTorchVariant();
			
			if (state.getValue(BlockStateProperties.WATERLOGGED) == true)
				torchSconce = GetUnlitTorchVariant();
			else
				torchSconce = GetTorchVariant();
			
			world.setBlock(pos, torchSconce.defaultBlockState()
					.setValue(DIRECTION, state.getValue(BlockStateProperties.HORIZONTAL_FACING))
					.setValue(WATERLOGGED, state.getValue(BlockStateProperties.WATERLOGGED)), UPDATE_ALL);
			
			if (!player.isCreative())
				stackInHand.setCount(stackInHand.getCount()-1);;
			
				return InteractionResult.CONSUME_PARTIAL;
		}
		
		return InteractionResult.FAIL;
	}
	
	@Override
    public List<ItemStack> getDrops(BlockState state, Builder builder) {
    	List<ItemStack> drops;
    	
    	Item item = state.getBlock().asItem();
    	ItemStack stack = new ItemStack(item, 1); 
    	drops = new ArrayList<ItemStack>();
    	drops.add(stack);
    	
    	return drops;
    }
}