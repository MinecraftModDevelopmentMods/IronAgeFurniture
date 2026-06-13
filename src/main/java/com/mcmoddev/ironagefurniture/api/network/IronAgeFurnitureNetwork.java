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
	}
}
