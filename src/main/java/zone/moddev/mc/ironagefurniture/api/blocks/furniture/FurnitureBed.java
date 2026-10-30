package zone.moddev.mc.ironagefurniture.api.blocks.furniture;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import zone.moddev.mc.ironagefurniture.api.UpholsteryItemData;
import zone.moddev.mc.ironagefurniture.api.enumerations.UpholsteryColour;
import zone.moddev.mc.ironagefurniture.api.enumerations.WoodBedSide;

/** One bed owns all its parts, while vanilla owns sleeping and respawning. */
public abstract class FurnitureBed extends BedBlock implements SimpleWaterloggedBlock {
    public static final EnumProperty<Direction> DIRECTION = FACING;
    public static final EnumProperty<WoodBedSide> SIDE = EnumProperty.create("side", WoodBedSide.class);
    public static final BooleanProperty UPPER = BooleanProperty.create("upper");
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    private static final ThreadLocal<Boolean> REMOVING = ThreadLocal.withInitial(() -> false);
    private static final VoxelShape MATTRESS_SHAPE = Block.box(0, 5, 0, 16, 10, 16);
    private static final VoxelShape CANOPY_SHAPE = Block.box(0, 13, 0, 16, 16, 16);
    protected final boolean doubleBed;

    protected FurnitureBed(String name, boolean doubleBed) {
        super(DyeColor.RED, Block.Properties.of(FurnitureWoodProperties.isFireproof(name) ? net.minecraft.world.level.material.Material.NETHER_WOOD : net.minecraft.world.level.material.Material.WOOD).strength(1, 10).sound(SoundType.WOOD)
                .noOcclusion());
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
    @Override public PushReaction getPistonPushReaction(BlockState state) { return PushReaction.DESTROY; }
    protected abstract int height();
    protected FurnitureBed blockForSide(WoodBedSide side) { return this; }
    public FurnitureBed itemBlock() { return this; }
    public boolean isDoubleBed() { return doubleBed; }
    public boolean head(BlockState state) { return state.getValue(PART) == BedPart.HEAD; }
    public boolean upper(BlockState state) { return state.hasProperty(UPPER) && state.getValue(UPPER); }

    @Override public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        // Colour and geometry live in persistent states and baked models, not a vanilla bed entity.
        return null;
    }
    @Override public RenderShape getRenderShape(BlockState state) {
        // 1.19.4 BedBlock defaults to an animated entity renderer, unlike our baked furniture models.
        return RenderShape.MODEL;
    }
    @Override public boolean isBed(BlockState state, BlockGetter world, BlockPos pos, net.minecraft.world.entity.Entity sleeper) {
        return !upper(state);
    }
    @Override public Direction getBedDirection(BlockState state, LevelReader world, BlockPos pos) {
        return state.getValue(FACING);
    }
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
        if (world.isClientSide) return;
        Map<BlockPos, BlockState> parts = structure(pos, state);
        parts.forEach((part, placed) -> {
            if (!part.equals(pos)) world.setBlock(part,
                    placed.setValue(WATERLOGGED, world.getFluidState(part).is(Fluids.WATER)), Block.UPDATE_CLIENTS);
        });
        parts.keySet().forEach(part -> world.updateNeighborsAt(part, this));
    }
    @Override public void onRemove(BlockState state, Level world, BlockPos pos, BlockState replacement, boolean moving) {
        if (state.getBlock() != replacement.getBlock() && !world.isClientSide && !REMOVING.get()) {
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
        super.onRemove(state, world, pos, replacement, moving);
    }
    @Override public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        return Collections.singletonList(UpholsteryItemData.create(itemBlock(), state.getValue(UpholsteryItemData.COLOUR)));
    }
    @Override public ItemStack getCloneItemStack(net.minecraft.world.level.BlockGetter world, BlockPos pos, BlockState state) {
        return UpholsteryItemData.create(itemBlock(), state.getValue(UpholsteryItemData.COLOUR));
    }
    @Override public ItemStack getCloneItemStack(BlockState state, HitResult hit, net.minecraft.world.level.BlockGetter world, BlockPos pos, Player player) {
        return getCloneItemStack(world, pos, state);
    }
    public BlockPos sleepPos(BlockPos pos, BlockState state) {
        if (upper(state)) pos = pos.below();
        return head(state) ? pos : pos.relative(state.getValue(FACING));
    }
    @Override public InteractionResult use(BlockState state, Level world, BlockPos pos,
            Player player, net.minecraft.world.InteractionHand hand, BlockHitResult hit) {
        if (world.isClientSide) return InteractionResult.SUCCESS;
        BlockPos sleep = sleepPos(pos, state);
        BlockState lower = world.getBlockState(sleep);
        if (!(lower.getBlock() instanceof FurnitureBed)) return InteractionResult.CONSUME;
        if (!BedBlock.canSetSpawn(world)) return destroyOnUse(lower, world, sleep, player);
        if (lower.getValue(OCCUPIED)) player.displayClientMessage(Component.translatable("block.minecraft.bed.occupied"), true);
        else player.startSleepInBed(sleep).ifLeft(problem -> {
            if (problem.getMessage() != null) player.displayClientMessage(problem.getMessage(), true);
        });
        return InteractionResult.SUCCESS;
    }
    protected InteractionResult destroyOnUse(BlockState state, Level world, BlockPos pos, Player player) {
        world.removeBlock(pos, false);
        Vec3 centre = Vec3.atCenterOf(pos);
        world.explode(null, world.damageSources().badRespawnPointExplosion(centre), null,
                centre, 5, true, Level.ExplosionInteraction.BLOCK);
        return InteractionResult.SUCCESS;
    }
    // The narrowly scoped LivingEntity sleep-position bridge uses the real mattress height.
    public OptionalDouble getSleepHeight(BlockState state, Level world, BlockPos pos) {
        return upper(state) ? OptionalDouble.empty() : OptionalDouble.of(10D / 16D);
    }
    @Override public void playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
        // onRemove owns the entire IAF structure, not vanilla's separate two-block creative path.
        return;
    }
    @Override public BlockState updateShape(BlockState state, Direction direction, BlockState neighbour,
            LevelAccessor world, BlockPos pos, BlockPos neighbourPos) {
        if (state.getValue(WATERLOGGED)) world.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
        return state;
    }
    @Override public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }
    @Override public boolean isFlammable(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
        return !FurnitureWoodProperties.fireproof(this);
    }
    @Override public int getFlammability(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
        return FurnitureWoodProperties.fireproof(this) ? 0 : 20;
    }
    @Override public int getFireSpreadSpeed(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
        return FurnitureWoodProperties.fireproof(this) ? 0 : 5;
    }
    @Override public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return upper(state) ? CANOPY_SHAPE : MATTRESS_SHAPE;
    }
    @Override public VoxelShape getOcclusionShape(BlockState state, BlockGetter world, BlockPos pos) { return Shapes.empty(); }
}
