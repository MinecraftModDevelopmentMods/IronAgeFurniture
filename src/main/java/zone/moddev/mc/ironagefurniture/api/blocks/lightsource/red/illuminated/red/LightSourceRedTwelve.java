package zone.moddev.mc.ironagefurniture.api.blocks.lightsource.red.illuminated.red;

import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.red.LightSourceRed;
import net.minecraft.util.Direction;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class LightSourceRedTwelve extends LightSourceRed {

	public static final int LIGHT_LEVEL = 12;

	@Override
	protected int GetLightLevel() {
		return 12;
	}

	public LightSourceRedTwelve(float hardness, float blastResistance, SoundType sound, String name) {
		super(Block.Properties.create(Material.GLASS).hardnessAndResistance(hardness, blastResistance).sound(sound).lightValue(LIGHT_LEVEL) );

		this.setDefaultState(this.getStateContainer().getBaseState() .with(DIRECTION, Direction.NORTH));
		this.generateShapes(this.getStateContainer().getValidStates());
		this.setRegistryName(name);
		this.flameParticle = RedstoneParticleData.REDSTONE_DUST;
	}
}
