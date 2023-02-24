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
    }
}