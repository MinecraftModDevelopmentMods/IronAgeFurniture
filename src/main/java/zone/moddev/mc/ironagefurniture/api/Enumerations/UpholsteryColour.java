package zone.moddev.mc.ironagefurniture.api.Enumerations;

/** Metadata zero and missing legacy data are always red. */
public enum UpholsteryColour {
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

	private static final UpholsteryColour[] BY_ITEM = new UpholsteryColour[16];
	private static final UpholsteryColour[] BY_CARPET = new UpholsteryColour[16];
	private final int itemMetadata;
	private final int carpetMetadata;
	private final String name;
	private final String textureName;

	static {
		for (UpholsteryColour colour : values()) {
			BY_ITEM[colour.itemMetadata] = colour;
			BY_CARPET[colour.carpetMetadata] = colour;
		}
	}

	UpholsteryColour(int itemMetadata, int carpetMetadata, String name, String textureName) {
		this.itemMetadata = itemMetadata;
		this.carpetMetadata = carpetMetadata;
		this.name = name;
		this.textureName = textureName;
	}

	public int getItemMetadata() { return itemMetadata; }
	public int getCarpetMetadata() { return carpetMetadata; }
	public String getSerializedName() { return name; }
	public String getTextureName() { return textureName; }

	public static UpholsteryColour byItemMetadata(int metadata) {
		return metadata >= 0 && metadata < BY_ITEM.length ? BY_ITEM[metadata] : RED;
	}

	public static UpholsteryColour byCarpetMetadata(int metadata) {
		return metadata >= 0 && metadata < BY_CARPET.length ? BY_CARPET[metadata] : RED;
	}

	public static UpholsteryColour byName(String name) {
		for (UpholsteryColour colour : values()) {
			if (colour.name.equalsIgnoreCase(name)) return colour;
		}
		return RED;
	}
}
