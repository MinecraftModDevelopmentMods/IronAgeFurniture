package zone.moddev.mc.ironagefurniture.api.blocks.lightsource.phasefour;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import zone.moddev.mc.ironagefurniture.api.CandleInventory;
import zone.moddev.mc.ironagefurniture.api.SconceMetalData;
import zone.moddev.mc.ironagefurniture.api.blocks.lightholder.LightHolderSconceFloor;
import zone.moddev.mc.ironagefurniture.api.tile.CandleSconceBlockEntity;
import zone.moddev.mc.ironagefurniture.init.RegistrationProperties;

/** Mixed wax colours share one flame state, not thousands of registered blocks. */
public final class CandleSconceBlock extends LightHolderSconceFloor implements EntityBlock {
    public static final IntegerProperty COUNT = IntegerProperty.create("candles", 1, 4);
    public static final BooleanProperty LIT = BooleanProperty.create("lit");
    public static final float CANDLE_SCALE = .75F;
    private final boolean wall;

    public CandleSconceBlock(String name, boolean wall) {
        super(RegistrationProperties.block(Block.Properties.of().strength(1, 10).sound(SoundType.METAL).noOcclusion()
                .lightLevel(state -> state.getValue(LIT) ? 11 + state.getValue(COUNT) : 0), name));
        this.wall = wall;
        registerDefaultState(defaultBlockState().setValue(COUNT, 1).setValue(LIT, false));
    }
    public boolean isWall() { return wall; }
    @Override protected void generateShapes(com.google.common.collect.ImmutableList<BlockState> states) { }
    @Override protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(COUNT, LIT);
    }
    @Override public BlockEntity newBlockEntity(BlockPos pos, BlockState state) { return new CandleSconceBlockEntity(pos, state); }

    @Override protected InteractionResult ActivateSconce(BlockState state, Level world, BlockPos pos,
            Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack held = player.getItemInHand(hand);
        if (world.isClientSide()) return InteractionResult.SUCCESS;
        if (!(world.getBlockEntity(pos) instanceof CandleSconceBlockEntity storage)) return InteractionResult.FAIL;
        if (held.isEmpty()) {
            ItemStack candle = storage.removeNewest();
            // The ordinary add method discards overflow in Creative. Stored items
            // must instead go back into a slot or drop safely beside the player.
            player.getInventory().placeItemBackInInventory(candle, true);
            if (storage.candles().isEmpty()) world.setBlock(pos, SconceMetalData.empty(state, wall), Block.UPDATE_ALL);
            return InteractionResult.SUCCESS_SERVER;
        }
        if (CandleInventory.accepts(held)) {
            if (storage.insert(held) && !player.isCreative()) held.shrink(1);
            return InteractionResult.SUCCESS_SERVER;
        }
        if (held.is(Items.WATER_BUCKET)) {
            world.setBlock(pos, state.setValue(LIT, false), Block.UPDATE_ALL);
            world.scheduleTick(pos, this, 2);
            return InteractionResult.SUCCESS_SERVER;
        }
        if (held.is(Items.FLINT_AND_STEEL) || held.is(Items.TORCH)) {
            if (!state.getValue(LIT) && !state.getValue(WATERLOGGED)) {
                world.setBlock(pos, state.setValue(LIT, true), Block.UPDATE_ALL);
                if (held.is(Items.FLINT_AND_STEEL) && !player.isCreative())
                    held.hurtAndBreak(1, player, hand == InteractionHand.MAIN_HAND
                            ? net.minecraft.world.entity.EquipmentSlot.MAINHAND : net.minecraft.world.entity.EquipmentSlot.OFFHAND);
            }
            return InteractionResult.SUCCESS_SERVER;
        }
        // Never let an unrelated held block replace or place through the holder.
        return InteractionResult.SUCCESS_SERVER;
    }

    @Override protected void neighborChanged(BlockState state, Level world, BlockPos pos, Block block,
            Orientation orientation, boolean moving) {
        if (!world.isClientSide()) world.scheduleTick(pos, this, 2);
    }
    @Override public void onPlace(BlockState state, Level world, BlockPos pos, BlockState previous, boolean moving) {
        super.onPlace(state, world, pos, previous, moving);
        if (!world.isClientSide()) world.scheduleTick(pos, this, 2);
    }
    @Override public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        boolean powered = world.hasNeighborSignal(pos);
        boolean lit = state.getValue(WATERLOGGED) ? powered : state.getValue(LIT) || powered;
        if (lit != state.getValue(LIT)) world.setBlock(pos, state.setValue(LIT, lit), Block.UPDATE_ALL);
    }
    @Override public boolean canConnectRedstone(BlockState state, BlockGetter world, BlockPos pos, Direction side) { return true; }
    @Override public boolean placeLiquid(LevelAccessor world, BlockPos pos, BlockState state, FluidState fluid) {
        if (state.getValue(WATERLOGGED) || !fluid.is(Fluids.WATER)) return false;
        if (!world.isClientSide()) {
            world.setBlock(pos, state.setValue(WATERLOGGED, true).setValue(LIT, false), Block.UPDATE_ALL);
            world.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
            world.scheduleTick(pos, this, 2);
        }
        return true;
    }
    @Override public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        Direction supportFace = wall ? state.getValue(DIRECTION) : Direction.UP;
        return Block.canSupportCenter(world, pos.relative(supportFace.getOpposite()), supportFace);
    }
    @Override protected BlockState updateShape(BlockState state, LevelReader world, ScheduledTickAccess ticks,
            BlockPos pos, Direction direction, BlockPos neighbourPos, BlockState neighbour, RandomSource random) {
        if (!canSurvive(state, world, pos)) return Blocks.AIR.defaultBlockState();
        if (state.getValue(WATERLOGGED)) ticks.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
        return state;
    }
    @Override public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        // The block entity's pre-removal hook releases the exact stored candles.
        return List.of(SconceMetalData.frame(state));
    }
    @Override public ItemStack getCloneItemStack(LevelReader world, BlockPos pos, BlockState state, boolean includeData) {
        return SconceMetalData.frame(state);
    }
    @Override public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        VoxelShape shape = wall ? Block.box(-.5, 2, 5, 7, 11, 11) : Block.box(5, 0, 5, 11, 11, 11);
        return zone.moddev.mc.ironagefurniture.api.blocks.base.FurnitureBlock.rotate(shape, state.getValue(DIRECTION));
    }
    @Override protected VoxelShape getOcclusionShape(BlockState state) { return net.minecraft.world.phys.shapes.Shapes.empty(); }

    public Vec3 candleOrigin(BlockState state, int slot, int count) {
        // Four small candles fit inside the original dish's four-pixel centre.
        double spacing = 1.25D / 16D;
        double x = count == 1 || count == 3 && slot == 2 ? .5 : .5 + ((slot & 1) == 0 ? -spacing : spacing);
        double z = count <= 2 ? .5 : .5 + (slot < 2 ? -spacing : spacing);
        if (wall) x -= .25;
        oshi.util.tuples.Pair<Double, Double> point = zone.moddev.mc.ironagefurniture.api.blocks.base.FurnitureBlock.rotate(x, z, state.getValue(DIRECTION));
        return new Vec3(point.getA(), 10D / 16D, point.getB());
    }
    public Vec3 candleWick(BlockState state, int slot, int count) {
        return candleOrigin(state, slot, count).add(0, 7D / 16D * CANDLE_SCALE, 0);
    }
    @Override public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
        if (!state.getValue(LIT)) return;
        if (!(world.getBlockEntity(pos) instanceof CandleSconceBlockEntity storage)) return;
        int count = storage.candles().size();
        for (int slot = 0; slot < count; slot++) {
            Vec3 wick = candleWick(state, slot, count);
            world.addParticle(ParticleTypes.SMALL_FLAME, pos.getX() + wick.x, pos.getY() + wick.y, pos.getZ() + wick.z, 0, 0, 0);
            if (random.nextInt(4) == 0) world.addParticle(ParticleTypes.SMOKE, pos.getX() + wick.x,
                    pos.getY() + wick.y + .04, pos.getZ() + wick.z, 0, 0, 0);
        }
    }
}
