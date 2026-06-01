package com.mcmoddev.ironagefurniture.api.tile;

import com.mcmoddev.ironagefurniture.api.Blocks.DiningTable.TableEmbeddedContent;
import com.mcmoddev.ironagefurniture.api.Blocks.SurfaceDisplayBlocker;
import com.mcmoddev.ironagefurniture.api.VasePlantHelper;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityDiningTable extends TileEntity {
	private ItemStack displayedItem;
	private ItemStack embeddedItem;
	private ItemStack embeddedFlowerPotPlant;
	private TableEmbeddedContent embeddedContent = TableEmbeddedContent.NONE;
	private int blockedConnections;

	public boolean hasDisplayedItem() {
		return this.displayedItem != null && this.displayedItem.stackSize > 0;
	}

	public boolean hasBlockedConnections() {
		return (this.blockedConnections & 15) != 0;
	}

	public boolean hasStoredData() {
		return this.hasDisplayedItem() || this.hasEmbeddedContent() || this.hasBlockedConnections();
	}

	public ItemStack getDisplayedItem() {
		return this.displayedItem;
	}

	public boolean hasEmbeddedContent() {
		return this.embeddedContent != TableEmbeddedContent.NONE
			&& this.embeddedItem != null
			&& this.embeddedItem.stackSize > 0;
	}

	public TableEmbeddedContent getEmbeddedContent() {
		return this.hasEmbeddedContent() ? this.embeddedContent : TableEmbeddedContent.NONE;
	}

	public int getEmbeddedLightLevel() {
		return this.getEmbeddedContent().getLightLevel();
	}

	public ItemStack getEmbeddedItem() {
		return this.embeddedItem;
	}

	public ItemStack getEmbeddedFlowerPotPlant() {
		return this.embeddedFlowerPotPlant;
	}

	public boolean hasEmbeddedFlowerPotPlant() {
		return this.embeddedFlowerPotPlant != null && this.embeddedFlowerPotPlant.stackSize > 0;
	}

	public boolean canSetEmbeddedContent(TableEmbeddedContent content) {
		return content != null && content != TableEmbeddedContent.NONE
			&& !this.hasDisplayedItem()
			&& !this.hasEmbeddedContent();
	}

	public boolean setEmbeddedContent(TableEmbeddedContent content, ItemStack itemStack) {
		if (!this.canSetEmbeddedContent(content) || itemStack == null || itemStack.stackSize <= 0) {
			return false;
		}

		int oldLight = this.getEmbeddedLightLevel();
		ItemStack storedStack = itemStack.copy();
		storedStack.stackSize = 1;
		this.embeddedContent = content;
		this.embeddedItem = storedStack;
		this.markForUpdate(oldLight != this.getEmbeddedLightLevel());
		return true;
	}

	public ItemStack removeEmbeddedItem() {
		int oldLight = this.getEmbeddedLightLevel();
		ItemStack itemStack = this.embeddedItem;
		this.embeddedItem = null;
		this.embeddedFlowerPotPlant = null;
		this.embeddedContent = TableEmbeddedContent.NONE;
		this.markForUpdate(oldLight != this.getEmbeddedLightLevel());
		return itemStack;
	}

	public boolean setEmbeddedFlowerPotPlant(ItemStack itemStack) {
		if (this.getEmbeddedContent() != TableEmbeddedContent.FLOWER_POT || this.hasEmbeddedFlowerPotPlant()
				|| itemStack == null || itemStack.stackSize <= 0) {
			return false;
		}

		this.embeddedFlowerPotPlant = itemStack.copy();
		this.embeddedFlowerPotPlant.stackSize = 1;
		this.markForUpdate();
		return true;
	}

	public ItemStack removeEmbeddedFlowerPotPlant() {
		ItemStack itemStack = this.embeddedFlowerPotPlant;
		this.embeddedFlowerPotPlant = null;
		this.markForUpdate();
		return itemStack;
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
			ItemStack vasePlant = VasePlantHelper.removePlant(itemStack);

			if (vasePlant != null) {
				net.minecraft.block.Block.spawnAsEntity(worldIn, pos, vasePlant);
			}

			net.minecraft.block.Block.spawnAsEntity(worldIn, pos, itemStack);
		}
	}

	public void dropEmbeddedItem(World worldIn, BlockPos pos) {
		ItemStack plantStack = this.removeEmbeddedFlowerPotPlant();

		if (plantStack != null) {
			net.minecraft.block.Block.spawnAsEntity(worldIn, pos, plantStack);
		}

		ItemStack itemStack = this.removeEmbeddedItem();

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

		if (compound.hasKey("EmbeddedContent") && compound.hasKey("EmbeddedItem")) {
			this.embeddedContent = TableEmbeddedContent.byName(compound.getString("EmbeddedContent"));
			this.embeddedItem = ItemStack.loadItemStackFromNBT(compound.getCompoundTag("EmbeddedItem"));

			if (!this.hasEmbeddedContent()) {
				this.embeddedContent = TableEmbeddedContent.NONE;
				this.embeddedItem = null;
			}
		} else {
			this.embeddedContent = TableEmbeddedContent.NONE;
			this.embeddedItem = null;
		}

		if (compound.hasKey("EmbeddedFlowerPotPlant")) {
			this.embeddedFlowerPotPlant = ItemStack.loadItemStackFromNBT(
				compound.getCompoundTag("EmbeddedFlowerPotPlant"));
		} else {
			this.embeddedFlowerPotPlant = null;
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

		if (this.hasEmbeddedContent()) {
			NBTTagCompound itemTag = new NBTTagCompound();
			this.embeddedItem.writeToNBT(itemTag);
			compound.setString("EmbeddedContent", this.embeddedContent.getName());
			compound.setTag("EmbeddedItem", itemTag);

			if (this.hasEmbeddedFlowerPotPlant()) {
				NBTTagCompound plantTag = new NBTTagCompound();
				this.embeddedFlowerPotPlant.writeToNBT(plantTag);
				compound.setTag("EmbeddedFlowerPotPlant", plantTag);
			} else {
				compound.removeTag("EmbeddedFlowerPotPlant");
			}
		} else {
			compound.removeTag("EmbeddedContent");
			compound.removeTag("EmbeddedItem");
			compound.removeTag("EmbeddedFlowerPotPlant");
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
		int oldLight = this.getEmbeddedLightLevel();
		this.readFromNBT(pkt.getNbtCompound());
		this.refreshRender();

		if (oldLight != this.getEmbeddedLightLevel()) {
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

	private void refreshRender() {
		if (this.world != null && this.world.isRemote && this.pos != null) {
			this.world.markBlockRangeForRenderUpdate(this.pos, this.pos);
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
