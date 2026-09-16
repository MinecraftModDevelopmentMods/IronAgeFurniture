package zone.moddev.mc.ironagefurniture.api.blocks.lightsource.torch;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;

import java.util.Random;

import zone.moddev.mc.ironagefurniture.init.ModVanillaLights;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class LightSourceSconceTorchWallUnlit extends LightSourceSconceTorchWall {
	public LightSourceSconceTorchWallUnlit(float hardness, float blastResistance, SoundType sound, String name) {
		super(zone.moddev.mc.ironagefurniture.init.RegistrationProperties.block(Block.Properties.of().strength(hardness, blastResistance).sound(sound), name));

		this.registerDefaultState(this.getStateDefinition().any().setValue(DIRECTION, Direction.NORTH));
		this.generateShapes(this.getStateDefinition().getPossibleStates());
		//this.setRegistryName(name);
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
		if (level.hasNeighborSignal(pos))
			Light(state, level, pos);
	}

	@Override
	protected boolean shouldRedstoneWireConnectTo(BlockState state, BlockGetter world, BlockPos pos,
			@Nullable Direction direction) {
		return true;
	}

	private void Light(BlockState state, Level world, BlockPos pos) {
		world.setBlock(pos, ModVanillaLights.light_metal_ironage_sconce_wall_torch_iron.get().defaultBlockState()
			.setValue(DIRECTION, state.getValue(BlockStateProperties.HORIZONTAL_FACING))
			.setValue(WATERLOGGED, state.getValue(BlockStateProperties.WATERLOGGED)), UPDATE_ALL);
	}

	protected Block GetEmptyVariant() {
		return ModVanillaLights.light_metal_ironage_sconce_wall_empty_iron.get();
	}

	@Override
	protected InteractionResult ActivateSconce(BlockState state, Level world, BlockPos pos, Player player,
											   InteractionHand hand, BlockHitResult rayTraceResult) {

		ItemStack stackInHand = player.getItemInHand(hand);
		boolean isWaterlogged = state.getValue(BlockStateProperties.WATERLOGGED);

		if (stackInHand.is(Items.FLINT_AND_STEEL) && !isWaterlogged) {

			if (!player.isCreative())
				stackInHand.setDamageValue(stackInHand.getDamageValue() + 1);

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

			return InteractionResult.CONSUME;
		}

		return InteractionResult.FAIL;
	}
}
