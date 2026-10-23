package zone.moddev.mc.ironagefurniture.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import zone.moddev.mc.ironagefurniture.api.entity.ReleasedLavaLamp;
import zone.moddev.mc.ironagefurniture.init.ModVanillaLights;

/** The exact standalone vial model through the target's immediate renderer. */
public final class ReleasedLavaLampRenderer extends EntityRenderer<ReleasedLavaLamp> {
    private final BlockRenderDispatcher blocks;
    public ReleasedLavaLampRenderer(EntityRendererProvider.Context context) { super(context); blocks = context.getBlockRenderDispatcher(); }
    @Override public void render(ReleasedLavaLamp entity, float yaw, float partialTicks, PoseStack poses,
            MultiBufferSource buffers, int light) {
        poses.pushPose();
        try {
            poses.translate(-.5, 0, -.5);
            blocks.renderSingleBlock(ModVanillaLights.light_metal_ironage_block_floor_lava_clear.get().defaultBlockState(),
                    poses, buffers, light, OverlayTexture.NO_OVERLAY);
        } finally { poses.popPose(); }
        super.render(entity, yaw, partialTicks, poses, buffers, light);
    }
    @Override public ResourceLocation getTextureLocation(ReleasedLavaLamp entity) { return TextureAtlas.LOCATION_BLOCKS; }
}
