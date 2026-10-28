package zone.moddev.mc.ironagefurniture.api.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import zone.moddev.mc.ironagefurniture.init.PhaseFourBlockEntities;

/** Stored shields are real items, including their complete data components. */
public final class ShieldChairBlockEntity extends BlockEntity {
    private ItemStack shield = new ItemStack(Items.SHIELD);

    public ShieldChairBlockEntity(BlockPos pos, BlockState state) {
        super(PhaseFourBlockEntities.SHIELD_CHAIR.get(), pos, state);
    }

    public ItemStack shield() { return shield.copy(); }

    public void setShield(ItemStack stack) {
        if (!stack.isEmpty() && !stack.is(Items.SHIELD))
            throw new IllegalArgumentException("A shield chair holds one shield");
        shield = stack.copyWithCount(1);
        setChanged();
        if (level != null)
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
    }

    @Override protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        // Missing data is an original, non-removable shield chair. An explicitly
        // encoded empty stack is an empty frame, not a request for a new shield.
        shield = tag.contains("Shield")
                ? ItemStack.OPTIONAL_CODEC.parse(registries.createSerializationContext(NbtOps.INSTANCE), tag.get("Shield"))
                        .result().orElseGet(() -> new ItemStack(Items.SHIELD))
                : new ItemStack(Items.SHIELD);
    }

    @Override protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("Shield", ItemStack.OPTIONAL_CODEC
                .encodeStart(registries.createSerializationContext(NbtOps.INSTANCE), shield).getOrThrow());
    }

    @Override public CompoundTag getUpdateTag(HolderLookup.Provider registries) { return saveCustomOnly(registries); }
    @Override public ClientboundBlockEntityDataPacket getUpdatePacket() { return ClientboundBlockEntityDataPacket.create(this); }

    /** Native 1.21.1 has no preRemoveSideEffects hook; the block calls this before removal. */
    public void dropShield() {
        ItemStack stored = shield;
        shield = ItemStack.EMPTY;
        if (level != null && !level.isClientSide() && !stored.isEmpty())
            Block.popResource(level, worldPosition, stored);
    }
    @Override public net.minecraft.world.phys.AABB getRenderBoundingBox() {
        return new net.minecraft.world.phys.AABB(getBlockPos()).expandTowards(0, 1, 0);
    }
}
