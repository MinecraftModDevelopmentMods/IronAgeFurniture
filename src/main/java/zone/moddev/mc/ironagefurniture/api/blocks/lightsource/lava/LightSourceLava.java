package zone.moddev.mc.ironagefurniture.api.blocks.lightsource.lava;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import zone.moddev.mc.ironagefurniture.api.blocks.base.FurnitureBlock;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.glow.LightSourceGlowdust;
import zone.moddev.mc.ironagefurniture.api.CreativeModeBreakTracker;
import zone.moddev.mc.ironagefurniture.init.ModVanillaLights;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.loot.LootParams.Builder;

public class LightSourceLava extends LightSourceGlowdust {
	@Override
	public void onLand(Level level, BlockPos pos, BlockState state, BlockState replacedState, FallingBlockEntity fallingEntity) {
		if (level.isClientSide)
			return;

		if (CreativeModeBreakTracker.shouldSuppressFallingLavaBreak(level, pos))
			return;

		if (replacedState.getFluidState().is(Fluids.WATER))
			breakIntoObsidianChunk(level, pos, state, null);
		else
			breakIntoFire(level, pos, null);
	}

	@Override
	protected void falling(FallingBlockEntity fallingEntity) {
		fallingEntity.dropItem = false;
	}

	@Override
	public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		BlockPos below = pos.below();
		if (level.getBlockState(below).is(Blocks.ICE)) {
			level.setBlockAndUpdate(below, Blocks.WATER.defaultBlockState());
			level.neighborChanged(below, Blocks.WATER, below);
		}

		super.tick(state, level, pos, random);
	}


	public LightSourceLava(float hardness, float blastResistance, SoundType sound, String name) {
		super(Block.Properties.of().strength(hardness, blastResistance).sound(sound).lightLevel((p_50886_) -> 15));

		this.registerDefaultState(this.getStateDefinition().any().setValue(DIRECTION, Direction.NORTH).setValue(WATERLOGGED, false));
		this.generateShapes(this.getStateDefinition().getPossibleStates());
		//this.setRegistryName(name);
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, Random rnd) {
		BlockPos blockpos = pos.above();
		if (level.getBlockState(blockpos).isAir() && !level.getBlockState(blockpos).isSolidRender(level, blockpos)) {
			if (rnd.nextInt(25) == 0) {

				double x = (double) pos.getX() + 0.5;
				double y = (double) pos.getY() + 0.25D;
				double z = (double) pos.getZ() + 0.5;

				level.addParticle(ParticleTypes.LAVA, x, y, z, 0.0D, 0.0D, 0.0D);
				level.playLocalSound(x, y, z, SoundEvents.LAVA_POP, SoundSource.BLOCKS, 0.2F + rnd.nextFloat() * 0.2F, 0.9F + rnd.nextFloat() * 0.15F, false);
			}

			if (rnd.nextInt(200) == 0) {
				level.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.LAVA_AMBIENT, SoundSource.BLOCKS, 0.2F + rnd.nextFloat() * 0.2F, 0.9F + rnd.nextFloat() * 0.15F, false);
			}
		}

		super.animateTick(state, level, pos, rnd);
	}

	@Override
	public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {

		boolean isSilkTouch = false;

		ItemStack tool = player.getInventory().getSelected();

		if (tool != null) {
			Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(tool);

			if (enchantments != null && !enchantments.isEmpty())
				isSilkTouch = enchantments.get(Enchantments.SILK_TOUCH) > 0;
		}

		if (isSilkTouch && !player.isCreative())
			Block.popResource(level, pos, new ItemStack(state.getBlock().asItem(), 1));

		boolean destroyed = super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);

		if (!isSilkTouch && !player.isCreative()) {
			breakIntoFire(level, pos, player);
		}

		return destroyed;
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, Builder builder) {
		// drops handled elsewhere
		List<ItemStack> drops;
		drops = new ArrayList<>();

		return drops;
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		Level level = context.getLevel();
		BlockState target = level.getBlockState(context.getClickedPos());

		if (target.getFluidState().getType() == Fluids.WATER) {
			playWaterBreakSounds(level, context.getClickedPos(), context.getPlayer());
			return ModVanillaLights.obsidian_chunk.get().defaultBlockState()
				.setValue(DIRECTION, context.getHorizontalDirection())
				.setValue(WATERLOGGED, true);
		}

		return super.getStateForPlacement(context);
	}

	@Override
	public boolean placeLiquid(LevelAccessor world, BlockPos pos, BlockState blockState, FluidState fluidState) {
		if (!blockState.getValue(BlockStateProperties.WATERLOGGED) && fluidState.getType() == Fluids.WATER) {
			if (!world.isClientSide()) {
				playWaterBreakSounds(world, pos, null);
				world.setBlock(pos, ModVanillaLights.obsidian_chunk.get().defaultBlockState().setValue(DIRECTION, blockState.getValue(BlockStateProperties.HORIZONTAL_FACING)).setValue(WATERLOGGED, true), UPDATE_ALL);
				world.scheduleTick(pos, fluidState.getType(), fluidState.getType().getTickDelay(world));
			}

			return true;
		}

		return super.placeLiquid(world, pos, blockState, fluidState);
	}

	private void breakIntoFire(Level level, BlockPos pos, Player player) {
		level.playSound(player, pos, SoundEvents.GLASS_BREAK, SoundSource.BLOCKS, 1.0F, 1.0F);
		level.setBlock(pos, Blocks.FIRE.defaultBlockState(), UPDATE_ALL);
	}

	private void breakIntoObsidianChunk(Level level, BlockPos pos, BlockState lampState, Player player) {
		playWaterBreakSounds(level, pos, player);
		level.setBlock(pos, ModVanillaLights.obsidian_chunk.get().defaultBlockState()
			.setValue(DIRECTION, lampState.getValue(DIRECTION))
			.setValue(WATERLOGGED, true), UPDATE_ALL);
	}

	private void playWaterBreakSounds(LevelAccessor level, BlockPos pos, Player player) {
		level.playSound(player, pos, SoundEvents.GLASS_BREAK, SoundSource.BLOCKS, 1.0F, 1.0F);
		level.playSound(player, pos, SoundEvents.LAVA_EXTINGUISH, SoundSource.BLOCKS, 0.5F,
			2.6F + (level.getRandom().nextFloat() - level.getRandom().nextFloat()) * 0.8F);
	}
}
