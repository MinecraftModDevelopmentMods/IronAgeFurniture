package zone.moddev.mc.ironagefurniture.api.blocks.furniture;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.attribute.BedRule;
import net.minecraft.world.attribute.EnvironmentAttribute;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.AbstractBedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import zone.moddev.mc.ironagefurniture.api.UpholsteryItemData;
import zone.moddev.mc.ironagefurniture.api.enumerations.UpholsteryColour;
import zone.moddev.mc.ironagefurniture.api.enumerations.WoodBedSide;
import zone.moddev.mc.ironagefurniture.init.RegistrationProperties;

/** One bed owns all its parts, while vanilla owns sleeping and respawning. */
public abstract class FurnitureBed extends AbstractBedBlock implements SimpleWaterloggedBlock {
    public static final EnumProperty<Direction> DIRECTION = FACING;
    public static final EnumProperty<WoodBedSide> SIDE = EnumProperty.create("side", WoodBedSide.class);
    public static final BooleanProperty UPPER = BooleanProperty.create("upper");
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    private static final ThreadLocal<Boolean> REMOVING = ThreadLocal.withInitial(() -> false);
    private static final VoxelShape MATTRESS_SHAPE = Block.box(0, 5, 0, 16, 10, 16);
    private static final VoxelShape CANOPY_SHAPE = Block.box(0, 13, 0, 16, 16, 16);
    protected final boolean doubleBed;

    protected FurnitureBed(String name, boolean doubleBed) {
        super(RegistrationProperties.block(Block.Properties.of().strength(1, 10).sound(SoundType.WOOD)
                .noOcclusion().pushReaction(PushReaction.POPPED), name));
        this.doubleBed = doubleBed;
        BlockState initial = defaultBlockState().setValue(FACING, Direction.NORTH).setValue(SIDE, WoodBedSide.LEFT)
                .setValue(UpholsteryItemData.COLOUR, UpholsteryColour.RED).setValue(WATERLOGGED, false);
        if (initial.hasProperty(UPPER)) initial = initial.setValue(UPPER, false);
        registerDefaultState(initial);
    }

    @Override protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(SIDE, WATERLOGGED, UpholsteryItemData.COLOUR);
        if (this instanceof CanopyBed) builder.add(UPPER);
    }

    protected abstract int height();
    protected FurnitureBed blockForSide(WoodBedSide side) { return this; }
    public FurnitureBed itemBlock() { return this; }
    public boolean isDoubleBed() { return doubleBed; }
    public boolean head(BlockState state) { return state.getValue(PART) == BedPart.HEAD; }
    public boolean upper(BlockState state) { return state.hasProperty(UPPER) && state.getValue(UPPER); }

    public BlockPos basePos(BlockPos pos, BlockState state) {
        if (upper(state)) pos = pos.below();
        if (head(state)) pos = pos.relative(state.getValue(FACING).getOpposite());
        if (doubleBed && state.getValue(SIDE) == WoodBedSide.RIGHT)
            pos = pos.relative(state.getValue(FACING).getCounterClockWise());
        return pos;
    }

    public Map<BlockPos, BlockState> structure(BlockPos base, BlockState state) {
        Map<BlockPos, BlockState> parts = new LinkedHashMap<>();
        for (WoodBedSide side : WoodBedSide.values()) {
            if (!doubleBed && side == WoodBedSide.RIGHT) continue;
            FurnitureBed block = blockForSide(side);
            for (BedPart part : BedPart.values()) for (int level = 0; level < height(); level++) {
                BlockPos pos = base.above(level);
                if (part == BedPart.HEAD) pos = pos.relative(state.getValue(FACING));
                if (side == WoodBedSide.RIGHT) pos = pos.relative(state.getValue(FACING).getClockWise());
                BlockState placed = block.defaultBlockState().setValue(PART, part)
                        .setValue(FACING, state.getValue(FACING)).setValue(SIDE, side)
                        .setValue(UpholsteryItemData.COLOUR, state.getValue(UpholsteryItemData.COLOUR));
                if (placed.hasProperty(UPPER)) placed = placed.setValue(UPPER, level > 0);
                parts.put(pos, placed);
            }
        }
        return parts;
    }

    @Override public BlockState getStateForPlacement(BlockPlaceContext context) {
        if (itemBlock() != this || context.getClickedFace() != Direction.UP) return null;
        BlockState state = defaultBlockState().setValue(FACING, context.getHorizontalDirection())
                .setValue(UpholsteryItemData.COLOUR, UpholsteryItemData.getColour(context.getItemInHand()));
        for (Map.Entry<BlockPos, BlockState> part : structure(context.getClickedPos(), state).entrySet()) {
            BlockPos pos = part.getKey();
            if (context.getLevel().isOutsideBuildHeight(pos) || !context.getLevel().getWorldBorder().isWithinBounds(pos)
                    || !context.getLevel().getBlockState(pos).canBeReplaced(context)) return null;
            if (!upper(part.getValue()) && !Block.canSupportCenter(context.getLevel(), pos.below(), Direction.UP)) return null;
        }
        return state.setValue(WATERLOGGED, context.getLevel().getFluidState(context.getClickedPos()).is(Fluids.WATER));
    }

    @Override public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        if (world.isClientSide()) return;
        Map<BlockPos, BlockState> parts = structure(pos, state);
        parts.forEach((part, placed) -> {
            if (!part.equals(pos)) world.setBlock(part,
                    placed.setValue(WATERLOGGED, world.getFluidState(part).is(Fluids.WATER)), Block.UPDATE_CLIENTS);
        });
        parts.keySet().forEach(part -> world.updateNeighborsAt(part, this));
    }

    @Override protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel world, BlockPos pos, boolean moving) {
        if (!REMOVING.get()) {
            REMOVING.set(true);
            try {
                structure(basePos(pos, state), state).forEach((part, expected) -> {
                    BlockState actual = world.getBlockState(part);
                    if (!part.equals(pos) && actual.getBlock() == expected.getBlock()
                            && actual.getValue(FACING) == expected.getValue(FACING)
                            && actual.getValue(PART) == expected.getValue(PART)
                            && upper(actual) == upper(expected)
                            && actual.getValue(SIDE) == expected.getValue(SIDE))
                        world.setBlock(part, actual.getFluidState().createLegacyBlock(), Block.UPDATE_CLIENTS);
                });
            } finally { REMOVING.remove(); }
        }
        super.affectNeighborsAfterRemoval(state, world, pos, moving);
    }

    @Override public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        return Collections.singletonList(UpholsteryItemData.create(itemBlock(), state.getValue(UpholsteryItemData.COLOUR)));
    }

    @Override public ItemStack getCloneItemStack(LevelReader world, BlockPos pos, BlockState state, boolean includeData, Player player) {
        return UpholsteryItemData.create(itemBlock(), state.getValue(UpholsteryItemData.COLOUR));
    }

    public BlockPos sleepPos(BlockPos pos, BlockState state) {
        if (upper(state)) pos = pos.below();
        return head(state) ? pos : pos.relative(state.getValue(FACING));
    }

    @Override protected InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
        if (world.isClientSide()) return InteractionResult.SUCCESS;
        BlockPos sleep = sleepPos(pos, state);
        BlockState lower = world.getBlockState(sleep);
        if (!(lower.getBlock() instanceof FurnitureBed bed)) return InteractionResult.CONSUME;
        BedRule rule = bed.getBedRule(world, sleep);
        if (rule.destroyOnUse()) return destroyOnUse(lower, world, sleep, player);
        if (lower.getValue(OCCUPIED)) player.sendOverlayMessage(Component.translatable("block.minecraft.bed.occupied"));
        else player.startSleepInBed(bed, lower, rule, sleep).ifLeft(problem -> {
            if (problem.message() != null) player.sendOverlayMessage(problem.message());
        });
        return InteractionResult.SUCCESS_SERVER;
    }

    @Override protected EnvironmentAttribute<BedRule> getBedEnvironmentAttribute() { return EnvironmentAttributes.BED_RULE; }
    @Override protected InteractionResult destroyOnUse(BlockState state, Level world, BlockPos pos, Player player) {
        world.removeBlock(pos, false);
        Vec3 centre = Vec3.atCenterOf(pos);
        world.explode(null, world.damageSources().badRespawnPointExplosion(centre), null,
                centre, 5, true, Level.ExplosionInteraction.BLOCK);
        return InteractionResult.SUCCESS_SERVER;
    }
    @Override protected void destroyOnLeave(Level world, BlockPos pos) { world.removeBlock(pos, false); }
    @Override public OptionalDouble getSleepHeight(BlockState state, Level world, BlockPos pos) {
        return upper(state) ? OptionalDouble.empty() : OptionalDouble.of(10D / 16D);
    }

    @Override public BlockState playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
        // The complete IAF structure is removed by affectNeighborsAfterRemoval.
        return state;
    }

    @Override protected BlockState updateShape(BlockState state, LevelReader world, ScheduledTickAccess ticks,
            BlockPos pos, Direction direction, BlockPos neighbourPos, BlockState neighbour, RandomSource random) {
        if (state.getValue(WATERLOGGED)) ticks.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
        return state;
    }
    @Override public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }
    @Override public boolean isFlammable(BlockState state, BlockGetter world, BlockPos pos, Direction face) { return !FurnitureWoodProperties.fireproof(this); }
    @Override public int getFlammability(BlockState state, BlockGetter world, BlockPos pos, Direction face) { return FurnitureWoodProperties.fireproof(this) ? 0 : 20; }
    @Override public int getFireSpreadSpeed(BlockState state, BlockGetter world, BlockPos pos, Direction face) { return FurnitureWoodProperties.fireproof(this) ? 0 : 5; }
    @Override public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return upper(state) ? CANOPY_SHAPE : MATTRESS_SHAPE;
    }
    @Override protected VoxelShape getOcclusionShape(BlockState state) { return net.minecraft.world.phys.shapes.Shapes.empty(); }
}
