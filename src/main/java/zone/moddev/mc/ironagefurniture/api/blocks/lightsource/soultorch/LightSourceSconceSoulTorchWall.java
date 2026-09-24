package zone.moddev.mc.ironagefurniture.api.blocks.lightsource.soultorch;

import zone.moddev.mc.ironagefurniture.api.blocks.base.FurnitureBlock;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.torch.LightSourceSconceTorchWall;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.Direction;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.world.World;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import com.mojang.datafixers.util.Pair;

import java.util.Random;

public class LightSourceSconceSoulTorchWall extends LightSourceSconceTorchWall {
	public LightSourceSconceSoulTorchWall(Properties properties) {
		super(properties);

		this.registerDefaultState(
			this.getStateDefinition().any().setValue(FurnitureBlock.DIRECTION, Direction.NORTH).setValue(FurnitureBlock.WATERLOGGED, false));

		this.generateShapes(this.getStateDefinition().getPossibleStates());
		this.flameParticle = ParticleTypes.SOUL_FIRE_FLAME;
	}

	public LightSourceSconceSoulTorchWall(float hardness, float blastResistance, SoundType sound, String name) {
		super(Block.Properties.of(Material.METAL).strength(hardness, blastResistance).sound(sound)
			.lightLevel((p_50886_) -> 12));

		this.registerDefaultState(this.getStateDefinition().any().setValue(FurnitureBlock.DIRECTION, Direction.NORTH));
		this.generateShapes(this.getStateDefinition().getPossibleStates());
		this.setRegistryName(name);
		this.flameParticle = ParticleTypes.SOUL_FIRE_FLAME;
	}

	@Override
	protected boolean CanEx() {
		return false;
	}

	@Override
	protected Block LightDrop() {
		return Blocks.SOUL_TORCH;
	}
}
