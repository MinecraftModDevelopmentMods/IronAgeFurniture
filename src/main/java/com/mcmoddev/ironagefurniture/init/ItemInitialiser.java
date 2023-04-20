package com.mcmoddev.ironagefurniture.init;

import com.mcmoddev.ironagefurniture.BlockObjectHolder;
import com.mcmoddev.ironagefurniture.IronAgeFurnitureConfiguration;
import com.mcmoddev.ironagefurniture.Ironagefurniture;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.BlockItem;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Ironagefurniture.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ItemInitialiser {
    public static final BlockItem basalt = null;
    
    public static Block getProperty(String property) {
        try {
            return (Block)BlockObjectHolder.class.getDeclaredField(property).get(null);
        }
        catch (Exception e) {
            return null;
        }
    }
    
    private static void registerChairs(RegistryEvent.Register<Item> event, String[] woodTypes, boolean log) {
    	String[] colours = {"green", "light_blue", "light_gray", "lime", "magenta", "orange", "pink", "purple", "red", "white", "yellow", "black", "blue", "brown", "cyan", "gray"};
    	
    	for (String wood : woodTypes) {
    		if (IronAgeFurnitureConfiguration.CLIENT.GENERATE_CLASSIC_CHAIRS.get())
	    		registerItem(event, getProperty("chair_wood_ironage_classic_" + wood));
    		
    		if (IronAgeFurnitureConfiguration.CLIENT.GENERATE_SHIELD_CHAIRS.get())
    			registerItem(event, getProperty("chair_wood_ironage_shield_" + wood));
    		
    		
    		if (IronAgeFurnitureConfiguration.CLIENT.GENERATE_SHORT_STOOLS.get())
    			registerItem(event, getProperty("chair_wood_ironage_stool_short_" + wood));
    		
    		
    		if (IronAgeFurnitureConfiguration.CLIENT.GENERATE_TALL_STOOLS.get())
    			registerItem(event, getProperty("chair_wood_ironage_stool_tall_" + wood));
    		
    		if (IronAgeFurnitureConfiguration.CLIENT.GENERATE_BENCHES.get()) {
    			registerItem(event, getProperty("chair_wood_ironage_bench_single_" + wood));
    			registerItem(event, getProperty("chair_wood_ironage_bench_back_single_" + wood));
    			
    			for (String colour : colours) {
    				registerItem(event, getProperty("chair_wood_ironage_bench_padded_" + colour + "_single_" + wood));
    				registerItem(event, getProperty("chair_wood_ironage_bench_back_padded_" + colour + "_single_" + wood));
				}

    			if (log)
    				registerItem(event, getProperty("chair_wood_ironage_bench_log_single_" + wood));
    		}
    	}    	
    }
    
    private static void registerItem(RegistryEvent.Register<Item> event, Block theBlock) {
    	event.getRegistry().register(new BlockItem(theBlock, new BlockItem.Properties().tab(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, theBlock.getRegistryName().getPath()));
    }
    
    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
    	String[] vanillaWood = {"oak", "acacia", "dark_oak", "birch", "jungle", "spruce"};
    	
    	registerChairs(event, vanillaWood, true);
    	
    	event.getRegistry().registerAll(
    			new BlockItem(BlockObjectHolder.light_metal_ironage_sconce_floor_empty_iron, new BlockItem.Properties().tab(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.light_metal_ironage_sconce_floor_empty_iron.getRegistryName().getPath()),
    			new BlockItem(BlockObjectHolder.light_metal_ironage_sconce_floor_torch_iron, new BlockItem.Properties()).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.light_metal_ironage_sconce_floor_torch_iron.getRegistryName().getPath()),
    			new BlockItem(BlockObjectHolder.light_metal_ironage_sconce_floor_torch_iron_unlit, new BlockItem.Properties()).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.light_metal_ironage_sconce_floor_torch_iron_unlit.getRegistryName().getPath()),
    			
    			new BlockItem(BlockObjectHolder.light_metal_ironage_sconce_wall_empty_iron, new BlockItem.Properties()).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.light_metal_ironage_sconce_wall_empty_iron.getRegistryName().getPath()),
    			
    			new BlockItem(BlockObjectHolder.light_metal_ironage_sconce_wall_torch_iron, new BlockItem.Properties()).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.light_metal_ironage_sconce_wall_torch_iron.getRegistryName().getPath()),
    			new BlockItem(BlockObjectHolder.light_metal_ironage_sconce_wall_torch_iron_unlit, new BlockItem.Properties()).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.light_metal_ironage_sconce_wall_torch_iron_unlit.getRegistryName().getPath()),
    			
    			new BlockItem(BlockObjectHolder.light_metal_ironage_sconce_wall_redtorch_iron, new BlockItem.Properties()).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.light_metal_ironage_sconce_wall_redtorch_iron.getRegistryName().getPath()),
    			new BlockItem(BlockObjectHolder.light_metal_ironage_sconce_wall_redtorch_iron_unlit, new BlockItem.Properties()).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.light_metal_ironage_sconce_wall_redtorch_iron_unlit.getRegistryName().getPath()),
    			
    			new BlockItem(BlockObjectHolder.light_metal_ironage_block_floor_glow_clear, new BlockItem.Properties().tab(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.light_metal_ironage_block_floor_glow_clear.getRegistryName().getPath()),
    			new BlockItem(BlockObjectHolder.light_metal_ironage_sconce_floor_glow_iron, new BlockItem.Properties()).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.light_metal_ironage_sconce_floor_glow_iron.getRegistryName().getPath()),
    			new BlockItem(BlockObjectHolder.light_metal_ironage_sconce_wall_glow_iron, new BlockItem.Properties()).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.light_metal_ironage_sconce_wall_glow_iron.getRegistryName().getPath()),
    			
    			new BlockItem(BlockObjectHolder.light_metal_ironage_block_floor_lava_clear, new BlockItem.Properties().tab(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.light_metal_ironage_block_floor_lava_clear.getRegistryName().getPath()),
    			new BlockItem(BlockObjectHolder.light_metal_ironage_sconce_floor_lava_iron, new BlockItem.Properties()).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.light_metal_ironage_sconce_floor_lava_iron.getRegistryName().getPath()),
    			new BlockItem(BlockObjectHolder.light_metal_ironage_sconce_wall_lava_iron, new BlockItem.Properties()).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.light_metal_ironage_sconce_wall_lava_iron.getRegistryName().getPath()),
    			
    			new BlockItem(BlockObjectHolder.light_metal_ironage_block_floor_red_clear, new BlockItem.Properties().tab(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.light_metal_ironage_block_floor_red_clear.getRegistryName().getPath()),
    			new BlockItem(BlockObjectHolder.light_metal_ironage_block_floor_red_clear_one, new BlockItem.Properties()).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.light_metal_ironage_block_floor_red_clear_one.getRegistryName().getPath()),
    			new BlockItem(BlockObjectHolder.light_metal_ironage_block_floor_red_clear_two, new BlockItem.Properties()).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.light_metal_ironage_block_floor_red_clear_two.getRegistryName().getPath()),
    			new BlockItem(BlockObjectHolder.light_metal_ironage_block_floor_red_clear_three, new BlockItem.Properties()).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.light_metal_ironage_block_floor_red_clear_three.getRegistryName().getPath()),
    			new BlockItem(BlockObjectHolder.light_metal_ironage_block_floor_red_clear_four, new BlockItem.Properties()).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.light_metal_ironage_block_floor_red_clear_four.getRegistryName().getPath()),
    			new BlockItem(BlockObjectHolder.light_metal_ironage_block_floor_red_clear_five, new BlockItem.Properties()).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.light_metal_ironage_block_floor_red_clear_five.getRegistryName().getPath()),
    			new BlockItem(BlockObjectHolder.light_metal_ironage_block_floor_red_clear_six, new BlockItem.Properties()).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.light_metal_ironage_block_floor_red_clear_six.getRegistryName().getPath()),
    			new BlockItem(BlockObjectHolder.light_metal_ironage_block_floor_red_clear_seven, new BlockItem.Properties()).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.light_metal_ironage_block_floor_red_clear_seven.getRegistryName().getPath()),
    			new BlockItem(BlockObjectHolder.light_metal_ironage_block_floor_red_clear_eight, new BlockItem.Properties()).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.light_metal_ironage_block_floor_red_clear_eight.getRegistryName().getPath()),
    			new BlockItem(BlockObjectHolder.light_metal_ironage_block_floor_red_clear_nine, new BlockItem.Properties()).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.light_metal_ironage_block_floor_red_clear_nine.getRegistryName().getPath()),
    			new BlockItem(BlockObjectHolder.light_metal_ironage_block_floor_red_clear_ten, new BlockItem.Properties()).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.light_metal_ironage_block_floor_red_clear_ten.getRegistryName().getPath()),
    			new BlockItem(BlockObjectHolder.light_metal_ironage_block_floor_red_clear_eleven, new BlockItem.Properties()).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.light_metal_ironage_block_floor_red_clear_eleven.getRegistryName().getPath()),
    			new BlockItem(BlockObjectHolder.light_metal_ironage_block_floor_red_clear_twelve, new BlockItem.Properties()).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.light_metal_ironage_block_floor_red_clear_twelve.getRegistryName().getPath()),
    			new BlockItem(BlockObjectHolder.light_metal_ironage_block_floor_red_clear_thirteen, new BlockItem.Properties()).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.light_metal_ironage_block_floor_red_clear_thirteen.getRegistryName().getPath()),
    			new BlockItem(BlockObjectHolder.light_metal_ironage_block_floor_red_clear_fourteen, new BlockItem.Properties()).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.light_metal_ironage_block_floor_red_clear_fourteen.getRegistryName().getPath()),
    			new BlockItem(BlockObjectHolder.light_metal_ironage_block_floor_red_clear_fifteen, new BlockItem.Properties()).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.light_metal_ironage_block_floor_red_clear_fifteen.getRegistryName().getPath()),
//    			new BlockItem(BlockObjectHolder.light_metal_ironage_sconce_floor_glow_iron, new BlockItem.Properties()).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.light_metal_ironage_sconce_floor_glow_iron.getRegistryName().getPath()),
//    			new BlockItem(BlockObjectHolder.light_metal_ironage_sconce_wall_glow_iron, new BlockItem.Properties()).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.light_metal_ironage_sconce_wall_glow_iron.getRegistryName().getPath()),
//    			
    			
    			new BlockItem(BlockObjectHolder.obsidian_chunk, new BlockItem.Properties().tab(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.obsidian_chunk.getRegistryName().getPath())

    	);
    	
    	if (IronAgeFurnitureConfiguration.CLIENT.INTEGRATION_BIOMESOPLENTY.get() && ModList.get().isLoaded("biomesoplenty")) {
    		String[] bopWood = {"biomesoplenty_cherry", "biomesoplenty_fir", "biomesoplenty_hellbark", 
					"biomesoplenty_jacaranda", "biomesoplenty_mahogany", "biomesoplenty_palm", 
					"biomesoplenty_redwood", "biomesoplenty_umbran", "biomesoplenty_willow",
					"biomesoplenty_dead"}; // TODO: move this array to a shared file
    		
    		registerChairs(event, bopWood, true);
    	}
    	
    	if (IronAgeFurnitureConfiguration.CLIENT.INTEGRATION_BIOMESYOUGO.get() && ModList.get().isLoaded("byg")) {
    		String[] bygWood = {"byg_aspen", "byg_baobab", "byg_blue_enchanted", "byg_bulbis", "byg_cherry", "byg_cika", 
					"byg_cypress", "byg_ebony", "byg_embur", "byg_ether", "byg_fir", "byg_glacial_oak", "byg_green_enchanted",
					"byg_holly", "byg_ironwood", "byg_jacaranda", "byg_lament", "byg_mahogany", "byg_mangrove", "byg_maple", "byg_nightshade",
					"byg_palm", "byg_pine", "byg_rainbow_eucalyptus", "byg_redwood", "byg_skyris", "byg_willow", "byg_witch_hazel", "byg_zelkova"};
			
    		registerChairs(event, bygWood, true);
    	}
    	
    	if (IronAgeFurnitureConfiguration.CLIENT.INTEGRATION_IMMERSIVEENGINEERING.get() && ModList.get().isLoaded("immersiveengineering")) {
    		String[] ieWood = {"immersiveengineering_treated_wood"};
    		registerChairs(event, ieWood, false);	
		}
    }
}
