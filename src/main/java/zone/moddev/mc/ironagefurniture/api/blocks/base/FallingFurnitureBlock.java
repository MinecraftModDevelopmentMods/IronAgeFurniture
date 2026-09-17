package zone.moddev.mc.ironagefurniture.api.blocks.base;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Fallable;
import net.minecraft.world.level.block.state.BlockState;

public abstract class FallingFurnitureBlock extends FurnitureBlock implements Fallable {

	public FallingFurnitureBlock(Properties properties) {
		super(properties);
	}

	@Override
	public void onPlace(BlockState state, Level level, BlockPos pos, BlockState blockState, boolean flag) {
		level.scheduleTick(pos, this, this.getDelayAfterPlace());
	}

	@Override
	protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks,
			BlockPos blockPos, Direction direction, BlockPos neighborPos, BlockState neighborState,
			RandomSource random) {
		ticks.scheduleTick(blockPos, this, this.getDelayAfterPlace());

		return super.updateShape(state, level, ticks, blockPos, direction, neighborPos, neighborState, random);
	}

	@Override
	public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource rnd) {
		if (isFree(level.getBlockState(pos.below())) && pos.getY() >= level.getMinY()) {
			FallingBlockEntity fallingblockentity = FallingBlockEntity.fall(level, pos, state);
			this.falling(fallingblockentity);
		}
	}

	protected void falling(FallingBlockEntity fallingBlockEntity) {

	}

	protected int getDelayAfterPlace() {
		return 2;
	}

	public static boolean isFree(BlockState state) {
		return state.isAir() || state.is(BlockTags.FIRE) || state.liquid() || state.canBeReplaced();
	}

	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource rnd) {
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
