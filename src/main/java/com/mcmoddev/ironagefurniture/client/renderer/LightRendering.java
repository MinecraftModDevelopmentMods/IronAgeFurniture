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
		ItemBlockRenderTypes.setRenderLayer(BlockObjectHolder.light_metal_ironage_block_floor_glow_clear.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockObjectHolder.light_metal_ironage_sconce_floor_glow_iron.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockObjectHolder.light_metal_ironage_sconce_wall_glow_iron.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockObjectHolder.light_metal_ironage_block_floor_lava_clear.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockObjectHolder.light_metal_ironage_sconce_floor_lava_iron.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockObjectHolder.light_metal_ironage_sconce_wall_lava_iron.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockObjectHolder.light_metal_ironage_sconce_wall_redtorch_iron.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockObjectHolder.light_metal_ironage_sconce_wall_redtorch_iron_unlit.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockObjectHolder.light_metal_ironage_sconce_floor_redtorch_iron.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockObjectHolder.light_metal_ironage_sconce_floor_redtorch_iron_unlit.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockObjectHolder.light_metal_ironage_block_floor_red_clear.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockObjectHolder.light_metal_ironage_block_floor_red_clear_one.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockObjectHolder.light_metal_ironage_block_floor_red_clear_two.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockObjectHolder.light_metal_ironage_block_floor_red_clear_three.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockObjectHolder.light_metal_ironage_block_floor_red_clear_four.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockObjectHolder.light_metal_ironage_block_floor_red_clear_five.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockObjectHolder.light_metal_ironage_block_floor_red_clear_six.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockObjectHolder.light_metal_ironage_block_floor_red_clear_seven.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockObjectHolder.light_metal_ironage_block_floor_red_clear_eight.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockObjectHolder.light_metal_ironage_block_floor_red_clear_nine.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockObjectHolder.light_metal_ironage_block_floor_red_clear_ten.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockObjectHolder.light_metal_ironage_block_floor_red_clear_eleven.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockObjectHolder.light_metal_ironage_block_floor_red_clear_twelve.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockObjectHolder.light_metal_ironage_block_floor_red_clear_thirteen.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockObjectHolder.light_metal_ironage_block_floor_red_clear_fourteen.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockObjectHolder.light_metal_ironage_block_floor_red_clear_fifteen.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockObjectHolder.light_metal_ironage_sconce_floor_red_iron.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockObjectHolder.light_metal_ironage_sconce_floor_red_iron_one.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockObjectHolder.light_metal_ironage_sconce_floor_red_iron_two.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockObjectHolder.light_metal_ironage_sconce_floor_red_iron_three.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockObjectHolder.light_metal_ironage_sconce_floor_red_iron_four.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockObjectHolder.light_metal_ironage_sconce_floor_red_iron_five.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockObjectHolder.light_metal_ironage_sconce_floor_red_iron_six.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockObjectHolder.light_metal_ironage_sconce_floor_red_iron_seven.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockObjectHolder.light_metal_ironage_sconce_floor_red_iron_eight.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockObjectHolder.light_metal_ironage_sconce_floor_red_iron_nine.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockObjectHolder.light_metal_ironage_sconce_floor_red_iron_ten.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockObjectHolder.light_metal_ironage_sconce_floor_red_iron_eleven.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockObjectHolder.light_metal_ironage_sconce_floor_red_iron_twelve.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockObjectHolder.light_metal_ironage_sconce_floor_red_iron_thirteen.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockObjectHolder.light_metal_ironage_sconce_floor_red_iron_fourteen.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockObjectHolder.light_metal_ironage_sconce_floor_red_iron_fifteen.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockObjectHolder.light_metal_ironage_sconce_wall_red_iron.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockObjectHolder.light_metal_ironage_sconce_wall_red_iron_one.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockObjectHolder.light_metal_ironage_sconce_wall_red_iron_two.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockObjectHolder.light_metal_ironage_sconce_wall_red_iron_three.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockObjectHolder.light_metal_ironage_sconce_wall_red_iron_four.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockObjectHolder.light_metal_ironage_sconce_wall_red_iron_five.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockObjectHolder.light_metal_ironage_sconce_wall_red_iron_six.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockObjectHolder.light_metal_ironage_sconce_wall_red_iron_seven.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockObjectHolder.light_metal_ironage_sconce_wall_red_iron_eight.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockObjectHolder.light_metal_ironage_sconce_wall_red_iron_nine.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockObjectHolder.light_metal_ironage_sconce_wall_red_iron_ten.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockObjectHolder.light_metal_ironage_sconce_wall_red_iron_eleven.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockObjectHolder.light_metal_ironage_sconce_wall_red_iron_twelve.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockObjectHolder.light_metal_ironage_sconce_wall_red_iron_thirteen.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockObjectHolder.light_metal_ironage_sconce_wall_red_iron_fourteen.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockObjectHolder.light_metal_ironage_sconce_wall_red_iron_fifteen.get(), RenderType.cutout());
	}
}
