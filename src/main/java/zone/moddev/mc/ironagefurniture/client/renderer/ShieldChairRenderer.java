package zone.moddev.mc.ironagefurniture.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.phys.Vec3;
import zone.moddev.mc.ironagefurniture.api.blocks.base.FurnitureBlock;
import zone.moddev.mc.ironagefurniture.api.tile.ShieldChairBlockEntity;

/** Vanilla's shield renderer supplies patterns, enchantment glint and pack models. */
public final class ShieldChairRenderer implements BlockEntityRenderer<ShieldChairBlockEntity, ShieldChairRenderer.State> {
    private final ItemModelResolver models;
    public ShieldChairRenderer(BlockEntityRendererProvider.Context context) { models = context.itemModelResolver(); }
    public static final class State extends BlockEntityRenderState {
        private final ItemStackRenderState shield = new ItemStackRenderState();
        private Direction facing = Direction.NORTH;
    }
    @Override public State createRenderState() { return new State(); }
    @Override public void extractRenderState(ShieldChairBlockEntity entity, State state, float partialTicks,
            Vec3 camera, ModelFeatureRenderer.CrumblingOverlay breaking) {
        BlockEntityRenderer.super.extractRenderState(entity, state, partialTicks, camera, breaking);
        state.facing = entity.getBlockState().getValue(FurnitureBlock.DIRECTION);
        models.updateForTopItem(state.shield, entity.shield(), ItemDisplayContext.FIXED, entity.getLevel(), null, 0);
    }
    @Override public void submit(State state, PoseStack poses, SubmitNodeCollector collector, CameraRenderState camera) {
        submitShield(state.shield, state.facing, poses, collector, state.lightCoords);
    }
    public static void submitShield(ItemStackRenderState shield, Direction facing, PoseStack poses,
            SubmitNodeCollector collector, int light) {
        if (shield.isEmpty()) return;
        poses.pushPose();
        poses.translate(.5, 1, .5);
        float yaw = switch (facing) { case NORTH -> 180; case EAST -> 90; case WEST -> 270; default -> 0; };
        poses.rotateDegrees(Axis.YP, yaw);
        poses.translate(0, 0, .34);
        poses.scale(.8F, .8F, .8F);
        shield.submit(poses, collector, light, OverlayTexture.NO_OVERLAY, 0);
        poses.popPose();
    }
}
