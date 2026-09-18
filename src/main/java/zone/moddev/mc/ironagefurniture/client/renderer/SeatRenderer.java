package zone.moddev.mc.ironagefurniture.client.renderer;

import zone.moddev.mc.ironagefurniture.api.entity.Seat;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class SeatRenderer extends EntityRenderer<Seat> {
    public SeatRenderer(EntityRendererManager context) {
        super(context);
    }

    @Override
    protected void renderNameTag(Seat entity, String component, MatrixStack stack, IRenderTypeBuffer source, int light) {}

    @Override
    public ResourceLocation getTextureLocation(Seat seatEntity) { return null; }
}
