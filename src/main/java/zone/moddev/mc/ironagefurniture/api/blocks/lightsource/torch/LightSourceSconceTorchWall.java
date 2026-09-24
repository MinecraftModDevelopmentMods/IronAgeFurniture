package zone.moddev.mc.ironagefurniture.api.blocks.lightsource.torch;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.shapes.VoxelShape;
import com.mojang.datafixers.util.Pair;
import net.minecraft.util.Direction;
import net.minecraft.particles.ParticleTypes;

import java.util.Random;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import zone.moddev.mc.ironagefurniture.BlockObjectHolder;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;

public class LightSourceSconceTorchWall extends LightSourceSconceTorchFloor {
	public LightSourceSconceTorchWall(Properties properties) {
		super(properties);

		this.registerDefaultState(this.getStateDefinition().any().setValue(DIRECTION, Direction.NORTH).setValue(WATERLOGGED, false));
        this.generateShapes(this.getStateDefinition().getPossibleStates());
        this.flameParticle = ParticleTypes.FLAME;
	}

    @Override
	public void animateTick(BlockState state, World level, BlockPos pos, Random rand) {
		if (HasFlame()) {
				Direction direction = state.getValue(DIRECTION);

				Pair<Double, Double> rotated = rotate(0.6D, 0.5D, state.getValue(DIRECTION));

				double d0 = (double)pos.getX() + rotated.getFirst();
				double d1 = (double)pos.getY() + 0.8D;
				double d2 = (double)pos.getZ() + rotated.getSecond();

				Direction direction1 = direction.getOpposite();

				level.addParticle(ParticleTypes.SMOKE, d0 + 0.27D * (double)direction1.getStepX(), d1 + 0.22D, d2 + 0.27D * (double)direction1.getStepZ(), 0.0D, 0.0D, 0.0D);
				level.addParticle(this.flameParticle, d0 + 0.27D * (double)direction1.getStepX(), d1 + 0.22D, d2 + 0.27D * (double)direction1.getStepZ(), 0.0D, 0.0D, 0.0D);
				}
    }

	@Override
	protected Block UnlitVariant() {
		return BlockObjectHolder.light_metal_ironage_sconce_wall_torch_iron_unlit;
	}

	@Override
	protected Block EmptyVariant() {
		return BlockObjectHolder.light_metal_ironage_sconce_wall_empty_iron;
	}

	@Override
	protected Block DropVariant() {
		return BlockObjectHolder.light_metal_ironage_sconce_floor_empty_iron;
	}

	@Override
	protected void generateShapes(ImmutableList<BlockState> states) {
		 ImmutableMap.Builder<BlockState, VoxelShape> builder = new ImmutableMap.Builder<>();
	        for(BlockState state : states)
	        {
				VoxelShape shapes = VoxelShapes.empty();

				shapes = VoxelShapes.joinUnoptimized(shapes, getShapes(rotate(Block.box(6, 3, 9, 10, 13, 13), Direction.SOUTH))[state.getValue(DIRECTION).get2DDataValue()], IBooleanFunction.OR); // torch

	            builder.put(state, shapes.optimize());
	        }

	        _shapes = builder.build();
	}

	public LightSourceSconceTorchWall(float hardness, float blastResistance, SoundType sound, String name) {
		super(Block.Properties.of(Material.METAL).strength(hardness, blastResistance).sound(sound).lightLevel((p_50886_) -> 14) );

		this.registerDefaultState(this.getStateDefinition().any().setValue(DIRECTION, Direction.NORTH));
		this.generateShapes(this.getStateDefinition().getPossibleStates());
		this.setRegistryName(name);
		this.flameParticle = ParticleTypes.FLAME;
	}

	@Override
	public BlockState updateShape(BlockState state, Direction direction, BlockState state2, IWorld level, BlockPos pos1, BlockPos pos2) {
		return direction.getOpposite() == state.getValue(DIRECTION) && !state.canSurvive(level, pos1) ? Blocks.AIR.defaultBlockState() : state;
	}

	@Override
	public boolean canSurvive(BlockState state, IWorldReader levelReader, BlockPos pos)
	{
	      Direction direction = state.getValue(DIRECTION);
	      BlockPos blockpos = pos.relative(direction.getOpposite());
	      return Block.canSupportCenter(levelReader, blockpos, direction);
	}
}
