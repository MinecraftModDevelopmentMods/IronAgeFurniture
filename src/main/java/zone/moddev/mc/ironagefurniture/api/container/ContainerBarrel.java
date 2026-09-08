package zone.moddev.mc.ironagefurniture.api.container;

import zone.moddev.mc.ironagefurniture.api.tile.TileEntityBarrel;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;

public class ContainerBarrel extends Container {
	private static final int DISPLAY_PROGRESS_TOTAL = 100;
	private static final int MAX_IN_PROGRESS_PERCENT = 99;

	private final TileEntityBarrel barrel;
	private int lastAgeProgress;
	private int lastAgeProgressTotal;
	private int lastSealed;

	public ContainerBarrel(TileEntityBarrel barrel) {
		this.barrel = barrel;
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return this.barrel != null && this.barrel.isUsableByPlayer(playerIn);
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();

		int ageProgress = this.getDisplayProgress(this.barrel.getAgeProgress(), this.barrel.getAgeProgressTotal());
		int ageProgressTotal = this.getDisplayProgressTotal(this.barrel.getAgeProgressTotal());

		this.sendFieldIfChanged(TileEntityBarrel.FIELD_AGE_PROGRESS, ageProgress, this.lastAgeProgress);
		this.sendFieldIfChanged(TileEntityBarrel.FIELD_AGE_PROGRESS_TOTAL, ageProgressTotal,
			this.lastAgeProgressTotal);
		this.sendFieldIfChanged(TileEntityBarrel.FIELD_SEALED, this.barrel.isSealed() ? 1 : 0,
			this.lastSealed);

		this.lastAgeProgress = ageProgress;
		this.lastAgeProgressTotal = ageProgressTotal;
		this.lastSealed = this.barrel.isSealed() ? 1 : 0;
	}

	@Override
	public void updateProgressBar(int id, int data) {
		this.barrel.setField(id, data);
	}

	private void sendFieldIfChanged(int field, int value, int previousValue) {
		if (value == previousValue) {
			return;
		}

		for (int i = 0; i < this.listeners.size(); i++) {
			((IContainerListener)this.listeners.get(i)).sendProgressBarUpdate(this, field, value);
		}
	}

	private int getDisplayProgress(int progress, int total) {
		if (total <= 0) {
			return 0;
		}

		return Math.min(MAX_IN_PROGRESS_PERCENT, Math.max(0, progress * DISPLAY_PROGRESS_TOTAL / total));
	}

	private int getDisplayProgressTotal(int total) {
		return total > 0 ? DISPLAY_PROGRESS_TOTAL : 0;
	}
}
