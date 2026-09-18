package zone.moddev.mc.ironagefurniture.api.blocks.lightsource.red.illuminated.sconce.wall;

import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.red.LightSourceSconceRedWall;
import net.minecraft.util.Direction;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class LightSourceSconceRedWallNine extends LightSourceSconceRedWall {

	public static final int LIGHT_LEVEL = 9;

	@Override
	protected int GetLightLevel() {
		return 9;
	}

	public LightSourceSconceRedWallNine(float hardness, float blastResistance, SoundType sound, String name) {
		super(Properties.create(Material.IRON).hardnessAndResistance(hardness, blastResistance).sound(sound).lightValue(LIGHT_LEVEL) );

		this.setDefaultState(this.getStateContainer().getBaseState() .with(DIRECTION, Direction.NORTH));
		this.generateShapes(this.getStateContainer().getValidStates());
		this.setRegistryName(name);
		this.flameParticle = RedstoneParticleData.REDSTONE_DUST;
	}
}
