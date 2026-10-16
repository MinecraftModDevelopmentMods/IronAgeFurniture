package zone.moddev.mc.ironagefurniture.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.block.BlockModelResolver;
import net.minecraft.client.renderer.block.model.BlockDisplayContext;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import zone.moddev.mc.ironagefurniture.api.blocks.base.FurnitureBlock;
import zone.moddev.mc.ironagefurniture.api.entity.FallingShieldChair;

/** The falling frame and its stored shield are extracted together. */
public final class FallingShieldChairRenderer extends EntityRenderer<FallingShieldChair, FallingShieldChairRenderer.State> {
    private static final BlockDisplayContext DISPLAY = BlockDisplayContext.create();
    private final BlockModelResolver blocks;
    private final ShieldChairRenderer shields;
    public FallingShieldChairRenderer(EntityRendererProvider.Context context) {
        super(context);
        blocks = context.getBlockModelResolver();
        shields = new ShieldChairRenderer(context.getModelSet(), context.getSprites());
    }
    public static final class State extends EntityRenderState {
        private final BlockModelRenderState frame = new BlockModelRenderState();
        private ShieldChairRenderer.ShieldState shield = ShieldChairRenderer.extractShield(net.minecraft.world.item.ItemStack.EMPTY);
        private Direction facing = Direction.NORTH;
    }
    @Override public State createRenderState() { return new State(); }
    @Override public void extractRenderState(FallingShieldChair entity, State state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        blocks.update(state.frame, entity.visibleState(), DISPLAY);
        if (entity.visibleState().hasProperty(FurnitureBlock.DIRECTION)) state.facing = entity.visibleState().getValue(FurnitureBlock.DIRECTION);
        state.shield = ShieldChairRenderer.extractShield(entity.shield());
    }
    @Override public void submit(State state, PoseStack poses, SubmitNodeCollector collector, CameraRenderState camera) {
        poses.pushPose();
        poses.translate(-.5, 0, -.5);
        state.frame.submit(poses, collector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
        shields.submitShield(state.shield, state.facing, poses, collector, state.lightCoords);
        poses.popPose();
        super.submit(state, poses, collector, camera);
    }
}
