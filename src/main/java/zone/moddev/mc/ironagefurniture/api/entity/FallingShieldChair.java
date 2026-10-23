package zone.moddev.mc.ironagefurniture.api.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import zone.moddev.mc.ironagefurniture.api.ShieldChairItemData;
import zone.moddev.mc.ironagefurniture.api.tile.ShieldChairBlockEntity;

/** Vanilla falling physics, with full shield data on landing, failure and reload. */
public final class FallingShieldChair extends FallingBlockEntity {
    private static final EntityDataAccessor<BlockState> FRAME = SynchedEntityData.defineId(FallingShieldChair.class, EntityDataSerializers.BLOCK_STATE);
    private static final EntityDataAccessor<ItemStack> SHIELD = SynchedEntityData.defineId(FallingShieldChair.class, EntityDataSerializers.ITEM_STACK);
    public FallingShieldChair(EntityType<? extends FallingShieldChair> type, Level level) { super(type, level); }
    @Override protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(FRAME, Blocks.AIR.defaultBlockState());
        builder.define(SHIELD, ItemStack.EMPTY);
    }

    public static void fallWithShield(ServerLevel level, BlockPos pos, BlockState state) {
        if (!(level.getBlockEntity(pos) instanceof ShieldChairBlockEntity storage)) return;
        CompoundTag contents = storage.saveWithoutMetadata(level.registryAccess());
        ItemStack shield = storage.shield();
        FallingShieldChair entity = new FallingShieldChair(zone.moddev.mc.ironagefurniture.api.entity.Entities.FALLING_SHIELD_CHAIR.get(), level);
        TagValueOutput output = TagValueOutput.createWithContext(ProblemReporter.DISCARDING, level.registryAccess());
        output.store("BlockState", BlockState.CODEC, state);
        output.store("TileEntityData", CompoundTag.CODEC, contents);
        entity.readAdditionalSaveData(TagValueInput.create(ProblemReporter.DISCARDING, level.registryAccess(), output.buildResult()));
        entity.entityData.set(FRAME, state);
        entity.entityData.set(SHIELD, shield);
        entity.setPos(pos.getX() + .5, pos.getY(), pos.getZ() + .5);
        entity.setStartPos(pos);
        entity.xo = entity.getX(); entity.yo = entity.getY(); entity.zo = entity.getZ();
        // Transfer ownership before removing the placed block. Its normal
        // destruction hook must not also release the shield now in flight.
        storage.setShield(ItemStack.EMPTY);
        level.setBlock(pos, state.getFluidState().createLegacyBlock(), Block.UPDATE_ALL);
        if (!level.addFreshEntity(entity)) {
            level.setBlockAndUpdate(pos, state);
            if (level.getBlockEntity(pos) instanceof ShieldChairBlockEntity restored) restored.setShield(shield);
        }
    }
    public ItemStack shield() { return entityData.get(SHIELD).copy(); }
    public BlockState visibleState() { return entityData.get(FRAME); }
    @Override public void tick() {
        super.tick();
        if (!isAlive() && level() instanceof ServerLevel server
                && server.getBlockEntity(blockPosition()) instanceof ShieldChairBlockEntity storage
                && ItemStack.isSameItemSameComponents(storage.shield(), shield())) {
            BlockState landed = server.getBlockState(blockPosition());
            server.sendBlockUpdated(blockPosition(), landed, landed, Block.UPDATE_CLIENTS);
        }
    }

    @Override protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        entityData.set(FRAME, getBlockState());
        if (blockData != null) {
            ValueInput contents = TagValueInput.create(ProblemReporter.DISCARDING, level().registryAccess(), blockData);
            entityData.set(SHIELD, contents.read("Shield", ItemStack.OPTIONAL_CODEC).orElse(ItemStack.EMPTY));
        }
    }
    @Override public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
        super.onSyncedDataUpdated(accessor);
        if (level().isClientSide() && accessor.equals(FRAME)) {
            TagValueOutput output = TagValueOutput.createWithContext(ProblemReporter.DISCARDING, level().registryAccess());
            output.store("BlockState", BlockState.CODEC, entityData.get(FRAME));
            super.readAdditionalSaveData(TagValueInput.create(ProblemReporter.DISCARDING, level().registryAccess(), output.buildResult()));
        }
    }
    @Override public ItemEntity spawnAtLocation(ServerLevel level, ItemStack stack, float offset) {
        if (stack.is(getBlockState().getBlock().asItem()))
            stack = ShieldChairItemData.createChair(getBlockState().getBlock(), shield());
        return super.spawnAtLocation(level, stack, offset);
    }
}
