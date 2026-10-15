package zone.moddev.mc.ironagefurniture.api.blocks.furniture;

import java.util.Collections;
import java.util.List;
import net.minecraft.util.RandomSource;
import com.google.common.collect.ImmutableList;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootParams;
import zone.moddev.mc.ironagefurniture.api.UpholsteryItemData;
import zone.moddev.mc.ironagefurniture.api.enumerations.ChairPart;
import zone.moddev.mc.ironagefurniture.api.enumerations.UpholsteryColour;

/** Tall chairs share placement and whole-chair removal, not bench connections. */
public abstract class MultiBlockChair extends Chair {
    @Override public boolean isFlammable(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
        return !FurnitureWoodProperties.fireproof(this);
    }
    @Override public int getFlammability(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
        return FurnitureWoodProperties.fireproof(this) ? 0 : super.getFlammability(state, world, pos, face);
    }
    @Override public int getFireSpreadSpeed(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
        return FurnitureWoodProperties.fireproof(this) ? 0 : super.getFireSpreadSpeed(state, world, pos, face);
    }
    public static final EnumProperty<ChairPart> PART = EnumProperty.create("part", ChairPart.class);
    private boolean removingParts;
    private VoxelShape[][] chairShapes;

    protected MultiBlockChair(String name) {
        super(1, 10, net.minecraft.world.level.block.SoundType.WOOD, name);
        registerDefaultState(defaultBlockState().setValue(DIRECTION, Direction.SOUTH).setValue(PART, ChairPart.LOWER)
                .setValue(UpholsteryItemData.COLOUR, UpholsteryColour.RED).setValue(WATERLOGGED, false));
    }
    public abstract int getChairHeight();
    protected abstract VoxelShape upperShape(ChairPart part);
    @Override protected void generateShapes(ImmutableList<BlockState> states) { }
    @Override protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(PART, UpholsteryItemData.COLOUR);
    }
    public ChairPart partForOffset(int offset) {
        return offset == 0 ? ChairPart.LOWER : offset == getChairHeight() - 1 ? ChairPart.UPPER : ChairPart.MIDDLE;
    }
    public int partOffset(ChairPart part) {
        return part == ChairPart.LOWER ? 0 : part == ChairPart.UPPER ? getChairHeight() - 1 : 1;
    }
    @Override public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos pos = context.getClickedPos();
        for (int offset = 0; offset < getChairHeight(); offset++) {
            BlockPos part = pos.above(offset);
            if (part.getY() >= context.getLevel().getMaxY() + 1
                    || !context.getLevel().getBlockState(part).canBeReplaced(context)) return null;
        }
        // The tall-chair models use the same orientation as classic chairs.
        return super.getStateForPlacement(context)
                .setValue(PART, ChairPart.LOWER).setValue(UpholsteryItemData.COLOUR, UpholsteryItemData.getColour(context.getItemInHand()));
    }
    @Override public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        if (world.isClientSide()) return;
        for (int offset = 1; offset < getChairHeight(); offset++) {
            BlockPos part = pos.above(offset);
            world.setBlock(part, state.setValue(PART, partForOffset(offset)).setValue(WATERLOGGED,
                    world.getFluidState(part).getType() == Fluids.WATER), 2);
        }
        world.updateNeighborsAt(pos, this);
    }
    @Override protected net.minecraft.world.InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
        BlockPos base = pos.below(partOffset(state.getValue(PART)));
        BlockState lower = world.getBlockState(base);
        return lower.getBlock() == this && !player.isShiftKeyDown()
                ? super.useWithoutItem(lower, world, base, player, hit) : net.minecraft.world.InteractionResult.PASS;
    }
    @Override public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        return Collections.singletonList(UpholsteryItemData.create(this, state.getValue(UpholsteryItemData.COLOUR)));
    }
    @Override public ItemStack getCloneItemStack(LevelReader world, BlockPos pos, BlockState state, boolean includeData, Player player) {
        return UpholsteryItemData.create(this, state.getValue(UpholsteryItemData.COLOUR));
    }
    @Override protected void affectNeighborsAfterRemoval(BlockState state, net.minecraft.server.level.ServerLevel world, BlockPos pos, boolean moving) {
        if (!world.isClientSide() && !removingParts) {
            BlockPos base = pos.below(partOffset(state.getValue(PART)));
            removingParts = true;
            try {
                for (int offset = 0; offset < getChairHeight(); offset++) {
                    BlockPos other = base.above(offset);
                    BlockState part = world.getBlockState(other);
                    if (!other.equals(pos) && part.getBlock() == this && part.getValue(PART) == partForOffset(offset)
                            && part.getValue(DIRECTION) == state.getValue(DIRECTION))
                        world.setBlock(other, part.getFluidState().createLegacyBlock(), 2);
                }
            } finally { removingParts = false; }
        }
        super.affectNeighborsAfterRemoval(state, world, pos, moving);
    }
    @Override protected BlockState updateShape(BlockState state, LevelReader world, ScheduledTickAccess ticks, BlockPos pos, Direction direction, BlockPos neighbourPos, BlockState neighbour, RandomSource random) {
        if (state.getValue(WATERLOGGED)) ticks.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
        return state;
    }
    // The accepted tall chairs are anchored multiblocks, rather than independent falling parts.
    @Override public void tick(BlockState state, net.minecraft.server.level.ServerLevel world, BlockPos pos, RandomSource random) { }
    @Override public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return cachedShape(state.getValue(PART), state.getValue(DIRECTION));
    }
    private synchronized VoxelShape cachedShape(ChairPart part, Direction facing) {
        // Colour and water do not change the frame. Forge rebuilds its state
        // caches on world entry; do not reconstruct the same shape each time.
        if (chairShapes == null) chairShapes = new VoxelShape[ChairPart.values().length][4];
        int direction = facing.get2DDataValue();
        if (chairShapes[part.ordinal()][direction] != null) return chairShapes[part.ordinal()][direction];
        VoxelShape shape = part == ChairPart.LOWER
                ? net.minecraft.world.phys.shapes.Shapes.or(Block.box(1, 6, 2, 15, 8, 15),
                        Block.box(0, 0, 2, 2, 13, 15), Block.box(14, 0, 2, 16, 13, 15),
                        Block.box(2, 0, 14, 14, 16, 16)) : upperShape(part);
        return chairShapes[part.ordinal()][direction] = UprightFurnitureShapes.rotate(shape, facing);
    }
    @Override protected VoxelShape getOcclusionShape(BlockState state) {
        return cachedShape(state.getValue(PART), state.getValue(DIRECTION));
    }
}
