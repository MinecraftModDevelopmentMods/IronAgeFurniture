package zone.moddev.mc.ironagefurniture.api.blocks.lightsource.phasefour;

import java.util.Collections;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootContext;
import zone.moddev.mc.ironagefurniture.api.blocks.base.FurnitureBlock;
import zone.moddev.mc.ironagefurniture.init.PhaseFourLighting;

/** A small candle and holder; the wall variant shares its inventory item. */
public final class Candle extends FurnitureBlock {
    private final boolean wall, lit;
    public Candle(String name, boolean wall, boolean lit) {
        super(Block.Properties.of(Material.DECORATION).strength(.2F).sound(SoundType.WOOL)
                .lightLevel(state -> lit ? 12 : 0));
        this.wall = wall;
        this.lit = lit;
        setRegistryName(name);
        registerDefaultState(defaultBlockState().setValue(DIRECTION, Direction.NORTH).setValue(WATERLOGGED, false));
    }
    public boolean isWall() { return wall; }
    public boolean isLit() { return lit; }
    @Override protected void generateShapes(ImmutableList<BlockState> states) { }
    @Override public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        VoxelShape shape = wall ? Block.box(5, 5, 0, 10, 10, 4)
                : Block.box(4, 0, 4, 9, 3, 11);
        return rotated(shape, state.getValue(DIRECTION), wall);
    }
    @Override public VoxelShape getOcclusionShape(BlockState state, BlockGetter world, BlockPos pos) {
        return getShape(state, world, pos, CollisionContext.empty());
    }
    public static VoxelShape rotated(VoxelShape shape, Direction facing, boolean wall) {
        // Floor models are authored north-facing; wall models face south.
        int turns = (facing.get2DDataValue() + (wall ? 0 : 2)) & 3;
        for (int turn = 0; turn < turns; turn++) shape = net.minecraft.world.phys.shapes.Shapes.box(
                1 - shape.max(Direction.Axis.Z), shape.min(Direction.Axis.Y), shape.min(Direction.Axis.X),
                1 - shape.min(Direction.Axis.Z), shape.max(Direction.Axis.Y), shape.max(Direction.Axis.X));
        return shape;
    }
    public static double[] rotatedPoint(double x, double z, Direction facing, boolean wall) {
        int turns = (facing.get2DDataValue() + (wall ? 0 : 2)) & 3;
        for (int turn = 0; turn < turns; turn++) { double previousX = x; x = 1 - z; z = previousX; }
        return new double[]{x, z};
    }
    @Override public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        Direction face = wall ? state.getValue(DIRECTION) : Direction.UP;
        return Block.canSupportCenter(world, pos.relative(face.getOpposite()), face);
    }
    @Override public BlockState getStateForPlacement(BlockPlaceContext context) {
        boolean wet = context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER;
        if (context.getClickedFace().getAxis().isHorizontal()) {
            BlockState onWall = PhaseFourLighting.candle(true, lit && !wet).defaultBlockState()
                    .setValue(DIRECTION, context.getClickedFace()).setValue(WATERLOGGED, wet);
            if (onWall.canSurvive(context.getLevel(), context.getClickedPos())) return onWall;
        }
        BlockState floor = PhaseFourLighting.candle(false, lit && !wet).defaultBlockState()
                .setValue(DIRECTION, context.getHorizontalDirection()).setValue(WATERLOGGED, wet);
        return floor.canSurvive(context.getLevel(), context.getClickedPos()) ? floor : null;
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
            world.setBlock(pos, LightInteractions.replacement(state, PhaseFourLighting.candle(wall, false))
                    .setValue(WATERLOGGED, true), 3);
            world.getLiquidTicks().scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
        }
        return true;
    }
    @Override public net.minecraft.world.InteractionResult use(BlockState state, Level world, BlockPos pos, Player player,
            InteractionHand hand, BlockHitResult hit) {
        if (!lit && !state.getValue(WATERLOGGED) && (player.getItemInHand(hand).getItem() == Items.FLINT_AND_STEEL
                || player.getItemInHand(hand).getItem() == Items.TORCH)) {
            if (!world.isClientSide) {
                LightInteractions.replace(world, pos, state, PhaseFourLighting.candle(wall, true));
                if (!player.isCreative() && player.getItemInHand(hand).getItem() == Items.FLINT_AND_STEEL)
                    player.getItemInHand(hand).hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));
            }
            return net.minecraft.world.InteractionResult.SUCCESS;
        }
        // A bucket click puts the flame out; it does not pour water over the holder.
        if (player.getItemInHand(hand).getItem() == Items.WATER_BUCKET) {
            if (!world.isClientSide)
                LightInteractions.replace(world, pos, state, PhaseFourLighting.candle(wall, false));
            return net.minecraft.world.InteractionResult.SUCCESS;
        }
        return net.minecraft.world.InteractionResult.PASS;
    }
    @Override public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        return Collections.singletonList(new ItemStack(PhaseFourLighting.candle(false, true)));
    }
    @Override public ItemStack getPickBlock(BlockState state, HitResult hit, BlockGetter world, BlockPos pos, Player player) {
        return new ItemStack(PhaseFourLighting.candle(false, true));
    }
    @Override public void animateTick(BlockState state, Level world, BlockPos pos, Random random) {
        if (!lit || state.getValue(WATERLOGGED)) return;
        double[] point = rotatedPoint((wall ? 7.5 : 6.5) / 16, (wall ? 1.5 : 6.5) / 16, state.getValue(DIRECTION), wall);
        double x = pos.getX() + point[0], y = pos.getY() + (wall ? 14.6 : 6.7) / 16, z = pos.getZ() + point[1];
        world.addParticle(PhaseFourLighting.CANDLE_FLAME, x, y, z, 0, 0, 0);
        if (random.nextInt(3) == 0) world.addParticle(ParticleTypes.SMOKE, x, y + .04, z, 0, 0, 0);
    }
}
