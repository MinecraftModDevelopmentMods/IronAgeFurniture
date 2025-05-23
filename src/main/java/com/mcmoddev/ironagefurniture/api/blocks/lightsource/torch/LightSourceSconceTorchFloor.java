package com.mcmoddev.ironagefurniture.api.blocks.lightsource.torch;

import com.mcmoddev.ironagefurniture.api.blocks.lightholder.LightHolderSconceFloor;
import com.mcmoddev.ironagefurniture.init.ModVanillaLights;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootContext.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;

public class LightSourceSconceTorchFloor extends LightHolderSconceFloor implements LiquidBlockContainer {
	protected static final VoxelShape AABB = Block.box(6.0D, 0.0D, 6.0D, 10.0D, 13.0D, 10.0D);
	protected ParticleOptions flameParticle;

	@Override
	public List<ItemStack> getDrops(BlockState state, Builder builder) {
		List<ItemStack> drops;
		drops = new ArrayList<>();

		Item sconceItem = DropVariant().asItem();
		ItemStack sconceStack = new ItemStack(sconceItem, 1);
		drops.add(sconceStack);
		
		if (ShouldDrop()) {
			Item lightItem = LightDrop().asItem();
			ItemStack lightStack = new ItemStack(lightItem, 1);
			
			drops.add(lightStack);
		}
		
		return drops;
	}

	protected Block LightDrop() {
		return Blocks.TORCH;
	}

	protected boolean ShouldDrop() {
		return true;
	}
	
	public LightSourceSconceTorchFloor(Properties properties) {
		super(properties);

		this.registerDefaultState(this.getStateDefinition().any().setValue(DIRECTION, Direction.NORTH).setValue(WATERLOGGED, false));
		this.generateShapes(this.getStateDefinition().getPossibleStates());
		this.flameParticle = ParticleTypes.FLAME;
	}

	public LightSourceSconceTorchFloor(float hardness, float blastResistance, SoundType sound, String name) {
		super(Block.Properties.of(Material.METAL).strength(hardness, blastResistance).sound(sound).lightLevel((p_50886_) -> 14));

		this.registerDefaultState(this.getStateDefinition().any().setValue(DIRECTION, Direction.NORTH));
		this.generateShapes(this.getStateDefinition().getPossibleStates());
		//this.setRegistryName(name);
		this.flameParticle = ParticleTypes.FLAME;
	}

	@Override
	protected void generateShapes(ImmutableList<BlockState> states) {
		ImmutableMap.Builder<BlockState, VoxelShape> builder = new ImmutableMap.Builder<>();
		for (BlockState state : states)
			builder.put(state, AABB);

		_shapes = builder.build();
	}

	@Override
	public boolean canConnectRedstone(BlockState state, BlockGetter world, BlockPos pos, Direction direction) {
		return true;
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
		if (HasFlame()) {
			double d0 = (double) pos.getX() + 0.5D;
			double d1 = (double) pos.getY() + 0.9D;
			double d2 = (double) pos.getZ() + 0.5D;

			level.addParticle(ParticleTypes.SMOKE, d0, d1, d2, 0.0D, 0.0D, 0.0D);
			level.addParticle(this.flameParticle, d0, d1, d2, 0.0D, 0.0D, 0.0D);
		}		
	}

	@Override
	public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource rnd) {
		if (CanEx() && !level.hasNeighborSignal(pos)) {
			if (state.getValue(BlockStateProperties.WATERLOGGED))
				Unlight(state, level, pos);
		}
		super.tick(state, level, pos, rnd);
	}
	
	@Override
	public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos blockPos,
								boolean flag) {
	    boolean hasSignal = this.hasNeighborSignal(level, pos, state);
	    boolean willTick = level.getBlockTicks().willTickThisTick(pos, this);

	    if (!hasSignal) {
	        if (!willTick) {
	            level.scheduleTick(pos, this, 2);
	        }
	    }
	}
	
	protected boolean hasNeighborSignal(Level level, BlockPos pos, BlockState state) {
		for (Direction direction : Direction.values()) {
	        if (direction != Direction.UP && level.hasSignal(pos.relative(direction), direction)) {
	            return true;
	        }
	    }
	    return false;
	}
	
	private void Unlight(BlockState state, Level world, BlockPos pos) {
		if (!CanEx())
			return;
		world.getBlockState(pos);
		world.setBlock(pos, UnlitVariant().defaultBlockState()
			.setValue(DIRECTION, state.getValue(BlockStateProperties.HORIZONTAL_FACING))
			.setValue(WATERLOGGED, state.getValue(BlockStateProperties.WATERLOGGED)), UPDATE_ALL);
		
		world.sendBlockUpdated(pos, state, world.getBlockState(pos), UPDATE_ALL);
	}

	@Override
	public boolean placeLiquid(LevelAccessor world, BlockPos pos, BlockState blockState, FluidState fluidState) {
		boolean success = super.placeLiquid(world, pos, blockState, fluidState);

		if (!blockState.getValue(BlockStateProperties.WATERLOGGED) && fluidState.getType() == Fluids.WATER) {
			if (world.isClientSide())
				return success;

			if (CanEx()) world.setBlock(pos, UnlitVariant().defaultBlockState()
				.setValue(DIRECTION, blockState.getValue(BlockStateProperties.HORIZONTAL_FACING))
				.setValue(WATERLOGGED, Boolean.valueOf(true)), UPDATE_ALL);

			world.scheduleTick(pos, fluidState.getType(), fluidState.getType().getTickDelay(world));
		} else {
			if (CanEx()) world.setBlock(pos, UnlitVariant().defaultBlockState()
				.setValue(DIRECTION, blockState.getValue(BlockStateProperties.HORIZONTAL_FACING))
				.setValue(WATERLOGGED, blockState.getValue(BlockStateProperties.WATERLOGGED)), UPDATE_ALL);
		}

		return success;
	}

	protected Block UnlitVariant() {
		return ModVanillaLights.light_metal_ironage_sconce_floor_torch_iron_unlit.get();
	}

	protected Block EmptyVariant() {
		return ModVanillaLights.light_metal_ironage_sconce_floor_empty_iron.get();
	}

	protected Block DropVariant() {
		return EmptyVariant();
	}
	
	protected boolean CanEx() {
		return true;
	}

	protected boolean HasFlame() {
		return true;
	}

	protected boolean InvertDirection() {
		return false;
	}

	@Override
	protected InteractionResult ActivateSconce(BlockState state, Level world, BlockPos pos, Player player,
											   InteractionHand hand, BlockHitResult rayTraceResult) {

		ItemStack stackInHand = player.getItemInHand(hand);

		if (CanEx() && stackInHand.is(Items.WATER_BUCKET)) {
			world.setBlock(pos, UnlitVariant().defaultBlockState()
				.setValue(DIRECTION, state.getValue(BlockStateProperties.HORIZONTAL_FACING))
				.setValue(WATERLOGGED, state.getValue(BlockStateProperties.WATERLOGGED)), UPDATE_ALL);

			return InteractionResult.SUCCESS;
		}

		if (stackInHand.is(LightDrop().asItem()) || stackInHand.isEmpty()) {
			Direction direction = state.getValue(BlockStateProperties.HORIZONTAL_FACING);

			if (InvertDirection())
				direction = direction.getOpposite();

			world.setBlock(pos, EmptyVariant().defaultBlockState()
				.setValue(DIRECTION, direction)
				.setValue(WATERLOGGED, state.getValue(BlockStateProperties.WATERLOGGED)), UPDATE_ALL);

			if (!player.isCreative()) {
				if (stackInHand.is(LightDrop().asItem()))
					stackInHand.setCount(stackInHand.getCount() + 1);
				else {
					Item itemTorch = LightDrop().asItem();
					ItemStack stackTorch = new ItemStack(itemTorch, 1);

					player.setItemInHand(hand, stackTorch);
				}
			}

			return InteractionResult.CONSUME_PARTIAL;
		}

		return InteractionResult.FAIL;
	}
}
