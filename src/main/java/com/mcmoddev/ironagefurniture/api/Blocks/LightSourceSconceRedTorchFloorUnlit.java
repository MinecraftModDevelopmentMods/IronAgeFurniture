package com.mcmoddev.ironagefurniture.api.Blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;

import java.util.List;
import java.util.Random;

import com.mcmoddev.ironagefurniture.BlockObjectHolder;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;

public class LightSourceSconceRedTorchFloorUnlit extends LightSourceSconceRedTorchFloor {

	public LightSourceSconceRedTorchFloorUnlit(Properties properties) {
		super(properties);

		this.registerDefaultState(
				this.getStateDefinition().any().setValue(DIRECTION, Direction.NORTH).setValue(WATERLOGGED, false));
		this.generateShapes(this.getStateDefinition().getPossibleStates());
		this.flameParticle = DustParticleOptions.REDSTONE;
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, Random rand) {
		//
	}

	@Override
	public int getSignal(BlockState state, BlockGetter getter, BlockPos pos, Direction direction) {
		return 0;
	}

	protected Block GetLitVariant() {
		return BlockObjectHolder.light_metal_ironage_sconce_floor_redtorch_iron;
	}

	protected Block GetEmptyVariant() {
		return BlockObjectHolder.light_metal_ironage_sconce_floor_empty_iron;
	}

	@Override
	public void tick(BlockState state, ServerLevel level, BlockPos pos, Random rnd) {
		List<LightSourceSconceRedTorchWallUnlit.Toggle> list = RECENT_TOGGLES.get(level);

		while (list != null && !list.isEmpty() && level.getGameTime() - (list.get(0)).when > 60L) {
			list.remove(0);
		}

		level.setBlock(pos,
				BlockObjectHolder.light_metal_ironage_sconce_wall_redtorch_iron.defaultBlockState()
						.setValue(DIRECTION, state.getValue(BlockStateProperties.HORIZONTAL_FACING))
						.setValue(WATERLOGGED, state.getValue(BlockStateProperties.WATERLOGGED)),
				UPDATE_ALL);

	}

	@Override
	public boolean isSignalSource(BlockState state) {
		return true;
	}

	@Override
	public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos blockPos,
								boolean flag) {
		if (!this.hasNeighborSignal(level, pos, state) && !level.getBlockTicks().willTickThisTick(pos, this)) {
			level.scheduleTick(pos, this, 2);
		}
	}

	public LightSourceSconceRedTorchFloorUnlit(float hardness, float blastResistance, SoundType sound, String name) {
		super(Block.Properties.of(Material.METAL).strength(hardness, blastResistance).sound(sound)
				.lightLevel((p_50886_) -> {
					return 0;
				}));

		this.registerDefaultState(this.getStateDefinition().any().setValue(DIRECTION, Direction.NORTH));
		this.generateShapes(this.getStateDefinition().getPossibleStates());
		this.setRegistryName(name);
		this.flameParticle = ParticleTypes.FLAME;
	}
}
