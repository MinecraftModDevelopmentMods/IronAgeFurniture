package zone.moddev.mc.ironagefurniture.client.model;

import java.util.Collection;
import java.util.EnumMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import zone.moddev.mc.ironagefurniture.Ironagefurniture;
import zone.moddev.mc.ironagefurniture.api.Enumerations.PaddedBenchColour;

import com.google.common.collect.ImmutableMap;

import java.util.function.Function;

import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public enum PaddedBenchModelLoader implements ICustomModelLoader {
	INSTANCE;

	private static final String ITEM_PREFIX = "models/padded/";
	private static final String COLOURED_ITEM_PREFIX = "models/item/padded/";
	private static final String BLOCK_PREFIX = "models/block/padded/";
	private static final Set<String> NAMESPACES = new LinkedHashSet<String>();

	static {
		NAMESPACES.add(Ironagefurniture.MODID);
	}

	/**
	 * Adds an add-on resource namespace to the padded-bench model loader.
	 *
	 * @param namespace namespace containing the add-on's padded bench models
	 */
	public static void registerNamespace(String namespace) {
		NAMESPACES.add(new ResourceLocation(namespace, "padded_bench").getNamespace());
	}

	@Override
	public boolean accepts(ResourceLocation modelLocation) {
		if (!NAMESPACES.contains(modelLocation.getNamespace())) {
			return false;
		}
		String path = modelLocation.getPath();
		return path.startsWith(ITEM_PREFIX) || path.startsWith(COLOURED_ITEM_PREFIX)
				|| path.startsWith(BLOCK_PREFIX);
	}

	@Override
	public IModel loadModel(ResourceLocation modelLocation) throws Exception {
		String path = modelLocation.getPath();
		if (path.startsWith(COLOURED_ITEM_PREFIX)) {
			String itemPath = path.substring(COLOURED_ITEM_PREFIX.length());
			int separator = itemPath.indexOf('/');
			if (separator <= 0 || separator == itemPath.length() - 1) {
				throw new IllegalArgumentException("Invalid padded bench item model: " + modelLocation);
			}

			String colourName = itemPath.substring(0, separator);
			PaddedBenchColour colour = PaddedBenchColour.byName(colourName);
			if (!colour.getSerializedName().equals(colourName)) {
				throw new IllegalArgumentException("Unknown padded bench item colour: " + colourName);
			}

			IModel delegate = ModelLoaderRegistry.getModel(new ResourceLocation(
					modelLocation.getNamespace(), "block/" + itemPath.substring(separator + 1)));
			return new ColouredItemModel(delegate, colour);
		}

		String delegatePath = path.substring(path.startsWith(BLOCK_PREFIX)
				? BLOCK_PREFIX.length() : ITEM_PREFIX.length());
		IModel delegate = ModelLoaderRegistry.getModel(
				new ResourceLocation(modelLocation.getNamespace(), "block/" + delegatePath));
		return new ColouredModel(delegate);
	}

	@Override
	public void onResourceManagerReload(IResourceManager resourceManager) {
		// ModelLoaderRegistry owns and clears the model cache on resource reload.
	}

	public static PaddedBenchBakedModel bakeVariants(IModel delegate, IModelState state,
			VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> textureGetter) {
		Map<PaddedBenchColour, IBakedModel> variants =
				new EnumMap<PaddedBenchColour, IBakedModel>(PaddedBenchColour.class);
		for (PaddedBenchColour colour : PaddedBenchColour.values()) {
			IModel coloured = delegate.retexture(ImmutableMap.of(
					"upholstery", woolTexture(colour).toString()));
			variants.put(colour, coloured.bake(state, format, textureGetter));
		}
		return new PaddedBenchBakedModel(variants);
	}

	private static IBakedModel bakeVariant(IModel delegate, PaddedBenchColour colour,
			IModelState state, VertexFormat format,
			Function<ResourceLocation, TextureAtlasSprite> textureGetter) {
		IModel coloured = delegate.retexture(ImmutableMap.of(
				"upholstery", woolTexture(colour).toString()));
		return coloured.bake(state, format, textureGetter);
	}

	public static ResourceLocation woolTexture(PaddedBenchColour colour) {
		return new ResourceLocation("minecraft", "blocks/wool_colored_"
				+ colour.getLegacyTextureName());
	}

	private static final class ColouredModel implements IModel {
		private final IModel delegate;

		private ColouredModel(IModel delegate) {
			this.delegate = delegate;
		}

		@Override
		public Collection<ResourceLocation> getDependencies() {
			return this.delegate.getDependencies();
		}

		@Override
		public Collection<ResourceLocation> getTextures() {
			LinkedHashSet<ResourceLocation> textures =
					new LinkedHashSet<ResourceLocation>(this.delegate.getTextures());
			for (PaddedBenchColour colour : PaddedBenchColour.values()) {
				textures.add(woolTexture(colour));
			}
			return textures;
		}

		@Override
		public IBakedModel bake(IModelState state, VertexFormat format,
				Function<ResourceLocation, TextureAtlasSprite> textureGetter) {
			return bakeVariants(this.delegate, state, format, textureGetter);
		}

		@Override
		public IModelState getDefaultState() {
			return this.delegate.getDefaultState();
		}
	}

	private static final class ColouredItemModel implements IModel {
		private final IModel delegate;
		private final PaddedBenchColour colour;

		private ColouredItemModel(IModel delegate, PaddedBenchColour colour) {
			this.delegate = delegate;
			this.colour = colour;
		}

		@Override
		public Collection<ResourceLocation> getDependencies() {
			return this.delegate.getDependencies();
		}

		@Override
		public Collection<ResourceLocation> getTextures() {
			LinkedHashSet<ResourceLocation> textures =
					new LinkedHashSet<ResourceLocation>(this.delegate.getTextures());
			textures.add(woolTexture(this.colour));
			return textures;
		}

		@Override
		public IBakedModel bake(IModelState state, VertexFormat format,
				Function<ResourceLocation, TextureAtlasSprite> textureGetter) {
			return bakeVariant(this.delegate, this.colour, state, format, textureGetter);
		}

		@Override
		public IModelState getDefaultState() {
			return this.delegate.getDefaultState();
		}
	}
}
