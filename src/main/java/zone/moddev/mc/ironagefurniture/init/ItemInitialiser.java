package zone.moddev.mc.ironagefurniture.init;

import zone.moddev.mc.ironagefurniture.BlockObjectHolder;
import zone.moddev.mc.ironagefurniture.IronAgeFurnitureConfiguration;
import zone.moddev.mc.ironagefurniture.Ironagefurniture;
import zone.moddev.mc.ironagefurniture.api.items.ThrowableLavaLampBlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.BlockItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import static zone.moddev.mc.ironagefurniture.init.resources.bop.BOP_WOOD_TYPES;
import static zone.moddev.mc.ironagefurniture.init.resources.bop.BOP_NETHER_WOOD_TYPES;
import static zone.moddev.mc.ironagefurniture.init.resources.byg.BYG_WOOD_TYPES;
import static zone.moddev.mc.ironagefurniture.init.resources.byg.BYG_NETHER_WOOD_TYPES;
import static zone.moddev.mc.ironagefurniture.init.resources.colours.COLOURS;
import static zone.moddev.mc.ironagefurniture.init.resources.immersiveengineering.IE_WOOD_TYPES;
import static zone.moddev.mc.ironagefurniture.init.resources.immersiveengineering.IE_NETHER_WOOD_TYPES;
import static zone.moddev.mc.ironagefurniture.init.resources.vanilla.VANILLA_WOOD_TYPES;
import static zone.moddev.mc.ironagefurniture.init.resources.vanilla.VANILLA_NETHER_WOOD_TYPES;

@Mod.EventBusSubscriber(modid = Ironagefurniture.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ItemInitialiser {

	private static Block getRegisteredBlock(String path) {
		ResourceLocation id = new ResourceLocation(Ironagefurniture.MODID, path);
		Block block = ForgeRegistries.BLOCKS.getValue(id);
		if (block == null || !id.equals(block.getRegistryName())) {
			throw new IllegalStateException("Furniture block was not registered: " + id);
		}
		return block;
	}

	private static void registerChairs(RegistryEvent.Register<Item> event, String[] woodTypes, boolean log, String[] netherWoodTypes) {
		for (String wood : woodTypes) {
			registerChairItems(event, log, wood);
		}
		
		for (String wood : netherWoodTypes) {
			registerChairItems(event, log, wood);
		}
	}

	private static void registerChairItems(RegistryEvent.Register<Item> event, boolean log, String wood) {
		if (IronAgeFurnitureConfiguration.CLIENT.GENERATE_CLASSIC_CHAIRS.get())
			registerItem(event, getRegisteredBlock("chair_wood_ironage_classic_" + wood));

		if (IronAgeFurnitureConfiguration.CLIENT.GENERATE_SHIELD_CHAIRS.get())
			registerItem(event, getRegisteredBlock("chair_wood_ironage_shield_" + wood));

		if (IronAgeFurnitureConfiguration.CLIENT.GENERATE_SHORT_STOOLS.get())
			registerItem(event, getRegisteredBlock("chair_wood_ironage_stool_short_" + wood));

		if (IronAgeFurnitureConfiguration.CLIENT.GENERATE_TALL_STOOLS.get())
			registerItem(event, getRegisteredBlock("chair_wood_ironage_stool_tall_" + wood));

		if (IronAgeFurnitureConfiguration.CLIENT.GENERATE_BENCHES.get()) {
			registerItem(event, getRegisteredBlock("chair_wood_ironage_bench_single_" + wood));
			registerItem(event, getRegisteredBlock("chair_wood_ironage_bench_back_single_" + wood));

			for (String colour : COLOURS) {
				registerItem(event, getRegisteredBlock("chair_wood_ironage_bench_padded_" + colour + "_single_" + wood));
				registerItem(event, getRegisteredBlock("chair_wood_ironage_bench_back_padded_" + colour + "_single_" + wood));
			}

			if (log)
				registerItem(event, getRegisteredBlock("chair_wood_ironage_bench_log_single_" + wood));
		}
	}

	private static void registerItem(RegistryEvent.Register<Item> event, Block theBlock) {
		event.getRegistry().register(new BlockItem(theBlock, new BlockItem.Properties().tab(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, theBlock.getRegistryName().getPath()));
	}

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		registerChairs(event, VANILLA_WOOD_TYPES, true, VANILLA_NETHER_WOOD_TYPES);

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

			new ThrowableLavaLampBlockItem(BlockObjectHolder.light_metal_ironage_block_floor_lava_clear, new BlockItem.Properties().tab(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.light_metal_ironage_block_floor_lava_clear.getRegistryName().getPath()),
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

			new BlockItem(BlockObjectHolder.light_metal_ironage_sconce_floor_red_iron, new BlockItem.Properties()).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.light_metal_ironage_sconce_floor_red_iron.getRegistryName().getPath()),
			new BlockItem(BlockObjectHolder.light_metal_ironage_sconce_floor_red_iron_one, new BlockItem.Properties()).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.light_metal_ironage_sconce_floor_red_iron_one.getRegistryName().getPath()),
			new BlockItem(BlockObjectHolder.light_metal_ironage_sconce_floor_red_iron_two, new BlockItem.Properties()).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.light_metal_ironage_sconce_floor_red_iron_two.getRegistryName().getPath()),
			new BlockItem(BlockObjectHolder.light_metal_ironage_sconce_floor_red_iron_three, new BlockItem.Properties()).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.light_metal_ironage_sconce_floor_red_iron_three.getRegistryName().getPath()),
			new BlockItem(BlockObjectHolder.light_metal_ironage_sconce_floor_red_iron_four, new BlockItem.Properties()).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.light_metal_ironage_sconce_floor_red_iron_four.getRegistryName().getPath()),
			new BlockItem(BlockObjectHolder.light_metal_ironage_sconce_floor_red_iron_five, new BlockItem.Properties()).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.light_metal_ironage_sconce_floor_red_iron_five.getRegistryName().getPath()),
			new BlockItem(BlockObjectHolder.light_metal_ironage_sconce_floor_red_iron_six, new BlockItem.Properties()).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.light_metal_ironage_sconce_floor_red_iron_six.getRegistryName().getPath()),
			new BlockItem(BlockObjectHolder.light_metal_ironage_sconce_floor_red_iron_seven, new BlockItem.Properties()).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.light_metal_ironage_sconce_floor_red_iron_seven.getRegistryName().getPath()),
			new BlockItem(BlockObjectHolder.light_metal_ironage_sconce_floor_red_iron_eight, new BlockItem.Properties()).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.light_metal_ironage_sconce_floor_red_iron_eight.getRegistryName().getPath()),
			new BlockItem(BlockObjectHolder.light_metal_ironage_sconce_floor_red_iron_nine, new BlockItem.Properties()).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.light_metal_ironage_sconce_floor_red_iron_nine.getRegistryName().getPath()),
			new BlockItem(BlockObjectHolder.light_metal_ironage_sconce_floor_red_iron_ten, new BlockItem.Properties()).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.light_metal_ironage_sconce_floor_red_iron_ten.getRegistryName().getPath()),
			new BlockItem(BlockObjectHolder.light_metal_ironage_sconce_floor_red_iron_eleven, new BlockItem.Properties()).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.light_metal_ironage_sconce_floor_red_iron_eleven.getRegistryName().getPath()),
			new BlockItem(BlockObjectHolder.light_metal_ironage_sconce_floor_red_iron_twelve, new BlockItem.Properties()).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.light_metal_ironage_sconce_floor_red_iron_twelve.getRegistryName().getPath()),
			new BlockItem(BlockObjectHolder.light_metal_ironage_sconce_floor_red_iron_thirteen, new BlockItem.Properties()).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.light_metal_ironage_sconce_floor_red_iron_thirteen.getRegistryName().getPath()),
			new BlockItem(BlockObjectHolder.light_metal_ironage_sconce_floor_red_iron_fourteen, new BlockItem.Properties()).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.light_metal_ironage_sconce_floor_red_iron_fourteen.getRegistryName().getPath()),
			new BlockItem(BlockObjectHolder.light_metal_ironage_sconce_floor_red_iron_fifteen, new BlockItem.Properties()).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.light_metal_ironage_sconce_floor_red_iron_fifteen.getRegistryName().getPath()),

			new BlockItem(BlockObjectHolder.light_metal_ironage_sconce_wall_red_iron, new BlockItem.Properties()).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.light_metal_ironage_sconce_wall_red_iron.getRegistryName().getPath()),
			new BlockItem(BlockObjectHolder.light_metal_ironage_sconce_wall_red_iron_one, new BlockItem.Properties()).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.light_metal_ironage_sconce_wall_red_iron_one.getRegistryName().getPath()),
			new BlockItem(BlockObjectHolder.light_metal_ironage_sconce_wall_red_iron_two, new BlockItem.Properties()).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.light_metal_ironage_sconce_wall_red_iron_two.getRegistryName().getPath()),
			new BlockItem(BlockObjectHolder.light_metal_ironage_sconce_wall_red_iron_three, new BlockItem.Properties()).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.light_metal_ironage_sconce_wall_red_iron_three.getRegistryName().getPath()),
			new BlockItem(BlockObjectHolder.light_metal_ironage_sconce_wall_red_iron_four, new BlockItem.Properties()).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.light_metal_ironage_sconce_wall_red_iron_four.getRegistryName().getPath()),
			new BlockItem(BlockObjectHolder.light_metal_ironage_sconce_wall_red_iron_five, new BlockItem.Properties()).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.light_metal_ironage_sconce_wall_red_iron_five.getRegistryName().getPath()),
			new BlockItem(BlockObjectHolder.light_metal_ironage_sconce_wall_red_iron_six, new BlockItem.Properties()).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.light_metal_ironage_sconce_wall_red_iron_six.getRegistryName().getPath()),
			new BlockItem(BlockObjectHolder.light_metal_ironage_sconce_wall_red_iron_seven, new BlockItem.Properties()).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.light_metal_ironage_sconce_wall_red_iron_seven.getRegistryName().getPath()),
			new BlockItem(BlockObjectHolder.light_metal_ironage_sconce_wall_red_iron_eight, new BlockItem.Properties()).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.light_metal_ironage_sconce_wall_red_iron_eight.getRegistryName().getPath()),
			new BlockItem(BlockObjectHolder.light_metal_ironage_sconce_wall_red_iron_nine, new BlockItem.Properties()).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.light_metal_ironage_sconce_wall_red_iron_nine.getRegistryName().getPath()),
			new BlockItem(BlockObjectHolder.light_metal_ironage_sconce_wall_red_iron_ten, new BlockItem.Properties()).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.light_metal_ironage_sconce_wall_red_iron_ten.getRegistryName().getPath()),
			new BlockItem(BlockObjectHolder.light_metal_ironage_sconce_wall_red_iron_eleven, new BlockItem.Properties()).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.light_metal_ironage_sconce_wall_red_iron_eleven.getRegistryName().getPath()),
			new BlockItem(BlockObjectHolder.light_metal_ironage_sconce_wall_red_iron_twelve, new BlockItem.Properties()).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.light_metal_ironage_sconce_wall_red_iron_twelve.getRegistryName().getPath()),
			new BlockItem(BlockObjectHolder.light_metal_ironage_sconce_wall_red_iron_thirteen, new BlockItem.Properties()).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.light_metal_ironage_sconce_wall_red_iron_thirteen.getRegistryName().getPath()),
			new BlockItem(BlockObjectHolder.light_metal_ironage_sconce_wall_red_iron_fourteen, new BlockItem.Properties()).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.light_metal_ironage_sconce_wall_red_iron_fourteen.getRegistryName().getPath()),
			new BlockItem(BlockObjectHolder.light_metal_ironage_sconce_wall_red_iron_fifteen, new BlockItem.Properties()).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.light_metal_ironage_sconce_wall_red_iron_fifteen.getRegistryName().getPath()),

			new BlockItem(BlockObjectHolder.obsidian_chunk, new BlockItem.Properties().tab(Ironagefurniture.IAF_GROUP)).setRegistryName(Ironagefurniture.MODID, BlockObjectHolder.obsidian_chunk.getRegistryName().getPath())
		);

		if (IronAgeFurnitureConfiguration.CLIENT.INTEGRATION_BIOMESOPLENTY.get() && ModList.get().isLoaded("biomesoplenty")) {
			registerChairs(event, BOP_WOOD_TYPES, true, BOP_NETHER_WOOD_TYPES);
		}

		if (IronAgeFurnitureConfiguration.CLIENT.INTEGRATION_BIOMESYOUGO.get() && ModList.get().isLoaded("byg")) {
			registerChairs(event, BYG_WOOD_TYPES, true, BYG_NETHER_WOOD_TYPES);
		}

		if (IronAgeFurnitureConfiguration.CLIENT.INTEGRATION_IMMERSIVEENGINEERING.get() && ModList.get().isLoaded("immersiveengineering")) {
			registerChairs(event, IE_WOOD_TYPES, false, IE_NETHER_WOOD_TYPES);
		}
	}
}
