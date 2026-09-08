package zone.moddev.mc.ironagefurniture.api.drink;

import javax.annotation.Nullable;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class DrinkEffectProvider implements ICapabilitySerializable<NBTTagCompound> {
	private final IDrinkEffectData data = new DrinkEffectData();

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
		return capability == DrinkEffectHandler.CAPABILITY;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		return capability == DrinkEffectHandler.CAPABILITY ? (T)this.data : null;
	}

	@Override
	public NBTTagCompound serializeNBT() {
		return this.data.serializeNBT();
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		this.data.deserializeNBT(nbt);
	}

	public static final class Storage implements Capability.IStorage<IDrinkEffectData> {
		@Override
		public NBTBase writeNBT(Capability<IDrinkEffectData> capability, IDrinkEffectData instance,
				EnumFacing side) {
			return instance.serializeNBT();
		}

		@Override
		public void readNBT(Capability<IDrinkEffectData> capability, IDrinkEffectData instance,
				EnumFacing side, NBTBase nbt) {
			if (nbt instanceof NBTTagCompound) instance.deserializeNBT((NBTTagCompound)nbt);
		}
	}
}
