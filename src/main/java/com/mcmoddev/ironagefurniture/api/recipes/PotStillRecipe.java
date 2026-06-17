package com.mcmoddev.ironagefurniture.api.recipes;

import com.mcmoddev.ironagefurniture.api.tile.TileEntityBarrel;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

public class PotStillRecipe implements IRecipe {
	private final Block sourceFoudre;
	private final Block potStill;
	private final ItemStack output;

	public PotStillRecipe(Block sourceFoudre, Block potStill) {
		this.sourceFoudre = sourceFoudre;
		this.potStill = potStill;
		this.output = new ItemStack(potStill, 1);
	}

	@Override
	public boolean matches(InventoryCrafting inv, World worldIn) {
		return this.matchesPattern(inv);
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		return this.matchesPattern(inv) ? new ItemStack(this.potStill, 1) : null;
	}

	@Override
	public int getRecipeSize() {
		return 2;
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

	private boolean matchesPattern(InventoryCrafting inv) {
		boolean foundFoudre = false;
		boolean foundFurnace = false;

		for (int slot = 0; slot < inv.getSizeInventory(); slot++) {
			ItemStack stack = inv.getStackInSlot(slot);

			if (stack == null || stack.stackSize <= 0) {
				continue;
			}

			if (!foundFoudre && this.isEmptySourceFoudre(stack)) {
				foundFoudre = true;
				continue;
			}

			if (!foundFurnace && this.isFurnace(stack)) {
				foundFurnace = true;
				continue;
			}

			return false;
		}

		return foundFoudre && foundFurnace;
	}

	private boolean isEmptySourceFoudre(ItemStack stack) {
		return stack != null
			&& stack.stackSize > 0
			&& stack.getItem() == Item.getItemFromBlock(this.sourceFoudre)
			&& TileEntityBarrel.getFluidFromItemStack(stack) == null;
	}

	private boolean isFurnace(ItemStack stack) {
		return stack != null && stack.stackSize > 0 && stack.getItem() == Item.getItemFromBlock(Blocks.FURNACE);
	}
}
