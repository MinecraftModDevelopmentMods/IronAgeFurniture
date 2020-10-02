package com.mcmoddev.ironagefurniture.api.Blocks;

import java.util.List;

import com.mcmoddev.ironagefurniture.api.Enumerations.BenchType;
import com.mcmoddev.ironagefurniture.api.entity.Seat;

import ibxm.Player;
import net.minecraft.block.Block;
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
			float hitZ, int meta, EntityLivingBase placer, ItemStack stack, BlockPos offsetPos, IBlockState defaultStateForPlacement, int horizontalIndex) {
		
		IBlockState offsetBlockState = world.getBlockState(offsetPos);
		IBlockState newOffsetBlockState;
		
		Block offsetBlock = offsetBlockState.getBlock();
		IBlockState stateForPlacement = null;
		
		if (offsetBlock.getUnlocalizedName().equals(defaultStateForPlacement.getBlock().getUnlocalizedName())) {
			EnumFacing newBlockFacing = EnumFacing.getHorizontal(horizontalIndex);
			EnumFacing existingBlockFacing = EnumFacing.getHorizontal(horizontalIndex).getOpposite();
			
			stateForPlacement =  super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, stack)
					.withProperty(FACING, newBlockFacing)
					.withProperty(TYPE, BenchType.END);
			
			newOffsetBlockState = offsetBlockState
					.withProperty(FACING, existingBlockFacing)
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
		
		IBlockState doubleEndBlockState = null;
		
		boolean nTested = false;
		boolean sTested = false;
		boolean eTested = false;
		boolean wTested = false;
		
		int horizontalIndex = facing.getHorizontalIndex();
		
		// do initial test to join on facing
		switch (horizontalIndex) {
			case 0: //s
				doubleEndBlockState = tryMakeDoubleBench(world, pos, facing, hitX, hitY, hitZ, meta, placer, stack, pos.south(1), stateForPlacement, horizontalIndex);
				nTested = true;
				break;
	
			case 1: //w
				doubleEndBlockState = tryMakeDoubleBench(world, pos, facing, hitX, hitY, hitZ, meta, placer, stack, pos.west(1), stateForPlacement, horizontalIndex);
				sTested = true;
				break;
	
			case 2: //n
				doubleEndBlockState = tryMakeDoubleBench(world, pos, facing, hitX, hitY, hitZ, meta, placer, stack, pos.north(1), stateForPlacement, horizontalIndex);
				eTested = true;
				break;
	
			case 3: //e
				doubleEndBlockState = tryMakeDoubleBench(world, pos, facing, hitX, hitY, hitZ, meta, placer, stack, pos.east(1), stateForPlacement, horizontalIndex);
				wTested = true;
				break;
	
			default:
			break;
		}

		// if none was found on the player facing, have a look around for others to join with
		if (doubleEndBlockState == null ) {
			if (!sTested)
				doubleEndBlockState = tryMakeDoubleBench(world, pos, facing, hitX, hitY, hitZ, meta, placer, stack, pos.north(1), stateForPlacement, 0);
			
			if (!wTested && doubleEndBlockState == null)
				doubleEndBlockState = tryMakeDoubleBench(world, pos, facing, hitX, hitY, hitZ, meta, placer, stack, pos.south(1), stateForPlacement, 1);
			
			if (!nTested && doubleEndBlockState == null)
				doubleEndBlockState = tryMakeDoubleBench(world, pos, facing, hitX, hitY, hitZ, meta, placer, stack, pos.east(1), stateForPlacement, 2);
			
			if (!eTested && doubleEndBlockState == null)
				doubleEndBlockState = tryMakeDoubleBench(world, pos, facing, hitX, hitY, hitZ, meta, placer, stack, pos.west(1), stateForPlacement, 3);		
		}
		
		if (doubleEndBlockState != null)
			return doubleEndBlockState;
//		//look around for an adjacent single bench to turn into two ends
//		Block blockEast = world.getBlockState(pos.east(1)).getBlock();
//		Block blockWest = world.getBlockState(pos.west(1)).getBlock();
//		Block blockNorth = world.getBlockState(pos.north(1)).getBlock();
//		Block blockSouth = world.getBlockState(pos.south(1)).getBlock();
//		
//		if (blockEast.getUnlocalizedName().equals(stateForPlacement.getBlock().getUnlocalizedName())) {
//			stateForPlacement =  super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, stack)
//					.withProperty(FACING, placer.getHorizontalFacing())
//					.withProperty(TYPE, BenchType.END);
//		}
//		
//		if (blockWest.getUnlocalizedName() == stateForPlacement.getBlock().getUnlocalizedName()) {
//			stateForPlacement =  super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, stack)
//					.withProperty(FACING, placer.getHorizontalFacing())
//					.withProperty(TYPE, BenchType.END);
//		}
//		
//		if (blockNorth.getUnlocalizedName() == stateForPlacement.getBlock().getUnlocalizedName()) {
//			stateForPlacement =  super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, stack)
//					.withProperty(FACING, placer.getHorizontalFacing())
//					.withProperty(TYPE, BenchType.END);
//		}
//		
//		if (blockSouth.getUnlocalizedName() == stateForPlacement.getBlock().getUnlocalizedName()) {
//			stateForPlacement =  super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, stack)
//					.withProperty(FACING, placer.getHorizontalFacing())
//					.withProperty(TYPE, BenchType.END);
//		}
		//if (facing == EnumFacing.EAST || facing == EnumFacing.WEST) {
			
		//}
		
		//look around for an adjacent bench
		//if (facing == EnumFacing.NORTH || facing == EnumFacing.SOUTH) {
			
		//}
		
		
		
		return stateForPlacement;
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