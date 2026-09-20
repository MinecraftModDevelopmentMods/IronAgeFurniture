package zone.moddev.mc.ironagefurniture.api.Enumerations;

/**
 * Stable colour contract for legacy padded-bench items and tile entities.
 *
 * <p>Metadata zero intentionally remains red so items created by older
 * IronAgeFurniture builds keep their appearance.</p>
 */
public enum PaddedBenchColour {
	RED(0, 14, "red", "red"),
	ORANGE(1, 1, "orange", "orange"),
	MAGENTA(2, 2, "magenta", "magenta"),
	LIGHT_BLUE(3, 3, "light_blue", "light_blue"),
	YELLOW(4, 4, "yellow", "yellow"),
	LIME(5, 5, "lime", "lime"),
	PINK(6, 6, "pink", "pink"),
	GRAY(7, 7, "gray", "gray"),
	LIGHT_GRAY(8, 8, "light_gray", "silver"),
	CYAN(9, 9, "cyan", "cyan"),
	PURPLE(10, 10, "purple", "purple"),
	BLUE(11, 11, "blue", "blue"),
	BROWN(12, 12, "brown", "brown"),
	GREEN(13, 13, "green", "green"),
	WHITE(14, 0, "white", "white"),
	BLACK(15, 15, "black", "black");

	private static final PaddedBenchColour[] BY_ITEM_METADATA = new PaddedBenchColour[16];
	private static final PaddedBenchColour[] BY_CARPET_METADATA = new PaddedBenchColour[16];
	private static final PaddedBenchColour[] CREATIVE_ORDER = {
			WHITE, ORANGE, MAGENTA, LIGHT_BLUE, YELLOW, LIME, PINK, GRAY,
			LIGHT_GRAY, CYAN, PURPLE, BLUE, BROWN, GREEN, RED, BLACK
	};

	static {
		for (PaddedBenchColour colour : values()) {
			BY_ITEM_METADATA[colour.itemMetadata] = colour;
			BY_CARPET_METADATA[colour.carpetMetadata] = colour;
		}
	}

	private final int itemMetadata;
	private final int carpetMetadata;
	private final String serializedName;
	private final String legacyTextureName;

	PaddedBenchColour(int itemMetadata, int carpetMetadata, String serializedName,
			String legacyTextureName) {
		this.itemMetadata = itemMetadata;
		this.carpetMetadata = carpetMetadata;
		this.serializedName = serializedName;
		this.legacyTextureName = legacyTextureName;
	}

	public int getItemMetadata() {
		return this.itemMetadata;
	}

	public int getCarpetMetadata() {
		return this.carpetMetadata;
	}

	public String getSerializedName() {
		return this.serializedName;
	}

	public String getLegacyTextureName() {
		return this.legacyTextureName;
	}

	public static PaddedBenchColour byItemMetadata(int metadata) {
		return metadata >= 0 && metadata < BY_ITEM_METADATA.length
				? BY_ITEM_METADATA[metadata] : RED;
	}

	public static PaddedBenchColour byCarpetMetadata(int metadata) {
		return metadata >= 0 && metadata < BY_CARPET_METADATA.length
				? BY_CARPET_METADATA[metadata] : RED;
	}

	public static PaddedBenchColour byName(String name) {
		if (name != null) {
			for (PaddedBenchColour colour : values()) {
				if (colour.serializedName.equalsIgnoreCase(name)) {
					return colour;
				}
			}
		}
		return RED;
	}

	public static PaddedBenchColour[] creativeOrder() {
		return CREATIVE_ORDER.clone();
	}
}
