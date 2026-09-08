package zone.moddev.mc.ironagefurniture.api.Blocks;

import java.util.Random;

import zone.moddev.mc.ironagefurniture.BlockObjectHolder;
import zone.moddev.mc.ironagefurniture.api.MineralogyCompat;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class LightSourceSconceRockSaltFloor extends LightSourceSconceGlowFloor {
	public LightSourceSconceRockSaltFloor(Material materialIn, String name, float resistance, float hardness) {
		super(materialIn, name, resistance, hardness);
		this.setLightLevel(1.0F);
	}

	@Override
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
		world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, pos.getX() + 0.5D, pos.getY() + 0.72D,
			pos.getZ() + 0.5D, 0.0D, 0.0D, 0.0D);
	}

	@Override
	protected Block LightDrop() {
		Block lamp = MineralogyCompat.getRockSaltLampBlock();
		return lamp == null ? Blocks.TORCH : lamp;
	}

	@Override
	protected Block GetWallVariant() {
		return BlockObjectHolder.light_metal_ironage_sconce_wall_rocksalt_iron;
	}

	@Override
	protected Block GetGlowVariant() {
		return BlockObjectHolder.light_metal_ironage_sconce_floor_rocksalt_iron;
	}

	@Override
	protected Block GetRockSaltVariant() {
		return BlockObjectHolder.light_metal_ironage_sconce_floor_rocksalt_iron;
	}
}
