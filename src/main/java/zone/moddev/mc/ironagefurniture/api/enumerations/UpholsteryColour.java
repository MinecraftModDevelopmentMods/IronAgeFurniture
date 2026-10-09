package zone.moddev.mc.ironagefurniture.api.enumerations;

import net.minecraft.util.IStringSerializable;

/** The old item metadata order is part of the 1.10/1.12 world-upgrade contract. */
public enum UpholsteryColour implements IStringSerializable {
    RED(14), ORANGE(1), MAGENTA(2), LIGHT_BLUE(3), YELLOW(4), LIME(5), PINK(6), GRAY(7),
    LIGHT_GRAY(8), CYAN(9), PURPLE(10), BLUE(11), BROWN(12), GREEN(13), WHITE(0), BLACK(15);

    private final int dyeMetadata;
    UpholsteryColour(int dyeMetadata) { this.dyeMetadata = dyeMetadata; }
    public int getItemMetadata() { return ordinal(); }
    public int getDyeMetadata() { return dyeMetadata; }
    @Override public String getName() { return name().toLowerCase(java.util.Locale.ROOT); }
    public static UpholsteryColour byName(String name) {
        for (UpholsteryColour colour : values()) if (colour.getName().equalsIgnoreCase(name)) return colour;
        return RED;
    }
    public static UpholsteryColour byItemMetadata(int value) {
        return value >= 0 && value < values().length ? values()[value] : RED;
    }
}
