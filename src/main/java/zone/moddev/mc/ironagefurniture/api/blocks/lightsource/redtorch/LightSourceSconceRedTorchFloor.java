package zone.moddev.mc.ironagefurniture.api.blocks.lightsource.redtorch;

import zone.moddev.mc.ironagefurniture.api.blocks.base.FurnitureBlock;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.torch.LightSourceSconceTorchFloor;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.ILiquidContainer;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.state.properties.BlockStateProperties;
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

public class LightSourceSconceRedTorchFloor extends LightSourceSconceTorchFloor implements ILiquidContainer {
	protected static final Map<IBlockReader, List<LightSourceSconceRedTorchFloor.Toggle>> RECENT_TOGGLES = new WeakHashMap<>();
	public static final int RECENT_TOGGLE_TIMER = 60;
	public static final int MAX_RECENT_TOGGLES = 8;
	public static final int RESTART_DELAY = 160;

	@Override
	protected boolean CanEx() {
		return false;
	}

	@Override
	protected Block LightDrop() {
		return Blocks.REDSTONE_TORCH;
	}

	public LightSourceSconceRedTorchFloor(AbstractBlock.Properties properties) {
		super(properties);

		this.registerDefaultState(this.getStateDefinition().any().setValue(FurnitureBlock.DIRECTION, Direction.NORTH).setValue(FurnitureBlock.WATERLOGGED, false));
        this.generateShapes(this.getStateDefinition().getPossibleStates());
        this.flameParticle = RedstoneParticleData.REDSTONE;
	}

	public LightSourceSconceRedTorchFloor(float hardness, float blastResistance, SoundType sound, String name) {
		super(Block.Properties.of(Material.METAL).strength(hardness, blastResistance).sound(sound).lightLevel((p_50886_) -> 8) );

		this.registerDefaultState(this.getStateDefinition().any().setValue(FurnitureBlock.DIRECTION, Direction.NORTH));
		this.generateShapes(this.getStateDefinition().getPossibleStates());
		this.setRegistryName(name);
		this.flameParticle = RedstoneParticleData.REDSTONE;
	}

	@Override
	public void animateTick(BlockState state, World level, BlockPos pos, Random rand) {
		if (HasFlame()) {
			double d0 = (double)pos.getX() + 0.5D + (rand.nextDouble() - 0.5D) * 0.2D;
	         double d1 = (double)pos.getY() + 0.7D + (rand.nextDouble() - 0.5D) * 0.2D;
	         double d2 = (double)pos.getZ() + 0.5D + (rand.nextDouble() - 0.5D) * 0.2D;
	         level.addParticle(this.flameParticle, d0, d1, d2, 0.0D, 0.0D, 0.0D);
		}
	}

	@Override
	public void onPlace(BlockState state, World level, BlockPos pos, BlockState state2, boolean flag) {
		for (Direction direction : Direction.values()) {
			level.updateNeighborsAt(pos.relative(direction), this);
		}
	}

	@Override
	public void onRemove(BlockState state, World level, BlockPos pos, BlockState state2, boolean flag) {
		if (flag)
			return;

		for (Direction direction : Direction.values())
			level.updateNeighborsAt(pos.relative(direction), this);
	}

	@Override
	public int getSignal(BlockState state, IBlockReader getter, BlockPos pos, Direction direction) {
		return Direction.UP != direction ? 15 : 0;
	}

	protected boolean hasNeighborSignal(World level, BlockPos pos, BlockState state) {
		return level.hasSignal(pos.below(), Direction.DOWN);
	}

	@Override
	public void tick(BlockState state, ServerWorld level, BlockPos pos, Random rnd) {
		boolean flag = this.hasNeighborSignal(level, pos, state);
		List<LightSourceSconceRedTorchFloor.Toggle> list = RECENT_TOGGLES.get(level);

		while (list != null && !list.isEmpty() && level.getGameTime() - (list.get(0)).when > 60L) {
			list.remove(0);
		}

		if (flag) {
			level.setBlock(pos,
					BlockObjectHolder.light_metal_ironage_sconce_floor_redtorch_iron_unlit.defaultBlockState()
							.setValue(FurnitureBlock.DIRECTION, state.getValue(BlockStateProperties.HORIZONTAL_FACING))
							.setValue(FurnitureBlock.WATERLOGGED, state.getValue(BlockStateProperties.WATERLOGGED)),
					3);

			if (isToggledTooFrequently(level, pos, true)) {
				level.levelEvent(1502, pos, 0);
				level.getBlockTicks().scheduleTick(pos, level.getBlockState(pos).getBlock(), 160);
			}
		}
	}

	@Override
	public boolean isSignalSource(BlockState state) {
		return true;
	}

	private static boolean isToggledTooFrequently(World level, BlockPos pos, boolean flag) {
		List<LightSourceSconceRedTorchFloor.Toggle> list = RECENT_TOGGLES.computeIfAbsent(level, (p_55680_) -> Lists.newArrayList());
		if (flag) list.add(new LightSourceSconceRedTorchFloor.Toggle(pos.immutable(), level.getGameTime()));

		int i = 0;

		for (int j = 0; j < list.size(); ++j) {
			LightSourceSconceRedTorchFloor.Toggle redstonetorchblock$toggle = list.get(j);
			if (redstonetorchblock$toggle.pos.equals(pos)) {
				i++;

				if (i >= 8)
					return true;
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

	@Override
	public void neighborChanged(BlockState state, World level, BlockPos pos, Block block, BlockPos blockPos,
								boolean flag) {
		boolean hasSignal = this.hasNeighborSignal(level, pos, state);
		boolean willTick = level.getBlockTicks().willTickThisTick(pos, this);

		if (hasSignal && !willTick) {
			level.getBlockTicks().scheduleTick(pos, this, 2);
		}
	}

	@Override
	public int getDirectSignal(BlockState state, IBlockReader getter, BlockPos pos, Direction direction) {
		return direction == Direction.DOWN ? state.getSignal(getter, pos, direction) : 0;
	}

	@Override
	protected Block UnlitVariant() {
		return BlockObjectHolder.light_metal_ironage_sconce_floor_redtorch_iron_unlit;
	}
}
