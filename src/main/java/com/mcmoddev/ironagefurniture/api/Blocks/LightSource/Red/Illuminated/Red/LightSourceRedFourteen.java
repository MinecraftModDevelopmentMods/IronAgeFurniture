package com.mcmoddev.ironagefurniture.api.Blocks.LightSource.Red.Illuminated.Red;

import com.mcmoddev.ironagefurniture.api.Blocks.LightSource.Red.LightSourceRed;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;

public class LightSourceRedFourteen extends LightSourceRed {

	public static final int LIGHT_LEVEL = 14;

	@Override
	protected int GetLightLevel() {
		return 14;
	}

	public LightSourceRedFourteen(float hardness, float blastResistance, SoundType sound, String name) {
		super(Block.Properties.of(Material.GLASS).strength(hardness, blastResistance).sound(sound).lightLevel((p_50886_) -> LIGHT_LEVEL) );

		this.registerDefaultState(this.getStateDefinition().any().setValue(DIRECTION, Direction.NORTH));
		this.generateShapes(this.getStateDefinition().getPossibleStates());
		this.setRegistryName(name);
		this.flameParticle = DustParticleOptions.REDSTONE;
	}
}
