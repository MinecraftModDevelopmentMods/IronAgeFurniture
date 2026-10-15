package zone.moddev.mc.ironagefurniture.api.enumerations;
import net.minecraft.util.StringRepresentable;
public enum WoodBedSide implements StringRepresentable {
    LEFT, RIGHT;
    @Override public String getSerializedName() { return name().toLowerCase(java.util.Locale.ROOT); }
}
