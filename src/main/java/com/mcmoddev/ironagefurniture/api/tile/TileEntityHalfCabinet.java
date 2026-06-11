package com.mcmoddev.ironagefurniture.api.tile;

public class TileEntityHalfCabinet extends TileEntityCabinet {
	private static final int INVENTORY_SIZE = 18;

	@Override
	protected int getInventorySize() {
		return INVENTORY_SIZE;
	}

	@Override
	public String getName() {
		return this.hasCustomName() ? super.getName() : "container.ironagefurniture.half_cabinet";
	}
}
