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
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import java.util.Random;

import zone.moddev.mc.ironagefurniture.BlockObjectHolder;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class LightSourceSconceTorchFloorUnlit extends LightSourceSconceTorchFloor {
	public LightSourceSconceTorchFloorUnlit(float hardness, float blastResistance, SoundType sound, String name) {
		super(Block.Properties.create(Material.IRON).hardnessAndResistance(hardness, blastResistance).sound(sound));

		this.setDefaultState(this.getStateContainer().getBaseState() .with(DIRECTION, Direction.NORTH));
		this.generateShapes(this.getStateContainer().getValidStates());
		this.setRegistryName(name);
	}

	@Override
	public void animateTick(BlockState state, World level, BlockPos pos, Random random)
	{
		if (level.isBlockPowered(pos))
				  Light(state, level, pos);
    }

	protected Block GetLitVariant() {
		return BlockObjectHolder.light_metal_ironage_sconce_floor_torch_iron;
	}

	protected Block GetEmptyVariant() {
		return BlockObjectHolder.light_metal_ironage_sconce_floor_empty_iron;
	}

	private void Light(BlockState state, World world, BlockPos pos) {
		world.setBlockState(pos, GetLitVariant().getDefaultState()
				 .with(DIRECTION, state .get(BlockStateProperties.HORIZONTAL_FACING))
				 .with(WATERLOGGED, state .get(BlockStateProperties.WATERLOGGED)), 3);
	}

	@Override
	protected boolean ActivateSconce(BlockState state, World world, BlockPos pos, PlayerEntity player,
			Hand hand, BlockRayTraceResult rayTraceResult) {

		ItemStack stackInHand = player.getHeldItem(hand);
		boolean isWaterlogged = state .get(BlockStateProperties.WATERLOGGED);


		if (stackInHand.getItem() == Items.FLINT_AND_STEEL && !isWaterlogged) {

			if (!player.isCreative())
				stackInHand.setDamage(stackInHand.getDamage()+1);

			Light(state, world, pos);

			return true;
		}

		if (stackInHand.getItem() == Blocks.TORCH.asItem() && !isWaterlogged) {
			Light(state, world, pos);

			return true;
		}

		if (player.isCreative() && (stackInHand.getItem() == Blocks.TORCH.asItem() || stackInHand.isEmpty())) {
			world.setBlockState(pos, GetEmptyVariant().getDefaultState()
					 .with(DIRECTION, state .get(BlockStateProperties.HORIZONTAL_FACING))
					 .with(WATERLOGGED, state .get(BlockStateProperties.WATERLOGGED)), 3);

			return true;
		}

		return false;
	}
}
