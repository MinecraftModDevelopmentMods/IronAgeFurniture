package zone.moddev.mc.ironagefurniture.api.blocks.lightsource.red.illuminated.sconce.floor;

import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.red.LightSourceSconceRedFloor;
import net.minecraft.util.Direction;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class LightSourceSconceRedFloorNine extends LightSourceSconceRedFloor {

	public static final int LIGHT_LEVEL = 9;

	@Override
	protected int GetLightLevel() {
		return 9;
	}

	public LightSourceSconceRedFloorNine(float hardness, float blastResistance, SoundType sound, String name) {
		super(Block.Properties.of(Material.METAL).strength(hardness, blastResistance).sound(sound).lightLevel((p_50886_) -> LIGHT_LEVEL) );

		this.registerDefaultState(this.getStateDefinition().any().setValue(DIRECTION, Direction.NORTH));
		this.generateShapes(this.getStateDefinition().getPossibleStates());
		this.setRegistryName(name);
		this.flameParticle = RedstoneParticleData.REDSTONE;
	}
}
