package zone.moddev.mc.ironagefurniture.api.blocks.lightsource.phasefour;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import zone.moddev.mc.ironagefurniture.api.SconceMetalData;
import zone.moddev.mc.ironagefurniture.api.blocks.furniture.UprightFurnitureShapes;
import zone.moddev.mc.ironagefurniture.api.blocks.lightholder.LightHolderSconceFloor;
import zone.moddev.mc.ironagefurniture.init.PhaseFourLighting;

/** Both torches are recovered together; water and power never consume either. */
public final class TwinTorchSconce extends LightHolderSconceFloor {
    private final boolean wall, lit;
    public TwinTorchSconce(String path, boolean wall, boolean lit) {
        super(Block.Properties.of().strength(1, 10).sound(SoundType.METAL)
                .noOcclusion().lightLevel(state -> lit ? 15 : 0));
        this.wall = wall;
        this.lit = lit;
    }
    public boolean isWall() { return wall; }
    public boolean isLit() { return lit; }
    private void replace(LevelAccessor level, BlockPos pos, BlockState state, boolean lit) {
        level.setBlock(pos, zone.moddev.mc.ironagefurniture.api.SconceMetalData.transition(PhaseFourLighting.twin(wall, lit), state), Block.UPDATE_ALL);
    }
    @Override protected InteractionResult ActivateSconce(BlockState state, Level level, BlockPos pos,
            Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide()) return InteractionResult.SUCCESS;
        ItemStack held = player.getItemInHand(hand);
        if (held.is(Items.WATER_BUCKET)) {
            replace(level, pos, state, false);
        } else if (!lit && !state.getValue(WATERLOGGED) && (held.is(Items.TORCH) || held.is(Items.FLINT_AND_STEEL))) {
            replace(level, pos, state, true);
            if (held.is(Items.FLINT_AND_STEEL) && !player.isCreative()) held.hurtAndBreak(1, player,
                    broken -> broken.broadcastBreakEvent(hand));
        } else if (held.isEmpty() || held.is(Items.TORCH)) {
            level.setBlock(pos, SconceMetalData.empty(state, wall), Block.UPDATE_ALL);
            ItemStack torches = new ItemStack(Items.TORCH, 2);
            player.getInventory().placeItemBackInInventory(torches);
        }
        BlockState current = level.getBlockState(pos);
        if (current.getBlock() instanceof TwinTorchSconce) level.scheduleTick(pos, current.getBlock(), 2);
        return InteractionResult.CONSUME;
    }
    @Override public void neighborChanged(BlockState state, Level level, BlockPos pos, Block changed, BlockPos changedPos, boolean moving) {
        if (!level.isClientSide()) level.scheduleTick(pos, this, 2);
    }
    @Override public void onPlace(BlockState state, Level level, BlockPos pos, BlockState previous, boolean moving) {
        super.onPlace(state, level, pos, previous, moving);
        if (!level.isClientSide()) level.scheduleTick(pos, this, 2);
    }
    @Override public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        boolean power = level.hasNeighborSignal(pos);
        boolean next = state.getValue(WATERLOGGED) ? power : lit || power;
        if (lit != next) replace(level, pos, state, next);
    }
    @Override public boolean placeLiquid(LevelAccessor level, BlockPos pos, BlockState state, FluidState fluid) {
        if (state.getValue(WATERLOGGED) || !fluid.is(Fluids.WATER)) return false;
        if (!level.isClientSide()) {
            replace(level, pos, state.setValue(WATERLOGGED, true), false);
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        return true;
    }
    @Override public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        Direction support = wall ? state.getValue(DIRECTION) : Direction.UP;
        return Block.canSupportCenter(level, pos.relative(support.getOpposite()), support);
    }
    @Override public BlockState updateShape(BlockState state, Direction direction, BlockState neighbour,
            LevelAccessor level, BlockPos pos, BlockPos neighbourPos) {
        if (!canSurvive(state, level, pos)) return Blocks.AIR.defaultBlockState();
        if (state.getValue(WATERLOGGED)) level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        return state;
    }
    @Override public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        return List.of(SconceMetalData.frame(state), new ItemStack(Items.TORCH, 2));
    }
    @Override public ItemStack getCloneItemStack(BlockState state, net.minecraft.world.phys.HitResult hit, net.minecraft.world.level.BlockGetter level, BlockPos pos, Player player) {
        return SconceMetalData.frame(state);
    }
    @Override public VoxelShape getShape(BlockState state, net.minecraft.world.level.BlockGetter level, BlockPos pos, CollisionContext context) {
        return zone.moddev.mc.ironagefurniture.api.blocks.base.FurnitureBlock.rotate(
                wall ? Block.box(-.5, 2, 4, 7, 16, 12) : Block.box(4, 0, 5, 12, 16, 11),
                wall ? state.getValue(DIRECTION) : state.getValue(DIRECTION).getClockWise());
    }
    @Override public VoxelShape getOcclusionShape(BlockState state, net.minecraft.world.level.BlockGetter level, BlockPos pos) { return net.minecraft.world.phys.shapes.Shapes.empty(); }
    @Override public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (!lit) return;
        double[][] wicks = wall ? new double[][]{{4.5, 15.35, 10.51}, {3.5, 15.35, 5.49}}
                : new double[][]{{5.42, 14.24, 7.25}, {10.58, 14.24, 8.75}};
        for (double[] wick : wicks) {
            var point = zone.moddev.mc.ironagefurniture.api.blocks.base.FurnitureBlock.rotate(wick[0]/16, wick[2]/16,
                    wall ? state.getValue(DIRECTION) : state.getValue(DIRECTION).getClockWise());
            level.addParticle(net.minecraft.core.particles.ParticleTypes.FLAME, pos.getX() + point.getA(), pos.getY() + wick[1]/16, pos.getZ() + point.getB(), 0, 0, 0);
            level.addParticle(net.minecraft.core.particles.ParticleTypes.SMOKE, pos.getX() + point.getA(), pos.getY() + wick[1]/16 + .04, pos.getZ() + point.getB(), 0, 0, 0);
        }
    }
}
