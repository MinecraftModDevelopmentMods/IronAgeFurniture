package com.mcmoddev.ironagefurniture.api.blocks.lightsource.glow;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import com.mcmoddev.ironagefurniture.api.blocks.base.FallingFurnitureBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.storage.loot.LootContext.Builder;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class LightSourceGlowdust extends FallingFurnitureBlock {
	@Override
	public float getShadeBrightness(BlockState state, BlockGetter getter, BlockPos pos) {
		return 1.0F;
	}

	@Override
	public boolean propagatesSkylightDown(BlockState state, BlockGetter getter, BlockPos pos) {
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

		this.registerDefaultState(this.getStateDefinition().any().setValue(DIRECTION, Direction.NORTH).setValue(WATERLOGGED, false));
		this.generateShapes(this.getStateDefinition().getPossibleStates());
	}

	public LightSourceGlowdust(float hardness, float blastResistance, SoundType sound, String name) {
		super(Block.Properties.of(Material.METAL).strength(hardness, blastResistance).sound(sound).lightLevel((p_50886_) -> 15));

		this.registerDefaultState(this.getStateDefinition().any().setValue(DIRECTION, Direction.NORTH));
		this.generateShapes(this.getStateDefinition().getPossibleStates());
		this.setRegistryName(name);
	}

	@Override
	protected void generateShapes(ImmutableList<BlockState> states) {
		ImmutableMap.Builder<BlockState, VoxelShape> builder = new ImmutableMap.Builder<>();
		for (BlockState state : states) {
			VoxelShape shapes = Shapes.empty();

			shapes = Shapes.joinUnoptimized(shapes, getShapes(rotate(Block.box(6, 0, 6, 10, 5, 10), Direction.SOUTH))[state.getValue(DIRECTION).get2DDataValue()], BooleanOp.OR); // jar

			builder.put(state, shapes.optimize());
		}

		_shapes = builder.build();
	}


	@Override
	public boolean canConnectRedstone(BlockState state, BlockGetter world, BlockPos pos, Direction direction) {
		return false;
	}
}