package com.mcmoddev.ironagefurniture.Blocks;

import java.util.List;

import com.mcmoddev.ironagefurniture.Ironagefurniture;
import com.mcmoddev.ironagefurniture.Enumerations.Rotation;
import com.mcmoddev.ironagefurniture.entity.Seat;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
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
	private final double yOffset;
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {

		for (Seat seat : worldIn.getEntitiesWithinAABB(Seat.class, new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1.0D, pos.getY() + 1.0D, pos.getZ() + 1.0D).expand(1D, 1D, 1D)))
		{
			if ((seat.SeatCoordinates.Match(pos.getX(), pos.getY(), pos.getZ())) && !seat.isBeingRidden())
			{
				playerIn.startRiding(seat);
				return false;
			}
		}
		
		Seat seat = new Seat(worldIn, pos.getX(), pos.getY(), pos.getZ(), this.yOffset);
		
		worldIn.spawnEntityInWorld(seat);
		playerIn.startRiding(seat);	
		worldIn.updateComparatorOutputLevel(pos, this);
		
		return true;
	}
	
	public Chair(Material materialIn, String name, float resistance, double yOffset, float hardness) {
		super(materialIn);
		this.yOffset = yOffset;
		InitChair(materialIn, name, resistance, hardness);
	}
	
	private void InitChair(Material materialIn, String name, float resistance, float hardness) {
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
		this.blockHardness = hardness;
		this.setCreativeTab(Ironagefurniture.ironagefurnitureTab);
	}
	
	public Chair(Material materialIn, String name, float resistance, float hardness) {
		super(materialIn);
		this.yOffset = 0.3;
		InitChair(materialIn, name, resistance, hardness);
	}
	
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY,
			float hitZ, int meta, EntityLivingBase placer, ItemStack stack) {

		return super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, stack).withProperty(FACING, placer.getHorizontalFacing());
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
	public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos)
	{
		for (Seat seat : worldIn.getEntitiesWithinAABB(Seat.class, new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1.0D, pos.getY() + 1.0D, pos.getZ() + 1.0D).expand(1D, 1D, 1D)))
			if (seat.SeatCoordinates.Match(pos.getX(), pos.getY(), pos.getZ()))
				return seat.isBeingRidden() ? 1 : 0;
		
		return 0;
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
			
			super.addCollisionBoxToList(pos, entityBox, collidingBoxes, BASEBB);
		}
	}
}
