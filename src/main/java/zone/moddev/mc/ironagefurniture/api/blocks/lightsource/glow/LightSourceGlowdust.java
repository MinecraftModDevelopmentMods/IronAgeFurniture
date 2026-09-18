package zone.moddev.mc.ironagefurniture.api.blocks.lightsource.glow;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import zone.moddev.mc.ironagefurniture.api.blocks.base.FallingFurnitureBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.Direction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockReader;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.world.storage.loot.LootContext.Builder;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.shapes.VoxelShape;

public class LightSourceGlowdust extends FallingFurnitureBlock {
	@Override
	public boolean propagatesSkylightDown(BlockState state, IBlockReader getter, BlockPos pos) {
		return true;
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

	public LightSourceGlowdust(Properties properties) {
		super(properties);

		this.setDefaultState(this.getStateContainer().getBaseState() .with(DIRECTION, Direction.NORTH) .with(WATERLOGGED, false));
		this.generateShapes(this.getStateContainer().getValidStates());
	}

	public LightSourceGlowdust(float hardness, float blastResistance, SoundType sound, String name) {
		super(Block.Properties.create(Material.IRON).hardnessAndResistance(hardness, blastResistance).sound(sound).lightValue(15));

		this.setDefaultState(this.getStateContainer().getBaseState() .with(DIRECTION, Direction.NORTH));
		this.generateShapes(this.getStateContainer().getValidStates());
		this.setRegistryName(name);
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
	public boolean canConnectRedstone(BlockState state, IBlockReader world, BlockPos pos, Direction direction) {
		return false;
	}
}
