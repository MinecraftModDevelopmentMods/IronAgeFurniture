package zone.moddev.mc.ironagefurniture.api.blocks.lightsource.redtorch;

import zone.moddev.mc.ironagefurniture.api.blocks.base.FurnitureBlock;
import net.minecraft.block.Block;
import net.minecraft.block.AbstractBlock;
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
	public int getSignal(BlockState p_55694_, IBlockReader p_55695_, BlockPos p_55696_, Direction p_55697_) {
		return 0;
	}

	@Override
	public void tick(BlockState state, ServerWorld level, BlockPos pos, Random rnd) {
		List<LightSourceSconceRedTorchWallUnlit.Toggle> list = RECENT_TOGGLES.get(level);

		while (list != null && !list.isEmpty() && level.getGameTime() - (list.get(0)).when > 60L)
			list.remove(0);

		level.setBlock(pos,
				BlockObjectHolder.light_metal_ironage_sconce_wall_redtorch_iron.defaultBlockState()
						.setValue(FurnitureBlock.DIRECTION, state.getValue(BlockStateProperties.HORIZONTAL_FACING))
						.setValue(FurnitureBlock.WATERLOGGED, state.getValue(BlockStateProperties.WATERLOGGED)),
				3);
	}

	@Override
	public boolean isSignalSource(BlockState p_55730_) {
		return true;
	}

	@Override
	public void neighborChanged(BlockState state, World level, BlockPos pos, Block p_55702_, BlockPos p_55703_,
								boolean p_55704_) {
		if (!this.hasNeighborSignal(level, pos, state) && !level.getBlockTicks().willTickThisTick(pos, this))
			level.getBlockTicks().scheduleTick(pos, this, 2);
	}

	public LightSourceSconceRedTorchWallUnlit(float hardness, float blastResistance, SoundType sound, String name) {
		super(Block.Properties.of(Material.METAL).strength(hardness, blastResistance).sound(sound)
				.lightLevel((p_50886_) -> 0));

		this.registerDefaultState(this.getStateDefinition().any().setValue(FurnitureBlock.DIRECTION, Direction.NORTH));
		this.generateShapes(this.getStateDefinition().getPossibleStates());
		this.setRegistryName(name);
		this.flameParticle = ParticleTypes.FLAME;
	}
}
