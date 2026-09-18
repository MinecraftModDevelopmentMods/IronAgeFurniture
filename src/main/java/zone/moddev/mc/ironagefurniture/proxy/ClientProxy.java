package zone.moddev.mc.ironagefurniture.proxy;

import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import zone.moddev.mc.ironagefurniture.client.renderer.SeatRenderer;
import zone.moddev.mc.ironagefurniture.registers.entities;

public class ClientProxy extends CommonProxy {

    @Override
    public void onSetupClient() {
        RenderingRegistry.registerEntityRenderingHandler(zone.moddev.mc.ironagefurniture.api.entity.Seat.class, SeatRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(zone.moddev.mc.ironagefurniture.api.entity.ThrownLavaLamp.class,
                manager -> new SpriteRenderer<>(manager, Minecraft.getInstance().getItemRenderer()));
    }
}
