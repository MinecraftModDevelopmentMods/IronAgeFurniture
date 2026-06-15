package com.mcmoddev.ironagefurniture.api.recipes;

import com.mcmoddev.ironagefurniture.api.tile.TileEntityBarrel;

import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

public class FoudreRecipe implements IRecipe {
	private static final int[] BARREL_SLOTS = new int[] { 0, 1, 2, 3, 5, 6, 7, 8 };

	private final Block sourceBarrel;
	private final Block foudre;
	private final ItemStack output;

	public FoudreRecipe(Block sourceBarrel, Block foudre) {
		this.sourceBarrel = sourceBarrel;
		this.foudre = foudre;
		this.output = new ItemStack(foudre, 1);
	}

	@Override
	public boolean matches(InventoryCrafting inv, World worldIn) {
		return this.matchesRing(inv);
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		return this.matchesRing(inv) ? new ItemStack(this.foudre, 1) : null;
	}

	@Override
	public int getRecipeSize() {
		return 9;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return this.output;
	}

	@Override
	public ItemStack[] getRemainingItems(InventoryCrafting inv) {
		ItemStack[] remaining = new ItemStack[inv.getSizeInventory()];

		for (int i = 0; i < remaining.length; i++) {
			ItemStack stack = inv.getStackInSlot(i);

			if (stack != null && stack.getItem().hasContainerItem(stack)) {
				remaining[i] = stack.getItem().getContainerItem(stack);
			}
		}

		return remaining;
	}

	private boolean matchesRing(InventoryCrafting inv) {
		if (inv.getSizeInventory() < 9 || !this.isSign(inv.getStackInSlot(4))) {
			return false;
		}

		for (int slot : BARREL_SLOTS) {
			ItemStack stack = inv.getStackInSlot(slot);

			if (!this.isEmptySourceBarrel(stack)) {
				return false;
			}
		}

		for (int slot = 9; slot < inv.getSizeInventory(); slot++) {
			ItemStack stack = inv.getStackInSlot(slot);

			if (stack != null && stack.stackSize > 0) {
				return false;
			}
		}

		return true;
	}

	private boolean isEmptySourceBarrel(ItemStack stack) {
		return stack != null
			&& stack.stackSize > 0
			&& stack.getItem() == Item.getItemFromBlock(this.sourceBarrel)
			&& TileEntityBarrel.getFluidFromItemStack(stack) == null;
	}

	private boolean isSign(ItemStack stack) {
		return stack != null && stack.stackSize > 0 && stack.getItem() == Items.SIGN;
	}
}
