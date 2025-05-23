package com.mcmoddev.ironagefurniture.api.blocks.lightholder;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mcmoddev.ironagefurniture.init.ModVanillaLights;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.storage.loot.LootContext.Builder;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;

public class LightHolderSconceWall extends LightHolderSconceFloor {
	public LightHolderSconceWall(float hardness, float blastResistance, SoundType sound, String name) {
		super(Block.Properties.of(Material.METAL).strength(hardness, blastResistance).sound(sound));

		this.registerDefaultState(this.getStateDefinition().any().setValue(DIRECTION, Direction.NORTH));
		this.generateShapes(this.getStateDefinition().getPossibleStates());
		//this.setRegistryName(name);
	}

	@Override
	public BlockState updateShape(BlockState state, Direction direction, BlockState state2, LevelAccessor level, BlockPos pos1, BlockPos pos2) {
		return direction.getOpposite() == state.getValue(DIRECTION) && !state.canSurvive(level, pos1) ? Blocks.AIR.defaultBlockState() : state;
	}

	@Override
	public boolean canSurvive(BlockState state, LevelReader levelReader, BlockPos pos) {
	    Direction direction = state.getValue(DIRECTION);
	    BlockPos adjacentPos = pos.relative(direction.getOpposite());
	    BlockState adjacentState = levelReader.getBlockState(adjacentPos);

	    boolean isSturdy = adjacentState.isFaceSturdy(levelReader, adjacentPos, direction.getOpposite());

	    return isSturdy;
	}

	@Override
	protected void generateShapes(ImmutableList<BlockState> states) {
		ImmutableMap.Builder<BlockState, VoxelShape> builder = new ImmutableMap.Builder<>();
		for (BlockState state : states) {
			VoxelShape shapes = Shapes.empty();

			shapes = Shapes.joinUnoptimized(shapes, getShapes(rotate(Block.box(1.5, 9, 5.5, 6.5, 10, 10.5), Direction.EAST))[state.getValue(DIRECTION).get2DDataValue()], BooleanOp.OR); // sconce holder

			builder.put(state, shapes.optimize());
		}

		_shapes = builder.build();
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, Builder builder) {
		List<ItemStack> drops;
		drops = new ArrayList<ItemStack>();

		Item item = ModVanillaLights.light_metal_ironage_sconce_floor_empty_iron.get().asItem();
		ItemStack stack = new ItemStack(item, 1);

		drops.add(stack);

		return drops;
	}

	@Override
	protected Block GetGlowVariant() {
		return ModVanillaLights.light_metal_ironage_sconce_wall_glow_iron.get();
	}

	@Override
	protected Block GetTorchVariant() {
		return ModVanillaLights.light_metal_ironage_sconce_wall_torch_iron.get();
	}

	@Override
	protected Block GetLavaVariant() {
		return ModVanillaLights.light_metal_ironage_sconce_wall_lava_iron.get();
	}

	@Override
	protected Block GetUnlitTorchVariant() {
		return ModVanillaLights.light_metal_ironage_sconce_wall_torch_iron_unlit.get();
	}

	@Override
	protected Block GetRedTorchVariant() {
		return ModVanillaLights.light_metal_ironage_sconce_wall_redtorch_iron.get();
	}

	@Override
	protected Block GetRedVariant() {
		return ModVanillaLights.light_metal_ironage_sconce_wall_red_iron.get();
	}

	@Override
	protected Block GetSoulTorchVariant() {
		return ModVanillaLights.light_metal_ironage_sconce_wall_soultorch_iron.get();
	}
}
