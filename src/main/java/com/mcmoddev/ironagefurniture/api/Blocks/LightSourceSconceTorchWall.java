/*package com.mcmoddev.ironagefurniture.api.Blocks;

import java.util.ArrayList;
import java.util.List;

import com.mcmoddev.ironagefurniture.BlockObjectHolder;

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

public class LightSourceSconceTorchWall extends LightSourceSconceTorch {
	protected static final AxisAlignedBB NORTHBB = new AxisAlignedBB(0.35D, 0.2D, 0.1D, 0.65D, 0.8D, 0.5D);
	
	protected IBlockState getUnlitVariant(IBlockState initialState) {
		return BlockObjectHolder.light_metal_ironage_sconce_wall_torch_iron_unlit.getDefaultState()
				.withProperty(FACING, (EnumFacing)initialState.getProperties().get(FACING));
	}
	
	protected IBlockState getLitVariant(IBlockState initialState) {
		return BlockObjectHolder.light_metal_ironage_sconce_wall_torch_iron.getDefaultState()
				.withProperty(FACING, (EnumFacing)initialState.getProperties().get(FACING));
	}
	
	public LightSourceSconceTorchWall(Material materialIn, String name, float resistance, float hardness, boolean lit) {
		super(materialIn, name, resistance, hardness, lit);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected IBlockState getHolderBlockState(IBlockState initialState) {
		return BlockObjectHolder.light_metal_ironage_sconce_wall_empty_iron.getDefaultState()
				.withProperty(FACING, (EnumFacing)initialState.getProperties().get(FACING));
	}

	@Override
	protected AxisAlignedBB getNorthBB() {
		return NORTHBB;
	}

	@Override
	protected AxisAlignedBB getEastBB() {
		return getEast(NORTHBB);
	}

	@Override
	protected AxisAlignedBB getSouthBB() {
		return getSouth(NORTHBB);
	}

	@Override
	protected AxisAlignedBB getWestBB() {
		return getWest(NORTHBB);
	}
	
	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		List<ItemStack> drops = new ArrayList<ItemStack>();
		
		drops.add(new ItemStack(BlockObjectHolder.light_metal_ironage_sconce_floor_empty_iron, 1));
		drops.add(new ItemStack(Blocks.TORCH, 1));
		
		return drops;
	}
}*/