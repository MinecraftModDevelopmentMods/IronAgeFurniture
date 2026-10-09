package zone.moddev.mc.ironagefurniture.api.enumerations;

import net.minecraft.util.IStringSerializable;

public enum ChairPart implements IStringSerializable {
    LOWER, MIDDLE, UPPER;
    @Override public String getName() { return name().toLowerCase(java.util.Locale.ROOT); }
}
