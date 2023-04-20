package com.mcmoddev.ironagefurniture.init;

import com.mcmoddev.ironagefurniture.IronAgeFurnitureConfiguration;
import com.mcmoddev.ironagefurniture.Ironagefurniture;
import com.mcmoddev.ironagefurniture.api.Blocks.BackBench;
import com.mcmoddev.ironagefurniture.api.Blocks.Bench;
import com.mcmoddev.ironagefurniture.api.Blocks.Chair;
import com.mcmoddev.ironagefurniture.api.Blocks.LightHolderSconceFloor;
import com.mcmoddev.ironagefurniture.api.Blocks.LightHolderSconceWall;
import com.mcmoddev.ironagefurniture.api.Blocks.LightSourceGlowdust;
import com.mcmoddev.ironagefurniture.api.Blocks.LightSourceLava;
import com.mcmoddev.ironagefurniture.api.Blocks.LightSourceRed;
import com.mcmoddev.ironagefurniture.api.Blocks.LightSourceRedEight;
import com.mcmoddev.ironagefurniture.api.Blocks.LightSourceRedEleven;
import com.mcmoddev.ironagefurniture.api.Blocks.LightSourceRedFifteen;
import com.mcmoddev.ironagefurniture.api.Blocks.LightSourceRedFive;
import com.mcmoddev.ironagefurniture.api.Blocks.LightSourceRedFour;
import com.mcmoddev.ironagefurniture.api.Blocks.LightSourceRedFourteen;
import com.mcmoddev.ironagefurniture.api.Blocks.LightSourceRedNine;
import com.mcmoddev.ironagefurniture.api.Blocks.LightSourceRedOne;
import com.mcmoddev.ironagefurniture.api.Blocks.LightSourceRedSeven;
import com.mcmoddev.ironagefurniture.api.Blocks.LightSourceRedSix;
import com.mcmoddev.ironagefurniture.api.Blocks.LightSourceRedTen;
import com.mcmoddev.ironagefurniture.api.Blocks.LightSourceRedThirteen;
import com.mcmoddev.ironagefurniture.api.Blocks.LightSourceRedThree;
import com.mcmoddev.ironagefurniture.api.Blocks.LightSourceRedTwelve;
import com.mcmoddev.ironagefurniture.api.Blocks.LightSourceRedTwo;
import com.mcmoddev.ironagefurniture.api.Blocks.LightSourceSconceGlowFloor;
import com.mcmoddev.ironagefurniture.api.Blocks.LightSourceSconceGlowWall;
import com.mcmoddev.ironagefurniture.api.Blocks.LightSourceSconceLavaFloor;
import com.mcmoddev.ironagefurniture.api.Blocks.LightSourceSconceLavaWall;
import com.mcmoddev.ironagefurniture.api.Blocks.LightSourceSconceRedTorchFloor;
import com.mcmoddev.ironagefurniture.api.Blocks.LightSourceSconceRedTorchFloorUnlit;
import com.mcmoddev.ironagefurniture.api.Blocks.LightSourceSconceRedTorchWall;
import com.mcmoddev.ironagefurniture.api.Blocks.LightSourceSconceRedTorchWallUnlit;
import com.mcmoddev.ironagefurniture.api.Blocks.LightSourceSconceTorchFloor;
import com.mcmoddev.ironagefurniture.api.Blocks.LightSourceSconceTorchFloorUnlit;
import com.mcmoddev.ironagefurniture.api.Blocks.LightSourceSconceTorchWall;
import com.mcmoddev.ironagefurniture.api.Blocks.LightSourceSconceTorchWallUnlit;
import com.mcmoddev.ironagefurniture.api.Blocks.LogBench;
import com.mcmoddev.ironagefurniture.api.Blocks.ObsideanLump;
import com.mcmoddev.ironagefurniture.api.Blocks.Stool;
import com.mcmoddev.ironagefurniture.api.Blocks.TallStool;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
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
	
	public static void registerChairs(RegistryEvent.Register<Block> event, String[] woods, boolean shield, boolean shortStool, boolean tallStool, boolean bench) { 
		registerChairs(event, woods, shield, shortStool, tallStool, bench, true);
	}
	
	public static void registerChairs(RegistryEvent.Register<Block> event, String[] woods, boolean shield, boolean shortStool, boolean tallStool, boolean bench, boolean log) { 
		String[] colours = {"green", "light_blue", "light_gray", "lime", "magenta", "orange", "pink", "purple", "red", "white", "yellow", "black", "blue", "brown", "cyan", "gray"};
		
		for (String wood : woods) {
			event.getRegistry().register(new Chair(1,10, SoundType.WOOD, "chair_wood_ironage_classic_" + wood));
			
			if (shield)
				event.getRegistry().register(new Chair(1,10, SoundType.WOOD, "chair_wood_ironage_shield_" + wood));

			if (shortStool)
				event.getRegistry().register(new Stool(1,10, SoundType.WOOD, "chair_wood_ironage_stool_short_" + wood));
			
			if (tallStool)
				event.getRegistry().register(new TallStool(1,10, SoundType.WOOD, "chair_wood_ironage_stool_tall_" + wood));
			
			if (bench) {
				event.getRegistry().register(new Bench(1,10, SoundType.WOOD, "chair_wood_ironage_bench_single_" + wood));
				event.getRegistry().register(new BackBench(1,10, SoundType.WOOD, "chair_wood_ironage_bench_back_single_" + wood));
				
				for (String colour : colours) {
					event.getRegistry().register(new Bench(1,10, SoundType.WOOD, "chair_wood_ironage_bench_padded_" + colour +"_single_" + wood));
					event.getRegistry().register(new BackBench(1,10, SoundType.WOOD, "chair_wood_ironage_bench_back_padded_" + colour + "_single_" + wood));
				}

				if (log)
					event.getRegistry().register(new LogBench(1,10, SoundType.WOOD, "chair_wood_ironage_bench_log_single_" + wood));
			}
		}
	}
	
	 @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
		event.getRegistry().register(new LightHolderSconceFloor(1,10, SoundType.METAL, "light_metal_ironage_sconce_floor_empty_iron"));
		
		event.getRegistry().register(new LightSourceSconceTorchFloor(1,10, SoundType.METAL, "light_metal_ironage_sconce_floor_torch_iron" ));
		event.getRegistry().register(new LightSourceSconceTorchFloorUnlit(1,10, SoundType.METAL, "light_metal_ironage_sconce_floor_torch_iron_unlit"));

		event.getRegistry().register(new LightSourceSconceRedTorchFloor(1,10, SoundType.METAL, "light_metal_ironage_sconce_floor_redtorch_iron" ));
		event.getRegistry().register(new LightSourceSconceRedTorchFloorUnlit(1,10, SoundType.METAL, "light_metal_ironage_sconce_floor_redtorch_iron_unlit"));
		
		event.getRegistry().register(new LightHolderSconceWall(1,10, SoundType.METAL, "light_metal_ironage_sconce_wall_empty_iron"));
		
		event.getRegistry().register(new LightSourceSconceTorchWall(1,10, SoundType.METAL, "light_metal_ironage_sconce_wall_torch_iron"));
		event.getRegistry().register(new LightSourceSconceTorchWallUnlit(1,10, SoundType.METAL, "light_metal_ironage_sconce_wall_torch_iron_unlit"));
		
		event.getRegistry().register(new LightSourceSconceRedTorchWall(1,10, SoundType.METAL, "light_metal_ironage_sconce_wall_redtorch_iron"));
		event.getRegistry().register(new LightSourceSconceRedTorchWallUnlit(1,10, SoundType.METAL, "light_metal_ironage_sconce_wall_redtorch_iron_unlit"));
		
		event.getRegistry().register(new LightSourceGlowdust(1,10, SoundType.GLASS, "light_metal_ironage_block_floor_glow_clear"));
		event.getRegistry().register(new LightSourceSconceGlowFloor(1,10, SoundType.METAL, "light_metal_ironage_sconce_floor_glow_iron" ));
		event.getRegistry().register(new LightSourceSconceGlowWall(1,10, SoundType.METAL, "light_metal_ironage_sconce_wall_glow_iron" ));
		
		event.getRegistry().register(new LightSourceLava(1,10, SoundType.GLASS, "light_metal_ironage_block_floor_lava_clear"));
		event.getRegistry().register(new LightSourceSconceLavaFloor(1,10, SoundType.METAL, "light_metal_ironage_sconce_floor_lava_iron" ));
		event.getRegistry().register(new LightSourceSconceLavaWall(1,10, SoundType.METAL, "light_metal_ironage_sconce_wall_lava_iron" ));
		
		event.getRegistry().register(new LightSourceRed(1,10, SoundType.GLASS, "light_metal_ironage_block_floor_red_clear"));
		event.getRegistry().register(new LightSourceRedOne(1,10, SoundType.GLASS, "light_metal_ironage_block_floor_red_clear_one"));
		event.getRegistry().register(new LightSourceRedTwo(1,10, SoundType.GLASS, "light_metal_ironage_block_floor_red_clear_two"));
		event.getRegistry().register(new LightSourceRedThree(1,10, SoundType.GLASS, "light_metal_ironage_block_floor_red_clear_three"));
		event.getRegistry().register(new LightSourceRedFour(1,10, SoundType.GLASS, "light_metal_ironage_block_floor_red_clear_four"));
		event.getRegistry().register(new LightSourceRedFive(1,10, SoundType.GLASS, "light_metal_ironage_block_floor_red_clear_five"));
		event.getRegistry().register(new LightSourceRedSix(1,10, SoundType.GLASS, "light_metal_ironage_block_floor_red_clear_six"));
		event.getRegistry().register(new LightSourceRedSeven(1,10, SoundType.GLASS, "light_metal_ironage_block_floor_red_clear_seven"));
		event.getRegistry().register(new LightSourceRedEight(1,10, SoundType.GLASS, "light_metal_ironage_block_floor_red_clear_eight"));
		event.getRegistry().register(new LightSourceRedNine(1,10, SoundType.GLASS, "light_metal_ironage_block_floor_red_clear_nine"));
		event.getRegistry().register(new LightSourceRedTen(1,10, SoundType.GLASS, "light_metal_ironage_block_floor_red_clear_ten"));
		event.getRegistry().register(new LightSourceRedEleven(1,10, SoundType.GLASS, "light_metal_ironage_block_floor_red_clear_eleven"));
		event.getRegistry().register(new LightSourceRedTwelve(1,10, SoundType.GLASS, "light_metal_ironage_block_floor_red_clear_twelve"));
		event.getRegistry().register(new LightSourceRedThirteen(1,10, SoundType.GLASS, "light_metal_ironage_block_floor_red_clear_thirteen"));
		event.getRegistry().register(new LightSourceRedFourteen(1,10, SoundType.GLASS, "light_metal_ironage_block_floor_red_clear_fourteen"));
		event.getRegistry().register(new LightSourceRedFifteen(1,10, SoundType.GLASS, "light_metal_ironage_block_floor_red_clear_fifteen"));
		//event.getRegistry().register(new LightSourceSconceGlowFloor(1,10, SoundType.METAL, "light_metal_ironage_sconce_floor_glow_iron" ));
		//event.getRegistry().register(new LightSourceSconceGlowWall(1,10, SoundType.METAL, "light_metal_ironage_sconce_wall_glow_iron" ));
		
		event.getRegistry().register(new ObsideanLump(1,10, SoundType.STONE, "obsidian_chunk"));
		
		String[] vanillaWood = {"oak", "acacia", "dark_oak", "birch", "jungle", "spruce"};

		registerChairs(event, vanillaWood, 
				IronAgeFurnitureConfiguration.CLIENT.GENERATE_SHIELD_CHAIRS.get(), 
				IronAgeFurnitureConfiguration.CLIENT.GENERATE_SHORT_STOOLS.get(), 
				IronAgeFurnitureConfiguration.CLIENT.GENERATE_TALL_STOOLS.get(),
				IronAgeFurnitureConfiguration.CLIENT.GENERATE_BENCHES.get());

		
		if (IronAgeFurnitureConfiguration.CLIENT.INTEGRATION_BIOMESOPLENTY.get() && ModList.get().isLoaded("biomesoplenty")) {
			String[] bopWood = {"biomesoplenty_cherry", "biomesoplenty_fir", "biomesoplenty_hellbark", 
								"biomesoplenty_jacaranda", "biomesoplenty_mahogany", "biomesoplenty_palm", 
								"biomesoplenty_redwood", "biomesoplenty_umbran", "biomesoplenty_willow",
								"biomesoplenty_dead"};
			
			registerChairs(event, bopWood, 
					IronAgeFurnitureConfiguration.CLIENT.GENERATE_SHIELD_CHAIRS.get(), 
					IronAgeFurnitureConfiguration.CLIENT.GENERATE_SHORT_STOOLS.get(), 
					IronAgeFurnitureConfiguration.CLIENT.GENERATE_TALL_STOOLS.get(),
					IronAgeFurnitureConfiguration.CLIENT.GENERATE_BENCHES.get());
		}
		
		if (IronAgeFurnitureConfiguration.CLIENT.INTEGRATION_BIOMESYOUGO.get() && ModList.get().isLoaded("byg")) {
			String[] bygWood = {"byg_aspen", "byg_baobab", "byg_blue_enchanted", "byg_bulbis", "byg_cherry", "byg_cika", 
					"byg_cypress", "byg_ebony", "byg_embur", "byg_ether", "byg_fir", "byg_glacial_oak", "byg_green_enchanted",
					"byg_holly", "byg_ironwood", "byg_jacaranda", "byg_lament", "byg_mahogany", "byg_mangrove", "byg_maple", "byg_nightshade",
					"byg_palm", "byg_pine", "byg_rainbow_eucalyptus", "byg_redwood", "byg_skyris", "byg_willow", "byg_witch_hazel", "byg_zelkova"};
			
			
			registerChairs(event, bygWood, 
					IronAgeFurnitureConfiguration.CLIENT.GENERATE_SHIELD_CHAIRS.get(), 
					IronAgeFurnitureConfiguration.CLIENT.GENERATE_SHORT_STOOLS.get(), 
					IronAgeFurnitureConfiguration.CLIENT.GENERATE_TALL_STOOLS.get(),
					IronAgeFurnitureConfiguration.CLIENT.GENERATE_BENCHES.get());
			
		}
		
		if (IronAgeFurnitureConfiguration.CLIENT.INTEGRATION_IMMERSIVEENGINEERING.get() && ModList.get().isLoaded("immersiveengineering")) {
			String[] ieWood = {"immersiveengineering_treated_wood"};
			
			registerChairs(event, ieWood, 
					IronAgeFurnitureConfiguration.CLIENT.GENERATE_SHIELD_CHAIRS.get(), 
					IronAgeFurnitureConfiguration.CLIENT.GENERATE_SHORT_STOOLS.get(), 
					IronAgeFurnitureConfiguration.CLIENT.GENERATE_TALL_STOOLS.get(),
					IronAgeFurnitureConfiguration.CLIENT.GENERATE_BENCHES.get(), false);
		}
	 }
}