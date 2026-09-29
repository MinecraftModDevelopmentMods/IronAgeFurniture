package zone.moddev.mc.ironagefurniture.client.model;

import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import javax.vecmath.Matrix4f;
import org.apache.commons.lang3.tuple.Pair;
import zone.moddev.mc.ironagefurniture.api.Enumerations.UpholsteryColour;
import zone.moddev.mc.ironagefurniture.api.Properties.UpholsteryColourProperty;
import zone.moddev.mc.ironagefurniture.api.UpholsteryColourHelper;
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
public final class UpholsteryBakedModel implements IPerspectiveAwareModel {
	private final Map<UpholsteryColour, IBakedModel> variants;
	private final IBakedModel fallback;
	private final ItemOverrideList overrides;

	public UpholsteryBakedModel(Map<UpholsteryColour, IBakedModel> variants) {
		this.variants = Collections.unmodifiableMap(new EnumMap<UpholsteryColour, IBakedModel>(variants));
		fallback = this.variants.get(UpholsteryColour.RED);
		overrides = new ItemOverrideList(Collections.<ItemOverride>emptyList()) {
			@Override public IBakedModel handleItemState(IBakedModel original, ItemStack stack,
					World world, EntityLivingBase entity) {
				return modelFor(UpholsteryColourHelper.getColour(stack));
			}
		};
	}

	private IBakedModel modelFor(UpholsteryColour colour) {
		IBakedModel model = variants.get(colour);
		return model == null ? fallback : model;
	}

	@Override public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long random) {
		UpholsteryColour colour = UpholsteryColour.RED;
		IBlockState clean = state;
		if (state instanceof IExtendedBlockState) {
			IExtendedBlockState extended = (IExtendedBlockState)state;
			if (extended.getUnlistedNames().contains(UpholsteryColourProperty.COLOUR)) {
				UpholsteryColour stored = extended.getValue(UpholsteryColourProperty.COLOUR);
				colour = stored == null ? UpholsteryColour.RED : stored;
			}
			clean = extended.getClean();
		}
		return modelFor(colour).getQuads(clean, side, random);
	}

	@Override public boolean isAmbientOcclusion() { return fallback.isAmbientOcclusion(); }
	@Override public boolean isGui3d() { return fallback.isGui3d(); }
	@Override public boolean isBuiltInRenderer() { return fallback.isBuiltInRenderer(); }
	@Override public TextureAtlasSprite getParticleTexture() { return fallback.getParticleTexture(); }
	@Override @SuppressWarnings("deprecation") public ItemCameraTransforms getItemCameraTransforms() {
		return fallback.getItemCameraTransforms();
	}
	@Override public ItemOverrideList getOverrides() { return overrides; }
	@Override public Pair<? extends IBakedModel, Matrix4f> handlePerspective(TransformType type) {
		return fallback instanceof IPerspectiveAwareModel
				? ((IPerspectiveAwareModel)fallback).handlePerspective(type) : Pair.of(this, null);
	}
}
