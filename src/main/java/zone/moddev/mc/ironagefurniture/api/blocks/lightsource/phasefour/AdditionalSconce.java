package zone.moddev.mc.ironagefurniture.api.blocks.lightsource.phasefour;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import com.google.common.collect.ImmutableList;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootContext;
import zone.moddev.mc.ironagefurniture.BlockObjectHolder;
import zone.moddev.mc.ironagefurniture.api.blocks.lightholder.LightHolderSconceFloor;
import zone.moddev.mc.ironagefurniture.init.PhaseFourLighting;

/** One-to-four candles or twin torches, with the usual removable sconce contents. */
public final class AdditionalSconce extends LightHolderSconceFloor {
    private final boolean wall, lit, twinTorch;
    private final int count;
    public AdditionalSconce(String name, boolean wall, boolean lit, boolean twinTorch, int count) {
        super(Block.Properties.of(Material.METAL).strength(1, 10).sound(SoundType.METAL)
                .lightLevel(state -> lit ? twinTorch ? 15 : 11 + count : 0));
        this.wall = wall;
        this.lit = lit;
        this.twinTorch = twinTorch;
        this.count = count;
        setRegistryName(name);
    }
    public boolean isWall() { return wall; }
    public boolean isLit() { return lit; }
    public boolean isTwinTorch() { return twinTorch; }
    public int contentsCount() { return count; }
    private Block variant(int count, boolean lit, boolean wall) {
        return PhaseFourLighting.sconce(wall, lit, twinTorch, count);
    }
    private Block empty() {
        return wall ? BlockObjectHolder.light_metal_ironage_sconce_wall_empty_iron
                : BlockObjectHolder.light_metal_ironage_sconce_floor_empty_iron;
    }
    private Item contents() { return twinTorch ? Items.TORCH : PhaseFourLighting.candle(false, true).asItem(); }
    @Override protected void generateShapes(ImmutableList<BlockState> states) { }
    @Override public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        VoxelShape shape = !wall ? Block.box(5, 0, 5, 11, 16, 11)
                : twinTorch ? Block.box(0, 2, 5, 7, 16, 11) : Block.box(5, 2, 0, 11, 16, 7);
        Direction facing = state.getValue(DIRECTION);
        // The original wall twin-torch model is authored east-facing.
        if (wall && twinTorch) facing = facing.getClockWise();
        return Candle.rotated(shape, facing, wall);
    }
    @Override public VoxelShape getOcclusionShape(BlockState state, BlockGetter world, BlockPos pos) {
        return getShape(state, world, pos, CollisionContext.empty());
    }
    @Override public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        Direction face = wall ? state.getValue(DIRECTION) : Direction.UP;
        return Block.canSupportCenter(world, pos.relative(face.getOpposite()), face);
    }
    @Override public BlockState getStateForPlacement(BlockPlaceContext context) {
        boolean wet = context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER;
        if (context.getClickedFace().getAxis().isHorizontal()) {
            BlockState onWall = variant(count, lit && !wet, true).defaultBlockState()
                    .setValue(DIRECTION, context.getClickedFace()).setValue(WATERLOGGED, wet);
            if (onWall.canSurvive(context.getLevel(), context.getClickedPos())) return onWall;
        }
        BlockState placed = variant(count, lit && !wet, false).defaultBlockState()
                .setValue(DIRECTION, context.getHorizontalDirection()).setValue(WATERLOGGED, wet);
        return placed.canSurvive(context.getLevel(), context.getClickedPos()) ? placed : null;
    }
    @Override public BlockState updateShape(BlockState state, Direction direction, BlockState neighbour,
            LevelAccessor world, BlockPos pos, BlockPos neighbourPos) {
        // Air requests normal destruction and drops; Minecraft restores any water afterwards.
        if (!state.canSurvive(world, pos)) return Blocks.AIR.defaultBlockState();
        if (state.getValue(WATERLOGGED)) world.getLiquidTicks().scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
        return state;
    }
    @Override public boolean placeLiquid(LevelAccessor world, BlockPos pos, BlockState state, FluidState fluid) {
        if (state.getValue(WATERLOGGED) || fluid.getType() != Fluids.WATER) return false;
        if (!world.isClientSide()) {
            world.setBlock(pos, LightInteractions.replacement(state, variant(count, false, wall)).setValue(WATERLOGGED, true), 3);
            world.getLiquidTicks().scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
        }
        return true;
    }
    @Override public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        return Arrays.asList(zone.moddev.mc.ironagefurniture.api.SconceMetalData.create(BlockObjectHolder.light_metal_ironage_sconce_floor_empty_iron, zone.moddev.mc.ironagefurniture.api.SconceMetalData.get(state)), new ItemStack(contents(), count));
    }
    @Override public ItemStack getPickBlock(BlockState state, HitResult hit, BlockGetter world, BlockPos pos, Player player) {
        return zone.moddev.mc.ironagefurniture.api.SconceMetalData.create(BlockObjectHolder.light_metal_ironage_sconce_floor_empty_iron, zone.moddev.mc.ironagefurniture.api.SconceMetalData.get(state));
    }
    @Override protected net.minecraft.world.InteractionResult ActivateSconce(BlockState state, Level world, BlockPos pos, Player player,
            InteractionHand hand, BlockHitResult hit) {
        ItemStack held = player.getItemInHand(hand);
        // Match single-torch sconces: extinguish without pouring or emptying the bucket.
        if (held.getItem() == Items.WATER_BUCKET) {
            if (!world.isClientSide)
                LightInteractions.replace(world, pos, state, variant(count, false, wall));
            return net.minecraft.world.InteractionResult.SUCCESS;
        }
        // Lighting an extinguished sconce does not consume another candle or torch.
        // Do this before the insertion/removal branches, as in the legacy release.
        if (!lit && !state.getValue(WATERLOGGED) && (held.getItem() == Items.FLINT_AND_STEEL
                || held.getItem() == Items.TORCH || !twinTorch && held.getItem() == contents())) {
            if (!world.isClientSide) {
                LightInteractions.replace(world, pos, state, variant(count, true, wall));
                if (!player.isCreative() && held.getItem() == Items.FLINT_AND_STEEL)
                    held.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));
            }
            return net.minecraft.world.InteractionResult.SUCCESS;
        }
        if (held.isEmpty() || held.getItem() == contents()) {
            if (!world.isClientSide) {
                if (!twinTorch && held.getItem() == contents() && count < 4) {
                    LightInteractions.replace(world, pos, state, variant(count + 1, !state.getValue(WATERLOGGED), wall));
                    if (!player.isCreative()) held.shrink(1);
                } else {
                    LightInteractions.replace(world, pos, state, empty());
                    LightInteractions.give(player, hand, contents(), count);
                }
            }
            return net.minecraft.world.InteractionResult.SUCCESS;
        }
        return PhaseFourLighting.isLightItem(held) ? net.minecraft.world.InteractionResult.SUCCESS : net.minecraft.world.InteractionResult.PASS;
    }
    boolean litAfterRedstone(boolean waterlogged, boolean powered) {
        // Power keeps submerged flames burning; dry flames stay lit afterwards.
        return waterlogged ? powered : lit || powered;
    }
    @Override public void onPlace(BlockState state, Level world, BlockPos pos, BlockState previous, boolean moving) {
        super.onPlace(state, world, pos, previous, moving);
        // Filling the holder or adding water needn't change an existing signal.
        if (previous.getBlock() != this)
            neighborChanged(state, world, pos, previous.getBlock(), pos, moving);
    }
    @Override public void neighborChanged(BlockState state, Level world, BlockPos pos, Block changed, BlockPos from, boolean moving) {
        if (!world.isClientSide && litAfterRedstone(state.getValue(WATERLOGGED), world.hasNeighborSignal(pos)) != lit)
            world.getBlockTicks().scheduleTick(pos, this, 2);
    }
    @Override public void tick(BlockState state, net.minecraft.server.level.ServerLevel world, BlockPos pos, Random random) {
        if (!world.isClientSide) {
            boolean nextLit = litAfterRedstone(state.getValue(WATERLOGGED), world.hasNeighborSignal(pos));
            if (nextLit != lit) LightInteractions.replace(world, pos, state, variant(count, nextLit, wall));
        }
    }
    @Override public boolean canConnectRedstone(BlockState state, BlockGetter world, BlockPos pos, Direction side) { return true; }
    @Override public void animateTick(BlockState state, Level world, BlockPos pos, Random random) {
        if (!lit) return;
        for (int index = 0; index < count; index++) {
            Vec3 flame = getFlameOffset(state, index);
            double x = pos.getX() + flame.x, y = pos.getY() + flame.y, z = pos.getZ() + flame.z;
            world.addParticle(twinTorch ? ParticleTypes.FLAME : PhaseFourLighting.CANDLE_FLAME, x, y, z, 0, 0, 0);
            if (twinTorch || random.nextInt(3) == 0) world.addParticle(ParticleTypes.SMOKE, x, y + .04, z, 0, 0, 0);
        }
    }
    public Vec3 getFlameOffset(BlockState state, int index) {
        double[] flame = flamePoint(index);
        Direction facing = state.getValue(DIRECTION);
        if (wall && twinTorch) facing = facing.getClockWise();
        double[] point = Candle.rotatedPoint(flame[0] / 16, flame[2] / 16, facing, wall);
        return new Vec3(point[0], flame[1] / 16, point[1]);
    }
    private double[] flamePoint(int index) {
        if (twinTorch) return wall ? new double[]{index == 0 ? 4.5 : 3.5, 15.6, index == 0 ? 10.5 : 5.5}
                : new double[]{index == 0 ? 6.1 : 9.9, 15.6, index == 0 ? 6.8 : 9.2};
        if (wall) {
            if (count == 1) return new double[]{7.5, 15.6, 4.5};
            if (count == 2) return index == 0 ? new double[]{9.5, 15.6, 2.5} : new double[]{6.5, 13.6, 5.5};
            if (count == 3) return index == 0 ? new double[]{9.5, 15.6, 2.5}
                    : index == 1 ? new double[]{9.5, 14.6, 5.5} : new double[]{6.5, 13.6, 5.5};
            return index == 0 ? new double[]{9.5, 15.6, 2.5} : index == 1 ? new double[]{9.5, 14.6, 5.5}
                    : index == 2 ? new double[]{6.5, 16.25, 2.5} : new double[]{6.5, 13.6, 5.5};
        }
        double[][] points;
        if (count == 1) points = new double[][]{{7.5, 15.6, 7.5}};
        else if (count == 2) points = new double[][]{{6.5, 15.6, 7.5}, {9.5, 15.6, 8.5}};
        else if (count == 3) points = new double[][]{{6.5, 15.6, 6.5}, {9.5, 15.6, 7.5}, {6.5, 14.6, 9.5}};
        else points = new double[][]{{6.5, 15.6, 6.5}, {9.5, 15.6, 9.5}, {9.5, 13.6, 6.5}, {6.5, 14.6, 9.5}};
        return points[index];
    }
}
