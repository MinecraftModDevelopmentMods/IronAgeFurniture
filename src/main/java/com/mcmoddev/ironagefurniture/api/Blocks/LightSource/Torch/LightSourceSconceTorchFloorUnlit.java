package com.mcmoddev.ironagefurniture.api.Blocks.LightSource.Torch;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import java.util.Random;

import com.mcmoddev.ironagefurniture.BlockObjectHolder;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;

public class LightSourceSconceTorchFloorUnlit extends LightSourceSconceTorchFloor {
	public LightSourceSconceTorchFloorUnlit(float hardness, float blastResistance, SoundType sound, String name) {
		super(Block.Properties.of(Material.METAL).strength(hardness, blastResistance).sound(sound));

		this.registerDefaultState(this.getStateDefinition().any().setValue(DIRECTION, Direction.NORTH));
		this.generateShapes(this.getStateDefinition().getPossibleStates());
		this.setRegistryName(name);
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, Random random)
	{
		if (level.hasNeighborSignal(pos))
	    	  Light(state, level, pos);
    }

	@Override
	public boolean canConnectRedstone(BlockState state, BlockGetter world, BlockPos pos, Direction direction) {
		return true;
	}

	protected Block GetLitVariant() {
		return BlockObjectHolder.light_metal_ironage_sconce_floor_torch_iron;
	}

	protected Block GetEmptyVariant() {
		return BlockObjectHolder.light_metal_ironage_sconce_floor_empty_iron;
	}

	private void Light(BlockState state, Level world, BlockPos pos) {
		world.setBlock(pos, GetLitVariant().defaultBlockState()
				.setValue(DIRECTION, state.getValue(BlockStateProperties.HORIZONTAL_FACING))
				.setValue(WATERLOGGED, state.getValue(BlockStateProperties.WATERLOGGED)), UPDATE_ALL);
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
