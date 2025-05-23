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
import com.mcmoddev.ironagefurniture.api.blocks.furniture.Bench;
import com.mcmoddev.ironagefurniture.api.blocks.furniture.BenchNether;

import java.util.function.Supplier;

public class ModVanillaPaddedBench {
	public static final DeferredRegister<Block> REGISTER = DeferredRegister.create(ForgeRegistries.BLOCKS, Ironagefurniture.MODID);
	
	private static RegistryObject<Block> register(String name, Supplier<Block> block) {
		return register(name, block, new Item.Properties()); 
	}
	
	private static RegistryObject<Block> register(String name, Supplier<Block> block, Item.Properties properties) {
		RegistryObject<Block> registryObject = REGISTER.register(name, block);
		
		ModItems.REGISTER.register(name, () -> new BlockItem(registryObject.get(), properties));
		
		return registryObject; 
	}
	
    public static RegistryObject<Block> chair_wood_ironage_bench_padded_green_single_oak = register("chair_wood_ironage_bench_padded_green_single_oak", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_green_single_oak"));;;
	public static RegistryObject<Block> chair_wood_ironage_bench_padded_green_single_acacia = register("chair_wood_ironage_bench_padded_green_single_acacia", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_green_single_acacia"));
	public static RegistryObject<Block> chair_wood_ironage_bench_padded_green_single_dark_oak = register("chair_wood_ironage_bench_padded_green_single_dark_oak", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_green_single_dark_oak"));
	public static RegistryObject<Block> chair_wood_ironage_bench_padded_green_single_birch = register("chair_wood_ironage_bench_padded_green_single_birch", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_green_single_birch"));
	public static RegistryObject<Block> chair_wood_ironage_bench_padded_green_single_jungle = register("chair_wood_ironage_bench_padded_green_single_jungle", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_green_single_jungle"));
	public static RegistryObject<Block> chair_wood_ironage_bench_padded_green_single_spruce = register("chair_wood_ironage_bench_padded_green_single_spruce", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_green_single_spruce"));
	public static RegistryObject<Block> chair_wood_ironage_bench_padded_green_single_warped = register("chair_wood_ironage_bench_padded_green_single_warped", () -> new BenchNether(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_green_single_warped"));
	public static RegistryObject<Block> chair_wood_ironage_bench_padded_green_single_crimson = register("chair_wood_ironage_bench_padded_green_single_crimson", () -> new BenchNether(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_green_single_crimson"));
	
	public static RegistryObject<Block> chair_wood_ironage_bench_padded_light_blue_single_oak = register("chair_wood_ironage_bench_padded_light_blue_single_oak", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_light_blue_single_oak"));;;
	public static RegistryObject<Block> chair_wood_ironage_bench_padded_light_blue_single_acacia = register("chair_wood_ironage_bench_padded_light_blue_single_acacia", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_light_blue_single_acacia"));
	public static RegistryObject<Block> chair_wood_ironage_bench_padded_light_blue_single_dark_oak = register("chair_wood_ironage_bench_padded_light_blue_single_dark_oak", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_light_blue_single_dark_oak"));
	public static RegistryObject<Block> chair_wood_ironage_bench_padded_light_blue_single_birch = register("chair_wood_ironage_bench_padded_light_blue_single_birch", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_light_blue_single_birch"));
	public static RegistryObject<Block> chair_wood_ironage_bench_padded_light_blue_single_jungle = register("chair_wood_ironage_bench_padded_light_blue_single_jungle", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_light_blue_single_jungle"));
	public static RegistryObject<Block> chair_wood_ironage_bench_padded_light_blue_single_spruce = register("chair_wood_ironage_bench_padded_light_blue_single_spruce", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_light_blue_single_spruce"));
    public static RegistryObject<Block> chair_wood_ironage_bench_padded_light_blue_single_warped = register("chair_wood_ironage_bench_padded_light_blue_single_warped", () -> new BenchNether(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_light_blue_single_warped"));
    public static RegistryObject<Block> chair_wood_ironage_bench_padded_light_blue_single_crimson = register("chair_wood_ironage_bench_padded_light_blue_single_crimson", () -> new BenchNether(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_light_blue_single_crimson"));
	
	public static RegistryObject<Block> chair_wood_ironage_bench_padded_light_gray_single_oak = register("chair_wood_ironage_bench_padded_light_gray_single_oak", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_light_gray_single_oak"));;;
	public static RegistryObject<Block> chair_wood_ironage_bench_padded_light_gray_single_acacia = register("chair_wood_ironage_bench_padded_light_gray_single_acacia", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_light_gray_single_acacia"));
	public static RegistryObject<Block> chair_wood_ironage_bench_padded_light_gray_single_dark_oak = register("chair_wood_ironage_bench_padded_light_gray_single_dark_oak", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_light_gray_single_dark_oak"));
	public static RegistryObject<Block> chair_wood_ironage_bench_padded_light_gray_single_birch = register("chair_wood_ironage_bench_padded_light_gray_single_birch", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_light_gray_single_birch"));
	public static RegistryObject<Block> chair_wood_ironage_bench_padded_light_gray_single_jungle = register("chair_wood_ironage_bench_padded_light_gray_single_jungle", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_light_gray_single_jungle"));
	public static RegistryObject<Block> chair_wood_ironage_bench_padded_light_gray_single_spruce = register("chair_wood_ironage_bench_padded_light_gray_single_spruce", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_light_gray_single_spruce"));
	public static RegistryObject<Block> chair_wood_ironage_bench_padded_light_gray_single_warped = register("chair_wood_ironage_bench_padded_light_gray_single_warped", () -> new BenchNether(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_light_gray_single_warped"));
	public static RegistryObject<Block> chair_wood_ironage_bench_padded_light_gray_single_crimson = register("chair_wood_ironage_bench_padded_light_gray_single_crimson", () -> new BenchNether(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_light_gray_single_crimson"));
	
	public static RegistryObject<Block> chair_wood_ironage_bench_padded_lime_single_oak = register("chair_wood_ironage_bench_padded_lime_single_oak", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_lime_single_oak"));;;
	public static RegistryObject<Block> chair_wood_ironage_bench_padded_lime_single_acacia = register("chair_wood_ironage_bench_padded_lime_single_acacia", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_lime_single_acacia"));
	public static RegistryObject<Block> chair_wood_ironage_bench_padded_lime_single_dark_oak = register("chair_wood_ironage_bench_padded_lime_single_dark_oak", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_lime_single_dark_oak"));
	public static RegistryObject<Block> chair_wood_ironage_bench_padded_lime_single_birch = register("chair_wood_ironage_bench_padded_lime_single_birch", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_lime_single_birch"));
	public static RegistryObject<Block> chair_wood_ironage_bench_padded_lime_single_jungle = register("chair_wood_ironage_bench_padded_lime_single_jungle", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_lime_single_jungle"));
	public static RegistryObject<Block> chair_wood_ironage_bench_padded_lime_single_spruce = register("chair_wood_ironage_bench_padded_lime_single_spruce", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_lime_single_spruce"));
	public static RegistryObject<Block> chair_wood_ironage_bench_padded_lime_single_warped = register("chair_wood_ironage_bench_padded_lime_single_warped", () -> new BenchNether(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_lime_single_warped"));
	public static RegistryObject<Block> chair_wood_ironage_bench_padded_lime_single_crimson = register("chair_wood_ironage_bench_padded_lime_single_crimson", () -> new BenchNether(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_lime_single_crimson"));
	
	public static RegistryObject<Block> chair_wood_ironage_bench_padded_magenta_single_oak = register("chair_wood_ironage_bench_padded_magenta_single_oak", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_magenta_single_oak"));;;
	public static RegistryObject<Block> chair_wood_ironage_bench_padded_magenta_single_acacia = register("chair_wood_ironage_bench_padded_magenta_single_acacia", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_magenta_single_acacia"));
	public static RegistryObject<Block> chair_wood_ironage_bench_padded_magenta_single_dark_oak = register("chair_wood_ironage_bench_padded_magenta_single_dark_oak", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_magenta_single_dark_oak"));
	public static RegistryObject<Block> chair_wood_ironage_bench_padded_magenta_single_birch = register("chair_wood_ironage_bench_padded_magenta_single_birch", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_magenta_single_birch"));
	public static RegistryObject<Block> chair_wood_ironage_bench_padded_magenta_single_jungle = register("chair_wood_ironage_bench_padded_magenta_single_jungle", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_magenta_single_jungle"));
	public static RegistryObject<Block> chair_wood_ironage_bench_padded_magenta_single_spruce = register("chair_wood_ironage_bench_padded_magenta_single_spruce", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_magenta_single_spruce"));
 	public static RegistryObject<Block> chair_wood_ironage_bench_padded_magenta_single_warped = register("chair_wood_ironage_bench_padded_magenta_single_warped", () -> new BenchNether(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_magenta_single_warped"));
 	public static RegistryObject<Block> chair_wood_ironage_bench_padded_magenta_single_crimson = register("chair_wood_ironage_bench_padded_magenta_single_crimson", () -> new BenchNether(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_magenta_single_crimson"));
	
	public static RegistryObject<Block> chair_wood_ironage_bench_padded_pink_single_oak = register("chair_wood_ironage_bench_padded_pink_single_oak", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_pink_single_oak"));;;
	public static RegistryObject<Block> chair_wood_ironage_bench_padded_pink_single_acacia = register("chair_wood_ironage_bench_padded_pink_single_acacia", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_pink_single_acacia"));
	public static RegistryObject<Block> chair_wood_ironage_bench_padded_pink_single_dark_oak = register("chair_wood_ironage_bench_padded_pink_single_dark_oak", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_pink_single_dark_oak"));
	public static RegistryObject<Block> chair_wood_ironage_bench_padded_pink_single_birch = register("chair_wood_ironage_bench_padded_pink_single_birch", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_pink_single_birch"));
	public static RegistryObject<Block> chair_wood_ironage_bench_padded_pink_single_jungle = register("chair_wood_ironage_bench_padded_pink_single_jungle", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_pink_single_jungle"));
	public static RegistryObject<Block> chair_wood_ironage_bench_padded_pink_single_spruce = register("chair_wood_ironage_bench_padded_pink_single_spruce", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_pink_single_spruce"));
 	public static RegistryObject<Block> chair_wood_ironage_bench_padded_pink_single_warped = register("chair_wood_ironage_bench_padded_pink_single_warped", () -> new BenchNether(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_pink_single_warped"));
 	public static RegistryObject<Block> chair_wood_ironage_bench_padded_pink_single_crimson = register("chair_wood_ironage_bench_padded_pink_single_crimson", () -> new BenchNether(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_pink_single_crimson"));
	
	public static RegistryObject<Block> chair_wood_ironage_bench_padded_red_single_oak = register("chair_wood_ironage_bench_padded_red_single_oak", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_red_single_oak"));;;
	public static RegistryObject<Block> chair_wood_ironage_bench_padded_red_single_acacia = register("chair_wood_ironage_bench_padded_red_single_acacia", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_red_single_acacia"));
	public static RegistryObject<Block> chair_wood_ironage_bench_padded_red_single_dark_oak = register("chair_wood_ironage_bench_padded_red_single_dark_oak", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_red_single_dark_oak"));
	public static RegistryObject<Block> chair_wood_ironage_bench_padded_red_single_birch = register("chair_wood_ironage_bench_padded_red_single_birch", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_red_single_birch"));
	public static RegistryObject<Block> chair_wood_ironage_bench_padded_red_single_jungle = register("chair_wood_ironage_bench_padded_red_single_jungle", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_red_single_jungle"));
	public static RegistryObject<Block> chair_wood_ironage_bench_padded_red_single_spruce = register("chair_wood_ironage_bench_padded_red_single_spruce", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_red_single_spruce"));
 	public static RegistryObject<Block> chair_wood_ironage_bench_padded_red_single_warped = register("chair_wood_ironage_bench_padded_red_single_warped", () -> new BenchNether(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_red_single_warped"));
 	public static RegistryObject<Block> chair_wood_ironage_bench_padded_red_single_crimson = register("chair_wood_ironage_bench_padded_red_single_crimson", () -> new BenchNether(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_red_single_crimson"));
	
	public static RegistryObject<Block> chair_wood_ironage_bench_padded_yellow_single_oak = register("chair_wood_ironage_bench_padded_yellow_single_oak", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_yellow_single_oak"));;;
	public static RegistryObject<Block> chair_wood_ironage_bench_padded_yellow_single_acacia = register("chair_wood_ironage_bench_padded_yellow_single_acacia", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_yellow_single_acacia"));
	public static RegistryObject<Block> chair_wood_ironage_bench_padded_yellow_single_dark_oak = register("chair_wood_ironage_bench_padded_yellow_single_dark_oak", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_yellow_single_dark_oak"));
	public static RegistryObject<Block> chair_wood_ironage_bench_padded_yellow_single_birch = register("chair_wood_ironage_bench_padded_yellow_single_birch", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_yellow_single_birch"));
	public static RegistryObject<Block> chair_wood_ironage_bench_padded_yellow_single_jungle = register("chair_wood_ironage_bench_padded_yellow_single_jungle", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_yellow_single_jungle"));
	public static RegistryObject<Block> chair_wood_ironage_bench_padded_yellow_single_spruce = register("chair_wood_ironage_bench_padded_yellow_single_spruce", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_yellow_single_spruce"));
   	public static RegistryObject<Block> chair_wood_ironage_bench_padded_yellow_single_warped = register("chair_wood_ironage_bench_padded_yellow_single_warped", () -> new BenchNether(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_yellow_single_warped"));
 	public static RegistryObject<Block> chair_wood_ironage_bench_padded_yellow_single_crimson = register("chair_wood_ironage_bench_padded_yellow_single_crimson", () -> new BenchNether(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_yellow_single_crimson"));
	
	public static RegistryObject<Block> chair_wood_ironage_bench_padded_black_single_oak = register("chair_wood_ironage_bench_padded_black_single_oak", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_black_single_oak"));;;
	public static RegistryObject<Block> chair_wood_ironage_bench_padded_black_single_acacia = register("chair_wood_ironage_bench_padded_black_single_acacia", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_black_single_acacia"));
	public static RegistryObject<Block> chair_wood_ironage_bench_padded_black_single_dark_oak = register("chair_wood_ironage_bench_padded_black_single_dark_oak", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_black_single_dark_oak"));
	public static RegistryObject<Block> chair_wood_ironage_bench_padded_black_single_birch = register("chair_wood_ironage_bench_padded_black_single_birch", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_black_single_birch"));
	public static RegistryObject<Block> chair_wood_ironage_bench_padded_black_single_jungle = register("chair_wood_ironage_bench_padded_black_single_jungle", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_black_single_jungle"));
	public static RegistryObject<Block> chair_wood_ironage_bench_padded_black_single_spruce = register("chair_wood_ironage_bench_padded_black_single_spruce", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_black_single_spruce"));
   	public static RegistryObject<Block> chair_wood_ironage_bench_padded_black_single_warped = register("chair_wood_ironage_bench_padded_black_single_warped", () -> new BenchNether(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_black_single_warped"));
 	public static RegistryObject<Block> chair_wood_ironage_bench_padded_black_single_crimson = register("chair_wood_ironage_bench_padded_black_single_crimson", () -> new BenchNether(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_black_single_crimson"));
	
	public static RegistryObject<Block> chair_wood_ironage_bench_padded_blue_single_oak = register("chair_wood_ironage_bench_padded_blue_single_oak", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_blue_single_oak"));;;
	public static RegistryObject<Block> chair_wood_ironage_bench_padded_blue_single_acacia = register("chair_wood_ironage_bench_padded_blue_single_acacia", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_blue_single_acacia"));
	public static RegistryObject<Block> chair_wood_ironage_bench_padded_blue_single_dark_oak = register("chair_wood_ironage_bench_padded_blue_single_dark_oak", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_blue_single_dark_oak"));
	public static RegistryObject<Block> chair_wood_ironage_bench_padded_blue_single_birch = register("chair_wood_ironage_bench_padded_blue_single_birch", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_blue_single_birch"));
	public static RegistryObject<Block> chair_wood_ironage_bench_padded_blue_single_jungle = register("chair_wood_ironage_bench_padded_blue_single_jungle", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_blue_single_jungle"));
	public static RegistryObject<Block> chair_wood_ironage_bench_padded_blue_single_spruce = register("chair_wood_ironage_bench_padded_blue_single_spruce", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_blue_single_spruce"));
   	public static RegistryObject<Block> chair_wood_ironage_bench_padded_blue_single_warped = register("chair_wood_ironage_bench_padded_blue_single_warped", () -> new BenchNether(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_blue_single_warped"));
 	public static RegistryObject<Block> chair_wood_ironage_bench_padded_blue_single_crimson = register("chair_wood_ironage_bench_padded_blue_single_crimson", () -> new BenchNether(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_blue_single_crimson"));
	
	public static RegistryObject<Block> chair_wood_ironage_bench_padded_brown_single_oak = register("chair_wood_ironage_bench_padded_brown_single_oak", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_brown_single_oak"));;;
	public static RegistryObject<Block> chair_wood_ironage_bench_padded_brown_single_acacia = register("chair_wood_ironage_bench_padded_brown_single_acacia", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_brown_single_acacia"));
	public static RegistryObject<Block> chair_wood_ironage_bench_padded_brown_single_dark_oak = register("chair_wood_ironage_bench_padded_brown_single_dark_oak", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_brown_single_dark_oak"));
	public static RegistryObject<Block> chair_wood_ironage_bench_padded_brown_single_birch = register("chair_wood_ironage_bench_padded_brown_single_birch", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_brown_single_birch"));
	public static RegistryObject<Block> chair_wood_ironage_bench_padded_brown_single_jungle = register("chair_wood_ironage_bench_padded_brown_single_jungle", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_brown_single_jungle"));
	public static RegistryObject<Block> chair_wood_ironage_bench_padded_brown_single_spruce = register("chair_wood_ironage_bench_padded_brown_single_spruce", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_brown_single_spruce"));
   	public static RegistryObject<Block> chair_wood_ironage_bench_padded_brown_single_warped = register("chair_wood_ironage_bench_padded_brown_single_warped", () -> new BenchNether(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_brown_single_warped"));
 	public static RegistryObject<Block> chair_wood_ironage_bench_padded_brown_single_crimson = register("chair_wood_ironage_bench_padded_brown_single_crimson", () -> new BenchNether(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_brown_single_crimson"));
	
	public static RegistryObject<Block> chair_wood_ironage_bench_padded_cyan_single_oak = register("chair_wood_ironage_bench_padded_cyan_single_oak", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_cyan_single_oak"));;;
	public static RegistryObject<Block> chair_wood_ironage_bench_padded_cyan_single_acacia = register("chair_wood_ironage_bench_padded_cyan_single_acacia", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_cyan_single_acacia"));
	public static RegistryObject<Block> chair_wood_ironage_bench_padded_cyan_single_dark_oak = register("chair_wood_ironage_bench_padded_cyan_single_dark_oak", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_cyan_single_dark_oak"));
	public static RegistryObject<Block> chair_wood_ironage_bench_padded_cyan_single_birch = register("chair_wood_ironage_bench_padded_cyan_single_birch", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_cyan_single_birch"));
	public static RegistryObject<Block> chair_wood_ironage_bench_padded_cyan_single_jungle = register("chair_wood_ironage_bench_padded_cyan_single_jungle", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_cyan_single_jungle"));
	public static RegistryObject<Block> chair_wood_ironage_bench_padded_cyan_single_spruce = register("chair_wood_ironage_bench_padded_cyan_single_spruce", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_cyan_single_spruce"));
   	public static RegistryObject<Block> chair_wood_ironage_bench_padded_cyan_single_warped = register("chair_wood_ironage_bench_padded_cyan_single_warped", () -> new BenchNether(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_cyan_single_warped"));
 	public static RegistryObject<Block> chair_wood_ironage_bench_padded_cyan_single_crimson = register("chair_wood_ironage_bench_padded_cyan_single_crimson", () -> new BenchNether(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_cyan_single_crimson"));
	
	public static RegistryObject<Block> chair_wood_ironage_bench_padded_gray_single_oak = register("chair_wood_ironage_bench_padded_gray_single_oak", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_gray_single_oak"));;;
	public static RegistryObject<Block> chair_wood_ironage_bench_padded_gray_single_acacia = register("chair_wood_ironage_bench_padded_gray_single_acacia", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_gray_single_acacia"));
	public static RegistryObject<Block> chair_wood_ironage_bench_padded_gray_single_dark_oak = register("chair_wood_ironage_bench_padded_gray_single_dark_oak", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_gray_single_dark_oak"));
	public static RegistryObject<Block> chair_wood_ironage_bench_padded_gray_single_birch = register("chair_wood_ironage_bench_padded_gray_single_birch", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_gray_single_birch"));
	public static RegistryObject<Block> chair_wood_ironage_bench_padded_gray_single_jungle = register("chair_wood_ironage_bench_padded_gray_single_jungle", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_gray_single_jungle"));
	public static RegistryObject<Block> chair_wood_ironage_bench_padded_gray_single_spruce = register("chair_wood_ironage_bench_padded_gray_single_spruce", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_gray_single_spruce"));
   	public static RegistryObject<Block> chair_wood_ironage_bench_padded_gray_single_warped = register("chair_wood_ironage_bench_padded_gray_single_warped", () -> new BenchNether(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_gray_single_warped"));
 	public static RegistryObject<Block> chair_wood_ironage_bench_padded_gray_single_crimson = register("chair_wood_ironage_bench_padded_gray_single_crimson", () -> new BenchNether(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_gray_single_crimson"));
	
	public static RegistryObject<Block> chair_wood_ironage_bench_padded_orange_single_oak = register("chair_wood_ironage_bench_padded_orange_single_oak", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_orange_single_oak"));;;
	public static RegistryObject<Block> chair_wood_ironage_bench_padded_orange_single_acacia = register("chair_wood_ironage_bench_padded_orange_single_acacia", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_orange_single_acacia"));
	public static RegistryObject<Block> chair_wood_ironage_bench_padded_orange_single_dark_oak = register("chair_wood_ironage_bench_padded_orange_single_dark_oak", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_orange_single_dark_oak"));
	public static RegistryObject<Block> chair_wood_ironage_bench_padded_orange_single_birch = register("chair_wood_ironage_bench_padded_orange_single_birch", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_orange_single_birch"));
	public static RegistryObject<Block> chair_wood_ironage_bench_padded_orange_single_jungle = register("chair_wood_ironage_bench_padded_orange_single_jungle", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_orange_single_jungle"));
	public static RegistryObject<Block> chair_wood_ironage_bench_padded_orange_single_spruce = register("chair_wood_ironage_bench_padded_orange_single_spruce", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_orange_single_spruce"));
    public static RegistryObject<Block> chair_wood_ironage_bench_padded_orange_single_warped = register("chair_wood_ironage_bench_padded_orange_single_warped", () -> new BenchNether(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_orange_single_warped"));
    public static RegistryObject<Block> chair_wood_ironage_bench_padded_orange_single_crimson = register("chair_wood_ironage_bench_padded_orange_single_crimson", () -> new BenchNether(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_orange_single_crimson"));
	
	public static RegistryObject<Block> chair_wood_ironage_bench_padded_purple_single_oak = register("chair_wood_ironage_bench_padded_purple_single_oak", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_purple_single_oak"));;;
	public static RegistryObject<Block> chair_wood_ironage_bench_padded_purple_single_acacia = register("chair_wood_ironage_bench_padded_purple_single_acacia", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_purple_single_acacia"));
	public static RegistryObject<Block> chair_wood_ironage_bench_padded_purple_single_dark_oak = register("chair_wood_ironage_bench_padded_purple_single_dark_oak", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_purple_single_dark_oak"));
	public static RegistryObject<Block> chair_wood_ironage_bench_padded_purple_single_birch = register("chair_wood_ironage_bench_padded_purple_single_birch", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_purple_single_birch"));
	public static RegistryObject<Block> chair_wood_ironage_bench_padded_purple_single_jungle = register("chair_wood_ironage_bench_padded_purple_single_jungle", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_purple_single_jungle"));
	public static RegistryObject<Block> chair_wood_ironage_bench_padded_purple_single_spruce = register("chair_wood_ironage_bench_padded_purple_single_spruce", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_purple_single_spruce"));
	public static RegistryObject<Block> chair_wood_ironage_bench_padded_purple_single_warped = register("chair_wood_ironage_bench_padded_purple_single_warped", () -> new BenchNether(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_purple_single_warped"));
	public static RegistryObject<Block> chair_wood_ironage_bench_padded_purple_single_crimson = register("chair_wood_ironage_bench_padded_purple_single_crimson", () -> new BenchNether(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_purple_single_crimson"));
	
	public static RegistryObject<Block> chair_wood_ironage_bench_padded_white_single_oak = register("chair_wood_ironage_bench_padded_white_single_oak", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_white_single_oak"));;;
	public static RegistryObject<Block> chair_wood_ironage_bench_padded_white_single_acacia = register("chair_wood_ironage_bench_padded_white_single_acacia", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_white_single_acacia"));
	public static RegistryObject<Block> chair_wood_ironage_bench_padded_white_single_dark_oak = register("chair_wood_ironage_bench_padded_white_single_dark_oak", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_white_single_dark_oak"));
	public static RegistryObject<Block> chair_wood_ironage_bench_padded_white_single_birch = register("chair_wood_ironage_bench_padded_white_single_birch", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_white_single_birch"));
	public static RegistryObject<Block> chair_wood_ironage_bench_padded_white_single_jungle = register("chair_wood_ironage_bench_padded_white_single_jungle", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_white_single_jungle"));
	public static RegistryObject<Block> chair_wood_ironage_bench_padded_white_single_spruce = register("chair_wood_ironage_bench_padded_white_single_spruce", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_white_single_spruce"));
	public static RegistryObject<Block> chair_wood_ironage_bench_padded_white_single_warped = register("chair_wood_ironage_bench_padded_white_single_warped", () -> new BenchNether(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_white_single_warped"));
	public static RegistryObject<Block> chair_wood_ironage_bench_padded_white_single_crimson = register("chair_wood_ironage_bench_padded_white_single_crimson", () -> new BenchNether(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_padded_white_single_crimson"));
}
