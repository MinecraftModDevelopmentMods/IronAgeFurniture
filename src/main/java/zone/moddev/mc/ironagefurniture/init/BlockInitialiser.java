package zone.moddev.mc.ironagefurniture.init;

import zone.moddev.mc.ironagefurniture.IronAgeFurnitureConfiguration;
import zone.moddev.mc.ironagefurniture.Ironagefurniture;
import zone.moddev.mc.ironagefurniture.api.blocks.furniture.*;
import zone.moddev.mc.ironagefurniture.api.blocks.lightholder.LightHolderSconceFloor;
import zone.moddev.mc.ironagefurniture.api.blocks.lightholder.LightHolderSconceWall;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.glow.LightSourceGlowdust;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.glow.LightSourceSconceGlowFloor;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.glow.LightSourceSconceGlowWall;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.lava.LightSourceLava;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.lava.LightSourceSconceLavaFloor;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.lava.LightSourceSconceLavaWall;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.lava.ObsideanLump;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.red.illuminated.red.*;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.red.illuminated.sconce.floor.*;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.red.illuminated.sconce.wall.*;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.red.LightSourceRed;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.red.LightSourceSconceRedFloor;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.red.LightSourceSconceRedWall;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.redtorch.LightSourceSconceRedTorchFloor;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.redtorch.LightSourceSconceRedTorchFloorUnlit;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.redtorch.LightSourceSconceRedTorchWall;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.redtorch.LightSourceSconceRedTorchWallUnlit;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.soultorch.LightSourceSconceSoulTorchFloor;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.soultorch.LightSourceSconceSoulTorchWall;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.torch.LightSourceSconceTorchFloor;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.torch.LightSourceSconceTorchFloorUnlit;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.torch.LightSourceSconceTorchWall;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.torch.LightSourceSconceTorchWallUnlit;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;

import static zone.moddev.mc.ironagefurniture.init.resources.bop.BOP_WOOD_TYPES;
import static zone.moddev.mc.ironagefurniture.init.resources.bop.BOP_NETHER_WOOD_TYPES;
import static zone.moddev.mc.ironagefurniture.init.resources.byg.BYG_WOOD_TYPES;
import static zone.moddev.mc.ironagefurniture.init.resources.byg.BYG_NETHER_WOOD_TYPES;
import static zone.moddev.mc.ironagefurniture.init.resources.colours.COLOURS;
import static zone.moddev.mc.ironagefurniture.init.resources.immersiveengineering.IE_WOOD_TYPES;
import static zone.moddev.mc.ironagefurniture.init.resources.immersiveengineering.IE_NETHER_WOOD_TYPES;
import static zone.moddev.mc.ironagefurniture.init.resources.vanilla.VANILLA_WOOD_TYPES;

import static zone.moddev.mc.ironagefurniture.init.resources.vanilla.VANILLA_NETHER_WOOD_TYPES;


/**
 * This class initialises all blocks in ironagefurniture.
 *
 * @author SkyBlade1978
 */
@Mod.EventBusSubscriber(modid = Ironagefurniture.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BlockInitialiser {
	public static void registerChairs(RegistryEvent.Register<Block> event, String[] woods, boolean shield, boolean shortStool, boolean tallStool, boolean bench, String[] netherwoods ) {
		registerChairs(event, woods, shield, shortStool, tallStool, bench, true, netherwoods);
	}

	public static void registerChairs(RegistryEvent.Register<Block> event, String[] woods, boolean shield, boolean shortStool, boolean tallStool, boolean bench, boolean log, String[] netherwoods) {

		for (String wood : woods) {

            PhaseFourFurniture.registerBlocks(event, wood);
			event.getRegistry().register(new Chair(1, 10, SoundType.WOOD, "chair_wood_ironage_classic_" + wood));

			if (shield)
				event.getRegistry().register(new ShieldChair(1, 10, SoundType.WOOD, "chair_wood_ironage_shield_" + wood));

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

		for (String wood : netherwoods) {

            PhaseFourFurniture.registerBlocks(event, wood);
			event.getRegistry().register(new ChairNether(1, 10, SoundType.WOOD, "chair_wood_ironage_classic_" + wood));

			if (shield)
				event.getRegistry().register(new ShieldChair(1, 10, SoundType.WOOD, "chair_wood_ironage_shield_" + wood));

			if (shortStool)
				event.getRegistry().register(new StoolNether(1, 10, SoundType.WOOD, "chair_wood_ironage_stool_short_" + wood));

			if (tallStool)
				event.getRegistry().register(new TallStoolNether(1, 10, SoundType.WOOD, "chair_wood_ironage_stool_tall_" + wood));

			if (bench) {
				event.getRegistry().register(new BenchNether(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_single_" + wood));
				event.getRegistry().register(new BackBenchNether(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_back_single_" + wood));

				for (String colour : COLOURS) {
					event.getRegistry().register(new BenchNether(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_" + colour + "_single_" + wood));
					event.getRegistry().register(new BackBenchNether(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_back_padded_" + colour + "_single_" + wood));
				}

				if (log)
					event.getRegistry().register(new LogBenchNether(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_log_single_" + wood));
			}
		}
	}

	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event) {
		SconceVariants.register(event.getRegistry()::register, "light_metal_ironage_sconce_floor_empty_iron", name -> new LightHolderSconceFloor(1, 10, SoundType.METAL, name));

		SconceVariants.register(event.getRegistry()::register, "light_metal_ironage_sconce_floor_torch_iron", name -> new LightSourceSconceTorchFloor(1, 10, SoundType.METAL, name));
		SconceVariants.register(event.getRegistry()::register, "light_metal_ironage_sconce_floor_torch_iron_unlit", name -> new LightSourceSconceTorchFloorUnlit(1, 10, SoundType.METAL, name));

		SconceVariants.register(event.getRegistry()::register, "light_metal_ironage_sconce_floor_redtorch_iron", name -> new LightSourceSconceRedTorchFloor(1, 10, SoundType.METAL, name));
		SconceVariants.register(event.getRegistry()::register, "light_metal_ironage_sconce_floor_redtorch_iron_unlit", name -> new LightSourceSconceRedTorchFloorUnlit(1, 10, SoundType.METAL, name));

		SconceVariants.register(event.getRegistry()::register, "light_metal_ironage_sconce_wall_empty_iron", name -> new LightHolderSconceWall(1, 10, SoundType.METAL, name));

		SconceVariants.register(event.getRegistry()::register, "light_metal_ironage_sconce_wall_torch_iron", name -> new LightSourceSconceTorchWall(1, 10, SoundType.METAL, name));
		SconceVariants.register(event.getRegistry()::register, "light_metal_ironage_sconce_wall_torch_iron_unlit", name -> new LightSourceSconceTorchWallUnlit(1, 10, SoundType.METAL, name));

		SconceVariants.register(event.getRegistry()::register, "light_metal_ironage_sconce_floor_soultorch_iron", name -> new LightSourceSconceSoulTorchFloor(1, 10, SoundType.METAL, name));
		SconceVariants.register(event.getRegistry()::register, "light_metal_ironage_sconce_wall_soultorch_iron", name -> new LightSourceSconceSoulTorchWall(1, 10, SoundType.METAL, name));

		SconceVariants.register(event.getRegistry()::register, "light_metal_ironage_sconce_wall_redtorch_iron", name -> new LightSourceSconceRedTorchWall(1, 10, SoundType.METAL, name));
		SconceVariants.register(event.getRegistry()::register, "light_metal_ironage_sconce_wall_redtorch_iron_unlit", name -> new LightSourceSconceRedTorchWallUnlit(1, 10, SoundType.METAL, name));

		event.getRegistry().register(new LightSourceGlowdust(.3F, .3F, SoundType.GLASS, "light_metal_ironage_block_floor_glow_clear"));
		SconceVariants.register(event.getRegistry()::register, "light_metal_ironage_sconce_floor_glow_iron", name -> new LightSourceSconceGlowFloor(1, 10, SoundType.METAL, name));
		SconceVariants.register(event.getRegistry()::register, "light_metal_ironage_sconce_wall_glow_iron", name -> new LightSourceSconceGlowWall(1, 10, SoundType.METAL, name));

		event.getRegistry().register(new LightSourceLava(.3F, .3F, SoundType.GLASS, "light_metal_ironage_block_floor_lava_clear"));
		SconceVariants.register(event.getRegistry()::register, "light_metal_ironage_sconce_floor_lava_iron", name -> new LightSourceSconceLavaFloor(1, 10, SoundType.METAL, name));
		SconceVariants.register(event.getRegistry()::register, "light_metal_ironage_sconce_wall_lava_iron", name -> new LightSourceSconceLavaWall(1, 10, SoundType.METAL, name));

		event.getRegistry().register(new LightSourceRed(.3F, .3F, SoundType.GLASS, "light_metal_ironage_block_floor_red_clear"));
		event.getRegistry().register(new LightSourceRedOne(.3F, .3F, SoundType.GLASS, "light_metal_ironage_block_floor_red_clear_one"));
		event.getRegistry().register(new LightSourceRedTwo(.3F, .3F, SoundType.GLASS, "light_metal_ironage_block_floor_red_clear_two"));
		event.getRegistry().register(new LightSourceRedThree(.3F, .3F, SoundType.GLASS, "light_metal_ironage_block_floor_red_clear_three"));
		event.getRegistry().register(new LightSourceRedFour(.3F, .3F, SoundType.GLASS, "light_metal_ironage_block_floor_red_clear_four"));
		event.getRegistry().register(new LightSourceRedFive(.3F, .3F, SoundType.GLASS, "light_metal_ironage_block_floor_red_clear_five"));
		event.getRegistry().register(new LightSourceRedSix(.3F, .3F, SoundType.GLASS, "light_metal_ironage_block_floor_red_clear_six"));
		event.getRegistry().register(new LightSourceRedSeven(.3F, .3F, SoundType.GLASS, "light_metal_ironage_block_floor_red_clear_seven"));
		event.getRegistry().register(new LightSourceRedEight(.3F, .3F, SoundType.GLASS, "light_metal_ironage_block_floor_red_clear_eight"));
		event.getRegistry().register(new LightSourceRedNine(.3F, .3F, SoundType.GLASS, "light_metal_ironage_block_floor_red_clear_nine"));
		event.getRegistry().register(new LightSourceRedTen(.3F, .3F, SoundType.GLASS, "light_metal_ironage_block_floor_red_clear_ten"));
		event.getRegistry().register(new LightSourceRedEleven(.3F, .3F, SoundType.GLASS, "light_metal_ironage_block_floor_red_clear_eleven"));
		event.getRegistry().register(new LightSourceRedTwelve(.3F, .3F, SoundType.GLASS, "light_metal_ironage_block_floor_red_clear_twelve"));
		event.getRegistry().register(new LightSourceRedThirteen(.3F, .3F, SoundType.GLASS, "light_metal_ironage_block_floor_red_clear_thirteen"));
		event.getRegistry().register(new LightSourceRedFourteen(.3F, .3F, SoundType.GLASS, "light_metal_ironage_block_floor_red_clear_fourteen"));
		event.getRegistry().register(new LightSourceRedFifteen(.3F, .3F, SoundType.GLASS, "light_metal_ironage_block_floor_red_clear_fifteen"));

		SconceVariants.register(event.getRegistry()::register, "light_metal_ironage_sconce_floor_red_iron", name -> new LightSourceSconceRedFloor(1, 10, SoundType.METAL, name));
		SconceVariants.register(event.getRegistry()::register, "light_metal_ironage_sconce_floor_red_iron_one", name -> new LightSourceSconceRedFloorOne(1, 10, SoundType.METAL, name));
		SconceVariants.register(event.getRegistry()::register, "light_metal_ironage_sconce_floor_red_iron_two", name -> new LightSourceSconceRedFloorTwo(1, 10, SoundType.METAL, name));
		SconceVariants.register(event.getRegistry()::register, "light_metal_ironage_sconce_floor_red_iron_three", name -> new LightSourceSconceRedFloorThree(1, 10, SoundType.METAL, name));
		SconceVariants.register(event.getRegistry()::register, "light_metal_ironage_sconce_floor_red_iron_four", name -> new LightSourceSconceRedFloorFour(1, 10, SoundType.METAL, name));
		SconceVariants.register(event.getRegistry()::register, "light_metal_ironage_sconce_floor_red_iron_five", name -> new LightSourceSconceRedFloorFive(1, 10, SoundType.METAL, name));
		SconceVariants.register(event.getRegistry()::register, "light_metal_ironage_sconce_floor_red_iron_six", name -> new LightSourceSconceRedFloorSix(1, 10, SoundType.METAL, name));
		SconceVariants.register(event.getRegistry()::register, "light_metal_ironage_sconce_floor_red_iron_seven", name -> new LightSourceSconceRedFloorSeven(1, 10, SoundType.METAL, name));
		SconceVariants.register(event.getRegistry()::register, "light_metal_ironage_sconce_floor_red_iron_eight", name -> new LightSourceSconceRedFloorEight(1, 10, SoundType.METAL, name));
		SconceVariants.register(event.getRegistry()::register, "light_metal_ironage_sconce_floor_red_iron_nine", name -> new LightSourceSconceRedFloorNine(1, 10, SoundType.METAL, name));
		SconceVariants.register(event.getRegistry()::register, "light_metal_ironage_sconce_floor_red_iron_ten", name -> new LightSourceSconceRedFloorTen(1, 10, SoundType.METAL, name));
		SconceVariants.register(event.getRegistry()::register, "light_metal_ironage_sconce_floor_red_iron_eleven", name -> new LightSourceSconceRedFloorEleven(1, 10, SoundType.METAL, name));
		SconceVariants.register(event.getRegistry()::register, "light_metal_ironage_sconce_floor_red_iron_twelve", name -> new LightSourceSconceRedFloorTwelve(1, 10, SoundType.METAL, name));
		SconceVariants.register(event.getRegistry()::register, "light_metal_ironage_sconce_floor_red_iron_thirteen", name -> new LightSourceSconceRedFloorThirteen(1, 10, SoundType.METAL, name));
		SconceVariants.register(event.getRegistry()::register, "light_metal_ironage_sconce_floor_red_iron_fourteen", name -> new LightSourceSconceRedFloorFourteen(1, 10, SoundType.METAL, name));
		SconceVariants.register(event.getRegistry()::register, "light_metal_ironage_sconce_floor_red_iron_fifteen", name -> new LightSourceSconceRedFloorFifteen(1, 10, SoundType.METAL, name));

		SconceVariants.register(event.getRegistry()::register, "light_metal_ironage_sconce_wall_red_iron", name -> new LightSourceSconceRedWall(1, 10, SoundType.METAL, name));
		SconceVariants.register(event.getRegistry()::register, "light_metal_ironage_sconce_wall_red_iron_one", name -> new LightSourceSconceRedWallOne(1, 10, SoundType.METAL, name));
		SconceVariants.register(event.getRegistry()::register, "light_metal_ironage_sconce_wall_red_iron_two", name -> new LightSourceSconceRedWallTwo(1, 10, SoundType.METAL, name));
		SconceVariants.register(event.getRegistry()::register, "light_metal_ironage_sconce_wall_red_iron_three", name -> new LightSourceSconceRedWallThree(1, 10, SoundType.METAL, name));
		SconceVariants.register(event.getRegistry()::register, "light_metal_ironage_sconce_wall_red_iron_four", name -> new LightSourceSconceRedWallFour(1, 10, SoundType.METAL, name));
		SconceVariants.register(event.getRegistry()::register, "light_metal_ironage_sconce_wall_red_iron_five", name -> new LightSourceSconceRedWallFive(1, 10, SoundType.METAL, name));
		SconceVariants.register(event.getRegistry()::register, "light_metal_ironage_sconce_wall_red_iron_six", name -> new LightSourceSconceRedWallSix(1, 10, SoundType.METAL, name));
		SconceVariants.register(event.getRegistry()::register, "light_metal_ironage_sconce_wall_red_iron_seven", name -> new LightSourceSconceRedWallSeven(1, 10, SoundType.METAL, name));
		SconceVariants.register(event.getRegistry()::register, "light_metal_ironage_sconce_wall_red_iron_eight", name -> new LightSourceSconceRedWallEight(1, 10, SoundType.METAL, name));
		SconceVariants.register(event.getRegistry()::register, "light_metal_ironage_sconce_wall_red_iron_nine", name -> new LightSourceSconceRedWallNine(1, 10, SoundType.METAL, name));
		SconceVariants.register(event.getRegistry()::register, "light_metal_ironage_sconce_wall_red_iron_ten", name -> new LightSourceSconceRedWallTen(1, 10, SoundType.METAL, name));
		SconceVariants.register(event.getRegistry()::register, "light_metal_ironage_sconce_wall_red_iron_eleven", name -> new LightSourceSconceRedWallEleven(1, 10, SoundType.METAL, name));
		SconceVariants.register(event.getRegistry()::register, "light_metal_ironage_sconce_wall_red_iron_twelve", name -> new LightSourceSconceRedWallTwelve(1, 10, SoundType.METAL, name));
		SconceVariants.register(event.getRegistry()::register, "light_metal_ironage_sconce_wall_red_iron_thirteen", name -> new LightSourceSconceRedWallThirteen(1, 10, SoundType.METAL, name));
		SconceVariants.register(event.getRegistry()::register, "light_metal_ironage_sconce_wall_red_iron_fourteen", name -> new LightSourceSconceRedWallFourteen(1, 10, SoundType.METAL, name));
		SconceVariants.register(event.getRegistry()::register, "light_metal_ironage_sconce_wall_red_iron_fifteen", name -> new LightSourceSconceRedWallFifteen(1, 10, SoundType.METAL, name));

		event.getRegistry().register(new ObsideanLump(1, 10, SoundType.STONE, "obsidian_chunk"));

		registerChairs(event, VANILLA_WOOD_TYPES,
			IronAgeFurnitureConfiguration.CLIENT.GENERATE_SHIELD_CHAIRS.get(),
			IronAgeFurnitureConfiguration.CLIENT.GENERATE_SHORT_STOOLS.get(),
			IronAgeFurnitureConfiguration.CLIENT.GENERATE_TALL_STOOLS.get(),
			IronAgeFurnitureConfiguration.CLIENT.GENERATE_BENCHES.get(),
			VANILLA_NETHER_WOOD_TYPES);


		if (IronAgeFurnitureConfiguration.CLIENT.INTEGRATION_BIOMESOPLENTY.get() && ModList.get().isLoaded("biomesoplenty")) {
			registerChairs(event, BOP_WOOD_TYPES,
				IronAgeFurnitureConfiguration.CLIENT.GENERATE_SHIELD_CHAIRS.get(),
				IronAgeFurnitureConfiguration.CLIENT.GENERATE_SHORT_STOOLS.get(),
				IronAgeFurnitureConfiguration.CLIENT.GENERATE_TALL_STOOLS.get(),
				IronAgeFurnitureConfiguration.CLIENT.GENERATE_BENCHES.get(),
				BOP_NETHER_WOOD_TYPES);
		}

		if (IronAgeFurnitureConfiguration.CLIENT.INTEGRATION_BIOMESYOUGO.get() && ModList.get().isLoaded("byg")) {
			registerChairs(event, BYG_WOOD_TYPES,
				IronAgeFurnitureConfiguration.CLIENT.GENERATE_SHIELD_CHAIRS.get(),
				IronAgeFurnitureConfiguration.CLIENT.GENERATE_SHORT_STOOLS.get(),
				IronAgeFurnitureConfiguration.CLIENT.GENERATE_TALL_STOOLS.get(),
				IronAgeFurnitureConfiguration.CLIENT.GENERATE_BENCHES.get(),
				BYG_NETHER_WOOD_TYPES);
		}

		if (IronAgeFurnitureConfiguration.CLIENT.INTEGRATION_IMMERSIVEENGINEERING.get() && ModList.get().isLoaded("immersiveengineering")) {
			registerChairs(event, IE_WOOD_TYPES,
				IronAgeFurnitureConfiguration.CLIENT.GENERATE_SHIELD_CHAIRS.get(),
				IronAgeFurnitureConfiguration.CLIENT.GENERATE_SHORT_STOOLS.get(),
				IronAgeFurnitureConfiguration.CLIENT.GENERATE_TALL_STOOLS.get(),
				IronAgeFurnitureConfiguration.CLIENT.GENERATE_BENCHES.get(), false, IE_NETHER_WOOD_TYPES);
		}
	}
}
