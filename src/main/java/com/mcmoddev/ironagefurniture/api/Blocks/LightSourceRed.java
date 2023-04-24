package com.mcmoddev.ironagefurniture.api.Blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.WeakHashMap;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.mcmoddev.ironagefurniture.BlockObjectHolder;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.storage.loot.LootContext.Builder;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class LightSourceRed extends FallingFurnitureBlock {
	protected ParticleOptions flameParticle;
	protected static final Map<BlockGetter, List<LightSourceRed.Toggle>> RECENT_TOGGLES = new WeakHashMap<>();
	public static final int RECENT_TOGGLE_TIMER = 60;
	public static final int MAX_RECENT_TOGGLES = 8;
	public static final int RESTART_DELAY = 160;
	public static final int LIGHT_LEVEL = 0;

	protected int GetLightLevel() {
		return 0;
	}

	@Override
	public float getShadeBrightness(BlockState state, BlockGetter getter, BlockPos pos) {
		return 1.0F;
	}

	@Override
	public boolean propagatesSkylightDown(BlockState state, BlockGetter getter, BlockPos pos) {
		return true;
	}

	@Override
    public List<ItemStack> getDrops(BlockState state, Builder builder) {
		List<ItemStack> drops;

    	Item item = BlockObjectHolder.light_metal_ironage_block_floor_red_clear.asItem();
    	ItemStack stack = new ItemStack(item, 1);
    	drops = new ArrayList<>();
    	drops.add(stack);

    	return drops;
    }

	public LightSourceRed(Properties properties) {
		super(properties);

		this.registerDefaultState(this.getStateDefinition().any().setValue(DIRECTION, Direction.NORTH).setValue(WATERLOGGED, false));
        this.generateShapes(this.getStateDefinition().getPossibleStates());
        this.flameParticle = DustParticleOptions.REDSTONE;
	}

	public LightSourceRed(float hardness, float blastResistance, SoundType sound, String name) {
		super(Block.Properties.of(Material.GLASS).strength(hardness, blastResistance).sound(sound).lightLevel((p_50886_) -> {
		    return LIGHT_LEVEL; }) );

		this.registerDefaultState(this.getStateDefinition().any().setValue(DIRECTION, Direction.NORTH));
		this.generateShapes(this.getStateDefinition().getPossibleStates());
		this.setRegistryName(name);
		this.flameParticle = DustParticleOptions.REDSTONE;
	}

	@Override
	protected void generateShapes(ImmutableList<BlockState> states) {
		 ImmutableMap.Builder<BlockState, VoxelShape> builder = new ImmutableMap.Builder<>();
	        for(BlockState state : states)
	        {
	        	VoxelShape shapes = Shapes.empty();

	        	shapes = Shapes.joinUnoptimized(shapes, getShapes(rotate(Block.box(6, 0, 6, 10, 5, 10), Direction.SOUTH))[state.getValue(DIRECTION).get2DDataValue()], BooleanOp.OR); // jar

	            builder.put(state, shapes.optimize());
	        }

	        _shapes = builder.build();
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

	protected LightSourceRed getBlockBySignalLevel(int level) {
		switch (level) {
			case 1 -> {
				return (LightSourceRed) BlockObjectHolder.light_metal_ironage_block_floor_red_clear_one;
			}
			case 2 -> {
				return (LightSourceRed) BlockObjectHolder.light_metal_ironage_block_floor_red_clear_two;
			}
			case 3 -> {
				return (LightSourceRed) BlockObjectHolder.light_metal_ironage_block_floor_red_clear_three;
			}
			case 4 -> {
				return (LightSourceRed) BlockObjectHolder.light_metal_ironage_block_floor_red_clear_four;
			}
			case 5 -> {
				return (LightSourceRed) BlockObjectHolder.light_metal_ironage_block_floor_red_clear_five;
			}
			case 6 -> {
				return (LightSourceRed) BlockObjectHolder.light_metal_ironage_block_floor_red_clear_six;
			}
			case 7 -> {
				return (LightSourceRed) BlockObjectHolder.light_metal_ironage_block_floor_red_clear_seven;
			}
			case 8 -> {
				return (LightSourceRed) BlockObjectHolder.light_metal_ironage_block_floor_red_clear_eight;
			}
			case 9 -> {
				return (LightSourceRed) BlockObjectHolder.light_metal_ironage_block_floor_red_clear_nine;
			}
			case 10 -> {
				return (LightSourceRed) BlockObjectHolder.light_metal_ironage_block_floor_red_clear_ten;
			}
			case 11 -> {
				return (LightSourceRed) BlockObjectHolder.light_metal_ironage_block_floor_red_clear_eleven;
			}
			case 12 -> {
				return (LightSourceRed) BlockObjectHolder.light_metal_ironage_block_floor_red_clear_twelve;
			}
			case 13 -> {
				return (LightSourceRed) BlockObjectHolder.light_metal_ironage_block_floor_red_clear_thirteen;
			}
			case 14 -> {
				return (LightSourceRed) BlockObjectHolder.light_metal_ironage_block_floor_red_clear_fourteen;
			}
			case 15 -> {
				return (LightSourceRed) BlockObjectHolder.light_metal_ironage_block_floor_red_clear_fifteen;
			}
			default -> {
				return (LightSourceRed) BlockObjectHolder.light_metal_ironage_block_floor_red_clear;
			}
		}
	}

	@Override
	public void tick(BlockState state, ServerLevel level, BlockPos pos, Random rnd) {
		int signal = this.getNeighborSignal(level, pos, state);
		List<LightSourceRed.Toggle> list = RECENT_TOGGLES.get(level);

		while (list != null && !list.isEmpty() && level.getGameTime() - (list.get(0)).when > 60L)
			list.remove(0);

		if (signal != GetLightLevel()) {
			LightSourceRed newBlock = getBlockBySignalLevel(signal);

			level.setBlock(pos,
					newBlock.defaultBlockState()
							.setValue(DIRECTION, state.getValue(BlockStateProperties.HORIZONTAL_FACING))
							.setValue(WATERLOGGED, state.getValue(BlockStateProperties.WATERLOGGED)),
					UPDATE_ALL);

			Block block = level.getBlockState(pos).getBlock();

			level.scheduleTick(pos, block, newBlock.getDelayAfterPlace());

			if (isToggledTooFrequently(level, pos, true)) {
				level.levelEvent(1502, pos, 0);
				level.scheduleTick(pos, level.getBlockState(pos).getBlock(), 160);
			}
		}

		super.tick(state, level, pos, rnd);
	}

	private static boolean isToggledTooFrequently(Level level, BlockPos pos, boolean flag) {
		List<LightSourceRed.Toggle> list = RECENT_TOGGLES.computeIfAbsent(level, (p_55680_) -> {
			return Lists.newArrayList();
		});
		if (flag) {
			list.add(new LightSourceRed.Toggle(pos.immutable(), level.getGameTime()));
		}

		int i = 0;

		for (Toggle redstonetorchblock$toggle : list) {

			if (!redstonetorchblock$toggle.pos.equals(pos))
				continue;

			++i;

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

		if (hasSignal && !willTick)
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