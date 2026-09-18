package zone.moddev.mc.ironagefurniture.api.blocks.lightsource.redtorch;

import zone.moddev.mc.ironagefurniture.api.blocks.base.FurnitureBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.Direction;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.world.server.ServerWorld;

import java.util.List;
import java.util.Random;

import zone.moddev.mc.ironagefurniture.BlockObjectHolder;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class LightSourceSconceRedTorchWallUnlit extends LightSourceSconceRedTorchWall {
	@Override
	public void animateTick(BlockState state, World level, BlockPos pos, Random rand) {
		//
	}

	@Override
	public int getWeakPower(BlockState p_55694_, IBlockReader p_55695_, BlockPos p_55696_, Direction p_55697_) {
		return 0;
	}

	@Override
	public void tick(BlockState state, World level, BlockPos pos, Random rnd) {
		List<LightSourceSconceRedTorchWallUnlit.Toggle> list = RECENT_TOGGLES.get(level);

		while (list != null && !list.isEmpty() && level.getGameTime() - (list.get(0)).when > 60L)
			list.remove(0);

		level.setBlockState(pos,
				BlockObjectHolder.light_metal_ironage_sconce_wall_redtorch_iron.getDefaultState()
						 .with(FurnitureBlock.DIRECTION, state .get(BlockStateProperties.HORIZONTAL_FACING))
						 .with(FurnitureBlock.WATERLOGGED, state .get(BlockStateProperties.WATERLOGGED)),
				3);
	}

	@Override
	public boolean canProvidePower(BlockState p_55730_) {
		return true;
	}

	@Override
	public void neighborChanged(BlockState state, World level, BlockPos pos, Block p_55702_, BlockPos p_55703_,
								boolean p_55704_) {
		if (!this.hasNeighborSignal(level, pos, state) && !level.getPendingBlockTicks().isTickPending(pos, this))
			level.getPendingBlockTicks().scheduleTick(pos, this, 2);
	}

	public LightSourceSconceRedTorchWallUnlit(float hardness, float blastResistance, SoundType sound, String name) {
		super(Block.Properties.create(Material.IRON).hardnessAndResistance(hardness, blastResistance).sound(sound)
				.lightValue(0));

		this.setDefaultState(this.getStateContainer().getBaseState() .with(FurnitureBlock.DIRECTION, Direction.NORTH));
		this.generateShapes(this.getStateContainer().getValidStates());
		this.setRegistryName(name);
		this.flameParticle = ParticleTypes.FLAME;
	}
}
