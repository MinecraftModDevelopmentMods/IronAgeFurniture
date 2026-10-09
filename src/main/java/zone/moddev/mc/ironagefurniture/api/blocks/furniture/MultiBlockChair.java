package zone.moddev.mc.ironagefurniture.api.blocks.furniture;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import com.google.common.collect.ImmutableList;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootContext;
import zone.moddev.mc.ironagefurniture.api.UpholsteryItemData;
import zone.moddev.mc.ironagefurniture.api.enumerations.ChairPart;
import zone.moddev.mc.ironagefurniture.api.enumerations.UpholsteryColour;

/** Tall chairs share placement and whole-chair removal, not bench connections. */
public abstract class MultiBlockChair extends Chair {
    public static final EnumProperty<ChairPart> PART = EnumProperty.create("part", ChairPart.class);
    private boolean removingParts;

    protected MultiBlockChair(String name) {
        super(1, 10, net.minecraft.block.SoundType.WOOD, name);
        registerDefaultState(defaultBlockState().setValue(DIRECTION, Direction.SOUTH).setValue(PART, ChairPart.LOWER)
                .setValue(UpholsteryItemData.COLOUR, UpholsteryColour.RED).setValue(WATERLOGGED, false));
    }
    public abstract int getChairHeight();
    protected abstract VoxelShape upperShape(ChairPart part);
    @Override protected void generateShapes(ImmutableList<BlockState> states) { }
    @Override protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(PART, UpholsteryItemData.COLOUR);
    }
    public ChairPart partForOffset(int offset) {
        return offset == 0 ? ChairPart.LOWER : offset == getChairHeight() - 1 ? ChairPart.UPPER : ChairPart.MIDDLE;
    }
    public int partOffset(ChairPart part) {
        return part == ChairPart.LOWER ? 0 : part == ChairPart.UPPER ? getChairHeight() - 1 : 1;
    }
    @Override public BlockState getStateForPlacement(BlockItemUseContext context) {
        BlockPos pos = context.getClickedPos();
        for (int offset = 0; offset < getChairHeight(); offset++) {
            BlockPos part = pos.above(offset);
            if (part.getY() >= context.getLevel().getHeight()
                    || !context.getLevel().getBlockState(part).canBeReplaced(context)) return null;
        }
        // The tall-chair models use the same orientation as classic chairs.
        return super.getStateForPlacement(context)
                .setValue(PART, ChairPart.LOWER).setValue(UpholsteryItemData.COLOUR, UpholsteryItemData.getColour(context.getItemInHand()));
    }
    @Override public void setPlacedBy(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        if (world.isClientSide) return;
        for (int offset = 1; offset < getChairHeight(); offset++) {
            BlockPos part = pos.above(offset);
            world.setBlock(part, state.setValue(PART, partForOffset(offset)).setValue(WATERLOGGED,
                    world.getFluidState(part).getType() == Fluids.WATER), 2);
        }
        world.updateNeighborsAt(pos, this);
    }
    @Override public net.minecraft.util.ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player,
            Hand hand, BlockRayTraceResult hit) {
        BlockPos base = pos.below(partOffset(state.getValue(PART)));
        BlockState lower = world.getBlockState(base);
        return lower.getBlock() == this && !player.isShiftKeyDown()
                ? super.use(lower, world, base, player, hand, hit) : net.minecraft.util.ActionResultType.PASS;
    }
    @Override public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        return Collections.singletonList(UpholsteryItemData.create(this, state.getValue(UpholsteryItemData.COLOUR)));
    }
    @Override public ItemStack getPickBlock(BlockState state, RayTraceResult hit, IBlockReader world,
            BlockPos pos, PlayerEntity player) {
        return UpholsteryItemData.create(this, state.getValue(UpholsteryItemData.COLOUR));
    }
    @Override public void onRemove(BlockState state, World world, BlockPos pos, BlockState replacement, boolean moving) {
        if (!world.isClientSide && replacement.getBlock() != this && !removingParts) {
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
        super.onRemove(state, world, pos, replacement, moving);
    }
    @Override public BlockState updateShape(BlockState state, Direction direction, BlockState neighbour,
            IWorld world, BlockPos pos, BlockPos neighbourPos) {
        if (state.getValue(WATERLOGGED)) world.getLiquidTicks().scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
        return state;
    }
    // The accepted tall chairs are anchored multiblocks, rather than independent falling parts.
    @Override public void tick(BlockState state, net.minecraft.world.server.ServerWorld world, BlockPos pos, Random random) { }
    @Override public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
        VoxelShape shape = state.getValue(PART) == ChairPart.LOWER
                ? net.minecraft.util.math.shapes.VoxelShapes.or(Block.box(1, 6, 2, 15, 8, 15),
                        Block.box(0, 0, 2, 2, 13, 15), Block.box(14, 0, 2, 16, 13, 15),
                        Block.box(2, 0, 14, 14, 16, 16)) : upperShape(state.getValue(PART));
        return UprightFurnitureShapes.rotate(shape, state.getValue(DIRECTION));
    }
    @Override public VoxelShape getOcclusionShape(BlockState state, IBlockReader world, BlockPos pos) {
        return getShape(state, world, pos, ISelectionContext.empty());
    }
}
