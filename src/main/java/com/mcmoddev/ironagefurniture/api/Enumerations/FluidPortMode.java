package com.mcmoddev.ironagefurniture.api.Enumerations;

public enum FluidPortMode {
	LOCKED(0),
	FLOOD(1),
	DRAIN(2);

	private final int id;

	FluidPortMode(int id) {
		this.id = id;
	}

	public int getId() {
		return this.id;
	}

	public boolean opensInlet() {
		return this == FLOOD;
	}

	public boolean opensOutlet() {
		return this == DRAIN;
	}

	public static FluidPortMode fromId(int id) {
		for (FluidPortMode mode : values()) {
			if (mode.id == id) {
				return mode;
			}
		}

		return LOCKED;
	}

	public static FluidPortMode fromLegacy(boolean inletOpen, boolean outletOpen) {
		if (inletOpen == outletOpen) {
			return LOCKED;
		}

		return inletOpen ? FLOOD : DRAIN;
	}
}
