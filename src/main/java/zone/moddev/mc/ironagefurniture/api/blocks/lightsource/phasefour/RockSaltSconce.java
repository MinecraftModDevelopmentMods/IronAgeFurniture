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
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.ItemStack;
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
import zone.moddev.mc.ironagefurniture.BlockObjectHolder;
import zone.moddev.mc.ironagefurniture.api.MineralogyCompat;
import zone.moddev.mc.ironagefurniture.api.blocks.lightholder.LightHolderSconceFloor;
import zone.moddev.mc.ironagefurniture.init.PhaseFourLighting;

/** Mineralogy's removable rock-salt lamp in a waterloggable IAF frame. */
public final class RockSaltSconce extends LightHolderSconceFloor {
    private final boolean wall;
    public RockSaltSconce(String name, boolean wall) {
        super(Block.Properties.of(Material.METAL).strength(1, 10).sound(SoundType.METAL).lightLevel(state -> 15));
        this.wall = wall;
        setRegistryName(name);
    }
    private Block empty() { return wall ? BlockObjectHolder.light_metal_ironage_sconce_wall_empty_iron
            : BlockObjectHolder.light_metal_ironage_sconce_floor_empty_iron; }
    @Override protected void generateShapes(ImmutableList<BlockState> states) { }
    @Override public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        VoxelShape shape = wall ? Block.box(0, 5, 5, 7, 16, 11) : Block.box(5, 0, 5, 11, 16, 11);
        // Rock-salt wall models, like twin-torch walls, are authored east-facing.
        return Candle.rotated(shape, wall ? state.getValue(DIRECTION).getClockWise() : state.getValue(DIRECTION), wall);
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
            BlockState placed = PhaseFourLighting.rockSalt(true).defaultBlockState().setValue(DIRECTION, context.getClickedFace()).setValue(WATERLOGGED, wet);
            if (placed.canSurvive(context.getLevel(), context.getClickedPos())) return placed;
        }
        BlockState floor = PhaseFourLighting.rockSalt(false).defaultBlockState().setValue(DIRECTION, context.getHorizontalDirection()).setValue(WATERLOGGED, wet);
        return floor.canSurvive(context.getLevel(), context.getClickedPos()) ? floor : null;
    }
    @Override public BlockState updateShape(BlockState state, Direction direction, BlockState neighbour,
            LevelAccessor world, BlockPos pos, BlockPos neighbourPos) {
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
    @Override public ItemStack getPickBlock(BlockState state, HitResult hit, BlockGetter world, BlockPos pos, Player player) {
        return zone.moddev.mc.ironagefurniture.api.SconceMetalData.create(BlockObjectHolder.light_metal_ironage_sconce_floor_empty_iron, zone.moddev.mc.ironagefurniture.api.SconceMetalData.get(state));
    }
    @Override protected net.minecraft.world.InteractionResult ActivateSconce(BlockState state, Level world, BlockPos pos, Player player,
            InteractionHand hand, BlockHitResult hit) {
        ItemStack held = player.getItemInHand(hand);
        if (held.isEmpty() || MineralogyCompat.isRockSaltLamp(held)) {
            if (!world.isClientSide) {
                LightInteractions.replace(world, pos, state, empty());
                LightInteractions.give(player, hand, MineralogyCompat.getRockSaltLamp().asItem(), 1);
            }
            return net.minecraft.world.InteractionResult.SUCCESS;
        }
        // Other light items must not accidentally place themselves beside a filled sconce.
        return PhaseFourLighting.isLightItem(held) ? net.minecraft.world.InteractionResult.SUCCESS : net.minecraft.world.InteractionResult.PASS;
    }
    @Override public void animateTick(BlockState state, Level world, BlockPos pos, Random random) {
        Direction facing = wall ? state.getValue(DIRECTION).getClockWise() : state.getValue(DIRECTION);
        double[] point = Candle.rotatedPoint(wall ? .23 : .5, .5, facing, wall);
        world.addParticle(ParticleTypes.SMOKE, pos.getX() + point[0], pos.getY() + (wall ? .92 : .72), pos.getZ() + point[1], 0, 0, 0);
    }
}
