package zone.moddev.mc.ironagefurniture.api.blocks.furniture;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import zone.moddev.mc.ironagefurniture.api.blocks.base.FallingFurnitureBlock;
import zone.moddev.mc.ironagefurniture.api.entity.Seat;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.Direction;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.util.Hand;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.block.SoundType;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.world.storage.loot.LootContext.Builder;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.shapes.VoxelShape;

import java.util.ArrayList;
import java.util.List;

public class Chair extends FallingFurnitureBlock implements IWaterLoggable {
	protected static final int FIRE_SPREAD_SPEED = 5;
	protected static final int FLAMMABILITY = 20;

	@Override
	public List<ItemStack> getDrops(BlockState state, Builder builder) {
		List<ItemStack> drops;

		Item item = state.getBlock().asItem();
		ItemStack stack = new ItemStack(item, 1);
		drops = new ArrayList<ItemStack>();
		drops.add(stack);

		return drops;
	}

	@Override
	public int getComparatorInputOverride(BlockState state, World world, BlockPos pos) {
		return Container.calcRedstone(world.getTileEntity(pos));
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

	public Chair(Properties properties) {
		super(properties);

		this.setDefaultState(this.getStateContainer().getBaseState() .with(DIRECTION, Direction.NORTH) .with(WATERLOGGED, false));
		this.generateShapes(this.getStateContainer().getValidStates());
	}

	public Chair(float hardness, float blastResistance, SoundType sound, String name) {
		super(Block.Properties.create(Material.WOOD).hardnessAndResistance(hardness, blastResistance).sound(sound));

		this.setDefaultState(this.getStateContainer().getBaseState() .with(DIRECTION, Direction.NORTH));
		this.generateShapes(this.getStateContainer().getValidStates());
		this.setRegistryName(name);
	}

	protected void generateShapes(ImmutableList<BlockState> states) {
		ImmutableMap.Builder<BlockState, VoxelShape> builder = new ImmutableMap.Builder<>();
		for (BlockState state : states) {
			VoxelShape shapes = VoxelShapes.empty();

			// chair body
			shapes = VoxelShapes.combine(shapes, getShapes(rotate(Block.makeCuboidShape(1, 7, 1, 15, 8, 14), Direction.SOUTH))[state .get(DIRECTION).getHorizontalIndex()], IBooleanFunction.OR); // chair base
			shapes = VoxelShapes.combine(shapes, getShapes(rotate(Block.makeCuboidShape(3, 9, 1, 13, 23, 2), Direction.SOUTH))[state .get(DIRECTION).getHorizontalIndex()], IBooleanFunction.OR); // chair back

			//legs
			shapes = VoxelShapes.combine(shapes, getShapes(rotate(Block.makeCuboidShape(2, 0, 12, 3, 8, 13), Direction.SOUTH))[state .get(DIRECTION).getHorizontalIndex()], IBooleanFunction.OR); //front left leg
			shapes = VoxelShapes.combine(shapes, getShapes(rotate(Block.makeCuboidShape(13, 0, 12, 14, 8, 13), Direction.SOUTH))[state .get(DIRECTION).getHorizontalIndex()], IBooleanFunction.OR); // front right leg
			shapes = VoxelShapes.combine(shapes, getShapes(rotate(Block.makeCuboidShape(1, 0, 1, 3, 22, 3), Direction.SOUTH))[state .get(DIRECTION).getHorizontalIndex()], IBooleanFunction.OR); // back left leg
			shapes = VoxelShapes.combine(shapes, getShapes(rotate(Block.makeCuboidShape(13, 0, 1, 15, 22, 3), Direction.SOUTH))[state .get(DIRECTION).getHorizontalIndex()], IBooleanFunction.OR); // back right leg

			builder.put(state, shapes.simplify());
		}

		_shapes = builder.build();
	}

	@Override
	public boolean onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult) {
		return Seat.create(world, pos, 0.3, player);
	}
}
