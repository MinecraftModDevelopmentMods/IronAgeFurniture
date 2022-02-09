/*package com.mcmoddev.ironagefurniture.api.Blocks;

import com.mcmoddev.ironagefurniture.Ironagefurniture;
import com.mcmoddev.ironagefurniture.api.Enumerations.LightType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext.Builder;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;

public abstract class LightSource extends BlockHBase {
	@Override
	public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) {
		// TODO Auto-generated method stub
		
		super.onBlockDestroyedByPlayer(worldIn, pos, state);
		worldIn.checkLight(pos);
	}
	
	protected abstract IBlockState getHolderBlockState(IBlockState initialState);
	protected abstract LightType getLightType();
	protected abstract AxisAlignedBB getNorthBB();
	protected abstract AxisAlignedBB getEastBB();
	protected abstract AxisAlignedBB getSouthBB();
	protected abstract AxisAlignedBB getWestBB();
	
	private void initLight(Material materialIn, String name, float resistance, float hardness) {
		
		this.setTickRandomly(true);
		
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
	
	public LightSource(Material materialIn, String name, float resistance, float hardness) {
		super(materialIn);
		
		initLight(materialIn, name, resistance, hardness);
	}
	
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY,
			float hitZ, int meta, EntityLivingBase placer, ItemStack stack) {

		IBlockState state = super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, stack)
				.withProperty(FACING, placer.getHorizontalFacing());
	
		world.checkLight(pos);
		
		return state;
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack) {
		// TODO Auto-generated method stub
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
		
		worldIn.checkLight(pos);
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) 
	{			
		return this.getDefaultState()
				.withProperty(FACING, EnumFacing.getHorizontal(meta));
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		int i = ((EnumFacing)state.getValue(FACING)).getHorizontalIndex();

		return i;
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, new IProperty[] { FACING });
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
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World worldIn, BlockPos pos) {
		return NULL_AABB;
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) 
	{
		switch ((EnumFacing)state.getValue(FACING))
        {
            case EAST:
                return getEastBB();
            case WEST:
                return getWestBB();
            case SOUTH:
                return getSouthBB();
            case NORTH:
                return getNorthBB();
            default:
                return getEastBB();
        }
	}
}*/