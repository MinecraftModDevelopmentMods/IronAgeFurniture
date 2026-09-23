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
	public void onEndFalling(World level, BlockPos pos, BlockState state, BlockState replacedState) {
		if (level.isRemote || CreativeModeBreakTracker.shouldSuppressFallingLavaBreak(level, pos))
			return;

		if (replacedState.getFluidState().getFluid() == Fluids.WATER)
			breakIntoObsidianChunk(level, pos, state, null);
		else
			breakIntoFire(level, pos, null);
	}

	@Override
	protected void onStartFalling(FallingBlockEntity fallingEntity) {
		fallingEntity.shouldDropItem = false;
	}

	@Override
	public void tick(BlockState state, World level, BlockPos pos, Random random) {
		BlockPos below = pos.down();
		if (level.getBlockState(below).getBlock() == Blocks.ICE) {
			level.setBlockState(below, Blocks.WATER.getDefaultState());
			level.neighborChanged(below, Blocks.WATER, below);
		}
		super.tick(state, level, pos, random);
	}


	public LightSourceLava(float hardness, float blastResistance, SoundType sound, String name) {
		super(Block.Properties.create(Material.IRON).hardnessAndResistance(hardness, blastResistance).sound(sound).lightValue(15));

		this.setDefaultState(this.getStateContainer().getBaseState() .with(DIRECTION, Direction.NORTH) .with(WATERLOGGED, false));
		this.generateShapes(this.getStateContainer().getValidStates());
		this.setRegistryName(name);
	}

	@Override
	public void animateTick(BlockState state, World level, BlockPos pos, Random rnd) {
		BlockPos blockpos = pos.up();
		if (level.getBlockState(blockpos).isAir() && !level.getBlockState(blockpos).isSolid()) {
			if (rnd.nextInt(25) == 0) {

				double x = (double) pos.getX() + 0.5;
				double y = (double) pos.getY() + 0.25D;
				double z = (double) pos.getZ() + 0.5;

				level.addParticle(ParticleTypes.LAVA, x, y, z, 0.0D, 0.0D, 0.0D);
				level.playSound(x, y, z, SoundEvents.BLOCK_LAVA_POP, SoundCategory.BLOCKS, 0.2F + rnd.nextFloat() * 0.2F, 0.9F + rnd.nextFloat() * 0.15F, false);
			}

			if (rnd.nextInt(200) == 0) {
				level.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_LAVA_AMBIENT, SoundCategory.BLOCKS, 0.2F + rnd.nextFloat() * 0.2F, 0.9F + rnd.nextFloat() * 0.15F, false);
			}
		}

		super.animateTick(state, level, pos, rnd);
	}

	@Override
	public boolean removedByPlayer(BlockState state, World level, BlockPos pos, PlayerEntity player, boolean willHarvest, IFluidState fluid) {

		boolean isSilkTouch = false;

		ItemStack tool = player.inventory.getCurrentItem();

		if (tool != null) {
			Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(tool);

			if (enchantments != null && !enchantments.isEmpty())
				isSilkTouch = enchantments.get(Enchantments.SILK_TOUCH) > 0;
		}

		if (isSilkTouch && !player.isCreative())
			Block.spawnAsEntity(level, pos, new ItemStack(state.getBlock().asItem(), 1));

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
		World level = context.getWorld();
		BlockState target = level.getBlockState(context.getPos());

		if (target.getFluidState().getFluid() == Fluids.WATER) {
			playWaterBreakSounds(level, context.getPos(), context.getPlayer());
			return BlockObjectHolder.obsidian_chunk.getDefaultState()
					 .with(DIRECTION, context.getPlacementHorizontalFacing())
					 .with(WATERLOGGED, true);
		}

		return super.getStateForPlacement(context);
	}

	@Override
	public boolean receiveFluid(IWorld world, BlockPos pos, BlockState blockState, IFluidState fluidState) {
		if (!blockState .get(BlockStateProperties.WATERLOGGED) && fluidState.getFluid() == Fluids.WATER) {
			if (!world.isRemote()) {
				playWaterBreakSounds(world, pos, null);
				world.setBlockState(pos, BlockObjectHolder.obsidian_chunk.getDefaultState()
						 .with(DIRECTION, blockState .get(BlockStateProperties.HORIZONTAL_FACING))
						 .with(WATERLOGGED, true), 3);
				world.getPendingFluidTicks().scheduleTick(pos, fluidState.getFluid(), fluidState.getFluid().getTickRate(world));
			}
			return true;
		}

		return super.receiveFluid(world, pos, blockState, fluidState);
	}

	private void breakIntoFire(World level, BlockPos pos, PlayerEntity player) {
		level.playSound(player, pos, SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.BLOCKS, 1.0F, 1.0F);
		level.setBlockState(pos, Blocks.FIRE.getDefaultState(), 3);
	}

	private void breakIntoObsidianChunk(World level, BlockPos pos, BlockState lampState, PlayerEntity player) {
		playWaterBreakSounds(level, pos, player);
		level.setBlockState(pos, BlockObjectHolder.obsidian_chunk.getDefaultState()
				 .with(DIRECTION, lampState .get(DIRECTION))
				 .with(WATERLOGGED, true), 3);
	}

	private void playWaterBreakSounds(IWorld level, BlockPos pos, PlayerEntity player) {
		level.playSound(player, pos, SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.BLOCKS, 1.0F, 1.0F);
		level.playSound(player, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F,
				2.6F + (level.getRandom().nextFloat() - level.getRandom().nextFloat()) * 0.8F);
	}
}
