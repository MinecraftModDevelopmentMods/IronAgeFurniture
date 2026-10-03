package zone.moddev.mc.ironagefurniture.api.blocks.lightsource.phasefour;

import java.util.Arrays;
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
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootContext;
import zone.moddev.mc.ironagefurniture.BlockObjectHolder;
import zone.moddev.mc.ironagefurniture.api.blocks.lightholder.LightHolderSconceFloor;
import zone.moddev.mc.ironagefurniture.init.PhaseFourLighting;

/** One-to-four candles or twin torches, with the usual removable sconce contents. */
public final class AdditionalSconce extends LightHolderSconceFloor {
    private final boolean wall, lit, twinTorch;
    private final int count;
    public AdditionalSconce(String name, boolean wall, boolean lit, boolean twinTorch, int count) {
        super(Block.Properties.create(Material.IRON).hardnessAndResistance(1, 10).sound(SoundType.METAL)
                .lightValue(lit ? twinTorch ? 15 : 11 + count : 0));
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
    @Override public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
        VoxelShape shape = !wall ? Block.makeCuboidShape(5, 0, 5, 11, 16, 11)
                : twinTorch ? Block.makeCuboidShape(0, 2, 5, 7, 16, 11) : Block.makeCuboidShape(5, 2, 0, 11, 16, 7);
        Direction facing = state.get(DIRECTION);
        // The original wall twin-torch model is authored east-facing.
        if (wall && twinTorch) facing = facing.rotateY();
        return Candle.rotated(shape, facing, wall);
    }
    @Override public VoxelShape getRenderShape(BlockState state, IBlockReader world, BlockPos pos) {
        return getShape(state, world, pos, ISelectionContext.dummy());
    }
    @Override public boolean isValidPosition(BlockState state, IWorldReader world, BlockPos pos) {
        Direction face = wall ? state.get(DIRECTION) : Direction.UP;
        return Block.func_220055_a(world, pos.offset(face.getOpposite()), face);
    }
    @Override public BlockState getStateForPlacement(BlockItemUseContext context) {
        boolean wet = context.getWorld().getFluidState(context.getPos()).getFluid() == Fluids.WATER;
        if (context.getFace().getAxis().isHorizontal()) {
            BlockState onWall = variant(count, lit && !wet, true).getDefaultState()
                    .with(DIRECTION, context.getFace()).with(WATERLOGGED, wet);
            if (onWall.isValidPosition(context.getWorld(), context.getPos())) return onWall;
        }
        BlockState placed = variant(count, lit && !wet, false).getDefaultState()
                .with(DIRECTION, context.getPlacementHorizontalFacing()).with(WATERLOGGED, wet);
        return placed.isValidPosition(context.getWorld(), context.getPos()) ? placed : null;
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
            world.setBlockState(pos, LightInteractions.replacement(state, variant(count, false, wall)).with(WATERLOGGED, true), 3);
            world.getPendingFluidTicks().scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }
        return true;
    }
    @Override public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        return Arrays.asList(new ItemStack(BlockObjectHolder.light_metal_ironage_sconce_floor_empty_iron), new ItemStack(contents(), count));
    }
    @Override public ItemStack getPickBlock(BlockState state, RayTraceResult hit, IBlockReader world, BlockPos pos, PlayerEntity player) {
        return new ItemStack(BlockObjectHolder.light_metal_ironage_sconce_floor_empty_iron);
    }
    @Override protected boolean ActivateSconce(BlockState state, World world, BlockPos pos, PlayerEntity player,
            Hand hand, BlockRayTraceResult hit) {
        ItemStack held = player.getHeldItem(hand);
        // Lighting an extinguished sconce does not consume another candle or torch.
        // Do this before the insertion/removal branches, as in the legacy release.
        if (!lit && !state.get(WATERLOGGED) && (held.getItem() == Items.FLINT_AND_STEEL
                || held.getItem() == Items.TORCH || !twinTorch && held.getItem() == contents())) {
            if (!world.isRemote) {
                LightInteractions.replace(world, pos, state, variant(count, true, wall));
                if (!player.isCreative() && held.getItem() == Items.FLINT_AND_STEEL)
                    held.damageItem(1, player, p -> p.sendBreakAnimation(hand));
            }
            return true;
        }
        if (held.isEmpty() || held.getItem() == contents()) {
            if (!world.isRemote) {
                if (!twinTorch && held.getItem() == contents() && count < 4) {
                    LightInteractions.replace(world, pos, state, variant(count + 1, !state.get(WATERLOGGED), wall));
                    if (!player.isCreative()) held.shrink(1);
                } else {
                    LightInteractions.replace(world, pos, state, empty());
                    LightInteractions.give(player, hand, contents(), count);
                }
            }
            return true;
        }
        return PhaseFourLighting.isLightItem(held);
    }
    @Override public void neighborChanged(BlockState state, World world, BlockPos pos, Block changed, BlockPos from, boolean moving) {
        if (!world.isRemote && !lit && !state.get(WATERLOGGED) && world.isBlockPowered(pos))
            world.getPendingBlockTicks().scheduleTick(pos, this, 2);
    }
    @Override public void tick(BlockState state, World world, BlockPos pos, Random random) {
        if (!world.isRemote && !lit && !state.get(WATERLOGGED) && world.isBlockPowered(pos))
            LightInteractions.replace(world, pos, state, variant(count, true, wall));
    }
    @Override public boolean canConnectRedstone(BlockState state, IBlockReader world, BlockPos pos, Direction side) { return true; }
    @Override public void animateTick(BlockState state, World world, BlockPos pos, Random random) {
        if (!lit || state.get(WATERLOGGED)) return;
        for (int index = 0; index < count; index++) {
            Vec3d flame = getFlameOffset(state, index);
            double x = pos.getX() + flame.x, y = pos.getY() + flame.y, z = pos.getZ() + flame.z;
            world.addParticle(twinTorch ? ParticleTypes.FLAME : PhaseFourLighting.CANDLE_FLAME, x, y, z, 0, 0, 0);
            if (twinTorch || random.nextInt(3) == 0) world.addParticle(ParticleTypes.SMOKE, x, y + .04, z, 0, 0, 0);
        }
    }
    public Vec3d getFlameOffset(BlockState state, int index) {
        double[] flame = flamePoint(index);
        Direction facing = state.get(DIRECTION);
        if (wall && twinTorch) facing = facing.rotateY();
        double[] point = Candle.rotatedPoint(flame[0] / 16, flame[2] / 16, facing, wall);
        return new Vec3d(point[0], flame[1] / 16, point[1]);
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
