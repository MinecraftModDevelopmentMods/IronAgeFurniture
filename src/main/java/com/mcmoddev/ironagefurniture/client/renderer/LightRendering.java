package com.mcmoddev.ironagefurniture.client.renderer;

import com.mcmoddev.ironagefurniture.BlockObjectHolder;
import com.mcmoddev.ironagefurniture.Ironagefurniture;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid = Ironagefurniture.MODID, bus = Bus.MOD, value = Dist.CLIENT)
public class LightRendering {

	@SubscribeEvent
	public static void clientSetup(FMLClientSetupEvent event) {
		ItemBlockRenderTypes.setRenderLayer(BlockObjectHolder.light_metal_ironage_block_floor_glow_clear, RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockObjectHolder.light_metal_ironage_sconce_floor_glow_iron, RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockObjectHolder.light_metal_ironage_sconce_wall_glow_iron, RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockObjectHolder.light_metal_ironage_block_floor_lava_clear, RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockObjectHolder.light_metal_ironage_sconce_floor_lava_iron, RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockObjectHolder.light_metal_ironage_sconce_wall_lava_iron, RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockObjectHolder.light_metal_ironage_sconce_wall_redtorch_iron, RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockObjectHolder.light_metal_ironage_sconce_wall_redtorch_iron_unlit, RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockObjectHolder.light_metal_ironage_sconce_floor_redtorch_iron, RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockObjectHolder.light_metal_ironage_sconce_floor_redtorch_iron_unlit, RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockObjectHolder.light_metal_ironage_block_floor_red_clear, RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockObjectHolder.light_metal_ironage_block_floor_red_clear_one, RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockObjectHolder.light_metal_ironage_block_floor_red_clear_two, RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockObjectHolder.light_metal_ironage_block_floor_red_clear_three, RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockObjectHolder.light_metal_ironage_block_floor_red_clear_four, RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockObjectHolder.light_metal_ironage_block_floor_red_clear_five, RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockObjectHolder.light_metal_ironage_block_floor_red_clear_six, RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockObjectHolder.light_metal_ironage_block_floor_red_clear_seven, RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockObjectHolder.light_metal_ironage_block_floor_red_clear_eight, RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockObjectHolder.light_metal_ironage_block_floor_red_clear_nine, RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockObjectHolder.light_metal_ironage_block_floor_red_clear_ten, RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockObjectHolder.light_metal_ironage_block_floor_red_clear_eleven, RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockObjectHolder.light_metal_ironage_block_floor_red_clear_twelve, RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockObjectHolder.light_metal_ironage_block_floor_red_clear_thirteen, RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockObjectHolder.light_metal_ironage_block_floor_red_clear_fourteen, RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockObjectHolder.light_metal_ironage_block_floor_red_clear_fifteen, RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockObjectHolder.light_metal_ironage_sconce_floor_red_iron, RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockObjectHolder.light_metal_ironage_sconce_floor_red_iron_one, RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockObjectHolder.light_metal_ironage_sconce_floor_red_iron_two, RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockObjectHolder.light_metal_ironage_sconce_floor_red_iron_three, RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockObjectHolder.light_metal_ironage_sconce_floor_red_iron_four, RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockObjectHolder.light_metal_ironage_sconce_floor_red_iron_five, RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockObjectHolder.light_metal_ironage_sconce_floor_red_iron_six, RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockObjectHolder.light_metal_ironage_sconce_floor_red_iron_seven, RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockObjectHolder.light_metal_ironage_sconce_floor_red_iron_eight, RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockObjectHolder.light_metal_ironage_sconce_floor_red_iron_nine, RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockObjectHolder.light_metal_ironage_sconce_floor_red_iron_ten, RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockObjectHolder.light_metal_ironage_sconce_floor_red_iron_eleven, RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockObjectHolder.light_metal_ironage_sconce_floor_red_iron_twelve, RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockObjectHolder.light_metal_ironage_sconce_floor_red_iron_thirteen, RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockObjectHolder.light_metal_ironage_sconce_floor_red_iron_fourteen, RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockObjectHolder.light_metal_ironage_sconce_floor_red_iron_fifteen, RenderType.cutout());
	}
}
