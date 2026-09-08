package zone.moddev.mc.ironagefurniture.api.drink;

import net.minecraft.nbt.NBTTagCompound;

public interface IDrinkEffectData {
	double getServings();
	void addServings(double servings);
	void setServings(double servings);
	int getRecoveryTicks();
	void setRecoveryTicks(int ticks);
	int getHangoverTicks();
	void setHangoverTicks(int ticks);
	boolean hasReachedHeavyEffect();
	void setReachedHeavyEffect(boolean reached);
	NBTTagCompound serializeNBT();
	void deserializeNBT(NBTTagCompound tag);
}
