package zone.moddev.mc.ironagefurniture.client.renderer;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.FallingBlockRenderer;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.nbt.CompoundTag;
import zone.moddev.mc.ironagefurniture.api.ShieldChairItemData;

/** Draws the frame and its saved shield together during a fall. */
public final class FallingShieldChairRenderer extends FallingBlockRenderer {
    private final ShieldChairRenderer shieldRenderer;

    public FallingShieldChairRenderer(EntityRendererProvider.Context manager) {
        super(manager);
        shieldRenderer = new ShieldChairRenderer(manager.getModelSet());
    }

    @Override public void render(FallingBlockEntity entity, float yaw, float partialTicks,
            com.mojang.blaze3d.vertex.PoseStack stack, net.minecraft.client.renderer.MultiBufferSource buffers, int light) {
        super.render(entity, yaw, partialTicks, stack, buffers, light);
        stack.pushPose();
        stack.translate(-.5, 0, -.5);
        shieldRenderer.renderShield(ShieldChairItemData.readShield(entity.blockData == null
                ? new CompoundTag() : entity.blockData), entity.getBlockState(), stack, buffers, light,
                net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY);
        stack.popPose();
    }
}
