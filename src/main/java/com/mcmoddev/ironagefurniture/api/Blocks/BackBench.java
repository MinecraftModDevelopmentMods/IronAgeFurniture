package com.mcmoddev.ironagefurniture.api.Blocks;

import java.util.List;

import com.mcmoddev.ironagefurniture.api.Enumerations.BenchType;
import com.mcmoddev.ironagefurniture.api.Enumerations.Rotation;
import com.mcmoddev.ironagefurniture.api.entity.Seat;
import com.mcmoddev.ironagefurniture.api.properties.BenchTypeProperty;
import com.mcmoddev.ironagefurniture.api.util.Swivel;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

import net.minecraft.entity.Entity;

import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
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
	
	public static final BenchTypeProperty TYPE = BenchTypeProperty.create("type", BenchType.SINGLE);;
	public static final DirectionProperty DIRECTION = BlockStateProperties.HORIZONTAL_FACING;
	
    public BackBench(float hardness, float blastResistance, SoundType sound, String name) {
		super(Block.Properties.create(Material.WOOD).harvestTool(ToolType.AXE)
				.hardnessAndResistance(hardness, blastResistance).sound(sound));
		
		this.setDefaultState(this.getStateContainer().getBaseState().with(DIRECTION, Direction.NORTH).with(TYPE, BenchType.SINGLE));
		
		this.generateShapes(this.getStateContainer().getValidStates());
		
		this.setRegistryName(name);
	}
	
    @Override
    	public BlockState getStateForPlacement(BlockState state, Direction facing, BlockState state2, IWorld world,
    			BlockPos pos1, BlockPos pos2, Hand hand) {
    		// TODO Auto-generated method stub
    		return super.getStateForPlacement(state, facing, state2, world, pos1, pos2, hand);
    	
    		BlockState stateForPlacement =  super.getStateForPlacement(state, facing, state2, world, pos1, pos2, hand)
    				.withProperty(DIRECTION, facing.ho)
    				.withProperty(TYPE, BenchType.SINGLE);
    		
    		if (!placer.isSneaking()) {
    			EnumFacing benchAxis = getBenchToJoinTo(placer.getHorizontalFacing(), world, pos);
    			
    			if (benchAxis != null) {
    				IBlockState blockStateToJoinTo = world.getBlockState(pos.offset(benchAxis));
    				
    				if (blockStateToJoinTo.getBlock().getUnlocalizedName().equals(stateForPlacement.getBlock().getUnlocalizedName())) {
    					EnumFacing benchFacing = getBenchToJoinToFacing(benchAxis, world, pos);
    					
    					boolean defaultFacing = true;
    					
    					if (getBenchType(blockStateToJoinTo) == BenchType.SINGLE) {

    						if (benchFacing == EnumFacing.NORTH && benchAxis == EnumFacing.EAST) {
    							benchFacing = Swivel.Rotate(benchAxis, Rotation.TwoSeventy);
    							defaultFacing = false;
    						}

    						if (benchFacing == EnumFacing.EAST && benchAxis == EnumFacing.SOUTH) {
    							benchFacing = Swivel.Rotate(benchAxis, Rotation.TwoSeventy);
    							defaultFacing = false;
    						}
    						
    						if (benchFacing == EnumFacing.SOUTH && benchAxis == EnumFacing.WEST) {
    							benchFacing = Swivel.Rotate(benchAxis, Rotation.TwoSeventy);
    							defaultFacing = false;
    						}
    						
    						if (benchFacing == EnumFacing.WEST && benchAxis == EnumFacing.NORTH) {
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
    
	private BenchType getBenchType(IBlockState blockstate) {
		if (blockstate == null)
			return null;
		
		if (blockstate.getBlock().getUnlocalizedName().contains("bench"))
			return (BenchType)blockstate.getProperties().get(TYPE);
		
		return null;
	}
	
	private EnumFacing getBenchDirection(IBlockState blockstate) {
		if (blockstate.getBlock().getUnlocalizedName().contains("bench"))
			return (EnumFacing)blockstate.getProperties().get(FACING);
		
		return null;
	}
	
	private int getOffset(EnumFacing direction, World world, BlockPos pos) {
		BenchType currentlyInspectedBenchType;
		IBlockState currentlyInspectedBenchState;
		String currentlyInspectedBlockName;
		
		int offset = 0;
		
		currentlyInspectedBenchState = world.getBlockState(pos.offset(direction));
		currentlyInspectedBenchType = getBenchType(currentlyInspectedBenchState);
		
		if (isBenchPiece(currentlyInspectedBenchType)) {
			EnumFacing blockFacing = Swivel.Rotate(getBenchDirection(currentlyInspectedBenchState), Rotation.Ninty ) ;
			String blockName = currentlyInspectedBenchState.getBlock().getUnlocalizedName();
			currentlyInspectedBlockName = blockName;
			
			while (isBenchPieceOnAxis(currentlyInspectedBenchType, direction, blockFacing, blockName, currentlyInspectedBlockName)) {
				offset++;
				
				currentlyInspectedBenchState = world.getBlockState(pos.offset(direction, offset + 1));
				currentlyInspectedBenchType = getBenchType(currentlyInspectedBenchState);
				currentlyInspectedBlockName = currentlyInspectedBenchState.getBlock().getUnlocalizedName();
				
				if (isBenchPiece(currentlyInspectedBenchType))
					blockFacing = Swivel.Rotate(getBenchDirection(currentlyInspectedBenchState), Rotation.Ninty ) ;
			}
		}
		return offset;
	}
	
	private IBlockState traceBench2(EnumFacing direction, World world, BlockPos pos, IBlockState blockState, EnumFacing benchFacing) {
		boolean invertLeftRight = false;
		
		if (benchFacing == EnumFacing.NORTH && direction == EnumFacing.EAST)
			invertLeftRight = true;
		
		if (benchFacing == EnumFacing.EAST && direction == EnumFacing.SOUTH)
			invertLeftRight = true;
		
		if (benchFacing == EnumFacing.SOUTH && direction == EnumFacing.WEST)
			invertLeftRight = true;
		
		if (benchFacing == EnumFacing.WEST && direction == EnumFacing.NORTH)
			invertLeftRight = true;
		
		BenchType left = BenchType.LEFT;
		BenchType right = BenchType.RIGHT;
		
		if (invertLeftRight) {
			left = BenchType.RIGHT;
			right = BenchType.LEFT;
		}
		
		IBlockState blockStateAtCurrentPos = blockState;
		
		BlockPos workingBlockPos = pos;
		
		int positiveOffset = getOffset(direction.getOpposite(), world, pos);
		int negativeOffset = getOffset(direction, world, pos);
		
		int workingPositiveOffset = positiveOffset;
		int workingNegativeOffset = negativeOffset;
		
		if (positiveOffset == 0 && negativeOffset == 0)
			return blockStateAtCurrentPos
					.withProperty(FACING, benchFacing)
					.withProperty(TYPE, BenchType.SINGLE);
		
		while (workingNegativeOffset > 0) {
			workingBlockPos = pos.offset(direction, workingNegativeOffset);
			
			if (workingNegativeOffset == negativeOffset)
				world.setBlockState(workingBlockPos, world.getBlockState(workingBlockPos)
						.withProperty(FACING, benchFacing)
						.withProperty(TYPE, left));
			else
				world.setBlockState(workingBlockPos, world.getBlockState(workingBlockPos)
						.withProperty(FACING, benchFacing)
						.withProperty(TYPE, BenchType.MIDDLE));
			
			workingNegativeOffset--;
		}
		
		if (negativeOffset > 0 && positiveOffset == 0)
			return blockStateAtCurrentPos
					.withProperty(FACING, benchFacing)
					.withProperty(TYPE, right);
		
		while (workingPositiveOffset > 0) {
			workingBlockPos = pos.offset(direction.getOpposite(), workingPositiveOffset);
			
			if (workingPositiveOffset == positiveOffset)
				world.setBlockState(workingBlockPos, world.getBlockState(workingBlockPos)
						.withProperty(FACING, benchFacing)
						.withProperty(TYPE, right));
			else
				world.setBlockState(workingBlockPos, world.getBlockState(workingBlockPos)
						.withProperty(FACING, benchFacing)
						.withProperty(TYPE, BenchType.MIDDLE));
			
			workingPositiveOffset--;
		}
		
		if (positiveOffset > 0 && negativeOffset == 0)
			return blockStateAtCurrentPos
					.withProperty(FACING, benchFacing)
					.withProperty(TYPE, left);
			
		return blockStateAtCurrentPos
				.withProperty(FACING, benchFacing)
				.withProperty(TYPE, BenchType.MIDDLE);	
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
	
	private boolean isBenchPieceOnAxis(BenchType benchType, EnumFacing benchAxis, EnumFacing blockAxis, String blockName, String currentBlockName) {
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
		
	private boolean isBenchEnd(EnumFacing facing, World world, BlockPos pos) {		
		BenchType benchType = getBenchType(world.getBlockState(pos.offset(facing)));
		
		if (benchType == BenchType.LEFT || benchType == BenchType.RIGHT)
			return true;

		return false;
	}
	
	private boolean isBenchSingle(EnumFacing facing, World world, BlockPos pos) {		
		BenchType benchType = getBenchType(world.getBlockState(pos.offset(facing)));
		
		if (benchType == BenchType.SINGLE)
			return true;

		return false;
	}
	
	private EnumFacing getBenchToJoinToFacing(EnumFacing benchDirection, World world, BlockPos pos) {
		return getBenchDirection(world.getBlockState(pos.offset(benchDirection)));
	}
	
	private EnumFacing getBenchToJoinTo(EnumFacing playerFacing, World world, BlockPos pos) {
		// again, favour player facing
		if (isBenchEnd(playerFacing, world, pos) || isBenchSingle(playerFacing, world, pos)) {
			if (isBenchSingle(playerFacing, world, pos))
				return playerFacing;
			
			EnumFacing targetFace = getBenchDirection(world.getBlockState(pos.offset(playerFacing)));
			
			if (targetFace == null)
				return null;
			
			targetFace = Swivel.Rotate(targetFace, Rotation.Ninty);
			
			if (isBenchEnd(playerFacing, world, pos) && (targetFace == playerFacing || targetFace == playerFacing.getOpposite()))
				return playerFacing;
		}
		
		for (EnumFacing face : EnumFacing.HORIZONTALS) {
			if (isBenchSingle(face, world, pos))
				return face;
			
			if (isBenchEnd(face, world, pos)) {
				EnumFacing targetFace = getBenchDirection(world.getBlockState(pos.offset(face)));
				
				if (targetFace == null)
					return null;
				
				targetFace = Swivel.Rotate(targetFace, Rotation.Ninty);
				
				if ((targetFace == face || targetFace == face.getOpposite()))
					return face;	
			}
		}
		
		return null;
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) 
	{
		BenchType benchType = BenchType.SINGLE;
		
		if ( meta > 3 && meta < 8) {
			benchType = BenchType.MIDDLE;
			meta -= 4;
		}
		
		if (meta > 7 && meta < 12) {
			benchType = BenchType.LEFT;
			meta -= 8;
		}
		
		if (meta > 11) {
			benchType = BenchType.RIGHT;
			meta -= 12;
		}
		
		return this.getDefaultState()
				.withProperty(TYPE, benchType)
				.withProperty(FACING, EnumFacing.getHorizontal(meta));
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		int i = ((EnumFacing)state.getValue(FACING)).getHorizontalIndex();

        if (state.getValue(TYPE) == BenchType.MIDDLE)
            i += 4;
        
        if (state.getValue(TYPE) == BenchType.LEFT)
            i += 8;
        
        if (state.getValue(TYPE) == BenchType.RIGHT)
            i += 12;
        
        return i;
	}

	@Override
	public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) {
		
		EnumFacing benchFacing = getBenchDirection(state);
		Rotation defaultRotation = Rotation.Ninty;
		EnumFacing benchAxis = Swivel.Rotate(getBenchDirection(state), defaultRotation);
		BenchType benchType = getBenchType(state);
		
		super.onBlockDestroyedByPlayer(worldIn, pos, state);
		
		if ((benchAxis != null && benchType != null) && (benchType != BenchType.SINGLE)) {
			IBlockState currentOffsetBlockState = worldIn.getBlockState(pos.offset(benchAxis));
			BenchType currentOffsetType = getBenchType(currentOffsetBlockState);
			
			if (currentOffsetType != null) {
				EnumFacing currentOffsetFacing = Swivel.Rotate(getBenchDirection(currentOffsetBlockState), defaultRotation); 
				
				if (currentOffsetFacing == benchAxis || currentOffsetFacing == benchAxis.getOpposite()) {
					IBlockState offsetBlockState = traceBench2(benchAxis, worldIn, pos.offset(benchAxis), worldIn.getBlockState(pos.offset(benchAxis)), benchFacing);
					
					if (offsetBlockState != null && isBenchPiece(getBenchType(offsetBlockState)))
						worldIn.setBlockState(pos.offset(benchAxis), offsetBlockState);
				}
			}
			
			IBlockState reverseCurrentOffsetBlockState = worldIn.getBlockState(pos.offset(benchAxis.getOpposite()));
			BenchType reverseOffsetType = getBenchType(reverseCurrentOffsetBlockState);
			
			if (reverseOffsetType != null) {
				EnumFacing reverseOffsetFacing = Swivel.Rotate(getBenchDirection(reverseCurrentOffsetBlockState), GetOpposite(defaultRotation)); 
			
				if (reverseOffsetFacing == benchAxis || reverseOffsetFacing == benchAxis.getOpposite()) {
					IBlockState reverseOffsetBlockState = traceBench2(benchAxis.getOpposite(), worldIn, pos.offset(benchAxis.getOpposite()), worldIn.getBlockState(pos.offset(benchAxis.getOpposite())), benchFacing);
					
					if (reverseOffsetBlockState != null && isBenchPiece(getBenchType(reverseOffsetBlockState)))
						worldIn.setBlockState(pos.offset(benchAxis.getOpposite()), reverseOffsetBlockState);
				}
			}		
		}
	}
	
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, new IProperty[] { FACING, TYPE });
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) 
	{
		return BB;
	}
	
	@Override
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox,
			List<AxisAlignedBB> collidingBoxes, Entity entityIn) {
		
		if (!(entityIn instanceof Seat)) {
			switch(state.getValue(FACING)) {
			case NORTH:
				super.addCollisionBoxToList(pos, entityBox, collidingBoxes, BACKNORTH);
				break;
			case SOUTH:
				super.addCollisionBoxToList(pos, entityBox, collidingBoxes, BACKSOUTH);
				break;
			case WEST:
				super.addCollisionBoxToList(pos, entityBox, collidingBoxes, BACKWEST);
				break;
			default:
				super.addCollisionBoxToList(pos, entityBox, collidingBoxes, BACKEAST);
				break;
			}
			
			super.addCollisionBoxToList(pos, entityBox, collidingBoxes, BBSHORT);
		}
	}
}