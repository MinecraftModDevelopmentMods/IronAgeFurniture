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
import zone.moddev.mc.ironagefurniture.api.blocks.furniture.TallStool;
import zone.moddev.mc.ironagefurniture.api.blocks.furniture.TallStoolNether;

import java.util.function.Supplier;

public class ModVanillaTallStools {
	public static final DeferredRegister<Block> REGISTER = DeferredRegister.create(BuiltInRegistries.BLOCK, Ironagefurniture.MODID);
	
	private static DeferredHolder<Block, Block> register(String name, Supplier<Block> block) {
		return register(name, block, new Item.Properties()); 
	}
	
	private static DeferredHolder<Block, Block> register(String name, Supplier<Block> block, Item.Properties properties) {
		DeferredHolder<Block, Block> registryObject = REGISTER.register(name, block);
		
		ModItems.REGISTER.register(name, () -> new BlockItem(registryObject.get(), properties));
		
		return registryObject; 
	}
	
	public static DeferredHolder<Block, Block> chair_wood_ironage_stool_tall_oak = register("chair_wood_ironage_stool_tall_oak", () -> new TallStool(1, 10, SoundType.WOOD, "chair_wood_ironage_stool_tall_oak"));
	public static DeferredHolder<Block, Block> chair_wood_ironage_stool_tall_spruce = register("chair_wood_ironage_stool_tall_spruce", () -> new TallStool(1, 10, SoundType.WOOD, "chair_wood_ironage_stool_tall_spruce"));
	public static DeferredHolder<Block, Block> chair_wood_ironage_stool_tall_birch = register("chair_wood_ironage_stool_tall_birch", () -> new TallStool(1, 10, SoundType.WOOD, "chair_wood_ironage_stool_tall_birch"));
	public static DeferredHolder<Block, Block> chair_wood_ironage_stool_tall_jungle = register("chair_wood_ironage_stool_tall_jungle", () -> new TallStool(1, 10, SoundType.WOOD, "chair_wood_ironage_stool_tall_jungle"));
	public static DeferredHolder<Block, Block> chair_wood_ironage_stool_tall_acacia = register("chair_wood_ironage_stool_tall_acacia", () -> new TallStool(1, 10, SoundType.WOOD, "chair_wood_ironage_stool_tall_acacia"));
	public static DeferredHolder<Block, Block> chair_wood_ironage_stool_tall_dark_oak = register("chair_wood_ironage_stool_tall_dark_oak", () -> new TallStool(1, 10, SoundType.WOOD, "chair_wood_ironage_stool_tall_dark_oak"));
	public static DeferredHolder<Block, Block> chair_wood_ironage_stool_tall_mangrove = register("chair_wood_ironage_stool_tall_mangrove", () -> new TallStool(1, 10, SoundType.WOOD, "chair_wood_ironage_stool_tall_mangrove"));
	public static DeferredHolder<Block, Block> chair_wood_ironage_stool_tall_cherry = register("chair_wood_ironage_stool_tall_cherry", () -> new TallStool(1, 10, SoundType.WOOD, "chair_wood_ironage_stool_tall_cherry"));
	public static DeferredHolder<Block, Block> chair_wood_ironage_stool_tall_bamboo = register("chair_wood_ironage_stool_tall_bamboo", () -> new TallStool(1, 10, SoundType.WOOD, "chair_wood_ironage_stool_tall_bamboo"));
	public static DeferredHolder<Block, Block> chair_wood_ironage_stool_tall_crimson = register("chair_wood_ironage_stool_tall_crimson", () -> new TallStoolNether(1, 10, SoundType.WOOD, "chair_wood_ironage_stool_tall_crimson"));
	public static DeferredHolder<Block, Block> chair_wood_ironage_stool_tall_warped = register("chair_wood_ironage_stool_tall_warped", () -> new TallStoolNether(1, 10, SoundType.WOOD, "chair_wood_ironage_stool_tall_warped"));
}
