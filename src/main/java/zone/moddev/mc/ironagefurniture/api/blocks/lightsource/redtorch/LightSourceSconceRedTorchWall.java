package zone.moddev.mc.ironagefurniture.api.blocks.lightsource.redtorch;

import zone.moddev.mc.ironagefurniture.api.blocks.base.FurnitureBlock;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.torch.LightSourceSconceTorchWall;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.BlockState;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import com.mojang.datafixers.util.Pair;
import net.minecraft.util.Direction;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.world.server.ServerWorld;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.WeakHashMap;

import com.google.common.collect.Lists;
import zone.moddev.mc.ironagefurniture.BlockObjectHolder;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class LightSourceSconceRedTorchWall extends LightSourceSconceTorchWall {
	protected static final Map<IBlockReader, List<LightSourceSconceRedTorchWall.Toggle>> RECENT_TOGGLES = new WeakHashMap<>();
	public static final int RECENT_TOGGLE_TIMER = 60;
	public static final int MAX_RECENT_TOGGLES = 8;
	public static final int RESTART_DELAY = 160;

	protected Block GetRedVariant() {
		return BlockObjectHolder.light_metal_ironage_sconce_wall_redtorch_iron;
	}

	@Override
	protected boolean CanEx() {
		return false;
	}

	@Override
	protected Block LightDrop() {
		return Blocks.REDSTONE_TORCH;
	}

	public LightSourceSconceRedTorchWall(Block.Properties properties) {
		super(properties);

		this.setDefaultState(
			this.getStateContainer().getBaseState() .with(FurnitureBlock.DIRECTION, Direction.NORTH) .with(FurnitureBlock.WATERLOGGED, false));

		this.generateShapes(this.getStateContainer().getValidStates());
		this.flameParticle = RedstoneParticleData.REDSTONE_DUST;
	}

	@Override
	public void animateTick(BlockState state, World level, BlockPos pos, Random rand) {
		if (!HasFlame())
			return;

		Direction direction = state .get(FurnitureBlock.DIRECTION);
		Pair<Double, Double> rotated = FurnitureBlock.rotate(0.6D, 0.5D, state .get(FurnitureBlock.DIRECTION));

		double d0 = (double) pos.getX() + rotated.getFirst();
		double d1 = (double) pos.getY() + 0.8D;
		double d2 = (double) pos.getZ() + rotated.getSecond();

		Direction direction1 = direction.getOpposite();

		level.addParticle(this.flameParticle, d0 + 0.27D * (double) direction1.getXOffset(), d1 + 0.22D,
			d2 + 0.27D * (double) direction1.getZOffset(), 0.0D, 0.0D, 0.0D);
	}

	@Override
	public void onBlockAdded(BlockState state, World level, BlockPos pos, BlockState state2, boolean flag) {
		for (Direction direction : Direction.values())
			level.notifyNeighborsOfStateChange(pos .offset(direction), this);
	}

	@Override
	public void onReplaced(BlockState state, World level, BlockPos pos, BlockState state2, boolean flag) {
		if (flag)
			return;

		for (Direction direction : Direction.values())
			level.notifyNeighborsOfStateChange(pos .offset(direction), this);
	}

	@Override
	public int getWeakPower(BlockState state, IBlockReader blockGetter, BlockPos pos, Direction direction) {
		return state .get(FurnitureBlock.DIRECTION) != direction ? 15 : 0;
	}

	protected boolean hasNeighborSignal(World level, BlockPos pos, BlockState state) {
		Direction direction = state .get(FurnitureBlock.DIRECTION).getOpposite();
		return level.isSidePowered(pos .offset(direction), direction);
	}

	@Override
	public void tick(BlockState state, World level, BlockPos pos, Random rnd) {
		boolean flag = this.hasNeighborSignal(level, pos, state);
		List<LightSourceSconceRedTorchWall.Toggle> list = RECENT_TOGGLES.get(level);

		while (list != null && !list.isEmpty() && level.getGameTime() - (list.get(0)).when > 60L)
			list.remove(0);

		if (!flag)
			return;

		level.setBlockState(pos,
			UnlitVariant().getDefaultState()
				 .with(FurnitureBlock.DIRECTION, state .get(BlockStateProperties.HORIZONTAL_FACING))
				 .with(FurnitureBlock.WATERLOGGED, state .get(BlockStateProperties.WATERLOGGED)),
			3);

		if (!isToggledTooFrequently(level, pos, true))
			return;

		level.playEvent(1502, pos, 0);
		level.getPendingBlockTicks().scheduleTick(pos, level.getBlockState(pos).getBlock(), 160);
	}

	@Override
	public boolean canProvidePower(BlockState state) {
		return true;
	}

	private static boolean isToggledTooFrequently(World level, BlockPos pos, boolean flag) {
		List<LightSourceSconceRedTorchWall.Toggle> list = RECENT_TOGGLES.computeIfAbsent(level, (p_55680_) -> Lists.newArrayList());

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
	public void neighborChanged(BlockState state, World level, BlockPos pos, Block block, BlockPos blockPos,
								boolean flag) {

		boolean hasSignal = this.hasNeighborSignal(level, pos, state);
		boolean willTick = level.getPendingBlockTicks().isTickPending(pos, this);

		if (hasSignal && !willTick)
			level.getPendingBlockTicks().scheduleTick(pos, this, 2);
	}

	@Override
	public int getStrongPower(BlockState state, IBlockReader getter, BlockPos pos, Direction direction) {
		return direction == Direction.DOWN ? state.getWeakPower(getter, pos, direction) : 0;
	}

	@Override
	protected Block UnlitVariant() {
		return BlockObjectHolder.light_metal_ironage_sconce_wall_redtorch_iron_unlit;
	}

	public LightSourceSconceRedTorchWall(float hardness, float blastResistance, SoundType sound, String name) {
		super(Block.Properties.create(Material.IRON).hardnessAndResistance(hardness, blastResistance).sound(sound)
			.lightValue(8));

		this.setDefaultState(this.getStateContainer().getBaseState() .with(FurnitureBlock.DIRECTION, Direction.NORTH));
		this.generateShapes(this.getStateContainer().getValidStates());
		this.setRegistryName(name);
		this.flameParticle = RedstoneParticleData.REDSTONE_DUST;
	}
}
