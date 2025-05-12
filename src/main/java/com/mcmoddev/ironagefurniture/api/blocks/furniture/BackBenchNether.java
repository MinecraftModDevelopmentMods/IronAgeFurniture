//package com.mcmoddev.ironagefurniture.api.blocks.furniture;
//
//import com.mcmoddev.ironagefurniture.api.enumerations.BenchType;
//import com.mcmoddev.ironagefurniture.api.properties.BenchTypeProperty;
//import net.minecraft.core.BlockPos;
//import net.minecraft.core.Direction;
//import net.minecraft.world.level.BlockGetter;
//import net.minecraft.world.level.block.Block;
//import net.minecraft.world.level.block.SoundType;
//import net.minecraft.world.level.block.state.BlockState;
//import net.minecraft.world.level.material.Material;
//
//public class BackBenchNether extends BackBench {
//	public static final BenchTypeProperty TYPE = BenchTypeProperty.create("type", BenchType.SINGLE, BenchType.LEFT, BenchType.MIDDLE, BenchType.RIGHT);
//
//	public BackBenchNether(float hardness, float blastResistance, SoundType sound, String name) {
//		super(Block.Properties.of(Material.NETHER_WOOD).strength(hardness, blastResistance).sound(sound));
//
//		this.registerDefaultState(this.getStateDefinition().any().setValue(DIRECTION, Direction.NORTH));
//		this.generateShapes(this.getStateDefinition().getPossibleStates());
//		this.setRegistryName(name);
//	}
//
//	@Override
//	public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
//		return false;
//	}
//}
