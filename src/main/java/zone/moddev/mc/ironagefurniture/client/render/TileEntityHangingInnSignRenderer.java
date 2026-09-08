package zone.moddev.mc.ironagefurniture.client.render;

import zone.moddev.mc.ironagefurniture.api.Blocks.HangingInnSign;
import zone.moddev.mc.ironagefurniture.api.tile.TileEntityHangingInnSign;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.ITextComponent;

public class TileEntityHangingInnSignRenderer extends TileEntitySpecialRenderer<TileEntityHangingInnSign> {
	private static final int TEXT_COLOR = 0xE6C98F;
	private static final int FIRST_LINE_Y = -15;
	private static final int LINE_HEIGHT = 10;
	private static final double TEXT_CENTER_Y = 0.395D;
	private static final double EDITOR_TEXT_CENTER_Y = -0.15D;
	private static final double TEXT_FACE_Z = -0.107D;
	private static final float MAX_TEXT_SCALE = 0.0105F;
	private static final float MAX_TEXT_WIDTH = 0.70F;

	@Override
	public void renderTileEntityAt(TileEntityHangingInnSign sign, double x, double y, double z,
			float partialTicks, int destroyStage) {
		if (sign.getWorld() == null) return;
		IBlockState state = sign.getWorld().getBlockState(sign.getPos());
		if (!(state.getBlock() instanceof HangingInnSign)) return;
		EnumFacing facing = state.getValue(HangingInnSign.FACING);
		Minecraft minecraft = Minecraft.getMinecraft();
		FontRenderer font = minecraft.fontRendererObj;
		boolean editing = minecraft.currentScreen instanceof GuiEditSign;
		String[] lines = new String[sign.signText.length];
		int widest = 1;

		for (int line = 0; line < lines.length; line++) {
			ITextComponent component = sign.signText[line];
			lines[line] = component == null ? "" : component.getFormattedText();
			widest = Math.max(widest, font.getStringWidth(lines[line]));
		}

		if (widest == 1) {
			lines[0] = sign.getInnName();
			widest = font.getStringWidth(lines[0]);
		}

		float scale = Math.min(MAX_TEXT_SCALE, MAX_TEXT_WIDTH / Math.max(1, widest));

		GlStateManager.pushMatrix();
		GlStateManager.translate(x + 0.5D, y + (editing ? EDITOR_TEXT_CENTER_Y : TEXT_CENTER_Y), z + 0.5D);

		if (editing) {
			// GuiEditSign applies the vanilla wall-sign rotation before invoking the tile renderer.
			// IAF metadata uses horizontal-facing indices, so cancel the remaining half-turn here
			// instead of applying the in-world orientation a second time.
			if ((sign.getBlockMetadata() & 3) != EnumFacing.NORTH.getHorizontalIndex()) {
				GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
			}
		} else {
			GlStateManager.rotate(180.0F - facing.getHorizontalAngle(), 0.0F, 1.0F, 0.0F);
		}

		GlStateManager.disableLighting();
		renderTextFace(font, lines, scale, sign.lineBeingEdited, false);

		if (!editing) {
			renderTextFace(font, lines, scale, -1, true);
		}

		GlStateManager.enableLighting();
		GlStateManager.popMatrix();
	}

	private static void renderTextFace(FontRenderer font, String[] lines, float scale, int editedLine,
			boolean reverse) {
		GlStateManager.pushMatrix();

		if (reverse) {
			GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
		}

		GlStateManager.translate(0.0D, 0.0D, TEXT_FACE_Z);
		GlStateManager.scale(-scale, -scale, scale);

		for (int line = 0; line < lines.length; line++) {
			String text = lines[line];

			if (line == editedLine) {
				text = "> " + text + " <";
			}

			if (!text.isEmpty()) {
				font.drawString(text, -font.getStringWidth(text) / 2,
					FIRST_LINE_Y + line * LINE_HEIGHT, TEXT_COLOR);
			}
		}

		GlStateManager.popMatrix();
	}
}
