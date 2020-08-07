//package com.mcmoddev.ironagefurniture;
//
//import java.util.Map;
//
//import com.mcmoddev.ironagefurniture.api.Blocks.Chair;
//
//import net.minecraft.block.Block;
//import net.minecraft.item.BlockItem;
//import net.minecraft.item.Item;
//import net.minecraft.item.ItemGroup;
//import net.minecraftforge.event.RegistryEvent;
//import net.minecraftforge.eventbus.api.SubscribeEvent;
//import net.minecraftforge.fml.common.Mod;
//
//@Mod.EventBusSubscriber(modid = Ironagefurniture.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
//public class IAFEventBusSubscriber {
//	
//	@SubscribeEvent
//    public static void registerBlocks(RegistryEvent.Register<Block> event) {
//    	
//		
//		for (Block block : Ironagefurniture.BlockRegistry.values()) {
//			event.getRegistry().register(block);
//		}
//	}
//
//	@SubscribeEvent
//    public static void registerItems(RegistryEvent.Register<Item> event) {
//		event.getRegistry().registerAll(
//		Ironagefurniture.ItemRegistry.values().toArray(new Item[Ironagefurniture.ItemRegistry.size()]));
//    }
//	
//	public BlockItem getBlockItem(Chair blockHandle) {
//		BlockItem blockItem = new BlockItem(blockHandle, new BlockItem.Properties().group(ItemGroup.BUILDING_BLOCKS));
//		
//		blockItem.setRegistryName(Mineralogy.MODID, blockHandle.getRegistryName().getPath());
//		
//		return blockItem;
//	}
////	
////	@SubscribeEvent
////	public static void registerItems(RegistryEvent.Register<Item> event) {
////		event.getRegistry().registerAll(
////				Ironagefurniture.ItemRegistry.values().toArray(new Item[Ironagefurniture.ItemRegistry.size()]));
////
////		for (Map.Entry<String, Block> map : Ironagefurniture.BlockRegistry.entrySet())  {
////			if (!map.getKey().contains("ITEMLESS"))
////				OreDictionary.registerOre(map.getKey(), map.getValue());
////		}
////		
////		
////		for (Map.Entry<String, Item> map : Ironagefurniture.ItemRegistry.entrySet())
////			OreDictionary.registerOre(map.getKey(), map.getValue());
////	}
//
////	@SubscribeEvent
////	public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
////		event.getRegistry().registerAll(MineralogyRegistry.MineralogyRecipeRegistry.values()
////				.toArray(new IRecipe[MineralogyRegistry.MineralogyRecipeRegistry.size()]));
////	}
//}