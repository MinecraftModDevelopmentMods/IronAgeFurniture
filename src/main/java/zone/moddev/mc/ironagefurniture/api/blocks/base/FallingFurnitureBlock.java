package zone.moddev.mc.ironagefurniture.api.blocks.base;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import java.util.Random;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Fallable;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;

public abstract class FallingFurnitureBlock extends FurnitureBlock implements Fallable {

	public FallingFurnitureBlock(Properties properties) {
		super(properties);
	}

	@Override
	public void onPlace(BlockState state, Level level, BlockPos pos, BlockState blockState, boolean flag) {
		zone.moddev.mc.ironagefurniture.api.NativeTicks.schedule(level, pos, this, this.getDelayAfterPlace());
	}

	@Override
	public BlockState updateShape(BlockState state, Direction direction, BlockState blockState, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos pos) {
		zone.moddev.mc.ironagefurniture.api.NativeTicks.schedule(levelAccessor, blockPos, this, this.getDelayAfterPlace());

		return super.updateShape(state, direction, blockState, levelAccessor, blockPos, pos);
	}

	@Override
	public void tick(BlockState state, ServerLevel level, BlockPos pos, Random rnd) {
		if (isFree(level.getBlockState(pos.below())) && pos.getY() >= level.getMinBuildHeight()) {
			FallingBlockEntity fallingblockentity = new FallingBlockEntity(level, pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, state);
			this.falling(fallingblockentity);
			level.addFreshEntity(fallingblockentity);
		}
	}

	protected void falling(FallingBlockEntity fallingBlockEntity) {

	}

	@Override
	public void onLand(Level level, BlockPos pos, BlockState state, BlockState replacedState,
			FallingBlockEntity fallingEntity) {
		if (!level.isClientSide && state.hasProperty(WATERLOGGED)
				&& replacedState.getFluidState().is(FluidTags.WATER)) {
			level.setBlock(pos, state.setValue(WATERLOGGED, true), Block.UPDATE_ALL);
			zone.moddev.mc.ironagefurniture.api.NativeTicks.schedule(level, pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
		}
	}

	protected int getDelayAfterPlace() {
		return 2;
	}

	public static boolean isFree(BlockState state) {
		return state.isAir() || state.is(BlockTags.FIRE) || state.getMaterial().isLiquid() || state.getMaterial().isReplaceable();
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, Random rnd) {
		if (rnd.nextInt(16) == 0) {
			BlockPos blockpos = pos.below();
			if (isFree(level.getBlockState(blockpos))) {
				double d0 = (double) pos.getX() + rnd.nextDouble();
				double d1 = (double) pos.getY() - 0.05D;
				double d2 = (double) pos.getZ() + rnd.nextDouble();
				level.addParticle(new BlockParticleOption(ParticleTypes.FALLING_DUST, state), d0, d1, d2, 0.0D, 0.0D, 0.0D);
			}
		}
	}
}
