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
import zone.moddev.mc.ironagefurniture.api.blocks.furniture.Stool;
import zone.moddev.mc.ironagefurniture.api.blocks.furniture.StoolNether;

import java.util.function.Supplier;

public class ModVanillaStools {
	public static final DeferredRegister<Block> REGISTER = DeferredRegister.create(BuiltInRegistries.BLOCK, Ironagefurniture.MODID);
	
	private static DeferredHolder<Block, Block> register(String name, Supplier<Block> block) {
		return register(name, block, new Item.Properties()); 
	}
	
	private static DeferredHolder<Block, Block> register(String name, Supplier<Block> block, Item.Properties properties) {
		DeferredHolder<Block, Block> registryObject = REGISTER.register(name, block);
		
		ModItems.REGISTER.register(name, () -> new BlockItem(registryObject.get(), RegistrationProperties.item(properties, name).useBlockDescriptionPrefix()));
		
		return registryObject; 
	}
	
	public static DeferredHolder<Block, Block> chair_wood_ironage_stool_short_oak = register("chair_wood_ironage_stool_short_oak", () -> new Stool(1, 10, SoundType.WOOD, "chair_wood_ironage_stool_short_oak"));
	public static DeferredHolder<Block, Block> chair_wood_ironage_stool_short_spruce = register("chair_wood_ironage_stool_short_spruce", () -> new Stool(1, 10, SoundType.WOOD, "chair_wood_ironage_stool_short_spruce"));
	public static DeferredHolder<Block, Block> chair_wood_ironage_stool_short_birch = register("chair_wood_ironage_stool_short_birch", () -> new Stool(1, 10, SoundType.WOOD, "chair_wood_ironage_stool_short_birch"));
	public static DeferredHolder<Block, Block> chair_wood_ironage_stool_short_jungle = register("chair_wood_ironage_stool_short_jungle", () -> new Stool(1, 10, SoundType.WOOD, "chair_wood_ironage_stool_short_jungle"));
	public static DeferredHolder<Block, Block> chair_wood_ironage_stool_short_acacia = register("chair_wood_ironage_stool_short_acacia", () -> new Stool(1, 10, SoundType.WOOD, "chair_wood_ironage_stool_short_acacia"));
	public static DeferredHolder<Block, Block> chair_wood_ironage_stool_short_dark_oak = register("chair_wood_ironage_stool_short_dark_oak", () -> new Stool(1, 10, SoundType.WOOD, "chair_wood_ironage_stool_short_dark_oak"));
	public static DeferredHolder<Block, Block> chair_wood_ironage_stool_short_mangrove = register("chair_wood_ironage_stool_short_mangrove", () -> new Stool(1, 10, SoundType.WOOD, "chair_wood_ironage_stool_short_mangrove"));
	public static DeferredHolder<Block, Block> chair_wood_ironage_stool_short_cherry = register("chair_wood_ironage_stool_short_cherry", () -> new Stool(1, 10, SoundType.WOOD, "chair_wood_ironage_stool_short_cherry"));
	public static DeferredHolder<Block, Block> chair_wood_ironage_stool_short_bamboo = register("chair_wood_ironage_stool_short_bamboo", () -> new Stool(1, 10, SoundType.WOOD, "chair_wood_ironage_stool_short_bamboo"));
	public static DeferredHolder<Block, Block> chair_wood_ironage_stool_short_pale_oak = register("chair_wood_ironage_stool_short_pale_oak", () -> new Stool(1, 10, SoundType.WOOD, "chair_wood_ironage_stool_short_pale_oak"));
	public static DeferredHolder<Block, Block> chair_wood_ironage_stool_short_crimson = register("chair_wood_ironage_stool_short_crimson", () -> new StoolNether(1, 10, SoundType.WOOD, "chair_wood_ironage_stool_short_crimson"));
	public static DeferredHolder<Block, Block> chair_wood_ironage_stool_short_warped = register("chair_wood_ironage_stool_short_warped", () -> new StoolNether(1, 10, SoundType.WOOD, "chair_wood_ironage_stool_short_warped"));
}
