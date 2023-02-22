package com.mcmoddev.ironagefurniture.api.Blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.WeakHashMap;

import com.google.common.collect.Lists;
import com.mcmoddev.ironagefurniture.BlockObjectHolder;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;

public class LightSourceSconceRedTorchFloor extends LightSourceSconceTorchFloor implements LiquidBlockContainer {
	protected static final Map<BlockGetter, List<LightSourceSconceRedTorchWall.Toggle>> RECENT_TOGGLES = new WeakHashMap<>();
	public static final int RECENT_TOGGLE_TIMER = 60;
	public static final int MAX_RECENT_TOGGLES = 8;
	public static final int RESTART_DELAY = 160;
	
	protected Block GetRedVariant() {
		return BlockObjectHolder.light_metal_ironage_sconce_floor_redtorch_iron;
	}

	@Override
	protected boolean CanEx() {
		return false;
	}

	@Override
	protected Block LightDrop() {
		return Blocks.REDSTONE_TORCH;
	}
	
	public LightSourceSconceRedTorchFloor(Properties properties) {
		super(properties);
		
		this.registerDefaultState(this.getStateDefinition().any().setValue(DIRECTION, Direction.NORTH).setValue(WATERLOGGED, false));
        this.generateShapes(this.getStateDefinition().getPossibleStates());
        this.flameParticle = DustParticleOptions.REDSTONE;
	}
	
	public LightSourceSconceRedTorchFloor(float hardness, float blastResistance, SoundType sound, String name) {
		super(Block.Properties.of(Material.METAL).strength(hardness, blastResistance).sound(sound).lightLevel((p_50886_) -> {
		    return 8; }) );

		this.registerDefaultState(this.getStateDefinition().any().setValue(DIRECTION, Direction.NORTH));
		this.generateShapes(this.getStateDefinition().getPossibleStates());
		this.setRegistryName(name);
		this.flameParticle = DustParticleOptions.REDSTONE;
	}

	@Override
	public boolean canConnectRedstone(BlockState state, BlockGetter world, BlockPos pos, Direction direction) {
		return true;
	}
	
	public void animateTick(BlockState state, Level level, BlockPos pos, Random rand) {
		if (HasFlame()) {
			double d0 = (double)pos.getX() + 0.5D + (rand.nextDouble() - 0.5D) * 0.2D;
	         double d1 = (double)pos.getY() + 0.7D + (rand.nextDouble() - 0.5D) * 0.2D;
	         double d2 = (double)pos.getZ() + 0.5D + (rand.nextDouble() - 0.5D) * 0.2D;
	         level.addParticle(this.flameParticle, d0, d1, d2, 0.0D, 0.0D, 0.0D);
		}
	}

	public void onPlace(BlockState state, Level level, BlockPos pos, BlockState state2, boolean flag) {
		for (Direction direction : Direction.values()) {
			level.updateNeighborsAt(pos.relative(direction), this);
		}

	}

	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState state2, boolean flag) {
		if (!flag) {
			for (Direction direction : Direction.values()) {
				level.updateNeighborsAt(pos.relative(direction), this);
			}

		}
	}

	public int getSignal(BlockState state, BlockGetter getter, BlockPos pos, Direction direction) {
		return Direction.UP != direction ? 15 : 0;
	}

	protected boolean hasNeighborSignal(Level level, BlockPos pos, BlockState state) {
		return level.hasSignal(pos.below(), Direction.DOWN);
	}

	public void tick(BlockState state, ServerLevel level, BlockPos pos, Random rnd) {
		boolean flag = this.hasNeighborSignal(level, pos, state);
		List<LightSourceSconceRedTorchWall.Toggle> list = RECENT_TOGGLES.get(level);

		while (list != null && !list.isEmpty() && level.getGameTime() - (list.get(0)).when > 60L) {
			list.remove(0);
		}

		if (flag) {
			level.setBlock(pos,
					BlockObjectHolder.light_metal_ironage_sconce_wall_redtorch_iron_unlit.defaultBlockState()
							.setValue(DIRECTION, state.getValue(BlockStateProperties.HORIZONTAL_FACING))
							.setValue(WATERLOGGED, state.getValue(BlockStateProperties.WATERLOGGED)),
					UPDATE_ALL);

			if (isToggledTooFrequently(level, pos, true)) {
				level.levelEvent(1502, pos, 0);
				level.scheduleTick(pos, level.getBlockState(pos).getBlock(), 160);
			}
		}

	}

	public boolean isSignalSource(BlockState state) {
		return true;
	}

	private static boolean isToggledTooFrequently(Level level, BlockPos pos, boolean flag) {
		List<LightSourceSconceRedTorchWall.Toggle> list = RECENT_TOGGLES.computeIfAbsent(level, (p_55680_) -> {
			return Lists.newArrayList();
		});
		if (flag) {
			list.add(new LightSourceSconceRedTorchWall.Toggle(pos.immutable(), level.getGameTime()));
		}

		int i = 0;

		for (int j = 0; j < list.size(); ++j) {
			LightSourceSconceRedTorchWall.Toggle redstonetorchblock$toggle = list.get(j);
			if (redstonetorchblock$toggle.pos.equals(pos)) {
				++i;
				if (i >= 8) {
					return true;
				}
			}
		}

		return false;
	}

	public static class Toggle {
		final BlockPos pos;
		final long when;

		public Toggle(BlockPos pos, long when) {
			this.pos = pos;
			this.when = when;
		}
	}

	public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos blockPos,
			boolean flag) {
		boolean hasSignal = this.hasNeighborSignal(level, pos, state);
		boolean willTick = level.getBlockTicks().willTickThisTick(pos, this);

		if (hasSignal && !willTick) {
			level.scheduleTick(pos, this, 2);
		}
	}

	public int getDirectSignal(BlockState state, BlockGetter getter, BlockPos pos, Direction direction) {
		return direction == Direction.DOWN ? state.getSignal(getter, pos, direction) : 0;
	}

	@Override
	protected Block UnlitVariant() {
		return BlockObjectHolder.light_metal_ironage_sconce_floor_redtorch_iron_unlit;
	}

	@Override
	protected Block EmptyVariant() {
		return BlockObjectHolder.light_metal_ironage_sconce_floor_empty_iron;
	}
}