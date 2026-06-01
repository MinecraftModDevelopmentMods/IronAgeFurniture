package com.mcmoddev.ironagefurniture.api.tile;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileEntityGlassVase extends TileEntity {
	private ItemStack plant;

	public boolean hasPlant() {
		return this.plant != null && this.plant.stackSize > 0;
	}

	public ItemStack getPlant() {
		return this.plant;
	}

	public void setPlant(ItemStack plantStack) {
		this.plant = plantStack == null ? null : plantStack.copy();

		if (this.plant != null) {
			this.plant.stackSize = 1;
		}

		this.markForUpdate();
	}

	public ItemStack removePlant() {
		ItemStack plantStack = this.plant;
		this.plant = null;
		this.markForUpdate();
		return plantStack;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);

		if (compound.hasKey("Plant")) {
			this.plant = ItemStack.loadItemStackFromNBT(compound.getCompoundTag("Plant"));
		} else {
			this.plant = null;
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);

		if (this.hasPlant()) {
			NBTTagCompound plantTag = new NBTTagCompound();
			this.plant.writeToNBT(plantTag);
			compound.setTag("Plant", plantTag);
		} else {
			compound.removeTag("Plant");
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
		this.refreshRender();
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

	private void refreshRender() {
		if (this.world != null && this.world.isRemote && this.pos != null) {
			this.world.markBlockRangeForRenderUpdate(this.pos, this.pos);
		}
	}
}
