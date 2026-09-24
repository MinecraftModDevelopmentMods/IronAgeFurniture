package zone.moddev.mc.ironagefurniture.api.blocks.lightsource.red.illuminated.sconce.wall;

import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.red.LightSourceSconceRedWall;
import net.minecraft.util.Direction;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class LightSourceSconceRedWallTen extends LightSourceSconceRedWall {

	public static final int LIGHT_LEVEL = 10;

	@Override
	protected int GetLightLevel() {
		return 10;
	}

	public LightSourceSconceRedWallTen(float hardness, float blastResistance, SoundType sound, String name) {
		super(Properties.of(Material.METAL).strength(hardness, blastResistance).sound(sound).lightLevel((p_50886_) -> LIGHT_LEVEL) );

		this.registerDefaultState(this.getStateDefinition().any().setValue(DIRECTION, Direction.NORTH));
		this.generateShapes(this.getStateDefinition().getPossibleStates());
		this.setRegistryName(name);
		this.flameParticle = RedstoneParticleData.REDSTONE;
	}
}
