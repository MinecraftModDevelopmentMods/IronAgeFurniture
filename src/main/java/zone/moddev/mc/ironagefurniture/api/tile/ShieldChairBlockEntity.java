package zone.moddev.mc.ironagefurniture.api.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import zone.moddev.mc.ironagefurniture.init.PhaseFourBlockEntities;

/** Stored shields are real items, not reconstructed banner or enchantment fields. */
public final class ShieldChairBlockEntity extends BlockEntity {
    private ItemStack shield = new ItemStack(Items.SHIELD);
    public ShieldChairBlockEntity(BlockPos pos, BlockState state) { super(PhaseFourBlockEntities.SHIELD_CHAIR.get(), pos, state); }
    public ItemStack shield() { return shield.copy(); }
    public void setShield(ItemStack stack) {
        if (!stack.isEmpty() && !stack.is(Items.SHIELD)) throw new IllegalArgumentException("A shield chair holds one shield");
        shield = stack.copyWithCount(1);
        setChanged();
        if (level != null) level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
    }
    @Override protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        shield = input.read("Shield", ItemStack.OPTIONAL_CODEC).orElseGet(() -> new ItemStack(Items.SHIELD));
    }
    @Override protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.store("Shield", ItemStack.OPTIONAL_CODEC, shield);
    }
    @Override public CompoundTag getUpdateTag(HolderLookup.Provider registries) { return saveCustomOnly(registries); }
    @Override public ClientboundBlockEntityDataPacket getUpdatePacket() { return ClientboundBlockEntityDataPacket.create(this); }
    @Override public void preRemoveSideEffects(BlockPos pos, BlockState state) {
        ItemStack stored = shield;
        shield = ItemStack.EMPTY;
        if (level != null && !level.isClientSide() && !stored.isEmpty()) Block.popResource(level, pos, stored);
        super.preRemoveSideEffects(pos, state);
    }
}
