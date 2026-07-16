package com.mcmoddev.ironagefurniture.client.resources;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mcmoddev.ironagefurniture.Ironagefurniture;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.client.resources.data.MetadataSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public final class GeneratedModelResourcePack implements IResourcePack {
	private static final String PACK_NAME = "Iron Age Furniture Generated Models";
	private static final String MANIFEST_PATH = "/assets/ironagefurniture/generated/simple_model_resources.json";
	private static final String GENERATED_MODELS = "generatedModels";
	private static final Set<String> RESOURCE_DOMAINS = Collections.singleton(Ironagefurniture.MODID);
	private static final Gson GSON = new Gson();
	private static GeneratedModelResourcePack instance;

	private final Map<ResourceLocation, byte[]> generatedResources;

	private GeneratedModelResourcePack() {
		this.generatedResources = loadGeneratedResources();
	}

	public static void install() {
		if (instance == null) {
			instance = new GeneratedModelResourcePack();
		}

		Minecraft minecraft = Minecraft.getMinecraft();
		@SuppressWarnings("unchecked")
		List<IResourcePack> defaultResourcePacks = ObfuscationReflectionHelper.getPrivateValue(Minecraft.class,
				minecraft, "defaultResourcePacks", "field_110449_ao");

		if (!defaultResourcePacks.contains(instance)) {
			defaultResourcePacks.add(instance);
			((SimpleReloadableResourceManager)minecraft.getResourceManager()).reloadResourcePack(instance);
		}
	}

	@Override
	public InputStream getInputStream(ResourceLocation location) throws IOException {
		byte[] bytes = this.generatedResources.get(location);
		if (bytes == null) {
			throw new FileNotFoundException(location.toString());
		}
		return new ByteArrayInputStream(bytes);
	}

	@Override
	public boolean resourceExists(ResourceLocation location) {
		return this.generatedResources.containsKey(location);
	}

	@Override
	public Set<String> getResourceDomains() {
		return RESOURCE_DOMAINS;
	}

	@Override
	public <T extends IMetadataSection> T getPackMetadata(MetadataSerializer metadataSerializer, String metadataSectionName)
			throws IOException {
		return null;
	}

	@Override
	public BufferedImage getPackImage() throws IOException {
		return new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
	}

	@Override
	public String getPackName() {
		return PACK_NAME;
	}

	private static Map<ResourceLocation, byte[]> loadGeneratedResources() {
		Map<ResourceLocation, byte[]> resources = new LinkedHashMap<ResourceLocation, byte[]>();
		InputStream input = GeneratedModelResourcePack.class.getResourceAsStream(MANIFEST_PATH);
		if (input == null) {
			return resources;
		}

		try {
			Reader reader = new InputStreamReader(input, StandardCharsets.UTF_8);
			try {
				JsonObject root = new JsonParser().parse(reader).getAsJsonObject();
				JsonObject models = root.getAsJsonObject(GENERATED_MODELS);
				if (models == null) {
					return resources;
				}

				for (Map.Entry<String, JsonElement> model : models.entrySet()) {
					ResourceLocation location = new ResourceLocation(Ironagefurniture.MODID, model.getKey());
					resources.put(location, GSON.toJson(model.getValue()).getBytes(StandardCharsets.UTF_8));
				}
			} finally {
				reader.close();
			}
		} catch (IOException e) {
			throw new IllegalStateException("Unable to load generated model resource manifest", e);
		}

		return resources;
	}
}
