package com.mcmoddev.ironagefurniture.api.blocks.lightsource.red;

import com.google.common.collect.Lists;
import com.mcmoddev.ironagefurniture.BlockObjectHolder;
import com.mcmoddev.ironagefurniture.api.blocks.base.FurnitureBlock;
import com.mcmoddev.ironagefurniture.api.blocks.lightsource.glow.LightSourceSconceGlowFloor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Material;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.WeakHashMap;

public class LightSourceSconceRedFloor extends LightSourceSconceGlowFloor implements LiquidBlockContainer {
	protected ParticleOptions flameParticle;
	protected static final Map<BlockGetter, List<LightSourceSconceRedFloor.Toggle>> RECENT_TOGGLES = new WeakHashMap<>();
	public static final int RECENT_TOGGLE_TIMER = 60;
	public static final int MAX_RECENT_TOGGLES = 8;
	public static final int RESTART_DELAY = 160;
	public static final int LIGHT_LEVEL = 0;

	protected int GetLightLevel() {
		return 0;
	}

	@Override
	protected boolean HasFlame() {
		return true;
	}

	@Override
	protected Block LightDrop() {
		return BlockObjectHolder.light_metal_ironage_block_floor_red_clear;
	}

	@Override
	public float getShadeBrightness(BlockState state, BlockGetter getter, BlockPos pos) {
		return 1.0F;
	}

	@Override
	public boolean propagatesSkylightDown(BlockState state, BlockGetter getter, BlockPos pos) {
		return true;
	}

	public LightSourceSconceRedFloor(Properties properties) {
		super(properties);

		this.registerDefaultState(this.getStateDefinition().any().setValue(DIRECTION, Direction.NORTH).setValue(WATERLOGGED, false));
		this.generateShapes(this.getStateDefinition().getPossibleStates());
		this.flameParticle = DustParticleOptions.REDSTONE;
	}

	public LightSourceSconceRedFloor(float hardness, float blastResistance, SoundType sound, String name) {
		super(Block.Properties.of(Material.METAL).strength(hardness, blastResistance).sound(sound).lightLevel((p_50886_) -> LIGHT_LEVEL) );

		this.registerDefaultState(this.getStateDefinition().any().setValue(FurnitureBlock.DIRECTION, Direction.NORTH));
		this.generateShapes(this.getStateDefinition().getPossibleStates());
		this.setRegistryName(name);
		this.flameParticle = DustParticleOptions.REDSTONE;
	}

	@Override
	public void onPlace(BlockState state, Level level, BlockPos pos, BlockState state2, boolean flag) {
		for (Direction direction : Direction.values())
			level.updateNeighborsAt(pos.relative(direction), this);

		super.onPlace(state, level, pos, state2, flag);
	}

	@Override
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState state2, boolean flag) {
		if (!flag)
			for (Direction direction : Direction.values())
				level.updateNeighborsAt(pos.relative(direction), this);
	}

	protected boolean hasNeighborSignal(Level level, BlockPos pos, BlockState state) {
		return level.hasSignal(pos.below(), Direction.DOWN);
	}

	protected int getNeighborSignal(Level level, BlockPos pos, BlockState state) {
		return level.getSignal(pos.below(), Direction.DOWN);
	}

	protected LightSourceSconceRedFloor getBlockBySignalLevel(int level) {
		switch (level) {
			case 1:
				return (LightSourceSconceRedFloor) BlockObjectHolder.light_metal_ironage_sconce_floor_red_iron_one;
			case 2:
				return (LightSourceSconceRedFloor) BlockObjectHolder.light_metal_ironage_sconce_floor_red_iron_two;
			case 3:
				return (LightSourceSconceRedFloor) BlockObjectHolder.light_metal_ironage_sconce_floor_red_iron_three;
			case 4:
				return (LightSourceSconceRedFloor) BlockObjectHolder.light_metal_ironage_sconce_floor_red_iron_four;
			case 5:
				return (LightSourceSconceRedFloor) BlockObjectHolder.light_metal_ironage_sconce_floor_red_iron_five;
			case 6:
				return (LightSourceSconceRedFloor) BlockObjectHolder.light_metal_ironage_sconce_floor_red_iron_six;
			case 7:
				return (LightSourceSconceRedFloor) BlockObjectHolder.light_metal_ironage_sconce_floor_red_iron_seven;
			case 8:
				return (LightSourceSconceRedFloor) BlockObjectHolder.light_metal_ironage_sconce_floor_red_iron_eight;
			case 9:
				return (LightSourceSconceRedFloor) BlockObjectHolder.light_metal_ironage_sconce_floor_red_iron_nine;
			case 10:
				return (LightSourceSconceRedFloor) BlockObjectHolder.light_metal_ironage_sconce_floor_red_iron_ten;
			case 11:
				return (LightSourceSconceRedFloor) BlockObjectHolder.light_metal_ironage_sconce_floor_red_iron_eleven;
			case 12:
				return (LightSourceSconceRedFloor) BlockObjectHolder.light_metal_ironage_sconce_floor_red_iron_twelve;
			case 13:
				return (LightSourceSconceRedFloor) BlockObjectHolder.light_metal_ironage_sconce_floor_red_iron_thirteen;
			case 14:
				return (LightSourceSconceRedFloor) BlockObjectHolder.light_metal_ironage_sconce_floor_red_iron_fourteen;
			case 15:
				return (LightSourceSconceRedFloor) BlockObjectHolder.light_metal_ironage_sconce_floor_red_iron_fifteen;
			default:
				return (LightSourceSconceRedFloor) BlockObjectHolder.light_metal_ironage_sconce_floor_red_iron;
		}
	}

	@Override
	public void tick(BlockState state, ServerLevel level, BlockPos pos, Random rnd) {
		int signal = this.getNeighborSignal(level, pos, state);
		List<LightSourceSconceRedFloor.Toggle> list = RECENT_TOGGLES.get(level);

		while (list != null && !list.isEmpty() && level.getGameTime() - (list.get(0)).when > 60L)
			list.remove(0);

		if (signal != GetLightLevel()) {
			LightSourceSconceRedFloor newBlock = getBlockBySignalLevel(signal);

			level.setBlock(pos,
				newBlock.defaultBlockState()
					.setValue(DIRECTION, state.getValue(BlockStateProperties.HORIZONTAL_FACING))
					.setValue(WATERLOGGED, state.getValue(BlockStateProperties.WATERLOGGED)),
				UPDATE_ALL);

			Block block = level.getBlockState(pos).getBlock();

			level.scheduleTick(pos, block, 2);

			if (isToggledTooFrequently(level, pos, true)) {
				level.levelEvent(1502, pos, 0);
				level.scheduleTick(pos, level.getBlockState(pos).getBlock(), 160);
			}
		}

		super.tick(state, level, pos, rnd);
	}

	private static boolean isToggledTooFrequently(Level level, BlockPos pos, boolean flag) {
		List<LightSourceSconceRedFloor.Toggle> list = RECENT_TOGGLES.computeIfAbsent(level, (p_55680_) -> Lists.newArrayList());

		if (flag)
			list.add(new Toggle(pos.immutable(), level.getGameTime()));

		int i = 0;

		for (LightSourceSconceRedFloor.Toggle redstonetorchblock$toggle : list) {

			if (!redstonetorchblock$toggle.pos.equals(pos))
				continue;

			i++;

			if (i >= 8)
				return true;
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

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, Random rnd) {
		if (GetLightLevel() <= 0)
			return;

		double d0 = (double)pos.getX() + 0.5D + (rnd.nextDouble() - 0.5D) * 0.2D;
		double d1 = (double)pos.getY() + 0.25D + (rnd.nextDouble() - 0.5D) * 0.2D;
		double d2 = (double)pos.getZ() + 0.5D + (rnd.nextDouble() - 0.5D) * 0.2D;

		level.addParticle(this.flameParticle, d0, d1, d2, 0.0D, 0.0D, 0.0D);
	}

	@Override
	public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos blockPos,
								boolean flag) {
		boolean hasSignal = this.hasNeighborSignal(level, pos, state);
		boolean willTick = level.getBlockTicks().willTickThisTick(pos, this);

		if ((hasSignal || GetLightLevel() > 0) && !willTick)
			level.scheduleTick(pos, this, 2);
	}

	@Override
	public int getDirectSignal(BlockState state, BlockGetter getter, BlockPos pos, Direction direction) {
		return direction == Direction.DOWN ? state.getSignal(getter, pos, direction) : 0;
	}

	@Override
	public boolean canConnectRedstone(BlockState state, BlockGetter world, BlockPos pos, Direction direction) {
		return true;
	}
}
