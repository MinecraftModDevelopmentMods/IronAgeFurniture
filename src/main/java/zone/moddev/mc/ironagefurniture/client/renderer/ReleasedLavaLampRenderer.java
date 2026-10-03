package zone.moddev.mc.ironagefurniture.client.renderer;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import zone.moddev.mc.ironagefurniture.BlockObjectHolder;
import zone.moddev.mc.ironagefurniture.api.entity.ReleasedLavaLamp;

/** Item rendering includes the glass; vanilla falling-block rendering omits it. */
public final class ReleasedLavaLampRenderer extends EntityRenderer<ReleasedLavaLamp> {
    public ReleasedLavaLampRenderer(EntityRendererManager manager) { super(manager); }
    @Override public void doRender(ReleasedLavaLamp lamp, double x, double y, double z, float yaw, float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.translatef((float)x, (float)y + .5F, (float)z);
        bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
        Minecraft.getInstance().getItemRenderer().renderItem(
                new ItemStack(BlockObjectHolder.light_metal_ironage_block_floor_lava_clear), ItemCameraTransforms.TransformType.NONE);
        GlStateManager.popMatrix();
        super.doRender(lamp, x, y, z, yaw, partialTicks);
    }
    @Override protected ResourceLocation getEntityTexture(ReleasedLavaLamp lamp) { return AtlasTexture.LOCATION_BLOCKS_TEXTURE; }
}
