package zone.moddev.mc.ironagefurniture.api.blocks.lightsource.red.illuminated.sconce.floor;

import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.red.LightSourceSconceRedFloor;
import net.minecraft.util.Direction;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class LightSourceSconceRedFloorThirteen extends LightSourceSconceRedFloor {

	public static final int LIGHT_LEVEL = 13;

	@Override
	protected int GetLightLevel() {
		return 13;
	}

	public LightSourceSconceRedFloorThirteen(float hardness, float blastResistance, SoundType sound, String name) {
		super(Block.Properties.create(Material.IRON).hardnessAndResistance(hardness, blastResistance).sound(sound).lightValue(LIGHT_LEVEL) );

		this.setDefaultState(this.getStateContainer().getBaseState() .with(DIRECTION, Direction.NORTH));
		this.generateShapes(this.getStateContainer().getValidStates());
		this.setRegistryName(name);
		this.flameParticle = RedstoneParticleData.REDSTONE_DUST;
	}
}
