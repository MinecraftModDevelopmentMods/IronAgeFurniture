package zone.moddev.mc.ironagefurniture.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import zone.moddev.mc.ironagefurniture.BlockObjectHolder;
import zone.moddev.mc.ironagefurniture.api.entity.ThrownLavaLamp;

/** Renders the known lava-lamp item even before the projectile stack synchronises. */
@OnlyIn(Dist.CLIENT)
public final class ThrownLavaLampRenderer extends EntityRenderer<ThrownLavaLamp> {
    private final ItemRenderer itemRenderer;

    public ThrownLavaLampRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(ThrownLavaLamp entity, float entityYaw, float partialTicks,
            PoseStack stack, MultiBufferSource buffers, int packedLight) {
        stack.pushPose();
        stack.mulPose(entityRenderDispatcher.cameraOrientation());
        stack.mulPose(Vector3f.YP.rotationDegrees(180.0F));
        itemRenderer.renderStatic(new ItemStack(
                BlockObjectHolder.light_metal_ironage_block_floor_lava_clear),
                ItemTransforms.TransformType.GROUND, packedLight,
                OverlayTexture.NO_OVERLAY, stack, buffers, 0);
        stack.popPose();
        super.render(entity, entityYaw, partialTicks, stack, buffers, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(ThrownLavaLamp entity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}
