package zone.moddev.mc.ironagefurniture.api.Enumerations;

import net.minecraft.util.IStringSerializable;

public enum WoodBedSide implements IStringSerializable {
	LEFT("left"),
	RIGHT("right");

	private static final WoodBedSide[] META_LOOKUP = values();
	private final String name;

	private WoodBedSide(String name) {
		this.name = name;
	}

	public static WoodBedSide byMetadata(int meta) {
		if (meta < 0 || meta >= META_LOOKUP.length) {
			meta = 0;
		}

		return META_LOOKUP[meta];
	}

	@Override
	public String toString() {
		return this.name;
	}

	@Override
	public String getName() {
		return this.name;
	}
}
