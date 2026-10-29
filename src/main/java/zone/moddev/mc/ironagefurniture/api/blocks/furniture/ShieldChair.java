package zone.moddev.mc.ironagefurniture.api.blocks.furniture;

import java.util.List;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
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
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import zone.moddev.mc.ironagefurniture.Ironagefurniture;
import zone.moddev.mc.ironagefurniture.api.ShieldChairItemData;
import zone.moddev.mc.ironagefurniture.api.entity.FallingShieldChair;
import zone.moddev.mc.ironagefurniture.api.tile.ShieldChairBlockEntity;

/** Sneak with an empty hand to remove the shield; click with a shield to attach it. */
@EventBusSubscriber(modid = Ironagefurniture.MODID)
public final class ShieldChair extends Chair implements EntityBlock {
    private final boolean fireproof;
    private ImmutableMap<BlockState, VoxelShape> shieldShapes;

    public ShieldChair(float hardness, float resistance, SoundType sound, String name) {
        super(Block.Properties.of().strength(hardness, resistance).sound(sound).dynamicShape());
        fireproof = FurnitureWoodProperties.isFireproof(name);
    }

    @Override protected void generateShapes(ImmutableList<BlockState> states) {
        VoxelShape frame = Shapes.or(Block.box(13, 0, 3, 14, 10, 4), Block.box(2, 0, 3, 3, 10, 4),
                Block.box(13, 12, 1, 15, 13, 13), Block.box(1, 12, 1, 3, 13, 13),
                Block.box(13, 11, 0, 15, 12, 4), Block.box(1, 11, 0, 3, 12, 4),
                Block.box(13, 10, 1, 15, 11, 4), Block.box(1, 10, 1, 3, 11, 4),
                Block.box(1, 7, 2, 15, 8, 13), Block.box(3, 7, 13, 13, 8, 14),
                Block.box(13, 0, 13, 15, 22, 15), Block.box(1, 0, 13, 3, 22, 15));
        VoxelShape shielded = Shapes.or(frame, Block.box(3, 8, 14.19, 13, 24, 14.94));
        ImmutableMap.Builder<BlockState, VoxelShape> empty = ImmutableMap.builder(), full = ImmutableMap.builder();
        for (BlockState state : states) {
            empty.put(state, UprightFurnitureShapes.rotate(frame, state.getValue(DIRECTION)).optimize());
            full.put(state, UprightFurnitureShapes.rotate(shielded, state.getValue(DIRECTION)).optimize());
        }
        _shapes = empty.build();
        shieldShapes = full.build();
    }

    @Override public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return world.getBlockEntity(pos) instanceof ShieldChairBlockEntity storage && storage.shield().isEmpty()
                ? _shapes.get(state) : shieldShapes.get(state);
    }

    @SubscribeEvent public static void allowEmptyHandRemoval(PlayerInteractEvent.RightClickBlock event) {
        if (!event.isCanceled() && event.getUseBlock() != Result.DENY
                && event.getEntity().isShiftKeyDown() && event.getItemStack().isEmpty()
                && event.getLevel().getBlockState(event.getPos()).getBlock() instanceof ShieldChair)
            event.setUseBlock(Result.ALLOW);
    }

    @Override public boolean isFlammable(BlockState state, BlockGetter world, BlockPos pos, Direction side) { return !fireproof; }
    @Override public int getFlammability(BlockState state, BlockGetter world, BlockPos pos, Direction side) { return fireproof ? 0 : 20; }
    @Override public int getFireSpreadSpeed(BlockState state, BlockGetter world, BlockPos pos, Direction side) { return fireproof ? 0 : 5; }
    @Override public BlockEntity newBlockEntity(BlockPos pos, BlockState state) { return new ShieldChairBlockEntity(pos, state); }

    @Override public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(world, pos, state, placer, stack);
        if (!world.isClientSide() && world.getBlockEntity(pos) instanceof ShieldChairBlockEntity storage)
            storage.setShield(ShieldChairItemData.getShield(stack));
    }

    @Override protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos,
            Player player, InteractionHand hand, BlockHitResult hit) {
        if (stack.isEmpty()) {
            InteractionResult result = useWithoutItem(state, world, pos, player, hit);
            return result.consumesAction() ? ItemInteractionResult.sidedSuccess(world.isClientSide())
                    : ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
        if (stack.is(Items.SHIELD) && world.getBlockEntity(pos) instanceof ShieldChairBlockEntity storage && storage.shield().isEmpty()) {
            if (!world.isClientSide()) {
                storage.setShield(stack);
                if (!player.isCreative()) stack.shrink(1);
            }
            return ItemInteractionResult.sidedSuccess(world.isClientSide());
        }
        return super.useItemOn(stack, state, world, pos, player, hand, hit);
    }

    @Override protected InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
        if (player.isShiftKeyDown() && world.getBlockEntity(pos) instanceof ShieldChairBlockEntity storage) {
            if (!world.isClientSide()) {
                ItemStack shield = storage.shield();
                storage.setShield(ItemStack.EMPTY);
                player.getInventory().placeItemBackInInventory(shield);
            }
            return InteractionResult.sidedSuccess(world.isClientSide());
        }
        return super.useWithoutItem(state, world, pos, player, hit);
    }

    @Override public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        return List.of(ShieldChairItemData.createChair(this, ItemStack.EMPTY));
    }

    @Override public void onRemove(BlockState state, Level world, BlockPos pos, BlockState replacement, boolean moving) {
        if (!state.is(replacement.getBlock()) && world.getBlockEntity(pos) instanceof ShieldChairBlockEntity storage)
            storage.dropShield();
        super.onRemove(state, world, pos, replacement, moving);
    }

    @Override public BlockState playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
        if (!world.isClientSide() && player.isCreative()
                && world.getBlockEntity(pos) instanceof ShieldChairBlockEntity storage)
            storage.setShield(ItemStack.EMPTY);
        return super.playerWillDestroy(world, pos, state, player);
    }

    @Override public ItemStack getCloneItemStack(BlockState state, HitResult hit, LevelReader world, BlockPos pos, Player player) {
        ItemStack shield = world.getBlockEntity(pos) instanceof ShieldChairBlockEntity storage
                ? storage.shield() : new ItemStack(Items.SHIELD);
        return ShieldChairItemData.createChair(this, shield);
    }

    @Override public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        if (isFree(world.getBlockState(pos.below())) && pos.getY() >= world.getMinBuildHeight())
            FallingShieldChair.fallWithShield(world, pos, state);
    }
}
