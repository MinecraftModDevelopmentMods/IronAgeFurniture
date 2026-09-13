package zone.moddev.mc.ironagefurniture.api.blocks.lightsource.torch;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import oshi.util.tuples.Pair;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;

import java.util.Random;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import zone.moddev.mc.ironagefurniture.init.ModVanillaLights;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;

public class LightSourceSconceTorchWall extends LightSourceSconceTorchFloor {
	public LightSourceSconceTorchWall(Properties properties) {
		super(properties);

		this.registerDefaultState(this.getStateDefinition().any().setValue(DIRECTION, Direction.NORTH).setValue(WATERLOGGED, false));
        this.generateShapes(this.getStateDefinition().getPossibleStates());
        this.flameParticle = ParticleTypes.FLAME;
	}

    @Override
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource rand) {
    	if (HasFlame()) {
    		Direction direction = state.getValue(DIRECTION);

    		Pair<Double, Double> rotated = rotate(0.6D, 0.5D, state.getValue(DIRECTION));

    		double d0 = (double)pos.getX() + rotated.getA();
    		double d1 = (double)pos.getY() + 0.8D;
    		double d2 = (double)pos.getZ() + rotated.getB();

    		Direction direction1 = direction.getOpposite();

    		level.addParticle(ParticleTypes.SMOKE, d0 + 0.27D * (double)direction1.getStepX(), d1 + 0.22D, d2 + 0.27D * (double)direction1.getStepZ(), 0.0D, 0.0D, 0.0D);
    		level.addParticle(this.flameParticle, d0 + 0.27D * (double)direction1.getStepX(), d1 + 0.22D, d2 + 0.27D * (double)direction1.getStepZ(), 0.0D, 0.0D, 0.0D);
   		}
    }

	@Override
	protected Block UnlitVariant() {
		return ModVanillaLights.light_metal_ironage_sconce_wall_torch_iron_unlit.get();
	}

	@Override
	protected Block EmptyVariant() {
		return ModVanillaLights.light_metal_ironage_sconce_wall_empty_iron.get();
	}

	@Override
	protected Block DropVariant() {
		return ModVanillaLights.light_metal_ironage_sconce_floor_empty_iron.get();
	}
	
	@Override
	protected void generateShapes(ImmutableList<BlockState> states) {
		 ImmutableMap.Builder<BlockState, VoxelShape> builder = new ImmutableMap.Builder<>();
	        for(BlockState state : states)
	        {
	        	VoxelShape shapes = Shapes.empty();

	        	shapes = Shapes.joinUnoptimized(shapes, getShapes(rotate(Block.box(6, 3, 9, 10, 13, 13), Direction.SOUTH))[state.getValue(DIRECTION).get2DDataValue()], BooleanOp.OR); // torch

	            builder.put(state, shapes.optimize());
	        }

	        _shapes = builder.build();
	}

	public LightSourceSconceTorchWall(float hardness, float blastResistance, SoundType sound, String name) {
		super(zone.moddev.mc.ironagefurniture.init.RegistrationProperties.block(Block.Properties.of().strength(hardness, blastResistance).sound(sound).lightLevel((p_50886_) -> 14) , name));

		this.registerDefaultState(this.getStateDefinition().any().setValue(DIRECTION, Direction.NORTH));
		this.generateShapes(this.getStateDefinition().getPossibleStates());
		//this.setRegistryName(name);
		this.flameParticle = ParticleTypes.FLAME;
	}

	@Override
	protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos,
			Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
		return direction.getOpposite() == state.getValue(DIRECTION) && !state.canSurvive(level, pos) ? Blocks.AIR.defaultBlockState() : state;
	}

	@Override
	public boolean canSurvive(BlockState state, LevelReader levelReader, BlockPos pos)
	{
	      Direction direction = state.getValue(DIRECTION);
	      BlockPos blockpos = pos.relative(direction.getOpposite());
	      BlockState blockstate = levelReader.getBlockState(blockpos);
	      return blockstate.isFaceSturdy(levelReader, blockpos, direction);
	}
}
