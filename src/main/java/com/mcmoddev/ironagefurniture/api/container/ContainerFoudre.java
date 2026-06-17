package com.mcmoddev.ironagefurniture.api.container;

import com.mcmoddev.ironagefurniture.api.tile.TileEntityFoudre;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerFoudre extends Container {
	private static final int DISPLAY_PROGRESS_TOTAL = 100;
	private static final int MAX_IN_PROGRESS_PERCENT = 99;
	private static final int FOUDRE_SLOT_COUNT = TileEntityFoudre.INGREDIENT_SLOTS;
	private static final int PLAYER_INVENTORY_Y = 134;
	private static final int PLAYER_HOTBAR_Y = 192;

	private final TileEntityFoudre foudre;
	private int lastBrewTime;
	private int lastBrewTimeTotal;
	private int lastAgeProgress;
	private int lastAgeProgressTotal;
	private int lastSealed;

	public ContainerFoudre(InventoryPlayer playerInventory, TileEntityFoudre foudre) {
		this.foudre = foudre;
		this.addFoudreSlots();
		this.addPlayerInventory(playerInventory);
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return this.foudre != null && this.foudre.isUsableByPlayer(playerIn);
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();

		int brewTime = this.getDisplayProgress(this.foudre.getBrewTime(), this.foudre.getBrewTimeTotal());
		int brewTimeTotal = this.getDisplayProgressTotal(this.foudre.getBrewTimeTotal());
		int ageProgress = this.getDisplayProgress(this.foudre.getAgeProgress(), this.foudre.getAgeProgressTotal());
		int ageProgressTotal = this.getDisplayProgressTotal(this.foudre.getAgeProgressTotal());

		for (int i = 0; i < this.listeners.size(); i++) {
			IContainerListener listener = (IContainerListener)this.listeners.get(i);

			if (this.lastBrewTime != brewTime) {
				listener.sendProgressBarUpdate(this, TileEntityFoudre.FIELD_BREW_TIME, brewTime);
			}

			if (this.lastBrewTimeTotal != brewTimeTotal) {
				listener.sendProgressBarUpdate(this, TileEntityFoudre.FIELD_BREW_TIME_TOTAL, brewTimeTotal);
			}

			if (this.lastAgeProgress != ageProgress) {
				listener.sendProgressBarUpdate(this, TileEntityFoudre.FIELD_AGE_PROGRESS, ageProgress);
			}

			if (this.lastAgeProgressTotal != ageProgressTotal) {
				listener.sendProgressBarUpdate(this, TileEntityFoudre.FIELD_AGE_PROGRESS_TOTAL, ageProgressTotal);
			}

			if (this.lastSealed != (this.foudre.isSealed() ? 1 : 0)) {
				listener.sendProgressBarUpdate(this, TileEntityFoudre.FIELD_SEALED,
					this.foudre.isSealed() ? 1 : 0);
			}
		}

		this.lastBrewTime = brewTime;
		this.lastBrewTimeTotal = brewTimeTotal;
		this.lastAgeProgress = ageProgress;
		this.lastAgeProgressTotal = ageProgressTotal;
		this.lastSealed = this.foudre.isSealed() ? 1 : 0;
	}

	@Override
	public void updateProgressBar(int id, int data) {
		this.foudre.setField(id, data);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
		if (this.foudre.isSealed()) {
			return null;
		}

		ItemStack moved = null;
		Slot slot = (Slot)this.inventorySlots.get(index);

		if (slot == null || !slot.getHasStack()) {
			return null;
		}

		ItemStack stack = slot.getStack();
		moved = stack.copy();

		if (index < FOUDRE_SLOT_COUNT) {
			if (!this.mergeItemStack(stack, FOUDRE_SLOT_COUNT, this.inventorySlots.size(), true)) {
				return null;
			}
		} else if (this.foudre.isItemValidForSlot(0, stack)) {
			if (!this.mergeItemStack(stack, 0, FOUDRE_SLOT_COUNT, false)) {
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

	private void addFoudreSlots() {
		this.addSlotToContainer(new FoudreIngredientSlot(this.foudre, 0, 76, 42));
		this.addSlotToContainer(new FoudreIngredientSlot(this.foudre, 1, 94, 42));
		this.addSlotToContainer(new FoudreIngredientSlot(this.foudre, 2, 76, 60));
		this.addSlotToContainer(new FoudreIngredientSlot(this.foudre, 3, 94, 60));
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

	private int getDisplayProgress(int progress, int total) {
		if (total <= 0) {
			return 0;
		}

		return Math.min(MAX_IN_PROGRESS_PERCENT, Math.max(0, progress * DISPLAY_PROGRESS_TOTAL / total));
	}

	private int getDisplayProgressTotal(int total) {
		return total > 0 ? DISPLAY_PROGRESS_TOTAL : 0;
	}

	private static class FoudreIngredientSlot extends Slot {
		FoudreIngredientSlot(TileEntityFoudre inventory, int index, int xPosition, int yPosition) {
			super(inventory, index, xPosition, yPosition);
		}

		@Override
		public boolean isItemValid(ItemStack stack) {
			return this.inventory.isItemValidForSlot(this.getSlotIndex(), stack);
		}

		@Override
		public boolean canTakeStack(EntityPlayer playerIn) {
			return !((TileEntityFoudre)this.inventory).isSealed();
		}
	}
}
