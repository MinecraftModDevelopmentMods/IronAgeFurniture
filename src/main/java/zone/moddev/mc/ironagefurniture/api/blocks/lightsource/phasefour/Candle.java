package zone.moddev.mc.ironagefurniture.api.blocks.lightsource.phasefour;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import com.google.common.collect.ImmutableList;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootContext;
import zone.moddev.mc.ironagefurniture.api.blocks.base.FurnitureBlock;
import zone.moddev.mc.ironagefurniture.init.PhaseFourLighting;

/** A small candle and holder; the wall variant shares its inventory item. */
public final class Candle extends FurnitureBlock {
    private final boolean wall, lit;
    public Candle(String name, boolean wall, boolean lit) {
        super(Block.Properties.of(Material.DECORATION).strength(.2F).sound(SoundType.WOOL)
                .lightLevel(lit ? 12 : 0));
        this.wall = wall;
        this.lit = lit;
        setRegistryName(name);
        registerDefaultState(defaultBlockState().setValue(DIRECTION, Direction.NORTH).setValue(WATERLOGGED, false));
    }
    public boolean isWall() { return wall; }
    public boolean isLit() { return lit; }
    @Override protected void generateShapes(ImmutableList<BlockState> states) { }
    @Override public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
        VoxelShape shape = wall ? Block.box(5, 5, 0, 10, 10, 4)
                : Block.box(4, 0, 4, 9, 3, 11);
        return rotated(shape, state.getValue(DIRECTION), wall);
    }
    @Override public VoxelShape getOcclusionShape(BlockState state, IBlockReader world, BlockPos pos) {
        return getShape(state, world, pos, ISelectionContext.empty());
    }
    public static VoxelShape rotated(VoxelShape shape, Direction facing, boolean wall) {
        // Floor models are authored north-facing; wall models face south.
        int turns = (facing.get2DDataValue() + (wall ? 0 : 2)) & 3;
        for (int turn = 0; turn < turns; turn++) shape = net.minecraft.util.math.shapes.VoxelShapes.box(
                1 - shape.max(Direction.Axis.Z), shape.min(Direction.Axis.Y), shape.min(Direction.Axis.X),
                1 - shape.min(Direction.Axis.Z), shape.max(Direction.Axis.Y), shape.max(Direction.Axis.X));
        return shape;
    }
    public static double[] rotatedPoint(double x, double z, Direction facing, boolean wall) {
        int turns = (facing.get2DDataValue() + (wall ? 0 : 2)) & 3;
        for (int turn = 0; turn < turns; turn++) { double previousX = x; x = 1 - z; z = previousX; }
        return new double[]{x, z};
    }
    @Override public boolean canSurvive(BlockState state, IWorldReader world, BlockPos pos) {
        Direction face = wall ? state.getValue(DIRECTION) : Direction.UP;
        return Block.canSupportCenter(world, pos.relative(face.getOpposite()), face);
    }
    @Override public BlockState getStateForPlacement(BlockItemUseContext context) {
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
            IWorld world, BlockPos pos, BlockPos neighbourPos) {
        // Air requests normal destruction and drops; Minecraft restores any water afterwards.
        if (!state.canSurvive(world, pos)) return Blocks.AIR.defaultBlockState();
        if (state.getValue(WATERLOGGED)) world.getLiquidTicks().scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
        return state;
    }
    @Override public boolean placeLiquid(IWorld world, BlockPos pos, BlockState state, IFluidState fluid) {
        if (state.getValue(WATERLOGGED) || fluid.getType() != Fluids.WATER) return false;
        if (!world.isClientSide()) {
            world.setBlock(pos, LightInteractions.replacement(state, PhaseFourLighting.candle(wall, false))
                    .setValue(WATERLOGGED, true), 3);
            world.getLiquidTicks().scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
        }
        return true;
    }
    @Override public net.minecraft.util.ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player,
            Hand hand, BlockRayTraceResult hit) {
        if (!lit && !state.getValue(WATERLOGGED) && (player.getItemInHand(hand).getItem() == Items.FLINT_AND_STEEL
                || player.getItemInHand(hand).getItem() == Items.TORCH)) {
            if (!world.isClientSide) {
                LightInteractions.replace(world, pos, state, PhaseFourLighting.candle(wall, true));
                if (!player.isCreative() && player.getItemInHand(hand).getItem() == Items.FLINT_AND_STEEL)
                    player.getItemInHand(hand).hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));
            }
            return net.minecraft.util.ActionResultType.SUCCESS;
        }
        // A bucket click puts the flame out; it does not pour water over the holder.
        if (player.getItemInHand(hand).getItem() == Items.WATER_BUCKET) {
            if (!world.isClientSide)
                LightInteractions.replace(world, pos, state, PhaseFourLighting.candle(wall, false));
            return net.minecraft.util.ActionResultType.SUCCESS;
        }
        return net.minecraft.util.ActionResultType.PASS;
    }
    @Override public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        return Collections.singletonList(new ItemStack(PhaseFourLighting.candle(false, true)));
    }
    @Override public ItemStack getPickBlock(BlockState state, RayTraceResult hit, IBlockReader world, BlockPos pos, PlayerEntity player) {
        return new ItemStack(PhaseFourLighting.candle(false, true));
    }
    @Override public void animateTick(BlockState state, World world, BlockPos pos, Random random) {
        if (!lit || state.getValue(WATERLOGGED)) return;
        double[] point = rotatedPoint((wall ? 7.5 : 6.5) / 16, (wall ? 1.5 : 6.5) / 16, state.getValue(DIRECTION), wall);
        double x = pos.getX() + point[0], y = pos.getY() + (wall ? 14.6 : 6.7) / 16, z = pos.getZ() + point[1];
        world.addParticle(PhaseFourLighting.CANDLE_FLAME, x, y, z, 0, 0, 0);
        if (random.nextInt(3) == 0) world.addParticle(ParticleTypes.SMOKE, x, y + .04, z, 0, 0, 0);
    }
}
