package zone.moddev.mc.ironagefurniture.api.blocks.lightsource.torch;

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

import zone.moddev.mc.ironagefurniture.BlockObjectHolder;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;

public class LightSourceSconceTorchFloorUnlit extends LightSourceSconceTorchFloor {
	public LightSourceSconceTorchFloorUnlit(float hardness, float blastResistance, SoundType sound, String name) {
		super(Block.Properties.of(Material.METAL).strength(hardness, blastResistance).sound(sound));

		this.registerDefaultState(this.defaultBlockState() .setValue(DIRECTION, Direction.NORTH));
		this.generateShapes(this.getStateDefinition().getPossibleStates());
		this.setRegistryName(name);
	}

    @Override protected boolean HasFlame() { return false; }

	protected Block GetLitVariant() {
		return BlockObjectHolder.light_metal_ironage_sconce_floor_torch_iron;
	}

	protected Block GetEmptyVariant() {
		return BlockObjectHolder.light_metal_ironage_sconce_floor_empty_iron;
	}

	private void Light(BlockState state, Level world, BlockPos pos) {
		world.setBlock(pos, GetLitVariant().defaultBlockState()
				 .setValue(DIRECTION, state.getValue(BlockStateProperties.HORIZONTAL_FACING))
				 .setValue(WATERLOGGED, state.getValue(BlockStateProperties.WATERLOGGED)).setValue(zone.moddev.mc.ironagefurniture.api.SconceMetalData.METAL, zone.moddev.mc.ironagefurniture.api.SconceMetalData.get(state)), 3);
	}

	@Override
	protected InteractionResult ActivateSconce(BlockState state, Level world, BlockPos pos, Player player,
			InteractionHand hand, BlockHitResult rayTraceResult) {

		ItemStack stackInHand = player.getItemInHand(hand);
		boolean isWaterlogged = state.getValue(BlockStateProperties.WATERLOGGED);

        if (stackInHand.getItem() == Items.WATER_BUCKET) return InteractionResult.SUCCESS;


		if (stackInHand.getItem() == Items.FLINT_AND_STEEL && !isWaterlogged) {

			if (!player.isCreative())
				stackInHand.setDamageValue(stackInHand.getDamageValue()+1);

			Light(state, world, pos);

			return InteractionResult.SUCCESS;
		}

		if (stackInHand.getItem() == Blocks.TORCH.asItem() && !isWaterlogged) {
			Light(state, world, pos);

			return InteractionResult.SUCCESS;
		}

		if (player.isCreative() && (stackInHand.getItem() == Blocks.TORCH.asItem() || stackInHand.isEmpty())) {
			world.setBlock(pos, GetEmptyVariant().defaultBlockState()
					 .setValue(DIRECTION, state.getValue(BlockStateProperties.HORIZONTAL_FACING))
					 .setValue(WATERLOGGED, state.getValue(BlockStateProperties.WATERLOGGED)).setValue(zone.moddev.mc.ironagefurniture.api.SconceMetalData.METAL, zone.moddev.mc.ironagefurniture.api.SconceMetalData.get(state)), 3);

			return InteractionResult.CONSUME;
		}

		return InteractionResult.FAIL;
	}
}
