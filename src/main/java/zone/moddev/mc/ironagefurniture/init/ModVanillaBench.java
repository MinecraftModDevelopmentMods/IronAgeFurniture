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
import zone.moddev.mc.ironagefurniture.api.blocks.furniture.Bench;
import zone.moddev.mc.ironagefurniture.api.blocks.furniture.BenchNether;

import java.util.function.Supplier;

public class ModVanillaBench {
	public static final DeferredRegister<Block> REGISTER = DeferredRegister.create(BuiltInRegistries.BLOCK, Ironagefurniture.MODID);
	
	private static DeferredHolder<Block, Block> register(String name, Supplier<Block> block) {
		return register(name, block, new Item.Properties()); 
	}
	
	private static DeferredHolder<Block, Block> register(String name, Supplier<Block> block, Item.Properties properties) {
		DeferredHolder<Block, Block> registryObject = REGISTER.register(name, block);
		
		ModItems.REGISTER.register(name, () -> new BlockItem(registryObject.get(), properties));
		
		return registryObject; 
	}
	
	public static DeferredHolder<Block, Block> chair_wood_ironage_bench_single_oak = register("chair_wood_ironage_bench_single_oak", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_single_oak"));;
	public static DeferredHolder<Block, Block> chair_wood_ironage_bench_single_spruce = register("chair_wood_ironage_bench_single_spruce", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_single_spruce"));;
	public static DeferredHolder<Block, Block> chair_wood_ironage_bench_single_birch = register("chair_wood_ironage_bench_single_birch", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_single_birch"));;
	public static DeferredHolder<Block, Block> chair_wood_ironage_bench_single_jungle = register("chair_wood_ironage_bench_single_jungle", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_single_jungle"));;
	public static DeferredHolder<Block, Block> chair_wood_ironage_bench_single_acacia = register("chair_wood_ironage_bench_single_acacia", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_single_acacia"));;
	public static DeferredHolder<Block, Block> chair_wood_ironage_bench_single_dark_oak = register("chair_wood_ironage_bench_single_dark_oak", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_single_dark_oak"));;
	public static DeferredHolder<Block, Block> chair_wood_ironage_bench_single_mangrove = register("chair_wood_ironage_bench_single_mangrove", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_single_mangrove"));;
	public static DeferredHolder<Block, Block> chair_wood_ironage_bench_single_cherry = register("chair_wood_ironage_bench_single_cherry", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_single_cherry"));;
	public static DeferredHolder<Block, Block> chair_wood_ironage_bench_single_bamboo = register("chair_wood_ironage_bench_single_bamboo", () -> new Bench(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_single_bamboo"));;
	public static DeferredHolder<Block, Block> chair_wood_ironage_bench_single_crimson = register("chair_wood_ironage_bench_single_crimson", () -> new BenchNether(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_single_crimson"));;
	public static DeferredHolder<Block, Block> chair_wood_ironage_bench_single_warped = register("chair_wood_ironage_bench_single_warped", () -> new BenchNether(1, 10, SoundType.WOOD, "chair_wood_ironage_bench_single_warped"));;
}
