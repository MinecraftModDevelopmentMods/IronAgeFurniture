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

public class Bench extends Chair {
	private static final AxisAlignedBB BBSHORT = new AxisAlignedBB(0.2, 0.0, 0.2, 0.8, 0.45, 0.8);
	public static final PropertyEnum<BenchType> TYPE = PropertyEnum.<BenchType>create("type", BenchType.class);
	
	public Bench(Material materialIn, String name, float resistance, boolean tall, double yOffset, float hardness) {
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
			EnumFacing playerFacing = placer.getHorizontalFacing();
			EnumFacing benchAxis;
			
			benchAxis = getBenchToJoinTo(playerFacing, world, pos);
			
			if (benchAxis != null) {
				stateForPlacement = traceBench(benchAxis, world, pos, stateForPlacement);
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
	
	private IBlockState traceBench(EnumFacing direction, World world, BlockPos pos, IBlockState blockState) {
		IBlockState thisPieceBlockState = computeBenchBlockState(0, direction, world, pos, true, blockState, BenchType.SINGLE);
		
		BenchType placedBenchType = getBenchType(thisPieceBlockState);
		BenchType offsetBenchType = getBenchType(world.getBlockState(pos.offset(direction)));
		BenchType reverseOffsetBenchType = getBenchType(world.getBlockState(pos.offset(direction.getOpposite())));
		
		
		if (offsetBenchType != null && isBenchPiece(offsetBenchType))
			traceBenchLine(direction, world, pos, thisPieceBlockState, placedBenchType);
		
		if (reverseOffsetBenchType != null && isBenchPiece(reverseOffsetBenchType))
			traceBenchLine(direction.getOpposite(), world, pos, thisPieceBlockState, placedBenchType);
		
		return thisPieceBlockState;
	}
	
	private void traceBenchLine (EnumFacing direction, World world, BlockPos pos, IBlockState blockState, BenchType placedBenchType) {
		int offset = 1;
		IBlockState nextPieceBlockState = computeBenchBlockState(offset, direction, world, pos, true, null, placedBenchType);
		BenchType nextPieceBenchType = getBenchType(nextPieceBlockState);
		
		if (isBenchPiece(nextPieceBenchType))
			world.setBlockState(pos.offset(direction, offset), nextPieceBlockState);
		
		while (isBenchPiece(nextPieceBenchType)) {
			offset++;
			nextPieceBlockState = computeBenchBlockState(offset, direction, world, pos, placedBenchType);
			
			if(isBenchPiece(getBenchType(nextPieceBlockState)))
				world.setBlockState(pos.offset(direction, offset), nextPieceBlockState);
			
			nextPieceBenchType = getBenchType(nextPieceBlockState);
		}
	}
	
	private boolean isBenchPiece(BenchType benchType) {
		if (benchType == BenchType.SINGLE || 
				benchType == BenchType.MIDDLE ||
						benchType == BenchType.END) 
			return true;
		else
			return false;
		
	}
	
	private IBlockState computeBenchBlockState(int offset, EnumFacing direction, World world, 
			BlockPos pos, boolean bypassCurrentPosCheck, IBlockState blockstate, BenchType placedBenchType) {

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
				&& ((currentType == BenchType.END || currentType == BenchType.MIDDLE) && 
						(reverseOffsetType == BenchType.END || reverseOffsetType == BenchType.MIDDLE))) {
			if ((getBenchDirection(currentPosBlockState) != getBenchDirection(reverseOffsetBlockState)) && 
					(getBenchDirection(currentPosBlockState) != getBenchDirection(reverseOffsetBlockState).getOpposite())) {
				return currentPosBlockState;	
			}
		}
		
		if (reverseOffsetType == null && offsetType == null)
			return currentPosBlockState
					.withProperty(FACING, Swivel.Rotate(direction, Rotation.Ninty))
					.withProperty(TYPE, BenchType.SINGLE);
		
		boolean benchInPath = false;
		
		if (offsetType == BenchType.END || offsetType == BenchType.MIDDLE) {
			EnumFacing offsetFacing = Swivel.Rotate(getBenchDirection(offsetBlockState), Rotation.Ninty);
			
			if (offsetFacing != direction && offsetFacing != direction.getOpposite())
				benchInPath = true;
		}
		
		if (isBenchPiece(offsetType) && (!isBenchPiece(reverseOffsetType)))
			return currentPosBlockState
					.withProperty(FACING, Swivel.Rotate(direction.getOpposite(), Rotation.Ninty))
					.withProperty(TYPE, BenchType.END);
		
		if ((!isBenchPiece(offsetType) || benchInPath) && isBenchPiece(reverseOffsetType))	
			return currentPosBlockState
					.withProperty(FACING, Swivel.Rotate(direction, Rotation.Ninty))
					.withProperty(TYPE, BenchType.END);
		
		if (isBenchPiece(offsetType) && isBenchPiece(reverseOffsetType))	
			return currentPosBlockState
					.withProperty(FACING, Swivel.Rotate(direction.getOpposite(), Rotation.Ninty))
					.withProperty(TYPE, BenchType.MIDDLE);
		
		return currentPosBlockState;
	}
	
	private IBlockState computeBenchBlockState(int offset, EnumFacing direction, World world, BlockPos pos, BenchType placedBenchType) {
		return computeBenchBlockState(offset, direction, world, pos, false, null, placedBenchType);
	}
	
	private boolean isBenchEnd(EnumFacing facing, World world, BlockPos pos) {		
		BenchType benchType = getBenchType(world.getBlockState(pos.offset(facing)));
		
		if (benchType == BenchType.END)
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
			benchType = BenchType.END;
			meta -= 8;
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
        
        if (state.getValue(TYPE) == BenchType.END)
            i += 8;
        
        return i;
	}

	@Override
	public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) {
		EnumFacing benchAxis = Swivel.Rotate(getBenchDirection(state), Rotation.Ninty);
		BenchType benchType = getBenchType(state);
		
		super.onBlockDestroyedByPlayer(worldIn, pos, state);
		
		if ((benchAxis != null && benchType != null) && (benchType != BenchType.SINGLE)) {
			IBlockState offsetBlockState = traceBench(benchAxis, worldIn, pos.offset(benchAxis), worldIn.getBlockState(pos.offset(benchAxis)));
			
			if (offsetBlockState != null && isBenchPiece(getBenchType(offsetBlockState)))
				worldIn.setBlockState(pos.offset(benchAxis), offsetBlockState);
			
			IBlockState reverseOffsetBlockState = traceBench(benchAxis.getOpposite(), worldIn, pos.offset(benchAxis.getOpposite()), worldIn.getBlockState(pos.offset(benchAxis.getOpposite())));
			
			if (reverseOffsetBlockState != null && isBenchPiece(getBenchType(reverseOffsetBlockState)))
				worldIn.setBlockState(pos.offset(benchAxis.getOpposite()), reverseOffsetBlockState);
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