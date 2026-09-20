package zone.moddev.mc.ironagefurniture.api.blocks.lightsource.lava;

import zone.moddev.mc.ironagefurniture.api.blocks.base.FurnitureBlock;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.glow.LightSourceSconceGlowWall;
import zone.moddev.mc.ironagefurniture.init.ModVanillaLights;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.FluidState;
import oshi.util.tuples.Pair;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.TickTask;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluids;

public class LightSourceSconceLavaWall extends LightSourceSconceGlowWall {
	@Override
	protected BlockState updateShape(BlockState state, LevelReader levelReader, ScheduledTickAccess ticks, BlockPos pos,
			Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
	    if (direction.getOpposite() == state.getValue(FurnitureBlock.DIRECTION) && !state.canSurvive(levelReader, pos)
			&& levelReader instanceof LevelAccessor levelAccessor) {
	        levelAccessor.destroyBlock(pos, true);
	        return LightDrop().defaultBlockState().setValue(FurnitureBlock.WATERLOGGED, false);
	    }

	    return super.updateShape(state, levelReader, ticks, pos, direction, neighborPos, neighborState, random);
	}
	
	@Override
	public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest,
									   FluidState fluid) {

		boolean isSilkTouch = false;

		ItemStack tool = player.getInventory().getSelectedItem();

		if (tool != null) {
			isSilkTouch = EnchantmentHelper.getItemEnchantmentLevel(level.registryAccess()
					.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.SILK_TOUCH), tool) > 0;
		}

		if (isSilkTouch && !player.isCreative())
			Block.popResource(level, pos, new ItemStack(LightDrop(), 1));

		boolean destroyed = super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);

		if (!isSilkTouch && !player.isCreative()) {
			level.playSound(player, pos, SoundEvents.GLASS_BREAK, SoundSource.BLOCKS, friction, explosionResistance);
			level.setBlock(pos, LightDrop().defaultBlockState().setValue(FurnitureBlock.WATERLOGGED, false), Block.UPDATE_ALL_IMMEDIATE, Block.UPDATE_ALL);
		}

		return destroyed;
	}

	@Override
	protected Block LightDrop() {
		return ModVanillaLights.light_metal_ironage_block_floor_lava_clear.get();
	}

	@Override
	protected boolean ShouldDrop() {
		return false;
	}
	
	public LightSourceSconceLavaWall(float hardness, float blastResistance, SoundType sound, String name) {
		super(zone.moddev.mc.ironagefurniture.init.RegistrationProperties.block(Block.Properties.of().strength(hardness, blastResistance).sound(sound).lightLevel((p_50886_) -> 14), name));

		this.registerDefaultState(this.getStateDefinition().any().setValue(FurnitureBlock.DIRECTION, Direction.NORTH));
		this.generateShapes(this.getStateDefinition().getPossibleStates());
	}

	@Override
	public boolean placeLiquid(LevelAccessor world, BlockPos pos, BlockState blockState, FluidState fluidState) {
		boolean success = super.placeLiquid(world, pos, blockState, fluidState);

		if (!success || blockState.getValue(BlockStateProperties.WATERLOGGED)
				|| fluidState.getType() != Fluids.WATER || world.isClientSide()) {
			return success;
		}

		world.setBlock(pos, EmptyVariant().defaultBlockState()
			.setValue(FurnitureBlock.DIRECTION, blockState.getValue(BlockStateProperties.HORIZONTAL_FACING))
			.setValue(FurnitureBlock.WATERLOGGED, true), Block.UPDATE_ALL);

		world.playSound(null, pos, SoundEvents.GLASS_BREAK, SoundSource.BLOCKS, friction, explosionResistance);
		world.playSound(null, pos, SoundEvents.LAVA_EXTINGUISH, SoundSource.BLOCKS, friction, explosionResistance);

		if (world instanceof ServerLevel serverLevel) {
			serverLevel.getServer().schedule(new TickTask(serverLevel.getServer().getTickCount() + 1, () -> {
				Block.popResource(serverLevel, pos, new ItemStack(ModVanillaLights.obsidian_chunk.get(), 1));
			}));
		}

		world.scheduleTick(pos, fluidState.getType(), fluidState.getType().getTickDelay(world));

		return success;
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource rnd) {
		BlockPos blockpos = pos.above();

		if (level.getBlockState(blockpos).isAir() && !level.getBlockState(blockpos).isSolidRender()) {
			if (rnd.nextInt(25) == 0) {
				Direction direction = state.getValue(FurnitureBlock.DIRECTION);

				Pair<Double, Double> rotated = FurnitureBlock.rotate(0.6D, 0.5D, state.getValue(FurnitureBlock.DIRECTION));

				double x = (double) pos.getX() + rotated.getA();
				double y = (double) pos.getY() + 0.5D;
				double z = (double) pos.getZ() + rotated.getB();

				Direction direction1 = direction.getOpposite();

				level.addParticle(ParticleTypes.LAVA, x + 0.27D * (double) direction1.getStepX(), y + 0.22D, z + 0.27D * (double) direction1.getStepZ(), 0.0D, 0.0D, 0.0D);
				level.playLocalSound(x, y, z, SoundEvents.LAVA_POP, SoundSource.BLOCKS, 0.2F + rnd.nextFloat() * 0.2F, 0.9F + rnd.nextFloat() * 0.15F, false);
			}

			if (rnd.nextInt(200) == 0)
				level.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.LAVA_AMBIENT, SoundSource.BLOCKS, 0.2F + rnd.nextFloat() * 0.2F, 0.9F + rnd.nextFloat() * 0.15F, false);
		}

		super.animateTick(state, level, pos, rnd);
	}
}
