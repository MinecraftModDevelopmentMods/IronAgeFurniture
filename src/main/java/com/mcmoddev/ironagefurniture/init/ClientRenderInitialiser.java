package com.mcmoddev.ironagefurniture.init;

import com.mcmoddev.ironagefurniture.BlockObjectHolder;
import com.mcmoddev.ironagefurniture.IronAgeFurnitureConfiguration;
import com.mcmoddev.ironagefurniture.Ironagefurniture;
import com.mcmoddev.ironagefurniture.api.entity.EntityFallingMetalBlock;
import com.mcmoddev.ironagefurniture.api.entity.EntityThrownLavaLamp;
import com.mcmoddev.ironagefurniture.api.tile.TileEntityBottleRack;
import com.mcmoddev.ironagefurniture.api.tile.TileEntityCabinet;
import com.mcmoddev.ironagefurniture.api.tile.TileEntityDiningTable;
import com.mcmoddev.ironagefurniture.api.tile.TileEntityFoudre;
import com.mcmoddev.ironagefurniture.api.tile.TileEntityGlassVase;
import com.mcmoddev.ironagefurniture.api.tile.TileEntityHalfCabinet;
import com.mcmoddev.ironagefurniture.api.tile.TileEntityHangingInnSign;
import com.mcmoddev.ironagefurniture.api.tile.TileEntitySurfaceDisplay;
import com.mcmoddev.ironagefurniture.api.tile.TileEntityWallShelf;
import com.mcmoddev.ironagefurniture.client.gui.HangingInnSignGuiStyler;
import com.mcmoddev.ironagefurniture.client.render.TileEntityBottleRackRenderer;
import com.mcmoddev.ironagefurniture.client.render.TileEntityCabinetRenderer;
import com.mcmoddev.ironagefurniture.client.render.TileEntityDiningTableRenderer;
import com.mcmoddev.ironagefurniture.client.render.TileEntityFoudreRenderer;
import com.mcmoddev.ironagefurniture.client.render.TileEntityGlassVaseRenderer;
import com.mcmoddev.ironagefurniture.client.render.TileEntityHangingInnSignRenderer;
import com.mcmoddev.ironagefurniture.client.render.TileEntitySurfaceDisplayRenderer;
import com.mcmoddev.ironagefurniture.client.render.TileEntityWallShelfRenderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderFallingBlock;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.item.Item;
import net.minecraft.block.state.IBlockState;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class ClientRenderInitialiser {
	protected ClientRenderInitialiser() {
		throw new IllegalAccessError("This class cannot be instansiated");
	}

	public static void RegisterTileEntityRenderers() {
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDiningTable.class, new TileEntityDiningTableRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCabinet.class, new TileEntityCabinetRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityHalfCabinet.class, new TileEntityCabinetRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityGlassVase.class, new TileEntityGlassVaseRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityWallShelf.class, new TileEntityWallShelfRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBottleRack.class, new TileEntityBottleRackRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityFoudre.class, new TileEntityFoudreRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityHangingInnSign.class, new TileEntityHangingInnSignRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySurfaceDisplay.class,
			new TileEntitySurfaceDisplayRenderer());
	}

	public static void RegisterGuiEventHandlers() {
		MinecraftForge.EVENT_BUS.register(new HangingInnSignGuiStyler());
	}

	public static void RegisterBlockStateMappers() {
		if (BlockObjectHolder.hanging_inn_sign == null
				|| !IronAgeFurnitureConfiguration.INTEGRATION_BASEMETALS
				|| !Loader.isModLoaded("basemetals")) {
			return;
		}

		ModelLoader.setCustomStateMapper(BlockObjectHolder.hanging_inn_sign, new StateMapperBase() {
			@Override
			protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
				return new ModelResourceLocation(Ironagefurniture.MODID + ":hanging_inn_sign_adamantine",
					getPropertyString(state.getProperties()));
			}
		});
	}

	public static void RegisterEntityRenderers() {
		if (BlockObjectHolder.light_metal_ironage_block_floor_lava_clear != null) {
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

		if (hasChandelierBlocks()) {
			RenderingRegistry.registerEntityRenderingHandler(EntityFallingMetalBlock.class,
				new IRenderFactory<EntityFallingMetalBlock>() {
					@Override
					public Render<? super EntityFallingMetalBlock> createRenderFor(RenderManager manager) {
						return new RenderFallingBlock(manager);
					}
				});
		}
	}

	private static boolean hasChandelierBlocks() {
		return BlockObjectHolder.chandelier_candle != null
			|| BlockObjectHolder.chandelier_torch != null
			|| BlockObjectHolder.chandelier_glowstone != null
			|| BlockObjectHolder.chandelier_lava != null
			|| BlockObjectHolder.chandelier_redstone != null;
	}
}
