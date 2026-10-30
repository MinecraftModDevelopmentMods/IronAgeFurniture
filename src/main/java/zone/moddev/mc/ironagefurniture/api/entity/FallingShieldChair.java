package zone.moddev.mc.ironagefurniture.api.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import zone.moddev.mc.ironagefurniture.api.ShieldChairItemData;
import zone.moddev.mc.ironagefurniture.api.tile.ShieldChairBlockEntity;

/** Native falling physics with complete shield data on landing, failed landing and reload. */
public final class FallingShieldChair extends FallingBlockEntity {
    private static final EntityDataAccessor<BlockState> FRAME = SynchedEntityData.defineId(FallingShieldChair.class, EntityDataSerializers.BLOCK_STATE);
    private static final EntityDataAccessor<ItemStack> SHIELD = SynchedEntityData.defineId(FallingShieldChair.class, EntityDataSerializers.ITEM_STACK);

    public FallingShieldChair(EntityType<? extends FallingShieldChair> type, Level level) { super(type, level); }

    @Override protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(FRAME, Blocks.AIR.defaultBlockState());
        entityData.define(SHIELD, ItemStack.EMPTY);
    }

    public static void fallWithShield(ServerLevel level, BlockPos pos, BlockState state) {
        if (!(level.getBlockEntity(pos) instanceof ShieldChairBlockEntity storage)) return;
        CompoundTag contents = storage.saveWithoutMetadata();
        ItemStack shield = storage.shield();
        FallingShieldChair entity = new FallingShieldChair(Entities.FALLING_SHIELD_CHAIR.get(), level);
        CompoundTag data = new CompoundTag();
        BlockState fallingState = state.hasProperty(BlockStateProperties.WATERLOGGED)
                ? state.setValue(BlockStateProperties.WATERLOGGED, false) : state;
        data.put("BlockState", NbtUtils.writeBlockState(fallingState));
        data.put("TileEntityData", contents);
        data.putBoolean("DropItem", true);
        entity.readAdditionalSaveData(data);
        entity.blocksBuilding = true;
        entity.setPos(pos.getX() + .5, pos.getY(), pos.getZ() + .5);
        entity.setStartPos(pos);
        entity.xo = entity.getX(); entity.yo = entity.getY(); entity.zo = entity.getZ();
        // Clear the placed owner before its native removal hook can drop anything.
        storage.setShield(ItemStack.EMPTY);
        if (!level.setBlock(pos, state.getFluidState().createLegacyBlock(), Block.UPDATE_ALL)) {
            storage.setShield(shield);
            return;
        }
        if (!level.addFreshEntity(entity)) {
            level.setBlockAndUpdate(pos, state);
            if (level.getBlockEntity(pos) instanceof ShieldChairBlockEntity restored) {
                restored.load(contents);
                restored.setChanged();
                level.sendBlockUpdated(pos, state, state, Block.UPDATE_CLIENTS);
            }
        }
    }

    public ItemStack shield() { return entityData.get(SHIELD).copy(); }
    public BlockState visibleState() { return entityData.get(FRAME); }

    @Override public void tick() {
        super.tick();
        if (!isAlive() && level() instanceof ServerLevel server
                && server.getBlockEntity(blockPosition()) instanceof ShieldChairBlockEntity storage
                && ItemStack.isSameItemSameTags(storage.shield(), shield())) {
            BlockState landed = server.getBlockState(blockPosition());
            server.sendBlockUpdated(blockPosition(), landed, landed, Block.UPDATE_CLIENTS);
        }
    }

    @Override protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        entityData.set(FRAME, getBlockState());
        entityData.set(SHIELD, blockData != null && blockData.contains("Shield")
                ? ItemStack.of(blockData.getCompound("Shield"))
                : ItemStack.EMPTY);
    }

    @Override public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
        super.onSyncedDataUpdated(accessor);
        if (level().isClientSide() && accessor.equals(FRAME)) {
            CompoundTag data = new CompoundTag();
            data.put("BlockState", NbtUtils.writeBlockState(entityData.get(FRAME)));
            super.readAdditionalSaveData(data);
        }
    }

    @Override public ItemEntity spawnAtLocation(ItemStack stack, float offset) {
        if (stack.is(getBlockState().getBlock().asItem()))
            stack = ShieldChairItemData.createChair(getBlockState().getBlock(), shield());
        return super.spawnAtLocation(stack, offset);
    }
}
