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
import com.mcmoddev.ironagefurniture.api.blocks.furniture.BackBench;
import java.util.function.Supplier;

public class ModVanillaBackBench {
	public static final DeferredRegister<Block> REGISTER = DeferredRegister.create(ForgeRegistries.BLOCKS, Ironagefurniture.MODID);
	
	private static RegistryObject<Block> register(String name, Supplier<Block> block) {
		return register(name, block, new Item.Properties()); 
	}
	
	private static RegistryObject<Block> register(String name, Supplier<Block> block, Item.Properties properties) {
		RegistryObject<Block> registryObject = REGISTER.register(name, block);
		
		ModItems.REGISTER.register(name, () -> new BlockItem(registryObject.get(), properties));
		
		return registryObject; 
	}
	
	public static RegistryObject<Block> chair_wood_ironage_bench_back_single_oak = register("chair_wood_ironage_bench_back_single_oak", () -> new BackBench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_back_single_oak"));
	public static RegistryObject<Block> chair_wood_ironage_bench_back_single_acacia = register("chair_wood_ironage_bench_back_single_acacia", () -> new BackBench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_back_single_acacia"));
	public static RegistryObject<Block> chair_wood_ironage_bench_back_single_dark_oak = register("chair_wood_ironage_bench_back_single_dark_oak", () -> new BackBench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_back_single_dark_oak"));
	public static RegistryObject<Block> chair_wood_ironage_bench_back_single_birch = register("chair_wood_ironage_bench_back_single_birch", () -> new BackBench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_back_single_birch"));
	public static RegistryObject<Block> chair_wood_ironage_bench_back_single_jungle = register("chair_wood_ironage_bench_back_single_jungle", () -> new BackBench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_back_single_jungle"));
	public static RegistryObject<Block> chair_wood_ironage_bench_back_single_spruce = register("chair_wood_ironage_bench_back_single_spruce", () -> new BackBench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_back_single_spruce"));
	
	
}
