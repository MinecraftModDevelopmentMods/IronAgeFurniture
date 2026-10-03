package zone.moddev.mc.ironagefurniture.api.enumerations;
import net.minecraft.util.IStringSerializable;
public enum CanopyBedPart implements IStringSerializable {
    FOOT_LOWER, FOOT_UPPER, HEAD_LOWER, HEAD_UPPER;
    @Override public String getName() { return name().toLowerCase(java.util.Locale.ROOT); }
    public boolean isHead() { return this == HEAD_LOWER || this == HEAD_UPPER; }
    public boolean isUpper() { return this == FOOT_UPPER || this == HEAD_UPPER; }
}
