package zone.moddev.mc.ironagefurniture.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import zone.moddev.mc.ironagefurniture.api.blocks.base.FurnitureBlock;
import zone.moddev.mc.ironagefurniture.api.entity.FallingShieldChair;

/** The in-flight wooden frame and stored shield share the placed model's transforms. */
public final class FallingShieldChairRenderer extends EntityRenderer<FallingShieldChair> {
    private final BlockRenderDispatcher blocks;
    private final ShieldChairRenderer shields;

    public FallingShieldChairRenderer(EntityRendererProvider.Context context) {
        super(context);
        blocks = context.getBlockRenderDispatcher();
        shields = new ShieldChairRenderer(context.getModelSet());
        shadowRadius = .5F;
    }

    @Override public void render(FallingShieldChair entity, float yaw, float partialTicks, PoseStack poses,
            MultiBufferSource buffers, int light) {
        BlockState frame = entity.visibleState();
        poses.pushPose();
        try {
            poses.translate(-.5, 0, -.5);
            blocks.renderSingleBlock(frame, poses, buffers, light, OverlayTexture.NO_OVERLAY);
            Direction facing = frame.hasProperty(FurnitureBlock.DIRECTION) ? frame.getValue(FurnitureBlock.DIRECTION) : Direction.NORTH;
            shields.renderShield(entity.shield(), facing, poses, buffers, light, OverlayTexture.NO_OVERLAY);
        } finally { poses.popPose(); }
        super.render(entity, yaw, partialTicks, poses, buffers, light);
    }

    @Override public ResourceLocation getTextureLocation(FallingShieldChair entity) { return TextureAtlas.LOCATION_BLOCKS; }
}
