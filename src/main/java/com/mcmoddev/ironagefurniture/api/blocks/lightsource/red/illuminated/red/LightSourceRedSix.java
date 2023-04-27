package com.mcmoddev.ironagefurniture.api.blocks.lightsource.red.illuminated.red;

import com.mcmoddev.ironagefurniture.api.blocks.lightsource.red.LightSourceRed;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;

public class LightSourceRedSix extends LightSourceRed {

	public static final int LIGHT_LEVEL = 6;

	@Override
	protected int GetLightLevel() {
		return 6;
	}

	public LightSourceRedSix(float hardness, float blastResistance, SoundType sound, String name) {
		super(Block.Properties.of(Material.GLASS).strength(hardness, blastResistance).sound(sound).lightLevel((p_50886_) -> LIGHT_LEVEL) );

		this.registerDefaultState(this.getStateDefinition().any().setValue(DIRECTION, Direction.NORTH));
		this.generateShapes(this.getStateDefinition().getPossibleStates());
		this.setRegistryName(name);
		this.flameParticle = DustParticleOptions.REDSTONE;
	}
}
