package zone.moddev.mc.ironagefurniture.client.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.model.ShieldModel;
import net.minecraft.client.renderer.model.Material;
import net.minecraft.client.renderer.model.ModelBakery;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.tileentity.BannerTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.tileentity.BannerTileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import zone.moddev.mc.ironagefurniture.api.blocks.furniture.ShieldChair;
import zone.moddev.mc.ironagefurniture.api.tile.ShieldChairTileEntity;

/** Draw the saved shield with vanilla banner patterns, but without its hand grip. */
@OnlyIn(Dist.CLIENT)
public final class ShieldChairRenderer extends TileEntityRenderer<ShieldChairTileEntity> {
    private final ModelRenderer plate = new ShieldModel().plate();
    public ShieldChairRenderer(TileEntityRendererDispatcher dispatcher) { super(dispatcher); }

    @Override public void render(ShieldChairTileEntity tile, float partialTicks, MatrixStack stack,
            IRenderTypeBuffer buffers, int light, int overlay) {
        if (tile.getLevel() == null || !tile.hasShield()) return;
        BlockState state = tile.getLevel().getBlockState(tile.getBlockPos());
        if (state.getBlock() instanceof ShieldChair)
            renderShield(tile.getShield(), state, stack, buffers, light, overlay);
    }

    public void renderShield(ItemStack shield, BlockState state, MatrixStack stack,
            IRenderTypeBuffer buffers, int light, int overlay) {
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
            if (patterned) BannerTileEntityRenderer.renderPatterns(stack, buffers, light, overlay,
                    plate, material, false, BannerTileEntity.createPatterns(ShieldItem.getColor(shield),
                            BannerTileEntity.getItemPatterns(shield)));
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
