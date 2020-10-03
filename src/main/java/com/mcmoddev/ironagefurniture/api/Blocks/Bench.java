package com.mcmoddev.ironagefurniture.api.Blocks;

import java.util.List;

import com.mcmoddev.ironagefurniture.api.Enumerations.BenchType;
import com.mcmoddev.ironagefurniture.api.Enumerations.Rotation;
import com.mcmoddev.ironagefurniture.api.entity.Seat;
import com.mcmoddev.ironagefurniture.api.util.Swivel;

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
			float hitZ, int meta, EntityLivingBase placer, ItemStack stack, BlockPos offsetPos, IBlockState defaultStateForPlacement, EnumFacing playerFacing) {
		
		IBlockState offsetBlockState = world.getBlockState(offsetPos);
		IBlockState newOffsetBlockState;
		
		Block offsetBlock = offsetBlockState.getBlock();
		IBlockState stateForPlacement = null;
		
		if (offsetBlock.getUnlocalizedName().equals(defaultStateForPlacement.getBlock().getUnlocalizedName())) {
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
			
			// do initial test to join on facing
			switch (playerFacing) {
				case SOUTH:
					doubleEndBlockState = tryMakeDoubleBench(world, pos, facing, hitX, hitY, hitZ, meta, placer, stack, pos.south(1), stateForPlacement, Swivel.Rotate(playerFacing, Rotation.Ninty));
					break;
		
				case WEST:
					doubleEndBlockState = tryMakeDoubleBench(world, pos, facing, hitX, hitY, hitZ, meta, placer, stack, pos.west(1), stateForPlacement, Swivel.Rotate(playerFacing, Rotation.Ninty));
					break;
		
				case NORTH:
					doubleEndBlockState = tryMakeDoubleBench(world, pos, facing, hitX, hitY, hitZ, meta, placer, stack, pos.north(1), stateForPlacement, Swivel.Rotate(playerFacing, Rotation.Ninty));
					break;
		
				case EAST:
					doubleEndBlockState = tryMakeDoubleBench(world, pos, facing, hitX, hitY, hitZ, meta, placer, stack, pos.east(1), stateForPlacement, Swivel.Rotate(playerFacing, Rotation.Ninty));
					break;
		
				default:
					break;
			}
	
			// if none was found on the player facing, have a look around for others to join with
			if (doubleEndBlockState == null ) {
				doubleEndBlockState = tryMakeDoubleBench(world, pos, facing, hitX, hitY, hitZ, meta, placer, stack, pos.north(1), stateForPlacement, Swivel.Rotate(EnumFacing.NORTH, Rotation.Ninty));
				
				if (doubleEndBlockState == null)
					doubleEndBlockState = tryMakeDoubleBench(world, pos, facing, hitX, hitY, hitZ, meta, placer, stack, pos.south(1), stateForPlacement, Swivel.Rotate(EnumFacing.SOUTH, Rotation.Ninty));
				
				if (doubleEndBlockState == null)
					doubleEndBlockState = tryMakeDoubleBench(world, pos, facing, hitX, hitY, hitZ, meta, placer, stack, pos.east(1), stateForPlacement, Swivel.Rotate(EnumFacing.EAST, Rotation.Ninty));
				
				if (doubleEndBlockState == null)
					doubleEndBlockState = tryMakeDoubleBench(world, pos, facing, hitX, hitY, hitZ, meta, placer, stack, pos.west(1), stateForPlacement, Swivel.Rotate(EnumFacing.WEST, Rotation.Ninty));		
			}
			
			if (doubleEndBlockState != null)
				return doubleEndBlockState;
		}				
		
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