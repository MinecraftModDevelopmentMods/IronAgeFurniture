package zone.moddev.mc.ironagefurniture.api.blocks.lightsource.glow;

import zone.moddev.mc.ironagefurniture.api.blocks.base.FurnitureBlock;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.torch.LightSourceSconceTorchWall;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.Direction;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import zone.moddev.mc.ironagefurniture.BlockObjectHolder;

public class LightSourceSconceGlowWall extends LightSourceSconceTorchWall {
	public LightSourceSconceGlowWall(Block.Properties properties) {
		super(properties);

		this.setDefaultState(this.getStateContainer().getBaseState() .with(FurnitureBlock.DIRECTION, Direction.NORTH) .with(FurnitureBlock.WATERLOGGED, false));
		this.generateShapes(this.getStateContainer().getValidStates());
	}

	@Override
	protected boolean HasFlame() {
		return false;
	}

	@Override
	protected boolean CanEx() {
		return false;
	}

	@Override
	protected Block LightDrop() {
		return BlockObjectHolder.light_metal_ironage_block_floor_glow_clear;
	}

	@Override
	protected void generateShapes(ImmutableList<BlockState> states) {
		ImmutableMap.Builder<BlockState, VoxelShape> builder = new ImmutableMap.Builder<>();
		for (BlockState state : states) {
			VoxelShape shapes = VoxelShapes.empty();

			shapes = VoxelShapes.combine(shapes, FurnitureBlock.getShapes(FurnitureBlock.rotate(Block.makeCuboidShape(6, 9, 9, 10, 10, 16), Direction.SOUTH))[state .get(FurnitureBlock.DIRECTION).getHorizontalIndex()], IBooleanFunction.OR); // sconce holder
			shapes = VoxelShapes.combine(shapes, FurnitureBlock.getShapes(FurnitureBlock.rotate(Block.makeCuboidShape(6, 5, 9, 10, 11, 13), Direction.SOUTH))[state .get(FurnitureBlock.DIRECTION).getHorizontalIndex()], IBooleanFunction.OR); // torch

			builder.put(state, shapes.simplify());
		}

		_shapes = builder.build();
	}

	public LightSourceSconceGlowWall(float hardness, float blastResistance, SoundType sound, String name) {
		super(Block.Properties.create(Material.IRON).hardnessAndResistance(hardness, blastResistance).sound(sound).lightValue(14));

		this.setDefaultState(this.getStateContainer().getBaseState() .with(FurnitureBlock.DIRECTION, Direction.NORTH));
		this.generateShapes(this.getStateContainer().getValidStates());
		this.setRegistryName(name);
	}
}
