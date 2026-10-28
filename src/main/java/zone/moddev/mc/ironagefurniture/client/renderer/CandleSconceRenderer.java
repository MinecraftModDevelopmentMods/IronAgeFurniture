package zone.moddev.mc.ironagefurniture.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.CandleBlock;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.phasefour.CandleSconceBlock;
import zone.moddev.mc.ironagefurniture.api.tile.CandleSconceBlockEntity;

/** Render installed vanilla single-candle models, including resource-pack replacements. */
public final class CandleSconceRenderer implements BlockEntityRenderer<CandleSconceBlockEntity> {
    private final BlockRenderDispatcher models;
    public CandleSconceRenderer(BlockEntityRendererProvider.Context context) { models = context.getBlockRenderDispatcher(); }
    @Override public void render(CandleSconceBlockEntity entity, float partialTicks, PoseStack poses,
            MultiBufferSource buffers, int light, int overlay) {
        var state = entity.getBlockState();
        MetalSconceRenderer.renderFrame(state, models, poses, buffers, light, overlay);
        if (!(state.getBlock() instanceof CandleSconceBlock holder)) return;
        var contents = entity.candles();
        for (int slot = 0; slot < contents.size(); slot++) {
            BlockItem item = (BlockItem)contents.get(slot).getItem();
            var candle = item.getBlock().defaultBlockState().setValue(CandleBlock.CANDLES, 1)
                    .setValue(CandleBlock.LIT, state.getValue(CandleSconceBlock.LIT));
            var origin = holder.candleOrigin(state, slot, contents.size());
            poses.pushPose();
            poses.translate(origin.x, origin.y, origin.z);
            poses.scale(CandleSconceBlock.CANDLE_SCALE, CandleSconceBlock.CANDLE_SCALE, CandleSconceBlock.CANDLE_SCALE);
            poses.translate(-.5, 0, -.5);
            MetalSconceRenderer.renderModel(models, candle, poses, buffers, light, overlay);
            poses.popPose();
        }
    }
}
