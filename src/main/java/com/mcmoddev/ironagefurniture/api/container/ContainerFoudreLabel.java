package com.mcmoddev.ironagefurniture.api.container;

import com.mcmoddev.ironagefurniture.api.tile.TileEntityFoudre;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class ContainerFoudreLabel extends Container {
	private final TileEntityFoudre foudre;

	public ContainerFoudreLabel(TileEntityFoudre foudre) {
		this.foudre = foudre;
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return this.foudre != null && this.foudre.isUsableByPlayer(playerIn);
	}
}
