package com.mcmoddev.ironagefurniture;

import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;

// This seems really not nice design, it'll do til we refactor the whole thing..
@Mod.EventBusSubscriber(modid = Ironagefurniture.MODID)
public class IAFEventBusSubscriber {
	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event) {
		for (Block block : Ironagefurniture.BlockRegistry.values()) {
			event.getRegistry().register(block);
		}
	}

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		event.getRegistry().registerAll(
				Ironagefurniture.ItemRegistry.values().toArray(new Item[Ironagefurniture.ItemRegistry.size()]));

		for (Map.Entry<String, Block> map : Ironagefurniture.BlockRegistry.entrySet())  {
			if (!map.getKey().contains("ITEMLESS"))
				OreDictionary.registerOre(map.getKey(), map.getValue());
		}
		
		
		for (Map.Entry<String, Item> map : Ironagefurniture.ItemRegistry.entrySet())
			OreDictionary.registerOre(map.getKey(), map.getValue());
	}
}