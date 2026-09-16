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

public class ModVanillaShieldChairs {
	public static final DeferredRegister<Block> REGISTER = DeferredRegister.create(BuiltInRegistries.BLOCK, Ironagefurniture.MODID);
	
	private static DeferredHolder<Block, Block> register(String name, Supplier<Block> block) {
		return register(name, block, new Item.Properties()); 
	}
	
	private static DeferredHolder<Block, Block> register(String name, Supplier<Block> block, Item.Properties properties) {
		DeferredHolder<Block, Block> registryObject = REGISTER.register(name, block);
		
		ModItems.REGISTER.register(name, () -> new BlockItem(registryObject.get(), RegistrationProperties.item(properties, name).useBlockDescriptionPrefix()));
		
		return registryObject; 
	}
	
	public static DeferredHolder<Block, Block> chair_wood_ironage_shield_oak = register("chair_wood_ironage_shield_oak", () -> new Chair(1, 10, SoundType.WOOD, "chair_wood_ironage_shield_oak"));
	public static DeferredHolder<Block, Block> chair_wood_ironage_shield_spruce = register("chair_wood_ironage_shield_spruce", () -> new Chair(1, 10, SoundType.WOOD, "chair_wood_ironage_shield_spruce"));
	public static DeferredHolder<Block, Block> chair_wood_ironage_shield_birch = register("chair_wood_ironage_shield_birch", () -> new Chair(1, 10, SoundType.WOOD, "chair_wood_ironage_shield_birch"));
	public static DeferredHolder<Block, Block> chair_wood_ironage_shield_jungle = register("chair_wood_ironage_shield_jungle", () -> new Chair(1, 10, SoundType.WOOD, "chair_wood_ironage_shield_jungle"));
	public static DeferredHolder<Block, Block> chair_wood_ironage_shield_acacia = register("chair_wood_ironage_shield_acacia", () -> new Chair(1, 10, SoundType.WOOD, "chair_wood_ironage_shield_acacia"));
	public static DeferredHolder<Block, Block> chair_wood_ironage_shield_dark_oak = register("chair_wood_ironage_shield_dark_oak", () -> new Chair(1, 10, SoundType.WOOD, "chair_wood_ironage_shield_dark_oak"));
	public static DeferredHolder<Block, Block> chair_wood_ironage_shield_mangrove = register("chair_wood_ironage_shield_mangrove", () -> new Chair(1, 10, SoundType.WOOD, "chair_wood_ironage_shield_mangrove"));
	public static DeferredHolder<Block, Block> chair_wood_ironage_shield_cherry = register("chair_wood_ironage_shield_cherry", () -> new Chair(1, 10, SoundType.WOOD, "chair_wood_ironage_shield_cherry"));
	public static DeferredHolder<Block, Block> chair_wood_ironage_shield_bamboo = register("chair_wood_ironage_shield_bamboo", () -> new Chair(1, 10, SoundType.WOOD, "chair_wood_ironage_shield_bamboo"));
	public static DeferredHolder<Block, Block> chair_wood_ironage_shield_pale_oak = register("chair_wood_ironage_shield_pale_oak", () -> new Chair(1, 10, SoundType.WOOD, "chair_wood_ironage_shield_pale_oak"));
	public static DeferredHolder<Block, Block> chair_wood_ironage_shield_crimson = register("chair_wood_ironage_shield_crimson", () -> new ChairNether(1, 10, SoundType.WOOD, "chair_wood_ironage_shield_crimson"));
	public static DeferredHolder<Block, Block> chair_wood_ironage_shield_warped = register("chair_wood_ironage_shield_warped", () -> new ChairNether(1, 10, SoundType.WOOD, "chair_wood_ironage_shield_warped"));
}
