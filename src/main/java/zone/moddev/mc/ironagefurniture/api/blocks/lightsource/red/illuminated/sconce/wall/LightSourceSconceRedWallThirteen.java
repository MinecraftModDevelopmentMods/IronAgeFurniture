package zone.moddev.mc.ironagefurniture.api.blocks.lightsource.red.illuminated.sconce.wall;

import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.red.LightSourceSconceRedWall;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.world.level.block.SoundType;

public class LightSourceSconceRedWallThirteen extends LightSourceSconceRedWall {

	public static final int LIGHT_LEVEL = 13;

	@Override
	protected int GetLightLevel() {
		return 13;
	}

	public LightSourceSconceRedWallThirteen(float hardness, float blastResistance, SoundType sound, String name) {
		super(zone.moddev.mc.ironagefurniture.init.RegistrationProperties.block(Properties.of().strength(hardness, blastResistance).sound(sound).lightLevel((p_50886_) -> LIGHT_LEVEL) , name));

		this.registerDefaultState(this.getStateDefinition().any().setValue(DIRECTION, Direction.NORTH));
		this.generateShapes(this.getStateDefinition().getPossibleStates());
		// this.setRegistryName(name);
		this.flameParticle = DustParticleOptions.REDSTONE;
	}
}
