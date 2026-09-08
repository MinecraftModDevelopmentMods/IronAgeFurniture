package zone.moddev.mc.ironagefurniture.api;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Loader;

public final class PowerAdvantageFluidCompat {
	private static final String POWER_ADVANTAGE_MODID = "poweradvantage";
	private static final String PA_FLUID_CONDUIT_BLOCK = "cyano.poweradvantage.api.fluid.FluidConduitBlock";
	private static final String PA_CONDUIT_REGISTRY = "com.mcmoddev.poweradvantage.conduitnetwork.ConduitRegistry";
	private static final String PA_FLUIDS = "com.mcmoddev.poweradvantage.init.Fluids";
	private static final String PA_CONDUIT_TYPE = "cyano.poweradvantage.api.ConduitType";
	private static final String PA_POWER_REQUEST = "cyano.poweradvantage.api.PowerRequest";
	private static final String PA_POWER_MACHINE = "cyano.poweradvantage.api.IPowerMachine";

	private static final int NO_TRANSFER = 0;
	private static final int MAX_TRANSFER_PER_PULSE = Fluid.BUCKET_VOLUME;

	private static boolean attemptedInit;
	private static boolean available;
	private static Class<?> fluidConduitBlockClass;
	private static Method getRegistryInstance;
	private static Method getRequestsForPower;
	private static Method fluidToConduitType;
	private static Method addEnergy;
	private static Field fluidConduitGeneral;
	private static Field requestAmount;
	private static Field requestPriority;
	private static Field requestEntity;
	private static byte minimumPipePriority;

	private PowerAdvantageFluidCompat() {
		throw new IllegalAccessError("This class cannot be instantiated");
	}

	public static int tryTransferFromOutlet(World world, BlockPos portPos, @Nullable EnumFacing portFace,
			@Nullable FluidStack availableFluid) {
		if (world == null || world.isRemote || portFace == null || availableFluid == null
				|| availableFluid.getFluid() == null || availableFluid.amount <= 0 || !init()) {
			return NO_TRANSFER;
		}

		BlockPos pipePos = portPos.offset(portFace);

		if (!isPowerAdvantageFluidConduit(world, pipePos)) {
			return NO_TRANSFER;
		}

		try {
			Object fluidNetworkType = fluidConduitGeneral.get(null);
			Object fluidType = fluidToConduitType.invoke(null, availableFluid.getFluid());
			Object registry = getRegistryInstance.invoke(null);
			Object requestsObject = getRequestsForPower.invoke(registry, world, pipePos, fluidNetworkType, fluidType);

			if (!(requestsObject instanceof List<?>)) {
				return NO_TRANSFER;
			}

			int offered = Math.min(availableFluid.amount, MAX_TRANSFER_PER_PULSE);
			int remaining = offered;

			for (Object request : (List<?>)requestsObject) {
				if (request == null || remaining <= 0) {
					break;
				}

				if (requestPriority.getByte(request) < minimumPipePriority) {
					break;
				}

				Object entity = requestEntity.get(request);

				if (entity == null) {
					continue;
				}

				int requested = Math.min(remaining, toPositiveInt(requestAmount.getFloat(request)));

				if (requested <= 0) {
					continue;
				}

				Object acceptedObject = addEnergy.invoke(entity, Float.valueOf(requested), fluidType);
				int accepted = acceptedObject instanceof Number ? toPositiveInt(((Number)acceptedObject).floatValue())
					: NO_TRANSFER;

				remaining -= Math.min(requested, accepted);
			}

			return offered - remaining;
		} catch (ReflectiveOperationException ex) {
			available = false;
			return NO_TRANSFER;
		} catch (RuntimeException ex) {
			return NO_TRANSFER;
		}
	}

	private static int toPositiveInt(float value) {
		if (value <= 0.0F) {
			return NO_TRANSFER;
		}

		return value >= Integer.MAX_VALUE ? Integer.MAX_VALUE : (int)value;
	}

	private static boolean isPowerAdvantageFluidConduit(World world, BlockPos pos) {
		if (fluidConduitBlockClass == null) {
			return false;
		}

		Block block = world.getBlockState(pos).getBlock();
		return fluidConduitBlockClass.isInstance(block);
	}

	private static boolean init() {
		if (attemptedInit) {
			return available;
		}

		attemptedInit = true;

		if (!Loader.isModLoaded(POWER_ADVANTAGE_MODID)) {
			return false;
		}

		try {
			fluidConduitBlockClass = Class.forName(PA_FLUID_CONDUIT_BLOCK);
			Class<?> conduitRegistryClass = Class.forName(PA_CONDUIT_REGISTRY);
			Class<?> fluidsClass = Class.forName(PA_FLUIDS);
			Class<?> conduitTypeClass = Class.forName(PA_CONDUIT_TYPE);
			Class<?> powerRequestClass = Class.forName(PA_POWER_REQUEST);
			Class<?> powerMachineClass = Class.forName(PA_POWER_MACHINE);

			getRegistryInstance = conduitRegistryClass.getMethod("getInstance");
			getRequestsForPower = conduitRegistryClass.getMethod("getRequestsForPower", World.class, BlockPos.class,
				conduitTypeClass, conduitTypeClass);
			fluidToConduitType = fluidsClass.getMethod("fluidToConduitType", Fluid.class);
			addEnergy = powerMachineClass.getMethod("addEnergy", float.class, conduitTypeClass);
			fluidConduitGeneral = fluidsClass.getField("fluidConduit_general");
			requestAmount = powerRequestClass.getField("amount");
			requestPriority = powerRequestClass.getField("priority");
			requestEntity = powerRequestClass.getField("entity");
			minimumPipePriority = powerRequestClass.getField("LAST_PRIORITY").getByte(null);
			available = true;
		} catch (ReflectiveOperationException ex) {
			available = false;
		}

		return available;
	}
}
