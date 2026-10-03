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
        setDefaultState(getDefaultState().with(DIRECTION, Direction.SOUTH).with(PART, ChairPart.LOWER)
                .with(UpholsteryItemData.COLOUR, UpholsteryColour.RED).with(WATERLOGGED, false));
    }
    public abstract int getChairHeight();
    protected abstract VoxelShape upperShape(ChairPart part);
    @Override protected void generateShapes(ImmutableList<BlockState> states) { }
    @Override protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(PART, UpholsteryItemData.COLOUR);
    }
    public ChairPart partForOffset(int offset) {
        return offset == 0 ? ChairPart.LOWER : offset == getChairHeight() - 1 ? ChairPart.UPPER : ChairPart.MIDDLE;
    }
    public int partOffset(ChairPart part) {
        return part == ChairPart.LOWER ? 0 : part == ChairPart.UPPER ? getChairHeight() - 1 : 1;
    }
    @Override public BlockState getStateForPlacement(BlockItemUseContext context) {
        BlockPos pos = context.getPos();
        for (int offset = 0; offset < getChairHeight(); offset++) {
            BlockPos part = pos.up(offset);
            if (part.getY() >= context.getWorld().getHeight()
                    || !context.getWorld().getBlockState(part).isReplaceable(context)) return null;
        }
        return super.getStateForPlacement(context).with(DIRECTION, context.getPlacementHorizontalFacing().getOpposite())
                .with(PART, ChairPart.LOWER).with(UpholsteryItemData.COLOUR, UpholsteryItemData.getColour(context.getItem()));
    }
    @Override public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        if (world.isRemote) return;
        for (int offset = 1; offset < getChairHeight(); offset++) {
            BlockPos part = pos.up(offset);
            world.setBlockState(part, state.with(PART, partForOffset(offset)).with(WATERLOGGED,
                    world.getFluidState(part).getFluid() == Fluids.WATER), 2);
        }
        world.notifyNeighbors(pos, this);
    }
    @Override public boolean onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player,
            Hand hand, BlockRayTraceResult hit) {
        BlockPos base = pos.down(partOffset(state.get(PART)));
        BlockState lower = world.getBlockState(base);
        return lower.getBlock() == this && !player.isSneaking()
                && super.onBlockActivated(lower, world, base, player, hand, hit);
    }
    @Override public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        return Collections.singletonList(UpholsteryItemData.create(this, state.get(UpholsteryItemData.COLOUR)));
    }
    @Override public ItemStack getPickBlock(BlockState state, RayTraceResult hit, IBlockReader world,
            BlockPos pos, PlayerEntity player) {
        return UpholsteryItemData.create(this, state.get(UpholsteryItemData.COLOUR));
    }
    @Override public void onReplaced(BlockState state, World world, BlockPos pos, BlockState replacement, boolean moving) {
        if (!world.isRemote && replacement.getBlock() != this && !removingParts) {
            BlockPos base = pos.down(partOffset(state.get(PART)));
            removingParts = true;
            try {
                for (int offset = 0; offset < getChairHeight(); offset++) {
                    BlockPos other = base.up(offset);
                    BlockState part = world.getBlockState(other);
                    if (!other.equals(pos) && part.getBlock() == this && part.get(PART) == partForOffset(offset)
                            && part.get(DIRECTION) == state.get(DIRECTION))
                        world.setBlockState(other, part.getFluidState().getBlockState(), 2);
                }
            } finally { removingParts = false; }
        }
        super.onReplaced(state, world, pos, replacement, moving);
    }
    @Override public BlockState updatePostPlacement(BlockState state, Direction direction, BlockState neighbour,
            IWorld world, BlockPos pos, BlockPos neighbourPos) {
        if (state.get(WATERLOGGED)) world.getPendingFluidTicks().scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        return state;
    }
    // The accepted tall chairs are anchored multiblocks, rather than independent falling parts.
    @Override public void tick(BlockState state, World world, BlockPos pos, Random random) { }
    @Override public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
        VoxelShape shape = state.get(PART) == ChairPart.LOWER
                ? net.minecraft.util.math.shapes.VoxelShapes.or(Block.makeCuboidShape(1, 6, 2, 15, 8, 15),
                        Block.makeCuboidShape(0, 0, 2, 2, 13, 15), Block.makeCuboidShape(14, 0, 2, 16, 13, 15),
                        Block.makeCuboidShape(2, 0, 14, 14, 16, 16)) : upperShape(state.get(PART));
        return UprightFurnitureShapes.rotate(shape, state.get(DIRECTION));
    }
    @Override public VoxelShape getRenderShape(BlockState state, IBlockReader world, BlockPos pos) {
        return getShape(state, world, pos, ISelectionContext.dummy());
    }
}
