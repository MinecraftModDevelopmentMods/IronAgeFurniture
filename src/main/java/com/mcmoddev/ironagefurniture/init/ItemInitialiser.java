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
    			new BlockItem(BlockObjectHolder.chair_wood_ironage_classic_spruce, new BlockItem.Properties().group(ItemGroup.BUILDING_BLOCKS)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_classic_spruce.getRegistryName().getPath()),
    			new BlockItem(BlockObjectHolder.chair_wood_ironage_shield_oak, new BlockItem.Properties().group(ItemGroup.BUILDING_BLOCKS)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_shield_oak.getRegistryName().getPath()),
    			new BlockItem(BlockObjectHolder.chair_wood_ironage_shield_acacia, new BlockItem.Properties().group(ItemGroup.BUILDING_BLOCKS)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_shield_acacia.getRegistryName().getPath()),
    			new BlockItem(BlockObjectHolder.chair_wood_ironage_shield_dark_oak, new BlockItem.Properties().group(ItemGroup.BUILDING_BLOCKS)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_shield_dark_oak.getRegistryName().getPath()),
    			new BlockItem(BlockObjectHolder.chair_wood_ironage_shield_birch, new BlockItem.Properties().group(ItemGroup.BUILDING_BLOCKS)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_shield_birch.getRegistryName().getPath()),
    			new BlockItem(BlockObjectHolder.chair_wood_ironage_shield_jungle, new BlockItem.Properties().group(ItemGroup.BUILDING_BLOCKS)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_shield_jungle.getRegistryName().getPath()),
    			new BlockItem(BlockObjectHolder.chair_wood_ironage_shield_spruce, new BlockItem.Properties().group(ItemGroup.BUILDING_BLOCKS)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_shield_spruce.getRegistryName().getPath()),
    			new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_short_oak, new BlockItem.Properties().group(ItemGroup.BUILDING_BLOCKS)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_short_oak.getRegistryName().getPath()),
    			new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_short_acacia, new BlockItem.Properties().group(ItemGroup.BUILDING_BLOCKS)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_short_acacia.getRegistryName().getPath()),
    			new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_short_dark_oak, new BlockItem.Properties().group(ItemGroup.BUILDING_BLOCKS)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_short_dark_oak.getRegistryName().getPath()),
    			new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_short_birch, new BlockItem.Properties().group(ItemGroup.BUILDING_BLOCKS)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_short_birch.getRegistryName().getPath()),
    			new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_short_jungle, new BlockItem.Properties().group(ItemGroup.BUILDING_BLOCKS)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_short_jungle.getRegistryName().getPath()),
    			new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_short_spruce, new BlockItem.Properties().group(ItemGroup.BUILDING_BLOCKS)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_short_spruce.getRegistryName().getPath()),
    			new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_tall_oak, new BlockItem.Properties().group(ItemGroup.BUILDING_BLOCKS)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_tall_oak.getRegistryName().getPath()),
    			new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_tall_acacia, new BlockItem.Properties().group(ItemGroup.BUILDING_BLOCKS)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_tall_acacia.getRegistryName().getPath()),
    			new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_tall_dark_oak, new BlockItem.Properties().group(ItemGroup.BUILDING_BLOCKS)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_tall_dark_oak.getRegistryName().getPath()),
    			new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_tall_birch, new BlockItem.Properties().group(ItemGroup.BUILDING_BLOCKS)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_tall_birch.getRegistryName().getPath()),
    			new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_tall_jungle, new BlockItem.Properties().group(ItemGroup.BUILDING_BLOCKS)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_tall_jungle.getRegistryName().getPath()),
    			new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_tall_spruce, new BlockItem.Properties().group(ItemGroup.BUILDING_BLOCKS)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_tall_spruce.getRegistryName().getPath())
    			);
    }
}

