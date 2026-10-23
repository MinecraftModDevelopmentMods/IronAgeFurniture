package zone.moddev.mc.ironagefurniture.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.data.ModelData;
import zone.moddev.mc.ironagefurniture.api.SconceMetalData;
import zone.moddev.mc.ironagefurniture.api.blocks.base.FurnitureBlock;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.phasefour.CandleSconceBlock;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.phasefour.RockSaltSconce;
import zone.moddev.mc.ironagefurniture.api.tile.MetalSconceBlockEntity;

/** Resolve optional metals from their actual block models, not guessed texture paths. */
public final class MetalSconceRenderer implements BlockEntityRenderer<MetalSconceBlockEntity> {
    private static final double[][] FLOOR = {{6.5,8,5.5,9.5,9,6.5},{6.5,8,9.5,9.5,9,10.5},
            {5.5,8,6.5,6.5,9,9.5},{9.5,8,6.5,10.5,9,9.5},{7.5,0,7.5,8.5,2,8.5},
            {7.5,1,6.5,8.5,3,7.5},{7.5,2,5.5,8.5,8,6.5}};
    private static final double[][] WALL = {{1.5,9,6.5,2.5,10,9.5},{5.5,9,6.5,6.5,10,9.5},
            {2.5,9,9.5,5.5,10,10.5},{2.5,9,5.5,5.5,10,6.5},{.5,2,7.5,2.5,3,8.5},
            {-.5,3,7.5,2.5,4,8.5},{-.5,10,7.5,2.5,11,8.5},{-.5,4,7.5,.5,10,8.5}};
    private static final double[][] CANDLE_DISH = {{6,9,6,10,10,10},{6,10,5,10,11,6},
            {6,10,10,10,11,11},{5,10,6,6,11,10},{10,10,6,11,11,10}};
    private final BlockRenderDispatcher models;
    public MetalSconceRenderer(BlockEntityRendererProvider.Context context) { models = context.getBlockRenderDispatcher(); }

    static void renderFrame(BlockState state, BlockRenderDispatcher models, PoseStack poses,
            MultiBufferSource buffers, int light, int overlay) {
        boolean wall = BuiltInRegistries.BLOCK.getKey(state.getBlock()).getPath().contains("sconce_wall");
        if (state.getBlock() instanceof RockSaltSconce && BuiltInRegistries.BLOCK.containsKey(RockSaltSconce.LAMP)) {
            var origin = FurnitureBlock.rotate(wall ? .25 : .5, .5, state.getValue(FurnitureBlock.DIRECTION));
            poses.pushPose();
            poses.translate(origin.getA() - .125, wall ? .375 : .3125, origin.getB() - .125);
            poses.scale(.25F, .375F, .25F);
            renderModel(models, BuiltInRegistries.BLOCK.get(RockSaltSconce.LAMP).defaultBlockState(), poses, buffers, light, overlay);
            poses.popPose();
        }
        if (!SconceMetalData.get(state).isBaseMetal()) return;
        BlockState material = SconceMetalData.storageModel(SconceMetalData.get(state));
        int rotation = (int)state.getValue(FurnitureBlock.DIRECTION).toYRot() + (wall ? 90 : 180);
        poses.pushPose();
        poses.translate(.5, 0, .5);
        poses.mulPose(Axis.YP.rotationDegrees(-rotation));
        poses.translate(-.5, 0, -.5);
        for (double[] box : wall ? WALL : FLOOR) renderBox(box, material, models, poses, buffers, light, overlay);
        if (state.getBlock() instanceof CandleSconceBlock) {
            poses.pushPose();
            if (wall) poses.translate(-.25, 0, 0);
            for (double[] box : CANDLE_DISH) renderBox(box, material, models, poses, buffers, light, overlay);
            poses.popPose();
        }
        poses.popPose();
    }
    private static void renderBox(double[] box, BlockState material, BlockRenderDispatcher models,
            PoseStack poses, MultiBufferSource buffers, int light, int overlay) {
        poses.pushPose();
        poses.translate(box[0]/16, box[1]/16, box[2]/16);
        poses.scale((float)(box[3]-box[0])/16, (float)(box[4]-box[1])/16, (float)(box[5]-box[2])/16);
        renderModel(models, material, poses, buffers, light, overlay);
        poses.popPose();
    }
    static void renderModel(BlockRenderDispatcher models, BlockState state, PoseStack poses,
            MultiBufferSource buffers, int light, int overlay) {
        models.renderSingleBlock(state, poses, buffers, light, overlay, ModelData.EMPTY, null);
    }
    @Override public void render(MetalSconceBlockEntity entity, float partialTicks, PoseStack poses,
            MultiBufferSource buffers, int light, int overlay) {
        renderFrame(entity.getBlockState(), models, poses, buffers, light, overlay);
    }
}
