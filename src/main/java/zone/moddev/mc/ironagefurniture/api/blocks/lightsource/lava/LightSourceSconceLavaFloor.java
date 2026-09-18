package zone.moddev.mc.ironagefurniture.api.blocks.lightsource.lava;

import zone.moddev.mc.ironagefurniture.BlockObjectHolder;
import zone.moddev.mc.ironagefurniture.api.blocks.base.FurnitureBlock;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.glow.LightSourceSconceGlowFloor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.Direction;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.world.World;
import net.minecraft.world.IWorld;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.ILiquidContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.BlockState;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.fluid.IFluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.block.material.Material;
import net.minecraft.world.storage.loot.LootContext.Builder;
import com.mojang.datafixers.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class LightSourceSconceLavaFloor extends LightSourceSconceGlowFloor implements ILiquidContainer {

	@Override
	public BlockState updateShape(BlockState state, Direction direction, BlockState state2, IWorld levelAccessor, BlockPos pos, BlockPos pos2) {
		if (direction == Direction.DOWN && !this.canSurvive(state, levelAccessor, pos)) {
	        levelAccessor.destroyBlock(pos, true);
	        return LightDrop().defaultBlockState().setValue(FurnitureBlock.WATERLOGGED, false);
		}

		return super.updateShape(state, direction, state2, levelAccessor, pos, pos2);
	}

	@Override
	protected Block LightDrop() {
		return BlockObjectHolder.light_metal_ironage_block_floor_lava_clear;
	}

	@Override
	public boolean removedByPlayer(BlockState state, World level, BlockPos pos, PlayerEntity player, boolean willHarvest,
									   IFluidState fluid) {

		boolean isSilkTouch = false;

		ItemStack tool = player.inventory.getSelected();

		if (tool != null) {
			Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(tool);

			if (enchantments != null && !enchantments.isEmpty())
				isSilkTouch = enchantments.get(Enchantments.SILK_TOUCH) > 0;
		}

		if (isSilkTouch && !player.isCreative())
			Block.popResource(level, pos, new ItemStack(LightDrop(), 1));

		boolean destroyed = super.removedByPlayer(state, level, pos, player, willHarvest, fluid);

		if (!isSilkTouch && !player.isCreative()) {
			level.playSound(player, pos, SoundEvents.GLASS_BREAK, SoundCategory.BLOCKS, 1.0F, 1.0F);
			level.setBlock(pos, Blocks.FIRE.defaultBlockState(), 3);
		}

		return destroyed;
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, Builder builder) {
		List<ItemStack> drops;
		drops = new ArrayList<>();

		Item item = EmptyVariant().asItem();
		ItemStack stack = new ItemStack(item, 1);

		drops.add(stack);

		return drops;
	}

	public LightSourceSconceLavaFloor(float hardness, float blastResistance, SoundType sound, String name) {
		super(Block.Properties.of(Material.METAL).strength(hardness, blastResistance).sound(sound).lightLevel(14));

		this.registerDefaultState(this.getStateDefinition().any().setValue(FurnitureBlock.DIRECTION, Direction.NORTH));
		this.generateShapes(this.getStateDefinition().getPossibleStates());
		this.setRegistryName(name);
	}

	@Override
	public void animateTick(BlockState state, World level, BlockPos pos, Random rnd) {
		BlockPos blockpos = pos.above();
		if (level.getBlockState(blockpos).isAir() && !level.getBlockState(blockpos).isSolidRender(level, blockpos)) {
			if (rnd.nextInt(25) == 0) {
				Direction direction = state.getValue(FurnitureBlock.DIRECTION);

				Pair<Double, Double> rotated = FurnitureBlock.rotate(0.6D, 0.5D, state.getValue(FurnitureBlock.DIRECTION));

				double x = (double) pos.getX() + rotated.getFirst();
				double y = (double) pos.getY() + 0.5D;
				double z = (double) pos.getZ() + rotated.getSecond();

				Direction direction1 = direction.getOpposite();

				level.addParticle(ParticleTypes.LAVA, x + 0.27D * (double) direction1.getStepX(), y + 0.22D, z + 0.27D * (double) direction1.getStepZ(), 0.0D, 0.0D, 0.0D);
				level.playLocalSound(x, y, z, SoundEvents.LAVA_POP, SoundCategory.BLOCKS, 0.2F + rnd.nextFloat() * 0.2F, 0.9F + rnd.nextFloat() * 0.15F, false);
			}

			if (rnd.nextInt(200) == 0)
				level.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.LAVA_AMBIENT, SoundCategory.BLOCKS, 0.2F + rnd.nextFloat() * 0.2F, 0.9F + rnd.nextFloat() * 0.15F, false);
		}

		super.animateTick(state, level, pos, rnd);
	}

	@Override
	public boolean placeLiquid(IWorld world, BlockPos pos, BlockState blockState, IFluidState fluidState) {
		boolean success = super.placeLiquid(world, pos, blockState, fluidState);

		if (!blockState.getValue(BlockStateProperties.WATERLOGGED) && fluidState.getType() == Fluids.WATER) {
			if (!world.isClientSide()) {

				world.setBlock(pos, EmptyVariant().defaultBlockState()
					.setValue(FurnitureBlock.DIRECTION, blockState.getValue(BlockStateProperties.HORIZONTAL_FACING))
					.setValue(FurnitureBlock.WATERLOGGED, Boolean.valueOf(true)), 3);

				world.playSound(null, pos, SoundEvents.GLASS_BREAK, SoundCategory.BLOCKS, 1.0F, 1.0F);
				world.playSound(null, pos, SoundEvents.LAVA_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 2.6F);

				Block.dropResources(blockState, (World) world, pos);
				world.setBlock(pos.below(), BlockObjectHolder.obsidian_chunk.defaultBlockState(), 3);

				world.getLiquidTicks().scheduleTick(pos, fluidState.getType(), fluidState.getType().getTickDelay(world));
			}
		} else {
			world.setBlock(pos, EmptyVariant().defaultBlockState()
				.setValue(FurnitureBlock.DIRECTION, blockState.getValue(BlockStateProperties.HORIZONTAL_FACING))
				.setValue(FurnitureBlock.WATERLOGGED, blockState.getValue(BlockStateProperties.WATERLOGGED)), 3);

		}

		return success;
	}
}
