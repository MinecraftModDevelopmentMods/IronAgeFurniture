package zone.moddev.mc.ironagefurniture.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BannerRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.Unit;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BannerPatternLayers;
import net.minecraft.world.phys.Vec3;
import zone.moddev.mc.ironagefurniture.api.blocks.base.FurnitureBlock;
import zone.moddev.mc.ironagefurniture.api.tile.ShieldChairBlockEntity;

/** Draw the stored shield as a full-size chair back, without its hand grip. */
public final class ShieldChairRenderer implements BlockEntityRenderer<ShieldChairBlockEntity, ShieldChairRenderer.State> {
    private final Model.Simple plate;
    private final SpriteGetter sprites;

    public ShieldChairRenderer(BlockEntityRendererProvider.Context context) {
        this(context.entityModelSet(), context.sprites());
    }

    public ShieldChairRenderer(EntityModelSet models, SpriteGetter sprites) {
        // Keep the pack's shield plate and textures, but never submit the handle.
        plate = new Model.Simple(models.bakeLayer(ModelLayers.SHIELD).getChild("plate"), RenderTypes::entitySolid);
        this.sprites = sprites;
    }

    public record ShieldState(DataComponentMap components, boolean present, boolean foil) {
        private static final ShieldState EMPTY = new ShieldState(DataComponentMap.EMPTY, false, false);
    }

    public static final class State extends BlockEntityRenderState {
        private ShieldState shield = ShieldState.EMPTY;
        private Direction facing = Direction.NORTH;
    }
    @Override public State createRenderState() { return new State(); }
    @Override public void extractRenderState(ShieldChairBlockEntity entity, State state, float partialTicks,
            Vec3 camera, ModelFeatureRenderer.CrumblingOverlay breaking) {
        BlockEntityRenderer.super.extractRenderState(entity, state, partialTicks, camera, breaking);
        state.facing = entity.getBlockState().getValue(FurnitureBlock.DIRECTION);
        state.shield = extractShield(entity.shield());
    }
    @Override public void submit(State state, PoseStack poses, SubmitNodeCollector collector, CameraRenderState camera) {
        submitShield(state.shield, state.facing, poses, collector, state.lightCoords);
    }
    public static ShieldState extractShield(ItemStack shield) {
        return shield.isEmpty() ? ShieldState.EMPTY : new ShieldState(shield.immutableComponents(), true, shield.hasFoil());
    }

    public void submitShield(ShieldState shield, Direction facing, PoseStack poses,
            SubmitNodeCollector collector, int light) {
        if (!shield.present()) return;
        poses.pushPose();
        try {
            poses.translate(.5, 1, .5);
            float yaw = switch (facing) { case NORTH -> 180; case EAST -> 90; case WEST -> 270; default -> 0; };
            poses.mulPose(Axis.YP.rotationDegrees(yaw));
            poses.translate(0, 0, .34);
            // Match the original 10 x 16 pixel back, not the inventory item's transform.
            poses.scale(10F / 12F, -16F / 22F, -.75F);

            BannerPatternLayers patterns = shield.components().getOrDefault(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY);
            DyeColor color = shield.components().get(DataComponents.BASE_COLOR);
            boolean patterned = color != null || !patterns.layers().isEmpty();
            SpriteId base = patterned ? Sheets.SHIELD_BASE : Sheets.SHIELD_BASE_NO_PATTERN;
            collector.submitModel(plate, Unit.INSTANCE, poses, light, OverlayTexture.NO_OVERLAY, -1, base, sprites, 0, null);
            if (patterned) {
                BannerRenderer.submitPatterns(sprites, poses, collector, light, OverlayTexture.NO_OVERLAY,
                        plate, Unit.INSTANCE, false, color == null ? DyeColor.WHITE : color, patterns, null);
            }
            // Follow the actual 26.1.2 vanilla shield renderer's ordered glint
            // pass, retaining only the plate rather than reintroducing a handle.
            if (shield.foil()) {
                collector.order(patterns.layers().size() + 1).submitModel(plate, Unit.INSTANCE, poses,
                        RenderTypes.entityGlint(), light, OverlayTexture.NO_OVERLAY, -1, sprites.get(base), 0, null);
            }
        } finally { poses.popPose(); }
    }
}
