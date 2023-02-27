package com.mcmoddev.ironagefurniture.api.Blocks;

import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;

public class LightSourceRedNine extends LightSourceRed {

	public static final int LIGHT_LEVEL = 9;
	
	@Override
	protected int GetLightLevel() {
		return 9;
	}
	
	public LightSourceRedNine(Properties properties) {
		super(properties);
		
		this.registerDefaultState(this.getStateDefinition().any().setValue(DIRECTION, Direction.NORTH).setValue(WATERLOGGED, false));
        this.generateShapes(this.getStateDefinition().getPossibleStates());
        this.flameParticle = DustParticleOptions.REDSTONE;
	}
	
	public LightSourceRedNine(float hardness, float blastResistance, SoundType sound, String name) {
		super(Block.Properties.of(Material.GLASS).strength(hardness, blastResistance).sound(sound).lightLevel((p_50886_) -> {
		    return LIGHT_LEVEL; }) );

		this.registerDefaultState(this.getStateDefinition().any().setValue(DIRECTION, Direction.NORTH));
		this.generateShapes(this.getStateDefinition().getPossibleStates());
		this.setRegistryName(name);
		this.flameParticle = DustParticleOptions.REDSTONE;
	}
}
