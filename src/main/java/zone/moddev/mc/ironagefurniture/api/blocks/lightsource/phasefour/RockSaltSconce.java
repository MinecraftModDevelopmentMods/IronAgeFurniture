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
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
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
import zone.moddev.mc.ironagefurniture.BlockObjectHolder;
import zone.moddev.mc.ironagefurniture.api.MineralogyCompat;
import zone.moddev.mc.ironagefurniture.api.blocks.lightholder.LightHolderSconceFloor;
import zone.moddev.mc.ironagefurniture.init.PhaseFourLighting;

/** Mineralogy's removable rock-salt lamp in a waterloggable IAF frame. */
public final class RockSaltSconce extends LightHolderSconceFloor {
    private final boolean wall;
    public RockSaltSconce(String name, boolean wall) {
        super(Block.Properties.create(Material.IRON).hardnessAndResistance(1, 10).sound(SoundType.METAL).lightValue(15));
        this.wall = wall;
        setRegistryName(name);
    }
    private Block empty() { return wall ? BlockObjectHolder.light_metal_ironage_sconce_wall_empty_iron
            : BlockObjectHolder.light_metal_ironage_sconce_floor_empty_iron; }
    @Override protected void generateShapes(ImmutableList<BlockState> states) { }
    @Override public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
        VoxelShape shape = wall ? Block.makeCuboidShape(0, 5, 5, 7, 16, 11) : Block.makeCuboidShape(5, 0, 5, 11, 16, 11);
        // Rock-salt wall models, like twin-torch walls, are authored east-facing.
        return Candle.rotated(shape, wall ? state.get(DIRECTION).rotateY() : state.get(DIRECTION), wall);
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
            BlockState placed = PhaseFourLighting.rockSalt(true).getDefaultState().with(DIRECTION, context.getFace()).with(WATERLOGGED, wet);
            if (placed.isValidPosition(context.getWorld(), context.getPos())) return placed;
        }
        BlockState floor = PhaseFourLighting.rockSalt(false).getDefaultState().with(DIRECTION, context.getPlacementHorizontalFacing()).with(WATERLOGGED, wet);
        return floor.isValidPosition(context.getWorld(), context.getPos()) ? floor : null;
    }
    @Override public BlockState updatePostPlacement(BlockState state, Direction direction, BlockState neighbour,
            IWorld world, BlockPos pos, BlockPos neighbourPos) {
        if (!state.isValidPosition(world, pos)) return state.getFluidState().getBlockState();
        if (state.get(WATERLOGGED)) world.getPendingFluidTicks().scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        return state;
    }
    @Override public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        ItemStack frame = zone.moddev.mc.ironagefurniture.api.SconceMetalData.create(BlockObjectHolder.light_metal_ironage_sconce_floor_empty_iron, zone.moddev.mc.ironagefurniture.api.SconceMetalData.get(state));
        Block lamp = MineralogyCompat.getRockSaltLamp();
        return lamp == null ? java.util.Collections.singletonList(frame) : Arrays.asList(frame, new ItemStack(lamp));
    }
    @Override public ItemStack getPickBlock(BlockState state, RayTraceResult hit, IBlockReader world, BlockPos pos, PlayerEntity player) {
        return zone.moddev.mc.ironagefurniture.api.SconceMetalData.create(BlockObjectHolder.light_metal_ironage_sconce_floor_empty_iron, zone.moddev.mc.ironagefurniture.api.SconceMetalData.get(state));
    }
    @Override protected boolean ActivateSconce(BlockState state, World world, BlockPos pos, PlayerEntity player,
            Hand hand, BlockRayTraceResult hit) {
        ItemStack held = player.getHeldItem(hand);
        if (held.isEmpty() || MineralogyCompat.isRockSaltLamp(held)) {
            if (!world.isRemote) {
                LightInteractions.replace(world, pos, state, empty());
                LightInteractions.give(player, hand, MineralogyCompat.getRockSaltLamp().asItem(), 1);
            }
            return true;
        }
        // Other light items must not accidentally place themselves beside a filled sconce.
        return PhaseFourLighting.isLightItem(held);
    }
    @Override public void animateTick(BlockState state, World world, BlockPos pos, Random random) {
        Direction facing = wall ? state.get(DIRECTION).rotateY() : state.get(DIRECTION);
        double[] point = Candle.rotatedPoint(wall ? .23 : .5, .5, facing, wall);
        world.addParticle(ParticleTypes.SMOKE, pos.getX() + point[0], pos.getY() + (wall ? .92 : .72), pos.getZ() + point[1], 0, 0, 0);
    }
}
