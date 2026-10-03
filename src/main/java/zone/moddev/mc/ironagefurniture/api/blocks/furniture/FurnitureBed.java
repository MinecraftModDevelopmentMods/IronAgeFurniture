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
        super(Block.Properties.create(Material.WOOD).hardnessAndResistance(1, 10).sound(SoundType.WOOD));
        this.doubleBed = doubleBed;
        setRegistryName(name);
        setDefaultState(getDefaultState().with(DIRECTION, Direction.SOUTH).with(SIDE, WoodBedSide.LEFT)
                .with(UpholsteryItemData.COLOUR, UpholsteryColour.RED).with(WATERLOGGED, false));
    }
    @Override protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
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
        if (upper(state)) pos = pos.down();
        if (head(state)) pos = pos.offset(state.get(DIRECTION));
        if (doubleBed && state.get(SIDE) == WoodBedSide.RIGHT) pos = pos.offset(state.get(DIRECTION).rotateY());
        return pos;
    }
    public Map<BlockPos, BlockState> structure(BlockPos base, BlockState state) {
        Map<BlockPos, BlockState> parts = new LinkedHashMap<>();
        for (WoodBedSide side : WoodBedSide.values()) {
            if (!doubleBed && side == WoodBedSide.RIGHT) continue;
            FurnitureBed block = blockForSide(side);
            for (boolean head : new boolean[]{false, true}) for (int level = 0; level < height(); level++) {
                BlockPos part = base.up(level);
                if (head) part = part.offset(state.get(DIRECTION).getOpposite());
                if (side == WoodBedSide.RIGHT) part = part.offset(state.get(DIRECTION).rotateYCCW());
                BlockState placed = block.partState(block.getDefaultState(), head, level > 0)
                        .with(DIRECTION, state.get(DIRECTION)).with(SIDE, side)
                        .with(UpholsteryItemData.COLOUR, state.get(UpholsteryItemData.COLOUR));
                parts.put(part, placed);
            }
        }
        return parts;
    }
    @Override public BlockState getStateForPlacement(BlockItemUseContext context) {
        if (itemBlock() != this || context.getFace() != Direction.UP) return null;
        BlockState state = partState(super.getStateForPlacement(context), false, false)
                .with(DIRECTION, context.getPlacementHorizontalFacing().getOpposite())
                .with(UpholsteryItemData.COLOUR, UpholsteryItemData.getColour(context.getItem()));
        for (Map.Entry<BlockPos, BlockState> part : structure(context.getPos(), state).entrySet()) {
            BlockPos pos = part.getKey();
            if (pos.getY() >= context.getWorld().getHeight()
                    || !context.getWorld().getBlockState(pos).isReplaceable(context)) return null;
            if (!upper(part.getValue()) && !Block.func_220055_a(context.getWorld(), pos.down(), Direction.UP)) return null;
        }
        return state;
    }
    @Override public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        if (world.isRemote) return;
        Map<BlockPos, BlockState> parts = structure(pos, state);
        // All cells were checked before BlockItem consumed the item. Suppress
        // neighbour callbacks until every structural part is in place.
        parts.forEach((part, placed) -> {
            if (!part.equals(pos)) world.setBlockState(part, placed.with(WATERLOGGED,
                    world.getFluidState(part).getFluid() == Fluids.WATER), 2);
        });
        parts.keySet().forEach(part -> world.notifyNeighbors(part, this));
    }
    @Override public void onReplaced(BlockState state, World world, BlockPos pos, BlockState replacement, boolean moving) {
        if (!world.isRemote && replacement.getBlock() != this && !REMOVING.get()) {
            REMOVING.set(true);
            try {
                structure(basePos(pos, state), state).forEach((part, expected) -> {
                    BlockState current = world.getBlockState(part);
                    if (!part.equals(pos) && current.getBlock() == expected.getBlock()
                            && current.get(DIRECTION) == expected.get(DIRECTION)
                            && current.get(SIDE) == expected.get(SIDE)
                            && head(current) == head(expected) && upper(current) == upper(expected))
                        world.setBlockState(part, current.getFluidState().getBlockState(), 2);
                });
            } finally { REMOVING.remove(); }
        }
        super.onReplaced(state, world, pos, replacement, moving);
    }
    @Override public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        return Collections.singletonList(UpholsteryItemData.create(itemBlock(), state.get(UpholsteryItemData.COLOUR)));
    }
    @Override public ItemStack getPickBlock(BlockState state, RayTraceResult hit, IBlockReader world,
            BlockPos pos, PlayerEntity player) {
        return UpholsteryItemData.create(itemBlock(), state.get(UpholsteryItemData.COLOUR));
    }
    public BlockPos sleepPos(BlockPos pos, BlockState state) {
        if (upper(state)) pos = pos.down();
        return head(state) ? pos : pos.offset(state.get(DIRECTION).getOpposite());
    }
    @Override public boolean onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player,
            Hand hand, BlockRayTraceResult hit) {
        if (world.isRemote) return true;
        BlockPos sleep = sleepPos(pos, state);
        if (!(world.getBlockState(sleep).getBlock() instanceof FurnitureBed)) return true;
        IForgeDimension.SleepResult permission = world.dimension.canSleepAt(player, sleep);
        if (permission == IForgeDimension.SleepResult.DENY) return true;
        if (permission == IForgeDimension.SleepResult.BED_EXPLODES) {
            world.removeBlock(pos, false);
            world.createExplosion(null, DamageSource.netherBedExplosion(), sleep.getX() + .5, sleep.getY() + .5,
                    sleep.getZ() + .5, 5, true, Explosion.Mode.DESTROY);
        } else if (world.getPlayers().stream().anyMatch(p -> p.isSleeping()
                && p.getBedPosition().filter(sleep::equals).isPresent())) {
            player.sendStatusMessage(new TranslationTextComponent("block.minecraft.bed.occupied"), true);
        } else player.trySleep(sleep).ifLeft(result -> {
            if (result != null) player.sendStatusMessage(result.getMessage(), true);
        });
        return true;
    }
    @Override public boolean isBed(BlockState state, IBlockReader world, BlockPos pos, Entity player) { return true; }
    @Override public boolean isBedFoot(BlockState state, IWorldReader world, BlockPos pos) { return !head(state); }
    @Override public Direction getBedDirection(BlockState state, IWorldReader world, BlockPos pos) {
        return state.get(DIRECTION).getOpposite();
    }
    @Override public void setBedOccupied(BlockState state, IWorldReader world, BlockPos pos, LivingEntity sleeper,
            boolean occupied) { /* Sleeping players, not an extra block property, determine occupancy. */ }
    @Override public Optional<Vec3d> getBedSpawnPosition(EntityType<?> type, BlockState state, IWorldReader world,
            BlockPos pos, LivingEntity sleeper) {
        BlockPos head = sleepPos(pos, state);
        for (int x = -1; x <= 1; x++) for (int z = -1; z <= 1; z++) {
            BlockPos exit = head.add(x, 0, z);
            if (!Block.func_220055_a(world, exit.down(), Direction.UP)) continue;
            float radius = type.getWidth() / 2;
            Vec3d centre = new Vec3d(exit.getX() + .5, exit.getY(), exit.getZ() + .5);
            if (world.areCollisionShapesEmpty(new AxisAlignedBB(centre.x - radius, centre.y, centre.z - radius,
                    centre.x + radius, centre.y + type.getHeight(), centre.z + radius))) return Optional.of(centre);
        }
        return Optional.empty();
    }
    @Override public BlockState updatePostPlacement(BlockState state, Direction direction, BlockState neighbour,
            IWorld world, BlockPos pos, BlockPos neighbourPos) {
        if (state.get(WATERLOGGED)) world.getPendingFluidTicks().scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        return state;
    }
    @Override public boolean isFlammable(BlockState state, IBlockReader world, BlockPos pos, Direction face) { return true; }
    @Override public int getFlammability(BlockState state, IBlockReader world, BlockPos pos, Direction face) { return 20; }
    @Override public int getFireSpreadSpeed(BlockState state, IBlockReader world, BlockPos pos, Direction face) { return 5; }
    @Override public PushReaction getPushReaction(BlockState state) { return PushReaction.DESTROY; }
    @Override public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
        return UprightFurnitureShapes.rotate(upper(state) ? Block.makeCuboidShape(0, 13, 0, 16, 16, 16)
                : Block.makeCuboidShape(0, 5, head(state) ? 1 : 0, 16, 10, head(state) ? 16 : 15), state.get(DIRECTION));
    }
    @Override public VoxelShape getRenderShape(BlockState state, IBlockReader world, BlockPos pos) {
        return getShape(state, world, pos, ISelectionContext.dummy());
    }
}
