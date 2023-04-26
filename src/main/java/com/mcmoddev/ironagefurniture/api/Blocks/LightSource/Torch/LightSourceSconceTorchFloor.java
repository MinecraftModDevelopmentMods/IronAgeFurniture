package com.mcmoddev.ironagefurniture.api.Blocks.LightSource.Torch;

import com.mcmoddev.ironagefurniture.api.Blocks.LightHolder.LightHolderSconceFloor;
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
import net.minecraft.world.InteractionHand;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mcmoddev.ironagefurniture.BlockObjectHolder;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;

public class LightSourceSconceTorchFloor extends LightHolderSconceFloor implements LiquidBlockContainer {
	protected static final int AABB_STANDING_OFFSET = 2;
	protected static final VoxelShape AABB = Block.box(6.0D, 0.0D, 6.0D, 10.0D, 13.0D, 10.0D);
	protected ParticleOptions flameParticle;

	@Override
    public List<ItemStack> getDrops(BlockState state, Builder builder) {
    	List<ItemStack> drops;
    	drops = new ArrayList<>();

    	Item item = EmptyVariant().asItem();
    	ItemStack stack = new ItemStack(item, 1);

    	Item item2 = LightDrop().asItem();
    	ItemStack stack2 = new ItemStack(item2, 1);

    	drops.add(stack);
    	drops.add(stack2);

    	return drops;
    }

	protected Block LightDrop() {
		return Blocks.TORCH;
	}

	public LightSourceSconceTorchFloor(Properties properties) {
		super(properties);

		this.registerDefaultState(this.getStateDefinition().any().setValue(DIRECTION, Direction.NORTH).setValue(WATERLOGGED, false));
        this.generateShapes(this.getStateDefinition().getPossibleStates());
        this.flameParticle = ParticleTypes.FLAME;
	}

	public LightSourceSconceTorchFloor(float hardness, float blastResistance, SoundType sound, String name) {
		super(Block.Properties.of(Material.METAL).strength(hardness, blastResistance).sound(sound).lightLevel((p_50886_) -> 14) );

		this.registerDefaultState(this.getStateDefinition().any().setValue(DIRECTION, Direction.NORTH));
		this.generateShapes(this.getStateDefinition().getPossibleStates());
		this.setRegistryName(name);
		this.flameParticle = ParticleTypes.FLAME;
	}

	@Override
	protected void generateShapes(ImmutableList<BlockState> states) {
		 ImmutableMap.Builder<BlockState, VoxelShape> builder = new ImmutableMap.Builder<>();
	        for(BlockState state : states)
				builder.put(state, AABB);

	        _shapes = builder.build();
	}

	@Override
	public boolean canConnectRedstone(BlockState state, BlockGetter world, BlockPos pos, Direction direction) {
		return true;
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, Random random)
	{
		if (HasFlame()) {
		  double d0 = (double)pos.getX() + 0.5D;
	      double d1 = (double)pos.getY() + 0.9D;
	      double d2 = (double)pos.getZ() + 0.5D;

	      level.addParticle(ParticleTypes.SMOKE, d0, d1, d2, 0.0D, 0.0D, 0.0D);
	      level.addParticle(this.flameParticle, d0, d1, d2, 0.0D, 0.0D, 0.0D);
		}

		if (CanEx() && !level.hasNeighborSignal(pos)) {
    	  if (state.getValue(BlockStateProperties.WATERLOGGED))
    		  Unlight(state, level, pos);
		}
    }

	private void Unlight(BlockState state, Level world, BlockPos pos) {
		if (CanEx()) {
		world.setBlock(pos, UnlitVariant().defaultBlockState()
				.setValue(DIRECTION, state.getValue(BlockStateProperties.HORIZONTAL_FACING))
				.setValue(WATERLOGGED, state.getValue(BlockStateProperties.WATERLOGGED)), UPDATE_ALL);
		}
	}

	@Override
	public boolean placeLiquid(LevelAccessor world, BlockPos pos, BlockState blockState, FluidState fluidState) {
		boolean success = super.placeLiquid(world, pos, blockState, fluidState);

		if (!blockState.getValue(BlockStateProperties.WATERLOGGED) && fluidState.getType() == Fluids.WATER) {
			if (world.isClientSide())
				return success;

			if (CanEx()) {
			   world.setBlock(pos, UnlitVariant().defaultBlockState()
					   .setValue(DIRECTION, blockState.getValue(BlockStateProperties.HORIZONTAL_FACING))
					   .setValue(WATERLOGGED, Boolean.valueOf(true)), UPDATE_ALL);
			}

			world.scheduleTick(pos, fluidState.getType(), fluidState.getType().getTickDelay(world));
		}
		else
		{
			if (CanEx()) {
				world.setBlock(pos, UnlitVariant().defaultBlockState()
	    				.setValue(DIRECTION, blockState.getValue(BlockStateProperties.HORIZONTAL_FACING))
	    				.setValue(WATERLOGGED, blockState.getValue(BlockStateProperties.WATERLOGGED)), UPDATE_ALL);
			}
		}

		return success;
	}

	protected Block UnlitVariant() {
		return BlockObjectHolder.light_metal_ironage_sconce_floor_torch_iron_unlit;
	}

	protected Block EmptyVariant() {
		return BlockObjectHolder.light_metal_ironage_sconce_floor_empty_iron;
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

		if (CanEx()) {
			if (stackInHand.is(Items.WATER_BUCKET)) {

				world.setBlock(pos, UnlitVariant().defaultBlockState()
						.setValue(DIRECTION, state.getValue(BlockStateProperties.HORIZONTAL_FACING))
						.setValue(WATERLOGGED, state.getValue(BlockStateProperties.WATERLOGGED)), UPDATE_ALL);

				return InteractionResult.SUCCESS;
			}
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
					stackInHand.setCount(stackInHand.getCount()+1);
				else
				{
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
