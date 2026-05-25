package com.mcmoddev.ironagefurniture.api.tile;

import com.mcmoddev.ironagefurniture.api.Blocks.SurfaceDisplayBlocker;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityDiningTable extends TileEntity {
	private ItemStack displayedItem;
	private int blockedConnections;

	public boolean hasDisplayedItem() {
		return this.displayedItem != null && this.displayedItem.stackSize > 0;
	}

	public boolean hasBlockedConnections() {
		return (this.blockedConnections & 15) != 0;
	}

	public boolean hasStoredData() {
		return this.hasDisplayedItem() || this.hasBlockedConnections();
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
		SurfaceDisplayBlocker.release(worldIn, pos);

		if (itemStack != null) {
			net.minecraft.block.Block.spawnAsEntity(worldIn, pos, itemStack);
		}
	}

	public boolean isConnectionBlocked(net.minecraft.util.EnumFacing direction) {
		return (this.blockedConnections & this.directionToMask(direction)) != 0;
	}

	public void setConnectionBlocked(net.minecraft.util.EnumFacing direction, boolean blocked) {
		int mask = this.directionToMask(direction);

		if (mask == 0) {
			return;
		}

		int oldBlockedConnections = this.blockedConnections;

		if (blocked) {
			this.blockedConnections |= mask;
		} else {
			this.blockedConnections &= ~mask;
		}

		if (this.blockedConnections != oldBlockedConnections) {
			this.markForUpdate();
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

		this.blockedConnections = compound.getInteger("BlockedConnections") & 15;
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

		compound.setInteger("BlockedConnections", this.blockedConnections & 15);

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

	private int directionToMask(net.minecraft.util.EnumFacing direction) {
		switch (direction) {
		case NORTH:
			return 1;
		case EAST:
			return 2;
		case SOUTH:
			return 4;
		case WEST:
			return 8;
		default:
			return 0;
		}
	}
}
