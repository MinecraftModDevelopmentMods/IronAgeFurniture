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
import zone.moddev.mc.ironagefurniture.api.blocks.furniture.Chair;
import zone.moddev.mc.ironagefurniture.api.blocks.furniture.ChairNether;

import java.util.function.Supplier;

public class ModVanillaChairs {
	public static final DeferredRegister<Block> REGISTER = DeferredRegister.create(BuiltInRegistries.BLOCK, Ironagefurniture.MODID);
	
	private static DeferredHolder<Block, Block> register(String name, Supplier<Block> block) {
		return register(name, block, new Item.Properties()); 
	}
	
	private static DeferredHolder<Block, Block> register(String name, Supplier<Block> block, Item.Properties properties) {
		DeferredHolder<Block, Block> registryObject = REGISTER.register(name, block);
		
		ModItems.REGISTER.register(name, () -> new BlockItem(registryObject.get(), properties));
		
		return registryObject; 
	}
	
	public static DeferredHolder<Block, Block> chair_wood_ironage_classic_oak = register("chair_wood_ironage_classic_oak", () -> new Chair(1, 10, SoundType.WOOD, "chair_wood_ironage_classic_oak"));
	public static DeferredHolder<Block, Block> chair_wood_ironage_classic_spruce = register("chair_wood_ironage_classic_spruce", () -> new Chair(1, 10, SoundType.WOOD, "chair_wood_ironage_classic_spruce"));
	public static DeferredHolder<Block, Block> chair_wood_ironage_classic_birch = register("chair_wood_ironage_classic_birch", () -> new Chair(1, 10, SoundType.WOOD, "chair_wood_ironage_classic_birch"));
	public static DeferredHolder<Block, Block> chair_wood_ironage_classic_jungle = register("chair_wood_ironage_classic_jungle", () -> new Chair(1, 10, SoundType.WOOD, "chair_wood_ironage_classic_jungle"));
	public static DeferredHolder<Block, Block> chair_wood_ironage_classic_acacia = register("chair_wood_ironage_classic_acacia", () -> new Chair(1, 10, SoundType.WOOD, "chair_wood_ironage_classic_acacia"));
	public static DeferredHolder<Block, Block> chair_wood_ironage_classic_dark_oak = register("chair_wood_ironage_classic_dark_oak", () -> new Chair(1, 10, SoundType.WOOD, "chair_wood_ironage_classic_dark_oak"));
	public static DeferredHolder<Block, Block> chair_wood_ironage_classic_mangrove = register("chair_wood_ironage_classic_mangrove", () -> new Chair(1, 10, SoundType.WOOD, "chair_wood_ironage_classic_mangrove"));
	public static DeferredHolder<Block, Block> chair_wood_ironage_classic_cherry = register("chair_wood_ironage_classic_cherry", () -> new Chair(1, 10, SoundType.WOOD, "chair_wood_ironage_classic_cherry"));
	public static DeferredHolder<Block, Block> chair_wood_ironage_classic_bamboo = register("chair_wood_ironage_classic_bamboo", () -> new Chair(1, 10, SoundType.WOOD, "chair_wood_ironage_classic_bamboo"));
	public static DeferredHolder<Block, Block> chair_wood_ironage_classic_crimson = register("chair_wood_ironage_classic_crimson", () -> new ChairNether(1, 10, SoundType.WOOD, "chair_wood_ironage_classic_crimson"));
	public static DeferredHolder<Block, Block> chair_wood_ironage_classic_warped = register("chair_wood_ironage_classic_warped", () -> new ChairNether(1, 10, SoundType.WOOD, "chair_wood_ironage_classic_warped"));
}
