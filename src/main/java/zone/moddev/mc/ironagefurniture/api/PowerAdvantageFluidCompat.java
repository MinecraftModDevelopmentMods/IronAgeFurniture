package zone.moddev.mc.ironagefurniture.api;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipFile;

import javax.annotation.Nullable;

import cyano.poweradvantage.api.fluid.FluidNetworkApi;
import net.minecraft.block.Block;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;

public final class PowerAdvantageFluidCompat {
	private static final String POWER_ADVANTAGE_MODID = "poweradvantage";
	private static final int NO_TRANSFER = 0;
	private static final int MAX_TRANSFER_PER_PULSE = Fluid.BUCKET_VOLUME;

	private static boolean backendResolved;
	private static boolean backendFailureLogged;
	private static Backend backend = Backend.DISABLED;

	private PowerAdvantageFluidCompat() {
		throw new IllegalAccessError("This class cannot be instantiated");
	}

	/** Initializes and logs the optional backend during Forge mod initialization. */
	public static void initialize() {
		if (Loader.isModLoaded(POWER_ADVANTAGE_MODID)) {
			getBackend();
		}
	}

	public static int tryTransferFromOutlet(World world, BlockPos portPos, @Nullable EnumFacing portFace,
			@Nullable FluidStack availableFluid) {
		if (world == null || world.isRemote || portPos == null || portFace == null || availableFluid == null
				|| availableFluid.getFluid() == null || availableFluid.amount <= 0
				|| !Loader.isModLoaded(POWER_ADVANTAGE_MODID)) {
			return NO_TRANSFER;
		}

		BlockPos conduitPos = portPos.offset(portFace);
		if (!world.isBlockLoaded(conduitPos)) {
			return NO_TRANSFER;
		}

		Backend selected = getBackend();
		try {
			switch (selected) {
			case API:
				return FluidApiBridge.offer(world, conduitPos, availableFluid);
			case LEGACY:
				return LegacyFluidBridge.offer(world, conduitPos, availableFluid);
			default:
				return NO_TRANSFER;
			}
		} catch (ReflectiveOperationException | RuntimeException | LinkageError failure) {
			disableFailedBackend(selected, failure);
			return NO_TRANSFER;
		}
	}

	private static synchronized Backend getBackend() {
		if (backendResolved) {
			return backend;
		}

		ModContainer powerAdvantage = Loader.instance().getIndexedModList().get(POWER_ADVANTAGE_MODID);
		backend = powerAdvantage == null ? Backend.DISABLED : detectBackend(powerAdvantage.getSource());
		backendResolved = true;

		String version = powerAdvantage == null || powerAdvantage.getVersion() == null
				? "unknown" : powerAdvantage.getVersion();
		FMLLog.info("[%s] Power Advantage %s fluid compatibility backend: %s",
				"ironagefurniture", version, backend.logName);
		return backend;
	}

	private static synchronized void disableFailedBackend(Backend failedBackend, Throwable failure) {
		if (backend == failedBackend) {
			backend = Backend.DISABLED;
		}
		if (!backendFailureLogged) {
			backendFailureLogged = true;
			FMLLog.warning("[%s] Disabling Power Advantage %s fluid compatibility after a %s",
					"ironagefurniture", failedBackend.logName, failure.getClass().getSimpleName());
		}
	}

	static Backend detectBackend(@Nullable File source) {
		if (CapabilityDetector.containsAll(source, FluidApiBridge.requiredClassEntries())) {
			return Backend.API;
		}
		if (CapabilityDetector.containsAll(source, LegacyFluidBridge.requiredClassEntries())) {
			return Backend.LEGACY;
		}
		return Backend.DISABLED;
	}

	enum Backend {
		API("typed-api"),
		LEGACY("legacy-reflection"),
		DISABLED("disabled");

		private final String logName;

		Backend(String logName) {
			this.logName = logName;
		}
	}

	private static final class CapabilityDetector {
		private CapabilityDetector() {
			throw new IllegalAccessError("This class cannot be instantiated");
		}

		private static boolean containsAll(@Nullable File source, String[] classEntries) {
			if (source == null || !source.exists()) {
				return false;
			}

			if (source.isDirectory()) {
				for (String classEntry : classEntries) {
					if (!new File(source, classEntry).isFile()) {
						return false;
					}
				}
				return true;
			}

			try (ZipFile zip = new ZipFile(source)) {
				for (String classEntry : classEntries) {
					if (zip.getEntry(classEntry) == null) {
						return false;
					}
				}
				return true;
			} catch (IOException | SecurityException ignored) {
				return false;
			}
		}
	}

	/**
	 * Keeps the optional Power Advantage API type reference out of the outer
	 * compatibility class. This bridge is invoked only when the loaded mod source
	 * proves that the API class is present.
	 */
	private static final class FluidApiBridge {
		private static final String FLUID_NETWORK_API_CLASS =
				"cyano/poweradvantage/api/fluid/FluidNetworkApi.class";

		private FluidApiBridge() {
			throw new IllegalAccessError("This class cannot be instantiated");
		}

		private static String[] requiredClassEntries() {
			return new String[] { FLUID_NETWORK_API_CLASS };
		}

		private static int offer(World world, BlockPos conduitPos, FluidStack availableFluid) {
			return FluidNetworkApi.offerFluid(world, conduitPos, availableFluid, MAX_TRANSFER_PER_PULSE);
		}
	}

	/**
	 * Removable compatibility adapter for the binary surface published in Power
	 * Advantage 2.3.0. No class outside this bridge performs behavioral reflection.
	 */
	private static final class LegacyFluidBridge {
		private static final String FLUID_CONDUIT_BLOCK_CLASS =
				"cyano.poweradvantage.api.fluid.FluidConduitBlock";
		private static final String CONDUIT_REGISTRY_CLASS =
				"cyano.poweradvantage.conduitnetwork.ConduitRegistry";
		private static final String FLUIDS_CLASS = "cyano.poweradvantage.init.Fluids";
		private static final String CONDUIT_TYPE_CLASS = "cyano.poweradvantage.api.ConduitType";
		private static final String POWER_REQUEST_CLASS = "cyano.poweradvantage.api.PowerRequest";
		private static final String POWER_MACHINE_CLASS = "cyano.poweradvantage.api.IPowerMachine";

		private static boolean initialized;
		private static Class<?> fluidConduitBlockClass;
		private static java.lang.reflect.Method getRegistryInstance;
		private static java.lang.reflect.Method getRequestsForPower;
		private static java.lang.reflect.Method fluidToConduitType;
		private static java.lang.reflect.Method addEnergy;
		private static java.lang.reflect.Field fluidConduitGeneral;
		private static java.lang.reflect.Field requestAmount;
		private static java.lang.reflect.Field requestPriority;
		private static java.lang.reflect.Field requestEntity;
		private static byte minimumPipePriority;

		private LegacyFluidBridge() {
			throw new IllegalAccessError("This class cannot be instantiated");
		}

		private static String[] requiredClassEntries() {
			return new String[] {
					FLUID_CONDUIT_BLOCK_CLASS.replace('.', '/') + ".class",
					CONDUIT_REGISTRY_CLASS.replace('.', '/') + ".class",
					FLUIDS_CLASS.replace('.', '/') + ".class",
					CONDUIT_TYPE_CLASS.replace('.', '/') + ".class",
					POWER_REQUEST_CLASS.replace('.', '/') + ".class",
					POWER_MACHINE_CLASS.replace('.', '/') + ".class"
			};
		}

		private static int offer(World world, BlockPos conduitPos, FluidStack availableFluid)
				throws ReflectiveOperationException {
			initialize();
			Block conduitBlock = world.getBlockState(conduitPos).getBlock();
			if (!fluidConduitBlockClass.isInstance(conduitBlock)) {
				return NO_TRANSFER;
			}

			Object fluidNetworkType = fluidConduitGeneral.get(null);
			Object fluidType = fluidToConduitType.invoke(null, availableFluid.getFluid());
			Object registry = getRegistryInstance.invoke(null);
			Object requestsObject = getRequestsForPower.invoke(
					registry, world, conduitPos, fluidNetworkType, fluidType);
			if (!(requestsObject instanceof List<?>)) {
				return NO_TRANSFER;
			}

			int offered = Math.min(availableFluid.amount, MAX_TRANSFER_PER_PULSE);
			int remaining = offered;
			for (Object request : (List<?>) requestsObject) {
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
				int requested = Math.min(remaining, positiveInt(requestAmount.getFloat(request)));
				if (requested <= 0) {
					continue;
				}

				Object acceptedObject = addEnergy.invoke(entity, Float.valueOf(requested), fluidType);
				int accepted = acceptedObject instanceof Number
						? positiveInt(((Number) acceptedObject).floatValue()) : NO_TRANSFER;
				remaining -= Math.min(requested, accepted);
			}
			return offered - remaining;
		}

		private static synchronized void initialize() throws ReflectiveOperationException {
			if (initialized) {
				return;
			}

			ClassLoader loader = PowerAdvantageFluidCompat.class.getClassLoader();
			fluidConduitBlockClass = Class.forName(FLUID_CONDUIT_BLOCK_CLASS, false, loader);
			Class<?> conduitRegistryClass = Class.forName(CONDUIT_REGISTRY_CLASS, false, loader);
			Class<?> fluidsClass = Class.forName(FLUIDS_CLASS, false, loader);
			Class<?> conduitTypeClass = Class.forName(CONDUIT_TYPE_CLASS, false, loader);
			Class<?> powerRequestClass = Class.forName(POWER_REQUEST_CLASS, false, loader);
			Class<?> powerMachineClass = Class.forName(POWER_MACHINE_CLASS, false, loader);

			getRegistryInstance = conduitRegistryClass.getMethod("getInstance");
			getRequestsForPower = conduitRegistryClass.getMethod("getRequestsForPower",
					World.class, BlockPos.class, conduitTypeClass, conduitTypeClass);
			fluidToConduitType = fluidsClass.getMethod("fluidToConduitType", Fluid.class);
			addEnergy = powerMachineClass.getMethod("addEnergy", float.class, conduitTypeClass);
			fluidConduitGeneral = fluidsClass.getField("fluidConduit_general");
			requestAmount = powerRequestClass.getField("amount");
			requestPriority = powerRequestClass.getField("priority");
			requestEntity = powerRequestClass.getField("entity");
			minimumPipePriority = powerRequestClass.getField("LAST_PRIORITY").getByte(null);
			initialized = true;
		}

		private static int positiveInt(float value) {
			if (Float.isNaN(value) || value <= 0.0F) {
				return NO_TRANSFER;
			}
			return value >= Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) value;
		}
	}
}
