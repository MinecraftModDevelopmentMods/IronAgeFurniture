package zone.moddev.mc.ironagefurniture.api.enumerations;
import net.minecraft.util.IStringSerializable;
public enum WoodBedSide implements IStringSerializable {
    LEFT, RIGHT;
    @Override public String getSerializedName() { return name().toLowerCase(java.util.Locale.ROOT); }
}
