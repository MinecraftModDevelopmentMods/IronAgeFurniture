package zone.moddev.mc.ironagefurniture.api.tile;

import javax.annotation.Nullable;

import zone.moddev.mc.ironagefurniture.api.Blocks.Cabinet;
import zone.moddev.mc.ironagefurniture.api.Blocks.SurfaceDisplayBlocker;
import zone.moddev.mc.ironagefurniture.api.VasePlantHelper;
import zone.moddev.mc.ironagefurniture.api.surface.SurfaceSetting;
import zone.moddev.mc.ironagefurniture.api.surface.SurfaceSetting.Slot;
import zone.moddev.mc.ironagefurniture.api.surface.SurfaceSettingHost;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

public class TileEntityCabinet extends TileEntityLockable
		implements ISidedInventory, ITickable, SurfaceSettingHost {
	private static final int INVENTORY_SIZE = 27;

	private ItemStack[] inventory = new ItemStack[this.getInventorySize()];
	private int[] allSlots;
	private String customName;
	private final SurfaceSetting surfaceSetting = new SurfaceSetting();
	private int blockedConnections;
	private EnumFacing verticalJoinDirection;
	public float doorAngle;
	public float prevDoorAngle;
	private int numPlayersUsing;
	private int ticksSinceSync;
	private final IItemHandler[] sidedHandlers = new IItemHandler[EnumFacing.values().length];
	private IItemHandler unsidedHandler;

	protected int getInventorySize() {
		return INVENTORY_SIZE;
	}

	private static int[] createSlotArray(int inventorySize) {
		int[] slots = new int[inventorySize];

		for (int i = 0; i < slots.length; i++) {
			slots[i] = i;
		}

		return slots;
	}

	private int[] getAllSlots() {
		if (this.allSlots == null || this.allSlots.length != this.inventory.length) {
			this.allSlots = createSlotArray(this.inventory.length);
		}

		return this.allSlots;
	}

	@Override
	public int getSizeInventory() {
		return this.inventory.length;
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
		if (player != null && !player.isSpectator()) {
			if (this.numPlayersUsing < 0) {
				this.numPlayersUsing = 0;
			}

			++this.numPlayersUsing;
			this.syncViewerCount();
		}
	}

	@Override
	public void closeInventory(EntityPlayer player) {
		if (player != null && !player.isSpectator() && this.getBlockType() instanceof Cabinet) {
			--this.numPlayersUsing;
			this.syncViewerCount();
		}
	}

	@Override
	public void update() {
		++this.ticksSinceSync;

		if (!this.world.isRemote && this.numPlayersUsing != 0
				&& (this.ticksSinceSync + this.pos.getX() + this.pos.getY() + this.pos.getZ()) % 200 == 0) {
			this.recalculatePlayersUsing();
		}

		boolean wasVisuallyOpen = this.isVisuallyOpen();
		this.prevDoorAngle = this.doorAngle;

		if (this.numPlayersUsing > 0 && this.doorAngle == 0.0F && this.shouldPlayDoorSound()) {
			this.playDoorSound(SoundEvents.BLOCK_CHEST_OPEN);
		}

		if ((this.numPlayersUsing == 0 && this.doorAngle > 0.0F)
				|| (this.numPlayersUsing > 0 && this.doorAngle < 1.0F)) {
			float previousAngle = this.doorAngle;

			if (this.numPlayersUsing > 0) {
				this.doorAngle += 0.1F;
			} else {
				this.doorAngle -= 0.1F;
			}

			if (this.doorAngle > 1.0F) {
				this.doorAngle = 1.0F;
			}
			if (this.doorAngle < 0.0F) {
				this.doorAngle = 0.0F;
			}

			if (this.doorAngle < 0.5F && previousAngle >= 0.5F && this.shouldPlayDoorSound()) {
				this.playDoorSound(SoundEvents.BLOCK_CHEST_CLOSE);
			}
		}

		if (wasVisuallyOpen != this.isVisuallyOpen()) {
			this.refreshRender();
		}
	}

	@Override
	public boolean receiveClientEvent(int id, int type) {
		if (id == 1) {
			this.numPlayersUsing = type;
			this.refreshRender();
			return true;
		}

		return super.receiveClientEvent(id, type);
	}

	public boolean isVisuallyOpen() {
		return this.numPlayersUsing > 0 || this.doorAngle > 0.0F;
	}

	private void syncViewerCount() {
		if (this.world == null || this.pos == null) {
			return;
		}

		this.world.addBlockEvent(this.pos, this.getBlockType(), 1, this.numPlayersUsing);
		this.world.notifyNeighborsOfStateChange(this.pos, this.getBlockType());
		this.world.notifyNeighborsOfStateChange(this.pos.down(), this.getBlockType());
		this.refreshRender();
	}

	private void recalculatePlayersUsing() {
		this.numPlayersUsing = 0;
		float range = 5.0F;
		AxisAlignedBB searchBox = new AxisAlignedBB((double)((float)this.pos.getX() - range),
			(double)((float)this.pos.getY() - range), (double)((float)this.pos.getZ() - range),
			(double)((float)(this.pos.getX() + 1) + range), (double)((float)(this.pos.getY() + 1) + range),
			(double)((float)(this.pos.getZ() + 1) + range));

		for (EntityPlayer player : this.world.getEntitiesWithinAABB(EntityPlayer.class, searchBox)) {
			if (player.openContainer instanceof ContainerChest) {
				IInventory inventory = ((ContainerChest)player.openContainer).getLowerChestInventory();

				if (inventory == this || inventory instanceof InventoryLargeChest
						&& ((InventoryLargeChest)inventory).isPartOfLargeChest(this)) {
					++this.numPlayersUsing;
				}
			}
		}
	}

	private void playDoorSound(SoundEvent soundEvent) {
		BlockPos joinedPos = this.getJoinedCabinetPos();
		double x = (double)this.pos.getX() + 0.5D;
		double y = (double)this.pos.getY() + 0.5D;
		double z = (double)this.pos.getZ() + 0.5D;

		if (joinedPos != null) {
			x = (x + (double)joinedPos.getX() + 0.5D) / 2.0D;
			y = (y + (double)joinedPos.getY() + 0.5D) / 2.0D;
			z = (z + (double)joinedPos.getZ() + 0.5D) / 2.0D;
		}

		this.world.playSound(null, x, y, z, soundEvent, SoundCategory.BLOCKS, 0.5F,
			this.world.rand.nextFloat() * 0.1F + 0.9F);
	}

	private boolean shouldPlayDoorSound() {
		BlockPos joinedPos = this.getJoinedCabinetPos();
		return joinedPos == null || this.comparePositions(this.pos, joinedPos) <= 0;
	}

	private BlockPos getJoinedCabinetPos() {
		if (this.world == null || this.pos == null) {
			return null;
		}

		IBlockState state = this.world.getBlockState(this.pos);

		if (!(state.getBlock() instanceof Cabinet)) {
			return null;
		}

		if (this.verticalJoinDirection == EnumFacing.UP || this.verticalJoinDirection == EnumFacing.DOWN) {
			return this.pos.offset(this.verticalJoinDirection);
		}

		Cabinet.CabinetType type = state.getValue(Cabinet.TYPE);
		EnumFacing facing = state.getValue(Cabinet.FACING);

		if (type == Cabinet.CabinetType.LEFT) {
			return this.pos.offset(facing.rotateYCCW());
		}
		if (type == Cabinet.CabinetType.RIGHT) {
			return this.pos.offset(facing.rotateY());
		}

		return null;
	}

	private int comparePositions(BlockPos first, BlockPos second) {
		if (first.getY() != second.getY()) {
			return first.getY() < second.getY() ? -1 : 1;
		}
		if (first.getZ() != second.getZ()) {
			return first.getZ() < second.getZ() ? -1 : 1;
		}
		if (first.getX() != second.getX()) {
			return first.getX() < second.getX() ? -1 : 1;
		}

		return 0;
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
		return this.getAllSlots();
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
		return this.surfaceSetting.hasAnyItem();
	}

	public ItemStack getDisplayedItem() {
		return this.surfaceSetting.getCompatibilityItem();
	}

	public EnumFacing getDisplayedItemFacing() {
		return this.surfaceSetting.getCompatibilityFacing();
	}

	public void setDisplayedItem(ItemStack displayedItem) {
		this.setDisplayedItem(displayedItem, this.getDisplayedItemFacing());
	}

	public void setDisplayedItem(ItemStack displayedItem, EnumFacing facing) {
		this.surfaceSetting.setSingleItem(displayedItem, facing);
		this.markSurfaceSettingChanged();
	}

	public ItemStack removeDisplayedItem() {
		ItemStack itemStack = this.surfaceSetting.removeCompatibilityItem();
		this.markSurfaceSettingChanged();
		return itemStack;
	}

	@Override
	public SurfaceSetting getSurfaceSetting() {
		return this.surfaceSetting;
	}

	@Override
	public void markSurfaceSettingChanged() {
		this.markForDisplayUpdate();
		this.refreshLighting();
	}

	public void dropDisplayedItem(World worldIn, BlockPos pos) {
		for (Slot slot : Slot.values()) {
			ItemStack itemStack = this.surfaceSetting.remove(slot);

			if (itemStack == null) {
				continue;
			}

			ItemStack vasePlant = VasePlantHelper.removePlant(itemStack);

			if (vasePlant != null) {
				net.minecraft.block.Block.spawnAsEntity(worldIn, pos, vasePlant);
			}

			net.minecraft.block.Block.spawnAsEntity(worldIn, pos, itemStack);
		}

		this.markSurfaceSettingChanged();
		SurfaceDisplayBlocker.release(worldIn, pos);
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

		this.surfaceSetting.readFromNBT(compound);

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

		this.surfaceSetting.writeToNBT(compound);

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

	private void refreshLighting() {
		if (this.world == null || this.pos == null) {
			return;
		}

		this.world.checkLight(this.pos);

		for (EnumFacing facing : EnumFacing.values()) {
			this.world.checkLight(this.pos.offset(facing));
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

}
