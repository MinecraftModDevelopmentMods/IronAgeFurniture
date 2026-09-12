package zone.moddev.mc.ironagefurniture.api.blocks.lightsource.red.illuminated.red;

import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.red.LightSourceRed;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;

public class LightSourceRedSeven extends LightSourceRed {

	public static final int LIGHT_LEVEL = 7;

	@Override
	protected int GetLightLevel() {
		return 7;
	}

	public LightSourceRedSeven(float hardness, float blastResistance, SoundType sound, String name) {
		super(Block.Properties.of().strength(hardness, blastResistance).sound(sound).lightLevel((p_50886_) -> LIGHT_LEVEL) );

		this.registerDefaultState(this.getStateDefinition().any().setValue(DIRECTION, Direction.NORTH));
		this.generateShapes(this.getStateDefinition().getPossibleStates());
		//this.setRegistryName(name);
		this.flameParticle = DustParticleOptions.REDSTONE;
	}
}
