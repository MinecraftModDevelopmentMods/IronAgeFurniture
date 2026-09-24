package zone.moddev.mc.ironagefurniture.client.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import zone.moddev.mc.ironagefurniture.BlockObjectHolder;
import zone.moddev.mc.ironagefurniture.api.entity.ThrownLavaLamp;

/** Renders the known lava-lamp item even before the projectile stack synchronises. */
@OnlyIn(Dist.CLIENT)
public final class ThrownLavaLampRenderer extends EntityRenderer<ThrownLavaLamp> {
    private final ItemRenderer itemRenderer;

    public ThrownLavaLampRenderer(EntityRendererManager manager) {
        super(manager);
        this.itemRenderer = Minecraft.getInstance().getItemRenderer();
    }

    @Override
    public void render(ThrownLavaLamp entity, float entityYaw, float partialTicks,
            MatrixStack stack, IRenderTypeBuffer buffers, int packedLight) {
        stack.pushPose();
        stack.mulPose(entityRenderDispatcher.cameraOrientation());
        stack.mulPose(Vector3f.YP.rotationDegrees(180.0F));
        itemRenderer.renderStatic(new ItemStack(
                BlockObjectHolder.light_metal_ironage_block_floor_lava_clear),
                ItemCameraTransforms.TransformType.GROUND, packedLight,
                OverlayTexture.NO_OVERLAY, stack, buffers);
        stack.popPose();
        super.render(entity, entityYaw, partialTicks, stack, buffers, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(ThrownLavaLamp entity) {
        return AtlasTexture.LOCATION_BLOCKS;
    }
}
