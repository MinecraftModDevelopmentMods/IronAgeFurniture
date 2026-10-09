package zone.moddev.mc.ironagefurniture.client.renderer;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.FallingBlockRenderer;
import net.minecraft.entity.item.FallingBlockEntity;
import net.minecraft.nbt.CompoundNBT;
import zone.moddev.mc.ironagefurniture.api.ShieldChairItemData;

/** Draws the frame and its saved shield together during a fall. */
public final class FallingShieldChairRenderer extends FallingBlockRenderer {
    private final ShieldChairRenderer shieldRenderer = new ShieldChairRenderer(net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher.instance);

    public FallingShieldChairRenderer(EntityRendererManager manager) { super(manager); }

    @Override public void render(FallingBlockEntity entity, float yaw, float partialTicks,
            com.mojang.blaze3d.matrix.MatrixStack stack, net.minecraft.client.renderer.IRenderTypeBuffer buffers, int light) {
        super.render(entity, yaw, partialTicks, stack, buffers, light);
        stack.pushPose();
        stack.translate(-.5, 0, -.5);
        shieldRenderer.renderShield(ShieldChairItemData.readShield(entity.blockData == null
                ? new CompoundNBT() : entity.blockData), entity.getBlockState(), stack, buffers, light,
                net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY);
        stack.popPose();
    }
}
