package zone.moddev.mc.ironagefurniture.client.renderer;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BannerTextures;
import net.minecraft.client.renderer.entity.model.ShieldModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.tileentity.BannerTileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import zone.moddev.mc.ironagefurniture.api.blocks.furniture.ShieldChair;
import zone.moddev.mc.ironagefurniture.api.tile.ShieldChairTileEntity;

/** Uses vanilla's shield textures, including banner designs and resource packs. */
@OnlyIn(Dist.CLIENT)
public final class ShieldChairRenderer extends TileEntityRenderer<ShieldChairTileEntity> {
    private final BannerTileEntity banner = new BannerTileEntity();
    private final RendererModel plate;

    public ShieldChairRenderer() {
        // ShieldModel hides its plate in 1.14. Recreate only that vanilla part,
        // leaving out the hand grip which would stick through the wooden frame.
        plate = new RendererModel(new ShieldModel(), 0, 0);
        plate.addBox(-6.0F, -11.0F, -2.0F, 12, 22, 1, 0.0F);
    }

    @Override public void render(ShieldChairTileEntity tile, double x, double y, double z,
            float partialTicks, int destroyStage) {
        if (tile.getWorld() == null || !tile.hasShield()) return;
        BlockState state = tile.getWorld().getBlockState(tile.getPos());
        if (state.getBlock() instanceof ShieldChair) renderShield(tile.getShield(), state, x, y, z);
    }

    public void renderShield(ItemStack shield, BlockState state, double x, double y, double z) {
        if (shield.isEmpty()) return;
        if (shield.getChildTag("BlockEntityTag") != null) {
            banner.loadFromItemStack(shield, ShieldItem.getColor(shield));
            Minecraft.getInstance().getTextureManager().bindTexture(BannerTextures.SHIELD_DESIGNS.getResourceLocation(
                    banner.getPatternResourceLocation(), banner.getPatternList(), banner.getColorList()));
        } else {
            Minecraft.getInstance().getTextureManager().bindTexture(BannerTextures.SHIELD_BASE_TEXTURE);
        }
        GlStateManager.pushMatrix();
        GlStateManager.enableRescaleNormal();
        try {
            GlStateManager.color4f(1, 1, 1, 1);
            GlStateManager.translated(x + 0.5D, y + 1.0D, z + 0.5D);
            GlStateManager.rotatef(yaw(state.get(ShieldChair.DIRECTION)), 0, 1, 0);
            GlStateManager.translated(0, 0, 0.34D);
            GlStateManager.scalef(10.0F / 12.0F, -16.0F / 22.0F, -0.75F);
            plate.render(0.0625F);
        } finally {
            GlStateManager.disableRescaleNormal();
            GlStateManager.popMatrix();
        }
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
