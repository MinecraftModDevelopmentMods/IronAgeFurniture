package zone.moddev.mc.ironagefurniture.migration;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;
import zone.moddev.mc.ironagefurniture.IronAgeFurnitureConfiguration;
import zone.moddev.mc.ironagefurniture.Ironagefurniture;

import static zone.moddev.mc.ironagefurniture.init.resources.bop.BOP_WOOD_TYPES;
import static zone.moddev.mc.ironagefurniture.init.resources.bop.BOP_NETHER_WOOD_TYPES;
import static zone.moddev.mc.ironagefurniture.init.resources.byg.BYG_WOOD_TYPES;
import static zone.moddev.mc.ironagefurniture.init.resources.byg.BYG_NETHER_WOOD_TYPES;
import static zone.moddev.mc.ironagefurniture.init.resources.immersiveengineering.IE_WOOD_TYPES;
import static zone.moddev.mc.ironagefurniture.init.resources.immersiveengineering.IE_NETHER_WOOD_TYPES;
import static zone.moddev.mc.ironagefurniture.init.resources.vanilla.VANILLA_NETHER_WOOD_TYPES;
import static zone.moddev.mc.ironagefurniture.init.resources.vanilla.VANILLA_WOOD_TYPES;

/** Registers itemless compatibility states for the shared 1.10/1.12 padded IDs. */
@Mod.EventBusSubscriber(modid = Ironagefurniture.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
@ObjectHolder(Ironagefurniture.MODID)
public final class LegacyPaddedMigrationRegistry {
	public static final BlockEntityType<LegacyPaddedBenchBlockEntity> padded_bench_colour = null;
	private static final List<Block> COMPATIBILITY_BLOCKS = new ArrayList<>();

	private LegacyPaddedMigrationRegistry() {
	}

	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event) {
		if (!IronAgeFurnitureConfiguration.CLIENT.GENERATE_BENCHES.get()) {
			return;
		}
		registerWoods(event, VANILLA_WOOD_TYPES);
		registerWoods(event, VANILLA_NETHER_WOOD_TYPES);
		if (IronAgeFurnitureConfiguration.CLIENT.INTEGRATION_BIOMESOPLENTY.get()
				&& ModList.get().isLoaded("biomesoplenty")) {
			registerWoods(event, BOP_WOOD_TYPES);
			registerWoods(event, BOP_NETHER_WOOD_TYPES);
		}
		if (IronAgeFurnitureConfiguration.CLIENT.INTEGRATION_BIOMESYOUGO.get()
				&& ModList.get().isLoaded("byg")) {
			registerWoods(event, BYG_WOOD_TYPES);
			registerWoods(event, BYG_NETHER_WOOD_TYPES);
		}
		if (IronAgeFurnitureConfiguration.CLIENT.INTEGRATION_IMMERSIVEENGINEERING.get()
				&& ModList.get().isLoaded("immersiveengineering")) {
			registerWoods(event, IE_WOOD_TYPES);
			registerWoods(event, IE_NETHER_WOOD_TYPES);
		}
	}

	private static void registerWoods(RegistryEvent.Register<Block> event, String[] woods) {
		for (String wood : woods) {
			Block bench = new LegacyPaddedBench(LegacyPaddedBenchIds.legacyPaddedPath(false, wood));
			Block backBench = new LegacyPaddedBackBench(LegacyPaddedBenchIds.legacyPaddedPath(true, wood));
			event.getRegistry().register(bench);
			event.getRegistry().register(backBench);
			COMPATIBILITY_BLOCKS.add(bench);
			COMPATIBILITY_BLOCKS.add(backBench);
		}
	}

	@SubscribeEvent
	public static void registerBlockEntities(RegistryEvent.Register<BlockEntityType<?>> event) {
		BlockEntityType<LegacyPaddedBenchBlockEntity> type = BlockEntityType.Builder
				.of(LegacyPaddedBenchBlockEntity::new,
						COMPATIBILITY_BLOCKS.toArray(new Block[COMPATIBILITY_BLOCKS.size()]))
				.build(null);
		type.setRegistryName(LegacyPaddedBenchIds.BLOCK_ENTITY_ID);
		event.getRegistry().register(type);
	}
}
