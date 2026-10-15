package zone.moddev.mc.ironagefurniture.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.RenderType;
import com.mojang.math.Vector3f;
import net.minecraft.client.model.ShieldModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.blockentity.BannerRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.core.Direction;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import zone.moddev.mc.ironagefurniture.api.blocks.furniture.ShieldChair;
import zone.moddev.mc.ironagefurniture.api.tile.ShieldChairTileEntity;

/** Draw the saved shield with vanilla banner patterns, but without its hand grip. */
@OnlyIn(Dist.CLIENT)
public final class ShieldChairRenderer implements BlockEntityRenderer<ShieldChairTileEntity> {
    private final ModelPart plate;
    public ShieldChairRenderer(BlockEntityRendererProvider.Context context) { this(context.getModelSet()); }
    public ShieldChairRenderer(net.minecraft.client.model.geom.EntityModelSet models) {
        plate = new ShieldModel(models.bakeLayer(net.minecraft.client.model.geom.ModelLayers.SHIELD)).plate();
    }

    @Override public void render(ShieldChairTileEntity tile, float partialTicks, PoseStack stack,
            MultiBufferSource buffers, int light, int overlay) {
        if (tile.getLevel() == null || !tile.hasShield()) return;
        BlockState state = tile.getLevel().getBlockState(tile.getBlockPos());
        if (state.getBlock() instanceof ShieldChair)
            renderShield(tile.getShield(), state, stack, buffers, light, overlay);
    }

    public void renderShield(ItemStack shield, BlockState state, PoseStack stack,
            MultiBufferSource buffers, int light, int overlay) {
        if (shield.isEmpty()) return;
        boolean patterned = shield.getTagElement("BlockEntityTag") != null;
        Material material = patterned ? ModelBakery.SHIELD_BASE : ModelBakery.NO_PATTERN_SHIELD;
        stack.pushPose();
        try {
            stack.translate(.5, 1, .5);
            stack.mulPose(Vector3f.YP.rotationDegrees(yaw(state.getValue(ShieldChair.DIRECTION))));
            stack.translate(0, 0, .34);
            stack.scale(10F / 12F, -16F / 22F, -.75F);
            plate.render(stack, material.sprite().wrap(ItemRenderer.getFoilBuffer(buffers,
                    material.renderType(RenderType::entitySolid), false, shield.hasFoil())), light, overlay);
            if (patterned) BannerRenderer.renderPatterns(stack, buffers, light, overlay,
                    plate, material, false, BannerBlockEntity.createPatterns(ShieldItem.getColor(shield),
                            BannerBlockEntity.getItemPatterns(shield)));
        } finally { stack.popPose(); }
    }

    private static float yaw(Direction facing) {
        switch (facing) {
        case NORTH: return 180;
        case EAST: return 90;
        case WEST: return 270;
        default: return 0;
        }
    }
}
