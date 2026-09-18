package zone.moddev.mc.ironagefurniture.api.blocks.furniture;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import zone.moddev.mc.ironagefurniture.api.blocks.base.FurnitureBlock;
import zone.moddev.mc.ironagefurniture.api.enumerations.BenchType;
import zone.moddev.mc.ironagefurniture.api.enumerations.Rotation;
import zone.moddev.mc.ironagefurniture.api.entity.Seat;
import zone.moddev.mc.ironagefurniture.api.properties.BenchTypeProperty;
import zone.moddev.mc.ironagefurniture.api.util.Swivel;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.util.Hand;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.IWorld;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.BlockState;
import net.minecraft.state.StateContainer;
import net.minecraft.fluid.Fluids;
import net.minecraft.block.material.Material;
import net.minecraft.world.storage.loot.LootContext.Builder;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.shapes.VoxelShape;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BackBench extends FurnitureBlock {
	protected static final int FIRE_SPREAD_SPEED = 5;
	protected static final int FLAMMABILITY = 20;

	public static final BenchTypeProperty TYPE = BenchTypeProperty.create("type", BenchType.SINGLE, BenchType.LEFT, BenchType.MIDDLE, BenchType.RIGHT);

	public BackBench(float hardness, float blastResistance, SoundType sound, String name) {
		super(Block.Properties.create(Material.WOOD).hardnessAndResistance(hardness, blastResistance).sound(sound));

		this.setDefaultState(this.getStateContainer().getBaseState() .with(DIRECTION, Direction.NORTH));
		this.generateShapes(this.getStateContainer().getValidStates());
		this.setRegistryName(name);
	}

	public BackBench(Properties properties) {
		super(properties);
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		super.fillStateContainer(builder);

		builder.add(TYPE);
	}

	@Override
	public boolean isFlammable(BlockState state, IBlockReader level, BlockPos pos, Direction direction) {
		return true;
	}

	@Override
	public int getFlammability(BlockState state, IBlockReader level, BlockPos pos, Direction direction) {
		return FLAMMABILITY;
	}

	@Override
	public int getFireSpreadSpeed(BlockState state, IBlockReader level, BlockPos pos, Direction direction) {
		return FIRE_SPREAD_SPEED;
	}

	@Override
	protected void generateShapes(ImmutableList<BlockState> states) {
		ImmutableMap.Builder<BlockState, VoxelShape> builder = new ImmutableMap.Builder<>();

		for (BlockState state : states) {
			BenchType type = state .get(TYPE);
			VoxelShape shapes = VoxelShapes.empty();
			shapes = VoxelShapes.combine(shapes, getShapes(rotate(Block.makeCuboidShape(0, 6, 1, 16, 7, 15), Direction.SOUTH))[state .get(DIRECTION).getHorizontalIndex()], IBooleanFunction.OR); // base
			shapes = VoxelShapes.combine(shapes, getShapes(rotate(Block.makeCuboidShape(0, 0, 0, 16, 16, 1), Direction.SOUTH))[state .get(DIRECTION).getHorizontalIndex()], IBooleanFunction.OR); // back

			switch (type) {
				case SINGLE:
					shapes = VoxelShapes.combine(shapes, getShapes(rotate(Block.makeCuboidShape(0, 0, 1, 1, 10, 12), Direction.SOUTH))[state .get(DIRECTION).getHorizontalIndex()], IBooleanFunction.OR);
					shapes = VoxelShapes.combine(shapes, getShapes(rotate(Block.makeCuboidShape(15, 0, 1, 16, 10, 12), Direction.SOUTH))[state .get(DIRECTION).getHorizontalIndex()], IBooleanFunction.OR);
					break;
				case LEFT:
					shapes = VoxelShapes.combine(shapes, getShapes(rotate(Block.makeCuboidShape(0, 0, 1, 1, 10, 12), Direction.SOUTH))[state .get(DIRECTION).getHorizontalIndex()], IBooleanFunction.OR);
					break;
				case RIGHT:
					shapes = VoxelShapes.combine(shapes, getShapes(rotate(Block.makeCuboidShape(15, 0, 1, 16, 10, 12), Direction.SOUTH))[state .get(DIRECTION).getHorizontalIndex()], IBooleanFunction.OR);
					break;
				default:
					break;
			}

			shapes = VoxelShapes.combine(shapes, getShapes(rotate(Block.makeCuboidShape(0, 2, 7, 16, 4, 9), Direction.SOUTH))[state .get(DIRECTION).getHorizontalIndex()], IBooleanFunction.OR);

			builder.put(state, shapes.simplify());
		}

		_shapes = builder.build();
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		World world = context.getWorld();
		BlockPos pos = context.getPos();

		BlockState stateForPlacement = this.getDefaultState()
			 .with(WATERLOGGED, world.getFluidState(context.getPos()).getFluid() == Fluids.WATER)
			 .with(DIRECTION, context.getPlacementHorizontalFacing())
			 .with(TYPE, BenchType.SINGLE);

		if (context.getPlayer().isSneaking())
			return stateForPlacement;

		Direction benchAxis = getBenchToJoinTo(context.getPlacementHorizontalFacing(), world, pos);

		if (benchAxis == null)
			return stateForPlacement;

		BlockState blockStateToJoinTo = world.getBlockState(pos .offset(benchAxis));

		if (blockStateToJoinTo.getBlock().getRegistryName().equals(stateForPlacement.getBlock().getRegistryName())) {
			Direction benchFacing = getBenchToJoinToFacing(benchAxis, world, pos);

			boolean defaultFacing = true;

			if (Objects.requireNonNull(getBenchType(blockStateToJoinTo)) == BenchType.SINGLE) {
				if (benchFacing == Direction.NORTH && benchAxis == Direction.EAST) {
					benchFacing = Swivel.Rotate(benchAxis, Rotation.TwoSeventy);
					defaultFacing = false;
				}
				if (benchFacing == Direction.EAST && benchAxis == Direction.SOUTH) {
					benchFacing = Swivel.Rotate(benchAxis, Rotation.TwoSeventy);
					defaultFacing = false;
				}
				if (benchFacing == Direction.SOUTH && benchAxis == Direction.WEST) {
					benchFacing = Swivel.Rotate(benchAxis, Rotation.TwoSeventy);
					defaultFacing = false;
				}
				if (benchFacing == Direction.WEST && benchAxis == Direction.NORTH) {
					benchFacing = Swivel.Rotate(benchAxis, Rotation.TwoSeventy);
					defaultFacing = false;
				}
				if (defaultFacing)
					benchFacing = Swivel.Rotate(benchAxis, Rotation.Ninty);
			}

			stateForPlacement = traceBench2(benchAxis, world, pos, stateForPlacement, benchFacing);
		}

		return stateForPlacement;
	}

	private boolean isIAFBench(BlockState blockstate) {
		ResourceLocation resource = blockstate.getBlock().getRegistryName();

		return resource.getNamespace().equals("ironagefurniture") && resource.getPath().contains("bench");
	}

	protected BenchType getBenchType(BlockState blockstate) {
		if (blockstate == null)
			return null;

		if (isIAFBench(blockstate))
			return blockstate .get(TYPE);

		return null;
	}

	private Direction getBenchDirection(BlockState blockstate) {
		if (isIAFBench(blockstate))
			return blockstate .get(DIRECTION);

		return null;
	}

	private int getOffset(Direction direction, IWorld world, BlockPos pos) {
		BenchType currentlyInspectedBenchType;
		BlockState currentlyInspectedBenchState;
		String currentlyInspectedBlockName;

		int offset = 0;

		currentlyInspectedBenchState = world.getBlockState(pos .offset(direction));
		currentlyInspectedBenchType = getBenchType(currentlyInspectedBenchState);

		if (!isBenchPiece(currentlyInspectedBenchType))
			return offset;

		Direction blockFacing = Swivel.Rotate(getBenchDirection(currentlyInspectedBenchState), Rotation.Ninty);
		String blockName = currentlyInspectedBenchState.getBlock().getRegistryName().getNamespace();
		currentlyInspectedBlockName = blockName;

		while (isBenchPieceOnAxis(currentlyInspectedBenchType, direction, blockFacing, blockName, currentlyInspectedBlockName)) {
			offset++;

			currentlyInspectedBenchState = world.getBlockState(pos .offset(direction, offset + 1));
			currentlyInspectedBenchType = getBenchType(currentlyInspectedBenchState);
			currentlyInspectedBlockName = currentlyInspectedBenchState.getBlock().getRegistryName().getNamespace();

			if (isBenchPiece(currentlyInspectedBenchType))
				blockFacing = Swivel.Rotate(getBenchDirection(currentlyInspectedBenchState), Rotation.Ninty);
		}
		return offset;
	}

	private BlockState traceBench2(Direction direction, IWorld world, BlockPos pos, BlockState blockState, Direction benchFacing) {
		boolean invertLeftRight = benchFacing == Direction.NORTH && direction == Direction.EAST;

		if (benchFacing == Direction.EAST && direction == Direction.SOUTH)
			invertLeftRight = true;

		if (benchFacing == Direction.SOUTH && direction == Direction.WEST)
			invertLeftRight = true;

		if (benchFacing == Direction.WEST && direction == Direction.NORTH)
			invertLeftRight = true;

		BenchType left = BenchType.LEFT;
		BenchType right = BenchType.RIGHT;

		if (invertLeftRight) {
			left = BenchType.RIGHT;
			right = BenchType.LEFT;
		}

		BlockPos workingBlockPos = pos;

		int positiveOffset = getOffset(direction.getOpposite(), world, pos);
		int negativeOffset = getOffset(direction, world, pos);

		int workingPositiveOffset = positiveOffset;
		int workingNegativeOffset = negativeOffset;

		if (positiveOffset == 0 && negativeOffset == 0)
			return blockState
				 .with(DIRECTION, benchFacing)
				 .with(TYPE, BenchType.SINGLE);

		while (workingNegativeOffset > 0) {
			workingBlockPos = pos .offset(direction, workingNegativeOffset);

			if (workingNegativeOffset == negativeOffset)
				world.setBlockState(workingBlockPos, world.getBlockState(workingBlockPos)
					 .with(DIRECTION, benchFacing)
					 .with(TYPE, left), 0);
			else
				world.setBlockState(workingBlockPos, world.getBlockState(workingBlockPos)
					 .with(DIRECTION, benchFacing)
					 .with(TYPE, BenchType.MIDDLE), 0);

			workingNegativeOffset--;
		}

		if (negativeOffset > 0 && positiveOffset == 0)
			return blockState
				 .with(DIRECTION, benchFacing)
				 .with(TYPE, right);

		while (workingPositiveOffset > 0) {
			workingBlockPos = pos .offset(direction.getOpposite(), workingPositiveOffset);

			if (workingPositiveOffset == positiveOffset)
				world.setBlockState(workingBlockPos, world.getBlockState(workingBlockPos)
					 .with(DIRECTION, benchFacing)
					 .with(TYPE, right), 0);
			else
				world.setBlockState(workingBlockPos, world.getBlockState(workingBlockPos)
					 .with(DIRECTION, benchFacing)
					 .with(TYPE, BenchType.MIDDLE), 0);

			workingPositiveOffset--;
		}

		if (positiveOffset > 0 && negativeOffset == 0)
			return blockState
				 .with(DIRECTION, benchFacing)
				 .with(TYPE, left);

		return blockState
			 .with(DIRECTION, benchFacing)
			 .with(TYPE, BenchType.MIDDLE);
	}

	private boolean isBenchPiece(BenchType benchType) {
		return benchType == BenchType.SINGLE ||
			benchType == BenchType.MIDDLE ||
			benchType == BenchType.LEFT ||
			benchType == BenchType.RIGHT;
	}

	private boolean isBenchPieceOnAxis(BenchType benchType, Direction benchAxis, Direction blockAxis, String blockName, String currentBlockName) {
		if (!blockName.equals(currentBlockName))
			return false;

		if (benchType == BenchType.SINGLE)
			return true;

		if (benchType == BenchType.MIDDLE || benchType == BenchType.LEFT || benchType == BenchType.RIGHT)
			return benchAxis == blockAxis || benchAxis == blockAxis.getOpposite();

		return false;
	}

	private Rotation GetOpposite(Rotation rotation) {
		switch (rotation) {
			case Ninty:
				return Rotation.TwoSeventy;
			case OneEighty:
				return Rotation.Zero;
			case TwoSeventy:
				return Rotation.Ninty;
			case Zero:
				return Rotation.OneEighty;
			default:
				throw new IllegalArgumentException();
		}
	}

	private boolean isBenchEnd(Direction facing, World world, BlockPos pos) {
		BenchType benchType = getBenchType(world.getBlockState(pos .offset(facing)));

		return benchType == BenchType.LEFT || benchType == BenchType.RIGHT;
	}

	private boolean isBenchSingle(Direction facing, World world, BlockPos pos) {
		return getBenchType(world.getBlockState(pos .offset(facing))) == BenchType.SINGLE;
	}

	private Direction getBenchToJoinToFacing(Direction benchDirection, World world, BlockPos pos) {
		return getBenchDirection(world.getBlockState(pos .offset(benchDirection)));
	}

	private Direction getBenchToJoinTo(Direction playerFacing, World world, BlockPos pos) {
		// again, favour player facing
		if (isBenchEnd(playerFacing, world, pos) || isBenchSingle(playerFacing, world, pos)) {
			if (isBenchSingle(playerFacing, world, pos))
				return playerFacing;

			Direction targetFace = getBenchDirection(world.getBlockState(pos .offset(playerFacing)));

			if (targetFace == null)
				return null;

			targetFace = Swivel.Rotate(targetFace, Rotation.Ninty);

			if (isBenchEnd(playerFacing, world, pos) && (targetFace == playerFacing || targetFace == playerFacing.getOpposite()))
				return playerFacing;
		}

		for (Direction face : Direction.values()) {
			if (face == Direction.UP || face == Direction.DOWN)
				continue;

			if (isBenchSingle(face, world, pos))
				return face;

			if (!isBenchEnd(face, world, pos))
				continue;

			Direction targetFace = getBenchDirection(world.getBlockState(pos .offset(face)));

			if (targetFace == null)
				return null;

			targetFace = Swivel.Rotate(targetFace, Rotation.Ninty);

			if ((targetFace == face || targetFace == face.getOpposite()))
				return face;
		}

		return null;
	}

	@Override
	public void onPlayerDestroy(IWorld worldIn, BlockPos pos, BlockState state) {

		Direction benchFacing = getBenchDirection(state);
		Rotation defaultRotation = Rotation.Ninty;
		Direction benchAxis = Swivel.Rotate(getBenchDirection(state), defaultRotation);
		BenchType benchType = getBenchType(state);

		super.onPlayerDestroy(worldIn, pos, state);

		if ((benchAxis == null || benchType == null) || (benchType == BenchType.SINGLE))
			return;

		BlockState currentOffsetBlockState = worldIn.getBlockState(pos .offset(benchAxis));
		BenchType currentOffsetType = getBenchType(currentOffsetBlockState);

		if (currentOffsetType != null) {
			Direction currentOffsetFacing = Swivel.Rotate(getBenchDirection(currentOffsetBlockState), defaultRotation);

			if (currentOffsetFacing == benchAxis || currentOffsetFacing == benchAxis.getOpposite()) {
				BlockState offsetBlockState = traceBench2(benchAxis, worldIn, pos .offset(benchAxis), worldIn.getBlockState(pos .offset(benchAxis)), benchFacing);

				if (isBenchPiece(getBenchType(offsetBlockState)))
					worldIn.setBlockState(pos .offset(benchAxis), offsetBlockState, 0);
			}
		}

		BlockState reverseCurrentOffsetBlockState = worldIn.getBlockState(pos .offset(benchAxis.getOpposite()));
		BenchType reverseOffsetType = getBenchType(reverseCurrentOffsetBlockState);

		if (reverseOffsetType == null)
			return;

		Direction reverseOffsetFacing = Swivel.Rotate(getBenchDirection(reverseCurrentOffsetBlockState), GetOpposite(defaultRotation));

		if (reverseOffsetFacing != benchAxis && reverseOffsetFacing != benchAxis.getOpposite())
			return;

		BlockState reverseOffsetBlockState = traceBench2(benchAxis.getOpposite(), worldIn, pos .offset(benchAxis.getOpposite()), worldIn.getBlockState(pos .offset(benchAxis.getOpposite())), benchFacing);

		if (isBenchPiece(getBenchType(reverseOffsetBlockState)))
			worldIn.setBlockState(pos .offset(benchAxis.getOpposite()), reverseOffsetBlockState, 0);
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, Builder builder) {
		List<ItemStack> drops;

		Item item = state.getBlock().asItem();
		ItemStack stack = new ItemStack(item, 1);
		drops = new ArrayList<>();
		drops.add(stack);

		return drops;
	}

	@Override
	public int getComparatorInputOverride(BlockState state, World world, BlockPos pos) {
		return Container.calcRedstone(world.getTileEntity(pos));
	}

	@Override
	public boolean hasComparatorInputOverride(BlockState state) {
		return state.getBlock() instanceof ITileEntityProvider;
	}

	@Override
	public void onReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
		if (state.getBlock() != newState.getBlock() && world.getTileEntity(pos) instanceof IInventory) {
			InventoryHelper.dropInventoryItems(world, pos, (IInventory) world.getTileEntity(pos));
			world .updateComparatorOutputLevel(pos, this);
		}

		super.onReplaced(state, world, pos, newState, isMoving);
	}

	@Override
	public boolean onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult) {
		return Seat.create(world, pos, 0.3, player);
	}
}
