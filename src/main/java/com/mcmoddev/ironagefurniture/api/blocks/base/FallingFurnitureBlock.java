package com.mcmoddev.ironagefurniture.api.blocks.base;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Fallable;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;

import java.util.Random;

public abstract class FallingFurnitureBlock extends FurnitureBlock implements Fallable {

	public FallingFurnitureBlock(Properties properties) {
		super(properties);
	}

	@Override
	public void onPlace(BlockState state, Level level, BlockPos pos, BlockState blockState, boolean flag) {
		level.scheduleTick(pos, this, this.getDelayAfterPlace());
	}

	@Override
	public BlockState updateShape(BlockState state, Direction direction, BlockState blockState, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos pos) {
		levelAccessor.scheduleTick(blockPos, this, this.getDelayAfterPlace());

		return super.updateShape(state, direction, blockState, levelAccessor, blockPos, pos);
	}

	@Override
	public void tick(BlockState state, ServerLevel level, BlockPos pos, Random rnd) {
		if (isFree(level.getBlockState(pos.below())) && pos.getY() >= level.getMinBuildHeight()) {
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
		Material material = state.getMaterial();

		return state.isAir() || state.is(BlockTags.FIRE) || material.isLiquid() || material.isReplaceable();
	}

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
