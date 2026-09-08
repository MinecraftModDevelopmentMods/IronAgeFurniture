package zone.moddev.mc.ironagefurniture.api.Enumerations;

import net.minecraft.util.IStringSerializable;

public enum CanopyBedPart implements IStringSerializable {
	FOOT_LOWER("foot_lower"),
	FOOT_UPPER("foot_upper"),
	HEAD_LOWER("head_lower"),
	HEAD_UPPER("head_upper");

	private static final CanopyBedPart[] META_LOOKUP = values();
	private final String name;

	private CanopyBedPart(String name) {
		this.name = name;
	}

	public static CanopyBedPart byMetadata(int meta) {
		if (meta < 0 || meta >= META_LOOKUP.length) {
			meta = 0;
		}

		return META_LOOKUP[meta];
	}

	public boolean isHead() {
		return this == HEAD_LOWER || this == HEAD_UPPER;
	}

	public boolean isUpper() {
		return this == FOOT_UPPER || this == HEAD_UPPER;
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
