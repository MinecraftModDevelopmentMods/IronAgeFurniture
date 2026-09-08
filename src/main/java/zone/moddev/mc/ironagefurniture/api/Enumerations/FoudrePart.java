package zone.moddev.mc.ironagefurniture.api.Enumerations;

import net.minecraft.util.IStringSerializable;

public enum FoudrePart implements IStringSerializable {
	FRONT_LEFT("front_left", 0, 0),
	FRONT_RIGHT("front_right", 1, 0),
	BACK_LEFT("back_left", 0, 1),
	BACK_RIGHT("back_right", 1, 1);

	private final String name;
	private final int lateralOffset;
	private final int depthOffset;

	private FoudrePart(String name, int lateralOffset, int depthOffset) {
		this.name = name;
		this.lateralOffset = lateralOffset;
		this.depthOffset = depthOffset;
	}

	@Override
	public String getName() {
		return this.name;
	}

	public int getLateralOffset() {
		return this.lateralOffset;
	}

	public int getDepthOffset() {
		return this.depthOffset;
	}

	public static FoudrePart byMetadata(int meta) {
		FoudrePart[] values = values();
		return values[Math.abs(meta) % values.length];
	}
}
