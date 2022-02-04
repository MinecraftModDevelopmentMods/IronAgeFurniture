package com.mcmoddev.ironagefurniture.api.Blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.TorchBlock;
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
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootContext.Builder;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.PoweredRailBlock;
import net.minecraft.world.level.block.RedstoneTorchBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mcmoddev.ironagefurniture.BlockObjectHolder;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;

public class LightSourceSconceTorchWallUnlit extends LightSourceSconceTorchWall {
	protected static final int AABB_STANDING_OFFSET = 2;
	protected static final VoxelShape AABB = Block.box(6.0D, 0.0D, 6.0D, 10.0D, 10.0D, 10.0D);
	
	public LightSourceSconceTorchWallUnlit(Properties properties) {
		super(properties);
		
		this.registerDefaultState(this.getStateDefinition().any().setValue(DIRECTION, Direction.NORTH).setValue(WATERLOGGED, false));
        this.generateShapes(this.getStateDefinition().getPossibleStates());
	}
	
	public LightSourceSconceTorchWallUnlit(float hardness, float blastResistance, SoundType sound, String name) {
		super(Block.Properties.of(Material.METAL).strength(hardness, blastResistance).sound(sound));

		this.registerDefaultState(this.getStateDefinition().any().setValue(DIRECTION, Direction.NORTH));
		this.generateShapes(this.getStateDefinition().getPossibleStates());
		this.setRegistryName(name);
	}

	public void animateTick(BlockState state, Level level, BlockPos pos, Random random)
	{
      // mask function
		if (level.hasNeighborSignal(pos))
	    	  Light(state, level, pos);
    }
	

	@Override
	public boolean canConnectRedstone(BlockState state, BlockGetter world, BlockPos pos, Direction direction) {
		return true;
	}
	
	private void Light(BlockState state, Level world, BlockPos pos) {
		world.setBlock(pos, BlockObjectHolder.light_metal_ironage_sconce_wall_torch_iron.defaultBlockState()
				.setValue(DIRECTION, state.getValue(BlockStateProperties.HORIZONTAL_FACING))
				.setValue(WATERLOGGED, state.getValue(BlockStateProperties.WATERLOGGED)), UPDATE_ALL);
	}
	
	protected Block GetEmptyVariant() {
		return BlockObjectHolder.light_metal_ironage_sconce_wall_empty_iron;
	}
	
	@Override
	protected InteractionResult ActivateSconce(BlockState state, Level world, BlockPos pos, Player player,
			InteractionHand hand, BlockHitResult rayTraceResult) {
		
		ItemStack stackInHand = player.getItemInHand(hand);
		boolean isWaterlogged = state.getValue(BlockStateProperties.WATERLOGGED);
		
		if (stackInHand.is(Items.FLINT_AND_STEEL) && !isWaterlogged) {
			
			if (!player.isCreative())
				stackInHand.setDamageValue(stackInHand.getDamageValue()+1);
		
			Light(state, world, pos);
			
			return InteractionResult.SUCCESS;
		}
		
		if (stackInHand.is(Blocks.TORCH.asItem()) && !isWaterlogged) {
			Light(state, world, pos);
			
			return InteractionResult.SUCCESS;
		}
		
		if (player.isCreative() && (stackInHand.is(Blocks.TORCH.asItem()) || stackInHand.isEmpty())) {
			world.setBlock(pos, GetEmptyVariant().defaultBlockState()
					.setValue(DIRECTION, state.getValue(BlockStateProperties.HORIZONTAL_FACING))
					.setValue(WATERLOGGED, state.getValue(BlockStateProperties.WATERLOGGED)), UPDATE_ALL);
			
			return InteractionResult.CONSUME_PARTIAL;
		}
		
		return InteractionResult.FAIL;
	}
}
