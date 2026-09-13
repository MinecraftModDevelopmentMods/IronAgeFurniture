package zone.moddev.mc.ironagefurniture.client.renderer;

import zone.moddev.mc.ironagefurniture.api.entity.Seat;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;

public class SeatRenderer extends EntityRenderer<Seat, EntityRenderState> {
    public SeatRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public EntityRenderState createRenderState() {
        return new EntityRenderState();
    }
}
