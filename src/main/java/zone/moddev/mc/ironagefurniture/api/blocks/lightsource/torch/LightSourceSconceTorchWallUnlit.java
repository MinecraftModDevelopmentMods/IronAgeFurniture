package zone.moddev.mc.ironagefurniture.api.blocks.lightsource.torch;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;

import java.util.Random;

import zone.moddev.mc.ironagefurniture.BlockObjectHolder;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class LightSourceSconceTorchWallUnlit extends LightSourceSconceTorchWall {
	public LightSourceSconceTorchWallUnlit(float hardness, float blastResistance, SoundType sound, String name) {
		super(Block.Properties.of(Material.METAL).strength(hardness, blastResistance).sound(sound));

		this.registerDefaultState(this.getStateDefinition().any().setValue(DIRECTION, Direction.NORTH));
		this.generateShapes(this.getStateDefinition().getPossibleStates());
		this.setRegistryName(name);
	}

	@Override
	public void animateTick(BlockState state, World level, BlockPos pos, Random random) {
		if (level.hasNeighborSignal(pos))
			Light(state, level, pos);
	}

	@Override
	public boolean canConnectRedstone(BlockState state, IBlockReader world, BlockPos pos, Direction direction) {
		return true;
	}

	private void Light(BlockState state, World world, BlockPos pos) {
		world.setBlock(pos, BlockObjectHolder.light_metal_ironage_sconce_wall_torch_iron.defaultBlockState()
			.setValue(DIRECTION, state.getValue(BlockStateProperties.HORIZONTAL_FACING))
			.setValue(WATERLOGGED, state.getValue(BlockStateProperties.WATERLOGGED)), 3);
	}

	protected Block GetEmptyVariant() {
		return BlockObjectHolder.light_metal_ironage_sconce_wall_empty_iron;
	}

	@Override
	protected ActionResultType ActivateSconce(BlockState state, World world, BlockPos pos, PlayerEntity player,
											   Hand hand, BlockRayTraceResult rayTraceResult) {

		ItemStack stackInHand = player.getItemInHand(hand);
		boolean isWaterlogged = state.getValue(BlockStateProperties.WATERLOGGED);

		if (stackInHand.getItem() == Items.FLINT_AND_STEEL && !isWaterlogged) {

			if (!player.isCreative())
				stackInHand.setDamageValue(stackInHand.getDamageValue() + 1);

			Light(state, world, pos);

			return ActionResultType.SUCCESS;
		}

		if (stackInHand.getItem() == Blocks.TORCH.asItem() && !isWaterlogged) {
			Light(state, world, pos);

			return ActionResultType.SUCCESS;
		}

		if (player.isCreative() && (stackInHand.getItem() == Blocks.TORCH.asItem() || stackInHand.isEmpty())) {
			world.setBlock(pos, GetEmptyVariant().defaultBlockState()
				.setValue(DIRECTION, state.getValue(BlockStateProperties.HORIZONTAL_FACING))
				.setValue(WATERLOGGED, state.getValue(BlockStateProperties.WATERLOGGED)), 3);

			return ActionResultType.CONSUME;
		}

		return ActionResultType.FAIL;
	}
}
