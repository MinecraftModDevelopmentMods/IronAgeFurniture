package zone.moddev.mc.ironagefurniture.api.tile;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import zone.moddev.mc.ironagefurniture.api.ShieldChairItemData;
import zone.moddev.mc.ironagefurniture.init.PhaseFourTileEntities;

/** Non-ticking storage shared by shield chairs of every wood. */
public final class ShieldChairTileEntity extends TileEntity {
    private ItemStack shield = new ItemStack(Items.SHIELD);

    public ShieldChairTileEntity() {
        super(PhaseFourTileEntities.shield_chair);
    }

    public boolean hasShield() { return !shield.isEmpty(); }
    public ItemStack getShield() { return shield.copy(); }

    public void setShield(ItemStack next) {
        if (next != null && !next.isEmpty() && next.getItem() != Items.SHIELD) return;
        shield = next == null ? ItemStack.EMPTY : next.copy();
        if (!shield.isEmpty()) shield.setCount(1);
        markDirty();
        if (world != null) {
            BlockState state = world.getBlockState(pos);
            world.notifyBlockUpdate(pos, state, state, 3);
        }
    }

    @Override public void read(CompoundNBT tag) {
        super.read(tag);
        shield = ShieldChairItemData.readShield(tag);
    }

    @Override public CompoundNBT write(CompoundNBT tag) {
        super.write(tag);
        tag.remove(ShieldChairItemData.SHIELD_TAG);
        tag.remove(ShieldChairItemData.EMPTY_TAG);
        tag.merge(ShieldChairItemData.writeShield(shield));
        return tag;
    }

    @Override public CompoundNBT getUpdateTag() { return write(new CompoundNBT()); }
    @Override public void handleUpdateTag(CompoundNBT tag) { read(tag); }
    @Override public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(pos, 0, getUpdateTag());
    }
    @Override public void onDataPacket(NetworkManager manager, SUpdateTileEntityPacket packet) {
        read(packet.getNbtCompound());
    }
}
