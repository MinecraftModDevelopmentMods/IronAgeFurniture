package com.mcmoddev.ironagefurniture.api.blocks.lightsource.soultorch;

import com.mcmoddev.ironagefurniture.api.blocks.base.FurnitureBlock;
import com.mcmoddev.ironagefurniture.api.blocks.lightsource.torch.LightSourceSconceTorchFloor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import oshi.util.tuples.Pair;

import java.util.Random;

public class LightSourceSconceSoulTorchFloor extends LightSourceSconceTorchFloor {
	public LightSourceSconceSoulTorchFloor(Properties properties) {
		super(properties);

		this.registerDefaultState(this.getStateDefinition().any().setValue(DIRECTION, Direction.NORTH).setValue(WATERLOGGED, false));
		this.generateShapes(this.getStateDefinition().getPossibleStates());
		this.flameParticle = ParticleTypes.SOUL_FIRE_FLAME;
	}

	public LightSourceSconceSoulTorchFloor(float hardness, float blastResistance, SoundType sound, String name) {
		super(Block.Properties.of(Material.METAL).strength(hardness, blastResistance).sound(sound).lightLevel((p_50886_) -> 12));

		this.registerDefaultState(this.getStateDefinition().any().setValue(DIRECTION, Direction.NORTH));
		this.generateShapes(this.getStateDefinition().getPossibleStates());
		this.setRegistryName(name);
		this.flameParticle = ParticleTypes.SOUL_FIRE_FLAME;
	}

	@Override
	protected boolean CanEx() {
		return false;
	}

	@Override
	protected Block LightDrop() {
		return Blocks.SOUL_TORCH;
	}

}
