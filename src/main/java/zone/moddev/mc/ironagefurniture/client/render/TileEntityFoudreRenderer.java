package zone.moddev.mc.ironagefurniture.client.render;

import org.lwjgl.opengl.GL11;

import zone.moddev.mc.ironagefurniture.api.Blocks.Foudre;
import zone.moddev.mc.ironagefurniture.api.tile.TileEntityFoudre;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityFoudreRenderer extends TileEntitySpecialRenderer<TileEntityFoudre> {
	private static final double LABEL_Y = 0.82D;
	private static final double LABEL_FACE_OFFSET = 0.535D;
	private static final double BASE_CENTER = 0.5D;
	private static final double RIGHT_HALF_BLOCK_OFFSET = 0.5D;
	private static final double TEXT_FACE_OFFSET = 0.004D;
	private static final double TEXT_VERTICAL_OFFSET = 0.006D;
	private static final double PLAQUE_WIDTH = 0.82D;
	private static final double PLAQUE_HEIGHT = 0.18D;
	private static final double PLAQUE_INNER_INSET = 0.045D;
	private static final double PLAQUE_INNER_Z_OFFSET = 0.001D;
	private static final int MAX_TEXT_WIDTH = 72;
	private static final int LABEL_TEXT_COLOR = 0x1F140C;
	private static final float PLAQUE_OUTER_RED = 0.18F;
	private static final float PLAQUE_OUTER_GREEN = 0.11F;
	private static final float PLAQUE_OUTER_BLUE = 0.06F;
	private static final float PLAQUE_INNER_RED = 0.54F;
	private static final float PLAQUE_INNER_GREEN = 0.42F;
	private static final float PLAQUE_INNER_BLUE = 0.26F;
	private static final float TEXT_BASE_SCALE = 0.010416667F;
	private static final float FULL_BRIGHTNESS = 1.0F;
	private static final float YAW_EAST = 90.0F;
	private static final float YAW_NORTH = 180.0F;
	private static final float YAW_SOUTH = 0.0F;
	private static final float YAW_WEST = 270.0F;

	@Override
	public void renderTileEntityAt(TileEntityFoudre te, double x, double y, double z, float partialTicks,
			int destroyStage) {
		String label = te.getLabel();

		if (label == null || label.isEmpty()) {
			return;
		}

		EnumFacing facing = this.getFacing(te);
		EnumFacing front = facing.getOpposite();
		EnumFacing right = facing.rotateYCCW();
		double localX = BASE_CENTER + right.getFrontOffsetX() * RIGHT_HALF_BLOCK_OFFSET
			+ front.getFrontOffsetX() * LABEL_FACE_OFFSET;
		double localZ = BASE_CENTER + right.getFrontOffsetZ() * RIGHT_HALF_BLOCK_OFFSET
			+ front.getFrontOffsetZ() * LABEL_FACE_OFFSET;

		GlStateManager.pushMatrix();
		GlStateManager.translate(x + localX, y + LABEL_Y, z + localZ);
		GlStateManager.rotate(this.getFaceYaw(front), 0.0F, 1.0F, 0.0F);
		GlStateManager.disableLighting();
		this.drawPlaque();

		FontRenderer font = this.getFontRenderer();
		int width = font.getStringWidth(label);
		float scale = TEXT_BASE_SCALE;

		if (width > MAX_TEXT_WIDTH) {
			scale *= (float)MAX_TEXT_WIDTH / (float)width;
		}

		GlStateManager.translate(0.0D, TEXT_VERTICAL_OFFSET, TEXT_FACE_OFFSET);
		GlStateManager.scale(scale, -scale, scale);
		font.drawString(label, -font.getStringWidth(label) / 2, -font.FONT_HEIGHT / 2, LABEL_TEXT_COLOR);
		GlStateManager.enableLighting();
		GlStateManager.popMatrix();
	}

	private void drawPlaque() {
		GlStateManager.disableTexture2D();
		GlStateManager.disableCull();
		this.drawPlaqueQuad(PLAQUE_WIDTH, PLAQUE_HEIGHT, 0.0D, PLAQUE_OUTER_RED, PLAQUE_OUTER_GREEN,
			PLAQUE_OUTER_BLUE);
		this.drawPlaqueQuad(PLAQUE_WIDTH - PLAQUE_INNER_INSET, PLAQUE_HEIGHT - PLAQUE_INNER_INSET,
			PLAQUE_INNER_Z_OFFSET, PLAQUE_INNER_RED, PLAQUE_INNER_GREEN, PLAQUE_INNER_BLUE);
		GlStateManager.enableCull();
		GlStateManager.enableTexture2D();
	}

	private void drawPlaqueQuad(double width, double height, double z, float red, float green, float blue) {
		double halfWidth = width / 2.0D;
		double halfHeight = height / 2.0D;
		Tessellator tessellator = Tessellator.getInstance();
		VertexBuffer renderer = tessellator.getBuffer();
		renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
		renderer.pos(-halfWidth, -halfHeight, z).color(red, green, blue, FULL_BRIGHTNESS).endVertex();
		renderer.pos(halfWidth, -halfHeight, z).color(red, green, blue, FULL_BRIGHTNESS).endVertex();
		renderer.pos(halfWidth, halfHeight, z).color(red, green, blue, FULL_BRIGHTNESS).endVertex();
		renderer.pos(-halfWidth, halfHeight, z).color(red, green, blue, FULL_BRIGHTNESS).endVertex();
		tessellator.draw();
	}

	private EnumFacing getFacing(TileEntityFoudre te) {
		World world = te.getWorld();
		BlockPos pos = te.getPos();

		if (world != null && pos != null) {
			IBlockState state = world.getBlockState(pos);

			if (state.getBlock() instanceof Foudre) {
				return state.getValue(Foudre.FACING);
			}
		}

		return EnumFacing.SOUTH;
	}

	private float getFaceYaw(EnumFacing facing) {
		switch (facing) {
		case EAST:
			return YAW_EAST;
		case NORTH:
			return YAW_NORTH;
		case SOUTH:
			return YAW_SOUTH;
		case WEST:
			return YAW_WEST;
		default:
			return YAW_SOUTH;
		}
	}
}
