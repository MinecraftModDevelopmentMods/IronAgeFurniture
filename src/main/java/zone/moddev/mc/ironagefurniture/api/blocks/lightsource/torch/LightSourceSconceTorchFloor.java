package zone.moddev.mc.ironagefurniture.api.blocks.lightsource.torch;

import zone.moddev.mc.ironagefurniture.api.blocks.lightholder.LightHolderSconceFloor;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.ILiquidContainer;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.IFluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.storage.loot.LootContext.Builder;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.util.Hand;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import zone.moddev.mc.ironagefurniture.BlockObjectHolder;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.IWorld;

public class LightSourceSconceTorchFloor extends LightHolderSconceFloor implements ILiquidContainer {
	protected static final VoxelShape AxisAlignedBB = Block.box(6.0D, 0.0D, 6.0D, 10.0D, 13.0D, 10.0D);
	protected IParticleData flameParticle;

	@Override
	public List<ItemStack> getDrops(BlockState state, Builder builder) {
		List<ItemStack> drops;
		drops = new ArrayList<>();

		Item sconceItem = DropVariant().asItem();
		ItemStack sconceStack = new ItemStack(sconceItem, 1);
		drops.add(sconceStack);

		if (ShouldDrop()) {
			Item lightItem = LightDrop().asItem();
			ItemStack lightStack = new ItemStack(lightItem, 1);

			drops.add(lightStack);
		}

		return drops;
	}

	protected Block LightDrop() {
		return Blocks.TORCH;
	}

	protected boolean ShouldDrop() {
		return true;
	}

	public LightSourceSconceTorchFloor(Properties properties) {
		super(properties);

		this.registerDefaultState(this.getStateDefinition().any().setValue(DIRECTION, Direction.NORTH).setValue(WATERLOGGED, false));
		this.generateShapes(this.getStateDefinition().getPossibleStates());
		this.flameParticle = ParticleTypes.FLAME;
	}

	public LightSourceSconceTorchFloor(float hardness, float blastResistance, SoundType sound, String name) {
		super(Block.Properties.of(Material.METAL).strength(hardness, blastResistance).sound(sound).lightLevel(14));

		this.registerDefaultState(this.getStateDefinition().any().setValue(DIRECTION, Direction.NORTH));
		this.generateShapes(this.getStateDefinition().getPossibleStates());
		this.setRegistryName(name);
		this.flameParticle = ParticleTypes.FLAME;
	}

	@Override
	protected void generateShapes(ImmutableList<BlockState> states) {
		ImmutableMap.Builder<BlockState, VoxelShape> builder = new ImmutableMap.Builder<>();
		for (BlockState state : states)
			builder.put(state, AxisAlignedBB);

		_shapes = builder.build();
	}

	@Override
	public boolean canConnectRedstone(BlockState state, IBlockReader world, BlockPos pos, Direction direction) {
		return true;
	}

	@Override
	public void animateTick(BlockState state, World level, BlockPos pos, Random random) {
		if (HasFlame()) {
			double d0 = (double) pos.getX() + 0.5D;
			double d1 = (double) pos.getY() + 0.9D;
			double d2 = (double) pos.getZ() + 0.5D;

			level.addParticle(ParticleTypes.SMOKE, d0, d1, d2, 0.0D, 0.0D, 0.0D);
			level.addParticle(this.flameParticle, d0, d1, d2, 0.0D, 0.0D, 0.0D);
		}
	}

	@Override
	public void tick(BlockState state, ServerWorld level, BlockPos pos, Random rnd) {
		if (CanEx() && !level.hasNeighborSignal(pos)) {
			if (state.getValue(BlockStateProperties.WATERLOGGED))
				Unlight(state, level, pos);
		}
		super.tick(state, level, pos, rnd);
	}

	@Override
	public void neighborChanged(BlockState state, World level, BlockPos pos, Block block, BlockPos blockPos,
								boolean flag) {
	    boolean hasSignal = this.hasNeighborSignal(level, pos, state);
	    boolean willTick = level.getBlockTicks().willTickThisTick(pos, this);

	    if (!hasSignal) {
	        if (!willTick) {
	            level.getBlockTicks().scheduleTick(pos, this, 2);
	        }
	    }
	}

	protected boolean hasNeighborSignal(World level, BlockPos pos, BlockState state) {
		for (Direction direction : Direction.values()) {
	        if (direction != Direction.UP && level.hasSignal(pos.relative(direction), direction)) {
	            return true;
	        }
	    }
	    return false;
	}

	private void Unlight(BlockState state, World world, BlockPos pos) {
		if (!CanEx())
			return;
		world.getBlockState(pos);
		world.setBlock(pos, UnlitVariant().defaultBlockState()
			.setValue(DIRECTION, state.getValue(BlockStateProperties.HORIZONTAL_FACING))
			.setValue(WATERLOGGED, state.getValue(BlockStateProperties.WATERLOGGED)), 3);

		world.sendBlockUpdated(pos, state, world.getBlockState(pos), 3);
	}

	@Override
	public boolean placeLiquid(IWorld world, BlockPos pos, BlockState blockState, IFluidState fluidState) {
		boolean success = super.placeLiquid(world, pos, blockState, fluidState);

		if (!blockState.getValue(BlockStateProperties.WATERLOGGED) && fluidState.getType() == Fluids.WATER) {
			if (world.isClientSide())
				return success;

			if (CanEx()) world.setBlock(pos, UnlitVariant().defaultBlockState()
				.setValue(DIRECTION, blockState.getValue(BlockStateProperties.HORIZONTAL_FACING))
				.setValue(WATERLOGGED, Boolean.valueOf(true)), 3);

			world.getLiquidTicks().scheduleTick(pos, fluidState.getType(), fluidState.getType().getTickDelay(world));
		} else {
			if (CanEx()) world.setBlock(pos, UnlitVariant().defaultBlockState()
				.setValue(DIRECTION, blockState.getValue(BlockStateProperties.HORIZONTAL_FACING))
				.setValue(WATERLOGGED, blockState.getValue(BlockStateProperties.WATERLOGGED)), 3);
		}

		return success;
	}

	protected Block UnlitVariant() {
		return BlockObjectHolder.light_metal_ironage_sconce_floor_torch_iron_unlit;
	}

	protected Block EmptyVariant() {
		return BlockObjectHolder.light_metal_ironage_sconce_floor_empty_iron;
	}

	protected Block DropVariant() {
		return EmptyVariant();
	}

	protected boolean CanEx() {
		return true;
	}

	protected boolean HasFlame() {
		return true;
	}

	protected boolean InvertDirection() {
		return false;
	}

	@Override
	protected ActionResultType ActivateSconce(BlockState state, World world, BlockPos pos, PlayerEntity player,
											   Hand hand, BlockRayTraceResult rayTraceResult) {

		ItemStack stackInHand = player.getItemInHand(hand);

		if (CanEx() && stackInHand.getItem() == Items.WATER_BUCKET) {
			world.setBlock(pos, UnlitVariant().defaultBlockState()
				.setValue(DIRECTION, state.getValue(BlockStateProperties.HORIZONTAL_FACING))
				.setValue(WATERLOGGED, state.getValue(BlockStateProperties.WATERLOGGED)), 3);

			return ActionResultType.SUCCESS;
		}

		if (stackInHand.getItem() == LightDrop().asItem() || stackInHand.isEmpty()) {
			Direction direction = state.getValue(BlockStateProperties.HORIZONTAL_FACING);

			if (InvertDirection())
				direction = direction.getOpposite();

			world.setBlock(pos, EmptyVariant().defaultBlockState()
				.setValue(DIRECTION, direction)
				.setValue(WATERLOGGED, state.getValue(BlockStateProperties.WATERLOGGED)), 3);

			if (!player.isCreative()) {
				if (stackInHand.getItem() == LightDrop().asItem())
					stackInHand.setCount(stackInHand.getCount() + 1);
				else {
					Item itemTorch = LightDrop().asItem();
					ItemStack stackTorch = new ItemStack(itemTorch, 1);

					player.setItemInHand(hand, stackTorch);
				}
			}

			return ActionResultType.CONSUME;
		}

		return ActionResultType.FAIL;
	}
}
