package zone.moddev.mc.ironagefurniture.api.blocks.lightsource.red;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import zone.moddev.mc.ironagefurniture.BlockObjectHolder;
import zone.moddev.mc.ironagefurniture.api.blocks.base.FurnitureBlock;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.torch.LightSourceSconceTorchWall;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.Direction;
import net.minecraft.particles.IParticleData;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.BlockState;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.shapes.VoxelShape;
import com.mojang.datafixers.util.Pair;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.WeakHashMap;

public class LightSourceSconceRedWall extends LightSourceSconceTorchWall {
	protected IParticleData flameParticle;
	protected static final Map<IBlockReader, List<LightSourceSconceRedWall.Toggle>> RECENT_TOGGLES = new WeakHashMap<>();
	public static final int RECENT_TOGGLE_TIMER = 60;
	public static final int MAX_RECENT_TOGGLES = 8;
	public static final int RESTART_DELAY = 160;
	public static final int LIGHT_LEVEL = 0;

	protected int GetLightLevel() {
		return 0;
	}

	@Override
	protected Block LightDrop() {
		return BlockObjectHolder.light_metal_ironage_block_floor_red_clear;
	}

	@Override
	public float getShadeBrightness(BlockState state, IBlockReader getter, BlockPos pos) {
		return 1.0F;
	}

	@Override
	public boolean propagatesSkylightDown(BlockState state, IBlockReader getter, BlockPos pos) {
		return true;
	}

	public LightSourceSconceRedWall(Properties properties) {
		super(properties);

		this.registerDefaultState(this.getStateDefinition().any().setValue(FurnitureBlock.DIRECTION, Direction.NORTH).setValue(FurnitureBlock.WATERLOGGED, false));
		this.generateShapes(this.getStateDefinition().getPossibleStates());
	}

	@Override
	protected boolean CanEx() {
		return false;
	}

	@Override
	protected void generateShapes(ImmutableList<BlockState> states) {
		ImmutableMap.Builder<BlockState, VoxelShape> builder = new ImmutableMap.Builder<>();
		for (BlockState state : states) {
			VoxelShape shapes = VoxelShapes.empty();

			shapes = VoxelShapes.joinUnoptimized(shapes, FurnitureBlock.getShapes(FurnitureBlock.rotate(Block.box(6, 9, 9, 10, 10, 16), Direction.SOUTH))[state.getValue(FurnitureBlock.DIRECTION).get2DDataValue()], IBooleanFunction.OR); // sconce holder
			shapes = VoxelShapes.joinUnoptimized(shapes, FurnitureBlock.getShapes(FurnitureBlock.rotate(Block.box(6, 5, 9, 10, 11, 13), Direction.SOUTH))[state.getValue(FurnitureBlock.DIRECTION).get2DDataValue()], IBooleanFunction.OR); // torch

			builder.put(state, shapes.optimize());
		}

		_shapes = builder.build();
	}

	public LightSourceSconceRedWall(float hardness, float blastResistance, SoundType sound, String name) {
		super(Properties.of(Material.METAL).strength(hardness, blastResistance).sound(sound).lightLevel(LIGHT_LEVEL));

		this.registerDefaultState(this.getStateDefinition().any().setValue(FurnitureBlock.DIRECTION, Direction.NORTH));
		this.generateShapes(this.getStateDefinition().getPossibleStates());
		this.setRegistryName(name);
	}

	@Override
	public void onPlace(BlockState state, World level, BlockPos pos, BlockState state2, boolean flag) {
		for (Direction direction : Direction.values())
			level.updateNeighborsAt(pos.relative(direction), this);

		super.onPlace(state, level, pos, state2, flag);
	}

	@Override
	public void onRemove(BlockState state, World level, BlockPos pos, BlockState state2, boolean flag) {
		if (flag)
			return;

		for (Direction direction : Direction.values())
			level.updateNeighborsAt(pos.relative(direction), this);
	}

	protected boolean hasNeighborSignal(World level, BlockPos pos, BlockState state) {
		Direction direction = state.getValue(FurnitureBlock.DIRECTION).getOpposite();
		return level.hasSignal(pos.relative(direction), direction);
	}

	protected int getNeighborSignal(World level, BlockPos pos, BlockState state) {
		Direction direction = state.getValue(FurnitureBlock.DIRECTION).getOpposite();
		return level.getSignal(pos.relative(direction), direction);
	}

	protected LightSourceSconceRedWall getBlockBySignalLevel(int level) {
		switch (level) {
			case 1:
				return (LightSourceSconceRedWall) BlockObjectHolder.light_metal_ironage_sconce_wall_red_iron_one;
			case 2:
				return (LightSourceSconceRedWall) BlockObjectHolder.light_metal_ironage_sconce_wall_red_iron_two;
			case 3:
				return (LightSourceSconceRedWall) BlockObjectHolder.light_metal_ironage_sconce_wall_red_iron_three;
			case 4:
				return (LightSourceSconceRedWall) BlockObjectHolder.light_metal_ironage_sconce_wall_red_iron_four;
			case 5:
				return (LightSourceSconceRedWall) BlockObjectHolder.light_metal_ironage_sconce_wall_red_iron_five;
			case 6:
				return (LightSourceSconceRedWall) BlockObjectHolder.light_metal_ironage_sconce_wall_red_iron_six;
			case 7:
				return (LightSourceSconceRedWall) BlockObjectHolder.light_metal_ironage_sconce_wall_red_iron_seven;
			case 8:
				return (LightSourceSconceRedWall) BlockObjectHolder.light_metal_ironage_sconce_wall_red_iron_eight;
			case 9:
				return (LightSourceSconceRedWall) BlockObjectHolder.light_metal_ironage_sconce_wall_red_iron_nine;
			case 10:
				return (LightSourceSconceRedWall) BlockObjectHolder.light_metal_ironage_sconce_wall_red_iron_ten;
			case 11:
				return (LightSourceSconceRedWall) BlockObjectHolder.light_metal_ironage_sconce_wall_red_iron_eleven;
			case 12:
				return (LightSourceSconceRedWall) BlockObjectHolder.light_metal_ironage_sconce_wall_red_iron_twelve;
			case 13:
				return (LightSourceSconceRedWall) BlockObjectHolder.light_metal_ironage_sconce_wall_red_iron_thirteen;
			case 14:
				return (LightSourceSconceRedWall) BlockObjectHolder.light_metal_ironage_sconce_wall_red_iron_fourteen;
			case 15:
				return (LightSourceSconceRedWall) BlockObjectHolder.light_metal_ironage_sconce_wall_red_iron_fifteen;
			default:
				return (LightSourceSconceRedWall) BlockObjectHolder.light_metal_ironage_sconce_wall_red_iron;
		}
	}

	@Override
	public void tick(BlockState state, ServerWorld level, BlockPos pos, Random rnd) {
		int signal = this.getNeighborSignal(level, pos, state);
		List<LightSourceSconceRedWall.Toggle> list = RECENT_TOGGLES.get(level);

		while (list != null && !list.isEmpty() && level.getGameTime() - (list.get(0)).when > 60L)
			list.remove(0);

		if (signal != GetLightLevel()) {
			LightSourceSconceRedWall newBlock = getBlockBySignalLevel(signal);

			level.setBlock(pos,
				newBlock.defaultBlockState()
					.setValue(DIRECTION, state.getValue(BlockStateProperties.HORIZONTAL_FACING))
					.setValue(WATERLOGGED, state.getValue(BlockStateProperties.WATERLOGGED)),
				3);

			Block block = level.getBlockState(pos).getBlock();

			level.getBlockTicks().scheduleTick(pos, block, 2);

			if (isToggledTooFrequently(level, pos, true)) {
				level.levelEvent(1502, pos, 0);
				level.getBlockTicks().scheduleTick(pos, level.getBlockState(pos).getBlock(), 160);
			}
		}

		super.tick(state, level, pos, rnd);
	}

	private static boolean isToggledTooFrequently(World level, BlockPos pos, boolean flag) {
		List<LightSourceSconceRedWall.Toggle> list = RECENT_TOGGLES.computeIfAbsent(level, (p_55680_) -> Lists.newArrayList());

		if (flag)
			list.add(new LightSourceSconceRedWall.Toggle(pos.immutable(), level.getGameTime()));

		int i = 0;

		for (LightSourceSconceRedWall.Toggle redstonetorchblock$toggle : list) {

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

//		double d0 = (double) pos.getX() + 0.5D + (rnd.nextDouble() - 0.5D) * 0.2D;
//		double d1 = (double) pos.getY() + 0.25D + (rnd.nextDouble() - 0.5D) * 0.2D;
//		double d2 = (double) pos.getZ() + 0.5D + (rnd.nextDouble() - 0.5D) * 0.2D;
//
//		level.addParticle(this.flameParticle, d0, d1, d2, 0.0D, 0.0D, 0.0D);
		Direction direction = state.getValue(FurnitureBlock.DIRECTION);
		Pair<Double, Double> rotated = FurnitureBlock.rotate(0.6D, 0.5D, state.getValue(FurnitureBlock.DIRECTION));

		double d0 = (double) pos.getX() + rotated.getFirst();
		double d1 = (double) pos.getY() + 0.8D;
		double d2 = (double) pos.getZ() + rotated.getSecond();

		Direction direction1 = direction.getOpposite();

		level.addParticle(this.flameParticle, d0 + 0.27D * (double) direction1.getStepX(), d1 + 0.22D,
			d2 + 0.27D * (double) direction1.getStepZ(), 0.0D, 0.0D, 0.0D);
	}

	@Override
	public void neighborChanged(BlockState state, World level, BlockPos pos, Block block, BlockPos blockPos,
								boolean flag) {
		boolean hasSignal = this.hasNeighborSignal(level, pos, state);
		boolean willTick = level.getBlockTicks().willTickThisTick(pos, this);

		if ((hasSignal || GetLightLevel() > 0) && !willTick)
			level.getBlockTicks().scheduleTick(pos, this, 2);
	}

	@Override
	public int getDirectSignal(BlockState state, IBlockReader getter, BlockPos pos, Direction direction) {
		return direction == Direction.DOWN ? state.getSignal(getter, pos, direction) : 0;
	}

	@Override
	public boolean canConnectRedstone(BlockState state, IBlockReader world, BlockPos pos, Direction direction) {
		return true;
	}
}
