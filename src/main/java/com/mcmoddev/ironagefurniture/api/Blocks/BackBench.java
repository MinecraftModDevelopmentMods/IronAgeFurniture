package com.mcmoddev.ironagefurniture.api.Blocks;

import com.mcmoddev.ironagefurniture.api.Enumerations.BenchType;
import com.mcmoddev.ironagefurniture.api.Enumerations.Rotation;
import com.mcmoddev.ironagefurniture.api.properties.BenchTypeProperty;
import com.mcmoddev.ironagefurniture.api.util.Swivel;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

public class BackBench extends Chair {
//	private static final AxisAlignedBB BB = new AxisAlignedBB(0.1, 0.0, 0.1, 0.9, 1.0, 0.9);
//	private static final AxisAlignedBB BBSHORT = new AxisAlignedBB(0.1, 0.0, 0.1, 0.9, 0.45, 0.9);
//	private static final AxisAlignedBB BACKEAST = new AxisAlignedBB(0.825, 0.6, 0.1, 0.9, 1.0, 0.9);
//	private static final AxisAlignedBB BACKNORTH = rotate(Rotation.Ninty, BACKEAST);
//	private static final AxisAlignedBB BACKSOUTH = RotateBB(Rotation.OneEighty, BACKEAST);
//	private static final AxisAlignedBB BACKWEST = RotateBB(Rotation.TwoSeventy, BACKEAST);
	
	public static final BenchTypeProperty TYPE = BenchTypeProperty.create("type", BenchType.SINGLE, BenchType.LEFT, BenchType.MIDDLE, BenchType.RIGHT);;
	
	public BackBench(float hardness, float blastResistance, SoundType sound, String name) {
		super(hardness, blastResistance, sound, name);
	}

	@Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        super.fillStateContainer(builder);
        
        builder.add(TYPE);
    }
	
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
    	World world = context.getWorld();
    	BlockPos pos = context.getPos();
    	
		BlockState stateForPlacement = this.getDefaultState()
				.with(WATERLOGGED, world.getFluidState(context.getPos()).getFluid() == Fluids.WATER)
				.with(DIRECTION, context.getPlacementHorizontalFacing())
				.with(TYPE, BenchType.SINGLE);
    	
		if (!context.isPlacerSneaking()) {
			Direction benchAxis = getBenchToJoinTo(context.getPlacementHorizontalFacing(), world, pos);
			
			if (benchAxis != null) {
				BlockState blockStateToJoinTo = world.getBlockState(pos.offset(benchAxis));
				
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
    
	private BenchType getBenchType(BlockState blockstate) {
		if (blockstate == null)
			return null;
		
		if (isIAFBench(blockstate))
			return blockstate.get(TYPE);
		
		return null;
	}
	
	private Direction getBenchDirection(BlockState blockstate) {
		if (isIAFBench(blockstate))
			return blockstate.get(DIRECTION);
		
		return null;
	}
	
	private int getOffset(Direction direction, IWorld world, BlockPos pos) {
		BenchType currentlyInspectedBenchType;
		BlockState currentlyInspectedBenchState;
		String currentlyInspectedBlockName;
		
		int offset = 0;
		
		currentlyInspectedBenchState = world.getBlockState(pos.offset(direction));
		currentlyInspectedBenchType = getBenchType(currentlyInspectedBenchState);
		
		if (isBenchPiece(currentlyInspectedBenchType)) {
			Direction blockFacing = Swivel.Rotate(getBenchDirection(currentlyInspectedBenchState), Rotation.Ninty ) ;
			String blockName = currentlyInspectedBenchState.getBlock().getRegistryName().getNamespace();
			currentlyInspectedBlockName = blockName;
			
			while (isBenchPieceOnAxis(currentlyInspectedBenchType, direction, blockFacing, blockName, currentlyInspectedBlockName)) {
				offset++;
				
				currentlyInspectedBenchState = world.getBlockState(pos.offset(direction, offset + 1));
				currentlyInspectedBenchType = getBenchType(currentlyInspectedBenchState);
				currentlyInspectedBlockName = currentlyInspectedBenchState.getBlock().getRegistryName().getNamespace();
				
				if (isBenchPiece(currentlyInspectedBenchType))
					blockFacing = Swivel.Rotate(getBenchDirection(currentlyInspectedBenchState), Rotation.Ninty ) ;
			}
		}
		return offset;
	}
	
	private BlockState traceBench2(Direction direction, IWorld world, BlockPos pos, BlockState blockState, Direction benchFacing) {
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
					.with(DIRECTION, benchFacing)
					.with(TYPE, BenchType.SINGLE);
		
		while (workingNegativeOffset > 0) {
			workingBlockPos = pos.offset(direction, workingNegativeOffset);
			
			if (workingNegativeOffset == negativeOffset)
				world.setBlockState(workingBlockPos, world.getBlockState(workingBlockPos)
						.with(DIRECTION, benchFacing)
						.with(TYPE, left), 0);
			else
				world.setBlockState(workingBlockPos, world.getBlockState(workingBlockPos)
						.with(DIRECTION, benchFacing)
						.with(TYPE, BenchType.MIDDLE), 0);
			
			workingNegativeOffset--;
		}
		
		if (negativeOffset > 0 && positiveOffset == 0)
			return blockStateAtCurrentPos
					.with(DIRECTION, benchFacing)
					.with(TYPE, right);
		
		while (workingPositiveOffset > 0) {
			workingBlockPos = pos.offset(direction.getOpposite(), workingPositiveOffset);
			
			if (workingPositiveOffset == positiveOffset)
				world.setBlockState(workingBlockPos, world.getBlockState(workingBlockPos)
						.with(DIRECTION, benchFacing)
						.with(TYPE, right), 0);
			else
				world.setBlockState(workingBlockPos, world.getBlockState(workingBlockPos)
						.with(DIRECTION, benchFacing)
						.with(TYPE, BenchType.MIDDLE), 0);
			
			workingPositiveOffset--;
		}
		
		if (positiveOffset > 0 && negativeOffset == 0)
			return blockStateAtCurrentPos
					.with(DIRECTION, benchFacing)
					.with(TYPE, left);
			
		return blockStateAtCurrentPos
				.with(DIRECTION, benchFacing)
				.with(TYPE, BenchType.MIDDLE);	
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
		
	private boolean isBenchEnd(Direction facing, World world, BlockPos pos) {		
		BenchType benchType = getBenchType(world.getBlockState(pos.offset(facing)));
		
		if (benchType == BenchType.LEFT || benchType == BenchType.RIGHT)
			return true;

		return false;
	}
	
	private boolean isBenchSingle(Direction facing, World world, BlockPos pos) {		
		BenchType benchType = getBenchType(world.getBlockState(pos.offset(facing)));
		
		if (benchType == BenchType.SINGLE)
			return true;

		return false;
	}
	
	private Direction getBenchToJoinToFacing(Direction benchDirection, World world, BlockPos pos) {
		return getBenchDirection(world.getBlockState(pos.offset(benchDirection)));
	}
	
	private Direction getBenchToJoinTo(Direction playerFacing, World world, BlockPos pos) {
		// again, favour player facing
		if (isBenchEnd(playerFacing, world, pos) || isBenchSingle(playerFacing, world, pos)) {
			if (isBenchSingle(playerFacing, world, pos))
				return playerFacing;
			
			Direction targetFace = getBenchDirection(world.getBlockState(pos.offset(playerFacing)));
			
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
					Direction targetFace = getBenchDirection(world.getBlockState(pos.offset(face)));
					
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
	
//	@Override
//	public BlockState getStateFromMeta(int meta) 
//	{
//		BenchType benchType = BenchType.SINGLE;
//		
//		if ( meta > 3 && meta < 8) {
//			benchType = BenchType.MIDDLE;
//			meta -= 4;
//		}
//		
//		if (meta > 7 && meta < 12) {
//			benchType = BenchType.LEFT;
//			meta -= 8;
//		}
//		
//		if (meta > 11) {
//			benchType = BenchType.RIGHT;
//			meta -= 12;
//		}
//		
//		return this.getDefaultState()
//				.with(TYPE, benchType)
//				.with(DIRECTION, Direction.getHorizontal(meta));
//	}

//	@Override
//	public int getMetaFromState(BlockState state)
//	{
//		int i = ((Direction)state.getValue(DIRECTION)).getHorizontalIndex();
//
//        if (state.getValue(TYPE) == BenchType.MIDDLE)
//            i += 4;
//        
//        if (state.getValue(TYPE) == BenchType.LEFT)
//            i += 8;
//        
//        if (state.getValue(TYPE) == BenchType.RIGHT)
//            i += 12;
//        
//        return i;
//	}

	@Override
	public void onPlayerDestroy(IWorld worldIn, BlockPos pos, BlockState state) {
		
		Direction benchFacing = getBenchDirection(state);
		Rotation defaultRotation = Rotation.Ninty;
		Direction benchAxis = Swivel.Rotate(getBenchDirection(state), defaultRotation);
		BenchType benchType = getBenchType(state);
		
		super.onPlayerDestroy(worldIn, pos, state);
		
		if ((benchAxis != null && benchType != null) && (benchType != BenchType.SINGLE)) {
			BlockState currentOffsetBlockState = worldIn.getBlockState(pos.offset(benchAxis));
			BenchType currentOffsetType = getBenchType(currentOffsetBlockState);
			
			if (currentOffsetType != null) {
				Direction currentOffsetFacing = Swivel.Rotate(getBenchDirection(currentOffsetBlockState), defaultRotation); 
				
				if (currentOffsetFacing == benchAxis || currentOffsetFacing == benchAxis.getOpposite()) {
					BlockState offsetBlockState = traceBench2(benchAxis, worldIn, pos.offset(benchAxis), worldIn.getBlockState(pos.offset(benchAxis)), benchFacing);
					
					if (offsetBlockState != null && isBenchPiece(getBenchType(offsetBlockState)))
						worldIn.setBlockState(pos.offset(benchAxis), offsetBlockState, 0);
				}
			}
			
			BlockState reverseCurrentOffsetBlockState = worldIn.getBlockState(pos.offset(benchAxis.getOpposite()));
			BenchType reverseOffsetType = getBenchType(reverseCurrentOffsetBlockState);
			
			if (reverseOffsetType != null) {
				Direction reverseOffsetFacing = Swivel.Rotate(getBenchDirection(reverseCurrentOffsetBlockState), GetOpposite(defaultRotation)); 
			
				if (reverseOffsetFacing == benchAxis || reverseOffsetFacing == benchAxis.getOpposite()) {
					BlockState reverseOffsetBlockState = traceBench2(benchAxis.getOpposite(), worldIn, pos.offset(benchAxis.getOpposite()), worldIn.getBlockState(pos.offset(benchAxis.getOpposite())), benchFacing);
					
					if (reverseOffsetBlockState != null && isBenchPiece(getBenchType(reverseOffsetBlockState)))
						worldIn.setBlockState(pos.offset(benchAxis.getOpposite()), reverseOffsetBlockState, 0);
				}
			}		
		}
	}
	
//	@Override
//	protected BlockStateContainer createBlockState()
//	{
//		return new BlockStateContainer(this, new IProperty[] { DIRECTION, TYPE });
//	}
//	
//	@Override
//	public AxisAlignedBB getBoundingBox(BlockState state, IBlockAccess source, BlockPos pos) 
//	{
//		return BB;
//	}
	
//	@Override
//	public void addCollisionBoxToList(BlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox,
//			List<AxisAlignedBB> collidingBoxes, Entity entityIn) {
//		
//		if (!(entityIn instanceof Seat)) {
//			switch(state.getValue(DIRECTION)) {
//			case NORTH:
//				super.addCollisionBoxToList(pos, entityBox, collidingBoxes, BACKNORTH);
//				break;
//			case SOUTH:
//				super.addCollisionBoxToList(pos, entityBox, collidingBoxes, BACKSOUTH);
//				break;
//			case WEST:
//				super.addCollisionBoxToList(pos, entityBox, collidingBoxes, BACKWEST);
//				break;
//			default:
//				super.addCollisionBoxToList(pos, entityBox, collidingBoxes, BACKEAST);
//				break;
//			}
//			
//			super.addCollisionBoxToList(pos, entityBox, collidingBoxes, BBSHORT);
//		}
//	}
}