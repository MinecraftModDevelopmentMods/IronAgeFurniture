package zone.moddev.mc.ironagefurniture.api.Enumerations;

import net.minecraft.util.IStringSerializable;

public enum ChairPart implements IStringSerializable {
	LOWER("lower"),
	MIDDLE("middle"),
	UPPER("upper");

	private static final ChairPart[] META_LOOKUP = values();
	private final String name;

	private ChairPart(String name) {
		this.name = name;
	}

	public static ChairPart byMetadata(int meta) {
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
