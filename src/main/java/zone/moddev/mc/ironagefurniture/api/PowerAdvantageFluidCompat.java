package zone.moddev.mc.ironagefurniture.api;

import javax.annotation.Nullable;

import cyano.poweradvantage.api.fluid.FluidNetworkApi;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Loader;

public final class PowerAdvantageFluidCompat {
	private static final String POWER_ADVANTAGE_MODID = "poweradvantage";
	private static final int NO_TRANSFER = 0;
	private static final int MAX_TRANSFER_PER_PULSE = Fluid.BUCKET_VOLUME;

	private PowerAdvantageFluidCompat() {
		throw new IllegalAccessError("This class cannot be instantiated");
	}

	public static int tryTransferFromOutlet(World world, BlockPos portPos, @Nullable EnumFacing portFace,
			@Nullable FluidStack availableFluid) {
		if (world == null || world.isRemote || portPos == null || portFace == null || availableFluid == null
				|| availableFluid.getFluid() == null || availableFluid.amount <= 0
				|| !Loader.isModLoaded(POWER_ADVANTAGE_MODID)) {
			return NO_TRANSFER;
		}

		return FluidApiBridge.offer(world, portPos.offset(portFace), availableFluid);
	}

	/**
	 * Keeps the optional Power Advantage type reference out of the outer compatibility
	 * class. The JVM loads this bridge only after Forge confirms that the mod is present.
	 */
	private static final class FluidApiBridge {
		private FluidApiBridge() {
			throw new IllegalAccessError("This class cannot be instantiated");
		}

		private static int offer(World world, BlockPos conduitPos, FluidStack availableFluid) {
			return FluidNetworkApi.offerFluid(world, conduitPos, availableFluid, MAX_TRANSFER_PER_PULSE);
		}
	}
}
