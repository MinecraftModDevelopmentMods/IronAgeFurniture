package com.mcmoddev.ironagefurniture.api.Blocks;

import java.util.List;

import com.mcmoddev.ironagefurniture.api.Enumerations.BenchType;
import com.mcmoddev.ironagefurniture.api.Enumerations.Rotation;
import com.mcmoddev.ironagefurniture.api.entity.Seat;
import com.mcmoddev.ironagefurniture.api.util.Swivel;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BackBench extends Chair {
	private static final AxisAlignedBB BBSHORT = new AxisAlignedBB(0.2, 0.0, 0.2, 0.8, 0.45, 0.8);
	public static final PropertyEnum<BenchType> TYPE = PropertyEnum.<BenchType>create("type", BenchType.class);
	
	public BackBench(Material materialIn, String name, float resistance, boolean tall, double yOffset, float hardness) {
		super(materialIn, name, resistance, yOffset, hardness);
	
		// TODO Auto-generated constructor stub
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY,
			float hitZ, int meta, EntityLivingBase placer, ItemStack stack) {
		IBlockState stateForPlacement =  super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, stack)
				.withProperty(FACING, placer.getHorizontalFacing())
				.withProperty(TYPE, BenchType.SINGLE);
		
		if (!placer.isSneaking()) {
			EnumFacing benchAxis = getBenchToJoinTo(placer.getHorizontalFacing(), world, pos);
			
			if (benchAxis != null)
				stateForPlacement = traceBench(benchAxis, world, pos, stateForPlacement);
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
	
	private IBlockState traceBench(EnumFacing direction, World world, BlockPos pos, IBlockState blockState) {
		IBlockState thisPieceBlockState = computeBenchBlockState(0, direction, world, pos, true, blockState, BenchType.SINGLE);
		
		BenchType placedBenchType = getBenchType(thisPieceBlockState);
		
		IBlockState offsetBenchState = world.getBlockState(pos.offset(direction));
		BenchType offsetBenchType = getBenchType(offsetBenchState);
		
		IBlockState reverseOffsetBenchState = world.getBlockState(pos.offset(direction.getOpposite()));
		BenchType reverseOffsetBenchType = getBenchType(reverseOffsetBenchState);
		
		
		if (offsetBenchType != null && isBenchPiece(offsetBenchType)) {
			EnumFacing offsetBenchFacing = Swivel.Rotate(getBenchDirection(offsetBenchState), Rotation.Ninty);
			
			if ((offsetBenchType == BenchType.SINGLE) || (offsetBenchType == BenchType.LEFT && (offsetBenchFacing == direction || offsetBenchFacing == direction.getOpposite())))
				traceBenchLine(direction, world, pos, thisPieceBlockState, placedBenchType);
		}
		
		if (reverseOffsetBenchType != null && isBenchPiece(reverseOffsetBenchType)) {
			EnumFacing reverseOffsetBenchFacing = Swivel.Rotate(getBenchDirection(reverseOffsetBenchState), Rotation.TwoSeventy);
			
			if ((reverseOffsetBenchType == BenchType.SINGLE) || (reverseOffsetBenchType == BenchType.LEFT && (reverseOffsetBenchFacing == direction  || reverseOffsetBenchFacing == direction.getOpposite())))
				traceBenchLine(direction.getOpposite(), world, pos, thisPieceBlockState, placedBenchType, Rotation.TwoSeventy);
		}
		
		return thisPieceBlockState;
	}
	
	private void traceBenchLine (EnumFacing direction, World world, BlockPos pos, IBlockState blockState, BenchType placedBenchType) {
		traceBenchLine(direction, world, pos, blockState, placedBenchType, Rotation.Ninty);
	}
	
	private void traceBenchLine (EnumFacing direction, World world, BlockPos pos, IBlockState blockState, BenchType placedBenchType, Rotation standardRotation) {
		int offset = 1;
		IBlockState nextPieceBlockState = computeBenchBlockState(offset, direction, world, pos, true, null, placedBenchType, standardRotation);
		BenchType nextPieceBenchType = getBenchType(nextPieceBlockState);
		
		if (isBenchPiece(nextPieceBenchType))
			world.setBlockState(pos.offset(direction, offset), nextPieceBlockState);
		
		while (isBenchPiece(nextPieceBenchType)) {
			offset++;
			nextPieceBlockState = computeBenchBlockState(offset, direction, world, pos, placedBenchType, standardRotation);
			
			if(isBenchPiece(getBenchType(nextPieceBlockState)))
				world.setBlockState(pos.offset(direction, offset), nextPieceBlockState);
			
			nextPieceBenchType = getBenchType(nextPieceBlockState);
		}
	}
	
	private boolean isBenchPiece(BenchType benchType) {
		if (benchType == BenchType.SINGLE || 
				benchType == BenchType.MIDDLE ||
						benchType == BenchType.LEFT) 
			return true;
		else
			return false;
		
	}
	
	private IBlockState computeBenchBlockState(int offset, EnumFacing direction, World world, 
			BlockPos pos, boolean bypassCurrentPosCheck, IBlockState blockstate, BenchType placedBenchType) {
		return computeBenchBlockState(offset, direction, world, pos, bypassCurrentPosCheck, blockstate, placedBenchType, Rotation.Ninty);
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
	
	private IBlockState computeBenchBlockState(int offset, EnumFacing direction, World world, 
			BlockPos pos, boolean bypassCurrentPosCheck, IBlockState blockstate, BenchType placedBenchType, Rotation standardRotation) {

		BlockPos offsetPos = pos.offset(direction, offset);
		
		IBlockState currentPosBlockState;
		
		if (blockstate != null)
			currentPosBlockState = blockstate;
		else
			currentPosBlockState = world.getBlockState(offsetPos);
		
		BenchType currentType = getBenchType(currentPosBlockState);
		
		IBlockState offsetBlockState = world.getBlockState(offsetPos.offset(direction));
		BenchType offsetType = getBenchType(offsetBlockState);
		
		IBlockState reverseOffsetBlockState;
		
		BenchType reverseOffsetType;
		
		if (offset == 1) {
			reverseOffsetBlockState = blockstate;
			reverseOffsetType = placedBenchType;
		} else {
			reverseOffsetBlockState = world.getBlockState(offsetPos.offset(direction.getOpposite()));
			reverseOffsetType = getBenchType(reverseOffsetBlockState);
		}
		
		if (!bypassCurrentPosCheck && !isBenchPiece(currentType)) 
			return currentPosBlockState;
		
		if (currentType == null && offsetType == null && reverseOffsetType == null)
			return null;
		
		if (currentType == null)
			return null;
		
		if ((currentPosBlockState != null && reverseOffsetBlockState != null) 
				&& ((currentType == BenchType.LEFT || currentType == BenchType.MIDDLE) && 
						(reverseOffsetType == BenchType.LEFT || reverseOffsetType == BenchType.MIDDLE))) {
			if ((getBenchDirection(currentPosBlockState) != getBenchDirection(reverseOffsetBlockState)) && 
					(getBenchDirection(currentPosBlockState) != getBenchDirection(reverseOffsetBlockState).getOpposite()))
				return currentPosBlockState;	
			
		}
		
		if (reverseOffsetType == null && offsetType == null)
			return currentPosBlockState
					.withProperty(FACING, Swivel.Rotate(direction, standardRotation))
					.withProperty(TYPE, BenchType.SINGLE);
		
		boolean benchInPath = false;
		
		if (offsetType == BenchType.LEFT || offsetType == BenchType.MIDDLE) {
			EnumFacing offsetFacing = Swivel.Rotate(getBenchDirection(offsetBlockState), standardRotation);
			
			if (offsetFacing != direction && offsetFacing != direction.getOpposite())
				benchInPath = true;
		}
		
		if (isBenchPiece(offsetType) && !isBenchPiece(reverseOffsetType)) 
			return currentPosBlockState
					.withProperty(FACING, Swivel.Rotate(direction.getOpposite(), standardRotation))
					.withProperty(TYPE, BenchType.LEFT);
		
		if ((!isBenchPiece(offsetType) || benchInPath) && isBenchPiece(reverseOffsetType))	
			return currentPosBlockState
					.withProperty(FACING, Swivel.Rotate(direction, GetOpposite(standardRotation)))
					.withProperty(TYPE, BenchType.LEFT);
		
		if (isBenchPiece(getBenchType(offsetBlockState)) && isBenchPiece(getBenchType(reverseOffsetBlockState))) {
			EnumFacing offsetFacing = getBenchDirection(offsetBlockState);
			EnumFacing reverseOffsetFacing = getBenchDirection(reverseOffsetBlockState);
			
			if (offsetFacing != reverseOffsetFacing && offsetFacing != reverseOffsetFacing.getOpposite())
				return currentPosBlockState
						.withProperty(FACING, Swivel.Rotate(direction.getOpposite(), standardRotation))
						.withProperty(TYPE, BenchType.LEFT);
		}
		
		if (isBenchPiece(offsetType) && isBenchPiece(reverseOffsetType))	
			return currentPosBlockState
					.withProperty(FACING, Swivel.Rotate(direction.getOpposite(), standardRotation))
					.withProperty(TYPE, BenchType.MIDDLE);
		
		return currentPosBlockState;
	}
	
	private IBlockState computeBenchBlockState(int offset, EnumFacing direction, World world, BlockPos pos, BenchType placedBenchType, Rotation standardRotation) {
		return computeBenchBlockState(offset, direction, world, pos, false, null, placedBenchType, standardRotation);
	}
	
//	private IBlockState computeBenchBlockState(int offset, EnumFacing direction, World world, BlockPos pos, BenchType placedBenchType) {
//		return computeBenchBlockState(offset, direction, world, pos, false, null, placedBenchType, Rotation.Ninty);
//	}
	
	private boolean isBenchEnd(EnumFacing facing, World world, BlockPos pos) {		
		BenchType benchType = getBenchType(world.getBlockState(pos.offset(facing)));
		
		if (benchType == BenchType.LEFT)
			return true;

		return false;
	}
	
	private boolean isBenchSingle(EnumFacing facing, World world, BlockPos pos) {		
		BenchType benchType = getBenchType(world.getBlockState(pos.offset(facing)));
		
		if (benchType == BenchType.SINGLE)
			return true;

		return false;
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
		
		if (meta > 7) {
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
		EnumFacing benchAxis = Swivel.Rotate(getBenchDirection(state), Rotation.Ninty);
		BenchType benchType = getBenchType(state);
		
		super.onBlockDestroyedByPlayer(worldIn, pos, state);
		
		if ((benchAxis != null && benchType != null) && (benchType != BenchType.SINGLE)) {
			IBlockState currentOffsetBlockState = worldIn.getBlockState(pos.offset(benchAxis));
			BenchType currentOffsetType = getBenchType(currentOffsetBlockState);
			
			if (currentOffsetType != null) {
				EnumFacing currentOffsetFacing = Swivel.Rotate(getBenchDirection(currentOffsetBlockState), Rotation.Ninty); 
				
				if (currentOffsetFacing == benchAxis || currentOffsetFacing == benchAxis.getOpposite()) {
					IBlockState offsetBlockState = traceBench(benchAxis, worldIn, pos.offset(benchAxis), worldIn.getBlockState(pos.offset(benchAxis)));
					
					if (offsetBlockState != null && isBenchPiece(getBenchType(offsetBlockState)))
						worldIn.setBlockState(pos.offset(benchAxis), offsetBlockState);
				}
			}
			
			IBlockState reverseCurrentOffsetBlockState = worldIn.getBlockState(pos.offset(benchAxis.getOpposite()));
			BenchType reverseOffsetType = getBenchType(reverseCurrentOffsetBlockState);
			
			if (reverseOffsetType != null) {
				EnumFacing reverseOffsetFacing = Swivel.Rotate(getBenchDirection(reverseCurrentOffsetBlockState), Rotation.Ninty); 
			
				if (reverseOffsetFacing == benchAxis || reverseOffsetFacing == benchAxis.getOpposite()) {
					IBlockState reverseOffsetBlockState = traceBench(benchAxis.getOpposite(), worldIn, pos.offset(benchAxis.getOpposite()), worldIn.getBlockState(pos.offset(benchAxis.getOpposite())));
					
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
		return BBSHORT;
	}
	
	@Override
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox,
			List<AxisAlignedBB> collidingBoxes, Entity entityIn) {
		
		if (!(entityIn instanceof Seat)) {
			super.addCollisionBoxToList(pos, entityBox, collidingBoxes, BBSHORT);
		}
	}
}