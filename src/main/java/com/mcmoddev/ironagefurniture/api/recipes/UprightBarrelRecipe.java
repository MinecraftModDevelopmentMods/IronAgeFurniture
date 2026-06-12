package com.mcmoddev.ironagefurniture.api.recipes;

import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class UprightBarrelRecipe implements IRecipe {
	private final Block sideBarrel;
	private final Block barrel;
	private final ItemStack output;

	public UprightBarrelRecipe(Block sideBarrel, Block barrel) {
		this.sideBarrel = sideBarrel;
		this.barrel = barrel;
		this.output = new ItemStack(barrel, 1);
	}

	@Override
	public boolean matches(InventoryCrafting inv, World worldIn) {
		return this.findSideBarrelStack(inv) != null;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		ItemStack sideBarrelStack = this.findSideBarrelStack(inv);

		if (sideBarrelStack == null) {
			return null;
		}

		ItemStack result = new ItemStack(this.barrel, 1);
		if (sideBarrelStack.hasTagCompound()) {
			result.setTagCompound((NBTTagCompound)sideBarrelStack.getTagCompound().copy());
		}
		return result;
	}

	@Override
	public int getRecipeSize() {
		return 1;
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

			if (stack == null) {
				continue;
			}

			if (stack.getItem().hasContainerItem(stack)) {
				remaining[i] = stack.getItem().getContainerItem(stack);
			} else if (stack.getItem() == Item.getItemFromBlock(this.sideBarrel)) {
				remaining[i] = new ItemStack(Items.STICK, 2);
			}
		}

		return remaining;
	}

	private ItemStack findSideBarrelStack(InventoryCrafting inv) {
		ItemStack sideBarrelStack = null;

		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);

			if (stack == null || stack.stackSize <= 0) {
				continue;
			}

			if (stack.getItem() == Item.getItemFromBlock(this.sideBarrel)) {
				if (sideBarrelStack != null) {
					return null;
				}
				sideBarrelStack = stack;
			} else {
				return null;
			}
		}

		return sideBarrelStack;
	}
}
