package zone.moddev.mc.ironagefurniture.migration;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import net.minecraft.util.ResourceLocation;
import zone.moddev.mc.ironagefurniture.Ironagefurniture;

/** Stable names shared by the pre-flattening padded-bench migration. */
public final class LegacyPaddedBenchIds {
	public static final String COLOR_TAG = "Color";
	public static final String BYG_ADDON_MODID = "iafbygaddon";
	public static final ResourceLocation TILE_ENTITY_ID =
			new ResourceLocation(Ironagefurniture.MODID, "padded_bench_colour");

	private static final String BENCH_PREFIX = "chair_wood_ironage_bench_padded_single_";
	private static final String BACK_BENCH_PREFIX = "chair_wood_ironage_bench_back_padded_single_";
	private static final Set<String> COLOURS = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
			"red", "orange", "magenta", "light_blue", "yellow", "lime", "pink", "gray",
			"light_gray", "cyan", "purple", "blue", "brown", "green", "white", "black")));
	private static final Map<String, String> RETIRED_BYG_WOODS = new LinkedHashMap<>();

	static {
		RETIRED_BYG_WOODS.put("frozen_oak", "byg_aspen");
		RETIRED_BYG_WOODS.put("great_oak", "oak");
		RETIRED_BYG_WOODS.put("hawthorn", "byg_cherry");
		RETIRED_BYG_WOODS.put("ironwood", "byg_ebony");
		RETIRED_BYG_WOODS.put("rowan", "byg_maple");
	}

	private LegacyPaddedBenchIds() {
	}

	public static boolean isLegacyPaddedPath(String path) {
		return path.startsWith(BENCH_PREFIX) || path.startsWith(BACK_BENCH_PREFIX);
	}

	public static boolean isModernRedPaddedPath(String path) {
		return path.startsWith("chair_wood_ironage_bench_padded_red_single_")
				|| path.startsWith("chair_wood_ironage_bench_back_padded_red_single_");
	}

	public static String normalizeColour(String colour) {
		if (colour == null) {
			return "red";
		}
		String normalized = colour.toLowerCase(Locale.ROOT);
		return COLOURS.contains(normalized) ? normalized : "red";
	}

	public static String modernPaddedPath(String legacyPath, String colour) {
		String normalized = normalizeColour(colour);
		String path;
		if (legacyPath.startsWith(BACK_BENCH_PREFIX)) {
			path = "chair_wood_ironage_bench_back_padded_" + normalized + "_single_"
					+ legacyPath.substring(BACK_BENCH_PREFIX.length());
		} else if (legacyPath.startsWith(BENCH_PREFIX)) {
			path = "chair_wood_ironage_bench_padded_" + normalized + "_single_"
					+ legacyPath.substring(BENCH_PREFIX.length());
		} else {
			throw new IllegalArgumentException("Not a legacy padded-bench path: " + legacyPath);
		}
		return renameFlattenedWood(path);
	}

	public static String modernPaddedPath(ResourceLocation oldId, String colour) {
		ResourceLocation current = currentId(oldId);
		return modernPaddedPath(current.getPath(), colour);
	}

	public static String colouredPathFromModernRed(String modernRedPath, String colour) {
		String normalized = normalizeColour(colour);
		if (modernRedPath.startsWith("chair_wood_ironage_bench_back_padded_red_single_")) {
			return modernRedPath.replaceFirst("_padded_red_single_", "_padded_" + normalized + "_single_");
		}
		if (modernRedPath.startsWith("chair_wood_ironage_bench_padded_red_single_")) {
			return modernRedPath.replaceFirst("_padded_red_single_", "_padded_" + normalized + "_single_");
		}
		throw new IllegalArgumentException("Not a modern red padded-bench path: " + modernRedPath);
	}

	public static String legacyPaddedPath(boolean backBench, String modernWood) {
		String legacyWood = "dark_oak".equals(modernWood) ? "big_oak" : modernWood;
		return (backBench ? BACK_BENCH_PREFIX : BENCH_PREFIX) + legacyWood;
	}

	public static ResourceLocation currentId(ResourceLocation oldId) {
		if (BYG_ADDON_MODID.equals(oldId.getNamespace())) {
			return new ResourceLocation(Ironagefurniture.MODID, renameBygAddonWood(oldId.getPath()));
		}
		if (!Ironagefurniture.MODID.equals(oldId.getNamespace())) {
			return oldId;
		}
		if (isLegacyPaddedPath(oldId.getPath())) {
			return oldId;
		}
		return new ResourceLocation(oldId.getNamespace(), renameFlattenedWood(oldId.getPath()));
	}

	public static boolean isLegacyNamespace(String namespace) {
		return Ironagefurniture.MODID.equals(namespace) || BYG_ADDON_MODID.equals(namespace);
	}

	public static String retiredBygFallback(String path) {
		for (Map.Entry<String, String> fallback : RETIRED_BYG_WOODS.entrySet()) {
			if (path.endsWith("_byg_" + fallback.getKey())) {
				return fallback.getKey() + " -> " + fallback.getValue();
			}
		}
		return null;
	}

	private static String renameBygAddonWood(String path) {
		for (Map.Entry<String, String> fallback : RETIRED_BYG_WOODS.entrySet()) {
			String suffix = "_byg_" + fallback.getKey();
			if (path.endsWith(suffix)) {
				String target = fallback.getValue();
				String targetSuffix = "oak".equals(target) ? "_oak" : "_" + target;
				return path.substring(0, path.length() - suffix.length()) + targetSuffix;
			}
		}
		return path;
	}

	private static String renameFlattenedWood(String path) {
		return path.endsWith("_big_oak")
				? path.substring(0, path.length() - "_big_oak".length()) + "_dark_oak"
				: path;
	}
}
