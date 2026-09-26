package zone.moddev.mc.ironagefurniture.api.Properties;

import zone.moddev.mc.ironagefurniture.api.Enumerations.UpholsteryColour;
import net.minecraftforge.common.property.IUnlistedProperty;

public final class UpholsteryColourProperty implements IUnlistedProperty<UpholsteryColour> {
	public static final UpholsteryColourProperty COLOUR = new UpholsteryColourProperty();
	private UpholsteryColourProperty() { }
	@Override public String getName() { return "upholstery_colour"; }
	@Override public boolean isValid(UpholsteryColour value) { return value != null; }
	@Override public Class<UpholsteryColour> getType() { return UpholsteryColour.class; }
	@Override public String valueToString(UpholsteryColour value) {
		return (value == null ? UpholsteryColour.RED : value).getSerializedName();
	}
}
