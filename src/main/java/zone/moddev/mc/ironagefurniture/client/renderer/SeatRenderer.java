package zone.moddev.mc.ironagefurniture.client.renderer;

import zone.moddev.mc.ironagefurniture.api.entity.Seat;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class SeatRenderer extends EntityRenderer<Seat> {
    public SeatRenderer(EntityRendererManager context) {
        super(context);
    }

    @Override
    protected ResourceLocation getEntityTexture(Seat seatEntity) { return null; }
}
