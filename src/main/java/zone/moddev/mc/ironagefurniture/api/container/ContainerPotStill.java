package zone.moddev.mc.ironagefurniture.api.container;

import zone.moddev.mc.ironagefurniture.api.tile.TileEntityPotStill;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerPotStill extends Container {
	private static final int DISPLAY_PROGRESS_TOTAL = 100;
	private static final int MAX_IN_PROGRESS_PERCENT = 99;
	private static final int STILL_SLOT_COUNT = TileEntityPotStill.FUEL_SLOTS;
	private static final int FUEL_SLOT_X = 50;
	private static final int FUEL_SLOT_Y = 115;
	private static final int PLAYER_INVENTORY_Y = 154;
	private static final int PLAYER_HOTBAR_Y = 212;

	private final TileEntityPotStill potStill;
	private int lastDistillTime;
	private int lastDistillTimeTotal;
	private int lastCanStart;
	private int lastActive;
	private int lastInputAmount;
	private int lastOutputAmount;

	public ContainerPotStill(InventoryPlayer playerInventory, TileEntityPotStill potStill) {
		this.potStill = potStill;
		this.addStillSlots();
		this.addPlayerInventory(playerInventory);
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return this.potStill != null && this.potStill.isUsableByPlayer(playerIn);
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();

		int distillTime = this.getDisplayProgress(this.potStill.getDistillTime(),
			this.potStill.getDistillTimeTotal());
		int distillTimeTotal = this.getDisplayProgressTotal(this.potStill.getDistillTimeTotal());

		this.sendFieldIfChanged(TileEntityPotStill.FIELD_DISTILL_TIME, distillTime, this.lastDistillTime);
		this.sendFieldIfChanged(TileEntityPotStill.FIELD_DISTILL_TIME_TOTAL, distillTimeTotal,
			this.lastDistillTimeTotal);
		this.sendFieldIfChanged(TileEntityPotStill.FIELD_CAN_START, this.potStill.canStartDistillation() ? 1 : 0,
			this.lastCanStart);
		this.sendFieldIfChanged(TileEntityPotStill.FIELD_ACTIVE, this.potStill.isDistilling() ? 1 : 0,
			this.lastActive);
		this.sendFieldIfChanged(TileEntityPotStill.FIELD_INPUT_AMOUNT, this.potStill.getInputAmount(),
			this.lastInputAmount);
		this.sendFieldIfChanged(TileEntityPotStill.FIELD_OUTPUT_AMOUNT, this.potStill.getOutputAmount(),
			this.lastOutputAmount);

		this.lastDistillTime = distillTime;
		this.lastDistillTimeTotal = distillTimeTotal;
		this.lastCanStart = this.potStill.canStartDistillation() ? 1 : 0;
		this.lastActive = this.potStill.isDistilling() ? 1 : 0;
		this.lastInputAmount = this.potStill.getInputAmount();
		this.lastOutputAmount = this.potStill.getOutputAmount();
	}

	@Override
	public void updateProgressBar(int id, int data) {
		this.potStill.setField(id, data);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
		if (this.potStill.isDistilling()) {
			return null;
		}

		ItemStack moved = null;
		Slot slot = (Slot)this.inventorySlots.get(index);

		if (slot == null || !slot.getHasStack()) {
			return null;
		}

		ItemStack stack = slot.getStack();
		moved = stack.copy();

		if (index < STILL_SLOT_COUNT) {
			if (!this.mergeItemStack(stack, STILL_SLOT_COUNT, this.inventorySlots.size(), true)) {
				return null;
			}
		} else if (this.potStill.isItemValidForSlot(TileEntityPotStill.FUEL_SLOT, stack)) {
			if (!this.mergeItemStack(stack, TileEntityPotStill.FUEL_SLOT, TileEntityPotStill.FUEL_SLOT + 1, false)) {
				return null;
			}
		} else {
			return null;
		}

		if (stack.stackSize <= 0) {
			slot.putStack(null);
		} else {
			slot.onSlotChanged();
		}

		slot.onPickupFromSlot(playerIn, stack);
		return moved;
	}

	private void sendFieldIfChanged(int field, int value, int previousValue) {
		if (value == previousValue) {
			return;
		}

		for (int i = 0; i < this.listeners.size(); i++) {
			((IContainerListener)this.listeners.get(i)).sendProgressBarUpdate(this, field, value);
		}
	}

	private int getDisplayProgress(int progress, int total) {
		if (total <= 0) {
			return 0;
		}

		return Math.min(MAX_IN_PROGRESS_PERCENT, Math.max(0, progress * DISPLAY_PROGRESS_TOTAL / total));
	}

	private int getDisplayProgressTotal(int total) {
		return total > 0 ? DISPLAY_PROGRESS_TOTAL : 0;
	}

	private void addStillSlots() {
		this.addSlotToContainer(new FuelSlot(this.potStill, TileEntityPotStill.FUEL_SLOT, FUEL_SLOT_X, FUEL_SLOT_Y));
	}

	private void addPlayerInventory(InventoryPlayer playerInventory) {
		for (int row = 0; row < 3; row++) {
			for (int column = 0; column < 9; column++) {
				this.addSlotToContainer(new Slot(playerInventory, column + row * 9 + 9,
					8 + column * 18, PLAYER_INVENTORY_Y + row * 18));
			}
		}

		for (int column = 0; column < 9; column++) {
			this.addSlotToContainer(new Slot(playerInventory, column, 8 + column * 18, PLAYER_HOTBAR_Y));
		}
	}

	private static class FuelSlot extends Slot {
		FuelSlot(TileEntityPotStill inventory, int index, int xPosition, int yPosition) {
			super(inventory, index, xPosition, yPosition);
		}

		@Override
		public boolean isItemValid(ItemStack stack) {
			return this.inventory.isItemValidForSlot(this.getSlotIndex(), stack);
		}

		@Override
		public boolean canTakeStack(EntityPlayer playerIn) {
			return !((TileEntityPotStill)this.inventory).isDistilling();
		}
	}
}
