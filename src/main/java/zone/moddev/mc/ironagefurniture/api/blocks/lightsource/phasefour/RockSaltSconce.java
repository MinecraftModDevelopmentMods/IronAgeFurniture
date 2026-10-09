package zone.moddev.mc.ironagefurniture.api.blocks.lightsource.phasefour;

import java.util.Arrays;
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
        super(Block.Properties.of(Material.METAL).strength(1, 10).sound(SoundType.METAL).lightLevel(15));
        this.wall = wall;
        setRegistryName(name);
    }
    private Block empty() { return wall ? BlockObjectHolder.light_metal_ironage_sconce_wall_empty_iron
            : BlockObjectHolder.light_metal_ironage_sconce_floor_empty_iron; }
    @Override protected void generateShapes(ImmutableList<BlockState> states) { }
    @Override public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
        VoxelShape shape = wall ? Block.box(0, 5, 5, 7, 16, 11) : Block.box(5, 0, 5, 11, 16, 11);
        // Rock-salt wall models, like twin-torch walls, are authored east-facing.
        return Candle.rotated(shape, wall ? state.getValue(DIRECTION).getClockWise() : state.getValue(DIRECTION), wall);
    }
    @Override public VoxelShape getOcclusionShape(BlockState state, IBlockReader world, BlockPos pos) {
        return getShape(state, world, pos, ISelectionContext.empty());
    }
    @Override public boolean canSurvive(BlockState state, IWorldReader world, BlockPos pos) {
        Direction face = wall ? state.getValue(DIRECTION) : Direction.UP;
        return Block.canSupportCenter(world, pos.relative(face.getOpposite()), face);
    }
    @Override public BlockState getStateForPlacement(BlockItemUseContext context) {
        boolean wet = context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER;
        if (context.getClickedFace().getAxis().isHorizontal()) {
            BlockState placed = PhaseFourLighting.rockSalt(true).defaultBlockState().setValue(DIRECTION, context.getClickedFace()).setValue(WATERLOGGED, wet);
            if (placed.canSurvive(context.getLevel(), context.getClickedPos())) return placed;
        }
        BlockState floor = PhaseFourLighting.rockSalt(false).defaultBlockState().setValue(DIRECTION, context.getHorizontalDirection()).setValue(WATERLOGGED, wet);
        return floor.canSurvive(context.getLevel(), context.getClickedPos()) ? floor : null;
    }
    @Override public BlockState updateShape(BlockState state, Direction direction, BlockState neighbour,
            IWorld world, BlockPos pos, BlockPos neighbourPos) {
        // Air requests normal destruction and drops; Minecraft restores any water afterwards.
        if (!state.canSurvive(world, pos)) return Blocks.AIR.defaultBlockState();
        if (state.getValue(WATERLOGGED)) world.getLiquidTicks().scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
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
    @Override protected net.minecraft.util.ActionResultType ActivateSconce(BlockState state, World world, BlockPos pos, PlayerEntity player,
            Hand hand, BlockRayTraceResult hit) {
        ItemStack held = player.getItemInHand(hand);
        if (held.isEmpty() || MineralogyCompat.isRockSaltLamp(held)) {
            if (!world.isClientSide) {
                LightInteractions.replace(world, pos, state, empty());
                LightInteractions.give(player, hand, MineralogyCompat.getRockSaltLamp().asItem(), 1);
            }
            return net.minecraft.util.ActionResultType.SUCCESS;
        }
        // Other light items must not accidentally place themselves beside a filled sconce.
        return PhaseFourLighting.isLightItem(held) ? net.minecraft.util.ActionResultType.SUCCESS : net.minecraft.util.ActionResultType.PASS;
    }
    @Override public void animateTick(BlockState state, World world, BlockPos pos, Random random) {
        Direction facing = wall ? state.getValue(DIRECTION).getClockWise() : state.getValue(DIRECTION);
        double[] point = Candle.rotatedPoint(wall ? .23 : .5, .5, facing, wall);
        world.addParticle(ParticleTypes.SMOKE, pos.getX() + point[0], pos.getY() + (wall ? .92 : .72), pos.getZ() + point[1], 0, 0, 0);
    }
}
