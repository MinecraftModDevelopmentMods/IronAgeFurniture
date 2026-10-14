package zone.moddev.mc.ironagefurniture.client.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import zone.moddev.mc.ironagefurniture.api.entity.ReleasedLavaLamp;

/** Keep the falling vial at its entity position rather than drawing a full sconce. */
public final class ReleasedLavaLampRenderer extends EntityRenderer<ReleasedLavaLamp> {
    public ReleasedLavaLampRenderer(EntityRendererManager manager) { super(manager); }
    @Override public void render(ReleasedLavaLamp lamp, float yaw, float partialTicks,
            MatrixStack stack, IRenderTypeBuffer buffers, int light) {
        stack.pushPose();
        stack.translate(0, .5, 0);
        Minecraft.getInstance().getItemRenderer().renderStatic(new ItemStack(lamp.getBlockState().getBlock()),
                ItemCameraTransforms.TransformType.NONE, light, OverlayTexture.NO_OVERLAY, stack, buffers);
        stack.popPose();
        super.render(lamp, yaw, partialTicks, stack, buffers, light);
    }
    @Override public ResourceLocation getTextureLocation(ReleasedLavaLamp lamp) { return AtlasTexture.LOCATION_BLOCKS; }
}
