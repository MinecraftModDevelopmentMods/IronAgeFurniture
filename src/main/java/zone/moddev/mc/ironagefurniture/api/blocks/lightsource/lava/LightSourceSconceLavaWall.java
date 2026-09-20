package zone.moddev.mc.ironagefurniture.api.blocks.lightsource.lava;

import zone.moddev.mc.ironagefurniture.api.blocks.base.FurnitureBlock;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.glow.LightSourceSconceGlowWall;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.Material;
import oshi.util.tuples.Pair;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;

import java.util.Map;
import java.util.Random;

import zone.moddev.mc.ironagefurniture.BlockObjectHolder;

import net.minecraft.core.BlockPos;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;

public class LightSourceSconceLavaWall extends LightSourceSconceGlowWall {
	@Override
	public BlockState updateShape(BlockState state, Direction direction, BlockState state2, LevelAccessor levelAccessor, BlockPos pos, BlockPos pos2) {
	    if (direction.getOpposite() == state.getValue(FurnitureBlock.DIRECTION) && !state.canSurvive(levelAccessor, pos)) {
	        levelAccessor.destroyBlock(pos, true);
	        return LightDrop().defaultBlockState().setValue(FurnitureBlock.WATERLOGGED, false);
	    }

	    return super.updateShape(state, direction, state2, levelAccessor, pos, pos2);
	}

	@Override
	public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state,
			BlockEntity blockEntity, ItemStack tool) {

		boolean isSilkTouch = false;

		if (!tool.isEmpty()) {
			Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(tool);

			if (enchantments != null && !enchantments.isEmpty())
				isSilkTouch = enchantments.get(Enchantments.SILK_TOUCH) > 0;
		}

		if (isSilkTouch && !player.isCreative())
			Block.popResource(level, pos, new ItemStack(LightDrop(), 1));

		super.playerDestroy(level, player, pos, state, blockEntity, tool);

		if (!isSilkTouch && !player.isCreative()) {
			level.playSound(player, pos, SoundEvents.GLASS_BREAK, SoundSource.BLOCKS, friction, explosionResistance);
			level.setBlock(pos, LightDrop().defaultBlockState().setValue(FurnitureBlock.WATERLOGGED, false), Block.UPDATE_ALL_IMMEDIATE, Block.UPDATE_ALL);
		}

	}

	@Override
	protected Block LightDrop() {
		return BlockObjectHolder.light_metal_ironage_block_floor_lava_clear;
	}

	@Override
	protected boolean ShouldDrop() {
		return false;
	}

	public LightSourceSconceLavaWall(float hardness, float blastResistance, SoundType sound, String name) {
		super(Block.Properties.of(Material.METAL).strength(hardness, blastResistance).sound(sound).lightLevel((p_50886_) -> 14));

		this.registerDefaultState(this.getStateDefinition().any().setValue(FurnitureBlock.DIRECTION, Direction.NORTH));
		this.generateShapes(this.getStateDefinition().getPossibleStates());
		this.setRegistryName(name);
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

		if (world instanceof ServerLevel) {
			ServerLevel serverLevel = (ServerLevel) world;
			serverLevel.getServer().tell(new TickTask(serverLevel.getServer().getTickCount() + 1, () -> {
				Block.popResource(serverLevel, pos, new ItemStack(BlockObjectHolder.obsidian_chunk, 1));
			}));
		}

		world.getLiquidTicks().scheduleTick(pos, fluidState.getType(), fluidState.getType().getTickDelay(world));

		return success;
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, Random rnd) {
		BlockPos blockpos = pos.above();

		if (level.getBlockState(blockpos).isAir() && !level.getBlockState(blockpos).isSolidRender(level, blockpos)) {
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
