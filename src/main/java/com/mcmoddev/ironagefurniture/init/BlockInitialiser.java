package com.mcmoddev.ironagefurniture.init;

import com.mcmoddev.ironagefurniture.IronAgeFurnitureConfiguration;
import com.mcmoddev.ironagefurniture.Ironagefurniture;
import com.mcmoddev.ironagefurniture.api.Blocks.Furniture.*;
import com.mcmoddev.ironagefurniture.api.Blocks.LightHolder.LightHolderSconceFloor;
import com.mcmoddev.ironagefurniture.api.Blocks.LightHolder.LightHolderSconceWall;
import com.mcmoddev.ironagefurniture.api.Blocks.LightSource.Glow.LightSourceGlowdust;
import com.mcmoddev.ironagefurniture.api.Blocks.LightSource.Glow.LightSourceSconceGlowFloor;
import com.mcmoddev.ironagefurniture.api.Blocks.LightSource.Glow.LightSourceSconceGlowWall;
import com.mcmoddev.ironagefurniture.api.Blocks.LightSource.Lava.LightSourceLava;
import com.mcmoddev.ironagefurniture.api.Blocks.LightSource.Lava.LightSourceSconceLavaFloor;
import com.mcmoddev.ironagefurniture.api.Blocks.LightSource.Lava.LightSourceSconceLavaWall;
import com.mcmoddev.ironagefurniture.api.Blocks.LightSource.Lava.ObsideanLump;
import com.mcmoddev.ironagefurniture.api.Blocks.LightSource.Red.Illuminated.Red.*;
import com.mcmoddev.ironagefurniture.api.Blocks.LightSource.Red.Illuminated.Sconce.Floor.*;
import com.mcmoddev.ironagefurniture.api.Blocks.LightSource.Red.Illuminated.Sconce.Wall.*;
import com.mcmoddev.ironagefurniture.api.Blocks.LightSource.Red.LightSourceRed;
import com.mcmoddev.ironagefurniture.api.Blocks.LightSource.Red.LightSourceSconceRedFloor;
import com.mcmoddev.ironagefurniture.api.Blocks.LightSource.Red.LightSourceSconceRedWall;
import com.mcmoddev.ironagefurniture.api.Blocks.LightSource.RedTorch.LightSourceSconceRedTorchFloor;
import com.mcmoddev.ironagefurniture.api.Blocks.LightSource.RedTorch.LightSourceSconceRedTorchFloorUnlit;
import com.mcmoddev.ironagefurniture.api.Blocks.LightSource.RedTorch.LightSourceSconceRedTorchWall;
import com.mcmoddev.ironagefurniture.api.Blocks.LightSource.RedTorch.LightSourceSconceRedTorchWallUnlit;
import com.mcmoddev.ironagefurniture.api.Blocks.LightSource.Torch.LightSourceSconceTorchFloor;
import com.mcmoddev.ironagefurniture.api.Blocks.LightSource.Torch.LightSourceSconceTorchFloorUnlit;
import com.mcmoddev.ironagefurniture.api.Blocks.LightSource.Torch.LightSourceSconceTorchWall;
import com.mcmoddev.ironagefurniture.api.Blocks.LightSource.Torch.LightSourceSconceTorchWallUnlit;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;

import static com.mcmoddev.ironagefurniture.init.resources.bop.BOP_WOOD_TYPES;
import static com.mcmoddev.ironagefurniture.init.resources.byg.BYG_WOOD_TYPES;
import static com.mcmoddev.ironagefurniture.init.resources.colours.COLOURS;
import static com.mcmoddev.ironagefurniture.init.resources.immersiveengineering.IE_WOOD_TYPES;
import static com.mcmoddev.ironagefurniture.init.resources.vanilla.VANILLA_WOOD_TYPES;


/**
 * This class initialises all blocks in ironagefurniture.
 *
 * @author SkyBlade1978
 */
@Mod.EventBusSubscriber(modid = Ironagefurniture.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BlockInitialiser {
	public static void registerChairs(RegistryEvent.Register<Block> event, String[] woods, boolean shield, boolean shortStool, boolean tallStool, boolean bench) {
		registerChairs(event, woods, shield, shortStool, tallStool, bench, true);
	}

	public static void registerChairs(RegistryEvent.Register<Block> event, String[] woods, boolean shield, boolean shortStool, boolean tallStool, boolean bench, boolean log) {

		for (String wood : woods) {
			event.getRegistry().register(new Chair(1, 10, SoundType.WOOD, "chair_wood_ironage_classic_" + wood));

			if (shield)
				event.getRegistry().register(new Chair(1, 10, SoundType.WOOD, "chair_wood_ironage_shield_" + wood));

			if (shortStool)
				event.getRegistry().register(new Stool(1, 10, SoundType.WOOD, "chair_wood_ironage_stool_short_" + wood));

			if (tallStool)
				event.getRegistry().register(new TallStool(1, 10, SoundType.WOOD, "chair_wood_ironage_stool_tall_" + wood));

			if (bench) {
				event.getRegistry().register(new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_single_" + wood));
				event.getRegistry().register(new BackBench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_back_single_" + wood));

				for (String colour : COLOURS) {
					event.getRegistry().register(new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_" + colour + "_single_" + wood));
					event.getRegistry().register(new BackBench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_back_padded_" + colour + "_single_" + wood));
				}

				if (log)
					event.getRegistry().register(new LogBench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_log_single_" + wood));
			}
		}
	}

	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event) {
		event.getRegistry().register(new LightHolderSconceFloor(1, 10, SoundType.METAL, "light_metal_ironage_sconce_floor_empty_iron"));

		event.getRegistry().register(new LightSourceSconceTorchFloor(1, 10, SoundType.METAL, "light_metal_ironage_sconce_floor_torch_iron"));
		event.getRegistry().register(new LightSourceSconceTorchFloorUnlit(1, 10, SoundType.METAL, "light_metal_ironage_sconce_floor_torch_iron_unlit"));

		event.getRegistry().register(new LightSourceSconceRedTorchFloor(1, 10, SoundType.METAL, "light_metal_ironage_sconce_floor_redtorch_iron"));
		event.getRegistry().register(new LightSourceSconceRedTorchFloorUnlit(1, 10, SoundType.METAL, "light_metal_ironage_sconce_floor_redtorch_iron_unlit"));

		event.getRegistry().register(new LightHolderSconceWall(1, 10, SoundType.METAL, "light_metal_ironage_sconce_wall_empty_iron"));

		event.getRegistry().register(new LightSourceSconceTorchWall(1, 10, SoundType.METAL, "light_metal_ironage_sconce_wall_torch_iron"));
		event.getRegistry().register(new LightSourceSconceTorchWallUnlit(1, 10, SoundType.METAL, "light_metal_ironage_sconce_wall_torch_iron_unlit"));

		event.getRegistry().register(new LightSourceSconceRedTorchWall(1, 10, SoundType.METAL, "light_metal_ironage_sconce_wall_redtorch_iron"));
		event.getRegistry().register(new LightSourceSconceRedTorchWallUnlit(1, 10, SoundType.METAL, "light_metal_ironage_sconce_wall_redtorch_iron_unlit"));

		event.getRegistry().register(new LightSourceGlowdust(1, 10, SoundType.GLASS, "light_metal_ironage_block_floor_glow_clear"));
		event.getRegistry().register(new LightSourceSconceGlowFloor(1, 10, SoundType.METAL, "light_metal_ironage_sconce_floor_glow_iron"));
		event.getRegistry().register(new LightSourceSconceGlowWall(1, 10, SoundType.METAL, "light_metal_ironage_sconce_wall_glow_iron"));

		event.getRegistry().register(new LightSourceLava(1, 10, SoundType.GLASS, "light_metal_ironage_block_floor_lava_clear"));
		event.getRegistry().register(new LightSourceSconceLavaFloor(1, 10, SoundType.METAL, "light_metal_ironage_sconce_floor_lava_iron"));
		event.getRegistry().register(new LightSourceSconceLavaWall(1, 10, SoundType.METAL, "light_metal_ironage_sconce_wall_lava_iron"));

		event.getRegistry().register(new LightSourceRed(1, 10, SoundType.GLASS, "light_metal_ironage_block_floor_red_clear"));
		event.getRegistry().register(new LightSourceRedOne(1, 10, SoundType.GLASS, "light_metal_ironage_block_floor_red_clear_one"));
		event.getRegistry().register(new LightSourceRedTwo(1, 10, SoundType.GLASS, "light_metal_ironage_block_floor_red_clear_two"));
		event.getRegistry().register(new LightSourceRedThree(1, 10, SoundType.GLASS, "light_metal_ironage_block_floor_red_clear_three"));
		event.getRegistry().register(new LightSourceRedFour(1, 10, SoundType.GLASS, "light_metal_ironage_block_floor_red_clear_four"));
		event.getRegistry().register(new LightSourceRedFive(1, 10, SoundType.GLASS, "light_metal_ironage_block_floor_red_clear_five"));
		event.getRegistry().register(new LightSourceRedSix(1, 10, SoundType.GLASS, "light_metal_ironage_block_floor_red_clear_six"));
		event.getRegistry().register(new LightSourceRedSeven(1, 10, SoundType.GLASS, "light_metal_ironage_block_floor_red_clear_seven"));
		event.getRegistry().register(new LightSourceRedEight(1, 10, SoundType.GLASS, "light_metal_ironage_block_floor_red_clear_eight"));
		event.getRegistry().register(new LightSourceRedNine(1, 10, SoundType.GLASS, "light_metal_ironage_block_floor_red_clear_nine"));
		event.getRegistry().register(new LightSourceRedTen(1, 10, SoundType.GLASS, "light_metal_ironage_block_floor_red_clear_ten"));
		event.getRegistry().register(new LightSourceRedEleven(1, 10, SoundType.GLASS, "light_metal_ironage_block_floor_red_clear_eleven"));
		event.getRegistry().register(new LightSourceRedTwelve(1, 10, SoundType.GLASS, "light_metal_ironage_block_floor_red_clear_twelve"));
		event.getRegistry().register(new LightSourceRedThirteen(1, 10, SoundType.GLASS, "light_metal_ironage_block_floor_red_clear_thirteen"));
		event.getRegistry().register(new LightSourceRedFourteen(1, 10, SoundType.GLASS, "light_metal_ironage_block_floor_red_clear_fourteen"));
		event.getRegistry().register(new LightSourceRedFifteen(1, 10, SoundType.GLASS, "light_metal_ironage_block_floor_red_clear_fifteen"));

		event.getRegistry().register(new LightSourceSconceRedFloor(1, 10, SoundType.METAL, "light_metal_ironage_sconce_floor_red_iron"));
		event.getRegistry().register(new LightSourceSconceRedFloorOne(1, 10, SoundType.METAL, "light_metal_ironage_sconce_floor_red_iron_one"));
		event.getRegistry().register(new LightSourceSconceRedFloorTwo(1, 10, SoundType.METAL, "light_metal_ironage_sconce_floor_red_iron_two"));
		event.getRegistry().register(new LightSourceSconceRedFloorThree(1, 10, SoundType.METAL, "light_metal_ironage_sconce_floor_red_iron_three"));
		event.getRegistry().register(new LightSourceSconceRedFloorFour(1, 10, SoundType.METAL, "light_metal_ironage_sconce_floor_red_iron_four"));
		event.getRegistry().register(new LightSourceSconceRedFloorFive(1, 10, SoundType.METAL, "light_metal_ironage_sconce_floor_red_iron_five"));
		event.getRegistry().register(new LightSourceSconceRedFloorSix(1, 10, SoundType.METAL, "light_metal_ironage_sconce_floor_red_iron_six"));
		event.getRegistry().register(new LightSourceSconceRedFloorSeven(1, 10, SoundType.METAL, "light_metal_ironage_sconce_floor_red_iron_seven"));
		event.getRegistry().register(new LightSourceSconceRedFloorEight(1, 10, SoundType.METAL, "light_metal_ironage_sconce_floor_red_iron_eight"));
		event.getRegistry().register(new LightSourceSconceRedFloorNine(1, 10, SoundType.METAL, "light_metal_ironage_sconce_floor_red_iron_nine"));
		event.getRegistry().register(new LightSourceSconceRedFloorTen(1, 10, SoundType.METAL, "light_metal_ironage_sconce_floor_red_iron_ten"));
		event.getRegistry().register(new LightSourceSconceRedFloorEleven(1, 10, SoundType.METAL, "light_metal_ironage_sconce_floor_red_iron_eleven"));
		event.getRegistry().register(new LightSourceSconceRedFloorTwelve(1, 10, SoundType.METAL, "light_metal_ironage_sconce_floor_red_iron_twelve"));
		event.getRegistry().register(new LightSourceSconceRedFloorThirteen(1, 10, SoundType.METAL, "light_metal_ironage_sconce_floor_red_iron_thirteen"));
		event.getRegistry().register(new LightSourceSconceRedFloorFourteen(1, 10, SoundType.METAL, "light_metal_ironage_sconce_floor_red_iron_fourteen"));
		event.getRegistry().register(new LightSourceSconceRedFloorFifteen(1, 10, SoundType.METAL, "light_metal_ironage_sconce_floor_red_iron_fifteen"));

		event.getRegistry().register(new LightSourceSconceRedWall(1, 10, SoundType.METAL, "light_metal_ironage_sconce_wall_red_iron"));
		event.getRegistry().register(new LightSourceSconceRedWallOne(1, 10, SoundType.METAL, "light_metal_ironage_sconce_wall_red_iron_one"));
		event.getRegistry().register(new LightSourceSconceRedWallTwo(1, 10, SoundType.METAL, "light_metal_ironage_sconce_wall_red_iron_two"));
		event.getRegistry().register(new LightSourceSconceRedWallThree(1, 10, SoundType.METAL, "light_metal_ironage_sconce_wall_red_iron_three"));
		event.getRegistry().register(new LightSourceSconceRedWallFour(1, 10, SoundType.METAL, "light_metal_ironage_sconce_wall_red_iron_four"));
		event.getRegistry().register(new LightSourceSconceRedWallFive(1, 10, SoundType.METAL, "light_metal_ironage_sconce_wall_red_iron_five"));
		event.getRegistry().register(new LightSourceSconceRedWallSix(1, 10, SoundType.METAL, "light_metal_ironage_sconce_wall_red_iron_six"));
		event.getRegistry().register(new LightSourceSconceRedWallSeven(1, 10, SoundType.METAL, "light_metal_ironage_sconce_wall_red_iron_seven"));
		event.getRegistry().register(new LightSourceSconceRedWallEight(1, 10, SoundType.METAL, "light_metal_ironage_sconce_wall_red_iron_eight"));
		event.getRegistry().register(new LightSourceSconceRedWallNine(1, 10, SoundType.METAL, "light_metal_ironage_sconce_wall_red_iron_nine"));
		event.getRegistry().register(new LightSourceSconceRedWallTen(1, 10, SoundType.METAL, "light_metal_ironage_sconce_wall_red_iron_ten"));
		event.getRegistry().register(new LightSourceSconceRedWallEleven(1, 10, SoundType.METAL, "light_metal_ironage_sconce_wall_red_iron_eleven"));
		event.getRegistry().register(new LightSourceSconceRedWallTwelve(1, 10, SoundType.METAL, "light_metal_ironage_sconce_wall_red_iron_twelve"));
		event.getRegistry().register(new LightSourceSconceRedWallThirteen(1, 10, SoundType.METAL, "light_metal_ironage_sconce_wall_red_iron_thirteen"));
		event.getRegistry().register(new LightSourceSconceRedWallFourteen(1, 10, SoundType.METAL, "light_metal_ironage_sconce_wall_red_iron_fourteen"));
		event.getRegistry().register(new LightSourceSconceRedWallFifteen(1, 10, SoundType.METAL, "light_metal_ironage_sconce_wall_red_iron_fifteen"));

		event.getRegistry().register(new ObsideanLump(1, 10, SoundType.STONE, "obsidian_chunk"));

		registerChairs(event, VANILLA_WOOD_TYPES,
			IronAgeFurnitureConfiguration.CLIENT.GENERATE_SHIELD_CHAIRS.get(),
			IronAgeFurnitureConfiguration.CLIENT.GENERATE_SHORT_STOOLS.get(),
			IronAgeFurnitureConfiguration.CLIENT.GENERATE_TALL_STOOLS.get(),
			IronAgeFurnitureConfiguration.CLIENT.GENERATE_BENCHES.get());


		if (IronAgeFurnitureConfiguration.CLIENT.INTEGRATION_BIOMESOPLENTY.get() && ModList.get().isLoaded("biomesoplenty")) {
			registerChairs(event, BOP_WOOD_TYPES,
				IronAgeFurnitureConfiguration.CLIENT.GENERATE_SHIELD_CHAIRS.get(),
				IronAgeFurnitureConfiguration.CLIENT.GENERATE_SHORT_STOOLS.get(),
				IronAgeFurnitureConfiguration.CLIENT.GENERATE_TALL_STOOLS.get(),
				IronAgeFurnitureConfiguration.CLIENT.GENERATE_BENCHES.get());
		}

		if (IronAgeFurnitureConfiguration.CLIENT.INTEGRATION_BIOMESYOUGO.get() && ModList.get().isLoaded("byg")) {
			registerChairs(event, BYG_WOOD_TYPES,
				IronAgeFurnitureConfiguration.CLIENT.GENERATE_SHIELD_CHAIRS.get(),
				IronAgeFurnitureConfiguration.CLIENT.GENERATE_SHORT_STOOLS.get(),
				IronAgeFurnitureConfiguration.CLIENT.GENERATE_TALL_STOOLS.get(),
				IronAgeFurnitureConfiguration.CLIENT.GENERATE_BENCHES.get());

		}

		if (IronAgeFurnitureConfiguration.CLIENT.INTEGRATION_IMMERSIVEENGINEERING.get() && ModList.get().isLoaded("immersiveengineering")) {
			registerChairs(event, IE_WOOD_TYPES,
				IronAgeFurnitureConfiguration.CLIENT.GENERATE_SHIELD_CHAIRS.get(),
				IronAgeFurnitureConfiguration.CLIENT.GENERATE_SHORT_STOOLS.get(),
				IronAgeFurnitureConfiguration.CLIENT.GENERATE_TALL_STOOLS.get(),
				IronAgeFurnitureConfiguration.CLIENT.GENERATE_BENCHES.get(), false);
		}
	}
}
