package com.mcmoddev.ironagefurniture.api.Items;

import java.util.List;

import com.mcmoddev.ironagefurniture.api.Blocks.Barrel;
import com.mcmoddev.ironagefurniture.api.tile.TileEntityBarrel;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class ItemBlockBarrel extends ItemBlock {
	public ItemBlockBarrel(Block block) {
		super(block);
		this.setMaxStackSize(this.getEmptyStackLimit());
	}

	@Override
	public int getItemStackLimit(ItemStack stack) {
		FluidStack fluid = TileEntityBarrel.getFluidFromItemStack(stack);
		return fluid == null || fluid.amount <= 0 ? this.getEmptyStackLimit() : 1;
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
		FluidStack fluid = TileEntityBarrel.getFluidFromItemStack(stack);

		if (fluid == null || fluid.amount <= 0) {
			return;
		}

		tooltip.add(fluid.getLocalizedName());
		tooltip.add(fluid.amount + " / " + this.getCapacity() + " mB");
	}

	private int getEmptyStackLimit() {
		return this.block instanceof Barrel ? ((Barrel)this.block).getEmptyItemStackLimit() : 16;
	}

	private int getCapacity() {
		return this.block instanceof Barrel ? ((Barrel)this.block).getBarrelCapacity() : TileEntityBarrel.CAPACITY;
	}
}
