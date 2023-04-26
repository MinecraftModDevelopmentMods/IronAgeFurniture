package com.mcmoddev.ironagefurniture.api.Blocks.LightSource.Red.Illuminated.Sconce.Floor;

import com.mcmoddev.ironagefurniture.api.Blocks.LightSource.Red.LightSourceSconceRedFloor;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;

public class LightSourceSconceRedFloorFour extends LightSourceSconceRedFloor {

	public static final int LIGHT_LEVEL = 4;

	@Override
	protected int GetLightLevel() {
		return 4;
	}

	public LightSourceSconceRedFloorFour(float hardness, float blastResistance, SoundType sound, String name) {
		super(Block.Properties.of(Material.METAL).strength(hardness, blastResistance).sound(sound).lightLevel((p_50886_) -> LIGHT_LEVEL) );

		this.registerDefaultState(this.getStateDefinition().any().setValue(DIRECTION, Direction.NORTH));
		this.generateShapes(this.getStateDefinition().getPossibleStates());
		this.setRegistryName(name);
		this.flameParticle = DustParticleOptions.REDSTONE;
	}
}
