package com.mcmoddev.ironagefurniture.init;

import com.mcmoddev.ironagefurniture.BlockObjectHolder;
import com.mcmoddev.ironagefurniture.Ironagefurniture;
import com.mcmoddev.ironagefurniture.api.Blocks.Chair;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.block.SoundType;
import net.minecraft.item.BlockItem;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Ironagefurniture.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ItemInitialiser {
    public static final BlockItem basalt = null;
    
    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
    	    	
    	event.getRegistry().registerAll(
    			new BlockItem(BlockObjectHolder.chair_wood_ironage_classic_oak, new BlockItem.Properties().group(ItemGroup.BUILDING_BLOCKS)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_classic_oak.getRegistryName().getPath()),
    			new BlockItem(BlockObjectHolder.chair_wood_ironage_classic_acacia, new BlockItem.Properties().group(ItemGroup.BUILDING_BLOCKS)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_classic_acacia.getRegistryName().getPath()),
    			new BlockItem(BlockObjectHolder.chair_wood_ironage_classic_dark_oak, new BlockItem.Properties().group(ItemGroup.BUILDING_BLOCKS)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_classic_dark_oak.getRegistryName().getPath()),
    			new BlockItem(BlockObjectHolder.chair_wood_ironage_classic_birch, new BlockItem.Properties().group(ItemGroup.BUILDING_BLOCKS)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_classic_birch.getRegistryName().getPath()),
    			new BlockItem(BlockObjectHolder.chair_wood_ironage_classic_jungle, new BlockItem.Properties().group(ItemGroup.BUILDING_BLOCKS)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_classic_jungle.getRegistryName().getPath()),
    			new BlockItem(BlockObjectHolder.chair_wood_ironage_classic_spruce, new BlockItem.Properties().group(ItemGroup.BUILDING_BLOCKS)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_classic_spruce.getRegistryName().getPath())
    			);
    }
}

