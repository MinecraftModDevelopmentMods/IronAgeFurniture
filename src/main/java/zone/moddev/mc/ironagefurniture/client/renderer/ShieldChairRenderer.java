package zone.moddev.mc.ironagefurniture.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BannerRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.core.Direction;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import zone.moddev.mc.ironagefurniture.api.blocks.base.FurnitureBlock;
import zone.moddev.mc.ironagefurniture.api.tile.ShieldChairBlockEntity;

/** Native immediate rendering of the complete shield plate, without its handle. */
public final class ShieldChairRenderer implements BlockEntityRenderer<ShieldChairBlockEntity> {
    private final ModelPart plate;

    public ShieldChairRenderer(BlockEntityRendererProvider.Context context) { this(context.getModelSet()); }
    public ShieldChairRenderer(EntityModelSet models) { plate = models.bakeLayer(ModelLayers.SHIELD).getChild("plate"); }

    @Override public void render(ShieldChairBlockEntity entity, float partialTicks, PoseStack poses,
            MultiBufferSource buffers, int light, int overlay) {
        renderShield(entity.shield(), entity.getBlockState().getValue(FurnitureBlock.DIRECTION), poses, buffers, light, overlay);
    }



    public void renderShield(ItemStack shield, Direction facing, PoseStack poses, MultiBufferSource buffers, int light, int overlay) {
        if (shield.isEmpty()) return;
        poses.pushPose();
        try {
            poses.translate(.5, 1, .5);
            float yaw = switch (facing) { case NORTH -> 180; case EAST -> 90; case WEST -> 270; default -> 0; };
            poses.mulPose(Axis.YP.rotationDegrees(yaw));
            poses.translate(0, 0, .34);
            poses.scale(10F / 12F, -16F / 22F, -.75F);
            var patterns = net.minecraft.world.level.block.entity.BannerBlockEntity.createPatterns(
                    net.minecraft.world.item.ShieldItem.getColor(shield),
                    net.minecraft.world.level.block.entity.BannerBlockEntity.getItemPatterns(shield));
            boolean patterned = net.minecraft.world.item.BlockItem.getBlockEntityData(shield) != null;
            Material base = patterned ? ModelBakery.SHIELD_BASE : ModelBakery.NO_PATTERN_SHIELD;
            if (patterned)
                BannerRenderer.renderPatterns(poses, buffers, light, overlay, plate, base, false,
                        patterns, false);
            else
                plate.render(poses, base.buffer(buffers, RenderType::entitySolid), light, overlay);
            // Keep foil separate from, and after, the opaque pattern layers.
            if (shield.hasFoil())
                plate.render(poses, base.sprite().wrap(buffers.getBuffer(RenderType.entityGlint())), light, overlay);
        } finally { poses.popPose(); }
    }
}
