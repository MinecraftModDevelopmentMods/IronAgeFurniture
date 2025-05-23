package com.mcmoddev.ironagefurniture.init;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import com.mcmoddev.ironagefurniture.Ironagefurniture;
import com.mcmoddev.ironagefurniture.ModItems;
import com.mcmoddev.ironagefurniture.api.blocks.furniture.LogBench;
import com.mcmoddev.ironagefurniture.api.blocks.furniture.LogBenchNether;

import java.util.function.Supplier;

public class ModVanillaLogBench {
	public static final DeferredRegister<Block> REGISTER = DeferredRegister.create(ForgeRegistries.BLOCKS, Ironagefurniture.MODID);
	
	private static RegistryObject<Block> register(String name, Supplier<Block> block) {
		return register(name, block, new Item.Properties()); 
	}
	
	private static RegistryObject<Block> register(String name, Supplier<Block> block, Item.Properties properties) {
		RegistryObject<Block> registryObject = REGISTER.register(name, block);
		
		ModItems.REGISTER.register(name, () -> new BlockItem(registryObject.get(), properties));
		
		return registryObject; 
	}

	public static RegistryObject<Block> chair_wood_ironage_bench_log_single_oak = register("chair_wood_ironage_bench_log_single_oak", () -> new LogBench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_log_single_oak"));;
	public static RegistryObject<Block> chair_wood_ironage_bench_log_single_acacia = register("chair_wood_ironage_bench_log_single_acacia", () -> new LogBench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_log_single_acacia"));
	public static RegistryObject<Block> chair_wood_ironage_bench_log_single_dark_oak = register("chair_wood_ironage_bench_log_single_dark_oak", () -> new LogBench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_log_single_dark_oak"));
	public static RegistryObject<Block> chair_wood_ironage_bench_log_single_birch = register("chair_wood_ironage_bench_log_single_birch", () -> new LogBench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_log_single_birch"));
	public static RegistryObject<Block> chair_wood_ironage_bench_log_single_jungle = register("chair_wood_ironage_bench_log_single_jungle", () -> new LogBench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_log_single_jungle"));
	public static RegistryObject<Block> chair_wood_ironage_bench_log_single_spruce = register("chair_wood_ironage_bench_log_single_spruce", () -> new LogBench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_log_single_spruce"));
	public static RegistryObject<Block> chair_wood_ironage_bench_log_single_warped = register("chair_wood_ironage_bench_log_single_warped", () -> new LogBenchNether(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_log_single_warped"));
	public static RegistryObject<Block> chair_wood_ironage_bench_log_single_crimson = register("chair_wood_ironage_bench_log_single_crimson", () -> new LogBenchNether(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_log_single_crimson"));
}
