package zone.moddev.mc.ironagefurniture.client.render;

import zone.moddev.mc.ironagefurniture.api.Blocks.ShieldChair;
import zone.moddev.mc.ironagefurniture.api.tile.TileEntityShieldChair;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelShield;
import net.minecraft.client.renderer.BannerTextures;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.util.EnumFacing;

/** Renders the stored vanilla shield design as a chair back, without its hand grip. */
public class TileEntityShieldChairRenderer extends TileEntitySpecialRenderer<TileEntityShieldChair> {
	private final ModelShield shieldModel = new ModelShield();
	private final TileEntityBanner banner = new TileEntityBanner();

	@Override public void renderTileEntityAt(TileEntityShieldChair tile, double x, double y, double z,
			float partialTicks, int destroyStage) {
		ItemStack shield = tile.getShieldForRender();
		if (shield == null || tile.getWorld() == null) return;
		IBlockState state = tile.getWorld().getBlockState(tile.getPos());
		if (!(state.getBlock() instanceof ShieldChair)) return;
		EnumFacing facing = state.getValue(ShieldChair.FACING);
		if (shield.getSubCompound("BlockEntityTag", false) != null) {
			banner.setItemValues(shield);
			Minecraft.getMinecraft().getTextureManager().bindTexture(
					BannerTextures.SHIELD_DESIGNS.getResourceLocation(banner.getPatternResourceLocation(),
							banner.getPatternList(), banner.getColorList()));
		} else {
			Minecraft.getMinecraft().getTextureManager().bindTexture(BannerTextures.SHIELD_BASE_TEXTURE);
		}

		GlStateManager.pushMatrix();
		GlStateManager.enableRescaleNormal();
		try {
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			GlStateManager.translate(x + 0.5D, y + 1.0D, z + 0.5D);
			GlStateManager.rotate(yaw(facing), 0.0F, 1.0F, 0.0F);
			GlStateManager.translate(0.0D, 0.0D, 0.34D);
			// The original baked back was 10 x 16 pixels; vanilla's plate is 12 x 22.
			GlStateManager.scale(10.0F / 12.0F, 16.0F / 22.0F, 0.75F);
			GlStateManager.scale(1.0F, -1.0F, -1.0F);
			shieldModel.plate.render(0.0625F);
		} finally {
			GlStateManager.disableRescaleNormal();
			GlStateManager.popMatrix();
		}
	}

	private static float yaw(EnumFacing facing) {
		switch (facing) {
		case NORTH: return 180.0F;
		case EAST: return 90.0F;
		case WEST: return 270.0F;
		default: return 0.0F;
		}
	}
}
