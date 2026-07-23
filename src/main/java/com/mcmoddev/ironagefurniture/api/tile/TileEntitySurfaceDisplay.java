package com.mcmoddev.ironagefurniture.api.tile;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

public class TileEntitySurfaceDisplay extends TileEntity {
	private ItemStack displayedItem;
	private EnumFacing displayedFacing = EnumFacing.NORTH;

	public boolean hasDisplayedItem() {
		return this.displayedItem != null && this.displayedItem.stackSize > 0;
	}

	public ItemStack getDisplayedItem() {
		return this.displayedItem;
	}

	public EnumFacing getDisplayedFacing() {
		return this.displayedFacing;
	}

	public void setDisplayedItem(ItemStack itemStack, EnumFacing facing) {
		this.displayedItem = itemStack == null ? null : itemStack.copy();
		this.displayedFacing = facing != null && facing.getAxis().isHorizontal() ? facing : EnumFacing.NORTH;
		this.markForUpdate();
	}

	public ItemStack removeDisplayedItem() {
		ItemStack itemStack = this.displayedItem;
		this.displayedItem = null;
		this.displayedFacing = EnumFacing.NORTH;
		this.markForUpdate();
		return itemStack;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		this.displayedItem = compound.hasKey("DisplayedItem")
			? ItemStack.loadItemStackFromNBT(compound.getCompoundTag("DisplayedItem")) : null;
		this.displayedFacing = compound.hasKey("DisplayedFacing")
			? EnumFacing.getHorizontal(compound.getInteger("DisplayedFacing") & 3) : EnumFacing.NORTH;
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);

		if (this.hasDisplayedItem()) {
			NBTTagCompound itemTag = new NBTTagCompound();
			this.displayedItem.writeToNBT(itemTag);
			compound.setTag("DisplayedItem", itemTag);
			compound.setInteger("DisplayedFacing", this.displayedFacing.getHorizontalIndex());
		} else {
			compound.removeTag("DisplayedItem");
			compound.removeTag("DisplayedFacing");
		}

		return compound;
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(this.pos, 0, this.getUpdateTag());
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
		this.readFromNBT(packet.getNbtCompound());

		if (this.world != null) {
			this.world.markBlockRangeForRenderUpdate(this.pos, this.pos);
		}
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		return this.writeToNBT(new NBTTagCompound());
	}

	private void markForUpdate() {
		this.markDirty();

		if (this.world != null && !this.world.isRemote) {
			this.world.notifyBlockUpdate(this.pos, this.world.getBlockState(this.pos),
				this.world.getBlockState(this.pos), 3);
		}
	}
}
