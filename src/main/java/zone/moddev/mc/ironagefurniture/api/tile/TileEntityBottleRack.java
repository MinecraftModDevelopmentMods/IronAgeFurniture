package zone.moddev.mc.ironagefurniture.api.tile;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityBottleRack extends TileEntity {
	public static final int SLOT_COUNT = 9;

	private final ItemStack[] bottles = new ItemStack[SLOT_COUNT];
	private final byte[] bottleFacings = new byte[SLOT_COUNT];

	public TileEntityBottleRack() {
		this.clearBottles();
	}

	public ItemStack getBottle(int slot) {
		return this.isValidSlot(slot) ? this.bottles[slot] : null;
	}

	public boolean hasBottle(int slot) {
		ItemStack bottle = this.getBottle(slot);
		return bottle != null && bottle.stackSize > 0;
	}

	public boolean hasStoredData() {
		for (int i = 0; i < this.bottles.length; i++) {
			if (this.hasBottle(i)) {
				return true;
			}
		}

		return false;
	}

	public boolean isSlotEmpty(int slot) {
		return this.isValidSlot(slot) && !this.hasBottle(slot);
	}

	public boolean insertBottle(int slot, ItemStack itemStack) {
		return this.insertBottle(slot, itemStack, null);
	}

	public boolean insertBottle(int slot, ItemStack itemStack, EnumFacing facing) {
		if (!this.isValidSlot(slot) || !this.isSlotEmpty(slot) || itemStack == null || itemStack.stackSize <= 0) {
			return false;
		}

		ItemStack storedStack = itemStack.copy();
		storedStack.stackSize = 1;
		this.bottles[slot] = storedStack;
		this.bottleFacings[slot] = this.getFacingByte(facing);
		this.markForUpdate();
		return true;
	}

	public ItemStack removeBottle(int slot) {
		if (!this.isValidSlot(slot)) {
			return null;
		}

		ItemStack itemStack = this.bottles[slot];
		this.bottles[slot] = null;
		this.bottleFacings[slot] = -1;

		if (itemStack != null) {
			this.markForUpdate();
		}

		return itemStack;
	}

	public List<ItemStack> removeAllBottles() {
		List<ItemStack> drops = new ArrayList<ItemStack>();

		for (int i = 0; i < this.bottles.length; i++) {
			ItemStack itemStack = this.bottles[i];

			if (itemStack != null && itemStack.stackSize > 0) {
				drops.add(itemStack);
				this.bottles[i] = null;
				this.bottleFacings[i] = -1;
			}
		}

		this.markForUpdate();
		return drops;
	}

	public void dropBottles(World worldIn, BlockPos pos) {
		for (ItemStack itemStack : this.removeAllBottles()) {
			Block.spawnAsEntity(worldIn, pos, itemStack);
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		this.clearBottles();
		NBTTagList list = compound.getTagList("Bottles", 10);

		for (int i = 0; i < list.tagCount(); i++) {
			NBTTagCompound itemTag = list.getCompoundTagAt(i);
			int slot = itemTag.getByte("Slot") & 255;

			if (this.isValidSlot(slot)) {
				this.bottles[slot] = ItemStack.loadItemStackFromNBT(itemTag);
				this.bottleFacings[slot] = itemTag.hasKey("Facing") && this.isValidFacing(itemTag.getByte("Facing"))
					? itemTag.getByte("Facing") : -1;
			}
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		NBTTagList list = new NBTTagList();

		for (int i = 0; i < this.bottles.length; i++) {
			ItemStack itemStack = this.bottles[i];

			if (itemStack != null && itemStack.stackSize > 0) {
				NBTTagCompound itemTag = new NBTTagCompound();
				itemTag.setByte("Slot", (byte)i);
				if (this.isValidFacing(this.bottleFacings[i])) {
					itemTag.setByte("Facing", this.bottleFacings[i]);
				}
				itemStack.writeToNBT(itemTag);
				list.appendTag(itemTag);
			}
		}

		if (list.tagCount() > 0) {
			compound.setTag("Bottles", list);
		} else {
			compound.removeTag("Bottles");
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

	private boolean isValidSlot(int slot) {
		return slot >= 0 && slot < this.bottles.length;
	}

	public EnumFacing getBottleFacing(int slot, EnumFacing fallback) {
		if (!this.isValidSlot(slot) || !this.isValidFacing(this.bottleFacings[slot])) {
			return fallback;
		}

		return EnumFacing.getHorizontal(this.bottleFacings[slot]);
	}

	private void clearBottles() {
		for (int i = 0; i < this.bottles.length; i++) {
			this.bottles[i] = null;
			this.bottleFacings[i] = -1;
		}
	}

	private byte getFacingByte(EnumFacing facing) {
		return facing != null && facing.getAxis().isHorizontal() ? (byte)facing.getHorizontalIndex() : -1;
	}

	private boolean isValidFacing(byte facing) {
		return facing >= 0 && facing < 4;
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
