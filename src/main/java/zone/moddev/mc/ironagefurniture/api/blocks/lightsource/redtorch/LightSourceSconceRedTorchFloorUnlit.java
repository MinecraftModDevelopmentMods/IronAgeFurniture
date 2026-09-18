package zone.moddev.mc.ironagefurniture.api.blocks.lightsource.redtorch;

import zone.moddev.mc.ironagefurniture.api.blocks.base.FurnitureBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.Direction;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.world.server.ServerWorld;

import java.util.List;
import java.util.Random;

import zone.moddev.mc.ironagefurniture.BlockObjectHolder;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class LightSourceSconceRedTorchFloorUnlit extends LightSourceSconceRedTorchFloor {
	@Override
	public void animateTick(BlockState state, World level, BlockPos pos, Random rand) {
		//
	}

	@Override
	public int getWeakPower(BlockState state, IBlockReader getter, BlockPos pos, Direction direction) {
		return 0;
	}

	@Override
	public void tick(BlockState state, World level, BlockPos pos, Random rnd) {
		List<LightSourceSconceRedTorchFloorUnlit.Toggle> list = RECENT_TOGGLES.get(level);

		while (list != null && !list.isEmpty() && level.getGameTime() - (list.get(0)).when > 60L)
			list.remove(0);

		level.setBlockState(pos,
				BlockObjectHolder.light_metal_ironage_sconce_floor_redtorch_iron.getDefaultState()
						 .with(FurnitureBlock.DIRECTION, state .get(BlockStateProperties.HORIZONTAL_FACING))
						 .with(FurnitureBlock.WATERLOGGED, state .get(BlockStateProperties.WATERLOGGED)),
				3);
	}

	@Override
	public void neighborChanged(BlockState state, World level, BlockPos pos, Block block, BlockPos blockPos,
								boolean flag) {
		if (!this.hasNeighborSignal(level, pos, state) && !level.getPendingBlockTicks().isTickPending(pos, this)) {
			level.getPendingBlockTicks().scheduleTick(pos, this, 2);
		}
	}

	public LightSourceSconceRedTorchFloorUnlit(float hardness, float blastResistance, SoundType sound, String name) {
		super(Block.Properties.create(Material.IRON).hardnessAndResistance(hardness, blastResistance).sound(sound)
				.lightValue(0));

		this.setDefaultState(this.getStateContainer().getBaseState() .with(FurnitureBlock.DIRECTION, Direction.NORTH));
		this.generateShapes(this.getStateContainer().getValidStates());
		this.setRegistryName(name);
		this.flameParticle = ParticleTypes.FLAME;
	}
}
