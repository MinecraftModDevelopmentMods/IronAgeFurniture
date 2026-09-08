package zone.moddev.mc.ironagefurniture.client.gui;

public final class MechanicalDial {
	private static final float MAX_CHANGE_PER_FRAME = 1.0F / 64.0F;

	private float displayedValue = Float.NaN;

	public void draw(int x, int y, int size, float targetValue, int centerColor, boolean enabled) {
		float target = Math.max(0.0F, Math.min(1.0F, targetValue));

		if (Float.isNaN(this.displayedValue)) {
			this.displayedValue = target;
		} else {
			this.displayedValue = MechanicalGuiRenderer.approach(this.displayedValue, target,
				MAX_CHANGE_PER_FRAME);
		}

		MechanicalGuiRenderer.drawDial(x, y, size, this.displayedValue, centerColor, enabled);
	}
}
