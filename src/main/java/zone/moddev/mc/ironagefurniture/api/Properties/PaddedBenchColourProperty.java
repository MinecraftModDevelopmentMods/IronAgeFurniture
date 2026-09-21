package zone.moddev.mc.ironagefurniture.api.Properties;

import zone.moddev.mc.ironagefurniture.api.Enumerations.PaddedBenchColour;

import net.minecraftforge.common.property.IUnlistedProperty;

public final class PaddedBenchColourProperty implements IUnlistedProperty<PaddedBenchColour> {
	public static final PaddedBenchColourProperty COLOUR = new PaddedBenchColourProperty();

	private PaddedBenchColourProperty() {
	}

	@Override
	public String getName() {
		return "upholstery_colour";
	}

	@Override
	public boolean isValid(PaddedBenchColour value) {
		return value != null;
	}

	@Override
	public Class<PaddedBenchColour> getType() {
		return PaddedBenchColour.class;
	}

	@Override
	public String valueToString(PaddedBenchColour value) {
		return value == null ? PaddedBenchColour.RED.getSerializedName()
				: value.getSerializedName();
	}
}

