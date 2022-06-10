package com.mcmoddev.ironagefurniture.api.Blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import com.mcmoddev.ironagefurniture.BlockObjectHolder;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.BlockGetter;

public class LightSourceSconceLavaFloor extends LightSourceSconceGlowFloor implements LiquidBlockContainer {
	protected static final int AABB_STANDING_OFFSET = 2;
	protected static final VoxelShape AABB = Block.box(6.0D, 0.0D, 6.0D, 10.0D, 10.0D, 10.0D);
	protected ParticleOptions flameParticle;
	
	@Override
	protected boolean HasFlame() {
		return false;
	}
	
	@Override
	protected boolean CanEx() {
		return false;
	}
	
	protected Block LightDrop() {
		return BlockObjectHolder.light_metal_ironage_block_floor_lava_clear;
	}
	
	public LightSourceSconceLavaFloor(Properties properties) {
		super(properties);
		
		this.registerDefaultState(this.getStateDefinition().any().setValue(DIRECTION, Direction.NORTH).setValue(WATERLOGGED, false));
        this.generateShapes(this.getStateDefinition().getPossibleStates());
	}
	
	public LightSourceSconceLavaFloor(float hardness, float blastResistance, SoundType sound, String name) {
		super(Block.Properties.of(Material.METAL).strength(hardness, blastResistance).sound(sound).lightLevel((p_50886_) -> {
		    return 14; }) );

		this.registerDefaultState(this.getStateDefinition().any().setValue(DIRECTION, Direction.NORTH));
		this.generateShapes(this.getStateDefinition().getPossibleStates());
		this.setRegistryName(name);
	}

	@Override
	public boolean canConnectRedstone(BlockState state, BlockGetter world, BlockPos pos, Direction direction) {
		return false;
	}
	
	protected Block EmptyVariant() {
		return BlockObjectHolder.light_metal_ironage_sconce_floor_empty_iron;
	}
}
