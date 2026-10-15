package zone.moddev.mc.ironagefurniture.api.tile;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import zone.moddev.mc.ironagefurniture.api.ShieldChairItemData;
import zone.moddev.mc.ironagefurniture.init.PhaseFourTileEntities;

/** Non-ticking storage shared by shield chairs of every wood. */
public final class ShieldChairTileEntity extends BlockEntity {
    private ItemStack shield = new ItemStack(Items.SHIELD);

    public ShieldChairTileEntity(net.minecraft.core.BlockPos pos, BlockState state) {
        super(PhaseFourTileEntities.shield_chair, pos, state);
    }

    public boolean hasShield() { return !shield.isEmpty(); }
    public ItemStack getShield() { return shield.copy(); }

    public void setShield(ItemStack next) {
        if (next != null && !next.isEmpty() && next.getItem() != Items.SHIELD) return;
        shield = next == null ? ItemStack.EMPTY : next.copy();
        if (!shield.isEmpty()) shield.setCount(1);
        setChanged();
        if (level != null) {
            BlockState state = level.getBlockState(worldPosition);
            level.sendBlockUpdated(worldPosition, state, state, 3);
        }
    }

    @Override public void load(CompoundTag tag) {
        super.load(tag);
        shield = ShieldChairItemData.readShield(tag);
    }

    @Override public CompoundTag save(CompoundTag tag) {
        super.save(tag);
        tag.remove(ShieldChairItemData.SHIELD_TAG);
        tag.remove(ShieldChairItemData.EMPTY_TAG);
        tag.merge(ShieldChairItemData.writeShield(shield));
        return tag;
    }

    @Override public CompoundTag getUpdateTag() { return save(new CompoundTag()); }
    @Override public void handleUpdateTag(CompoundTag tag) { load(tag); }
    @Override public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return new ClientboundBlockEntityDataPacket(worldPosition, 0, getUpdateTag());
    }
    @Override public void onDataPacket(Connection manager, ClientboundBlockEntityDataPacket packet) {
        load(packet.getTag());
    }
}
