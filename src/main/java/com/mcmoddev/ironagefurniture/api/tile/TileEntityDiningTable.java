package com.mcmoddev.ironagefurniture.api.tile;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityDiningTable extends TileEntity {
	private ItemStack displayedItem;

	public boolean hasDisplayedItem() {
		return this.displayedItem != null && this.displayedItem.stackSize > 0;
	}

	public ItemStack getDisplayedItem() {
		return this.displayedItem;
	}

	public void setDisplayedItem(ItemStack displayedItem) {
		this.displayedItem = displayedItem == null ? null : displayedItem.copy();
		this.markForUpdate();
	}

	public ItemStack removeDisplayedItem() {
		ItemStack itemStack = this.displayedItem;
		this.displayedItem = null;
		this.markForUpdate();
		return itemStack;
	}

	public void dropDisplayedItem(World worldIn, BlockPos pos) {
		ItemStack itemStack = this.removeDisplayedItem();

		if (itemStack != null) {
			net.minecraft.block.Block.spawnAsEntity(worldIn, pos, itemStack);
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);

		if (compound.hasKey("DisplayedItem")) {
			this.displayedItem = ItemStack.loadItemStackFromNBT(compound.getCompoundTag("DisplayedItem"));
		} else {
			this.displayedItem = null;
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);

		if (this.hasDisplayedItem()) {
			NBTTagCompound itemTag = new NBTTagCompound();
			this.displayedItem.writeToNBT(itemTag);
			compound.setTag("DisplayedItem", itemTag);
		} else {
			compound.removeTag("DisplayedItem");
		}

		return compound;
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(this.pos, 0, this.getUpdateTag());
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		this.readFromNBT(pkt.getNbtCompound());
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
