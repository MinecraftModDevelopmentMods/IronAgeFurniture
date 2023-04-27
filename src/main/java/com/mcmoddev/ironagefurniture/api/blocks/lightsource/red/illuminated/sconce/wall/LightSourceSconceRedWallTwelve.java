package com.mcmoddev.ironagefurniture.api.blocks.lightsource.red.illuminated.sconce.wall;

import com.mcmoddev.ironagefurniture.api.blocks.lightsource.red.LightSourceSconceRedWall;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;

public class LightSourceSconceRedWallTwelve extends LightSourceSconceRedWall {

	public static final int LIGHT_LEVEL = 12;

	@Override
	protected int GetLightLevel() {
		return 12;
	}

	public LightSourceSconceRedWallTwelve(float hardness, float blastResistance, SoundType sound, String name) {
		super(Properties.of(Material.METAL).strength(hardness, blastResistance).sound(sound).lightLevel((p_50886_) -> LIGHT_LEVEL) );

		this.registerDefaultState(this.getStateDefinition().any().setValue(DIRECTION, Direction.NORTH));
		this.generateShapes(this.getStateDefinition().getPossibleStates());
		this.setRegistryName(name);
		this.flameParticle = DustParticleOptions.REDSTONE;
	}
}
