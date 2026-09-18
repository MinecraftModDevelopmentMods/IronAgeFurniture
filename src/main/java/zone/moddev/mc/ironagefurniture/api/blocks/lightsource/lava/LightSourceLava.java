package zone.moddev.mc.ironagefurniture.api.blocks.lightsource.lava;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import zone.moddev.mc.ironagefurniture.BlockObjectHolder;
import zone.moddev.mc.ironagefurniture.api.CreativeModeBreakTracker;
import zone.moddev.mc.ironagefurniture.api.blocks.base.FurnitureBlock;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.glow.LightSourceGlowdust;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.Direction;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.entity.item.FallingBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.IWorld;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.BlockState;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.fluid.IFluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.block.material.Material;
import net.minecraft.world.storage.loot.LootContext.Builder;

public class LightSourceLava extends LightSourceGlowdust {
	@Override
	public void onLand(World level, BlockPos pos, BlockState state, BlockState replacedState) {
		if (level.isClientSide || CreativeModeBreakTracker.shouldSuppressFallingLavaBreak(level, pos))
			return;

		if (replacedState.getFluidState().getType() == Fluids.WATER)
			breakIntoObsidianChunk(level, pos, state, null);
		else
			breakIntoFire(level, pos, null);
	}

	@Override
	protected void falling(FallingBlockEntity fallingEntity) {
		fallingEntity.dropItem = false;
	}

	@Override
	public void tick(BlockState state, ServerWorld level, BlockPos pos, Random random) {
		BlockPos below = pos.below();
		if (level.getBlockState(below).getBlock() == Blocks.ICE) {
			level.setBlockAndUpdate(below, Blocks.WATER.defaultBlockState());
			level.neighborChanged(below, Blocks.WATER, below);
		}
		super.tick(state, level, pos, random);
	}


	public LightSourceLava(float hardness, float blastResistance, SoundType sound, String name) {
		super(Block.Properties.of(Material.METAL).strength(hardness, blastResistance).sound(sound).lightLevel(15));

		this.registerDefaultState(this.getStateDefinition().any().setValue(DIRECTION, Direction.NORTH).setValue(WATERLOGGED, false));
		this.generateShapes(this.getStateDefinition().getPossibleStates());
		this.setRegistryName(name);
	}

	@Override
	public void animateTick(BlockState state, World level, BlockPos pos, Random rnd) {
		BlockPos blockpos = pos.above();
		if (level.getBlockState(blockpos).isAir() && !level.getBlockState(blockpos).isSolidRender(level, blockpos)) {
			if (rnd.nextInt(25) == 0) {

				double x = (double) pos.getX() + 0.5;
				double y = (double) pos.getY() + 0.25D;
				double z = (double) pos.getZ() + 0.5;

				level.addParticle(ParticleTypes.LAVA, x, y, z, 0.0D, 0.0D, 0.0D);
				level.playLocalSound(x, y, z, SoundEvents.LAVA_POP, SoundCategory.BLOCKS, 0.2F + rnd.nextFloat() * 0.2F, 0.9F + rnd.nextFloat() * 0.15F, false);
			}

			if (rnd.nextInt(200) == 0) {
				level.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.LAVA_AMBIENT, SoundCategory.BLOCKS, 0.2F + rnd.nextFloat() * 0.2F, 0.9F + rnd.nextFloat() * 0.15F, false);
			}
		}

		super.animateTick(state, level, pos, rnd);
	}

	@Override
	public boolean removedByPlayer(BlockState state, World level, BlockPos pos, PlayerEntity player, boolean willHarvest, IFluidState fluid) {

		boolean isSilkTouch = false;

		ItemStack tool = player.inventory.getSelected();

		if (tool != null) {
			Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(tool);

			if (enchantments != null && !enchantments.isEmpty())
				isSilkTouch = enchantments.get(Enchantments.SILK_TOUCH) > 0;
		}

		if (isSilkTouch && !player.isCreative())
			Block.popResource(level, pos, new ItemStack(state.getBlock().asItem(), 1));

		boolean destroyed = super.removedByPlayer(state, level, pos, player, willHarvest, fluid);

		if (!isSilkTouch && !player.isCreative())
			breakIntoFire(level, pos, player);

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
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		World level = context.getLevel();
		BlockState target = level.getBlockState(context.getClickedPos());

		if (target.getFluidState().getType() == Fluids.WATER) {
			playWaterBreakSounds(level, context.getClickedPos(), context.getPlayer());
			return BlockObjectHolder.obsidian_chunk.defaultBlockState()
					.setValue(DIRECTION, context.getHorizontalDirection())
					.setValue(WATERLOGGED, true);
		}

		return super.getStateForPlacement(context);
	}

	@Override
	public boolean placeLiquid(IWorld world, BlockPos pos, BlockState blockState, IFluidState fluidState) {
		if (!blockState.getValue(BlockStateProperties.WATERLOGGED) && fluidState.getType() == Fluids.WATER) {
			if (!world.isClientSide()) {
				playWaterBreakSounds(world, pos, null);
				world.setBlock(pos, BlockObjectHolder.obsidian_chunk.defaultBlockState()
						.setValue(DIRECTION, blockState.getValue(BlockStateProperties.HORIZONTAL_FACING))
						.setValue(WATERLOGGED, true), 3);
				world.getLiquidTicks().scheduleTick(pos, fluidState.getType(), fluidState.getType().getTickDelay(world));
			}
			return true;
		}

		return super.placeLiquid(world, pos, blockState, fluidState);
	}

	private void breakIntoFire(World level, BlockPos pos, PlayerEntity player) {
		level.playSound(player, pos, SoundEvents.GLASS_BREAK, SoundCategory.BLOCKS, 1.0F, 1.0F);
		level.setBlock(pos, Blocks.FIRE.defaultBlockState(), 3);
	}

	private void breakIntoObsidianChunk(World level, BlockPos pos, BlockState lampState, PlayerEntity player) {
		playWaterBreakSounds(level, pos, player);
		level.setBlock(pos, BlockObjectHolder.obsidian_chunk.defaultBlockState()
				.setValue(DIRECTION, lampState.getValue(DIRECTION))
				.setValue(WATERLOGGED, true), 3);
	}

	private void playWaterBreakSounds(IWorld level, BlockPos pos, PlayerEntity player) {
		level.playSound(player, pos, SoundEvents.GLASS_BREAK, SoundCategory.BLOCKS, 1.0F, 1.0F);
		level.playSound(player, pos, SoundEvents.LAVA_EXTINGUISH, SoundCategory.BLOCKS, 0.5F,
				2.6F + (level.getRandom().nextFloat() - level.getRandom().nextFloat()) * 0.8F);
	}
}
