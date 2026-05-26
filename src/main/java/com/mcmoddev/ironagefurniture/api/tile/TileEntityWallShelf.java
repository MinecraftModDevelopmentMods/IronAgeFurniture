package com.mcmoddev.ironagefurniture.api.tile;

import com.mcmoddev.ironagefurniture.api.Blocks.SurfaceDisplayBlocker;
import com.mcmoddev.ironagefurniture.api.Blocks.WallShelf.ShelfLamp;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityWallShelf extends TileEntity {
	private ItemStack displayedItem;
	private ShelfLamp embeddedLamp = ShelfLamp.NONE;

	public boolean hasDisplayedItem() {
		return this.displayedItem != null && this.displayedItem.stackSize > 0;
	}

	public ItemStack getDisplayedItem() {
		return this.displayedItem;
	}

	public boolean hasStoredData() {
		return this.hasDisplayedItem() || this.hasEmbeddedLamp();
	}

	public boolean hasEmbeddedLamp() {
		return this.embeddedLamp != ShelfLamp.NONE;
	}

	public ShelfLamp getEmbeddedLamp() {
		return this.embeddedLamp;
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

	public void setEmbeddedLamp(ShelfLamp embeddedLamp) {
		ShelfLamp oldLamp = this.embeddedLamp;
		this.embeddedLamp = embeddedLamp == null ? ShelfLamp.NONE : embeddedLamp;
		this.markForUpdate(oldLamp != this.embeddedLamp);
	}

	public ShelfLamp removeEmbeddedLamp() {
		ShelfLamp lamp = this.embeddedLamp;
		this.embeddedLamp = ShelfLamp.NONE;
		this.markForUpdate(lamp != this.embeddedLamp);
		return lamp;
	}

	public void dropDisplayedItem(World worldIn, BlockPos pos) {
		ItemStack itemStack = this.removeDisplayedItem();
		SurfaceDisplayBlocker.release(worldIn, pos);

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

		if (compound.hasKey("EmbeddedLamp")) {
			this.embeddedLamp = ShelfLamp.byName(compound.getString("EmbeddedLamp"));
		} else {
			this.embeddedLamp = ShelfLamp.NONE;
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

		if (this.hasEmbeddedLamp()) {
			compound.setString("EmbeddedLamp", this.embeddedLamp.getName());
		} else {
			compound.removeTag("EmbeddedLamp");
		}

		return compound;
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(this.pos, 0, this.getUpdateTag());
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		ShelfLamp oldLamp = this.embeddedLamp;
		this.readFromNBT(pkt.getNbtCompound());

		if (oldLamp != this.embeddedLamp) {
			this.refreshLighting();
		}
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		return this.writeToNBT(new NBTTagCompound());
	}

	private void markForUpdate() {
		this.markForUpdate(false);
	}

	private void markForUpdate(boolean lightChanged) {
		this.markDirty();

		if (this.world != null && !this.world.isRemote) {
			this.world.notifyBlockUpdate(this.pos, this.world.getBlockState(this.pos),
				this.world.getBlockState(this.pos), 3);
		}

		if (lightChanged) {
			this.refreshLighting();
		}
	}

	private void refreshLighting() {
		if (this.world == null || this.pos == null) {
			return;
		}

		this.world.checkLight(this.pos);

		for (EnumFacing facing : EnumFacing.values()) {
			this.world.checkLight(this.pos.offset(facing));
		}

		if (this.world.isRemote) {
			this.world.markBlockRangeForRenderUpdate(this.pos.add(-15, -15, -15), this.pos.add(15, 15, 15));
		}
	}
}
