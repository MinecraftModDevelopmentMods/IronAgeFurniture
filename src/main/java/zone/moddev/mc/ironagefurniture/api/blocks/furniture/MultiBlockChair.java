package zone.moddev.mc.ironagefurniture.api.blocks.furniture;

import java.util.Collections;
import java.util.List;
import com.google.common.collect.ImmutableList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import zone.moddev.mc.ironagefurniture.api.UpholsteryItemData;
import zone.moddev.mc.ironagefurniture.api.enumerations.ChairPart;
import zone.moddev.mc.ironagefurniture.api.enumerations.UpholsteryColour;

/** Tall chairs share placement and whole-chair removal, not bench connections. */
public abstract class MultiBlockChair extends Chair {
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
            if (context.getLevel().isOutsideBuildHeight(part)
                    || !context.getLevel().getBlockState(part).canBeReplaced(context)) return null;
        }
        return super.getStateForPlacement(context).setValue(PART, ChairPart.LOWER)
                .setValue(UpholsteryItemData.COLOUR, UpholsteryItemData.getColour(context.getItemInHand()));
    }
    @Override public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        if (world.isClientSide) return;
        for (int offset = 1; offset < getChairHeight(); offset++) {
            BlockPos part = pos.above(offset);
            world.setBlock(part, state.setValue(PART, partForOffset(offset)).setValue(WATERLOGGED,
                    world.getFluidState(part).getType() == Fluids.WATER), Block.UPDATE_CLIENTS);
        }
        world.updateNeighborsAt(pos, this);
    }
    @Override protected InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos,
            Player player, BlockHitResult hit) {
        BlockPos base = pos.below(partOffset(state.getValue(PART)));
        BlockState lower = world.getBlockState(base);
        return lower.getBlock() == this && !player.isShiftKeyDown()
                ? super.useWithoutItem(lower, world, base, player, hit) : InteractionResult.PASS;
    }
    @Override protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level world,
            BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        // 1.20.6 has a separate held-item hook; decorative parts must still sit at the base.
        BlockPos base = pos.below(partOffset(state.getValue(PART)));
        BlockState lower = world.getBlockState(base);
        return lower.getBlock() == this && !player.isShiftKeyDown()
                ? super.useItemOn(stack, lower, world, base, player, hand, hit)
                : ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }
    @Override public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        return Collections.singletonList(UpholsteryItemData.create(this, state.getValue(UpholsteryItemData.COLOUR)));
    }
    @Override public ItemStack getCloneItemStack(LevelReader world, BlockPos pos, BlockState state) {
        return UpholsteryItemData.create(this, state.getValue(UpholsteryItemData.COLOUR));
    }
    @Override public ItemStack getCloneItemStack(BlockState state, HitResult hit, LevelReader world, BlockPos pos, Player player) {
        return getCloneItemStack(world, pos, state);
    }
    @Override public void onRemove(BlockState state, Level world, BlockPos pos, BlockState replacement, boolean moving) {
        if (state.getBlock() != replacement.getBlock() && !world.isClientSide && !removingParts) {
            BlockPos base = pos.below(partOffset(state.getValue(PART)));
            removingParts = true;
            try {
                for (int offset = 0; offset < getChairHeight(); offset++) {
                    BlockPos other = base.above(offset);
                    BlockState part = world.getBlockState(other);
                    if (!other.equals(pos) && part.getBlock() == this && part.getValue(PART) == partForOffset(offset)
                            && part.getValue(DIRECTION) == state.getValue(DIRECTION))
                        world.setBlock(other, part.getFluidState().createLegacyBlock(), Block.UPDATE_CLIENTS);
                }
            } finally { removingParts = false; }
        }
        super.onRemove(state, world, pos, replacement, moving);
    }
    @Override public BlockState updateShape(BlockState state, Direction direction, BlockState neighbour,
            LevelAccessor world, BlockPos pos, BlockPos neighbourPos) {
        if (state.getValue(WATERLOGGED)) world.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
        return state;
    }
    // Tall chairs are anchored multiblocks, not independent falling parts.
    @Override public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) { }
    @Override public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return cachedShape(state.getValue(PART), state.getValue(DIRECTION));
    }
    @Override public VoxelShape getOcclusionShape(BlockState state, BlockGetter world, BlockPos pos) {
        return cachedShape(state.getValue(PART), state.getValue(DIRECTION));
    }
    private synchronized VoxelShape cachedShape(ChairPart part, Direction facing) {
        if (chairShapes == null) chairShapes = new VoxelShape[ChairPart.values().length][4];
        int direction = facing.get2DDataValue();
        if (chairShapes[part.ordinal()][direction] != null) return chairShapes[part.ordinal()][direction];
        VoxelShape shape = part == ChairPart.LOWER
                ? Shapes.or(Block.box(1, 6, 2, 15, 8, 15), Block.box(0, 0, 2, 2, 13, 15),
                        Block.box(14, 0, 2, 16, 13, 15), Block.box(2, 0, 14, 14, 16, 16)) : upperShape(part);
        return chairShapes[part.ordinal()][direction] = UprightFurnitureShapes.rotate(shape, facing);
    }
    @Override public boolean isFlammable(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
        return !FurnitureWoodProperties.fireproof(this);
    }
    @Override public int getFlammability(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
        return FurnitureWoodProperties.fireproof(this) ? 0 : super.getFlammability(state, world, pos, face);
    }
    @Override public int getFireSpreadSpeed(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
        return FurnitureWoodProperties.fireproof(this) ? 0 : super.getFireSpreadSpeed(state, world, pos, face);
    }
}
