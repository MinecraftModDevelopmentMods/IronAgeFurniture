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

	private IBlockState tryMakeDoubleBench(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY,
			float hitZ, int meta, EntityLivingBase placer, ItemStack stack, BlockPos offsetPos, IBlockState defaultStateForPlacement, EnumFacing playerFacing) {
		
		IBlockState offsetBlockState = world.getBlockState(offsetPos);
		IBlockState newOffsetBlockState;
		
		//Block offsetBlock = offsetBlockState.getBlock();
		IBlockState stateForPlacement = null;
		
		if (offsetBlockState.getProperties().get(TYPE) == BenchType.SINGLE) {
			stateForPlacement =  super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, stack)
					.withProperty(FACING, playerFacing.getOpposite())
					.withProperty(TYPE, BenchType.END);
			
			newOffsetBlockState = offsetBlockState
					.withProperty(FACING, playerFacing)
					.withProperty(TYPE, BenchType.END);
			
			world.setBlockState(offsetPos, newOffsetBlockState);
		}
		
		return stateForPlacement;
	}
	
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY,
			float hitZ, int meta, EntityLivingBase placer, ItemStack stack) {

		IBlockState stateForPlacement =  super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, stack)
				.withProperty(FACING, placer.getHorizontalFacing())
				.withProperty(TYPE, BenchType.SINGLE);
		
		if (!placer.isSneaking()) {
		
			IBlockState doubleEndBlockState = null;
	
			EnumFacing playerFacing = placer.getHorizontalFacing();
			EnumFacing benchAxis;
			
			// do initial test to join on facing
			doubleEndBlockState = tryMakeDoubleBench(world, pos, facing, hitX, hitY, hitZ, meta, placer, stack, pos.offset(playerFacing), stateForPlacement, Swivel.Rotate(playerFacing, Rotation.Ninty));
	
			// if none was found on the player facing, have a look around for others to join with
			if (doubleEndBlockState == null ) {
				for (EnumFacing face : EnumFacing.HORIZONTALS) {
					doubleEndBlockState = tryMakeDoubleBench(world, pos, facing, hitX, hitY, hitZ, meta, placer, stack, pos.offset(face), stateForPlacement, Swivel.Rotate(face, Rotation.Ninty));
				
					if (doubleEndBlockState != null)
						break;
				}
			}
		
			// next, we need to see if this new piece of bench now forms part of a longer unit
			if (doubleEndBlockState != null)
				stateForPlacement = doubleEndBlockState;
			else {
				benchAxis = getBenchToJoinTo(playerFacing, world, pos);
				
				if (benchAxis != null) {
					stateForPlacement = traceBench(benchAxis, world, pos, stateForPlacement);
				}
			}
		}				
		
		return stateForPlacement;
	}

	private IBlockState traceBench(EnumFacing direction, World world, BlockPos pos, IBlockState blockState) {
//		traceBench(direction, world, pos, 0, blockState);
//		
//		IBlockState offsetBlockState = world.getBlockState(pos.offset(direction.getOpposite()));
//		
//		if (offsetBlockState.getProperties().get(TYPE) == BenchType.SINGLE || 
//				offsetBlockState.getProperties().get(TYPE) == BenchType.MIDDLE ||
//				offsetBlockState.getProperties().get(TYPE) == BenchType.END) {
//			
//			traceBench(direction.getOpposite(), world, pos, 0, blockState);
//		}
		
		
		IBlockState thisPieceBlockState = computeBenchBlockState(0, direction, world, pos, true, blockState, BenchType.SINGLE);
		BenchType placedBenchType = (BenchType)thisPieceBlockState.getProperties().get(TYPE);
		
		
		BenchType offsetBenchType = (BenchType)world.getBlockState(pos.offset(direction)).getProperties().get(TYPE);
		BenchType reverseOffsetBenchType = (BenchType)world.getBlockState(pos.offset(direction.getOpposite())).getProperties().get(TYPE);
		
		
		if (isBenchPiece(offsetBenchType))
			traceBenchLine(direction, world, pos, thisPieceBlockState, placedBenchType);
		
		if (isBenchPiece(reverseOffsetBenchType))
			traceBenchLine(direction.getOpposite(), world, pos, thisPieceBlockState, placedBenchType);
		
		return thisPieceBlockState;
	}
	
	private void traceBenchLine (EnumFacing direction, World world, BlockPos pos, IBlockState blockState, BenchType placedBenchType) {
		int offset = 1;
		IBlockState nextPieceBlockState = computeBenchBlockState(offset, direction, world, pos, true, null, placedBenchType);
		BenchType nextPieceBenchType = (BenchType)nextPieceBlockState.getProperties().get(TYPE);
		world.setBlockState(pos.offset(direction, offset), nextPieceBlockState);
		
		while (isBenchPiece(nextPieceBenchType)) {
			offset++;
			nextPieceBlockState = computeBenchBlockState(offset, direction, world, pos, placedBenchType);
			world.setBlockState(pos.offset(direction, offset), nextPieceBlockState);
			nextPieceBenchType = (BenchType)nextPieceBlockState.getProperties().get(TYPE);
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
		
		IBlockState currentPosBlockState = world.getBlockState(offsetPos);
		
		if (blockstate != null)
			currentPosBlockState = blockstate;
		else
			currentPosBlockState = world.getBlockState(offsetPos);
		
		BenchType currentType = (BenchType)currentPosBlockState.getProperties().get(TYPE);
		
		IBlockState offsetBlockState = world.getBlockState(offsetPos.offset(direction));
		BenchType offsetType = (BenchType)offsetBlockState.getProperties().get(TYPE);
		
		IBlockState reverseOffsetBlockState;
		
		BenchType reverseOffsetType;
		
		if (offset == 1) {
			reverseOffsetBlockState = blockstate;
			reverseOffsetType = placedBenchType;
		} else {
			reverseOffsetBlockState = world.getBlockState(offsetPos.offset(direction.getOpposite()));
			reverseOffsetType = (BenchType)reverseOffsetBlockState.getProperties().get(TYPE);
		}
		
		if (!bypassCurrentPosCheck && !isBenchPiece(currentType)) 
			return currentPosBlockState;

		if (!isBenchPiece(offsetType) && !isBenchPiece(reverseOffsetType))
			return currentPosBlockState
					.withProperty(FACING, Swivel.Rotate(direction, Rotation.Ninty))
					.withProperty(TYPE, BenchType.SINGLE);
		
		
		if (isBenchPiece(offsetType) && !isBenchPiece(reverseOffsetType))
			return currentPosBlockState
					.withProperty(FACING, Swivel.Rotate(direction.getOpposite(), Rotation.Ninty))
					.withProperty(TYPE, BenchType.END);
		
		if (!isBenchPiece(offsetType) && isBenchPiece(reverseOffsetType))	
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
	
//	private IBlockState traceBench(EnumFacing direction, World world, BlockPos pos, int offset, IBlockState blockState) {
//		
//		IBlockState offsetBlockState = world.getBlockState(pos.offset(direction, offset + 1));
//		
//		if (offsetBlockState.getProperties().get(TYPE) == BenchType.SINGLE || 
//				offsetBlockState.getProperties().get(TYPE) == BenchType.MIDDLE ||
//				offsetBlockState.getProperties().get(TYPE) == BenchType.END) {
//			
//			IBlockState currentBlockState = traceBench(direction, world, pos, offset + 1, blockState);
//			
//			// the last iteration was an end, now trace back
//			if (currentBlockState.getProperties().get(TYPE) == BenchType.END) {
//				IBlockState newBlockState = currentBlockState
//						.withProperty(FACING, direction)
//						.withProperty(TYPE, BenchType.MIDDLE);
//				
//					world.setBlockState(pos, newBlockState);
//				
//			}
//		} else {
//			IBlockState newBlockState = world.getBlockState(pos.offset(direction, offset));
//				
//			newBlockState = blockState
//					.withProperty(FACING, direction)
//					.withProperty(TYPE, BenchType.END);
//			
//				world.setBlockState(pos, newBlockState);
//			
//				return newBlockState;	
//		}
//		
//		return blockState;
//	}
	
	private boolean isBenchEnd(EnumFacing facing, World world, BlockPos pos) {		
		if (world.getBlockState(pos.offset(facing)).getProperties().get(TYPE) == BenchType.END)
			return true;

		return false;
	}
	
	private EnumFacing getBenchToJoinTo(EnumFacing playerFacing, World world, BlockPos pos) {
		EnumFacing benchAxis = null;
		
		// again, favour player facing
		if (isBenchEnd(playerFacing, world, pos))
			benchAxis = playerFacing;
		
		if (benchAxis == null) {
			for (EnumFacing face : EnumFacing.HORIZONTALS) {
				if (isBenchEnd(face, world, pos))
					benchAxis = playerFacing;
				
				if (benchAxis != null)
					break;
			}
		}
		
		return benchAxis;
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