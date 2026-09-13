package zone.moddev.mc.ironagefurniture.client.renderer;

import zone.moddev.mc.ironagefurniture.Ironagefurniture;
import zone.moddev.mc.ironagefurniture.init.ModVanillaLights;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class LightRendering {

	public static void clientSetup(FMLClientSetupEvent event) {
		ItemBlockRenderTypes.setRenderLayer(ModVanillaLights.light_metal_ironage_block_floor_glow_clear.get(), ChunkSectionLayer.CUTOUT);
		ItemBlockRenderTypes.setRenderLayer(ModVanillaLights.light_metal_ironage_sconce_floor_glow_iron.get(), ChunkSectionLayer.CUTOUT);
		ItemBlockRenderTypes.setRenderLayer(ModVanillaLights.light_metal_ironage_sconce_wall_glow_iron.get(), ChunkSectionLayer.CUTOUT);
		ItemBlockRenderTypes.setRenderLayer(ModVanillaLights.light_metal_ironage_block_floor_lava_clear.get(), ChunkSectionLayer.CUTOUT);
		ItemBlockRenderTypes.setRenderLayer(ModVanillaLights.light_metal_ironage_sconce_floor_lava_iron.get(), ChunkSectionLayer.CUTOUT);
		ItemBlockRenderTypes.setRenderLayer(ModVanillaLights.light_metal_ironage_sconce_wall_lava_iron.get(), ChunkSectionLayer.CUTOUT);
		ItemBlockRenderTypes.setRenderLayer(ModVanillaLights.light_metal_ironage_sconce_wall_redtorch_iron.get(), ChunkSectionLayer.CUTOUT);
		ItemBlockRenderTypes.setRenderLayer(ModVanillaLights.light_metal_ironage_sconce_wall_redtorch_iron_unlit.get(), ChunkSectionLayer.CUTOUT);
		ItemBlockRenderTypes.setRenderLayer(ModVanillaLights.light_metal_ironage_sconce_floor_redtorch_iron.get(), ChunkSectionLayer.CUTOUT);
		ItemBlockRenderTypes.setRenderLayer(ModVanillaLights.light_metal_ironage_sconce_floor_redtorch_iron_unlit.get(), ChunkSectionLayer.CUTOUT);
		ItemBlockRenderTypes.setRenderLayer(ModVanillaLights.light_metal_ironage_block_floor_red_clear.get(), ChunkSectionLayer.CUTOUT);
		ItemBlockRenderTypes.setRenderLayer(ModVanillaLights.light_metal_ironage_block_floor_red_clear_one.get(), ChunkSectionLayer.CUTOUT);
		ItemBlockRenderTypes.setRenderLayer(ModVanillaLights.light_metal_ironage_block_floor_red_clear_two.get(), ChunkSectionLayer.CUTOUT);
		ItemBlockRenderTypes.setRenderLayer(ModVanillaLights.light_metal_ironage_block_floor_red_clear_three.get(), ChunkSectionLayer.CUTOUT);
		ItemBlockRenderTypes.setRenderLayer(ModVanillaLights.light_metal_ironage_block_floor_red_clear_four.get(), ChunkSectionLayer.CUTOUT);
		ItemBlockRenderTypes.setRenderLayer(ModVanillaLights.light_metal_ironage_block_floor_red_clear_five.get(), ChunkSectionLayer.CUTOUT);
		ItemBlockRenderTypes.setRenderLayer(ModVanillaLights.light_metal_ironage_block_floor_red_clear_six.get(), ChunkSectionLayer.CUTOUT);
		ItemBlockRenderTypes.setRenderLayer(ModVanillaLights.light_metal_ironage_block_floor_red_clear_seven.get(), ChunkSectionLayer.CUTOUT);
		ItemBlockRenderTypes.setRenderLayer(ModVanillaLights.light_metal_ironage_block_floor_red_clear_eight.get(), ChunkSectionLayer.CUTOUT);
		ItemBlockRenderTypes.setRenderLayer(ModVanillaLights.light_metal_ironage_block_floor_red_clear_nine.get(), ChunkSectionLayer.CUTOUT);
		ItemBlockRenderTypes.setRenderLayer(ModVanillaLights.light_metal_ironage_block_floor_red_clear_ten.get(), ChunkSectionLayer.CUTOUT);
		ItemBlockRenderTypes.setRenderLayer(ModVanillaLights.light_metal_ironage_block_floor_red_clear_eleven.get(), ChunkSectionLayer.CUTOUT);
		ItemBlockRenderTypes.setRenderLayer(ModVanillaLights.light_metal_ironage_block_floor_red_clear_twelve.get(), ChunkSectionLayer.CUTOUT);
		ItemBlockRenderTypes.setRenderLayer(ModVanillaLights.light_metal_ironage_block_floor_red_clear_thirteen.get(), ChunkSectionLayer.CUTOUT);
		ItemBlockRenderTypes.setRenderLayer(ModVanillaLights.light_metal_ironage_block_floor_red_clear_fourteen.get(), ChunkSectionLayer.CUTOUT);
		ItemBlockRenderTypes.setRenderLayer(ModVanillaLights.light_metal_ironage_block_floor_red_clear_fifteen.get(), ChunkSectionLayer.CUTOUT);
		ItemBlockRenderTypes.setRenderLayer(ModVanillaLights.light_metal_ironage_sconce_floor_red_iron.get(), ChunkSectionLayer.CUTOUT);
		ItemBlockRenderTypes.setRenderLayer(ModVanillaLights.light_metal_ironage_sconce_floor_red_iron_one.get(), ChunkSectionLayer.CUTOUT);
		ItemBlockRenderTypes.setRenderLayer(ModVanillaLights.light_metal_ironage_sconce_floor_red_iron_two.get(), ChunkSectionLayer.CUTOUT);
		ItemBlockRenderTypes.setRenderLayer(ModVanillaLights.light_metal_ironage_sconce_floor_red_iron_three.get(), ChunkSectionLayer.CUTOUT);
		ItemBlockRenderTypes.setRenderLayer(ModVanillaLights.light_metal_ironage_sconce_floor_red_iron_four.get(), ChunkSectionLayer.CUTOUT);
		ItemBlockRenderTypes.setRenderLayer(ModVanillaLights.light_metal_ironage_sconce_floor_red_iron_five.get(), ChunkSectionLayer.CUTOUT);
		ItemBlockRenderTypes.setRenderLayer(ModVanillaLights.light_metal_ironage_sconce_floor_red_iron_six.get(), ChunkSectionLayer.CUTOUT);
		ItemBlockRenderTypes.setRenderLayer(ModVanillaLights.light_metal_ironage_sconce_floor_red_iron_seven.get(), ChunkSectionLayer.CUTOUT);
		ItemBlockRenderTypes.setRenderLayer(ModVanillaLights.light_metal_ironage_sconce_floor_red_iron_eight.get(), ChunkSectionLayer.CUTOUT);
		ItemBlockRenderTypes.setRenderLayer(ModVanillaLights.light_metal_ironage_sconce_floor_red_iron_nine.get(), ChunkSectionLayer.CUTOUT);
		ItemBlockRenderTypes.setRenderLayer(ModVanillaLights.light_metal_ironage_sconce_floor_red_iron_ten.get(), ChunkSectionLayer.CUTOUT);
		ItemBlockRenderTypes.setRenderLayer(ModVanillaLights.light_metal_ironage_sconce_floor_red_iron_eleven.get(), ChunkSectionLayer.CUTOUT);
		ItemBlockRenderTypes.setRenderLayer(ModVanillaLights.light_metal_ironage_sconce_floor_red_iron_twelve.get(), ChunkSectionLayer.CUTOUT);
		ItemBlockRenderTypes.setRenderLayer(ModVanillaLights.light_metal_ironage_sconce_floor_red_iron_thirteen.get(), ChunkSectionLayer.CUTOUT);
		ItemBlockRenderTypes.setRenderLayer(ModVanillaLights.light_metal_ironage_sconce_floor_red_iron_fourteen.get(), ChunkSectionLayer.CUTOUT);
		ItemBlockRenderTypes.setRenderLayer(ModVanillaLights.light_metal_ironage_sconce_floor_red_iron_fifteen.get(), ChunkSectionLayer.CUTOUT);
		ItemBlockRenderTypes.setRenderLayer(ModVanillaLights.light_metal_ironage_sconce_wall_red_iron.get(), ChunkSectionLayer.CUTOUT);
		ItemBlockRenderTypes.setRenderLayer(ModVanillaLights.light_metal_ironage_sconce_wall_red_iron_one.get(), ChunkSectionLayer.CUTOUT);
		ItemBlockRenderTypes.setRenderLayer(ModVanillaLights.light_metal_ironage_sconce_wall_red_iron_two.get(), ChunkSectionLayer.CUTOUT);
		ItemBlockRenderTypes.setRenderLayer(ModVanillaLights.light_metal_ironage_sconce_wall_red_iron_three.get(), ChunkSectionLayer.CUTOUT);
		ItemBlockRenderTypes.setRenderLayer(ModVanillaLights.light_metal_ironage_sconce_wall_red_iron_four.get(), ChunkSectionLayer.CUTOUT);
		ItemBlockRenderTypes.setRenderLayer(ModVanillaLights.light_metal_ironage_sconce_wall_red_iron_five.get(), ChunkSectionLayer.CUTOUT);
		ItemBlockRenderTypes.setRenderLayer(ModVanillaLights.light_metal_ironage_sconce_wall_red_iron_six.get(), ChunkSectionLayer.CUTOUT);
		ItemBlockRenderTypes.setRenderLayer(ModVanillaLights.light_metal_ironage_sconce_wall_red_iron_seven.get(), ChunkSectionLayer.CUTOUT);
		ItemBlockRenderTypes.setRenderLayer(ModVanillaLights.light_metal_ironage_sconce_wall_red_iron_eight.get(), ChunkSectionLayer.CUTOUT);
		ItemBlockRenderTypes.setRenderLayer(ModVanillaLights.light_metal_ironage_sconce_wall_red_iron_nine.get(), ChunkSectionLayer.CUTOUT);
		ItemBlockRenderTypes.setRenderLayer(ModVanillaLights.light_metal_ironage_sconce_wall_red_iron_ten.get(), ChunkSectionLayer.CUTOUT);
		ItemBlockRenderTypes.setRenderLayer(ModVanillaLights.light_metal_ironage_sconce_wall_red_iron_eleven.get(), ChunkSectionLayer.CUTOUT);
		ItemBlockRenderTypes.setRenderLayer(ModVanillaLights.light_metal_ironage_sconce_wall_red_iron_twelve.get(), ChunkSectionLayer.CUTOUT);
		ItemBlockRenderTypes.setRenderLayer(ModVanillaLights.light_metal_ironage_sconce_wall_red_iron_thirteen.get(), ChunkSectionLayer.CUTOUT);
		ItemBlockRenderTypes.setRenderLayer(ModVanillaLights.light_metal_ironage_sconce_wall_red_iron_fourteen.get(), ChunkSectionLayer.CUTOUT);
		ItemBlockRenderTypes.setRenderLayer(ModVanillaLights.light_metal_ironage_sconce_wall_red_iron_fifteen.get(), ChunkSectionLayer.CUTOUT);
	}
}
