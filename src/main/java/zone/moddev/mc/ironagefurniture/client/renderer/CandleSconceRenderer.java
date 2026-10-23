package zone.moddev.mc.ironagefurniture.client.renderer;

import java.util.ArrayList;
import java.util.List;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.block.BlockModelResolver;
import net.minecraft.client.renderer.block.model.BlockDisplayContext;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.phasefour.CandleSconceBlock;
import zone.moddev.mc.ironagefurniture.api.tile.CandleSconceBlockEntity;

/** Render the installed vanilla block models, including resource-pack replacements. */
public final class CandleSconceRenderer implements BlockEntityRenderer<CandleSconceBlockEntity, CandleSconceRenderer.State> {
    private static final BlockDisplayContext DISPLAY = BlockDisplayContext.create();
    private final BlockModelResolver models;
    public CandleSconceRenderer(BlockEntityRendererProvider.Context context) { models = context.blockModelResolver(); }
    public static final class State extends BlockEntityRenderState {
        private List<Part> candles = List.of();
        private final MetalSconceRenderer.Frame frame = new MetalSconceRenderer.Frame();
    }
    private record Part(BlockModelRenderState model, Vec3 origin) { }
    @Override public State createRenderState() { return new State(); }
    @Override public void extractRenderState(CandleSconceBlockEntity entity, State state, float partialTicks,
            Vec3 cameraPosition, ModelFeatureRenderer.CrumblingOverlay breaking) {
        BlockEntityRenderer.super.extractRenderState(entity, state, partialTicks, cameraPosition, breaking);
        BlockState blockState = entity.getBlockState();
        MetalSconceRenderer.extractFrame(state.frame, blockState, models);
        if (!(blockState.getBlock() instanceof CandleSconceBlock holder)) { state.candles = List.of(); return; }
        List<ItemStack> contents = entity.candles();
        List<Part> parts = new ArrayList<>(contents.size());
        for (int slot = 0; slot < contents.size(); slot++) {
            BlockItem item = (BlockItem)contents.get(slot).getItem();
            BlockState candle = item.getBlock().defaultBlockState().setValue(CandleBlock.CANDLES, 1)
                    .setValue(CandleBlock.LIT, blockState.getValue(CandleSconceBlock.LIT));
            BlockModelRenderState model = new BlockModelRenderState();
            models.update(model, candle, DISPLAY);
            parts.add(new Part(model, holder.candleOrigin(blockState, slot, contents.size())));
        }
        state.candles = List.copyOf(parts);
    }
    @Override public void submit(State state, PoseStack poses, SubmitNodeCollector collector, CameraRenderState camera) {
        // Submission uses only the extracted snapshot, never the live world.
        MetalSconceRenderer.submitFrame(state.frame, poses, collector, state.lightCoords);
        for (Part part : state.candles) {
            poses.pushPose();
            poses.translate(part.origin.x, part.origin.y, part.origin.z);
            poses.scale(CandleSconceBlock.CANDLE_SCALE, CandleSconceBlock.CANDLE_SCALE, CandleSconceBlock.CANDLE_SCALE);
            poses.translate(-.5, 0, -.5);
            part.model.submit(poses, collector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
            poses.popPose();
        }
    }
}
