package zone.moddev.mc.ironagefurniture.client.renderer;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import zone.moddev.mc.ironagefurniture.api.blocks.base.LightHolderSconce;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.glow.LightSourceGlowdust;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.red.LightSourceRed;

/** Keeps glass in the cutout layer for every placed lamp and sconce. */
public final class LightRendering {
	private LightRendering() {
	}

	public static void clientSetup(FMLClientSetupEvent event) {
		event.enqueueWork(() -> BuiltInRegistries.BLOCK.forEach(block -> {
			if (block instanceof LightHolderSconce || block instanceof LightSourceGlowdust
					|| block instanceof LightSourceRed) {
				ItemBlockRenderTypes.setRenderLayer(block, RenderType.cutout());
			}
		}));
	}
}
