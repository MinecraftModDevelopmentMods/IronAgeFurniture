package com.mcmoddev.ironagefurniture.init;

import com.mcmoddev.ironagefurniture.BlockObjectHolder;
import com.mcmoddev.ironagefurniture.api.entity.EntityThrownLavaLamp;
import com.mcmoddev.ironagefurniture.api.tile.TileEntityDiningTable;
import com.mcmoddev.ironagefurniture.api.tile.TileEntityWallShelf;
import com.mcmoddev.ironagefurniture.client.render.TileEntityDiningTableRenderer;
import com.mcmoddev.ironagefurniture.client.render.TileEntityWallShelfRenderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.item.Item;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class ClientRenderInitialiser {
	protected ClientRenderInitialiser() {
		throw new IllegalAccessError("This class cannot be instansiated");
	}

	public static void RegisterTileEntityRenderers() {
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDiningTable.class, new TileEntityDiningTableRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityWallShelf.class, new TileEntityWallShelfRenderer());
	}

	public static void RegisterEntityRenderers() {
		RenderingRegistry.registerEntityRenderingHandler(EntityThrownLavaLamp.class,
			new IRenderFactory<EntityThrownLavaLamp>() {
				@Override
				public Render<? super EntityThrownLavaLamp> createRenderFor(RenderManager manager) {
					return new RenderSnowball<EntityThrownLavaLamp>(manager,
						Item.getItemFromBlock(BlockObjectHolder.light_metal_ironage_block_floor_lava_clear),
						Minecraft.getMinecraft().getRenderItem());
				}
			});
	}
}
