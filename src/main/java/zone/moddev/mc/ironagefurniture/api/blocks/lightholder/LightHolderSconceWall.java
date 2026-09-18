package zone.moddev.mc.ironagefurniture.api.blocks.lightholder;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import zone.moddev.mc.ironagefurniture.BlockObjectHolder;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.world.storage.loot.LootContext.Builder;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;

public class LightHolderSconceWall extends LightHolderSconceFloor {
	public LightHolderSconceWall(float hardness, float blastResistance, SoundType sound, String name) {
		super(Block.Properties.create(Material.IRON).hardnessAndResistance(hardness, blastResistance).sound(sound));

		this.setDefaultState(this.getStateContainer().getBaseState() .with(DIRECTION, Direction.NORTH));
		this.generateShapes(this.getStateContainer().getValidStates());
		this.setRegistryName(name);
	}

	@Override
	public BlockState updatePostPlacement(BlockState state, Direction direction, BlockState state2, IWorld level, BlockPos pos1, BlockPos pos2) {
		return direction.getOpposite() == state .get(DIRECTION) && !state.isValidPosition(level, pos1) ? Blocks.AIR.getDefaultState() : state;
	}

	@Override
	public boolean isValidPosition(BlockState state, IWorldReader levelReader, BlockPos pos) {
	    Direction direction = state .get(DIRECTION);
	    BlockPos adjacentPos = pos .offset(direction.getOpposite());
	    BlockState adjacentState = levelReader.getBlockState(adjacentPos);

	    boolean isSturdy = Block.hasSolidSide(adjacentState, levelReader, adjacentPos, direction.getOpposite());

	    return isSturdy;
	}

	@Override
	protected void generateShapes(ImmutableList<BlockState> states) {
		ImmutableMap.Builder<BlockState, VoxelShape> builder = new ImmutableMap.Builder<>();
		for (BlockState state : states) {
			VoxelShape shapes = VoxelShapes.empty();

			shapes = VoxelShapes.combine(shapes, getShapes(rotate(Block.makeCuboidShape(1.5, 9, 5.5, 6.5, 10, 10.5), Direction.EAST))[state .get(DIRECTION).getHorizontalIndex()], IBooleanFunction.OR); // sconce holder

			builder.put(state, shapes.simplify());
		}

		_shapes = builder.build();
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, Builder builder) {
		List<ItemStack> drops;
		drops = new ArrayList<ItemStack>();

		Item item = BlockObjectHolder.light_metal_ironage_sconce_floor_empty_iron.asItem();
		ItemStack stack = new ItemStack(item, 1);

		drops.add(stack);

		return drops;
	}

	@Override
	protected Block GetGlowVariant() {
		return BlockObjectHolder.light_metal_ironage_sconce_wall_glow_iron;
	}

	@Override
	protected Block GetTorchVariant() {
		return BlockObjectHolder.light_metal_ironage_sconce_wall_torch_iron;
	}

	@Override
	protected Block GetLavaVariant() {
		return BlockObjectHolder.light_metal_ironage_sconce_wall_lava_iron;
	}

	@Override
	protected Block GetUnlitTorchVariant() {
		return BlockObjectHolder.light_metal_ironage_sconce_wall_torch_iron_unlit;
	}

	@Override
	protected Block GetRedTorchVariant() {
		return BlockObjectHolder.light_metal_ironage_sconce_wall_redtorch_iron;
	}

	@Override
	protected Block GetRedVariant() {
		return BlockObjectHolder.light_metal_ironage_sconce_wall_red_iron;
	}

}
