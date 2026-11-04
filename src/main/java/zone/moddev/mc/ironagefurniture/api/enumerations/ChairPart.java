package zone.moddev.mc.ironagefurniture.api.enumerations;

import net.minecraft.util.StringRepresentable;

public enum ChairPart implements StringRepresentable {
    LOWER, MIDDLE, UPPER;
    @Override public String getSerializedName() { return name().toLowerCase(java.util.Locale.ROOT); }
}
