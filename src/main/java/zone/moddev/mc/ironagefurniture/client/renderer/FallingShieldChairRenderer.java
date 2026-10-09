package zone.moddev.mc.ironagefurniture.client.renderer;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.FallingBlockRenderer;
import net.minecraft.entity.item.FallingBlockEntity;
import net.minecraft.nbt.CompoundNBT;
import zone.moddev.mc.ironagefurniture.api.ShieldChairItemData;

/** Draws the frame and its saved shield together during a fall. */
public final class FallingShieldChairRenderer extends FallingBlockRenderer {
    private final ShieldChairRenderer shieldRenderer = new ShieldChairRenderer();

    public FallingShieldChairRenderer(EntityRendererManager manager) { super(manager); }

    @Override public void doRender(FallingBlockEntity entity, double x, double y, double z,
            float yaw, float partialTicks) {
        super.doRender(entity, x, y, z, yaw, partialTicks);
        shieldRenderer.renderShield(ShieldChairItemData.readShield(entity.tileEntityData == null
                ? new CompoundNBT() : entity.tileEntityData), entity.getBlockState(), x - 0.5D, y, z - 0.5D);
    }
}
