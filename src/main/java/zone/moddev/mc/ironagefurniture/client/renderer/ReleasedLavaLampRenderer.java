package zone.moddev.mc.ironagefurniture.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.state.CameraRenderState;
import zone.moddev.mc.ironagefurniture.api.entity.ReleasedLavaLamp;
import zone.moddev.mc.ironagefurniture.init.ModVanillaLights;

public final class ReleasedLavaLampRenderer extends EntityRenderer<ReleasedLavaLamp, ReleasedLavaLampRenderer.State> {
    private final BlockRenderDispatcher models;
    public ReleasedLavaLampRenderer(EntityRendererProvider.Context context) { super(context); models = context.getBlockRenderDispatcher(); }
    public static final class State extends EntityRenderState { private final ExtractedBlockModel lamp = new ExtractedBlockModel(); }
    @Override public State createRenderState() { return new State(); }
    @Override public void extractRenderState(ReleasedLavaLamp entity, State state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.lamp.extract(models, ModVanillaLights.light_metal_ironage_block_floor_lava_clear.get().defaultBlockState(), entity.level(), entity.blockPosition());
    }
    @Override public void submit(State state, PoseStack poses, SubmitNodeCollector collector, CameraRenderState camera) {
        poses.pushPose();
        poses.translate(-.5, 0, -.5);
        state.lamp.submit(poses, collector, state.lightCoords);
        poses.popPose();
        super.submit(state, poses, collector, camera);
    }
}
