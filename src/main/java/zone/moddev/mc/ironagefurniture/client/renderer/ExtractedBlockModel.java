package zone.moddev.mc.ironagefurniture.client.renderer;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.RenderTypeHelper;
import net.minecraftforge.client.model.data.ModelData;

/** A 1.21.11 block-model snapshot; submission never consults a world or model resolver. */
final class ExtractedBlockModel {
    private record Quad(BakedQuad geometry, int tint) { }
    private record Batch(RenderType type, List<Quad> quads) { }
    private List<Batch> batches = List.of();

    void extract(BlockRenderDispatcher models, BlockState state, BlockAndTintGetter level, BlockPos pos) {
        Map<RenderType, List<Quad>> collected = new LinkedHashMap<>();
        var colors = Minecraft.getInstance().getBlockColors();
        var model = models.getBlockModel(state);
        for (var layer : model.getRenderTypes(state, RandomSource.create(state.getSeed(pos)), ModelData.EMPTY)) {
            List<net.minecraft.client.renderer.block.model.BlockModelPart> parts = new ArrayList<>();
            model.collectParts(RandomSource.create(state.getSeed(pos)), parts, ModelData.EMPTY, layer);
            List<Quad> quads = collected.computeIfAbsent(RenderTypeHelper.getEntityRenderType(layer), ignored -> new ArrayList<>());
            for (var part : parts) {
            for (int face = 0; face <= Direction.values().length; face++) {
                Direction direction = face == Direction.values().length ? null : Direction.values()[face];
                for (BakedQuad quad : part.getQuads(direction)) {
                    quads.add(new Quad(quad, quad.isTinted() ? colors.getColor(state, level, pos, quad.tintIndex()) : -1));
                }
            }
            }
        }
        batches = collected.entrySet().stream().map(entry -> new Batch(entry.getKey(), List.copyOf(entry.getValue()))).toList();
    }

    void submit(PoseStack poses, SubmitNodeCollector collector, int light) {
        for (Batch batch : batches) {
            collector.submitCustomGeometry(poses, batch.type(), (pose, consumer) -> {
                for (Quad quad : batch.quads()) {
                    int color = quad.tint();
                    consumer.putBulkData(pose, quad.geometry(), ((color >> 16) & 255) / 255F,
                            ((color >> 8) & 255) / 255F, (color & 255) / 255F, 1F, light, OverlayTexture.NO_OVERLAY);
                }
            });
        }
    }
}
