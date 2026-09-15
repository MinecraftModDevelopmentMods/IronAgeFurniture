package zone.moddev.mc.ironagefurniture.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;

import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.glow.LightSourceGlowdust;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.red.LightSourceRed;

import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.FallingBlockRenderState;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Preserves Forge's normal falling-block renderer while using the model-aware
 * block submission path for Iron Age Furniture's transparent falling lamps.
 *
 * <p>Forge 61's moving-block submission still resolves one legacy render layer.
 * The supported render-layer registration API no longer updates that legacy
 * lookup, so a lamp otherwise falls back to the opaque solid layer.</p>
 */
public final class FurnitureFallingBlockRenderer
		extends EntityRenderer<FallingBlockEntity, FallingBlockRenderState> {

	public FurnitureFallingBlockRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.shadowRadius = 0.5F;
	}

	@Override
	public boolean shouldRender(FallingBlockEntity entity, Frustum frustum, double x, double y, double z) {
		return super.shouldRender(entity, frustum, x, y, z)
				&& entity.getBlockState() != entity.level().getBlockState(entity.blockPosition());
	}

	@Override
	public void submit(FallingBlockRenderState state, PoseStack poseStack,
			SubmitNodeCollector collector, CameraRenderState cameraState) {
		BlockState blockState = state.movingBlockRenderState.blockState;
		if (blockState.getRenderShape() == RenderShape.MODEL) {
			poseStack.pushPose();
			poseStack.translate(-0.5D, 0.0D, -0.5D);
			if (usesTransparentLampModel(blockState)) {
				collector.submitBlock(poseStack, blockState, state.lightCoords,
						OverlayTexture.NO_OVERLAY, state.outlineColor);
			} else {
				collector.submitMovingBlock(poseStack, state.movingBlockRenderState);
			}
			poseStack.popPose();
			super.submit(state, poseStack, collector, cameraState);
		}
	}

	private static boolean usesTransparentLampModel(BlockState state) {
		return state.getBlock() instanceof LightSourceGlowdust
				|| state.getBlock() instanceof LightSourceRed;
	}

	@Override
	public FallingBlockRenderState createRenderState() {
		return new FallingBlockRenderState();
	}

	@Override
	public void extractRenderState(FallingBlockEntity entity, FallingBlockRenderState state,
			float partialTick) {
		super.extractRenderState(entity, state, partialTick);
		BlockPos blockPos = BlockPos.containing(entity.getX(), entity.getBoundingBox().maxY, entity.getZ());
		state.movingBlockRenderState.randomSeedPos = entity.getStartPos();
		state.movingBlockRenderState.blockPos = blockPos;
		state.movingBlockRenderState.blockState = entity.getBlockState();
		state.movingBlockRenderState.biome = entity.level().getBiome(blockPos);
		state.movingBlockRenderState.level = entity.level();
	}
}
