package com.mcmoddev.ironagefurniture.init;

import com.mcmoddev.ironagefurniture.IronAgeFurnitureConfiguration;
import com.mcmoddev.ironagefurniture.Ironagefurniture;
import com.mcmoddev.ironagefurniture.api.Blocks.Chair;
import com.mcmoddev.ironagefurniture.api.Blocks.Stool;
import com.mcmoddev.ironagefurniture.api.Blocks.TallStool;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;


/**
 * This class initialises all blocks in ironagefurniture.
 *
 * @author SkyBlade1978
 *
 */
@Mod.EventBusSubscriber(modid = Ironagefurniture.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BlockInitialiser {
	
	 @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
		event.getRegistry().register(new Chair(1,10, SoundType.WOOD, "chair_wood_ironage_classic_oak"));		
		event.getRegistry().register(new Chair(1,10, SoundType.WOOD, "chair_wood_ironage_classic_acacia"));
		event.getRegistry().register(new Chair(1,10, SoundType.WOOD, "chair_wood_ironage_classic_dark_oak"));
		event.getRegistry().register(new Chair(1,10, SoundType.WOOD, "chair_wood_ironage_classic_birch"));
		event.getRegistry().register(new Chair(1,10, SoundType.WOOD, "chair_wood_ironage_classic_jungle"));
		event.getRegistry().register(new Chair(1,10, SoundType.WOOD, "chair_wood_ironage_classic_spruce"));

		if (IronAgeFurnitureConfiguration.CLIENT.GENERATE_SHIELD_CHAIRS.get()) {
			event.getRegistry().register(new Chair(1,10, SoundType.WOOD, "chair_wood_ironage_shield_oak"));		
			event.getRegistry().register(new Chair(1,10, SoundType.WOOD, "chair_wood_ironage_shield_acacia"));
			event.getRegistry().register(new Chair(1,10, SoundType.WOOD, "chair_wood_ironage_shield_dark_oak"));
			event.getRegistry().register(new Chair(1,10, SoundType.WOOD, "chair_wood_ironage_shield_birch"));
			event.getRegistry().register(new Chair(1,10, SoundType.WOOD, "chair_wood_ironage_shield_jungle"));
			event.getRegistry().register(new Chair(1,10, SoundType.WOOD, "chair_wood_ironage_shield_spruce"));
		}
		
		if (IronAgeFurnitureConfiguration.CLIENT.GENERATE_SHORT_STOOLS.get()) {
			event.getRegistry().register(new Stool(1,10, SoundType.WOOD, "chair_wood_ironage_stool_short_oak"));		
			event.getRegistry().register(new Stool(1,10, SoundType.WOOD, "chair_wood_ironage_stool_short_acacia"));
			event.getRegistry().register(new Stool(1,10, SoundType.WOOD, "chair_wood_ironage_stool_short_dark_oak"));
			event.getRegistry().register(new Stool(1,10, SoundType.WOOD, "chair_wood_ironage_stool_short_birch"));
			event.getRegistry().register(new Stool(1,10, SoundType.WOOD, "chair_wood_ironage_stool_short_jungle"));
			event.getRegistry().register(new Stool(1,10, SoundType.WOOD, "chair_wood_ironage_stool_short_spruce"));
		}
	
		if (IronAgeFurnitureConfiguration.CLIENT.GENERATE_TALL_STOOLS.get()) {
			event.getRegistry().register(new TallStool(1,10, SoundType.WOOD, "chair_wood_ironage_stool_tall_oak"));		
			event.getRegistry().register(new TallStool(1,10, SoundType.WOOD, "chair_wood_ironage_stool_tall_acacia"));
			event.getRegistry().register(new TallStool(1,10, SoundType.WOOD, "chair_wood_ironage_stool_tall_dark_oak"));
			event.getRegistry().register(new TallStool(1,10, SoundType.WOOD, "chair_wood_ironage_stool_tall_birch"));
			event.getRegistry().register(new TallStool(1,10, SoundType.WOOD, "chair_wood_ironage_stool_tall_jungle"));
			event.getRegistry().register(new TallStool(1,10, SoundType.WOOD, "chair_wood_ironage_stool_tall_spruce"));
		}
		
		if (IronAgeFurnitureConfiguration.CLIENT.INTEGRATION_BIOMESOPLENTY.get() && ModList.get().isLoaded("biomesoplenty")) {
			if (IronAgeFurnitureConfiguration.CLIENT.GENERATE_CLASSIC_CHAIRS.get()) {
				event.getRegistry().register(new Chair(1,10, SoundType.WOOD, "chair_wood_ironage_classic_biomesoplenty_cherry"));
				event.getRegistry().register(new Chair(1,10, SoundType.WOOD, "chair_wood_ironage_classic_biomesoplenty_ethereal"));
				event.getRegistry().register(new Chair(1,10, SoundType.WOOD, "chair_wood_ironage_classic_biomesoplenty_fir"));
				event.getRegistry().register(new Chair(1,10, SoundType.WOOD, "chair_wood_ironage_classic_biomesoplenty_hellbark"));
				event.getRegistry().register(new Chair(1,10, SoundType.WOOD, "chair_wood_ironage_classic_biomesoplenty_jacaranda"));
				event.getRegistry().register(new Chair(1,10, SoundType.WOOD, "chair_wood_ironage_classic_biomesoplenty_magic"));
				event.getRegistry().register(new Chair(1,10, SoundType.WOOD, "chair_wood_ironage_classic_biomesoplenty_mahogany"));	
				event.getRegistry().register(new Chair(1,10, SoundType.WOOD, "chair_wood_ironage_classic_biomesoplenty_palm"));
				event.getRegistry().register(new Chair(1,10, SoundType.WOOD, "chair_wood_ironage_classic_biomesoplenty_redwood"));
				event.getRegistry().register(new Chair(1,10, SoundType.WOOD, "chair_wood_ironage_classic_biomesoplenty_umbran"));
				event.getRegistry().register(new Chair(1,10, SoundType.WOOD, "chair_wood_ironage_classic_biomesoplenty_willow"));
				event.getRegistry().register(new Chair(1,10, SoundType.WOOD, "chair_wood_ironage_classic_biomesoplenty_dead"));
			}
		
			if (IronAgeFurnitureConfiguration.CLIENT.GENERATE_SHIELD_CHAIRS.get()) {
				event.getRegistry().register(new Chair(1,10, SoundType.WOOD, "chair_wood_ironage_shield_biomesoplenty_cherry"));
				event.getRegistry().register(new Chair(1,10, SoundType.WOOD, "chair_wood_ironage_shield_biomesoplenty_ethereal"));
				event.getRegistry().register(new Chair(1,10, SoundType.WOOD, "chair_wood_ironage_shield_biomesoplenty_fir"));
				event.getRegistry().register(new Chair(1,10, SoundType.WOOD, "chair_wood_ironage_shield_biomesoplenty_hellbark"));
				event.getRegistry().register(new Chair(1,10, SoundType.WOOD, "chair_wood_ironage_shield_biomesoplenty_jacaranda"));
				event.getRegistry().register(new Chair(1,10, SoundType.WOOD, "chair_wood_ironage_shield_biomesoplenty_magic"));
				event.getRegistry().register(new Chair(1,10, SoundType.WOOD, "chair_wood_ironage_shield_biomesoplenty_mahogany"));	
				event.getRegistry().register(new Chair(1,10, SoundType.WOOD, "chair_wood_ironage_shield_biomesoplenty_palm"));
				event.getRegistry().register(new Chair(1,10, SoundType.WOOD, "chair_wood_ironage_shield_biomesoplenty_redwood"));
				event.getRegistry().register(new Chair(1,10, SoundType.WOOD, "chair_wood_ironage_shield_biomesoplenty_umbran"));
				event.getRegistry().register(new Chair(1,10, SoundType.WOOD, "chair_wood_ironage_shield_biomesoplenty_willow"));
				event.getRegistry().register(new Chair(1,10, SoundType.WOOD, "chair_wood_ironage_shield_biomesoplenty_dead"));
			}
			
			if (IronAgeFurnitureConfiguration.CLIENT.GENERATE_SHORT_STOOLS.get()) {
				event.getRegistry().register(new Stool(1,10, SoundType.WOOD, "chair_wood_ironage_stool_short_biomesoplenty_cherry"));
				event.getRegistry().register(new Stool(1,10, SoundType.WOOD, "chair_wood_ironage_stool_short_biomesoplenty_ethereal"));
				event.getRegistry().register(new Stool(1,10, SoundType.WOOD, "chair_wood_ironage_stool_short_biomesoplenty_fir"));
				event.getRegistry().register(new Stool(1,10, SoundType.WOOD, "chair_wood_ironage_stool_short_biomesoplenty_hellbark"));
				event.getRegistry().register(new Stool(1,10, SoundType.WOOD, "chair_wood_ironage_stool_short_biomesoplenty_jacaranda"));
				event.getRegistry().register(new Stool(1,10, SoundType.WOOD, "chair_wood_ironage_stool_short_biomesoplenty_magic"));
				event.getRegistry().register(new Stool(1,10, SoundType.WOOD, "chair_wood_ironage_stool_short_biomesoplenty_mahogany"));	
				event.getRegistry().register(new Stool(1,10, SoundType.WOOD, "chair_wood_ironage_stool_short_biomesoplenty_palm"));
				event.getRegistry().register(new Stool(1,10, SoundType.WOOD, "chair_wood_ironage_stool_short_biomesoplenty_redwood"));
				event.getRegistry().register(new Stool(1,10, SoundType.WOOD, "chair_wood_ironage_stool_short_biomesoplenty_umbran"));
				event.getRegistry().register(new Stool(1,10, SoundType.WOOD, "chair_wood_ironage_stool_short_biomesoplenty_willow"));
				event.getRegistry().register(new Stool(1,10, SoundType.WOOD, "chair_wood_ironage_stool_short_biomesoplenty_dead"));
			}
			
			if (IronAgeFurnitureConfiguration.CLIENT.GENERATE_SHORT_STOOLS.get()) {
				event.getRegistry().register(new TallStool(1,10, SoundType.WOOD, "chair_wood_ironage_stool_tall_biomesoplenty_cherry"));
				event.getRegistry().register(new TallStool(1,10, SoundType.WOOD, "chair_wood_ironage_stool_tall_biomesoplenty_ethereal"));
				event.getRegistry().register(new TallStool(1,10, SoundType.WOOD, "chair_wood_ironage_stool_tall_biomesoplenty_fir"));
				event.getRegistry().register(new TallStool(1,10, SoundType.WOOD, "chair_wood_ironage_stool_tall_biomesoplenty_hellbark"));
				event.getRegistry().register(new TallStool(1,10, SoundType.WOOD, "chair_wood_ironage_stool_tall_biomesoplenty_jacaranda"));
				event.getRegistry().register(new TallStool(1,10, SoundType.WOOD, "chair_wood_ironage_stool_tall_biomesoplenty_magic"));
				event.getRegistry().register(new TallStool(1,10, SoundType.WOOD, "chair_wood_ironage_stool_tall_biomesoplenty_mahogany"));	
				event.getRegistry().register(new TallStool(1,10, SoundType.WOOD, "chair_wood_ironage_stool_tall_biomesoplenty_palm"));
				event.getRegistry().register(new TallStool(1,10, SoundType.WOOD, "chair_wood_ironage_stool_tall_biomesoplenty_redwood"));
				event.getRegistry().register(new TallStool(1,10, SoundType.WOOD, "chair_wood_ironage_stool_tall_biomesoplenty_umbran"));
				event.getRegistry().register(new TallStool(1,10, SoundType.WOOD, "chair_wood_ironage_stool_tall_biomesoplenty_willow"));
				event.getRegistry().register(new TallStool(1,10, SoundType.WOOD, "chair_wood_ironage_stool_tall_biomesoplenty_dead"));
			}
		}
		
		if (IronAgeFurnitureConfiguration.CLIENT.INTEGRATION_IMMERSIVEENGINEERING.get() && ModList.get().isLoaded("immersiveengineering")) {
			if (IronAgeFurnitureConfiguration.CLIENT.GENERATE_CLASSIC_CHAIRS.get()) {
				event.getRegistry().register(new Chair(1,10, SoundType.WOOD, "chair_wood_ironage_classic_immersiveengineering_treated_wood"));
			}
		
			if (IronAgeFurnitureConfiguration.CLIENT.GENERATE_SHIELD_CHAIRS.get()) {
				event.getRegistry().register(new Chair(1,10, SoundType.WOOD, "chair_wood_ironage_shield_immersiveengineering_treated_wood"));
			}
			
			if (IronAgeFurnitureConfiguration.CLIENT.GENERATE_SHORT_STOOLS.get()) {
				event.getRegistry().register(new Stool(1,10, SoundType.WOOD, "chair_wood_ironage_stool_short_immersiveengineering_treated_wood"));
			}
			
			if (IronAgeFurnitureConfiguration.CLIENT.GENERATE_SHORT_STOOLS.get()) {
				event.getRegistry().register(new TallStool(1,10, SoundType.WOOD, "chair_wood_ironage_stool_tall_immersiveengineering_treated_wood"));
			}
		}
	 }
}