package com.mcmoddev.ironagefurniture.api.container;

import com.mcmoddev.ironagefurniture.ItemObjectHolder;
import com.mcmoddev.ironagefurniture.api.DrinkProperties;
import com.mcmoddev.ironagefurniture.api.Items.ItemDrinkware;
import com.mcmoddev.ironagefurniture.api.Items.ItemDrinkware.MaterialType;
import com.mcmoddev.ironagefurniture.api.Items.ItemDrinkware.VesselType;
import com.mcmoddev.ironagefurniture.api.tile.TileEntityHangingInnSign;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerInnkeeper extends Container {
	public static final int FIELD_PURSE = 0;
	public static final int ACTION_SELL = 0;
	public static final int ACTION_BUY_BOTTLES = 1;
	public static final int ACTION_BUY_GLASSES = 2;
	public static final int ACTION_BUY_MUGS = 3;
	private static final int SALE_SLOTS = 4;

	private final TileEntityHangingInnSign sign;
	private final InventoryBasic appraisal = new InventoryBasic("Innkeeper Appraisal", false, SALE_SLOTS);
	private int clientPurse;
	private int lastPurse = -1;

	public ContainerInnkeeper(InventoryPlayer playerInventory, TileEntityHangingInnSign sign) {
		this.sign = sign;
		for (int i = 0; i < SALE_SLOTS; i++) {
			this.addSlotToContainer(new AppraisalSlot(this.appraisal, i, 52 + i * 18, 42));
		}
		for (int row = 0; row < 3; row++) {
			for (int col = 0; col < 9; col++) {
				this.addSlotToContainer(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 138 + row * 18));
			}
		}
		for (int col = 0; col < 9; col++) this.addSlotToContainer(new Slot(playerInventory, col, 8 + col * 18, 198));
	}

	public TileEntityHangingInnSign getSign() { return this.sign; }
	public int getPurse() { return this.sign.getWorld() != null && this.sign.getWorld().isRemote ? this.clientPurse : this.sign.getPurse(); }

	public int getPreviewUnits() {
		long total = 0;
		for (int i = 0; i < SALE_SLOTS; i++) total += DrinkProperties.getStackValueUnits(this.appraisal.getStackInSlot(i));
		return (int)Math.min(Integer.MAX_VALUE, total);
	}

	public void performAction(EntityPlayer player, int action) {
		if (action == ACTION_SELL) sell(player);
		else if (action == ACTION_BUY_BOTTLES) buy(player, new ItemStack(Items.GLASS_BOTTLE, 8), 1);
		else if (action == ACTION_BUY_GLASSES && ItemObjectHolder.drinkware != null) {
			buy(player, new ItemStack(ItemObjectHolder.drinkware, 4,
				ItemDrinkware.getMetadata(VesselType.WINE_GLASS, MaterialType.GLASS, 0)), 1);
		} else if (action == ACTION_BUY_MUGS && ItemObjectHolder.drinkware != null) {
			buy(player, new ItemStack(ItemObjectHolder.drinkware, 4,
				ItemDrinkware.getMetadata(VesselType.MUG, MaterialType.CLAY, 0)), 1);
		}
	}

	private void sell(EntityPlayer player) {
		int units = this.getPreviewUnits();
		int emeralds = units / 1000;
		if (emeralds < 1 || emeralds > this.sign.getPurse()) return;
		for (int i = 0; i < SALE_SLOTS; i++) {
			ItemStack stack = this.appraisal.getStackInSlot(i);
			if (stack != null && !DrinkProperties.isAppraisable(stack)) return;
		}
		if (!this.sign.spendPurse(emeralds)) return;
		for (int i = 0; i < SALE_SLOTS; i++) {
			ItemStack stack = this.appraisal.removeStackFromSlot(i);
			ItemStack returned = DrinkProperties.createEmptyReturn(stack);
			if (returned != null && !player.inventory.addItemStackToInventory(returned)) player.dropItem(returned, false);
		}
		ItemStack payment = new ItemStack(Items.EMERALD, emeralds);
		if (!player.inventory.addItemStackToInventory(payment)) player.dropItem(payment, false);
		this.detectAndSendChanges();
	}

	private void buy(EntityPlayer player, ItemStack goods, int cost) {
		if (!removeEmeralds(player.inventory, cost)) return;
		if (!player.inventory.addItemStackToInventory(goods)) player.dropItem(goods, false);
	}

	private static boolean removeEmeralds(InventoryPlayer inventory, int count) {
		int available = 0;
		for (ItemStack stack : inventory.mainInventory) if (stack != null && stack.getItem() == Items.EMERALD) available += stack.stackSize;
		if (available < count) return false;
		for (int i = 0; i < inventory.mainInventory.length && count > 0; i++) {
			ItemStack stack = inventory.mainInventory[i];
			if (stack == null || stack.getItem() != Items.EMERALD) continue;
			int removed = Math.min(count, stack.stackSize);
			stack.stackSize -= removed;
			count -= removed;
			if (stack.stackSize <= 0) inventory.mainInventory[i] = null;
		}
		inventory.markDirty();
		return true;
	}

	@Override public boolean canInteractWith(EntityPlayer player) { return this.sign.isUsableByPlayer(player); }

	@Override
	public void onContainerClosed(EntityPlayer player) {
		super.onContainerClosed(player);
		for (int i = 0; i < SALE_SLOTS; i++) {
			ItemStack stack = this.appraisal.removeStackFromSlot(i);
			if (stack != null && !player.inventory.addItemStackToInventory(stack)) player.dropItem(stack, false);
		}
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		int purse = this.sign.getPurse();
		if (purse != this.lastPurse) {
			for (IContainerListener listener : this.listeners) listener.sendProgressBarUpdate(this, FIELD_PURSE, purse);
			this.lastPurse = purse;
		}
	}

	@Override public void updateProgressBar(int id, int data) { if (id == FIELD_PURSE) this.clientPurse = data; }

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int index) {
		ItemStack result = null;
		Slot slot = this.inventorySlots.get(index);
		if (slot != null && slot.getHasStack()) {
			ItemStack stack = slot.getStack();
			result = stack.copy();
			if (index < SALE_SLOTS) {
				if (!this.mergeItemStack(stack, SALE_SLOTS, this.inventorySlots.size(), true)) return null;
			} else if (DrinkProperties.isAppraisable(stack)) {
				if (!this.mergeItemStack(stack, 0, SALE_SLOTS, false)) return null;
			} else return null;
			if (stack.stackSize == 0) slot.putStack(null); else slot.onSlotChanged();
		}
		return result;
	}

	private static final class AppraisalSlot extends Slot {
		private AppraisalSlot(InventoryBasic inventory, int index, int x, int y) { super(inventory, index, x, y); }
		@Override public boolean isItemValid(ItemStack stack) { return DrinkProperties.isAppraisable(stack); }
	}
}
