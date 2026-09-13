package zone.moddev.mc.ironagefurniture.init;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredHolder;

import zone.moddev.mc.ironagefurniture.Ironagefurniture;
import zone.moddev.mc.ironagefurniture.ModItems;
import zone.moddev.mc.ironagefurniture.api.blocks.furniture.BackBench;
import zone.moddev.mc.ironagefurniture.api.blocks.furniture.Bench;
import zone.moddev.mc.ironagefurniture.api.blocks.furniture.Chair;
import zone.moddev.mc.ironagefurniture.api.blocks.furniture.ChairNether;
import zone.moddev.mc.ironagefurniture.api.blocks.furniture.Stool;
import zone.moddev.mc.ironagefurniture.api.blocks.furniture.TallStool;

import java.util.function.Supplier;

public class ModIEBlocks {
	public static final DeferredRegister<Block> REGISTER = DeferredRegister.create(BuiltInRegistries.BLOCK, Ironagefurniture.MODID);
	
	private static DeferredHolder<Block, Block> register(String name, Supplier<Block> block) {
		return register(name, block, new Item.Properties()); 
	}
	
	private static DeferredHolder<Block, Block> register(String name, Supplier<Block> block, Item.Properties properties) {
		DeferredHolder<Block, Block> registryObject = REGISTER.register(name, block);
		
		ModItems.REGISTER.register(name, () -> new BlockItem(registryObject.get(), properties));
		
		return registryObject; 
	}
	public static DeferredHolder<Block, Block> chair_wood_ironage_classic_immersiveengineering_treated_wood = register("chair_wood_ironage_classic_immersiveengineering_treated_wood", () -> new Chair(1, 10, SoundType.WOOD, "chair_wood_ironage_classic_immersiveengineering_treated_wood"));

	public static DeferredHolder<Block, Block> chair_wood_ironage_shield_immersiveengineering_treated_wood = register("chair_wood_ironage_shield_immersiveengineering_treated_wood", () -> new Chair(1, 10, SoundType.WOOD, "chair_wood_ironage_shield_immersiveengineering_treated_wood"));

	public static DeferredHolder<Block, Block> chair_wood_ironage_stool_short_immersiveengineering_treated_wood = register("chair_wood_ironage_stool_short_immersiveengineering_treated_wood", () -> new Stool(1, 10, SoundType.WOOD, "chair_wood_ironage_stool_short_immersiveengineering_treated_wood"));

	public static DeferredHolder<Block, Block> chair_wood_ironage_stool_tall_immersiveengineering_treated_wood = register("chair_wood_ironage_stool_tall_immersiveengineering_treated_wood", () -> new TallStool(1, 10, SoundType.WOOD, "chair_wood_ironage_stool_tall_immersiveengineering_treated_wood"));

	public static DeferredHolder<Block, Block> chair_wood_ironage_bench_single_immersiveengineering_treated_wood = register("chair_wood_ironage_bench_single_immersiveengineering_treated_wood", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_single_immersiveengineering_treated_wood"));
	public static DeferredHolder<Block, Block> chair_wood_ironage_bench_back_single_immersiveengineering_treated_wood = register("chair_wood_ironage_bench_back_single_immersiveengineering_treated_wood", () -> new BackBench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_back_single_immersiveengineering_treated_wood"));

	public static DeferredHolder<Block, Block> chair_wood_ironage_bench_padded_green_single_immersiveengineering_treated_wood = register("chair_wood_ironage_bench_padded_green_single_immersiveengineering_treated_wood", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_green_single_immersiveengineering_treated_wood"));
	public static DeferredHolder<Block, Block> chair_wood_ironage_bench_padded_light_blue_single_immersiveengineering_treated_wood = register("chair_wood_ironage_bench_padded_light_blue_single_immersiveengineering_treated_wood", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_light_blue_single_immersiveengineering_treated_wood"));
	public static DeferredHolder<Block, Block> chair_wood_ironage_bench_padded_light_gray_single_immersiveengineering_treated_wood = register("chair_wood_ironage_bench_padded_light_gray_single_immersiveengineering_treated_wood", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_light_gray_single_immersiveengineering_treated_wood"));
	public static DeferredHolder<Block, Block> chair_wood_ironage_bench_padded_lime_single_immersiveengineering_treated_wood = register("chair_wood_ironage_bench_padded_lime_single_immersiveengineering_treated_wood", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_lime_single_immersiveengineering_treated_wood"));
	public static DeferredHolder<Block, Block> chair_wood_ironage_bench_padded_magenta_single_immersiveengineering_treated_wood = register("chair_wood_ironage_bench_padded_magenta_single_immersiveengineering_treated_wood", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_magenta_single_immersiveengineering_treated_wood"));
	public static DeferredHolder<Block, Block> chair_wood_ironage_bench_padded_orange_single_immersiveengineering_treated_wood = register("chair_wood_ironage_bench_padded_orange_single_immersiveengineering_treated_wood", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_orange_single_immersiveengineering_treated_wood"));
	public static DeferredHolder<Block, Block> chair_wood_ironage_bench_padded_pink_single_immersiveengineering_treated_wood = register("chair_wood_ironage_bench_padded_pink_single_immersiveengineering_treated_wood", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_pink_single_immersiveengineering_treated_wood"));
	public static DeferredHolder<Block, Block> chair_wood_ironage_bench_padded_purple_single_immersiveengineering_treated_wood = register("chair_wood_ironage_bench_padded_purple_single_immersiveengineering_treated_wood", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_purple_single_immersiveengineering_treated_wood"));
	public static DeferredHolder<Block, Block> chair_wood_ironage_bench_padded_red_single_immersiveengineering_treated_wood = register("chair_wood_ironage_bench_padded_red_single_immersiveengineering_treated_wood", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_red_single_immersiveengineering_treated_wood"));
	public static DeferredHolder<Block, Block> chair_wood_ironage_bench_padded_white_single_immersiveengineering_treated_wood = register("chair_wood_ironage_bench_padded_white_single_immersiveengineering_treated_wood", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_white_single_immersiveengineering_treated_wood"));
	public static DeferredHolder<Block, Block> chair_wood_ironage_bench_padded_yellow_single_immersiveengineering_treated_wood = register("chair_wood_ironage_bench_padded_yellow_single_immersiveengineering_treated_wood", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_yellow_single_immersiveengineering_treated_wood"));
	public static DeferredHolder<Block, Block> chair_wood_ironage_bench_padded_black_single_immersiveengineering_treated_wood = register("chair_wood_ironage_bench_padded_black_single_immersiveengineering_treated_wood", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_black_single_immersiveengineering_treated_wood"));
	public static DeferredHolder<Block, Block> chair_wood_ironage_bench_padded_blue_single_immersiveengineering_treated_wood = register("chair_wood_ironage_bench_padded_blue_single_immersiveengineering_treated_wood", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_blue_single_immersiveengineering_treated_wood"));
	public static DeferredHolder<Block, Block> chair_wood_ironage_bench_padded_brown_single_immersiveengineering_treated_wood = register("chair_wood_ironage_bench_padded_brown_single_immersiveengineering_treated_wood", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_brown_single_immersiveengineering_treated_wood"));
	public static DeferredHolder<Block, Block> chair_wood_ironage_bench_padded_cyan_single_immersiveengineering_treated_wood = register("chair_wood_ironage_bench_padded_cyan_single_immersiveengineering_treated_wood", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_cyan_single_immersiveengineering_treated_wood"));
	public static DeferredHolder<Block, Block> chair_wood_ironage_bench_padded_gray_single_immersiveengineering_treated_wood = register("chair_wood_ironage_bench_padded_gray_single_immersiveengineering_treated_wood", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_gray_single_immersiveengineering_treated_wood"));

	public static DeferredHolder<Block, Block> chair_wood_ironage_bench_back_padded_green_single_immersiveengineering_treated_wood = register("chair_wood_ironage_bench_back_padded_green_single_immersiveengineering_treated_wood", () -> new BackBench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_back_padded_green_single_immersiveengineering_treated_wood"));
	public static DeferredHolder<Block, Block> chair_wood_ironage_bench_back_padded_light_blue_single_immersiveengineering_treated_wood = register("chair_wood_ironage_bench_back_padded_light_blue_single_immersiveengineering_treated_wood", () -> new BackBench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_back_padded_light_blue_single_immersiveengineering_treated_wood"));
	public static DeferredHolder<Block, Block> chair_wood_ironage_bench_back_padded_light_gray_single_immersiveengineering_treated_wood = register("chair_wood_ironage_bench_back_padded_light_gray_single_immersiveengineering_treated_wood", () -> new BackBench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_back_padded_light_gray_single_immersiveengineering_treated_wood"));
	public static DeferredHolder<Block, Block> chair_wood_ironage_bench_back_padded_lime_single_immersiveengineering_treated_wood = register("chair_wood_ironage_bench_back_padded_lime_single_immersiveengineering_treated_wood", () -> new BackBench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_back_padded_lime_single_immersiveengineering_treated_wood"));
	public static DeferredHolder<Block, Block> chair_wood_ironage_bench_back_padded_magenta_single_immersiveengineering_treated_wood = register("chair_wood_ironage_bench_back_padded_magenta_single_immersiveengineering_treated_wood", () -> new BackBench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_back_padded_magenta_single_immersiveengineering_treated_wood"));
	public static DeferredHolder<Block, Block> chair_wood_ironage_bench_back_padded_orange_single_immersiveengineering_treated_wood = register("chair_wood_ironage_bench_back_padded_orange_single_immersiveengineering_treated_wood", () -> new BackBench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_back_padded_orange_single_immersiveengineering_treated_wood"));
	public static DeferredHolder<Block, Block> chair_wood_ironage_bench_back_padded_pink_single_immersiveengineering_treated_wood = register("chair_wood_ironage_bench_back_padded_pink_single_immersiveengineering_treated_wood", () -> new BackBench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_back_padded_pink_single_immersiveengineering_treated_wood"));
	public static DeferredHolder<Block, Block> chair_wood_ironage_bench_back_padded_purple_single_immersiveengineering_treated_wood = register("chair_wood_ironage_bench_back_padded_purple_single_immersiveengineering_treated_wood", () -> new BackBench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_back_padded_purple_single_immersiveengineering_treated_wood"));
	public static DeferredHolder<Block, Block> chair_wood_ironage_bench_back_padded_red_single_immersiveengineering_treated_wood = register("chair_wood_ironage_bench_back_padded_red_single_immersiveengineering_treated_wood", () -> new BackBench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_back_padded_red_single_immersiveengineering_treated_wood"));
	public static DeferredHolder<Block, Block> chair_wood_ironage_bench_back_padded_white_single_immersiveengineering_treated_wood = register("chair_wood_ironage_bench_back_padded_white_single_immersiveengineering_treated_wood", () -> new BackBench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_back_padded_white_single_immersiveengineering_treated_wood"));
	public static DeferredHolder<Block, Block> chair_wood_ironage_bench_back_padded_yellow_single_immersiveengineering_treated_wood = register("chair_wood_ironage_bench_back_padded_yellow_single_immersiveengineering_treated_wood", () -> new BackBench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_back_padded_yellow_single_immersiveengineering_treated_wood"));
	public static DeferredHolder<Block, Block> chair_wood_ironage_bench_back_padded_black_single_immersiveengineering_treated_wood = register("chair_wood_ironage_bench_back_padded_black_single_immersiveengineering_treated_wood", () -> new BackBench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_back_padded_black_single_immersiveengineering_treated_wood"));
	public static DeferredHolder<Block, Block> chair_wood_ironage_bench_back_padded_blue_single_immersiveengineering_treated_wood = register("chair_wood_ironage_bench_back_padded_blue_single_immersiveengineering_treated_wood", () -> new BackBench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_back_padded_blue_single_immersiveengineering_treated_wood"));
	public static DeferredHolder<Block, Block> chair_wood_ironage_bench_back_padded_brown_single_immersiveengineering_treated_wood = register("chair_wood_ironage_bench_back_padded_brown_single_immersiveengineering_treated_wood", () -> new BackBench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_back_padded_brown_single_immersiveengineering_treated_wood"));
	public static DeferredHolder<Block, Block> chair_wood_ironage_bench_back_padded_cyan_single_immersiveengineering_treated_wood = register("chair_wood_ironage_bench_back_padded_cyan_single_immersiveengineering_treated_wood", () -> new BackBench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_back_padded_cyan_single_immersiveengineering_treated_wood"));
	public static DeferredHolder<Block, Block> chair_wood_ironage_bench_back_padded_gray_single_immersiveengineering_treated_wood = register("chair_wood_ironage_bench_back_padded_gray_single_immersiveengineering_treated_wood", () -> new BackBench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_back_padded_gray_single_immersiveengineering_treated_wood"));
}
