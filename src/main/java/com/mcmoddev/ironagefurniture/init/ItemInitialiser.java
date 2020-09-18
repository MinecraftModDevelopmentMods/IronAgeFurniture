package com.mcmoddev.ironagefurniture.init;

import com.mcmoddev.ironagefurniture.BlockObjectHolder;
import com.mcmoddev.ironagefurniture.IronAgeFurnitureConfiguration;
import com.mcmoddev.ironagefurniture.Ironagefurniture;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.BlockItem;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
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
    	
    	if (IronAgeFurnitureConfiguration.CLIENT.INTEGRATION_BIOMESOPLENTY.get() && ModList.get().isLoaded("biomesoplenty")) {
    		if (IronAgeFurnitureConfiguration.CLIENT.GENERATE_CLASSIC_CHAIRS.get()) {
		    	event.getRegistry().registerAll(
				    	new BlockItem(BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_cherry, new BlockItem.Properties().group(ItemGroup.BUILDING_BLOCKS)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_cherry.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_ethereal, new BlockItem.Properties().group(ItemGroup.BUILDING_BLOCKS)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_ethereal.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_fir, new BlockItem.Properties().group(ItemGroup.BUILDING_BLOCKS)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_fir.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_hellbark, new BlockItem.Properties().group(ItemGroup.BUILDING_BLOCKS)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_hellbark.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_jacaranda, new BlockItem.Properties().group(ItemGroup.BUILDING_BLOCKS)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_jacaranda.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_magic, new BlockItem.Properties().group(ItemGroup.BUILDING_BLOCKS)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_magic.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_mahogany, new BlockItem.Properties().group(ItemGroup.BUILDING_BLOCKS)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_mahogany.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_palm, new BlockItem.Properties().group(ItemGroup.BUILDING_BLOCKS)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_palm.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_redwood, new BlockItem.Properties().group(ItemGroup.BUILDING_BLOCKS)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_redwood.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_umbran, new BlockItem.Properties().group(ItemGroup.BUILDING_BLOCKS)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_umbran.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_willow, new BlockItem.Properties().group(ItemGroup.BUILDING_BLOCKS)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_willow.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_dead, new BlockItem.Properties().group(ItemGroup.BUILDING_BLOCKS)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_dead.getRegistryName().getPath())
				);
    		}
    		
    		if (IronAgeFurnitureConfiguration.CLIENT.GENERATE_SHIELD_CHAIRS.get()) {
		    	event.getRegistry().registerAll(
				    	new BlockItem(BlockObjectHolder.chair_wood_ironage_shield_biomesoplenty_cherry, new BlockItem.Properties().group(ItemGroup.BUILDING_BLOCKS)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_shield_biomesoplenty_cherry.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_shield_biomesoplenty_ethereal, new BlockItem.Properties().group(ItemGroup.BUILDING_BLOCKS)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_shield_biomesoplenty_ethereal.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_shield_biomesoplenty_fir, new BlockItem.Properties().group(ItemGroup.BUILDING_BLOCKS)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_shield_biomesoplenty_fir.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_shield_biomesoplenty_hellbark, new BlockItem.Properties().group(ItemGroup.BUILDING_BLOCKS)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_shield_biomesoplenty_hellbark.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_shield_biomesoplenty_jacaranda, new BlockItem.Properties().group(ItemGroup.BUILDING_BLOCKS)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_shield_biomesoplenty_jacaranda.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_shield_biomesoplenty_magic, new BlockItem.Properties().group(ItemGroup.BUILDING_BLOCKS)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_shield_biomesoplenty_magic.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_shield_biomesoplenty_mahogany, new BlockItem.Properties().group(ItemGroup.BUILDING_BLOCKS)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_shield_biomesoplenty_mahogany.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_shield_biomesoplenty_palm, new BlockItem.Properties().group(ItemGroup.BUILDING_BLOCKS)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_shield_biomesoplenty_palm.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_shield_biomesoplenty_redwood, new BlockItem.Properties().group(ItemGroup.BUILDING_BLOCKS)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_shield_biomesoplenty_redwood.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_shield_biomesoplenty_umbran, new BlockItem.Properties().group(ItemGroup.BUILDING_BLOCKS)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_shield_biomesoplenty_umbran.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_shield_biomesoplenty_willow, new BlockItem.Properties().group(ItemGroup.BUILDING_BLOCKS)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_shield_biomesoplenty_willow.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_shield_biomesoplenty_dead, new BlockItem.Properties().group(ItemGroup.BUILDING_BLOCKS)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_shield_biomesoplenty_dead.getRegistryName().getPath())
				);
    		}
    		
    		if (IronAgeFurnitureConfiguration.CLIENT.GENERATE_SHORT_STOOLS.get()) {
		    	event.getRegistry().registerAll(
				    	new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_short_biomesoplenty_cherry, new BlockItem.Properties().group(ItemGroup.BUILDING_BLOCKS)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_short_biomesoplenty_cherry.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_short_biomesoplenty_ethereal, new BlockItem.Properties().group(ItemGroup.BUILDING_BLOCKS)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_short_biomesoplenty_ethereal.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_short_biomesoplenty_fir, new BlockItem.Properties().group(ItemGroup.BUILDING_BLOCKS)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_short_biomesoplenty_fir.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_short_biomesoplenty_hellbark, new BlockItem.Properties().group(ItemGroup.BUILDING_BLOCKS)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_short_biomesoplenty_hellbark.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_short_biomesoplenty_jacaranda, new BlockItem.Properties().group(ItemGroup.BUILDING_BLOCKS)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_short_biomesoplenty_jacaranda.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_short_biomesoplenty_magic, new BlockItem.Properties().group(ItemGroup.BUILDING_BLOCKS)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_short_biomesoplenty_magic.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_short_biomesoplenty_mahogany, new BlockItem.Properties().group(ItemGroup.BUILDING_BLOCKS)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_short_biomesoplenty_mahogany.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_short_biomesoplenty_palm, new BlockItem.Properties().group(ItemGroup.BUILDING_BLOCKS)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_short_biomesoplenty_palm.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_short_biomesoplenty_redwood, new BlockItem.Properties().group(ItemGroup.BUILDING_BLOCKS)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_short_biomesoplenty_redwood.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_short_biomesoplenty_umbran, new BlockItem.Properties().group(ItemGroup.BUILDING_BLOCKS)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_short_biomesoplenty_umbran.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_short_biomesoplenty_willow, new BlockItem.Properties().group(ItemGroup.BUILDING_BLOCKS)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_short_biomesoplenty_willow.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_short_biomesoplenty_dead, new BlockItem.Properties().group(ItemGroup.BUILDING_BLOCKS)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_short_biomesoplenty_dead.getRegistryName().getPath())
				);
    		}
    		
    		if (IronAgeFurnitureConfiguration.CLIENT.GENERATE_TALL_STOOLS.get()) {
		    	event.getRegistry().registerAll(
				    	new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_tall_biomesoplenty_cherry, new BlockItem.Properties().group(ItemGroup.BUILDING_BLOCKS)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_tall_biomesoplenty_cherry.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_tall_biomesoplenty_ethereal, new BlockItem.Properties().group(ItemGroup.BUILDING_BLOCKS)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_tall_biomesoplenty_ethereal.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_tall_biomesoplenty_fir, new BlockItem.Properties().group(ItemGroup.BUILDING_BLOCKS)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_tall_biomesoplenty_fir.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_tall_biomesoplenty_hellbark, new BlockItem.Properties().group(ItemGroup.BUILDING_BLOCKS)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_tall_biomesoplenty_hellbark.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_tall_biomesoplenty_jacaranda, new BlockItem.Properties().group(ItemGroup.BUILDING_BLOCKS)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_tall_biomesoplenty_jacaranda.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_tall_biomesoplenty_magic, new BlockItem.Properties().group(ItemGroup.BUILDING_BLOCKS)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_tall_biomesoplenty_magic.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_tall_biomesoplenty_mahogany, new BlockItem.Properties().group(ItemGroup.BUILDING_BLOCKS)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_tall_biomesoplenty_mahogany.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_tall_biomesoplenty_palm, new BlockItem.Properties().group(ItemGroup.BUILDING_BLOCKS)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_tall_biomesoplenty_palm.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_tall_biomesoplenty_redwood, new BlockItem.Properties().group(ItemGroup.BUILDING_BLOCKS)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_tall_biomesoplenty_redwood.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_tall_biomesoplenty_umbran, new BlockItem.Properties().group(ItemGroup.BUILDING_BLOCKS)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_tall_biomesoplenty_umbran.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_tall_biomesoplenty_willow, new BlockItem.Properties().group(ItemGroup.BUILDING_BLOCKS)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_tall_biomesoplenty_willow.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_tall_biomesoplenty_dead, new BlockItem.Properties().group(ItemGroup.BUILDING_BLOCKS)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_tall_biomesoplenty_dead.getRegistryName().getPath())
				);
    		}
    	}
    }
}

//chair_wood_ironage_classic_biomesoplenty_cherry 	
//chair_wood_ironage_classic_biomesoplenty_ethereal 
//chair_wood_ironage_classic_biomesoplenty_fir 		
//chair_wood_ironage_classic_biomesoplenty_hellbark 
//chair_wood_ironage_classic_biomesoplenty_jacaranda
//chair_wood_ironage_classic_biomesoplenty_magic 	
//chair_wood_ironage_classic_biomesoplenty_mahogany 
//chair_wood_ironage_classic_biomesoplenty_palm 	
//chair_wood_ironage_classic_biomesoplenty_redwood 	
//chair_wood_ironage_classic_biomesoplenty_umbran 	
//chair_wood_ironage_classic_biomesoplenty_willow 	
//chair_wood_ironage_classic_biomesoplenty_dead 	
