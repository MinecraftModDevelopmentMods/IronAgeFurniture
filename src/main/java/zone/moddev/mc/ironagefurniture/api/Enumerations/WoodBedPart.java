package zone.moddev.mc.ironagefurniture.api.Enumerations;

import net.minecraft.util.IStringSerializable;

public enum WoodBedPart implements IStringSerializable {
	FOOT("foot"),
	HEAD("head");

	private static final WoodBedPart[] META_LOOKUP = values();
	private final String name;

	private WoodBedPart(String name) {
		this.name = name;
	}

	public static WoodBedPart byMetadata(int meta) {
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
