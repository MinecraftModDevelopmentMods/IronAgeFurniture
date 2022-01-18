package com.mcmoddev.ironagefurniture.api.Blocks;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mcmoddev.ironagefurniture.api.Enumerations.BenchType;
import com.mcmoddev.ironagefurniture.api.Enumerations.Rotation;
import com.mcmoddev.ironagefurniture.api.properties.BenchTypeProperty;
import com.mcmoddev.ironagefurniture.api.util.Swivel;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;

public class BackBench extends Chair {
	public static final BenchTypeProperty TYPE = BenchTypeProperty.create("type", BenchType.SINGLE, BenchType.LEFT, BenchType.MIDDLE, BenchType.RIGHT);;
	
	public BackBench(float hardness, float blastResistance, SoundType sound, String name) {
		super(hardness, blastResistance, sound, name);
	}

	@Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        super.createBlockStateDefinition(builder);
        
        builder.add(TYPE);
    }
	
	 @Override
	protected void generateShapes(ImmutableList<BlockState> states)
   {
       ImmutableMap.Builder<BlockState, VoxelShape> builder = new ImmutableMap.Builder<>();
       for(BlockState state : states)
       {
       	BenchType type = state.getValue(TYPE);;
       	
       	VoxelShape shapes = Shapes.empty();
       
   		// bench base
       	shapes = Shapes.joinUnoptimized(shapes, getShapes(rotate(Block.box(0, 6, 1, 16, 7, 15), Direction.SOUTH))[state.getValue(DIRECTION).get2DDataValue()], BooleanOp.OR); // chair base
       	
       	//bench back
       	shapes = Shapes.joinUnoptimized(shapes, getShapes(rotate(Block.box(0, 0, 0, 16, 16, 1), Direction.SOUTH))[state.getValue(DIRECTION).get2DDataValue()], BooleanOp.OR); // chair back
    	
       	switch (type) {
			case SINGLE:
				//bench leg
	        	shapes = Shapes.joinUnoptimized(shapes, getShapes(rotate(Block.box(0, 0, 1, 1, 10, 12), Direction.SOUTH))[state.getValue(DIRECTION).get2DDataValue()], BooleanOp.OR); 
	        	shapes = Shapes.joinUnoptimized(shapes, getShapes(rotate(Block.box(15, 0, 1, 16, 10, 12), Direction.SOUTH))[state.getValue(DIRECTION).get2DDataValue()], BooleanOp.OR);
	        		
				break;

			case LEFT:
				shapes = Shapes.joinUnoptimized(shapes, getShapes(rotate(Block.box(0, 0, 1, 1, 10, 12), Direction.SOUTH))[state.getValue(DIRECTION).get2DDataValue()], BooleanOp.OR); 
				break;
				
			case RIGHT:
				shapes = Shapes.joinUnoptimized(shapes, getShapes(rotate(Block.box(15, 0, 1, 16, 10, 12), Direction.SOUTH))[state.getValue(DIRECTION).get2DDataValue()], BooleanOp.OR);
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
	
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
    	Level world = context.getLevel();
    	BlockPos pos = context.getClickedPos();
    	
		BlockState stateForPlacement = this.defaultBlockState()
				.setValue(WATERLOGGED, world.getFluidState(context.getClickedPos()).getType() == Fluids.WATER)
				.setValue(DIRECTION, context.getHorizontalDirection())
				.setValue(TYPE, BenchType.SINGLE);
    	
		if (!context.getPlayer().isShiftKeyDown()) {
			Direction benchAxis = getBenchToJoinTo(context.getHorizontalDirection(), world, pos);
			
			if (benchAxis != null) {
				BlockState blockStateToJoinTo = world.getBlockState(pos.relative(benchAxis));
				
				if (blockStateToJoinTo.getBlock().getRegistryName().equals(stateForPlacement.getBlock().getRegistryName())) {
					Direction benchFacing = getBenchToJoinToFacing(benchAxis, world, pos);
					
					boolean defaultFacing = true;
					
					if (getBenchType(blockStateToJoinTo) == BenchType.SINGLE) {

						if (benchFacing == Direction.NORTH && benchAxis == Direction.EAST) {
							benchFacing = Swivel.Rotate(benchAxis, Rotation.TwoSeventy);
							defaultFacing = false;
						}

						if (benchFacing == Direction.EAST && benchAxis == Direction.SOUTH) {
							benchFacing = Swivel.Rotate(benchAxis, Rotation.TwoSeventy);
							defaultFacing = false;
						}
						
						if (benchFacing == Direction.SOUTH && benchAxis == Direction.WEST) {
							benchFacing = Swivel.Rotate(benchAxis, Rotation.TwoSeventy);
							defaultFacing = false;
						}
						
						if (benchFacing == Direction.WEST && benchAxis == Direction.NORTH) {
							benchFacing = Swivel.Rotate(benchAxis, Rotation.TwoSeventy);
							defaultFacing = false;
						}
						
						if (defaultFacing)
							benchFacing = Swivel.Rotate(benchAxis, Rotation.Ninty);
						
					}
					
					stateForPlacement = traceBench2(benchAxis, world, pos, stateForPlacement, benchFacing);
				}
			}
		}				
		
		return stateForPlacement;
    }
    
    private boolean isIAFBench(BlockState blockstate) {
    	ResourceLocation resource = blockstate.getBlock().getRegistryName();
    	
    	if (resource.getNamespace().equals("ironagefurniture") && resource.getPath().contains("bench"))
    		return true;
    	
    	return false;
    }
    
	protected BenchType getBenchType(BlockState blockstate) {
		if (blockstate == null)
			return null;
		
		if (isIAFBench(blockstate))
			return blockstate.getValue(TYPE);
		
		return null;
	}
	
	private Direction getBenchDirection(BlockState blockstate) {
		if (isIAFBench(blockstate))
			return blockstate.getValue(DIRECTION);
		
		return null;
	}
	
	private int getOffset(Direction direction, LevelAccessor world, BlockPos pos) {
		BenchType currentlyInspectedBenchType;
		BlockState currentlyInspectedBenchState;
		String currentlyInspectedBlockName;
		
		int offset = 0;
		
		currentlyInspectedBenchState = world.getBlockState(pos.relative(direction));
		currentlyInspectedBenchType = getBenchType(currentlyInspectedBenchState);
		
		if (isBenchPiece(currentlyInspectedBenchType)) {
			Direction blockFacing = Swivel.Rotate(getBenchDirection(currentlyInspectedBenchState), Rotation.Ninty ) ;
			String blockName = currentlyInspectedBenchState.getBlock().getRegistryName().getNamespace();
			currentlyInspectedBlockName = blockName;
			
			while (isBenchPieceOnAxis(currentlyInspectedBenchType, direction, blockFacing, blockName, currentlyInspectedBlockName)) {
				offset++;
				
				currentlyInspectedBenchState = world.getBlockState(pos.relative(direction, offset + 1));
				currentlyInspectedBenchType = getBenchType(currentlyInspectedBenchState);
				currentlyInspectedBlockName = currentlyInspectedBenchState.getBlock().getRegistryName().getNamespace();
				
				if (isBenchPiece(currentlyInspectedBenchType))
					blockFacing = Swivel.Rotate(getBenchDirection(currentlyInspectedBenchState), Rotation.Ninty ) ;
			}
		}
		return offset;
	}
	
	private BlockState traceBench2(Direction direction, LevelAccessor world, BlockPos pos, BlockState blockState, Direction benchFacing) {
		boolean invertLeftRight = false;
		
		if (benchFacing == Direction.NORTH && direction == Direction.EAST)
			invertLeftRight = true;
		
		if (benchFacing == Direction.EAST && direction == Direction.SOUTH)
			invertLeftRight = true;
		
		if (benchFacing == Direction.SOUTH && direction == Direction.WEST)
			invertLeftRight = true;
		
		if (benchFacing == Direction.WEST && direction == Direction.NORTH)
			invertLeftRight = true;
		
		BenchType left = BenchType.LEFT;
		BenchType right = BenchType.RIGHT;
		
		if (invertLeftRight) {
			left = BenchType.RIGHT;
			right = BenchType.LEFT;
		}
		
		BlockState blockStateAtCurrentPos = blockState;
		
		BlockPos workingBlockPos = pos;
		
		int positiveOffset = getOffset(direction.getOpposite(), world, pos);
		int negativeOffset = getOffset(direction, world, pos);
		
		int workingPositiveOffset = positiveOffset;
		int workingNegativeOffset = negativeOffset;
		
		if (positiveOffset == 0 && negativeOffset == 0)
			return blockStateAtCurrentPos
					.setValue(DIRECTION, benchFacing)
					.setValue(TYPE, BenchType.SINGLE);
		
		while (workingNegativeOffset > 0) {
			workingBlockPos = pos.relative(direction, workingNegativeOffset);
			
			if (workingNegativeOffset == negativeOffset)
				world.setBlock(workingBlockPos, world.getBlockState(workingBlockPos)
						.setValue(DIRECTION, benchFacing)
						.setValue(TYPE, left), 0);
			else
				world.setBlock(workingBlockPos, world.getBlockState(workingBlockPos)
						.setValue(DIRECTION, benchFacing)
						.setValue(TYPE, BenchType.MIDDLE), 0);
			
			workingNegativeOffset--;
		}
		
		if (negativeOffset > 0 && positiveOffset == 0)
			return blockStateAtCurrentPos
					.setValue(DIRECTION, benchFacing)
					.setValue(TYPE, right);
		
		while (workingPositiveOffset > 0) {
			workingBlockPos = pos.relative(direction.getOpposite(), workingPositiveOffset);
			
			if (workingPositiveOffset == positiveOffset)
				world.setBlock(workingBlockPos, world.getBlockState(workingBlockPos)
						.setValue(DIRECTION, benchFacing)
						.setValue(TYPE, right), 0);
			else
				world.setBlock(workingBlockPos, world.getBlockState(workingBlockPos)
						.setValue(DIRECTION, benchFacing)
						.setValue(TYPE, BenchType.MIDDLE), 0);
			
			workingPositiveOffset--;
		}
		
		if (positiveOffset > 0 && negativeOffset == 0)
			return blockStateAtCurrentPos
					.setValue(DIRECTION, benchFacing)
					.setValue(TYPE, left);
			
		return blockStateAtCurrentPos
				.setValue(DIRECTION, benchFacing)
				.setValue(TYPE, BenchType.MIDDLE);	
	}
	
	private boolean isBenchPiece(BenchType benchType) {
		if (benchType == BenchType.SINGLE || 
				benchType == BenchType.MIDDLE ||
						benchType == BenchType.LEFT ||
							benchType == BenchType.RIGHT) 
			return true;
		else
			return false;	
	}
	
	private boolean isBenchPieceOnAxis(BenchType benchType, Direction benchAxis, Direction blockAxis, String blockName, String currentBlockName) {
		if (!blockName.equals(currentBlockName))
			return false;
		
		if (benchType == BenchType.SINGLE)
			return true;
		
		if(benchType == BenchType.MIDDLE || benchType == BenchType.LEFT || benchType == BenchType.RIGHT) {
			if (benchAxis == blockAxis || benchAxis == blockAxis.getOpposite())
				return true;
		}
			
		return false;	
	}
	
	private Rotation GetOpposite(Rotation rotation) {
		switch (rotation) {
			case Ninty:
				return Rotation.TwoSeventy;
				
			case OneEighty:
				return Rotation.Zero;
				
			case TwoSeventy:
				return Rotation.Ninty;
				
			case Zero:
				return Rotation.OneEighty;
				
			default:
				return Rotation.Zero;
		}
	}
		
	private boolean isBenchEnd(Direction facing, Level world, BlockPos pos) {		
		BenchType benchType = getBenchType(world.getBlockState(pos.relative(facing)));
		
		if (benchType == BenchType.LEFT || benchType == BenchType.RIGHT)
			return true;

		return false;
	}
	
	private boolean isBenchSingle(Direction facing, Level world, BlockPos pos) {		
		BenchType benchType = getBenchType(world.getBlockState(pos.relative(facing)));
		
		if (benchType == BenchType.SINGLE)
			return true;

		return false;
	}
	
	private Direction getBenchToJoinToFacing(Direction benchDirection, Level world, BlockPos pos) {
		return getBenchDirection(world.getBlockState(pos.relative(benchDirection)));
	}
	
	private Direction getBenchToJoinTo(Direction playerFacing, Level world, BlockPos pos) {
		// again, favour player facing
		if (isBenchEnd(playerFacing, world, pos) || isBenchSingle(playerFacing, world, pos)) {
			if (isBenchSingle(playerFacing, world, pos))
				return playerFacing;
			
			Direction targetFace = getBenchDirection(world.getBlockState(pos.relative(playerFacing)));
			
			if (targetFace == null)
				return null;
			
			targetFace = Swivel.Rotate(targetFace, Rotation.Ninty);
			
			if (isBenchEnd(playerFacing, world, pos) && (targetFace == playerFacing || targetFace == playerFacing.getOpposite()))
				return playerFacing;
		}
		
		for (Direction face : Direction.values()) {
			if (face != Direction.UP && face != Direction.DOWN) {
				if (isBenchSingle(face, world, pos))
					return face;
				
				if (isBenchEnd(face, world, pos)) {
					Direction targetFace = getBenchDirection(world.getBlockState(pos.relative(face)));
					
					if (targetFace == null)
						return null;
					
					targetFace = Swivel.Rotate(targetFace, Rotation.Ninty);
					
					if ((targetFace == face || targetFace == face.getOpposite()))
						return face;	
				}
			}
		}
		
		return null;
	}

	@Override
	public void destroy(LevelAccessor worldIn, BlockPos pos, BlockState state) {
		
		Direction benchFacing = getBenchDirection(state);
		Rotation defaultRotation = Rotation.Ninty;
		Direction benchAxis = Swivel.Rotate(getBenchDirection(state), defaultRotation);
		BenchType benchType = getBenchType(state);
		
		super.destroy(worldIn, pos, state);
		
		if ((benchAxis != null && benchType != null) && (benchType != BenchType.SINGLE)) {
			BlockState currentOffsetBlockState = worldIn.getBlockState(pos.relative(benchAxis));
			BenchType currentOffsetType = getBenchType(currentOffsetBlockState);
			
			if (currentOffsetType != null) {
				Direction currentOffsetFacing = Swivel.Rotate(getBenchDirection(currentOffsetBlockState), defaultRotation); 
				
				if (currentOffsetFacing == benchAxis || currentOffsetFacing == benchAxis.getOpposite()) {
					BlockState offsetBlockState = traceBench2(benchAxis, worldIn, pos.relative(benchAxis), worldIn.getBlockState(pos.relative(benchAxis)), benchFacing);
					
					if (offsetBlockState != null && isBenchPiece(getBenchType(offsetBlockState)))
						worldIn.setBlock(pos.relative(benchAxis), offsetBlockState, 0);
				}
			}
			
			BlockState reverseCurrentOffsetBlockState = worldIn.getBlockState(pos.relative(benchAxis.getOpposite()));
			BenchType reverseOffsetType = getBenchType(reverseCurrentOffsetBlockState);
			
			if (reverseOffsetType != null) {
				Direction reverseOffsetFacing = Swivel.Rotate(getBenchDirection(reverseCurrentOffsetBlockState), GetOpposite(defaultRotation)); 
			
				if (reverseOffsetFacing == benchAxis || reverseOffsetFacing == benchAxis.getOpposite()) {
					BlockState reverseOffsetBlockState = traceBench2(benchAxis.getOpposite(), worldIn, pos.relative(benchAxis.getOpposite()), worldIn.getBlockState(pos.relative(benchAxis.getOpposite())), benchFacing);
					
					if (reverseOffsetBlockState != null && isBenchPiece(getBenchType(reverseOffsetBlockState)))
						worldIn.setBlock(pos.relative(benchAxis.getOpposite()), reverseOffsetBlockState, 0);
				}
			}		
		}
	}
	
}