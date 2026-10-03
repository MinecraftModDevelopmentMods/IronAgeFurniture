package zone.moddev.mc.ironagefurniture.api.blocks.lightsource.phasefour;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import com.google.common.collect.ImmutableList;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
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
        super(Block.Properties.create(Material.MISCELLANEOUS).hardnessAndResistance(.2F).sound(SoundType.CLOTH)
                .lightValue(lit ? 12 : 0));
        this.wall = wall;
        this.lit = lit;
        setRegistryName(name);
        setDefaultState(getDefaultState().with(DIRECTION, Direction.NORTH).with(WATERLOGGED, false));
    }
    public boolean isWall() { return wall; }
    public boolean isLit() { return lit; }
    @Override protected void generateShapes(ImmutableList<BlockState> states) { }
    @Override public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
        VoxelShape shape = wall ? Block.makeCuboidShape(5, 5, 0, 10, 10, 4)
                : Block.makeCuboidShape(4, 0, 4, 9, 3, 11);
        return rotated(shape, state.get(DIRECTION), wall);
    }
    @Override public VoxelShape getRenderShape(BlockState state, IBlockReader world, BlockPos pos) {
        return getShape(state, world, pos, ISelectionContext.dummy());
    }
    public static VoxelShape rotated(VoxelShape shape, Direction facing, boolean wall) {
        // Floor models are authored north-facing; wall models face south.
        int turns = (facing.getHorizontalIndex() + (wall ? 0 : 2)) & 3;
        for (int turn = 0; turn < turns; turn++) shape = net.minecraft.util.math.shapes.VoxelShapes.create(
                1 - shape.getEnd(Direction.Axis.Z), shape.getStart(Direction.Axis.Y), shape.getStart(Direction.Axis.X),
                1 - shape.getStart(Direction.Axis.Z), shape.getEnd(Direction.Axis.Y), shape.getEnd(Direction.Axis.X));
        return shape;
    }
    public static double[] rotatedPoint(double x, double z, Direction facing, boolean wall) {
        int turns = (facing.getHorizontalIndex() + (wall ? 0 : 2)) & 3;
        for (int turn = 0; turn < turns; turn++) { double previousX = x; x = 1 - z; z = previousX; }
        return new double[]{x, z};
    }
    @Override public boolean isValidPosition(BlockState state, IWorldReader world, BlockPos pos) {
        Direction face = wall ? state.get(DIRECTION) : Direction.UP;
        return Block.func_220055_a(world, pos.offset(face.getOpposite()), face);
    }
    @Override public BlockState getStateForPlacement(BlockItemUseContext context) {
        boolean wet = context.getWorld().getFluidState(context.getPos()).getFluid() == Fluids.WATER;
        if (context.getFace().getAxis().isHorizontal()) {
            BlockState onWall = PhaseFourLighting.candle(true, lit && !wet).getDefaultState()
                    .with(DIRECTION, context.getFace()).with(WATERLOGGED, wet);
            if (onWall.isValidPosition(context.getWorld(), context.getPos())) return onWall;
        }
        BlockState floor = PhaseFourLighting.candle(false, lit && !wet).getDefaultState()
                .with(DIRECTION, context.getPlacementHorizontalFacing()).with(WATERLOGGED, wet);
        return floor.isValidPosition(context.getWorld(), context.getPos()) ? floor : null;
    }
    @Override public BlockState updatePostPlacement(BlockState state, Direction direction, BlockState neighbour,
            IWorld world, BlockPos pos, BlockPos neighbourPos) {
        if (!state.isValidPosition(world, pos)) return state.getFluidState().getBlockState();
        if (state.get(WATERLOGGED)) world.getPendingFluidTicks().scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        return state;
    }
    @Override public boolean receiveFluid(IWorld world, BlockPos pos, BlockState state, IFluidState fluid) {
        if (state.get(WATERLOGGED) || fluid.getFluid() != Fluids.WATER) return false;
        if (!world.isRemote()) {
            world.setBlockState(pos, LightInteractions.replacement(state, PhaseFourLighting.candle(wall, false))
                    .with(WATERLOGGED, true), 3);
            world.getPendingFluidTicks().scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }
        return true;
    }
    @Override public boolean onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player,
            Hand hand, BlockRayTraceResult hit) {
        if (!lit && !state.get(WATERLOGGED) && (player.getHeldItem(hand).getItem() == Items.FLINT_AND_STEEL
                || player.getHeldItem(hand).getItem() == Items.TORCH)) {
            if (!world.isRemote) {
                LightInteractions.replace(world, pos, state, PhaseFourLighting.candle(wall, true));
                if (!player.isCreative() && player.getHeldItem(hand).getItem() == Items.FLINT_AND_STEEL)
                    player.getHeldItem(hand).damageItem(1, player, p -> p.sendBreakAnimation(hand));
            }
            return true;
        }
        // Water buckets use IWaterLoggable, including their normal consumption and sound.
        return false;
    }
    @Override public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        return Collections.singletonList(new ItemStack(PhaseFourLighting.candle(false, true)));
    }
    @Override public ItemStack getPickBlock(BlockState state, RayTraceResult hit, IBlockReader world, BlockPos pos, PlayerEntity player) {
        return new ItemStack(PhaseFourLighting.candle(false, true));
    }
    @Override public void animateTick(BlockState state, World world, BlockPos pos, Random random) {
        if (!lit || state.get(WATERLOGGED)) return;
        double[] point = rotatedPoint((wall ? 7.5 : 6.5) / 16, (wall ? 1.5 : 6.5) / 16, state.get(DIRECTION), wall);
        double x = pos.getX() + point[0], y = pos.getY() + (wall ? 14.6 : 6.7) / 16, z = pos.getZ() + point[1];
        world.addParticle(PhaseFourLighting.CANDLE_FLAME, x, y, z, 0, 0, 0);
        if (random.nextInt(3) == 0) world.addParticle(ParticleTypes.SMOKE, x, y + .04, z, 0, 0, 0);
    }
}
