package com.mcmoddev.ironagefurniture.init;

import com.mcmoddev.ironagefurniture.api.tile.TileEntityDiningTable;
import com.mcmoddev.ironagefurniture.client.render.TileEntityDiningTableRenderer;

import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ClientRenderInitialiser {
	protected ClientRenderInitialiser() {
		throw new IllegalAccessError("This class cannot be instansiated");
	}

	public static void RegisterTileEntityRenderers() {
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDiningTable.class, new TileEntityDiningTableRenderer());
	}
}
