package com.mcmoddev.ironagefurniture.api.Items;

import java.util.List;

import com.mcmoddev.ironagefurniture.api.tile.TileEntityBarrel;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class ItemBlockBarrel extends ItemBlock {
	public ItemBlockBarrel(Block block) {
		super(block);
		this.setMaxStackSize(16);
	}

	@Override
	public int getItemStackLimit(ItemStack stack) {
		FluidStack fluid = TileEntityBarrel.getFluidFromItemStack(stack);
		return fluid == null || fluid.amount <= 0 ? 16 : 1;
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
		FluidStack fluid = TileEntityBarrel.getFluidFromItemStack(stack);

		if (fluid == null || fluid.amount <= 0) {
			return;
		}

		tooltip.add(fluid.getLocalizedName());
		tooltip.add(fluid.amount + " / " + TileEntityBarrel.CAPACITY + " mB");
	}
}
