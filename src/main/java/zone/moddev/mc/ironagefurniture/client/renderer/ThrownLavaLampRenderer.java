package zone.moddev.mc.ironagefurniture.client.renderer;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import zone.moddev.mc.ironagefurniture.BlockObjectHolder;
import zone.moddev.mc.ironagefurniture.api.entity.ThrownLavaLamp;

/** Renders a thrown lamp with the known lava-lamp item instead of a transient entity stack. */
@OnlyIn(Dist.CLIENT)
public final class ThrownLavaLampRenderer extends EntityRenderer<ThrownLavaLamp> {
    private final ItemRenderer itemRenderer;

    public ThrownLavaLampRenderer(EntityRendererManager manager) {
        super(manager);
        this.itemRenderer = Minecraft.getInstance().getItemRenderer();
    }

    @Override
    public void doRender(ThrownLavaLamp entity, double x, double y, double z,
            float entityYaw, float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.translatef((float) x, (float) y, (float) z);
        GlStateManager.enableRescaleNormal();
        GlStateManager.rotatef(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotatef((renderManager.options.thirdPersonView == 2 ? -1.0F : 1.0F)
                * renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotatef(180.0F, 0.0F, 1.0F, 0.0F);
        bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);

        if (renderOutlines) {
            GlStateManager.enableColorMaterial();
            GlStateManager.setupSolidRenderingTextureCombine(getTeamColor(entity));
        }

        itemRenderer.renderItem(new ItemStack(
                BlockObjectHolder.light_metal_ironage_block_floor_lava_clear),
                ItemCameraTransforms.TransformType.GROUND);

        if (renderOutlines) {
            GlStateManager.tearDownSolidRenderingTextureCombine();
            GlStateManager.disableColorMaterial();
        }

        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    @Override
    protected ResourceLocation getEntityTexture(ThrownLavaLamp entity) {
        return AtlasTexture.LOCATION_BLOCKS_TEXTURE;
    }
}
