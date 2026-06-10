package com.mcmoddev.ironagefurniture.api.tile;

import javax.annotation.Nullable;

import com.mcmoddev.ironagefurniture.api.Blocks.Cabinet;
import com.mcmoddev.ironagefurniture.api.Blocks.SurfaceDisplayBlocker;
import com.mcmoddev.ironagefurniture.api.VasePlantHelper;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

public class TileEntityCabinet extends TileEntityLockable implements ISidedInventory {
	private static final int INVENTORY_SIZE = 27;
	private static final int[] ALL_SLOTS = createSlotArray();

	private ItemStack[] inventory = new ItemStack[INVENTORY_SIZE];
	private String customName;
	private ItemStack displayedItem;
	private EnumFacing displayedFacing = EnumFacing.NORTH;
	private int blockedConnections;
	private EnumFacing verticalJoinDirection;
	private final IItemHandler[] sidedHandlers = new IItemHandler[EnumFacing.values().length];
	private IItemHandler unsidedHandler;

	private static int[] createSlotArray() {
		int[] slots = new int[INVENTORY_SIZE];

		for (int i = 0; i < slots.length; i++) {
			slots[i] = i;
		}

		return slots;
	}

	@Override
	public int getSizeInventory() {
		return INVENTORY_SIZE;
	}

	@Override
	@Nullable
	public ItemStack getStackInSlot(int index) {
		return this.inventory[index];
	}

	@Override
	@Nullable
	public ItemStack decrStackSize(int index, int count) {
		ItemStack itemStack = ItemStackHelper.getAndSplit(this.inventory, index, count);

		if (itemStack != null) {
			this.markDirty();
		}

		return itemStack;
	}

	@Override
	@Nullable
	public ItemStack removeStackFromSlot(int index) {
		ItemStack itemStack = ItemStackHelper.getAndRemove(this.inventory, index);

		if (itemStack != null) {
			this.markDirty();
		}

		return itemStack;
	}

	@Override
	public void setInventorySlotContents(int index, @Nullable ItemStack stack) {
		this.inventory[index] = stack;

		if (stack != null && stack.stackSize > this.getInventoryStackLimit()) {
			stack.stackSize = this.getInventoryStackLimit();
		}

		this.markDirty();
	}

	@Override
	public String getName() {
		return this.hasCustomName() ? this.customName : "container.ironagefurniture.cabinet";
	}

	@Override
	public boolean hasCustomName() {
		return this.customName != null && !this.customName.isEmpty();
	}

	public void setCustomName(String customName) {
		this.customName = customName;
	}

	@Override
	public String getGuiID() {
		return "minecraft:chest";
	}

	@Override
	public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
		return new ContainerChest(playerInventory, this, playerIn);
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		return this.world.getTileEntity(this.pos) != this ? false
			: player.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D,
				(double)this.pos.getZ() + 0.5D) <= 64.0D;
	}

	@Override
	public void openInventory(EntityPlayer player) {
	}

	@Override
	public void closeInventory(EntityPlayer player) {
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return true;
	}

	@Override
	public int getField(int id) {
		return 0;
	}

	@Override
	public void setField(int id, int value) {
	}

	@Override
	public int getFieldCount() {
		return 0;
	}

	@Override
	public void clear() {
		for (int i = 0; i < this.inventory.length; i++) {
			this.inventory[i] = null;
		}
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		return ALL_SLOTS;
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
		return direction == null || direction != this.getFrontFacing();
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
		return true;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return (T)this.getItemHandler(facing);
		}

		return super.getCapability(capability, facing);
	}

	private IItemHandler getItemHandler(EnumFacing facing) {
		if (facing == null) {
			if (this.unsidedHandler == null) {
				this.unsidedHandler = new InvWrapper(this);
			}

			return this.unsidedHandler;
		}

		int index = facing.ordinal();

		if (this.sidedHandlers[index] == null) {
			this.sidedHandlers[index] = new SidedInvWrapper(this, facing);
		}

		return this.sidedHandlers[index];
	}

	private EnumFacing getFrontFacing() {
		if (this.world == null || this.pos == null) {
			return EnumFacing.NORTH;
		}

		net.minecraft.block.state.IBlockState state = this.world.getBlockState(this.pos);
		return state.getBlock() instanceof Cabinet ? state.getValue(Cabinet.FACING) : EnumFacing.NORTH;
	}

	public boolean hasDisplayedItem() {
		return this.displayedItem != null && this.displayedItem.stackSize > 0;
	}

	public ItemStack getDisplayedItem() {
		return this.displayedItem;
	}

	public EnumFacing getDisplayedItemFacing() {
		return this.displayedFacing;
	}

	public void setDisplayedItem(ItemStack displayedItem) {
		this.setDisplayedItem(displayedItem, this.displayedFacing);
	}

	public void setDisplayedItem(ItemStack displayedItem, EnumFacing facing) {
		this.displayedItem = displayedItem == null ? null : displayedItem.copy();
		this.displayedFacing = this.horizontalOrNorth(facing);
		this.markForDisplayUpdate();
	}

	public ItemStack removeDisplayedItem() {
		ItemStack itemStack = this.displayedItem;
		this.displayedItem = null;
		this.displayedFacing = EnumFacing.NORTH;
		this.markForDisplayUpdate();
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

	public boolean isConnectionBlocked(EnumFacing direction) {
		return (this.blockedConnections & this.directionToMask(direction)) != 0;
	}

	public void setConnectionBlocked(EnumFacing direction, boolean blocked) {
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
			this.markForDisplayUpdate();
		}
	}

	public EnumFacing getVerticalJoinDirection() {
		return this.verticalJoinDirection;
	}

	public void setVerticalJoinDirection(EnumFacing direction) {
		EnumFacing normalizedDirection = direction == EnumFacing.UP || direction == EnumFacing.DOWN ? direction : null;

		if (this.verticalJoinDirection != normalizedDirection) {
			this.verticalJoinDirection = normalizedDirection;
			this.markForDisplayUpdate();
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		this.inventory = new ItemStack[this.getSizeInventory()];
		NBTTagList items = compound.getTagList("Items", 10);

		for (int i = 0; i < items.tagCount(); i++) {
			NBTTagCompound itemTag = items.getCompoundTagAt(i);
			int slot = itemTag.getByte("Slot") & 255;

			if (slot >= 0 && slot < this.inventory.length) {
				this.inventory[slot] = ItemStack.loadItemStackFromNBT(itemTag);
			}
		}

		if (compound.hasKey("CustomName", 8)) {
			this.customName = compound.getString("CustomName");
		} else {
			this.customName = null;
		}

		if (compound.hasKey("DisplayedItem")) {
			this.displayedItem = ItemStack.loadItemStackFromNBT(compound.getCompoundTag("DisplayedItem"));
			this.displayedFacing = compound.hasKey("DisplayedFacing")
				? EnumFacing.getHorizontal(compound.getInteger("DisplayedFacing") & 3)
				: EnumFacing.NORTH;
		} else {
			this.displayedItem = null;
			this.displayedFacing = EnumFacing.NORTH;
		}

		this.blockedConnections = compound.getInteger("BlockedConnections") & 63;
		this.verticalJoinDirection = null;

		if (compound.hasKey("VerticalJoinDirection", 3)) {
			EnumFacing direction = EnumFacing.getFront(compound.getInteger("VerticalJoinDirection"));

			if (direction == EnumFacing.UP || direction == EnumFacing.DOWN) {
				this.verticalJoinDirection = direction;
			}
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		NBTTagList items = new NBTTagList();

		for (int i = 0; i < this.inventory.length; i++) {
			if (this.inventory[i] != null) {
				NBTTagCompound itemTag = new NBTTagCompound();
				itemTag.setByte("Slot", (byte)i);
				this.inventory[i].writeToNBT(itemTag);
				items.appendTag(itemTag);
			}
		}

		compound.setTag("Items", items);

		if (this.hasCustomName()) {
			compound.setString("CustomName", this.customName);
		} else {
			compound.removeTag("CustomName");
		}

		if (this.hasDisplayedItem()) {
			NBTTagCompound itemTag = new NBTTagCompound();
			this.displayedItem.writeToNBT(itemTag);
			compound.setTag("DisplayedItem", itemTag);
			compound.setInteger("DisplayedFacing", this.displayedFacing.getHorizontalIndex());
		} else {
			compound.removeTag("DisplayedItem");
			compound.removeTag("DisplayedFacing");
		}

		compound.setInteger("BlockedConnections", this.blockedConnections & 63);

		if (this.verticalJoinDirection != null) {
			compound.setInteger("VerticalJoinDirection", this.verticalJoinDirection.getIndex());
		} else {
			compound.removeTag("VerticalJoinDirection");
		}

		return compound;
	}

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
		return oldState.getBlock() != newState.getBlock();
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

	private void markForDisplayUpdate() {
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

	private int directionToMask(EnumFacing direction) {
		switch (direction) {
		case NORTH:
			return 1;
		case EAST:
			return 2;
		case SOUTH:
			return 4;
		case WEST:
			return 8;
		case UP:
			return 16;
		case DOWN:
			return 32;
		default:
			return 0;
		}
	}

	private EnumFacing horizontalOrNorth(EnumFacing facing) {
		if (facing == EnumFacing.NORTH || facing == EnumFacing.EAST
				|| facing == EnumFacing.SOUTH || facing == EnumFacing.WEST) {
			return facing;
		}

		return EnumFacing.NORTH;
	}
}
