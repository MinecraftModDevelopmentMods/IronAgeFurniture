package zone.moddev.mc.ironagefurniture.api.blocks.lightsource.red.illuminated.red;

import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.red.LightSourceRed;
import net.minecraft.util.Direction;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class LightSourceRedThirteen extends LightSourceRed {

	public static final int LIGHT_LEVEL = 13;

	@Override
	protected int GetLightLevel() {
		return 13;
	}

	public LightSourceRedThirteen(float hardness, float blastResistance, SoundType sound, String name) {
		super(Block.Properties.of(Material.GLASS).strength(hardness, blastResistance).sound(sound).lightLevel((p_50886_) -> LIGHT_LEVEL) );

		this.registerDefaultState(this.getStateDefinition().any().setValue(DIRECTION, Direction.NORTH));
		this.generateShapes(this.getStateDefinition().getPossibleStates());
		this.setRegistryName(name);
		this.flameParticle = RedstoneParticleData.REDSTONE;
	}
}
