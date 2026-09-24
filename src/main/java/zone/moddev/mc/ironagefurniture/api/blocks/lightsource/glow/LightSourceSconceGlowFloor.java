package zone.moddev.mc.ironagefurniture.api.blocks.lightsource.glow;

import zone.moddev.mc.ironagefurniture.api.blocks.base.FurnitureBlock;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.torch.LightSourceSconceTorchFloor;
import net.minecraft.block.Block;
import net.minecraft.block.ILiquidContainer;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.Direction;
import net.minecraft.particles.IParticleData;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import zone.moddev.mc.ironagefurniture.BlockObjectHolder;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;

public class LightSourceSconceGlowFloor extends LightSourceSconceTorchFloor implements ILiquidContainer {
	protected static final VoxelShape AxisAlignedBB = Block.box(6.0D, 0.0D, 6.0D, 10.0D, 10.0D, 10.0D);
	protected IParticleData flameParticle;

	@Override
	protected boolean HasFlame() {
		return false;
	}

	@Override
	protected boolean CanEx() {
		return false;
	}

	@Override
	protected Block LightDrop() {
		return BlockObjectHolder.light_metal_ironage_block_floor_glow_clear;
	}

	public LightSourceSconceGlowFloor(AbstractBlock.Properties properties) {
		super(properties);

		this.registerDefaultState(this.getStateDefinition().any().setValue(FurnitureBlock.DIRECTION, Direction.NORTH).setValue(FurnitureBlock.WATERLOGGED, false));
        this.generateShapes(this.getStateDefinition().getPossibleStates());
	}

	public LightSourceSconceGlowFloor(float hardness, float blastResistance, SoundType sound, String name) {
		super(Block.Properties.of(Material.METAL).strength(hardness, blastResistance).sound(sound).lightLevel((p_50886_) -> 14) );

		this.registerDefaultState(this.getStateDefinition().any().setValue(FurnitureBlock.DIRECTION, Direction.NORTH));
		this.generateShapes(this.getStateDefinition().getPossibleStates());
		this.setRegistryName(name);
	}

	@Override
	protected void generateShapes(ImmutableList<BlockState> states) {
		 ImmutableMap.Builder<BlockState, VoxelShape> builder = new ImmutableMap.Builder<>();
	        for(BlockState state : states)
				builder.put(state, AxisAlignedBB);

	        _shapes = builder.build();
	}

	@Override
	public boolean canConnectRedstone(BlockState state, IBlockReader world, BlockPos pos, Direction direction) {
		return false;
	}
}
