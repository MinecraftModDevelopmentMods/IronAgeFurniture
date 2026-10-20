package zone.moddev.mc.ironagefurniture.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.phys.Vec3;
import zone.moddev.mc.ironagefurniture.api.SconceMetalData;
import zone.moddev.mc.ironagefurniture.api.blocks.base.FurnitureBlock;
import zone.moddev.mc.ironagefurniture.api.tile.MetalSconceBlockEntity;

/** Resolve optional metals from their real storage-block models, not guessed texture paths. */
public final class MetalSconceRenderer implements BlockEntityRenderer<MetalSconceBlockEntity, MetalSconceRenderer.State> {
    private static final double[][] FLOOR = {{6.5,8,5.5,9.5,9,6.5},{6.5,8,9.5,9.5,9,10.5},
            {5.5,8,6.5,6.5,9,9.5},{9.5,8,6.5,10.5,9,9.5},{7.5,0,7.5,8.5,2,8.5},
            {7.5,1,6.5,8.5,3,7.5},{7.5,2,5.5,8.5,8,6.5}};
    private static final double[][] WALL = {{1.5,9,6.5,2.5,10,9.5},{5.5,9,6.5,6.5,10,9.5},
            {2.5,9,9.5,5.5,10,10.5},{2.5,9,5.5,5.5,10,6.5},{.5,2,7.5,2.5,3,8.5},
            {-.5,3,7.5,2.5,4,8.5},{-.5,10,7.5,2.5,11,8.5},{-.5,4,7.5,.5,10,8.5}};
    private static final double[][] CANDLE_DISH = {{6,9,6,10,10,10},{6,10,5,10,11,6},
            {6,10,10,10,11,11},{5,10,6,6,11,10},{10,10,6,11,11,10}};
    private final BlockRenderDispatcher models;
    public MetalSconceRenderer(BlockEntityRendererProvider.Context context) { models = context.blockRenderDispatcher(); }
    public static final class State extends BlockEntityRenderState {
        final Frame frame = new Frame();
    }
    static final class Frame {
        final ExtractedBlockModel material = new ExtractedBlockModel();
        boolean visible, wall, candleDish;
        int rotation;
        final ExtractedBlockModel lamp = new ExtractedBlockModel();
        boolean hasLamp;
        Vec3 lampOrigin = Vec3.ZERO;
    }
    static void extractFrame(Frame frame, BlockState state, BlockRenderDispatcher models, BlockAndTintGetter level, BlockPos pos) {
        frame.visible = SconceMetalData.get(state).isBaseMetal();
        frame.wall = BuiltInRegistries.BLOCK.getKey(state.getBlock()).getPath().contains("sconce_wall");
        frame.candleDish = state.getBlock() instanceof zone.moddev.mc.ironagefurniture.api.blocks.lightsource.phasefour.CandleSconceBlock;
        frame.hasLamp = state.getBlock() instanceof zone.moddev.mc.ironagefurniture.api.blocks.lightsource.phasefour.RockSaltSconce
                && BuiltInRegistries.BLOCK.containsKey(zone.moddev.mc.ironagefurniture.api.blocks.lightsource.phasefour.RockSaltSconce.LAMP);
        if (frame.hasLamp) {
            frame.lamp.extract(models, BuiltInRegistries.BLOCK.getValue(zone.moddev.mc.ironagefurniture.api.blocks.lightsource.phasefour.RockSaltSconce.LAMP).defaultBlockState(), level, pos);
            var origin = zone.moddev.mc.ironagefurniture.api.blocks.base.FurnitureBlock.rotate(frame.wall ? .25 : .5, .5, state.getValue(FurnitureBlock.DIRECTION));
            frame.lampOrigin = new Vec3(origin.getA(), frame.wall ? .375 : .3125, origin.getB());
        }
        if (!frame.visible) return;
        frame.rotation = (int)state.getValue(FurnitureBlock.DIRECTION).toYRot() + (frame.wall ? 90 : 180);
        frame.material.extract(models, SconceMetalData.storageModel(SconceMetalData.get(state)), level, pos);
    }
    static void submitFrame(Frame frame, PoseStack poses, SubmitNodeCollector collector, int light) {
        if (frame.hasLamp) {
            poses.pushPose();
            poses.translate(frame.lampOrigin.x - .125, frame.lampOrigin.y, frame.lampOrigin.z - .125);
            poses.scale(.25F, .375F, .25F);
            frame.lamp.submit(poses, collector, light);
            poses.popPose();
        }
        if (!frame.visible) return;
        poses.pushPose();
        poses.translate(.5, 0, .5);
        poses.mulPose(Axis.YP.rotationDegrees(-frame.rotation));
        poses.translate(-.5, 0, -.5);
        for (double[] box : frame.wall ? WALL : FLOOR) {
            poses.pushPose();
            poses.translate(box[0]/16, box[1]/16, box[2]/16);
            poses.scale((float)(box[3]-box[0])/16, (float)(box[4]-box[1])/16, (float)(box[5]-box[2])/16);
            frame.material.submit(poses, collector, light);
            poses.popPose();
        }
        if (frame.candleDish) {
            poses.pushPose();
            if (frame.wall) poses.translate(-.25, 0, 0);
            for (double[] box : CANDLE_DISH) {
                poses.pushPose();
                poses.translate(box[0]/16, box[1]/16, box[2]/16);
                poses.scale((float)(box[3]-box[0])/16, (float)(box[4]-box[1])/16, (float)(box[5]-box[2])/16);
                frame.material.submit(poses, collector, light);
                poses.popPose();
            }
            poses.popPose();
        }
        poses.popPose();
    }
    @Override public State createRenderState() { return new State(); }
    @Override public void extractRenderState(MetalSconceBlockEntity entity, State state, float partialTicks,
            Vec3 camera, ModelFeatureRenderer.CrumblingOverlay breaking) {
        BlockEntityRenderer.super.extractRenderState(entity, state, partialTicks, camera, breaking);
        extractFrame(state.frame, entity.getBlockState(), models, entity.getLevel(), entity.getBlockPos());
    }
    @Override public void submit(State state, PoseStack poses, SubmitNodeCollector collector, CameraRenderState camera) {
        submitFrame(state.frame, poses, collector, state.lightCoords);
    }
}
