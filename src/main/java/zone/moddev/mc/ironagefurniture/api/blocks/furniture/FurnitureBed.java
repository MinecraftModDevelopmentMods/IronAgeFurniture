package zone.moddev.mc.ironagefurniture.api.blocks.furniture;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import com.google.common.collect.ImmutableList;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootContext;
import zone.moddev.mc.ironagefurniture.api.UpholsteryItemData;
import zone.moddev.mc.ironagefurniture.api.blocks.base.FurnitureBlock;
import zone.moddev.mc.ironagefurniture.api.enumerations.UpholsteryColour;
import zone.moddev.mc.ironagefurniture.api.enumerations.WoodBedSide;

/** A bed is one object even when the player clicks, burns or breaks a decorative part. */
public abstract class FurnitureBed extends FurnitureBlock {
    public static final EnumProperty<WoodBedSide> SIDE = EnumProperty.create("side", WoodBedSide.class);
    private static final ThreadLocal<Boolean> REMOVING = ThreadLocal.withInitial(() -> false);
    protected final boolean doubleBed;

    protected FurnitureBed(String name, boolean doubleBed) {
        super(Block.Properties.of(Material.WOOD).strength(1, 10).sound(SoundType.WOOD));
        this.doubleBed = doubleBed;
        setRegistryName(name);
        registerDefaultState(defaultBlockState().setValue(DIRECTION, Direction.SOUTH).setValue(SIDE, WoodBedSide.LEFT)
                .setValue(UpholsteryItemData.COLOUR, UpholsteryColour.RED).setValue(WATERLOGGED, false));
    }
    @Override protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(SIDE, UpholsteryItemData.COLOUR);
    }
    @Override protected void generateShapes(ImmutableList<BlockState> states) { }
    protected abstract boolean head(BlockState state);
    protected abstract boolean upper(BlockState state);
    protected abstract BlockState partState(BlockState state, boolean head, boolean upper);
    protected abstract int height();
    protected FurnitureBed blockForSide(WoodBedSide side) { return this; }
    public FurnitureBed itemBlock() { return this; }
    public boolean isDoubleBed() { return doubleBed; }

    public BlockPos basePos(BlockPos pos, BlockState state) {
        if (upper(state)) pos = pos.below();
        if (head(state)) pos = pos.relative(state.getValue(DIRECTION));
        if (doubleBed && state.getValue(SIDE) == WoodBedSide.RIGHT) pos = pos.relative(state.getValue(DIRECTION).getClockWise());
        return pos;
    }
    public Map<BlockPos, BlockState> structure(BlockPos base, BlockState state) {
        Map<BlockPos, BlockState> parts = new LinkedHashMap<>();
        for (WoodBedSide side : WoodBedSide.values()) {
            if (!doubleBed && side == WoodBedSide.RIGHT) continue;
            FurnitureBed block = blockForSide(side);
            for (boolean head : new boolean[]{false, true}) for (int level = 0; level < height(); level++) {
                BlockPos part = base.above(level);
                if (head) part = part.relative(state.getValue(DIRECTION).getOpposite());
                if (side == WoodBedSide.RIGHT) part = part.relative(state.getValue(DIRECTION).getCounterClockWise());
                BlockState placed = block.partState(block.defaultBlockState(), head, level > 0)
                        .setValue(DIRECTION, state.getValue(DIRECTION)).setValue(SIDE, side)
                        .setValue(UpholsteryItemData.COLOUR, state.getValue(UpholsteryItemData.COLOUR));
                parts.put(part, placed);
            }
        }
        return parts;
    }
    @Override public BlockState getStateForPlacement(BlockPlaceContext context) {
        if (itemBlock() != this || context.getClickedFace() != Direction.UP) return null;
        BlockState state = partState(super.getStateForPlacement(context), false, false)
                .setValue(DIRECTION, context.getHorizontalDirection().getOpposite())
                .setValue(UpholsteryItemData.COLOUR, UpholsteryItemData.getColour(context.getItemInHand()));
        for (Map.Entry<BlockPos, BlockState> part : structure(context.getClickedPos(), state).entrySet()) {
            BlockPos pos = part.getKey();
            if (pos.getY() >= context.getLevel().getHeight()
                    || !context.getLevel().getBlockState(pos).canBeReplaced(context)) return null;
            if (!upper(part.getValue()) && !Block.canSupportCenter(context.getLevel(), pos.below(), Direction.UP)) return null;
        }
        return state;
    }
    @Override public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        if (world.isClientSide) return;
        Map<BlockPos, BlockState> parts = structure(pos, state);
        // All cells were checked before BlockItem consumed the item. Suppress
        // neighbour callbacks until every structural part is in place.
        parts.forEach((part, placed) -> {
            if (!part.equals(pos)) world.setBlock(part, placed.setValue(WATERLOGGED,
                    world.getFluidState(part).getType() == Fluids.WATER), 2);
        });
        parts.keySet().forEach(part -> world.updateNeighborsAt(part, this));
    }
    @Override public void onRemove(BlockState state, Level world, BlockPos pos, BlockState replacement, boolean moving) {
        if (!world.isClientSide && replacement.getBlock() != this && !REMOVING.get()) {
            REMOVING.set(true);
            try {
                structure(basePos(pos, state), state).forEach((part, expected) -> {
                    BlockState current = world.getBlockState(part);
                    if (!part.equals(pos) && current.getBlock() == expected.getBlock()
                            && current.getValue(DIRECTION) == expected.getValue(DIRECTION)
                            && current.getValue(SIDE) == expected.getValue(SIDE)
                            && head(current) == head(expected) && upper(current) == upper(expected))
                        world.setBlock(part, current.getFluidState().createLegacyBlock(), 2);
                });
            } finally { REMOVING.remove(); }
        }
        super.onRemove(state, world, pos, replacement, moving);
    }
    @Override public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        return Collections.singletonList(UpholsteryItemData.create(itemBlock(), state.getValue(UpholsteryItemData.COLOUR)));
    }
    @Override public ItemStack getPickBlock(BlockState state, HitResult hit, BlockGetter world,
            BlockPos pos, Player player) {
        return UpholsteryItemData.create(itemBlock(), state.getValue(UpholsteryItemData.COLOUR));
    }
    public BlockPos sleepPos(BlockPos pos, BlockState state) {
        if (upper(state)) pos = pos.below();
        return head(state) ? pos : pos.relative(state.getValue(DIRECTION).getOpposite());
    }
    @Override public net.minecraft.world.InteractionResult use(BlockState state, Level world, BlockPos pos, Player player,
            InteractionHand hand, BlockHitResult hit) {
        if (world.isClientSide) return net.minecraft.world.InteractionResult.SUCCESS;
        BlockPos sleep = sleepPos(pos, state);
        if (!(world.getBlockState(sleep).getBlock() instanceof FurnitureBed)) return net.minecraft.world.InteractionResult.SUCCESS;
        if (!world.dimensionType().bedWorks()) {
            world.removeBlock(pos, false);
            world.explode(null, DamageSource.badRespawnPointExplosion(), null, sleep.getX() + .5, sleep.getY() + .5,
                    sleep.getZ() + .5, 5, true, Explosion.BlockInteraction.DESTROY);
        } else if (world.players().stream().anyMatch(p -> p.isSleeping()
                && p.getSleepingPos().filter(sleep::equals).isPresent())) {
            player.displayClientMessage(new TranslatableComponent("block.minecraft.bed.occupied"), true);
        } else player.startSleepInBed(sleep).ifLeft(result -> {
            if (result != null) player.displayClientMessage(result.getMessage(), true);
        });
        return net.minecraft.world.InteractionResult.SUCCESS;
    }
    @Override public boolean isBed(BlockState state, BlockGetter world, BlockPos pos, Entity player) { return true; }
    public boolean isBedFoot(BlockState state, LevelReader world, BlockPos pos) { return !head(state); }
    @Override public Direction getBedDirection(BlockState state, LevelReader world, BlockPos pos) {
        return state.getValue(DIRECTION).getOpposite();
    }
    @Override public void setBedOccupied(BlockState state, Level world, BlockPos pos, LivingEntity sleeper,
            boolean occupied) { /* Sleeping players, not an extra block property, determine occupancy. */ }
    @Override public Optional<Vec3> getRespawnPosition(BlockState state, EntityType<?> type, LevelReader world,
            BlockPos pos, float orientation, LivingEntity sleeper) {
        BlockPos head = sleepPos(pos, state);
        for (int x = -1; x <= 1; x++) for (int z = -1; z <= 1; z++) {
            BlockPos exit = head.offset(x, 0, z);
            if (!Block.canSupportCenter(world, exit.below(), Direction.UP)) continue;
            float radius = type.getWidth() / 2;
            Vec3 centre = new Vec3(exit.getX() + .5, exit.getY(), exit.getZ() + .5);
            if (world.noCollision(new AABB(centre.x - radius, centre.y, centre.z - radius,
                    centre.x + radius, centre.y + type.getHeight(), centre.z + radius))) return Optional.of(centre);
        }
        return Optional.empty();
    }
    @Override public BlockState updateShape(BlockState state, Direction direction, BlockState neighbour,
            LevelAccessor world, BlockPos pos, BlockPos neighbourPos) {
        if (state.getValue(WATERLOGGED)) world.getLiquidTicks().scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
        return state;
    }
    @Override public boolean isFlammable(BlockState state, BlockGetter world, BlockPos pos, Direction face) { return !FurnitureWoodProperties.fireproof(this); }
    @Override public int getFlammability(BlockState state, BlockGetter world, BlockPos pos, Direction face) { return FurnitureWoodProperties.fireproof(this) ? 0 : 20; }
    @Override public int getFireSpreadSpeed(BlockState state, BlockGetter world, BlockPos pos, Direction face) { return FurnitureWoodProperties.fireproof(this) ? 0 : 5; }
    @Override public PushReaction getPistonPushReaction(BlockState state) { return PushReaction.DESTROY; }
    @Override public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return cachedShape(upper(state) ? 2 : head(state) ? 1 : 0, state.getValue(DIRECTION));
    }
    private VoxelShape[][] bedShapes;
    private synchronized VoxelShape cachedShape(int part, Direction facing) {
        // Reuse the geometry across upholstery colours, wet states and sides.
        if (bedShapes == null) bedShapes = new VoxelShape[3][4];
        int direction = facing.get2DDataValue();
        if (bedShapes[part][direction] == null) bedShapes[part][direction] = UprightFurnitureShapes.rotate(
                part == 2 ? Block.box(0, 13, 0, 16, 16, 16)
                        : Block.box(0, 5, part == 1 ? 1 : 0, 16, 10, part == 1 ? 16 : 15), facing);
        return bedShapes[part][direction];
    }
    @Override public VoxelShape getOcclusionShape(BlockState state, BlockGetter world, BlockPos pos) {
        return getShape(state, world, pos, CollisionContext.empty());
    }
}
