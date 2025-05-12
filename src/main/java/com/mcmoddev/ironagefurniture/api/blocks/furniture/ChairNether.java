//package com.mcmoddev.ironagefurniture.api.blocks.furniture;
//
//import net.minecraft.core.BlockPos;
//import net.minecraft.core.Direction;
//import net.minecraft.world.level.BlockGetter;
//import net.minecraft.world.level.block.Block;
//import net.minecraft.world.level.block.SimpleWaterloggedBlock;
//import net.minecraft.world.level.block.SoundType;
//import net.minecraft.world.level.block.state.BlockState;
//import net.minecraft.world.level.material.Material;
//
//public class ChairNether extends Chair implements SimpleWaterloggedBlock {
//	@Override
//	public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
//		return false;
//	}
//	
//	public ChairNether(Properties properties) {
//		super(properties);
//
//		this.registerDefaultState(this.getStateDefinition().any().setValue(DIRECTION, Direction.NORTH).setValue(WATERLOGGED, false));
//		this.generateShapes(this.getStateDefinition().getPossibleStates());
//	}
//
//	public ChairNether(float hardness, float blastResistance, SoundType sound, String name) {
//		super(Block.Properties.of(Material.WOOD).strength(hardness, blastResistance).sound(sound));
//
//		this.registerDefaultState(this.getStateDefinition().any().setValue(DIRECTION, Direction.NORTH));
//		this.generateShapes(this.getStateDefinition().getPossibleStates());
//		this.setRegistryName(name);
//	}
//}
