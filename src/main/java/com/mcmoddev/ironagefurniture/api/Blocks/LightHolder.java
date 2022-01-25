package com.mcmoddev.ironagefurniture.api.Blocks;

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

import com.google.common.collect.ImmutableList;
import com.mcmoddev.ironagefurniture.api.entity.Seat;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;

public abstract class LightHolder extends FurnitureBlock {
	public LightHolder(Properties properties) {
		super(properties);
		
		this.registerDefaultState(this.getStateDefinition().any().setValue(DIRECTION, Direction.NORTH).setValue(WATERLOGGED, false));
        this.generateShapes(this.getStateDefinition().getPossibleStates());
		// TODO Auto-generated constructor stub
	}
	
	public LightHolder(float hardness, float blastResistance, SoundType sound, String name) {
		super(Block.Properties.of(Material.METAL).strength(hardness, blastResistance).sound(sound));

		this.registerDefaultState(this.getStateDefinition().any().setValue(DIRECTION, Direction.NORTH));
		this.generateShapes(this.getStateDefinition().getPossibleStates());
		this.setRegistryName(name);
	}
	
	
    protected abstract InteractionResult ActivateSconce(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult rayTraceResult);
    
	@Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult rayTraceResult)
    {
        return ActivateSconce(state, world, pos, player, hand, rayTraceResult);
    }
	/*	
//	@Override
//	public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) {
//		// TODO Auto-generated method stub		
//		super.onBlockDestroyedByPlayer(worldIn, pos, state);
//		worldIn.checkLight(pos);
//	}
//	
	private void initLight(Material materialIn, String name, float resistance, float hardness) {
//		if (materialIn == Material.STONE) {	
//			this.sound(SoundType.STONE);
//			this.setHarvestLevel("pickaxe", 0);
//		}
//		
//		if (materialIn == Material.WOOD) {	
//			this.setSoundType(SoundType.WOOD);
//			this.setHarvestLevel("axe", 0);
//		}
//		
//		if (materialIn == Material.IRON) {	
//			this.setSoundType(SoundType.METAL);
//			this.setHarvestLevel("pickaxe", 1);
//		}
//
//		this.blockResistance = resistance;
//		this.blockHardness = hardness;
//		this.lightValue = 0;
//		
//		this.setCreativeTab(Ironagefurniture.ironagefurnitureTab);
	}
	
	public LightHolder(Material materialIn, String name, float resistance, float hardness) {
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
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) 
	{
		switch(state.getValue(FACING)) {
		case NORTH:
			return getNorthBB();
		case SOUTH:
			return getSouthBB();
		case WEST:
			return getWestBB();
		default:
			return getEastBB();
		}
	}
	
	@Override
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox,
			List<AxisAlignedBB> collidingBoxes, Entity entityIn) {
		//
	} */
}
