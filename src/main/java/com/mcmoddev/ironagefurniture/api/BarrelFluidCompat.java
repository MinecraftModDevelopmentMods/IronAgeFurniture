package com.mcmoddev.ironagefurniture.api;

import javax.annotation.Nullable;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public final class BarrelFluidCompat {
	private static final String MILK_FLUID_NAME = "milk";
	private static final String PAMS_FRESH_MILK = "harvestcraft:freshmilkitem";
	private static final ResourceLocation MILK_TEXTURE = new ResourceLocation("minecraft:blocks/quartz_block_top");

	private BarrelFluidCompat() {
	}

	public static void init() {
		getMilkFluid();
	}

	@Nullable
	public static Fluid getMilkFluid() {
		Fluid fluid = FluidRegistry.getFluid(MILK_FLUID_NAME);

		if (fluid == null) {
			FluidRegistry.registerFluid(new MilkFluid());
			fluid = FluidRegistry.getFluid(MILK_FLUID_NAME);
		}

		return fluid;
	}

	public static boolean isPamsFreshMilk(ItemStack stack) {
		Item freshMilk = getPamsFreshMilkItem();
		return stack != null && stack.stackSize > 0 && freshMilk != null && stack.getItem() == freshMilk;
	}

	@Nullable
	private static Item getPamsFreshMilkItem() {
		return Item.getByNameOrId(PAMS_FRESH_MILK);
	}

	private static class MilkFluid extends Fluid {
		MilkFluid() {
			super(MILK_FLUID_NAME, MILK_TEXTURE, MILK_TEXTURE);
			this.setUnlocalizedName(MILK_FLUID_NAME);
			this.setDensity(1030);
			this.setViscosity(1500);
		}

		@Override
		public String getLocalizedName(FluidStack stack) {
			return "Milk";
		}

		@Override
		public int getColor() {
			return 0xFFFFFFFF;
		}

		@Override
		public int getColor(FluidStack stack) {
			return 0xFFFFFFFF;
		}
	}
}
