package zone.moddev.mc.ironagefurniture.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import zone.moddev.mc.ironagefurniture.api.entity.ReleasedLavaLamp;

/** Keep the falling vial at its entity position rather than drawing a full sconce. */
public final class ReleasedLavaLampRenderer extends EntityRenderer<ReleasedLavaLamp> {
    public ReleasedLavaLampRenderer(EntityRendererProvider.Context manager) { super(manager); }
    @Override public void render(ReleasedLavaLamp lamp, float yaw, float partialTicks,
            PoseStack stack, MultiBufferSource buffers, int light) {
        stack.pushPose();
        stack.translate(0, .5, 0);
        Minecraft.getInstance().getItemRenderer().renderStatic(new ItemStack(lamp.getBlockState().getBlock()),
                ItemTransforms.TransformType.NONE, light, OverlayTexture.NO_OVERLAY, stack, buffers, lamp.getId());
        stack.popPose();
        super.render(lamp, yaw, partialTicks, stack, buffers, light);
    }
    @Override public ResourceLocation getTextureLocation(ReleasedLavaLamp lamp) { return TextureAtlas.LOCATION_BLOCKS; }
}
