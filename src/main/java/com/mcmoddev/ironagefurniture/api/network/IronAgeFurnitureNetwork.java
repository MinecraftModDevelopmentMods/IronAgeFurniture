package com.mcmoddev.ironagefurniture.api.network;

import com.mcmoddev.ironagefurniture.Ironagefurniture;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public final class IronAgeFurnitureNetwork {
	public static final SimpleNetworkWrapper channel = NetworkRegistry.INSTANCE.newSimpleChannel(
		Ironagefurniture.MODID);

	private IronAgeFurnitureNetwork() {
		throw new IllegalAccessError("This class cannot be instantiated");
	}

	public static void init() {
		channel.registerMessage(FoudreLabelMessage.Handler.class, FoudreLabelMessage.class, 0, Side.SERVER);
		channel.registerMessage(PotStillStartMessage.Handler.class, PotStillStartMessage.class, 1, Side.SERVER);
		channel.registerMessage(FoudreSealMessage.Handler.class, FoudreSealMessage.class, 2, Side.SERVER);
		channel.registerMessage(FoudreFlushMessage.Handler.class, FoudreFlushMessage.class, 3, Side.SERVER);
		channel.registerMessage(PotStillFlushMessage.Handler.class, PotStillFlushMessage.class, 4, Side.SERVER);
		channel.registerMessage(FoudreBarrelTransferMessage.Handler.class, FoudreBarrelTransferMessage.class, 5,
			Side.SERVER);
		channel.registerMessage(PotStillBarrelTransferMessage.Handler.class, PotStillBarrelTransferMessage.class, 6,
			Side.SERVER);
		channel.registerMessage(BarrelSealMessage.Handler.class, BarrelSealMessage.class, 7, Side.SERVER);
		channel.registerMessage(FoudrePortToggleMessage.Handler.class, FoudrePortToggleMessage.class, 8,
			Side.SERVER);
	}
}
