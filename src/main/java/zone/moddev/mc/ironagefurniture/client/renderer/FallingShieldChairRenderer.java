package zone.moddev.mc.ironagefurniture.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.core.Direction;
import zone.moddev.mc.ironagefurniture.api.blocks.base.FurnitureBlock;
import zone.moddev.mc.ironagefurniture.api.entity.FallingShieldChair;

/** The falling frame and its stored shield are extracted together. */
public final class FallingShieldChairRenderer extends EntityRenderer<FallingShieldChair, FallingShieldChairRenderer.State> {
    private final BlockRenderDispatcher blocks;
    private final ShieldChairRenderer shields;
    public FallingShieldChairRenderer(EntityRendererProvider.Context context) {
        super(context);
        blocks = context.getBlockRenderDispatcher();
        shields = new ShieldChairRenderer(context.getModelSet(), context.getMaterials());
    }
    public static final class State extends EntityRenderState {
        private final ExtractedBlockModel frame = new ExtractedBlockModel();
        private ShieldChairRenderer.ShieldState shield = ShieldChairRenderer.extractShield(net.minecraft.world.item.ItemStack.EMPTY);
        private Direction facing = Direction.NORTH;
    }
    @Override public State createRenderState() { return new State(); }
    @Override public void extractRenderState(FallingShieldChair entity, State state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.frame.extract(blocks, entity.visibleState(), entity.level(), entity.blockPosition());
        if (entity.visibleState().hasProperty(FurnitureBlock.DIRECTION)) state.facing = entity.visibleState().getValue(FurnitureBlock.DIRECTION);
        state.shield = ShieldChairRenderer.extractShield(entity.shield());
    }
    @Override public void submit(State state, PoseStack poses, SubmitNodeCollector collector, CameraRenderState camera) {
        poses.pushPose();
        poses.translate(-.5, 0, -.5);
        state.frame.submit(poses, collector, state.lightCoords);
        shields.submitShield(state.shield, state.facing, poses, collector, state.lightCoords);
        poses.popPose();
        super.submit(state, poses, collector, camera);
    }
}
