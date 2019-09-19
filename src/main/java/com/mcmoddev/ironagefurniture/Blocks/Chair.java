package com.mcmoddev.ironagefurniture.Blocks;

import java.util.List;

import com.mcmoddev.ironagefurniture.Ironagefurniture;
import com.mcmoddev.ironagefurniture.Enumerations.Rotation;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
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

public class Chair extends BlockHBase {
	private static final AxisAlignedBB BB = new AxisAlignedBB(0.1, 0.0, 0.1, 0.9, 1.6, 0.9);
	private static final AxisAlignedBB BASEBB = new AxisAlignedBB(0.1, 0.0, 0.1, 0.9, 0.5, 0.9);
	private static final AxisAlignedBB BACKEAST = new AxisAlignedBB(0.825, 0.6, 0.1, 0.9, 1.2, 0.9);
	private static final AxisAlignedBB BACKNORTH = RotateBB(Rotation.Ninty, BACKEAST);
	private static final AxisAlignedBB BACKSOUTH = RotateBB(Rotation.OneEighty, BACKEAST);
	private static final AxisAlignedBB BACKWEST = RotateBB(Rotation.TwoSeventy, BACKEAST);
	
	public Chair(Material materialIn, String name, float resistance) {
		super(materialIn);
		
		if (materialIn == Material.ROCK) {	
			this.setSoundType(SoundType.STONE);
			this.setHarvestLevel("pickaxe", 0);
		}
		
		if (materialIn == Material.WOOD) {	
			this.setSoundType(SoundType.WOOD);
			this.setHarvestLevel("axe", 0);
		}
		
		if (materialIn == Material.IRON) {	
			this.setSoundType(SoundType.METAL);
			this.setHarvestLevel("pickaxe", 1);
		}
				this.blockResistance = resistance;
				
		this.setCreativeTab(Ironagefurniture.ironagefurnitureTab);
	}
	
	@Override
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox,
			List<AxisAlignedBB> collidingBoxes, Entity entityIn) {
		
		EnumFacing facing = state.getValue(FACING);
		switch(facing) 
		{
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
		
		super.addCollisionBoxToList(pos, entityBox, collidingBoxes, BASEBB);
	}
	
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY,
			float hitZ, int meta, EntityLivingBase placer, ItemStack stack) {
		IBlockState state = super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, stack);
		state = state.withProperty(FACING, placer.getHorizontalFacing());
		return state;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) 
	{
		return this.getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta));
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return ((EnumFacing) state.getValue(FACING)).getHorizontalIndex();
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, new IProperty[] { FACING });
	}
	
	@Override
	public boolean hasComparatorInputOverride(IBlockState state) 
	{
		return true;
	}

	@Override
	public boolean isFullCube(IBlockState bs) {
		return false;
	}

	@Override
	public boolean isOpaqueCube(IBlockState bs) {
		return false;
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) 
	{
		return BB;
	}
}
