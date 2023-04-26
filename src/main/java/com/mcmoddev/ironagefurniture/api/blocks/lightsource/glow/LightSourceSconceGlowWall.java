package com.mcmoddev.ironagefurniture.api.blocks.lightsource.glow;

import com.mcmoddev.ironagefurniture.api.blocks.base.FurnitureBlock;
import com.mcmoddev.ironagefurniture.api.blocks.lightsource.torch.LightSourceSconceTorchWall;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.core.Direction;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mcmoddev.ironagefurniture.BlockObjectHolder;

public class LightSourceSconceGlowWall extends LightSourceSconceTorchWall {
	public LightSourceSconceGlowWall(BlockBehaviour.Properties properties) {
		super(properties);

		this.registerDefaultState(this.getStateDefinition().any().setValue(FurnitureBlock.DIRECTION, Direction.NORTH).setValue(FurnitureBlock.WATERLOGGED, false));
		this.generateShapes(this.getStateDefinition().getPossibleStates());
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
			VoxelShape shapes = Shapes.empty();

			shapes = Shapes.joinUnoptimized(shapes, FurnitureBlock.getShapes(FurnitureBlock.rotate(Block.box(6, 9, 9, 10, 10, 16), Direction.SOUTH))[state.getValue(FurnitureBlock.DIRECTION).get2DDataValue()], BooleanOp.OR); // sconce holder
			shapes = Shapes.joinUnoptimized(shapes, FurnitureBlock.getShapes(FurnitureBlock.rotate(Block.box(6, 5, 9, 10, 11, 13), Direction.SOUTH))[state.getValue(FurnitureBlock.DIRECTION).get2DDataValue()], BooleanOp.OR); // torch

			builder.put(state, shapes.optimize());
		}

		_shapes = builder.build();
	}

	public LightSourceSconceGlowWall(float hardness, float blastResistance, SoundType sound, String name) {
		super(Block.Properties.of(Material.METAL).strength(hardness, blastResistance).sound(sound).lightLevel((p_50886_) -> 14));

		this.registerDefaultState(this.getStateDefinition().any().setValue(FurnitureBlock.DIRECTION, Direction.NORTH));
		this.generateShapes(this.getStateDefinition().getPossibleStates());
		this.setRegistryName(name);
	}
}
