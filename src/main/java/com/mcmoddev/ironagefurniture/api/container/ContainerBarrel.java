package com.mcmoddev.ironagefurniture.api.container;

import com.mcmoddev.ironagefurniture.api.tile.TileEntityBarrel;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class ContainerBarrel extends Container {
	private final TileEntityBarrel barrel;

	public ContainerBarrel(TileEntityBarrel barrel) {
		this.barrel = barrel;
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return this.barrel != null && this.barrel.isUsableByPlayer(playerIn);
	}
}
