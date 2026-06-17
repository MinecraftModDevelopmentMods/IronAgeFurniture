package com.mcmoddev.ironagefurniture.api.Items;

import java.util.List;

import com.mcmoddev.ironagefurniture.api.DrinkDisplayHelper;
import com.mcmoddev.ironagefurniture.api.tile.TileEntityPotStill;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class ItemBlockPotStill extends ItemBlock {
	public ItemBlockPotStill(Block block) {
		super(block);
		this.setMaxStackSize(1);
	}

	@Override
	public int getItemStackLimit(ItemStack stack) {
		return 1;
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
		this.addTankTooltip("Input", TileEntityPotStill.getInputFromItemStack(stack),
			TileEntityPotStill.INPUT_CAPACITY, tooltip);
		this.addTankTooltip("Output", TileEntityPotStill.getOutputFromItemStack(stack),
			TileEntityPotStill.OUTPUT_CAPACITY, tooltip);
	}

	private void addTankTooltip(String label, FluidStack fluid, int capacity, List<String> tooltip) {
		if (fluid == null || fluid.amount <= 0 || fluid.getFluid() == null) {
			return;
		}

		tooltip.add(label + ": " + DrinkDisplayHelper.getDisplayName(fluid));
		for (String line : DrinkDisplayHelper.getQualityTooltipLines(fluid)) {
			if (!line.isEmpty()) {
				tooltip.add(line);
			}
		}

		tooltip.add(fluid.amount + " / " + capacity + " mB");
	}
}
