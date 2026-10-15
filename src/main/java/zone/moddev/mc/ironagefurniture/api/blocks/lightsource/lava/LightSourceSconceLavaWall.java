package zone.moddev.mc.ironagefurniture.api.blocks.lightsource.lava;

import zone.moddev.mc.ironagefurniture.api.blocks.base.FurnitureBlock;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.glow.LightSourceSconceGlowWall;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.Material;
import oshi.util.tuples.Pair;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;

import java.util.Random;

import zone.moddev.mc.ironagefurniture.BlockObjectHolder;
import zone.moddev.mc.ironagefurniture.api.entity.ReleasedLavaLamp;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.phasefour.LightInteractions;

import net.minecraft.core.BlockPos;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;

public class LightSourceSconceLavaWall extends LightSourceSconceGlowWall {
    @Override public void onPlace(BlockState state, Level world, BlockPos pos, BlockState old, boolean moving) {
        super.onPlace(state, world, pos, old, moving);
        scheduleRelease(world, pos);
    }

    @Override public void neighborChanged(BlockState state, Level world, BlockPos pos, Block neighbour,
            BlockPos neighbourPos, boolean moving) {
        super.neighborChanged(state, world, pos, neighbour, neighbourPos, moving);
        scheduleRelease(world, pos);
    }

    private void scheduleRelease(Level world, BlockPos pos) {
        if (!world.isClientSide && world.getBlockState(pos).getBlock() == this
                && canRelease(world.hasNeighborSignal(pos), world.isEmptyBlock(pos.below())))
            world.getBlockTicks().scheduleTick(pos, this, 2);
    }

    static boolean canRelease(boolean powered, boolean airBelow) { return powered && airBelow; }

    @Override public void tick(BlockState state, net.minecraft.server.level.ServerLevel world, BlockPos pos, Random random) {
        if (world.isClientSide || world.getBlockState(pos).getBlock() != this || !state.canSurvive(world, pos)
                || !canRelease(world.hasNeighborSignal(pos), world.isEmptyBlock(pos.below()))) return;
        BlockState empty = LightInteractions.replacement(state, EmptyVariant());
        if (!world.setBlock(pos, empty, 3)) return;
        Player nearest = world.getNearestPlayer(pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5, -1, false);
        if (!releaseLamp(world, pos, state, nearest != null && nearest.isCreative())
                && world.getBlockState(pos).getBlock() == empty.getBlock())
            world.setBlock(pos, state, 3);
    }

    private boolean releaseLamp(Level world, BlockPos pos, BlockState state, boolean preserveOnLanding) {
        Direction outwards = state.getValue(FurnitureBlock.DIRECTION).getOpposite();
        ReleasedLavaLamp lamp = new ReleasedLavaLamp(world,
                pos.getX() + .5 + .27 * outwards.getStepX(), pos.getY() + 5.0 / 16.0,
                pos.getZ() + .5 + .27 * outwards.getStepZ(),
                LightDrop().defaultBlockState().setValue(FurnitureBlock.DIRECTION, state.getValue(FurnitureBlock.DIRECTION)),
                pos.getY(), preserveOnLanding);
        return world.addFreshEntity(lamp);
    }

	@Override
	public BlockState updateShape(BlockState state, Direction direction, BlockState state2, LevelAccessor levelAccessor, BlockPos pos, BlockPos pos2) {
	    if (direction.getOpposite() == state.getValue(FurnitureBlock.DIRECTION) && !state.canSurvive(levelAccessor, pos)) {
	        levelAccessor.destroyBlock(pos, true);
	        return LightDrop().defaultBlockState().setValue(FurnitureBlock.WATERLOGGED, false);
	    }

	    return super.updateShape(state, direction, state2, levelAccessor, pos, pos2);
	}

	@Override
	public boolean removedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest,
									   FluidState fluid) {

		boolean isSilkTouch = EnchantmentHelper.getItemEnchantmentLevel(
                Enchantments.SILK_TOUCH, player.getMainHandItem()) > 0;

		boolean destroyed = super.removedByPlayer(state, level, pos, player, willHarvest, fluid);
		// The client predicts ordinary removal; falling and drops belong to the server.
		if (destroyed && !level.isClientSide && !player.isCreative()) {
			if (isSilkTouch)
				Block.popResource(level, pos, new ItemStack(LightDrop(), 1));
			else if (!releaseLamp(level, pos, state, false)) {
                // A protection mod can refuse the entity. Keep both contents and
                // frame, and return false so Forge does not also drop the frame.
                level.setBlock(pos, state, 3);
                return false;
			}
		}

		return destroyed;
	}

	@Override
	protected Block LightDrop() {
		return BlockObjectHolder.light_metal_ironage_block_floor_lava_clear;
	}

	@Override
	protected boolean ShouldDrop() {
		return false;
	}

	public LightSourceSconceLavaWall(float hardness, float blastResistance, SoundType sound, String name) {
		super(Block.Properties.of(Material.METAL).strength(hardness, blastResistance).sound(sound).lightLevel((p_50886_) -> 14));

		this.registerDefaultState(this.defaultBlockState() .setValue(FurnitureBlock.DIRECTION, Direction.NORTH));
		this.generateShapes(this.getStateDefinition().getPossibleStates());
		this.setRegistryName(name);
	}

	@Override
	public boolean placeLiquid(LevelAccessor world, BlockPos pos, BlockState blockState, FluidState fluidState) {
		boolean success = super.placeLiquid(world, pos, blockState, fluidState);

		if (!success || blockState.getValue(BlockStateProperties.WATERLOGGED)
				|| fluidState.getType() != Fluids.WATER || world.isClientSide()) {
			return success;
		}

		world.setBlock(pos, EmptyVariant().defaultBlockState()
			.setValue(FurnitureBlock.DIRECTION, blockState.getValue(BlockStateProperties.HORIZONTAL_FACING))
			.setValue(FurnitureBlock.WATERLOGGED, true)
            .setValue(zone.moddev.mc.ironagefurniture.api.SconceMetalData.METAL, zone.moddev.mc.ironagefurniture.api.SconceMetalData.get(blockState)), 3);

		world.playSound(null, pos, SoundEvents.GLASS_BREAK, SoundSource.BLOCKS, friction, explosionResistance);
		world.playSound(null, pos, SoundEvents.LAVA_EXTINGUISH, SoundSource.BLOCKS, friction, explosionResistance);

		if (world instanceof ServerLevel) {
			ServerLevel serverWorld = (ServerLevel) world;
			serverWorld.getServer().tell(new TickTask(serverWorld.getServer().getTickCount() + 1, () -> {
				Block.popResource(serverWorld, pos, new ItemStack(BlockObjectHolder.obsidian_chunk, 1));
			}));
		}

		world.getLiquidTicks().scheduleTick(pos, fluidState.getType(), fluidState.getType().getTickDelay(world));

		return success;
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, Random rnd) {
		BlockPos blockpos = pos.above();

		if (level.getBlockState(blockpos).isAir() && !level.getBlockState(blockpos).isSolidRender(level, blockpos)) {
			if (rnd.nextInt(25) == 0) {
				Direction direction = state.getValue(FurnitureBlock.DIRECTION);

				Pair<Double, Double> rotated = FurnitureBlock.rotate(0.6D, 0.5D, state.getValue(FurnitureBlock.DIRECTION));

				double x = (double) pos.getX() + rotated.getA();
				double y = (double) pos.getY() + 0.5D;
				double z = (double) pos.getZ() + rotated.getB();

				Direction direction1 = direction.getOpposite();

				level.addParticle(ParticleTypes.LAVA, x + 0.27D * (double) direction1.getStepX(), y + 0.22D, z + 0.27D * (double) direction1.getStepZ(), 0.0D, 0.0D, 0.0D);
				level.playLocalSound(x, y, z, SoundEvents.LAVA_POP, SoundSource.BLOCKS, 0.2F + rnd.nextFloat() * 0.2F, 0.9F + rnd.nextFloat() * 0.15F, false);
			}

			if (rnd.nextInt(200) == 0)
				level.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.LAVA_AMBIENT, SoundSource.BLOCKS, 0.2F + rnd.nextFloat() * 0.2F, 0.9F + rnd.nextFloat() * 0.15F, false);
		}

		super.animateTick(state, level, pos, rnd);
	}
}
