package com.mcmoddev.ironagefurniture.api.recipes;

import com.mcmoddev.ironagefurniture.api.tile.TileEntityBarrel;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

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

	private boolean matchesPattern(InventoryCrafting inv) {
		return inv.getSizeInventory() >= 9
			&& this.isIronNugget(inv.getStackInSlot(0))
			&& this.isGlassBottle(inv.getStackInSlot(1))
			&& this.isIronNugget(inv.getStackInSlot(2))
			&& this.isIronBars(inv.getStackInSlot(3))
			&& this.isEmptySourceFoudre(inv.getStackInSlot(4))
			&& this.isIronBars(inv.getStackInSlot(5))
			&& this.isGlassBottle(inv.getStackInSlot(6))
			&& this.isBucket(inv.getStackInSlot(7))
			&& this.isGlassBottle(inv.getStackInSlot(8))
			&& this.hasNoExtraItems(inv);
	}

	private boolean hasNoExtraItems(InventoryCrafting inv) {
		for (int slot = 9; slot < inv.getSizeInventory(); slot++) {
			ItemStack stack = inv.getStackInSlot(slot);

			if (stack != null && stack.stackSize > 0) {
				return false;
			}
		}

		return true;
	}

	private boolean isEmptySourceFoudre(ItemStack stack) {
		return stack != null
			&& stack.stackSize > 0
			&& stack.getItem() == Item.getItemFromBlock(this.sourceFoudre)
			&& TileEntityBarrel.getFluidFromItemStack(stack) == null;
	}

	private boolean isGlassBottle(ItemStack stack) {
		return stack != null && stack.stackSize > 0 && stack.getItem() == Items.GLASS_BOTTLE;
	}

	private boolean isBucket(ItemStack stack) {
		return stack != null && stack.stackSize > 0 && stack.getItem() == Items.BUCKET;
	}

	private boolean isIronBars(ItemStack stack) {
		return stack != null && stack.stackSize > 0 && stack.getItem() == Item.getItemFromBlock(Blocks.IRON_BARS);
	}

	private boolean isIronNugget(ItemStack stack) {
		if (stack == null || stack.stackSize <= 0) {
			return false;
		}

		for (int id : OreDictionary.getOreIDs(stack)) {
			if ("nuggetIron".equals(OreDictionary.getOreName(id))) {
				return true;
			}
		}

		return false;
	}
}
