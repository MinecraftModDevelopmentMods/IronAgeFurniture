package zone.moddev.mc.ironagefurniture.api.blocks.furniture;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import com.google.common.collect.ImmutableList;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraftforge.common.extensions.IForgeDimension;
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
    @Override protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
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
    @Override public BlockState getStateForPlacement(BlockItemUseContext context) {
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
    @Override public void setPlacedBy(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
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
    @Override public void onRemove(BlockState state, World world, BlockPos pos, BlockState replacement, boolean moving) {
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
    @Override public ItemStack getPickBlock(BlockState state, RayTraceResult hit, IBlockReader world,
            BlockPos pos, PlayerEntity player) {
        return UpholsteryItemData.create(itemBlock(), state.getValue(UpholsteryItemData.COLOUR));
    }
    public BlockPos sleepPos(BlockPos pos, BlockState state) {
        if (upper(state)) pos = pos.below();
        return head(state) ? pos : pos.relative(state.getValue(DIRECTION).getOpposite());
    }
    @Override public net.minecraft.util.ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player,
            Hand hand, BlockRayTraceResult hit) {
        if (world.isClientSide) return net.minecraft.util.ActionResultType.SUCCESS;
        BlockPos sleep = sleepPos(pos, state);
        if (!(world.getBlockState(sleep).getBlock() instanceof FurnitureBed)) return net.minecraft.util.ActionResultType.SUCCESS;
        IForgeDimension.SleepResult permission = world.dimension.canSleepAt(player, sleep);
        if (permission == IForgeDimension.SleepResult.DENY) return net.minecraft.util.ActionResultType.SUCCESS;
        if (permission == IForgeDimension.SleepResult.BED_EXPLODES) {
            world.removeBlock(pos, false);
            world.explode(null, DamageSource.netherBedExplosion(), sleep.getX() + .5, sleep.getY() + .5,
                    sleep.getZ() + .5, 5, true, Explosion.Mode.DESTROY);
        } else if (world.players().stream().anyMatch(p -> p.isSleeping()
                && p.getSleepingPos().filter(sleep::equals).isPresent())) {
            player.displayClientMessage(new TranslationTextComponent("block.minecraft.bed.occupied"), true);
        } else player.startSleepInBed(sleep).ifLeft(result -> {
            if (result != null) player.displayClientMessage(result.getMessage(), true);
        });
        return net.minecraft.util.ActionResultType.SUCCESS;
    }
    @Override public boolean isBed(BlockState state, IBlockReader world, BlockPos pos, Entity player) { return true; }
    @Override public boolean isBedFoot(BlockState state, IWorldReader world, BlockPos pos) { return !head(state); }
    @Override public Direction getBedDirection(BlockState state, IWorldReader world, BlockPos pos) {
        return state.getValue(DIRECTION).getOpposite();
    }
    @Override public void setBedOccupied(BlockState state, IWorldReader world, BlockPos pos, LivingEntity sleeper,
            boolean occupied) { /* Sleeping players, not an extra block property, determine occupancy. */ }
    @Override public Optional<Vec3d> getBedSpawnPosition(EntityType<?> type, BlockState state, IWorldReader world,
            BlockPos pos, LivingEntity sleeper) {
        BlockPos head = sleepPos(pos, state);
        for (int x = -1; x <= 1; x++) for (int z = -1; z <= 1; z++) {
            BlockPos exit = head.offset(x, 0, z);
            if (!Block.canSupportCenter(world, exit.below(), Direction.UP)) continue;
            float radius = type.getWidth() / 2;
            Vec3d centre = new Vec3d(exit.getX() + .5, exit.getY(), exit.getZ() + .5);
            if (world.noCollision(new AxisAlignedBB(centre.x - radius, centre.y, centre.z - radius,
                    centre.x + radius, centre.y + type.getHeight(), centre.z + radius))) return Optional.of(centre);
        }
        return Optional.empty();
    }
    @Override public BlockState updateShape(BlockState state, Direction direction, BlockState neighbour,
            IWorld world, BlockPos pos, BlockPos neighbourPos) {
        if (state.getValue(WATERLOGGED)) world.getLiquidTicks().scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
        return state;
    }
    @Override public boolean isFlammable(BlockState state, IBlockReader world, BlockPos pos, Direction face) { return true; }
    @Override public int getFlammability(BlockState state, IBlockReader world, BlockPos pos, Direction face) { return 20; }
    @Override public int getFireSpreadSpeed(BlockState state, IBlockReader world, BlockPos pos, Direction face) { return 5; }
    @Override public PushReaction getPistonPushReaction(BlockState state) { return PushReaction.DESTROY; }
    @Override public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
        return UprightFurnitureShapes.rotate(upper(state) ? Block.box(0, 13, 0, 16, 16, 16)
                : Block.box(0, 5, head(state) ? 1 : 0, 16, 10, head(state) ? 16 : 15), state.getValue(DIRECTION));
    }
    @Override public VoxelShape getOcclusionShape(BlockState state, IBlockReader world, BlockPos pos) {
        return getShape(state, world, pos, ISelectionContext.empty());
    }
}
