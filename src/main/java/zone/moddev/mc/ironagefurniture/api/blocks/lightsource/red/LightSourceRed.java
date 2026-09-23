package zone.moddev.mc.ironagefurniture.api.blocks.lightsource.red;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.WeakHashMap;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import zone.moddev.mc.ironagefurniture.BlockObjectHolder;

import zone.moddev.mc.ironagefurniture.api.blocks.base.FallingFurnitureBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.Direction;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.particles.IParticleData;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.BlockState;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.block.material.Material;
import net.minecraft.world.storage.loot.LootContext.Builder;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.shapes.VoxelShape;

public class LightSourceRed extends FallingFurnitureBlock {
	protected IParticleData flameParticle;
	protected static final Map<IBlockReader, List<LightSourceRed.Toggle>> RECENT_TOGGLES = new WeakHashMap<>();
	public static final int RECENT_TOGGLE_TIMER = 60;
	public static final int MAX_RECENT_TOGGLES = 8;
	public static final int RESTART_DELAY = 160;
	public static final int LIGHT_LEVEL = 0;

	protected int GetLightLevel() {
		return 0;
	}

	@Override
	public boolean propagatesSkylightDown(BlockState state, IBlockReader getter, BlockPos pos) {
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

		this.setDefaultState(this.getStateContainer().getBaseState() .with(DIRECTION, Direction.NORTH) .with(WATERLOGGED, false));
		this.generateShapes(this.getStateContainer().getValidStates());
		this.flameParticle = RedstoneParticleData.REDSTONE_DUST;
	}

	public LightSourceRed(float hardness, float blastResistance, SoundType sound, String name) {
		super(Block.Properties.create(Material.GLASS).hardnessAndResistance(hardness, blastResistance).sound(sound).lightValue(LIGHT_LEVEL));

		this.setDefaultState(this.getStateContainer().getBaseState() .with(DIRECTION, Direction.NORTH));
		this.generateShapes(this.getStateContainer().getValidStates());
		this.setRegistryName(name);
		this.flameParticle = RedstoneParticleData.REDSTONE_DUST;
	}

	@Override
	protected void generateShapes(ImmutableList<BlockState> states) {
		ImmutableMap.Builder<BlockState, VoxelShape> builder = new ImmutableMap.Builder<>();
		for (BlockState state : states) {
			VoxelShape shapes = VoxelShapes.empty();

			shapes = VoxelShapes.combine(shapes, getShapes(rotate(Block.makeCuboidShape(6, 0, 6, 10, 5, 10), Direction.SOUTH))[state .get(DIRECTION).getHorizontalIndex()], IBooleanFunction.OR); // jar

			builder.put(state, shapes.simplify());
		}

		_shapes = builder.build();
	}

	@Override
	public void onBlockAdded(BlockState state, World level, BlockPos pos, BlockState state2, boolean flag) {
		for (Direction direction : Direction.values())
			level.notifyNeighborsOfStateChange(pos .offset(direction), this);

		super.onBlockAdded(state, level, pos, state2, flag);
	}

	@Override
	public void onReplaced(BlockState state, World level, BlockPos pos, BlockState state2, boolean flag) {
		if (flag)
			return;

		for (Direction direction : Direction.values())
			level.notifyNeighborsOfStateChange(pos .offset(direction), this);
	}

	protected boolean hasNeighborSignal(World level, BlockPos pos, BlockState state) {
		for (Direction direction : Direction.values()) {
	        if (direction != Direction.UP && level.isSidePowered(pos .offset(direction), direction)) {
	            return true;
	        }
	    }
	    return false;
	}

	protected int getNeighborSignal(World level, BlockPos pos, BlockState state) {
		int maxSignal = 0;
	    for (Direction direction : Direction.values()) {
	        if (direction != Direction.UP) {
	            maxSignal = Math.max(maxSignal, level.getRedstonePower(pos .offset(direction), direction));
	        }
	    }
	    return maxSignal;
	}

	protected LightSourceRed getBlockBySignalLevel(int level) {
		switch (level) {
			case 1:
				return (LightSourceRed) BlockObjectHolder.light_metal_ironage_block_floor_red_clear_one;
			case 2:
				return (LightSourceRed) BlockObjectHolder.light_metal_ironage_block_floor_red_clear_two;
			case 3:
				return (LightSourceRed) BlockObjectHolder.light_metal_ironage_block_floor_red_clear_three;
			case 4:
				return (LightSourceRed) BlockObjectHolder.light_metal_ironage_block_floor_red_clear_four;
			case 5:
				return (LightSourceRed) BlockObjectHolder.light_metal_ironage_block_floor_red_clear_five;
			case 6:
				return (LightSourceRed) BlockObjectHolder.light_metal_ironage_block_floor_red_clear_six;
			case 7:
				return (LightSourceRed) BlockObjectHolder.light_metal_ironage_block_floor_red_clear_seven;
			case 8:
				return (LightSourceRed) BlockObjectHolder.light_metal_ironage_block_floor_red_clear_eight;
			case 9:
				return (LightSourceRed) BlockObjectHolder.light_metal_ironage_block_floor_red_clear_nine;
			case 10:
				return (LightSourceRed) BlockObjectHolder.light_metal_ironage_block_floor_red_clear_ten;
			case 11:
				return (LightSourceRed) BlockObjectHolder.light_metal_ironage_block_floor_red_clear_eleven;
			case 12:
				return (LightSourceRed) BlockObjectHolder.light_metal_ironage_block_floor_red_clear_twelve;
			case 13:
				return (LightSourceRed) BlockObjectHolder.light_metal_ironage_block_floor_red_clear_thirteen;
			case 14:
				return (LightSourceRed) BlockObjectHolder.light_metal_ironage_block_floor_red_clear_fourteen;
			case 15:
				return (LightSourceRed) BlockObjectHolder.light_metal_ironage_block_floor_red_clear_fifteen;
			default:
				return (LightSourceRed) BlockObjectHolder.light_metal_ironage_block_floor_red_clear;
		}
	}

	@Override
	public void tick(BlockState state, World level, BlockPos pos, Random rnd) {
		int signal = this.getNeighborSignal(level, pos, state);
		List<LightSourceRed.Toggle> list = RECENT_TOGGLES.get(level);

		while (list != null && !list.isEmpty() && level.getGameTime() - (list.get(0)).when > 60L)
			list.remove(0);

		if (signal != GetLightLevel()) {
			LightSourceRed newBlock = getBlockBySignalLevel(signal);

			level.setBlockState(pos,
				newBlock.getDefaultState()
					 .with(DIRECTION, state .get(BlockStateProperties.HORIZONTAL_FACING))
					 .with(WATERLOGGED, state .get(BlockStateProperties.WATERLOGGED)),
				3);

			Block block = level.getBlockState(pos).getBlock();

				level.getPendingBlockTicks().scheduleTick(pos, block, 2);

			if (isToggledTooFrequently(level, pos, true)) {
				level.playEvent(1502, pos, 0);
				level.getPendingBlockTicks().scheduleTick(pos, level.getBlockState(pos).getBlock(), 160);
			}
		}

		super.tick(state, level, pos, rnd);
	}

	private static boolean isToggledTooFrequently(World level, BlockPos pos, boolean flag) {
		List<LightSourceRed.Toggle> list = RECENT_TOGGLES.computeIfAbsent(level, (p_55680_) -> Lists.newArrayList());

		if (flag)
			list.add(new Toggle(pos.toImmutable(), level.getGameTime()));

		int i = 0;

		for (Toggle redstonetorchblock$toggle : list) {

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
	public void animateTick(BlockState state, World level, BlockPos pos, Random rnd) {
		if (GetLightLevel() <= 0)
			return;

		double d0 = (double) pos.getX() + 0.5D + (rnd.nextDouble() - 0.5D) * 0.2D;
		double d1 = (double) pos.getY() + 0.25D + (rnd.nextDouble() - 0.5D) * 0.2D;
		double d2 = (double) pos.getZ() + 0.5D + (rnd.nextDouble() - 0.5D) * 0.2D;

		level.addParticle(this.flameParticle, d0, d1, d2, 0.0D, 0.0D, 0.0D);
	}

	@Override
	public void neighborChanged(BlockState state, World level, BlockPos pos, Block block, BlockPos blockPos,
								boolean flag) {
	    boolean hasSignal = this.hasNeighborSignal(level, pos, state);
	    boolean willTick = level.getPendingBlockTicks().isTickPending(pos, this);

	    if (hasSignal || GetLightLevel() > 0) {
	        if (!willTick) {
	            level.getPendingBlockTicks().scheduleTick(pos, this, 2);
	        }
	    }
	}

	@Override
	public int getStrongPower(BlockState state, IBlockReader getter, BlockPos pos, Direction direction) {
		return direction == Direction.DOWN ? state.getWeakPower(getter, pos, direction) : 0;
	}

	@Override
	public boolean canConnectRedstone(BlockState state, IBlockReader world, BlockPos pos, Direction direction) {
		return true;
	}
}
