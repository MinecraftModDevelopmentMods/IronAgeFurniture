package zone.moddev.mc.ironagefurniture.client.model;

import java.util.Collection;
import java.util.EnumMap;
import java.util.LinkedHashSet;
import java.util.Map;
import zone.moddev.mc.ironagefurniture.Ironagefurniture;
import zone.moddev.mc.ironagefurniture.api.Enumerations.UpholsteryColour;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.IRetexturableModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/** Uses the same pre-baked per-wool-texture approach as the Phase 3 padded benches. */
@SideOnly(Side.CLIENT)
public enum UpholsteryModelLoader implements ICustomModelLoader {
	INSTANCE;

	private static final String BLOCK_PREFIX = "models/block/upholstered/";
	private static final String ITEM_PREFIX = "models/item/upholstered/";

	@Override public boolean accepts(ResourceLocation location) {
		if (!Ironagefurniture.MODID.equals(location.getResourceDomain())) return false;
		String path = location.getResourcePath();
		return path.startsWith(BLOCK_PREFIX) || path.startsWith(ITEM_PREFIX);
	}

	@Override public IModel loadModel(ResourceLocation location) throws Exception {
		String path = location.getResourcePath();
		if (path.startsWith(BLOCK_PREFIX)) {
			return new ColouredModel(ModelLoaderRegistry.getModel(new ResourceLocation(
					location.getResourceDomain(), "block/" + path.substring(BLOCK_PREFIX.length()))));
		}
		String itemPath = path.substring(ITEM_PREFIX.length());
		int slash = itemPath.indexOf('/');
		if (slash <= 0 || slash == itemPath.length() - 1)
			throw new IllegalArgumentException("Invalid upholstered item model: " + location);
		String colourName = itemPath.substring(0, slash);
		UpholsteryColour colour = UpholsteryColour.byName(colourName);
		if (!colour.getSerializedName().equals(colourName))
			throw new IllegalArgumentException("Unknown upholstered item colour: " + colourName);
		String itemName = itemPath.substring(slash + 1);
		String modelName = inventoryModelName(itemName);
		return new ColouredItemModel(ModelLoaderRegistry.getModel(new ResourceLocation(
				location.getResourceDomain(), "block/" + modelName)), colour);
	}

	public static String inventoryModelName(String itemName) {
		if (itemName.startsWith("bed_wood_foot_left_"))
			return "bed_wood_double_inventory_" + itemName.substring("bed_wood_foot_left_".length());
		if (itemName.startsWith("bed_wood_foot_"))
			return "bed_wood_single_inventory_" + itemName.substring("bed_wood_foot_".length());
		if (itemName.startsWith("bed_canopy_foot_left_lower_"))
			return "bed_canopy_double_inventory_" + itemName.substring("bed_canopy_foot_left_lower_".length());
		if (itemName.startsWith("bed_canopy_foot_lower_"))
			return "bed_canopy_single_inventory_" + itemName.substring("bed_canopy_foot_lower_".length());
		if (itemName.startsWith("chair_wood_ironage_wingback_")
				|| itemName.startsWith("chair_wood_ironage_throne_"))
			return itemName + "_inventory";
		throw new IllegalArgumentException("Unknown upholstered item: " + itemName);
	}

	@Override public void onResourceManagerReload(IResourceManager manager) { }

	private static ResourceLocation woolTexture(UpholsteryColour colour) {
		return new ResourceLocation("minecraft", "blocks/wool_colored_" + colour.getTextureName());
	}

	private static IBakedModel bakeColour(IModel delegate, UpholsteryColour colour, IModelState state,
			VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> textureGetter) {
		if (!(delegate instanceof IRetexturableModel))
			throw new IllegalStateException("Upholstery model is not retexturable: " + delegate);
		return ((IRetexturableModel)delegate).retexture(ImmutableMap.of("cloth",
				woolTexture(colour).toString())).bake(state, format, textureGetter);
	}

	private static Collection<ResourceLocation> textures(IModel delegate, boolean all,
			UpholsteryColour chosen) {
		LinkedHashSet<ResourceLocation> textures = new LinkedHashSet<ResourceLocation>(delegate.getTextures());
		for (UpholsteryColour colour : UpholsteryColour.values()) {
			if (all || colour == chosen) textures.add(woolTexture(colour));
		}
		return textures;
	}

	private static final class ColouredModel implements IModel {
		private final IModel delegate;
		private ColouredModel(IModel delegate) { this.delegate = delegate; }
		@Override public Collection<ResourceLocation> getDependencies() { return delegate.getDependencies(); }
		@Override public Collection<ResourceLocation> getTextures() { return textures(delegate, true, null); }
		@Override public IBakedModel bake(IModelState state, VertexFormat format,
				Function<ResourceLocation, TextureAtlasSprite> textureGetter) {
			Map<UpholsteryColour, IBakedModel> variants = new EnumMap<UpholsteryColour, IBakedModel>(UpholsteryColour.class);
			for (UpholsteryColour colour : UpholsteryColour.values())
				variants.put(colour, bakeColour(delegate, colour, state, format, textureGetter));
			return new UpholsteryBakedModel(variants);
		}
		@Override public IModelState getDefaultState() { return delegate.getDefaultState(); }
	}

	private static final class ColouredItemModel implements IModel {
		private final IModel delegate;
		private final UpholsteryColour colour;
		private ColouredItemModel(IModel delegate, UpholsteryColour colour) {
			this.delegate = delegate;
			this.colour = colour;
		}
		@Override public Collection<ResourceLocation> getDependencies() { return delegate.getDependencies(); }
		@Override public Collection<ResourceLocation> getTextures() { return textures(delegate, false, colour); }
		@Override public IBakedModel bake(IModelState state, VertexFormat format,
				Function<ResourceLocation, TextureAtlasSprite> textureGetter) {
			return bakeColour(delegate, colour, state, format, textureGetter);
		}
		@Override public IModelState getDefaultState() { return delegate.getDefaultState(); }
	}
}
