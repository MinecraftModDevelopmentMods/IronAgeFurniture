package zone.moddev.mc.ironagefurniture.client.renderer;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import zone.moddev.mc.ironagefurniture.api.blocks.base.LightHolderSconce;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.glow.LightSourceGlowdust;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.red.LightSourceRed;

/**
 * Keeps the glass layers consistent with the block models. Forge 61.1.0 does
 * not expose its parsed Fast render hint through ModelContext, so Fast graphics
 * still needs the native block-layer fallback registered during client setup.
 */
public final class LightRendering {
	private LightRendering() {
	}

	public static void clientSetup(FMLClientSetupEvent event) {
		event.enqueueWork(() -> BuiltInRegistries.BLOCK.forEach(block -> {
			if (block instanceof LightHolderSconce || block instanceof LightSourceGlowdust
					|| block instanceof LightSourceRed) {
				ItemBlockRenderTypes.setRenderLayer(block, ChunkSectionLayer.CUTOUT);
			}
		}));
	}
}
