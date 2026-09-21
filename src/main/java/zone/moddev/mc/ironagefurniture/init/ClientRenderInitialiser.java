package zone.moddev.mc.ironagefurniture.init;

import zone.moddev.mc.ironagefurniture.BlockObjectHolder;
import zone.moddev.mc.ironagefurniture.api.entity.EntityThrownLavaLamp;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.item.Item;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/** Client-only render registration for the throwable lava lamp. */
@SideOnly(Side.CLIENT)
public final class ClientRenderInitialiser {

	private ClientRenderInitialiser() {
		throw new IllegalAccessError("This class cannot be instantiated");
	}

	public static void registerEntityRenderers() {
		if (BlockObjectHolder.light_metal_ironage_block_floor_lava_clear == null) {
			return;
		}

		RenderingRegistry.registerEntityRenderingHandler(EntityThrownLavaLamp.class,
				new IRenderFactory<EntityThrownLavaLamp>() {
					@Override
					public Render<? super EntityThrownLavaLamp> createRenderFor(RenderManager manager) {
						return new RenderSnowball<EntityThrownLavaLamp>(manager,
								Item.getItemFromBlock(
										BlockObjectHolder.light_metal_ironage_block_floor_lava_clear),
								Minecraft.getMinecraft().getRenderItem());
					}
				});
	}
}
