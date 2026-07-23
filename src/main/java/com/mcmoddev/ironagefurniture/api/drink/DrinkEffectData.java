package com.mcmoddev.ironagefurniture.api.drink;

import net.minecraft.nbt.NBTTagCompound;

public class DrinkEffectData implements IDrinkEffectData {
	private double servings;
	private int recoveryTicks;
	private int hangoverTicks;
	private boolean reachedHeavyEffect;

	@Override public double getServings() { return this.servings; }
	@Override public void addServings(double value) { this.servings = Math.max(0.0D, this.servings + value); }
	@Override public void setServings(double value) { this.servings = Math.max(0.0D, value); }
	@Override public int getRecoveryTicks() { return this.recoveryTicks; }
	@Override public void setRecoveryTicks(int value) { this.recoveryTicks = Math.max(0, value); }
	@Override public int getHangoverTicks() { return this.hangoverTicks; }
	@Override public void setHangoverTicks(int value) { this.hangoverTicks = Math.max(0, value); }
	@Override public boolean hasReachedHeavyEffect() { return this.reachedHeavyEffect; }
	@Override public void setReachedHeavyEffect(boolean value) { this.reachedHeavyEffect = value; }

	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setDouble("Servings", this.servings);
		tag.setInteger("RecoveryTicks", this.recoveryTicks);
		tag.setInteger("HangoverTicks", this.hangoverTicks);
		tag.setBoolean("ReachedHeavyEffect", this.reachedHeavyEffect);
		return tag;
	}

	@Override
	public void deserializeNBT(NBTTagCompound tag) {
		this.servings = Math.max(0.0D, tag.getDouble("Servings"));
		this.recoveryTicks = Math.max(0, tag.getInteger("RecoveryTicks"));
		this.hangoverTicks = Math.max(0, tag.getInteger("HangoverTicks"));
		this.reachedHeavyEffect = tag.getBoolean("ReachedHeavyEffect");
	}
}
