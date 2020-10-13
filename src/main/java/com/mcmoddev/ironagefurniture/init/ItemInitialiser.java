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
    			new BlockItem(BlockObjectHolder.chair_wood_ironage_classic_oak, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_classic_oak.getRegistryName().getPath()),
    			new BlockItem(BlockObjectHolder.chair_wood_ironage_classic_acacia, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_classic_acacia.getRegistryName().getPath()),
    			new BlockItem(BlockObjectHolder.chair_wood_ironage_classic_dark_oak, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_classic_dark_oak.getRegistryName().getPath()),
    			new BlockItem(BlockObjectHolder.chair_wood_ironage_classic_birch, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_classic_birch.getRegistryName().getPath()),
    			new BlockItem(BlockObjectHolder.chair_wood_ironage_classic_jungle, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_classic_jungle.getRegistryName().getPath()),
    			new BlockItem(BlockObjectHolder.chair_wood_ironage_classic_spruce, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_classic_spruce.getRegistryName().getPath()),
    			new BlockItem(BlockObjectHolder.chair_wood_ironage_shield_oak, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_shield_oak.getRegistryName().getPath()),
    			new BlockItem(BlockObjectHolder.chair_wood_ironage_shield_acacia, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_shield_acacia.getRegistryName().getPath()),
    			new BlockItem(BlockObjectHolder.chair_wood_ironage_shield_dark_oak, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_shield_dark_oak.getRegistryName().getPath()),
    			new BlockItem(BlockObjectHolder.chair_wood_ironage_shield_birch, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_shield_birch.getRegistryName().getPath()),
    			new BlockItem(BlockObjectHolder.chair_wood_ironage_shield_jungle, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_shield_jungle.getRegistryName().getPath()),
    			new BlockItem(BlockObjectHolder.chair_wood_ironage_shield_spruce, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_shield_spruce.getRegistryName().getPath()),
    			new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_short_oak, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_short_oak.getRegistryName().getPath()),
    			new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_short_acacia, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_short_acacia.getRegistryName().getPath()),
    			new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_short_dark_oak, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_short_dark_oak.getRegistryName().getPath()),
    			new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_short_birch, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_short_birch.getRegistryName().getPath()),
    			new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_short_jungle, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_short_jungle.getRegistryName().getPath()),
    			new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_short_spruce, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_short_spruce.getRegistryName().getPath()),
    			new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_tall_oak, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_tall_oak.getRegistryName().getPath()),
    			new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_tall_acacia, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_tall_acacia.getRegistryName().getPath()),
    			new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_tall_dark_oak, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_tall_dark_oak.getRegistryName().getPath()),
    			new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_tall_birch, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_tall_birch.getRegistryName().getPath()),
    			new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_tall_jungle, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_tall_jungle.getRegistryName().getPath()),
    			new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_tall_spruce, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_tall_spruce.getRegistryName().getPath())
    			
    	);
    	
    	if (IronAgeFurnitureConfiguration.CLIENT.INTEGRATION_BIOMESOPLENTY.get() && ModList.get().isLoaded("biomesoplenty")) {
    		if (IronAgeFurnitureConfiguration.CLIENT.GENERATE_CLASSIC_CHAIRS.get()) {
		    	event.getRegistry().registerAll(
				    	new BlockItem(BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_cherry, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_cherry.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_fir, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_fir.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_hellbark, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_hellbark.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_jacaranda, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_jacaranda.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_magic, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_magic.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_mahogany, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_mahogany.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_palm, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_palm.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_redwood, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_redwood.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_umbran, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_umbran.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_willow, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_willow.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_dead, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_classic_biomesoplenty_dead.getRegistryName().getPath())
				);
    		}
    		
    		if (IronAgeFurnitureConfiguration.CLIENT.GENERATE_SHIELD_CHAIRS.get()) {
		    	event.getRegistry().registerAll(
				    	new BlockItem(BlockObjectHolder.chair_wood_ironage_shield_biomesoplenty_cherry, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_shield_biomesoplenty_cherry.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_shield_biomesoplenty_fir, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_shield_biomesoplenty_fir.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_shield_biomesoplenty_hellbark, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_shield_biomesoplenty_hellbark.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_shield_biomesoplenty_jacaranda, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_shield_biomesoplenty_jacaranda.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_shield_biomesoplenty_magic, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_shield_biomesoplenty_magic.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_shield_biomesoplenty_mahogany, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_shield_biomesoplenty_mahogany.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_shield_biomesoplenty_palm, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_shield_biomesoplenty_palm.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_shield_biomesoplenty_redwood, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_shield_biomesoplenty_redwood.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_shield_biomesoplenty_umbran, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_shield_biomesoplenty_umbran.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_shield_biomesoplenty_willow, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_shield_biomesoplenty_willow.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_shield_biomesoplenty_dead, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_shield_biomesoplenty_dead.getRegistryName().getPath())
				);
    		}
    		
    		if (IronAgeFurnitureConfiguration.CLIENT.GENERATE_SHORT_STOOLS.get()) {
		    	event.getRegistry().registerAll(
				    	new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_short_biomesoplenty_cherry, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_short_biomesoplenty_cherry.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_short_biomesoplenty_fir, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_short_biomesoplenty_fir.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_short_biomesoplenty_hellbark, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_short_biomesoplenty_hellbark.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_short_biomesoplenty_jacaranda, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_short_biomesoplenty_jacaranda.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_short_biomesoplenty_magic, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_short_biomesoplenty_magic.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_short_biomesoplenty_mahogany, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_short_biomesoplenty_mahogany.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_short_biomesoplenty_palm, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_short_biomesoplenty_palm.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_short_biomesoplenty_redwood, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_short_biomesoplenty_redwood.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_short_biomesoplenty_umbran, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_short_biomesoplenty_umbran.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_short_biomesoplenty_willow, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_short_biomesoplenty_willow.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_short_biomesoplenty_dead, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_short_biomesoplenty_dead.getRegistryName().getPath())
				);
    		}
    		
    		if (IronAgeFurnitureConfiguration.CLIENT.GENERATE_TALL_STOOLS.get()) {
		    	event.getRegistry().registerAll(
				    	new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_tall_biomesoplenty_cherry, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_tall_biomesoplenty_cherry.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_tall_biomesoplenty_fir, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_tall_biomesoplenty_fir.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_tall_biomesoplenty_hellbark, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_tall_biomesoplenty_hellbark.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_tall_biomesoplenty_jacaranda, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_tall_biomesoplenty_jacaranda.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_tall_biomesoplenty_magic, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_tall_biomesoplenty_magic.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_tall_biomesoplenty_mahogany, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_tall_biomesoplenty_mahogany.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_tall_biomesoplenty_palm, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_tall_biomesoplenty_palm.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_tall_biomesoplenty_redwood, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_tall_biomesoplenty_redwood.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_tall_biomesoplenty_umbran, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_tall_biomesoplenty_umbran.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_tall_biomesoplenty_willow, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_tall_biomesoplenty_willow.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_tall_biomesoplenty_dead, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_tall_biomesoplenty_dead.getRegistryName().getPath())
				);
    		}
    	}
    	
    	if (IronAgeFurnitureConfiguration.CLIENT.INTEGRATION_BIOMESYOUGO.get() && ModList.get().isLoaded("byg")) {
    		if (IronAgeFurnitureConfiguration.CLIENT.GENERATE_CLASSIC_CHAIRS.get()) {
		    	event.getRegistry().registerAll(
				    	new BlockItem(BlockObjectHolder.chair_wood_ironage_classic_byg_aspen, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_classic_byg_aspen.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_classic_byg_baobab, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_classic_byg_baobab.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_classic_byg_blue_enchanted, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_classic_byg_blue_enchanted.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_classic_byg_bulbis, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_classic_byg_bulbis.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_classic_byg_cherry, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_classic_byg_cherry.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_classic_byg_cika, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_classic_byg_cika.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_classic_byg_cypress, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_classic_byg_cypress.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_classic_byg_ebony, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_classic_byg_ebony.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_classic_byg_embur, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_classic_byg_embur.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_classic_byg_fir, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_classic_byg_fir.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_classic_byg_glacial_oak, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_classic_byg_glacial_oak.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_classic_byg_green_enchanted, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_classic_byg_green_enchanted.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_classic_byg_holly, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_classic_byg_holly.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_classic_byg_jacaranda, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_classic_byg_jacaranda.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_classic_byg_mahogany, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_classic_byg_mahogany.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_classic_byg_mangrove, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_classic_byg_mangrove.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_classic_byg_maple, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_classic_byg_maple.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_classic_byg_pine, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_classic_byg_pine.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_classic_byg_rainbow_eucalyptus, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_classic_byg_rainbow_eucalyptus.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_classic_byg_redwood, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_classic_byg_redwood.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_classic_byg_skyris, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_classic_byg_skyris.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_classic_byg_willow, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_classic_byg_willow.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_classic_byg_witch_hazel, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_classic_byg_witch_hazel.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_classic_byg_zelkova, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_classic_byg_zelkova.getRegistryName().getPath())
				);
    		}
    		
    		if (IronAgeFurnitureConfiguration.CLIENT.GENERATE_SHIELD_CHAIRS.get()) {
		    	event.getRegistry().registerAll(
				    	new BlockItem(BlockObjectHolder.chair_wood_ironage_shield_byg_aspen, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_shield_byg_aspen.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_shield_byg_baobab, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_shield_byg_baobab.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_shield_byg_blue_enchanted, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_shield_byg_blue_enchanted.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_shield_byg_bulbis, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_shield_byg_bulbis.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_shield_byg_cherry, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_shield_byg_cherry.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_shield_byg_cika, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_shield_byg_cika.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_shield_byg_cypress, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_shield_byg_cypress.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_shield_byg_ebony, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_shield_byg_ebony.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_shield_byg_embur, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_shield_byg_embur.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_shield_byg_fir, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_shield_byg_fir.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_shield_byg_glacial_oak, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_shield_byg_glacial_oak.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_shield_byg_green_enchanted, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_shield_byg_green_enchanted.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_shield_byg_holly, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_shield_byg_holly.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_shield_byg_jacaranda, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_shield_byg_jacaranda.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_shield_byg_mahogany, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_shield_byg_mahogany.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_shield_byg_mangrove, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_shield_byg_mangrove.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_shield_byg_maple, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_shield_byg_maple.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_shield_byg_pine, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_shield_byg_pine.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_shield_byg_rainbow_eucalyptus, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_shield_byg_rainbow_eucalyptus.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_shield_byg_redwood, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_shield_byg_redwood.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_shield_byg_skyris, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_shield_byg_skyris.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_shield_byg_willow, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_shield_byg_willow.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_shield_byg_witch_hazel, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_shield_byg_witch_hazel.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_shield_byg_zelkova, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_shield_byg_zelkova.getRegistryName().getPath())
									);
    		}
    		
    		if (IronAgeFurnitureConfiguration.CLIENT.GENERATE_SHORT_STOOLS.get()) {
		    	event.getRegistry().registerAll(
				    	new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_short_byg_aspen, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_short_byg_aspen.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_short_byg_baobab, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_short_byg_baobab.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_short_byg_blue_enchanted, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_short_byg_blue_enchanted.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_short_byg_bulbis, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_short_byg_bulbis.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_short_byg_cherry, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_short_byg_cherry.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_short_byg_cika, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_short_byg_cika.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_short_byg_cypress, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_short_byg_cypress.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_short_byg_ebony, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_short_byg_ebony.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_short_byg_embur, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_short_byg_embur.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_short_byg_fir, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_short_byg_fir.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_short_byg_glacial_oak, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_short_byg_glacial_oak.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_short_byg_green_enchanted, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_short_byg_green_enchanted.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_short_byg_holly, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_short_byg_holly.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_short_byg_jacaranda, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_short_byg_jacaranda.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_short_byg_mahogany, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_short_byg_mahogany.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_short_byg_mangrove, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_short_byg_mangrove.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_short_byg_maple, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_short_byg_maple.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_short_byg_pine, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_short_byg_pine.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_short_byg_rainbow_eucalyptus, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_short_byg_rainbow_eucalyptus.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_short_byg_redwood, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_short_byg_redwood.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_short_byg_skyris, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_short_byg_skyris.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_short_byg_willow, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_short_byg_willow.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_short_byg_witch_hazel, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_short_byg_witch_hazel.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_short_byg_zelkova, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_short_byg_zelkova.getRegistryName().getPath())
						);
    		}
    		
    		if (IronAgeFurnitureConfiguration.CLIENT.GENERATE_TALL_STOOLS.get()) {
		    	event.getRegistry().registerAll(
				    	new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_tall_byg_aspen, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_tall_byg_aspen.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_tall_byg_baobab, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_tall_byg_baobab.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_tall_byg_blue_enchanted, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_tall_byg_blue_enchanted.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_tall_byg_bulbis, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_tall_byg_bulbis.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_tall_byg_cherry, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_tall_byg_cherry.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_tall_byg_cika, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_tall_byg_cika.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_tall_byg_cypress, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_tall_byg_cypress.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_tall_byg_ebony, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_tall_byg_ebony.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_tall_byg_embur, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_tall_byg_embur.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_tall_byg_fir, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_tall_byg_fir.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_tall_byg_glacial_oak, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_tall_byg_glacial_oak.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_tall_byg_green_enchanted, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_tall_byg_green_enchanted.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_tall_byg_holly, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_tall_byg_holly.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_tall_byg_jacaranda, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_tall_byg_jacaranda.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_tall_byg_mahogany, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_tall_byg_mahogany.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_tall_byg_mangrove, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_tall_byg_mangrove.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_tall_byg_maple, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_tall_byg_maple.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_tall_byg_pine, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_tall_byg_pine.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_tall_byg_rainbow_eucalyptus, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_tall_byg_rainbow_eucalyptus.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_tall_byg_redwood, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_tall_byg_redwood.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_tall_byg_skyris, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_tall_byg_skyris.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_tall_byg_willow, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_tall_byg_willow.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_tall_byg_witch_hazel, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_tall_byg_witch_hazel.getRegistryName().getPath()),
						new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_tall_byg_zelkova, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_tall_byg_zelkova.getRegistryName().getPath())
					);
    		}
    	}
    	
    	
    	if (IronAgeFurnitureConfiguration.CLIENT.INTEGRATION_IMMERSIVEENGINEERING.get() && ModList.get().isLoaded("immersiveengineering")) {
			if (IronAgeFurnitureConfiguration.CLIENT.GENERATE_CLASSIC_CHAIRS.get()) {
				event.getRegistry().registerAll(
				    	new BlockItem(BlockObjectHolder.chair_wood_ironage_classic_immersiveengineering_treated_wood, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_classic_immersiveengineering_treated_wood.getRegistryName().getPath())
				);
			}
		
			if (IronAgeFurnitureConfiguration.CLIENT.GENERATE_SHIELD_CHAIRS.get()) {
				event.getRegistry().registerAll(
				    	new BlockItem(BlockObjectHolder.chair_wood_ironage_shield_immersiveengineering_treated_wood, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_shield_immersiveengineering_treated_wood.getRegistryName().getPath())
				);
			}
			
			if (IronAgeFurnitureConfiguration.CLIENT.GENERATE_SHORT_STOOLS.get()) {
				event.getRegistry().registerAll(
				    	new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_short_immersiveengineering_treated_wood, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_short_immersiveengineering_treated_wood.getRegistryName().getPath())
				);
			}
			
			if (IronAgeFurnitureConfiguration.CLIENT.GENERATE_SHORT_STOOLS.get()) {
				event.getRegistry().registerAll(
				    	new BlockItem(BlockObjectHolder.chair_wood_ironage_stool_tall_immersiveengineering_treated_wood, new BlockItem.Properties().group(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.chair_wood_ironage_stool_tall_immersiveengineering_treated_wood.getRegistryName().getPath())
				);
			}
		}
    }
}
