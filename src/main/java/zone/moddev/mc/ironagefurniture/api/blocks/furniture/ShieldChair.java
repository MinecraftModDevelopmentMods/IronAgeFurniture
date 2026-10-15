package zone.moddev.mc.ironagefurniture.api.blocks.furniture;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import zone.moddev.mc.ironagefurniture.api.ShieldChairItemData;
import zone.moddev.mc.ironagefurniture.api.tile.ShieldChairBlockEntity;

/** Sneak with an empty hand to remove the shield; click with a shield to attach it. */
public final class ShieldChair extends Chair implements EntityBlock {
    private final boolean fireproof;
    public ShieldChair(float hardness, float resistance, SoundType sound, String name) {
        super(hardness, resistance, sound, name);
        fireproof = FurnitureWoodProperties.isFireproof(name);
    }
    @Override public boolean isFlammable(BlockState state, net.minecraft.world.level.BlockGetter world, BlockPos pos, net.minecraft.core.Direction side) { return !fireproof; }
    @Override public int getFlammability(BlockState state, net.minecraft.world.level.BlockGetter world, BlockPos pos, net.minecraft.core.Direction side) { return fireproof ? 0 : 20; }
    @Override public int getFireSpreadSpeed(BlockState state, net.minecraft.world.level.BlockGetter world, BlockPos pos, net.minecraft.core.Direction side) { return fireproof ? 0 : 5; }
    @Override public BlockEntity newBlockEntity(BlockPos pos, BlockState state) { return new ShieldChairBlockEntity(pos, state); }
    @Override public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(world, pos, state, placer, stack);
        if (!world.isClientSide() && world.getBlockEntity(pos) instanceof ShieldChairBlockEntity storage)
            storage.setShield(ShieldChairItemData.getShield(stack));
    }
    @Override protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos,
            Player player, InteractionHand hand, BlockHitResult hit) {
        if (stack.is(Items.SHIELD) && world.getBlockEntity(pos) instanceof ShieldChairBlockEntity storage && storage.shield().isEmpty()) {
            if (!world.isClientSide()) {
                storage.setShield(stack);
                if (!player.isCreative()) stack.shrink(1);
            }
            return InteractionResult.SUCCESS;
        }
        return super.useItemOn(stack, state, world, pos, player, hand, hit);
    }
    @Override protected InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
        if (player.isShiftKeyDown() && world.getBlockEntity(pos) instanceof ShieldChairBlockEntity storage) {
            if (!world.isClientSide()) {
                ItemStack shield = storage.shield();
                storage.setShield(ItemStack.EMPTY);
                player.getInventory().placeItemBackInInventory(shield, net.minecraft.util.Prediction.SERVER_ONLY);
            }
            return InteractionResult.SUCCESS;
        }
        return super.useWithoutItem(state, world, pos, player, hit);
    }
    @Override public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        // Stored contents drop once from the block entity's pre-removal hook.
        return List.of(ShieldChairItemData.createChair(this, ItemStack.EMPTY));
    }
    @Override public ItemStack getCloneItemStack(LevelReader world, BlockPos pos, BlockState state, boolean includeData, Player player) {
        ItemStack shield = world.getBlockEntity(pos) instanceof ShieldChairBlockEntity storage ? storage.shield() : new ItemStack(Items.SHIELD);
        return ShieldChairItemData.createChair(this, shield);
    }
    @Override public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        if (!isFree(world.getBlockState(pos.below())) || pos.getY() < world.getMinY()) return;
        zone.moddev.mc.ironagefurniture.api.entity.FallingShieldChair.fallWithShield(world, pos, state);
    }
}
