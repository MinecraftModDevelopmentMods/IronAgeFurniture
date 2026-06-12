package com.mcmoddev.ironagefurniture.api.tile;

import net.minecraftforge.fluids.Fluid;

public class TileEntityFoudre extends TileEntityBarrel {
	public static final int CAPACITY = 128 * Fluid.BUCKET_VOLUME;

	public TileEntityFoudre() {
		super(CAPACITY);
	}

	@Override
	public String getContainerNameKey() {
		return "container.ironagefurniture.foudre";
	}
}
