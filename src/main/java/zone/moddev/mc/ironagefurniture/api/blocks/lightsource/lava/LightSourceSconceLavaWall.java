package zone.moddev.mc.ironagefurniture.api.blocks.lightsource.lava;

import zone.moddev.mc.ironagefurniture.api.blocks.base.FurnitureBlock;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.glow.LightSourceSconceGlowWall;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.fluid.IFluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.block.material.Material;
import com.mojang.datafixers.util.Pair;
import net.minecraft.util.Direction;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.SoundCategory;

import java.util.Random;

import zone.moddev.mc.ironagefurniture.BlockObjectHolder;
import zone.moddev.mc.ironagefurniture.api.entity.ReleasedLavaLamp;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.phasefour.LightInteractions;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.concurrent.TickDelayedTask;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.world.World;
import net.minecraft.world.IWorld;

public class LightSourceSconceLavaWall extends LightSourceSconceGlowWall {
    @Override public void onPlace(BlockState state, World world, BlockPos pos, BlockState old, boolean moving) {
        super.onPlace(state, world, pos, old, moving);
        scheduleRelease(world, pos);
    }

    @Override public void neighborChanged(BlockState state, World world, BlockPos pos, Block neighbour,
            BlockPos neighbourPos, boolean moving) {
        super.neighborChanged(state, world, pos, neighbour, neighbourPos, moving);
        scheduleRelease(world, pos);
    }

    private void scheduleRelease(World world, BlockPos pos) {
        if (!world.isClientSide && world.getBlockState(pos).getBlock() == this
                && canRelease(world.hasNeighborSignal(pos), world.isEmptyBlock(pos.below())))
            world.getBlockTicks().scheduleTick(pos, this, 2);
    }

    static boolean canRelease(boolean powered, boolean airBelow) { return powered && airBelow; }

    @Override public void tick(BlockState state, net.minecraft.world.server.ServerWorld world, BlockPos pos, Random random) {
        if (world.isClientSide || world.getBlockState(pos).getBlock() != this || !state.canSurvive(world, pos)
                || !canRelease(world.hasNeighborSignal(pos), world.isEmptyBlock(pos.below()))) return;
        BlockState empty = LightInteractions.replacement(state, EmptyVariant());
        if (!world.setBlock(pos, empty, 3)) return;
        Direction outwards = state.getValue(FurnitureBlock.DIRECTION).getOpposite();
        PlayerEntity nearest = world.getNearestPlayer(pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5, -1, false);
        ReleasedLavaLamp lamp = new ReleasedLavaLamp(world,
                pos.getX() + .5 + .27 * outwards.getStepX(), pos.getY() + 5.0 / 16.0,
                pos.getZ() + .5 + .27 * outwards.getStepZ(),
                LightDrop().defaultBlockState().setValue(FurnitureBlock.DIRECTION, state.getValue(FurnitureBlock.DIRECTION)),
                pos.getY(), nearest != null && nearest.isCreative());
        if (!world.addFreshEntity(lamp) && world.getBlockState(pos).getBlock() == empty.getBlock())
            world.setBlock(pos, state, 3);
    }

	@Override
	public BlockState updateShape(BlockState state, Direction direction, BlockState state2, IWorld levelAccessor, BlockPos pos, BlockPos pos2) {
	    if (direction.getOpposite() == state.getValue(FurnitureBlock.DIRECTION) && !state.canSurvive(levelAccessor, pos)) {
	        levelAccessor.destroyBlock(pos, true);
	        return LightDrop().defaultBlockState().setValue(FurnitureBlock.WATERLOGGED, false);
	    }

	    return super.updateShape(state, direction, state2, levelAccessor, pos, pos2);
	}

	@Override
	public boolean removedByPlayer(BlockState state, World level, BlockPos pos, PlayerEntity player, boolean willHarvest,
									   IFluidState fluid) {

		boolean isSilkTouch = EnchantmentHelper.getItemEnchantmentLevel(
                Enchantments.SILK_TOUCH, player.getMainHandItem()) > 0;

		if (isSilkTouch && !player.isCreative())
			Block.popResource(level, pos, new ItemStack(LightDrop(), 1));

		boolean destroyed = super.removedByPlayer(state, level, pos, player, willHarvest, fluid);

		if (!isSilkTouch && !player.isCreative()) {
			level.playSound(player, pos, SoundEvents.GLASS_BREAK, SoundCategory.BLOCKS, 1.0F, 1.0F);
			level.setBlock(pos, Blocks.FIRE.defaultBlockState(), 3);
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
		super(Block.Properties.of(Material.METAL).strength(hardness, blastResistance).sound(sound).lightLevel(14));

		this.registerDefaultState(this.defaultBlockState() .setValue(FurnitureBlock.DIRECTION, Direction.NORTH));
		this.generateShapes(this.getStateDefinition().getPossibleStates());
		this.setRegistryName(name);
	}

	@Override
	public boolean placeLiquid(IWorld world, BlockPos pos, BlockState blockState, IFluidState fluidState) {
		boolean success = super.placeLiquid(world, pos, blockState, fluidState);

		if (!success || blockState.getValue(BlockStateProperties.WATERLOGGED)
				|| fluidState.getType() != Fluids.WATER || world.isClientSide()) {
			return success;
		}

		world.setBlock(pos, EmptyVariant().defaultBlockState()
			.setValue(FurnitureBlock.DIRECTION, blockState.getValue(BlockStateProperties.HORIZONTAL_FACING))
			.setValue(FurnitureBlock.WATERLOGGED, true)
            .setValue(zone.moddev.mc.ironagefurniture.api.SconceMetalData.METAL, zone.moddev.mc.ironagefurniture.api.SconceMetalData.get(blockState)), 3);

		world.playSound(null, pos, SoundEvents.GLASS_BREAK, SoundCategory.BLOCKS, 1.0F, 1.0F);
		world.playSound(null, pos, SoundEvents.LAVA_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 2.6F);

		if (world instanceof ServerWorld) {
			ServerWorld serverWorld = (ServerWorld) world;
			serverWorld.getServer().tell(new TickDelayedTask(serverWorld.getServer().getTickCount() + 1, () -> {
				Block.popResource(serverWorld, pos, new ItemStack(BlockObjectHolder.obsidian_chunk, 1));
			}));
		}

		world.getLiquidTicks().scheduleTick(pos, fluidState.getType(), fluidState.getType().getTickDelay(world));

		return success;
	}

	@Override
	public void animateTick(BlockState state, World level, BlockPos pos, Random rnd) {
		BlockPos blockpos = pos.above();

		if (level.getBlockState(blockpos).isAir() && !level.getBlockState(blockpos).isSolidRender(level, blockpos)) {
			if (rnd.nextInt(25) == 0) {
				Direction direction = state.getValue(FurnitureBlock.DIRECTION);

				Pair<Double, Double> rotated = FurnitureBlock.rotate(0.6D, 0.5D, state.getValue(FurnitureBlock.DIRECTION));

				double x = (double) pos.getX() + rotated.getFirst();
				double y = (double) pos.getY() + 0.5D;
				double z = (double) pos.getZ() + rotated.getSecond();

				Direction direction1 = direction.getOpposite();

				level.addParticle(ParticleTypes.LAVA, x + 0.27D * (double) direction1.getStepX(), y + 0.22D, z + 0.27D * (double) direction1.getStepZ(), 0.0D, 0.0D, 0.0D);
				level.playLocalSound(x, y, z, SoundEvents.LAVA_POP, SoundCategory.BLOCKS, 0.2F + rnd.nextFloat() * 0.2F, 0.9F + rnd.nextFloat() * 0.15F, false);
			}

			if (rnd.nextInt(200) == 0)
				level.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.LAVA_AMBIENT, SoundCategory.BLOCKS, 0.2F + rnd.nextFloat() * 0.2F, 0.9F + rnd.nextFloat() * 0.15F, false);
		}

		super.animateTick(state, level, pos, rnd);
	}
}
