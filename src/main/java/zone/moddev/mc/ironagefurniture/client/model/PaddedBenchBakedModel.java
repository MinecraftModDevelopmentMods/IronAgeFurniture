package zone.moddev.mc.ironagefurniture.client.model;

import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import javax.vecmath.Matrix4f;

import org.apache.commons.lang3.tuple.Pair;

import zone.moddev.mc.ironagefurniture.api.Enumerations.PaddedBenchColour;
import zone.moddev.mc.ironagefurniture.api.Properties.PaddedBenchColourProperty;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ItemOverride;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public final class PaddedBenchBakedModel implements IPerspectiveAwareModel {
	private final Map<PaddedBenchColour, IBakedModel> variants;
	private final IBakedModel fallback;
	private final ItemOverrideList itemOverrides;

	public PaddedBenchBakedModel(Map<PaddedBenchColour, IBakedModel> variants) {
		this.variants = Collections.unmodifiableMap(
				new EnumMap<PaddedBenchColour, IBakedModel>(variants));
		this.fallback = this.variants.get(PaddedBenchColour.RED);
		this.itemOverrides = new ItemOverrideList(Collections.<ItemOverride>emptyList()) {
			@Override
			public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stack,
					World world, EntityLivingBase entity) {
				return PaddedBenchBakedModel.this.modelFor(
						PaddedBenchColour.byItemMetadata(stack.getMetadata()));
			}
		};
	}

	private IBakedModel modelFor(PaddedBenchColour colour) {
		IBakedModel model = this.variants.get(colour);
		return model == null ? this.fallback : model;
	}

	@Override
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long random) {
		PaddedBenchColour colour = PaddedBenchColour.RED;
		IBlockState cleanState = state;
		if (state instanceof IExtendedBlockState) {
			IExtendedBlockState extendedState = (IExtendedBlockState)state;
			if (extendedState.getUnlistedNames().contains(PaddedBenchColourProperty.COLOUR)) {
				PaddedBenchColour stored = extendedState.getValue(PaddedBenchColourProperty.COLOUR);
				colour = stored == null ? PaddedBenchColour.RED : stored;
			}
			cleanState = extendedState.getClean();
		}
		return this.modelFor(colour).getQuads(cleanState, side, random);
	}

	@Override
	public boolean isAmbientOcclusion() {
		return this.fallback.isAmbientOcclusion();
	}

	@Override
	public boolean isGui3d() {
		return this.fallback.isGui3d();
	}

	@Override
	public boolean isBuiltInRenderer() {
		return this.fallback.isBuiltInRenderer();
	}

	@Override
	public TextureAtlasSprite getParticleTexture() {
		return this.fallback.getParticleTexture();
	}

	@Override
	@SuppressWarnings("deprecation")
	public ItemCameraTransforms getItemCameraTransforms() {
		return this.fallback.getItemCameraTransforms();
	}

	@Override
	public ItemOverrideList getOverrides() {
		return this.itemOverrides;
	}

	@Override
	public Pair<? extends IBakedModel, Matrix4f> handlePerspective(TransformType transformType) {
		if (this.fallback instanceof IPerspectiveAwareModel) {
			return ((IPerspectiveAwareModel)this.fallback).handlePerspective(transformType);
		}
		return Pair.of(this, null);
	}
}
